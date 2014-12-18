package Test.MerchantPanel.Offers;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Offers.OfferListPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.TestBase;

public class TestOfferRetryPage extends TestBase{


	@Test(description = "Verify message in transaction response for invalid payment type", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyCancelOnOfferRetryforInvalidOfferPaymentCC(Config testConfig)
	{
		int merchantLoginRow = 1;
		int transactionRowNum = 1;
		int cardDetailsRowNum = 1;

		int paymentTypeRowNum = 325;

		OffersHelper oHelper = new OffersHelper(testConfig);
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);
		// navigate to offer list page
		new OfferListPage(testConfig);

		// set invalid ptype offer to use for transactions
		oHelper.setOfferInfo("InvalidOfferforCCDC");

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.00");

		// login to admin
		helper.DoLogin();
		// make transaction with created offer
		helper.GetTestTransactionPage();

		// make transaction with Offer for DC
		testConfig.putRunTimeProperty("RetryOffer","1");
		
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Discount not available on payment mode chosen by user.");
		testConfig.putRunTimeProperty("CancelRetryOffer","1");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);

		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,
				transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Verify message in transaction response for invalid payment type", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyRetryOnValidNBOfferPaymentTypeDC(Config testConfig)
	{
		//******************AXIS******************
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

		// set invalid ptype offer to use for transactions
		oHelper.setOfferInfo("InvalidOfferforCCDC");

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.00");

		// login to admin
		helper.DoLogin();
		// make transaction with created offer
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("RetryOffer","1");
		//testConfig.putRunTimeProperty("NewOfferRetryPage","1");
		
		// make transaction with offer missing CC card bin
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.PaymentOptionsPage);

		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Verify Retry Mesage for invalid offers", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyRetryMessageforExpiredoffers(Config testConfig)
	{	

		//******************AXIS******************
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
		//set invalid offer to use for transactions
		oHelper.setOfferInfo("ExpiredOffer");
		helper.DoLogin();
		// make transaction with created offer
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.00");
		//testConfig.putRunTimeProperty("NewOfferRetryPage","1");
		testConfig.putRunTimeProperty("RetryOffer","2");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Offer expired.");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//******************HDFC******************
		merchantLoginRow = 3;
		transactionRowNum = 3;
		paymentTypeRowNum = 5;
		cardDetailsRowNum = 1;
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);
		// navigate to offer list page
		new OfferListPage(testConfig);
		//set invalid offer to use for transactions
		oHelper.setOfferInfo("ExpiredOffer");
		// make transaction with created offer
		helper.GetTestTransactionPage();
		//set Retry Offer to 1 to verify this page
		testConfig.putRunTimeProperty("RetryOffer","12");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Offer expired.");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify Retry Mesage for invalid offers", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyRetryMessageforCountExceededoffers(Config testConfig)
	{
		//******************AXIS******************
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
		//set invalid offer to use for transactions
		oHelper.setOfferInfo("CountExceededOffer");
		// make transaction with created offer
		helper.DoLogin();
		helper.GetTestTransactionPage();
		//testConfig.putRunTimeProperty("NewOfferRetryPage","1");
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.00");
		//set Retry Offer to 1 to verify this page
		testConfig.putRunTimeProperty("RetryOffer","3");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Offer count exhausted.");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);


		//******************HDFC******************
		merchantLoginRow = 3;
		transactionRowNum = 3;
		paymentTypeRowNum = 5;
		cardDetailsRowNum = 1;
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);
		// navigate to offer list page
		new OfferListPage(testConfig);
		//set invalid offer to use for transactions
		oHelper.setOfferInfo("CountExceededOffer");
		// make transaction with created offer
		helper.GetTestTransactionPage();
		//set Retry Offer to 1 to verify this page
		testConfig.putRunTimeProperty("RetryOffer","11");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Offer count exhausted.");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify Retry Mesage for invalid offers", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyRetryMessageforCountperCardExceededOffers(Config testConfig)
	{
		//******************AXIS******************
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

		oHelper.setOfferInfo("CountPerCardExceeded");
		// make transaction with created offer
		helper.DoLogin();
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.00");
		//testConfig.putRunTimeProperty("NewOfferRetryPage","1");
		testConfig.putRunTimeProperty("RetryOffer","4");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Card limit reached. No of times discount availed = 1, maximum times discount per card = 1");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);


		//******************HDFC******************
		merchantLoginRow = 3;
		transactionRowNum = 3;
		paymentTypeRowNum = 5;
		cardDetailsRowNum = 1;
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);
		// navigate to offer list page
		new OfferListPage(testConfig);
		//set invalid offer to use for transactions
		oHelper.setOfferInfo("CountPerCardExceeded");
		// make transaction with created offer
		helper.GetTestTransactionPage();
		//set Retry Offer to 1 to verify this page
		testConfig.putRunTimeProperty("RetryOffer","5");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Card limit reached. No of times discount availed = 1, maximum times discount per card = 1");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify Retry Mesage for invalid offers", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyRetryMessageforAmountPerCardExceeded(Config testConfig)
	{		
		//******************AXIS******************
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

		//set invalid offer to use for transactions
		oHelper.setOfferInfo("AmountPerCardExceeded");
		// make transaction with created offer
		helper.DoLogin();
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.00");
		//testConfig.putRunTimeProperty("NewOfferRetryPage","1");
		//set Retry Offer to 1 to verify this page
		testConfig.putRunTimeProperty("RetryOffer","6");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Card limit on amount reached. Total discount given till date = 5, maximum discount per card = 5.00");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//******************HDFC******************
		merchantLoginRow = 3;
		transactionRowNum = 3;
		paymentTypeRowNum = 5;
		cardDetailsRowNum = 1;
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);
		// navigate to offer list page
		new OfferListPage(testConfig);
		//set invalid offer to use for transactions
		oHelper.setOfferInfo("AmountPerCardExceeded");
		// make transaction with created offer
		helper.GetTestTransactionPage();
		
		//set Retry Offer to 1 to verify this page
		testConfig.putRunTimeProperty("RetryOffer","10");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Card limit on amount reached. Total discount given till date = 5, maximum discount per card = 5.00");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify Retry Mesage for invalid offers", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_VerifyRetryforInvalidOfferforDCNB(Config testConfig)
	{
		
		//******************AXIS******************
		int merchantLoginRow = 1;
		int transactionRowNum = 1;
		int paymentTypeRowNum = 35;
		int cardDetailsRowNum = 1;
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		OffersHelper oHelper = new OffersHelper(testConfig);
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);
		// navigate to offer list page
		new OfferListPage(testConfig);

		//set invalid offer to use for transactions
		oHelper.setOfferInfo("OfferForCCwithoutBin");
		// make transaction with created offer
		helper.DoLogin();
		
		//DC Transaction
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.00");
		//set Retry Offer to 1 to verify this page
		testConfig.putRunTimeProperty("RetryOffer","1");
		//testConfig.putRunTimeProperty("NewOfferRetryPage","1");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Discount not available on payment mode chosen by user.");
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//NB Transaction
		paymentTypeRowNum = 61;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;
		testConfig.removeRunTimeProperty("key");

		
		//******************HDFC******************
		merchantLoginRow = 3;
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;
		oHelper = new OffersHelper(testConfig);
		dHelper = new DashboardHelper(testConfig);
		// merchant login
		dHelper.doMerchantLogin(merchantLoginRow);
		// navigate to offer list page
		new OfferListPage(testConfig);

		//set invalid offer to use for transactions
		oHelper.setOfferInfo("OfferForCCwithoutBin");
		// make transaction with created offer
		
		//DC Transaction
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.00");
		//set Retry Offer to 1 to verify this page
		testConfig.putRunTimeProperty("RetryOffer","9");
		//testConfig.putRunTimeProperty("NewOfferRetryPage","1");
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Discount not available on payment mode chosen by user.");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		//NB Transaction
		paymentTypeRowNum = 60;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());
	}
	
}
