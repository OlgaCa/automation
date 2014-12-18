package Test.MerchantPanel.Transactions;


import java.io.File;
import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Transactions.CapturePage;
import PageObject.MerchantPanel.Transactions.RefundPage;
import PageObject.MerchantPanel.Transactions.RequestsPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage.SearchOn;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;


public class TestRequestRefund extends TestBase{


	@Test(description = "Verify Full refund transaction", 
			dataProvider="GetRequestRefundTestConfig", timeOut=600000, groups="1")
	public void test_VerifyCapturedTransaction(Config testConfig, String[] testType)
	{
		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();

		testConfig.putRunTimeProperty("amount", "1.00");
		int transactionRowNum = 1;
		int paymentTypeRowNum =17;
		int cardDetailsRowNum = 1;
		int requestDetailRowNum=1;
		int PGinfoRowNum=3;

		tranHelper.GetTestTransactionPage();

		String txnId=testConfig.getRunTimeProperty("transactionId");
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		String amount = tranHelper.testResponse.actualResponse.get("amount");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request refund 
		refundTransaction.SearchTransaction(txnId, SearchOn.TransactionId);
		refundTransaction.fullRefundFirstTransaction();

		//View refunded transaction on request page			
		RequestsPage request= dashBoard.ClickViewRequest();
		TransactionFilterPage filter= new TransactionFilterPage(testConfig);
		filter.SearchTransaction(txnId, SearchOn.TransactionId);

		request.VerifyRefundRequest(requestDetailRowNum,paymentTypeRowNum,transactionRowNum,PGinfoRowNum,amount,tranHelper);

		//Verify Refunded transaction details from Database
		refundTransaction.VerifyRefundTransaction(transactionRowNum,requestDetailRowNum,amount,"false");

		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Verify Partial refund transaction", 
			dataProvider="GetRequestRefundTestConfig", timeOut=600000, groups="1")
	public void test_VerifyPartialRefundTransaction(Config testConfig, String[] testType)
	{
		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();

		testConfig.putRunTimeProperty("amount", "1.00");
		int transactionRowNum = 1;
		int paymentTypeRowNum =17;
		int cardDetailsRowNum = 1;
		int requestDetailRowNum=1;
		int PGinfoRowNum=3;

		tranHelper.GetTestTransactionPage();

		String txnId=testConfig.getRunTimeProperty("transactionId");
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		String amount = "0.80";

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request refund 
		refundTransaction.SearchTransaction(txnId, SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(amount);

		//View refunded transaction on request page			
		RequestsPage request= dashBoard.ClickViewRequest();
		TransactionFilterPage filter= new TransactionFilterPage(testConfig);
		filter.SearchTransaction(txnId, SearchOn.TransactionId);

		request.VerifyRefundRequest(requestDetailRowNum,paymentTypeRowNum,transactionRowNum,PGinfoRowNum,amount,tranHelper);

		//Verify Refunded transaction details from Database
		refundTransaction.VerifyRefundTransaction(transactionRowNum,requestDetailRowNum,amount,"false");

		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Verify refund request popup for already refund transaction", 
			dataProvider="GetRequestRefundTestConfig", timeOut=600000, groups="1")
	public void test_VerifyrefundTransaction(Config testConfig, String[] testType)
	{

		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum =17;
		int cardDetailsRowNum = 1;

		tranHelper.GetTestTransactionPage();

		String txnId=testConfig.getRunTimeProperty("transactionId");
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request refund 
		refundTransaction.SearchTransaction(txnId, SearchOn.TransactionId);

		refundTransaction.fullRefundFirstTransaction();

		String popUp= refundTransaction.getPopupText();
		Helper.compareContains(testConfig, "Pop up message", "There already exists refund request(s) for following transaction ids with same corresponding amount.", popUp);

		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify refund request for higher amount than actual amount", 
			dataProvider="GetRequestRefundTestConfig", timeOut=600000, groups="1")
	public void test_VerifyfaluireRefundTransaction(Config testConfig, String[] testType)
	{

		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum =17;
		int cardDetailsRowNum = 1;
		int requestDetailRowNum= 4;
		String amount="1000.00";  

		tranHelper.GetTestTransactionPage();

		String txnId=testConfig.getRunTimeProperty("transactionId");
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request refund 
		refundTransaction.SearchTransaction(txnId, SearchOn.TransactionId);

		refundTransaction.RefundFirstTransaction(amount);

		CapturePage captureTxn= dashBoard.ClickRequestCapture();
		Browser.wait(testConfig, 60);
		captureTxn.VerifyCaptureTransaction(requestDetailRowNum, transactionRowNum);


		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify refund request for zero amount and verify message in download excel", 
			dataProvider="GetRequestRefundTestConfig", timeOut=600000, groups="1")
	public void test_VerifyRefundDownloadSummary(Config testConfig, String[] testType) 
	{

		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum =17;
		int cardDetailsRowNum = 1;
		String amount="0.00";  

		tranHelper.GetTestTransactionPage();

		String txnId=testConfig.getRunTimeProperty("transactionId");
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request refund 
		refundTransaction.SearchTransaction(txnId, SearchOn.TransactionId);
		testConfig.putRunTimeProperty("failureAmount", amount);

		//refundTransaction.RefundFirstTransaction(amount);
		refundTransaction.RefundFirstTransaction(amount);
		String refId = testConfig.getRunTimeProperty("token");
		refundTransaction.downloadRefundSummary(refId);	

		// verify failure data in excel sheet
		refundTransaction.verifyRefundFailureExcelSheet(refId);

		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify count of refunded status transaction with date range filter", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void refundedStatusFilters(Config testConfig)  {

		int transactionRowNum = 1;
		int FilterRow = 22;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");
		testConfig.putRunTimeProperty("from",Helper.changeDateFormat(dataSheet.GetData(FilterRow,"date_from")));
		testConfig.putRunTimeProperty("to",Helper.changeDateFormat(dataSheet.GetData(FilterRow,"date_to")));

		testConfig.putRunTimeProperty("merchant_id", "5910");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		refundTransaction.verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Verify count of refunded status transaction with amount filter", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void refundedStatusAmountFilters(Config testConfig)  {

		int transactionRowNum = 1;
		int FilterRow = 23;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");
		testConfig.putRunTimeProperty("min_amount",
				dataSheet.GetData(FilterRow, "min_amount"));
		testConfig.putRunTimeProperty("max_amount",
				dataSheet.GetData(FilterRow, "max_amount"));


		testConfig.putRunTimeProperty("merchant_id", "5910");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		refundTransaction.verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Verify Export to excel exports all in progress transaction details of the week by default", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void exportExcelbyWeek(Config testConfig)   {

		int transactionRowNum = 1;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);	
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickRequestRefund();
		dashBoard.ClickExportExcelButton();
		Browser.wait(testConfig,30);
		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);
		Helper.compareEquals(testConfig, "result count of transaction in excel", dashBoard.transactionno(),String.valueOf(tr.getRecordsNum()-1));	
		Assert.assertTrue(testConfig.getTestResult());

	}


	@Test(description = "Verify Export to excel exports all in progress transaction details by date when count is less than 1000", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void exportExcelbyDateFilter(Config testConfig)   {

		int transactionRowNum = 1;
		int FilterRow = 18;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickRequestRefund();

		dashBoard.Filldateinputfrom(dataSheet.GetData(FilterRow,"date_from"));
		dashBoard.Filldateinputto(dataSheet.GetData(FilterRow, "date_to"));
		dashBoard.apply();		
		dashBoard.ClickExportExcelButton();
		Browser.wait(testConfig, 20);
		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);
		Helper.compareEquals(testConfig, "result count of transaction in excel", dashBoard.transactionno(),String.valueOf(tr.getRecordsNum()-1));	
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify Export to excel exports all transactions details when count is less than 1000", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void VerifyFirstRowexportExcel(Config testConfig)   {

		int transactionRowNum = 1;
		int excelRowNum= 1;

		//testConfig.putRunTimeProperty("merchant_id", "2");
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundPage=dashBoard.ClickRequestRefund();


		dashBoard.ClickExportExcelButton();
		Browser.wait(testConfig, 20);
		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);


		Helper.compareEquals(testConfig, "Customer name in excel", refundPage.getCustomerName(),String.valueOf(tr.GetData(excelRowNum,"Customer Name")));
		Helper.compareEquals(testConfig, "PayU id in excel", refundPage.getPayuId(),String.valueOf(tr.GetData(excelRowNum,"PayU ID")));
		Helper.compareEquals(testConfig, "Amount in excel", refundPage.getAmount(),String.valueOf(tr.GetData(excelRowNum,"Amount")));
		Helper.compareEquals(testConfig, "Status in excel", refundPage.getStatus(),String.valueOf(tr.GetData(excelRowNum,"Status")));
		Helper.compareEquals(testConfig, "Bank name in excel", refundPage.getBankname(),String.valueOf(tr.GetData(excelRowNum,"Bank Name")));
		Helper.compareEquals(testConfig, "req amount in excel", refundPage.getReqamount(),String.valueOf(tr.GetData(excelRowNum,"Requested Amount")));
		Helper.compareEquals(testConfig, "discount", refundPage.getDiscount(),String.valueOf(tr.GetData(excelRowNum,"Discount")));
		Helper.compareEquals(testConfig, "merchant name in excel", refundPage.getMerchantname(),String.valueOf(tr.GetData(excelRowNum,"Merchant Name")));
		Helper.compareEquals(testConfig, "transaction id", refundPage.getTxnId(),String.valueOf(tr.GetData(excelRowNum,"Transaction ID")));
		Helper.compareEquals(testConfig, "Payment gateway", refundPage.getPG(),String.valueOf(tr.GetData(excelRowNum,"Payment Gateway")));
		Helper.compareEquals(testConfig, "last name", refundPage.getLastname(),String.valueOf(tr.GetData(excelRowNum,"Last name")));
		Helper.compareEquals(testConfig, "cistomer email", refundPage.getEmail(),String.valueOf(tr.GetData(excelRowNum,"Customer Email")));
		Helper.compareEquals(testConfig, "Ip adderess", refundPage.getIPaddress(),String.valueOf(tr.GetData(excelRowNum,"Customer IP Address")));
		Helper.compareEquals(testConfig, "Card number", refundPage.getcardNo(),String.valueOf(tr.GetData(excelRowNum,"Card Number")));
		Helper.compareEquals(testConfig, "Payment type in excel", refundPage.getField2(),String.valueOf(tr.GetData(excelRowNum,"Field 2")));
		Helper.compareEquals(testConfig, "Field 2 in excel", refundPage.getPaymenttype(),String.valueOf(tr.GetData(excelRowNum,"Payment Type")));
		Helper.compareEquals(testConfig, "PGMID in excel", refundPage.getPGMID(),String.valueOf(tr.GetData(excelRowNum,"PG MID")));
		Helper.compareEquals(testConfig, "Offer fail reason", refundPage.getOfferfailReason(),String.valueOf(tr.GetData(excelRowNum,"Offer Failure Reason")));
		Helper.compareEquals(testConfig, "offer type", refundPage.getofferType(),String.valueOf(tr.GetData(excelRowNum,"Offer Type")));
		Helper.compareEquals(testConfig, "offer key", refundPage.getOfferKey(),String.valueOf(tr.GetData(excelRowNum,"Offer Key")));
		Helper.compareEquals(testConfig, "error code", refundPage.getErrorcode(),String.valueOf(tr.GetData(excelRowNum,"Error Code")));
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify captured transaction on refund page", 
			dataProvider="GetRequestRefundTestConfig", timeOut=600000, groups="1")
	public void test_VerifyTransactionRefund(Config testConfig, String[] testType)
	{
		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();

		testConfig.putRunTimeProperty("amount", "1.00");
		int transactionRowNum = 1;
		int paymentTypeRowNum =17;
		int cardDetailsRowNum = 1;
		int PGinfoRowNum=3;

		tranHelper.GetTestTransactionPage();

		String txnId=testConfig.getRunTimeProperty("transactionId");
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		String amount = tranHelper.testResponse.actualResponse.get("amount");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request refund 
		refundTransaction.SearchTransaction(txnId, SearchOn.TransactionId);
		refundTransaction.VerifyRefundRequest(paymentTypeRowNum, transactionRowNum, PGinfoRowNum, amount, tranHelper);

		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Verify count of CC payment transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void ccPaymentFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 26;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		refundTransaction.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	@Test(description = "Verify count of DC payment transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void DCPaymentFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 27;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		refundTransaction.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Verify count of NB payment transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void NBPaymentFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 28;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		refundTransaction.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Verify count of EMI payment transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void EMIPaymentFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 29;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		refundTransaction.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Verify count of COD payment transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void CODPaymentFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 30;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		refundTransaction.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	
}


