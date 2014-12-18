package Test.MerchantPanel.Transactions;

import java.io.File;
import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
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
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;


public class TestRequestPage extends TestBase{


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
	@Test(description = "Verify Export to excel exports all transactions details when count is less than 1000", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void exportExcelbyDateFilter(Config testConfig)   {

		int transactionRowNum = 3;
		int FilterRow = 18;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewRequest();

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

		int transactionRowNum = 7;
		int excelRowNum= 1;

		//testConfig.putRunTimeProperty("merchant_id", "2");
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RequestsPage requestPage=dashBoard.ClickViewRequest();


		dashBoard.ClickExportExcelButton();
		Browser.wait(testConfig, 20);
		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);

		Helper.compareEquals(testConfig, "req id in excel", requestPage.getreqID(),String.valueOf(tr.GetData(excelRowNum,"Request ID")));
		Helper.compareEquals(testConfig, "Customer name in excel", requestPage.getName(),String.valueOf(tr.GetData(excelRowNum,"Customer Name")));
		Helper.compareEquals(testConfig, "PayU id in excel", requestPage.getPayuId(),String.valueOf(tr.GetData(excelRowNum,"PayU ID")));
		Helper.compareEquals(testConfig, "Amount in excel", requestPage.getAmount(),String.valueOf(tr.GetData(excelRowNum,"Amount")));
		Helper.compareEquals(testConfig, "Req action in excel", requestPage.getAction(),String.valueOf(tr.GetData(excelRowNum,"Requested Action")));
		Helper.compareEquals(testConfig, "Status in excel", requestPage.getStatus(),String.valueOf(tr.GetData(excelRowNum,"Status")));
		Helper.compareEquals(testConfig, "Bank name in excel", requestPage.getBankname(),String.valueOf(tr.GetData(excelRowNum,"Bank Name")));
		Helper.compareEquals(testConfig, "req amount in excel", requestPage.getReqAmount1(),String.valueOf(tr.GetData(excelRowNum,"Requested Amount")));
		Helper.compareEquals(testConfig, "discount", requestPage.getDiscount(),String.valueOf(tr.GetData(excelRowNum,"Discount")));
		Helper.compareEquals(testConfig, "merchant name in excel", requestPage.getMerchantname(),String.valueOf(tr.GetData(excelRowNum,"Merchant Name")));
		Helper.compareEquals(testConfig, "transaction id", requestPage.getTxnId(),String.valueOf(tr.GetData(excelRowNum,"Transaction ID")));
		Helper.compareEquals(testConfig, "Payment gateway", requestPage.getPG(),String.valueOf(tr.GetData(excelRowNum,"Payment Gateway")));
		Helper.compareEquals(testConfig, "last name", requestPage.getLastname(),String.valueOf(tr.GetData(excelRowNum,"Last name")));
		Helper.compareEquals(testConfig, "cistomer email", requestPage.geteMail(),String.valueOf(tr.GetData(excelRowNum,"Customer Email")));
		Helper.compareEquals(testConfig, "Ip adderess", requestPage.getipAddress(),String.valueOf(tr.GetData(excelRowNum,"Customer IP Address")));
		Helper.compareEquals(testConfig, "Card number", requestPage.getCardno(),String.valueOf(tr.GetData(excelRowNum,"Card Number")));
		Helper.compareEquals(testConfig, "Payment type", requestPage.getPaymenttype(),String.valueOf(tr.GetData(excelRowNum,"Payment Type")));
		Helper.compareEquals(testConfig, "Net amount in excel", requestPage.getNamount(),String.valueOf(tr.GetData(excelRowNum,"Net Amount")));
		Helper.compareEquals(testConfig, "Service Fee in excel", requestPage.getServiceFee(),String.valueOf(tr.GetData(excelRowNum,"Service Fee")));
		Helper.compareEquals(testConfig, "Service tax in excel", requestPage.getServicetax(),String.valueOf(tr.GetData(excelRowNum,"Service Tax")));
		Helper.compareEquals(testConfig, "token in excel", requestPage.getToken(),String.valueOf(tr.GetData(excelRowNum,"Token")));
		Helper.compareEquals(testConfig, "settlement in excel", requestPage.getSettle(),String.valueOf(tr.GetData(excelRowNum,"Settlement")));
		Helper.compareEquals(testConfig, "card category", requestPage.getCardCategory(),String.valueOf(tr.GetData(excelRowNum,"International/Domestic")));
		Helper.compareEquals(testConfig, "PGMID in excel", requestPage.getPgmid(),String.valueOf(tr.GetData(excelRowNum,"PG MID")));
		Helper.compareEquals(testConfig, "Offer fail reason", requestPage.getOfferFailreason(),String.valueOf(tr.GetData(excelRowNum,"Offer Failure Reason")));
		Helper.compareEquals(testConfig, "offer type", requestPage.getOfferType(),String.valueOf(tr.GetData(excelRowNum,"Offer Type")));

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
		RequestsPage request= dashBoard.ClickViewRequest();

		request.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
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
		RequestsPage request= dashBoard.ClickViewRequest();

		request.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
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
		RequestsPage request= dashBoard.ClickViewRequest();

		request.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
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
		RequestsPage request= dashBoard.ClickViewRequest();
		request.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
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
		RequestsPage request= dashBoard.ClickViewRequest();

		request.verifyPaymentTypeFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	@Test(description = "Verify edit fields filter on request page", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void editFieldsFilters(Config testConfig)  {

		
		int transactionRowNum = 7;
		int excelRowNum= 1;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RequestsPage requestPage=dashBoard.ClickViewRequest();
		
		//apply Edit field filter on request page
		dashBoard.editFields();
		requestPage.CheckEditfields();
		dashBoard.apply();
        
		//Export to excel for all optional fields
		dashBoard.ClickExportExcelButton();
		Browser.wait(testConfig, 20);
		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);
		
		//Compare excel sheet first row values with actual field values

		Helper.compareEquals(testConfig, "req id in excel", requestPage.getreqID(),String.valueOf(tr.GetData(excelRowNum,"Request ID")));
		Helper.compareEquals(testConfig, "Customer name in excel", requestPage.getName(),String.valueOf(tr.GetData(excelRowNum,"Customer Name")));
		Helper.compareEquals(testConfig, "PayU id in excel", requestPage.getPayuId(),String.valueOf(tr.GetData(excelRowNum,"PayU ID")));
		Helper.compareEquals(testConfig, "Amount in excel", requestPage.getAmount(),String.valueOf(tr.GetData(excelRowNum,"Amount")));
		Helper.compareEquals(testConfig, "Req action in excel", requestPage.getAction(),String.valueOf(tr.GetData(excelRowNum,"Requested Action")));
		Helper.compareEquals(testConfig, "Status in excel", requestPage.getStatus(),String.valueOf(tr.GetData(excelRowNum,"Status")));
		Helper.compareEquals(testConfig, "Bank name in excel", requestPage.getBankname(),String.valueOf(tr.GetData(excelRowNum,"Bank Name")));
		Helper.compareEquals(testConfig, "req amount in excel", requestPage.getReqAmount1(),String.valueOf(tr.GetData(excelRowNum,"Requested Amount")));
		Helper.compareEquals(testConfig, "discount", requestPage.getDiscount(),String.valueOf(tr.GetData(excelRowNum,"Discount")));
		Helper.compareEquals(testConfig, "merchant name in excel", requestPage.getMerchantname(),String.valueOf(tr.GetData(excelRowNum,"Merchant Name")));
		Helper.compareEquals(testConfig, "transaction id", requestPage.getTxnId(),String.valueOf(tr.GetData(excelRowNum,"Transaction ID")));
		Helper.compareEquals(testConfig, "Payment gateway", requestPage.getPG(),String.valueOf(tr.GetData(excelRowNum,"Payment Gateway")));
		Helper.compareEquals(testConfig, "last name", requestPage.getLastname(),String.valueOf(tr.GetData(excelRowNum,"Last name")));
		Helper.compareEquals(testConfig, "cistomer email", requestPage.geteMail(),String.valueOf(tr.GetData(excelRowNum,"Customer Email")));
		Helper.compareEquals(testConfig, "Ip adderess", requestPage.getipAddress(),String.valueOf(tr.GetData(excelRowNum,"Customer IP Address")));
		Helper.compareEquals(testConfig, "Card number", requestPage.getCardno(),String.valueOf(tr.GetData(excelRowNum,"Card Number")));
		Helper.compareEquals(testConfig, "Payment type", requestPage.getPaymenttype(),String.valueOf(tr.GetData(excelRowNum,"Payment Type")));
		Helper.compareEquals(testConfig, "Net amount in excel", requestPage.getNamount(),String.valueOf(tr.GetData(excelRowNum,"Net Amount")));
		Helper.compareEquals(testConfig, "Service Fee in excel", requestPage.getServiceFee(),String.valueOf(tr.GetData(excelRowNum,"Service Fee")));
		Helper.compareEquals(testConfig, "Service tax in excel", requestPage.getServicetax(),String.valueOf(tr.GetData(excelRowNum,"Service Tax")));
		Helper.compareEquals(testConfig, "token in excel", requestPage.getToken(),String.valueOf(tr.GetData(excelRowNum,"Token")));
		Helper.compareEquals(testConfig, "settlement in excel", requestPage.getSettle(),String.valueOf(tr.GetData(excelRowNum,"Settlement")));
		Helper.compareEquals(testConfig, "card category", requestPage.getECardCategory(),String.valueOf(tr.GetData(excelRowNum,"International/Domestic")));
		Helper.compareEquals(testConfig, "PGMID in excel", requestPage.getEPgmid(),String.valueOf(tr.GetData(excelRowNum,"PG MID")));
		Helper.compareEquals(testConfig, "Offer fail reason", requestPage.getEOfferFailreason(),String.valueOf(tr.GetData(excelRowNum,"Offer Failure Reason")));
		Helper.compareEquals(testConfig, "Customer phone", requestPage.getEPhone(),String.valueOf(tr.GetData(excelRowNum,"Customer Phone")));
		Helper.compareEquals(testConfig, "City", requestPage.getEcity(),String.valueOf(tr.GetData(excelRowNum,"City")));
		Helper.compareEquals(testConfig, "Name on card", requestPage.getEcardName(),String.valueOf(tr.GetData(excelRowNum,"Name on Card")));
		Helper.compareEquals(testConfig, "country", requestPage.getECountry(),String.valueOf(tr.GetData(excelRowNum,"Country")));
		Helper.compareEquals(testConfig, "Zip code", requestPage.getEzipcode(),String.valueOf(tr.GetData(excelRowNum,"ZipCode")));
		Helper.compareEquals(testConfig, "field 3", requestPage.getField3(),String.valueOf(tr.GetData(excelRowNum,"Field3")));
		Helper.compareEquals(testConfig, "field 4", requestPage.getField4(),String.valueOf(tr.GetData(excelRowNum,"Field4")));
		Helper.compareEquals(testConfig, "field 5", requestPage.getField5(),String.valueOf(tr.GetData(excelRowNum,"Field5")));
		Helper.compareEquals(testConfig, "Address2", requestPage.getEAddress2(),String.valueOf(tr.GetData(excelRowNum,"Address2")));
		Helper.compareEquals(testConfig, "Prod info", requestPage.getEProdInfo(),String.valueOf(tr.GetData(excelRowNum,"Product Info")));

		Assert.assertTrue(testConfig.getTestResult());

	}
	
}