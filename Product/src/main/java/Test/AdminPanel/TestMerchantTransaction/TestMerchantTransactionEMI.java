package Test.AdminPanel.TestMerchantTransaction;

import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantEMITab;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.Helper;
import Utils.Popup;
import Utils.TestBase;

public class TestMerchantTransactionEMI extends TestBase {
	
	DashboardPage dashBoard;

	@Test(description = "Verify URL for emi banking transaction for HDFC2 gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_HDFC2(Config testConfig) 
	{	
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//3 months
		int transactionRowNum = 3;
		int paymentTypeRowNum = 43;
		int cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);

		//6 months
		transactionRowNum = 3;
		paymentTypeRowNum = 46;
		cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());				
	}
	
	@Test(description = "Verify emi transaction through HDFC2 master gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_HDFC_CF(Config testConfig)
	{	

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//6 months
		int transactionRowNum = 3;
		int paymentTypeRowNum = 46;
		int cardDetailsRowNum = 1;
		merHelper.GetTestMerchantTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "EMI:2");
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

	@Test(description = "Verify URL for emi banking transaction for CITI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_CITI(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//3 months
		int transactionRowNum = 2;
		int paymentTypeRowNum = 42;
		int cardDetailsRowNum = 16;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.bankRedirectPage = (BankRedirectPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		merHelper.bankRedirectPage.VerifyRedirectedURL(merHelper.paymentTypeData, paymentTypeRowNum);

		//6 months
		transactionRowNum = 2;
		paymentTypeRowNum = 45;
		cardDetailsRowNum = 16;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.bankRedirectPage = (BankRedirectPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		merHelper.bankRedirectPage.VerifyRedirectedURL(merHelper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());				
	}
	/**
	 * Verify the error messages when special characters are entered in EMI Tab
	 */
	@Test(description = "Verify the error messages when Special characters are entered in EMI Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnEMITab(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 42;
		merHelper.merEMITab = (MerchantEMITab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		merHelper.merEMITab.SelectMerchantBankAndDuration(paymentTypeRowNum);

		//Special char input 
		verifyErrors(merHelper, 4, testConfig);

		Assert.assertTrue(testConfig.getTestResult());

	}
	/**
	 * Verify the error messages when input with spaces are entered in EMI Tab
	 */
	@Test(description = "Verify the error messages when input with spaces are entered in EMI Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnEMITabWithSpaces(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 42;
		merHelper.merEMITab = (MerchantEMITab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		merHelper.merEMITab.SelectMerchantBankAndDuration(paymentTypeRowNum);
		//Input with spaces
		verifyErrors(merHelper, 5, testConfig);
		Assert.assertTrue(testConfig.getTestResult());

	}
	/**
	 * Verify the error messages when input with spaces only are entered in EMI Tab
	 */
	@Test(description = "Verify the error messages when input with spaces only are entered in EMI Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnEMITabWithSpacesOnly(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 42;
		merHelper.merEMITab = (MerchantEMITab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		merHelper.merEMITab.SelectMerchantBankAndDuration(paymentTypeRowNum);
		//Spaces Only
		verifyErrors(merHelper, 6, testConfig);

		Assert.assertTrue(testConfig.getTestResult());

	}
	/**
	 * Verify the error messages when blank inputs are entered in EMI Tab
	 */
	@Test(description = "Verify the error messages when blank inputs are entered in EMI Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnEMITabWithBlankInput(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 42;
		merHelper.merEMITab = (MerchantEMITab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		merHelper.merEMITab.SelectMerchantBankAndDuration(paymentTypeRowNum);
		//Blank Input
		verifyErrors(merHelper, 60, testConfig);


		Assert.assertTrue(testConfig.getTestResult());

	}
	/**
	 * Verify the error messages when wrong details are entered in EMI Tab
	 */
	@Test(description = "Verify the error messages when wrong details are entered in EMI Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnEMITabWithWrongDetails(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 42;
		merHelper.merEMITab = (MerchantEMITab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		merHelper.merEMITab.SelectMerchantBankAndDuration(paymentTypeRowNum);
		//Wrong details
		verifyErrors(merHelper, 61,testConfig);


		Assert.assertTrue(testConfig.getTestResult());

	}
	/**
	 * Verify the error messages when alphabetical card details are entered in EMI Tab
	 */
	@Test(description = "Verify the error messages when alphabetical card details are entered in EMI Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnEMITabWithAlphabeticalDetails(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 42;
		merHelper.merEMITab = (MerchantEMITab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		merHelper.merEMITab.SelectMerchantBankAndDuration(paymentTypeRowNum);
		//Alphabet card details
		verifyErrors(merHelper, 59,testConfig);

		Assert.assertTrue(testConfig.getTestResult());

	}
	/**
	 * verify error messages when invalid inputs are entered in EMI card details
	 * @param merHelper
	 * @param inputCardDetailsRowNum
	 * @param testConfig
	 */
	private void verifyErrors(TransactionMerchantHelper merHelper, int inputCardDetailsRowNum,Config testConfig)
	{
		merHelper.cardDetailsData.GetData(inputCardDetailsRowNum, "Comments");
		merHelper.merEMITab.FillEMICardDetails(inputCardDetailsRowNum);
		merHelper.merEMITab.clickPayNowToGetError();
		String txt = Popup.text(testConfig);
		if (inputCardDetailsRowNum==5){

			Helper.compareEquals(testConfig, "Name with spaces filled", "CVV validation failed", txt);
		}
		else {
			Helper.compareEquals(testConfig, "card validation error message", "Card validation failed", txt);
		}

	}


}
