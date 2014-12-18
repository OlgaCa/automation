package Test.AdminPanel.Payments;

import java.io.File;
import java.util.Hashtable;

import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.WebServices.WebServicesHelper;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import PageObject.MerchantPanel.Transactions.CancelPage;
import PageObject.MerchantPanel.Transactions.CancelRefundPage;
import PageObject.MerchantPanel.Transactions.CapturePage;
import PageObject.MerchantPanel.Transactions.RefundPage;
import PageObject.MerchantPanel.Transactions.RequestsPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage.SearchOn;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Test.MerchantPanel.Payments.TestAutoRefund;
import Test.MerchantPanel.Payments.TestAutoRefund.AutoRefund;
import Test.MerchantPanel.Payments.TestAutoRefund.verifyReconResult;
import Test.MerchantPanel.Payments.TestReconUpload;
import Test.MerchantPanel.Payments.TestReconUpload.verifyReconResponse;
import Test.MerchantPanel.Transactions.TestCancelBulkUpload;
import Test.MerchantPanel.Transactions.TestCaptureBulkUpload;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;
import Utils.Helper.FileType;

public class LiveCardTestCases extends TestBase {
	DashboardPage dashBoard;

	// 1-Test.MerchantPanel.Payments.TestAutoRefund.TestAuthAutoRefundOff
	@Test(description = "Verify Auto Refund for Auth Transaction with AutoRefund=0", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void TestAuthAutoRefundOff(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 2; // AutomationMerchant2
		TestAutoRefund objectOfTestAutoRefund = new TestAutoRefund();
		Browser.wait(testConfig, 1);
		objectOfTestAutoRefund.changeAutoRefundStatus(transactionRowNum,
				helper, AutoRefund.off);

		// performing auth transaction
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;

		int outputExcelRowToBeVerifiedRowNum = 1;
		int reconResponseRowNum = 6;

		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.BankRedirectPage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		objectOfTestAutoRefund.writeToExcel(reconFile, 1,
				testConfig.getRunTimeProperty("mihpayid"),
				helper.testResponse.actualResponse.get("amount"));

		objectOfTestAutoRefund.reconUpload(reconFile, testConfig);

		objectOfTestAutoRefund.verifyReconResponse(testConfig,
				outputExcelRowToBeVerifiedRowNum, verifyReconResult.auth,
				reconResponseRowNum);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.doMerchantLogin(transactionRowNum);

		DashboardPage dashBoard = new DashboardPage(testConfig);
		dashBoard.ClickClose();
		dashBoard.ClickViewAll();

		objectOfTestAutoRefund.waitForActionDetailsRow(testConfig, 2);
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(
				testConfig);
		dashboardHelper.transactionsPage = merchantTransaction
				.SearchTransaction(testConfig
						.getRunTimeProperty("transactionId"));

		dashboardHelper.transactionsPage.VerifyAutoRefund(
				helper.transactionData, verifyReconResult.auth, AutoRefund.off,
				testConfig);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// 2-Test.MerchantPanel.Payments.TestAutoRefund.TestAuthAutoRefundOn
	@Test(description = "Verify Auto Refund for Auth Transaction with AutoRefund=1", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void TestAuthAutoRefundOn(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 2; // AutomationMerchant2
		TestAutoRefund objectOfTestAutoRefund = new TestAutoRefund();
		objectOfTestAutoRefund.changeAutoRefundStatus(transactionRowNum,
				helper, AutoRefund.on);

		// performing auth transaction
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;

		int outputExcelRowToBeVerifiedRowNum = 1;
		int reconResponseRowNum = 6;

		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.BankRedirectPage);

		objectOfTestAutoRefund.changeAutoRefundStatus(transactionRowNum,
				helper, AutoRefund.off);

		System.out.println("payuid: "
				+ testConfig.getRunTimeProperty("mihpayid"));
		System.out.println("transactionId: "
				+ testConfig.getRunTimeProperty("transactionId"));

		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		objectOfTestAutoRefund.writeToExcel(reconFile, 1,
				testConfig.getRunTimeProperty("mihpayid"),
				helper.testResponse.actualResponse.get("amount"));

		objectOfTestAutoRefund.reconUpload(reconFile, testConfig);
		objectOfTestAutoRefund.verifyReconResponse(testConfig,
				outputExcelRowToBeVerifiedRowNum, verifyReconResult.auth,
				reconResponseRowNum);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.doMerchantLogin(transactionRowNum);

		DashboardPage dashBoard = new DashboardPage(testConfig);
		dashBoard.ClickClose();
		dashBoard.ClickViewAll();

		objectOfTestAutoRefund.waitForActionDetailsRow(testConfig, 2);
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(
				testConfig);
		dashboardHelper.transactionsPage = merchantTransaction
				.SearchTransaction(testConfig
						.getRunTimeProperty("transactionId"));

		dashboardHelper.transactionsPage.VerifyAutoRefund(
				helper.transactionData, verifyReconResult.auth, AutoRefund.on,
				testConfig);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// 3-Test.MerchantPanel.Payments.TestReconUpload.TestAuthTransactionReconUpload
	@Test(description = "Perform an auth transaction recon upload file", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void TestAuthTransactionReconUpload(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		TestReconUpload objOfTestReconUpload = new TestReconUpload();
		// CC
		int transactionRowNum = 2;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;

		int outputExcelRowToBeVerifiedRowNum = 1;
		int reconResponseRowNum = 6;

		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.BankRedirectPage);

		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		objOfTestReconUpload.writeToExcel(reconFile, 1,
				testConfig.getRunTimeProperty("mihpayid"),
				helper.testResponse.actualResponse.get("amount"));

		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();

		objOfTestReconUpload.verifyReconResponse(testConfig,
				outputExcelRowToBeVerifiedRowNum,
				verifyReconResponse.notReconciled, reconResponseRowNum);

		// Call refund_transaction using mihpayid, token and amount
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		String actualWebServiceResponse = wshelper
				.cancelrefund_transaction_executeAdminPanel(helper.home,
						testConfig.getRunTimeProperty("merchantKey"),
						testConfig.getRunTimeProperty("mihpayid"),
						helper.transactionData.GetData(transactionRowNum,
								"amount"));
		wshelper.cancel_transaction_verify(actualWebServiceResponse,
				helper.testResponse.actualResponse, "1",
				"Cancel Request Queued", "", "102");
		testConfig.putRunTimeProperty("UseStoredDetails", "0");

		Assert.assertTrue(testConfig.getTestResult());
		// FAILURE - Transaction is done with payu as aggregator

	}

	// 4-Test.MerchantPanel.Transactions.TestCancelBulkUpload.TestValidBulkUpload
	@Test(description = "Verify one transaction upload for  request", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void TestValidBulkUploadforCancel(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		TestCancelBulkUpload objOfCancelBulkUpload = new TestCancelBulkUpload();
		// MasterCard Debit Cards
		int transactionRowNum = 2;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;
		int requestRowNum = 3;

		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.BankRedirectPage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		String bulkUploadFile = Helper
				.getExcelFile(testConfig, FileType.refund);

		objOfCancelBulkUpload.writeToExcel(bulkUploadFile, 1,
				testConfig.getRunTimeProperty("mihpayid"), "authorized",
				helper.testResponse.actualResponse.get("amount"));

		// Navigating to Merchant panel to cancel request
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		CancelPage cancelTransaction = dashBoard.ClickCancel();

		// Upload bulk cancel file
		cancelTransaction.BulkFileupload(bulkUploadFile);
		cancelTransaction.CancelBulkTransaction();

		// Verify bulk canceled transaction status
		cancelTransaction.VerifyCancelTransaction(requestRowNum,
				transactionRowNum);
		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		Assert.assertTrue(testConfig.getTestResult());
	}

	// 5-Test.MerchantPanel.Transactions.TestCaptureBulkUpload.TestValidBulkUpload
	@Test(description = "Verify capture transaction bulk upload", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void TestValidBulkUploadforCapture(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		TestCaptureBulkUpload objOfCancelBulkUpload = new TestCaptureBulkUpload();
		// MasterCard Debit Cards
		int transactionRowNum = 2;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;
		int requestRowNum = 2;

		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.BankRedirectPage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		String bulkUploadFile = Helper
				.getExcelFile(testConfig, FileType.refund);

		objOfCancelBulkUpload.writeToExcel(bulkUploadFile, 1,
				testConfig.getRunTimeProperty("mihpayid"), "authorized",
				helper.testResponse.actualResponse.get("amount"));

		// Navigating to Merchant panel to capture request
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = (DashboardPage) dashboardHelper
				.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		CapturePage captureTransaction = dashBoard.ClickRequestCapture();

		// Upload bulk capture file
		captureTransaction.BulkFileupload(bulkUploadFile);
		captureTransaction.CaptureBulkTransaction();

		// Verify bulk captured transaction status
		captureTransaction.VerifyCaptureTransaction(requestRowNum,
				transactionRowNum);
		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		Assert.assertTrue(testConfig.getTestResult());
	}

	// 6,7,8,9-Test.MerchantPanel.Transactions.TestRequestCapture.VerifyFirstRowexportExcel & test_VerifyAuthTransaction & test_CC_CITI & test_VerifyCITITransaction
	@Test(description = "Verify Export to excel, Verify Auth, Citi transactions and capture through Dashboard", dataProvider = "GetTestConfig", timeOut = 3000000, groups = "LiveCardTC")
	public void VerifyFirstRowexportExcel_Auth_CapturethroughDashboard_CCCiti(Config testConfig) throws Exception {

		//***6-Test.AdminPanel.Payments.TestTransactionCC.test_CC_CITI***
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// CC
		int transactionRowNum = 2;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;
		int requestRowNum = 2;
		int requestRow = 6;
		String amount = "2.00";
		int PGInfoRow = 9;

		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.BankRedirectPage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//***7-Test.MerchantPanel.Transactions.TestRequestCapture.test_VerifyAuthTransaction***
		String txnId = testConfig.getRunTimeProperty("transactionId");
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		// Capture transaction
		CapturePage captureTransaction = dashBoard.ClickRequestCapture();
		captureTransaction.VerifyAuthtransaction(paymentTypeRowNum, transactionRowNum, PGInfoRow, amount, helper);

		//***8-Test.MerchantPanel.Transactions.TestRequestCapture.VerifyFirstRowexportExcel***
		int excelRowNum = 1;
		captureTransaction = dashBoard.ClickRequestCapture();
		dashBoard.ClickExportExcelButton();
		Browser.wait(testConfig, 20);
		String exportexcelfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig, "Sheet1",exportexcelfilepath + fileName);
		Helper.compareEquals(testConfig, "Customer name in excel",
				captureTransaction.getCustomerName(),
				String.valueOf(tr.GetData(excelRowNum, "Customer Name")));
		Helper.compareEquals(testConfig, "PayU id in excel",
				captureTransaction.getPayuId(),
				String.valueOf(tr.GetData(excelRowNum, "PayU ID")));
		Helper.compareEquals(testConfig, "Amount in excel",
				captureTransaction.getAmount(),
				String.valueOf(tr.GetData(excelRowNum, "Amount")));
		Helper.compareEquals(testConfig, "Status in excel",
				captureTransaction.getStatus(),
				String.valueOf(tr.GetData(excelRowNum, "Status")));
		Helper.compareEquals(testConfig, "Bank name in excel",
				captureTransaction.getBankname(),
				String.valueOf(tr.GetData(excelRowNum, "Bank Name")));
		Helper.compareEquals(testConfig, "req amount in excel",
				captureTransaction.getReqamount(),
				String.valueOf(tr.GetData(excelRowNum, "Requested Amount")));
		Helper.compareEquals(testConfig, "discount", captureTransaction.getDiscount(),
				String.valueOf(tr.GetData(excelRowNum, "Discount")));
		Helper.compareEquals(testConfig, "merchant name in excel",
				captureTransaction.getMerchantname(),
				String.valueOf(tr.GetData(excelRowNum, "Merchant Name")));
		Helper.compareEquals(testConfig, "transaction id",
				captureTransaction.getTxnId(),
				String.valueOf(tr.GetData(excelRowNum, "Transaction ID")));
		Helper.compareEquals(testConfig, "Payment gateway",
				captureTransaction.getPG(),
				String.valueOf(tr.GetData(excelRowNum, "Payment Gateway")));
		Helper.compareEquals(testConfig, "last name",
				captureTransaction.getLastname(),
				String.valueOf(tr.GetData(excelRowNum, "Last name")));
		Helper.compareEquals(testConfig, "cistomer email",
				captureTransaction.getEmail(),
				String.valueOf(tr.GetData(excelRowNum, "Customer Email")));
		Helper.compareEquals(testConfig, "Ip adderess",
				captureTransaction.getIPaddress(),
				String.valueOf(tr.GetData(excelRowNum, "Customer IP Address")));
		Helper.compareEquals(testConfig, "Card number",
				captureTransaction.getcardNo(),
				String.valueOf(tr.GetData(excelRowNum, "Card Number")));
		Helper.compareEquals(testConfig, "Payment type in excel",
				captureTransaction.getField2(),
				String.valueOf(tr.GetData(excelRowNum, "Field 2")));
		Helper.compareEquals(testConfig, "Field 2 in excel",
				captureTransaction.getPaymenttype(),
				String.valueOf(tr.GetData(excelRowNum, "Payment Type")));
		Helper.compareEquals(testConfig, "PGMID in excel",
				captureTransaction.getPGMID(),
				String.valueOf(tr.GetData(excelRowNum, "PG MID")));
		Helper.compareEquals(testConfig, "Offer fail reason",
				captureTransaction.getOfferfailReason(),
				String.valueOf(tr.GetData(excelRowNum, "Offer Failure Reason")));
		Helper.compareEquals(testConfig, "offer type",
				captureTransaction.getofferType(),
				String.valueOf(tr.GetData(excelRowNum, "Offer Type")));
		Helper.compareEquals(testConfig, "offer key",
				captureTransaction.getOfferKey(),
				String.valueOf(tr.GetData(excelRowNum, "Offer Key")));
		Helper.compareEquals(testConfig, "error code",
				captureTransaction.getErrorcode(),
				String.valueOf(tr.GetData(excelRowNum, "Error Code")));

		//***9-Test.MerchantPanel.Transactions.TestRequestCapture.test_VerifyCITITransaction***
		// Request capture
		captureTransaction = dashBoard.ClickRequestCapture();
		captureTransaction.SearchTransaction(txnId, SearchOn.TransactionId);
		captureTransaction.CaptureFirstTransaction();
		// View captured transaction on request page
		RequestsPage request = dashBoard.ClickViewRequest();
		TransactionFilterPage filter = new TransactionFilterPage(testConfig);
		filter.SearchTransaction(txnId, SearchOn.TransactionId);

		request.VerifyRefundRequest(requestRowNum, paymentTypeRowNum,
				transactionRowNum, PGInfoRow, amount, helper);

		// Verify captured transaction details from Database

		Browser.wait(testConfig, 60);
		captureTransaction.VerifyCaptureTransaction(requestRow,transactionRowNum);
		// Request refund for captured transaction
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();
		refundTransaction.SearchTransaction(txnId, SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(amount);
		testConfig.putRunTimeProperty("UseStoredDetails", "0");

		Assert.assertTrue(testConfig.getTestResult());
	}


	//10,11,12,13-Test.MerchantPanel.Transactions.TestRequestCancel.test_VerifyAuthTransaction & VerifyFirstRowexportExcel & test_VerifyCITITransaction & test_VerifyCancelDownloadSummary
	@Test(description = "Verify credit card transaction through CITI gateway on Dashboard", dataProvider = "GetTestConfig", timeOut = 3000000, groups = "LiveCardTC")
	public void test_VerifyAuthTransaction_ExportToExcel_CancelThroughDashboard_DownloadSummary(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// CC
		int transactionRowNum = 2;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;
		String amount = "2.00";	
		int PGInfoRow = 9;

		//***10-Test.MerchantPanel.Transactions.TestRequestCancel.test_VerifyAuthTransaction
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.BankRedirectPage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		String txnId = testConfig.getRunTimeProperty("transactionId");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		// Capture transaction
		CancelPage cancelPage = dashBoard.ClickCancel();
		cancelPage.VerifyAuthtransaction(paymentTypeRowNum,
				transactionRowNum, PGInfoRow, amount, helper);

		//***11-Test.MerchantPanel.Transactions.TestRequestCancel.VerifyFirstRowexportExcel***
		int excelRowNum= 1;	
		//testConfig.putRunTimeProperty("merchant_id", "2");
		cancelPage=dashBoard.ClickCancel();
		dashBoard.ClickExportExcelButton();
		Browser.wait(testConfig, 20);
		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);
		Helper.compareEquals(testConfig, "Customer name in excel", cancelPage.getCustomerName(),String.valueOf(tr.GetData(excelRowNum,"Customer Name")));
		Helper.compareEquals(testConfig, "PayU id in excel", cancelPage.getPayuId(),String.valueOf(tr.GetData(excelRowNum,"PayU ID")));
		Helper.compareEquals(testConfig, "Amount in excel", cancelPage.getAmount(),String.valueOf(tr.GetData(excelRowNum,"Amount")));
		Helper.compareEquals(testConfig, "Status in excel", cancelPage.getStatus(),String.valueOf(tr.GetData(excelRowNum,"Status")));
		Helper.compareEquals(testConfig, "Bank name in excel", cancelPage.getBankname(),String.valueOf(tr.GetData(excelRowNum,"Bank Name")));
		Helper.compareEquals(testConfig, "req amount in excel", cancelPage.getReqamount(),String.valueOf(tr.GetData(excelRowNum,"Requested Amount")));
		Helper.compareEquals(testConfig, "discount", cancelPage.getDiscount(),String.valueOf(tr.GetData(excelRowNum,"Discount")));
		Helper.compareEquals(testConfig, "merchant name in excel", cancelPage.getMerchantname(),String.valueOf(tr.GetData(excelRowNum,"Merchant Name")));
		Helper.compareEquals(testConfig, "transaction id", cancelPage.getTxnId(),String.valueOf(tr.GetData(excelRowNum,"Transaction ID")));
		Helper.compareEquals(testConfig, "Payment gateway", cancelPage.getPG(),String.valueOf(tr.GetData(excelRowNum,"Payment Gateway")));
		Helper.compareEquals(testConfig, "last name", cancelPage.getLastname(),String.valueOf(tr.GetData(excelRowNum,"Last name")));
		Helper.compareEquals(testConfig, "cistomer email", cancelPage.getEmail(),String.valueOf(tr.GetData(excelRowNum,"Customer Email")));
		Helper.compareEquals(testConfig, "Ip adderess", cancelPage.getIPaddress(),String.valueOf(tr.GetData(excelRowNum,"Customer IP Address")));
		Helper.compareEquals(testConfig, "Card number", cancelPage.getcardNo(),String.valueOf(tr.GetData(excelRowNum,"Card Number")));
		Helper.compareEquals(testConfig, "Payment type in excel", cancelPage.getField2(),String.valueOf(tr.GetData(excelRowNum,"Field 2")));
		Helper.compareEquals(testConfig, "Field 2 in excel", cancelPage.getPaymenttype(),String.valueOf(tr.GetData(excelRowNum,"Payment Type")));
		Helper.compareEquals(testConfig, "PGMID in excel", cancelPage.getPGMID(),String.valueOf(tr.GetData(excelRowNum,"PG MID")));
		Helper.compareEquals(testConfig, "Offer fail reason", cancelPage.getOfferfailReason(),String.valueOf(tr.GetData(excelRowNum,"Offer Failure Reason")));
		Helper.compareEquals(testConfig, "offer type", cancelPage.getofferType(),String.valueOf(tr.GetData(excelRowNum,"Offer Type")));
		Helper.compareEquals(testConfig, "offer key", cancelPage.getOfferKey(),String.valueOf(tr.GetData(excelRowNum,"Offer Key")));
		Helper.compareEquals(testConfig, "error code", cancelPage.getErrorcode(),String.valueOf(tr.GetData(excelRowNum,"Error Code")));

		//***12-Test.MerchantPanel.Transactions.TestRequestCancel.test_VerifyCancelDownloadSummary***
		
		CancelPage cancelTransaction = dashBoard.ClickCancel();

		// Request refund
		cancelTransaction.SearchTransaction(txnId, SearchOn.TransactionId);
		cancelTransaction.CancelFirstTransaction();
		cancelTransaction.RefundFailureTransaction();
		String refId = testConfig.getRunTimeProperty("token");
		cancelTransaction.downloadCancelSummary(refId);

		// verify failure data in excel sheet
		cancelTransaction.verifyRefundFailureExcelSheet(refId);

		//***13-Test.MerchantPanel.Transactions.TestRequestCancel.test_VerifyCITITransaction****
		int requestRowNum = 7;
		amount = "2.00";
		PGInfoRow = 9;

		// View cancelled transaction on request page
		cancelTransaction = dashBoard.ClickCancel();
		RequestsPage request = dashBoard.ClickViewRequest();
		TransactionFilterPage filter = new TransactionFilterPage(testConfig);
		filter.SearchTransaction(txnId, SearchOn.TransactionId);

		// Verify cancelled transaction on request page
		request.VerifyRefundRequest(requestRowNum, paymentTypeRowNum,transactionRowNum, PGInfoRow, amount, helper);
		// Verify cancelled transaction details from Database
		cancelTransaction.VerifyCancelTransaction(requestRowNum,transactionRowNum);
		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		Assert.assertTrue(testConfig.getTestResult());
	}	

	// 14-Test.MerchantPanel.Transactions.TestRequestCancelRefund.test_VerifyAuthTransaction
	@Test(description = "Verify cancel ransaction on Cancel/Refund Page", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void test_VerifyAuthTransactionforCancelOrRefund(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// CC
		int transactionRowNum = 2;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;
		int requestRowNum = 3;
		String amount = "2.00";
		int PGInfoRow = 9;

		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.BankRedirectPage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		String txnId = testConfig.getRunTimeProperty("transactionId");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		CancelRefundPage cancelRefundTransaction = dashBoard
				.ClickCancelRefund();

		// Request cancel/refund
		cancelRefundTransaction
		.SearchTransaction(txnId, SearchOn.TransactionId);
		cancelRefundTransaction.RefundFirstTransaction();

		// View refunded transaction on request page
		RequestsPage request = dashBoard.ClickViewRequest();
		TransactionFilterPage filter = new TransactionFilterPage(testConfig);
		filter.SearchTransaction(txnId, SearchOn.TransactionId);

		request.VerifyRefundRequest(requestRowNum, paymentTypeRowNum,
				transactionRowNum, PGInfoRow, amount, helper);

		// Verify Refunded transaction details from Database
		cancelRefundTransaction.VerifyCancelRefundTransaction(requestRowNum,
				transactionRowNum);

		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		Assert.assertTrue(testConfig.getTestResult());
	}


	//15-Test.AdminPanel.Payments.TestTransactionCC.test_CC_ICICITRAVEL
	@Test(description = "Verify credit card transaction through ICICITRAVEL gateway",dataProvider="GetTestConfig", timeOut=600000, groups="LiveCardTC")
	public void test_CC_ICICITRAVEL(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CC
		int transactionRowNum = 6;
		int paymentTypeRowNum = 6;
		int cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		/*TODO uncomment this code when ICICI Travel starts working on PP with actual card details 
		//Call refund_transaction using mihpayid, token and amount
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		String actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(helper.home, testConfig.getRunTimeProperty("merchantKey"),testConfig.getRunTimeProperty("mihpayid"), helper.transactionData.GetData(transactionRowNum, "amount"));
		wshelper.refund_transaction_verify(actualWebServiceResponse, helper.testResponse.actualResponse, "1","Refund Request Queued","", "102");

		testConfig.putRunTimeProperty("UseStoredDetails", "0");*/
		Assert.assertTrue(testConfig.getTestResult());
	}


	// 16-Test.AdminPanel.Payments.TestTransactionCC.test_CC_SBIPG
	@Test(description = "Verify credit card transaction through SBIPG gateway", dataProvider = "GetTestConfig", timeOut = 800000, groups = "LiveCardTC")
	public void test_CC_SBIPG(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// CC
		int transactionRowNum = 5;
		int paymentTypeRowNum = 7;
		int cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.BankRedirectPage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// Call cancel_refund_transaction using mihpayid, token and amount
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		String actualWebServiceResponse = wshelper
				.cancelrefund_transaction_executeAdminPanel(helper.home,
						testConfig.getRunTimeProperty("merchantKey"),
						testConfig.getRunTimeProperty("mihpayid"),
						helper.transactionData.GetData(transactionRowNum,
								"amount"));
		wshelper.refund_transaction_verify(actualWebServiceResponse,
				helper.testResponse.actualResponse, "1",
				"Refund Request Queued", "", "102");
		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		testConfig.logFail("**Redmine ID for this bug is 22192**");
		Assert.assertTrue(testConfig.getTestResult());
	}

	// 17-Test.AdminPanel.Payments.TestTransactionDC.test_DC_CITI
	@Test(description = "Verify debit card transaction through CITI", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void test_DC_CITI(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// Call refund_transaction using mihpayid, token and amount
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		String actualWebServiceResponse = wshelper
				.cancelrefund_transaction_executeAdminPanel(helper.home,
						testConfig.getRunTimeProperty("merchantKey"),
						testConfig.getRunTimeProperty("mihpayid"),
						testConfig.getRunTimeProperty("amount"));
		wshelper.cancel_transaction_verify(actualWebServiceResponse,
				helper.testResponse.actualResponse, "1",
				"Cancel Request Queued", "", "102");
		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		testConfig.removeRunTimeProperty("amount");

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify debit card transaction through CITI with additional charge", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void test_DC_CITI_CF(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// CITI credit Card
		int transactionRowNum = 2;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("amount", "1.00");
		testConfig.putRunTimeProperty("additionalCharges", "CC:1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		String amnt = helper.testResponse.actualResponse.get("amount");
		double amount = Double.parseDouble(amnt);
		
		String addCharge = helper.testResponse.actualResponse.get("additionalCharges");
		double additionalCharges = Double.parseDouble(addCharge);
		amount = amount+additionalCharges;	
		String transactionAmount = String.valueOf(amount)+"0";
		
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);
		
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);
		

		// Call refund_transaction using mihpayid, token and amount
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		String actualWebServiceResponse = wshelper
				.cancelrefund_transaction_executeAdminPanel(helper.home,
						testConfig.getRunTimeProperty("merchantKey"),
						testConfig.getRunTimeProperty("mihpayid"),
						testConfig.getRunTimeProperty("amount"));
		wshelper.cancel_transaction_verify(actualWebServiceResponse,
				helper.testResponse.actualResponse, "1",
				"Cancel Request Queued", "", "102");
		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		testConfig.removeRunTimeProperty("amount");

		Assert.assertTrue(testConfig.getTestResult());
	}

	
	// 18-Test.AdminPanel.Payments.TestTransactionDC.test_DC_SBIPG
	@Test(description = "Verify debit card transaction through SBIPG", dataProvider = "GetTestConfig", timeOut = 800000, groups = "LiveCardTC")
	public void test_DC_SBIPG(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Visa Debit Cards
		int transactionRowNum = 5;
		int paymentTypeRowNum = 39;
		int cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// Call cancel_refund_transaction using mihpayid, token and amount
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		String actualWebServiceResponse = wshelper
				.cancelrefund_transaction_executeAdminPanel(helper.home,
						testConfig.getRunTimeProperty("merchantKey"),
						testConfig.getRunTimeProperty("mihpayid"),
						helper.transactionData.GetData(transactionRowNum,
								"amount"));
		wshelper.refund_transaction_verify(actualWebServiceResponse,
				helper.testResponse.actualResponse, "1",
				"Refund Request Queued", "", "102");
		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		testConfig.logFail("**Redmine ID for this bug is 22192**");
		Assert.assertTrue(testConfig.getTestResult());
	}

	// 19-Test.AdminPanel.WebServices.TestWebServicesUI.test_RC_Auth
	@Test(description = "Test Race Condition for auth transaction with multiple partial cancel, where amount exceeds the range", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void test_RC_Auth(Config testConfig) {
		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();

		// Do a seamless CITI transaction
		int transactionRowNum = 2;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper
				.DoTestTransaction(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum,
						ExpectedResponsePage.BankRedirectPage);
		tranhelper.testResponse.VerifyTransactionResponse(
				tranhelper.transactionData, transactionRowNum,
				tranhelper.paymentTypeData, paymentTypeRowNum);

		Hashtable<String, String> expectedTransactionResponse = tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData
				.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		// Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);

		// Call full cancel_transaction using mihpayid, token and amount
		String actualWebServiceResponse = wshelper
				.cancel_transaction_executeAdminPanel(tranhelper.home,
						merchantKey,
						expectedTransactionResponse.get("mihpayid"), "2");
		wshelper.cancel_transaction_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1", "Cancelled Request Queued",
				"", "102");

		// Call check_action_status using txn_update_id
		actualWebServiceResponse = wshelper
				.check_action_status_cancel_executeAdminPanel(tranhelper.home,
						merchantKey,
						testConfig.getRunTimeProperty("txn_update_id"));
		wshelper.check_action_status_cancel_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"1 out of 1 Transactions Fetched Successfully", "cancel", "",
				"queued", "2.00", "0.00");

		// Call race condition refund_transaction using mihpayid, token and
		// amount for race condition
		actualWebServiceResponse = wshelper
				.cancel_transaction_executeAdminPanel(tranhelper.home,
						merchantKey,
						expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.cancel_transaction_verify(actualWebServiceResponse,
				expectedTransactionResponse, "0", "Cancelled failed", "", "112");

		// Call check_action_status using request_id
		actualWebServiceResponse = wshelper
				.check_action_status_executeAdminPanel(tranhelper.home,
						merchantKey,
						testConfig.getRunTimeProperty("txn_update_id"));
		wshelper.check_action_status_cancel_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"1 out of 1 Transactions Fetched Successfully", "cancel", "",
				"queued", "1.00", "0.00");

		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		Assert.assertTrue(testConfig.getTestResult());
	}

	// 20-Test.AdminPanel.WebServices.TestWebServicesUI.test_RC_Auth_CR
	@Test(description = "Test Race Condition for successful auth transaction with multiple partial cancel/refund, where amount exceeds the range", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void test_RC_Auth_CR(Config testConfig) {

		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();

		// Do a successful auth transaction
		int transactionRowNum = 2;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper
				.DoTestTransaction(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum,
						ExpectedResponsePage.BankRedirectPage);
		tranhelper.testResponse.VerifyTransactionResponse(
				tranhelper.transactionData, transactionRowNum,
				tranhelper.paymentTypeData, paymentTypeRowNum);

		Hashtable<String, String> expectedTransactionResponse = tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData
				.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		// Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);

		// Call check_payment using mihpayid
		String actualWebServiceResponse = wshelper
				.check_payment_executeAdminPanel(tranhelper.home, merchantKey,
						expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"Transaction Fetched Successfully", "", "",
				"0000-00-00 00:00:00");

		// Call check_action_status using request_id
		actualWebServiceResponse = wshelper
				.check_action_status_executeAdminPanel(tranhelper.home,
						merchantKey,
						testConfig.getRunTimeProperty("request_id"));
		wshelper.check_action_status_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"1 out of 1 Transactions Fetched Successfully", "auth", "",
				"SUCCESS", "2.00", "0.00");

		// Call partial cancel_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper
				.cancelrefund_transaction_executeAdminPanel(tranhelper.home,
						merchantKey,
						expectedTransactionResponse.get("mihpayid"), "2");
		wshelper.cancel_transaction_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1", "Cancel Request Queued", "",
				"102");

		// Call check_action_status using request_id
		actualWebServiceResponse = wshelper
				.check_action_status_executeAdminPanel(tranhelper.home,
						merchantKey,
						testConfig.getRunTimeProperty("request_id"));
		wshelper.check_action_status_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"1 out of 1 Transactions Fetched Successfully", "auth", "",
				"SUCCESS", "2.00", "0.00");

		// Call partial refund_transaction using mihpayid, token and amount for
		// race condition
		actualWebServiceResponse = wshelper
				.cancel_transaction_executeAdminPanel(tranhelper.home,
						merchantKey,
						expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse,
				expectedTransactionResponse, "0", "Cancelled failed", "", "112");

		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		Assert.assertTrue(testConfig.getTestResult());

	}

	// 21-Test.AdminPanel.WebServices.TestWebServicesUI.test_WS_CITI_Cancel
	@Test(description = "Test webservices - CITI Cancel", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void test_WS_CITI_Cancel(Config testConfig) {
		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();

		// Do a seamless CITI transaction
		int transactionRowNum = 2;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper
				.DoTestTransaction(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum,
						ExpectedResponsePage.BankRedirectPage);
		tranhelper.testResponse.VerifyTransactionResponse(
				tranhelper.transactionData, transactionRowNum,
				tranhelper.paymentTypeData, paymentTypeRowNum);

		Hashtable<String, String> expectedTransactionResponse = tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData
				.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		// Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);

		// Call refund_transaction using mihpayid, token and amount
		String actualWebServiceResponse = wshelper
				.refund_transaction_executeAdminPanel(tranhelper.home,
						merchantKey,
						expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.refund_transaction_verify(actualWebServiceResponse,
				expectedTransactionResponse, "0", "Refund failed", null, "111");

		// Call check_payment using mihpayid
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(
				tranhelper.home, merchantKey,
				expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"Transaction Fetched Successfully", "", "",
				"0000-00-00 00:00:00");

		// Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(
				tranhelper.home, merchantKey,
				testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"1 out of 1 Transactions Fetched Successfully", "",
				"0000-00-00 00:00:00");

		// Call get_payment_status using txnid
		actualWebServiceResponse = wshelper
				.get_payment_status_executeAdminPanel(tranhelper.home,
						merchantKey,
						testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"1 out of 1 Transactions Fetched Successfully", "",
				"0000-00-00 00:00:00");

		// Call partial cancel_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper
				.cancel_transaction_executeAdminPanel(tranhelper.home,
						merchantKey,
						expectedTransactionResponse.get("mihpayid"), "0.9");
		wshelper.cancel_transaction_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1", "Cancelled Request Queued",
				"", "102");

		// Call check_action_status using txn_update_id
		actualWebServiceResponse = wshelper
				.check_action_status_cancel_executeAdminPanel(tranhelper.home,
						merchantKey,
						testConfig.getRunTimeProperty("txn_update_id"));

		while (actualWebServiceResponse.contains("queued")) {
			Browser.wait(testConfig, 70);
			actualWebServiceResponse = wshelper
					.check_action_status_cancel_executeAdminPanel(
							tranhelper.home, merchantKey,
							testConfig.getRunTimeProperty("txn_update_id"));
		}
		wshelper.check_action_status_cancel_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"1 out of 1 Transactions Fetched Successfully", "cancel", "",
				"success", "0.9", "0.00");

		// Call full cancel_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper
				.cancelrefund_transaction_executeAdminPanel(tranhelper.home,
						merchantKey,
						expectedTransactionResponse.get("mihpayid"), "1.1");
		wshelper.cancel_transaction_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1", "Cancel Request Queued", "",
				"102");

		// Call check_action_status using txn_update_id
		actualWebServiceResponse = wshelper
				.check_action_status_cancel_executeAdminPanel(tranhelper.home,
						merchantKey,
						testConfig.getRunTimeProperty("txn_update_id"));

		while (actualWebServiceResponse.contains("queued")) {
			Browser.wait(testConfig, 60);
			actualWebServiceResponse = wshelper
					.check_action_status_executeAdminPanel(tranhelper.home,
							merchantKey,
							testConfig.getRunTimeProperty("txn_update_id"));
		}
		wshelper.check_action_status_cancel_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"1 out of 1 Transactions Fetched Successfully", "cancel", "",
				"success", "1.10", "0.00");

		// Call refund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper
				.refund_transaction_executeAdminPanel(tranhelper.home,
						merchantKey,
						expectedTransactionResponse.get("mihpayid"), "1.1");
		wshelper.refund_transaction_verify(actualWebServiceResponse,
				expectedTransactionResponse, "0", "Refund failed", null, "111");

		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		Assert.assertTrue(testConfig.getTestResult());
	}

	// 22-Test.AdminPanel.WebServices.TestWebServicesUI.test_WS_CITI_Capture
	@Test(description = "Test webservices - CITI Capture", dataProvider = "GetTestConfig", timeOut = 600000, groups = "LiveCardTC")
	public void test_WS_CITI_Capture(Config testConfig) {
		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();

		// Do a CITI transaction
		int transactionRowNum = 2;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper
				.DoTestTransaction(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum,
						ExpectedResponsePage.BankRedirectPage);
		tranhelper.testResponse.VerifyTransactionResponse(
				tranhelper.transactionData, transactionRowNum,
				tranhelper.paymentTypeData, paymentTypeRowNum);

		Hashtable<String, String> expectedTransactionResponse = tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData
				.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		// Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);

		// Call refund_transaction using mihpayid, token and amount
		String actualWebServiceResponse = wshelper
				.refund_transaction_executeAdminPanel(tranhelper.home,
						merchantKey,
						expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.refund_transaction_verify(actualWebServiceResponse,
				expectedTransactionResponse, "0", "Refund failed", null, "111");

		// Call check_payment using mihpayid
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(
				tranhelper.home, merchantKey,
				expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"Transaction Fetched Successfully", "", "",
				"0000-00-00 00:00:00");

		// Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(
				tranhelper.home, merchantKey,
				testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"1 out of 1 Transactions Fetched Successfully", "",
				"0000-00-00 00:00:00");

		// Call get_payment_status using txnid
		actualWebServiceResponse = wshelper
				.get_payment_status_executeAdminPanel(tranhelper.home,
						merchantKey,
						testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"1 out of 1 Transactions Fetched Successfully", "",
				"0000-00-00 00:00:00");

		// Call capture_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper
				.capture_transaction_executeAdminPanel(tranhelper.home,
						merchantKey,
						expectedTransactionResponse.get("mihpayid"));
		wshelper.capture_transaction_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1", "Capture Request Queued", "",
				"102");

		// Call check_action_status using request_id
		actualWebServiceResponse = wshelper
				.check_action_status_executeAdminPanel(tranhelper.home,
						merchantKey,
						testConfig.getRunTimeProperty("request_id"));

		while (actualWebServiceResponse.contains("QUEUED")) {
			Browser.wait(testConfig, 60);
			actualWebServiceResponse = wshelper
					.check_action_status_executeAdminPanel(tranhelper.home,
							merchantKey,
							testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse,
				expectedTransactionResponse, "1",
				"1 out of 1 Transactions Fetched Successfully", "capture", "",
				"success", "2.00", "0.00");

		// Call refund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper
				.refund_transaction_executeAdminPanel(tranhelper.home,
						merchantKey,
						expectedTransactionResponse.get("mihpayid"), "2");
		wshelper.refund_transaction_verify(
				actualWebServiceResponse,
				expectedTransactionResponse,
				"1",
				"Capture is done today, please check for  refund status tomorrow",
				"", "226");

		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		Assert.assertTrue(testConfig.getTestResult());
	}

}
//