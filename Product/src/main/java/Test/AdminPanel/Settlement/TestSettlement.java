package Test.AdminPanel.Settlement;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.ManualUpdate.MerchantSettlement;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ManualUpdateTab;
import Test.AdminPanel.ReconAndBankFeeSettlement.ReconHelper;
import Test.AdminPanel.ReconAndBankFeeSettlement.ReconHelper.ReconType;
import Test.AdminPanel.Settlement.SettlementHelper.tidUtrResponseResult;
import Utils.Config;
import Utils.DataBase;
import Utils.Helper;
import Utils.TestBase;
import Utils.Helper.FileType;

public class TestSettlement extends TestBase {

	ManualUpdatePage manualTransactionUpdate;
	MerchantSettlement merchantSettlement;
	
	// TC-540
	@Test(description = "Settlement: Upload an invalid CitiBankNodal file and verify the same.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void invalidCitiBankNodalSettlement(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		String filePath = testConfig
				.getRunTimeProperty("CitiBankNodal_invalid");

		HomePage home = new HomePage(testConfig);

		ManualUpdatePage manualTransactionUpdate = home
				.ClickManualTransactionUpdate();
		merchantSettlement = manualTransactionUpdate.ClickMerchantSettlement();

		merchantSettlement.uploadInvalidCitiBankNodalFile(testConfig, filePath);

		Assert.assertTrue(testConfig.getTestResult());
	}

	//TC-541
	@Test(description = "Settlement: Upload an CitiBankNodal file with missing column and verify the same.", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void invalidCitiBankNodalSettlement_MissingColumn(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		String filePath = Helper.getExcelFile(testConfig, FileType.incompleteCitinodal);		
		HomePage home = new HomePage(testConfig);

		ManualUpdatePage manualTransactionUpdate = home.ClickManualTransactionUpdate();
		merchantSettlement = manualTransactionUpdate.ClickMerchantSettlement();
		manualTransactionUpdate = merchantSettlement.uploadInvalidCitiBankNodalFile1(testConfig, filePath);

		Assert.assertTrue(testConfig.getTestResult());	
	}
	
	//Complete Settlement Flow
	//TC-539
	@Test(description = "Settlement: Upload an CitiBankNodal file with correct details and settle it", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void CitiBankNodalSettlement_ForCapturedTransaction(Config testConfig)
	{
		
		ReconHelper recon = new ReconHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		int transactionRowNum = 7;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int reconRowNum[]=new int[1];
		String mihpayid[]=new String[1];
		reconRowNum[0]=1;
		 
		//Do a Transaction in captured state
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 60, 1);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		mihpayid[0]=map.get("id");
		
		//Recon the transaction
		String reconFile=recon.writeAdminReconFile(reconRowNum,mihpayid);
		String refId=recon.UploadAndCompareExcel(helper, reconFile, reconRowNum,mihpayid);
		
		
		SettlementHelper shelper =new SettlementHelper(testConfig);
		shelper.DoMerchantSettlement(testConfig, helper, refId,helper.testResponse);
		HomePage home = new HomePage(testConfig);
		String filePath = Helper.getExcelFile(testConfig, FileType.incompleteCitinodal);
		//Settle the recon output
		helper.home.navigateToAdminHome();
		ManualUpdatePage manualTransactionUpdate = home.ClickManualTransactionUpdate();
		merchantSettlement = manualTransactionUpdate.ClickMerchantSettlement();
		manualTransactionUpdate = merchantSettlement.uploadInvalidCitiBankNodalFile1(testConfig, filePath);
	}

	// TC-1532
	@Test(description = "Verifying Failure for 'TID UTR summary' due to mandatory column missing in' TID UTR' file", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void VerifyingFailureForTidUtrSummaryDueToMandatoryColumnMissingInTidUtrFile(
			Config testConfig) {

		SettlementHelper settlementhelper = new SettlementHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		String invalidFileUploadError = "CitiNodal TID UTR Summary Error: Mandatory"+
										" column missing. Mandatory columns are: narrative,"+
										" transaction description, customer reference, value"+
										" date, bank reference, amount";
		//Go To merchant settlement
		helper.DoLogin();
		helper.home.ClickManualTransactionUpdate();
		
		
		//Scenario 1 for Customer Reference
		settlementhelper.uploadTidUtrSummaryFileWithColumnMissingAndVerifyError("Customer Reference"
																,invalidFileUploadError);
		
		//Scenario 2 for Narrative
		settlementhelper.uploadTidUtrSummaryFileWithColumnMissingAndVerifyError("Narrative"
																		,invalidFileUploadError);
		//Scenario 3 for Transaction Description
		settlementhelper.uploadTidUtrSummaryFileWithColumnMissingAndVerifyError(
									"Transaction Description",invalidFileUploadError);
		
		//Scenario 4 for Value Date
		settlementhelper.uploadTidUtrSummaryFileWithColumnMissingAndVerifyError(
											"Value Date",invalidFileUploadError);
		
		//Scenario 5 for Bank Reference
		settlementhelper.uploadTidUtrSummaryFileWithColumnMissingAndVerifyError(
													"Bank Reference",invalidFileUploadError);
		
		//Scenario 6 for Amount
		settlementhelper.uploadTidUtrSummaryFileWithColumnMissingAndVerifyError(
															"Amount",invalidFileUploadError);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// TC-1531
	@Test(description = "Verifying Failure for TID Generation due to mandatory column missing in' citybank nodal summary' file", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void VerifyingFailureForTidGenerationDueToMandatoryColumnMissingIncitybankNodalSummaryFile(
			Config testConfig) {

		SettlementHelper settlementhelper = new SettlementHelper(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		String invalidFileUploadError = "TID Generation Error: Mandatory column"+
										" missing. Mandatory columns are: name, merchant_id,"+
										" preforma code, rtgs code, transaction amount, transaction"+
										" count, merchant amount, date, bene_ac_no, ifsc_code, "+
										"settlement_id, commission charge";
		//Go To merchant settlement
		helper.DoLogin();
		helper.home.ClickManualTransactionUpdate();
		
		
		//Scenario 1 for Settlement Id
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError("Settlement Id"
																,invalidFileUploadError);
		
		//Scenario 2 for Name
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError("Name"
																		,invalidFileUploadError);
		//Scenario 3 for Merchant Id
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError(
									"Merchant Id",invalidFileUploadError);
		
		//Scenario 4 for Performa Code
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError(
											"Performa Code",invalidFileUploadError);
		
		//Scenario 5 for RTGS Code
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError(
													"RTGS Code",invalidFileUploadError);
		
		//Scenario 6 for Transaction Amount
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError(
													"Transaction Amount",invalidFileUploadError);

		//Scenario 7 for Transaction Count
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError(
													"Transaction Count",invalidFileUploadError);
		
		//Scenario 8 for Merchant Amount
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError(
														"Merchant Amount",invalidFileUploadError);
		//Scenario 9 for Date
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError(
									"Date",invalidFileUploadError);
		
		//Scenario 10 for Bene Ac No
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError(
											"Bene Ac No",invalidFileUploadError);
		
		//Scenario 11 for IFSC Code
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError(
													"IFSC Code",invalidFileUploadError);
		
		//Scenario 12 for Commision Charge
		settlementhelper.uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError(
													"Commision Charge",invalidFileUploadError);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	// TC-1530
	@Test(description = "Verifying failure message for nodal settlement summary due to invalid Citibank Nodal ID", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void VerifyingFailureMessageForNodalSettlementSummaryDueToInvalidCitibankNodalId(
			Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		String strRandomNodalId = Helper.generateRandomAlphaNumericString(6);
		String invalidNodalIdError = "No citibank nodal settlement summary "
				+ "for this citibank nodal id: " + strRandomNodalId + "."
				+ " Please crosscheck citibank nodal id you submitted.";
		// Go To merchant settlement
		helper.DoLogin();
		MerchantSettlement merchantSettlement = (MerchantSettlement) helper
				.getManualUpdateTab(ManualUpdateTab.MerchantSettlement);

		// Enter Random value
		merchantSettlement.getUTRLevelSettlementSummary(testConfig,
				strRandomNodalId);
		// Verify Error Message
		Helper.compareExcelEquals(testConfig, "File upload Error Message",
				invalidNodalIdError,
				merchantSettlement.GetErrorMessageOnHeaderOfManualUpdate());

		Assert.assertTrue(testConfig.getTestResult());
	}

	//TC-1529 
	@Test(description = "Verifying failure on uploading citibanknodal xls file due to incorrect value in 'amount' column", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void VerifyingFailureOnUploadingCitibanknodalXlsFileDueToIncorrectValueInAmountColumn(
			Config testConfig) {
	TransactionHelper helper = new TransactionHelper(testConfig, true);
	SettlementHelper settlementHelper = new SettlementHelper(testConfig);
	ReconHelper reconHelper = new ReconHelper(testConfig);
	int transactionRowNum = 7;
	int paymentTypeRowNum =5;
	int cardDetailsRowNum =1;
	int reconRowNum= 1;
	int bankAndFeeSettlementRow = 21;
	helper.DoLogin();
	helper.GetTestTransactionPage();
	//Do transaction and recon for success transaction
	reconHelper.doTestTransactionAndRecon(helper, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, reconRowNum,ReconType.Success);	
	//go to merchant settlement and upload citibank nodal file
	helper.home.navigateToAdminHome();
	settlementHelper.settlement = (MerchantSettlement) helper
			.getManualUpdateTab(ManualUpdateTab.MerchantSettlement);
	settlementHelper.uploadCitibankNodalFileWithChangedValueAndVerifyResult("amount",bankAndFeeSettlementRow);
	Assert.assertTrue(testConfig.getTestResult());
	}
	
	//TC-1528 
		@Test(description = "Verifying failure on uploading citibanknodal xls file due to incorrect value in 'action' column", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void VerifyingFailureOnUploadingCitibanknodalXlsFileDueToIncorrectValueInActionColumn(
			Config testConfig) {
	TransactionHelper helper = new TransactionHelper(testConfig, true);
	SettlementHelper settlementHelper = new SettlementHelper(testConfig);
	ReconHelper reconHelper = new ReconHelper(testConfig);
	int transactionRowNum = 7;
	int paymentTypeRowNum =5;
	int cardDetailsRowNum =1;
	int reconRowNum= 1;
	int bankAndFeeSettlementRow = 22;
	helper.DoLogin();
	helper.GetTestTransactionPage();
	//Do transaction and recon for success transaction
	reconHelper.doTestTransactionAndRecon(helper, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, reconRowNum,ReconType.Success);	
	//go to merchant settlement and upload citibank nodal file
	helper.home.navigateToAdminHome();
	settlementHelper.settlement = (MerchantSettlement) helper
			.getManualUpdateTab(ManualUpdateTab.MerchantSettlement);
	settlementHelper.uploadCitibankNodalFileWithChangedValueAndVerifyResult("action",bankAndFeeSettlementRow);
	Assert.assertTrue(testConfig.getTestResult());
	}

	//TC-1527 
	@Test(description = "Verifying failure on uploading citibanknodal xls file due to invalid transaction id", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void VerifyingFailureOnUploadingCitibanknodalXlsFileDueToInvalidTransactionId(
			Config testConfig) {
	TransactionHelper helper = new TransactionHelper(testConfig, true);
	SettlementHelper settlementHelper = new SettlementHelper(testConfig);
	ReconHelper reconHelper = new ReconHelper(testConfig);
	int transactionRowNum = 7;
	int paymentTypeRowNum =5;
	int cardDetailsRowNum =1;
	int reconRowNum= 1;
	int bankAndFeeSettlementRow = 23;
	helper.DoLogin();
	helper.GetTestTransactionPage();
	
	//Do transaction and recon for success transaction
	reconHelper.doTestTransactionAndRecon(helper, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, reconRowNum,ReconType.Success);	
	//go to merchant settlement and upload citibank nodal file
	helper.home.navigateToAdminHome();
	settlementHelper.settlement = (MerchantSettlement) helper
			.getManualUpdateTab(ManualUpdateTab.MerchantSettlement);
	//upload and compare citibank nodal response
	settlementHelper.uploadCitibankNodalFileWithChangedValueAndVerifyResult("transactionid",bankAndFeeSettlementRow);
	Assert.assertTrue(testConfig.getTestResult());
	}

	//TC-1524 
	@Test(description = "Verifying merchant settlement after Recon success for chargeback", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void VerifyingMerchantSettlementAfterReconSuccessForChargeback(
			Config testConfig) {
	TransactionHelper helper = new TransactionHelper(testConfig, true);
	SettlementHelper settlementHelper = new SettlementHelper(testConfig);
	ReconHelper reconHelper = new ReconHelper(testConfig);
	int transactionRowNum = 162;
	int paymentTypeRowNum =381;
	int cardDetailsRowNum =1;
	int reconRowNum= 30;
	
	helper.DoLogin();
	helper.GetTestTransactionPage();
	
	//Do transaction and recon for success transaction
	reconHelper.doTestTransactionAndRecon(helper, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, reconRowNum,ReconType.Chargeback);	
	//go to merchant settlement and upload citibank nodal file
	helper.home.navigateToAdminHome();
	settlementHelper.settlement = (MerchantSettlement) helper
			.getManualUpdateTab(ManualUpdateTab.MerchantSettlement);
	//Section 6 Steps 16-19
	//upload and compare citibank nodal response
	String settlementId = settlementHelper.uploadCitibankNodalFileAndVerifyResult();
	//Section Steps 20 to 22
	//Get merchant settlement summary file
	settlementHelper.settlement.getCitiBankNodalSummary(testConfig, settlementId);
	//Section 8 Steps 23 to 25
	//Upload citibank nodal summary
	String tidValue = settlementHelper.uploadCitibakNodalSummaryForTidGeneration();
	//Section 9
	//Write generated tid id utr file 
	settlementHelper.uploadTidUtrFileWithTid(tidValue,tidUtrResponseResult.Pass);
	
	Assert.assertTrue(testConfig.getTestResult());
	}
	
	//TC-1525 Test Case failing for Settlement amount verification
	@Test(description = "Verifying merchant settlement after Recon success for chargeback reversal", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void VerifyingMerchantSettlementAfterReconSuccessForChargebackReversal(
			Config testConfig) {
	TransactionHelper helper = new TransactionHelper(testConfig, true);
	SettlementHelper settlementHelper = new SettlementHelper(testConfig);
	ReconHelper reconHelper = new ReconHelper(testConfig);
	int transactionRowNum = 162;
	int paymentTypeRowNum =381;
	int cardDetailsRowNum =1;
	int reconRowNum= 29;
	
	helper.DoLogin();
	helper.GetTestTransactionPage();
	
	//Do transaction and recon for success transaction
	reconHelper.doTestTransactionAndRecon(helper, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, reconRowNum,ReconType.ChargebackReversal);	
	//go to merchant settlement and upload citibank nodal file
	helper.home.navigateToAdminHome();
	settlementHelper.settlement = (MerchantSettlement) helper
			.getManualUpdateTab(ManualUpdateTab.MerchantSettlement);
	//Section 6 Steps 16-19
	//upload and compare citibank nodal response
	String settlementId = settlementHelper.uploadCitibankNodalFileAndVerifyResult();
	//Section Steps 20 to 22
	//Get merchant settlement summary file
	settlementHelper.settlement.getCitiBankNodalSummary(testConfig, settlementId);
	//Section 8 Steps 23 to 25
	//Upload citibank nodal summary
	String tidValue = settlementHelper.uploadCitibakNodalSummaryForTidGeneration();
	//Section 9
	//Write generated tid id utr file 
	settlementHelper.uploadTidUtrFileWithTid(tidValue,tidUtrResponseResult.Pass);
	
	Assert.assertTrue(testConfig.getTestResult());
	}

	//TC-1524  
	@Test(description = "Verifying merchant settlement after Recon success for refunded transaction", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void VerifyingMerchantSettlementAfterReconSuccessForRefundedTransaction(
			Config testConfig) {
	TransactionHelper helper = new TransactionHelper(testConfig, true);
	SettlementHelper settlementHelper = new SettlementHelper(testConfig);
	ReconHelper reconHelper = new ReconHelper(testConfig);
	int transactionRowNum = 162;
	int paymentTypeRowNum =381;
	int cardDetailsRowNum =1;
	int reconRowNum= 31;
	
	helper.DoLogin();
	helper.GetTestTransactionPage();
	
	//Do transaction and recon for success transaction
	reconHelper.doTestTransactionAndRecon(helper, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, reconRowNum,ReconType.Refund);	
	//go to merchant settlement and upload citibank nodal file
	helper.home.navigateToAdminHome();
	settlementHelper.settlement = (MerchantSettlement) helper
			.getManualUpdateTab(ManualUpdateTab.MerchantSettlement);
	//Section 6 Steps 16-19
	//upload and compare citibank nodal response
	String settlementId = settlementHelper.uploadCitibankNodalFileAndVerifyResult();
	//Section Steps 20 to 22
	//Get merchant settlement summary file
	settlementHelper.settlement.getCitiBankNodalSummary(testConfig, settlementId);
	//Section 8 Steps 23 to 25
	//Upload citibank nodal summary
	String tidValue = settlementHelper.uploadCitibakNodalSummaryForTidGeneration();
	//Section 9
	//Write generated tid id utr file 
	settlementHelper.uploadTidUtrFileWithTid(tidValue,tidUtrResponseResult.Pass);
	
	Assert.assertTrue(testConfig.getTestResult());
	}

	//TC-1523  
	@Test(description = "Verifying merchant settlement after Recon success for refunded reversal", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void VerifyingMerchantSettlementAfterReconSuccessForRefundedReversalTransaction(
			Config testConfig) {
	TransactionHelper helper = new TransactionHelper(testConfig, true);
	SettlementHelper settlementHelper = new SettlementHelper(testConfig);
	ReconHelper reconHelper = new ReconHelper(testConfig);
	int transactionRowNum = 162;
	int paymentTypeRowNum =381;
	int cardDetailsRowNum =1;
	int reconRowNum= 24;
	
	helper.DoLogin();
	helper.GetTestTransactionPage();
	
	//Do transaction and recon for success transaction
	reconHelper.doTestTransactionAndRecon(helper, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, reconRowNum,ReconType.RefundReversal);	
	//go to merchant settlement and upload citibank nodal file
	helper.home.navigateToAdminHome();
	settlementHelper.settlement = (MerchantSettlement) helper
			.getManualUpdateTab(ManualUpdateTab.MerchantSettlement);
	//Section 6 Steps 16-19
	//upload and compare citibank nodal response
	String settlementId = settlementHelper.uploadCitibankNodalFileAndVerifyResult();
	//Section Steps 20 to 22
	//Get merchant settlement summary file
	settlementHelper.settlement.getCitiBankNodalSummary(testConfig, settlementId);
	//Section 8 Steps 23 to 25
	//Upload citibank nodal summary
	String tidValue = settlementHelper.uploadCitibakNodalSummaryForTidGeneration();
	//Section 9
	//Write generated tid id utr file 
	settlementHelper.uploadTidUtrFileWithTid(tidValue,tidUtrResponseResult.Pass);
	
	Assert.assertTrue(testConfig.getTestResult());
	}
	
	//TC-1527  
	@Test(description = "Verifying failure on uploading successfully uploaded TID_UTR summary file second time", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void VerifyingFailureOnUploadingSuccessfullyUploadedTid_utrSummaryFileSecondTime(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		SettlementHelper settlementHelper = new SettlementHelper(testConfig);
		ReconHelper reconHelper = new ReconHelper(testConfig);
		int transactionRowNum = 162;
		int paymentTypeRowNum =381;
		int cardDetailsRowNum =1;
		int reconRowNum= 24;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		//Do transaction and recon for success transaction
		reconHelper.doTestTransactionAndRecon(helper, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, reconRowNum,ReconType.RefundReversal);	
		//go to merchant settlement and upload citibank nodal file
		helper.home.navigateToAdminHome();
		settlementHelper.settlement = (MerchantSettlement) helper
				.getManualUpdateTab(ManualUpdateTab.MerchantSettlement);
		//Section 6 Steps 16-19
		//upload and compare citibank nodal response
		String settlementId = settlementHelper.uploadCitibankNodalFileAndVerifyResult();
		//Section Steps 20 to 22
		//Get merchant settlement summary file
		settlementHelper.settlement.getCitiBankNodalSummary(testConfig, settlementId);
		//Section 8 Steps 23 to 25
		//Upload citibank nodal summary
		String tidValue = settlementHelper.uploadCitibakNodalSummaryForTidGeneration();
		//Section 9
		//Write generated tid id utr file 
		settlementHelper.uploadTidUtrFileWithTid(tidValue,tidUtrResponseResult.Pass);
		//Repeat :upload generated tid utr file with same tid and verify respone
		settlementHelper.uploadTidUtrFileWithTid(tidValue,tidUtrResponseResult.Fail);
		Assert.assertTrue(testConfig.getTestResult());	
		}

}
