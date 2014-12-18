package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsMerchantParamsPage;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;

public class TestSeamlessRSA extends TestBase{

	
	
	@Test(description = "Verify RSA feature  For Credit Card", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_RSA(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC 
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("publickey", "publickey1234");
		testConfig.putRunTimeProperty("ProcessingPageURL_Expected", "https://secure.payu.in/_seamless_payment");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePageWithProcessURL);
		
		Helper.compareContains(testConfig, "Processing Page URL for Seamless RSA", testConfig.getRunTimeProperty("ProcessingPageURL_Expected"),testConfig.getRunTimeProperty("Processing_Page_URL_Actual") );
		
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}
	
	
	
	@Test(description = "VerifySeamless Transaction Page using Public Key For DebitCard(Maestro)", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_RSA(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//DC
		int transactionRowNum = 110;
		int paymentTypeRowNum = 320;
		int cardDetailsRowNum = 1;
		
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("IsSeamlessDCTxn", "yes");
		testConfig.putRunTimeProperty("publickey", "abc123");
		testConfig.putRunTimeProperty("processingPageURL_Expected", "https://secure.payu.in/_seamless_payment");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePageWithProcessURL);

		Helper.compareContains(testConfig, "Processing Page URL for Seamless RSA", testConfig.getRunTimeProperty("ProcessingPageURL_Expected"),testConfig.getRunTimeProperty("Processing_Page_URL_Actual") );
		
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}
	
	
	/*
	
	
	@Test(description = "veirfy Saved Card transaction with RSA flow", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void SavedSeamlessCardRSATransaction(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC - DINR
		int transactionRowNum = 2;
		int paymentTypeRowNum = 235;
		int cardDetailsRowNum=18;
		int storeCardDetailsRowNum = 1;
		boolean verifythroughSeamless=true;
		testConfig.putRunTimeProperty("publickey", "publickey1234");
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, verifythroughSeamless,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);


		//CC - VISA/Master
		transactionRowNum = 2;
		paymentTypeRowNum = 235;
		cardDetailsRowNum=18;
		storeCardDetailsRowNum = 1;

		testConfig.putRunTimeProperty("publickey", "publickey1234");
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum,verifythroughSeamless, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

	}
	
	@Test(description = "Verify offer for a seamless credit card transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferSeamlessCCTransaction(Config testConfig) {
		int merchantLoginRow = 1;
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		OffersHelper oHelper = new OffersHelper(testConfig);
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);
		// navigate to offer list page
		new OfferListPage(testConfig);

		// set offer to use for transactions
		oHelper.setOfferInfo("ValidOffer");

		// login to admin
		helper.DoLogin();
		// make transaction with created offer
		helper.GetTestTransactionPage();

		// do a seamless credit card transaction for 10 Rs
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "5.00");
		testConfig.putRunTimeProperty("publickey", "publickey1234");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount, offer key for 10 rupees seamless transaction
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		helper.GetTestTransactionPage();

		// do a seamless credit card transaction for 5 Rs
		testConfig.putRunTimeProperty("amount", "5.00");
		testConfig.putRunTimeProperty("discount", "4.00");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;

		// verify amount,discount, offer key for 5 rupees seamless transaction
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		helper.testResponse.overrideExpectedTransactionData = true;
		Assert.assertTrue(testConfig.getTestResult());

	}

	*/
	
	
	@Test(description = "Verify RSA For COD", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_RSA_COD(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC 
		int transactionRowNum = 110;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("publickey", "abc123");
		testConfig.putRunTimeProperty("PaymentOptionPageURL_Expected", "https://test.payu.in/_payment_options");

		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		
		helper.payment.VerifyRedirectedURL_PaymentOptionPage();

		Assert.assertTrue(testConfig.getTestResult());	
	}
	

/////////////////////////// This TC would fail unless changes in Database are rolled back /////////////////////////
	
	@Test(description = "Verify RSA using Public Key For EMI", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_RSA_EMI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC 
		int transactionRowNum = 1;
		int paymentTypeRowNum = 350;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		
		testConfig.putRunTimeProperty("publickey", "abc123");
		testConfig.putRunTimeProperty("ProcessingPageURL_Expected", "https://secure.payu.in/_seamless_payment");
		
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePageWithProcessURL);
		
		Helper.compareContains(testConfig, "Processing Page URL for Seamless RSA", testConfig.getRunTimeProperty("ProcessingPageURL_Expected"),testConfig.getRunTimeProperty("Processing_Page_URL_Actual") );
			
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
		
	}
	

	
	@Test(description = "Verify RSA for Netbanking", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_RSA_NB(Config testConfig)
	{

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Axis Bank
		int transactionRowNum = 110;
		int paymentTypeRowNum = 351;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();	
		

		
		testConfig.putRunTimeProperty("publickey", "abc123");
		
		testConfig.putRunTimeProperty("ProcessingPageURL_Expected", "https://secure.payu.in/_seamless_payment");
		
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPageWithProcessingURL);
		
		Helper.compareContains(testConfig, "Processing Page URL for Seamless RSA", testConfig.getRunTimeProperty("ProcessingPageURL_Expected"),testConfig.getRunTimeProperty("Processing_Page_URL_Actual") );
			
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
		
		
	}
	
	
	@Test(description = "Verify RSA For Cash Card", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_RSA_CashCard(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Axis Bank
		int transactionRowNum = 110;
		int paymentTypeRowNum = 353;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();	
		
		testConfig.putRunTimeProperty("publickey", "abc123");
		
		testConfig.putRunTimeProperty("ProcessingPageURL_Expected", "https://secure.payu.in/_seamless_payment");
		
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPageWithProcessingURL);
		
		Helper.compareContains(testConfig, "Processing Page URL for Seamless RSA", testConfig.getRunTimeProperty("ProcessingPageURL_Expected"),testConfig.getRunTimeProperty("Processing_Page_URL_Actual"));
			
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
		
	}
	
	
	
	
	
	@Test(description = "Verify RSA For Paisa Wallet", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_RSA_PaisaWallet(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Axis Bank
		int transactionRowNum = 110;
		int paymentTypeRowNum = 352;
		
		helper.GetTestTransactionPage();	
		
		testConfig.putRunTimeProperty("publickey", "abc123");
		
		testConfig.putRunTimeProperty("PayUMoneyURL_Expected", "https://test.payumoney.com/payments");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.FillSeamlessCodes(paymentTypeRowNum);
		
		helper.trans.clickSubmitButton();
		
		Helper.compareContains(testConfig, "PayU Money URL", testConfig.getRunTimeProperty("PayUMoneyURL_Expected"),helper.getURLForPayUMoneyPage());
		
		Assert.assertTrue(testConfig.getTestResult());
			
	}
	
	

	@Test(description = "Verify PublicKey and PrivateKey On Params section for the merchant", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_RSA_MerchantKeys(Config testConfig){
		
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		
		String merchantName="AutomationMerchantRSA";
		
		MerchantListPage merchantList=new MerchantListPage(testConfig);
		helper.GetMerchantListPage();	
		
		merchantList.SearchMerchant(merchantName);
		testConfig.putRunTimeProperty("merchantid", merchantList.getMerchantId());
		
		helper.merchantDetailsPage=merchantList.clickFirstMerchantKey();
		
		helper.paramPage= helper.merchantDetailsPage.ClickParams();
		
		ParamsMerchantParamsPage paramPage=new ParamsMerchantParamsPage(testConfig);
		Assert.assertTrue(paramPage.getMerchantPublicKey()!=null,"Public key for merchant is null on UI");
		Assert.assertTrue(paramPage.getMerchantPrivateKey()!=null,"Private key for merchant is null on UI");
		
		
		testConfig.putRunTimeProperty("key", "merchant_public_key");
		
		Helper.compareEquals(testConfig, "Merchant Public key", paramPage.getMerchantPublicKey(), helper.GetMerchantKeyFromDatabase());
		
		testConfig.putRunTimeProperty("key", "merchant_private_key");
		
		Helper.compareEquals(testConfig, "Merchant Private key", paramPage.getMerchantPrivateKey(), helper.GetMerchantKeyFromDatabase());
		

		Assert.assertTrue(testConfig.getTestResult());	
		
	}
	
}