package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;

public class TestTransactionStoreCard extends TestBase {
	 
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	@Test(description = "Save a new Store Card", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void test1_Save_new_store_card(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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
		
		//DC-MAES
		transactionRowNum = 1;
		paymentTypeRowNum =319;
		cardDetailsRowNum=1;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum,paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
		Assert.assertTrue(testConfig.getTestResult());
	}
		
	@Test(description = "Save Multiple Cards", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void test2_Multiple_CardsOfSameType(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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
	
	
		//DC Card 1 - MAST
		transactionRowNum = 3;
		cardDetailsRowNum=1;
		paymentTypeRowNum = 19;
		storeCardDetailsRowNum = 1;
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
	public void test3_SavedCardTransaction(Config testConfig) throws InterruptedException {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum,verifythroughSeamless, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		//CC - VISA/Master
		transactionRowNum = 2;
		paymentTypeRowNum = 235;
		cardDetailsRowNum=18;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
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
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
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
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum,verifythroughSeamless, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		

		//DC Card 1 - MAST
		transactionRowNum = 3;
		cardDetailsRowNum=1;
		paymentTypeRowNum = 19;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "2");
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
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
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		helper.testResponse = (TestResponsePage)helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.testResponse = (TestResponsePage)helper.DoPaymentWithSavedStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum,verifythroughSeamless, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
		}

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

		//************To Do store card checkbox and link verification for NB, EMI, Cash Card, COD and some DC********
		@Test(description = "Store this card option is not displayed for NB_EMI_CASH if user credentials field is not blank", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
		public void Verify_NBandDC_ForStoreCardOptions(Config testConfig) throws InterruptedException {
			
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			helper.DoLogin();
			
			//******************Net Banking******************
			//AXIS Net Banking
			int transactionRowNum = 1;
			int paymentTypeRowNum = 61;
			int storeCardDetailsRowNum = 1;
			int cardDetailsRowNum=-1;
			testConfig.putRunTimeProperty("UseStoredDetails", "1");
			testConfig.putRunTimeProperty("RemoveStoreCard", "false");
			helper.bankRedirectPage = (BankRedirectPage) helper.DoPaymentAndSaveCard(transactionRowNum,paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
			helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;
			
			//******************DC Banking******************
			//BOI Debit Card
			transactionRowNum = 1;
			paymentTypeRowNum = 10;
			cardDetailsRowNum = -1;
			testConfig.putRunTimeProperty("RemoveStoreCard", "false");
			helper.bankRedirectPage = (BankRedirectPage) helper.DoPaymentAndSaveCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,storeCardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
			helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

			Assert.assertTrue(testConfig.getTestResult());
		}
		
		@Test(description = "Store this card option should not displayed for EMI if user credentials field is not blank", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
		public void Verify_EMI_ForStoreCardOptions(Config testConfig) throws InterruptedException {
			
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			helper.DoLogin();
		
			//******************AXIS EMI******************
			//12 months
			int transactionRowNum = 1;
			int paymentTypeRowNum = 260;
			int cardDetailsRowNum = 1;
			int storeCardDetailsRowNum = 1;
			testConfig.putRunTimeProperty("UseStoredDetails", "1");
			testConfig.putRunTimeProperty("RemoveStoreCard", "false");
			helper.testResponse = (TestResponsePage) helper.DoPaymentAndSaveCard(transactionRowNum,paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);


			//******************HDFC EMI******************
			//12 months
			transactionRowNum = 4;
			paymentTypeRowNum = 41;
			cardDetailsRowNum = 1;
			testConfig.putRunTimeProperty("UseStoredDetails", "1");
			testConfig.putRunTimeProperty("RemoveStoreCard", "false");
			helper.testResponse = (TestResponsePage) helper.DoPaymentAndSaveCard(transactionRowNum,paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
			Assert.assertTrue(testConfig.getTestResult());
		}
		@Test(description = "Store this card option should not displayed for CashCard if user credentials field is not blank", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
			public void Verify_Cash_ForStoreCardOptions(Config testConfig) throws InterruptedException {
				
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			helper.DoLogin();
			
			//******************Airtel Money******************
			//AMON
			int transactionRowNum = 1;
			int paymentTypeRowNum = 52;
			int cardDetailsRowNum = -1;
			int storeCardDetailsRowNum = 1;
			testConfig.putRunTimeProperty("RemoveStoreCard", "false");
			helper.bankRedirectPage = (BankRedirectPage) helper.DoPaymentAndSaveCard(transactionRowNum,paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
			helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

			//******************Citi Premier Miles******************
			//CPMC
			transactionRowNum = 2;
			paymentTypeRowNum = 53;
			cardDetailsRowNum = 1;
			testConfig.putRunTimeProperty("RemoveStoreCard", "true");
			helper.testResponse = (TestResponsePage) helper.DoPaymentAndSaveCard(transactionRowNum,paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
			
			Assert.assertTrue(testConfig.getTestResult());
}
		
		/**
		 * @param args
		 * @throws InterruptedException 
		 */
		@Test(description = "Verify symbols not allowed in storeCard", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
		public void test6_InvalidDataInStoreCard(Config testConfig) throws InterruptedException {
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			helper.DoLogin();

			// CC-DINR
			int transactionRowNum = 2;
			int paymentTypeRowNum = 235;
			int cardDetailsRowNum=18;
			int storeCardDetailsRowNum = 6;
			testConfig.putRunTimeProperty("UseStoredDetails", "1");
			testConfig.putRunTimeProperty("RemoveStoreCard", "false");
			helper.payment = (PaymentOptionsPage)helper.DoPaymentAndSaveCard(transactionRowNum,paymentTypeRowNum,cardDetailsRowNum, storeCardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
			helper.payment.verifyStoreCardErrorLabel("Only alphanumeric,spaces,* and _ allowed.");
			
		}
		
		//delete a store card and ok functionality of manage card. to check learn more
		//check store card from diner with cbk option with or without any other card saved
		//special characters in store card name
		//card number is store card
		//no name is entered in store card
}