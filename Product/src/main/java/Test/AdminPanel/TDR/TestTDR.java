package Test.AdminPanel.TDR;

import java.io.File;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.ManualUpdate.ReconPage;
import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.TDRList;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Settlement.InvalidFilePage;
import PageObject.AdminPanel.uploadTDR.uploadTDRpage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.FileHandler;
import Utils.Helper;
import Utils.Helper.FileType;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestTDR extends TestBase{

	MerchantDetailsPage merchantDetailsPage;
	TDRList tdrList;
	ManualUpdatePage manualTransactionUpdate;
	ReconPage reconPage;
	InvalidFilePage invalidFilepage;

	@Test(description = "Verify add TDR rule", 
			dataProvider="GetTestConfig", timeOut=600000, groups ="1")
	public void addTDR(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int testMerchantDataRow = 44;
		int paramRow=1;

		HomePage home = new HomePage(testConfig);
		MerchantListPage merchantListPage = home.clickMerchantList();

		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		tdrList.addParameters(paramRow);
		Browser.wait(testConfig, 10);

		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		tdrList.verifyTDR(paramRow);

		paramRow=5;
		tdrList.addParameters(paramRow);
		Browser.wait(testConfig, 2);

		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		tdrList.verifyTDR(paramRow);

		tdrList.removeTDR(testConfig);

		tdrList = merchantDetailsPage.ClickTDRList(testConfig);

		tdrList.verifyTDRnotPresent(testConfig);

		Assert.assertTrue(testConfig.getTestResult());  
	}



	@Test(description = "Verify add invalid TDR rule", 
			dataProvider="GetTestConfig", timeOut=600000, groups ="1")
	public void addInvalidTDR(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int testMerchantDataRow = 44;
		int paramRow=6;

		HomePage home = new HomePage(testConfig);
		MerchantListPage merchantListPage = home.clickMerchantList();

		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		tdrList.addParameters(paramRow);
		Browser.wait(testConfig, 2);

		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		tdrList.verifyTDRnotPresent(testConfig);

		Assert.assertTrue(testConfig.getTestResult());  
	}

	@Test(description = "Verify add TDR rule without selecting the payment option.", 
			dataProvider="GetTestConfig", timeOut=600000, groups ="1")
	public void addTDR_without_payment_option(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int testMerchantDataRow = 44;
		int paramRow=9;
		int TestParamRow =1;

		HomePage home = new HomePage(testConfig);
		MerchantListPage merchantListPage = home.clickMerchantList();

		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		tdrList.addParameters(paramRow);
		Browser.wait(testConfig, 2);

		tdrList = merchantDetailsPage.ClickTDRList(testConfig);

		tdrList.verifyTDR(TestParamRow);

		tdrList.removeTDR(testConfig);

		Assert.assertTrue(testConfig.getTestResult());  
	}

	@Test(description = "Verify add TDR rule without selecting the gateway.", 
			dataProvider="GetTestConfig", timeOut=600000, groups ="1")
	public void addTDR_without_gateway(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int testMerchantDataRow = 44;
		int paramRow=10;

		HomePage home = new HomePage(testConfig);
		MerchantListPage merchantListPage = home.clickMerchantList();

		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		tdrList.addParameters(paramRow);
		Browser.wait(testConfig, 2);

		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		int TestParamRow =1;
		tdrList.verifyTDR(TestParamRow);

		tdrList.removeTDR(testConfig);

		Assert.assertTrue(testConfig.getTestResult());  
	}

	@Test(description = "Verify add TDR rule using upload TDR.", 
			dataProvider="GetTestConfig", timeOut=600000, groups ="1")
	public void uploadTDR(Config testConfig)
	{
		//create new helper and upload tdr file
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		String uploadFile = Helper.getExcelFile(testConfig, FileType.uploadTDR);

		HomePage home = new HomePage(testConfig);
		uploadTDRpage tdrPage = home.ClickUploadTDR();

		tdrPage.uploadTDR(testConfig,uploadFile);

		Browser.wait(testConfig, 2);

		//open and read the downloaded file
		String tdrfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,tdrfilepath);
		String fileName = file.getName();

		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", tdrfilepath+fileName);

		String result = tr.GetCurrentEnvironmentData(1, "result");
		Browser.wait(testConfig, 1);

		//verify the result field in the downloaded file
		Helper.compareEquals(testConfig, "TDR file uploaded successfully", "SUCCESS - New Rule Added for Merchant" , result);

		/*if(!result.equals("SUCCESS - New Rule Added for Merchant"))
			testConfig.logFail("Failed to upload TDR file properly");*/


		//go to tdr list again and verify that the rule has been added
		MerchantListPage merchantListPage = tdrPage.clickMerchantList();

		int testMerchantDataRow = 44;
		int paramRow=1;

		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		tdrList = merchantDetailsPage.ClickTDRList(testConfig);

		tdrList.verifyTDR(paramRow);

		tdrList.removeTDR(testConfig);

		Assert.assertTrue(testConfig.getTestResult());  
	}

	@Test(description = "Verify upload TDR: uploading an invalid TDR file", 
			dataProvider="GetTestConfig", timeOut=600000, groups ="1")
	public void uploadInvalidTDR(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		HomePage home = new HomePage(testConfig);
		uploadTDRpage tdrPage = home.ClickUploadTDR();

		String uploadFile = Helper.getExcelFile(testConfig, FileType.invalidTDR);

		tdrPage.uploadTDR(testConfig,uploadFile);

		Browser.wait(testConfig, 2);

		String tdrfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,tdrfilepath);
		String fileName = file.getName();

		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", tdrfilepath+fileName);

		String result = tr.GetCurrentEnvironmentData(1, "result");
		Browser.wait(testConfig, 1);

		Helper.compareEquals(testConfig, "invalid TDR rule file", "FAILURE - Invalid Flat and Percent Charges" , result);

		//go to tdr list again and verify that the rule has been added
		MerchantListPage merchantListPage = tdrPage.clickMerchantList();

		int testMerchantDataRow = 44;
		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		tdrList = merchantDetailsPage.ClickTDRList(testConfig);

		tdrList.verifyTDRnotPresent(testConfig);

		Assert.assertTrue(testConfig.getTestResult());  
	}

	@Test(description = "TDR rule transaction: Test that TDR rule is hit.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDR_1(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int TDRrow = 1;

		float t_amount = 50;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_tdr_calculation(testConfig,TDRrow);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "TDR rule transaction: Test that TDR rule is not hit.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDR_2(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//AMEX
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		float t_amount = 5;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 1, 1);
		String payuId = map1.get("mihpayid");

		int retries = 60;
		Map<String,String> map2 = null;

		while(retries>0){
			testConfig.putRunTimeProperty("payuId", payuId);
			Browser.wait(testConfig, 2);
			map2 = DataBase.executeSelectQuery(testConfig, 4, 1);
			if(map2 != null)
				retries = 0;
			else retries--;
		}

		Float fee1 = new Float(map2.get("mer_service_fee"));

		Browser.wait(testConfig, 3);
		Helper.compareEquals(testConfig, "TDR rule not hit- fee", fee1, 0);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "TDR rule transaction: Test that TDR rule is hit and amount becomes negative", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDR_3(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//AMEX
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		testConfig.putRunTimeProperty("amount", "12");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 1, 1);
		String payuId = map1.get("mihpayid");

		int retries = 60;
		Map<String,String> map2 = null;

		while(retries>0){
			testConfig.putRunTimeProperty("payuId", payuId);
			Browser.wait(testConfig, 2);
			map2 = DataBase.executeSelectQuery(testConfig, 4, 1);
			if(map2 != null)
				retries = 0;
			else retries--;
		}
		Float net_amount = new Float(map2.get("mer_net_amount"));

		Browser.wait(testConfig, 2);

		if(net_amount >= 0){
			testConfig.logFail("TDR rule not hit properly");
		}
		testConfig.logPass("verfied net amount is negative-TDR rule hit properly");

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "TDR rule transaction: Test that TDR rule is hit. 100 < AMount < 1000", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDR_4(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int TDRrow = 2;

		float t_amount = 400;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_tdr_calculation(testConfig,TDRrow);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "TDR rule transaction: Test that TDR rule is hit. Amount > 1000", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDR_5(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int TDRrow = 4;

		float t_amount = 5000;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_tdr_calculation(testConfig,TDRrow);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "TDR rule transaction: Debit Card Transaction where rule for Payment Option is not made", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDR_6(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 17;
		int cardDetailsRowNum = 1;
		int TDRrow = 8;

		float t_amount = 50;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_tdr_calculation(testConfig,TDRrow);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "TDR rule transaction: Debit Card Transaction where rule for Payment Option is made", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDR_7(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 35;
		int cardDetailsRowNum = 1;
		int TDRrow = 8;

		float t_amount = 50;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_tdr_calculation(testConfig,TDRrow);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "TDR rule transaction: Test that TDR rule is not hit.Add Rule with international. Perform transaction with domestic.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDR_8(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int testMerchantDataRow = 44;
		int paramRow=11;

		HomePage home = new HomePage(testConfig);
		MerchantListPage merchantListPage = home.clickMerchantList();

		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		tdrList.addParameters(paramRow);
		Browser.wait(testConfig, 10);


		//AMEX
		int transactionRowNum = 44;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		float t_amount = 50;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		int retries = 60;
		Map<String,String> map2 = null;

		while(retries>0){
			Browser.wait(testConfig, 2);
			map2 = DataBase.executeSelectQuery(testConfig, 4, 1);
			if(map2 != null)
				retries = 0;
			else retries--;
		}

		Float fee1 = new Float(map2.get("mer_service_fee"));

		Browser.wait(testConfig, 3);
		Helper.compareEquals(testConfig, "Rule not hit-service fee", fee1 , 0);

		merchantListPage = home.clickMerchantList();
		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		tdrList = merchantDetailsPage.ClickTDRList(testConfig);

		tdrList.removeTDR(testConfig);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "TDR rule transaction: Test that TDR rule is hit with rule for Rewards.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDR_9(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int TDRrow = 1;

		float t_amount = 50;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_tdr_calculation(testConfig,TDRrow);

		String filePath = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		String mihpayid = testConfig.getRunTimeProperty("mihpayid");

		FileHandler.write_edit(filePath,"Sheet1", 1, 1, mihpayid, 0);
		FileHandler.write_edit(filePath,"Sheet1", 1, 12, "rewards", 0);

		helper.home = helper.testResponse.ClickHomeLink();

		manualTransactionUpdate = helper.home.ClickManualTransactionUpdate();
		reconPage = manualTransactionUpdate.ClickRecon();
		reconPage.uploadRecon(testConfig, filePath);

		Browser.wait(testConfig, 2);

		//open and read the downloaded file
		String reconFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,reconFilePath);
		String recon_fileName = file.getName();

		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", reconFilePath + recon_fileName);

		String result = tr.GetCurrentEnvironmentData(1, "result");
		Browser.wait(testConfig, 1);

		//verify the result field in the downloaded file
		Helper.compareEquals(testConfig, "reconcile TDR Transaction properly", "SUCCESS - Transaction reconciled successfully" , result);

		int rewards_row = 12;
		helper.verify_tdr_calculation(testConfig,rewards_row);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "TDR rule transaction: Test that TDR rule is hit with rule for On Us.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDR_10(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int TDRrow = 1;

		float t_amount = 50;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_tdr_calculation(testConfig,TDRrow);


		String filePath = Helper.getExcelFile(testConfig, FileType.reconTDR2);

		String mihpayid = testConfig.getRunTimeProperty("mihpayid");

		FileHandler.write_edit(filePath,"Sheet1", 1, 1, mihpayid, 0);

		helper.home = helper.testResponse.ClickHomeLink();

		manualTransactionUpdate = helper.home.ClickManualTransactionUpdate();
		reconPage = manualTransactionUpdate.ClickRecon();
		reconPage.uploadRecon(testConfig, filePath);


		Browser.wait(testConfig, 2);

		//open and read the downloaded file
		String reconFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,reconFilePath);
		String recon_fileName = file.getName();

		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", reconFilePath + recon_fileName);

		String result = tr.GetCurrentEnvironmentData(1, "result");
		Browser.wait(testConfig, 1);

		//verify the result field in the downloaded file
		Helper.compareEquals(testConfig, "reconcile TDR Transaction properly", "SUCCESS - Transaction reconciled successfully" , result);

		int rewards_row = 13;
		helper.verify_tdr_calculation(testConfig,rewards_row);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "TDR rule transaction: Test that TDR rule is hit with rule for On Us.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDR_CF(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Do a transaction with additional charge
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int TDRrow = 1;

		helper.GetTestTransactionPage();

		float t_amount = 40;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		testConfig.putRunTimeProperty("additionalCharges", String.valueOf("CC:10"));

		String amount = testConfig.getRunTimeProperty("amount");
		double transactionamount = Double.parseDouble(amount);

		String keyvalue = testConfig.getRunTimeProperty("additionalCharges");
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

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge
		helper.verify_tdr_calculation(testConfig,TDRrow);

		String filePath = Helper.getExcelFile(testConfig, FileType.reconTDR2);

		String mihpayid = testConfig.getRunTimeProperty("mihpayid");
		FileHandler.write_edit(filePath,"Sheet1", 1, 1, mihpayid, 0);
		FileHandler.write_edit(filePath, "Sheet1", 1, 4,
				transactionAmount, 0);
		FileHandler.write_edit(filePath, "Sheet1", 1, 7,
				transactionAmount, 0);

		helper.home = helper.testResponse.ClickHomeLink();
		manualTransactionUpdate = helper.home.ClickManualTransactionUpdate();
		reconPage = manualTransactionUpdate.ClickRecon();
		reconPage.uploadRecon(testConfig, filePath);

		Browser.wait(testConfig, 2);

		//open and read the downloaded file
		String reconFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,reconFilePath);
		String recon_fileName = file.getName();

		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", reconFilePath + recon_fileName);

		String result = tr.GetCurrentEnvironmentData(1, "result");
		Browser.wait(testConfig, 1);

		//verify the result field in the downloaded file
		Helper.compareEquals(testConfig, "reconcile TDR Transaction properly", "SUCCESS - Transaction reconciled successfully" , result);

		int rewards_row = 13;
		helper.verify_tdr_calculation(testConfig,rewards_row);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Test TDR calculation if rule for accrewards1 hits at the time of recon.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_verifyTDRaccReward
	(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int TDRrow = 1;

		float t_amount = 50;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_tdr_calculation(testConfig,TDRrow);

		String filePath = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		String mihpayid = testConfig.getRunTimeProperty("mihpayid");

		//update values in recon file
		FileHandler.write_edit(filePath,"Sheet1", 1, 1, mihpayid, 0);
		FileHandler.write_edit(filePath,"Sheet1", 1, 12, "accrewards1", 0);

		helper.home = helper.testResponse.ClickHomeLink();

		manualTransactionUpdate = helper.home.ClickManualTransactionUpdate();
		reconPage = manualTransactionUpdate.ClickRecon();
		reconPage.uploadRecon(testConfig, filePath);

		Browser.wait(testConfig, 2);

		//open and read the downloaded file
		String reconFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,reconFilePath);
		String recon_fileName = file.getName();

		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", reconFilePath + recon_fileName);

		String result = tr.GetCurrentEnvironmentData(1, "result");
		float serFee = new Float (tr.GetCurrentEnvironmentData(1, "service tax"));
		float serTax = new Float (tr.GetCurrentEnvironmentData(1, "pay u charge"));

		String serviceTax=String.format("%.2f", serTax);
		String serviceFee=String.format("%.2f", serFee);

		Browser.wait(testConfig, 1);

		//verify the result field in the downloaded file
		Helper.compareEquals(testConfig, "reconcile TDR Transaction properly", "SUCCESS - Transaction reconciled successfully" , result);

		int rewards_row = 14;
		helper.verify_tdr_calculation(testConfig,rewards_row);
		helper.verify_TDR_recon(serviceFee,serviceTax,rewards_row);
		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Test the correct TDR calculation in case of Transaction reconciled with another payment type.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_PaymentType(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int TDRrow = 1;

		float t_amount = 50;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_tdr_calculation(testConfig,TDRrow);

		String filePath = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		String mihpayid = testConfig.getRunTimeProperty("mihpayid");

		FileHandler.write_edit(filePath,"Sheet1", 1, 1, mihpayid, 0);
		FileHandler.write_edit(filePath,"Sheet1", 1, 11, "DC", 0);

		helper.home = helper.testResponse.ClickHomeLink();

		manualTransactionUpdate = helper.home.ClickManualTransactionUpdate();
		reconPage = manualTransactionUpdate.ClickRecon();
		reconPage.uploadRecon(testConfig, filePath);

		Browser.wait(testConfig, 2);

		//open and read the downloaded file
		String reconFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,reconFilePath);
		String recon_fileName = file.getName();

		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", reconFilePath + recon_fileName);

		String result = tr.GetCurrentEnvironmentData(1, "result");
		float serFee = new Float (tr.GetCurrentEnvironmentData(1, "service tax"));
		float serTax = new Float (tr.GetCurrentEnvironmentData(1, "pay u charge"));

		String serviceTax=String.format("%.2f", serTax);
		String serviceFee=String.format("%.2f", serFee);

		Browser.wait(testConfig, 1);

		//verify the result field in the downloaded file
		Helper.compareEquals(testConfig, "reconcile TDR Transaction properly", "SUCCESS - Transaction reconciled successfully" , result);

		int rewards_row = 15;
		helper.verify_tdr_calculation(testConfig,rewards_row);
		helper.verify_TDR_recon(serviceFee,serviceTax,rewards_row);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Test that TDR rule should not hit if transaction amount is less than minimum amount of TDR rule.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_MinAmount(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int TDRrow = 16;

		float t_amount = 9;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		helper.verify_tdr_calculation(testConfig,TDRrow);
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Test that TDR rule should not hit if rule not present for that payment type.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void transaction_TDRnotHit(Config testConfig)
	{TransactionHelper helper = new TransactionHelper(testConfig, true);
	helper.DoLogin();

	//Do DC maestro transaction
	int transactionRowNum = 32;
	int paymentTypeRowNum = 23;
	int cardDetailsRowNum = 1;

	helper.GetTestTransactionPage();
	helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

	int rewards_row = 16;
	//Verify TDR calculation
	helper.verify_tdr_calculation(testConfig,rewards_row);

	Assert.assertTrue(testConfig.getTestResult());	
	}

}
