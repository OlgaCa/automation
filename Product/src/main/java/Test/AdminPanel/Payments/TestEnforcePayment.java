package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.EnforceWrongPage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;

public class TestEnforcePayment extends TestBase{

	@Test(description = "Verify avaliable payment tab after enforcing debitcard option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceDC(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 81;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyDCTabPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify avaliable payment tab after enforcing creditcard option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceCC(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction 
		int transactionRowNum = 91;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyCCTabPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify avaliable payment tab after enforcing netbanking option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 82;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyNBDCPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}	

	@Test(description = "Verify avaliable payment tab after enforcing emi option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceEMI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 83;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyEMITabPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}	

	@Test(description = "Verify avaliable payment tab after enforcing cash card option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceCashcard(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 85;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyCashcardTabPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}	

	@Test(description = "Verify avaliable payment tab after enforcing COD option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceCOD(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction 
		int transactionRowNum = 84;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyCODTabPresent();
		Assert.assertTrue(testConfig.getTestResult());
	}	

	@Test(description = "Verify avaliable payment tab after enforcing CC and DC option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceDC_CCOption(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 86;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifyDC_option();

		Assert.assertTrue(testConfig.getTestResult());
	}	

	@Test(description = "Verify avaliable payment tab after enforcing multiple DC option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceMultipleDCOption(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 87;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifymultipleDC_option();

		Assert.assertTrue(testConfig.getTestResult());
	}	

	@Test(description = "Verify avaliable payment tab after enforcing multiple netbanking option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceMultipleNBOption(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 88;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifymultipleNB_option();

		Assert.assertTrue(testConfig.getTestResult());
	}	

	@Test(description = "Verify avaliable payment tab after enforcing multiple Cash card option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceMultipleCashOption(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 89;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);

		helper.payment.verifymultipleCash_option();

		Assert.assertTrue(testConfig.getTestResult());
	}	

	@Test(description = "Verify avaliable payment tab after enforcing multiple EMI option", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceMultipleEMIOption(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 90;

		helper.GetTestTransactionPage();

		helper.payment=(PaymentOptionsPage) helper.GetPaymentOptionPage(transactionRowNum);
		
		helper.payment.verifymultipleEMI_option();

		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify response of enforced seamless transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_enforceSeamless(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		// Initiate a transaction
		int transactionRowNum = 87;
		int paymentTypeRow=3;
		int cardDetailsRow=1;
		
		
		testConfig.putRunTimeProperty("WrongEnforceMessage","1");
		helper.GetTestTransactionPage();
				      
		helper.DoTestTransaction(transactionRowNum, paymentTypeRow, cardDetailsRow,ExpectedResponsePage.TestResponsePage);
		
		EnforceWrongPage enforcePay=new EnforceWrongPage(testConfig);
		enforcePay.verifyMessage();
		Assert.assertTrue(testConfig.getTestResult());
	}	
}