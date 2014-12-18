package Test.MerchantPanel.Payments;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.WebServices.WebServicesHelper;
import PageObject.MerchantPanel.Home.DashboardPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.FileHandler;
import Utils.Helper;
import Utils.Helper.FileType;
import Utils.TestBase;
import Utils.TestDataReader;


public class TestReconUpload extends TestBase {
	
	public enum verifyReconResponse {
		verifyAllfield ,notReconciled, result
	};
	
	public enum setParameter
	{
		amount,transactionId, category, action, status, cardtype 
	}
	
	
	@Test(description = "Verify Invalid amount recon upload", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestInValidAmountReconUpload(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		// Perform failed transaction through CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 1;
		
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=7;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		
		writeToExcel(reconFile, 1, testConfig.getRunTimeProperty("mihpayid"),"333");
		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 10, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "CC", 0);
		
		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.result,reconResponseRowNum);				

		Assert.assertTrue(testConfig.getTestResult());			
	}

	@Test(description = "Verify invalid category recon upload", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestInValidCategoryReconUpload(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		// Perform failed transaction through CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 1;
		
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=8;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		
		writeToExcel(reconFile, 1, testConfig.getRunTimeProperty("mihpayid"),helper.testResponse.actualResponse.get("amount"),"abcd", setParameter.category);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "success", 0);
		
		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.result,reconResponseRowNum);				

		Assert.assertTrue(testConfig.getTestResult());			
	}

	@Test(description = "Verify Invalid cardType recon upload", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestInValidCardTypeReconUpload(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		// Perform failed transaction through CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 1;
		
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=15;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		
		writeToExcel(reconFile, 1, testConfig.getRunTimeProperty("mihpayid"),helper.testResponse.actualResponse.get("amount"),"abcd",setParameter.cardtype);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "CC", 0);
		
		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.result,reconResponseRowNum);				

		Assert.assertTrue(testConfig.getTestResult());			
	}

	@Test(description = "Verify Invalid status recon upload", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestInValidStatusReconUpload(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		// Perform failed transaction through CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 1;
		
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=14;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		writeToExcel(reconFile, 1, testConfig.getRunTimeProperty("mihpayid"),helper.testResponse.actualResponse.get("amount"),"abcd",setParameter.status);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "ABCD", 0);
		
		
		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.result,reconResponseRowNum);				

		Assert.assertTrue(testConfig.getTestResult());			
	}

	
	@Test(description = "Verify Invalid action recon upload", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestInValidActionReconUpload(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		// Perform failed transaction through CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 1;
		
		int reconResponseRowNum=16;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		writeToExcel(reconFile, 1, testConfig.getRunTimeProperty("mihpayid"),helper.testResponse.actualResponse.get("amount"),"abcd",setParameter.action);
		
		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		
		verifyInvalidAction(testConfig,reconResponseRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());			
	}
	
	@Test(description = "Verify Invalid payuid recon upload", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestInValidPayuIdReconUpload(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		// Perform failed transaction through CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 1;
		
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=3;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		writeToExcel(reconFile, 1, "fdsfsdfsdfs",helper.testResponse.actualResponse.get("amount"));
		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 10, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "CC", 0);
		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.result,reconResponseRowNum);				
		Assert.assertTrue(testConfig.getTestResult());			
	}

	
	@Test(description = "Verify Payu Aggregator recon upload", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestPayUAggReconUpload(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 4;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 17;
		
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=11;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		writeToExcel(reconFile, 1, testConfig.getRunTimeProperty("mihpayid"),helper.testResponse.actualResponse.get("amount"),"NB",setParameter.category);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "success", 0);
		
		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.notReconciled,reconResponseRowNum);				

		Assert.assertTrue(testConfig.getTestResult());			
	}
	
	@Test(description = "test valid recon upload", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestValidReconUpload(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		// Perform failed transaction through CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 1;
		
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=1;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		writeToExcel(reconFile, 1, testConfig.getRunTimeProperty("mihpayid"), helper.testResponse.actualResponse.get("amount"));
		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 10, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "CC", 0);

		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.verifyAllfield,reconResponseRowNum);				

		Assert.assertTrue(testConfig.getTestResult());			
	}
	
	@Test(description = "Verify for failed transaction recon upload file", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestFailedTransactionReconUpload(Config testConfig)
			 {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Perform failed transaction through CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 17;
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=1;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		
		writeToExcel(reconFile, 1, testConfig.getRunTimeProperty("mihpayid"),
				helper.testResponse.actualResponse.get("amount"));
		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 10, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "CC", 0);

		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.verifyAllfield,reconResponseRowNum);				

		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify for user cancelled transaction recon upload file", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestUserCancelledReconUpload(Config testConfig)
			 {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 12;
		int paymentTypeRowNum = 226;
		int cardDetailsRowNum = -1;
		
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=2;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		writeToExcel(reconFile, 1, testConfig.getRunTimeProperty("mihpayid"),
				helper.testResponse.actualResponse.get("amount"));
		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 10, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "CC", 0);

		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.notReconciled,reconResponseRowNum);				


		
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Perform a captured transaction recon upload file", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestCapturedReconUpload(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=5;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);
		
		writeToExcel(reconFile, 1, testConfig.getRunTimeProperty("mihpayid"), helper.testResponse.actualResponse.get("amount"));
		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 10, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "CC", 0);
		
		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.notReconciled,reconResponseRowNum);				

		Assert.assertTrue(testConfig.getTestResult());		
		
	}
	
	
// public void TestAuthTransactionReconUpload(Config testConfig) - moved to LiveCard TestCases
		@Test(description = "Verify for other bounced transaction recon upload file", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestBouncedTransactionReconUpload(Config testConfig)
			 {

		int transactionRowNum = 3;
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=9;
		testConfig.putRunTimeProperty("merchant_id", "5913");

		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);

		Map<String, String> transactionMap1 = DataBase.executeSelectQuery(testConfig, 13, 1);
		
		writeToExcel(reconFile, 1, transactionMap1.get("id"),transactionMap1.get("amount"));
		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 10, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "CC", 0);
		
        testConfig.putRunTimeProperty("mihpayid", transactionMap1.get("id"));
		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.notReconciled,reconResponseRowNum);				
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify for other initiated transaction recon upload file", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestInitiatedTransactionReconUpload(Config testConfig)
			 {

		int transactionRowNum = 3;
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=10;
		 testConfig.putRunTimeProperty("merchant_id", "5913");

		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);

		Map<String, String> transactionMap1 = DataBase.executeSelectQuery(testConfig, 14, 1);
		
		writeToExcel(reconFile, 1, transactionMap1.get("id"),transactionMap1.get("amount"));
		
        testConfig.putRunTimeProperty("mihpayid", transactionMap1.get("id"));
		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.notReconciled,reconResponseRowNum);				

		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify for other in progress transaction recon upload file", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestInProgressTransactionReconUpload(Config testConfig)
			 {

		int transactionRowNum = 3;
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=4;
		 testConfig.putRunTimeProperty("merchant_id", "5913");
		String reconFile = Helper.getExcelFile(testConfig, FileType.recon);

		Map<String, String> transactionMap1 = DataBase.executeSelectQuery(testConfig, 12, 1);
		writeToExcel(reconFile, 1, transactionMap1.get("id"),transactionMap1.get("amount"));

        testConfig.putRunTimeProperty("mihpayid", transactionMap1.get("id"));
		// Navigating to Merchant panel to View all transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ReconcileFileupload(reconFile);
		dashBoard.ClickReconcileButton();
		verifyReconResponse(testConfig, outputExcelRowToBeVerifiedRowNum,verifyReconResponse.notReconciled,reconResponseRowNum);				

		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	
	/**
	 * 
	 * @param filePath is the path where sheet has to be modified
	 * @param row of the excel file
	 * @param mihpayid is the transaction id
	 * @param amt is the amount 
	 */
	public void writeToExcel(String filePath, int row, String mihpayid, String amt)
	{
		//filePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//"+ filePath;

		FileHandler.write_edit(filePath,"Sheet1", row, 11, mihpayid, 0);
		FileHandler.write_edit(filePath, "Sheet1", row, 3, amt, 0);
		FileHandler.write_edit(filePath, "Sheet1", row, 6, amt, 0);
	}
	
	public void writeToExcel(String filePath, int row, String mihpayid, String amt, String category, setParameter type)
	{
		writeToExcel(filePath, row, mihpayid, amt);
		//filePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//"+ filePath;
		
		switch(type){
		case category:
			FileHandler.write_edit(filePath, "Sheet1", row, 12, category, 0);
			break;
		case action:
			FileHandler.write_edit(filePath, "Sheet1", row, 1, category, 0);
			break;
			
		case status:
			FileHandler.write_edit(filePath, "Sheet1", row, 2, category, 0);
			break;
			
		case cardtype:
			FileHandler.write_edit(filePath, "Sheet1", row, 10, category, 0);
			break;
		default:
			System.out.println("Not Handelled");
			break;
		}	
	}
	
	
	/**
	 * @param testConfig
	 * @param outputExcelRowToBeVerified
	 */
	public void verifyReconResponse(Config testConfig, int outputExcelRowToBeVerified, verifyReconResponse verifyEnum, int reconResponseRow)
	{ 
		
			DecimalFormat fiveprecision = new DecimalFormat("0.00000"); 
			DecimalFormat twoprecision = new DecimalFormat("0.00");  
			double dtemp;
			
			Browser.wait(testConfig,20);
			String reconfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
			File file = Browser.lastFileModified(testConfig,reconfilepath);
			String fileName = file.getName();

			TestDataReader tr = new TestDataReader(testConfig,"Sheet1", reconfilepath+fileName);

			TestDataReader dataSheet = new TestDataReader(testConfig,"ReconResponse");
	
				
		switch(verifyEnum)
		{
		case verifyAllfield : 
			Map<String, String> transaction_updateMap = DataBase.executeSelectQuery(testConfig,4, 1);
		
			testConfig.putRunTimeProperty("txn_update_id", transaction_updateMap.get("id"));
			
			Map<String, String> transaction_settlementMap = DataBase.executeSelectQuery(testConfig,5, 1);

			Helper.compareEquals(testConfig, "request id", transaction_updateMap.get("id"), tr.GetData(outputExcelRowToBeVerified, "requestid"));
			Helper.compareEquals(testConfig, "reconciliation id", transaction_settlementMap.get("reconciliation_id"), tr.GetData(outputExcelRowToBeVerified, "reconciliationid"));

			dtemp = Double.parseDouble(tr.GetData(outputExcelRowToBeVerified, "servicefee"));
			Helper.compareEquals(testConfig, "servicefee", transaction_settlementMap.get("mer_service_fee"), fiveprecision.format(dtemp));

			dtemp = Double.parseDouble(tr.GetData(outputExcelRowToBeVerified, "servicetax"));
			Helper.compareEquals(testConfig, "servicetax", transaction_settlementMap.get("bank_service_tax"),fiveprecision.format(dtemp));
			dtemp = Double.parseDouble(tr.GetData(outputExcelRowToBeVerified, "net"));

			Helper.compareEquals(testConfig, "net", transaction_settlementMap.get("bank_net_amount"), fiveprecision.format(dtemp));
			Helper.compareEquals(testConfig, "utr", transaction_settlementMap.get("bank_utr"), tr.GetData(outputExcelRowToBeVerified, "utr"));

			String expected = transaction_settlementMap.get("addedon").split(" ")[0];
			String actual = tr.GetData(outputExcelRowToBeVerified, "transactionDate").split(" ")[0];
			Helper.compareEquals(testConfig, "transactionDate", expected, actual);

			Helper.compareEquals(testConfig, "PayUMID", transaction_updateMap.get("merchant_id"), tr.GetData(outputExcelRowToBeVerified, "PayUMID"));

			dtemp = Double.parseDouble(tr.GetData(outputExcelRowToBeVerified, "amount"));
			Helper.compareEquals(testConfig, "amount", transaction_updateMap.get("amount"), twoprecision.format(dtemp));

			Helper.compareEquals(testConfig, "status", transaction_updateMap.get("status"), tr.GetData(outputExcelRowToBeVerified, "status"));
			Helper.compareEquals(testConfig, "action", transaction_updateMap.get("action"), tr.GetData(outputExcelRowToBeVerified, "action"));
			Helper.compareEquals(testConfig, "transactionid", transaction_updateMap.get("txnid"), tr.GetData(outputExcelRowToBeVerified, "transactionid"));

			Helper.compareEquals(testConfig, "result", dataSheet.GetData(reconResponseRow, "result"), tr.GetData(outputExcelRowToBeVerified, "result"));
			Helper.compareEquals(testConfig, "prev_status", dataSheet.GetData(reconResponseRow, "prev_status"), tr.GetData(outputExcelRowToBeVerified, "prev_status"));
			break;

		case notReconciled: 

				Map<String, String> transactionMap = DataBase.executeSelectQuery(testConfig,11, 1);
				
						
				dtemp = Double.parseDouble(tr.GetData(outputExcelRowToBeVerified, "amount"));
				Helper.compareEquals(testConfig, "amount", transactionMap.get("amount"), twoprecision.format(dtemp));
				Helper.compareEquals(testConfig, "transactionid", transactionMap.get("id"), tr.GetData(outputExcelRowToBeVerified, "transactionid"));
				Helper.compareEquals(testConfig, "result", dataSheet.GetData(reconResponseRow, "result"), tr.GetData(outputExcelRowToBeVerified, "result"));

				break;
				
		case result: 

			Helper.compareEquals(testConfig, "result", dataSheet.GetData(reconResponseRow, "result"), tr.GetData(outputExcelRowToBeVerified, "result"));
			break;

		}
		
	}
	
	public void verifyInvalidAction(Config testConfig, int reconResponseRowNum)
	{
		TestDataReader dataSheet = new TestDataReader(testConfig,"ReconResponse");
		System.out.println("Inside action case ");
		Browser.wait(testConfig, 2);
		String response = Element.getPageElement(testConfig, How.xPath, "//div[@id='txn-data']/div/div/div[2]").getText();
		Helper.compareEquals(testConfig,"recon response ", dataSheet.GetData(reconResponseRowNum, "result"), response);
		
	}
	

}

