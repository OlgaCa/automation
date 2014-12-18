package Test.AdminPanel.ReconAndBankFeeSettlement;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import PageObject.AdminPanel.ManualUpdate.ChargebackTab;
import PageObject.AdminPanel.ManualUpdate.ChargebackTab.ChargebackFileType;
import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.ManualUpdate.ReconPage;
import PageObject.AdminPanel.ManualUpdate.RefundTab;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ManualUpdateTab;
import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import Test.NewMerchantPanel.Transactions.TransactionsHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.FileHandler;
import Utils.Helper;
import Utils.Helper.FileType;
import Utils.TestDataReader;

public class ReconHelper {

	public Config testConfig;
	ManualUpdatePage manualUpdatePage;
	ReconPage reconPage;
	MerchantTransactionsPage merchantTransactionsPage;
	TestDataReader bankfeesettlementdata;

	public Hashtable<String, String> actualReconData;
	
	public ReconHelper(Config testConfig)
	{
		this.testConfig = testConfig;
		
	}
	
	private String [] ReconOutputFields = {"transactionid","action","status","amount","servicefee","servicetax","net","utr","refnum","category","cardtype","requestid",
			"reconciliationid","PayUMID","pgMID","merchantName","transactionDate","refundDate","pay u charge",	"service tax",	"settlement amount",
			"commission charge","result","prev_status"};
	
	/**
	 * @param testConfig - Object of Config
	 * @param refId - Reference id of the reconned transaction
	 * @param reconRowNum - Row Number of ReconOutputFile Sheet
	 * @param reconOutputRow - Row Number of downloaded Sheet
	 * @param mihpayid - PayU id of the transaction reconned
	 * 
	 */
	private void readAndCompareReconOutput(Config testConfig,String refId,int reconRowNum,int reconOutputRow,String mihpayid) {
		
		
		//get last modified file from downloads with reference id in it 
		String downloadedfilepath = "C://Users//"+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.DesiredFileDownload(testConfig, downloadedfilepath, refId);
		String fileName=file.getName();
		downloadedfilepath=downloadedfilepath+fileName;
		
		Browser.wait(testConfig, 1);
		TestDataReader downloadedfile = new TestDataReader(testConfig,"Sheet1", downloadedfilepath);
		TestDataReader reconSheet = new TestDataReader(testConfig, "ReconFileOutput");	
		
		Browser.wait(testConfig, 1);
		String actualDataOutput="";
		String expectedDataOutput = null;
		double serviceTax=0,serviceFee = 0,amount=0,commission=0,net=0;
		double payucharge=0,payuServiceTax=0,payuSettlementAmount=0;
		for(String FieldName:ReconOutputFields)
		{
	
			// Get Actual data in Downloaded recon Output
			switch (FieldName){
			case "amount": 
				amount=Double.parseDouble(downloadedfile.GetData(reconOutputRow,FieldName));
				actualDataOutput=String.valueOf(amount);
				break;
			case "net":
				net=Double.parseDouble(downloadedfile.GetData(reconOutputRow,FieldName));
				actualDataOutput=String.valueOf(net);
				break;
			case "servicefee":
				serviceFee=Double.parseDouble(downloadedfile.GetData(reconOutputRow,FieldName));
				actualDataOutput=String.valueOf(serviceFee);
				break;
			case "servicetax":
				serviceTax=Double.parseDouble(downloadedfile.GetData(reconOutputRow,FieldName));
				actualDataOutput=String.valueOf(serviceTax);
				break;				
			case "pay u charge":
				if (downloadedfile.GetData(reconOutputRow,FieldName)!="{skip}")
				{
				payucharge=Double.parseDouble(downloadedfile.GetData(reconOutputRow,FieldName));
				actualDataOutput=String.valueOf(payucharge);
				}
				
				break;
			case "service tax":
				if (downloadedfile.GetData(reconOutputRow,FieldName)!="{skip}")
				{
				payuServiceTax=Double.parseDouble(downloadedfile.GetData(reconOutputRow,FieldName));
				actualDataOutput=String.valueOf(payuServiceTax);
				}
				break;
			case "settlement amount":
				if (downloadedfile.GetData(reconOutputRow,FieldName)!="{skip}")
				{
				payuSettlementAmount=Double.parseDouble(downloadedfile.GetData(reconOutputRow,FieldName));
				actualDataOutput=String.valueOf(payuSettlementAmount);
				}
				break;
			case "commission charge":
				if (downloadedfile.GetData(reconOutputRow,"settlement amount")!="{skip}")
				commission=Double.parseDouble(downloadedfile.GetData(reconOutputRow,FieldName));
				actualDataOutput=String.valueOf(commission);
				break;
			default:
				actualDataOutput=downloadedfile.GetData(reconOutputRow,FieldName);
				break;
			}
			if(actualDataOutput.equalsIgnoreCase("{skip}"))	
				actualDataOutput="";
			
		//Get Expected data from recon Sheet
		switch (FieldName)
		{
		case "utr": 
			expectedDataOutput= testConfig.getRunTimeProperty("UTR");
			break;
		case "refnum": 
			expectedDataOutput= testConfig.getRunTimeProperty("refNo");
			break;	
		case "transactionid":
			expectedDataOutput=mihpayid;
			break;
		//case "transactionDate":
			//expectedDataOutput=testResponse.actualResponse.get("addedon");
			//break;
		case "amount":
			expectedDataOutput=reconSheet.GetData(reconRowNum, FieldName);
			if(testConfig.getRunTimeProperty("amount") != null)
				expectedDataOutput =testConfig.getRunTimeProperty("amount");
			amount=Double.parseDouble(expectedDataOutput);
			//amount=1.70;
			break;
		case "servicefee":
			expectedDataOutput=reconSheet.GetData(reconRowNum, FieldName);
			serviceFee=Double.parseDouble(expectedDataOutput);
			break;
		case "servicetax":
			expectedDataOutput=reconSheet.GetData(reconRowNum, FieldName);
			serviceTax=Double.parseDouble(expectedDataOutput);
			break;
		case "net":
			net=amount-serviceFee-serviceTax;
			if (reconSheet.GetData(reconRowNum, "action").equals("refund")||
					reconSheet.GetData(reconRowNum, "action").equals("chargeback")){
				net =-(net);
			}
			expectedDataOutput=String.valueOf(net);
			break;
		case "pay u charge":
			if (reconSheet.GetData(reconRowNum,FieldName)!="{skip}")
			{
			expectedDataOutput=reconSheet.GetData(reconRowNum, FieldName);
			payucharge=Double.parseDouble(expectedDataOutput);
			}
			break;
		case "service tax":
			if (reconSheet.GetData(reconRowNum,FieldName)!="{skip}")
			{expectedDataOutput=reconSheet.GetData(reconRowNum, FieldName);
			payuServiceTax=Double.parseDouble(expectedDataOutput);
			}
			break;
		case "settlement amount" :
			if (testConfig.getRunTimeProperty("check_merchant_amount") == null) {
				if (reconSheet.GetData(reconRowNum, "service tax") != "{skip}") {
					payuSettlementAmount = net - payuServiceTax- payucharge;
					expectedDataOutput = String.valueOf(payuSettlementAmount);
				}
			}
				break;
			case "commission charge" :
				expectedDataOutput = "{skip}";
				if (testConfig.getRunTimeProperty("check_merchant_amount") == null) {
					if (reconSheet.GetData(reconRowNum, "service tax") != "{skip}") {
						commission = net - payuSettlementAmount;
					} else
						commission = 0;
					expectedDataOutput = String.valueOf(commission);
				}
				break;
		case "refundDate":
		case "action":
		case "status":
		case "category":
		case "cardtype":
		case "requestid":
		case "PayUMID":
		case "pgMID":
		case "result":
		case "prev_status":
			expectedDataOutput=reconSheet.GetData(reconRowNum, FieldName);
			break;
		case "merchantName":
		case "transactionDate":
			expectedDataOutput = "{skip}";
			break;
		case "reconciliationid":
			expectedDataOutput = "{skip}";
		default:	
			expectedDataOutput = "{skip}";
			break;
		}
		
		
		//Comparing Actual and Expected Results
		if(expectedDataOutput == null)
		{
			Helper.compareEquals(testConfig, "Recon Output Field '" + FieldName + "'", expectedDataOutput, actualDataOutput);
			continue;
		}

		//No need to verify if we did not entered this value
		if(expectedDataOutput.equalsIgnoreCase("{skip}")) 
		{
			testConfig.logWarning("Skipping the verification of Recon Output Field '" + FieldName + "' as '" +  actualDataOutput);
			continue;
		}

		if(expectedDataOutput.startsWith("{contains}"))
		{
			expectedDataOutput = expectedDataOutput.replace("{contains}", "");
			Helper.compareContains(testConfig, "Recon Output Field '" + FieldName + "'", expectedDataOutput,  actualDataOutput);
		}

		else
		{
			Helper.compareEquals(testConfig, "Recon Output Field '" + FieldName + "'", expectedDataOutput,  actualDataOutput);
		}
	}

	//restore the screenshot value
	testConfig.enableScreenshot = true;

	Helper.compareTrue(testConfig, "Recon Output", testConfig.getTestResult());

}

	/**
	 * Upload recon file on recon page and then compares data from the file generated
	 * @param helper
	 * @param reconFileInput - input data row 
	 * @param reconRowNumber = row number for comparison from data
	 * @param mihpayid -payuid
	 * @return refernce id for the recon
	 */
	public String UploadAndCompareExcel(TransactionHelper helper,
			String reconFileInput, int[] reconRowNumber, String[] mihpayid) {
		helper.home.navigateToAdminHome();

		// Navigate to Admin Panel Recon Option
		manualUpdatePage = helper.home.ClickManualTransactionUpdate();
		reconPage = manualUpdatePage.ClickRecon();

		// upload Recon File
		String refId = reconPage.uploadRecon(testConfig, reconFileInput);

		// Read data from downloaded file and compare contents
		int reconOutputRow = 1;
		for (int i = 0; i < reconRowNumber.length; i++) {

			readAndCompareReconOutput(testConfig, refId, reconRowNumber[i],
					reconOutputRow, mihpayid[i]);
			reconOutputRow++;
		}
		return refId;
	}

	/** Finds and compares values in recon file generated
	 * @param reconRowNum row number from data file
	 * @param referenceId reference id generated for recon
	 * @param mihpayid payuid of the transaction
	 */
	public void compareReconFile(int[] reconRowNum, String referenceId, String[] mihpayid) {
		for (int i = 0; i < reconRowNum.length; i++) {
			int reconOutputRow = 1;
			readAndCompareReconOutput(testConfig, referenceId, reconRowNum[i],
					reconOutputRow, mihpayid[i]);
			reconOutputRow++;
		}

	}
	/**
	 * @param reconSheet - Object of ReconOutputFile
	 * @param reconRowNum - Row Number of ReconOutputFile
	 * @	return Recon File ready for upload
	 */
	public String writeAdminReconFile(int [] reconRowNum, String []  mihpayid) {		
		TestDataReader reconSheet = new TestDataReader(testConfig, "ReconFileOutput");	
		String randomUTR=Helper.generateRandomAlphaNumericString(4);
		if(testConfig.getRunTimeProperty("UTR")!=null){
			randomUTR=testConfig.getRunTimeProperty("UTR");
		}
		testConfig.putRunTimeProperty("UTR",randomUTR );
		String randomRefNo=Double.toString(Helper.generateRandomNumber(5));
		testConfig.putRunTimeProperty("refNo",randomRefNo );
		String strReconcilationId  = "";
		if(testConfig.getRunTimeProperty("ReconcilationId")!=null){
			strReconcilationId=testConfig.getRunTimeProperty("ReconcilationId");
		}
		String reconFileName = Helper.getExcelFile(testConfig, FileType.adminRecon);
		
		int reconRecord=1;
		for(int i=0;i<reconRowNum.length;i++){
		String mihpayuid=mihpayid[i];
		String mode = reconSheet.GetData(reconRowNum[i], "category");
		String amount=reconSheet.GetData(reconRowNum[i], "amount");
		if(testConfig.getRunTimeProperty("amount") != null){
			amount = testConfig.getRunTimeProperty("amount");
		}
		String servicetax=reconSheet.GetData(reconRowNum[i], "servicetax");
		String servicefee=reconSheet.GetData(reconRowNum[i], "servicefee");
		String status=reconSheet.GetData(reconRowNum[i], "status");
		strReconcilationId=reconSheet.GetData(reconRowNum[i], "reconciliationid");
		//Calculating net
		Double netamount=Double.parseDouble(amount)-Double.parseDouble(servicetax)-Double.parseDouble(servicefee);
		
		//
		String net = String.valueOf(netamount);
		String action = reconSheet.GetData(reconRowNum[i], "action");
		if(action.equals("refund")||action.equals("chargeback")){
			netamount=-(Double.parseDouble(amount)-Double.parseDouble(servicetax)-Double.parseDouble(servicefee));
			net = String.valueOf(netamount);
		}
		String cardtype=reconSheet.GetData(reconRowNum[i], "cardtype");
		String forceBankTdr="0";
		//filling transaction details in Recon File
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord, 1, mihpayuid,"PayU ID");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord, 2, action,"action");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord, 3, status,"status");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord, 4, amount,"amount");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord, 5, servicefee,"Service Fee");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord, 6, servicetax,"Service Tax");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord, 7, net,"net");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord, 8, randomUTR,"UTR");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord, 9, randomRefNo,"Ref No");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord, 10, strReconcilationId,"Reconcialtion ID");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord,11, mode, "Mode");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord,12, cardtype, "Category");
		FileHandler.write_editXLS(testConfig, reconFileName, "Sheet1", reconRecord,13, forceBankTdr, "ForceBankTDR");
		reconRecord++;
		}
		return reconFileName;
				
	}

	/**
	 * Writes admin recon file in download folder with specified column missing
	 * @param reconRowNum
	 * @param columnName
	 * @return
	 */
	public String writeAdminReconFileWithColumnMissing(int[] reconRowNum,
			String columnName) {
		String reconFileName = Helper.getExcelFile(testConfig,
				FileType.adminRecon);
		testConfig.logComment("Adding empty column in recon file for "
				+ columnName);
		switch (columnName) {
			case "requestid" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 0);
				break;
			case "transactionid" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 1);
				break;
			case "action" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 2);
				break;
			case "status" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 3);
				break;
			case "amount" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 4);
				break;
			case "servicefee" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 5);
				break;
			case "servicetax" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 6);
				break;
			case "net" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 7);
				break;
			case "utr" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 8);
				break;
			case "refnum" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 9);
				break;
			case "reconciliationid" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 10);
				break;
			case "category" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 11);
				break;
			case "card_type" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 12);
				break;
			case "forcebanktdr" :
				FileHandler.setCellBlank(reconFileName, "SHeet1", 0, 13);
				break;
			default :
				testConfig.logFail("Coulumn not defined for " + columnName);
				break;
		}
		return reconFileName;
	}

	/**
	 * Creates a refund reversal file and returns its path
	 * @param mihpayid - payu id
	 * @param refundReversalRow- row number if input data in productTestData
	 * @return
	 */
	public String createRefundReversalFile(String mihpayid,
			int refundReversalRow) {
		testConfig.putRunTimeProperty("RefundDataSheet",
				"\\Parameters\\Refund Reversal File.xls");
		//test Data file
		TestDataReader refundReversal = new TestDataReader(testConfig,
				"RefundReversalDetails");
		//inputs for all rows
		String refundFile = Helper.getExcelFile(testConfig, FileType.refund);
		String amount = refundReversal.GetData(refundReversalRow, "Amount");
		String Refnum = refundReversal.GetData(refundReversalRow, "Refnum");
		String Status = refundReversal.GetData(refundReversalRow, "Status");
		String Force = refundReversal.GetData(refundReversalRow, "Force");
		//edit all columns
		FileHandler.write_edit(refundFile, "Sheet1", 1, 0, mihpayid);
		FileHandler.write_edit(refundFile, "Sheet1", 1, 1,amount);
		FileHandler.write_edit(refundFile, "Sheet1", 1, 2,Refnum);
		FileHandler.write_edit(refundFile, "Sheet1", 1, 3,Status);
		FileHandler.write_edit(refundFile, "Sheet1", 1, 4,Force);
		
		return refundFile;
	}

	
	/**
	 * Creates a recconcilation file and uploads in following steps
	 * Creates a reconcilation file
	 * Goes to recon tab in manual transaction update
	 * Uploads and clicks submit
	 * Verifies Error message that gets displayed
	 * @param helper 
	 * @param reconRowNum - Data for input file
	 * @param columnNameMissing - column name to be missing
	 * @param reconcilationError - Error displayed on uploading invalid file
	 */
	public void uploadReconWithColumnMissingAndVerifyErrorMessage(TransactionHelper helper, int[] reconRowNum, String columnNameMissing,
			String reconcilationError) {
		String reconFile = writeAdminReconFileWithColumnMissing(
				reconRowNum, "transactionid");
		// Click on Home link.
		// Click on 'Manual Transaction update' link on home page.
		helper.home.navigateToAdminHome();
		ManualUpdatePage manualUpdatePage = helper.home
				.ClickManualTransactionUpdate();
		// Click on 'Recon' Tab
		ReconPage reconPage = manualUpdatePage.ClickRecon();
		// Click on Browse link under the 'Upload Reconcilation Data' section
		// and select 'AdminRecon'xls file to upload.
		// Click on submit button.
		reconPage.ReconcileFileBrowseandSubmitFileWithFullPath(reconFile);
		//Verify that following error message is displayed
		reconPage.verifyFileUploadError(reconcilationError);
		
	}
	
	/**
	 * Gets Chargeback page as following
	 *Navigates to home
	 *Clicks on manual transaction update link
	 *Selects chargeback tab
	 * @param helper
	 * @return
	 */
	public ChargebackTab getChargebackPage(TransactionHelper helper) {
		helper.home.navigateToAdminHome();
		ManualUpdatePage manualUpdatePage = helper.home
				.ClickManualTransactionUpdate();
		return manualUpdatePage.clickOnChargebackTab();
	}
	
	 /**
	  * verify entry for the specified payuid in txn_update table
	  *  @param mihpayid 
	 * @param testConfig
	 * @return 
	 */
	public void verifyEntryNotCreatedInTxnUpdateTable(String mihpayid){
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		Map<String,String> map = null;
			map = DataBase.executeSelectQuery(testConfig, 4, 1);
			if(map == null)
			{	
				testConfig.logPass("No Entry has been made with id " + mihpayid);
				return;
			}
			else
				testConfig.logFail("txn_update entry made with id:"+mihpayid );
	}
	/**
	 * Refunds reverses reversal for specified mihpayid by following steps
	 * Goes to refund tab
	 * Uploads refund reversal file and submits
	 * @param strMihpayid -transaction id
	 * @param refundReversalRow - data for refund reversal file file
	 */
	public void doRefundReversal(TransactionHelper helper ,String strMihpayid, int refundReversalRow) {
		//go home apge then manual update page
		helper.home.navigateToAdminHome();
		ManualUpdatePage manualUpdatePage = helper.home.ClickManualTransactionUpdate();
		// Click on 'Refund' tab.
		RefundTab refundTab = manualUpdatePage.goToRefundTab();
		// Browse 'Refund reversal' file and click Submit button.
		refundTab.createAndUploadRefundReversalFile(strMihpayid,
				refundReversalRow);
	}
	public enum ReconType{Success,Refund,RefundReversal,Chargeback,ChargebackReversal};
	/** Does a test transaction
	 * Uploads and compares recon using fresh transaction id generated
	 * @param helper
	 * @param transactionRowNum - Transaction row number to fill transaction details
	 * @param paymentTypeRowNum- Details for filling payment
	 * @param cardDetailsRowNum- Card details from data file
	 * @param reconRowNum- Recon row num to get file 
	 * @return
	 */
	public String doTestTransactionAndRecon(TransactionHelper helper,int transactionRowNum,int paymentTypeRowNum,int cardDetailsRowNum,int reconRowNum,ReconType recontype){
		String mihpayid[]=new String[1];
		int[] reconRow =new int[1];
		reconRow[0]=reconRowNum;
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		mihpayid[0]=helper.testResponse.actualResponse.get("mihpayid");
		//Recon the transaction
		String reconFile=writeAdminReconFile(reconRow,mihpayid);
		if(recontype==ReconType.Refund||recontype==ReconType.RefundReversal){
			testConfig.putRunTimeProperty("action_name", "refund");
			DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
			MerchantPanelPage merchantPage=dashboardHelper.doMerchantLogin(transactionRowNum);
			TransactionsHelper tranHelper=new TransactionsHelper(testConfig);
			
			tranHelper.performRefundAction(
					helper.testResponse.actualResponse.get("amount"),
					testConfig.getRunTimeProperty("txnid"),
					"Successfully queued 1 out of 1 request(s).", "Refunded",
					"0.00");
			//if refund reversal go further and do refund reversal
			if(recontype==ReconType.RefundReversal){
				testConfig.putRunTimeProperty("action_name", "refundreversal");
				doRefundReversal(helper,mihpayid[0],2);
			}
			
		}
		if(recontype==ReconType.Chargeback||recontype==ReconType.ChargebackReversal){
			//if chargeback
			testConfig.putRunTimeProperty("action_name", "chargeback");
			ChargebackTab chargeback = getChargebackPage(helper);
			chargeback.createAndUploadChargebackfile(mihpayid[0],
					2, ChargebackFileType.chargeback);
			//if chargeback reversal go further and do chargeback reversal
			if(recontype==ReconType.ChargebackReversal){
				testConfig.putRunTimeProperty("action_name", "chargebackreversal");
				chargeback.createAndUploadChargebackfile(mihpayid[0],
						2, ChargebackFileType.chargebackReversal);
			}
		}
		helper.home.navigateToAdminHome();
		reconPage = (ReconPage) helper
				.getManualUpdateTab(ManualUpdateTab.Recon);
		String referenceid = reconPage.uploadRecon(testConfig,reconFile);
		return referenceid;
	}

	
	/**Edit last downloaded recon file with random values for specified column
	 * @param columnName - Name of the column whose value needs to be changed
	 * @return- File with values changed
	 */
	public String editDownloadedReconFileFor(String columnName) {
		//Find File
		String downloadedfilepath=getLastDownloadedReconFileAndConvertToExcel();
		//Convert it to Excel format
		String strTargetPath = FileHandler.saveXMLAsExcel(testConfig, downloadedfilepath,"");
		
		switch (columnName) {
		case "amount":
			FileHandler.write_edit(strTargetPath, "SettlementInputXLS", 1, 7, String.valueOf(Helper.generateRandomNumber(3)));
			break;
		case "action":
			FileHandler.write_edit(strTargetPath, "SettlementInputXLS", 1, 3, Helper.generateRandomAlphabetsString(6));
			break;
		case "transactionid":
			FileHandler.write_edit(strTargetPath, "SettlementInputXLS", 1, 1, String.valueOf(Helper.generateRandomNumber(8)));
		default:
			break;
		}
		
		
		return strTargetPath;
	}
	
	/**Find Last downloaded recon file and converts it to excel format
	 * @return File path
	 */
	public String getLastDownloadedReconFileAndConvertToExcel(){
		String downloadedfilepath = System.getProperty("user.home") + "\\Downloads\\";
		File file = Browser.lastFileModifiedWithDesiredName(testConfig, downloadedfilepath,"reconciliation");
		
		String fileName=file.getName();
		downloadedfilepath=downloadedfilepath+fileName;
		testConfig.logComment("Found file " + downloadedfilepath);
		String strTargetPath = System.getProperty("user.home") + "\\Downloads\\"+Helper.getCurrentDateTime("HH-mm-ss")+".xls";
		FileHandler.saveXMLAsExcel(testConfig, downloadedfilepath,strTargetPath);
		return strTargetPath;
	}
}
