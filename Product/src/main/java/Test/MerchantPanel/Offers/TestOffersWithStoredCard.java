package Test.MerchantPanel.Offers;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;


/**
 * This class deals with offers using stored card details
 * 
 */
public class TestOffersWithStoredCard extends TestBase {


	// Test-Case ID: Product-1364
	@Test(description = "Verifying Offer with 'Max Amount per card' for stored card", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyOfferWithMaxAmountPerCard(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 122;
		int cardRowNum = 1;
		int paymentTypeRowNum = 371;
		// 3 a) Offer Key
		String offerKey = "maxamountpercard@3074";
		String limitMessage = "Limit Exceeded! You have exceeded the limit to avail the benefit.";
		// 3 b) User Credentials
		String merchantDetails = "am1";
		String bankCode = "am1";
		String storedName = "AMT_Store2";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		String failReason = "Card limit on amount reached. Total discount given till date = 1, maximum discount per card = 1.00";
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", failReason);
		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		testConfig.putRunTimeProperty("StoreCardName", storedName);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter following fields
		// a)User credentials as amm:amm
		// b)offer Key
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();
		// 5) On 'Credit Card' payment option,select stored card "AMT_Store1"
		// from "Select your card"
		// dropdown list and enter CVV number.
		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillSavedCardDetails(cardRowNum);

		// 6) Click on 'Pay Now' Button.
		payment.clickPayNowButton();

		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.validateLimitExceedMessagesOnProcessingPage();

		Helper.compareEquals(testConfig,
				"Limit Exceed message for Max Amount Per Card case",limitMessage,
				testConfig.getRunTimeProperty("limitMessage_Actual"));

		TestResponsePage testResponseForCC = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testResponseForCC
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1362
	@Test(description = "Verifying Offer with 'Max count per card' for stored card", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyOfferWithMaxCountPerCard(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 122;
		int cardRowNum = 1;
		int paymentTypeRowNum = 341;
		// 3 a) Offer Key
		String offerKey = "MaxCountstorecard@3072";
		// 3 b) User Credentials
		String merchantDetails = "dc1";
		String bankCode = "dc1";
		String storedName = "test_Store2";
		String failReason = "Card limit reached. No of times discount availed = 1, maximum times discount per card = 1";

		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", failReason);

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		testConfig.putRunTimeProperty("StoreCardName", storedName);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		String limitMessage = "Limit Exceeded! You have exceeded the limit to avail the benefit.Only 1 transaction(s) allowed per card.";
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");

		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter following fields
		// a)User credentials as amm:amm
		// b)offer Key
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();
		// 5) On 'Credit Card' payment option,select stored card "AMT_Store1"
		// from "Select your card"
		// dropdown list and enter CVV number.
		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		helper.dcTab = payment.clickDCTab();
		helper.dcTab.FillSavedCardDetails(cardRowNum);

		// 6) Click on 'Pay Now' Button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.validateLimitExceedMessagesOnProcessingPage();

		Helper.compareEquals(testConfig,
				"Limit Exceed message for Max Amount Per Card case",
				limitMessage,
				testConfig.getRunTimeProperty("limitMessage_Actual"));

		TestResponsePage testResponseForDC = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testResponseForDC
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1361
	@Test(description = "Verifying offer is availed after deleting stored card and then using same card details", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingOfferIsAvailedAfterDeletingStoredCardAndThenUsingSameCardDetails(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 123;
		int cardRowNum = 1;
		int paymentTypeRowNum = 371;
		int storedCardNumber = 7;
		// 3 a) Offer Key
		String offerKey = "teststorecard@3064";
		// 3 b) User Credentials
		String merchantDetails = "mm";
		String bankCode = "mm";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);

		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		testConfig.putRunTimeProperty("Amount_Text", "Amount: 10.00");
		testConfig
				.putRunTimeProperty("Discount_Text", "Discount awarded: 1.00");
		testConfig.putRunTimeProperty("Payable_Amount_Text",
				"Net amount payable: 9.00");

		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter following fields
		// a)User credentials as amm:amm
		// b)offer Key
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();
		// 5) Enter Card details for credit card payment type.
		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		helper.ccTab = payment.clickCCTab();
		// helper.ccTab.FillSavedCardDetails(cardRowNum);
		// 6) Click on 'Store this Card' link.
		// 7) Enter the Name user want to give to stored card in textfield e.g.
		// "testStoredCard"(without quotes".
		helper.ccTab.removeCCStoredCard();
		helper.ccTab.FillCardDetails(cardRowNum);
		helper.ccTab.FillStoreCardDetails(storedCardNumber);
		// 6) Click on 'Pay Now' Button.
		payment.clickPayNowButton();

		// Verify after step6.
		// a) Transaction processing page displays as following.
		// helper.verifyTransactionAmountsDisplayed();
		TestResponsePage testResponseForDC = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testResponseForDC
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		// Delete stored card and retry transaction
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter following fields
		// a)User credentials as amm:amm
		// b)offer Key
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();
		// 5) Enter Card details for credit card payment type.
		helper.ccTab = payment.clickCCTab();
		helper.ccTab.clickRemoveCard();
		helper.ccTab.FillCardDetails(cardRowNum);
		// 6) Click on 'Pay Now' Button.
		payment.clickPayNowButton();

		// Verify after step6.
		// a) Transaction processing page displays as following.
		// helper.verifyTransactionAmountsDisplayed();
		testResponseForDC = new TestResponsePage(testConfig);
		testConfig.removeRunTimeProperty("UseStoredDetails");
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testResponseForDC
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1360
	@Test(description = "Verifying 'check offer' link when offer is valid for a specific payment type only", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingCheckOfferLinkWhenOfferIsValidForASpecificPaymentTypeOnlyForCreditCard(
			Config testConfig) {
		// ---------------------------------------------------------------------------------//
		// Test-Case ID: Product-1360 Scenario 1
		int transactionRowNum = 122;
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();

		// 3 a) Offer Key
		String offerKey = "specificpayment@3062";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 3 b) User Credentials
		String merchantDetails = "ab";
		String bankCode = "ab";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		// Offer message to be displayed

		String strOfferMessage = "Your card is valid for offer. You will get a discount of Rs 1. Please "
				+ "complete the transaction to avail the offer.";

		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.ccTab = payment.clickCCTab();

		// 6) Click on "Check Offer" link.
		helper.ccTab.clickOnCheckOfferLink();
		// Verify after step 6
		Helper.compareContains(testConfig, "Offer Message",
				strOfferMessage,
				testConfig.getRunTimeProperty("checkOfferMessage_actual"));

		// ---------------------------------------------------------------------//
		// Test-Case ID: Product-1360 Scenario 2
		transactionRowNum = 122;
		int cardRow = 1;
		// helper = new TransactionHelper(testConfig, false);

		// 3 a) Offer Key
		offerKey = "specificpayment@3062";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 3 b) User Credentials
		merchantDetails = "ab";
		bankCode = "ab";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		// Offer message to be displayed

		strOfferMessage = "Your card is not eligible for an offer."
				+ " Please try with other card to avail the offer.";

		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.dcTab = payment.clickDCTab();

		// 6) Click on "Check Offer" link.
		helper.dcTab.FillCardDetails(cardRow);
		helper.dcTab.clickOnCheckOfferLink();
		// Verify after step 6
		Helper.compareContains(testConfig, "Offer Message",
				strOfferMessage,
				testConfig.getRunTimeProperty("checkOfferMessage_actual"));
		// --------------------------------------------------------------------//
		// Test-Case ID: Product-1360 Scenario 3

		transactionRowNum = 122;
		int paymentTypeRowNum = 113;

		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		testConfig.putRunTimeProperty("Amount_Text", " Amount: 10.00");
		testConfig
				.putRunTimeProperty("Discount_Text", "Discount awarded: 1.00");
		testConfig.putRunTimeProperty("Payable_Amount_Text",
				"Net amount payable: 9.00");
		offerKey = "specificpayment@3062";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 3 b) User Credentials
		merchantDetails = "ab";
		bankCode = "ab";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		// Offer message to be displayed
		strOfferMessage = "Payment method selected is not eligible for an offer. Please try with other payment method to avail the offer.";

		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();

		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on "Check Offer" link.
		helper.nbTab.clickOnCheckOfferLink();
		// Verify after step 6
		Helper.compareContains(testConfig, "Offer Message",
				strOfferMessage,
				testConfig.getRunTimeProperty("checkOfferMessage_actual"));

		// ------------------------------------------------------------//
		// Test-Case ID: Product-1360 Scenario 4
		transactionRowNum = 122;
		paymentTypeRowNum = 353;
		// 3 a) Offer Key
		offerKey = "specificpayment@3062";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 3 b) User Credentials
		merchantDetails = "ab";
		bankCode = "ab";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		// Offer message to be displayed
		strOfferMessage = "Payment method selected is valid for offer. You will get a discount "
				+ "of Rs 1. Please complete the transaction to avail the offer.";
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// and select Bank e.g. "Airtel money"
		// on payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on "Check Offer" link.
		helper.cashCardTab.clickOnCheckOfferLink();
		// Verify after step 6
		Helper.compareContains(testConfig, "Offer Message",
				strOfferMessage,
				testConfig.getRunTimeProperty("checkOfferMessage_actual"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1357
	@Test(description = "Verifying 'check offer' link for all payment types when offer is valid for all payment types", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingCheckOfferLinkForAllPaymentTypesWhenOfferIsValidForAllPaymentTypes(
			Config testConfig) {
		// ---------------------------------------------------------------//
		// Test-Case ID: Product-1357 Scenario 1
		int transactionRowNum = 122;
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();

		// 3 a) Offer Key
		String offerKey = "allpaymenttype@3060";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 3 b) User Credentials
		String merchantDetails = "ab";
		String bankCode = "ab";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		// Offer message to be displayed

		String strOfferMessage = "Your card is valid for offer. You will get a discount of Rs 1. Please "
				+ "complete the transaction to avail the offer.";
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.ccTab = payment.clickCCTab();

		// 6) Click on "Check Offer" link.
		helper.ccTab.clickOnCheckOfferLink();
		// Verify after step 6
		Helper.compareContains(testConfig, "Offer Message",
				strOfferMessage,
				testConfig.getRunTimeProperty("checkOfferMessage_actual"));

		// -----------------------------------------------------------------//
		// Test-Case ID: Product-1357 Scenario 2
		transactionRowNum = 122;
		int cardRow = 1;

		// 3 a) Offer Key
		offerKey = "allpaymenttype@3060";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 3 b) User Credentials
		merchantDetails = "ab";
		bankCode = "ab";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		// Offer message to be displayed

		strOfferMessage = "Your card is valid for offer. You will get a discount of Rs 1. Please "
				+ "complete the transaction to avail the offer.";

		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.dcTab = payment.clickDCTab();

		// 6) Click on "Check Offer" link.
		helper.dcTab.FillCardDetails(cardRow);
		helper.dcTab.clickOnCheckOfferLink();
		// Verify after step 6
		Helper.compareContains(testConfig, "Offer Message",
				strOfferMessage,
				testConfig.getRunTimeProperty("checkOfferMessage_actual"));

		// ----------------------------------------------------------//
		// Test-Case ID: Product-1357 Scenario 3
		transactionRowNum = 122;
		int paymentTypeRowNum = 113;
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		testConfig.putRunTimeProperty("Amount_Text", " Amount: 10.00");
		testConfig
				.putRunTimeProperty("Discount_Text", "Discount awarded: 1.00");
		testConfig.putRunTimeProperty("Payable_Amount_Text",
				"Net amount payable: 9.00");
		offerKey = "allpaymenttype@3060";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 3 b) User Credentials
		merchantDetails = "ab";
		bankCode = "ab";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		// Offer message to be displayed
		strOfferMessage = "Payment method selected is valid for offer."
				+ " You will get a discount of Rs 1. Please complete the transaction to avail the offer.";

		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();

		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on "Check Offer" link.
		helper.nbTab.clickOnCheckOfferLink();
		// Verify after step 6
		Helper.compareContains(testConfig, "Offer Message",
				strOfferMessage,
				testConfig.getRunTimeProperty("checkOfferMessage_actual"));
		// ------------------------------------------------------//
		// Test-Case ID: Product-1357 Scenario 4
		transactionRowNum = 122;
		paymentTypeRowNum = 353;

		// 3 a) Offer Key
		offerKey = "allpaymenttype@3060";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 3 b) User Credentials
		merchantDetails = "ab";
		bankCode = "ab";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		// Offer message to be displayed
		strOfferMessage = "Payment method selected is valid for offer. You will get a discount "
				+ "of Rs 1. Please complete the transaction to avail the offer.";

		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// and select Bank e.g. "Airtel money"
		// on payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on "Check Offer" link.
		helper.cashCardTab.clickOnCheckOfferLink();
		// Verify after step 6
		Helper.compareContains(testConfig, "Offer Message",
				strOfferMessage,
				testConfig.getRunTimeProperty("checkOfferMessage_actual"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1356
	@Test(description = "Verifying offer failure for credit card with disallowed bin using stored card details.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyOfferFailureForUsingDifferentPaymentTypeThanSpecifiedForOffer(
			Config testConfig) {
		// ---------------------------------------------------------------------//
		// Test-Case ID: Product-1356 Scenario 1
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		// make transaction with valid CC
		int transactionRowNum = 122;
		int paymentTypeRowNum = 371;
		int cardDetailsRowNum = 8;
		String strSorryMessage_expected = "Sorry! We cannot give you the discount. We regret to inform you"
				+ " that we are unable to process discount on your transaction for this payment method.";
		testConfig.putRunTimeProperty("strSorryMessage_expected",
				strSorryMessage_expected);
		helper.DoLogin();
		helper.GetTestTransactionPage();

		String offerKey = "CCdisallowedbin@3056";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("Offer_Failure_Reason",
				"Discount not available on payment mode chosen by user.");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");

		// 3 b) User Credentials
		String merchantDetails = "ab";
		String bankCode = "ab";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);

		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillSavedCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();

		helper.getSorryMessageDisplayedOnProcessingPage();
		String strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);
		TestResponsePage testResponseForCC = new TestResponsePage(testConfig);

		testResponseForCC.overrideExpectedTransactionData = true;
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// verify discount, status, offer type and offer key from database
		testResponseForCC
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);

		// -------------------------------------------------------------------//
		// Test-Case ID: Product-1356 Scenario 2
		// 3 b) User Credentials
		merchantDetails = "ab";
		bankCode = "ab";
		offerKey = "CCdisallowedbin@3056";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("Offer_Failure_Reason",
				"Discount not available on payment mode chosen by user.");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		testConfig.putRunTimeProperty("usingStoredCard", "no");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		strSorryMessage_expected = "Sorry! We cannot give you the discount. We regret to inform you"
				+ " that we are unable to process discount on your transaction for this payment method.";
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		// make transaction with valid DC (VISA)
		paymentTypeRowNum = 367;
		cardDetailsRowNum = 1;
		transactionRowNum = 122;
		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		payment = new PaymentOptionsPage(testConfig);
		helper.dcTab = payment.clickDCTab();

		TestDataReader paymentTypeData = new TestDataReader(testConfig,
				"PaymentType");

		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum,
				"Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);

		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();

		helper.getSorryMessageDisplayedOnProcessingPage();
		strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);
		TestResponsePage testResponseForDC = new TestResponsePage(testConfig);

		testResponseForDC.overrideExpectedTransactionData = true;

		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testResponseForCC = new TestResponsePage(testConfig);
		// verify discount, status, offer type and offer key from database
		testResponseForCC
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1355
	@Test(description = "Verifying offer availed for allowed bin using stored card details.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingOfferAvailedForAllowedBinUsingStoredCardDetails(
			Config testConfig) {
		// ---------------------------------------------------------------------//
		// Test-Case ID: Product-1355 Scenario 1
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		// make transaction with valid CC
		int transactionRowNum = 123;
		int paymentTypeRowNum = 371;
		int cardDetailsRowNum = 8;

		helper.DoLogin();
		helper.GetTestTransactionPage();

		String offerKey = "CCAllowedBin@3054";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		HashMap<String, String> TransactionAmounts = new HashMap<>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		// 3 b) User Credentials
		String merchantDetails = "ab";
		String bankCode = "ab";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);

		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillSavedCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);

		TestResponsePage testResponseForCC = new TestResponsePage(testConfig);

		testResponseForCC.overrideExpectedTransactionData = true;
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// verify discount, status, offer type and offer key from database
		testResponseForCC
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		// ---------------------------------------------------------------------//
		// Test-Case ID: Product-1355 Scenario 2
		// 3 b) User Credentials
		merchantDetails = "ab";
		bankCode = "ab";
		offerKey = "CCAllowedBin@3054";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		testConfig.putRunTimeProperty("usingStoredCard", "no");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		// make transaction with valid DC (VISA)
		paymentTypeRowNum = 341;
		cardDetailsRowNum = 1;
		transactionRowNum = 123;
		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		payment = new PaymentOptionsPage(testConfig);
		helper.dcTab = payment.clickDCTab();

		TestDataReader paymentTypeData = new TestDataReader(testConfig,
				"PaymentType");

		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum,
				"Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);

		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		TestResponsePage testResponseForDC = new TestResponsePage(testConfig);

		testResponseForDC.overrideExpectedTransactionData = true;

		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testResponseForCC = new TestResponsePage(testConfig);
		// verify discount, status, offer type and offer key from database
		testResponseForCC
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1354
	@Test(description = "Verifying offer availed using stored card details.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingOfferAvailedUsingStoredCardDetails(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		// make transaction with valid CC
		int transactionRowNum = 123;
		int paymentTypeRowNum = 371;
		int cardDetailsRowNum = 8;

		helper.DoLogin();
		helper.GetTestTransactionPage();

		String offerKey = "testdisc@3048";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		HashMap<String, String> TransactionAmounts = new HashMap<>(3);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

		// 3 b) User Credentials
		String merchantDetails = "ab";
		String bankCode = "ab";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);

		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillSavedCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);

		TestResponsePage testResponseForCC = new TestResponsePage(testConfig);

		testResponseForCC.overrideExpectedTransactionData = true;
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// verify discount, status, offer type and offer key from database
		testResponseForCC
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1352
	@Test(description = "Verifying offer is availed with storing the card details", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingOfferIsAvailedWithStoringTheCardDetails(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 123;
		int cardRowNum = 1;
		int paymentTypeRowNum = 371;
		int storedCardNumber = 8;
		// 3 a) Offer Key
		String offerKey = "testdisc@3048";
		// 3 b) User Credentials
		String merchantDetails = "nbm";
		String bankCode = "nbm";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("UseStoredDetails", "1");

		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);

		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		testConfig.putRunTimeProperty("Amount_Text", "Amount: 10.00");
		testConfig
				.putRunTimeProperty("Discount_Text", "Discount awarded: 1.00");
		testConfig.putRunTimeProperty("Payable_Amount_Text",
				"Net amount payable: 9.00");

		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter following fields
		// a)User credentials as amm:amm
		// b)offer Key
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();
		// 5) Enter Card details for credit card payment type.
		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		helper.ccTab = payment.clickCCTab();
		// helper.ccTab.FillSavedCardDetails(cardRowNum);
		// 6) Click on 'Store this Card' link.
		// 7) Enter the Name user want to give to stored card in textfield e.g.
		// "testStoredCard"(without quotes".
		helper.ccTab.removeCCStoredCard();
		helper.ccTab.FillCardDetails(cardRowNum);
		helper.ccTab.FillStoreCardDetails(storedCardNumber);
		// 6) Click on 'Pay Now' Button.
		payment.clickPayNowButton();

		// Verify after step6.
		// a) Transaction processing page displays as following.
		// helper.verifyTransactionAmountsDisplayed();
		TestResponsePage testResponseForDC = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testResponseForDC
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		// Delete stored card and retry transaction
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter following fields
		// a)User credentials as amm:amm
		// b)offer Key
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();
		// 5) Enter Card details for credit card payment type.
		helper.ccTab = payment.clickCCTab();
		helper.ccTab.verifyStoredCardIsPresent(storedCardNumber);
		helper.ccTab.clickRemoveCard();

		Assert.assertTrue(testConfig.getTestResult());
	}

}
