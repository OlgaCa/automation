package Test.MerchantPanel.Transactions;

import java.io.File;
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
import Utils.TestDataReader;
import Utils.TestBase;

public class TestCODCancelDownload extends TestBase {

	@Test(description = "Verify that a pop up with the reference id is displayed and summary download", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void CodCancelRequestIDSummary(Config testConfig)   {
		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Do in-progress COD without Zipdial 
		int transactionRowNum = 23;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse =(TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.TestResponsePage);

		String ReqID;
		int outputExcelRowToBeVerified=1;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);

		dashBoard.ClickCodCancel();

		ReqID= dashBoard.selectTransactionCancelAction();

		dashBoard.downloadCancelSummary(ReqID);

		testConfig.putRunTimeProperty("tokenid", ReqID);
		Map<String, String> transaction_updateMap = DataBase.executeSelectQuery(testConfig, 50, 1);
		Browser.wait(testConfig, 30);

		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		Helper.compareEquals(testConfig, "amount",transaction_updateMap.get("amount"),tr.GetData(outputExcelRowToBeVerified, "amount"));
		Helper.compareEquals(testConfig, "transaction id",transaction_updateMap.get("txnid"),tr.GetData(outputExcelRowToBeVerified, "transactionid"));
		Helper.compareEquals(testConfig, "reference id",transaction_updateMap.get("token"),tr.GetData(outputExcelRowToBeVerified, "reference_id"));
		Helper.compareEquals(testConfig, "request id",transaction_updateMap.get("id"),tr.GetData(outputExcelRowToBeVerified, "request_id"));
		Helper.compareEquals(testConfig, "Status",transaction_updateMap.get("status"),tr.GetData(outputExcelRowToBeVerified, "result"));	

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify Export to excel exports all cancelled transaction details of the week by default", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void exportExcelbyWeek(Config testConfig)   {

		int transactionRowNum = 23;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickCodCancel();
		dashBoard.ClickExportExcelButton();
		Browser.wait(testConfig, 30);
		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);
		Helper.compareEquals(testConfig, "result count of transaction in excel", dashBoard.transactionno(),String.valueOf(tr.getRecordsNum()-1));	
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify Export to excel exports all cancel transaction details by date when count is less than 1000", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void exportExcelbyDate(Config testConfig)   {

		//Do in-progress COD without Zipdial 
		int transactionRowNum = 23;
		int FilterRow = 18;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		TestDataReader dataSheet = new TestDataReader(testConfig, "Filter");

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickCodCancel();
		Browser.wait(testConfig, 5);

		dashBoard.Filldateinputfrom(dataSheet.GetData(FilterRow,"date_from"));
		dashBoard.Filldateinputto(dataSheet.GetData(FilterRow, "date_to"));
		dashBoard.apply();
		Browser.wait(testConfig, 5);

		dashBoard.ClickExportExcelButton();

		Browser.wait(testConfig,30);
		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);
		Helper.compareEquals(testConfig, "result count of transaction in excel", dashBoard.transactionno(),String.valueOf(tr.getRecordsNum()-1));	
		Assert.assertTrue(testConfig.getTestResult());

	}

}

