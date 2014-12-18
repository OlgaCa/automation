package Test.AdminPanel.TestMerchantTransaction;

import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantDCTab;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage;
import PageObject.MerchantPanel.Transactions.TransactionsPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage.SearchOn;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.Helper;
import Utils.Popup;
import Utils.TestBase;

public class TestMerchantTransactionDC extends TestBase  {

	DashboardPage dashBoard;

	@Test(description = "Verify debit card transaction through CITI" , 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CITI(Config testConfig) 
	{

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//Master card CITI Debit Card
		int transactionRowNum = 2;
		int paymentTypeRowNum = 18;
		int cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());


		//Visa Debit Card Citi Card
		transactionRowNum = 2;
		paymentTypeRowNum = 36;
		cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	

	}

	@Test(description = "Verify debit card transaction through HDFC2 gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_HDFC2(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//MasterCard HDFC Debit Cards
		int transactionRowNum = 3;
		int paymentTypeRowNum = 19;
		int cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	//TODO: after convenience fee is deployed
	@Test(description = "Verify debit card transaction through HDFC2 master gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_HDFC_CF(Config testConfig)
	{	

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//Debit card
		int transactionRowNum = 70;
		int paymentTypeRowNum = 19;
		int cardDetailsRowNum = 1;
		int requestDetailRowNum = 1;
		int PGinfoRowNum = 15;
		merHelper.GetTestMerchantTransactionPage();
		//testConfig.putRunTimeProperty("additionalCharges", "DC:2");
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
		/*TransactionFilterPage filter= new TransactionFilterPage(testConfig);
		filter.SearchTransaction(testConfig.getRunTimeProperty("transactionId"), SearchOn.TransactionId);
		transactionsPage.VerifyTransaction(requestDetailRowNum,paymentTypeRowNum,transactionRowNum,PGinfoRowNum,transactionAmount,merHelper);
*/
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(merHelper.transactionData,transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum, merHelper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	

	}

	@Test(description = "Verify debit card transaction through IOnternational Transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_InternationalTransaction(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//MasterCard AXIS Debit Cards
		int transactionRowNum = 1;
		int paymentTypeRowNum = 17;
		int cardDetailsRowNum = 20;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.tryAgainPage = (TryAgainPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
		merHelper.tryAgainPage.verifyRetryForPageElements();
		Helper.compareEquals(testConfig, "Reason of Failure ", "Reason for failure: Bank denied transaction on the card.", merHelper.tryAgainPage.getReasonForInternationalFailure());
		Assert.assertTrue(testConfig.getTestResult());	
	}
	// Not working because of bug. Mode is CC instead of DC.
	@Test(description = "Verify debit card transaction through International card for disallowed merchant", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_InternationalTransactionsFailure(Config testConfig) 
	{	

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//Master card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 238;
		int cardDetailsRowNum = 20;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	

	}
	@Test(description = "Verify debit card transaction through AXIS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_AXIS(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 1;
		int paymentTypeRowNum = 17;
		int cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);


		//Visa Debit Cards
		transactionRowNum = 1;
		paymentTypeRowNum = 35;
		cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}
	@Test(description = "Verify debit card transaction through CITI" , 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_CITI(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("UseStoredDetails", "0");

		Assert.assertTrue(testConfig.getTestResult());	
	}

	/**
	 * Verify the error message when special characters are entered in card details
	 * @param testConfig

	 */
	@Test(description = "Verify the error messages when special characters are entered in Debit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnDebitCardTabWithSpecialCharacters(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();
		int transactionRowNum = 1;
		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("Visa Debit Cards (All Banks)");

		//Special char input 
		verifyErrors(merHelper, 4, testConfig);

		Assert.assertTrue(testConfig.getTestResult());


	}
	/**
	 * Verify the error message when inputs with spaces are entered in card details
	 * @param testConfig

	 */
	@Test(description = "Verify the error messages when input with spaces are entered in Debit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnDebitCardTabWithSpaces(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();
		int transactionRowNum = 1;
		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("Visa Debit Cards (All Banks)");
		//Input with spaces
		verifyErrors(merHelper, 5, testConfig);
		Assert.assertTrue(testConfig.getTestResult());

	}
	/**
	 * Verify the error message when inputs with spaces only are entered in card details
	 * @param testConfig

	 */
	@Test(description = "Verify the error messages when spaces only are entered in Debit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnDebitCardTabWithSpacesOnly(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();
		int transactionRowNum = 1;
		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("Visa Debit Cards (All Banks)");
		//Spaces Only
		verifyErrors(merHelper, 6,testConfig);
		Assert.assertTrue(testConfig.getTestResult());
	}
	/**
	 * Verify the error message when wrong details are entered in card details
	 * @param testConfig

	 */
	@Test(description = "Verify the error messages when wrong details are entered in Debit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnDebitCardTabWithWrongDetails(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();
		int transactionRowNum = 1;
		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("Visa Debit Cards (All Banks)");
		//Wrong details
		verifyErrors(merHelper, 61, testConfig);
		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verify the error message when only alphabets are entered in card details
	 * @param testConfig

	 */
	@Test(description = "Verify the error messages when alphabetical card details are entered in Debit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnDebitCardTabWithAlphabetCardDetails(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();
		int transactionRowNum = 1;
		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("Visa Debit Cards (All Banks)");
		//Alphabet card details
		verifyErrors(merHelper, 59, testConfig);
		Assert.assertTrue(testConfig.getTestResult());
	}
	/**
	 * Verify the error message when blank inputs are entered in card details
	 * @param testConfig

	 */
	@Test(description = "Verify the error messages when blank inputs are entered in Debit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnDebitCardTabWithBlankInputs(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();
		int transactionRowNum = 1;
		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("Visa Debit Cards (All Banks)");
		//Blank Input
		verifyErrors(merHelper, 60, testConfig);
		Assert.assertTrue(testConfig.getTestResult());
	}
	/**
	 * verify the error message when expiry card details are filled in SBI Maestro card
	 * @param testConfig
	 */
	@Test(description = "Verify the error messages when expiry card details are entered inSBI Maestro", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnDebitCardTabWithExpiryDetails(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();
		//Check for Maestro only Expiry is mandatory (CVV is optional)
		int transactionRowNum = 6;
		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("State Bank Maestro Cards");
		//Enter No CVV Expiry card details
		verifyErrors(merHelper, 3, testConfig);
		Assert.assertTrue(testConfig.getTestResult());
	}
	/**
	 * verify error messages when invalid inputs are entered in debit card details
	 * @param merHelper
	 * @param inputCardDetailsRowNum
	 * @param testConfig
	 */
	private void verifyErrors(TransactionMerchantHelper merHelper, int inputCardDetailsRowNum, Config testConfig)
	{
		merHelper.cardDetailsData.GetData(inputCardDetailsRowNum, "Comments");
		merHelper.merDCTab.FillDebitCardDetails(inputCardDetailsRowNum);
		merHelper.merDCTab.clickPayNowToGetError();
		String txt = Popup.text(testConfig);
		if (inputCardDetailsRowNum==5){

			Helper.compareEquals(testConfig, "Name with spaces", "CVV validation failed", txt);
		}
		else if(inputCardDetailsRowNum==3)
		{
			Helper.compareEquals(testConfig, "Card expiry details filled", "Card expiry validation failed", txt);
		}
		else{ 
			Helper.compareEquals(testConfig, "card validation error message", "Card validation failed", txt);
		}


	}
}