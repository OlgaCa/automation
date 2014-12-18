package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;


import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.EnforceWrongPage;

import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;

public class TestDropCategory extends TestBase{

	@Test(description = "Verify avaliable payment tab after droping all paymnet types other than creditcard option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_dropCC(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 92;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyCCTabPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify avaliable payment tab after droping all paymnet types other than debitcard option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_dropDC(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 93;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyDCTabPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify avaliable payment tab after droping all paymnet types other than netbanking option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_dropNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 94;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyNBDCPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify avaliable payment tab after droping all paymnet types other than emi option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_dropEMI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 95;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyEMITabPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify avaliable payment tab after droping all paymnet types other than cashcard option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_dropCashcard(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 96;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyCashcardTabPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify avaliable payment tab after droping all paymnet types other than COD option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_dropCOD(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 97;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyCODTabPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify avaliable payment tab after droping all paymnet types other than DC options", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_dropDCoptions(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 98;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifymultipleDC_option();
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify response of drop category with seamless transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_dropSeamless(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 93;
		int paymentTypeRow=3;
		int cardDetailsRow=1;
		
		
		testConfig.putRunTimeProperty("WrongEnforceMessage","1");
		helper.GetTestTransactionPage();
				      
		helper.DoTestTransaction(transactionRowNum, paymentTypeRow, cardDetailsRow,ExpectedResponsePage.TestResponsePage);
		
		EnforceWrongPage enforcePay=new EnforceWrongPage(testConfig);
		enforcePay.verifyMessage();
		Assert.assertTrue(testConfig.getTestResult());
	}	
	
	@Test(description = "Verify avaliable payment tab after droping all paymnet types other than Cash card options", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_dropCashoptions(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 99;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifymultipleCash_option();
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify avaliable payment tab after droping all paymnet types other than EMI options", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_dropEMIoptions(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 100;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifymultipleEMI_option();
		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify avaliable payment tab after droping and enforcing creditcard option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_dropEnforceCC(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 91;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);
		// drop category will be ignored if enforce category present
		helper.payment.verifyCCTabPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}
}