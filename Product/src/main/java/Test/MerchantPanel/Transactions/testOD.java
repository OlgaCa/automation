package Test.MerchantPanel.Transactions;

import java.awt.AWTException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.WebServices.WebServicesHelper;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Transactions.RefundPage;
import PageObject.MerchantPanel.Transactions.RequestsPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage.SearchOn;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class testOD extends TestBase {





	@Test(description = "Validating refund failure for case where refund amount > transaction amount", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyRefundFailure(Config testConfig) 
	{
		int transactionRowNum = 34;

		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();

		//setup data for tomorrow's test run
		int paymentTypeRowNum =5;
		int cardDetailsRowNum = 1;

		tranHelper.GetTestTransactionPage();

		//testConfig.getRunTimeProperty("transactionId");
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		testConfig.putRunTimeProperty("daybeforeyest",Helper.getDateBeforeOrAfterDays(-2,"yyyy-MM-dd"));
		testConfig.putRunTimeProperty("yesterday",Helper.getDateBeforeOrAfterDays(-1,"yyyy-MM-dd"));
		testConfig.putRunTimeProperty("curdate",Helper.getDateBeforeOrAfterDays(0,"yyyy-MM-dd"));
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");

		//If environment specific key is present then use that
		testConfig.putRunTimeProperty("merchantId",data.GetCurrentEnvironmentData(transactionRowNum, "merchantid"));

		//testConfig.putRunTimeProperty("merchantId", "6033");
		// get a transaction less than 24 hrs
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 17, 1);	
		if(map!=null)
		{
			String payuId = map.get("mihpayid");
			String amount = map.get("amount");

			double amt = Double.parseDouble(amount);
			double diff = 0.2;
			amt = amt + diff;
			amount = String.format("%.2f", amt);

			testConfig.putRunTimeProperty("mihpayid", payuId);
			testConfig.putRunTimeProperty("failureAmount", amount);
			DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
			DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
			RefundPage refundTransaction = dashBoard.ClickRequestRefund();

			//Request for full refund 
			refundTransaction.SearchTransaction(testConfig.getRunTimeProperty("mihpayid"), SearchOn.PayUId);

			String error = refundTransaction.RefundTransactionToGetFailure(amount);

			String expected = "Amount Entered exceeds the refundable amount.\nAmount Entered - " + amount + "\nAmount Refundable - " + String.format("%.0f", Double.parseDouble(map.get("amount")));

			Helper.compareEquals(testConfig, "Excess Refund error", expected, error);
		}
		else testConfig.logFail("No transactions less than 24 hrs present for this merchant");

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify full refund request for a transaction >24 hr when (Captured+Auth) amount>refund requested amount when OD is not set ", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyFullRefundTodayTransactionGreaterThanTodayRefund(Config testConfig)
	{

		//setup data for tomorrow's test run
		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();
		int transactionRowNum = 34;
		int paymentTypeRowNum =5;
		int cardDetailsRowNum = 1;
		int requestDetailRowNum=1;

		tranHelper.GetTestTransactionPage();

		//testConfig.getRunTimeProperty("transactionId");
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);


		testConfig.putRunTimeProperty("daybeforeyest",Helper.getDateBeforeOrAfterDays(-2,"yyyy-MM-dd"));
		testConfig.putRunTimeProperty("yesterday",Helper.getDateBeforeOrAfterDays(-1,"yyyy-MM-dd"));
		TestDataReader loginData = new TestDataReader(testConfig, "TransactionDetails"); 

		testConfig.putRunTimeProperty("merchantId",loginData.GetCurrentEnvironmentData(transactionRowNum, "merchantid"));

		// get a transaction greater than 24 hrs that has not been refunded
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 18, 1);	
		String payuId = map.get("mihpayid");
		String amount = map.get("amount");

		testConfig.putRunTimeProperty("mihpayid", payuId);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request for full refund 
		refundTransaction.SearchTransaction(payuId, SearchOn.PayUId);

		refundTransaction.fullRefundFirstTransaction();

		//View refunded transaction on request page			
		RequestsPage request= dashBoard.ClickViewRequest();
		TransactionFilterPage filter= new TransactionFilterPage(testConfig);
		filter.SearchTransaction(payuId, SearchOn.PayUId);


		for(int i=0;i<10;i++)
		{
			// wait for 1 minute and verify success status on refund request
			Browser.wait(testConfig, 60);
			Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 4, -1);
			if(map1.get("action").contentEquals("refund"))
			{
				if(map1.get("status").contentEquals("success"))
					break;
				else testConfig.logComment("refund status is " + map1.get("status"));
			}
			else
			{
				testConfig.logFail("Refund entry not found in txn_update");
				break;
			}
		}

		requestDetailRowNum = 5;

		//Verify Refunded transaction details from Database
		refundTransaction.VerifyRefundTransaction(transactionRowNum,requestDetailRowNum,amount,"true");

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Validate full refund when Captured +auth amount is > refunded when OD is set to some amount", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyFullRefundTransactionWithODLimit(Config testConfig) 
	{
		//setup data for tomorrow's test run
		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();
		int transactionRowNum = 35;
		int paymentTypeRowNum =5;
		int cardDetailsRowNum = 1;
		int requestDetailRowNum=1;

		tranHelper.GetTestTransactionPage();

		//testConfig.getRunTimeProperty("transactionId");
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);


		testConfig.putRunTimeProperty("daybeforeyest",Helper.getDateBeforeOrAfterDays(-2,"yyyy-MM-dd"));
		testConfig.putRunTimeProperty("yesterday",Helper.getDateBeforeOrAfterDays(-1,"yyyy-MM-dd"));
		TestDataReader loginData = new TestDataReader(testConfig, "TransactionDetails"); 

		testConfig.putRunTimeProperty("merchantId",loginData.GetCurrentEnvironmentData(transactionRowNum, "merchantid"));

		// get a transaction greater than 24 hrs that has not been refunded
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 18, 1);	
		String payuId = map.get("mihpayid");
		String amount = map.get("amount");

		testConfig.putRunTimeProperty("mihpayid", payuId);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request for full refund 
		refundTransaction.SearchTransaction(payuId, SearchOn.PayUId);

		refundTransaction.fullRefundFirstTransaction();

		//View refunded transaction on request page			
		RequestsPage request= dashBoard.ClickViewRequest();
		TransactionFilterPage filter= new TransactionFilterPage(testConfig);
		filter.SearchTransaction(payuId, SearchOn.PayUId);


		//Verify Refunded transaction details from Database
		refundTransaction.VerifyRefundTransaction(transactionRowNum,requestDetailRowNum,amount,"true");
		for(int i=0;i<10;i++)
		{
			// wait for 1 minute and verify success status on refund request
			Browser.wait(testConfig, 60);
			Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 4, -1);
			if(map1.get("action").contentEquals("refund"))
			{
				if(map1.get("status").contentEquals("success"))
					break;
				else testConfig.logComment("refund status is " + map1.get("status"));
			}
			else
			{
				testConfig.logFail("Refund entry not found in txn_update");
				break;
			}
		}
		
		requestDetailRowNum = 5;

		refundTransaction.VerifyRefundTransaction(transactionRowNum,requestDetailRowNum,amount,"true");

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description =" Validating partial refund request status for recent (>24 hr) Transaction without OD limit and total amount refundable greater than captured amount", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	
	public void test_VerifyPartialRefundTransactionGreaterThan24hr(Config testConfig)
	{

		//setup data for tomorrow's test run
		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();
		int transactionRowNum = 34;
		int paymentTypeRowNum =5;
		int cardDetailsRowNum = 1;
		int requestDetailRowNum=1;

		tranHelper.GetTestTransactionPage();

		//testConfig.getRunTimeProperty("transactionId");
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);


		testConfig.putRunTimeProperty("daybeforeyest",Helper.getDateBeforeOrAfterDays(-2,"yyyy-MM-dd"));
		testConfig.putRunTimeProperty("yesterday",Helper.getDateBeforeOrAfterDays(-1,"yyyy-MM-dd"));
		TestDataReader loginData = new TestDataReader(testConfig, "TransactionDetails"); 

		//If environment specific key is present then use that
		testConfig.putRunTimeProperty("merchantId",loginData.GetCurrentEnvironmentData(transactionRowNum, "merchantid"));

		// get a transaction greater than 24 hrs
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 18, 1);	
		String payuId = map.get("mihpayid");
		String amount = map.get("amount");

		double amt = Double.parseDouble(amount);
		double diff = 0.2;
		amt = amt - diff;
		amount = String.format("%.2f", amt);

		testConfig.putRunTimeProperty("mihpayid", payuId);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request for full refund 
		refundTransaction.SearchTransaction(payuId, SearchOn.PayUId);

		refundTransaction.RefundFirstTransaction(amount);

		//View refunded transaction on request page			
		RequestsPage request= dashBoard.ClickViewRequest();
		TransactionFilterPage filter= new TransactionFilterPage(testConfig);
		filter.SearchTransaction(payuId, SearchOn.PayUId);

		//Verify Refunded transaction details from Database
		refundTransaction.VerifyRefundTransaction(transactionRowNum,requestDetailRowNum,amount,"true");
		for(int i=0;i<10;i++)
		{
			// wait for 1 minute and verify success status on refund request
			Browser.wait(testConfig, 60);
			Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 4, -1);
			if(map1.get("action").contentEquals("refund"))
			{
				if(map1.get("status").contentEquals("success"))
					break;
				else testConfig.logComment("refund status is " + map1.get("status"));
			}
			else
			{
				testConfig.logFail("Refund entry not found in txn_update");
				break;
			}
		}

		requestDetailRowNum = 5;
		refundTransaction.VerifyRefundTransaction(transactionRowNum,requestDetailRowNum,amount,"true");

		Assert.assertTrue(testConfig.getTestResult());

	}
}
