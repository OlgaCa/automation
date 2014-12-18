
package Test.MerchantPanel.Offers;


import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class MultipleOffers extends TestBase{

	
	
	@Test(description = "Verifying combination of 1 valid  and other invalid offer key that doesnot exist in DB", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyValidAndInvalidOfferKeyComboWithNoDBEntry(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 117;
		int paymentTypeRowNum = 354;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "appliedoffer@2802,Offervalid@279087";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "appliedoffer@2802");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
	
		//make transaction with valid DC (VISA)
		
		paymentTypeRowNum= 355;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	
	

	@Test(description = "Verifying that system should Avail offer with maximum discount passed as parameters with offer keys by the user", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyMaximumDiscountAvailed(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 118;
		int paymentTypeRowNum = 356;
		int cardDetailsRowNum = 1;
		 
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "testmaxdiscount@2822@3,testmaxdiscount3@2832@2";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "testmaxdiscount@2822");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"maxOfferAvailed");
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
	
		// verify discount, status, offer type and offer key from database
			testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		
		
		//make transaction with valid DC (VISA)
		paymentTypeRowNum= 357;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"maxOfferAvailed");
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
			
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	
	

	
	@Test(description = "Verifying Offer failure when offer keys entered in 'offer key' field are without comma separations", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferFailureForOfferKeysWithoutComma(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 119;
		int paymentTypeRowNum = 358;
		int cardDetailsRowNum = 1;
		 
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "Offermin@2788Offervalid@2790";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer Key.");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("offer_type", "");
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		testResponseForCC.overrideExpectedTransactionData = true;
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
			testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
				
		
		Assert.assertTrue(testConfig.getTestResult());
		
		//make transaction with valid DC (VISA)
		paymentTypeRowNum= 359;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		testResponseForDC.overrideExpectedTransactionData = true;
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	
	
	
	@Test(description = "Verifying Offer failure in case, used offerkeys which doesnot eixist", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferFailureForNonExistantKeys(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 156;
		int paymentTypeRowNum = 364;
		int cardDetailsRowNum = 1;
		 
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "nonexist@1234,nonexist2@1258";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer.");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		helper.verifyRetryPageElements();
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		testResponseForCC.overrideExpectedTransactionData=true;
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
				
		
		Assert.assertTrue(testConfig.getTestResult());
		
		//make transaction with valid DC (VISA)
		paymentTypeRowNum= 365;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		testResponseForDC.overrideExpectedTransactionData = true;
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
				testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
				
		Assert.assertTrue(testConfig.getTestResult());
	}


	
	@Test(description = "Verify Availing Valid offer out of 1 valid offer and other nonexistant", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferAvailedInCaseOfOneValidAndOneNonExistantOfferKey(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 120;
		int paymentTypeRowNum = 360;
		int cardDetailsRowNum = 1;
		 
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "nonexist@1234,Offervalid@2790";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "offervalid@2790");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		 
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneNonExistant");
				
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		testResponseForCC.overrideExpectedTransactionData=true;
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
				
		
		Assert.assertTrue(testConfig.getTestResult());
		
		//make transaction with valid DC (VISA)
		paymentTypeRowNum= 361;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneNonExistant");
		
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		testResponseForDC.overrideExpectedTransactionData = true;
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	
	@Test(description = "Verifying  offer where net payable amount after discount can not be less than 1", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferWhenNetPayableAmountCannotBeLessThanOne(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 121;
		int paymentTypeRowNum = 362;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "maxdis1@2818,maxdis2@2820";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "maxdis2@2820");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("maxDiscount", "yes");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"offerThatReducesAmountLessThanOne");
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
	
		//make transaction with valid DC (VISA)
		
		paymentTypeRowNum= 363;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
	
		 helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"offerThatReducesAmountLessThanOne");
				
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	
	
	@Test(description = "Verifying that system should pick offer set with maximum discount out of the three offers entered in the 'offer key' field by the user.", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyMaximumDiscountInCaseOfThreeOffers(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 118;
		int paymentTypeRowNum = 356;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "MaxiDis1@3020,MaxiDis2@3022,MaxiDis3@3024";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "MaxiDis3@3024");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("maxDiscount", "yes");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"maxOfferAvailed");
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
	
		//make transaction with valid DC (VISA)
		
		paymentTypeRowNum= 357;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
	
		 helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"maxOfferAvailed");
				
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	
	@Test(description = "Verifying Retry button navigates to payment option page", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyRetryButton(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 122;
		int paymentTypeRowNum = 364;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "offerfail1@3044,offerfail2@3046";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offer_failure_reason", "Invalid Offer.");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		testConfig.putRunTimeProperty("hitRetryButton", "yes");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyRetryPageElements();
		
		Helper.compareTrue(testConfig, "Pay Now button on payment screen" , payment.verifyPayNowButton());
		
		// verification for DC scenario
		
		paymentTypeRowNum = 365;
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
	
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer failure message on processing page
		helper.verifyRetryPageElements();
		
		Helper.compareTrue(testConfig, "Pay Now button on payment screen" , payment.verifyPayNowButton());
		// verify user redirects to payment page
		
		Assert.assertTrue(testConfig.getTestResult());		
	}
	
	
	
	@Test(description = "Verifying on clicking Continue button available on Retry page, system navigates to Test response page", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyContinueButtonOnRetryPage(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 156;
		int paymentTypeRowNum = 364;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "retrytest1@2810,retrytest2@2812";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offer_failure_reason", "Invalid Offer.");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		testConfig.putRunTimeProperty("clickContinue", "yes");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyRetryPageElements();
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
	
		//make transaction with valid DC (VISA)
		
		paymentTypeRowNum= 365;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
	
		helper.verifyRetryPageElements();
				
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());

	}
		
	
	
	
	@Test(description = "Verifying offer failure for a case where offers are valid for a specific merchant's account and are used in transaction for different mechant account", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferFailureForSpecificMerchantAccount(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 156;
		int paymentTypeRowNum = 364;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "someothermerchant1@2784,someothermerchant2@2786";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offer_failure_reason", "Invalid Offer.");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyRetryPageElements();
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
	
		//make transaction with valid DC (VISA)
		
		paymentTypeRowNum= 365;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
	
		helper.verifyRetryPageElements();
				
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	
	@Test(description = "Availing valid offer out of 1 Expired and other still valid by date", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyValidOfferWithOneExpiredOffer(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 117;
		int paymentTypeRowNum = 354;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "expiryoffer@2800,appliedoffer@2802";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "appliedoffer@2802");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
	
		//make transaction with valid DC (VISA)
		
		paymentTypeRowNum= 355;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	
	
	@Test(description = "Verify multiple offers set with different payment types", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyMultipleOfferSetWithDifferentPaymentTypes(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 120;
		int paymentTypeRowNum = 360;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "testdebitcardset@2796,testvalid@2798";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "testvalid@2798");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
	
		//make transaction with valid DC (VISA)
		
		paymentTypeRowNum= 361;
		
		helper.GetTestTransactionPage();
		offerKey ="testcreditcardset@2794,testvalid@2798";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "testvalid@2798");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
		
		
		/////////////// Other scenario ////////////////////////
		
		// make transaction with valid CC
				transactionRowNum = 117;
				paymentTypeRowNum = 354;
							
				
				helper.GetTestTransactionPage();
				
				offerKey = "testdebitcardset@2796,testcreditcardset@2794";  
				
				testConfig.putRunTimeProperty("offerKey", offerKey);
				testConfig.putRunTimeProperty("offeravailed", "testcreditcardset@2794");
				
				
				
				helper.trans.FillTransactionDetails(transactionRowNum);
				helper.trans.clickSubmitButton();
				
				PaymentOptionsPage payment2=new PaymentOptionsPage(testConfig);
				
				helper.ccTab= payment2.clickCCTab();
				helper.ccTab.FillCardDetails(cardDetailsRowNum);
				payment2.clickPayNowButton();
				
				// verify Offer success message on processing page
				helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
				
				TestResponsePage testResponseForCC2=new TestResponsePage(testConfig);
				
				// verify discount and offer key
				testResponseForCC2.VerifyTransactionResponse(helper.transactionData,
						transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
				
				// verify discount, status, offer type and offer key from database
				testResponseForCC2.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
			
				//make transaction with valid DC (VISA)
				
				paymentTypeRowNum= 355;
				
				helper.GetTestTransactionPage();
				
				
				testConfig.putRunTimeProperty("offeravailed", "testdebitcardset@2796");
				
				helper.trans.FillTransactionDetails(transactionRowNum);
				helper.trans.clickSubmitButton();
				
				helper.dcTab= payment.clickDCTab();
				
				TestDataReader paymentTypeData2=new TestDataReader(testConfig, "PaymentType");
				
				debitCardName = paymentTypeData2.GetData(paymentTypeRowNum, "Bank Name");
				helper.dcTab.SelectDebitCard(debitCardName);
				
				helper.dcTab.FillCardDetails(cardDetailsRowNum);
				payment.clickPayNowButton();
				
				// verify Offer success message on processing page
				helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
				
				TestResponsePage testResponseForDC2=new TestResponsePage(testConfig);
				
				// verify discount and offer key
				testResponseForDC2.VerifyTransactionResponse(helper.transactionData,
						transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
				
				// verify discount, status, offer type and offer key from database
				testResponseForDC2.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
				
				Assert.assertTrue(testConfig.getTestResult());
	}


	
	@Test(description = "Verify offer meeting  minimum amount condition should be availed", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferForMinimumAmount(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 120;
		int paymentTypeRowNum = 360;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "offermin@2788,offervalid@2790";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "offervalid@2790");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
	
		//make transaction with valid DC (VISA)
		
		paymentTypeRowNum= 361;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	
	@Test(description = "Verifying valid offer should get availed when user uses one offer created for that specific merchant's account and uses second offer which is created specifically for some other merchant's account", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyValidOfferGetsAvailed(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 117;
		int paymentTypeRowNum = 354;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "Validforthismerchant@2804,qqq@2010";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "Validforthismerchant@2804");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
	
		//make transaction with valid DC (VISA)
		
		paymentTypeRowNum= 355;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"oneValidOneInvalid");
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	
	
	
	
	@Test(description = "Verify offer failure due to  transaction amount in use is less than the 'Minimum amount' condition set for offers", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferFailureWhenAmountLessThanMinimum(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 156;
		int paymentTypeRowNum = 364;
		int cardDetailsRowNum = 1;
		 
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "Automationmerchant3@2762,Automationmerchant3a@2764";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer.");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		
		//testConfig.putRunTimeProperty("noRetryButton", "yes");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		helper.verifyRetryPageElements();
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		testResponseForCC.overrideExpectedTransactionData=true;
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
				
		
		Assert.assertTrue(testConfig.getTestResult());
		
		//make transaction with valid DC (VISA)
		paymentTypeRowNum= 365;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		testResponseForDC.overrideExpectedTransactionData = true;
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
				testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
				
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	
	
	
	
	@Test(description = "Verify offer failure due to Date Expiration", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferFailureDueTODateExpiration(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 156;
		int paymentTypeRowNum = 364;
		int cardDetailsRowNum = 1;
		 
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "ExpiryOfffer1@2780,ExpiryOfffer2@2782";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer.");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		
		
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		helper.verifyRetryPageElements();
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		testResponseForCC.overrideExpectedTransactionData=true;
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
				
		
		Assert.assertTrue(testConfig.getTestResult());
		
		//make transaction with valid DC (VISA)
		paymentTypeRowNum= 365;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		testResponseForDC.overrideExpectedTransactionData = true;
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
				testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
				
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	
	
	
	@Test(description = "Verifying offer failure in transaction due to reason that offers are valid for specific 'Payment Type' only and user used these offers with different payment type", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferFailureForUsingDifferentPaymentTypeThanSpecifiedForOffer(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 156;
		int paymentTypeRowNum = 364;
		int cardDetailsRowNum = 1;
		 
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "OfferDebitCard@2772,OfferDebitCard2@2776";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer.");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		helper.verifyRetryPageElements();
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		testResponseForCC.overrideExpectedTransactionData=true;
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
				
		
		Assert.assertTrue(testConfig.getTestResult());
		
		//make transaction with valid DC (VISA)
		paymentTypeRowNum= 365;
		
		offerKey = "OfferCreditCard@2770,OfferCreditCard2@2774";
		testConfig.putRunTimeProperty("offerKey", offerKey);
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		testResponseForDC.overrideExpectedTransactionData = true;
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
				testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
				
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	
	@Test(description = "Verifying Retry Button not to be displayed if 'Retry allowed per Transaction' count is set as zero for the merchant", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyRetryButtonNotDisplayed(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 119;
		int paymentTypeRowNum = 358;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "retrytest1@2810,retrytest2@2812";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offer_failure_reason", "Invalid Offer.");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("nonExistantOfferKey", "yes");
		testConfig.putRunTimeProperty("noRetryButton", "yes");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyRetryPageElements();
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
	
		//make transaction with valid DC (VISA)
		
		paymentTypeRowNum= 359;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
	
		helper.verifyRetryPageElements();
				
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	
	
	@Test(description = "Verifying that system should pick offer set with maximum discount out of the multiple offers entered in the 'offer key' field by the user", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyOfferSetWithMaximumDiscountGetsAvailed(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		// make transaction with valid CC
		int transactionRowNum = 117;
		int paymentTypeRowNum = 354;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		
		String offerKey = "Maxdiscount1@2766,Maxdiscount2@2768";  
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offeravailed", "Maxdiscount2@2768");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		testConfig.putRunTimeProperty("maxDiscount", "yes");
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		PaymentOptionsPage payment=new PaymentOptionsPage(testConfig);
		
		helper.ccTab= payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
		helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"maxOfferAvailed");
		
		TestResponsePage testResponseForCC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForCC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForCC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
	
		//make transaction with valid DC (VISA)
		
		paymentTypeRowNum= 355;
		
		helper.GetTestTransactionPage();
		
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.trans.clickSubmitButton();
		
		helper.dcTab= payment.clickDCTab();
		
		TestDataReader paymentTypeData=new TestDataReader(testConfig, "PaymentType");
		
		String debitCardName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		helper.dcTab.SelectDebitCard(debitCardName);
		
		helper.dcTab.FillCardDetails(cardDetailsRowNum);
		payment.clickPayNowButton();
		
		// verify Offer success message on processing page
	
		 helper.verifyOfferRelatedStringsOnProcessingPage(testConfig.getRunTimeProperty("amount"),"maxOfferAvailed");
				
		
		TestResponsePage testResponseForDC=new TestResponsePage(testConfig);
		
		// verify discount and offer key
		testResponseForDC.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		// verify discount, status, offer type and offer key from database
		testResponseForDC.verifyDetailsFromDatabase_ForMultipleOffers(transactionRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());

	}
	
}
