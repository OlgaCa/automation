package Test.MerchantPanel.Transactions;


import java.io.File;
import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.NewResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Transactions.GenerateInvoiceTransactionPage;
import PageObject.MerchantPanel.Transactions.TransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;


public class TestGenerateInvoice extends TestBase{


	@Test(description = "Verify generate invoice transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyInvoiceTransaction(Config testConfig)	{
		int transactionRowNum=1;
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		TransactionsPage  invoiceTransaction = dashBoard.ClickViewAllTxn();
		invoiceTransaction.clickFirstTxn();
		GenerateInvoiceTransactionPage generateInvoice= invoiceTransaction.ClickGInvoice();

		generateInvoice.EnterAmount("100");
		generateInvoice.selectTemplate("382");
		generateInvoice.Submit();
		generateInvoice.Ok();
		//read the downloaded file
		Browser.wait(testConfig, 5);
		String massInvoiceResult = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,massInvoiceResult);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", massInvoiceResult+fileName);

		Assert.assertTrue(tr.GetData(1, "invoice_status").endsWith("success"));


	}
	@Test(description = "Verify multiple generate invoice transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyMultipleInvoiceTransaction(Config testConfig)	{
		int transactionRowNum=1;
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		TransactionsPage  invoiceTransaction = dashBoard.ClickViewAllTxn();
		invoiceTransaction.clickMulticheck();
		GenerateInvoiceTransactionPage generateInvoice= invoiceTransaction.ClickGInvoice();

		generateInvoice.EnterAmount("100");
		generateInvoice.selectTemplate("382");
		generateInvoice.Submit();
		generateInvoice.Ok();
		//read the downloaded file
		Browser.wait(testConfig, 5);
		String massInvoiceResult = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,massInvoiceResult);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", massInvoiceResult+fileName);

		Assert.assertTrue(tr.GetData(1, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(2, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(3, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(4, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(5, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(6, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(7, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(8, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(9, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(10, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(11, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(12, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(13, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(14, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(15, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(16, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(17, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(18, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(19, "invoice_status").endsWith("success"));
		Assert.assertTrue(tr.GetData(20, "invoice_status").endsWith("success"));

	}

	@Test(description = "Verify message on entering invalid invoice amount", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyInvalidAmount(Config testConfig)	{
		int transactionRowNum=1;
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		TransactionsPage  invoiceTransaction = dashBoard.ClickViewAllTxn();
		invoiceTransaction.clickFirstTxn();
		GenerateInvoiceTransactionPage generateInvoice= invoiceTransaction.ClickGInvoice();

		//enter special charactors
		generateInvoice.EnterAmount("*()^");
		generateInvoice.Submit();
		generateInvoice.invalidAmount();
		generateInvoice.ClickOk();

		//Enter blank/zero amount 
		invoiceTransaction.clickFirstTxn();
		invoiceTransaction.ClickGInvoice();
		generateInvoice.EnterAmount("0");
		generateInvoice.Submit();
		generateInvoice.invalidAmount();
		generateInvoice.ClickOk();

		//enter negative amount
		invoiceTransaction.clickFirstTxn();
		invoiceTransaction.ClickGInvoice();
		generateInvoice.EnterAmount("-3400");
		generateInvoice.Submit();
		generateInvoice.invalidAmount();

		//enter alphanumeric amount
		invoiceTransaction.clickFirstTxn();
		invoiceTransaction.ClickGInvoice();
		generateInvoice.EnterAmount("100ljdkj");
		generateInvoice.Submit();
		generateInvoice.invalidAmount();

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify generate invoice transaction without selecting transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifySelectInvoiceTransaction(Config testConfig)	{
		int transactionRowNum=1;
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		TransactionsPage  invoiceTransaction = dashBoard.ClickViewAllTxn();
		invoiceTransaction.ClickGenerateInvoice();
		Browser.wait(testConfig, 5);
		invoiceTransaction.transactionPage();
		Assert.assertTrue(testConfig.getTestResult());


	}
	@Test(description = "Verify Go back button functionality", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyGoBack(Config testConfig)	{
		int transactionRowNum=1;
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		TransactionsPage  invoiceTransaction = dashBoard.ClickViewAllTxn();
		invoiceTransaction.clickFirstTxn();

		GenerateInvoiceTransactionPage genInvoice= invoiceTransaction.ClickGInvoice();
		genInvoice.clickGoback();
		invoiceTransaction.transactionPage();
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify generate invoice for an invoice transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyGenerateInvoiceTransaction(Config testConfig)	{
		int transactionRowNum = 12;
		int cardDetailsRowNum = 1;

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		// login as merchant and fill email invoice form
		dashboardHelper.loginAndFillEmailInvoice(transactionRowNum,"invoice");
		String txnId=testConfig.getRunTimeProperty("txnId");
		Browser.wait(testConfig, 3);
		// verify close button present on pop up
		dashboardHelper.invoiceTransactionConfirmationPage.verifyCloseButtonPresent();

		// copy link in pop up
		String url2 = dashboardHelper.invoiceTransactionConfirmationPage.retInvoiceURL();
		Browser.navigateToURL(testConfig, url2);
		//dashboardHelper.invoiceTransactionConfirmationPage.CopyLink();

		NewResponsePage newResponse = helper.makePaymentViaCreditCard(cardDetailsRowNum);
		// verify for success transaction
		newResponse.verifyPageText(true);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		TransactionsPage  invoiceTransaction = dashBoard.ClickViewAllTxn();
		invoiceTransaction.clickFirstTxn();
		GenerateInvoiceTransactionPage generateInvoice= invoiceTransaction.ClickGInvoice();

		generateInvoice.EnterAmount("100");
		generateInvoice.selectTemplate("382");
		generateInvoice.Submit();
		generateInvoice.Ok();
		//read the downloaded file
		Browser.wait(testConfig, 5);
		String massInvoiceResult = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,massInvoiceResult);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", massInvoiceResult+fileName);

		Assert.assertTrue(tr.GetData(1, "invoice_status").endsWith("success"));


	}
}