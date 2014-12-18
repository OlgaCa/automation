package Test.MerchantPanel.Billing;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.ManualUpdate.MerchantSettlement;
import PageObject.AdminPanel.ManualUpdate.ReconPage;
import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsMerchantParamsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Billing.BillingsPage;
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

public class TestBilling extends TestBase {

	ManualUpdatePage manualUpdatePage;
	ReconPage reconPage;
	MerchantSettlement merchantSettlement;
	DashboardPage dashBoard;
	BillingsPage billingsPage;
	MerchantTransactionsPage merchantTransactionsPage;
	TestDataReader testDataReader;
	MerchantDetailsPage merchantDetailsPage;
	ParamsPage paramsPage;
	ParamsMerchantParamsPage paramsMerchantParamsPage;
	
	

	public enum AdditionalChargeType
	{
		PERC, FLAT;
	}

	@Test(description = "Perform a captured transaction, upload settlement file and verify the transaction data", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestCapturedEsscomSettlementUpload(Config testConfig) {
		SoftAssert soft=new SoftAssert();

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		

		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;		

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);

		//String amount1 = helper.testResponse.actualResponse.get("amount");
		//double amt1 = Double.parseDouble(amount1);
		helper.verify_txn_update_data(testConfig);
		String IstMihpayid = helper.testResponse.actualResponse.get("mihpayid");

		String settlementFile = Helper.getExcelFile(testConfig, FileType.esscomSettlement);

		FileHandler.write_edit(settlementFile,"Sheet1", 1, 4, helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 6, helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 7, helper.GetTxnUpdateData()[0], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 8, helper.GetTxnUpdateData()[1], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 9, helper.testResponse.actualResponse.get("amount"), 0);

		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 10, random_UTR, 0);

		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 11, "'"+current_date, 0);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);

		//String amount2 = helper.testResponse.actualResponse.get("amount");
		helper.verify_txn_update_data(testConfig);

		String IIndMihpayid = helper.testResponse.actualResponse.get("mihpayid");
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 4, helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 6, helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 7, helper.GetTxnUpdateData()[0], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 8, helper.GetTxnUpdateData()[1], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 9, helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 10, random_UTR, 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 11, "'"+current_date, 0);

		/*double amt2 = Double.parseDouble(amount2);
		amt2 = amt1+amt2;	
		String amount = String.valueOf(amt2)+"0";*/

		//Navigating to Merchant Settlement 		
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();
		merchantSettlement.esscomSettlementFileUpload(testConfig, settlementFile);

		verify_esscom_settlement_output(testConfig, IstMihpayid, IIndMihpayid);

		//Esscom settled transactions are no longer shown on billings panel
		/*Browser.wait(testConfig, 2);

		//Navigating to Dashboard's Billing tab
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		//Verify Recent payment,Settlement data, settlement id and UTR number in Billing Tab's Most Recent Payments
		Helper.compareEquals(testConfig, "Settlement Id", "Most recent payment - "+settlementid[0].toUpperCase(), billingsPage.SettlementId());
		Helper.compareEquals(testConfig, "UTR value", random_UTR, billingsPage.UTR());
		Helper.compareEquals(testConfig, "Transaction Amount", amount, billingsPage.getTransactionAmount());
		Helper.compareEquals(testConfig, "TDR Fee", helper.GetTxnUpdateData()[0], billingsPage.getTDRFee());
		Helper.compareEquals(testConfig, "Service Tax Amount", helper.GetTxnUpdateData()[1], billingsPage.getServiceTax());
		Helper.compareEquals(testConfig, "Net Amount", amount, billingsPage.getNetAmount());
		Helper.compareEquals(testConfig, "Total Settlement Amount", amount, billingsPage.getTotalSettlement());

		//Verify Settled transaction in View Transactions
		String noOfTransactions = billingsPage.getNoOfTransactions();
		merchantTransactionsPage = billingsPage.clickUTR(random_UTR);		
		Helper.compareEquals(testConfig, "No. of Records", noOfTransactions, merchantTransactionsPage.getNumberRecords());

		//Verify transaction detail
		dashboardHelper.transactionsPage = merchantTransactionsPage.selectBillingTransaction();
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);
		 */
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Perform a captured transaction, upload settlement file and verify the transaction data", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestCapturedEsscomSettlementUploadWithCF(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 64;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;		

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		String amnt1 = helper.testResponse.actualResponse.get("amount");
		double amount1 = Double.parseDouble(amnt1);

		String addCharge = helper.testResponse.actualResponse.get("additionalCharges");
		double additionalCharges = Double.parseDouble(addCharge);
		amount1 = amount1+additionalCharges;	
		String transactionAmount1 = String.valueOf(amount1)+"0";

		//double amt1 = Double.parseDouble(transactionAmount1);
		helper.verify_txn_update_data(testConfig);
		String IstMihpayid = helper.testResponse.actualResponse.get("mihpayid");

		String settlementFile = Helper.getExcelFile(testConfig, FileType.esscomSettlement);

		FileHandler.write_edit(settlementFile,"Sheet1", 1, 4, helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 6, transactionAmount1, 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 7, helper.GetTxnUpdateData()[0], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 8, helper.GetTxnUpdateData()[1], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 9, transactionAmount1, 0);

		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 10, random_UTR, 0);

		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 11, "'"+current_date, 0);

		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		String amnt2 = helper.testResponse.actualResponse.get("amount");
		double amount2 = Double.parseDouble(amnt2);

		addCharge = helper.testResponse.actualResponse.get("additionalCharges");
		additionalCharges = Double.parseDouble(addCharge);
		amount2 = amount2+additionalCharges;	
		String transactionAmount2 = String.valueOf(amount1)+"0";

		//double amt2 = Double.parseDouble(transactionAmount2);
		//String amount2 = helper.testResponse.actualResponse.get("amount");
		helper.verify_txn_update_data(testConfig);

		String IIndMihpayid = helper.testResponse.actualResponse.get("mihpayid");
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 4, helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 6, transactionAmount2, 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 7, helper.GetTxnUpdateData()[0], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 8, helper.GetTxnUpdateData()[1], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 9, transactionAmount2, 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 10, random_UTR, 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 11, "'"+current_date, 0);

		/*amt2 = Double.parseDouble(transactionAmount2);
		amt2 = amt1+amt2;	
		String amount = String.valueOf(amt2)+"0";*/

		//Navigating to Merchant Settlement 		
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();
		merchantSettlement.esscomSettlementFileUpload(testConfig, settlementFile);

		verify_esscom_settlement_output(testConfig, IstMihpayid, IIndMihpayid);

		//Esscom settled transactions are no longer shown on billings page
		/*Browser.wait(testConfig, 2);

		//Navigating to Dashboard's Billing tab
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		//Verify Recent payment,Settlement data, settlement id and UTR number in Billing Tab's Most Recent Payments
		Helper.compareEquals(testConfig, "Settlement Id", "Most recent payment - "+settlementid[0].toUpperCase(), billingsPage.SettlementId());
		Helper.compareEquals(testConfig, "UTR value", random_UTR, billingsPage.UTR());
		Helper.compareEquals(testConfig, "Transaction Amount", amount, billingsPage.getTransactionAmount());
		Helper.compareEquals(testConfig, "TDR Fee", helper.GetTxnUpdateData()[0], billingsPage.getTDRFee());
		Helper.compareEquals(testConfig, "Service Tax Amount", helper.GetTxnUpdateData()[1], billingsPage.getServiceTax());
		Helper.compareEquals(testConfig, "Net Amount", amount, billingsPage.getNetAmount());
		Helper.compareEquals(testConfig, "Total Settlement Amount", amount, billingsPage.getTotalSettlement());

		//Verify Settled transaction in View Transactions
		String noOfTransactions = billingsPage.getNoOfTransactions();
		merchantTransactionsPage = billingsPage.clickUTR(random_UTR);		
		Helper.compareEquals(testConfig, "No. of Records", noOfTransactions, merchantTransactionsPage.getNumberRecords());

		//Verify transaction detail
		testConfig.putRunTimeProperty("Amount", transactionAmount2);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount2);
		dashboardHelper.transactionsPage = merchantTransactionsPage.selectBillingTransaction();
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);
		 */
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of captured transaction after recon and verify", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestCapturedCitiBankNodalSettlementUpload(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		//Navigating to Dashboard's Billing tab to clear settlement Id if any exist for the same date
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();
		manualUpdatePage = billingsPage.DeleteSettlementId(testConfig, helper);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

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
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		//Entering value in .xls file in Parameters from generated recon output
		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settlement_id after verifying
		String settlementid = verify_settlement(testConfig, payuid);

		//Download summary entering settlement id	
		merchantSettlement.getCitiBankNodalSummary(testConfig, settlementid);
		
		//verify merchant summary
		settledFile = verify_MerchantSummary(testConfig, payuid);
		merchantSettlement.settleCitiBankNodalFileForTIDGeneration(testConfig, settledFile);

		String settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();

		merchantSettlement.cronRun("updateTxnWiseTid.php");
		
		//Verifying if TID is generated successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		String TID = verify_TIDgenerated(testConfig);
		String settlement_id= testDataReader.GetData(1, "settlement_id");
		String merchant_amount= testDataReader.GetData(1, "merchant amount");
		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		String bank_ref_num = Helper.generateRandomAlphaNumericString(6);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RequestsPage request= dashBoard.ClickViewRequest();

		request.SearchTransaction(settlement_id, SearchOn.PayuSettlementId);
		Helper.compareEquals(testConfig, "Settlement Status", "approval pending",request.getSettle());

		helper.GetTestTransactionPage();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Final settlement
		String tidFile = Helper.getExcelFile(testConfig, FileType.TIDUTR);

		FileHandler.write_edit(tidFile,"Sheet1", 1, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 18, current_date, 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseUtr.php");

		//Verifying if TID UTR number is updated successfully
		verify_TIDsettlement(testConfig, payuid);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		//Verify Recent payment,Settlement data, settlement id and UTR number in Billing Tab's Most Recent Payments
		Helper.compareEquals(testConfig, "Settlement Id", "Most recent payment - "+settlementid.toUpperCase(), billingsPage.SettlementId());
		Helper.compareEquals(testConfig, "UTR value", random_UTR, billingsPage.UTR());
		Helper.compareEquals(testConfig, "Transaction Amount", helper.testResponse.actualResponse.get("amount"), billingsPage.getTransactionAmount());
		Helper.compareEquals(testConfig, "TDR Fee", helper.GetTxnUpdateData()[0], billingsPage.getTDRFee());
		Helper.compareEquals(testConfig, "Service Tax Amount", helper.GetTxnUpdateData()[1], billingsPage.getServiceTax());
		Helper.compareEquals(testConfig, "Net Amount", helper.GetTxnUpdateData()[2], billingsPage.getNetAmount());
		Helper.compareEquals(testConfig, "Total Settlement Amount", helper.testResponse.actualResponse.get("amount"), billingsPage.getTotalSettlement());

		//Verify Settled transaction in View Transactions
		String noOfTransactions = billingsPage.getNoOfTransactions();
		merchantTransactionsPage = billingsPage.clickUTR(random_UTR);		
		Helper.compareEquals(testConfig, "No. of Records", noOfTransactions, merchantTransactionsPage.getNumberRecords());

		//Verify transaction detail
		dashboardHelper.transactionsPage = merchantTransactionsPage.selectBillingTransaction();
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of captured transaction after recon and verify", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestCapturedCitiBankNodalSettlementUploadWithCF(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		int transactionRowNum = 67;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;

		//Navigating to Dashboard's Billing tab to clear settlement Id if any exist for the same date
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();
		manualUpdatePage = billingsPage.DeleteSettlementId(testConfig, helper);

		helper.GetTestTransactionPage();

		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[1];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);

		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);		

		helper.verify_txn_update_data(testConfig);
		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		//Entering value in .xls file in Parameters 
		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settlement_id after verifying
		String settlementid = verify_settlement(testConfig, payuid);

		//Download summary entering settlement id	
		merchantSettlement.getCitiBankNodalSummary(testConfig, settlementid);

		//Enter values from each column of downloaded summary file to .xls file
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Name= testDataReaderSummary.GetData(1, "NAME");
		String MERCHANT_ID= testDataReaderSummary.GetData(1, "MERCHANT_ID");
		String PREFORMA_CODE= testDataReaderSummary.GetData(1, "PREFORMA CODE");
		String RTGS_CODE= testDataReaderSummary.GetData(1, "RTGS CODE");
		String TRANSACTION_AMOUNT= testDataReaderSummary.GetData(1, "TRANSACTION AMOUNT");
		String TRANSACTION_COUNT= testDataReaderSummary.GetData(1, "TRANSACTION COUNT");
		String MERCHANT_AMOUNT= testDataReaderSummary.GetData(1, "MERCHANT AMOUNT");
		String DATE= testDataReaderSummary.GetData(1, "DATE");
		String BENE_AC_NO= testDataReaderSummary.GetData(1, "BENE_AC_NO");
		String IFSC_CODE= testDataReaderSummary.GetData(1, "IFSC_CODE");
		String settlement_id= testDataReaderSummary.GetData(1, "settlement_id");
		String COMMISSION_CHARGE= testDataReaderSummary.GetData(1, "COMMISSION CHARGE");

		//Entering value in .xls file in Parameters 
		settledFile = Helper.getExcelFile(testConfig, FileType.merchantSummary);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,Name, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,MERCHANT_ID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,PREFORMA_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,RTGS_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,TRANSACTION_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,TRANSACTION_COUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,MERCHANT_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,DATE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,BENE_AC_NO, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,IFSC_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,settlement_id, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,COMMISSION_CHARGE, 0);

		merchantSettlement.settleCitiBankNodalFileForTIDGeneration(testConfig, settledFile);

		String settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseTid.php");

		//Verifying if TID is generated successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		String TID = verify_TIDgenerated(testConfig);
		settlement_id= testDataReader.GetData(1, "settlement_id");
		String merchant_amount= testDataReader.GetData(1, "merchant amount");
		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		String bank_ref_num = Helper.generateRandomAlphaNumericString(6);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RequestsPage request= dashBoard.ClickViewRequest();

		request.SearchTransaction(settlement_id, SearchOn.PayuSettlementId);
		Helper.compareEquals(testConfig, "Settlement Status", "approval pending",request.getSettle());

		helper.GetTestTransactionPage();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Final settlement
		String tidFile = Helper.getExcelFile(testConfig, FileType.TIDUTR);

		FileHandler.write_edit(tidFile,"Sheet1", 1, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 18, current_date, 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseUtr.php");

		//Verifying if TID UTR number is update successfully
		verify_TIDsettlement(testConfig, payuid);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		//Verify Recent payment,Settlement data, settlement id and UTR number in Billing Tab's Most Recent Payments
		//Commented, because for the same merchant recent transaction is already done
		Helper.compareEquals(testConfig, "Settlement Id", "Most recent payment - "+settlementid.toUpperCase(), billingsPage.SettlementId());
		Helper.compareEquals(testConfig, "UTR value", random_UTR, billingsPage.UTR());
		Helper.compareEquals(testConfig, "Transaction Amount", transactionAmount, billingsPage.getTransactionAmount());
		Helper.compareEquals(testConfig, "TDR Fee", helper.GetTxnUpdateData()[0], billingsPage.getTDRFee());
		Helper.compareEquals(testConfig, "Service Tax Amount", helper.GetTxnUpdateData()[1], billingsPage.getServiceTax());
		Helper.compareEquals(testConfig, "Net Amount", helper.GetTxnUpdateData()[2], billingsPage.getNetAmount());
		Helper.compareEquals(testConfig, "Total Settlement Amount", transactionAmount, billingsPage.getTotalSettlement());

		//Verify Settled transaction in View Transactions
		String noOfTransactions = billingsPage.getNoOfTransactions();
		merchantTransactionsPage = billingsPage.clickUTR(random_UTR);		
		Helper.compareEquals(testConfig, "No. of Records", noOfTransactions, merchantTransactionsPage.getNumberRecords());

		//Verify transaction detail
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);
		testConfig.putRunTimeProperty("Transaction ID", helper.testResponse.actualResponse.get("txnid"));

		dashboardHelper.transactionsPage = merchantTransactionsPage.selectBillingTransaction();
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of failed transaction after recon and verify", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestFailedCitiBankNodalSettlementUpload(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		int transactionRowNum = 2;
		int paymentTypeRowNum = 9;
		int cardDetailsRowNum = 1;

		//Navigating to Dashboard's Billing tab to clear settlement Id if any exist for the same date
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();
		manualUpdatePage = billingsPage.DeleteSettlementId(testConfig, helper);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

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
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		//Entering value in .xls file in Parameters 
		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settlement_id after verifying
		String settlementid = verify_settlement(testConfig, payuid);

		//Download summary entering settlement id	
		merchantSettlement.getCitiBankNodalSummary(testConfig, settlementid);

		//Enter values from each column of downloaded summary file to .xls file
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Name= testDataReaderSummary.GetData(1, "NAME");
		String MERCHANT_ID= testDataReaderSummary.GetData(1, "MERCHANT_ID");
		String PREFORMA_CODE= testDataReaderSummary.GetData(1, "PREFORMA CODE");
		String RTGS_CODE= testDataReaderSummary.GetData(1, "RTGS CODE");
		String TRANSACTION_AMOUNT= testDataReaderSummary.GetData(1, "TRANSACTION AMOUNT");
		String TRANSACTION_COUNT= testDataReaderSummary.GetData(1, "TRANSACTION COUNT");
		String MERCHANT_AMOUNT= testDataReaderSummary.GetData(1, "MERCHANT AMOUNT");
		String DATE= testDataReaderSummary.GetData(1, "DATE");
		String BENE_AC_NO= testDataReaderSummary.GetData(1, "BENE_AC_NO");
		String IFSC_CODE= testDataReaderSummary.GetData(1, "IFSC_CODE");
		String settlement_id= testDataReaderSummary.GetData(1, "settlement_id");
		String COMMISSION_CHARGE= testDataReaderSummary.GetData(1, "COMMISSION CHARGE");

		//Entering value in .xls file in Parameters 
		settledFile = Helper.getExcelFile(testConfig, FileType.merchantSummary);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,Name, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,MERCHANT_ID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,PREFORMA_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,RTGS_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,TRANSACTION_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,TRANSACTION_COUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,MERCHANT_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,DATE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,BENE_AC_NO, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,IFSC_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,settlement_id, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,COMMISSION_CHARGE, 0);

		merchantSettlement.settleCitiBankNodalFileForTIDGeneration(testConfig, settledFile);

		String settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();

		merchantSettlement.cronRun("updateTxnWiseTid.php");
		
		//Verifying if TID is generated successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		String TID = verify_TIDgenerated(testConfig);
		settlement_id= testDataReader.GetData(1, "settlement_id");
		String merchant_amount= testDataReader.GetData(1, "merchant amount");
		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		String bank_ref_num = Helper.generateRandomAlphaNumericString(6);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RequestsPage request= dashBoard.ClickViewRequest();

		request.SearchTransaction(settlement_id, SearchOn.PayuSettlementId);
		Helper.compareEquals(testConfig, "Settlement Status", "approval pending",request.getSettle());

		helper.GetTestTransactionPage();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Final settlement
		String tidFile = Helper.getExcelFile(testConfig, FileType.TIDUTR); 

		FileHandler.write_edit(tidFile,"Sheet1", 1, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 18, current_date, 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseUtr.php");

		//Verifying if TID UTR number is update successfully
		verify_TIDsettlement(testConfig, payuid);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		//Verify Recent payment,Settlement data, settlement id and UTR number in Billing Tab's Most Recent Payments
		Helper.compareEquals(testConfig, "Settlement Id", "Most recent payment - "+settlementid.toUpperCase(), billingsPage.SettlementId());
		Helper.compareEquals(testConfig, "UTR value", random_UTR, billingsPage.UTR());
		Helper.compareEquals(testConfig, "Transaction Amount", helper.testResponse.actualResponse.get("amount"), billingsPage.getTransactionAmount());
		Helper.compareEquals(testConfig, "TDR Fee", helper.GetTxnUpdateData()[0], billingsPage.getTDRFee());
		Helper.compareEquals(testConfig, "Service Tax Amount", helper.GetTxnUpdateData()[1], billingsPage.getServiceTax());
		Helper.compareEquals(testConfig, "Net Amount", helper.GetTxnUpdateData()[2], billingsPage.getNetAmount());
		Helper.compareEquals(testConfig, "Total Settlement Amount", helper.testResponse.actualResponse.get("amount"), billingsPage.getTotalSettlement());

		//Verify Settled transaction in View Transactions
		String noOfTransactions = billingsPage.getNoOfTransactions();
		merchantTransactionsPage = billingsPage.clickUTR(random_UTR);		
		Helper.compareEquals(testConfig, "No. of Records", noOfTransactions, merchantTransactionsPage.getNumberRecords());

		//Verify transaction detail
		dashboardHelper.transactionsPage = merchantTransactionsPage.selectBillingTransaction();
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of failed transaction after recon and verify", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestFailedCitiBankNodalSettlementUploadWithCF(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		int transactionRowNum = 66;
		int paymentTypeRowNum = 4;
		int cardDetailsRowNum = 1;

		//Navigating to Dashboard's Billing tab to clear settlement Id if any exist for the same date
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();
		manualUpdatePage = billingsPage.DeleteSettlementId(testConfig, helper);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		String amnt = helper.testResponse.actualResponse.get("amount");
		double amount = Double.parseDouble(amnt);

		String addCharge = helper.testResponse.actualResponse.get("additionalCharges");
		double additionalCharges = Double.parseDouble(addCharge);
		amount = amount+additionalCharges;	
		String transactionAmount = String.valueOf(amount)+"0";
		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		//Entering value in .xls file in Parameters 
		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settlement_id after verifying
		String settlementid = verify_settlement(testConfig, payuid);

		//Download summary entering settlement id	
		merchantSettlement.getCitiBankNodalSummary(testConfig, settlementid);

		//Enter values from each column of downloaded summary file to .xls file
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Name= testDataReaderSummary.GetData(1, "NAME");
		String MERCHANT_ID= testDataReaderSummary.GetData(1, "MERCHANT_ID");
		String PREFORMA_CODE= testDataReaderSummary.GetData(1, "PREFORMA CODE");
		String RTGS_CODE= testDataReaderSummary.GetData(1, "RTGS CODE");
		String TRANSACTION_AMOUNT= testDataReaderSummary.GetData(1, "TRANSACTION AMOUNT");
		String TRANSACTION_COUNT= testDataReaderSummary.GetData(1, "TRANSACTION COUNT");
		String MERCHANT_AMOUNT= testDataReaderSummary.GetData(1, "MERCHANT AMOUNT");
		String DATE= testDataReaderSummary.GetData(1, "DATE");
		String BENE_AC_NO= testDataReaderSummary.GetData(1, "BENE_AC_NO");
		String IFSC_CODE= testDataReaderSummary.GetData(1, "IFSC_CODE");
		String settlement_id= testDataReaderSummary.GetData(1, "settlement_id");
		String COMMISSION_CHARGE= testDataReaderSummary.GetData(1, "COMMISSION CHARGE");

		//Entering value in .xls file in Parameters 
		settledFile = Helper.getExcelFile(testConfig, FileType.merchantSummary);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,Name, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,MERCHANT_ID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,PREFORMA_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,RTGS_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,TRANSACTION_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,TRANSACTION_COUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,MERCHANT_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,DATE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,BENE_AC_NO, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,IFSC_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,settlement_id, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,COMMISSION_CHARGE, 0);

		merchantSettlement.settleCitiBankNodalFileForTIDGeneration(testConfig, settledFile);

		String settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();

		//Verifying if TID is generated successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		String TID = verify_TIDgenerated(testConfig);
		settlement_id= testDataReader.GetData(1, "settlement_id");
		String merchant_amount= testDataReader.GetData(1, "merchant amount");
		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		String bank_ref_num = Helper.generateRandomAlphaNumericString(6);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RequestsPage request= dashBoard.ClickViewRequest();

		request.SearchTransaction(settlement_id, SearchOn.PayuSettlementId);
		Helper.compareEquals(testConfig, "Settlement Status", "approval pending",request.getSettle());

		helper.GetTestTransactionPage();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Final settlement
		String tidFile = Helper.getExcelFile(testConfig, FileType.TIDUTR);

		FileHandler.write_edit(tidFile,"Sheet1", 1, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 18, current_date, 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();

		//Verifying if TID UTR number is update successfully
		verify_TIDsettlement(testConfig, payuid);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		//Verify Recent payment,Settlement data, settlement id and UTR number in Billing Tab's Most Recent Payments
		Helper.compareEquals(testConfig, "Settlement Id", "Most recent payment - "+settlementid.toUpperCase(), billingsPage.SettlementId());
		Helper.compareEquals(testConfig, "UTR value", random_UTR, billingsPage.UTR());
		Helper.compareEquals(testConfig, "Transaction Amount", transactionAmount, billingsPage.getTransactionAmount());
		Helper.compareEquals(testConfig, "TDR Fee", helper.GetTxnUpdateData()[0], billingsPage.getTDRFee());
		Helper.compareEquals(testConfig, "Service Tax Amount", helper.GetTxnUpdateData()[1], billingsPage.getServiceTax());
		Helper.compareEquals(testConfig, "Net Amount", helper.GetTxnUpdateData()[2], billingsPage.getNetAmount());
		Helper.compareEquals(testConfig, "Total Settlement Amount", transactionAmount, billingsPage.getTotalSettlement());

		//Verify Settled transaction in View Transactions
		String noOfTransactions = billingsPage.getNoOfTransactions();
		merchantTransactionsPage = billingsPage.clickUTR(random_UTR);		
		Helper.compareEquals(testConfig, "No. of Records", noOfTransactions, merchantTransactionsPage.getNumberRecords());

		//Verify transaction detail
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);
		testConfig.putRunTimeProperty("Transaction ID", helper.testResponse.actualResponse.get("txnid"));
		dashboardHelper.transactionsPage = merchantTransactionsPage.selectBillingTransaction();
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of dropped transaction after recon and verify", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestDroppedCitiBankNodalSettlementUpload(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		testConfig.putRunTimeProperty("merchantid", "5912");
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 7, 1);
		String payuid = map.get("id");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				map.get("id"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				map.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		// Navigating to Admin Recon
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		//Entering value in .xls file in Parameters 
		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,map.get("id"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settlement_id after verifying
		String settlementid = verify_settlement(testConfig, payuid);

		//Download summary entering settlement id	
		merchantSettlement.getCitiBankNodalSummary(testConfig, settlementid);

		//Enter values from each column of downloaded summary file to .xls file
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Name= testDataReaderSummary.GetData(1, "NAME");
		String MERCHANT_ID= testDataReaderSummary.GetData(1, "MERCHANT_ID");
		String PREFORMA_CODE= testDataReaderSummary.GetData(1, "PREFORMA CODE");
		String RTGS_CODE= testDataReaderSummary.GetData(1, "RTGS CODE");
		String TRANSACTION_AMOUNT= testDataReaderSummary.GetData(1, "TRANSACTION AMOUNT");
		String TRANSACTION_COUNT= testDataReaderSummary.GetData(1, "TRANSACTION COUNT");
		String MERCHANT_AMOUNT= testDataReaderSummary.GetData(1, "MERCHANT AMOUNT");
		String DATE= testDataReaderSummary.GetData(1, "DATE");
		String BENE_AC_NO= testDataReaderSummary.GetData(1, "BENE_AC_NO");
		String IFSC_CODE= testDataReaderSummary.GetData(1, "IFSC_CODE");
		String settlement_id= testDataReaderSummary.GetData(1, "settlement_id");
		String COMMISSION_CHARGE= testDataReaderSummary.GetData(1, "COMMISSION CHARGE");

		//Entering value in .xls file in Parameters 
		settledFile = Helper.getExcelFile(testConfig, FileType.merchantSummary);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,Name, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,MERCHANT_ID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,PREFORMA_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,RTGS_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,TRANSACTION_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,TRANSACTION_COUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,MERCHANT_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,DATE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,BENE_AC_NO, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,IFSC_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,settlement_id, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,COMMISSION_CHARGE, 0);

		merchantSettlement.settleCitiBankNodalFileForTIDGeneration(testConfig, settledFile);
		
		merchantSettlement.cronRun("updateTxnWiseTid.php");

		String settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();

		//Verifying if TID is generated successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		String TID = verify_TIDgenerated(testConfig);
		settlement_id= testDataReader.GetData(1, "settlement_id");
		String merchant_amount= testDataReader.GetData(1, "merchant amount");
		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		String bank_ref_num = Helper.generateRandomAlphaNumericString(6);

		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Final settlement
		String tidFile = Helper.getExcelFile(testConfig, FileType.TIDUTR);

		FileHandler.write_edit(tidFile,"Sheet1", 1, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 18, current_date, 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseUtr.php");

		//Verifying if TID UTR number is update successfully
		verify_TIDsettlement(testConfig, payuid);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of captured transaction after recon with invalid data and verify", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestCapturedInvalidDataCitiBankNodalSettlementUpload(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

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
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering invalid action and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		TestDataReader reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(20, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering invalid requestid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(19, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering invalid payuid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(18, "failureReason"), file_result);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of failed transaction after recon with invalid data and verify", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestFailedInvalidDataCitiBankNodalSettlementUpload(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 9;
		int cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

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
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering invalid action and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		TestDataReader reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(20, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering invalid requestid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(19, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering invalid payuid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(18, "failureReason"), file_result);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of dropped transaction after recon with invalid data and verify", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestDroppedInvalidDataCitiBankNodalSettlementUpload(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		testConfig.putRunTimeProperty("merchantid", "5912");
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 7, 1);
		String payuid = map.get("id");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				map.get("id"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				map.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering invalid action and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,map.get("id"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		TestDataReader reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(20, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering invalid requestid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,map.get("id"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(19, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering invalid payuid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,Helper.generateRandomAlphabetsString(5), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(18, "failureReason"), file_result);

		Assert.assertTrue(testConfig.getTestResult());	
	}


	@Test(description = "Settlement: Upload a valid CitiBankNodal file of captured transaction after recon with blank data and verify", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestCapturedBlankDataCitiBankNodalSettlementUpload(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

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
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");


		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering blank action and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,"", 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		TestDataReader reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(20, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering blank requestid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,"", 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(19, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering blank payuid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,"", 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(18, "failureReason"), file_result);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of failed transaction after recon with blank data and verify", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestFailedBlankDataCitiBankNodalSettlementUpload(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 9;
		int cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

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
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");


		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering blank action and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,"", 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		TestDataReader reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(20, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering blank requestid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,"", 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(19, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering blank payuid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,"", 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(18, "failureReason"), file_result);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of dropped transaction after recon with blank data and verify", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestDroppedBlankDataCitiBankNodalSettlementUpload(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		testConfig.putRunTimeProperty("merchantid", "5912");
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 7, 1);
		String payuid = map.get("id");
		String reconFile = Helper.getExcelFile(testConfig, FileType.adminRecon);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				map.get("id"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				map.get("amount"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				map.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering blank action and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,map.get("id"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,"", 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		String settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		TestDataReader reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(20, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering blank requestid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,map.get("id"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,"", 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(19, "failureReason"), file_result);

		settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		//Entering blank payuid and other valid values in .xls file in Parameters 
		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,"", 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,map.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settled file for verification
		settledfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settledfilepath);
		fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", settledfilepath + fileName);
		file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");
		// Verifying transaction is not settled
		Helper.compareEquals(testConfig, "Transaction not settled reason",
				reconoutputdata.GetData(18, "failureReason"), file_result);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify the transaction data in Excel sheet and DB", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void VerifyExportToExcelData(Config testConfig) {

		int transactionRowNum = 2;
		int outputExcelRowToBeVerified=1;

		//Navigating to Dashboard's Billing tab
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		testDataReader = new TestDataReader(testConfig,"TransactionDetails");
		String merchantName = testDataReader.GetData(transactionRowNum, "Comments");

		//Apply Filter
		testConfig.putRunTimeProperty("todate",Helper.getCurrentDate("yyyy-MM-dd"));
		testConfig.putRunTimeProperty("fromdate",Helper.getFourWeekStartingDate("yyyy-MM-dd"));		
		billingsPage.applyDateFilter();

		//Verify Export To Excel
		billingsPage.ExportToExcel();
		Browser.wait(testConfig,10);

		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		testDataReader = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);

		int noofrows = billingsPage.settledTransactionNo();
		Helper.compareEquals(testConfig, "result count of transaction in excel", noofrows,testDataReader.getRecordsNum()-1);	

		//Verify data in Excel and DB
		String[] settledData = billingsPage.GetSettledTransactions(merchantName);
		Helper.compareEquals(testConfig, "UTR no",settledData[1],testDataReader.GetData(outputExcelRowToBeVerified, "UTR Number"));
		Helper.compareEquals(testConfig, "Settlement Date",settledData[2],testDataReader.GetData(outputExcelRowToBeVerified, "Settlement Date"));
		Helper.compareEquals(testConfig, "Amount",settledData[3],testDataReader.GetData(outputExcelRowToBeVerified, "Amount"));
		Helper.compareEquals(testConfig, "Settled Amount",settledData[4],testDataReader.GetData(outputExcelRowToBeVerified, "Settled Amount"));	

		Assert.assertTrue(testConfig.getTestResult());
	}	

	@Test(description = "Verify Mail link and New to Payments link", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void VerifyNewToPayments(Config testConfig) {

		int transactionRowNum = 3;

		//Navigating to Dashboard's Billing tab
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		//Verifying Mail Link
		Helper.compareEquals(testConfig, "Email Link", "mailto:contact@payu.in", billingsPage.getEmailLink());

		//Verifying New to payments link
		billingsPage.getNewToPayment();
		Browser.wait(testConfig,3);

		Assert.assertTrue(testConfig.getTestResult());
	}	

	@Test(description = "Verify Pending Payments", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void VerifyPendingPayments(Config testConfig) {

		int transactionRowNum = 3;

		//Navigating to Dashboard's Billing tab
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		testDataReader = new TestDataReader(testConfig,"TransactionDetails");
		String merchantName = testDataReader.GetData(transactionRowNum, "Comments");

		RequestsPage requestsPage = billingsPage.clickPendingPayments();
		testConfig.putRunTimeProperty("todate",Helper.getDateBeforeOrAfterDays(1,"yyyy-MM-dd"));
		testConfig.putRunTimeProperty("fromdate",Helper.getDateBeforeOrAfterDays(-7, "yyyy-MM-dd"));	

		Helper.compareEquals(testConfig, "No. of Records", billingsPage.GetCountOfUnSettledTransactions(merchantName), "{count(*)="+requestsPage.getNumberRecords()+"}");

		//Verify Unsettled checkbox in Order status is checked
		requestsPage.unsettledBox();

		//Verify first row data from DB
		requestsPage.VerifyUnsettledTransactionData(merchantName);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Settlement: Upload a TIDUTR file with invalid and duplicate  TIDs", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestInvalidDuplicateTIDUpload(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int i=1;

		//Navigating to manual transaction update for uploading TID file
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Uploading duplicate data in TIDUTR file
		String tidFile = Helper.getExcelFile(testConfig, FileType.invalidTIDUTR);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,"100046644646", 0);
		FileHandler.write_edit(tidFile,"Sheet1", 2, 11,"100046644646", 0);
		FileHandler.write_edit(tidFile,"Sheet1", 3, 11,"100046644646", 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		String tidutrfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, tidutrfilepath);
		String fileName = file.getName();

		//Verifying if TID UTR is uploaded showing failure reason
		testDataReader = new TestDataReader(testConfig, "Sheet1",tidutrfilepath + fileName);
		TestDataReader tidutrUpload = new TestDataReader(testConfig,
				"ReconResponse");

		for(i=1; i<testDataReader.getRecordsNum(); i++) {
			Helper.compareEquals(testConfig, "Transaction not settled reason for "+i+" row",
					tidutrUpload.GetData(17, "result"), testDataReader.GetData(i, "result"));			
		}

		//Navigate in to dashboard to check TID UTR should not be updated
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(1);
		RequestsPage request= dashBoard.ClickViewRequest();

		//Search for invalid TIDs through UTR no.		
		for(i=1; i<testDataReader.getRecordsNum(); i++) {
			request.SearchTransaction(testDataReader.GetData(i, "utr number"), SearchOn.UTRno);		
			String nullRecord = request.getNumberRecords();
			Helper.compareEquals(testConfig, "Settlement Status for "+i+" row's utr", "0",nullRecord);
		}

		//Upload invalid TIDs
		helper.GetTestTransactionPage();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Uploading invalid data in TIDUTR file
		tidFile = Helper.getExcelFile(testConfig, FileType.invalidTIDUTR);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,"100046644646", 0);
		FileHandler.write_edit(tidFile,"Sheet1", 2, 11,"100046644647", 0);
		FileHandler.write_edit(tidFile,"Sheet1", 3, 11,"100046644648", 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		tidutrfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, tidutrfilepath);
		fileName = file.getName();

		//Verifying if TID UTR is uploaded showing failure reason
		testDataReader = new TestDataReader(testConfig, "Sheet1",tidutrfilepath + fileName);
		tidutrUpload = new TestDataReader(testConfig,
				"ReconResponse");

		for(i=1; i<testDataReader.getRecordsNum(); i++) {
			Helper.compareEquals(testConfig, "Transaction not settled reason for "+i+" row",
					tidutrUpload.GetData(18, "result"), testDataReader.GetData(i, "result"));			
		}

		//Navigate in to dashboard to check TID UTR should not be updated
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(1);
		request= dashBoard.ClickViewRequest();

		//Search for invalid TIDs through UTR no.		
		for(i=1; i<testDataReader.getRecordsNum(); i++) {
			request.SearchTransaction(testDataReader.GetData(i, "utr number"), SearchOn.UTRno);		
			String nullRecord = request.getNumberRecords();
			Helper.compareEquals(testConfig, "Settlement Status for "+i+" row's utr", "0",nullRecord);
		}

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a TIDUTR file with duplicate  TIDs", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestDuplicateTIDUpload(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int i=1;

		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

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
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12, "domestic", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		//Entering value in .xls file in Parameters from generated recon output
		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settlement_id after verifying
		String settlementid = verify_settlement(testConfig, payuid);

		//Download summary entering settlement id	
		merchantSettlement.getCitiBankNodalSummary(testConfig, settlementid);

		//Verify Merchant Summary
		settledFile = verify_MerchantSummary(testConfig, payuid);

		merchantSettlement.settleCitiBankNodalFileForTIDGeneration(testConfig, settledFile);

		String settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseTid.php");

		//Verifying if TID is generated successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		String TID = verify_TIDgenerated(testConfig);
		//String settlement_id= testDataReader.GetData(1, "settlement_id");
		String merchant_amount= testDataReader.GetData(1, "merchant amount");
		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		String bank_ref_num = Helper.generateRandomAlphaNumericString(6);

		////Verifying UTR is not generated on entering duplicate TIDs
		String tidFile = Helper.getExcelFile(testConfig, FileType.invalidTIDUTR);

		FileHandler.write_edit(tidFile,"Sheet1", 1, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 18, current_date, 0);

		//Entering duplicate data in 2nd row
		FileHandler.write_edit(tidFile,"Sheet1", 2, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 2, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 2, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 2, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 2, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 2, 18, current_date, 0);	
		//Entering duplicate data in 3rd row
		FileHandler.write_edit(tidFile,"Sheet1", 3, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 3, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 3, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 3, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 3, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 3, 18, current_date, 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		String tidutrfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, tidutrfilepath);
		fileName = file.getName();

		merchantSettlement.cronRun("updateTxnWiseUtr.php");
		
		//Verifying if TID UTR number is not updated on uploading duplicate TIDs
		testDataReader = new TestDataReader(testConfig, "Sheet1",tidutrfilepath + fileName);
		TestDataReader tidutrUpload = new TestDataReader(testConfig,
				"ReconResponse");

		for(i=1; i<testDataReader.getRecordsNum(); i++) {
			Helper.compareEquals(testConfig, "Transaction not settled reason for "+i+" row",
					tidutrUpload.GetData(17, "result"), testDataReader.GetData(i, "result"));			
		}

		//Navigating to Dashboard's Billing tab
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RequestsPage request= dashBoard.ClickViewRequest();

		//Search for duplicate TIDs through UTR no.		
		for(i=1; i<testDataReader.getRecordsNum(); i++) {
			request.SearchTransaction(testDataReader.GetData(i, "utr number"), SearchOn.UTRno);		
			String nullRecord = request.getNumberRecords();
			Helper.compareEquals(testConfig, "Settlement Status for "+i+" row's utr", "0",nullRecord);
		}

		//Verifying UTR is not generated on entering one invalid TID and others invalid duplicate TIDs
		helper.GetTestTransactionPage();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Entering one invalid TID and others invalid duplicate TIDs
		tidFile = Helper.getExcelFile(testConfig, FileType.invalidTIDUTR);

		//Entering invalid TID
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,"110000767863745", 0);
		//Entering invalid duplicate TIDs
		FileHandler.write_edit(tidFile,"Sheet1", 2, 11,"110065464", 0);
		FileHandler.write_edit(tidFile,"Sheet1", 3, 11,"110065464", 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		tidutrfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, tidutrfilepath);
		fileName = file.getName();

		//Verifying if TID UTR number is not updated on uploading one invalid and other duplicate TIDs
		testDataReader = new TestDataReader(testConfig, "Sheet1",tidutrfilepath + fileName);
		tidutrUpload = new TestDataReader(testConfig,
				"ReconResponse");

		Helper.compareEquals(testConfig, "Transaction not settled reason for 1 row with invalid TID",
				tidutrUpload.GetData(18, "result"), testDataReader.GetData(1, "result"));

		for(i=2; i<testDataReader.getRecordsNum(); i++) {
			Helper.compareEquals(testConfig, "Transaction not settled reason for "+i+" row",
					tidutrUpload.GetData(17, "result"), testDataReader.GetData(i, "result"));			
		}

		//Navigate in to dashboard to check TID UTR should not be updated
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		request= dashBoard.ClickViewRequest();

		//Search for invalid TIDs through UTR no.		
		for(i=1; i<testDataReader.getRecordsNum(); i++) {
			request.SearchTransaction(testDataReader.GetData(i, "utr number"), SearchOn.UTRno);		
			String nullRecord = request.getNumberRecords();
			Helper.compareEquals(testConfig, "Settlement Status for "+i+" row's utr", "0",nullRecord);
		}

		//Verifying UTR is generated only for valid TID not for other invalid TIDs
		helper.GetTestTransactionPage();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Entering one valid TID and others invalid
		tidFile = Helper.getExcelFile(testConfig, FileType.invalidTIDUTR);

		FileHandler.write_edit(tidFile,"Sheet1", 1, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 18, current_date, 0);

		//Entering invalid TIDs
		FileHandler.write_edit(tidFile,"Sheet1", 2, 11,"110065464", 0);
		FileHandler.write_edit(tidFile,"Sheet1", 3, 11,"4542671562676", 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		tidutrfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, tidutrfilepath);
		fileName = file.getName();

		//Verifying if TID UTR number is not updated on uploading duplicate TIDs
		testDataReader = new TestDataReader(testConfig, "Sheet1",tidutrfilepath + fileName);
		tidutrUpload = new TestDataReader(testConfig,
				"ReconResponse");

		Helper.compareEquals(testConfig, "Transaction successfully settled message for 1 row with valid TID",
				tidutrUpload.GetData(20, "result"), testDataReader.GetData(1, "result"));

		for(i=2; i<testDataReader.getRecordsNum(); i++) {
			Helper.compareEquals(testConfig, "Transaction not settled reason for "+i+" row",
					tidutrUpload.GetData(18, "result"), testDataReader.GetData(i, "result"));			
		}

		//Navigate in to dashboard to check TID UTR should not be updated
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		request= dashBoard.ClickViewRequest();

		//Search for valid TID through UTR no.		
		request.SearchTransaction(testDataReader.GetData(1, "utr number"), SearchOn.UTRno);		
		String successRecord = request.getNumberRecords();
		Helper.compareEquals(testConfig, "Settlement Status for 1 row's utr with Valid TID", "1",successRecord);

		//Search for invalid TIDs through UTR no.
		for(i=2; i<testDataReader.getRecordsNum(); i++) {
			request.SearchTransaction(testDataReader.GetData(i, "utr number"), SearchOn.UTRno);		
			String nullRecord = request.getNumberRecords();
			Helper.compareEquals(testConfig, "Settlement Status for "+i+" row's utr", "0",nullRecord);
		}

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of captured transaction after recon and verify settled amount should" +
			"should be amount entered and commission should be additional charge FLAT charged for billing merchant", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestCapturedBillingSettlementUpload_InvalidFLAT(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		//Do Billing transaction with FLAT additional charge as special character
		int transactionRowNum = 76;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int paramRow = 12;

		//Navigating to Dashboard's Billing tab to clear settlement Id if any exist for the same date
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();
		manualUpdatePage = billingsPage.DeleteSettlementId(testConfig, helper);

		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.FLAT, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		String fixed_amount = helper.testResponse.actualResponse.get("amount");
		String amount = fixed_amount;
		String addCharge = helper.testResponse.actualResponse.get("additionalCharges");

		double amnt = Double.parseDouble(amount);		
		double additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		String transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on FLAT
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.FLAT, paramRow);

		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.FLAT, paramRow);
		Helper.compareEquals(testConfig, "Commission charged", helper.testResponse.actualResponse.get("additionalCharges"),
				testDataReaderSummary.GetData(1, "commission charge"));

		//Do Billing transaction with FLAT additional charge as alphanumeric invalid data
		paramRow = 13;
		testConfig.putRunTimeProperty("amount", fixed_amount);
		helper.GetTestTransactionPage();
		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.FLAT, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		amount = helper.testResponse.actualResponse.get("amount");
		addCharge = helper.testResponse.actualResponse.get("additionalCharges");

		amnt = Double.parseDouble(amount);		
		additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on FLAT
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.FLAT, paramRow);
		payuid = helper.testResponse.actualResponse.get("mihpayid");
		reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.FLAT, paramRow);
		Helper.compareEquals(testConfig, "Commission charged", helper.testResponse.actualResponse.get("additionalCharges"),
				testDataReaderSummary.GetData(1, "commission charge"));

		//Do Billing transaction with FLAT additional charge as alphabets
		paramRow = 14;
		testConfig.putRunTimeProperty("amount", fixed_amount);
		helper.GetTestTransactionPage();
		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.FLAT, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		amount = helper.testResponse.actualResponse.get("amount");
		addCharge = "0.00";

		amnt = Double.parseDouble(amount);		
		additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on FLAT
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.FLAT, paramRow);

		payuid = helper.testResponse.actualResponse.get("mihpayid");
		reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.FLAT, paramRow);
		Helper.compareEquals(testConfig, "Commission charged", "0",
				testDataReaderSummary.GetData(1, "commission charge"));

		//Do Billing transaction with FLAT additional charge as blank data
		paramRow = 15;
		testConfig.putRunTimeProperty("amount", fixed_amount);
		helper.GetTestTransactionPage();
		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.FLAT, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		amount = helper.testResponse.actualResponse.get("amount");
		addCharge = "0";

		amnt = Double.parseDouble(amount);		
		additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on FLAT
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.FLAT, paramRow);
		payuid = helper.testResponse.actualResponse.get("mihpayid");
		reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.FLAT, paramRow);
		Helper.compareEquals(testConfig, "Commission charged", "0",
				testDataReaderSummary.GetData(1, "commission charge"));

		//Do Billing transaction with FLAT additional charge as 0
		paramRow = 16;
		testConfig.putRunTimeProperty("amount", fixed_amount);
		helper.GetTestTransactionPage();
		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.FLAT, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		amount = helper.testResponse.actualResponse.get("amount");
		addCharge = "0";

		amnt = Double.parseDouble(amount);		
		additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on FLAT
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.FLAT, paramRow);

		payuid = helper.testResponse.actualResponse.get("mihpayid");
		reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.FLAT, paramRow);
		Helper.compareEquals(testConfig, "Commission charged", "0",
				testDataReaderSummary.GetData(1, "commission charge"));


		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of captured transaction after recon and verify settled amount should" +
			"should be amount entered and commission should be additional charge FLAT charged for billing merchant", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestCapturedBillingSettlementUpload_FLAT(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		//Do Billing transaction with FLAT additional charge
		int transactionRowNum = 76;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int paramRow = 11;

		//Navigating to Dashboard's Billing tab to clear settlement Id if any exist for the same date
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();
		manualUpdatePage = billingsPage.DeleteSettlementId(testConfig, helper);

		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.FLAT, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String amount = helper.testResponse.actualResponse.get("amount");
		String addCharge = helper.testResponse.actualResponse.get("additionalCharges");

		double amnt = Double.parseDouble(amount);		
		double additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		String transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on FLAT
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.FLAT, paramRow);

		String reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.FLAT, paramRow);
		Helper.compareEquals(testConfig, "Commission charged", helper.testResponse.actualResponse.get("additionalCharges"),
				testDataReaderSummary.GetData(1, "commission charge"));

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		//Entering value in .xls file in Parameters from generated recon output
		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settlement_id after verifying
		String settlementid = verify_settlement(testConfig, payuid);

		//Download summary entering settlement id	
		merchantSettlement.getCitiBankNodalSummary(testConfig, settlementid);

		//Enter values from each column of downloaded summary file to .xls file
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Name= testDataReaderSummary.GetData(1, "NAME");
		String MERCHANT_ID= testDataReaderSummary.GetData(1, "MERCHANT_ID");
		String PREFORMA_CODE= testDataReaderSummary.GetData(1, "PREFORMA CODE");
		String RTGS_CODE= testDataReaderSummary.GetData(1, "RTGS CODE");
		String TRANSACTION_AMOUNT= testDataReaderSummary.GetData(1, "TRANSACTION AMOUNT");
		String TRANSACTION_COUNT= testDataReaderSummary.GetData(1, "TRANSACTION COUNT");
		String MERCHANT_AMOUNT= testDataReaderSummary.GetData(1, "MERCHANT AMOUNT");
		String DATE= testDataReaderSummary.GetData(1, "DATE");
		String BENE_AC_NO= testDataReaderSummary.GetData(1, "BENE_AC_NO");
		String IFSC_CODE= testDataReaderSummary.GetData(1, "IFSC_CODE");
		String settlement_id= testDataReaderSummary.GetData(1, "settlement_id");
		String COMMISSION_CHARGE= testDataReaderSummary.GetData(1, "COMMISSION CHARGE");

		//Entering value in .xls file in Parameters 
		settledFile = Helper.getExcelFile(testConfig, FileType.merchantSummary);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,Name, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,MERCHANT_ID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,PREFORMA_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,RTGS_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,TRANSACTION_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,TRANSACTION_COUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,MERCHANT_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,DATE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,BENE_AC_NO, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,IFSC_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,settlement_id, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,COMMISSION_CHARGE, 0);

		merchantSettlement.settleCitiBankNodalFileForTIDGeneration(testConfig, settledFile);
		
		String settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseTid.php");

		//Verifying if UTR number is update successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		String TID = verify_TIDgenerated(testConfig);
		settlement_id= testDataReader.GetData(1, "settlement_id");
		String merchant_amount= testDataReader.GetData(1, "merchant amount");
		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		String bank_ref_num = Helper.generateRandomAlphaNumericString(6);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RequestsPage request= dashBoard.ClickViewRequest();

		request.SearchTransaction(settlement_id, SearchOn.PayuSettlementId);
		Helper.compareEquals(testConfig, "Settlement Status", "approval pending",request.getSettle());

		helper.GetTestTransactionPage();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Final settlement
		String tidFile = Helper.getExcelFile(testConfig, FileType.TIDUTR);

		FileHandler.write_edit(tidFile,"Sheet1", 1, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 18, current_date, 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();

		merchantSettlement.cronRun("updateTxnWiseUtr.php");
		
		//Verifying if TID UTR number is update successfully
		verify_TIDsettlement(testConfig, payuid);
		
		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		//Verify Recent payment,Settlement data, settlement id and UTR number in Billing Tab's Most Recent Payments
		Helper.compareEquals(testConfig, "Settlement Id", "Most recent payment - "+settlementid.toUpperCase(), billingsPage.SettlementId());
		Helper.compareEquals(testConfig, "UTR value", random_UTR, billingsPage.UTR());
		transactionAmount = String.format("%.2f", amnt);
		Helper.compareEquals(testConfig, "Transaction Amount", transactionAmount, billingsPage.getTransactionAmount());
		Helper.compareEquals(testConfig, "TDR Fee", helper.GetTxnUpdateData()[0], billingsPage.getTDRFee());
		Helper.compareEquals(testConfig, "Service Tax Amount", helper.GetTxnUpdateData()[1], billingsPage.getServiceTax());
		Helper.compareEquals(testConfig, "Net Amount", helper.GetTxnUpdateData()[2], billingsPage.getNetAmount());
		Helper.compareEquals(testConfig, "Total Settlement Amount", helper.testResponse.actualResponse.get("amount"), billingsPage.getTotalSettlement());

		//Verify Settled transaction in View Transactions
		String noOfTransactions = billingsPage.getNoOfTransactions();
		merchantTransactionsPage = billingsPage.clickUTR(random_UTR);		
		Helper.compareEquals(testConfig, "No. of Records", noOfTransactions, merchantTransactionsPage.getNumberRecords());

		//Verify transaction detail
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);
		dashboardHelper.transactionsPage = merchantTransactionsPage.selectBillingTransaction();
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of failed/dropped transaction after recon and verify settled amount should" +
			"should be amount entered and commission should be additional charge FLAT charged for billing merchant", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestFailedBillingSettlementUpload_FLAT(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		//Do Billing transaction with FLAT additional charge
		int transactionRowNum = 76;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 17;
		int paramRow = 11;

		//Navigating to Dashboard's Billing tab to clear settlement Id if any exist for the same date
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();
		manualUpdatePage = billingsPage.DeleteSettlementId(testConfig, helper);

		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.FLAT, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		String amount = helper.testResponse.actualResponse.get("amount");
		String addCharge = helper.testResponse.actualResponse.get("additionalCharges");
		String payuid = helper.testResponse.actualResponse.get("mihpayid");

		double amnt = Double.parseDouble(amount);		
		double additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		String transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on FLAT
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.FLAT, paramRow);

		String reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.FLAT, paramRow);
		Helper.compareEquals(testConfig, "Commission charged", helper.testResponse.actualResponse.get("additionalCharges"),
				testDataReaderSummary.GetData(1, "commission charge"));

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		//Entering value in .xls file in Parameters from generated recon output
		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settlement_id after verifying
		String settlementid = verify_settlement(testConfig, payuid);

		//Download summary entering settlement id	
		merchantSettlement.getCitiBankNodalSummary(testConfig, settlementid);

		settledFile = verify_MerchantSummary(testConfig, payuid);
		merchantSettlement.settleCitiBankNodalFileForTIDGeneration(testConfig, settledFile);

		String settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();

		merchantSettlement.cronRun("updateTxnWiseTid.php");
		
		//Verifying if UTR number is update successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		String TID= verify_TIDgenerated(testConfig);
		String settlement_id= testDataReader.GetData(1, "settlement_id");
		String merchant_amount= testDataReader.GetData(1, "merchant amount");
		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		String bank_ref_num = Helper.generateRandomAlphaNumericString(6);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RequestsPage request= dashBoard.ClickViewRequest();

		request.SearchTransaction(settlement_id, SearchOn.PayuSettlementId);
		Helper.compareEquals(testConfig, "Settlement Status", "approval pending",request.getSettle());

		helper.GetTestTransactionPage();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Final settlement
		String tidFile = Helper.getExcelFile(testConfig, FileType.TIDUTR);

		FileHandler.write_edit(tidFile,"Sheet1", 1, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 18, current_date, 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseUtr.php");

		//Verifying if TID UTR number is update successfully
		verify_TIDsettlement(testConfig, payuid);
		
		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		//Verify Recent payment,Settlement data, settlement id and UTR number in Billing Tab's Most Recent Payments
		Helper.compareEquals(testConfig, "Settlement Id", "Most recent payment - "+settlementid.toUpperCase(), billingsPage.SettlementId());
		Helper.compareEquals(testConfig, "UTR value", random_UTR, billingsPage.UTR());
		transactionAmount = String.format("%.2f", amnt);
		Helper.compareEquals(testConfig, "Transaction Amount", transactionAmount, billingsPage.getTransactionAmount());
		Helper.compareEquals(testConfig, "TDR Fee", helper.GetTxnUpdateData()[0], billingsPage.getTDRFee());
		Helper.compareEquals(testConfig, "Service Tax Amount", helper.GetTxnUpdateData()[1], billingsPage.getServiceTax());
		Helper.compareEquals(testConfig, "Net Amount", helper.GetTxnUpdateData()[2], billingsPage.getNetAmount());
		Helper.compareEquals(testConfig, "Total Settlement Amount", helper.testResponse.actualResponse.get("amount"), billingsPage.getTotalSettlement());

		//Verify Settled transaction in View Transactions
		String noOfTransactions = billingsPage.getNoOfTransactions();
		merchantTransactionsPage = billingsPage.clickUTR(random_UTR);		
		Helper.compareEquals(testConfig, "No. of Records", noOfTransactions, merchantTransactionsPage.getNumberRecords());

		//Verify transaction detail
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);
		dashboardHelper.transactionsPage = merchantTransactionsPage.selectBillingTransaction();
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of captured transaction after recon and verify settled amount should" +
			"should be amount entered and commission should be additional charge PERC charged for billing merchant", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestCapturedBillingSettlementUpload_InvalidPERC(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		//Do Billing transaction with PERC additional charge as special character
		int transactionRowNum = 76;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int paramRow = 18;

		//Navigating to Dashboard's Billing tab to clear settlement Id if any exist for the same date
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();
		manualUpdatePage = billingsPage.DeleteSettlementId(testConfig, helper);

		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.PERC, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		
		String fixed_amount = helper.testResponse.actualResponse.get("amount");
		String amount = fixed_amount;
		String addCharge = helper.testResponse.actualResponse.get("additionalCharges");

		double amnt = Double.parseDouble(amount);		
		double additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		String transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on PERC
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.PERC, paramRow);

		String reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.PERC, paramRow);
		Float commission = new Float(helper.testResponse.actualResponse.get("additionalCharges"));
		String expectedCommission = String.format("%.0f", commission);
		Helper.compareEquals(testConfig, "Commission charged", expectedCommission,
				testDataReaderSummary.GetData(1, "commission charge"));

		//Do Billing transaction with PERC additional charge as alphanumeric invalid data
		paramRow = 19;
		testConfig.putRunTimeProperty("amount", fixed_amount);
		helper.GetTestTransactionPage();
		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.PERC, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		payuid = helper.testResponse.actualResponse.get("mihpayid");
		amount = helper.testResponse.actualResponse.get("amount");
		addCharge = helper.testResponse.actualResponse.get("additionalCharges");

		amnt = Double.parseDouble(amount);		
		additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on PERC
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.PERC, paramRow);

		reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.PERC, paramRow);
		commission = new Float(helper.testResponse.actualResponse.get("additionalCharges"));
		expectedCommission = String.format("%.0f", commission);
		Helper.compareEquals(testConfig, "Commission charged", expectedCommission,
				testDataReaderSummary.GetData(1, "commission charge"));

		//Do Billing transaction with PERC additional charge as alphabets
		paramRow = 20;
		testConfig.putRunTimeProperty("amount", fixed_amount);
		helper.GetTestTransactionPage();
		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.PERC, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		payuid = helper.testResponse.actualResponse.get("mihpayid");
		amount = helper.testResponse.actualResponse.get("amount");
		addCharge = "0.00";

		amnt = Double.parseDouble(amount);		
		additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on PERC
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.PERC, paramRow);

		reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.PERC, paramRow);
		Helper.compareEquals(testConfig, "Commission charged", "0",
				testDataReaderSummary.GetData(1, "commission charge"));

		//Do Billing transaction with PERC additional charge as blank data
		paramRow = 21;
		testConfig.putRunTimeProperty("amount", fixed_amount);
		helper.GetTestTransactionPage();
		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.PERC, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		payuid = helper.testResponse.actualResponse.get("mihpayid");
		amount = helper.testResponse.actualResponse.get("amount");
		addCharge = "0";

		amnt = Double.parseDouble(amount);		
		additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on PERC
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.PERC, paramRow);

		reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.PERC, paramRow);
		Helper.compareEquals(testConfig, "Commission charged", "0",
				testDataReaderSummary.GetData(1, "commission charge"));

		//Do Billing transaction with PERC additional charge as 0
		paramRow = 22;
		testConfig.putRunTimeProperty("amount", fixed_amount);
		helper.GetTestTransactionPage();
		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.PERC, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		payuid = helper.testResponse.actualResponse.get("mihpayid");
		amount = helper.testResponse.actualResponse.get("amount");
		addCharge = "0";

		amnt = Double.parseDouble(amount);		
		additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on PERC
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.PERC, paramRow);

		reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.PERC, paramRow);
		Helper.compareEquals(testConfig, "Commission charged", "0",
				testDataReaderSummary.GetData(1, "commission charge"));


		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Settlement: Upload a valid CitiBankNodal file of captured transaction after recon and verify settled amount should" +
			"should be amount entered and commission should be additional charge PERC charged for billing merchant", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestCapturedBillingSettlementUpload_PERC(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		//Do Billing transaction with FLAT additional charge
		int transactionRowNum = 76;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int paramRow = 17;

		//Navigating to Dashboard's Billing tab to clear settlement Id if any exist for the same date
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();
		manualUpdatePage = billingsPage.DeleteSettlementId(testConfig, helper);

		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.PERC, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String amount = helper.testResponse.actualResponse.get("amount");
		String addCharge = helper.testResponse.actualResponse.get("additionalCharges");

		double amnt = Double.parseDouble(amount);		
		double additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		String transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on PERC
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.PERC, paramRow);

		String reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);

		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.PERC, paramRow);
		Float commission = new Float(helper.testResponse.actualResponse.get("additionalCharges"));
		String commissionCharge = String.format("%.0f", commission);

		Helper.compareEquals(testConfig, "Commission charged", commissionCharge,
				testDataReaderSummary.GetData(1, "commission charge"));

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		//Entering value in .xls file in Parameters from generated recon output
		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settlement_id after verifying
		String settlementid = verify_settlement(testConfig, payuid);

		//Download summary entering settlement id	
		merchantSettlement.getCitiBankNodalSummary(testConfig, settlementid);

		//Enter values from each column of downloaded summary file to .xls file
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Name= testDataReaderSummary.GetData(1, "NAME");
		String MERCHANT_ID= testDataReaderSummary.GetData(1, "MERCHANT_ID");
		String PREFORMA_CODE= testDataReaderSummary.GetData(1, "PREFORMA CODE");
		String RTGS_CODE= testDataReaderSummary.GetData(1, "RTGS CODE");
		String TRANSACTION_AMOUNT= testDataReaderSummary.GetData(1, "TRANSACTION AMOUNT");
		String TRANSACTION_COUNT= testDataReaderSummary.GetData(1, "TRANSACTION COUNT");
		String MERCHANT_AMOUNT= testDataReaderSummary.GetData(1, "MERCHANT AMOUNT");
		String DATE= testDataReaderSummary.GetData(1, "DATE");
		String BENE_AC_NO= testDataReaderSummary.GetData(1, "BENE_AC_NO");
		String IFSC_CODE= testDataReaderSummary.GetData(1, "IFSC_CODE");
		String settlement_id= testDataReaderSummary.GetData(1, "settlement_id");
		String COMMISSION_CHARGE= testDataReaderSummary.GetData(1, "COMMISSION CHARGE");

		//Entering value in .xls file in Parameters 
		settledFile = Helper.getExcelFile(testConfig, FileType.merchantSummary);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,Name, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,MERCHANT_ID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,PREFORMA_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,RTGS_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,TRANSACTION_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,TRANSACTION_COUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,MERCHANT_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,DATE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,BENE_AC_NO, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,IFSC_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,settlement_id, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,COMMISSION_CHARGE, 0);

		merchantSettlement.settleCitiBankNodalFileForTIDGeneration(testConfig, settledFile);

		String settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseTid.php");

		//Verifying if UTR number is update successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		Helper.compareEquals(testConfig, "File Result", "SUCCESS - New TID generated", testDataReader.GetData(1, "result"));
		String TID= testDataReader.GetData(1, "tid");
		settlement_id= testDataReader.GetData(1, "settlement_id");
		String merchant_amount= testDataReader.GetData(1, "merchant amount");
		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		String bank_ref_num = Helper.generateRandomAlphaNumericString(6);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RequestsPage request= dashBoard.ClickViewRequest();

		request.SearchTransaction(settlement_id, SearchOn.PayuSettlementId);
		Helper.compareEquals(testConfig, "Settlement Status", "approval pending",request.getSettle());

		helper.GetTestTransactionPage();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Final settlement
		String tidFile = Helper.getExcelFile(testConfig, FileType.TIDUTR);

		FileHandler.write_edit(tidFile,"Sheet1", 1, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 18, current_date, 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseUtr.php");

		//Verifying if TID UTR number is update successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		Helper.compareEquals(testConfig, "Success status of UTR TID upload", "SUCCESS - UTR and value date updated successfully", testDataReader.GetData(1, "result"));

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		//Verify Recent payment,Settlement data, settlement id and UTR number in Billing Tab's Most Recent Payments
		Helper.compareEquals(testConfig, "Settlement Id", "Most recent payment - "+settlementid.toUpperCase(), billingsPage.SettlementId());
		Helper.compareEquals(testConfig, "UTR value", random_UTR, billingsPage.UTR());
		transactionAmount = String.format("%.2f", amnt);
		Helper.compareEquals(testConfig, "Transaction Amount", transactionAmount, billingsPage.getTransactionAmount());
		Helper.compareEquals(testConfig, "TDR Fee", helper.GetTxnUpdateData()[0], billingsPage.getTDRFee());
		Helper.compareEquals(testConfig, "Service Tax Amount", helper.GetTxnUpdateData()[1], billingsPage.getServiceTax());
		Helper.compareEquals(testConfig, "Net Amount", helper.GetTxnUpdateData()[2], billingsPage.getNetAmount());
		Helper.compareEquals(testConfig, "Total Settlement Amount", helper.testResponse.actualResponse.get("amount"), billingsPage.getTotalSettlement());

		//Verify Settled transaction in View Transactions
		String noOfTransactions = billingsPage.getNoOfTransactions();
		merchantTransactionsPage = billingsPage.clickUTR(random_UTR);		
		Helper.compareEquals(testConfig, "No. of Records", noOfTransactions, merchantTransactionsPage.getNumberRecords());

		//Verify transaction detail
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);
		dashboardHelper.transactionsPage = merchantTransactionsPage.selectBillingTransaction();
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}



	@Test(description = "Settlement: Upload a valid CitiBankNodal file of failed/dropped transaction after recon and verify settled amount should" +
			"should be amount entered and commission should be additional charge PERC charged for billing merchant", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestFailedBillingSettlementUpload_PERC(Config testConfig) throws AWTException, IOException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		//Do Billing transaction with FLAT additional charge
		int transactionRowNum = 76;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 17;
		int paramRow = 17;

		//Navigating to Dashboard's Billing tab to clear settlement Id if any exist for the same date
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();
		manualUpdatePage = billingsPage.DeleteSettlementId(testConfig, helper);

		billingAdditionalCharge(testConfig, transactionRowNum, helper, AdditionalChargeType.PERC, paramRow);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		String payuid = helper.testResponse.actualResponse.get("mihpayid");
		String amount = helper.testResponse.actualResponse.get("amount");
		String addCharge = helper.testResponse.actualResponse.get("additionalCharges");

		double amnt = Double.parseDouble(amount);		
		double additionalCharges = Double.parseDouble(addCharge);
		amnt = amnt+additionalCharges;	
		String transactionAmount = String.valueOf(amnt)+"0";

		//Verify AdditionalCharge on PERC
		verify_billingAdditionalCharge(testConfig, transactionRowNum, helper, helper.testResponse, AdditionalChargeType.PERC, paramRow);

		String reconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(reconFile, "Sheet1", 1, 1,
				helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 7,
				transactionAmount, 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 11,
				helper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 12,
				"default", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 3, "success", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 5, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 6, "0", 0);
		FileHandler.write_edit(reconFile, "Sheet1", 1, 13, "0", 0);
		
		// Navigating to Admin Recon
		helper.home = helper.testResponse.ClickHomeLink();
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();

		reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, reconFile);

		helper.verify_recon(testConfig, payuid);
		Browser.wait(testConfig, 2);
		String summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		//Enter values from each column of downloaded summary file to .xls file
		TestDataReader testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		helper.verify_billing_tdr_calculation(testConfig, helper.testResponse, AdditionalChargeType.PERC, paramRow);
		Float commission = new Float(helper.testResponse.actualResponse.get("additionalCharges"));
		String commissionCharge = String.format("%.0f", commission);

		Helper.compareEquals(testConfig, "Commission charged", commissionCharge,
				testDataReaderSummary.GetData(1, "commission charge"));

		//Column values from Downloaded summary
		String Action= testDataReaderSummary.GetData(1, "action");
		String Status= testDataReaderSummary.GetData(1, "status");
		String ServiceFee= testDataReaderSummary.GetData(1, "servicefee");
		String ServiceTax= testDataReaderSummary.GetData(1, "servicetax");
		String UTR= Helper.generateRandomAlphabetsString(5);
		String Refnum= testDataReaderSummary.GetData(1, "refnum");
		String Category= testDataReaderSummary.GetData(1, "category");
		String CardType= testDataReaderSummary.GetData(1, "cardtype");
		String RequestId= testDataReaderSummary.GetData(1, "requestid");
		String ReconciliationId= testDataReaderSummary.GetData(1, "reconciliationid");
		String PayUMID= testDataReaderSummary.GetData(1, "PayUMID");
		String MerchantName= testDataReaderSummary.GetData(1, "merchantName");
		String pgMID= testDataReaderSummary.GetData(1, "pgMID");
		String transactionDate= testDataReaderSummary.GetData(1, "transactionDate");
		String refundDate= testDataReaderSummary.GetData(1, "refundDate");
		String payuCharge= testDataReaderSummary.GetData(1, "pay u charge");
		String tdrServiceTax= testDataReaderSummary.GetData(1, "service tax");
		String commission_charge= testDataReaderSummary.GetData(1, "commission charge");
		String result= testDataReaderSummary.GetData(1, "result");
		String prev_status= testDataReaderSummary.GetData(1, "prev_status");

		//Entering value in .xls file in Parameters from generated recon output
		String settledFile = Helper.getExcelFile(testConfig, FileType.citinodalSettlement);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,helper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,Action, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,Status, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,ServiceFee, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,ServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,transactionAmount, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,UTR, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,Refnum, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,Category, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,CardType, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,RequestId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 12,ReconciliationId, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 13,PayUMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 14,MerchantName, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 15,pgMID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 16,transactionDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 17,refundDate, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 18,payuCharge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 19,tdrServiceTax, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 20,helper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 21,commission_charge, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 22,result, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 23,prev_status, 0);

		//Navigating to Merchant Settlement 		
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Upload CitiBankNodal File
		merchantSettlement.uploadCitiBankNodalFile(testConfig, settledFile);

		//Get Settlement_id after verifying
		String settlementid = verify_settlement(testConfig, payuid);

		//Download summary entering settlement id	
		merchantSettlement.getCitiBankNodalSummary(testConfig, settlementid);

		//Enter values from each column of downloaded summary file to .xls file
		summaryfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, summaryfilepath);
		fileName = file.getName();

		testDataReaderSummary = new TestDataReader(testConfig, "Sheet1",summaryfilepath + fileName);

		//Column values from Downloaded summary
		String Name= testDataReaderSummary.GetData(1, "NAME");
		String MERCHANT_ID= testDataReaderSummary.GetData(1, "MERCHANT_ID");
		String PREFORMA_CODE= testDataReaderSummary.GetData(1, "PREFORMA CODE");
		String RTGS_CODE= testDataReaderSummary.GetData(1, "RTGS CODE");
		String TRANSACTION_AMOUNT= testDataReaderSummary.GetData(1, "TRANSACTION AMOUNT");
		String TRANSACTION_COUNT= testDataReaderSummary.GetData(1, "TRANSACTION COUNT");
		String MERCHANT_AMOUNT= testDataReaderSummary.GetData(1, "MERCHANT AMOUNT");
		String DATE= testDataReaderSummary.GetData(1, "DATE");
		String BENE_AC_NO= testDataReaderSummary.GetData(1, "BENE_AC_NO");
		String IFSC_CODE= testDataReaderSummary.GetData(1, "IFSC_CODE");
		String settlement_id= testDataReaderSummary.GetData(1, "settlement_id");
		String COMMISSION_CHARGE= testDataReaderSummary.GetData(1, "COMMISSION CHARGE");

		//Entering value in .xls file in Parameters 
		settledFile = Helper.getExcelFile(testConfig, FileType.merchantSummary);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,Name, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,MERCHANT_ID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,PREFORMA_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,RTGS_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,TRANSACTION_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,TRANSACTION_COUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,MERCHANT_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,DATE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,BENE_AC_NO, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,IFSC_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,settlement_id, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,COMMISSION_CHARGE, 0);

		merchantSettlement.settleCitiBankNodalFileForTIDGeneration(testConfig, settledFile);

		String settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseTid.php");

		//Verifying if UTR number is update successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		Helper.compareEquals(testConfig, "File Result", "SUCCESS - New TID generated", testDataReader.GetData(1, "result"));
		String TID= testDataReader.GetData(1, "tid");
		settlement_id= testDataReader.GetData(1, "settlement_id");
		String merchant_amount= testDataReader.GetData(1, "merchant amount");
		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		String bank_ref_num = Helper.generateRandomAlphaNumericString(6);

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RequestsPage request= dashBoard.ClickViewRequest();

		request.SearchTransaction(settlement_id, SearchOn.PayuSettlementId);
		Helper.compareEquals(testConfig, "Settlement Status", "approval pending",request.getSettle());

		helper.GetTestTransactionPage();
		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

		//Final settlement
		String tidFile = Helper.getExcelFile(testConfig, FileType.TIDUTR);

		FileHandler.write_edit(tidFile,"Sheet1", 1, 9,merchant_amount, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 10, bank_ref_num, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 11,TID, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 12, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 14, "UTR "+random_UTR, 0);
		FileHandler.write_edit(tidFile,"Sheet1", 1, 18, current_date, 0);

		merchantSettlement.uploadTIDUTRFIle(testConfig, tidFile);
		settlementfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		file = Browser.lastFileModified(testConfig, settlementfilepath);
		fileName = file.getName();
		
		merchantSettlement.cronRun("updateTxnWiseUtr.php");

		//Verifying if TID UTR number is update successfully
		testDataReader = new TestDataReader(testConfig, "Sheet1",settlementfilepath + fileName);
		Helper.compareEquals(testConfig, "Success status of UTR TID upload", "SUCCESS - UTR and value date updated successfully", testDataReader.GetData(1, "result"));

		//Navigating to Dashboard's Billing tab
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		billingsPage = dashBoard.clickBilling();

		//Verify Recent payment,Settlement data, settlement id and UTR number in Billing Tab's Most Recent Payments
		Helper.compareEquals(testConfig, "Settlement Id", "Most recent payment - "+settlementid.toUpperCase(), billingsPage.SettlementId());
		Helper.compareEquals(testConfig, "UTR value", random_UTR, billingsPage.UTR());
		transactionAmount = String.format("%.2f", amnt);
		Helper.compareEquals(testConfig, "Transaction Amount", transactionAmount, billingsPage.getTransactionAmount());
		Helper.compareEquals(testConfig, "TDR Fee", helper.GetTxnUpdateData()[0], billingsPage.getTDRFee());
		Helper.compareEquals(testConfig, "Service Tax Amount", helper.GetTxnUpdateData()[1], billingsPage.getServiceTax());
		Helper.compareEquals(testConfig, "Net Amount", helper.GetTxnUpdateData()[2], billingsPage.getNetAmount());
		Helper.compareEquals(testConfig, "Total Settlement Amount", helper.testResponse.actualResponse.get("amount"), billingsPage.getTotalSettlement());

		//Verify Settled transaction in View Transactions
		String noOfTransactions = billingsPage.getNoOfTransactions();
		merchantTransactionsPage = billingsPage.clickUTR(random_UTR);		
		Helper.compareEquals(testConfig, "No. of Records", noOfTransactions, merchantTransactionsPage.getNumberRecords());

		//Verify transaction detail
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);
		dashboardHelper.transactionsPage = merchantTransactionsPage.selectBillingTransaction();
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	/**
	 * @param testConfig
	 * @return settlement id
	 */
	public String verify_settlement(Config testConfig, String Payuid){ 
		String settlementFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,settlementFilePath);
		String settlement_fileName = file.getName();

		//Get data from downloaded excel
		TestDataReader testDataReader  = new TestDataReader(testConfig,"Sheet1", "C:\\Users\\"+System.getProperty("user.name")+"\\Downloads\\"+settlement_fileName);
		String file_mid = testDataReader.GetCurrentEnvironmentData(1, "mid");
		String file_requestid = testDataReader.GetCurrentEnvironmentData(1, "requestid");
		String file_mihpayuid = testDataReader.GetCurrentEnvironmentData(1, "payu reference");
		String amount = testDataReader.GetCurrentEnvironmentData(1, "amount");
		Float file_amount = new Float(amount);
		String serviceCharge = testDataReader.GetCurrentEnvironmentData(1, "pay u charge");
		Float file_serviceCharge = new Float(serviceCharge);
		String serviceTax = testDataReader.GetCurrentEnvironmentData(1, "service tax");
		Float file_serviceTax = new Float(serviceTax);
		String file_settlementAmount = testDataReader.GetCurrentEnvironmentData(1, "settlement amount");
		String file_transactionType = testDataReader.GetCurrentEnvironmentData(1, "transactiontype");
		String file_settlementid = testDataReader.GetCurrentEnvironmentData(1, "settlement_id");
		String file_result = testDataReader.GetCurrentEnvironmentData(1, "result");

		//Get data from txn_update table of DB
		testConfig.putRunTimeProperty("mihpayid", Payuid);
		Map<String, String> txn_update = DataBase.executeSelectQuery(testConfig, 4, 1);
		Helper.compareEquals(testConfig, "Merchant Id", file_mid, txn_update.get("merchant_id"));
		Helper.compareEquals(testConfig, "Request Id", file_requestid, txn_update.get("id"));
		Helper.compareEquals(testConfig, "Transaction Payu Id", file_mihpayuid, txn_update.get("txnid"));
		Helper.compareEquals(testConfig, "Transaction Amount", String.format("%.2f",file_amount), txn_update.get("amount"));
		Helper.compareEquals(testConfig, "TDR PayU Charge", String.format("%.2f",file_serviceCharge), 
				txn_update.get("mer_service_fee"));
		Helper.compareEquals(testConfig, "TDR Service Tax", String.format("%.2f",file_serviceTax), 
				txn_update.get("mer_service_tax"));
		Helper.compareEquals(testConfig, "Transaction Settlement Amount", file_settlementAmount, txn_update.get("mer_net_amount"));
		Helper.compareEquals(testConfig, "Transaction Action Type", file_transactionType, txn_update.get("action"));
		Helper.compareEquals(testConfig, "File Result", file_result, "SUCCESS - Transaction updated successfully");

		return file_settlementid;
	}

	/**
	 * @param testConfig
	 * @return settlement id
	 */
	public String[] verify_esscom_settlement_output(Config testConfig, String Istpayuids, String IIndpayuid){ 
		String settlementFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,settlementFilePath);
		String settlement_fileName = file.getName();

		TestDataReader testDataReader  = new TestDataReader(testConfig,"Sheet1", "C:\\Users\\"+System.getProperty("user.name")+"\\Downloads\\"+settlement_fileName);
		String[] mihpayid = new String[2];
		String[] file_amount = new String[2];
		String[] file_transactionFee = new String[2];
		String[] file_serviceTax = new String[2];
		String[] file_amountSettled = new String[2];
		String[] file_transactionType = new String[2];
		String[] file_utr = new String[2];
		String[] file_valueDate = new String[2];
		String[] file_cardType = new String[2];
		String[] file_result = new String[2];
		String[] file_settlementid = new String[2];

		//Get data of Ist row from downloaded excel
		mihpayid[0] = testDataReader.GetCurrentEnvironmentData(1, "merchant ref no");
		file_amount[0] = testDataReader.GetCurrentEnvironmentData(1, "amount");
		file_transactionFee[0] = testDataReader.GetCurrentEnvironmentData(1, "transaction fee");
		file_serviceTax[0] = testDataReader.GetCurrentEnvironmentData(1, "service tax");
		file_amountSettled[0] = testDataReader.GetCurrentEnvironmentData(1, "amount settled");
		file_transactionType[0] = testDataReader.GetCurrentEnvironmentData(1, "transactiontype");
		file_utr[0] = testDataReader.GetCurrentEnvironmentData(1, "utr");
		file_valueDate[0] = testDataReader.GetCurrentEnvironmentData(1, "value date");
		file_cardType[0] = testDataReader.GetCurrentEnvironmentData(1, "cardtype");
		file_result[0] = testDataReader.GetCurrentEnvironmentData(1, "result");
		file_settlementid[0] = testDataReader.GetCurrentEnvironmentData(1, "settlement_id");

		//Get data from txn_update table in DB
		Map<String, String> txn_update = null;
		testConfig.putRunTimeProperty("mihpayid", Istpayuids);
		txn_update = DataBase.executeSelectQuery(testConfig, 4, 1);

		//Get data from txn_update table in DB
		Map<String, String> txn_info = null;
		testConfig.putRunTimeProperty("payu_id", Istpayuids);
		txn_info = DataBase.executeSelectQuery(testConfig, 91, 1);

		//verify data between excel and db
		Helper.compareEquals(testConfig, "Mihpayid of Ist transaction", 
				mihpayid[0], txn_update.get("txnid"));
		Helper.compareEquals(testConfig, "Amount of Ist transaction", 
				file_amount[0], txn_update.get("amount"));
		Helper.compareEquals(testConfig, "Transaction fee of Ist transaction", 
				file_transactionFee[0], txn_update.get("mer_service_fee"));
		Helper.compareEquals(testConfig, "Service Tax of Ist transaction", 
				file_serviceTax[0], txn_update.get("mer_service_tax"));
		Helper.compareEquals(testConfig, "Amount Settled of Ist transaction", 
				file_amountSettled[0], txn_update.get("mer_net_amount"));
		Helper.compareEquals(testConfig, "Transaction Status of Ist transaction", 
				file_transactionType[0], txn_update.get("action"));
		Helper.compareEquals(testConfig, "UTR of Ist transaction", 
				file_utr[0], txn_update.get("mer_utr"));

		String valueDate1[] = file_valueDate[0].split("'");
		Helper.compareEquals(testConfig, "Value date of Ist transaction", 
				valueDate1[1], txn_update.get("value_date"));

		Helper.compareEquals(testConfig, "CardType of Ist transaction", 
				file_cardType[0], txn_info.get("cardtype"));
		Helper.compareEquals(testConfig, "SettlementId of Ist transaction", 
				file_settlementid[0], txn_update.get("settlement_id"));
		Helper.compareEquals(testConfig, "Settlement Result of 1st transaction",
				file_result[0], "SUCCESS - Transaction updated successfully");

		//Get data of 2nd row from downloaded excel
		mihpayid[1] = testDataReader.GetCurrentEnvironmentData(2, "merchant ref no");
		file_amount[1] = testDataReader.GetCurrentEnvironmentData(2, "amount");
		file_transactionFee[1] = testDataReader.GetCurrentEnvironmentData(2, "transaction fee");
		file_serviceTax[1] = testDataReader.GetCurrentEnvironmentData(2, "service tax");
		file_amountSettled[1] = testDataReader.GetCurrentEnvironmentData(2, "amount settled");
		file_transactionType[1] = testDataReader.GetCurrentEnvironmentData(2, "transactiontype");
		file_utr[1] = testDataReader.GetCurrentEnvironmentData(2, "utr");
		file_valueDate[1] = testDataReader.GetCurrentEnvironmentData(2, "value date");
		file_cardType[1] = testDataReader.GetCurrentEnvironmentData(2, "cardtype");
		file_result[1] = testDataReader.GetCurrentEnvironmentData(2, "result");
		file_settlementid[1] = testDataReader.GetCurrentEnvironmentData(2, "settlement_id");

		//Get data from txn_update table in DB
		testConfig.putRunTimeProperty("mihpayid", IIndpayuid);
		txn_update = DataBase.executeSelectQuery(testConfig, 4, 1);

		//Get data from txn_update table in DB
		testConfig.putRunTimeProperty("payu_id", IIndpayuid);
		txn_info = DataBase.executeSelectQuery(testConfig, 91, 1);

		//verify data between excel and db
		Helper.compareEquals(testConfig, "Mihpayid of 2nd transaction", 
				mihpayid[1], txn_update.get("txnid"));
		Helper.compareEquals(testConfig, "Amount of 2nd transaction", 
				file_amount[1], txn_update.get("amount"));
		Helper.compareEquals(testConfig, "Transaction fee of 2nd transaction", 
				file_transactionFee[1], txn_update.get("mer_service_fee"));
		Helper.compareEquals(testConfig, "Service Tax of 2nd transaction", 
				file_serviceTax[1], txn_update.get("mer_service_tax"));
		Helper.compareEquals(testConfig, "Amount Settled of 2nd transaction", 
				file_amountSettled[1], txn_update.get("mer_net_amount"));
		Helper.compareEquals(testConfig, "Transaction Status of 2nd transaction", 
				file_transactionType[1], txn_update.get("action"));
		Helper.compareEquals(testConfig, "UTR of 2nd transaction", 
				file_utr[1], txn_update.get("mer_utr"));

		String valueDate2[] = file_valueDate[0].split("'");
		Helper.compareEquals(testConfig, "Value date of 2nd transaction", 
				valueDate2[1], txn_update.get("value_date"));

		Helper.compareEquals(testConfig, "CardType of 2nd transaction", 
				file_cardType[1], txn_info.get("cardtype"));
		Helper.compareEquals(testConfig, "SettlementId of 2nd transaction", 
				file_settlementid[1], txn_update.get("settlement_id"));
		Helper.compareEquals(testConfig, "Settlement Result of 2nd transaction",
				file_result[1], "SUCCESS - Transaction updated successfully");


		return file_settlementid;
	}

	/**
	 * @param testConfig
	 *//*
	public void verify_recon(Config testConfig) {
		String settlementFilePath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, settlementFilePath);
		String settlement_fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", "C:\\Users\\" + System.getProperty("user.name")
				+ "\\Downloads\\" + settlement_fileName);
		TestDataReader reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");

		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		if (testDataReader.GetData(1, "prev_status").equals("failed")
				|| testDataReader.GetData(1, "prev_status").equals("dropped")) {
			Helper.compareContains(testConfig, "File Result",
					reconoutputdata.GetData(13, "successVerify"),
					file_result);
		} else {
			Helper.compareEquals(testConfig, "File Result",
					reconoutputdata.GetData(14, "successVerify"),
					file_result);
		}

	}*/
	public String verify_MerchantSummary(Config testConfig , String Payuid) {
		String summaryfilepath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, summaryfilepath);
		String fileName = file.getName();

		TestDataReader testDataReaderSummary = new TestDataReader(testConfig,
				"Sheet1", "C:\\Users\\" + System.getProperty("user.name")
				+ "\\Downloads\\" + fileName);


		//Column values from Downloaded summary
		String Name= testDataReaderSummary.GetData(1, "NAME");
		String MERCHANT_ID= testDataReaderSummary.GetData(1, "MERCHANT_ID");
		String PREFORMA_CODE= testDataReaderSummary.GetData(1, "PREFORMA CODE");
		String RTGS_CODE= testDataReaderSummary.GetData(1, "RTGS CODE");
		String TRANSACTION_AMOUNT= testDataReaderSummary.GetData(1, "TRANSACTION AMOUNT");
		String TRANSACTION_COUNT= testDataReaderSummary.GetData(1, "TRANSACTION COUNT");
		String MERCHANT_AMOUNT= testDataReaderSummary.GetData(1, "MERCHANT AMOUNT");
		String DATE= testDataReaderSummary.GetData(1, "DATE");
		String BENE_AC_NO= testDataReaderSummary.GetData(1, "BENE_AC_NO");
		String IFSC_CODE= testDataReaderSummary.GetData(1, "IFSC_CODE");
		String settlement_id= testDataReaderSummary.GetData(1, "settlement_id");
		String COMMISSION_CHARGE= testDataReaderSummary.GetData(1, "COMMISSION CHARGE");

		//Get data from txn_update table of DB
		testConfig.putRunTimeProperty("mihpayid", Payuid);
		Map<String, String> txn_update = DataBase.executeSelectQuery(testConfig, 4, 1);

		//Verify the data from DB and Excel
		Helper.compareEquals(testConfig, "Merchant Id", MERCHANT_ID, txn_update.get("merchant_id"));
		Helper.compareEquals(testConfig, "Transaction Amount", TRANSACTION_AMOUNT, txn_update.get("inr_amount"));
		Helper.compareEquals(testConfig, "Merchant Amount", MERCHANT_AMOUNT, txn_update.get("mer_net_amount"));
		Helper.compareEquals(testConfig, "Settlement Id ", settlement_id, txn_update.get("settlement_id"));

		//Entering value in .xls file in Parameters 
		String settledFile = Helper.getExcelFile(testConfig, FileType.merchantSummary);

		FileHandler.write_edit(settledFile,"Sheet1", 1, 0,Name, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 1,MERCHANT_ID, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 2,PREFORMA_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 3,RTGS_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 4,TRANSACTION_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 5,TRANSACTION_COUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 6,MERCHANT_AMOUNT, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 7,DATE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 8,BENE_AC_NO, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 9,IFSC_CODE, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 10,settlement_id, 0);
		FileHandler.write_edit(settledFile,"Sheet1", 1, 11,COMMISSION_CHARGE, 0);

		return settledFile;
	}

	/**
	 * @param testConfig
	 */
	public String verify_TIDgenerated(Config testConfig) {
		String tidGeneratedFilePath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, tidGeneratedFilePath);
		String tidutr_fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", "C:\\Users\\" + System.getProperty("user.name")
				+ "\\Downloads\\" + tidutr_fileName);
		TestDataReader reconoutputdata = new TestDataReader(testConfig,
				"ReconResponse");
		String file_merchantId = testDataReader.GetCurrentEnvironmentData(1,
				"merchant_id");
		String file_merchantAmount = testDataReader.GetCurrentEnvironmentData(1,
				"merchant amount");
		String file_settlementId = testDataReader.GetCurrentEnvironmentData(1,
				"settlement_id");
		String file_TID = testDataReader.GetCurrentEnvironmentData(1,
				"tid");
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		
		//Get data from merchant_settlement table in DB
		testConfig.putRunTimeProperty("id", file_TID);
		Map<String, String> merchant_settlement = DataBase.executeSelectQuery(testConfig, 92, 1);
		
		//verify data from db and excel
		Helper.compareEquals(testConfig, "Merchant Id", file_merchantId, merchant_settlement.get("merchant_id"));
		Helper.compareEquals(testConfig, "Merchant Amount", file_merchantAmount, merchant_settlement.get("amount"));
		Helper.compareEquals(testConfig, "Settlement Id", file_settlementId, merchant_settlement.get("settlement_id"));
		Helper.compareEquals(testConfig, "TID exists in merchant settlement table", file_TID, merchant_settlement.get("id"));
		Helper.compareEquals(testConfig, "Success status of UTR TID upload", file_result, reconoutputdata.GetData(19, "result"));

		return file_TID;
	}

	/**
	 * @param testConfig
	 */
	public void verify_TIDsettlement(Config testConfig, String Payuid) {
		String tidutrFilePath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, tidutrFilePath);
		String tidutr_fileName = file.getName();

		testDataReader = new TestDataReader(testConfig,
				"Sheet1", "C:\\Users\\" + System.getProperty("user.name")
				+ "\\Downloads\\" + tidutr_fileName);
		TestDataReader reconoutputdata = new TestDataReader(testConfig,
				"ReconResponse");

		String file_merchantId = testDataReader.GetCurrentEnvironmentData(1,
				"merchant_id");
		String file_UTR = testDataReader.GetCurrentEnvironmentData(1,
				"utr number");
		String file_TID = testDataReader.GetCurrentEnvironmentData(1,
				"TID");
		String file_ValueDate = testDataReader.GetCurrentEnvironmentData(1,
				"value date");
		String file_settlementId = testDataReader.GetCurrentEnvironmentData(1,
				"settlement_id");
		String file_Amount = testDataReader.GetCurrentEnvironmentData(1,
				"amount");
		String file_result = testDataReader.GetCurrentEnvironmentData(1,
				"result");
		
		//Get data from txn_update table in DB
		testConfig.putRunTimeProperty("mihpayid", Payuid);
		Map<String, String> txn_update = DataBase.executeSelectQuery(testConfig, 4, 1);
				
		//Get data from merchant settlement table in DB
		testConfig.putRunTimeProperty("id", file_TID);
		Map<String, String> merchant_settlement = DataBase.executeSelectQuery(testConfig, 92, 1);
				
		//verify data between DB and excel
		Helper.compareEquals(testConfig, "Merchant Id", file_merchantId, merchant_settlement.get("merchant_id"));
		Helper.compareEquals(testConfig, "Merchant UTR", file_UTR, txn_update.get("mer_utr"));
		Helper.compareEquals(testConfig, "TID", file_TID, merchant_settlement.get("id"));
		Helper.compareEquals(testConfig, "Value Date", file_ValueDate, txn_update.get("value_date"));
		Helper.compareEquals(testConfig, "Settlement Id", file_settlementId, txn_update.get("settlement_id"));
		Helper.compareEquals(testConfig, "Amount", file_Amount, txn_update.get("mer_net_amount"));
		Helper.compareEquals(testConfig, "Success status of UTR TID upload", 
				file_result, reconoutputdata.GetData(20, "result"));

	}

	/**
	 * @param testConfig
	 * @param transactionRowNum
	 * @param helper
	 * @param testResponsePage
	 * @param addnChargeType
	 * @param paramRow Additional charge to be picked from row
	 */
	public void billingAdditionalCharge(Config testConfig, int transactionRowNum, TransactionHelper helper, AdditionalChargeType addnChargeType, int paramRow) {

		helper.transactionData = new TestDataReader(helper.testConfig,"TransactionDetails");
		String merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");

		MerchantListPage merchantListPage = helper.home.clickMerchantList();
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		paramsPage = merchantDetailsPage.ClickParams();
		paramsMerchantParamsPage =paramsPage.clickMerchantParams();

		switch (addnChargeType) {
		case FLAT:
			switch (paramRow) {
			case 11:
				paramsMerchantParamsPage.addKeyValue(11);
				break;
			case 12:
				paramsMerchantParamsPage.addKeyValue(12);
				break;
			case 13:
				paramsMerchantParamsPage.addKeyValue(13);
				break;
			case 14:
				paramsMerchantParamsPage.addKeyValue(14);
				break;
			case 15:
				paramsMerchantParamsPage.addKeyValue(15);
				break;
			case 16:
				paramsMerchantParamsPage.addKeyValue(16);
				break;
			}			
			break;

		case PERC:
			switch (paramRow) {
			case 17:
				paramsMerchantParamsPage.addKeyValue(17);
				break;
			case 18:
				paramsMerchantParamsPage.addKeyValue(18);
				break;
			case 19:
				paramsMerchantParamsPage.addKeyValue(19);
				break;
			case 20:
				paramsMerchantParamsPage.addKeyValue(20);
				break;
			case 21:
				paramsMerchantParamsPage.addKeyValue(21);
				break;
			case 22:
				paramsMerchantParamsPage.addKeyValue(22);
				break;
			}			
			break;

		default:
			break;
		}


	}
	/**
	 * @param testConfig
	 * @param transactionRowNum
	 * @param helper
	 * @param testResponsePage
	 * @param addnChargeType
	 * @param paramRow Additional charge to be picked from row
	 */
	public void verify_billingAdditionalCharge(Config testConfig, int transactionRowNum, TransactionHelper helper, TestResponsePage testResponsePage, AdditionalChargeType addnChargeType, int paramRow) {

		TestDataReader data;
		String ExpectedAddnCharge = null;
		String ActualAddnCharge = null;
		String value;
		Float addnCharge;
		Float addnValue;
		Float calculateAddnCharge;

		switch (addnChargeType) {
		case FLAT:
			data = new TestDataReader(testConfig,"Params");

			//For correct data
			if (paramRow == 11) {
				value = data.GetData(11, "value");
				String flatAddntype[] = value.split(":");
				value = flatAddntype[1];

				addnCharge = new Float(testResponsePage.actualResponse.get("additionalCharges"));
				addnValue = new Float(value);
				calculateAddnCharge = (float) (addnValue+((addnValue*12.36)/100));

				ExpectedAddnCharge = String.format("%.2f", calculateAddnCharge);
				ActualAddnCharge = String.format("%.2f", addnCharge);
			}

			//For Data with special characters
			else if (paramRow == 12) {
				value = data.GetData(11, "value");
				String Addntype[] = value.split(":");
				String flatAddntype[] = Addntype[1].split("!");
				value = flatAddntype[0];

				addnCharge = new Float(testResponsePage.actualResponse.get("additionalCharges"));
				addnValue = new Float(value);
				calculateAddnCharge = (float) (addnValue+((addnValue*12.36)/100));

				ExpectedAddnCharge = String.format("%.2f", calculateAddnCharge);
				ActualAddnCharge = String.format("%.2f", addnCharge);
			}

			//For alphanumeric invalid data
			else if (paramRow == 13) {
				value = data.GetData(11, "value");
				String Addntype[] = value.split(":");
				String flatAddntype[] = Addntype[1].split("a");
				value = flatAddntype[0];

				addnCharge = new Float(testResponsePage.actualResponse.get("additionalCharges"));
				addnValue = new Float(value);
				calculateAddnCharge = (float) (addnValue+((addnValue*12.36)/100));

				ExpectedAddnCharge = String.format("%.2f", calculateAddnCharge);
				ActualAddnCharge = String.format("%.2f", addnCharge);
			}

			/*For invalid data containing
			1. Alphabets
			2. Blank data
			3. 0 amount */
			else if (paramRow == 14 || paramRow ==15 || paramRow == 16) {
				value = data.GetData(11, "value");
				String Addntype[] = value.split(":");
				value = Addntype[1];

				if (value.contains("abc") || value.contains("") || value.contains("0")) {
					addnCharge = (float) 0.0;
					addnValue = (float) 0.0;
					calculateAddnCharge = (float) (addnValue+((addnValue*12.36)/100));

					ExpectedAddnCharge = String.format("%.2f", calculateAddnCharge);
					ActualAddnCharge = String.format("%.2f", addnCharge);
				}
			}			

			Helper.compareEquals(testConfig, "Additional Charge deducted", ExpectedAddnCharge, ActualAddnCharge);			
			break;

		case PERC:
			data = new TestDataReader(testConfig,"Params");

			//For correct data
			if (paramRow == 17) {
				value = data.GetData(17, "value");
				String flatAddntype[] = value.split(":");
				value = flatAddntype[1];

				Float amount = new Float(testResponsePage.actualResponse.get("amount"));
				addnCharge = new Float(testResponsePage.actualResponse.get("additionalCharges"));
				addnValue = new Float(value);
				calculateAddnCharge = (float) (((addnValue*amount)/100));

				ExpectedAddnCharge = String.format("%.2f", calculateAddnCharge);
				ActualAddnCharge = String.format("%.2f", addnCharge);
			}

			//For Data with special characters
			else if (paramRow == 18) {
				value = data.GetData(11, "value");
				String Addntype[] = value.split(":");
				String flatAddntype[] = Addntype[1].split("!");
				value = flatAddntype[0];

				Float amount = new Float(testResponsePage.actualResponse.get("amount"));
				addnCharge = new Float(testResponsePage.actualResponse.get("additionalCharges"));
				addnValue = new Float(value);
				calculateAddnCharge = (float) (((addnValue*amount)/100));

				ExpectedAddnCharge = String.format("%.2f", calculateAddnCharge);
				ActualAddnCharge = String.format("%.2f", addnCharge);
			}

			//For alphanumeric invalid data
			else if (paramRow == 19) {
				value = data.GetData(11, "value");
				String Addntype[] = value.split(":");
				String flatAddntype[] = Addntype[1].split("a");
				value = flatAddntype[0];

				Float amount = new Float(testResponsePage.actualResponse.get("amount"));
				addnCharge = new Float(testResponsePage.actualResponse.get("additionalCharges"));
				addnValue = new Float(value);
				calculateAddnCharge = (float) (((addnValue*amount)/100));

				ExpectedAddnCharge = String.format("%.2f", calculateAddnCharge);
				ActualAddnCharge = String.format("%.2f", addnCharge);
			}

			/*For invalid data containing
			1. Alphabets
			2. Blank data
			3. 0 amount */
			else if (paramRow == 20 || paramRow == 21 || paramRow == 22) {
				value = data.GetData(11, "value");
				String Addntype[] = value.split(":");
				value = Addntype[1];

				if (value.contains("abc") || value.contains("") || value.contains("0")) {
					addnCharge = (float) 0.0;
					addnValue = (float) 0.0;

					Float amount = new Float(testResponsePage.actualResponse.get("amount"));
					calculateAddnCharge = (float) (((addnValue*amount)/100));

					ExpectedAddnCharge = String.format("%.2f", calculateAddnCharge);
					ActualAddnCharge = String.format("%.2f", addnCharge);
				}
			}		
			/*data = new TestDataReader(testConfig,"Params");
			value = data.GetData(11, "value");
			String percAddntype[] = value.split(":");
			value = percAddntype[1];

			Float amount = new Float(testResponsePage.actualResponse.get("amount"));
			addnCharge = new Float(testResponsePage.actualResponse.get("additionalCharges"));
			addnValue = new Float(value);
			calculateAddnCharge = (float) (((addnValue*amount)/100));

			ExpectedAddnCharge = String.format("%.2f", calculateAddnCharge);
			ActualAddnCharge = String.format("%.2f", addnCharge);*/

			Helper.compareEquals(testConfig, "Additional Charge deducted", ExpectedAddnCharge, ActualAddnCharge);

			break;

		default:
			break;
		}


	}
}
