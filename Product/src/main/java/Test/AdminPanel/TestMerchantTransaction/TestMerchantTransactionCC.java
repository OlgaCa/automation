package Test.AdminPanel.TestMerchantTransaction;

import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantCCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantCCTab.MerCardType;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Helper;
import Utils.Popup;
import Utils.TestBase;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;

public class TestMerchantTransactionCC extends TestBase {

	DashboardPage dashBoard;

	@Test(description = "Verify credit card transaction through AMEX gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_AMEX(Config testConfig)
	{	
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//AMEX
		int transactionRowNum = 1;
		int paymentTypeRowNum = 2;
		int cardDetailsRowNum = -1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.bankRedirectPage =(BankRedirectPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		merHelper.bankRedirectPage.VerifyRedirectedURL(merHelper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());	


	}
	@Test(description = "Verify credit card transaction through AXIS master gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_AXIS(Config testConfig)
	{	

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//Master card
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	


	}
	//TODO: after convenience fee is deployed
	/*@Test(description = "Verify credit card transaction through HDFC2 master gateway with convenience fee", 
			dataProvider="GetTestConfig")
	public void test_CC_HDFC_CF(Config testConfig)
	{	

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//Credit card
		int transactionRowNum = 65;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CC:2");
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.overrideExpectedTransactionData = true;
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		String amnt = merHelper.testResponse.actualResponse.get("amount");
		double amount = Double.parseDouble(amnt);
		
		String addCharge = merHelper.testResponse.actualResponse.get("additionalCharges");
		double additionalCharges = Double.parseDouble(addCharge);
		amount = amount+additionalCharges;	
		String transactionAmount = String.valueOf(amount)+"0";
		
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);
		
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(merHelper.transactionData,transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum, merHelper.cardDetailsData, cardDetailsRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());	

	}
	
	@Test(description = "Verify credit card transaction through HDFC2 master gateway with convenience fee", 
			dataProvider="GetTestConfig")
	public void test_CCandDC_HDFC_CF(Config testConfig)
	{	

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//Credit Card
		int transactionRowNum = 65;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CC:2,DC:3");
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.overrideExpectedTransactionData = true;
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);

		String amnt = merHelper.testResponse.actualResponse.get("amount");
		double amount = Double.parseDouble(amnt);
		
		String addCharge = merHelper.testResponse.actualResponse.get("additionalCharges");
		double additionalCharges = Double.parseDouble(addCharge);
		amount = amount+additionalCharges;	
		String transactionAmount = String.valueOf(amount)+"0";
		
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);
		
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(merHelper.transactionData,transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum, merHelper.cardDetailsData, cardDetailsRowNum);
		
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CC:2,DC:3");
		testConfig.putRunTimeProperty("amount", merHelper.transactionData.GetData(transactionRowNum, "amount"));
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.overrideExpectedTransactionData = true;
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);

		amnt = merHelper.testResponse.actualResponse.get("amount");
		amount = Double.parseDouble(amnt);
		
		addCharge = merHelper.testResponse.actualResponse.get("additionalCharges");
		additionalCharges = Double.parseDouble(addCharge);
		amount = amount+additionalCharges;	
		transactionAmount = String.valueOf(amount)+"0";
		
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);
		
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(merHelper.transactionData,transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum, merHelper.cardDetailsData, cardDetailsRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());	
	}
	*/
	@Test(description = "Verify credit card transaction through International card", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_InternationalTransactions(Config testConfig) 
	{	

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//Master card
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 20;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.tryAgainPage = (TryAgainPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
		merHelper.tryAgainPage.verifyRetryForPageElements();
		Helper.compareEquals(testConfig, "Reason of Failure ", "Reason for failure: Bank denied transaction on the card.", merHelper.tryAgainPage.getReasonForInternationalFailure());
		Assert.assertTrue(testConfig.getTestResult());	


	}
	@Test(description = "Verify credit card transaction through International card for disallowed merchant", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_InternationalTransactionsFailure(Config testConfig)
	{	

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//Master card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 336;
		int cardDetailsRowNum = 20;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	

	}
	/**
	 * Verify the error message when special characters are entered in card details
	 * @param testConfig
	  
	 */
	@Test(description = "Verify the error messages when invalid inputs are entered in Credit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnCreditCardTab(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		merHelper.merCCTab = (MerchantCCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);

		//Special char input 
		verifyErrors(merHelper, 4, testConfig);


		Assert.assertTrue(testConfig.getTestResult());

	}
	/**
	 * Verify the error message when inputs with spaces are entered in card details
	 * @param testConfig
	  
	 */
	@Test(description = "Verify the error messages when invalid inputs with spaces are entered in Credit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnCreditCardTabWithSpaces(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		merHelper.merCCTab = (MerchantCCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);

		//Input with spaces
		verifyErrors(merHelper, 5, testConfig);


		Assert.assertTrue(testConfig.getTestResult());
	}
	/**
	 * Verify the error message when inputs with spaces only are entered in card details
	 * @param testConfig
	  
	 */
	@Test(description = "Verify the error messages when invalid inputs  with spaces only are entered in Credit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnCreditCardTabWithSpacesOnly(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		merHelper.merCCTab = (MerchantCCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);

		//Spaces Only
		verifyErrors(merHelper, 6, testConfig);


		Assert.assertTrue(testConfig.getTestResult());
	}
	/**
	 * Verify the error message when blank inputs are entered in card details
	 * @param testConfig
	  
	 */
	@Test(description = "Verify the error messages when blank inputs  are entered in Credit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnCreditCardTabWithBlankInputs(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		merHelper.merCCTab = (MerchantCCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);

		//Blank Input
		verifyErrors(merHelper, 60, testConfig);

		Assert.assertTrue(testConfig.getTestResult());
	}
	/**
	 * Verify the error message when wrong details are entered in card details
	 * @param testConfig
	  
	 */
	@Test(description = "Verify the error messages when wrong details  are entered in Credit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnCreditCardTabWithWrongDetails(Config testConfig)
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		merHelper.merCCTab = (MerchantCCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);

		//Wrong details
		verifyErrors(merHelper, 61, testConfig);


		Assert.assertTrue(testConfig.getTestResult());
	}
	/**
	 * Verify the error message when only alphabets are entered in card details
	 * @param testConfig
	  
	 */
	@Test(description = "Verify the error messages when alphabetical card details are entered in Credit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnCreditCardTabWithAlphabeticalCardDetails(Config testConfig)
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		merHelper.merCCTab = (MerchantCCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);


		//Alphabet card details
		verifyErrors(merHelper, 59, testConfig);
		Assert.assertTrue(testConfig.getTestResult());

	}
	/**
	 * Verify the error message when invalid card details are entered in AMEX card 
	 * @param testConfig
	  
	 */
	@Test(description = "Verify the error messages when invalid card details are entered in Amex Credit Card ", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnCreditCardTabCardDetailsWithAmex(Config testConfig)
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		merHelper.merCCTab = (MerchantCCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);


		//Amex card
		merHelper.merCCTab.chooseCreditCardType(MerCardType.Amex);
		verifyErrors(merHelper, 1, testConfig);
		Assert.assertTrue(testConfig.getTestResult());

	}
	/**
	 * Compares and verify errors when invalid inputs are entered in card details
	 * @param merHelper
	 * @param inputCardDetailsRowNum
	 * @param testConfig
	 
	 */
	private void verifyErrors(TransactionMerchantHelper merHelper, int inputCardDetailsRowNum, Config testConfig)
	{

	    merHelper.cardDetailsData.GetData(inputCardDetailsRowNum, "Comments");
		merHelper.merCCTab.FillCardDetails(inputCardDetailsRowNum);
		merHelper.merCCTab.clickPayNowToGetError();
		String txt = Popup.text(testConfig);
		if (inputCardDetailsRowNum==5){

			Helper.compareEquals(testConfig, "Name with spaces filled", "CVV validation failed", txt);
		}
		else if(inputCardDetailsRowNum==1){
			Helper.compareEquals(testConfig, "cvv validation ", "CVV validation failed", txt);
		}
		else {
			Helper.compareEquals(testConfig, "card validation error message", "Card validation failed", txt);
		}

	}



	@Test(description = "Verify credit card transaction with preInitMode = 0 and preInitAllowed = 1", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_merchantTransaction_without_preInitMode(Config testConfig) 
	{	

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//Master card
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		testConfig.putRunTimeProperty("preInitMode", "0");
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	


	}

	@Test(description = "Verify incomplete transaction with preInitMode = 1 and preInitAllowed = 1", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_incompleteTransaction_with_preInitMode(Config testConfig) 
	{	

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//Master card
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		//merHelper.paymentOptions = (PaymentOptionsPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		merHelper.payment = (PaymentOptionsPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Element.click(testConfig, Element.getPageElement(testConfig, How.id, "payment1"), "Iframe");
		Browser.wait(testConfig, 10);
		Element.verifyElementPresent(testConfig, Element.getPageElement(testConfig, How.id, "payment1"), "Payment Button");

		Assert.assertTrue(testConfig.getTestResult());	


	}

	
	@Test(description = "Verify credit card transaction using preinitiated flow with ajax calls", 
			dataProvider="GetTwoBrowserTestConfig", timeOut=600000, groups="1")
	public void test_preinit_cc(Config testConfig, Config secondaryConfig) 
	{	
		
		// merHelper: Test Merchant transaction page. Actual Transaction is done here.
		// tranHelper: Test transaction page. used to make a second S2S call to payu.
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		TransactionHelper tranHelper = new TransactionHelper(secondaryConfig, false); 

		//admin login
		merHelper.DoLogin();
		merHelper.GetTestMerchantTransactionPage();


		//Master card
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		
		//Fill Transaction details, Card details and click on submit on the merHelper.
		merHelper.payment = (PaymentOptionsPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);


		
		tranHelper.DoLogin();
		tranHelper.GetTestTransactionPage();

		//Fill Transaction details, click on submit on the tranHelper.
		tranHelper.payment = (PaymentOptionsPage)tranHelper.updatePreinitiatedTransaction(transactionRowNum, testConfig, secondaryConfig);

		
		//Automatically update the Transaction on merHelper and receive the test Response Page. 
		merHelper.testResponse = merHelper.autoGetResponse(testConfig);
		
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		
		secondaryConfig.closeBrowser();
		
		Assert.assertTrue(testConfig.getTestResult());	


	}


}
