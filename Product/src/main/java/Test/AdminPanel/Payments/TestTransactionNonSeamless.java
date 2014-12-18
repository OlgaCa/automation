package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.ErrorResponsePage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;

public class TestTransactionNonSeamless extends TestBase{

	//TODO add test cases for transaction row 21 and 22

	/**
	 * @author prashant.singh
	 * Verify that amount and transaction id displayed on Payment Option Page matches the one given in Test Transaction Page
	 */
	@Test(description="Verify that amount and transaction id displayed on Payment Option Page matches the one given in Test Transaction Page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_AmountAndTranasctionIdOnPaymentOptionPage(Config testConfig) 
	{
		int transactionRowNum = 1;

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		helper.GetPaymentOptionsPage(transactionRowNum,PaymentMode.creditcard);

		String expectedTxnId = testConfig.getRunTimeProperty("transactionId");
		String expectedAmount = helper.transactionData.GetData(transactionRowNum, "amount");
		String expectedPaymentTypeLabel = "Choose a payment method";

		Helper.compareEquals(testConfig, "Choose PaymentType Label", expectedPaymentTypeLabel, helper.payment.getChoosePaymentTypeLabel());
		Helper.compareEquals(testConfig, "Transaction Amount", "Rs. " + expectedAmount, helper.payment.getAmount());
		Helper.compareEquals(testConfig, "Transaction Id", "ID: " + expectedTxnId, helper.payment.getTransactionId());

		Assert.assertTrue(testConfig.getTestResult());	
	}

	//This test case is no more valid as billing details are not displayed on Payment option page
	/*
	*//**
	 * @author prashant.singh
	 * Verify that Billing Details displayed on Payment Option Page matches the one given in Test Transaction Page
	 *//*
	@Test(description="Verify that Billing Details displayed on Payment Option Page matches the one given in Test Transaction Page",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_BillingDetailsOnPaymentOptionPage(Config testConfig)
	{
		int transactionRowNum = 12;

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		helper.GetPaymentOptionsPage(transactionRowNum,PaymentMode.creditcard);
		helper.payment.clickShowHideDetails();

		String expectedBillingLabel = "Billing Details";
		Helper.compareEquals(testConfig, "Billing Label", expectedBillingLabel, helper.payment.getBillingLabel());

		String expectedName = helper.transactionData.GetData(transactionRowNum, "firstname") + " " +helper.transactionData.GetData(transactionRowNum, "lastname");
		String expectedEmail = helper.transactionData.GetData(transactionRowNum, "email");
		String expectedPhone = helper.transactionData.GetData(transactionRowNum, "phone");
		String expectedBillingName = expectedName + '\n' + expectedEmail + '\n' + expectedPhone;

		String expectedAddress1 = helper.transactionData.GetData(transactionRowNum, "address1");
		String expectedCity = helper.transactionData.GetData(transactionRowNum, "city");
		String expectedZipcode = helper.transactionData.GetData(transactionRowNum, "zipcode");
		String expectedCountry = helper.transactionData.GetData(transactionRowNum, "country");
		String expectedBillingAddress = expectedAddress1 + '\n' + expectedCity + '\n' + expectedCountry + " - " + expectedZipcode;

		Helper.compareEquals(testConfig, "Billing Name", expectedBillingName, helper.payment.getBillingName());
		Helper.compareEquals(testConfig, "Billing Address", expectedBillingAddress, helper.payment.getBillingAddress());

		Assert.assertTrue(testConfig.getTestResult());	
	}*/


	/**
	 * Verify that if user clicks on Go back link on payment option page, he/she is redirected to response page with user cancelled status
	 */
	@Test(description = "Verify that if user clicks on Go back link on payment option page, he/she is redirected to response page with user cancelled status",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_UserCancelledTransaction(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 12;
		int paymentTypeRowNum = 226;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * @author jyoti.patial
	 * Verify max transaction amount limit 
	 */
	@Test(description = "Verify max transaction amount limit",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_LargeAmountTransaction(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 13;
		int paymentTypeRowNum = 246;
		int cardDetailsRowNum = 1;

		String maxTransactionAmount = "9999999999999.99";

		helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);
		Helper.compareEquals(testConfig, "Max Transaction Amount", "Rs. " + maxTransactionAmount, helper.payment.getAmount());

		helper.testResponse = (TestResponsePage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		testConfig.putRunTimeProperty("amount", maxTransactionAmount);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());			
	}

	//This test case is no more needed as billing details are no more displayed on payment page
/*	*//**
	 * Verify transaction with large billing details 
	 *//*
	@Test(description = "Verify transaction with large billing details",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_LargeBillingDetailsTransaction(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 22;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		//Verify TransactionId and amount Details
		helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);
		String expectedTxnId = testConfig.getRunTimeProperty("transactionId");
		String expectedAmount = helper.transactionData.GetData(transactionRowNum, "amount");
		String expectedPaymentTypeLabel = "Choose a payment method";

		Helper.compareEquals(testConfig, "Choose PaymentType Label", expectedPaymentTypeLabel, helper.payment.getChoosePaymentTypeLabel());
		Helper.compareEquals(testConfig, "Transaction Amount", "Rs. " + expectedAmount, helper.payment.getAmount());
		Helper.compareEquals(testConfig, "Transaction Id", "ID: " + expectedTxnId, helper.payment.getTransactionId());

		//Verify Billing Details
		helper.payment.clickShowHideDetails();

		String expectedBillingLabel = "Billing Details";
		Helper.compareEquals(testConfig, "Billing Label", expectedBillingLabel, helper.payment.getBillingLabel());

		String expectedName = helper.transactionData.GetData(transactionRowNum, "firstname") + " " +helper.transactionData.GetData(transactionRowNum, "lastname");
		String expectedEmail = helper.transactionData.GetData(transactionRowNum, "email");
		String expectedPhone = helper.transactionData.GetData(transactionRowNum, "phone");
		String expectedBillingName = expectedName + '\n' + expectedEmail + '\n' + expectedPhone;

		String expectedAddress1 = helper.transactionData.GetData(transactionRowNum, "address1");
		String expectedCity = helper.transactionData.GetData(transactionRowNum, "city");
		String expectedZipcode = helper.transactionData.GetData(transactionRowNum, "zipcode");
		String expectedCountry = helper.transactionData.GetData(transactionRowNum, "country");
		String expectedBillingAddress = expectedAddress1 + '\n' + expectedCity + '\n' + expectedCountry + " - " + expectedZipcode;

		Helper.compareEquals(testConfig, "Billing Name", expectedBillingName, helper.payment.getBillingName());
		Helper.compareEquals(testConfig, "Billing Address", expectedBillingAddress, helper.payment.getBillingAddress());

		helper.testResponse = (TestResponsePage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());			
	}
*/
	/**
	 * 
	 * Verify transaction when billing details are not posted 
	 */
	@Test(description = "Verify transaction when billing details are not posted",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WithoutBillingDetailsTransaction(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 21;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		//Verify TransactionId and amount Details
		helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);
		String expectedTxnId = testConfig.getRunTimeProperty("transactionId");
		String expectedAmount = helper.transactionData.GetData(transactionRowNum, "amount");
		String expectedPaymentTypeLabel = "Choose a payment method";

		Helper.compareEquals(testConfig, "Choose PaymentType Label", expectedPaymentTypeLabel, helper.payment.getChoosePaymentTypeLabel());
		Helper.compareEquals(testConfig, "Transaction Amount", "Rs. " + expectedAmount, helper.payment.getAmount());
		Helper.compareEquals(testConfig, "Transaction Id", "ID: " + expectedTxnId, helper.payment.getTransactionId());

		//Verify Billing Details
		helper.payment.VerifyBillingTabAbsent();

		helper.testResponse = (TestResponsePage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());			
	}

	/**
	 * @author jyoti.patial
	 * Verify error when transaction amount is blank 
	 */
	@Test(description = "Verify errors when transaction amount is invalid",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_InvalidAmountTransactions(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int currencySymbolAmountTransactionRow = 14;
		helper.GetTestTransactionPage();
		helper.transactionData = helper.trans.FillTransactionDetails(currencySymbolAmountTransactionRow);
		String currencySymbolAmountError = helper.transactionData.GetData(currencySymbolAmountTransactionRow, "ErrorMessage");
		String currencySymbolAmountErrorMessage = helper.transactionData.GetData(currencySymbolAmountTransactionRow, "MainErrorMessage");
		ErrorResponsePage errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Currency Symbol Amount Error Message", currencySymbolAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Currency Symbol Amount Error", currencySymbolAmountError, errorResponse.getReasonforFailure());

		int zeroAmountTransactionRow = 15;
		helper.GetTestTransactionPage();
		helper.transactionData = helper.trans.FillTransactionDetails(zeroAmountTransactionRow);
		String zeroAmountError = helper.transactionData.GetData(zeroAmountTransactionRow, "ErrorMessage");
		String zeroAmountErrorMessage = helper.transactionData.GetData(zeroAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Zero Amount Error Message", zeroAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Zero Amount Error", zeroAmountError, errorResponse.getReasonforFailure());

		int negativeAmountTransactionRow = 16;
		helper.GetTestTransactionPage();
		helper.transactionData = helper.trans.FillTransactionDetails(negativeAmountTransactionRow);
		String negativeAmountError = helper.transactionData.GetData(negativeAmountTransactionRow, "ErrorMessage");
		String negativeAmountErrorMessage = helper.transactionData.GetData(negativeAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Negative Amount Error Message", negativeAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Negative Amount Error", negativeAmountError, errorResponse.getReasonforFailure());

		int specialCharAmountTransactionRow = 17;
		helper.GetTestTransactionPage();
		helper.transactionData = helper.trans.FillTransactionDetails(specialCharAmountTransactionRow);
		String specialCharAmountError = helper.transactionData.GetData(specialCharAmountTransactionRow, "ErrorMessagesCash");
		String specialCharAmountErrorMessage = helper.transactionData.GetData(specialCharAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Special Char Amount Error Message", specialCharAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Special Char Amount Error", specialCharAmountError, errorResponse.getReasonforFailure());

		int specialCharInitNumAmountTransactionRow = 18;
		helper.GetTestTransactionPage();
		helper.transactionData = helper.trans.FillTransactionDetails(specialCharInitNumAmountTransactionRow);
		String specialCharInitNumAmountError = helper.transactionData.GetData(specialCharInitNumAmountTransactionRow, "ErrorMessagesCash");
		String specialCharInitNumAmountErrorMessage = helper.transactionData.GetData(specialCharInitNumAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Special Char Amount Error Message", specialCharInitNumAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Special Char Amount Error", specialCharInitNumAmountError, errorResponse.getReasonforFailure());

		int blankAmountTransactionRow = 19;
		helper.GetTestTransactionPage();
		helper.transactionData = helper.trans.FillTransactionDetails(blankAmountTransactionRow);
		String blankAmountError = helper.transactionData.GetData(blankAmountTransactionRow, "ErrorMessage");
		helper.trans.SubmitToGetErrorOnPage();

		Helper.compareEquals(testConfig, "Blank Amount Error", blankAmountError, helper.trans.getErrorMessage());

		int spaceAmountTransactionRow = 20;
		helper.GetTestTransactionPage();
		helper.transactionData = helper.trans.FillTransactionDetails(spaceAmountTransactionRow);
		String spaceAmountError = helper.transactionData.GetData(spaceAmountTransactionRow, "ErrorMessage");
		helper.trans.SubmitToGetErrorOnPage();

		Helper.compareEquals(testConfig, "Blank Amount Error", spaceAmountError, helper.trans.getErrorMessage());

		Assert.assertTrue(testConfig.getTestResult());			
	}

	/**
	 * @author jyoti.patial
	 * Verify error when transaction amount is blank 
	 */
	//TODO: after convenience fee is deployed
	@Test(description = "Verify errors when amount is invalid in convenience fee",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_InvalidConvenienceFeeTransactions(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int currencySymbolAmountTransactionRow = 14;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		
		testConfig.putRunTimeProperty("additionalCharges", "CC:"+helper.transactionData.GetData(currencySymbolAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(currencySymbolAmountTransactionRow, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, currencySymbolAmountTransactionRow, helper.paymentTypeData, paymentTypeRowNum);

		int zeroAmountTransactionRow = 15;
		paymentTypeRowNum = 3;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CC:"+helper.transactionData.GetData(zeroAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(zeroAmountTransactionRow, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, zeroAmountTransactionRow, helper.paymentTypeData, paymentTypeRowNum);

		int negativeAmountTransactionRow = 16;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CC:"+helper.transactionData.GetData(negativeAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(negativeAmountTransactionRow);
		String negativeAmountError = helper.transactionData.GetData(negativeAmountTransactionRow, "ErrorMessagesCC");
		String negativeAmountErrorMessage = helper.transactionData.GetData(negativeAmountTransactionRow, "MainErrorMessage");
		ErrorResponsePage errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Negative Amount Error Message", negativeAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Negative Amount Error", negativeAmountError, errorResponse.getReasonforFailure());

		int specialCharAmountTransactionRow = 17;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CC:"+helper.transactionData.GetData(specialCharAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(specialCharAmountTransactionRow);
		String specialCharAmountError = helper.transactionData.GetData(specialCharAmountTransactionRow, "ErrorMessagesCash");
		String specialCharAmountErrorMessage = helper.transactionData.GetData(specialCharAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Special Char Amount Error Message", specialCharAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Special Char Amount Error", specialCharAmountError, errorResponse.getReasonforFailure());

		int specialCharInitNumAmountTransactionRow = 18;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CC:"+helper.transactionData.GetData(specialCharInitNumAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(specialCharInitNumAmountTransactionRow);
		String specialCharInitNumAmountError = helper.transactionData.GetData(specialCharInitNumAmountTransactionRow, "ErrorMessagesCash");
		String specialCharInitNumAmountErrorMessage = helper.transactionData.GetData(specialCharInitNumAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Special Char Amount Error Message", specialCharInitNumAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Special Char Amount Error", specialCharInitNumAmountError, errorResponse.getReasonforFailure());

		negativeAmountTransactionRow = 16;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "DC:"+helper.transactionData.GetData(negativeAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(negativeAmountTransactionRow);
		negativeAmountError = helper.transactionData.GetData(negativeAmountTransactionRow, "ErrorMessagesDC");
		negativeAmountErrorMessage = helper.transactionData.GetData(negativeAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Negative Amount Error Message", negativeAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Negative Amount Error", negativeAmountError, errorResponse.getReasonforFailure());

		negativeAmountTransactionRow = 16;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "NB:"+helper.transactionData.GetData(negativeAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(negativeAmountTransactionRow);
		negativeAmountError = helper.transactionData.GetData(negativeAmountTransactionRow, "ErrorMessagesNB");
		negativeAmountErrorMessage = helper.transactionData.GetData(negativeAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Negative Amount Error Message", negativeAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Negative Amount Error", negativeAmountError, errorResponse.getReasonforFailure());

		negativeAmountTransactionRow = 16;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "EMI:"+helper.transactionData.GetData(negativeAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(negativeAmountTransactionRow);
		negativeAmountError = helper.transactionData.GetData(negativeAmountTransactionRow, "ErrorMessagesEMI");
		negativeAmountErrorMessage = helper.transactionData.GetData(negativeAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Negative Amount Error Message", negativeAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Negative Amount Error", negativeAmountError, errorResponse.getReasonforFailure());

		negativeAmountTransactionRow = 16;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "COD:"+helper.transactionData.GetData(negativeAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(negativeAmountTransactionRow);
		negativeAmountError = helper.transactionData.GetData(negativeAmountTransactionRow, "ErrorMessagesCOD");
		negativeAmountErrorMessage = helper.transactionData.GetData(negativeAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Negative Amount Error Message", negativeAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Negative Amount Error", negativeAmountError, errorResponse.getReasonforFailure());

		specialCharAmountTransactionRow = 17;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "COD:"+helper.transactionData.GetData(specialCharAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(specialCharAmountTransactionRow);
		specialCharAmountError = helper.transactionData.GetData(specialCharAmountTransactionRow, "ErrorMessagesCash");
		specialCharAmountErrorMessage = helper.transactionData.GetData(specialCharAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Special Char Amount Error Message", specialCharAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Special Char Amount Error", specialCharAmountError, errorResponse.getReasonforFailure());

		specialCharInitNumAmountTransactionRow = 18;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "COD:"+helper.transactionData.GetData(specialCharInitNumAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(specialCharInitNumAmountTransactionRow);
		specialCharInitNumAmountError = helper.transactionData.GetData(specialCharInitNumAmountTransactionRow, "ErrorMessagesCash");
		specialCharInitNumAmountErrorMessage = helper.transactionData.GetData(specialCharInitNumAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Special Char Amount Error Message", specialCharInitNumAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Special Char Amount Error", specialCharInitNumAmountError, errorResponse.getReasonforFailure());

		negativeAmountTransactionRow = 16;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CASH:"+helper.transactionData.GetData(negativeAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(negativeAmountTransactionRow);
		negativeAmountError = helper.transactionData.GetData(negativeAmountTransactionRow, "ErrorMessagesCASH");
		negativeAmountErrorMessage = helper.transactionData.GetData(negativeAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Negative Amount Error Message", negativeAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Negative Amount Error", negativeAmountError, errorResponse.getReasonforFailure());

		specialCharAmountTransactionRow = 17;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CASH:"+helper.transactionData.GetData(specialCharAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(specialCharAmountTransactionRow);
		specialCharAmountError = helper.transactionData.GetData(specialCharAmountTransactionRow, "ErrorMessagesCASH");
		specialCharAmountErrorMessage = helper.transactionData.GetData(specialCharAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Special Char Amount Error Message", specialCharAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Special Char Amount Error", specialCharAmountError, errorResponse.getReasonforFailure());

		specialCharInitNumAmountTransactionRow = 18;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CASH:"+helper.transactionData.GetData(specialCharInitNumAmountTransactionRow, "amount"));
		testConfig.putRunTimeProperty("amount", "1.00");
		helper.transactionData = helper.trans.FillTransactionDetails(specialCharInitNumAmountTransactionRow);
		specialCharInitNumAmountError = helper.transactionData.GetData(specialCharInitNumAmountTransactionRow, "ErrorMessagesCASH");
		specialCharInitNumAmountErrorMessage = helper.transactionData.GetData(specialCharInitNumAmountTransactionRow, "MainErrorMessage");
		errorResponse = helper.trans.SubmitToGetErrorResponsePage();

		Helper.compareEquals(testConfig, "Special Char Amount Error Message", specialCharInitNumAmountErrorMessage, errorResponse.getAmountErrorMessage());
		Helper.compareEquals(testConfig, "Special Char Amount Error", specialCharInitNumAmountError, errorResponse.getReasonforFailure());

		Assert.assertTrue(testConfig.getTestResult());			
	}
	
	/**
	 * @author prashant.singh
	 * Verify the links on footer of Payment Options Page
	 */
	@Test(description="Verify the links on footer of Payment Options Page",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_PaymentOptionsFooterLinks(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		helper.GetPaymentOptionsPage(1,PaymentMode.creditcard);
		String expectedFooterNote = helper.cardDetailsData.GetData(1, "Note");

		Helper.compareEquals(testConfig, "Footer Note Text", expectedFooterNote, helper.payment.getFooterNoteText());

		String oldWindow = "";
		String expectedUrl = "";

		oldWindow = helper.payment.clickVerifiedByVisaLink();
		expectedUrl = "https://usa.visa.com/personal/security/vbv/index.jsp?ep=v_sym_/verified";
		VerifyUrlAndCloseThisWindowAndSwitchToOld(testConfig, expectedUrl, oldWindow);

		oldWindow = helper.payment.clickMasterCardLink();
		expectedUrl = "http://www.mastercard.com/us/business/en/corporate/securecode/sc_popup.html?language=en";
		VerifyUrlAndCloseThisWindowAndSwitchToOld(testConfig, expectedUrl, oldWindow);

		oldWindow = helper.payment.clickVeriSignSecuredLink();
		expectedUrl = "https://sealinfo.verisign.com/splash?form_file=fdf/splash.fdf&dn=www.payu.in&lang=en";
		VerifyUrlAndCloseThisWindowAndSwitchToOld(testConfig, expectedUrl, oldWindow);

		oldWindow = helper.payment.clickPciLink();
		expectedUrl = "https://seal.controlcase.com/index.php?page=showCert&cId=3877025869";
		VerifyUrlAndCloseThisWindowAndSwitchToOld(testConfig, expectedUrl, oldWindow);

		Helper.compareTrue(testConfig, "Amex Safetry Link", helper.payment.IsAmexSafetyLinkPresent());
		Helper.compareTrue(testConfig, "Amex Link", helper.payment.IsAmexLinkpresent());

		Assert.assertTrue(testConfig.getTestResult());
	}

	private void VerifyUrlAndCloseThisWindowAndSwitchToOld(Config testConfig, String expectedUrl, String oldWindowHandle)
	{
		Browser.verifyURL(testConfig, expectedUrl);
		Browser.closeBrowser(testConfig);
		Browser.switchToGivenWindow(testConfig, oldWindowHandle);
	}
}