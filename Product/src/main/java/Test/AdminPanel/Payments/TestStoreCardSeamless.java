package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.DCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.InputField;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;

public class TestStoreCardSeamless extends TestBase {

	// Test-Case ID: Product-1504
	@Test(description = "Verifying seamless store card for invalid PG and Ibibo Code passed as parameter", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingSeamlessStoreCardForInvalidPgAndIbiboCodePassedAsParameter(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 122;
		int storeCardRow = 2;
		int paymentTypeRow =366;
		int cardRow =1;
		// --------------------------------------------------------------//
		// Scenario for invalid PG.
		helper.DoTestTransactionWithInvalidInput(InputField.PG, transactionRowNum,
											paymentTypeRow, cardRow, storeCardRow);
		//Scenario for invalid ibibo code
		helper.DoTestTransactionWithInvalidInput(InputField.Ibibo_code, transactionRowNum,
												paymentTypeRow, cardRow, storeCardRow);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1492
	@Test(description = "Verifying seamless stored card when invalid CVV value is passed as parameter", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingSeamlessStoredCardWhenInvalidCvvValueIsPassedAsParameter(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 122;
		int storeCardRow = 2;
		int paymentTypeRowNum = 366;
		int cardDetailsRowNum =2;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		// --------------------------------------------------------------//
		// Scenario for Credit Card
		// Do Transaction
		TestResponsePage testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(
												transactionRowNum,paymentTypeRowNum,
												cardDetailsRowNum, storeCardRow ,
													ExpectedResponsePage.TestResponsePage);
		//verify response
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//verify card is stored
		helper.ccTab = (CCTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);
		helper.ccTab.verifyStoredCardIsPresent(storeCardRow);

		// -------------------------------------------------------------------//
		// Scenario 2 for debit card
		paymentTypeRowNum = 367;
		testConfig.removeRunTimeProperty("userString");
		// Do Transaction
		testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(
				transactionRowNum,paymentTypeRowNum,
				cardDetailsRowNum, storeCardRow ,
					ExpectedResponsePage.TestResponsePage);
		//verify response
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//verify card is stored
		helper.dcTab = (DCTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.verifyStoredCardIsPresent(storeCardRow);
		
		Assert.assertTrue(testConfig.getTestResult());
		}

	// Test-Case ID: Product-1488
	@Test(description = "Verifying Multiple cards saved through seamless stored card, 1 for Credit and 2nd for debit card.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingMultipleCardsSavedThroughSeamlessStoredCard1ForCreditAnd2ndForDebitCard(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 122;
		int creditStoreCardRow = 1;
		int paymentTypeRowNum = 366;
		int storeCardRowNum =1;	
		// --------------------------------------------------------------//
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		// 1) Click on 'Test Transaction'
		//Do transaction
		TestResponsePage testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditStoreCardRow, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//Store card name
		String StoredName1= testConfig.getRunTimeProperty("StoreCardName");
		// ---------------------2--------------------------------//
		paymentTypeRowNum = 367;
		creditStoreCardRow = 72;
		// 1) Click on 'Test Transaction'
		//Do transaction
		testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditStoreCardRow, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//go to payment options page and verify
		helper.dcTab = (DCTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.verifyStoredCardIsPresent(storeCardRowNum);
		helper.ccTab =helper.payment.clickCCTab();
		//get and store first transactions stored card name
		testConfig.putRunTimeProperty("StoreCardName",StoredName1);
		helper.ccTab.verifyStoredCardIsPresent(storeCardRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1487
	@Test(description = "Verifying Multiple cards saved through seamless stored card for Debit Card only", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingMultipleCardsSavedThroughSeamlessStoredCardForDebitCardOnly(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 122;
		int paymentTypeRowNum = 367;
		int creditStoreCardRow = 1;
		int storeCardRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		//First Transaction
		TestResponsePage testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditStoreCardRow, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//Second Transaction
		creditStoreCardRow = 72;
		testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditStoreCardRow, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//verify stored card is present
		helper.dcTab = (DCTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.verifyStoredCardIsPresent(storeCardRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1486
	@Test(description = "Verifying Multiple cards saved through seamless stored card for Credit Card only", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingMultipleCardsSavedThroughSeamlessStoredCardForCreditCardOnly(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 122;
		int paymentTypeRowNum = 366;
		int creditStoreCardRow = 1;
		int storeCardRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		//First Transaction
		TestResponsePage testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditStoreCardRow, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//Second Transaction
		creditStoreCardRow = 72;
		testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditStoreCardRow, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//verify stored card is present
		helper.ccTab = (CCTab)helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);
		helper.ccTab.verifyStoredCardIsPresent(storeCardRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	// Test-Case ID: Product-1485
	@Test(description = "Verifying non seamless transaction through seamless stored card for Debit Card", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNonSeamlessTransactionThroughSeamlessStoredCardForDebitCard(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 122;
		int paymentTypeRowNum = 367;
		int debitStoreCardRow = 1;
		int storeCardRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		
		//Debit card Transaction
		TestResponsePage testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						debitStoreCardRow, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		
		//do non-seamless transaction
		testResponse = (TestResponsePage) helper.DoPaymentWithSavedStoreCard(
				transactionRowNum, paymentTypeRowNum, storeCardRowNum, false,
				ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1484
	@Test(description = "Verifying seamless transaction using storedcard token and without passing Card details", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingSeamlessTransactionUsingStoredcardTokenAndWithoutPassingCardDetails(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 122;
		int paymentTypeRowNum = 366;
		int CardDetailsRow = 1;
		int storeCardDetailsRow =1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");

		//Credit card Transaction with card details
		TestResponsePage testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						CardDetailsRow, storeCardDetailsRow,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// credit card transaction without card details, Using Card Token
		CardDetailsRow = 7;
		testResponse = (TestResponsePage) helper.DoPaymentAndSaveCard(
				transactionRowNum, paymentTypeRowNum, CardDetailsRow,
				storeCardDetailsRow, ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		//Debit card Transaction with card details
		paymentTypeRowNum = 367;
		CardDetailsRow = 1;
		testResponse = (TestResponsePage) helper.DoPaymentAndSaveCard(
				transactionRowNum, paymentTypeRowNum, CardDetailsRow,
				storeCardDetailsRow, ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);		
		
		//debit card transaction without card details , Using Card Token
		CardDetailsRow = 7;
		testResponse = (TestResponsePage) helper.DoPaymentAndSaveCard(
				transactionRowNum, paymentTypeRowNum, CardDetailsRow,
				storeCardDetailsRow, ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
		
	}
	
	// Test-Case ID: Product-1482
	@Test(description = "Verifying non seamless transaction through seamless stored card for 'Credit Card'", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNonSeamlessTransactionThroughSeamlessStoredCardForCreditCard(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 122;
		int paymentTypeRowNum = 366;
		int creditStoreCardRow = 1;
		int storeCardRowNum = 1;
		
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		
		//Credit card Transaction
		helper.GetTestTransactionPage();
		TestResponsePage testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditStoreCardRow, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		//do non-seamless transaction
		testResponse = (TestResponsePage) helper.DoPaymentWithSavedStoreCard(
				transactionRowNum, paymentTypeRowNum, storeCardRowNum, false,
				ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1481
	@Test(description = "Verifying Storecard seamless when Store this card value passed is invalid", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingStorecardSeamlessWhenStoreThisCardValuePassedIsInvalid(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 122;
		int paymentTypeRowNum = 366;
		int creditCardRow = 1;
		int storeCardDetailsRow =9;
		testConfig.putRunTimeProperty("stored_card", "9");
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		
		//Credit card Transaction
		TestResponsePage testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditCardRow, storeCardDetailsRow,
						ExpectedResponsePage.TestResponsePage);
		//verify response for normal credit card  without card token
		testConfig.removeRunTimeProperty("UseStoredDetails");
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		//Debit card Transaction
		paymentTypeRowNum = 367;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditCardRow, storeCardDetailsRow,
						ExpectedResponsePage.TestResponsePage);
		//verify response for normal credit card  without card token
		testConfig.removeRunTimeProperty("UseStoredDetails");
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1480
	@Test(description = "Verifying Storecard seamless when Store this card value passed is 1 with Store Card Name", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingStorecardWhenStoreThisCardValuePassedIs1WithStoreCardName(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 122;
		int paymentTypeRowNum = 379;
		int creditCardRow = 1;
		int storeCardDetailsRow =1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		
		//Scenario for Credit card 
		TestResponsePage testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditCardRow, storeCardDetailsRow,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		testConfig.removeRunTimeProperty("StoreCardName");
		//Scenario for Debit card 
		paymentTypeRowNum = 378;
		testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditCardRow, storeCardDetailsRow,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
		
	}

	// Test-Case ID: Product-1479
	@Test(description = "Verifying Storecard seamless when Store this card value passed is 1 without Store Card Name", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingStorecardSeamlessWhenStoreThisCardValuePassedIs1WithoutStoreCardName(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 122;
		int paymentTypeRowNum = 379;
		int creditCardRow = 1;
		int storeCardDetailsRow = 10;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		
		//Scenario for Credit card 
		TestResponsePage testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditCardRow, storeCardDetailsRow,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		testConfig.removeRunTimeProperty("StoreCardName");
		//Scenario for Debit card 
		paymentTypeRowNum = 378;
		testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditCardRow, storeCardDetailsRow,
						ExpectedResponsePage.TestResponsePage);
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
		
	}

	// Test-Case ID: Product-1483
	@Test(description = "Verifying seamless transaction using storedcard token", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingSeamlessTransactionUsingStoredcardToken(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 122;
		int paymentTypeRowNum = 366;
		int creditStoreCardRow = 1;
		int storeCardRow =1;
		
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		//Credit Card Scenario
		
		TestResponsePage testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditStoreCardRow, storeCardRow,
						ExpectedResponsePage.TestResponsePage);
		//get card token
		String StoreCardToken =testResponse.actualResponse.get("cardToken");
		testConfig.putRunTimeProperty("StoreCardToken", StoreCardToken);
		
		//Do transaction with card token
		testResponse = (TestResponsePage) helper.DoPaymentWithSavedStoreCard(
				transactionRowNum, paymentTypeRowNum, storeCardRow, true,
				ExpectedResponsePage.TestResponsePage);
		//verify response for normal credit card  without card token
		testConfig.removeRunTimeProperty("UseStoredDetails");
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.removeRunTimeProperty("StoreCardName");
		
		//Debit Card Scenario
		paymentTypeRowNum = 367;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		//Credit Card Scenario
		testResponse = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						creditStoreCardRow, storeCardRow,
						ExpectedResponsePage.TestResponsePage);
		//get card token
		StoreCardToken =testResponse.actualResponse.get("cardToken");
		testConfig.putRunTimeProperty("StoreCardToken", StoreCardToken);
		
		//Do transaction with card token
		testResponse = (TestResponsePage) helper.DoPaymentWithSavedStoreCard(
				transactionRowNum, paymentTypeRowNum, storeCardRow, true,
				ExpectedResponsePage.TestResponsePage);
		//verify response for normal credit card  without card token
		testConfig.removeRunTimeProperty("UseStoredDetails");
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1494
	@Test(description = "Verifying seamless store card when invalid card number is passed as parameter.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingSeamlessStoreCardWhenInvalidCardNumberIsPassedAsParameter(
			Config testConfig) {
		int transactionRow = 122;
		int paymentTypeRow =366;
		int cardDetailsRow = 35;
		int storedCardDetailsRow =1;
		String expectedInvalidCardErrorMessage = "Invalid card number."+
													" Please re-enter the card"+
														" details and click Pay Now.";
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		helper.DoLogin();
		//Credit Card Scenario
		PaymentOptionsPage payment = (PaymentOptionsPage) helper
				.DoPaymentAndSaveCard(transactionRow, paymentTypeRow,
						cardDetailsRow, storedCardDetailsRow,
						ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Invalid Card Details Error Message",
				expectedInvalidCardErrorMessage, payment.getInvalidCardError());
		
		//Debit Card Scenario
		paymentTypeRow =367;
		testConfig.removeRunTimeProperty("StoreCardName");
		payment = (PaymentOptionsPage) helper
				.DoPaymentAndSaveCard(transactionRow, paymentTypeRow,
						cardDetailsRow, storedCardDetailsRow,
						ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Invalid Card Details Error Message",
				expectedInvalidCardErrorMessage, payment.getInvalidCardError());
		Assert.assertTrue(testConfig.getTestResult());
	}

}