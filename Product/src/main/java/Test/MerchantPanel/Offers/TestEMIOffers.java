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

/**
 * This class deals with EMI offers
 * 
 */
public class TestEMIOffers extends TestBase {

	// Test-Case ID: Product-1350
	@Test(description = "Verifying 'Net Payable Amount' equal or more than1 for EMI Offer", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingNetPayableAmountEqualOrMoreThanOneForEMIOffer(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 124;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		String offerKey = "EMINetPayable@3032";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Original Amount: 10.00
		// Maximum Discount: 10.00
		// Discount Awarded: 9.00
		// Net Amount Payable: 1.00
		// EMI per month for 3 months: 0.33
		HashMap<String, String> TransactionAmounts = new HashMap<>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "Discount awarded: 9.00");
		TransactionAmounts.put("Maximum_Discount_Text",
				"Maximum Discount: 10.00");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 1.00");
		TransactionAmounts.put("EMI_Amount_Text",
				"EMI per month for 3 months: 0.33");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1341
	@Test(description = "Verify EMI offer failure when Max Amount per card limit exceeds.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyEmiOfferFailureWhenMaxAmountPerCardLimitExceeds(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 125;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		String offerKey = "MaxAmtperCard@3000";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		String failReason = "Card limit on amount reached. Total discount given "
				+ "till date = 1, maximum discount per card = 1.00";
		testConfig.putRunTimeProperty("Offer_Failure_Reason", failReason);

		String limitMessage = "Limit Exceeded! You have exceeded the limit to avail the benefit.";
		
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.validateLimitExceedMessagesOnProcessingPageForEMI(limitMessage);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1340
	@Test(description = " Verifying EMI offer failure when offer valid for specific Bank only", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingEmiOfferFailureWhenOfferValidForSpecificBankOnly(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 125;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		String offerKey = "EMIHDFC@2998";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		String failReason = "Discount not available on payment mode chosen by user.";
		testConfig.putRunTimeProperty("Offer_Failure_Reason", failReason);

		String limitMessage = "Sorry! We cannot give you the discount. We regret to inform you"
				+ " that we are unable to process discount on your transaction for this payment method.";
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.validateLimitExceedMessagesOnProcessingPageForEMI(limitMessage);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1336
	@Test(description = "Verify EMI offer with disallowed bin that is restricted for credit card", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyEmiOfferWithDisallowedBinThatIsRestrictedForCreditCard(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 126;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		String offerKey = "CreditcardBinOnly@2980";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Original Amount: 10.00
		// Discount Awarded: 1
		// Net Amount Payable: 9
		// EMI per month for 3 months: 3
		HashMap<String, String> TransactionAmounts = new HashMap<String, String>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "discount awarded: 1");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		TransactionAmounts.put("EMI_Amount_Text",
				"EMI per month for 3 months: 3");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1329
	@Test(description = "Verify EMI offer failure when Max Count per card limit exceeds.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyEMIOfferFailureWhenMaxCountPerCardLimitExceeds(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 125;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		
		String offerKey = "EMIspecificOffercount@2978";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		String failReason = "Card limit reached. No of times discount"
				+ " availed = 1, maximum times discount per card = 1";
		testConfig.putRunTimeProperty("Offer_Failure_Reason", failReason);

		String limitMessage = "Limit Exceeded! You have exceeded the limit to "
				+ "avail the benefit.Only 1 transaction(s) allowed per card.";
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.validateLimitExceedMessagesOnProcessingPageForEMI(limitMessage);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1326
	@Test(description = "EMI Offer availed out of multiple offers having discount in Rupee and in percent", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_EmiOfferAvailedOutOfMultipleOffersHavingDiscountInRupeeAndInPercent(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 127;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		// Offer Key: discInPercent@2966 is set with discount in percent
		// Discount : 30%
		String offerKey = "discInPercent@2966";
		// Offer Key: DiscInRupee@2968 is set with discount in rupee
		// Discount : 2.00
		offerKey = offerKey + ",DiscInRupee@2968";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Original Amount: 10.00
		// Discount Awarded: 3
		// Net Amount Payable: 7
		// EMI per month for 3 months: 2.33
		HashMap<String, String> TransactionAmounts = new HashMap<String, String>(4);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "discount awarded: 3");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 7.00");
		TransactionAmounts.put("EMI_Amount_Text",
				"EMI per month for 3 months: 2.33");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("offeravailed", "discInPercent@2966");

		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1325
	@Test(description = "Verifying EMI specific offer is availed out of multiple offers", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingEMISpecificOfferIsAvailedOutOfMultipleOffers(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 126;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		// Offer Key:CreditCardonly@2964is active for credit card only.
		// Discount : 2.00
		String offerKey = "CreditCardonly@2964";
		// Offer Key: EMIOnly@2962 is active for EMI only.
		// Discount : 1.00
		offerKey = offerKey + ",EMIOnly@2962";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Original Amount: 10.00
		// Discount Awarded: 1
		// Net Amount Payable: 9
		// EMI per month for 3 months: 3
		HashMap<String, String> TransactionAmounts = new HashMap<String, String>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "discount awarded: 1");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		TransactionAmounts.put("EMI_Amount_Text",
				"EMI per month for 3 months: 3");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("offeravailed", "EMIOnly@2962");

		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1324
	@Test(description = "Verifying EMI offer availed for discount passed as parameter", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingEMIOfferAvailedForDiscountPassedAsParameter(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 126;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		String offerKey = "discountpara@2960@1";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Original Amount: 10.00
		// Discount Awarded: 1
		// Net Amount Payable: 9
		// EMI per month for 3 months: 3
		HashMap<String, String> TransactionAmounts = new HashMap<String, String>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "discount awarded: 1");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		TransactionAmounts.put("EMI_Amount_Text",
				"EMI per month for 3 months: 3");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("offeravailed", "discountpara@2960");
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1323
	@Test(description = "Verifying EMI offer for disallowed bin when checkbox is disabled", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingEMIOfferForDisallowedBinWhenCheckboxIsDisabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 125;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		String offerKey = "EMIdisAlloweddisabled@2958";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		String failReason = "Discount not available on payment mode chosen by user.";
		testConfig.putRunTimeProperty("Offer_Failure_Reason", failReason);

		String limitMessage = "Sorry! We cannot give you the discount. We regret "
				+ "to inform you that we are unable to process discount on your "
				+ "transaction for this payment method.";
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.validateLimitExceedMessagesOnProcessingPageForEMI(limitMessage);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Test-Case ID: Product-1322
	@Test(description = "Verifying EMI offer for allowed bin when checkbox is disabled", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingEMIOfferForAllowedBinWhenCheckboxIsDisabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 126;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		String offerKey = "EMIAlloweddisabled@2956";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Original Amount: 10.00
		// Discount Awarded: 1
		// Net Amount Payable: 9
		// EMI per month for 3 months: 3
		HashMap<String, String> TransactionAmounts = new HashMap<String, String>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "discount awarded: 1");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		TransactionAmounts.put("EMI_Amount_Text",
				"EMI per month for 3 months: 3");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig
				.putRunTimeProperty("offeravailed", "EMIAlloweddisabled@2956");
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Test-Case ID: Product-1321
	@Test(description = "Verifying EMI offer for disallowed bin when checkbox is disabled", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingEMIOfferForDisallowedBinWhenCheckboxIsEnabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 125;
		int paymentTypeRowNum = 368;
		int cardRowNum = 1;
		String offerKey = "EMIDisallowedbin@2954";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		
		testConfig.putRunTimeProperty("error_Message", "Wrong payment method selected.");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		String failReason = "Discount not available on payment mode chosen by user.";
		testConfig.putRunTimeProperty("Offer_Failure_Reason", failReason);

		String transactionFailedMessageExpected = "Wrong Payment Type Selected";
		testConfig.putRunTimeProperty("transactionFailedMessageExpected",
				transactionFailedMessageExpected);
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		testResponse.overrideExpectedTransactionData = true;
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1320
	@Test(description = "Verifying EMI offer for allowed bin when checkbox is enabled.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingEMIOfferForAllowedBinWhenCheckboxIsEnabled(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 126;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		String offerKey = "EMIAllowedbin@2950";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Original Amount: 10.00
		// Discount Awarded: 1
		// Net Amount Payable: 9
		// EMI per month for 3 months: 3
		HashMap<String, String> TransactionAmounts = new HashMap<String, String>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "discount awarded: 1");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		TransactionAmounts.put("EMI_Amount_Text",
				"EMI per month for 3 months: 3");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("offeravailed", "EMIAllowedbin@2950");
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// verify discount and offer key
		// testConfig.putRunTimeProperty("offerKey", "discountpara@2960");
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1318
	@Test(description = "Verify EMI Offer Failure due to expiry date", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyEmiOfferFailureDueToExpiryDate(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 125;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		String offerKey = "ExpiredofferEMI@2948";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		String failReason = "Offer expired.";
		testConfig.putRunTimeProperty("Offer_Failure_Reason", failReason);

		String limitMessage = "Sorry! We regret to inform you that we are unable to "
				+ "process discount on your transaction as the offer is not valid.";
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.validateLimitExceedMessagesOnProcessingPageForEMI(limitMessage);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1317
	@Test(description = "Verify EMI Offer availed with Maximum discount out of multiple offers", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyEmiOfferAvailedWithMaximumDiscountOutOfMultipleOffers(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 128;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		// Offer Key: OfferMaxdis1@2944
		// Discount:1.00
		String offerKey = "OfferMaxdis1@2944";
		// Offer Key: OfferMaxdis2@2946
		// Discount: 2.00
		offerKey = offerKey + ",OfferMaxdis2@2946";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Original Amount: 10.00
		// Discount Awarded: 3
		// Net Amount Payable: 7
		// EMI per month for 3 months: 2.33
		HashMap<String, String> TransactionAmounts = new HashMap<String, String>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "discount awarded: 2");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 8.00");
		TransactionAmounts.put("EMI_Amount_Text",
				"EMI per month for 3 months: 2.67");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("offeravailed", "OfferMaxdis2@2946");

		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1316
	@Test(description = "Verify EMI offer failure when Offer count limit exceeds.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyEmiOfferFailureWhenOfferCountLimitExceeds(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 125;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		String offerKey = "EMIminAmt@2940";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		String failReason = "Offer count exhausted.";
		testConfig.putRunTimeProperty("Offer_Failure_Reason", failReason);

		String limitMessage = "Sorry! We regret to inform you that we are unable to "
				+ "process discount on your transaction as the offer is not valid.";
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.validateLimitExceedMessagesOnProcessingPageForEMI(limitMessage);

		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1315
	@Test(description = "Verify EMI Offer for Minimum Amount Criteria", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyEMIOfferForMinimumAmountCriteria(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum = 126;
		int paymentTypeRowNum = 257;
		int cardRowNum = 1;
		// Offer Key: EMIMinOfferpass@2936
		// Minimum Amount:9
		String offerKey = "EMIMinOfferpass@2936";
		// Offer Key: EMIMinOfferFail@2938
		// Minimum Amount: 11.00
		offerKey = offerKey + ",EMIMinOfferFail@2938";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		// Original Amount: 10.00
		// Discount Awarded: 1
		// Net Amount Payable: 9
		// EMI per month for 3 months: 3.00
		HashMap<String, String> TransactionAmounts = new HashMap<String, String>(3);
		TransactionAmounts.put("Amount_Text", " Amount: 10.00");
		TransactionAmounts.put("Discount_Text", "discount awarded: 1");
		TransactionAmounts.put("Payable_Amount_Text",
				"Net amount payable: 9.00");
		TransactionAmounts.put("EMI_Amount_Text",
				"EMI per month for 3 months: 3");
		testConfig.putRunTimeProperty("addedon",
				Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("offeravailed", "EMIMinOfferpass@2936");

		helper.DoLogin();
		// 1) Click on 'Test Transaction' link
		helper.GetTestTransactionPage();
		// 2) Enter value in 'Merchant Key' field
		// 3) Enter Offer Key in offer key field.
		helper.trans.FillTransactionDetails(transactionRowNum);
		// 4) Click on Submit Button.
		helper.trans.clickSubmitButton();

		PaymentOptionsPage payment = new PaymentOptionsPage(testConfig);
		// 5) Select 'EMI" option on payment option page and select Bank:
		// Axis, Duration: 3 months and Enter card details
		helper.emiTab = payment.clickEMITab();
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);
		helper.emiTab.FillCardDetails(cardRowNum);
		// 6) Click on 'pay Now' button.
		payment.clickPayNowButton();
		// Verify after step6.
		// a) Transaction processing page displays as following.
		helper.verifyTransactionAmountsDisplayed(TransactionAmounts);
		// verify discount and offer key
		TestResponsePage testResponse = new TestResponsePage(testConfig);
		// b)Verify test response page as normal for EMI with values
		testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// c) verify following values in DB in table txn_offer
		// status ,offer type ,offer key, discount for payuid in test response
		// for merchant id: 5913
		testResponse
				.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

}