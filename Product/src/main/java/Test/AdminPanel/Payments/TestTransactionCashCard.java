package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.CashCardTab;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestTransactionCashCard extends TestBase{
	DashboardPage dashBoard;
	
	@Test(description="Verify cash card transaction through DONE cash card", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CashCard_DONE(Config testConfig) 
	{	
		//import org.openqa.selenium.WebDriver;
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//DONE
		int transactionRowNum = 3;
		int paymentTypeRowNum = 331;
		int cardDetailsRowNum = -1;
		testConfig.putRunTimeProperty("popup", "true");
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		//System.out.println("RAHUL"+helper.bankRedirectPage+"KUMAR");
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
        //System.out.println(testConfig.driver.getCurrentUrl());
		Assert.assertTrue(testConfig.getTestResult());
	}
	

	@Test(description="Verify cash card transaction through AIRTELMONEY gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CashCard_AIRTELMONEY(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//AMON
		int transactionRowNum = 1;
		int paymentTypeRowNum = 52;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify cash card transaction through AIRTELMONEY gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CashCard_AIRTELMONEY_CF(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//AMON
		int transactionRowNum = 68;
		int paymentTypeRowNum = 52;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();

		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[5];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify cash card transaction through CITI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CashCard_CITI(Config testConfig) 
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CPMC
		int transactionRowNum = 2;
		int paymentTypeRowNum = 53;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify cash card transaction through CITI gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CashCard_CITI_CF(Config testConfig) 
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CPMC
		int transactionRowNum = 69;
		int paymentTypeRowNum = 53;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();

		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[5];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Verify on Dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify cash card transaction through ItzCash gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CashCard_ItzCash(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//ITZC
		int transactionRowNum = 1;
		int paymentTypeRowNum = 54;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify cash card transaction through ItzCash gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CashCard_ItzCash_CF(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//ITZC
		int transactionRowNum = 68;
		int paymentTypeRowNum = 54;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();

		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[5];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify labels on cash card tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CashCardTabPageElements(Config testConfig) 
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		int transactionRowNum = 1;

		CashCardTab ccOption= (CashCardTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.cashcard);

		String expected = helper.cardDetailsData.GetData(1, "CashCard");
		Helper.compareEquals(testConfig, "Cash card Tab text", expected, ccOption.getCashCardTabText());

		expected = helper.cardDetailsData.GetData(10, "CashCard");
		Helper.compareEquals(testConfig, "Cash card Label", expected, ccOption.getCashCardLabel());

		expected = helper.cardDetailsData.GetData(10, "Note");
		Helper.compareEquals(testConfig, "Cash card Note", expected, ccOption.getNoteText());

		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verify the error messages when invalid inputs are entered in Cash Card Tab
	 */
	@Test(description = "Verify the error messages when invalid inputs are entered in Cash Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnCashCardTab(Config testConfig)  
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 53;

		helper.cashCardTab = (CashCardTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.cashcard);

		helper.paymentTypeData = new TestDataReader(testConfig, "PaymentType");
		String cashCardName = helper.paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.cashCardTab.SelectCashCard(cashCardName);

		//Special char input 
		verifyErrors(helper, 4, 
				helper.cardDetailsData.GetData(11, "Name"), helper.cardDetailsData.GetData(13, "CC")
				, helper.cardDetailsData.GetData(11, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));

		//Input with spaces
		verifyErrors(helper, 5, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(11, "CC")
				, helper.cardDetailsData.GetData(14, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));
		
		//Spaces Only
		verifyErrors(helper, 6, 
				helper.cardDetailsData.GetData(11, "Name"), helper.cardDetailsData.GetData(13, "CC")
				, helper.cardDetailsData.GetData(13, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));
		
		//Blank Input
		verifyErrors(helper, 7, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(13, "CC")
				, helper.cardDetailsData.GetData(13, "CVV"), helper.cardDetailsData.GetData(11, "Year"));
		
		//Wrong details
		verifyErrors(helper, 8, 
				helper.cardDetailsData.GetData(12, "Name"), helper.cardDetailsData.GetData(11, "CC")
				, helper.cardDetailsData.GetData(14, "CVV"), helper.cardDetailsData.GetData(12, "Year"));
		
		//Alphabet card details
		verifyErrors(helper, 9, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(13, "CC")
				, helper.cardDetailsData.GetData(11, "CVV"), helper.cardDetailsData.GetData(14, "Mon"));

		Assert.assertTrue(testConfig.getTestResult());
	}

	private void verifyErrors(TransactionHelper helper, int inputCardDetailsRowNum, String cardNameError, String cardNumberError, String cvvError, String expiryError)
	{
		String comments = helper.cardDetailsData.GetData(inputCardDetailsRowNum, "Comments");
		helper.cashCardTab.FillCardDetails(inputCardDetailsRowNum);
		helper.payment.clickPayNowToGetError();

		Helper.compareEquals(helper.testConfig, "Card name '"+ comments +"' error", cardNameError, helper.cashCardTab.getCardNameErrorMessage());

		Helper.compareEquals(helper.testConfig, "Card number '"+ comments +"' error", cardNumberError, helper.cashCardTab.getCardNumberErrorMessage());

		Helper.compareEquals(helper.testConfig, "CVV number '"+ comments +"' error", cvvError, helper.cashCardTab.getCvvNumberErrorMessage());

		Helper.compareEquals(helper.testConfig, "Expiry Date '"+ comments +"' error", expiryError, helper.cashCardTab.getExpiryDateErrorMessage());
	}
}