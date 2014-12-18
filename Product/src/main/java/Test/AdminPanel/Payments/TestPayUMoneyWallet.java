/*package Test.AdminPanel.Payments;

import java.util.Arrays;
import java.util.List;

import org.apache.velocity.anakia.Escape;
import org.testng.Assert;
import org.testng.annotations.Test;


import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsMerchantParamsPage;
import PageObject.AdminPanel.Payments.PaymentOptions.PayUMoneyTab;
import PageObject.AdminPanel.Payments.PaymentOptions.PayUMoneyTab.PayuMoneyCredentialInputs;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.Customer.PaymentPage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.PaisaTestHelper.PaisaUIHelper;
import Test.PaisaTestHelper.PaisaUIHelper.ExpectedLandingPage;
import Test.PaisaTestHelper.PaisaUIHelper.PaymentType;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.PaisaPOSTAPIReturnValues;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestPayUMoneyWallet extends TestBase{

	// Test Case ID: Product - 1503 
	@Test(description = "Verifying PayuMoney Debit card transaction when Compulsive merchant param is", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyingPayuMoneyDebitCardTansactionWhenCompulsiveMerchantParamIsSet(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 148;
		int paymentTypeRowNum = 376;
		int cardDetailsRowNum = 1;
		int loginDetailsRowNum =4;

		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		String PayUMoneyLoginURL_Expected="payumoney.com/payments";
		testConfig.putRunTimeProperty("payment_using_wallet",
				"Yes");
		// User Logs in
		helper.DoLogin();
		helper.GetTestTransactionPage();
		//Fill transaction details and submit
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.SubmitToGetPayuMoneyPage();
		// Compare the Payu money URL
		Browser.verifyURL(testConfig, PayUMoneyLoginURL_Expected);
		//Login PayuMoney
		testConfig.setTestDataExcelDynamic("paisa");
		PaisaUIHelper paisahelper = new PaisaUIHelper(testConfig);
		PaymentPage payment = paisahelper.loginPayuMoneyAsGuest(loginDetailsRowNum);
		//Make payment	
		payment.FillDebitCardDetails(cardDetailsRowNum);		
		payment.clickPayment();		
		// Validate Response		
		testConfig.setTestDataExcelDynamic("product");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		TestResponsePage responseFinal = new TestResponsePage(testConfig);
		// verify discount and offer key
		responseFinal.VerifyTransactionResponse(helper.transactionData,
						transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("modeForWallet", "DC");
		testConfig.putRunTimeProperty("ibiboCode", "MAST");
		responseFinal.verifyDetailsFromDatabase_ForWalletMoney(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());	
	}	
		
	// Test Case ID: Product - 1496 
	@Test(description = "Verifying seamless transaction through PayuMoney for Debit Card", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyingSeamlessTransactionThroughPayuMoneyForDebitCard(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		int transactionRowNum = 149;
		int paymentTypeRowNum = 377;
		int cardDetailsRowNum = 1;
		int LoginCredentialsRow = 4;
		testConfig.putRunTimeProperty("wallet_amount", "0.2");
		String PayUMoneyLoginURL_Expected = "payumoney.com/payments";
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("ibiboCode",
				"MAST");
		testConfig.putRunTimeProperty("modeForWallet",
				"DC");	
		// User Logs in 
		helper.DoLogin();
		// User clicks on Test Transaction Link
		helper.GetTestTransactionPage();
		// User select the Merchant, provide Seamless details and clicks submit button
		helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PayUMoneyPaymentLoginPage);
		// Compare the Payu money URL  
		Browser.verifyURL(testConfig,PayUMoneyLoginURL_Expected);
		// Login
		testConfig.setTestDataExcelDynamic("paisa");
		PaisaUIHelper paisahelper = new PaisaUIHelper(testConfig);
		PaymentPage payment = paisahelper.loginPayuMoneyAsGuest(LoginCredentialsRow);
		// Create object of PaisaUIHelper and make the transaction for Debit card
		payment.FillDebitCardDetails(cardDetailsRowNum);
		payment.clickPayment();
		testConfig.setTestDataExcelDynamic("product");		
		// Validate Response		
		TestResponsePage responseFinal = new TestResponsePage(testConfig);
		// verify discount and offer key
		responseFinal.VerifyTransactionResponse(helper.transactionData,
						transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		responseFinal.verifyDetailsFromDatabase_ForWalletMoney(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());	
	}
	
	// Test Case ID: Product - 1441 
	@Test(description = "Validation for Email Id field on PayuMoney tab when its value passed through 'Test Transaction'page", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_ValidationForEmailIdFieldOnPayuMoneyTabWhenItsValuePassedThroughTestTransactionPage(Config testConfig)
	{
	TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 110;
		helper.DoLogin();
		
		// Sceanrio 1 - Email Id value as payumony@gmail.com
	
		String PayUMoneyLoginURL_Expected = "payumoney.com/payments";
	
		String email = Helper.generateRandomAlphabetsString(5)+"@"
						+Helper.generateRandomAlphabetsString(5)+"."+"com";
		testConfig.putRunTimeProperty("email", email);
		

		PayUMoneyTab payUMoneyPage=(PayUMoneyTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Email Id's default value on PayU Money tab", testConfig.getRunTimeProperty("email"), payUMoneyPage.getDefaultEmailValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndReturnPaymentLoginPage();
		Browser.verifyURL(testConfig, PayUMoneyLoginURL_Expected);
		
		// Scenario 2 - Email ID value as test@examplecom
		PayUMoneyLoginURL_Expected = "payumoney.com/payments";
		email = Helper.generateRandomAlphabetsString(5)+"@"
						+Helper.generateRandomAlphabetsString(5)+"com";
		testConfig.putRunTimeProperty("email", email);
		payUMoneyPage=(PayUMoneyTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Email Id's default value on PayU Money tab", testConfig.getRunTimeProperty("email"), payUMoneyPage.getDefaultEmailValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.email);

		// Sceanrio 3 - Email Id value as abc@gmail.net
		PayUMoneyLoginURL_Expected = "payumoney.com/payments";
		email = Helper.generateRandomAlphabetsString(5)+"@"
						+Helper.generateRandomAlphabetsString(5)+".net";
		testConfig.putRunTimeProperty("email", email);
		payUMoneyPage=(PayUMoneyTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Email Id's default value on PayU Money tab", testConfig.getRunTimeProperty("email"), payUMoneyPage.getDefaultEmailValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndReturnPaymentLoginPage();
		Browser.verifyURL(testConfig, PayUMoneyLoginURL_Expected);
		
		// Scenario 4 - Email ID value as testexample.com
		PayUMoneyLoginURL_Expected = "payumoney.com/payments";
		email = Helper.generateRandomAlphabetsString(5)+""
						+Helper.generateRandomAlphabetsString(5)+".com";
		testConfig.putRunTimeProperty("email", email);
		payUMoneyPage=(PayUMoneyTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Email Id's default value on PayU Money tab", testConfig.getRunTimeProperty("email"), payUMoneyPage.getDefaultEmailValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.email);
		
		// Sceanrio 5 - Email Id value as abc@gmail.in
		PayUMoneyLoginURL_Expected = "payumoney.com/payments";
		email = Helper.generateRandomAlphabetsString(5)+"@"
						+Helper.generateRandomAlphabetsString(5)+".in";
		testConfig.putRunTimeProperty("email", email);
		payUMoneyPage=(PayUMoneyTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Email Id's default value on PayU Money tab", testConfig.getRunTimeProperty("email"), payUMoneyPage.getDefaultEmailValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndReturnPaymentLoginPage();
		Browser.verifyURL(testConfig, PayUMoneyLoginURL_Expected);
		
		// Scenario 6 - Email ID value as test@@example.com
		PayUMoneyLoginURL_Expected = "payumoney.com/payments";
		email = Helper.generateRandomAlphabetsString(5)+"@@"
						+Helper.generateRandomAlphabetsString(5)+".com";
		testConfig.putRunTimeProperty("email", email);
		payUMoneyPage=(PayUMoneyTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Email Id's default value on PayU Money tab", testConfig.getRunTimeProperty("email"), payUMoneyPage.getDefaultEmailValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.email);
		
		// Sceanrio 7 - Email Id value as abc@yahoo.co.in
		PayUMoneyLoginURL_Expected = "payumoney.com/payments";
		email = Helper.generateRandomAlphabetsString(5)+"@"
						+Helper.generateRandomAlphabetsString(5)+".co.in";
		testConfig.putRunTimeProperty("email", email);
		payUMoneyPage=(PayUMoneyTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Email Id's default value on PayU Money tab", testConfig.getRunTimeProperty("email"), payUMoneyPage.getDefaultEmailValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndReturnPaymentLoginPage();
		Browser.verifyURL(testConfig, PayUMoneyLoginURL_Expected);

		// Scenario 8 - Email ID value as test.example@com
		PayUMoneyLoginURL_Expected = "payumoney.com/payments";
		email = Helper.generateRandomAlphabetsString(5)+"."
						+Helper.generateRandomAlphabetsString(5)+"@com";
		testConfig.putRunTimeProperty("email", email);
		payUMoneyPage=(PayUMoneyTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Email Id's default value on PayU Money tab", testConfig.getRunTimeProperty("email"), payUMoneyPage.getDefaultEmailValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.email);
		
		// Scenario 9 - Email ID value as @abc@gmail.com
		PayUMoneyLoginURL_Expected = "payumoney.com/payments";
		email = "@"+ Helper.generateRandomAlphabetsString(5)+"@"
						+Helper.generateRandomAlphabetsString(5)+".com";
		testConfig.putRunTimeProperty("email", email);
		payUMoneyPage=(PayUMoneyTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Email Id's default value on PayU Money tab", testConfig.getRunTimeProperty("email"), payUMoneyPage.getDefaultEmailValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.email);
		
		// Scenario 10 - Email ID value as _abc@gmail.com
		PayUMoneyLoginURL_Expected = "payumoney.com/payments";
		email = "_"+Helper.generateRandomAlphabetsString(5)+"@"
						+Helper.generateRandomAlphabetsString(5)+".com";
		testConfig.putRunTimeProperty("email", email);
		payUMoneyPage=(PayUMoneyTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Email Id's default value on PayU Money tab", testConfig.getRunTimeProperty("email"), payUMoneyPage.getDefaultEmailValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.email);
		
		// Scenario 11 - Email ID value as BLANK
		payUMoneyPage=(PayUMoneyTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Email Id's default value on PayU Money tab", testConfig.getRunTimeProperty("email"), payUMoneyPage.getDefaultEmailValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.email);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	// Test Case ID: Product - 1440 
	@Test(description = "Validation for Phone No. field on 'PayuMoney' tab when its value passed from Test Transaction page", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_ValidationForPhoneNumberFieldOnPayuMoneyTabWhenItsValuePassedThroughTestTransactionPage(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 110;
		helper.DoLogin();
		
		// Sceanrio 1 - Phone Number value as 9899897888
		String PayUMoneyLoginURL_Expected="payumoney.com/payments";
		String phone =Long.toString( Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("phone", phone);
		PayUMoneyTab payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		// Validate valid email id appears already filled in
		Helper.compareEquals(testConfig, "Phone Number default value on PayU Money tab", phone,payUMoneyPage.getDefaultPhoneValue());
		// click Pay Using PayUMoney button
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndReturnPaymentLoginPage();
		// Validate PayuMoney URL
		Browser.verifyURL(testConfig, PayUMoneyLoginURL_Expected);
		
		// Scenario 2 - Phone Number is 98997
		 phone =Long.toString( Helper.generateRandomNumber(5));
		 testConfig.putRunTimeProperty("phone", phone);
		 payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		 payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.phonenumber);
		 
		// Sceanrio 3 - Phone Number is +919889788890
		phone ="+"+Long.toString( Helper.generateRandomNumber(12));
		testConfig.putRunTimeProperty("phone", phone);
		String PhoneNumberWithoutCountryCode =phone.substring(phone.length() - 10,
				phone.length());
		payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Phone Number's default value on PayU Money tab",PhoneNumberWithoutCountryCode, payUMoneyPage.getDefaultPhoneValue());
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndReturnPaymentLoginPage();
		Browser.verifyURL(testConfig, PayUMoneyLoginURL_Expected);
		
	
		// Scenario 4 - Phone Nuber value is BLANK
		testConfig.putRunTimeProperty("phone", "");
		payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.phonenumber);

		// Scenario 5 - Phone Nuber value is abcdefghj
		phone = Helper.generateRandomAlphabetsString(6);
		testConfig.putRunTimeProperty("phone", phone);
		payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.phonenumber);
		
		// Scenario 6 - Phone Number value is 98765432101234
		phone = Long.toString(Helper.generateRandomNumber(12));
		testConfig.putRunTimeProperty("phone", phone);
		payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		Helper.compareEquals(testConfig,
				"Phone Number's default value on PayU Money tab",
				phone.substring(phone.length() - 10,
						phone.length()), payUMoneyPage
						.getDefaultPhoneValue());

		Assert.assertTrue(testConfig.getTestResult());	
	}	
	
	// Test Case ID: Product - 1399 
	@Test(description = "Verifying Transaction failure through 'PayUMoney' due to incorrect card details", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingTransactionFailureThroughPayumoneyDueToIncorrectCardDetails(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		// make transaction with valid CC
		int transactionRowNum = 150;
		int cardDetailsRowNum =7;
		int paymentTypeRowNum = 373;
		int LoginCredentialsRow=4;
		helper.DoLogin();

		// Sceanrio 1 - For Credit Card
		testConfig.putRunTimeProperty("transactionFailedMessageExpected", "Yes");
		testConfig.putRunTimeProperty("email", "payumony@gmail.com");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("modeForWallet", "CC");
		String PayUMoneyLoginURL_Expected="payumoney.com/payments";
		
		PayUMoneyTab payUMoneyPage = (PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		payUMoneyPage.clickPayUsingPayUMoneyButton();
		Browser.verifyURL(testConfig,PayUMoneyLoginURL_Expected);
		testConfig.setTestDataExcelDynamic("paisa");
		PaisaUIHelper paisahelper = new PaisaUIHelper(testConfig);
		PaymentPage payment = paisahelper.loginPayuMoneyAsGuest(LoginCredentialsRow);
		payment.FillCreditCardDetails(cardDetailsRowNum);
		payment.clickPayment();

		// Validate Response
		TestResponsePage responseFinal = new TestResponsePage(testConfig);
		testConfig.setTestDataExcelDynamic("product");
		// verify discount and offer key
		responseFinal.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		responseFinal.verifyDetailsFromDatabase_ForWalletMoney(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product - 1495 
	@Test(description = "Verifying seamless transaction through PayuMoney for Credit card", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyingSeamlessTransactionThroughPayuMoneyForCreditCard(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		int transactionRowNum = 149;
		int paymentTypeRowNum = 374;
		int cardDetailsRowNum = 1;
		int loginCredentialsRow=4;
		testConfig.putRunTimeProperty("wallet_amount", "0.2");
	
		String PayUMoneyLoginURL_Expected = "payumoney.com/payments";
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("modeForWallet",
				"CC");	
		// User Logs in 
		helper.DoLogin();
		// User clicks on Test Transaction Link
		helper.GetTestTransactionPage();
		// User select the Merchant, provide Seamless details and clicks submit button
		helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PayUMoneyPaymentLoginPage);
		
		// Compare the Payu money URL  
		Browser.verifyURL(testConfig,PayUMoneyLoginURL_Expected);
		// Login
		testConfig.setTestDataExcelDynamic("paisa");
		PaisaUIHelper paisahelper = new PaisaUIHelper(testConfig);
		PaymentPage payment = paisahelper.loginPayuMoneyAsGuest(loginCredentialsRow);
		// Create object of PaisaUIHelper and make the transaction for Debit card
		payment.FillCreditCardDetails(cardDetailsRowNum);
		payment.clickPayment();
		testConfig.setTestDataExcelDynamic("product");		
		// Validate Response		
		TestResponsePage responseFinal = new TestResponsePage(testConfig);
		// verify discount and offer key
		responseFinal.VerifyTransactionResponse(helper.transactionData,
						transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		responseFinal.verifyDetailsFromDatabase_ForWalletMoney(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());	
	}

	// Test Case ID: Product - 1394 
	@Test(description = "Verifying Validation for Phone No. when value is entered from 'PayuMoney' tab.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingValidationForPhoneNoWhenValueIsEnteredFromPayumoneyTab(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 149;
		helper.DoLogin();
		TestDataReader data = new TestDataReader(testConfig,
				"TransactionDetails");
		String phoneNumber = data.GetData(transactionRowNum, "phone");
		String PayUMoneyRedirected_Expected="payumoney.com";
		// Scenario for valid phone no.
		PayUMoneyTab payUMoneyPage = (PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		String phone_number_actual = payUMoneyPage
			.getDefaultPhoneValue();
		Helper.compareEquals(testConfig, "Phone Number", phoneNumber,
				phone_number_actual);
		payUMoneyPage.clickPayUsingPayUMoneyButton();
		Browser.verifyURL(testConfig,PayUMoneyRedirected_Expected);

		// Scenario 2 For Invalid Phone number
		// ----------------------------------------------------------//
		transactionRowNum = 152;
		phoneNumber = data.GetData(transactionRowNum, "phone");
		payUMoneyPage = (PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		phone_number_actual = payUMoneyPage
				.getDefaultPhoneValue();
		Helper.compareEquals(testConfig, "Phone Number", phoneNumber,
				phone_number_actual);
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.phonenumber);

		// Scenario 3 for entering alphanumeric characters in Phone. No. field		
		// --------------------------------------------------
		transactionRowNum = 153;
		phoneNumber = data.GetData(transactionRowNum, "phone");
		payUMoneyPage = (PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		payUMoneyPage.FillDetails(transactionRowNum);
		phone_number_actual = payUMoneyPage
				.getDefaultPhoneValue();
		Helper.compareEquals(testConfig, "Phone Number", "",phone_number_actual);
	
		// Scenario 4 for more than 10character length value in the phone no.
		// Field.
		// ----------------------------------------------------------//

		transactionRowNum = 154;
		phoneNumber = data.GetData(transactionRowNum, "phone");
		payUMoneyPage = (PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		payUMoneyPage.FillDetails(transactionRowNum);
		phone_number_actual =payUMoneyPage.getDefaultPhoneValue();
		Helper.compareEquals(testConfig, "Phone Number", phoneNumber.substring(0, 10),
				phone_number_actual);
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	// Test Case ID: Product - 1393 
	@Test(description = "Verifying Validation for Email Id when value is entered from 'PayuMoney' tab.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingValidationForEmailIdFieldWhenValueIsEnteredFromPayumoneyTab(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 149;
		helper.DoLogin();
		String PayUMoneyRedirected_Expected="payumoney.com";
		// 	Scenario for valid email id
		PayUMoneyTab payUMoneyPage = (PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		payUMoneyPage.FillDetails(transactionRowNum );
		payUMoneyPage.clickPayUsingPayUMoneyButton();
		Browser.verifyURL(testConfig,PayUMoneyRedirected_Expected);

		//  	Scenario 2 for Invalid Email id.
		transactionRowNum = 151;
		payUMoneyPage = (PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.wallet);
		payUMoneyPage.FillDetails(transactionRowNum );
		payUMoneyPage.clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs.email);
		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test Case ID: Product - 1397 
	@Test(description = "Verifying Cashback Percent value for PayuMoney Wallet on payment option page.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingCashbackPercentValueForPayumoneyWalletOnPaymentOptionPage(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 71;
		helper.DoLogin();
		PayUMoneyTab payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum,PaymentMode.wallet);
		Helper.compareEquals(testConfig, "Percentage displayed on page",
				"5%", payUMoneyPage.offerIcon.getText());
		Helper.compareEquals(testConfig, "Message on Top of section",
				"Use PayUMoney to get 5% Discount", payUMoneyPage.offerHeading.getText());
		Helper.compareEquals(testConfig, "Message on Left Hand Side",
				"Discount: 5%", payUMoneyPage.leftHeading.getText());
		Element.verifyElementPresent(testConfig, payUMoneyPage.RewardLink,
				"Reward link on PayU Money");
		Helper.compareEquals(testConfig, "Cash Back Message above Email",
				"Enter details to get Discount",
				payUMoneyPage.formLabel.getText());

		// ----------------------------------------------//
		// Scenario 2 for 2%
		transactionRowNum = 70;
		payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum,PaymentMode.wallet);
		Element.verifyElementNotPresent(testConfig, payUMoneyPage.offerIcon, "Offer Icon on Payment Page");
		Helper.compareEquals(testConfig, "Message on Top of section",
				"Use PayUMoney to get 2% Discount", payUMoneyPage.offerHeading.getText());
		Helper.compareEquals(testConfig, "Message on Left Hand Side",
				"Discount: 2%", payUMoneyPage.leftHeading.getText());
		Element.verifyElementPresent(testConfig, payUMoneyPage.RewardLink,
				"Reward link on PayU Money");
		Helper.compareEquals(testConfig, "Cash Back Message above Email",
				"Enter details to get Discount",
				payUMoneyPage.formLabel.getText());
		// -----------------------------------------------------------------//
		// Scenario 3 for merchant not set
		transactionRowNum = 149;
		payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum,PaymentMode.wallet);
		Element.verifyElementNotPresent(testConfig, payUMoneyPage.offerIcon, "Offer Icon on Payment Page");
		Helper.compareEquals(testConfig, "Message on Top of section",
				"Pay using PayUMoney", payUMoneyPage.offerHeading.getText());
		Helper.compareEquals(testConfig, "Cash Back Message above Email",
				"Enter details", payUMoneyPage.formLabel.getText());

		// ----------------------------------------------------------------//
		// Scenario 4 for 1%
		transactionRowNum = 2;
		payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum,PaymentMode.wallet);
		Element.verifyElementNotPresent(testConfig, payUMoneyPage.offerIcon, "Offer Icon on Payment Page");
		Helper.compareEquals(testConfig, "Message on Top of section",
				"Use PayUMoney to get 1% Discount", payUMoneyPage.offerHeading.getText());
		Helper.compareEquals(testConfig, "Message on Left Hand Side",
				"Discount: 1%", payUMoneyPage.leftHeading.getText());

		Element.verifyElementPresent(testConfig, payUMoneyPage.RewardLink,
				"Reward link on PayU Money");
		Helper.compareEquals(testConfig, "Cash Back Message above Email",
				"Enter details to get Discount",
				payUMoneyPage.formLabel.getText());
		// ----------------------------------------------------------------------//
		// Scenario for 0%
		transactionRowNum = 5;
		payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum,PaymentMode.wallet);
		Element.verifyElementNotPresent(testConfig, payUMoneyPage.offerIcon, "Offer Icon on Payment Page");
		Helper.compareEquals(testConfig, "Message on Top of section",
				"Pay using PayUMoney", payUMoneyPage.offerHeading.getText());
		Helper.compareEquals(testConfig, "Cash Back Message above Email",
				"Enter details", payUMoneyPage.formLabel.getText());
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	// Test Case ID: Product - 1398 
	@Test(description = "Verifying Credit, Debit Card and Netbanking transaction through PayUMoney", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingCreditDebitCardAndNetbankingTransactionThroughPayumoney(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		// --------------------------------------------------------------------------------//
		// Scenario 1
		// make transaction with valid CC
		int transactionRowNum = 149;
		int paymentTypeRowNum = 374;
		int LoginCredentialsRow =4;
		helper.DoLogin();

		// Sceanrio 1 - For Credit Card
		testConfig.putRunTimeProperty("wallet_amount", "0.2");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("modeForWallet", "CC");
		
		PayUMoneyTab payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum,PaymentMode.wallet);
		payUMoneyPage.clickPayUsingPayUMoneyButton();
		testConfig.setTestDataExcelDynamic("paisa");
		PaisaUIHelper paisaUiHelper = new PaisaUIHelper(testConfig);
		//Login
		paisaUiHelper.payment = paisaUiHelper.loginPayuMoneyAsGuest(LoginCredentialsRow);
		//make payment
		paisaUiHelper.doPayment(PaymentType.CreditCard, ExpectedLandingPage.TransactionResponsePage);
		// Validate Response
		TestResponsePage responseFinal = new TestResponsePage(testConfig);
		testConfig.setTestDataExcelDynamic("product");
		// verify discount and offer key
		responseFinal.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		responseFinal
				.verifyDetailsFromDatabase_ForWalletMoney(transactionRowNum);
		 //----------------------------------------------------------------------//
		// Scenatrio 2 for debit card
		int cardDetailRowNum = 3;
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("modeForWallet", "DC");
		testConfig.putRunTimeProperty("ibiboCode", "MAST");
		//testConfig.putRunTimeProperty("isWallet", "yes");
		testConfig.setTestDataExcelDynamic("product");
		payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum,PaymentMode.wallet);
		payUMoneyPage.clickPayUsingPayUMoneyButton();
		testConfig.setTestDataExcelDynamic("paisa");
		paisaUiHelper.payment = new PaymentPage(testConfig);
		paisaUiHelper.payment.FillDebitCardDetails(cardDetailRowNum);
		paisaUiHelper.payment.clickPayment();
		// Validate Response
		responseFinal = new TestResponsePage(testConfig);
		testConfig
				.setTestDataExcelDynamic("product");
		responseFinal.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		responseFinal
				.verifyDetailsFromDatabase_ForWalletMoney(transactionRowNum);
		
		// ----------------------------------------------------------------------//
		// Scenatrio 3 for net
		testConfig.setTestDataExcelDynamic("product");
		payUMoneyPage =(PayUMoneyTab)helper.GetPaymentOptionsPage(transactionRowNum,PaymentMode.wallet);
		payUMoneyPage.clickPayUsingPayUMoneyButton();
		testConfig.setTestDataExcelDynamic("paisa");
		String[] sheetNames = {"CardDetails"};
		paisaUiHelper = new PaisaUIHelper(testConfig,sheetNames);
		paisaUiHelper.payment = new PaymentPage(testConfig);
		//click on netbanking
		paisaUiHelper.doPayment(PaymentType.NetBanking, ExpectedLandingPage.MerchantBankVerificationPage);
		testConfig.setTestDataExcelDynamic("product");
		TestDataReader testDataReader =  new TestDataReader(testConfig, "BankRedirect");
		Browser.verifyURL(testConfig,testDataReader.GetData(1, "RedirectURL"));
		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test Case ID: Product - 1438 
	@Test(description = "Verifying 'PayuMoney' should be dropped with Credit, Debit Card and 'NetBanking' payment types", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingPayumoneyShouldBeDroppedWithCreditDebitCardAndNetbankingPaymentTypes(
			Config testConfig) {
		
		int transactionRowNum = 149;
		List<String> tabsSHouldNotBePresent;
		List<String> tabsSHouldBePresent;
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		// Scenario for Credit Card in 'Drop Category'
		testConfig.putRunTimeProperty("DropCategory", "CC");
		tabsSHouldBePresent = Arrays.asList("Debit Card", "NetBanking", "EMI",
				"Cash Card");
		tabsSHouldNotBePresent = Arrays.asList("Credit Card", "PayuMoney");
		
		PaymentOptionsPage payment =helper.GetPaymentOptionPage(transactionRowNum);
		payment.verifyPaymentTabsOnPage(tabsSHouldNotBePresent, false);
		payment.verifyPaymentTabsOnPage(tabsSHouldBePresent, true);
		// ------------------------------------------------------------//
		// Scenario Debit Card in 'Drop Category'
		testConfig.putRunTimeProperty("DropCategory", "DC");
		tabsSHouldNotBePresent = Arrays.asList("Debit Card", "PayuMoney");
		tabsSHouldBePresent = Arrays.asList("Credit Card", "NetBanking", "EMI",
				"Cash Card");
		payment =helper.GetPaymentOptionPage(transactionRowNum);
		payment.verifyPaymentTabsOnPage(tabsSHouldNotBePresent, false);
		payment.verifyPaymentTabsOnPage(tabsSHouldBePresent, true);
		// --------------------------------------------------------------//
		// Scenario Netbanking in 'Drop Category'
		testConfig.putRunTimeProperty("DropCategory", "NB");
		tabsSHouldNotBePresent =  Arrays.asList("NetBanking", "PayuMoney");
		tabsSHouldBePresent = Arrays.asList("Credit Card", "Debit Card", "EMI",
				"Cash Card");
		payment =helper.GetPaymentOptionPage(transactionRowNum);
		payment.verifyPaymentTabsOnPage(tabsSHouldNotBePresent, false);
		payment.verifyPaymentTabsOnPage(tabsSHouldBePresent, true);
		// --------------------------------------------------------------//
		// Scenario EMI in 'Drop Category'
		testConfig.putRunTimeProperty("DropCategory", "EMI");
		tabsSHouldNotBePresent = Arrays.asList("EMI");
		tabsSHouldBePresent = Arrays.asList("Credit Card", "Debit Card",
				"NetBanking", "Cash Card");
		payment =helper.GetPaymentOptionPage(transactionRowNum);
		payment.verifyPaymentTabsOnPage(tabsSHouldNotBePresent, false);
		payment.verifyPaymentTabsOnPage(tabsSHouldBePresent, true);
		// --------------------------------------------------------------//
		// Scenario Cash Card in 'Drop Category'
		testConfig.putRunTimeProperty("DropCategory", "CASH");
		tabsSHouldNotBePresent = Arrays.asList("Cash Card");
		tabsSHouldBePresent = Arrays.asList("Credit Card", "Debit Card",
				"NetBanking", "EMI");
		payment =helper.GetPaymentOptionPage(transactionRowNum);
		payment.verifyPaymentTabsOnPage(tabsSHouldNotBePresent, false);
		payment.verifyPaymentTabsOnPage(tabsSHouldBePresent, true);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product - 1439 
	@Test(description = "Verifying PayUMoney tab is absent when transaction is done with offerkey", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingPayumoneyTabIsAbsentWhenTransactionIsDoneWithOfferkey(
			Config testConfig) {
		// Steps
		int transactionRowNum = 149;
		List<String> tabsSHouldNotBePresent;
		String offerKey = "testPayuMoneyTab@3090";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		helper.DoLogin();
		PaymentOptionsPage payment = helper.GetPaymentOptionPage(transactionRowNum);
		tabsSHouldNotBePresent = Arrays.asList("PayuMoney");
		payment.verifyPaymentTabsOnPage(tabsSHouldNotBePresent, false);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product - 1502 
	@Test(description = "Verifying PayuMoney Credit card transaction when Compulsive merchant param is", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingPayuMoneyCreditCardTansactionWhenCompulsiveMerchantParamIsSet(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		int transactionRowNum = 148;
		int paymentTypeRowNum = 375;
		int UserLoginDetailRowNum = 4;
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		String PayUMoneyLoginURL_Expected="payumoney.com/payments";
		testConfig.putRunTimeProperty("payment_using_wallet",
				"Yes");
		testConfig.putRunTimeProperty("modeForWallet",
				"CC");
		helper.DoLogin();
		helper.GetTestTransactionPage();
		//Fill transaction details and submit
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.SubmitToGetPayuMoneyPage();
		// Compare the Payu money URL
		Browser.verifyURL(testConfig, PayUMoneyLoginURL_Expected);
		// Fill Login Details
		testConfig.setTestDataExcelDynamic("paisa");
		PaisaUIHelper paisaUIHelper = new PaisaUIHelper(testConfig);
		paisaUIHelper.payment =paisaUIHelper.loginPayuMoneyAsGuest(UserLoginDetailRowNum);
		paisaUIHelper.doPayment(PaymentType.CreditCard, ExpectedLandingPage.TransactionResponsePage);
		// Validate Response		
		testConfig.setTestDataExcelDynamic("product");
		TestResponsePage responseFinal = new TestResponsePage(testConfig);
		// verify discount and offer key
		responseFinal.VerifyTransactionResponse(helper.transactionData,
						transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		responseFinal.verifyDetailsFromDatabase_ForWalletMoney(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());	
	}
		

}

*/