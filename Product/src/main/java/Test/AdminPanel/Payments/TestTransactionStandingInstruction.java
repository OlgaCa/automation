package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.DCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestTransactionStandingInstruction extends TestBase{
	
	//Test Case ID : Product-293
	@Test(description = "Save Card with SI, for non SI merchant", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void TestSaveCardSINonSImerchant(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// Store credit card
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum=1;
		int storeCardDetailsRowNum = 1;
		boolean verifythroughSeamless=false;
		//Pass SI=2
		testConfig.putRunTimeProperty("SI", "2");
		testConfig.putRunTimeProperty("UseStoredDetails", "AutomationMerchantSI");
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		testConfig.putRunTimeProperty("key", "allow_standing_instruction");

		//make transaction, save card and verify test response
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//verify saved store card, do transaction using saved card and verify test response
		helper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum,verifythroughSeamless, ExpectedResponsePage.TestResponsePage);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1472 
	@Test(description = "Verifying UI elements displayed for 'CC' and 'DC' when SI Value passed is 2", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingUiElementsDisplayedForCcAndDcWhenSIValuePassedIsTwo(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum =122;
		int paymentTypeRowNum = 370;
		// SI= 2
		testConfig.putRunTimeProperty("SI", "2");
		//--------- Scenario for Credit Card.-------------------
		helper.DoLogin();
		PaymentOptionsPage payment = helper
				.GetPaymentOptionPage(transactionRowNum);
		CCTab ccTab = payment.clickCCTab();
		ccTab.verifyStoreCardDisplayed();
		
		//--------- Scenario for Debit Card.--------------------
		DCTab dcTab = payment.clickDCTab();
		TestDataReader paymentTypeData = new TestDataReader(testConfig,
				"PaymentType");
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum,
				"Bank Name");
		dcTab.SelectDebitCard(debitCardName);
		dcTab.verifyStoreCardDisplayed();
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1466  
	@Test(description = "Verifying 'save' checkbox after deleting Saved card details when SI value passed is 2", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingSaveCheckboxAfterDeletingSavedCardDetailsWhenSiValuePassedIsTwo(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum =122;
		int paymentTypeRowNum = 366;
		int cardDetailsRowNum = 1;
		int storeCardRowNum =9;
		// SI= 2
		
		testConfig.putRunTimeProperty("SI", "2");
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		testConfig.putRunTimeProperty("check_si_merchant", "NO");
		helper.DoLogin();
		
		//Section 1 Steps1-4
		//Section 2 Steps 5-8
		TestResponsePage response = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		// Verify transaction response Array
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// Section 3  Step 9
		//Remove existing card from CC tab
		helper.doTransactionAndRemoveStoredCards(transactionRowNum, paymentTypeRowNum);		
		// Debit Card Scenario
		//Section 5-6 Steps 1-9
		storeCardRowNum =10;
		paymentTypeRowNum = 367;
		//Debit Card Scenario
		testConfig.removeRunTimeProperty("userString");
		TestResponsePage response_DC = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		// Validate Response
		response_DC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// Section 7-8 Steps 10-12
		helper.doTransactionAndRemoveStoredCards(transactionRowNum, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1464 
	@Test(description = "Verifying 'save' checkbox after deleting Saved card details when SI value passed is 1", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingSaveCheckboxAfterDeletingSavedCardDetailsWhenSiValuePassedIsOne(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum =122;
		int paymentTypeRowNum = 371;
		int cardDetailsRowNum = 1;
		int storeCardRowNum = 9;
		// SI= 1 defined_user_credetials
		testConfig.putRunTimeProperty("SI", "1");
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		testConfig.putRunTimeProperty("check_si_merchant", "NO");
		helper.DoLogin();
		
		//Section 1 Steps1-4
		//Section 2 Steps 5-8
		TestResponsePage response = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		// Verify transaction response Array
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// Section 3-4  Step 9-10
		//Remove existing card from CC tab
		helper.doTransactionAndRemoveStoredCards(transactionRowNum, paymentTypeRowNum);	
		// Debit Card Scenarios
		paymentTypeRowNum = 365;

		//Debit Card Scenario
		//Section 5-6 Steps 1-9
		testConfig.removeRunTimeProperty("userString");
		TestResponsePage response_DC = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		// Validate Response
		response_DC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// Section 7-8 Steps 10-12
		helper.doTransactionAndRemoveStoredCards(transactionRowNum, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	//Failing bug id :#37785
	// Test Case Id: Product -1461 
	@Test(description = "'Standing Instruction' Transaction when SI value passed as 1 and Debit card details are not saved", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_StandingInstructionTransactionWhenSIValuePassedas1AndDebitCardDetailsAreNotSaved(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum =122;
		int paymentTypeRowNum = 367;
		int cardDetailsRowNum = 1;

		// SI= 1
		testConfig.putRunTimeProperty("SI", "1");
		testConfig.putRunTimeProperty("no_cardToken", "yes");
		// Section 1 Steps 1-5
		// Do a Test Transaction
		helper.DoLogin();
		helper.GetTestTransactionPage();
		TestResponsePage response = (TestResponsePage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Verify Response 
		//Section 2 Steps 6-7
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case Id: Product-1449 
	@Test(description = "Verifying SI Transaction using saved card details for Standing Information through Credit Card", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingSITransactionUsingSavedCardDetailsForStandingInformationThroughCreditCard(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum =122;
		int paymentTypeRowNum = 371;
		int cardDetailsRowNum = 1;
		int storeCardRowNum =9;
	
		testConfig.putRunTimeProperty("SI", "1");
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		testConfig.putRunTimeProperty("check_si_merchant", "NO");
		helper.DoLogin();
		
		//Section 1 Steps1-4
		//Section 2 Steps 5-8
		TestResponsePage response = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		// Verify transaction response Array
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//Section 3-4
		//Steps 8 -10
		response = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum, paymentTypeRowNum, storeCardRowNum, false, ExpectedResponsePage.TestResponsePage);
		// Validate Response
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case Id: Product-1448 
	@Test(description = "'Standing Instruction' Transaction when SI value passed 2 and Card details are not saved.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_StandingInstructionTransactionWhenSIValuePassed2AndCardDetailsAreNotSaved(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum =122;
		int paymentTypeRowNum = 371;
		int cardDetailsRowNum = 1;

		
		testConfig.putRunTimeProperty("SI", "2");
		testConfig.putRunTimeProperty("no_cardToken", "yes");
		// Scenario for Credit Card.
		helper.DoLogin();
		helper.GetTestTransactionPage();
		TestResponsePage response = (TestResponsePage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Verify Response 
		//Section 2 Steps 6-7
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// Debit Card Scenario
		paymentTypeRowNum = 365;
		testConfig.removeRunTimeProperty("UseStoredDetails");
		helper.GetTestTransactionPage();
		response = (TestResponsePage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//Verify Response 
		//Section 2 Steps 6-7
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1447 
	@Test(description = "Verifying 'Standing Instruction' Transaction when SI value passed is invalid and saving card details", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingStandingInstructionTransactionWhenSIValuePassedIsInvalidAndSavingCardDetails(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum =122;
		int paymentTypeRowNum = 371;
		int cardDetailsRowNum = 1;
		int storeCardRowNum = 9;
		testConfig.putRunTimeProperty("SI", "0");
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		testConfig.putRunTimeProperty("check_si_merchant", "NO");
		helper.DoLogin();
		//Scenario For Credit Card
		//Section 1 Steps1-4
		//Section 2 Steps 5-8
		TestResponsePage response = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		// Verify transaction response Array
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//Scenario For Debit Card
		//Section 3 Steps1-5
		//Section 4 Steps 6-9
		paymentTypeRowNum = 365;
		storeCardRowNum = 10;
		response = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		// Verify transaction response Array
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1446 
	@Test(description = "Verifying 'Standing Instruction' Transaction when SI value passed as 2 and card details saved", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingStandingInstructionTransactionWhenSIValuePassedAs2AndCardDetailsSaved(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum =122;
		int paymentTypeRowNum = 371;
		int cardDetailsRowNum = 1;
		int storeCardRowNum =9;
		// SI= 2
		
		testConfig.putRunTimeProperty("SI", "2");
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		testConfig.putRunTimeProperty("check_si_merchant", "NO");
		helper.DoLogin();
		
		//Section 1 Steps1-4
		//Section 2 Steps 5-8
		TestResponsePage response = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		// Verify transaction response Array
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// DC Scenario
		//Section 3-4 Steps1-9
		paymentTypeRowNum = 365;
		response = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		// Verify transaction response Array
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1445 
	@Test(description = "Verifying 'Standing Instruction' Transaction when SI value passed as1 and card details saved", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingStandingInstructionTransactionWhenSIValuePassedAs1AndCardDetailsSaved(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		int transactionRowNum =122;
		int paymentTypeRowNum = 371;
		int cardDetailsRowNum = 1;
		int storeCardRowNum=9;
		
		testConfig.putRunTimeProperty("SI", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		testConfig.putRunTimeProperty("check_si_merchant", "NO");

		helper.DoLogin();
		// Credit Card Scenario
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		TestResponsePage response = (TestResponsePage) helper
				.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,
						cardDetailsRowNum, storeCardRowNum,
						ExpectedResponsePage.TestResponsePage);
		// Verify transaction response Array
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		// Debit Card Scenario
		paymentTypeRowNum = 365;
		storeCardRowNum = 10;
		response = (TestResponsePage) helper.DoPaymentAndSaveCard(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				storeCardRowNum, ExpectedResponsePage.TestResponsePage);
		// Validate Transaction Response
		response.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-1470 
	@Test(description = "Verifying UI elements displayed for 'CC' and 'DC' when SI Value passed is 1", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingUiElementsForCcAndDcWhenSIValuePassedIs1(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 122;
		// SI= 1
		
		testConfig.putRunTimeProperty("SI", "1");
		helper.DoLogin();
		//Scenario for credit card
		PaymentOptionsPage payment = helper
				.GetPaymentOptionPage(transactionRowNum);
		CCTab ccTab = payment.clickCCTab();
		// Verify that Save this Card Link does not appear
		ccTab.verifyStoreCardDoesNotExists();
		ccTab.verifyStoreCardCheckboxSetAsChecked();
		// Debit card scenario Scenario
		DCTab dcTab = payment.clickDCTab();
		int paymentTypeRow= 365;
		TestDataReader data = new TestDataReader(testConfig, "PaymentType");
		String bankName = data.GetData(paymentTypeRow, "Bank Name");
		dcTab.SelectDebitCard(bankName);
		// verify Save This Card link is available
		dcTab.verifyStoreCardDisplayed();
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test-Case ID: Product-2012 
	@Test(description = "Verifying UI elements displayed for 'CC' and 'DC' when SI Value is blank", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void test_VerifyingUIElementsDisplayedForCCAndDcWhenSiValueIsBlank(
			Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		int transactionRowNum = 122;
		testConfig.putRunTimeProperty("SI", "");
		helper.DoLogin();
		// Scenario for credit card
		PaymentOptionsPage payment = helper
				.GetPaymentOptionPage(transactionRowNum);
		CCTab ccTab = payment.clickCCTab();
		// a) Verify that "Store this Card" link should be displayed on 'Credit
		// Card' tab.
		// b) Checkbox "Save card number and expiry for next transaction" should
		// be displayed as unchecked.
		ccTab.verifyStoreCardCheckboxSetAsUnchecked();
		// Debit card scenario Scenario
		DCTab dcTab = payment.clickDCTab();
		int paymentTypeRow = 365;
		TestDataReader data = new TestDataReader(testConfig, "PaymentType");
		String bankName = data.GetData(paymentTypeRow, "Bank Name");
		dcTab.SelectDebitCard(bankName);
		// verify Save This Card link is available
		dcTab.verifyStoreCardDisplayed();
		Assert.assertTrue(testConfig.getTestResult());
	}


}
