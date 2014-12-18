package Test.MerchantPanel.Offers;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

// This class deals with cash card offers

public class TestCashCardOffers extends TestBase {

	// Test-Case ID: Product-1314
	@Test(description = "Verifying Store card link should not appear for Cash Card", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingStoreCardLinkShouldNotAppearForCashCard(
			Config testConfig) {

		// get transaction details
		int transactionRowNum = 117;
		int paymentTypeRowNum = 53;

		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();

		// 3 a) Offer Key
		String offerKey = "cashcardonlytest@2930";
		// 3 b) User Credentials
		String merchantDetails = "ab";
		String bankCode = "ab";

		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("merchantKey", merchantDetails);
		testConfig.putRunTimeProperty("bankcode", bankCode);

		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter following fields
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// and select Bank e.g. "City Reward points"
		// on payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// Verify after step 5
		// 'Store card' link should not be displayed next to 'Card Number'
		// Field.
		helper.cashCardTab.verifyStoreCardFeatureNotDisplayedIn_CashCard();

		Assert.assertTrue(testConfig.getTestResult());

	}

	//FIXME BUG-ID -37377
	// Test-Case ID: Product-1313
	@Test(description = "Verifying CashCard offer by clicking on'Check Offer'link.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingCashCardOfferByClickingOnCheckOfferLink(
			Config testConfig) {

		int transactionRowNum = 117;
		int paymentTypeRowNum = 53;
		int cardDetailsRowNum = 1;

		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();

		// 3 a) Offer Key
		String offerKey = "cashcardonlytest@2930";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// Offer message to be displayed
		String strOfferMessage = "Payment method selected is valid for offer. You will get a discount "
				+ "of Rs 1. Please complete the transaction to avail the offer.";
		testConfig.putRunTimeProperty("checkOfferMessage_expected",
				strOfferMessage);

		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// and select Bank e.g. "City Reward points"
		// on payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on "Check Offer" link.
		helper.cashCardTab.clickOnCheckOfferLink();
		// Verify after step 6
		Helper.compareContains(testConfig, "Offer Message",
				strOfferMessage,
				testConfig.getRunTimeProperty("checkOfferMessage_actual"));
		// 7) Follow step1 to step 5 again.
		payment.clickDCTab();
		helper.cashCardTab = payment.clickCashCardTab();
		// and select Bank e.g. "City Reward points"
		// on payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 8) Enter the card details.
		helper.cashCardTab.FillCardDetails(cardDetailsRowNum);
		// 9) Now, click on "Check offer" link.
		helper.cashCardTab.clickOnCheckOfferLink();
		// Verify after step 9
		Helper.compareContains(testConfig, "Offer Message",
				strOfferMessage,
				testConfig.getRunTimeProperty("checkOfferMessage_actual"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1312
	@Test(description = "Verify CashCard Offer active for all payment types Scenario 1 for Cash Card", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyCashcardOfferActiveForAllPaymentTypes(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();
		int transactionRowNum = 118;
		int paymentTypeRowNum = 353;
		// 3 a) Offer Key
		String offerKey = "Validalltypes@2924";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		HashMap<String, String> TransactionAmounts = new HashMap<>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);
		// Test-Case ID: Product-1312 Scenario 2

		// make transaction with valid CC
		transactionRowNum = 120;
		paymentTypeRowNum = 360;
		int cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();

		offerKey = "Validalltypes@2924";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "Validalltypes@2924");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();

		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(
				testConfig.getRunTimeProperty("amount"), "oneValidOneInvalid");

		TestResponsePage testResponseForCC = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// verify discount, status, offer type and offer key from database
		testResponseForCC
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		// Test-Case ID: Product-1312 Scenario 3
		// make transaction with valid CC
		transactionRowNum = 120;
		paymentTypeRowNum = 361;
		cardDetailsRowNum = 1;

		offerKey = "Validalltypes@2924";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "Validalltypes@2924");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		payment = new PaymentOptionsPage(testConfig);

		helper.dcTab = payment.clickDCTab();

		TestDataReader paymentTypeData2 = new TestDataReader(testConfig,
				"PaymentType");

		String debitCardName = paymentTypeData2.GetData(paymentTypeRowNum,
				"Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);

		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();

		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(
				testConfig.getRunTimeProperty("amount"), "oneValidOneInvalid");

		TestResponsePage testResponseForDC2 = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponseForDC2.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// verify discount, status, offer type and offer key from database
		testResponseForDC2
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);

		// Test-Case ID: Product-1312 Scenario 4
		transactionRowNum = 120;
		paymentTypeRowNum = 60;

		offerKey = "Validalltypes@2924";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		payment = new PaymentOptionsPage(testConfig);
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.

		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);
		// Test-Case ID: Product-1312 Scenario 5
		transactionRowNum = 120;
		paymentTypeRowNum = 260;
		int cardRow = 1;

		offerKey = "Validalltypes@2924";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "Validalltypes@2924");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		// EMI per month for 12 months: 0.75
		TransactionAmounts.put("EMI_Amount_Text",
				"EMI per month for 12 months: 0.75");

		testConfig.putRunTimeProperty("offerKey", offerKey);
		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		payment = new PaymentOptionsPage(testConfig);
		// select emi tab
		helper.emiTab = payment.clickEMITab();
		// fill payment details
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();
		// verify amounts displayed on processing page
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);

		TestResponsePage testResponseForEMI = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponseForEMI.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1309
	@Test(description = "Verify Offer Retry page in case of invalid Cashcard offer", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyOfferRetryPageInCaseOfInvalidCashcardOffer(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();
		int transactionRowNum = 122;
		int paymentTypeRowNum = 353;

		String strSorryMessage_expected = "Sorry! This offer is not valid.To avail this offer"
				+ " amount must be atleast 11.00";
		// 3 a) Offer Key
		String offerKey = "InvalidOfferCashCard@3122";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();

		// a) Verify Retry page with its ui elements Retry and Continue showing
		// following message.
		helper.verifyRetryPageElements();
		String strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);
		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1308
	@Test(description = "CashCard Multiple Offer for discount in Rupee and in percent", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_CashCardMultipleOfferForDiscountInRupeeAndInPercent(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		// Verify after step6.
		// a) Transaction processing page displays as following.
		// Amount: 10.00
		// Discount awarded: 3.00
		// Net amount payable: 7.00
		HashMap<String, String> TransactionAmounts = new HashMap<>(3);
		TransactionAmounts.put("Amount_Text", "Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 3.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 7.00");

		helper.DoLogin();
		int transactionRowNum = 118;
		int paymentTypeRowNum = 353;
		// 3 a) Offer Key
		String offerKey = "discInPercent@2912,DiscInRupee@2914";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		// e.g."discInPercent@2912,DiscInRupee@2914"(without quotes).
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();

		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.

		payment.clickPayNowButton();

		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);

		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1307
	@Test(description = "Verifying 'Net Payable Amount' equal or more than1 for CashCard offer", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNetPayableAmountEqualOrMoreThanOneForCashCardOffer(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();
		int transactionRowNum = 118;
		int paymentTypeRowNum = 353;
		// 3 a) Offer Key
		String offerKey = "NetPayableAmt@2910";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		// e.g."discInPercent@2912,DiscInRupee@2914"(without quotes).
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		// Original Amount: 10.00
		// Maximum Discount: 9.50
		// Discount Awarded: 9.00
		// Net amount payable1.00
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", "Original Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount Awarded: 9.00");
		TransactionAmounts.put("Maximum_Discount_Text",
				"Maximum Discount: 9.50");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 1.00");
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1301
	@Test(description = "Verifying Cashcard specific offer is availed out of multiple offers", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingCashcardSpecificOfferIsAvailedOutOfMultipleOffers(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();
		int transactionRowNum = 118;
		int paymentTypeRowNum = 353;
		// 3 a) Offer Key
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 2.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 8.00");
		String offerKey = "CashCardOnly@2908,cconly@2906";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		// e.g.CashCardOnly@2908,cconly@2906"(without quotes).
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		// Amount: 10.00
		// Discount awarded: 2.00
		// Net amount payable 8.00
		
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1290
	@Test(description = "Verifying Cashcard offer availed for discount passed as parameter in offer key field ", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingCashcardOfferAvailedForDiscountPassedAsParameterInOfferKeyField(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();
		int transactionRowNum = 118;
		int paymentTypeRowNum = 353;
		// 3 a) Offer Key
		String offerKey = "discountpara@2898@1";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		// e.g.discountpara@2898@1(without quotes).
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1289
	@Test(description = "Verify Cashcard offer with disallowed bin that is restricted for credit card", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyCashcardOfferWithDisallowedBinThatIsRestrictedForCreditCard(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();
		int transactionRowNum = 118;
		int paymentTypeRowNum = 353;
		// 3 a) Offer Key
		String offerKey = "CashCardOnly@2908";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		// e.g.CashCardOnly@2908(without quotes).
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		// Amount: 10.00
		// Discount: 2.00
		// Net amount payable 8.00
		
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 2.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 8.00");
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1288
	@Test(description = "Verifying Cashcard offer with enabled checkbox", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingCashcardOfferWithEnabledCheckbox(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();
		int transactionRowNum = 118;
		int paymentTypeRowNum = 353;
		// 3 a) Offer Key
		String offerKey = "cashcardenab@2952";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		// e.g.cashcardenab@2952(without quotes).
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		// Amount: 10.00
		// Discount: 1.00
		// Net amount payable 9.00
		
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1287
	@Test(description = "Verify Cashcard Offer Failure due to expiry date", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyCashcardOfferFailureDueToExpiryDate(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();
		int transactionRowNum = 118;
		int paymentTypeRowNum = 353;

		// 3 a) Offer Key
		String offerKey = "ExpiredofferCC@2874";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		// e.g.ExpiredofferCC@2874(without quotes).
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Verify intermediate processing page showing following message.
		// "sorry, we regret to inform you that we are unable to process your
		// discount on your transaction as offer is not valid"

		String strSorryMessage_expected = "Sorry! We regret to inform you that we are unable to process discount on your transaction"
				+ " as the offer is not valid.";
		testConfig.putRunTimeProperty("strSorryMessage_expected",
				strSorryMessage_expected);

		helper.getSorryMessageDisplayedOnProcessingPage();

		String strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);
		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1286
	@Test(description = "Verify Cashcard Offer availed with Maximum discount out of multiple offers", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyCashcardOfferAvailedWithMaximumDiscountOutOfMultipleOffers(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();
		int transactionRowNum = 118;
		int paymentTypeRowNum = 353;
		// 3 a) Offer Key
		String offerKey = "CCmax1@2870,CCmax2@2872";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		// e.g."CCmax1@2870,CCmax2@2872"(without quotes).
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		// Amount: 10.00
		// Discount awarded: 3.00
		// Net amount payable 7.00
		
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 3.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 7.00");
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1285
	@Test(description = "Verify Cashcard Offer when Offer count limit exceeds", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyCashcardOfferWhenOfferCountLimitExceeds(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();
		// Go back to home page
		Browser.navigateToURL(testConfig,
				testConfig.getRunTimeProperty("AdminPortalHome"));

		int transactionRowNum = 118;
		int paymentTypeRowNum = 353;
		// 3 a) Offer Key
		String offerKey = "offercountset@2866";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		// e.g.cashcardfailure@2868(without quotes).
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();

		String strSorryMessage_expected = "Sorry! We regret to inform you that we are unable to process discount on your transaction"
				+ " as the offer is not valid.";
		testConfig.putRunTimeProperty("strSorryMessage_expected",
				strSorryMessage_expected);

		helper.getSorryMessageDisplayedOnProcessingPage();

		String strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1284
	@Test(description = "Verify Cashcard offer availed successfully when  Minimum Amount criteria is met", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyCashcardOfferAvailedWhenMinimumAmountCriteriaMet(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		helper.DoLogin();

		// Scenario 1
		// for successfully offer availed for 'Minimum Amount' Criteria.

		int transactionRowNum = 118;
		int paymentTypeRowNum = 353;
		// 3 a) Offer Key
		String offerKey = "Cashcard@2860";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		// e.g."CCmax1@2870,CCmax2@2872"(without quotes).
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		transactionRowNum = 118;
		paymentTypeRowNum = 353;
		// 3 a) Offer Key
		String offerKey2 = "cashcardfailure@2868";
		testConfig.putRunTimeProperty("offerKey", offerKey2);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer keys with comma separation in 'offer Key' field
		// e.g.cashcardfailure@2868(without quotes).
		helper.trans.FillTransactionDetails(transactionRowNum);

		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'CashCard' option
		helper.cashCardTab = payment.clickCashCardTab();
		// e.g. "Airtel Money" on
		// payment option page.
		helper.selectBankInCashCard(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Verify intermediate processing page showing following message.
		// "sorry, we regret to inform you that we are unable to process your
		// discount on your transaction as offer is not valid"

		String strSorryMessage_expected = "Sorry! This offer is not valid.To avail this offer"
				+ " amount must be atleast 11.00";
		testConfig.putRunTimeProperty("strSorryMessage_expected",
				strSorryMessage_expected);

		helper.getSorryMessageDisplayedOnProcessingPage();

		String strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);
		// b) Verify system redirects to Airtel Money website
		// "https://ecommerce.airtelmoney.in"

		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}
}
