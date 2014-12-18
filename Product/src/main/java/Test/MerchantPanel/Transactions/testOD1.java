package Test.MerchantPanel.Transactions;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsMerchantParamsPage;
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

public class testOD1 extends TestBase{

	@Test(description = "Validating full refund status for (>24 hr) Transaction without OD limit and total amount refundable less than requested", 
			dataProvider="GetRequestRefundTestConfig", timeOut=600000, groups="1")
	public void test_Verify_x_FullRefundQueuedStatus(Config testConfig, String[] testType)
	{

		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();

		int transactionRowNum = 45;
		int requestDetailRowNum=1;
		Map<String,String> map;
		String payuId = null;


		//Get all captured transactions <24 hr and fire refund requests on them to make captured amt as 0
		makeRecentCapturedTransactionAmountZero(testConfig,tranHelper,transactionRowNum);
		
		for(int i=0;i<testType.length;i++)
		{		
			// request refund for a transaction > 24 hrs
			testConfig.putRunTimeProperty("daybeforeyest",Helper.getDateBeforeOrAfterDays(-2,"yyyy-MM-dd"));
			testConfig.putRunTimeProperty("yesterday",Helper.getDateBeforeOrAfterDays(-1,"yyyy-MM-dd"));
			testConfig.putRunTimeProperty("curdate", Helper.getDateBeforeOrAfterDays(0,"yyyy-MM-dd"));

			// get a transaction greater than 24 hrs
			map = DataBase.executeSelectQuery(testConfig, 17, 1);	
			if(map!=null)
			{
				payuId = map.get("mihpayid");
				String amount = map.get("amount");
				if(testType[i].contentEquals("partial"))
				{
					double amt = Double.parseDouble(amount);
					double diff = 0.2;
					amt = amt - diff;
					amount = String.format("%.2f", amt);

				}
				testConfig.putRunTimeProperty("mihpayid", payuId);
				DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
				DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
				RefundPage refundTransaction = dashBoard.ClickRequestRefund();

				//Request for full refund 
				refundTransaction.SearchTransaction(payuId, SearchOn.PayUId);

				refundTransaction.fullRefundFirstTransaction();

				//View refunded transaction on request page			
				dashBoard.ClickViewRequest();
				TransactionFilterPage filter= new TransactionFilterPage(testConfig);
				filter.SearchTransaction(payuId, SearchOn.PayUId);

				refundTransaction.VerifyRefundTransaction(transactionRowNum, requestDetailRowNum, amount, "true");
			}

			else testConfig.logFail("No transactions older than 24 hrs present for this merchant");
		}

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Validate full refund when Captured +auth amount is < refunded but when OD is added , total amount (OD+Cap+Auth)>refunded amount",dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_VerifyFullRefundWithODLimitGreaterThanRefundAmount(Config testConfig)
	{
		TransactionHelper tranHelper = new TransactionHelper(testConfig,true);
		tranHelper.DoLogin();
		int transactionRowNum = 46;
		int requestDetailRowNum = 5;
		int paymentTypeRowNum = 5;
		String payuId = null;
		String transAmount = "10";
		//Get all captured transactions <24 hr and fire refund requests on them to make captured amt as 0
		
		makeTransactionsForSetUpData(testConfig,1,tranHelper,paymentTypeRowNum,transactionRowNum,3,transAmount);
		makeRecentCapturedTransactionAmountZero(testConfig,tranHelper,transactionRowNum);

		Map<String,String> map = getTransactionGreaterThan24Hr(testConfig,transactionRowNum);

		if(map!=null)
		{
			payuId = map.get("mihpayid");
			String amount = map.get("amount");
			testConfig.putRunTimeProperty("mihpayid", payuId);
			DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
			DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);

			RefundPage refundTransaction = dashBoard.ClickRequestRefund();

			//Request for full refund 
			refundTransaction.SearchTransaction(payuId, SearchOn.PayUId);

			refundTransaction.fullRefundFirstTransaction();

			//View refunded transaction on request page			
			dashBoard.ClickViewRequest();
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
			refundTransaction.VerifyRefundTransaction(transactionRowNum,requestDetailRowNum,amount,"true");


			//refundTransaction.VerifyRefundTransaction(transactionRowNum, requestDetailRowNum, amount, "true");
		}

		else testConfig.logFail("No transactions older than 24 hrs present for this merchant");


		//setup data for tomorrow's test run
		makeTransactionsForSetUpData(testConfig,1,tranHelper,paymentTypeRowNum,transactionRowNum,1,transAmount);
		Assert.assertTrue(testConfig.getTestResult());

	}

//	@Test(description = "Validate full refund when Captured +auth amount is < refunded but when OD is added , total amount (OD+Cap+Auth)=refunded amount",dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_VerifyODAmountEqualToRefundAmount(Config testConfig)
	{
		TransactionHelper tranHelper = new TransactionHelper(testConfig,true);
		int transactionRowNum = 46;
		int requestDetailRowNum = 5;
		int paymentTypeRowNum = 5;
		String payuId = null;
		String transAmount = "50";
		makeTransactionsForSetUpData(testConfig,1,tranHelper,paymentTypeRowNum,transactionRowNum,3,transAmount);
		//Get all captured transactions <24 hr and fire refund requests on them to make captured amt as 0
		makeRecentCapturedTransactionAmountZero(testConfig,tranHelper,transactionRowNum);

		Map<String,String> map = getTransactionGreaterThan24Hr(testConfig,transactionRowNum);

		if(map!=null)
		{
			payuId = map.get("mihpayid");
			String amount = map.get("amount");
			testConfig.putRunTimeProperty("mihpayid", payuId);
			DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
			DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);

			RefundPage refundTransaction = dashBoard.ClickRequestRefund();

			//Request for full refund 
			refundTransaction.SearchTransaction(payuId, SearchOn.PayUId);

			refundTransaction.fullRefundFirstTransaction();

			//View refunded transaction on request page			
			dashBoard.ClickViewRequest();
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
			refundTransaction.VerifyRefundTransaction(transactionRowNum,requestDetailRowNum,amount,"true");

			//Validate full refund when Captured +auth amount is < refunded and when OD is added , total amount (OD+Cap+Auth)<refunded amount. Refund should be queued

			map = getTransactionGreaterThan24Hr(testConfig,transactionRowNum);

			if(map!=null)
			{
				payuId = map.get("mihpayid");
				amount = map.get("amount");
				testConfig.putRunTimeProperty("mihpayid", payuId);
				dashboardHelper = new DashboardHelper(testConfig);
				dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);

				refundTransaction = dashBoard.ClickRequestRefund();

				//Request for full refund 
				refundTransaction.SearchTransaction(payuId, SearchOn.PayUId);

				refundTransaction.fullRefundFirstTransaction();

				//View refunded transaction on request page			
				dashBoard.ClickViewRequest();
				filter= new TransactionFilterPage(testConfig);
				filter.SearchTransaction(payuId, SearchOn.PayUId);
				/*for(int i=0;i<10;i++)
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
				}*/

				requestDetailRowNum = 1;
				refundTransaction.VerifyRefundTransaction(transactionRowNum,requestDetailRowNum,amount,"true");



				//	refundTransaction.VerifyRefundTransaction(transactionRowNum, requestDetailRowNum, amount, "true");
			}

			else testConfig.logFail("No transactions older than 24 hrs present for this merchant");


			//setup data for tomorrow's test run
			makeTransactionsForSetUpData(testConfig,2,tranHelper,paymentTypeRowNum,transactionRowNum,3,transAmount);
			//set OD limit to 50
			tranHelper.transactionData = new TestDataReader(tranHelper.testConfig,"TransactionDetails");
			String merchantName = tranHelper.transactionData.GetData(transactionRowNum, "Comments");
			MerchantListPage merchantListPage = tranHelper.home.clickMerchantList();
			merchantListPage.SearchMerchant(merchantName);
			MerchantDetailsPage merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
			merchantDetailsPage.ClickParams();
			ParamsMerchantParamsPage mParams = new ParamsMerchantParamsPage(testConfig);
			mParams.updateKeyValue("overdraft_limit", "50");
			Assert.assertTrue(testConfig.getTestResult());
		}
	}


	public void makeTransactionsForSetUpData(Config testConfig,int count, TransactionHelper tranHelper,int paymentTypeRowNum,int transactionRowNum,int cardDetailsRowNum,String amount)
	{
		testConfig.putRunTimeProperty("amount", amount);
		//setup data for tomorrow's test run
		for(int i = 0;i<count;i++)
		{
			// tranHelper.DoLogin();

			tranHelper.GetTestTransactionPage();

			//testConfig.getRunTimeProperty("transactionId");
			tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		}

	}
	
	public Map<String,String> getTransactionGreaterThan24Hr(Config testConfig, int transactionRowNum)
	{
		// request refund for a transaction > 24 hrs
		testConfig.putRunTimeProperty("daybeforeyest",Helper.getDateBeforeOrAfterDays(-2,"yyyy-MM-dd"));
		testConfig.putRunTimeProperty("yesterday",Helper.getDateBeforeOrAfterDays(-1,"yyyy-MM-dd"));
		testConfig.putRunTimeProperty("curdate", Helper.getDateBeforeOrAfterDays(0,"yyyy-MM-dd"));
		TestDataReader loginData = new TestDataReader(testConfig, "TransactionDetails"); 

		//If environment specific key is present then use that
		testConfig.putRunTimeProperty("merchantId",loginData.GetCurrentEnvironmentData(transactionRowNum, "merchantid"));
		// get a transaction greater than 24 hrs
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 17, 1);	
		return map;
	}

	//Get all captured transactions <24 hr and fire refund requests on them to make captured amt as 0
	public void makeRecentCapturedTransactionAmountZero(Config testConfig,TransactionHelper tranHelper,int transactionRowNum)
	{
		Map<String,String> map;
	//	tranHelper.DoLogin();

		do {
			testConfig.putRunTimeProperty("yesterday", Helper.getDateBeforeOrAfterDays(-1,"yyyy-MM-dd"));
			TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");

			//If environment specific key is present then use that
			testConfig.putRunTimeProperty("merchantKey",data.GetCurrentEnvironmentData(transactionRowNum, "key"));
			//set merchant id
			testConfig.putRunTimeProperty("merchantId",data.GetCurrentEnvironmentData(transactionRowNum, "merchantid"));

			map = DataBase.executeSelectQuery(testConfig, 18, 1);	
			if(map!=null)
			{
				String payuId = map.get("mihpayid");
				String amount = map.get("amount");
				testConfig.putRunTimeProperty("mihpayid", payuId);
				WebServicesHelper wshelper = new WebServicesHelper(testConfig);	
				//Call full refund_transaction using mihpayid, token and amount
				wshelper.refund_transaction_executeAdminPanel(tranHelper.home, testConfig.getRunTimeProperty("merchantKey"),payuId, amount);

			}
			else {
				testConfig.logComment("No more <24 hr transactions left to be refunded ");
				break;
			}
		} while((!map.get("mihpayid").isEmpty()));
	}
	
	@Test(description = "Daily data creation for OD merchants", 
			dataProvider="GetRequestRefundTestConfig", timeOut=600000, groups="1")
	public void test_z_dailyDataCreation(Config testConfig, String[] testType)
	{				
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 46;
		int paymentTypeRowNum = 5;
		String transAmount = "10";
		makeTransactionsForSetUpData(testConfig,3,helper,paymentTypeRowNum,transactionRowNum,3,transAmount);
		transactionRowNum = 34;
		makeTransactionsForSetUpData(testConfig,3,helper,paymentTypeRowNum,transactionRowNum,3,transAmount);
		transactionRowNum = 35;
		makeTransactionsForSetUpData(testConfig,3,helper,paymentTypeRowNum,transactionRowNum,3,transAmount);
		transactionRowNum = 45;
		makeTransactionsForSetUpData(testConfig,3,helper,paymentTypeRowNum,transactionRowNum,3,transAmount);
	}
}