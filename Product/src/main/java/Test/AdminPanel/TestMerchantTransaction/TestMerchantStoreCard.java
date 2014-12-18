package Test.AdminPanel.TestMerchantTransaction;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;

public class TestMerchantStoreCard extends TestBase {

	@Test(description = "Verify credit card transaction through AXIS master gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test1_SaveNewCard(Config testConfig) throws Exception
	{	
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//CC-Master card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransactionwithStoreCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum, false,ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		merHelper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
		
		//DC-MAST
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransactionwithStoreCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum,false, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		merHelper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
		
		Assert.assertTrue(testConfig.getTestResult());
	}			

	@Test(description = "Save Multiple Cards", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test2_SaveMultipleCards(Config testConfig) throws Exception {

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//CC Test Card -1
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransactionwithStoreCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum,false, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		merHelper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
		
		//CC Test Card -2
		transactionRowNum = 3;
		paymentTypeRowNum = 328;
		cardDetailsRowNum=19;
		storeCardDetailsRowNum = 2;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransactionwithStoreCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum,false, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		merHelper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
	
		//DC Test Card -1
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum=1;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransactionwithStoreCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum,false, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		merHelper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, true);
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Use Saved Card", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test3_doTransactionWithSavedCardTransaction(Config testConfig) throws InterruptedException {
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//CC Visa/Mast Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransactionwithStoreCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum,false, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransactionwithStoreCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,storeCardDetailsRowNum,true, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		
		//DC MAST Card
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum = 1;
		storeCardDetailsRowNum = 1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransactionwithStoreCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum,false, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("RemoveStoreCard", "false");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransactionwithStoreCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,storeCardDetailsRowNum,true, ExpectedResponsePage.TestResponsePage);
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());

		}

	@Test(description = "Do transaction without Saving Card", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test4_transaction_without_saving_card(Config testConfig) throws Exception {
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//CC - Visa/Mast Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum=1;
		int storeCardDetailsRowNum = -1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransactionwithStoreCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum,false, ExpectedResponsePage.TestResponsePage);
		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		
		//DC - MAST Card
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		cardDetailsRowNum=1;
		storeCardDetailsRowNum = -1;
		testConfig.putRunTimeProperty("UseStoredDetails", "1");
		testConfig.putRunTimeProperty("RemoveStoreCard", "true");
		merHelper.GetTestMerchantTransactionPage();
		merHelper.testResponse = (TestResponsePage) merHelper.DoTestMerchantTransactionwithStoreCard(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, storeCardDetailsRowNum,false, ExpectedResponsePage.TestResponsePage);
		testConfig.putRunTimeProperty("UseStoredDetails", "0");
		merHelper.testResponse.VerifyTransactionResponse(merHelper.transactionData, transactionRowNum, merHelper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Store this card option is not displayed if user credentials field blank", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void test5_transaction_without_userCredential(Config testConfig) throws InterruptedException {
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();
		// CC
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int storeCardDetailsRowNum = -1;
		merHelper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, false);
	
		//DC
		transactionRowNum = 3;
		paymentTypeRowNum = 19;
		storeCardDetailsRowNum = -1;
		merHelper.verifyStoreCard(transactionRowNum,paymentTypeRowNum, storeCardDetailsRowNum, false);
		Assert.assertTrue(testConfig.getTestResult());	
	}

}
