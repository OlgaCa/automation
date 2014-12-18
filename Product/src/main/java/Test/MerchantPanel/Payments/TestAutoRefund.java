package Test.MerchantPanel.Payments;

import java.io.File;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.ManualUpdate.ReconPage;
import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.EditMerchantPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.EditPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import PageObject.MerchantPanel.Transactions.RequestsPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage.SearchOn;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.FileHandler;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;
import Utils.Helper.FileType;

public class TestAutoRefund extends TestBase{

	MerchantDetailsPage merchantDetailsPage;
	EditMerchantPage editMerchantPage;
	EditPage editPage;
	ManualUpdatePage manualUpdatePage;
	ReconPage reconPage;

	public enum verifyReconResult {
		failed, userCancelled, auth, bounced, dropped, dropped_on, dropped_off, captured
	}

	public enum AutoRefund
	{
		on, off;
	}

	@Test(description = "Verify Auto Refund for failed transactions with AutoRefund=1", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestFailedTransactionsAutoRefundOn(Config testConfig)
	{

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying failed transaction done through CITI gateway is autorefunded
		 when AutoRefund is set 1
		 Merchant - AutomationMerchant3
		 ****************
		 *****************/
		int transactionRowNum =3;    
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.on);

		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 1;
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Recon the transaction
		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify Recon file downloaded
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum,verifyReconResult.failed ,reconResponseRowNum);

		//Navigate to dashboard
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 2);
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);

		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		String request_id = dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.failed, AutoRefund.on, testConfig);

		//Verify transaction in Dashboard
		verifyViewRequest(dashBoard, request_id, verifyReconResult.failed);

		/****************
		 ****************
		 Verifying failed transaction done through HDFC2 gateway is autorefunded
		 when AutoRefund is set 1
		 Merchant - AutomationMerchant3
		 ****************
		 *****************/
		helper.GetTestTransactionPage();
		transactionRowNum =3;    
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.on);

		paymentTypeRowNum = 237;
		cardDetailsRowNum = 17;
		reconResponseRowNum=12;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Recon the transaction
		payuid = helper.testResponse.actualResponse.get("mihpayid");
		reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);

		//Verify the recon downloaded file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum,verifyReconResult.failed ,reconResponseRowNum);

		//Navigate to Dashboard
		dashboardHelper = new DashboardHelper(testConfig);
		 dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 2);
		merchantTransaction = new MerchantTransactionsPage(testConfig);

		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		request_id = dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.failed, AutoRefund.on, testConfig);

		//Verify the transaction in dashboard
		verifyViewRequest(dashBoard, request_id, verifyReconResult.failed);

		/****************
		 ****************
		 Verifying failed transaction done through AXIS gateway is autorefunded
		 when AutoRefund is set 1
		 Merchant - AutomationMerchant20
		 ****************
		 *****************/	

		helper.GetTestTransactionPage();
		transactionRowNum =33;   
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.on);

		paymentTypeRowNum = 234;
		cardDetailsRowNum = 17;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Recon the transaction
		payuid = helper.testResponse.actualResponse.get("mihpayid");
		reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify the downloaded recon file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum,verifyReconResult.failed ,reconResponseRowNum);

		//Navigate to dashboard
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 2);
		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		request_id = dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.failed, AutoRefund.on, testConfig);

		//Verify the transaction in dashboard
		verifyViewRequest(dashBoard, request_id, verifyReconResult.failed);
		testConfig.logComment("Test failing for HDFC/AXIS autorefund: Issue is http://redmine.ibibo.com/issues/23071");

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify Auto Refund for failed transactions with AutoRefund=0", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestFailedTransactionsAutoRefundOff(Config testConfig)
	{

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		/****************
		 ****************
		 Verifying failed transaction done through CITI gateway is not autorefunded
		 when AutoRefund is set 0
		 Merchant - AutomationMerchant3
		 ****************
		 *****************/
		int transactionRowNum = 3;   
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.off);

		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 17;
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);

		//Recon the transaction
		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify Recon file downloaded
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum,verifyReconResult.failed ,reconResponseRowNum);

		//Navigate to dashboard
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 1);
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData,verifyReconResult.failed,AutoRefund.off, testConfig);

		/****************
		 ****************
		 Verifying failed transaction done through HDFC2 gateway is not autorefunded
		 when AutoRefund is set 0
		 Merchant - AutomationMerchant3
		 ****************
		 *****************/
		helper.GetTestTransactionPage();
		transactionRowNum =3;   
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.off);

		paymentTypeRowNum = 237;
		cardDetailsRowNum = 17;
		outputExcelRowToBeVerifiedRowNum= 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Recon the transaction
		payuid = helper.testResponse.actualResponse.get("mihpayid");
		reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify Recon file downloaded
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum,verifyReconResult.failed ,reconResponseRowNum);
		
		//Navigate to dashboard
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 1);
		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData,verifyReconResult.failed,AutoRefund.off, testConfig);

		/****************
		 ****************
		 Verifying failed transaction done through AXIS gateway is not autorefunded
		 when AutoRefund is set 0
		 Merchant - AutomationMerchant20
		 ****************
		 *****************/
		helper.GetTestTransactionPage();
		transactionRowNum =33;  
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.off);

		paymentTypeRowNum = 234;
		cardDetailsRowNum = 17;
		outputExcelRowToBeVerifiedRowNum= 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Recon the transaction
		payuid = helper.testResponse.actualResponse.get("mihpayid");
		reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify Recon file downloaded
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum,verifyReconResult.failed ,reconResponseRowNum);

		//Navigate to dashboard
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 1);
		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData,verifyReconResult.failed,AutoRefund.off, testConfig);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify Auto Refund for captured transactions with AutoRefund=1", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestCapturedTransactionsAutoRefundOn(Config testConfig)
	{

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying captured transaction done through HDFC2 gateway is autorefunded
		 when AutoRefund is set 1
		 Merchant - AutomationMerchant3
		 ****************
		 *****************/
		int transactionRowNum = 3; 
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=5;

		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.on);
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Recon the transaction
		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify the downloaded recon file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum,verifyReconResult.captured ,reconResponseRowNum);

		//Navigate to dashboard
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		//Verify the transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.captured, AutoRefund.on, testConfig);


		/****************
		 ****************
		 Verifying captured transaction done through AXIS gateway is autorefunded
		 when AutoRefund is set 1
		 Merchant - AutomationMerchant1
		 ****************
		 *****************/

		helper.GetTestTransactionPage();
		transactionRowNum =1;   
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.on);

		paymentTypeRowNum = 3;
		cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Recon the transaction
		payuid = helper.testResponse.actualResponse.get("mihpayid");
		reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify the downloaded recon file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum,verifyReconResult.captured ,reconResponseRowNum);

		//Navigate to dashboard
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		//Verify the transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.captured, AutoRefund.on, testConfig);

		Assert.assertTrue(testConfig.getTestResult());

	}


	@Test(description = "Verify Auto Refund for captured transactions with AutoRefund=0", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestCapturedTransactionsAutoRefundOff(Config testConfig)
	{

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying captured transaction done through HDFC2 gateway is not autorefunded
		 when AutoRefund is set 0
		 Merchant - AutomationMerchant3
		****************
		*****************/
		int transactionRowNum = 3; 
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=5;

		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.off);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_txn_update_data(testConfig);
		//Recon the transaction
		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);


		reconUpload(reconFile, testConfig);
		//Verify downloaded excel file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum,verifyReconResult.captured ,reconResponseRowNum);

		//Navigate to dashboard
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.captured, AutoRefund.off, testConfig);

		/****************
		 ****************
		 Verifying captured transaction done through AXIS gateway is not autorefunded
		 when AutoRefund is set 0
		 Merchant - AutomationMerchant1
		****************
		*****************/		
		helper.GetTestTransactionPage();
		transactionRowNum =1;   
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.off);

		paymentTypeRowNum = 3;
		cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_txn_update_data(testConfig);
		//Recon the transaction
		payuid = helper.testResponse.actualResponse.get("mihpayid");
		reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);


		reconUpload(reconFile, testConfig);
		//Verify downloaded recon file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum,verifyReconResult.captured ,reconResponseRowNum);

		//Navigate to dashboard
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.captured, AutoRefund.off, testConfig);

		Assert.assertTrue(testConfig.getTestResult());Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify Auto Refund for User Cancelled transaction with AutoRefund=1", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestUserCancelledAutoRefundOn(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying usercancelled transaction is not autorefunded
		 when AutoRefund is set 1
		 Merchant - AutomationMerchant6
		 ****************
		 *****************/
		
		int transactionRowNum =6;
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.on);

		int paymentTypeRowNum = 226;
		int cardDetailsRowNum = -1;

		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum = 13;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Recon the transaction
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				"CC", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum, verifyReconResult.userCancelled, reconResponseRowNum);

		//Navigate to dashboard
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.userCancelled,AutoRefund.on, testConfig);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify Auto Refund for User Cancelled Transaction with AutoRefund=0", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestUserCancelledAutoRefundOff(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		/****************
		 ****************
		 Verifying usercancelled transaction is not autorefunded
		 when AutoRefund is set 0
		 Merchant - AutomationMerchant6
		 ****************
		 *****************/
		
		int transactionRowNum =6;  
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.off);

		int paymentTypeRowNum = 226;
		int cardDetailsRowNum = -1;
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum = 13;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Recon the transaction
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				"CC", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum, verifyReconResult.userCancelled, reconResponseRowNum);

		//Navigate to dashboard
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.userCancelled, AutoRefund.off, testConfig);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify Auto Refund for Bounced Transaction with AutoRefund=1", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestBouncedAutoRefundOn(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying bounced transaction done is not autorefunded
		 when AutoRefund is set 1
		 Merchant - AutomationMerchant2
		 ****************
		 *****************/
		
		int transactionRowNum =2; 
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=13;
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.on);

		//Extracting the bounced transaction details from db for AutomationMerchant2
		testConfig.putRunTimeProperty("merchant_id", "5911");
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 13, 1);

		testConfig.putRunTimeProperty("txnId", map.get("txnid"));

		//Recon the transaction
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				map.get("id"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				"CC", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum, verifyReconResult.bounced, reconResponseRowNum);

		//Navigate to dashboard
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("txnId"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.bounced,AutoRefund.on, testConfig);
		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Verify Auto Refund for Bounced Transaction with AutoRefund=0", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestBouncedAutoRefundOff(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		/****************
		 ****************
		 Verifying bounced transaction is not autorefunded
		 when AutoRefund is set 0
		 Merchant - AutomationMerchant2
		 ****************
		 *****************/
		
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=13;
		int transactionRowNum =2; 
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.off);

		//Extracting the bounced transaction details from db for AutomationMerchant2
		testConfig.putRunTimeProperty("merchant_id", "5911");
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 13, 1);

		testConfig.putRunTimeProperty("txnId", map.get("txnid"));

		//Recon the file
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				map.get("id"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				"CC", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum, verifyReconResult.bounced, reconResponseRowNum);

		//Navigate to dashboard
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("txnId"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.bounced,AutoRefund.off, testConfig);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify Auto Refund for Dropped Transaction with AutoRefund=1", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestDroppedAutoRefundOn(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying dropped transaction through CITI gateway is autorefunded
		 when AutoRefund is set 1
		 Merchant - AutomationMerchant2
		 ****************
		 *****************/
		int transactionRowNum =2;   
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum =1;
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.on);

		//Extracting the Citi netbanking dropped transaction details from db for AutomationMerchant2
		testConfig.putRunTimeProperty("merchantid", "5911");
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 87, 1);

		testConfig.putRunTimeProperty("mihpayid", map.get("id"));
		testConfig.putRunTimeProperty("txnId", map.get("txnid"));

		//Recon the transaction
		String payuid = map.get("id");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				"CC", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify downloaded recon file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum, verifyReconResult.dropped_on, reconResponseRowNum);

		//Navigate to dashboard
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 1);
		waitForDroppedStatus(testConfig);

		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(map.get("txnid"));
		String request_id = dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.dropped,AutoRefund.on, testConfig);

		//Verify transaction in dashboard
		verifyViewRequest(dashBoard, request_id, verifyReconResult.failed);

		/****************
		 ****************
		 Verifying dropped transaction through AXIS gateway is autorefunded
		 when AutoRefund is set 1
		 Merchant - AutomationMerchant2
		 ****************
		 *****************/
		//Extracting the AXIS netbanking dropped transaction details from db for AutomationMerchant2
		helper.GetTestTransactionPage();
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.on);
		map = DataBase.executeSelectQuery(testConfig, 88, 1);

		reconResponseRowNum = 12;
		testConfig.putRunTimeProperty("mihpayid", map.get("id"));
		testConfig.putRunTimeProperty("txnId", map.get("txnid"));

		//Recon the transaction
		payuid = map.get("id");
		reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				"CC", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify downloaded recon file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum, verifyReconResult.dropped_on, reconResponseRowNum);

		//Navigate to dashboard
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 1);
		waitForDroppedStatus(testConfig);

		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(map.get("txnid"));
		request_id = dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.dropped,AutoRefund.on, testConfig);

		//Verify transaction in dashbaord
		verifyViewRequest(dashBoard, request_id, verifyReconResult.failed);

		/****************
		 ****************
		 Verifying dropped transaction through HDFC2 gateway is autorefunded
		 when AutoRefund is set 1
		 Merchant - AutomationMerchant2
		 ****************
		 *****************/
		//Extracting the HDFC netbanking dropped transaction details from db for AutomationMerchant2
		helper.GetTestTransactionPage();
		map = DataBase.executeSelectQuery(testConfig, 89, 1);

		testConfig.putRunTimeProperty("mihpayid", map.get("id"));
		testConfig.putRunTimeProperty("txnId", map.get("txnid"));

		//Recon the transaction
		payuid = map.get("id");
		reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				map.get("id"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				"CC", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify downloaded recon file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum, verifyReconResult.dropped_on, reconResponseRowNum);

		//Navigate to dashboard
		dashboardHelper = new DashboardHelper(testConfig);
		 dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 1);
		waitForDroppedStatus(testConfig);

		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(map.get("txnid"));
		request_id = dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.dropped,AutoRefund.on, testConfig);

		//Verify transaction in dashboard
		verifyViewRequest(dashBoard, request_id, verifyReconResult.failed);

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify Auto Refund for Dropped Transaction with AutoRefund=0", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestDroppedAutoRefundOff(Config testConfig)  {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying dropped transaction through CITI gateway is not autorefunded
		 when AutoRefund is set 0
		 Merchant - AutomationMerchant3
		 ****************
		 *****************/
		int transactionRowNum =3;   
		int outputExcelRowToBeVerifiedRowNum= 1;
		int reconResponseRowNum=1;
		changeAutoRefundStatus(transactionRowNum, helper, AutoRefund.off);

		//Extracting the Citi netbanking dropped transaction details from db for AutomationMerchant2
		testConfig.putRunTimeProperty("merchantid", "5912");
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 87, 1);

		testConfig.putRunTimeProperty("txnId", map.get("txnid"));
		testConfig.putRunTimeProperty("mihpayid", map.get("id"));

		//Recon the transaction
		String payuid = map.get("id");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				"CC", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify downloaded recon file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum, verifyReconResult.dropped_off, reconResponseRowNum);

		//Navigate to dashboard
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 1);
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(map.get("txnid"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.dropped,AutoRefund.off, testConfig);

		/****************
		 ****************
		 Verifying dropped transaction through AXIS gateway is not autorefunded
		 when AutoRefund is set 0
		 Merchant - AutomationMerchant2
		 ****************
		 *****************/
		//Extracting the AXIS netbanking dropped transaction details from db for AutomationMerchant2
		map = DataBase.executeSelectQuery(testConfig, 88, 1);

		testConfig.putRunTimeProperty("mihpayid", map.get("id"));
		testConfig.putRunTimeProperty("txnId", map.get("txnid"));

		//Recon the transaction
		payuid = map.get("id");
		reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				"CC", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		reconUpload(reconFile, testConfig);
		//Verify downloaded recon file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum, verifyReconResult.dropped_off, reconResponseRowNum);

		//Navigate to dashboard
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 1);
		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(map.get("txnid"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.dropped,AutoRefund.off, testConfig);

		/****************
		 ****************
		 Verifying dropped transaction through HDFC2 gateway is not autorefunded
		 when AutoRefund is set 0
		 Merchant - AutomationMerchant2
		 ****************
		 *****************/
		//Extracting the HDFC netbanking dropped transaction details from db for AutomationMerchant2
		map = DataBase.executeSelectQuery(testConfig, 89, 1);

		testConfig.putRunTimeProperty("mihpayid", map.get("id"));
		testConfig.putRunTimeProperty("txnId", map.get("txnid"));

		//Recon the transaction
		payuid = map.get("id");
		reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				payuid, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				"CC", 0);

		reconUpload(reconFile, testConfig);
		//Verify downloaded recon file
		helper.verify_recon(testConfig, payuid);
		verifyReconResponse(testConfig,outputExcelRowToBeVerifiedRowNum, verifyReconResult.dropped_off, reconResponseRowNum);

		//Navigate to dashboard
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();

		waitForActionDetailsRow(testConfig, 1);
		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(map.get("txnid"));

		//Verify transaction in dashboard
		dashboardHelper.transactionsPage.VerifyAutoRefund(helper.transactionData, verifyReconResult.dropped,AutoRefund.off, testConfig);

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
		filePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//"+ filePath;

		FileHandler.write_edit(filePath,"Sheet1", row, 11, mihpayid, 0);
		FileHandler.write_edit(filePath, "Sheet1", row, 3, amt, 0);
		FileHandler.write_edit(filePath, "Sheet1", row, 6, amt, 0);
	}

	/**
	 * 
	 * @param testConfig
	 * @param outputExcelRowToBeVerified
	 */
	public void verifyReconResponse(Config testConfig, int outputExcelRowToBeVerified,verifyReconResult verifyEnum, int reconResponseRow)
	{	
		Browser.wait(testConfig,10);
		String reconfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,reconfilepath);
		String fileName = file.getName();

		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", reconfilepath+fileName);

		TestDataReader trecon = new TestDataReader(testConfig, "ReconResponse");

		Helper.compareEquals(testConfig, "result", trecon.GetData(reconResponseRow, "result"), tr.GetData(outputExcelRowToBeVerified, "result"));
	}


	/*
	 * Redirect to the home link and then navigate to the recon tab in manual update
	 * then finally browse the latest file and then click submit
	 * @param reconFile, name of the reconFile to be uploaded
	 * @param testConfig, object of Config
	 * 
	 */
	public void reconUpload(String reconFile,Config testConfig) 
	{
		Browser.navigateToURL(testConfig, testConfig.getRunTimeProperty("AdminPortalHome")+"/home");
		HomePage homePage =new HomePage(testConfig);
		manualUpdatePage = homePage.ClickManualTransactionUpdate();
		reconPage =manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);
		//ReconcileFileBrowseandSubmit(reconFile);     
	}


	public void waitForActionDetailsRow(Config testConfig, int no_row)
	{
		int retries = 60;
		Map<String,String> map2 = null;

		String mihPayId = testConfig.getRunTimeProperty("mihpayid");

		while(retries>0){
			testConfig.putRunTimeProperty("mihpayid", mihPayId);
			Browser.wait(testConfig, 1);

			map2 = DataBase.executeSelectQuery(testConfig,48, 1);

			if(map2.get("cnt").equals(Integer.toString(no_row)))
				retries = 0;
			else
				retries--;
			System.out.println(retries);
		}
	}


	public void waitForDroppedStatus(Config testConfig)
	{
		int retries = 60;
		Map<String,String> map2 = null;

		String mihPayId = testConfig.getRunTimeProperty("mihpayid");

		while(retries>0){
			testConfig.putRunTimeProperty("mihpayid", mihPayId);
			Browser.wait(testConfig, 2);
			map2 = DataBase.executeSelectQuery(testConfig,4, -1);
			if(map2.get("status").equalsIgnoreCase("requested"))
				retries = 0;
			else
				retries--;
			System.out.println(retries);
		}
	}

	public void verifyViewRequest(DashboardPage dashBoard, String request_id, verifyReconResult reconEnum) {

		RequestsPage request= dashBoard.ClickViewRequest();

		request.SearchTransaction(request_id, SearchOn.RequestId);

		String expected_action ="refund";
		String expected_status="";
		switch(reconEnum){
		case failed:
			expected_status= "queued";
			break;

		case dropped:
			expected_status= "requested";


		default:
			break;
		}

		request.verifyFirstRowStatus(expected_action, expected_status);
	}

	public void changeAutoRefundStatus(int transactionRowNum, TransactionHelper helper, AutoRefund stat)
	{
		helper.transactionData = new TestDataReader(helper.testConfig,"TransactionDetails");
		String merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");

		MerchantListPage merchantListPage = helper.home.clickMerchantList();
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		editMerchantPage = merchantDetailsPage.ClickEditMerchant();
		EditPage editPage;
		editPage = editMerchantPage.clickEdit();

		switch(stat){
		case on:	
			editPage.AutoRefundOn();
			editPage.submit_changes();
			break;

		case off:
			editPage.AutoRefundOff();
			editPage.submit_changes();
			break;
		}
	}
}	



