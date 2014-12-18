package Test.AdminPanel.Settlement;


import java.io.File;
import java.util.Map;

import org.openqa.selenium.WebElement;

import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.ManualUpdate.MerchantSettlement;
import PageObject.AdminPanel.ManualUpdate.ReconPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.ReconAndBankFeeSettlement.ReconHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.FileHandler;
import Utils.Helper;
import Utils.Helper.FileType;
import Utils.TestDataReader;


public class SettlementHelper {
	public Config testConfig;
	ManualUpdatePage manualUpdatePage;
	ReconPage reconPage;
	MerchantTransactionsPage merchantTransactionsPage;
	TestDataReader bankfeesettlementdata;
	MerchantSettlement settlement;
	
	public SettlementHelper(Config testConfig) {
		this.testConfig = testConfig;
	}
	
	public void DoMerchantSettlement(Config testConfig,TransactionHelper thelper,String ReconReferenceId, TestResponsePage testResponse){
		
		// Navigate to Admin Panel Settlement  Option				
		manualUpdatePage=new ManualUpdatePage(testConfig);
		String SettlementInputFileName;
		
		//Upload the Recon output in Settlement "Upload Settlement Citibank Nodal File option"
		String downloadedfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.DesiredFileDownload(testConfig, downloadedfilepath, ReconReferenceId);
		SettlementInputFileName=downloadedfilepath+file.getName();
		String SettlementXLSFileName="XLS"+file.getName();
		
		//upload citibank nodal response
		FileHandler.saveXMLAsExcel(testConfig, SettlementInputFileName, downloadedfilepath+SettlementXLSFileName);
		settlement=manualUpdatePage.ClickMerchantSettlement();
		settlement.UploadCitiNoadalFile(testConfig, downloadedfilepath+SettlementXLSFileName);
		
		//Get Citibank Nodal Settlement Summary
		
		File SettlementOutputFile = Browser.lastFileModifiedWithDesiredName(testConfig, downloadedfilepath, "citibanknodalresponse");
		System.out.println(SettlementOutputFile.getName());
		
		TestDataReader settlementOutputDataReader =new TestDataReader(testConfig, "PayU",downloadedfilepath+SettlementOutputFile.getName());
		String SettlementId=settlementOutputDataReader.GetData(1, "settlement_id");
		settlement.getCitiBankNodalSummary(testConfig, SettlementId);
		
	
		//Upload Settlement Summary for TID generation 		
		File InputSummaryForTIDGeneration = Browser.lastFileModifiedWithDesiredName(testConfig, downloadedfilepath, "merchant_settlement_summary");
		System.out.println(InputSummaryForTIDGeneration.getName());
		String CitiBankSummaryForTDR=InputSummaryForTIDGeneration.getName();
		String XLSCitiBankSummaryForTDR="XLS"+InputSummaryForTIDGeneration.getName();
		FileHandler.saveXMLAsExcel(testConfig, downloadedfilepath+CitiBankSummaryForTDR, downloadedfilepath+XLSCitiBankSummaryForTDR);
		
		settlement.settleCitiBankNodalFileForTIDGeneration(testConfig, downloadedfilepath+"XLS"+CitiBankSummaryForTDR);
		
		//Read TID from the output
		File TIDOutputFile = Browser.lastFileModifiedWithDesiredName(testConfig, downloadedfilepath, "citibanknodaltidresponse");	
		TestDataReader TIDOutputReader =new TestDataReader(testConfig, "PayU",downloadedfilepath+TIDOutputFile.getName());
		String TDRId=TIDOutputReader.GetData(1, "tid");

		//Get UTR Level Citibank Nodal Summary
		settlement.getUTRLevelSettlementSummary(testConfig, SettlementId);
		//verifyTransactionSettlementDatainDB(testConfig, SettlementId,TDRId,testResponse);

		/*
		//File UTRWiseMerchantSettlement = Browser.lastFileModifiedWithDesiredName(testConfig, downloadedfilepath, "utr_wise_merchant_settlement_summary");
		//Upload UTR Summary
		String InputTIDUTRSummary=UTRWiseMerchantSettlement.getName();
		FileHandler.saveXMLAsExcel(testConfig,downloadedfilepath+InputTIDUTRSummary , downloadedfilepath+"XLS"+InputTIDUTRSummary);
		*/
	}

	public void verifyTransactionSettlementDatainDB(Config testConfig, String SettlementId, String TDRID, TestResponsePage testResponse){
		testConfig.putRunTimeProperty("settlementId", SettlementId);
		
		//verify if transaction entry exists in transaction_Settlement
		testConfig.putRunTimeProperty("mihpayid",testResponse.actualResponse.get("mihpayid") );
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 4, 1);
		testConfig.putRunTimeProperty("txn_update_id",map.get("id"));
		Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 5, 1);
		if (map1.get("txn_update_id").contains(map.get("id")))
		testConfig.logComment("Entry of "+map.get("id")+" exists in transaction_settlement");
		else{
			testConfig.logFail("Entry of "+map.get("id")+" does not exist in transaction_settlement");
			return;
		}
		
		//verify the entry in merchant_settlement
		String actualResponse="",expectedValue="";
		testConfig.putRunTimeProperty("settlementid",SettlementId);
		Map<String,String> map2 = DataBase.executeSelectQuery(testConfig, 103, 1);
		
		String [] ParametersToBeCompared = {"id","merchan_id","amount"};
		for(String expectedKey:ParametersToBeCompared){
				
				switch(expectedKey)
				{
				case "id":
					expectedValue=TDRID;
					actualResponse=map2.get(expectedKey);
					break;
				case "merchant_id":
					expectedValue=testConfig.getRunTimeProperty("merchantid");
					actualResponse=map2.get(expectedKey);
					break;
				case "amount":
					expectedValue=testResponse.actualResponse.get("amount");
					actualResponse=map2.get(expectedKey);
					break;
				case "settlement_id":
					expectedValue=SettlementId;
					actualResponse=map.get(expectedKey);
					break;
				case "merchant_settlement_id":
					expectedValue=TDRID;
					actualResponse=map.get(expectedKey);
					break;
				case "mer_settled":
					expectedValue="1";
					actualResponse=map.get(expectedKey);
					break;
					
					default:
						expectedValue="{skip}";
						break;
				}
				
				if(expectedValue.equalsIgnoreCase("{skip}")) 
				{
					testConfig.logWarning("Skipping the verification of Response Param '" + expectedKey + "' as '" + actualResponse+"'");
					continue;
				}
				else
				{
					Utils.Helper.compareEquals(testConfig, expectedKey, expectedValue, actualResponse);
				}		
		}
	}
	
	/**
	 * Creates a tid utr summmary file with a column missing as specified in parameter
	 * @param MissingcolumnName- column name that has to be deleted
	 * @return file path
	 */
	private String createTidUtrSummaryWithColumnMissing(String MissingcolumnName){
		String TidUtrSummaryFileName =Helper.getExcelFile(testConfig,
				FileType.TIDUTR);
		testConfig.logComment("Adding empty column in TID UTR Summary for "
				+ MissingcolumnName);
		switch (MissingcolumnName) {
			case "Amount" :
				FileHandler.setCellBlank(TidUtrSummaryFileName, "SHeet1", 0, 9);
				break;
			case "Bank Reference" :
				FileHandler.setCellBlank(TidUtrSummaryFileName, "SHeet1", 0, 10);
				break;
			case "Customer Reference" :
				FileHandler.setCellBlank(TidUtrSummaryFileName, "SHeet1", 0, 11);
				break;
			case "Narrative" :
				FileHandler.setCellBlank(TidUtrSummaryFileName, "SHeet1", 0, 12);
				break;
			case "Transaction Description" :
				FileHandler.setCellBlank(TidUtrSummaryFileName, "SHeet1", 0, 13);
				break;
			case "Value Date" :
				FileHandler.setCellBlank(TidUtrSummaryFileName, "SHeet1", 0, 18);
				break;		
			default :
				testConfig.logFail("Coulumn not defined for " + MissingcolumnName);
				break;
		}
		return TidUtrSummaryFileName;
	}

	/**Uploads TID UTR summary file with specified columnn
	 *  missing and verifies error message
	 * @param MissingcolumnName Name of the column that should be removed
	 * @param ErrorMessage- error message that has to be verified on uploading file
	 */
	public void uploadTidUtrSummaryFileWithColumnMissingAndVerifyError(
			String MissingcolumnName, String ErrorMessage) {
		ManualUpdatePage manualUpdatePage =  new ManualUpdatePage(testConfig);
		MerchantSettlement merchantSettlement = manualUpdatePage.ClickMerchantSettlement();
		String filePath = createTidUtrSummaryWithColumnMissing(MissingcolumnName);
		merchantSettlement.uploadTIDUTRFIle(testConfig, filePath);
		Helper.compareExcelEquals(testConfig, "File upload Error Message",
				ErrorMessage, merchantSettlement.GetErrorMessageOnHeaderOfManualUpdate());
	}
	
	/**Uploads Citibank Nodal summary file with specified columnn
	 *  missing and verifies error message
	 * @param MissingcolumnName Name of the column that should be removed
	 * @param ErrorMessage- error message that has to be verified on uploading file
	 */
	public void uploadCitybankNodalSummaryFileWithColumnMissingAndVerifyError(
			String MissingcolumnName, String ErrorMessage) {
		ManualUpdatePage manualUpdatePage =  new ManualUpdatePage(testConfig);
		MerchantSettlement merchantSettlement = manualUpdatePage.ClickMerchantSettlement();
		String filePath = createCitiBankNodalSummaryWithColumnMissing(MissingcolumnName);
		merchantSettlement.settleCitiBankNodalFileForTIDGeneration(testConfig, filePath);
		Helper.compareExcelEquals(testConfig, "File upload Error Message",
				ErrorMessage, merchantSettlement.GetErrorMessageOnHeaderOfManualUpdate());
	}

	/**Creates citibank nodal summary file with specified column missing
	 * @param MissingcolumnName
	 * @return file path
	 */
	private String createCitiBankNodalSummaryWithColumnMissing(
			String MissingcolumnName) {
		String citibankNodalSummaryFileName =Helper.getExcelFile(testConfig,
				FileType.merchantSummary);
		testConfig.logComment("Adding empty column in Merchant summary file for "
				+ MissingcolumnName);
		switch (MissingcolumnName) {
		case "Name" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 0);
			break;
		case "Merchant Id" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 1);
			break;
		case "Performa Code" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 2);
			break;
		case "RTGS Code" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 3);
			break;
		case "Transaction Amount" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 4);
			break;
		case "Transaction Count" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 5);
			break;
		case "Merchant Amount" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 6);
			break;
		case "Date" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 7);
			break;
		case "Bene Ac No" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 8);
			break;
		case "IFSC Code" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 9);
			break;
		case "Settlement Id" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 10);
			break;
		case "Commision Charge" :
			FileHandler.setCellBlank(citibankNodalSummaryFileName, "SHeet1", 0, 11);
			break;
		default :
			testConfig.logFail("Coulumn not defined for " + MissingcolumnName);
			break;
		}
		return citibankNodalSummaryFileName;

	}
	
	/**Uploads citibank nodal file replacing specified with random values and then verifies result 
	 * @param ValueToBeChanged- name of the column whose value needs to be changed in citibank nodal file
	 * @param ResultRow of Result
	 */
	public void uploadCitibankNodalFileWithChangedValueAndVerifyResult(String ValueToBeChanged,int ResultRow) {
		ReconHelper reconHelper =  new ReconHelper(testConfig);
		//edit file for specified column
		String strTargetpath = reconHelper.editDownloadedReconFileFor(ValueToBeChanged);
		//upload file
		settlement.uploadCitiBankNodalFile(testConfig, strTargetpath);
		//get latest downloaded citinodal response and compare result
		String downloadedfilepath = findDownloadedSettlementFile(SettlementFileType.CitibanknodalResponse);
		//Compare Results
		TestDataReader downloadedfile = new TestDataReader(testConfig,"Sheet1", downloadedfilepath);
		TestDataReader DataFile =  new TestDataReader(testConfig, "BankFeeSettlement");
		Helper.compareEquals(testConfig, "Result from Citibank Nodal", DataFile.GetData(ResultRow, "failureReason"),downloadedfile.GetData(1, "result"));
	}

	/**Uploads citibak nodal file and verifies success result
	 * @return settlement id generated
	 */
	public String uploadCitibankNodalFileAndVerifyResult() {
		ReconHelper reconHelper =  new ReconHelper(testConfig);
		//Find recon file
		String downloadedReconFile =reconHelper.getLastDownloadedReconFileAndConvertToExcel();
		settlement.uploadCitiBankNodalFile(testConfig, downloadedReconFile);
		String downloadedCitibank = findDownloadedSettlementFile(SettlementFileType.CitibanknodalResponse);
		//Compare Results
		//making a test commit
		TestDataReader downloadedfile = new TestDataReader(testConfig,"Sheet1", downloadedCitibank);
		TestDataReader DataFile =  new TestDataReader(testConfig, "BankFeeSettlement");
		Helper.compareEquals(testConfig, "Result from Citibank Nodal", DataFile.GetData(24, "successVerify"),downloadedfile.GetData(1, "result"));
		String actual_settlementid = downloadedfile.GetData(1, "settlement_id");
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 116, 1);
		Helper.compareEquals(testConfig, "Settlement id", map.get("settlement_id"), actual_settlementid);
		return actual_settlementid;
	}

	
	/**Uploads citibank nodal summary file for tid generation and then verifies result
	 * @return merchant settlement id generated
	 */
	public String uploadCitibakNodalSummaryForTidGeneration() {
		
		String downloadedmerchantSettlementSummary=findDownloadedSettlementFile(SettlementFileType.CitibankNodalSummary);
		String strTargetPath = FileHandler.saveXMLAsExcel(testConfig, downloadedmerchantSettlementSummary,"");
		settlement.settleCitiBankNodalFileForTIDGeneration(testConfig, strTargetPath);
		String actual_Merchant_settlement_id=null;
		String actual_settlement_id=null;
		for(int tries=0;tries<3;tries++){
			if(actual_Merchant_settlement_id!=null && actual_Merchant_settlement_id!="")
				break;
			Map<String,String> map = DataBase.executeSelectQuery(testConfig, 116, 1);
			actual_Merchant_settlement_id =map.get("merchant_settlement_id");
			actual_settlement_id= map.get("settlement_id");
			String currenturl= testConfig.driver.getCurrentUrl();
			Browser.navigateToURL(testConfig, testConfig.getRunTimeProperty("CronUrl"));
			WebElement reconCron = Element.getPageElement(testConfig, How.linkText, "updateTxnWiseTid.php");
			Element.click(testConfig, reconCron, "Update TXN Wise TID");
			Browser.wait(testConfig, 15);
			Browser.navigateToURL(testConfig, currenturl);
			Element.getPageElement(testConfig,How.linkText, "Merchant Settlement").click();
			
		}
		//Find downloaded file and verify values
		String downloadedCitibankNodalTidResponse = findDownloadedSettlementFile(SettlementFileType.CitibankNodalTidResponse);
		TestDataReader downloadedfile = new TestDataReader(testConfig,"PayU", downloadedCitibankNodalTidResponse);
		TestDataReader DataFile =  new TestDataReader(testConfig, "BankFeeSettlement");
		String expected_merchantTid = downloadedfile.GetData(1, "tid");
		String expected_settlementid=downloadedfile.GetData(1, "settlement_id");
		String expected_result =DataFile.GetData(25, "successVerify");
		Helper.compareEquals(testConfig, "Settlement id", expected_settlementid, actual_settlement_id);
		Helper.compareEquals(testConfig, "Merchant Settlement id", expected_merchantTid, actual_Merchant_settlement_id);
		Helper.compareEquals(testConfig, "Result", expected_result,downloadedfile.GetData(1, "result"));
		
		return actual_Merchant_settlement_id;
	}
	
	enum SettlementFileType{CitibankNodalSummary,CitibankNodal,CitibankNodalTidResponse,CitibankNodalUtrResponse,CitibanknodalResponse };
	
	/**
	 * Finds downloaded file from downloads folder with mentioned keyword
	 * If keyword not mentioned then returns last modified file
	 * @param fileType Type of settlement file
	 * @return file path
	 */
	public String findDownloadedSettlementFile(SettlementFileType fileType){
		String downloadedFile= System.getProperty("user.home") + "//Downloads//";
		String keyWord="";
		switch (fileType) {
		case CitibankNodalSummary:
			keyWord="merchant_settlement_summary";
			break;
		case CitibankNodal :
			keyWord="citibanknodalresponse";
			break;
		case CitibankNodalTidResponse :
			keyWord="citibanknodaltidresponse";
			break;
		case CitibankNodalUtrResponse :
			keyWord="citibanknodalutrresponse";
			break;
		case CitibanknodalResponse:
			keyWord="citibanknodalresponse";
		default:
			break;
		}
		File file = Browser.lastFileModifiedWithDesiredName(testConfig, downloadedFile, keyWord);
		String fileName=file.getName();
		downloadedFile=downloadedFile+fileName;
		testConfig.logComment("Found file " + downloadedFile);
		return downloadedFile;
	}
	
	public enum tidUtrResponseResult{Fail, Pass};
	
	/**Uploads Tid Utr file on merchant settlements tab with specified tid and current date
	 * @param tidValue
	 * @param result
	 */
	public void uploadTidUtrFileWithTid(String tidValue,tidUtrResponseResult result) {
		
		String tidUtrSummaryFile =Helper.getExcelFile(testConfig,
				FileType.TIDUTR);
		FileHandler.write_edit(tidUtrSummaryFile, "Sheet1", 1, 11, tidValue);
		FileHandler.write_edit(tidUtrSummaryFile, "Sheet1", 1, 18, Helper.getCurrentDate("yyyy-MM-dd"));
		String utr_value= Helper.generateRandomAlphabetsString(5);
		FileHandler.write_edit(tidUtrSummaryFile, "Sheet1", 1, 12,"UTR "+ utr_value);
		settlement.uploadTIDUTRFIle(testConfig, tidUtrSummaryFile);
		
		//Execute cron 
		String currenturl= testConfig.driver.getCurrentUrl();
		Browser.navigateToURL(testConfig, testConfig.getRunTimeProperty("CronUrl"));
		WebElement reconCron = Element.getPageElement(testConfig, How.linkText, "updateTxnWiseUtr.php");
		Element.click(testConfig, reconCron, "Update TXN Wise Utr");
		Browser.wait(testConfig, 15);
		Browser.navigateToURL(testConfig, currenturl);
		
		String citibankNodalUtrResponse=findDownloadedSettlementFile(SettlementFileType.CitibankNodalUtrResponse);
		TestDataReader downloadedfile = new TestDataReader(testConfig,"PayU", citibankNodalUtrResponse);
		TestDataReader DataFile =  new TestDataReader(testConfig, "BankFeeSettlement");
		String actual_result = downloadedfile.GetData(1, "result");
		String actual_utr = downloadedfile.GetData(1, "utr number");
		if(result==tidUtrResponseResult.Pass){
			//if result is pass go ahead and make database verifications
			String expected_result =DataFile.GetData(26, "successVerify");
			Helper.compareEquals(testConfig, "Utr value from citibank TID UTR summary", utr_value, actual_utr);
			Helper.compareEquals(testConfig, "Result from citibank TID UTR summary", expected_result, actual_result);
			Map<String,String> map = DataBase.executeSelectQuery(testConfig, 116, 1);
			Helper.compareEquals(testConfig, "UTR id from database", utr_value, map.get("mer_utr"));
			testConfig.putRunTimeProperty("id", downloadedfile.GetData(1, "TID"));
			Map<String,String> map2 = DataBase.executeSelectQuery(testConfig, 92, 1);
			Helper.compareEquals(testConfig, "UTR value from merchant settlement", utr_value, map2.get("utr"));
			Helper.compareEquals(testConfig, "Value date from merchant settlement", Helper.getCurrentDate("yyyy-MM-dd"), map2.get("value_date"));
			Helper.compareEquals(testConfig, "UTR value from merchant settlement", "1", map2.get("txns_tid_updated"));
		}
		else {
			String expected_result =DataFile.GetData(26, "failureReason");
			Helper.compareEquals(testConfig, "Result from citibank TID UTR summary", expected_result, actual_result);
		}
	}

}
