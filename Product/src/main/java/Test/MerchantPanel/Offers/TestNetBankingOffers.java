package Test.MerchantPanel.Offers;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;

public class TestNetBankingOffers extends TestBase {

	// This class deals with cash card offers

	// Test-Case ID: Product-1350
	@Test(description = "Verifying 'Net Payable Amount'  equal or more than 1 for Netbanking offer", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNetPayableAmountEqualOrMoreThanOneForNetbankingOffer(
			Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();
		// Amount: 10.00
		// Discount awarded: 9.00
		// Net amount payable 1.00
		// Maximum Discount: 10
		HashMap<String, String> TransactionAmounts = new HashMap<>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 9.00");
		TransactionAmounts.put("Maximum_Discount_Text",
				"Maximum Discount: 10.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 1.00");
		String offerKey = "NBNetPayableAMT@3028";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();

		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1348
	@Test(description = "Verify NetBanking offer having NetBanking active and CashCard active with 'citybank reward point' selected.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyNetbankingOfferHavingNetbankingActiveAndCashcardActiveWithCitybankRewardPointSelected(
			Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		HashMap<String, String> TransactionAmounts = new HashMap<>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		String offerKey = "CashCardCityselected@3018";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();

		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1347
	@Test(description = "Verifying Netbanking Offer by clicking on'Check Offer'link.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNetbankingOfferByClickingOnCheckOfferLink(
			Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();
		// Amount: 10.00
		// Discount awarded: 2.00
		// Net amount payable 8.00
		HashMap<String, String> TransactionAmounts = new HashMap<>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 2.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 8.00");
		String offerKey = "checkofferlink@3016";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// Offer message to be displayed
		String strOfferMessage = "Payment method selected is valid for offer. You will get a discount "
				+ "of Rs 2. Please complete the transaction to avail the offer.";
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
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on "Check Offer" link.
		helper.nbTab.clickOnCheckOfferLink();
		// Verify after step 6
		Helper.compareContains(testConfig, "Offer Message",
				testConfig.getRunTimeProperty("checkOfferMessage_expected"),
				testConfig.getRunTimeProperty("checkOfferMessage_actual"));
		// 7) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step 7
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1346
	@Test(description = "Verifying Netbanking Offer failure when offer valid for specific Bank only.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNetbankingOfferFailureWhenOfferValidForSpecificBankOnly(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();

		// Offer key: NBHDFCOnly@3014 is valid for HDFC Bank only
		String offerKey = "NBHDFCOnly@3014";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Offer message to be displayed
		String strSorryMessage_expected = "Sorry! We regret to inform you that we are unable to process"
				+ " discount on your transaction as the offer is not valid.";

		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();
		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// a) Verify Retry page with its ui elements Retry and Continue showing
		// following message.
		helper.getSorryMessageDisplayedOnProcessingPage();
		String strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1283
	@Test(description = "Verifying Netbanking Offer when checkbox is enabled/disabled.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNetbankingOfferWhenCheckboxIsEnabledDisabled(
			Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();
		// --------------Scenario for "Don't allow transaction" checkbox
		// enabled-----------------------
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		HashMap<String, String> TransactionAmounts = new HashMap<>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		String offerKey = "netbanking@2852";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();

		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		// ----------Scenario for "Don't allow transaction" checkbox
		// Disabled-----------------------
		// Amount: 10.00
		// Discount awarded: 5.00
		// Net amount payable 5.00
		TransactionAmounts.put("Discount_Text", "Discount awarded: 5.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 5.00");
		offerKey = "netbanking2@2854";
		testConfig.putRunTimeProperty("offerKey", offerKey);

		helper.GetTestTransactionPage();

		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1345
	@Test(description = "NetBanking Offer availed out of multiple offers having discount in Rupee and in percent", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_NetbankingOfferAvailedOutOfMultipleOffersHavingDiscountInRupeeAndInPercent(
			Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();
		// Amount: 10.00
		// Discount awarded: 3.00
		// Net amount payable 7.00
		HashMap<String, String> TransactionAmounts = new HashMap<String, String>();
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 3.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 7.00");

		// Offer Key: NBdisinPercent@3012 is set with discount in percent
		// Discount : 30%
		String offerKey = "NBdisinPercent@3012";
		// Offer Key: NBdisinRupee@3010 is set with discount in rupee
		// Discount : 2.00
		offerKey = offerKey + ",NBdisinRupee@3010";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();

		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1344
	@Test(description = "Verifying NetBanking specific offer is availed out of multiple offers", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNetbankingSpecificOfferIsAvailedOutOfMultipleOffers(
			Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();
		// Amount: 10.00
		// Discount awarded: 2.00
		// Net amount payable 8.00
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 2.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 8.00");

		// Offer Key: CreditCardOfferonly@3006 is active for credit card only.
		// Discount: 1.00
		String offerKey = "CreditCardOfferonly@3006";
		// Offer Key: NBdisinRupee@3010 is set with discount in rupee
		// Discount : 2.00
		offerKey = offerKey + ",NBdisinRupee@3010";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();

		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1343
	@Test(description = "Verifying NetBanking offer availed for discount passed as parameter", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNetbankingOfferAvailedForDiscountPassedAsParameter(
			Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");

		// Offer Key
		String offerKey = "NBDiscountPara@3004@1";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();

		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1342
	@Test(description = "Verify NetBanking Offer Failure due to expiry date", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyNetbankingOfferFailureDueToExpiryDate(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();

		// Offer key: NBExpiryOffer@3002 is no longer valid
		String offerKey = "NBExpiryOffer@3002";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Offer message to be displayed
		String strSorryMessage_expected = "Sorry! We regret to inform you that we are unable to process"
				+ " discount on your transaction as the offer is not valid.";
		
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();
		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// a) Verify Retry page with its ui elements Retry and Continue showing
		// following message.
		helper.getSorryMessageDisplayedOnProcessingPage();
		String strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1339
	@Test(description = "Verify Netbanking Offer availed with Maximum discount out of multiple offers", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyNetbankingOfferAvailedWithMaximumDiscountOutOfMultipleOffers(
			Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();
		// Amount: 10.00
		// Discount awarded: 2.00
		// Net amount payable 8.00
		HashMap<String, String > TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 2.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 8.00");

		// Offer Key: NetbankingMaxdis@2992
		// Discount: 1.00
		String offerKey = "CreditCardOfferonly@3006";
		// Offer Key: NetbankingMaxdis2@2994
		// Discount : 2.00
		offerKey = offerKey + ",NBdisinRupee@3010";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();

		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1338
	@Test(description = "Verify NetBanking offer failure when Offer count limit exceeds.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyNetbankingOfferFailureWhenOfferCountLimitExceeds(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();

		String offerKey = "NetBankOfferCount@2990";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Offer message to be displayed
		String strSorryMessage_expected = "Sorry! We regret to inform you that we are unable to process"
				+ " discount on your transaction as the offer is not valid.";
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();
		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// a) Verify Retry page with its ui elements Retry and Continue showing
		// following message.
		helper.getSorryMessageDisplayedOnProcessingPage();
		String strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}

	// Test-Case ID: Product-1337
	@Test(description = "Verify NetBanking Offer for Minimum Amount Criteria is met", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyNetbankingOfferForMinimumAmountCriteria(
			Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 120;
		int paymentTypeRowNum = 60;

		helper.DoLogin();
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		String offerKey = "NetBankMinAmtPass@2986";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();

		// 2) Enter value in 'Merchant Key' field
		// 3) Enter offer key in 'offer Key' field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'Netbanking' option and select Bank e.g.
		// "Axis Bank Netbanking".
		helper.nbTab = payment.clickNBTab();
		helper.nbTab.fillNetBankingPaymentDetails(paymentTypeRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);
		// Test-Case ID: Product-1337 Scenario 2
		transactionRowNum = 120;
		paymentTypeRowNum = 60;

		offerKey = "NetBankMinAmtFail@2988";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Offer message to be displayed
		String strSorryMessage_expected = "Sorry! This offer is not valid.To avail this offer amount must be atleast 11.00";
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
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// a) Verify Retry page with its ui elements Retry and Continue showing
		// following message.
		helper.getSorryMessageDisplayedOnProcessingPage();
		String strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);
		// b)Verify system redirects to Axis bank page
		// "https://www.axisbank.co.in/"
		
		helper.bankRedirectPage = new BankRedirectPage(testConfig);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData,
				paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}
}
