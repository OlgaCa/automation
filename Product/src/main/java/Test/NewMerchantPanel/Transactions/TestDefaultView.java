package Test.NewMerchantPanel.Transactions;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import PageObject.NewMerchantPanel.Transactions.RequestsPage;
import Test.NewMerchantPanel.Transactions.TransactionsHelper;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestDefaultView extends TestBase {

	private TransactionsPage transactionPage;
	private RequestsPage requestPage;
	private MerchantPanelPage merchantPage;
	private TransactionsHelper transactionHelper;
	/**
	 * Test Case ID Product-1948:  Verify Capture Transaction on Merchant Panel
	 * 
	 * @param testConfig
	 * @throws InterruptedException 
	 */
	
	@Test(description = "Verify Capture Transaction on Merchant Panel", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyCapturedTransactionsOnMerchantPanel(Config testConfig) throws InterruptedException {
		
		int transactionRowNum = 157;
		int paymentTypeRowNum =3;
		int cardDetailsRowNum = 1;
		int requestQueryRowNum=120;

		//perform the transaction and verify the status
		TransactionHelper transHelper = new TransactionHelper(testConfig, false);
		transHelper.DoLogin();
		transHelper.GetTestTransactionPage();

		transHelper.testResponse=(TestResponsePage) transHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		transHelper.testResponse.VerifyTransactionResponse(new TestDataReader(
				testConfig, "TransactionDetails"), transactionRowNum,
				new TestDataReader(testConfig, "PaymentType"), paymentTypeRowNum);
		
		String txnID=testConfig.getRunTimeProperty("txnId");

		//login into the  Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		merchantPage=helper.doMerchantLogin(transactionRowNum);
		transactionPage=merchantPage.ClickTransactionsTab();
		//verify transaction status
		transactionPage.verifyTransactionStatus(txnID, "Captured",1);
		transactionHelper=new TransactionsHelper(testConfig);
		transactionHelper.clickSelectiveCoulmnsAndCompareDetailsWithDB(requestQueryRowNum);

		Assert.assertTrue(testConfig.getTestResult());	

	}
	
	
	/**
	 * To verify all the data after a user completes a failed transaction(DC) with the data displayed in the merchant panel 
	 * and in the database for the same transaction id.
	 * Product-1955:Verify_Failure_Transaction on Merchant Panel
	 * @param testConfig
	 * @throws InterruptedException 
	 */
	@Test(description = "Verify Failed Transaction on Merchant Panel", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyFailedTransactionsOnMerchantPanel(Config testConfig) throws InterruptedException {

		int transactionRowNum = 157;
		int paymentTypeRowNum =377;
		int cardDetailsRowNum = 72;
		int requestQueryRowNum=120;

		//perform the transaction and verify the status
		TransactionHelper transHelper = new TransactionHelper(testConfig, false);
		transHelper.DoLogin();
		transHelper.GetTestTransactionPage();

		transHelper.testResponse=(TestResponsePage) transHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		transHelper.testResponse.VerifyTransactionResponse(new TestDataReader(
				testConfig, "TransactionDetails"), transactionRowNum,
				new TestDataReader(testConfig, "PaymentType"), paymentTypeRowNum);
		String txnID=testConfig.getRunTimeProperty("txnId");
		//login into the  Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		merchantPage=helper.doMerchantLogin(transactionRowNum);
		transactionPage=merchantPage.ClickTransactionsTab();
		//verify the transaction status
		transactionPage.verifyTransactionStatus(txnID, "Failed",1);
		//verify weather Transaction Details in Dashboard
		transactionHelper=new TransactionsHelper(testConfig);
		transactionHelper.clickSelectiveCoulmnsAndCompareDetailsWithDB(requestQueryRowNum);
		Assert.assertTrue(testConfig.getTestResult());	

	}

	/**
	 * Test Case ID Product-1534 : Verifying Default View of All Transactions Option Under Transaction Tab
	 * @param testConfig
	 */
	@Test(description = "Verify Default view on Transactions page", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyDefaultViewInTransaction(Config testConfig) {

		int transactionRowNum = 157;
		int expectedDateRange=3;
		// login into the Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		merchantPage = helper.doMerchantLogin(transactionRowNum);
		testConfig.putRunTimeProperty("merchantID", "6912");
		transactionPage = merchantPage.ClickTransactionsTab();
		
		transactionPage.verifyDateRange(expectedDateRange);
		transactionPage.verifyDefaultColumns();
		transactionPage.verifyActionsItems();		
		
		Assert.assertTrue(testConfig.getTestResult());	

	}
	
	/**
	 * Product-1535:Verify_Default View_All Requests
	 * @param testConfig
	 */
	@Test(description = "Verify Default view on Request Page", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyDefaultViewInRequest(Config testConfig) {

		int transactionRowNum = 157;
		// login into the Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		merchantPage = helper.doMerchantLogin(transactionRowNum);

		testConfig.putRunTimeProperty("merchantID", "6912");
		requestPage = merchantPage.ClickRequestsTab();
		requestPage.verifyDefaultColumns();
		Assert.assertTrue(testConfig.getTestResult());	

	}
}
