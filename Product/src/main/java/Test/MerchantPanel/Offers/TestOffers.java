package Test.MerchantPanel.Offers;

/**
 * Author: Vidya Priyadarshini
 */



import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.OfferPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Offers.OfferListPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.TestMerchantTransaction.TransactionMerchantHelper;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.TestBase;

public class TestOffers extends TestBase {

	@Test(description = "Verify offer creation", dataProvider = "GetTestConfig", timeOut=1200000, groups = "1")
	public void test_VerifyCreateAndEditOffer(Config testConfig)
	{
		int offerRow = 1;
		int merchantLoginRow = 1;
		Boolean isExpired = false;
		OffersHelper oHelper = new OffersHelper(testConfig);

		// login and create offer
		OfferListPage olPage = oHelper.doLoginAndCreateOffer(offerRow,merchantLoginRow, isExpired);

		// verify offer details from Offer list page
		olPage.verifyOfferEntry(offerRow);
		String offerTitle = testConfig.getRunTimeProperty("Offer Title");
		String id = oHelper.getOfferId(offerTitle);
		// click edit offer link 
		oHelper.OfferPage = oHelper.clickEditOfferLink(offerRow, id);
		oHelper.OfferPage.verifyEditOfferPage(offerRow);
		// change offer details 
		offerRow = 2; 

		oHelper.OfferPage.clearForm();
		//clear payment types
		testConfig.putRunTimeProperty("CC","");
		testConfig.putRunTimeProperty("DC","");
		testConfig.putRunTimeProperty("NB","");

		oHelper.OfferPage.fillOfferForm(offerRow,true, false); 
		// click submit 
		olPage =oHelper.OfferPage.clickSubmitButton(); 
		// verify edited offer details from Offer list page 
		olPage.verifyOfferEntry(offerRow);
		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verifies offer transactions for an existing offer
	 * 
	 * @param testConfig

	 */
	@Test(description = "Verify offer creation", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferTransactionAndOfferViewLink(Config testConfig)
	{
		// int offerRow = 1;
		int merchantLoginRow = 1;
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);

		OfferListPage offerListPage = new OfferListPage(testConfig);
		OffersHelper oHelper = new OffersHelper(testConfig);
		// set offer to use for transactions
		oHelper.setOfferInfo("ValidOffer");

		// login to admin
		helper.DoLogin();
		// make transaction with created offer
		helper.GetTestTransactionPage();
		String amount = "10.00";
		testConfig.putRunTimeProperty("amount", amount);
		testConfig.putRunTimeProperty("discount", "5.00");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;

		// verify amount,discount,offer key from response
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// make transaction with equal amount and discount values
		String expectedDiscount = "4.00";
		amount = "5.00";
		testConfig.putRunTimeProperty("amount", amount);
		testConfig.putRunTimeProperty("discount", expectedDiscount);
		// make transaction with created offer
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;

		// verify amount,discount,offer key from response
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		// make one rupee transaction and verify 0 discount
		// make transaction with created offer
		expectedDiscount = "0.00";
		amount = "1.00";
		testConfig.putRunTimeProperty("amount", amount);
		testConfig.putRunTimeProperty("discount", expectedDiscount);

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;

		// verify amount,discount, offer key for 1 rupee transaction
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		helper.testResponse.overrideExpectedTransactionData = false;

		// verify transaction shows up after clicking View Transaction link
		String transactionId = helper.testResponse.actualResponse.get("txnid");
		oHelper.doLoginAndViewOfferList(merchantLoginRow);

		String offerKey = testConfig.getRunTimeProperty("offerKey");
		// click view transaction link
		helper.mTPage = offerListPage.clickViewTransactionsLink(offerKey);
		helper.mTPage.SearchTransaction(transactionId);
		// verify offer key shows up in transactiondetailsPage
		oHelper.verifyViewTransactionOfferKey(offerKey);

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify offer for merchant test transaction", dataProvider = "GetTestConfig", timeOut=700000, groups = "1")
	public void test_VerifyOfferMerchantTransaction(Config testConfig)  {
		int merchantLoginRow = 1;
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		TransactionMerchantHelper mHelper = new TransactionMerchantHelper(testConfig, false);


		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);

		new OfferListPage(testConfig);
		OffersHelper oHelper = new OffersHelper(testConfig);
		// set offer to use for transactions
		oHelper.setOfferInfo("ValidOffer");

		//login as admin
		mHelper.DoLogin();
		mHelper.GetTestMerchantTransactionPage();
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "5.00");

		// do merchant test transaction
		OfferPage oPage = (OfferPage) mHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.OfferPage);
		mHelper.testResponse = oPage.clickBookButton();
		mHelper.testResponse.overrideExpectedTransactionData = true;

		mHelper.testResponse.VerifyTransactionResponse(mHelper.transactionData,
				transactionRowNum, mHelper.paymentTypeData, paymentTypeRowNum);

	}

	@Test(description = "Verify offer for a seamless credit card transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferSeamlessCCTransaction(Config testConfig) {
		int merchantLoginRow = 1;
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		OffersHelper oHelper = new OffersHelper(testConfig);
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);
		// navigate to offer list page
		new OfferListPage(testConfig);

		// set offer to use for transactions
		oHelper.setOfferInfo("ValidOffer");

		// login to admin
		helper.DoLogin();
		// make transaction with created offer
		helper.GetTestTransactionPage();

		// do a seamless credit card transaction for 10 Rs
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "5.00");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount, offer key for 10 rupees seamless transaction
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		helper.GetTestTransactionPage();

		// do a seamless credit card transaction for 5 Rs
		testConfig.putRunTimeProperty("amount", "5.00");
		testConfig.putRunTimeProperty("discount", "4.00");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;

		// verify amount,discount, offer key for 5 rupees seamless transaction
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		helper.testResponse.overrideExpectedTransactionData = true;
		Assert.assertTrue(testConfig.getTestResult());

	}


	/*
	 * Verify offer count upper limit when crossed, fails offer key discount
	 * 
	 * @param testConfig

	 */
	@Test(description = "Verify message in transaction response for expired offer", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyInvalidOffer(Config testConfig)
	{
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.00");

		// login to admin
		helper.DoLogin();
		// make transaction with created offer
		helper.GetTestTransactionPage();	
		// make transaction with created expired offer
		testConfig.putRunTimeProperty("offerKey", "invalidoffer@689");
		testConfig.putRunTimeProperty("offer_type", "instant");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer for merchant.");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Create an offer for CC payment type and verify transaction success for DC mode", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyValidOfferPaymentTypeCCAndDC(Config testConfig)
	{
		int merchantLoginRow = 1;
		int transactionRowNum = 1;
		int paymentTypeRowNum = 17;
		int cardDetailsRowNum = 1;
		OffersHelper oHelper = new OffersHelper(testConfig);
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);
		// navigate to offer list page
		new OfferListPage(testConfig);
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "5.00");

		oHelper.setOfferInfo("ValidOffer");
		// login to admin
		helper.DoLogin();
		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a CC transaction through offer created on AutomationMerchant1
		transactionRowNum = 3;
		paymentTypeRowNum = 5;
		cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("discount", "0.00");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer for merchant.");
		testConfig.putRunTimeProperty("offer_type", "instant");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Do transactions on AutomationMerchant1, AutomationMerchant3 and AutomationMerchant2, through offer shared between AutomationMerchant1 and AutomationMerchant3 without specifiying bin in CC",
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyValidSharedOfferOnAM1WoBin(Config testConfig)
	{
		//Do a CC transaction through offer created on AutomationMerchant1
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String offerKey = "TestSharedOfferWoBin@1214";

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "5.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// login to admin
		helper.DoLogin();
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a DC transaction through offer created on AutomationMerchant1, without specified BIN
		transactionRowNum = 1;
		paymentTypeRowNum = 17;
		cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("discount", "0.00");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Discount not available on payment mode chosen by user.");

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a CC transaction through offer created on AutomationMerchant1, shared with AutomationMerchant3
		transactionRowNum = 3;
		paymentTypeRowNum = 5;
		cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("discount", "5.00");
		testConfig.removeRunTimeProperty("Offer_Failure_Reason");

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a DC transaction through offer created on AutomationMerchant1, shared with AutomationMerchant3. without specified BIN
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("discount", "0.00");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Discount not available on payment mode chosen by user.");

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a CC transaction through offer created on AutomationMerchant1, not shared with AutomationMerchant2
		transactionRowNum = 2;
		paymentTypeRowNum = 18;
		cardDetailsRowNum = 1;

		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer for merchant.");
		testConfig.putRunTimeProperty("offer_type", "instant");
		helper.GetTestTransactionPage();
		
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Do transactions on AutomationMerchant3, AutomationMerchant1 and AutomationMerchant2, through offer shared between AutomationMerchant3 and AutomationMerchant1 without specifiying bin in CC",
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyValidSharedOfferOnAM3WoBin(Config testConfig)
	{
		//Do a CC transaction through offer created on AutomationMerchant3
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		String offerKey = "TestSharedOfferWoBin@1212";

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "5.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// login to admin
		helper.DoLogin(); 
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a DC transaction through offer created on AutomationMerchant3 without specified BIN
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("discount", "0.00");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Discount not available on payment mode chosen by user.");

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a CC transaction through offer created on AutomationMerchant3, shared with AutomationMerchant1
		transactionRowNum = 1;
		paymentTypeRowNum = 3;
		cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("discount", "5.00");
		testConfig.removeRunTimeProperty("Offer_Failure_Reason");

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a DC transaction through offer created on AutomationMerchant3, shared with AutomationMerchant1, without specified BIN
		transactionRowNum = 1;
		paymentTypeRowNum = 17;
		cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("discount", "0.00");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Discount not available on payment mode chosen by user.");

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do transaction through offer created on AutomationMerchant3, not shared with AutomationMerchant2
		transactionRowNum = 2;
		paymentTypeRowNum = 18;
		cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer for merchant.");
		testConfig.putRunTimeProperty("offer_type", "instant");
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Do transactions on AutomationMerchant1, AutomationMerchant3 and AutomationMerchant2, through offer shared between AutomationMerchant1 and AutomationMerchant3",
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyValidSharedOfferOnAM1(Config testConfig)
	{
		//Do a CC transaction through offer created on AutomationMerchant1
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String offerKey = "TestSharedOffer@1208";

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "5.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// login to admin
		helper.DoLogin();
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a DC transaction through offer created on AutomationMerchant1, with specified BIN
		transactionRowNum = 1;
		paymentTypeRowNum = 17;
		cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a CC transaction through offer created on AutomationMerchant1, shared with AutomationMerchant3
		transactionRowNum = 3;
		paymentTypeRowNum = 5;
		cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a DC transaction through offer created on AutomationMerchant1, shared with AutomationMerchant3, with specified BIN
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do transaction through offer created on AutomationMerchant1, not shared with AutomationMerchant2
		transactionRowNum = 2;
		paymentTypeRowNum = 18;
		cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("discount", "0.00");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer for merchant.");
		testConfig.putRunTimeProperty("offer_type", "instant");

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Do transactions on AutomationMerchant3, AutomationMerchant1 and AutomationMerchant2, through offer shared between AutomationMerchant3 and AutomationMerchant1",
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyValidSharedOfferOnAM3(Config testConfig)
	{
		//Do a CC transaction through offer created on AutomationMerchant3
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		String offerKey = "TestSharedOffer@1210";

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "5.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// login to admin
		helper.DoLogin();
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do transaction through offer created on AutomationMerchant3, with specified BIN
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a CC transaction through offer created on AutomationMerchant3, shared with AutomationMerchant1
		transactionRowNum = 1;
		paymentTypeRowNum = 3;
		cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a DC transaction through offer created on AutomationMerchant3, shared with AutomationMerchant1, with specified BIN
		transactionRowNum = 1;
		paymentTypeRowNum = 17;
		cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do transaction through offer created on AutomationMerchant3, not shared with AutomationMerchant2
		transactionRowNum = 2;
		paymentTypeRowNum = 18;
		cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("discount", "0.00");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer for merchant.");
		testConfig.putRunTimeProperty("offer_type", "instant");
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Do transactions on AutomationMerchant3, AutomationMerchant1 and AutomationMerchant2, through offer shared between AutomationMerchant3 and AutomationMerchant1",
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyExpiredSharedOfferOnAM3(Config testConfig)
	{
		//Do a CC transaction through offer created on AutomationMerchant3
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		String offerKey = "TestExpiredSharedOffer@1216";

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Offer expired.");

		// login to admin
		helper.DoLogin();
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do transaction through offer created on AutomationMerchant3, with specified BIN
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a CC transaction through offer created on AutomationMerchant3, shared with AutomationMerchant1
		transactionRowNum = 1;
		paymentTypeRowNum = 3;
		cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do a DC transaction through offer created on AutomationMerchant3, shared with AutomationMerchant1, with specified BIN
		transactionRowNum = 1;
		paymentTypeRowNum = 17;
		cardDetailsRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Do transaction through offer created on AutomationMerchant3, not shared with AutomationMerchant2
		transactionRowNum = 2;
		paymentTypeRowNum = 18;
		cardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer for merchant.");
		testConfig.putRunTimeProperty("offer_type", "instant");
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Verify Round off functionality", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyRoundOff(Config testConfig)
	{
		int transactionRowNum = 1;
		int paymentTypeRowNum = 17;
		int cardDetailsRowNum = 1;

		String offerKey = "RoundOffZeroMin20@1562";
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "20.00");
		testConfig.putRunTimeProperty("discount", "2.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// login to admin
		helper.DoLogin();
		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Verify round off value upto two digits
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;

		offerKey = "RoundOffTwoMin10@1564";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.17");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Verify round off value upto two digits for single digit discount
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;

		offerKey = "OfferNegative@1580";
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "1.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify Round off functionality", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyMinimumAmount(Config testConfig)
	{
		int transactionRowNum = 1;
		int paymentTypeRowNum = 17;
		int cardDetailsRowNum = 1;

		String offerKey = "RoundOffZeroMin20@1562";
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "20.00");
		testConfig.putRunTimeProperty("discount", "2.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// login to admin
		helper.DoLogin();
		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//**********Verify round off value upto two digits*********
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;

		offerKey = "RoundOffTwoMin10@1564";
		testConfig.putRunTimeProperty("amount", "9.00");
		testConfig.putRunTimeProperty("discount", "0.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		helper.GetTestTransactionPage();
		//set Retry Offer to 1 to verify this page
		testConfig.putRunTimeProperty("RetryOffer","7");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Offer is valid on amount greater than 10.00");

		// make transaction with valid DC     

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//*********** Offer with zero minimum amount***************
		offerKey = "minimumAmount@1626";
		//helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "20.00");
		testConfig.putRunTimeProperty("discount", "11.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.removeRunTimeProperty("RetryOffer");
		testConfig.removeRunTimeProperty("Offer_Failure_Reason");

		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify Round off functionality", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyMinimumAmountWithAdditionalCharges(Config testConfig)
	{
		//*********** Offer with zero minimum amount and additional charges***************
		int transactionRowNum = 70;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;

		String offerKey = "RoundOffTwoMin10@1564";
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.17");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// login to admin
		helper.DoLogin();
		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//*********** Offer with zero minimum amount***************
		offerKey = "minimumAmount@1626";
		//helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "9.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Verify maximum discount functionality", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyMinimumAmountDecimalValues(Config testConfig)
	{
		//*********** Minimum Amount with decimal value, Transaction with exact amount ***************
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;

		String offerKey = "MinimumAmountDecimal@1656";
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "9.99");
		testConfig.putRunTimeProperty("discount", "2.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// login to admin
		helper.DoLogin();
		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//*********** Minimum Amount with decimal value, Transaction with less amount ***************

		testConfig.putRunTimeProperty("amount", "9.98");
		testConfig.putRunTimeProperty("discount", "0.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		//set Retry Offer to 1 to verify this page
		testConfig.putRunTimeProperty("RetryOffer","8");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Offer is valid on amount greater than 9.99");


		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify maximum discount functionality", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyMaxDiscount(Config testConfig)
	{
		//*********** Discount is more than transaction amount ***************
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;

		String offerKey = "MaxDiscountAdditionCharges@1654";
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "9.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// login to admin
		helper.DoLogin();
		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//*********** Discount is more than transaction amount + Additional Discount ***************

		transactionRowNum = 70;
		offerKey = "MaxDiscountAdditionCharges@1654";
		//helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "9.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);

		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Verify maximum discount functionality when offer amount is greater than max discount", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyMaxoffer(Config testConfig)
	{
		//*********** Offer key is edited to define max Discount for a transaction ***************
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;

		String offerKey = "MaxDiscountAdditionCharges@1654@5";
		String offerAvailed="MaxDiscountAdditionCharges@1654";
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "5.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerAvailed);

		// login to admin
		helper.DoLogin();
		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify maximum discount functionality when offer amount is less than max discount ", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyMaxofferamount(Config testConfig)
	{
		//*********** Offer key is edited to define max Discount for a transaction ***************
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;

		String offerKey = "MaxDiscountAdditionCharges@1654@40";
		String offerAvailed="MaxDiscountAdditionCharges@1654";
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		testConfig.putRunTimeProperty("amount", "50.00");
		testConfig.putRunTimeProperty("discount", "15.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerAvailed);

		// login to admin
		helper.DoLogin();
		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify max offer for DC with same card bin, Round off/min amount with max offer functionality", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyMaxOfferMinAmount(Config testConfig)
	{
		int transactionRowNum = 1;
		int paymentTypeRowNum = 17;
		int cardDetailsRowNum = 1;

		String offerKey = "RoundOffZeroMin20@1562@2";
		String offerAvailed = "RoundOffZeroMin20@1562";
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "20.00");
		testConfig.putRunTimeProperty("discount", "2.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offerAvailed", offerAvailed);

		helper.DoLogin();
		
		// make transaction with DC with same card bin    
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key 
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//**********Verify offer failure due to min amount check*********
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;

		offerKey = "RoundOffTwoMin10@1564@20";
		testConfig.putRunTimeProperty("amount", "9.00");
		testConfig.putRunTimeProperty("discount", "0.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		helper.GetTestTransactionPage();
		//set Retry Offer to 1 to verify this page
		testConfig.putRunTimeProperty("RetryOffer","7");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Offer is valid on amount greater than 10.00");

		// make transaction with valid DC     

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//*********** Offer with zero minimum amount***************
		offerKey = "minimumAmount@1626@20";
		offerAvailed="minimumAmount@1626";
		//helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "20.00");
		testConfig.putRunTimeProperty("discount", "11.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offerAvailed", offerAvailed);
		testConfig.removeRunTimeProperty("RetryOffer");
		testConfig.removeRunTimeProperty("Offer_Failure_Reason");

		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	//FIXME BUG ID :38316
	@Test(description = "Verify offer functionality when Maxoffer amount is ZERO/negative ", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyZeroMaxofferamount(Config testConfig)
	{
		//*********** Offer key is edited to define max Discount as Zero for a transaction ***************
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;

		String offerKey = "MaxDiscountAdditionCharges@1654@0";
		String offerAvailed="MaxDiscountAdditionCharges@1654";
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		testConfig.putRunTimeProperty("amount", "50.00");
		testConfig.putRunTimeProperty("discount", "15.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerAvailed);

		// login to admin
		helper.DoLogin();
		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		//*********** Offer key is edited to define max Discount as negative for a transaction ***************
		
		offerKey = "MaxDiscountAdditionCharges@1654@-10";
		offerAvailed="MaxDiscountAdditionCharges@1654";
		helper = new TransactionHelper(testConfig, false);
		
		testConfig.putRunTimeProperty("amount", "50.00");
		testConfig.putRunTimeProperty("discount", "15.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", offerAvailed);

		// login to admin
		helper.DoLogin();
		// make transaction with valid DC     
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		// verify amount,discount and offer key
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	

}