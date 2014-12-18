package Test.AdminPanel.ReconAndBankFeeSettlement;

import java.io.File;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.ManualUpdate.ReconPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.FileHandler;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;
import Utils.Helper.FileType;

public class BankFeeSettlement extends TestBase {
/*
	public Config testConfig;

	ManualUpdatePage manualUpdatePage;
	ReconPage reconPage;
	MerchantTransactionsPage merchantTransactionsPage;
	TestDataReader bankfeesettlementdata;

	@Test(description = "Perform a captured transaction, upload recon file, do bank fee settlement with/without adding bank fee and tax and verify the transaction data", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestCapturedTransactionReconUpload(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 7;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.verify_txn_update_data(testConfig);
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);
		String payuid = helper.testResponse.actualResponse.get("mihpayid");

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);

		String downloadedfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, downloadedfilepath);
		String fileName = file.getName();

		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"Sheet1", downloadedfilepath + fileName);

		// Column values from Downloaded summary
		String transactionid = downloadedfile.GetData(1, "transactionid");
		String action = downloadedfile.GetData(1, "action");
		String status = downloadedfile.GetData(1, "status");
		String amount = downloadedfile.GetData(1, "amount");
		String servicefee = downloadedfile.GetData(1, "servicefee");
		String servicetax = downloadedfile.GetData(1, "servicetax");
		String net = downloadedfile.GetData(1, "net");
		String utr = Helper.generateRandomAlphaNumericString(5);
		String refnum = downloadedfile.GetData(1, "refnum");
		String category = downloadedfile.GetData(1, "category");
		String cardtype = downloadedfile.GetData(1, "cardtype");
		String requestid = downloadedfile.GetData(1, "requestid");
		String reconciliationid = downloadedfile.GetData(1, "reconciliationid");
		String PayUMID = downloadedfile.GetData(1, "PayUMID");
		String merchantName = downloadedfile.GetData(1, "merchantName");
		String pgMID = downloadedfile.GetData(1, "pgMID");
		String transactionDate = downloadedfile.GetData(1, "transactionDate");
		String refundDate = downloadedfile.GetData(1, "refundDate");
		String payucharge = downloadedfile.GetData(1, "pay u charge");
		String service_tax = downloadedfile.GetData(1, "service tax");
		String settlement_amount = downloadedfile.GetData(1,
				"settlement amount");
		String commission_charge = downloadedfile.GetData(1,
				"commission charge");
		String result = downloadedfile.GetData(1, "result");
		String prev_status = downloadedfile.GetData(1, "prev_status");

		// Entering value in .xls file in Parameters
		String settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);


		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		// Verifying transaction updated successfully
		verify_successful_transaction(testConfig);
		verify_settlementtime(testConfig);

		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount
		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4,
				bankfeesettlementdata.GetData(1, "servicefee"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5,
				bankfeesettlementdata.GetData(1, "servicetax"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6,
				bankfeesettlementdata.GetData(1, "netamount"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		// Verifying transaction updated successfully
		verify_successful_transaction(testConfig);
		verify_settlementtime(testConfig);

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4,
				bankfeesettlementdata.GetData(2, "servicefee"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5,
				bankfeesettlementdata.GetData(2, "servicetax"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6,
				bankfeesettlementdata.GetData(2, "netamount"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		// Verifying transaction updated successfully
		verify_successful_transaction(testConfig);
		verify_settlementtime(testConfig);

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4,
				bankfeesettlementdata.GetData(3, "servicefee"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5,
				bankfeesettlementdata.GetData(3, "servicetax"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6,
				bankfeesettlementdata.GetData(3, "netamount"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		// Verifying transaction updated successfully
		verify_successful_transaction(testConfig);
		verify_settlementtime(testConfig);

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount, such that
		// entered amount is less than servicefee + servicetax + net
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4,
				bankfeesettlementdata.GetData(4, "servicefee"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5,
				bankfeesettlementdata.GetData(4, "servicetax"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6,
				bankfeesettlementdata.GetData(4, "netamount"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(4, "failureReason"), file_result);

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount, such that
		// entered amount is more than servicefee + servicetax + net
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4,
				bankfeesettlementdata.GetData(5, "servicefee"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5,
				bankfeesettlementdata.GetData(5, "servicetax"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6,
				bankfeesettlementdata.GetData(5, "netamount"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(4, "failureReason"), file_result);

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Perform a failed transaction, upload recon file, do bank fee settlement with/without adding bank fee and tax and verify the transaction data", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestFailedTransactionReconUpload(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 1;
		int cardDetailsRowNum = 2;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);

		String downloadedfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, downloadedfilepath);
		String fileName = file.getName();

		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"Sheet1", downloadedfilepath + fileName);

		// Column values from Downloaded summary
		String transactionid = downloadedfile.GetData(1, "transactionid");
		String action = downloadedfile.GetData(1, "action");
		String status = downloadedfile.GetData(1, "status");
		String amount = downloadedfile.GetData(1, "amount");
		String servicefee = downloadedfile.GetData(1, "servicefee");
		String servicetax = downloadedfile.GetData(1, "servicetax");
		String net = downloadedfile.GetData(1, "net");
		String utr = Helper.generateRandomAlphaNumericString(5);
		String refnum = downloadedfile.GetData(1, "refnum");
		String category = downloadedfile.GetData(1, "category");
		String cardtype = downloadedfile.GetData(1, "cardtype");
		String requestid = downloadedfile.GetData(1, "requestid");
		String reconciliationid = downloadedfile.GetData(1, "reconciliationid");
		String PayUMID = downloadedfile.GetData(1, "PayUMID");
		String merchantName = downloadedfile.GetData(1, "merchantName");
		String pgMID = downloadedfile.GetData(1, "pgMID");
		String transactionDate = downloadedfile.GetData(1, "transactionDate");
		String refundDate = downloadedfile.GetData(1, "refundDate");
		String payucharge = downloadedfile.GetData(1, "pay u charge");
		String service_tax = downloadedfile.GetData(1, "service tax");
		String settlement_amount = downloadedfile.GetData(1,
				"settlement amount");
		String commission_charge = downloadedfile.GetData(1,
				"commission charge");
		String result = downloadedfile.GetData(1, "result");
		String prev_status = downloadedfile.GetData(1, "prev_status");

		// Entering value in .xls file in Parameters
		String settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		// Verifying transaction updated successfully
		verify_failed_transaction(testConfig);
		verify_settlementtime(testConfig);

		// Entering value in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount
		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4,
				bankfeesettlementdata.GetData(6, "servicefee"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5,
				bankfeesettlementdata.GetData(6, "servicetax"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6,
				bankfeesettlementdata.GetData(6, "netamount"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		// Verifying transaction updated successfully
		verify_failed_transaction(testConfig);
		verify_settlementtime(testConfig);

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4,
				bankfeesettlementdata.GetData(7, "servicefee"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5,
				bankfeesettlementdata.GetData(7, "servicetax"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6,
				bankfeesettlementdata.GetData(7, "netamount"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		// Verifying transaction updated successfully
		verify_failed_transaction(testConfig);
		verify_settlementtime(testConfig);

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4,
				bankfeesettlementdata.GetData(8, "servicefee"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5,
				bankfeesettlementdata.GetData(8, "servicetax"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6,
				bankfeesettlementdata.GetData(8, "netamount"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		// Verifying transaction updated successfully
		verify_failed_transaction(testConfig);
		verify_settlementtime(testConfig);

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount, such that
		// entered amount is less than servicefee + servicetax + net
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4,
				bankfeesettlementdata.GetData(9, "servicefee"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5,
				bankfeesettlementdata.GetData(9, "servicetax"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6,
				bankfeesettlementdata.GetData(9, "netamount"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(9, "failureReason"), file_result);

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount, such that
		// entered amount is more than servicefee + servicetax + net
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4,
				bankfeesettlementdata.GetData(10, "servicefee"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5,
				bankfeesettlementdata.GetData(10, "servicetax"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6,
				bankfeesettlementdata.GetData(10, "netamount"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(10, "failureReason"), file_result);

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify if bank fee of dropped transactions can be settled", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestDroppedTransactionReconUpload(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 7, 1);
		String payuid = map.get("id");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, map.get("id"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4, map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7, map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11, "CC", 0);

		// Navigating to Admin Recon
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();
		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);

		String downloadedfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, downloadedfilepath);
		String fileName = file.getName();

		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"Sheet1", downloadedfilepath + fileName);

		// Column values from Downloaded summary
		String transactionid = downloadedfile.GetData(1, "transactionid");
		String action = downloadedfile.GetData(1, "action");
		String status = downloadedfile.GetData(1, "status");
		String amount = downloadedfile.GetData(1, "amount");
		String servicefee = downloadedfile.GetData(1, "servicefee");
		String servicetax = downloadedfile.GetData(1, "servicetax");
		String net = downloadedfile.GetData(1, "net");
		String utr = Helper.generateRandomAlphaNumericString(5);
		String refnum = downloadedfile.GetData(1, "refnum");
		String category = downloadedfile.GetData(1, "category");
		String cardtype = downloadedfile.GetData(1, "cardtype");
		String requestid = downloadedfile.GetData(1, "requestid");
		String reconciliationid = downloadedfile.GetData(1, "reconciliationid");
		String PayUMID = downloadedfile.GetData(1, "PayUMID");
		String merchantName = downloadedfile.GetData(1, "merchantName");
		String pgMID = downloadedfile.GetData(1, "pgMID");
		String transactionDate = downloadedfile.GetData(1, "transactionDate");
		String refundDate = downloadedfile.GetData(1, "refundDate");
		String payucharge = downloadedfile.GetData(1, "pay u charge");
		String service_tax = downloadedfile.GetData(1, "service tax");
		String settlement_amount = downloadedfile.GetData(1,
				"settlement amount");
		String commission_charge = downloadedfile.GetData(1,
				"commission charge");
		String result = downloadedfile.GetData(1, "result");
		String prev_status = downloadedfile.GetData(1, "prev_status");

		// Entering value in .xls file in Parameters
		String settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		// Verifying transaction updated successfully
		verify_dropped_transaction(testConfig);

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount, such that
		// entered amount is less than servicefee + servicetax + net
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount
		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(11, "failureReason"), file_result);

		// Entering value in .xls file in Parameters with updated bank fee, bank
		// tax and net amount, such that
		// entered amount is more than servicefee + servicetax + net
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4,
				bankfeesettlementdata.GetCurrentEnvironmentData(11,
						"servicefee"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5,
				bankfeesettlementdata.GetCurrentEnvironmentData(11,
						"servicetax"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6,
				bankfeesettlementdata
				.GetCurrentEnvironmentData(11, "netamount"), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(11, "failureReason"), file_result);

		Assert.assertTrue(testConfig.getTestResult());

	}


	@Test(description = "Verify if bank fee of failed transactions can be settled, when invalid data is entered in "
			+ "fields: UTR, refnum, category, cardtype, reconciliationid, PayUMID, merchantName, pgMID, transactionDate, "
			+ "refundDate, pay u charge, service tax, settlement amount, settlement amount, "
			+ "commission charge, result and prev_status", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestInvalidFieldsFailedTransactionSettled(Config testConfig){
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 1;
		int cardDetailsRowNum = 2;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);
		String payuid = helper.testResponse.actualResponse.get("mihpayid");

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);

		String downloadedfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, downloadedfilepath);
		String fileName = file.getName();

		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"Sheet1", downloadedfilepath + fileName);

		// Column values from Downloaded summary
		String transactionid = downloadedfile.GetData(1, "transactionid");
		String action = downloadedfile.GetData(1, "action");
		String status = downloadedfile.GetData(1, "status");
		String amount = downloadedfile.GetData(1, "amount");
		String servicefee = downloadedfile.GetData(1, "servicefee");
		String servicetax = downloadedfile.GetData(1, "servicetax");
		String net = downloadedfile.GetData(1, "net");
		String requestid = downloadedfile.GetData(1, "requestid");

		// Entering value in .xls file in Parameters
		String settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 16,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23,
				Helper.generateRandomAlphabetsString(5), 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		// Verifying transaction updated successfully
		verify_failed_transaction(testConfig);

		Assert.assertTrue(testConfig.getTestResult());

	}


	@Test(description = "Verify if bank fee of dropped transactions can be settled, when blank data is entered in "
			+ "fields: UTR, refnum, category, cardtype, reconciliationid, PayUMID, merchantName, pgMID, transactionDate, "
			+ "refundDate, pay u charge, service tax, settlement amount, settlement amount, "
			+ "commission charge, result and prev_status", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestBlankFieldsDroppedTransactionSettled(Config testConfig){
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 7, 1);
		String payuid = map.get("id");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, map.get("id"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4, map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7, map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11, "CC", 0);

		// Navigating to Admin Recon
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();
		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);

		String downloadedfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, downloadedfilepath);
		String fileName = file.getName();

		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"Sheet1", downloadedfilepath + fileName);

		// Column values from Downloaded summary
		String transactionid = downloadedfile.GetData(1, "transactionid");
		String action = downloadedfile.GetData(1, "action");
		String status = downloadedfile.GetData(1, "status");
		String amount = downloadedfile.GetData(1, "amount");
		String servicefee = downloadedfile.GetData(1, "servicefee");
		String servicetax = downloadedfile.GetData(1, "servicetax");
		String net = downloadedfile.GetData(1, "net");
		String requestid = downloadedfile.GetData(1, "requestid");

		// Entering value in .xls file in Parameters
		String settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 16, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, "", 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		// Verifying transaction updated successfully
		verify_dropped_transaction(testConfig);

		Assert.assertTrue(testConfig.getTestResult());

	}

	// Negative Cases

	@Test(description = "Verify if bank fee of successful transactions cannot be settled with invalid data in parameters", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestInvalidDataInCapturedTransactionUpload(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 7;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.verify_txn_update_data(testConfig);
		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);

		String downloadedfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, downloadedfilepath);
		String fileName = file.getName();

		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"Sheet1", downloadedfilepath + fileName);

		// Column values from Downloaded summary
		String transactionid = downloadedfile.GetData(1, "transactionid");
		String action = downloadedfile.GetData(1, "action");
		String status = downloadedfile.GetData(1, "status");
		String amount = downloadedfile.GetData(1, "amount");
		String servicefee = downloadedfile.GetData(1, "servicefee");
		String servicetax = downloadedfile.GetData(1, "servicetax");
		String net = downloadedfile.GetData(1, "net");
		String utr = Helper.generateRandomAlphaNumericString(5);
		String refnum = downloadedfile.GetData(1, "refnum");
		String category = downloadedfile.GetData(1, "category");
		String cardtype = downloadedfile.GetData(1, "cardtype");
		String requestid = downloadedfile.GetData(1, "requestid");
		String reconciliationid = downloadedfile.GetData(1, "reconciliationid");
		String PayUMID = downloadedfile.GetData(1, "PayUMID");
		String merchantName = downloadedfile.GetData(1, "merchantName");
		String pgMID = downloadedfile.GetData(1, "pgMID");
		String transactionDate = downloadedfile.GetData(1, "transactionDate");
		String refundDate = downloadedfile.GetData(1, "refundDate");
		String payucharge = downloadedfile.GetData(1, "pay u charge");
		String service_tax = downloadedfile.GetData(1, "service tax");
		String settlement_amount = downloadedfile.GetData(1,
				"settlement amount");
		String commission_charge = downloadedfile.GetData(1,
				"commission charge");
		String result = downloadedfile.GetData(1, "result");
		String prev_status = downloadedfile.GetData(1, "prev_status");

		// Entering invalid status and valid values in .xls file in Parameters
		String settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(15, "failureReason"), file_result);

		// Entering invalid action and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(16, "failureReason"), file_result);

		// Entering invalid requestid and valid values in .xls file in
		// Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(17, "failureReason"), file_result);

		// Entering invalid payuid and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(18, "failureReason"), file_result);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify if bank fee of failed transactions cannot be settled with invalid data in parameters", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestInvalidDataInFailedTransactionUpload(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 1;
		int cardDetailsRowNum = 2;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);
		String payuid = helper.testResponse.actualResponse.get("mihpayid");

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);

		String downloadedfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, downloadedfilepath);
		String fileName = file.getName();

		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"Sheet1", downloadedfilepath + fileName);

		// Column values from Downloaded summary
		String transactionid = downloadedfile.GetData(1, "transactionid");
		String action = downloadedfile.GetData(1, "action");
		String status = downloadedfile.GetData(1, "status");
		String amount = downloadedfile.GetData(1, "amount");
		String servicefee = downloadedfile.GetData(1, "servicefee");
		String servicetax = downloadedfile.GetData(1, "servicetax");
		String net = downloadedfile.GetData(1, "net");
		String utr = Helper.generateRandomAlphaNumericString(5);
		String refnum = downloadedfile.GetData(1, "refnum");
		String category = downloadedfile.GetData(1, "category");
		String cardtype = downloadedfile.GetData(1, "cardtype");
		String requestid = downloadedfile.GetData(1, "requestid");
		String reconciliationid = downloadedfile.GetData(1, "reconciliationid");
		String PayUMID = downloadedfile.GetData(1, "PayUMID");
		String merchantName = downloadedfile.GetData(1, "merchantName");
		String pgMID = downloadedfile.GetData(1, "pgMID");
		String transactionDate = downloadedfile.GetData(1, "transactionDate");
		String refundDate = downloadedfile.GetData(1, "refundDate");
		String payucharge = downloadedfile.GetData(1, "pay u charge");
		String service_tax = downloadedfile.GetData(1, "service tax");
		String settlement_amount = downloadedfile.GetData(1,
				"settlement amount");
		String commission_charge = downloadedfile.GetData(1,
				"commission charge");
		String result = downloadedfile.GetData(1, "result");
		String prev_status = downloadedfile.GetData(1, "prev_status");

		// Entering invalid status and valid values in .xls file in Parameters
		String settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(15, "failureReason"), file_result);

		// Entering invalid action and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(16, "failureReason"), file_result);

		// Entering invalid requestid and valid values in .xls file in
		// Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(17, "failureReason"), file_result);

		// Entering invalid payuid and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(18, "failureReason"), file_result);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify if bank fee of dropped transactions cannot be settled with invalid data in parameters", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestInvalidDataInDroppedTransactionUpload(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 7, 1);
		String payuid = map.get("id");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, map.get("id"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4, map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7, map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11, "CC", 0);

		// Navigating to Admin Recon
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();
		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);

		String downloadedfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, downloadedfilepath);
		String fileName = file.getName();

		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"Sheet1", downloadedfilepath + fileName);

		// Column values from Downloaded summary
		String transactionid = downloadedfile.GetData(1, "transactionid");
		String action = downloadedfile.GetData(1, "action");
		String status = downloadedfile.GetData(1, "status");
		String amount = downloadedfile.GetData(1, "amount");
		String servicefee = downloadedfile.GetData(1, "servicefee");
		String servicetax = downloadedfile.GetData(1, "servicetax");
		String net = downloadedfile.GetData(1, "net");
		String utr = Helper.generateRandomAlphaNumericString(5);
		String refnum = downloadedfile.GetData(1, "refnum");
		String category = downloadedfile.GetData(1, "category");
		String cardtype = downloadedfile.GetData(1, "cardtype");
		String requestid = downloadedfile.GetData(1, "requestid");
		String reconciliationid = downloadedfile.GetData(1, "reconciliationid");
		String PayUMID = downloadedfile.GetData(1, "PayUMID");
		String merchantName = downloadedfile.GetData(1, "merchantName");
		String pgMID = downloadedfile.GetData(1, "pgMID");
		String transactionDate = downloadedfile.GetData(1, "transactionDate");
		String refundDate = downloadedfile.GetData(1, "refundDate");
		String payucharge = downloadedfile.GetData(1, "pay u charge");
		String service_tax = downloadedfile.GetData(1, "service tax");
		String settlement_amount = downloadedfile.GetData(1,
				"settlement amount");
		String commission_charge = downloadedfile.GetData(1,
				"commission charge");
		String result = downloadedfile.GetData(1, "result");
		String prev_status = downloadedfile.GetData(1, "prev_status");

		// Entering invalid status and valid values in .xls file in Parameters
		String settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(15, "failureReason"), file_result);

		// Entering invalid action and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(16, "failureReason"), file_result);

		// Entering invalid requestid and valid values in .xls file in
		// Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(17, "failureReason"), file_result);

		// Entering invalid payuid and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0,
				Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(18, "failureReason"), file_result);

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify if bank fee of successful transactions cannot be settled with blank data in parameters", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestBlankDataInCapturedTransactionUpload(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 7;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.verify_txn_update_data(testConfig);
		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);

		String downloadedfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, downloadedfilepath);
		String fileName = file.getName();

		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"Sheet1", downloadedfilepath + fileName);

		// Column values from Downloaded summary
		String transactionid = downloadedfile.GetData(1, "transactionid");
		String action = downloadedfile.GetData(1, "action");
		String status = downloadedfile.GetData(1, "status");
		String amount = downloadedfile.GetData(1, "amount");
		String servicefee = downloadedfile.GetData(1, "servicefee");
		String servicetax = downloadedfile.GetData(1, "servicetax");
		String net = downloadedfile.GetData(1, "net");
		String utr = Helper.generateRandomAlphaNumericString(5);
		String refnum = downloadedfile.GetData(1, "refnum");
		String category = downloadedfile.GetData(1, "category");
		String cardtype = downloadedfile.GetData(1, "cardtype");
		String requestid = downloadedfile.GetData(1, "requestid");
		String reconciliationid = downloadedfile.GetData(1, "reconciliationid");
		String PayUMID = downloadedfile.GetData(1, "PayUMID");
		String merchantName = downloadedfile.GetData(1, "merchantName");
		String pgMID = downloadedfile.GetData(1, "pgMID");
		String transactionDate = downloadedfile.GetData(1, "transactionDate");
		String refundDate = downloadedfile.GetData(1, "refundDate");
		String payucharge = downloadedfile.GetData(1, "pay u charge");
		String service_tax = downloadedfile.GetData(1, "service tax");
		String settlement_amount = downloadedfile.GetData(1,
				"settlement amount");
		String commission_charge = downloadedfile.GetData(1,
				"commission charge");
		String result = downloadedfile.GetData(1, "result");
		String prev_status = downloadedfile.GetData(1, "prev_status");

		// Entering invalid status and valid values in .xls file in Parameters
		String settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(15, "failureReason"), file_result);

		// Entering invalid action and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(16, "failureReason"), file_result);

		// Entering invalid requestid and valid values in .xls file in
		// Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(17, "failureReason"), file_result);

		// Entering invalid payuid and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(18, "failureReason"), file_result);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify if bank fee of failed transactions cannot be settled with blank data in parameters", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestBlankDataInFailedTransactionUpload(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 1;
		int cardDetailsRowNum = 2;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);

		String downloadedfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, downloadedfilepath);
		String fileName = file.getName();

		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"Sheet1", downloadedfilepath + fileName);

		// Column values from Downloaded summary
		String transactionid = downloadedfile.GetData(1, "transactionid");
		String action = downloadedfile.GetData(1, "action");
		String status = downloadedfile.GetData(1, "status");
		String amount = downloadedfile.GetData(1, "amount");
		String servicefee = downloadedfile.GetData(1, "servicefee");
		String servicetax = downloadedfile.GetData(1, "servicetax");
		String net = downloadedfile.GetData(1, "net");
		String utr = Helper.generateRandomAlphaNumericString(5);
		String refnum = downloadedfile.GetData(1, "refnum");
		String category = downloadedfile.GetData(1, "category");
		String cardtype = downloadedfile.GetData(1, "cardtype");
		String requestid = downloadedfile.GetData(1, "requestid");
		String reconciliationid = downloadedfile.GetData(1, "reconciliationid");
		String PayUMID = downloadedfile.GetData(1, "PayUMID");
		String merchantName = downloadedfile.GetData(1, "merchantName");
		String pgMID = downloadedfile.GetData(1, "pgMID");
		String transactionDate = downloadedfile.GetData(1, "transactionDate");
		String refundDate = downloadedfile.GetData(1, "refundDate");
		String payucharge = downloadedfile.GetData(1, "pay u charge");
		String service_tax = downloadedfile.GetData(1, "service tax");
		String settlement_amount = downloadedfile.GetData(1,
				"settlement amount");
		String commission_charge = downloadedfile.GetData(1,
				"commission charge");
		String result = downloadedfile.GetData(1, "result");
		String prev_status = downloadedfile.GetData(1, "prev_status");

		// Entering invalid status and valid values in .xls file in Parameters
		String settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(15, "failureReason"), file_result);

		// Entering invalid action and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(16, "failureReason"), file_result);

		// Entering invalid requestid and valid values in .xls file in
		// Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(17, "failureReason"), file_result);

		// Entering blank payuid and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(18, "failureReason"), file_result);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify if bank fee of dropped transactions cannot be settled with blank data in parameters", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void TestBlankDataInDroppedTransactionUpload(Config testConfig)  {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 7, 1);
		String payuid = map.get("id");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1, map.get("id"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4, map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7, map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11, "CC", 0);

		// Navigating to Admin Recon
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();
		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);

		String downloadedfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, downloadedfilepath);
		String fileName = file.getName();

		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"Sheet1", downloadedfilepath + fileName);

		// Column values from Downloaded summary
		String transactionid = downloadedfile.GetData(1, "transactionid");
		String action = downloadedfile.GetData(1, "action");
		String status = downloadedfile.GetData(1, "status");
		String amount = downloadedfile.GetData(1, "amount");
		String servicefee = downloadedfile.GetData(1, "servicefee");
		String servicetax = downloadedfile.GetData(1, "servicetax");
		String net = downloadedfile.GetData(1, "net");
		String utr = Helper.generateRandomAlphaNumericString(5);
		String refnum = downloadedfile.GetData(1, "refnum");
		String category = downloadedfile.GetData(1, "category");
		String cardtype = downloadedfile.GetData(1, "cardtype");
		String requestid = downloadedfile.GetData(1, "requestid");
		String reconciliationid = downloadedfile.GetData(1, "reconciliationid");
		String PayUMID = downloadedfile.GetData(1, "PayUMID");
		String merchantName = downloadedfile.GetData(1, "merchantName");
		String pgMID = downloadedfile.GetData(1, "pgMID");
		String transactionDate = downloadedfile.GetData(1, "transactionDate");
		String refundDate = downloadedfile.GetData(1, "refundDate");
		String payucharge = downloadedfile.GetData(1, "pay u charge");
		String service_tax = downloadedfile.GetData(1, "service tax");
		String settlement_amount = downloadedfile.GetData(1,
				"settlement amount");
		String commission_charge = downloadedfile.GetData(1,
				"commission charge");
		String result = downloadedfile.GetData(1, "result");
		String prev_status = downloadedfile.GetData(1, "prev_status");

		// Entering blank status and valid values in .xls file in Parameters
		String settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(15, "failureReason"), file_result);

		// Entering blank action and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(16, "failureReason"), file_result);

		// Entering blank requestid and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, transactionid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(17, "failureReason"), file_result);

		// Entering blank payuid and valid values in .xls file in Parameters
		settledFile = Helper.getExcelFile(testConfig, FileType.bankSettledRecon);

		FileHandler.write_edit(settledFile, "Sheet1", 1, 0, "", 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 1, action, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 2, status, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 3, amount, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 4, servicefee, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 5, servicetax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 6, net, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 7, utr, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 8, refnum, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 9, category, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 10, cardtype, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 11, requestid, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 12, reconciliationid,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 13, PayUMID, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 14, merchantName, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 15, pgMID, 0);
		FileHandler
		.write_edit(settledFile, "Sheet1", 1, 16, transactionDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 17, refundDate, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 18, payucharge, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 19, service_tax, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 20, settlement_amount,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 21, commission_charge,
				0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 22, result, 0);
		FileHandler.write_edit(settledFile, "Sheet1", 1, 23, prev_status, 0);

		reconPage.uploadBankFeeSettlement(testConfig, settledFile);

		settledfilepath = "C://Users//" + System.getProperty("user.name")
				+ "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig, "Sheet1",
				settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				bankfeesettlementdata.GetData(18, "failureReason"), file_result);

		Assert.assertTrue(testConfig.getTestResult());

	}

	*//**
	 * @param testConfig
	 *//*
	public void verify_settlementtime(Config testConfig) {
		Helper.compareEquals(testConfig, "Settlement Time", "", "");
		testConfig
		.logComment("Settlement time can not be verified, due to redmine: 20477");

	}

	*//**
	 * @param testConfig
	 *//*
	public void verify_successful_transaction(Config testConfig) {
		String settlementFilePath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, settlementFilePath);
		String settled_fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settlementFilePath + settled_fileName);
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");

		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 4, 1);
		Float amount = new Float(map.get("amount"));
		Float bank_service_fee = new Float(map.get("bank_service_fee"));
		Float bank_service_tax = new Float(map.get("bank_service_tax"));
		Float bank_net_amount = new Float(map.get("bank_net_amount"));

		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		Helper.compareEquals(testConfig, "Transaction Id", map.get("txnid"),testDataReader.GetData(1, "transactionid"));
		Helper.compareEquals(testConfig, "Action done", map.get("action"),testDataReader.GetData(1, "action"));
		Helper.compareEquals(testConfig, "Current Status", map.get("status"),testDataReader.GetData(1, "status").toUpperCase());
		Helper.compareEquals(testConfig, "Transaction Amount",	amount.toString(), testDataReader.GetData(1, "amount"));

		if (map.get("bank_service_fee").equals("0.00")) {
			String bankfee = bank_service_fee.toString();
			int fee = 0;
			bankfee = "" + fee;
			Helper.compareContains(testConfig, "Bank Service Fee", bankfee,
					testDataReader.GetData(1, "servicefee"));
		} else if (map.get("bank_service_fee").equals("1.00")) {
			String bankfee = bank_service_fee.toString();
			double fee = 1.0;
			bankfee = "" + fee;
			Helper.compareContains(testConfig, "Bank Service Fee", bankfee,
					testDataReader.GetData(1, "servicefee"));
		} else {
			Helper.compareEquals(testConfig, "Bank Service Fee",
					bank_service_fee.toString(),
					testDataReader.GetData(1, "servicefee"));
		}
		if (map.get("bank_service_tax").equals("0.00")) {
			String banktax = bank_service_tax.toString();
			int fee = 0;
			banktax = "" + fee;
			Helper.compareContains(testConfig, "Bank Service Tax", banktax,
					testDataReader.GetData(1, "servicetax"));
		} else {
			Helper.compareEquals(testConfig, "Bank Service Tax",
					bank_service_tax.toString(),
					testDataReader.GetData(1, "servicetax"));
		}
		if (map.get("bank_net_amount").equals("0.00")) {
			String banknetamount = bank_net_amount.toString();
			double fee = 0.0;
			banknetamount = "" + fee;
			Helper.compareContains(testConfig, "Bank Net Amount",
					banknetamount, testDataReader.GetData(1, "net"));
		} else if (map.get("bank_net_amount").equals("1.00")) {
			String banknetamount = bank_net_amount.toString();
			double fee = 1.0;
			banknetamount = "" + fee;
			Helper.compareContains(testConfig, "Bank Net Amount",
					banknetamount, testDataReader.GetData(1, "net"));
		} else if (map.get("bank_net_amount").endsWith(".00")) {
			Helper.compareContains(testConfig, "Bank Net Amount",
					bank_net_amount.toString(),
					testDataReader.GetData(1, "net"));
		} else {
			Helper.compareContains(testConfig, "Net Amount",
					bank_net_amount.toString(),
					testDataReader.GetData(1, "net"));
		}
		if (map.get("bank_utr").equals("")){
			Helper.compareEquals(testConfig, "UTR", "{skip}",testDataReader.GetData(1, "utr"));
		}
		else{
			Helper.compareEquals(testConfig, "UTR", map.get("bank_utr"),testDataReader.GetData(1, "utr")); 
		}
		Helper.compareEquals(testConfig, "Request Id", map.get("id"),testDataReader.GetData(1, "requestid"));
		Helper.compareEquals(testConfig, "Reconciliation Id", map.get("reconciliation_id"), testDataReader.GetData(1, "reconciliationid"));
		Helper.compareEquals(testConfig, "File Result",	bankfeesettlementdata.GetData(12, "successVerify"), file_result);

	}

	*//**
	 * @param testConfig
	 *//*
	public void verify_failed_transaction(Config testConfig) {
		String settlementFilePath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, settlementFilePath);
		String settled_fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settlementFilePath + settled_fileName);
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");

		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 4, 1);
		Float amount = new Float(map.get("amount"));
		Float bank_service_fee = new Float(map.get("bank_service_fee"));
		Float bank_service_tax = new Float(map.get("bank_service_tax"));

		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		Helper.compareEquals(testConfig, "Transaction Id", map.get("txnid"),
				testDataReader.GetData(1, "transactionid"));
		Helper.compareEquals(testConfig, "Action done", map.get("action"),
				testDataReader.GetData(1, "action"));
		Helper.compareEquals(testConfig, "Current Status", map.get("status"),
				testDataReader.GetData(1, "status"));

		if (map.get("amount").equals("2.00")) {
			String bankamount = amount.toString();
			int fee = 2;
			bankamount = "" + fee;
			Helper.compareContains(testConfig, "Bank Amount", bankamount,
					testDataReader.GetData(1, "amount"));
		} else {
			Helper.compareEquals(testConfig, "Bank Amount", amount.toString(),
					testDataReader.GetData(1, "amount"));
		}
		if (map.get("bank_service_fee").equals("0.00")) {
			String bankfee = bank_service_fee.toString();
			int fee = 0;
			bankfee = "" + fee;
			Helper.compareContains(testConfig, "Bank Service Fee", bankfee,
					testDataReader.GetData(1, "servicefee"));
		} else if (map.get("bank_service_fee").equals("2.00")) {
			String bankfee = bank_service_fee.toString();
			int fee = 2;
			bankfee = "" + fee;
			Helper.compareContains(testConfig, "Bank Service Fee", bankfee,
					testDataReader.GetData(1, "servicefee"));
		} else {
			Helper.compareEquals(testConfig, "Bank Service Fee",
					bank_service_fee.toString(),
					testDataReader.GetData(1, "servicefee"));
		}
		if (map.get("bank_service_tax").equals("0.00")) {
			String banktax = bank_service_tax.toString();
			int fee = 0;
			banktax = "" + fee;
			Helper.compareContains(testConfig, "Bank Service Tax", banktax,
					testDataReader.GetData(1, "servicetax"));
		} else {
			Helper.compareEquals(testConfig, "Bank Service Tax",
					bank_service_tax.toString(),
					testDataReader.GetData(1, "servicetax"));
		}
		if (map.get("bank_net_amount").equals("0.00")) {
			Float bank_net_amount = new Float(map.get("bank_net_amount"));
			String banknetamount = bank_net_amount.toString();
			int fee = 0;
			banknetamount = "" + fee;
			Helper.compareContains(testConfig, "Bank Net Amount",
					banknetamount, testDataReader.GetData(1, "net"));
		} else if (map.get("bank_net_amount").equals("2.00")) {
			String banknetamount = (map.get("bank_net_amount"));
			Helper.compareContains(testConfig, "Bank Net Amount",
					banknetamount, testDataReader.GetData(1, "net"));
		} else if (map.get("bank_net_amount").endsWith("0")) {
			Float bank_net_amount = new Float(map.get("bank_net_amount"));
			bank_net_amount = (float) bank_net_amount.doubleValue();
			Helper.compareContains(testConfig, "Bank Net Amount",
					bank_net_amount.toString(),
					testDataReader.GetData(1, "net"));
		}

		if (map.get("bank_utr").equals("")){
			Helper.compareEquals(testConfig, "UTR", "{skip}",testDataReader.GetData(1, "utr"));
		}
		else{
			Helper.compareEquals(testConfig, "UTR", map.get("bank_utr"),testDataReader.GetData(1, "utr")); 
		}
		Helper.compareEquals(testConfig, "Request Id", map.get("id"),testDataReader.GetData(1, "requestid"));
		Helper.compareEquals(testConfig, "Reconciliation Id", map.get("reconciliation_id"), testDataReader.GetData(1, "reconciliationid"));
		Helper.compareEquals(testConfig, "File Result",	bankfeesettlementdata.GetData(12, "successVerify"), file_result);

	}

	*//**
	 * @param testConfig
	 *//*
	public void verify_dropped_transaction(Config testConfig) {
		String settlementFilePath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, settlementFilePath);
		String settled_fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settlementFilePath + settled_fileName);
		bankfeesettlementdata = new TestDataReader(testConfig,
				"BankFeeSettlement");

		String mihpayid = testDataReader.GetCurrentEnvironmentData(1,
				"transactionid");
		testConfig.putRunTimeProperty("mihpayid", mihpayid);

		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 4, 1);
		Float amount = new Float(map.get("amount"));
		Float bank_service_fee = new Float(map.get("bank_service_fee"));
		Float bank_service_tax = new Float(map.get("bank_service_tax"));

		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		Helper.compareEquals(testConfig, "Transaction Id", map.get("txnid"),
				testDataReader.GetData(1, "transactionid"));
		Helper.compareEquals(testConfig, "Action done", map.get("action"),
				testDataReader.GetData(1, "action"));
		Helper.compareEquals(testConfig, "Current Status", map.get("status"),
				testDataReader.GetData(1, "status"));
		Helper.compareEquals(testConfig, "Transaction Amount",
				amount.toString(), testDataReader.GetData(1, "amount") + ".0");

		if (map.get("bank_service_fee").equals("0.00")) {
			String bankfee = bank_service_fee.toString();
			int fee = 0;
			bankfee = "" + fee;
			Helper.compareContains(testConfig, "Bank Service Fee", bankfee,
					testDataReader.GetData(1, "servicefee"));
		} else if (map.get("bank_service_fee").equals("1.00")) {
			String bankfee = bank_service_fee.toString();
			int fee = 1;
			bankfee = "" + fee;
			Helper.compareContains(testConfig, "Bank Service Fee", bankfee,
					testDataReader.GetData(1, "servicefee"));
		} else {
			Helper.compareEquals(testConfig, "Bank Service Fee",
					bank_service_fee.toString(),
					testDataReader.GetData(1, "servicefee"));
		}
		if (map.get("bank_service_tax").equals("0.00")) {
			String banktax = bank_service_tax.toString();
			int fee = 0;
			banktax = "" + fee;
			Helper.compareContains(testConfig, "Bank Service Tax", banktax,
					testDataReader.GetData(1, "servicetax"));
		} else {
			Helper.compareEquals(testConfig, "Bank Service Tax",
					bank_service_tax.toString(),
					testDataReader.GetData(1, "servicetax"));
		}
		if (map.get("bank_net_amount").equals("0.00")) {
			Float bank_net_amount = new Float(map.get("bank_net_amount"));
			String banknetamount = bank_net_amount.toString();
			int fee = 0;
			banknetamount = "" + fee;
			Helper.compareContains(testConfig, "Bank Net Amount",
					banknetamount, testDataReader.GetData(1, "net"));
		} else if (map.get("bank_net_amount").equals("1.00")) {
			Float bank_net_amount = new Float(map.get("bank_net_amount"));
			String banknetamount = bank_net_amount.toString();
			int fee = 1;
			banknetamount = "" + fee;
			Helper.compareContains(testConfig, "Bank Net Amount",
					banknetamount, testDataReader.GetData(1, "net"));
		} else {
			Helper.compareEquals(testConfig, "Net Amount",
					map.get("bank_net_amount").toString(),
					testDataReader.GetData(1, "net"));
		}
		if (map.get("bank_utr").equals("")){
			Helper.compareEquals(testConfig, "UTR", "{skip}",testDataReader.GetData(1, "utr"));
		}
		else{
			Helper.compareEquals(testConfig, "UTR", map.get("bank_utr"),testDataReader.GetData(1, "utr")); 
		}
		Helper.compareEquals(testConfig, "Request Id", map.get("id"),testDataReader.GetData(1, "requestid"));
		Helper.compareEquals(testConfig, "Reconciliation Id", map.get("reconciliation_id"), testDataReader.GetData(1, "reconciliationid"));
		Helper.compareEquals(testConfig, "File Result",	bankfeesettlementdata.GetData(12, "successVerify"), file_result);

	}

*/
}
