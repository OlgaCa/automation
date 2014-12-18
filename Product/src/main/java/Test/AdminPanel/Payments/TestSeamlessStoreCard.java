package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;

public class TestSeamlessStoreCard  extends TestBase{
	
	@Test(description = "Save a new Store Card", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void Save_new_store_CCcard(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		// CC-DINR
		int transactionRowNum = 2;
		int paymentTypeRowNum = 235;
		int cardDetailsRowNum=18;
		int storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum,paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
		testConfig.removeRunTimeProperty("userString");
		Assert.assertTrue(testConfig.getTestResult());
	}
	

	@Test(description = "Save a new Store Card", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void Save_new_store_DCcard(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
	
		//DC-MAES
		int transactionRowNum = 1;
		int paymentTypeRowNum =319;
		int cardDetailsRowNum=1;
		int storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum,paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
		Assert.assertTrue(testConfig.getTestResult());
	}

	
	@Test(description = "Save Multiple Cards", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void Multiple_CCCardsOfSameType(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC DINR Card 1
		int transactionRowNum = 2;
		int cardDetailsRowNum=18;
		int paymentTypeRowNum = 235;
		int storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
		
	
		//CC DINR Card 2
		transactionRowNum = 2;
		paymentTypeRowNum = 236;
		cardDetailsRowNum=19;
		storeCardDetailsRowNum = 2;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
	
		Assert.assertTrue(testConfig.getTestResult());	
	}

	
	@Test(description = "Save Multiple Cards", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void Multiple_DCCardsOfSameType(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		
		//DC Card 1 - MAST
		int transactionRowNum = 3;
		int cardDetailsRowNum=1;
		int paymentTypeRowNum = 19;
		int storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
		
		//DC Card 1 - MAES
		transactionRowNum = 2;
		cardDetailsRowNum=20;
		paymentTypeRowNum = 330;
		storeCardDetailsRowNum = 5;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	
	
	@Test(description = "Use Saved Card", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void SavedSeamlessCCTransactionthroughNonseamless(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC - DINR
		int transactionRowNum = 2;
		int paymentTypeRowNum = 235;
		int cardDetailsRowNum=18;
		int storeCardDetailsRowNum = 1;
		boolean verifythroughSeamless=false;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, verifythroughSeamless,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		//CC - VISA/Master
		transactionRowNum = 2;
		paymentTypeRowNum = 235;
		cardDetailsRowNum=18;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum,verifythroughSeamless, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		//CC - AMEX
		transactionRowNum = 2;
		paymentTypeRowNum = 326;
		cardDetailsRowNum=55;
		storeCardDetailsRowNum = 4; 
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		helper.bankRedirectPage = (BankRedirectPage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,storeCardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.testResponse = (TestResponsePage)helper.bankRedirectPage.SubmitButtonForSuccessAMEX();
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.bankRedirectPage = (BankRedirectPage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum,verifythroughSeamless, ExpectedResponsePage.BankRedirectPage);
		helper.testResponse = (TestResponsePage)helper.bankRedirectPage.SubmitButtonForSuccessAMEX();
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
		}
	
	

	@Test(description = "Use Saved Card", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void SavedSeamlessDCTransactionthroughNonseamless(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
	
		//DC - Visa
		int transactionRowNum = 5;
		int paymentTypeRowNum = 267;
		int cardDetailsRowNum=1;
		int storeCardDetailsRowNum = 1;
		boolean verifythroughSeamless=false;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, verifythroughSeamless,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		
		//DC Card 1 - MAST
		transactionRowNum = 3;
		cardDetailsRowNum=1;
		paymentTypeRowNum = 19;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, verifythroughSeamless,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		
		//DC Card 1 - MAES
		transactionRowNum = 2;
		cardDetailsRowNum=20;
		paymentTypeRowNum = 330;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum,verifythroughSeamless, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);		
		Assert.assertTrue(testConfig.getTestResult());
}
	
	
	@Test(description = "Use Saved Card", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void SavedSeamlessCardTransactionthroughSeamless(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC - DINR
		int transactionRowNum = 2;
		int paymentTypeRowNum = 235;
		int cardDetailsRowNum=18;
		int storeCardDetailsRowNum = 1;
		boolean verifythroughSeamless=true;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, verifythroughSeamless,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		//CC - VISA/Master
		transactionRowNum = 2;
		paymentTypeRowNum = 235;
		cardDetailsRowNum=18;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum,verifythroughSeamless, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		
		//CC - AMEX
		transactionRowNum = 2;
		paymentTypeRowNum = 326;
		cardDetailsRowNum=55;
		storeCardDetailsRowNum = 4; 
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		helper.bankRedirectPage = (BankRedirectPage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,storeCardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.testResponse = (TestResponsePage)helper.bankRedirectPage.SubmitButtonForSuccessAMEX();
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.bankRedirectPage = (BankRedirectPage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum,verifythroughSeamless, ExpectedResponsePage.BankRedirectPage);
		helper.testResponse = (TestResponsePage)helper.bankRedirectPage.SubmitButtonForSuccessAMEX();
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		//DC - Visa
		transactionRowNum = 5;
		paymentTypeRowNum = 267;
		cardDetailsRowNum=1;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, verifythroughSeamless,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		

		//DC Card 1 - MAST
		transactionRowNum = 3;
		cardDetailsRowNum=1;
		paymentTypeRowNum = 19;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, verifythroughSeamless,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		
		//DC Card 1 - MAES
		transactionRowNum = 2;
		cardDetailsRowNum=20;
		paymentTypeRowNum = 330;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum,verifythroughSeamless, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
		}
	
	
	//testConfig.getRunTimeProperty("seamlessSavedCard")!=null
	
/*
	@Test(description = "Do transaction without Saving Card", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void test4_transaction_without_saving_card(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// CC
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum=1;
		int storeCardDetailsRowNum = -1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum,paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		//DC
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum=1;
		storeCardDetailsRowNum = -1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum,paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
		
	}
	
		@Test(description = "Store this card option is not displayed if user credentials field blank", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
		public void test5_transaction_without_userCredential(Config testConfig) throws InterruptedException {
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			helper.DoLogin();

			// CC
			int transactionRowNum = 3;
			int paymentTypeRowNum = 5;
			int storeCardDetailsRowNum = -1;
			helper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, false);
			
			//DC
			transactionRowNum = 3;
			paymentTypeRowNum = 19;
			storeCardDetailsRowNum = -1;
			helper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, false);
			Assert.assertTrue(testConfig.getTestResult());		
	}

*/
	
	@Test(description = "Use Saved Card", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void SavedDebitCardTransaction(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
	
		//DC - Visa
		int transactionRowNum = 5;
		int paymentTypeRowNum = 267;
		int cardDetailsRowNum=1;
		int storeCardDetailsRowNum = 1;
		boolean verifythroughSeamless=false;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, verifythroughSeamless,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
	}

	@Test(description = "save and do transaction using saved Card in seamless flow", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void testTransactionSavedSeamlessStoredcard(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//DC Card 1 - MAST
		int transactionRowNum = 3;
		int cardDetailsRowNum=1;
		int paymentTypeRowNum = 19;
		int storeCardDetailsRowNum = 1;
		boolean verifythroughSeamless=true;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, verifythroughSeamless,ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
		}

	
}
