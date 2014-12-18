package Test.MerchantPanel.Payments;


import java.io.File;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Transactions.TransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Test.MerchantPanel.Offers.OffersHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestFilter extends TestBase {

	@Test(description = "Verify edit fields ", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void editFieldsFilters(Config testConfig)  {

		int transactionRowNum = 3;

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		dashBoard.ClickViewAll();
		dashBoard.editFields();

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of user cancelled transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void userCancelledFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 4;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of bounced transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void bouncedFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 3;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of amount filter transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void amountFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 1;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");
		testConfig.putRunTimeProperty("min_amount",
				dataSheet.GetData(FilterRow, "min_amount"));
		testConfig.putRunTimeProperty("max_amount",
				dataSheet.GetData(FilterRow, "max_amount"));

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of dropped transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void droppedFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 5;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of failed by bank transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void failedbybankFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 6;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of CC payment transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void ccPaymentFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 8;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of DC payment transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void dcPaymentFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 9;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of EMI payment transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void emiPaymentFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 10;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of NB payment transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void nbPaymentFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 11;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of COD payment transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void codPaymentFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 12;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of captured status transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void capturedStatusFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 14;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of cancelled status transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void cancelledStatusFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 15;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of refunded status transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void refundedStatusFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 16;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of auth status transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void authStatusFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 17;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of auth status transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void dateFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 18;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of unique transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void uniqueFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 19;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify count of not reattempts transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void retryFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 20;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		testConfig.putRunTimeProperty("merchant_id", "5912");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	

	public void verifyFilter(Config testConfig, int FilterRow,
			DashboardPage dashBoard, TestDataReader dataSheet) 

	{
		dashBoard.ClickViewAll();

		if (dataSheet.GetData(FilterRow, "date_from").equals("0")) {
			testConfig.putRunTimeProperty("to",
					Helper.getDateBeforeOrAfterDays(1, "yyyy-MM-dd"));
			testConfig.putRunTimeProperty("from",
					Helper.getDateBeforeOrAfterDays(-7, "yyyy-MM-dd"));

		} else {
			dashBoard.Filldateinputfrom(dataSheet.GetData(FilterRow,"date_from"));
			dashBoard.Filldateinputto(dataSheet.GetData(FilterRow, "date_to"));

			testConfig.putRunTimeProperty("from",Helper.changeDateFormat(dataSheet.GetData(FilterRow,"date_from")));	
			testConfig.putRunTimeProperty("to", Helper.getDateBeforeOrAfterDays(1, "yyyy-MM-dd",dataSheet.GetData(FilterRow,"date_to")));

		}

		Browser.wait(testConfig, 2);
		dashBoard.AmountTab();
		Browser.wait(testConfig, 2);

		if (!dataSheet.GetData(FilterRow, "min_amount").equals(null))
			dashBoard.SubmitMinAmount(dataSheet.GetData(FilterRow, "min_amount"));
		
		if (!dataSheet.GetData(FilterRow, "max_amount").equals(null))
			dashBoard.SubmitMaxAmount(dataSheet.GetData(FilterRow, "max_amount"));

		dashBoard.Status();
		Browser.wait(testConfig, 2);
		dashBoard.allCheckboxStatus();
		Browser.wait(testConfig, 2);

		if (dataSheet.GetData(FilterRow, "all_status").equals("1"))
			dashBoard.allCheckboxStatus();

		if (dataSheet.GetData(FilterRow, "captured_status").equals("1"))
			dashBoard.Capturedstatus();

		if (dataSheet.GetData(FilterRow, "cancelled_status").equals("1"))
			dashBoard.Cancelledstatus();

		if (dataSheet.GetData(FilterRow, "refunded_status").equals("1"))
			dashBoard.Refundedstatus();

		if (dataSheet.GetData(FilterRow, "auth_status").equals("1"))
			dashBoard.Authstatus();

		
		dashBoard.UnsucessfulTransaction();
		Browser.wait(testConfig, 2);
		dashBoard.allUnsucessfulTransaction();
		Browser.wait(testConfig, 2);

		if (dataSheet.GetData(FilterRow, "all_unsucessful_transaction").equals("1"))
			dashBoard.allUnsucessfulTransaction();

		if (dataSheet.GetData(FilterRow, "bounced").equals("1"))
			dashBoard.BouncedTransaction();

		if (dataSheet.GetData(FilterRow, "user_cancelled").equals("1"))
			dashBoard.usercancelledTransaction();

		if (dataSheet.GetData(FilterRow, "dropped").equals("1"))
			dashBoard.DroppedTransaction();

		if (dataSheet.GetData(FilterRow, "failed_by_bank").equals("1"))
			dashBoard.failedbybankTransaction();

		
		dashBoard.paymentType();
		Browser.wait(testConfig, 2);
		dashBoard.ALLpaymentType();
		Browser.wait(testConfig, 2);

		if (dataSheet.GetData(FilterRow, "all_payment_type").equals("1"))
			dashBoard.ALLpaymentType();

		if (dataSheet.GetData(FilterRow, "cc_payment_type").equals("1"))
			dashBoard.CCpaymentType();

		if (dataSheet.GetData(FilterRow, "dc_payment_type").equals("1"))
			dashBoard.DCpaymentType();

		if (dataSheet.GetData(FilterRow, "nb_payment_type").equals("1"))
			dashBoard.NBpaymentType();

		if (dataSheet.GetData(FilterRow, "COD_payment_type").equals("1"))
			dashBoard.CODpaymentType();

		if (dataSheet.GetData(FilterRow, "emi_payment_type").equals("1"))
			dashBoard.EMIpaymentType();

		
		if (dataSheet.GetData(FilterRow, "unique").equals("1"))
			{
			dashBoard.uniquetransaction();
			Browser.wait(testConfig, 5);
			}

		if (dataSheet.GetData(FilterRow, "offer").equals("1"))
			{
			dashBoard.offerTab();
			Browser.wait(testConfig, 2);
			}

		if (dataSheet.GetData(FilterRow, "retry").equals("1"))
			dashBoard.retrytransaction();

		Browser.wait(testConfig, 5);

		 dashBoard.apply();
		 Browser.wait(testConfig, 5);
		Map<String, String> transaction_count = DataBase.executeSelectQuery(
				testConfig,
				Integer.parseInt(dataSheet.GetData(FilterRow, "sql_row")), 1);

		Helper.compareEquals(testConfig, "result count",transaction_count.get("total"), dashBoard.transactionno());
	
	}
	
	@Test(description = "Verify count of offer transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void offerFilters(Config testConfig)  {

		int transactionRowNum = 3;
		int FilterRow = 21;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
	dHelper.doMerchantLogin(transactionRowNum);

	// navigate to offer list page
	Browser.navigateToURL(testConfig,
			testConfig.getRunTimeProperty("OfferListUrl"));
	
	OffersHelper oHelper = new OffersHelper(testConfig);

	// set offer to use for transactions
			oHelper.setOfferInfo("testOffer");

		// login to admin
				helper.DoLogin();
				// make transaction with created offer
				helper.GetTestTransactionPage();
				
				helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
						transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
						ExpectedResponsePage.TestResponsePage);

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");
		testConfig.putRunTimeProperty("merchant_id", "5912");
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);

		verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Verify edit fields filter on request page", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void editFieldFilters(Config testConfig)  {

	
	int transactionRowNum = 7;
	int excelRowNum= 1;

	DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
	DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
	TransactionsPage requestPage=dashBoard.ClickViewAllTxn();
	
	//apply Edit field filter on view transaction page
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

	
	Helper.compareEquals(testConfig, "PayU id in excel", requestPage.getPayuId(),String.valueOf(tr.GetData(excelRowNum,"PayU ID")));
	Helper.compareEquals(testConfig, "Amount in excel", requestPage.getAmount(),String.valueOf(tr.GetData(excelRowNum,"Amount")));
	Helper.compareEquals(testConfig, "Status in excel", requestPage.getStatus(),String.valueOf(tr.GetData(excelRowNum,"Status")));
	Helper.compareEquals(testConfig, "req amount in excel", requestPage.getReqAmount1(),String.valueOf(tr.GetData(excelRowNum,"Requested Amount")));
	Helper.compareEquals(testConfig, "discount", requestPage.getDiscount(),String.valueOf(tr.GetData(excelRowNum,"Discount")));
	Helper.compareEquals(testConfig, "merchant name in excel", requestPage.getMerchantname(),String.valueOf(tr.GetData(excelRowNum,"Merchant Name")));
	Helper.compareEquals(testConfig, "Payment gateway", requestPage.getPG(),String.valueOf(tr.GetData(excelRowNum,"Payment Gateway")));
	Helper.compareEquals(testConfig, "cistomer email", requestPage.geteMail(),String.valueOf(tr.GetData(excelRowNum,"Customer Email")));
	Helper.compareEquals(testConfig, "Ip adderess", requestPage.getipAddress(),String.valueOf(tr.GetData(excelRowNum,"Customer IP Address")));
	Helper.compareEquals(testConfig, "Card number", requestPage.getCardno(),String.valueOf(tr.GetData(excelRowNum,"Card Number")));
	Helper.compareEquals(testConfig, "Payment type", requestPage.getPaymenttype(),String.valueOf(tr.GetData(excelRowNum,"Payment Type")));
	Helper.compareEquals(testConfig, "Customer phone", requestPage.getPhone(),String.valueOf(tr.GetData(excelRowNum,"Customer Phone")));
	Helper.compareEquals(testConfig, "City", requestPage.getEcity(),String.valueOf(tr.GetData(excelRowNum,"City")));
	Helper.compareEquals(testConfig, "Name on card", requestPage.getEcardName(),String.valueOf(tr.GetData(excelRowNum,"Name on Card")));
	Helper.compareEquals(testConfig, "country", requestPage.getECountry(),String.valueOf(tr.GetData(excelRowNum,"Country")));
	Helper.compareEquals(testConfig, "Zip code", requestPage.getEzipcode(),String.valueOf(tr.GetData(excelRowNum,"ZipCode")));
	Helper.compareEquals(testConfig, "field 3", requestPage.getField3(),String.valueOf(tr.GetData(excelRowNum,"UDF 3")));
	Helper.compareEquals(testConfig, "field 4", requestPage.getField4(),String.valueOf(tr.GetData(excelRowNum,"UDF 4")));
	Helper.compareEquals(testConfig, "field 5", requestPage.getField5(),String.valueOf(tr.GetData(excelRowNum,"UDF 5")));
	Helper.compareEquals(testConfig, "Prod info", requestPage.getProdInfo(),String.valueOf(tr.GetData(excelRowNum,"Product Info")));
	Helper.compareEquals(testConfig, "Shipping first name", requestPage.getFname(),String.valueOf(tr.GetData(excelRowNum,"Shipping Firstname")));
	Helper.compareEquals(testConfig, "Shipping last name", requestPage.getLname(),String.valueOf(tr.GetData(excelRowNum,"Shipping Lastname")));
	Helper.compareEquals(testConfig, "Address1", requestPage.getAdd1(),String.valueOf(tr.GetData(excelRowNum,"Shipping Address1")));
	Helper.compareEquals(testConfig, "Address2", requestPage.getAdd2(),String.valueOf(tr.GetData(excelRowNum,"Shipping Address1")));
	Helper.compareEquals(testConfig, "Shipping city", requestPage.getEcity(),String.valueOf(tr.GetData(excelRowNum,"Shipping City")));
	Helper.compareEquals(testConfig, "Shipping State", requestPage.getEstate(),String.valueOf(tr.GetData(excelRowNum,"Shipping State")));
	Helper.compareEquals(testConfig, "Shipping Zip code", requestPage.getEzipcode(),String.valueOf(tr.GetData(excelRowNum,"Shipping Zipcode")));
	Helper.compareEquals(testConfig, "Shipping country", requestPage.getECountry(),String.valueOf(tr.GetData(excelRowNum,"Shipping Country")));
	Helper.compareEquals(testConfig, "Shipping phone", requestPage.getEPhone(),String.valueOf(tr.GetData(excelRowNum,"Shipping Phone")));









	
	Assert.assertTrue(testConfig.getTestResult());

}

}