package Test.AdminPanel.ReconAndBankFeeSettlement;


import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.ManualUpdate.ChargebackTab;
import PageObject.AdminPanel.ManualUpdate.ChargebackTab.ChargebackFileType;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Transactions.RefundPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage.SearchOn;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Helper;
import Utils.TestBase;

public class TestRecon extends TestBase {

	public Config testConfig;

	//Test Case Id: Product-310
	@Test(description = "Perform a captured transaction, upload recon file and compare Recon output", dataProvider = "GetTestConfig", groups = "1")
	public void TestCapturedTransactionReconUpload(Config testConfig) {
		ReconHelper recon = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		int transactionRowNum = 7;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int reconRowNum[]=new int[1];
		String mihpayid[]=new String[1];
		reconRowNum[0]=1;
		 
		//Do a Transaction in captured state
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		mihpayid[0]=testConfig.getRunTimeProperty("mihpayid");
		//Recon the transaction
		String reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Test Case Id: Product-311
	@Test(description = "Perform a failed transaction, upload recon file and compare Recon output", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestFailedTransactionReconUpload(Config testConfig) {
		ReconHelper recon = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		int transactionRowNum = 161;
		int paymentTypeRowNum = 380;
		int cardDetailsRowNum = 39;
		int reconRowNum[]=new int[1];
		String mihpayid[]=new String[1];
		reconRowNum[0]=2;
		
		//Do a Transaction in captured state
		helper.tryAgainPage = (TryAgainPage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TryAgainPage);
		helper.testResponse = helper.tryAgainPage.clickOnMerchant();
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//Recon the transaction
		String reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		Assert.assertTrue(testConfig.getTestResult());
		
	}

	//Test Case Id: Product-312
	@Test(description = "Perform a user cancelled transaction, upload recon file and compare Recon output", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestUserCancelledTransactionReconUpload(Config testConfig) {
		ReconHelper recon = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		int transactionRowNum = 2;
		int paymentTypeRowNum = 226;
		int cardDetailsRowNum = 2;
		int reconRowNum[]=new int[1];
		String mihpayid[]=new String[1];
		reconRowNum[0]=4;
		
		//Do a Transaction in captured state
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");
		
		//Recon the transaction
		String reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	//Test Case Id: Product-313
	@Test(description = "Perform an in progress transaction, upload recon file and compare Recon output", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestInProgressTransactionReconUpload(Config testConfig) {
		ReconHelper recon = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		//AMEX
		int transactionRowNum = 161;
		int paymentTypeRowNum = 60;
		int cardDetailsRowNum = -1;
		
		int reconRowNum[]=new int[1];
		String mihpayid[]=new String[1];
		reconRowNum[0]=3;
		
		//Do a Transaction in captured state
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 60, 1);
		mihpayid[0]=map.get("id");
		//helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
		
		
		//Recon the transaction
		String reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		Assert.assertTrue(testConfig.getTestResult());


	}

	//Test Case Id: Product-314
	@Test(description = "Perform a valid transaction, upload recon file with invalid action and compare Recon output", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void InvalidActionsinReconFile(Config testConfig) {
		ReconHelper recon = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		//Invalid action "cancel" uploaded in recon file
		int transactionRowNum = 23;
		int paymentTypeRowNum = 8;
		int cardDetailsRowNum = -1;

		int reconRowNum[]=new int[1];
		String mihpayid[]=new String[1];
		reconRowNum[0]=5;
		
		//Do a Transaction in captured state
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 60, 1);
		mihpayid[0]=map.get("id");
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
			
		//Recon the transaction
		String reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		

		//Invalid action "in progress" uploaded in recon file
		helper.GetTestTransactionPage();
		transactionRowNum = 7;
		paymentTypeRowNum = 5;
		cardDetailsRowNum = 1;
		reconRowNum[0]=6;
		
		//Do a Transaction in captured state
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		
		//Recon the transaction
		reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		Assert.assertTrue(testConfig.getTestResult());
		

	}


	@Test(description = "Perform a valid transaction, upload recon file with invalid status and compare Recon output", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestInvalidStatusinReconFile(Config testConfig) {
		ReconHelper recon = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		//Invalid action "cancel" uploaded in recon file
		int transactionRowNum = 23;
		int paymentTypeRowNum = 8;
		int cardDetailsRowNum = -1;

		int reconRowNum[]=new int[1];
		String mihpayid[]=new String[1];
		reconRowNum[0]=7;
		
		//Do a Transaction in captured state
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 60, 1);
		mihpayid[0]=map.get("id");
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
		
		//Recon the transaction
		String reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		

		//Invalid action "in progress" uploaded in recon file
		helper.GetTestTransactionPage();
		transactionRowNum = 7;
		paymentTypeRowNum = 5;
		cardDetailsRowNum = 1;
		reconRowNum[0]=8;
		
		//Do a Transaction in captured state
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");
		//Recon the transaction
		reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		Assert.assertTrue(testConfig.getTestResult());
		
	}


	@Test(description = "Perform a valid transaction, upload recon file with blank status and action and compare Recon output", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void BlankStatusandActioninReconFile(Config testConfig) {
		
		ReconHelper recon = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		//*******Blank Status is uploaded in recon file*******
		int transactionRowNum = 23;
		int paymentTypeRowNum = 8;
		int cardDetailsRowNum = -1;

		int reconRowNum[]=new int[1];
		String mihpayid[]=new String[1];
		reconRowNum[0]=9;
		
		//Do a Transaction in captured state
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 60, 1);
		mihpayid[0]=map.get("id");
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
			
		//Recon the transaction
		String reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		

		//*******Blank action is uploaded in recon file*******
		helper.GetTestTransactionPage();
		transactionRowNum = 7;
		paymentTypeRowNum = 5;
		cardDetailsRowNum = 1;
		reconRowNum[0]=10;
		
		//Do a Transaction in captured state
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");
		
		//Recon the transaction
		reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		
		//*******Blank Transaction id is uploaded in Recon*******
		reconRowNum[0]=12;
		mihpayid[0]="";
		reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		//Assert.assertTrue(testConfig.getTestResult());
		
	}

	@Test(description = "Perform a valid transaction, refund it, upload recon file and compare Recon output", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void RefundTransactioninReconFile(Config testConfig) {
		ReconHelper recon = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int transactionRowNum = 7;
		int paymentTypeRowNum =5;
		int cardDetailsRowNum = 1;
		int reconRowNum[]=new int[1];
		String mihpayid[]=new String[1];
		reconRowNum[0]=11;
		
		helper.GetTestTransactionPage();
		
		helper.testResponse = (TestResponsePage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request refund 
		refundTransaction.SearchTransaction(mihpayid[0], SearchOn.PayUId);
		refundTransaction.RefundFullFirstTransaction();
		
		//Recon Refunded Transaction
		String reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		Assert.assertTrue(testConfig.getTestResult());
		
	}

	// Test-Case ID: Product-1505
	@Test(description = "Verifying Recon success for Refund reversal", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingReconSuccessForRefundReversal(Config testConfig) {

		ReconHelper recon = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 122;
		int paymentTypeRowNum = 371;
		int cardDetailsRowNum = 1;
		int refundReversalRow = 2;
		int reconRowNum[] = new int[1];
		String mihpayid[] = new String[1];
		reconRowNum[0] = 24;

		helper.GetTestTransactionPage();
		// Do Transaction
		// Section 1 Steps 1-2
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");
		// Write Recon File
		String reconFile = recon.writeAdminReconFile(reconRowNum, mihpayid);
		// ---------------------------------------------------------------------------//
		// Steps for Refunding the Transaction
		// Section 2 Steps 3-9
		// Go to Dashboard
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = (DashboardPage) dashboardHelper
				.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();
		refundTransaction.RefundFullFirstTransaction();
		// Steps for Refund reversal
		// Section 3 Steps 10-12
		String strMihpayid = mihpayid[0];
		recon.doRefundReversal(helper,strMihpayid, refundReversalRow);
		// Steps for uloading AdminRecon File
		// Section 4-5-6 Steps 10-18
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum, mihpayid);
		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1506
	@Test(description = "Verifying Recon Failure due to invalid txnid", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingReconFailureDueToInvalidTxnid(Config testConfig) {
		ReconHelper reconHelper = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int reconRowNum[] = new int[1];
		String mihpayid[] = new String[1];
		reconRowNum[0] = 25;
		mihpayid[0] =String.valueOf(Helper.generateRandomNumber(17));
		/*
		 * Steps for uploading 'AdminRecon' file. steps for performing cron
		 * operation. steps to download excel file Section 1 -2-3
		 */
		String reconFile = reconHelper.writeAdminReconFile(reconRowNum,
				mihpayid);
		helper.DoLogin();
		reconHelper.UploadAndCompareExcel(helper, reconFile, reconRowNum,
				mihpayid);
		reconHelper.verifyEntryNotCreatedInTxnUpdateTable(mihpayid[0]);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1510
	@Test(description = "Verifying Recon Failure due to incorrect Amount.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingReconFailureDueToIncorrectAmount(Config testConfig) {

		ReconHelper reconHelper = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// -----------------------------------------------------------------------//
		// Steps for Credit card transaction
		// 1) Click on test Transaction link and perform a credit card
		// transaction.
		int transactionRowNum = 161;
		int paymentTypeRowNum = 366;
		int cardDetailsRowNum = 1;
		int reconRowNum[] = new int[1];
		String mihpayid[] = new String[1];
		reconRowNum[0] = 26;
		

		// Do a test transaction
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");
		// Write recon file
		String reconFile = reconHelper.writeAdminReconFile(reconRowNum,
				mihpayid);

		// Steps for uploading 'AdminRecon' file.
		reconHelper.UploadAndCompareExcel(helper, reconFile, reconRowNum,
				mihpayid);
		// c) Verify db values in 'txn_update' table with following sql queries.
		String[] ActionCase = {"capture"};
		helper.verifyEntriesInTxnUpdate(
				testConfig.getRunTimeProperty("mihpayid"), ActionCase);
		// d) Verify 'transaction_settlement' table with following sql queries.
		helper.verifyEntryCreatedInTransactionSettlement(reconRowNum[0],mihpayid[0], false);
		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1513
	@Test(description = "Verifying Recon Failure due to mandatory column missing in Recon file", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingReconFailureDueToMandatoryColumnMissingInReconFile(
			Config testConfig) {
		ReconHelper reconHelper = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int reconRowNum[] = new int[1];
		String mihpayid[] = new String[1];
		reconRowNum[0] = 27;
		helper.DoLogin();
		mihpayid[0] = "";
		String ReconcilationError = "Reconciliation Error: Mandatory column missing. "
				+ "Mandatory columns are: requestid, transactionid, action, status, amount, servicefee"
				+ ", servicetax, net, utr, refnum, reconciliationid, category, card_type";
		// ----------------------------------------------------------------------------------------------------//
		// 1 Steps for uploading 'Recon' file without 'transactionid' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum,
				"transactionid", ReconcilationError);
		// 2 Steps for uploading 'Recon' file without 'status' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum, "status",
				ReconcilationError);
		// 3 Steps for uploading 'Recon' file without 'action' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum, "action",
				ReconcilationError);
		// 4 Steps for uploading 'Recon' file without 'amount' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum, "amount",
				ReconcilationError);
		// 5 Steps for uploading 'Recon' file without 'requestid' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum,
				"requestid", ReconcilationError);
		// 6 Steps for uploading 'Recon' file without 'service fee' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum,
				"servicefee", ReconcilationError);
		// 7 Steps for uploading 'Recon' file without 'service tax' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum,
				"servicetax", ReconcilationError);
		// 8 Steps for uploading 'Recon' file without 'net' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum, "net",
				ReconcilationError);
		// 9 Steps for uploading 'Recon' file without 'utr' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum, "utr",
				ReconcilationError);
		// 10 Steps for uploading 'Recon' file without 'refnum' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum, "refnum",
				ReconcilationError);
		// 11 Steps for uploading 'Recon' file without 'reconcilationid' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum,
				"reconciliationid", ReconcilationError);
		// 12 Steps for uploading 'Recon' file without 'CATEGORY' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum,
				"category", ReconcilationError);
		// 13 Steps for uploading 'Recon' file without 'cardtype' column.
		reconHelper.uploadReconWithColumnMissingAndVerifyErrorMessage(helper, reconRowNum,
				"card_type", ReconcilationError);
		Assert.assertTrue(testConfig.getTestResult());

	}

	//Note: Test Case fails due to application issues
	// Test-Case ID: Product-1514
	@Test(description = "Verifying Recon for chargeback reversal", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingReconForChargebackReversal(Config testConfig) {

		ReconHelper reconHelper = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		// -----------------------------------------------------------------------//
		// Steps for Credit card transaction
		// 1) Click on test Transaction link and perform a credit card
		// transaction.
		int transactionRowNum = 158;
		int paymentTypeRowNum = 366;
		int cardDetailsRowNum = 1;
		int refundReversalRow = 2;
		int reconRowNum[] = new int[1];
		String mihpayid[] = new String[1];
		reconRowNum[0] = 29;
		
		// Steps for credit card transaction
		// Section 1
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");
		// Write Recon File
		String reconFile = reconHelper.writeAdminReconFile(reconRowNum,
				mihpayid);
		// Steps to upload chargeback file
		// Section 2 Steps 3-6
		ChargebackTab chargeback = reconHelper.getChargebackPage(helper);
		chargeback.createAndUploadChargebackfile(mihpayid[0],
				refundReversalRow, ChargebackFileType.chargeback);
		Browser.wait(testConfig, 10);
		// Steps to Upload chargeback reversal file.
		// Section 3 steps 7
		chargeback.createAndUploadChargebackfile(mihpayid[0],
				refundReversalRow, ChargebackFileType.chargebackReversal);
		// Verify Under Result column of the downloaded excel file 'Success'
		// should be displayed.
		chargeback.verifyChargebackFileDownloaded("SUCCESS");
		// Steps for uploading 'AdminRecon' file.
		// Section 4-5-6 Steps 8-18
		reconHelper.UploadAndCompareExcel(helper, reconFile, reconRowNum,
				mihpayid);
		// c) Verify db values in 'txn_update' table with following sql queries.
		String ActionCases[] = {"capture", "chargeback", "chargebackreversal"};
		helper.verifyEntriesInTxnUpdate(
				testConfig.getRunTimeProperty("mihpayid"), ActionCases);
		// d) Verify db values in 'transaction_settlement' table with following
		// sql queries.
		Assert.assertTrue(testConfig.getTestResult());

	}

	//Note: Test Case fails due to application issues
	// Test-Case ID: Product-1515
	@Test(description = "Verifying Recon for chargeback", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingReconForChargeback(Config testConfig) {

		ReconHelper reconHelper = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		int transactionRowNum = 146;
		int paymentTypeRowNum = 366;
		int cardDetailsRowNum = 1;
		int refundReversalrow = 2;
		int reconRowNum[] = new int[1];
		String mihpayid[] = new String[1];
		reconRowNum[0] = 30;
		

		// Steps for Credit card transaction
		// go to test transaction page
		helper.GetTestTransactionPage();
		// fill transaction details and click on submit button
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");
		// Write Recon File
		String reconFile = reconHelper.writeAdminReconFile(reconRowNum,
				mihpayid);
		// Steps to upload chargeback file
		ChargebackTab chargeback = reconHelper.getChargebackPage(helper);
		chargeback.createAndUploadChargebackfile(mihpayid[0],
				refundReversalrow, ChargebackFileType.chargeback);
		// a) Verify that an Download Excel file dialogbox is displayed.
		// b) Verify Under Result column of the downloaded excel file 'Success'
		// should be displayed.
		chargeback.verifyChargebackFileDownloaded("SUCCESS");
		// Steps for uploading 'AdminRecon' file.
		reconHelper.UploadAndCompareExcel(helper, reconFile, reconRowNum,
				mihpayid);
		// c) Verify db values in 'txn_update' table with following sql queries.
		String ActionCases[] = {"capture", "chargeback"};
		helper.verifyEntriesInTxnUpdate(
				testConfig.getRunTimeProperty("mihpayid"), ActionCases);

		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1516
	@Test(description = "Verifying Recon for Refunded transaction", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingReconForRefundTransaction(Config testConfig) {

		ReconHelper recon = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// 1) Click on test Transaction link and perform a credit card
		// transaction.
		int transactionRowNum = 122;
		int paymentTypeRowNum = 371;
		int cardDetailsRowNum = 1;
		int reconRowNum[] = new int[1];
		String mihpayid[] = new String[1];
		reconRowNum[0] = 31;
		
		// -----------------------------------------------------------------------//
		// Steps for Credit card transaction
		// Section 1 Steps 1-2
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// Write Recon File
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");
		String reconFile = recon.writeAdminReconFile(reconRowNum, mihpayid);

		// ---------------------------------------------------------------------------//
		// Steps for Refunding the Transaction
		// Section 2 Steps 3-9
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = (DashboardPage) dashboardHelper
				.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();
		refundTransaction.RefundFullFirstTransaction();
		// Steps for uloading AdminRecon File
		// Section 3-4-5 Steps 10-18
		recon.UploadAndCompareExcel(helper, reconFile, reconRowNum, mihpayid);
		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1517
	@Test(description = "Verifying Recon successful for dropped transaction.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingReconSuccessfulForDroppedTransaction(
			Config testConfig) {

		ReconHelper reconHelper = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int reconRowNum[] = new int[1];
		String mihpayid[] = new String[1];
		reconRowNum[0] = 32;
		helper.DoLogin();

		testConfig.putRunTimeProperty("check_merchant_amount", "NO");
		// Steps for copying payu ID of dropped transaction
		testConfig.putRunTimeProperty("transaction_status", "Dropped");
		testConfig.putRunTimeProperty("mode", "DC");
		testConfig.putRunTimeProperty("merchantid", "5914");
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 117,
				1);
		mihpayid[0] = map.get("id");
		String amount =map.get("amount");
		//replacing trailing zeroes from amount if any
		amount = amount.indexOf(".") < 0 ? amount : amount.replaceAll("0*$", "").replaceAll("\\.$", "");
		testConfig.putRunTimeProperty("amount",amount);
		testConfig.putRunTimeProperty("mihpayid", mihpayid[0]);
		// Steps for uploading 'AdminRecon' file.
		String reconFile = reconHelper.writeAdminReconFile(reconRowNum,
				mihpayid);
		reconHelper.UploadAndCompareExcel(helper, reconFile, reconRowNum,
				mihpayid);
		// c) Verify db values in 'txn_update' table with following sql queries.
		String[] ActionCases = {"capture"};
		helper.verifyEntriesInTxnUpdate(mihpayid[0], ActionCases);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1519
	@Test(description = "Verifying Recon Failure for the bounced transaction", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingReconFailureForBouncedTransaction(
			Config testConfig) {

		ReconHelper reconHelper = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int reconRowNum[] = new int[1];
		String mihpayid[] = new String[1];
		reconRowNum[0] = 33;
		helper.DoLogin();

		testConfig.putRunTimeProperty("check_merchant_amount", "NO");
		// Steps for copying payu ID of dropped transaction
		testConfig.putRunTimeProperty("transaction_status", "Bounced");
		testConfig.putRunTimeProperty("mode", "DC");
		testConfig.putRunTimeProperty("merchantid", "5914");
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 117,
				1);
		mihpayid[0] = map.get("id");
		String amount =map.get("amount");
		//replacing trailing zeroes from amount if any
		amount = amount.indexOf(".") < 0 ? amount : amount.replaceAll("0*$", "").replaceAll("\\.$", "");
		testConfig.putRunTimeProperty("amount",amount);
		testConfig.putRunTimeProperty("mihpayid", mihpayid[0]);
		// Steps for uploading 'AdminRecon' file.
		String reconFile = reconHelper.writeAdminReconFile(reconRowNum,
				mihpayid);
		reconHelper.UploadAndCompareExcel(helper, reconFile, reconRowNum,
				mihpayid);
		// c) Verify db values in 'txn_update' table with following sql queries.
		reconHelper.verifyEntryNotCreatedInTxnUpdateTable(mihpayid[0]);
		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1520
	@Test(description = "Verifying Recon Failure for transaction in initiated state", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingReconFailureForTransactionInInitiatedState(
			Config testConfig) {

		ReconHelper reconHelper = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int reconRowNum[] = new int[1];
		String mihpayid[] = new String[1];
		reconRowNum[0] = 33;
		int transactionRow =146;
		helper.DoLogin();
		
		PaymentOptionsPage payment = helper.GetPaymentOptionPage(transactionRow);
		String transactionId = payment.getTransactionId();
		testConfig.putRunTimeProperty("check_merchant_amount", "NO");
		// Get id from database
		testConfig.putRunTimeProperty("txnid", transactionId);
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 109,
				1);
		mihpayid[0] = map.get("id");
		testConfig.putRunTimeProperty("mihpayid", mihpayid[0]);
		// Steps for uploading 'AdminRecon' file.
		String reconFile = reconHelper.writeAdminReconFile(reconRowNum,
				mihpayid);
		reconHelper.UploadAndCompareExcel(helper, reconFile, reconRowNum,
				mihpayid);
		// c) Verify db values in 'txn_update' table with following sql queries.
		reconHelper.verifyEntryNotCreatedInTxnUpdateTable(mihpayid[0]);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1521
	@Test(description = "Verifying successful recon for the transaction when 'autorefund' flag is on.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingSuccessfulReconForTheTransactionWhenAutorefundFlagIsOn(
			Config testConfig) {

		ReconHelper reconHelper = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 147;
		int paymentTypeRowNum = 367;
		int cardDetailsRowNum = 1;
		int reconRowNum[] = new int[1];
		String mihpayid[] = new String[1];
		reconRowNum[0] = 34;
		
		testConfig.putRunTimeProperty("offerKey", "failtrnsaction@3152");

		// Steps for Credit card transaction
		// Section 1 Steps 1-5
		helper.GetTestTransactionPage();
		TryAgainPage tryAgainpage = (TryAgainPage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TryAgainPage);
		TestResponsePage testresponse = tryAgainpage.clickOnMerchant();
		mihpayid[0] = testConfig.getRunTimeProperty("mihpayid");
		// write recon file
		String reconFile = reconHelper.writeAdminReconFile(reconRowNum,
				mihpayid);
		// Steps for uloading AdminRecon File
		// Section 2-3-4 Steps 3-11
		reconHelper.UploadAndCompareExcel(helper, reconFile, reconRowNum,
				mihpayid);
		// c) Verify db values in 'txn_update' table with following sql queries.
		String[] ActionCase = {"capture"};
		helper.verifyEntriesInTxnUpdate(
				testConfig.getRunTimeProperty("mihpayid"), ActionCase);
		// d) Verify db values in 'transaction_settlement' table with following
		// sql queries.
		helper.verifyEntryCreatedInTransactionSettlement(reconRowNum[0],
				testConfig.getRunTimeProperty("txn_update_id"), true);

		Assert.assertTrue(testConfig.getTestResult());

	}

}
	
