package Test.MerchantPanel.Offers;

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
import Utils.TestDataReader;

public class TestOffersWithAdditionalCharges extends TestBase{
	
		/**
		 * Verifies offer transactions for an existing offer 
		 * 
		 * @param testConfig

		 */
		@Test(description = "Verify offer creation and apply additional charges", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void test_VerifyOfferTransactionAndOfferViewLink_CF(Config testConfig)
		{
			// int offerRow = 1;
			int merchantLoginRow = 68;
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

			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
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
			
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
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
			
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
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

		@Test(description = "Verify offer for merchant test transaction with additional charges", dataProvider = "GetTestConfig", timeOut=700000, groups = "1")
		public void test_VerifyOfferMerchantTransaction_CF(Config testConfig)  {
			int merchantLoginRow = 68;
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
			Assert.assertTrue(testConfig.getTestResult());
		}

		@Test(description = "Verify offer for a seamless credit card transaction with additional charges", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void test_VerifyOfferSeamlessCCTransaction_CF(Config testConfig) {
			int merchantLoginRow = 1;
			int transactionRowNum = 68;
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
			
			TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
			String amnt = transactionData.GetData(transactionRowNum, "amount");
			double transactionamount = Double.parseDouble(amnt);

			String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
			String [] keyValue = keyvalue.split(":");
			keyvalue = keyValue[1];
			keyValue = keyvalue.split(",");
			String addCharge = keyValue[0].trim();
			
			//verify amount on processing page
			double additionalCharges = Double.parseDouble(addCharge);
			transactionamount = transactionamount+additionalCharges+5;		
			String transactionAmount = String.valueOf(transactionamount)+"0";
			testConfig.putRunTimeProperty("totalAmount", transactionAmount);
			testConfig.putRunTimeProperty("verifyProcessingPage", "true");
		
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
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
		@Test(description = "Verify message in transaction response for expired offer with additional charges", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void test_VerifyInvalidOffer_CF(Config testConfig)
		{
			int transactionRowNum = 68;
			int paymentTypeRowNum = 3;
			int cardDetailsRowNum = 1;
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			testConfig.putRunTimeProperty("amount", "1.00");
			testConfig.putRunTimeProperty("discount", "0.00");

			// login to admin
			helper.DoLogin();
			// make transaction with created offer
			helper.GetTestTransactionPage();	
			// make transaction with created expired offer
			testConfig.putRunTimeProperty("offerKey", "invalidoffer@9");
			testConfig.putRunTimeProperty("offer_type", "instant");
			
			TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
			String amnt = transactionData.GetData(transactionRowNum, "amount");
			double transactionamount = Double.parseDouble(amnt);

			String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
			String [] keyValue = keyvalue.split(":");
			keyvalue = keyValue[1];
			keyValue = keyvalue.split(",");
			String addCharge = keyValue[0].trim();
			
			//verify amount on processing page
			double additionalCharges = Double.parseDouble(addCharge);
			transactionamount = transactionamount+additionalCharges;		
			String transactionAmount = String.valueOf(transactionamount)+"0";
			testConfig.putRunTimeProperty("totalAmount", transactionAmount);
			testConfig.putRunTimeProperty("verifyProcessingPage", "true");
		
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
					ExpectedResponsePage.TestResponsePage);
			helper.testResponse.overrideExpectedTransactionData = true;
			testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer for merchant.");
			helper.testResponse.VerifyTransactionResponse(helper.transactionData,
					transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

			Assert.assertTrue(testConfig.getTestResult());

		}
		@Test(description = "Create an offer for CC payment type and verify transaction success for DC mode with additional charges", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void test_VerifyValidOfferPaymentTypeCCAndDC_CF(Config testConfig)
		{
			int merchantLoginRow = 1;
			int transactionRowNum = 68;
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
			
			TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
			String amnt = transactionData.GetData(transactionRowNum, "amount");
			double transactionamount = Double.parseDouble(amnt);

			String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
			String [] keyValue = keyvalue.split(":");
			keyvalue = keyValue[1];
			keyValue = keyvalue.split(",");
			String addCharge = keyValue[0].trim();
			
			//verify amount on processing page
			double additionalCharges = Double.parseDouble(addCharge);
			transactionamount = transactionamount+additionalCharges+5;		
			String transactionAmount = String.valueOf(transactionamount)+"0";
			testConfig.putRunTimeProperty("totalAmount", transactionAmount);
			testConfig.putRunTimeProperty("verifyProcessingPage", "true");
		
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
					ExpectedResponsePage.TestResponsePage);

			helper.testResponse.overrideExpectedTransactionData = true;
			// verify amount,discount and offer key
			helper.testResponse.VerifyTransactionResponse(helper.transactionData,
					transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

			Assert.assertTrue(testConfig.getTestResult());
		}

			@Test(description = "Do transactions on AutomationMerchant1, AutomationMerchant3 and AutomationMerchant2, through offer shared between AutomationMerchant1 and AutomationMerchant3 without specifiying bin in CC with additional charges",
				dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void test_VerifyValidSharedOfferOnAM1WoBin_CF(Config testConfig)
		{
			//Do a CC transaction through offer created on AutomationMerchant1
			int transactionRowNum = 68;
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
			transactionRowNum = 68;
			paymentTypeRowNum = 17;
			cardDetailsRowNum = 1;
			testConfig.putRunTimeProperty("discount", "0.00");
			testConfig.putRunTimeProperty("Offer_Failure_Reason", "Discount not available on payment mode chosen by user.");

			helper.GetTestTransactionPage();
		
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
					ExpectedResponsePage.TestResponsePage);

			helper.testResponse.overrideExpectedTransactionData = true;			// verify amount,discount and offer key
			helper.testResponse.VerifyTransactionResponse(helper.transactionData,
					transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

			//Do a CC transaction through offer created on AutomationMerchant1, shared with AutomationMerchant3
			transactionRowNum = 70;
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
			transactionRowNum = 70;
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
			transactionRowNum = 69;
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

		@Test(description = "Do transactions on AutomationMerchant1, AutomationMerchant3 and AutomationMerchant2, through offer shared between AutomationMerchant1 and AutomationMerchant3 with additional charges",
				dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void test_VerifyValidSharedOfferOnAM1_CF(Config testConfig)
		{
			//Do a CC transaction through offer created on AutomationMerchant1
			int transactionRowNum = 68;
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
			transactionRowNum = 68;
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
			transactionRowNum = 70;
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
			transactionRowNum = 70;
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
			transactionRowNum = 69;
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

		@Test(description = "Do transactions on AutomationMerchant3, AutomationMerchant1 and AutomationMerchant2, through offer shared between AutomationMerchant3 and AutomationMerchant1 with additional charges",
				dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void test_VerifyValidSharedOfferOnAM3_CF(Config testConfig)
		{
			//Do a CC transaction through offer created on AutomationMerchant3
			int transactionRowNum = 70;
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
			transactionRowNum = 70;
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
			transactionRowNum = 68;
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
			transactionRowNum = 68;
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
			transactionRowNum = 69;
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

		@Test(description = "Do transactions on AutomationMerchant3, AutomationMerchant1 and AutomationMerchant2, through offer shared between AutomationMerchant3 and AutomationMerchant1 and apply additional charges",
				dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void test_VerifyExpiredSharedOfferOnAM3_CF(Config testConfig)
		{
			//Do a CC transaction through offer created on AutomationMerchant3
			int transactionRowNum = 70;
			int paymentTypeRowNum = 5;
			int cardDetailsRowNum = 1;
			String offerKey = "TestExpiredSharedOffer@1216";

			TransactionHelper helper = new TransactionHelper(testConfig, false);
			testConfig.putRunTimeProperty("amount", "10.00");
			testConfig.putRunTimeProperty("offerKey", offerKey);
			testConfig.putRunTimeProperty("Offer_Failure_Reason", "Offer expired.");
			testConfig.putRunTimeProperty("offer_type", "instant");
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
			transactionRowNum = 70;
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
			transactionRowNum = 68;
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
			transactionRowNum = 68;
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
			transactionRowNum = 69;
			paymentTypeRowNum = 18;
			cardDetailsRowNum = 1;
			testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer for merchant.");
			

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

