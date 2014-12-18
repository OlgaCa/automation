package Test.MerchantPanel.Transactions;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.NewResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage.SearchOn;
import PageObject.MerchantPanel.Transactions.ViewInvoicesPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;


public class TestViewInvoices extends TestBase{
	
	 
	@Test(description = "Verify email invoice transaction on view invoices page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyEmailInvoiceTransactionSuccess(Config testConfig) {
		
		int transactionRowNum = 12;
		int cardDetailsRowNum = 1;
		int PGinfoRow= 3;
		int paymentTypeRow= 17;
		
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
		
		Assert.assertTrue(testConfig.getTestResult());
		
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		ViewInvoicesPage viewInvoice= dashBoard.ClickViewInvoices();
		viewInvoice.SearchTransaction(txnId, SearchOn.TransactionId);
		viewInvoice.VerifyinvoiceTransaction(paymentTypeRow, transactionRowNum, PGinfoRow, helper);
		Assert.assertTrue(testConfig.getTestResult());
			}
	
	@Test(description = "Verify count of invoice transaction with date range filter", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void invoiceDateFilters(Config testConfig)  {

		int transactionRowNum = 1;
		int FilterRow = 31;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");
		testConfig.putRunTimeProperty("from",Helper.changeDateFormat(dataSheet.GetData(FilterRow,"date_from")));
		testConfig.putRunTimeProperty("to",Helper.changeDateFormat(dataSheet.GetData(FilterRow,"date_to")));

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);
		ViewInvoicesPage viewInvoice= dashBoard.ClickViewInvoices();

		viewInvoice.verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Verify count of refunded status transaction with amount filter", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void invoiceStatusAmountFilters(Config testConfig)  {

		int transactionRowNum = 1;
		int FilterRow = 32;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");
		testConfig.putRunTimeProperty("min_amount",
				dataSheet.GetData(FilterRow, "min_amount"));
		testConfig.putRunTimeProperty("max_amount",
				dataSheet.GetData(FilterRow, "max_amount"));


		testConfig.putRunTimeProperty("merchant_id", "5910");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);
		ViewInvoicesPage viewInvoice= dashBoard.ClickViewInvoices();

		viewInvoice.verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());
		
	}
	@Test(description = "Verify count of CC invoice transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void ccPaymentFilters(Config testConfig)  {

		int transactionRowNum = 1;
		int FilterRow = 33;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5910");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		ViewInvoicesPage viewInvoice= dashBoard.ClickViewInvoices();

		viewInvoice.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	@Test(description = "Verify count of DC invoice transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void dcPaymentFilters(Config testConfig)  {

		int transactionRowNum = 1;
		int FilterRow = 34;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5910");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		ViewInvoicesPage viewInvoice= dashBoard.ClickViewInvoices();

		viewInvoice.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify count of NB invoice transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void nbPaymentFilters(Config testConfig)  {

		int transactionRowNum = 1;
		int FilterRow = 35;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5910");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		ViewInvoicesPage viewInvoice= dashBoard.ClickViewInvoices();

		viewInvoice.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify count of EMI invoice transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void emiPaymentFilters(Config testConfig)  {

		int transactionRowNum = 1;
		int FilterRow = 36;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5910");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		ViewInvoicesPage viewInvoice= dashBoard.ClickViewInvoices();

		viewInvoice.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify count of cod invoice transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void codPaymentFilters(Config testConfig)  {

		int transactionRowNum = 1;
		int FilterRow = 37;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5910");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		ViewInvoicesPage viewInvoice= dashBoard.ClickViewInvoices();

		viewInvoice.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	@Test(description = "Verify Export to excel exports all in progress transaction details of the week by default", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void exportExcelbyWeek(Config testConfig)   {

		int transactionRowNum = 1;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);	
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewInvoices();
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
		dashBoard.ClickViewInvoices();

		dashBoard.Filldateinputfrom(dataSheet.GetData(FilterRow,"date_from"));
		dashBoard.Filldateinputto(dataSheet.GetData(FilterRow, "date_to"));
		dashBoard.FillCaptureddatefrom(dataSheet.GetData(FilterRow,"date_from"));
		dashBoard.FillCaptureddateto(dataSheet.GetData(FilterRow, "date_to"));
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
		ViewInvoicesPage invoice=dashBoard.ClickViewInvoices();


		dashBoard.ClickExportExcelButton();
		Browser.wait(testConfig, 20);
		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);


		Helper.compareEquals(testConfig, "Customer name in excel", invoice.getCustomerName(),String.valueOf(tr.GetData(excelRowNum,"Customer Name")));
		Helper.compareEquals(testConfig, "PayU id in excel", invoice.getPayuId(),String.valueOf(tr.GetData(excelRowNum,"PayU ID")));
		Helper.compareEquals(testConfig, "Amount in excel", invoice.getAmount(),String.valueOf(tr.GetData(excelRowNum,"Amount")));
		Helper.compareEquals(testConfig, "Status in excel", invoice.getStatus(),String.valueOf(tr.GetData(excelRowNum,"Status")));
		Helper.compareEquals(testConfig, "Bank name in excel", invoice.getBankname(),String.valueOf(tr.GetData(excelRowNum,"Bank Name")));
		Helper.compareEquals(testConfig, "req amount in excel", invoice.getReqamount(),String.valueOf(tr.GetData(excelRowNum,"Requested Amount")));
		Helper.compareEquals(testConfig, "discount", invoice.getDiscount(),String.valueOf(tr.GetData(excelRowNum,"Discount")));
		Helper.compareEquals(testConfig, "merchant name in excel", invoice.getMerchantname(),String.valueOf(tr.GetData(excelRowNum,"Merchant Name")));
		Helper.compareEquals(testConfig, "transaction id", invoice.getTxnId(),String.valueOf(tr.GetData(excelRowNum,"Transaction ID")));
		Helper.compareEquals(testConfig, "Payment gateway", invoice.getPG(),String.valueOf(tr.GetData(excelRowNum,"Payment Gateway")));
		Helper.compareEquals(testConfig, "last name", invoice.getLastname(),String.valueOf(tr.GetData(excelRowNum,"Last name")));
		Helper.compareEquals(testConfig, "cistomer email", invoice.getEmail(),String.valueOf(tr.GetData(excelRowNum,"Customer Email")));
		Helper.compareEquals(testConfig, "Ip adderess", invoice.getIPaddress(),String.valueOf(tr.GetData(excelRowNum,"Customer IP Address")));
		Helper.compareEquals(testConfig, "Card number", invoice.getcardNo(),String.valueOf(tr.GetData(excelRowNum,"Card Number")));
		Helper.compareEquals(testConfig, "Payment type in excel", invoice.getField2(),String.valueOf(tr.GetData(excelRowNum,"Field 2")));
		Helper.compareEquals(testConfig, "Field 2 in excel", invoice.getPaymenttype(),String.valueOf(tr.GetData(excelRowNum,"Payment Type")));
		Helper.compareEquals(testConfig, "PGMID in excel", invoice.getPGMID(),String.valueOf(tr.GetData(excelRowNum,"PG MID")));
		Helper.compareEquals(testConfig, "Offer fail reason", invoice.getOfferfailReason(),String.valueOf(tr.GetData(excelRowNum,"Offer Failure Reason")));
		Helper.compareEquals(testConfig, "offer type", invoice.getofferType(),String.valueOf(tr.GetData(excelRowNum,"Offer Type")));
		Helper.compareEquals(testConfig, "offer key", invoice.getOfferKey(),String.valueOf(tr.GetData(excelRowNum,"Offer Key")));
		Helper.compareEquals(testConfig, "error code", invoice.getErrorcode(),String.valueOf(tr.GetData(excelRowNum,"Error Code")));
		Assert.assertTrue(testConfig.getTestResult());

	}

	
	
}