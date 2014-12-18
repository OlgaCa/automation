package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;


public class TestTransactionSeamlessCashCard extends TestBase{

	@Test(description="Verify seamless cash card transaction through AIRTELMONEY gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_CashCard_AIRTELMONEY(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//AMON
		int transactionRowNum = 1;
		int paymentTypeRowNum = 52;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//AMON with Blank card details
		transactionRowNum = 1;
		paymentTypeRowNum = 52;
		cardDetailsRowNum = 7;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify seamless cash card transaction through CITI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_CashCard_CITI(Config testConfig) 
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, true);
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

	@Test(description="Verify seamless DONE cash card transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_CashCard_DONE(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//DONE
		int transactionRowNum = 3;
		int paymentTypeRowNum = 331;
		int cardDetailsRowNum = -1;
		testConfig.putRunTimeProperty("popup", "true");
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	
	@Test(description="Verify seamless cash card transaction through ItzCash gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_CashCard_ItzCash(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//ITZC
		int transactionRowNum = 1;
		int paymentTypeRowNum = 54;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//ITZC with blank cartd details
		transactionRowNum = 1;
		paymentTypeRowNum = 54;
		cardDetailsRowNum = 7;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description="Verify seamless cash card transaction with wrong Ibibo code", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_CashCard_WrongIbiboCode(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//invalid Ibibo code
		int transactionRowNum = 1;
		int paymentTypeRowNum = 317;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValueCashCardTab().equals("Select Cash Card Type"));

		//Blank Ibibo code
		transactionRowNum = 1;
		paymentTypeRowNum = 318;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValueCashCardTab().equals("Select Cash Card Type"));
	}

	@Test(description="Verify seamless cash card transaction through CITI gateway with blank card Details", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_CashCard_CITI_BlankCardDetails(Config testConfig) 
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Blank Card Number
		int transactionRowNum = 2;
		int paymentTypeRowNum = 53;
		int cardDetailsRowNum = 43;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValueCashCardTab().equals("Select Cash Card Type"));

		//Blank Name
		transactionRowNum = 2;
		paymentTypeRowNum = 53;
		cardDetailsRowNum = 44;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValueCashCardTab().equals("Select Cash Card Type"));

		//Blank CVV
		transactionRowNum = 2;
		paymentTypeRowNum = 53;
		cardDetailsRowNum = 45;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValueCashCardTab().equals("Select Cash Card Type"));

		//Blank Exp Date
		transactionRowNum = 2;
		paymentTypeRowNum = 53;
		cardDetailsRowNum = 46;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValueCashCardTab().equals("Select Cash Card Type"));

		// Blank Exp Year
		transactionRowNum = 2;
		paymentTypeRowNum = 53;
		cardDetailsRowNum = 47;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValueCashCardTab().equals("Select Cash Card Type"));

		//Special Characters
		transactionRowNum = 2;
		paymentTypeRowNum = 53;
		cardDetailsRowNum = 7;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValueCashCardTab().equals("Select Cash Card Type"));
	}

}

