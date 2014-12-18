package Test.MerchantPanel.Transactions;

import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Helper;
import Utils.TestBase;

public class TestCodCancel extends TestBase {

	@Test(description = "COD Pending state : cancel transaction in merchant panel", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void codPendingTransactioninMerchantPanel(Config testConfig)  {

		//Do pending COD without Zipdial 
		int transactionRowNum = 23;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		helper.testResponse =(TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.TestResponsePage);
  
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		Browser.wait(testConfig, 5);
		dashBoard.ClickCodVerify();
		dashBoard.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));  
		dashBoard.selectTransactionVerifyAction();
		dashBoard.ClickCodCancel();
		
		dashBoard.SearchTransactionMerchantPanel(testConfig.getRunTimeProperty("transactionId"));  
		Helper.compareEquals(testConfig, "result count","1", dashBoard.transactionno());	
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	@Test(description = "Navigate to COD Cancel page", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void codCancelTransactionCount(Config testConfig) {

		int sqlrow = 52;
		int transactionRowNum = 23;
		testConfig.putRunTimeProperty("merchant_id", "5961");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);
		testConfig.putRunTimeProperty("to",
				Helper.getDateBeforeOrAfterDays(1, "yyyy-MM-dd"));
		testConfig.putRunTimeProperty("from",
				Helper.getDateBeforeOrAfterDays(-7, "yyyy-MM-dd"));

		dashBoard.ClickCodCancel();
		Browser.wait(testConfig, 10);

		Map<String, String> transaction_count = DataBase.executeSelectQuery(
				testConfig, sqlrow, 1);

		Helper.compareEquals(testConfig, "result count",
				transaction_count.get("total"), dashBoard.transactionno());

		Assert.assertTrue(testConfig.getTestResult());

	}
	
	@Test(description = "COD cancel in progress transaction in merchant panel", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void codInProgressTransactioninMerchantPanel(Config testConfig)  {

		//Do in-progress COD without Zipdial 
		int transactionRowNum = 23;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		helper.testResponse =(TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.TestResponsePage);
  
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickCodCancel();
		dashBoard.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));  
		Helper.compareEquals(testConfig, "result count","1", dashBoard.transactionno());	
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	
	
	

	@Test(description = "cod Cancel Next page link is working fine", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void verifyNextLink(Config testConfig)   {

		//Do in-progress COD without Zipdial 
		int transactionRowNum = 23;
		String beforeNextLink;
		String afterNextLink;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
	
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickCodCancel();
		
		beforeNextLink = dashBoard.transactionRow();
		if(dashBoard.ClickNextLink())
		{
		afterNextLink = dashBoard.transactionRow();
		Helper.compareTrue(testConfig, "next link", !beforeNextLink.equals(afterNextLink));
		}		
		Assert.assertTrue(testConfig.getTestResult());

		
	}
	
	
	@Test(description = "Verify that a pop up with the reference id is displayed", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void generateCodCancelRequestID(Config testConfig)   {

		//Do in-progress COD without Zipdial 
		int transactionRowNum = 23;
		String ReqID;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
	
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickCodCancel();
		ReqID= dashBoard.selectTransactionCancelAction();

		Helper.compareTrue(testConfig, "Request ID", !ReqID.equals(null));
		Assert.assertTrue(testConfig.getTestResult());	
	}

	
	
	
}
	
	