package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;


import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;

public class TestTransactionSeamlessDC extends TestBase{

	//Valid test cases using CBK Gateway

	@Test(description = "Verify seamless debit card transaction through CBK gateway", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_DC_CBK(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//BOI Debit Card
		int transactionRowNum = 1;
		int paymentTypeRowNum = 10;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());	
	}


	//valid test cases using CCAVENUE gateway

	@Test(description = "Verify seamless debit card transaction through CCAVENUE", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_DC_CCAVENUE(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Canara Bank Debit Card
		int transactionRowNum = 2;
		int paymentTypeRowNum = 11;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());	
	}

	//valid test cases using SBIPG gateway

	@Test(description = "Verify seamless debit card transaction through SBIPG", 
			dataProvider="GetTestConfig",timeOut=600000, groups="2")
	public void test_DC_SBIPG(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//SBI Maestro Card
		int transactionRowNum = 5;
		int	paymentTypeRowNum = 338;
		int	cardDetailsRowNum = 62;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	//valid test cases using AXIS gateway

	@Test(description = "Verify seamless debit card transaction through AXIS gateway", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_DC_AXIS(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Visa Debit Cards
		int transactionRowNum = 1;
		int paymentTypeRowNum = 35;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	//valid test cases using HDFC2 gateway

	@Test(description = "Verify seamless debit card transaction through HDFC2 gateway", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_DC_HDFC2(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 3;
		int paymentTypeRowNum = 19;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	//valid test cases using ICICITRAVEL gateway

	@Test(description = "Verify seamless debit card transaction through ICICITRAVEL gateway", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_DC_ICICITRAVEL(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Master Card
		int transactionRowNum = 6;
		int paymentTypeRowNum = 340;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	//valid test cases using TECHPROCESS gateway
	@Test(description = "Verify seamless debit card transaction through TECHPROCESS gateway", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_DC_TECHPROCESS(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Other Maestro Card
		int transactionRowNum = 7;
		int paymentTypeRowNum = 339;
		int cardDetailsRowNum = 62;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify seamless transactions using AXIS gateway with invalid card name"
			,dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_Invalid_cardName_DC(Config testConfig) throws InterruptedException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//AXIS Gateway
		//special characters in card name
		int transactionRowNum = 64;
		int paymentTypeRowNum = 307;
		int cardDetailsRowNum = 36;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.logFail("**Redmine ID for this bug is 21445**");

		//numbers in card name
		transactionRowNum = 64;
		paymentTypeRowNum = 307;
		cardDetailsRowNum = 37;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.logFail("**Redmine ID for this bug is 21445**");

		//HDFC gateway
		//special characters in card name
		transactionRowNum = 3;
		paymentTypeRowNum = 272;
		cardDetailsRowNum = 36;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//numbers in card name
		transactionRowNum = 3;
		paymentTypeRowNum = 272;
		cardDetailsRowNum = 37;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.logFail("**Redmine ID for this bug is 21445**");


		Assert.assertTrue(testConfig.getTestResult());

	}	

	@Test(description = "Verify seamless transactions using AXIS gateway with invalid Expiry month", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_Invalid_CCEXPDate_DC(Config testConfig) throws InterruptedException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//00 in EXPMON field
		int transactionRowNum = 64;
		int paymentTypeRowNum = 308;	
		int cardDetailsRowNum = 49;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Special Characters in EXPMON field
		transactionRowNum = 64;
		paymentTypeRowNum = 308;	
		cardDetailsRowNum = 50;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Alphabets in EXPMON field
		transactionRowNum = 64;
		paymentTypeRowNum = 308;	
		cardDetailsRowNum = 51;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//With HDFC Gateway
		//00 in EXPMON field
		transactionRowNum = 3;
		paymentTypeRowNum = 286;	
		cardDetailsRowNum = 49;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Special Characters in EXPMON field
		transactionRowNum = 3;
		paymentTypeRowNum = 287;	
		cardDetailsRowNum = 50;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Alphabets in EXPMON field
		transactionRowNum = 3;
		paymentTypeRowNum = 287;	
		cardDetailsRowNum = 51;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify seamless transactions using AXIS gateway with combinations of PG code and Ibibo codes", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_InvalidIbiboCode_DC(Config testConfig) throws InterruptedException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Special characters in ibibo code
		int transactionRowNum = 1;
		int paymentTypeRowNum = 280;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyErrorMessage().equals("Note: Selected payment option is not available at this moment. Kindly try a different payment option or try again after some time."));
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Select Debit Card Type"));


		//Numbers in ibibo code
		transactionRowNum = 64;
		paymentTypeRowNum = 281;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyErrorMessage().equals("Note: Selected payment option is not available at this moment. Kindly try a different payment option or try again after some time."));
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Select Debit Card Type"));

		//valid but not related ibibocode
		transactionRowNum = 64;
		paymentTypeRowNum = 282;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyErrorMessage().equals("Note: Selected payment option is not available at this moment. Kindly try a different payment option or try again after some time."));
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Select Debit Card Type"));

		//Blank characters in Ibibo code
		transactionRowNum = 64;
		paymentTypeRowNum = 289;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);

		//Invalid code combinations
		//PG:CC, ibiboCode:MAST
		transactionRowNum = 1;
		paymentTypeRowNum = 299;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Instruction Message" ,"Choose a Payment Method", helper.payment.verifyErrorMessageChoosePaymentMethod());
		testConfig.logFail("**Redmine ID for this bug is 19391**");

		//PG:DC, ibiboCode:AMEX
		transactionRowNum = 1;
		paymentTypeRowNum = 300;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Instruction Message" ,"Choose a Payment Method", helper.payment.verifyErrorMessageChoosePaymentMethod());
		testConfig.logFail("**Redmine ID for this bug is 19391**");

		//With HDFC gateway
		//Special characters in ibibo code
		transactionRowNum = 3;
		paymentTypeRowNum = 283;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyErrorMessage().equals("Note: Selected payment option is not available at this moment. Kindly try a different payment option or try again after some time."));
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Select Debit Card Type"));

		//Numbers in ibibo code
		transactionRowNum = 3;
		paymentTypeRowNum = 284;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyErrorMessage().equals("Note: Selected payment option is not available at this moment. Kindly try a different payment option or try again after some time."));
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Select Debit Card Type"));

		//Non existing value in ibibo code
		transactionRowNum = 3;
		paymentTypeRowNum = 285;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyErrorMessage().equals("Note: Selected payment option is not available at this moment. Kindly try a different payment option or try again after some time."));
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Select Debit Card Type"));

		//Blank characters in Ibibo code
		transactionRowNum = 3;
		paymentTypeRowNum = 290;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Select Debit Card Type"));

		//Invalid code combinations
		//PG:CC, ibiboCode:MAST
		transactionRowNum = 3;
		paymentTypeRowNum = 301;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Instruction Message" ,"Choose a Payment Method", helper.payment.verifyErrorMessageChoosePaymentMethod());
		testConfig.logFail("**Redmine ID for this bug is 19391**");

		//PG:DC, ibiboCode:AMEX
		transactionRowNum = 3;
		paymentTypeRowNum = 302;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Instruction Message" ,"Choose a Payment Method", helper.payment.verifyErrorMessageChoosePaymentMethod());
		testConfig.logFail("**Redmine ID for this bug is 19391**");
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Select Debit Card Type"));
	}	

	@Test(description = "Verify seamless transactions using AXIS gateway with invalid PG code", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_invalidPGCode_DC_AXIS_RetryCountZero(Config testConfig) throws InterruptedException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Special characters in PG code
		int transactionRowNum = 64;
		int paymentTypeRowNum = 293;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Instruction Message" ,"Choose a Payment Method", helper.payment.verifyErrorMessageChoosePaymentMethod());

		//Numbers in PG code
		transactionRowNum = 64;
		paymentTypeRowNum = 294;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Instruction Message" ,"Choose a Payment Method", helper.payment.verifyErrorMessageChoosePaymentMethod());

		//invalid characters in PG code
		transactionRowNum = 64;
		paymentTypeRowNum = 295;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Instruction Message" ,"Choose a Payment Method", helper.payment.verifyErrorMessageChoosePaymentMethod());


		//Blank characters in PG code
		transactionRowNum = 64;
		paymentTypeRowNum = 291;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyErrorMessageChoosePaymentMethod().equals("Choose a Payment Method"));

		//With HDFC Gateway
		//Special characters in PG code
		transactionRowNum = 3;
		paymentTypeRowNum = 296;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Instruction Message" ,"Choose a Payment Method", helper.payment.verifyErrorMessageChoosePaymentMethod());

		//Numbers in PG code
		transactionRowNum = 3;
		paymentTypeRowNum = 297;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Instruction Message" ,"Choose a Payment Method", helper.payment.verifyErrorMessageChoosePaymentMethod());

		//invalid characters in PG code
		transactionRowNum = 3;
		paymentTypeRowNum = 298;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Instruction Message" ,"Choose a Payment Method", helper.payment.verifyErrorMessageChoosePaymentMethod());

		//Blank characters in PG code
		transactionRowNum = 3;
		paymentTypeRowNum = 292;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyErrorMessageChoosePaymentMethod().equals("Choose a Payment Method"));


		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify seamless transactions using AXIS gateway with invalid card number"
			,dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_Invalid_cardNumber_DC(Config testConfig) throws InterruptedException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//00000000000000000 in card number
		int transactionRowNum = 64;
		int paymentTypeRowNum = 310;
		int cardDetailsRowNum = 32;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		testConfig.logFail("**Try again page is not displayed. Redmine ID for this bug is 20864**");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Negative number in card number
		transactionRowNum = 64;
		paymentTypeRowNum = 309;
		cardDetailsRowNum = 34;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		testConfig.logFail("**Redmine ID for this bug is 21444**");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Less than 16 digits in card number
		transactionRowNum = 64;
		paymentTypeRowNum = 311;
		cardDetailsRowNum = 35;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.logFail("**Redmine ID for this bug is 18089**");

		//With HDFC Gateway
		//special characters in card number
		transactionRowNum = 3;
		paymentTypeRowNum = 275;
		cardDetailsRowNum = 31;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Alphabets in card number
		transactionRowNum = 3;
		paymentTypeRowNum = 275;
		cardDetailsRowNum = 33;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//00000000000000000 in card number
		transactionRowNum = 3;
		paymentTypeRowNum = 276;
		cardDetailsRowNum = 32;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		testConfig.logFail("**Redmine ID for this bug is 20864**");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Negative number in card number
		transactionRowNum = 3;
		paymentTypeRowNum = 277;
		cardDetailsRowNum = 34;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		testConfig.logFail("**Redmine ID for this bug is 21444**");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Less than 16 digits in card number
		transactionRowNum = 3;
		paymentTypeRowNum = 278;
		cardDetailsRowNum = 35;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.logFail("**Redmine ID for this bug is 18089**");



		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify seamless transactions using AXIS gateway with invalid CVV number", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_Invalid_CVVNumber_DC(Config testConfig) throws InterruptedException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//000 in CVV number
		int transactionRowNum = 64;
		int paymentTypeRowNum = 310;
		int cardDetailsRowNum = 39;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Negative number in CVV number
		transactionRowNum = 64;
		paymentTypeRowNum = 312;
		cardDetailsRowNum = 41;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Less than 3 digits in CVV number
		transactionRowNum = 64;
		paymentTypeRowNum = 312;
		cardDetailsRowNum = 42;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.logFail("**Redmine ID for this bug is 21446**");

		//With HDFC Gateway
		//special characters in CVV number
		transactionRowNum = 3;
		paymentTypeRowNum = 279;
		cardDetailsRowNum = 38;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Alphabets in CVV number
		transactionRowNum = 3;
		paymentTypeRowNum = 279;
		cardDetailsRowNum = 40;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//000 in CVV number
		transactionRowNum = 3;
		paymentTypeRowNum = 273;
		cardDetailsRowNum = 39;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Negative number in CVV number
		transactionRowNum = 3;
		paymentTypeRowNum = 274;
		cardDetailsRowNum = 41;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Less than 3 digits in CVV number
		transactionRowNum = 3;
		paymentTypeRowNum = 272;
		cardDetailsRowNum = 42;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.logFail("**Redmine ID for this bug is 21446**");

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify seamless transactions using AXIS gateway with blank fields of MAST card", 

			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_Seamless_MAST_AXISGateway_Blank_fields(Config testConfig) throws InterruptedException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//blank card number
		int transactionRowNum = 64;
		int paymentTypeRowNum = 309;
		int cardDetailsRowNum = 43;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("MasterCard Debit Cards (All Banks)"));

		//blank card name
		transactionRowNum = 64;
		paymentTypeRowNum = 309;
		cardDetailsRowNum = 44;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("MasterCard Debit Cards (All Banks)"));

		//blank cvv number
		transactionRowNum = 64;
		paymentTypeRowNum = 309;
		cardDetailsRowNum = 45;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("MasterCard Debit Cards (All Banks)"));

		//blank expiry date
		transactionRowNum = 64;
		paymentTypeRowNum = 309;
		cardDetailsRowNum = 46;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("MasterCard Debit Cards (All Banks)"));

		//blank expiry year
		transactionRowNum = 64;
		paymentTypeRowNum = 309;
		cardDetailsRowNum = 47;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("MasterCard Debit Cards (All Banks)"));

		//blank all fields
		transactionRowNum = 64;
		paymentTypeRowNum = 309;
		cardDetailsRowNum = 7;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("MasterCard Debit Cards (All Banks)"));

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify seamless transactions using HDFC gateway with blank fields of VISA card", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_Seamless_VISA_HDFCGateway_Blank_fields(Config testConfig) throws InterruptedException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//blank card number
		int transactionRowNum = 3;
		int paymentTypeRowNum = 37;
		int cardDetailsRowNum = 43;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Visa Debit Cards (All Banks)"));

		//blank card name
		transactionRowNum = 3;
		paymentTypeRowNum = 37;
		cardDetailsRowNum = 44;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Visa Debit Cards (All Banks)"));

		//blank cvv number
		transactionRowNum = 3;
		paymentTypeRowNum = 37;
		cardDetailsRowNum = 45;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Visa Debit Cards (All Banks)"));

		//blank expiry date
		transactionRowNum = 3;
		paymentTypeRowNum = 37;
		cardDetailsRowNum = 46;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Visa Debit Cards (All Banks)"));

		//blank expiry year
		transactionRowNum = 3;
		paymentTypeRowNum = 37;
		cardDetailsRowNum = 47;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Visa Debit Cards (All Banks)"));

		//blank all fields
		transactionRowNum = 3;
		paymentTypeRowNum = 37;
		cardDetailsRowNum = 7;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Visa Debit Cards (All Banks)"));
	}

	@Test(description = "Verify seamless transactions using CITI gateway with blank fields of CITD card", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_Seamless_CITD_CITIGateway_Blank_fields(Config testConfig) throws InterruptedException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//blank card number
		int transactionRowNum = 2;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 43;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("CITI Debit Card"));

		//blank card name
		transactionRowNum = 2;
		paymentTypeRowNum = 12;
		cardDetailsRowNum = 44;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("CITI Debit Card"));

		//blank cvv number
		transactionRowNum = 2;
		paymentTypeRowNum = 12;
		cardDetailsRowNum = 45;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("CITI Debit Card"));

		//blank expiry date
		transactionRowNum = 2;
		paymentTypeRowNum = 12;
		cardDetailsRowNum = 46;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("CITI Debit Card"));

		//blank expiry year
		transactionRowNum = 2;
		paymentTypeRowNum = 12;
		cardDetailsRowNum = 47;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("CITI Debit Card"));

		//blank all fields
		transactionRowNum = 2;
		paymentTypeRowNum = 12;
		cardDetailsRowNum = 7;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("CITI Debit Card"));
	}

	@Test(description = "Verify seamless transactions using HDFC gateway with blank fields of maestro card", 
			dataProvider="GetTestConfig",timeOut=600000, groups="1")
	public void test_Seamless_MAES_HDFCGateway_Blank_fields(Config testConfig) throws InterruptedException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//blank card number
		int transactionRowNum = 63;
		int paymentTypeRowNum = 320;
		int cardDetailsRowNum = 43;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Other Maestro Cards"));

		//blank card name
		transactionRowNum = 63;
		paymentTypeRowNum = 320;
		cardDetailsRowNum = 44;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Other Maestro Cards"));

		//blank cvv number
		transactionRowNum = 63;
		paymentTypeRowNum = 320;
		cardDetailsRowNum = 45;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//blank expiry date
		transactionRowNum = 63;
		paymentTypeRowNum = 320;
		cardDetailsRowNum = 46;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//blank expiry year
		transactionRowNum = 63;
		paymentTypeRowNum = 320;
		cardDetailsRowNum = 47;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//blank all fields
		transactionRowNum = 63;
		paymentTypeRowNum = 320;
		cardDetailsRowNum = 7;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("Other Maestro Cards"));

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify seamless transactions using SBI gateway with blank fields of SBI maestro card", 
			dataProvider="GetTestConfig",timeOut=700000, groups="1")
	public void test_Seamless_SMAE_SBIGateway_Blank_fields(Config testConfig) throws InterruptedException
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//blank card number
		int transactionRowNum = 5;
		int paymentTypeRowNum = 31;
		int cardDetailsRowNum = 43;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		helper.payment.VerifyOptionlFieldsFunctionalityForSMAE();
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("SBI Maestro Card"));

		//blank card name
		transactionRowNum = 5;
		paymentTypeRowNum = 31;
		cardDetailsRowNum = 44;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		helper.payment.VerifyOptionlFieldsFunctionalityForSMAE();
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("SBI Maestro Card"));

		//blank cvv number
		transactionRowNum = 5;
		paymentTypeRowNum = 322;
		cardDetailsRowNum = 45;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//blank expiry date
		transactionRowNum = 5;
		paymentTypeRowNum = 321;
		cardDetailsRowNum = 46;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//blank expiry year
		transactionRowNum = 5;
		paymentTypeRowNum = 321;
		cardDetailsRowNum = 47;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//blank all fields
		transactionRowNum = 5;
		paymentTypeRowNum = 322;
		cardDetailsRowNum = 7;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		helper.payment.VerifyOptionlFieldsFunctionalityForSMAE();
		Assert.assertTrue(helper.payment.verifyDropDownValue().equals("SBI Maestro Card"));

		Assert.assertTrue(testConfig.getTestResult());

	}
}