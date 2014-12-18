package Test.MerchantPanel.Transactions;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Transactions.CancelPage;
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


public class TestRequestCancel extends TestBase{

	//public void test_VerifyCITITransaction(Config testConfig) - moved to LiveCard TestCase - 13

	@Test(description = "Verify Export to excel exports all in progress transaction details of the week by default", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void exportExcelbyWeek(Config testConfig)   {

		int transactionRowNum = 2;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);	
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickCancel();
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

		int transactionRowNum = 2;
		int FilterRow = 18;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickCancel();
		
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
	@Test(description = "Verify count of auth status transaction with date range filter", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void refundedStatusFilters(Config testConfig) throws Exception {

		int transactionRowNum = 2;
		int FilterRow = 24;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");
		testConfig.putRunTimeProperty("from",Helper.changeDateFormat(dataSheet.GetData(FilterRow,"date_from")));
		testConfig.putRunTimeProperty("to",Helper.changeDateFormat(dataSheet.GetData(FilterRow,"date_to")));
		
		testConfig.putRunTimeProperty("merchant_id", "5911");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);
		CapturePage captureTransaction = dashBoard.ClickRequestCapture();

		captureTransaction.verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Verify count of auth status transaction with amount filter", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void refundedStatusAmountFilters(Config testConfig) throws Exception {

		int transactionRowNum = 2;
		int FilterRow = 25;

		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");
		testConfig.putRunTimeProperty("min_amount",
				dataSheet.GetData(FilterRow, "min_amount"));
		testConfig.putRunTimeProperty("max_amount",
				dataSheet.GetData(FilterRow, "max_amount"));


		testConfig.putRunTimeProperty("merchant_id", "5911");

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper
				.doMerchantLogin(transactionRowNum);
		CapturePage captureTransaction = dashBoard.ClickRequestCapture();

		captureTransaction.verifyFilter(testConfig, FilterRow, dashBoard, dataSheet);
		Assert.assertTrue(testConfig.getTestResult());

}
	//public void VerifyFirstRowexportExcel(Config testConfig) throws Exception - moved to LiveCard TestCase - 11
	
	//public void test_VerifyAuthTransaction(Config testConfig) - moved to LiveCard TestCases - 10
		
	//public void test_VerifyCancelDownloadSummary(Config testConfig, String[] testType) - moved to LiveCard TestCase - 12
}






