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

public class ZeroNonZeroOffer extends TestBase {

	// Test-Case ID: Product-1298
	@Test(description = "Verifying NonZero offer for allowed bin when checkbox is disabled", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNonzeroOfferForAllowedBinWhenCheckboxIsDisabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 120;
		int paymentTypeRowNum = 366;
		int cardRow = 1;
		helper.DoLogin();

		String offerKey = "nonzero_dbled@2896";
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);

		// 5) Enter Credit Card details where Card number is having bin"512345".
		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();
		// verify amounts displayed on processing page
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);

		TestResponsePage testResponse = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		// Product-1298 Scenario 2

		transactionRowNum = 120;
		paymentTypeRowNum = 367;
		cardRow = 1;

		offerKey = "nonzero_dbled@2896";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		// 5) Enter Debit Card details where Card number is having bin"512345".
		helper.dcTab = payment.clickDCTab();
		TestDataReader paymentTypeData2 = new TestDataReader(testConfig,
				"PaymentType");

		String debitCardName = paymentTypeData2.GetData(paymentTypeRowNum,
				"Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);

		helper.dcTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();
		// verify amounts displayed on processing page
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);

		testResponse = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1297
	@Test(description = "Verifying NonZero offer for disallowed bin when checkbox is disabled", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNonzeroOfferForDisallowedBinWhenCheckboxIsDisabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 119;
		int paymentTypeRowNum = 366;
		int cardRow = 1;
		helper.DoLogin();

		String offerKey = "nonzerowithuncheck@2888";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		String strSorryMessage_expected = "Sorry! We cannot give you the discount. We regret to inform"
				+ " you that we are unable to process discount on your transaction for this payment method.";
		testConfig.putRunTimeProperty("strSorryMessage_expected",
				strSorryMessage_expected);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("Offer_Failure_Reason",
				"Discount not available on payment mode chosen by user.");
		testConfig.putRunTimeProperty("nonExistantOfferKey", "Yes");

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);

		// 5) Enter Credit Card details where Card number is having bin"512345".
		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();
		// Verify following after step6:
		// a) Intermediate Processing page is displayed showing message.
		helper.getSorryMessageDisplayedOnProcessingPage();
		String strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);

		TestResponsePage testResponse = new TestResponsePage(testConfig);

		// b) Verify test response page as normal for 'Credit card' with
		// following values:
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// b) Verify following values in DB in table txn_offer
		// status, Offer type, offer Key, discount for payuid in test response
		// for merchant id:
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		transactionRowNum = 119;
		paymentTypeRowNum = 367;
		cardRow = 1;

		offerKey = "nonzerowithuncheck@2888";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		strSorryMessage_expected = "Sorry! We cannot give you the discount. We regret to inform"
				+ " you that we are unable to process discount on your transaction for this payment method.";
		testConfig.putRunTimeProperty("strSorryMessage_expected",
				strSorryMessage_expected);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("Offer_Failure_Reason",
				"Discount not available on payment mode chosen by user.");
		testConfig.putRunTimeProperty("nonExistantOfferKey", "Yes");

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		// 5) Enter Debit Card details where Card number is having bin"512345".
		helper.dcTab = payment.clickDCTab();
		TestDataReader paymentTypeData2 = new TestDataReader(testConfig,
				"PaymentType");

		String debitCardName = paymentTypeData2.GetData(paymentTypeRowNum,
				"Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);

		helper.dcTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();
		// Verify following after step6:
		// a) Intermediate Processing page is displayed showing message.
		helper.getSorryMessageDisplayedOnProcessingPage();
		strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);

		testResponse = new TestResponsePage(testConfig);

		// b) Verify test response page as normal for 'Credit card' with
		// following values:
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// b) Verify following values in DB in table txn_offer
		// status, Offer type, offer Key, discount for payuid in test response
		// for merchant id:
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1296
	@Test(description = "Verifying Zero offer for allowed bin when checkbox is Enabled", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingZeroOfferForAllowedBinWhenCheckboxIsEnabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 119;
		int paymentTypeRowNum = 366;
		int cardRow = 1;
		helper.DoLogin();

		String offerKey = "Zerowithcheck2@2894";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);

		// 5) Enter Credit Card details where Card number is having bin"512345".
		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();

		TestResponsePage testResponse = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		// Product-1296 Scenario 2

		transactionRowNum = 119;
		paymentTypeRowNum = 367;
		cardRow = 1;

		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		// 5) Enter Debit Card details where Card number is having bin"512345".
		helper.dcTab = payment.clickDCTab();
		TestDataReader paymentTypeData2 = new TestDataReader(testConfig,
				"PaymentType");

		String debitCardName = paymentTypeData2.GetData(paymentTypeRowNum,
				"Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);

		helper.dcTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();

		testResponse = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1295
	@Test(description = "Zero offer for disallowed bin when checkbox is Enabled", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_ZeroOfferForDisallowedBinWhenCheckboxIsEnabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 119;
		int paymentTypeRowNum = 382;
		int cardRow = 1;
		helper.DoLogin();

		String offerKey = "Zerowithcheck@2882";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		String strSorryMessage_expected = "Sorry! We cannot give you the discount. We regret to inform"
				+ " you that we are unable to process discount on your transaction for this payment method.";
		testConfig.putRunTimeProperty("strSorryMessage_expected",
				strSorryMessage_expected);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("Offer_Failure_Reason",
				"Discount not available on payment mode chosen by user.");
		testConfig.putRunTimeProperty("nonExistantOfferKey", "Yes");

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);

		// 5) Enter Credit Card details where Card number is having bin"512345".
		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();
		// Verify following after step6:
		// a) Intermediate Processing page is displayed showing message.
		helper.getSorryMessageDisplayedOnProcessingPage();
		String strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);

		TestResponsePage testResponse = new TestResponsePage(testConfig);

		// b) Verify test response page as normal for 'Credit card' with
		// following values:
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// b) Verify following values in DB in table txn_offer
		// status, Offer type, offer Key, discount for payuid in test response
		// for merchant id:
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		transactionRowNum = 119;
		paymentTypeRowNum = 383;
		cardRow = 1;

		offerKey = "Zerowithcheck@2882";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		strSorryMessage_expected = "Sorry! We cannot give you the discount. We regret to inform"
				+ " you that we are unable to process discount on your transaction for this payment method.";
		testConfig.putRunTimeProperty("strSorryMessage_expected",
				strSorryMessage_expected);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("Offer_Failure_Reason",
				"Discount not available on payment mode chosen by user.");
		testConfig.putRunTimeProperty("nonExistantOfferKey", "Yes");

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		// 5) Enter Debit Card details where Card number is having bin"512345".
		helper.dcTab = payment.clickDCTab();
		TestDataReader paymentTypeData2 = new TestDataReader(testConfig,
				"PaymentType");

		String debitCardName = paymentTypeData2.GetData(paymentTypeRowNum,
				"Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);

		helper.dcTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();
		// Verify following after step6:
		// a) Intermediate Processing page is displayed showing message.
		helper.getSorryMessageDisplayedOnProcessingPage();
		strSorryMessage_Actual = testConfig
				.getRunTimeProperty("strSorryMessage_Actual");
		Helper.compareEquals(testConfig, "Sorry Message ",
				strSorryMessage_expected, strSorryMessage_Actual);

		testResponse = new TestResponsePage(testConfig);

		// b) Verify test response page as normal for 'Credit card' with
		// following values:
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// b) Verify following values in DB in table txn_offer
		// status, Offer type, offer Key, discount for payuid in test response
		// for merchant id:
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// TODO test case incomplete due to issues
	// Test-Case ID: Product-1282
	@Test(description = "Verifying nonzero offer for disallowed bin when checkbox is enabled.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNonzeroOfferForDisallowedBinWhenCheckboxIsEnabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 119;
		int paymentTypeRowNum = 382;
		int cardRow = 1;
		helper.DoLogin();

		String offerKey = "offerfailure@2844";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		String strSorryMessage_expected = "Sorry! We cannot give you the discount. We regret to inform"
				+ " you that we are unable to process discount on your transaction for this payment method.";
		testConfig.putRunTimeProperty("strSorryMessage_expected",
				strSorryMessage_expected);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("Offer_Failure_Reason",
				"Discount not available on payment mode chosen by user.");
		testConfig.putRunTimeProperty("nonExistantOfferKey", "Yes");

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);

		// 5) Enter Credit Card details where Card number is having bin"512345".
		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();

		TestResponsePage testResponse = new TestResponsePage(testConfig);

		// b) Verify test response page as normal for 'Credit card' with
		// following values:
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// b) Verify following values in DB in table txn_offer
		// status, Offer type, offer Key, discount for payuid in test response
		// for merchant id:
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		transactionRowNum = 119;
		paymentTypeRowNum = 383;
		cardRow = 1;

		offerKey = "offerfailure@2844";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		strSorryMessage_expected = "Sorry! We cannot give you the discount. We regret to inform"
				+ " you that we are unable to process discount on your transaction for this payment method.";
		testConfig.putRunTimeProperty("strSorryMessage_expected",
				strSorryMessage_expected);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("Offer_Failure_Reason",
				"Discount not available on payment mode chosen by user.");
		testConfig.putRunTimeProperty("nonExistantOfferKey", "Yes");

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		// 5) Enter Debit Card details where Card number is having bin"512345".
		helper.dcTab = payment.clickDCTab();
		TestDataReader paymentTypeData2 = new TestDataReader(testConfig,
				"PaymentType");

		String debitCardName = paymentTypeData2.GetData(paymentTypeRowNum,
				"Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);

		helper.dcTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();

		testResponse = new TestResponsePage(testConfig);

		// b) Verify test response page as normal for 'Credit card' with
		// following values:
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// b) Verify following values in DB in table txn_offer
		// status, Offer type, offer Key, discount for payuid in test response
		// for merchant id:
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1276
	@Test(description = "Verifying nonzero offer for allowed bin when checkbox is enabled", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNonzeroOfferForAllowedBinWhenCheckboxIsEnabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 120;
		int paymentTypeRowNum = 366;
		int cardRow = 1;
		helper.DoLogin();

		String offerKey = "Checkoffer@2842";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		// Amount: 10.00
		// Discount awarded: 1.00
		// Net amount payable 9.00
		HashMap<String, String> TransactionAmounts = new HashMap<String, String>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 1.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);

		// 5) Enter Credit Card details where Card number is having bin"512345".
		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();
		// verify amounts displayed on processing page
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);

		TestResponsePage testResponse = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		// Product-1298 Scenario 2

		transactionRowNum = 120;
		paymentTypeRowNum = 367;
		cardRow = 1;

		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		
		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		// 5) Enter Debit Card details where Card number is having bin"512345".
		helper.dcTab = payment.clickDCTab();
		TestDataReader paymentTypeData2 = new TestDataReader(testConfig,
				"PaymentType");

		String debitCardName = paymentTypeData2.GetData(paymentTypeRowNum,
				"Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);

		helper.dcTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();
		// verify amounts displayed on processing page
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);

		testResponse = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1275
	@Test(description = "Verifying Zero offer for allowed bin when checkbox is disabled", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingZeroOfferForAllowedBinWhenCheckboxIsDisabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 119;
		int paymentTypeRowNum = 366;
		int cardRow = 1;
		helper.DoLogin();

		String offerKey = "cardbin@2836";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);

		// 5) Enter Credit Card details where Card number is having bin"512345".
		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();

		TestResponsePage testResponse = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		// Product-1275 Scenario 2

		transactionRowNum = 119;
		paymentTypeRowNum = 367;
		cardRow = 1;

		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		// 5) Enter Debit Card details where Card number is having bin"512345".
		helper.dcTab = payment.clickDCTab();
		TestDataReader paymentTypeData2 = new TestDataReader(testConfig,
				"PaymentType");

		String debitCardName = paymentTypeData2.GetData(paymentTypeRowNum,
				"Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);

		helper.dcTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();

		testResponse = new TestResponsePage(testConfig);

		// verify discount and offer key
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1274
	@Test(description = "Verifying Zero offer for disallowed bin when checkbox is disabled", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingZeroOfferForDisallowedBinWhenCheckboxIsDisabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 119;
		int paymentTypeRowNum = 366;
		int cardRow = 1;
		helper.DoLogin();

		String offerKey = "offercheck@2834";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("Offer_Failure_Reason",
				"Discount not available on payment mode chosen by user.");
		testConfig.putRunTimeProperty("nonExistantOfferKey", "Yes");

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);

		// 5) Enter Credit Card details where Card number is having bin"512345".
		helper.ccTab = payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();

		TestResponsePage testResponse = new TestResponsePage(testConfig);

		// b) Verify test response page as normal for 'Credit card' with
		// following values:
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// b) Verify following values in DB in table txn_offer
		// status, Offer type, offer Key, discount for payuid in test response
		// for merchant id:
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		transactionRowNum = 119;
		paymentTypeRowNum = 367;
		cardRow = 1;

		offerKey = "offercheck@2834";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("Offer_Failure_Reason",
				"Discount not available on payment mode chosen by user.");
		testConfig.putRunTimeProperty("nonExistantOfferKey", "Yes");

		helper.GetTestTransactionPage();

		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();

		// 5) Enter Debit Card details where Card number is having bin"512345".
		payment = new PaymentOptionsPage(testConfig);
		helper.dcTab = payment.clickDCTab();
		TestDataReader paymentTypeData2 = new TestDataReader(testConfig,
				"PaymentType");

		String debitCardName = paymentTypeData2.GetData(paymentTypeRowNum,
				"Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);

		helper.dcTab.FillCardDetails(cardRow);
		// click on pay now buttom
		payment.clickPayNowButton();
		testResponse = new TestResponsePage(testConfig);

		// b) Verify test response page as normal for 'Credit card' with
		// following values:
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// b) Verify following values in DB in table txn_offer
		// status, Offer type, offer Key, discount for payuid in test response
		// for merchant id:
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

}
