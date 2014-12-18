package Test.AdminPanel.WebServices;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.ManualUpdate.MerchantSettlement;
import PageObject.AdminPanel.ManualUpdate.ReconPage;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.NewResponsePage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import PageObject.AdminPanel.WebServices.WebServicesHelper;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import PageObject.MerchantPanel.Offers.OfferListPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Test.MerchantPanel.Offers.OffersHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.FileHandler;
import Utils.GmailLogin;
import Utils.GmailVerification;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;
import Utils.Helper.FileType;

public class TestWebServicesUI extends TestBase
{

	ManualUpdatePage manualUpdatePage;
	MerchantSettlement merchantSettlement;
	MerchantTransactionsPage merchantTransactionsPage;
	//TODO - Add test cases for -- get_user_cards, vendor_Details
		
	@Test(description = "Test webservices - AXIS", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_AXIS(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//Do a seamless AXIS transaction
		int transactionRowNum = 12;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);

		//Call check_payment using mihpayid
		String actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully", tranhelper.testResponse.actualResponse.get("amount"), "", "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call partial refund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued","", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));

		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 70);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "1.00", "-1.00");

		//Call full refund_transaction/cancelrefund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "2");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued", "", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));

		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 70);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "2.00", "-2.00");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "1.00", "-2.00");

		//Call check_payment using mihpayid
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully",
				tranhelper.testResponse.actualResponse.get("amount"), "", "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Do extra refund for failure
		//Call cancelrefund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "3");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Refund FAILURE - Invalid amount", null, "105");

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Test webservices - HDFC", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_HDFC(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//Do a seamless HDFC transaction
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);

		//Call check_payment using mihpayid
		String actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully",
				tranhelper.testResponse.actualResponse.get("amount"), "", "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call partial refund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued","", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));

		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 70);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "1.00", "-1.00");

		//Call full refund_transaction/cancelrefund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "2");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued", "", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));

		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 60);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "2.00", "-2.00");

		//Call check_payment using mihpayid
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully",
				tranhelper.testResponse.actualResponse.get("amount"), "", "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Do extra refund for failure
		//Call cancelrefund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "3");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Refund FAILURE - Invalid amount", null, "105");

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Test webservices - AMEX", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_AMEX(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//Do a seamless AMEX transaction
		int transactionRowNum = 2;
		int paymentTypeRowNum = 326;
		int cardDetailsRowNum = 55;
		tranhelper.GetTestTransactionPage();
		tranhelper.bankRedirectPage = (BankRedirectPage)tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		tranhelper.testResponse = (TestResponsePage)tranhelper.bankRedirectPage.SubmitButtonForSuccessAMEX();
		tranhelper.testResponse.VerifyTransactionResponse(tranhelper.transactionData, transactionRowNum, tranhelper.paymentTypeData, paymentTypeRowNum);

		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);

		//Call check_payment using mihpayid
		String actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully", 
				tranhelper.testResponse.actualResponse.get("amount"), "", "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call partial refund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued","", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));

		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 70);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "1.00", "-1.00");

		//Call full refund_transaction/cancelrefund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued", "", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));

		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 70);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "1.00", "-1.00");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "1.00", "-1.00");

		//Call check_payment using mihpayid
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully", 
				tranhelper.testResponse.actualResponse.get("amount"), "", "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Do extra refund for failure
		//Call cancelrefund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "3");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Refund FAILURE - Invalid amount", null, "105");

		Assert.assertTrue(testConfig.getTestResult());
	}
	
	
//*****************Store Card API*******************
	        //*************TODO store card param in merchant panel =0, save card should not reflect
			//*************TODO store card api param verification
			//*************TODO for junk input 
            //*************TODO user credential for shared merchant
	        //*************TODO get user card for multiple store card
	//get user card
	@Test(description = "Test webservices - GetUserCards", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_Get_User_Card(Config testConfig) throws InterruptedException {
		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();
        
		//CC - CC
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum=1;
		
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");
		String bankcode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
		String userCredentials = merchantKey + ":" + bankcode;
		
		//Variables to save user card API
		TestDataReader cardData = new  TestDataReader(testConfig, "CardDetails");
		String cardName = cardData.GetData(cardDetailsRowNum, "Name") + merchantKey + "-" + bankcode;
		String cardType = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
		String cardMode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "mode");
		
		String nameOnCard = cardData.GetData(cardDetailsRowNum, "Name"); 
		String cardNo = cardData.GetData(cardDetailsRowNum, "CC"); 
		String cardExpMon = cardData.GetData(cardDetailsRowNum, "Mon"); 
		String cardExpYr = cardData.GetData(cardDetailsRowNum, "Year"); 
				
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		//Call save_user_card web service
		String actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
				
		//Call get_user_cards using user_credential
	    actualWebServiceResponse = wshelper.get_user_cards_executeAdminPanel(tranhelper.home, merchantKey, userCredentials);
		wshelper.get_user_card_verify(actualWebServiceResponse, "1", "Cards fetched Succesfully", "Test Card", "Test CardQBKSbB-CC", "2017", "05", "CC", "23b1b658c3683da34040379f03042a3589a8d3c2", "0", "CC", "512345XXXXXX2346", "MASTERCARD", "512345");

		//Call get_user_cards using invalid user_credential
		userCredentials = merchantKey;
		actualWebServiceResponse = wshelper.get_user_cards_executeAdminPanel(tranhelper.home, merchantKey, userCredentials);
		wshelper.get_user_card_verify(actualWebServiceResponse, "0", "user credential is invalid", "", "", "", "", "", "", "", "", "", "", "");

		//Call get_user_cards using user_credential of other merchant
		userCredentials = "ra:ra";
		actualWebServiceResponse = wshelper.get_user_cards_executeAdminPanel(tranhelper.home, merchantKey, userCredentials);
		wshelper.get_user_card_verify(actualWebServiceResponse, "0", "Card not found.", "", "", "", "", "", "", "", "", "", "", "");
		
		//Call get_user_cards using blank user_credential
		userCredentials = "";
		actualWebServiceResponse = wshelper.get_user_cards_executeAdminPanel(tranhelper.home, merchantKey, userCredentials);
		wshelper.get_user_card_verify(actualWebServiceResponse, "0", "user credential is empty", "", "", "", "", "", "", "", "", "", "", "");

		Assert.assertTrue(testConfig.getTestResult());
	}

//save user card	
	@Test(description = "Test webservices - SaveUserCards", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_Save_User_Card(Config testConfig) {
		
		System.out.println("This test case is failing due to issue #35646");
		
		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();
		
		//CC - CC
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum=1;
		
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");
		String bankcode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
		String userCredentials = merchantKey + ":" + bankcode;
		
		TestDataReader cardData = new  TestDataReader(testConfig, "CardDetails");
		String cardName = cardData.GetData(cardDetailsRowNum, "Name") + merchantKey + "-" + bankcode;
		String cardType = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
		String cardMode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "mode");
		
		String nameOnCard = cardData.GetData(cardDetailsRowNum, "Name"); 
		String cardNo = cardData.GetData(cardDetailsRowNum, "CC"); 
		String cardExpMon = cardData.GetData(cardDetailsRowNum, "Mon"); 
		String cardExpYr = cardData.GetData(cardDetailsRowNum, "Year"); 
		String cardToken = "23b1b658c3683da34040379f03042a3589a8d3c2";
		
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
	    
		//Positive test case for all valid details
		String actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.save_user_card_verify(actualWebServiceResponse, "1", "Card Stored Successfully.", cardToken, "512345XXXXXX2346", cardName);
	  
	    cardToken = null;
		//Save user card with invalid user credentials
	    userCredentials = merchantKey;
		actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
		wshelper.save_user_card_verify(actualWebServiceResponse, "0", "User credentials is invalid", "", "", "");
		
		//Save user card with blank user credentials
		userCredentials = "";
		actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
		wshelper.save_user_card_verify(actualWebServiceResponse, "0", "user credential is empty", "", "", "");
		userCredentials = merchantKey + ":" + bankcode;
		
		//Save user card with blank card mode
		cardMode = "";
		actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
		wshelper.save_user_card_verify(actualWebServiceResponse, "0", "cardMode is empty", "", "", "");
		cardMode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "mode");
		
		//Save user card with blank card type
		cardType = "";
		actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
		wshelper.save_user_card_verify(actualWebServiceResponse, "0", "cardType is empty", "", "", "");
		cardType = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
		
		//Save user card with invalid name on card
		nameOnCard = "!@#$%";
		actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
		wshelper.save_user_card_verify(actualWebServiceResponse, "0", "Name on Card is invalid", "", "", "");
		
		//Save user card with blank name on card
		nameOnCard = "";
		actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
		wshelper.save_user_card_verify(actualWebServiceResponse, "0", "nameOnCard is empty", "", "", "");
		nameOnCard = cardData.GetData(cardDetailsRowNum, "Name");
		
		//Save user card with invalid card number
		cardNo = "5123456789012345";
		actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
		wshelper.save_user_card_verify(actualWebServiceResponse, "0", "CardNumber is invalid", "", "", "");
		
		//Save user card with blank card number
		cardNo = "";
		actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
		wshelper.save_user_card_verify(actualWebServiceResponse, "0", "card No is empty", "", "", "");
		cardNo = cardData.GetData(cardDetailsRowNum, "CC");
		
		//Save user card with invalid card expiry month
		cardExpMon = "13";
		cardExpYr = "2010";
		actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard,cardNo, cardExpMon, cardExpYr);
		wshelper.save_user_card_verify(actualWebServiceResponse, "0", "Expiry Detail is invalid", "", "", "");

		//Save user card with blank expiry month
		cardExpMon = "";
		actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard,cardNo, cardExpMon, cardExpYr);
		wshelper.save_user_card_verify(actualWebServiceResponse, "0", "card expiry month is empty", "", "", "");
		cardExpMon = cardData.GetData(cardDetailsRowNum, "Mon");
		
		//Save user card with blank expiry year
		cardExpYr = "";
		actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard,cardNo, cardExpMon, cardExpYr);
		wshelper.save_user_card_verify(actualWebServiceResponse, "0", "card expiry year is empty", "", "", "");
		cardExpYr = cardData.GetData(cardDetailsRowNum, "Year");
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
//Edit user card
	@Test(description = "Test webservices - EditUserCards", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_Edit_User_Card(Config testConfig) {
		
		System.out.println("This test case is failing due to issue #35646");
		
		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();
		
		//CC - CC
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum=1;
		
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");
		String bankcode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
		String userCredentials = merchantKey + ":" + bankcode;
		
		String cardToken = "23b1b658c3683da34040379f03042a3589a8d3c2";
		
		TestDataReader cardData = new  TestDataReader(testConfig, "CardDetails");
		String cardName = cardData.GetData(cardDetailsRowNum, "Name") + merchantKey + "-" + bankcode;
		String cardType = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
		String cardMode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "mode");
		
		String nameOnCard = cardData.GetData(cardDetailsRowNum, "Name"); 
		String cardNo = cardData.GetData(cardDetailsRowNum, "CC"); 
		String cardExpMon = cardData.GetData(cardDetailsRowNum, "Mon"); 
		String cardExpYr = cardData.GetData(cardDetailsRowNum, "Year"); 
		
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
	      
		//Call save_user_card web service
		String actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
			  
		//Call edit_user_card web service
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "1", "Test CardQBKSbB-CC edited Successfully.", cardToken, "512345XXXXXX2346", cardName);
	    
	    //Call edit_user_card web service with invalid user credentials
	    userCredentials = merchantKey;
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "User credentials is invalid", "", "", "");
	    
	    //Call edit_user_card web service with blank user credentials
	    userCredentials = "";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "user credential is empty", "", "", "");
	    userCredentials = merchantKey + ":" + bankcode;
	    
	    //Call edit_user_card web service with invalid cardToken
	    cardToken = "2482d97b0829cd5b0ee69fd14ac1f1c6035d6";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "cardToken is invalid", "", "", "");

	    //Call edit_user_card web service with blank cardToken
	    cardToken = "";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "card token is empty", "", "", "");
	    cardToken = "2482d97b0829cd5b0ee69fd14ac1f1c6035d6ca5";
	    
	    //Call edit_user_card web service with blank cardMode
	    cardMode = "";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "cardMode is empty", "", "", "");
	    cardMode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "mode");
	    
	    //Call edit_user_card web service with blank cardType
	    cardType = "";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "cardType is empty", "", "", "");
	    cardType = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
	    
	    //Call edit_user_card web service with invalid name on card
	    nameOnCard = "!@#$%";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "Name on Card is invalid", "", "", "");
	    	    
	    //Call edit_user_card web service with blank name on card
	    nameOnCard = "";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "nameOnCard is empty", "", "", "");
	    nameOnCard = cardData.GetData(cardDetailsRowNum, "Name");
	    
	    //Call edit_user_card web service with invalid card number
	    cardNo = "5123456789012345";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "CardNumber is invalid", "", "", "");
	    
	    //Call edit_user_card web service with blank cardNo
	    cardNo = "";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "card No is empty", "", "", "");
	    cardNo = cardData.GetData(cardDetailsRowNum, "CC"); 
	    
	    //Call edit_user_card web service with invalid card expiry details
	    cardExpMon = "13";
		cardExpYr = "2010";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "Expiry Detail is invalid", "", "", "");
	    cardExpMon = cardData.GetData(cardDetailsRowNum, "Mon"); 
		cardExpYr = cardData.GetData(cardDetailsRowNum, "Year"); 
		
		//Call edit_user_card web service with blank card expiry month
		cardExpMon = "";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "card expiry month is empty", "", "", "");
	    cardExpMon = cardData.GetData(cardDetailsRowNum, "Mon"); 
	    
	    //Call edit_user_card web service with blank card expiry year
	    cardExpYr = "";
	    actualWebServiceResponse = wshelper.edit_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
	    wshelper.edit_user_card_verify(actualWebServiceResponse, "0", "card expiry year is empty", "", "", "");
	    cardExpYr = cardData.GetData(cardDetailsRowNum, "Year");
	    
	   	Assert.assertTrue(testConfig.getTestResult());
	}   

	//Delete user card
		@Test(description = "Test webservices -DeleteUserCards", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
		public void test_WS_Delete_User_Card(Config testConfig) {
			
		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();
			
		//CC - CC
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum=1;
			
		//parameters for delete user card
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");
		String bankcode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
		String userCredentials = merchantKey + ":" + bankcode;
			
		String cardToken = "23b1b658c3683da34040379f03042a3589a8d3c2";
			
		//Additional parameters for save user card
		TestDataReader cardData = new  TestDataReader(testConfig, "CardDetails");
		String cardName = cardData.GetData(cardDetailsRowNum, "Name") + merchantKey + "-" + bankcode;
		String cardType = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
		String cardMode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "mode");
			
		String nameOnCard = cardData.GetData(cardDetailsRowNum, "Name"); 
		String cardNo = cardData.GetData(cardDetailsRowNum, "CC"); 
		String cardExpMon = cardData.GetData(cardDetailsRowNum, "Mon"); 
		String cardExpYr = cardData.GetData(cardDetailsRowNum, "Year"); 
			
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		    
		//Call save_user_card web service
		String actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
				  
		//Call delete_user_card web service with all valid details
		actualWebServiceResponse = wshelper.delete_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
		wshelper.delete_user_card_verify(actualWebServiceResponse, "1", "Test CardQBKSbB-CC card deleted successfully");
		    
		//Call delete_user_card web service with invalid user credentials
		userCredentials = merchantKey;
		actualWebServiceResponse = wshelper.delete_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
		wshelper.delete_user_card_verify(actualWebServiceResponse, "0", "user credential is invalid");
		    
		//Call delete_user_card web service with user credentials of other merchant
		userCredentials = "ra:ra";
		actualWebServiceResponse = wshelper.delete_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
		wshelper.delete_user_card_verify(actualWebServiceResponse, "0", "cardToken is invalid");
		    
		//Call delete_user_card web service with blank user credentials
		userCredentials = "";
		actualWebServiceResponse = wshelper.delete_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
		wshelper.delete_user_card_verify(actualWebServiceResponse, "0", "user credential is empty");
		userCredentials = merchantKey + ":" + bankcode;
		    
		//Call delete_user_card web service with invalid cardToken
		cardToken = "2482d97b0829cd5b0ee69fd14ac1f1c6035d6";
		actualWebServiceResponse = wshelper.delete_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
		wshelper.delete_user_card_verify(actualWebServiceResponse, "0", "cardToken is invalid");
		    
		//Call delete_user_card web service with blank cardToken
		cardToken = "";
		actualWebServiceResponse = wshelper.delete_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
		wshelper.delete_user_card_verify(actualWebServiceResponse, "0", "Card token missing");    
		
        Assert.assertTrue(testConfig.getTestResult());
	}
		
		//Get user card	details
		@Test(description = "Test webservices - GetUserCardDetails", dataProvider = "GetTestConfig", timeOut=600000, groups="1")
		public void test_WS_Get_UserCard_Details(Config testConfig) {
			
			TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
			tranhelper.DoLogin();
			
			//CC - CC
			int transactionRowNum = 43;
			int paymentTypeRowNum = 3;
			int cardDetailsRowNum=1;
			
			String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");
			String bankcode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
			String userCredentials = merchantKey + ":" + bankcode;
			
			TestDataReader cardData = new  TestDataReader(testConfig, "CardDetails");
			String cardName = cardData.GetData(cardDetailsRowNum, "Name") + merchantKey + "-" + bankcode;
			String cardType = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
			String cardMode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "mode");
			
			String nameOnCard = cardData.GetData(cardDetailsRowNum, "Name"); 
			String cardNo = cardData.GetData(cardDetailsRowNum, "CC"); 
			String cardExpMon = cardData.GetData(cardDetailsRowNum, "Mon"); 
			String cardExpYr = cardData.GetData(cardDetailsRowNum, "Year"); 
			String cardToken = "c7cc6a0acd51d11a05887b06b1496a7637f6f46e";
			
			WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		    
			//Call save user card using all valid details 
			String actualWebServiceResponse = wshelper.save_user_card_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardName, cardMode, cardType, nameOnCard, cardNo, cardExpMon, cardExpYr);
			            
   		    //Call get_usercard_details using all valid details
		    actualWebServiceResponse = wshelper.get_usercard_details_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
			wshelper.get_usercard_details_verify(actualWebServiceResponse, "1", "Card details fetched Succesfully", "2017", "05", "CC", "Test Card", "c7cc6a0acd51d11a05887b06b1496a7637f6f46e", "CC");
			
			//Call get_usercard_details using invalid user credentials 
			userCredentials = merchantKey;
			actualWebServiceResponse = wshelper.get_usercard_details_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
			wshelper.get_usercard_details_verify(actualWebServiceResponse, "0", "user credential is invalid", "", "", "", "", "", "");
			
			//Call get_usercard_details using blank user credentials 
			userCredentials = "";
			actualWebServiceResponse = wshelper.get_usercard_details_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
			wshelper.get_usercard_details_verify(actualWebServiceResponse, "0", "user credential is empty", "", "", "", "", "", "");
			userCredentials = merchantKey + ":" + bankcode;
			
			//Call get_usercard_details using invalid card token
			cardToken = "c7cc6a0acd51d11a05887b06b1496a7637f6f4";
			actualWebServiceResponse = wshelper.get_usercard_details_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
			wshelper.get_usercard_details_verify(actualWebServiceResponse, "0", "Card details not found for the given token.", "", "", "", "", "", "");
			
			//Call get_usercard_details using blank card token
			cardToken = "";
			actualWebServiceResponse = wshelper.get_usercard_details_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
			wshelper.get_usercard_details_verify(actualWebServiceResponse, "0", "card token is empty", "", "", "", "", "", "");
			cardToken = "c7cc6a0acd51d11a05887b06b1496a7637f6f46e";
			
			//Call get_usercard_details using valid user credentials and card token of other merchant
			//This card is stored for testMerchant3
			userCredentials = "auto:auto";
			cardToken = "ec77bf24410254b1284427e04f6d4dee75b799aa";
			actualWebServiceResponse = wshelper.get_usercard_details_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
			wshelper.get_usercard_details_verify(actualWebServiceResponse, "0", "Card details not found for the given token.", "", "", "", "", "", "");
			userCredentials = merchantKey + ":" + bankcode;
			cardToken = "c7cc6a0acd51d11a05887b06b1496a7637f6f46e";
			
			//Call get_usercard_details for the merchant for which store card public key is not uploaded through merchant panel
			transactionRowNum = 1;
			merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");
			bankcode = tranhelper.paymentTypeData.GetData(paymentTypeRowNum, "bankcode");
			userCredentials = merchantKey + ":" + bankcode;
			cardToken = "23b1b658c3683da34040379f03042a3589a8d3c2";
			actualWebServiceResponse = wshelper.get_usercard_details_executeAdminPanel(tranhelper.home, merchantKey, userCredentials, cardToken);
			wshelper.get_usercard_details_verify(actualWebServiceResponse, "0", "Card details cannot be fetched now. Please try again.", "", "", "", "", "", "");
			
			Assert.assertTrue(testConfig.getTestResult());
		}
		
	
	@Test(description = "Test webservices - COD Verify",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_COD(Config testConfig) 
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();

		//Do in-progress COD without Zipdial 
		int transactionRowNum = 23;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse =(TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.TestResponsePage);
		tranhelper.testResponse.VerifyTransactionResponse(tranhelper.transactionData, transactionRowNum, tranhelper.paymentTypeData, paymentTypeRowNum, codRowNum);

		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Call check_payment using mihpayid
		String actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully",
				"", "","");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "");

		Assert.assertTrue(testConfig.getTestResult());
		testConfig.logFail("Test case failed, because of redmine issue: 20665");
	}
	//public void test_WS_CITI_Capture(Config testConfig) - moved to LiveCard TestCase - 22

	//public void test_WS_CITI_Cancel(Config testConfig) - moved to LiveCard TestCase - 21


	@Test(description = "Test webservices - AXIS with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_AXIS_CF(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//Do a seamless AXIS transaction
		int transactionRowNum = 12;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		tranhelper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CC:2");
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		tranhelper.testResponse.overrideExpectedTransactionData = true;
		String amnt = tranhelper.testResponse.actualResponse.get("amount");
		double amount = Double.parseDouble(amnt);
        
		String addCharge = tranhelper.testResponse.actualResponse.get("additionalCharges");
		double additionalCharges = Double.parseDouble(addCharge);
		amount = amount+additionalCharges;	
		
		String transactionAmount = String.valueOf(amount)+"0";
		testConfig.putRunTimeProperty("amount", transactionAmount);
        
		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);

		//Call check_payment using mihpayid
		String actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully", transactionAmount, "", "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call partial refund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued","", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));

		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 70);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "1.00", "-1.00");

		//Call full refund_transaction/cancelrefund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "2");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued", "", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));

		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 60);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "2.00", "-2.00");

		//Call refund_transaction/cancelrefund_transaction for additional charge using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "2");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Refund FAILURE - Invalid amount", null, "105");

		//Call check_payment using mihpayid
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully", 
				transactionAmount, "", "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Do extra refund for failure
		//Call cancelrefund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "3");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Refund FAILURE - Invalid amount", null, "105");

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Test webservices - HDFC with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_HDFC_CF(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//Do a seamless HDFC transaction
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		tranhelper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "CC:2");
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		tranhelper.testResponse.overrideExpectedTransactionData = true;
		String amnt = tranhelper.testResponse.actualResponse.get("amount");
		double amount = Double.parseDouble(amnt);

		String addCharge = tranhelper.testResponse.actualResponse.get("additionalCharges");
		double additionalCharges = Double.parseDouble(addCharge);
		amount = amount+additionalCharges;	
		String transactionAmount = String.valueOf(amount)+"0";
		testConfig.putRunTimeProperty("amount", transactionAmount);		

		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);

		//Call check_payment using mihpayid
		String actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully", 
				transactionAmount, "", "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call partial refund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued","", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));

		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 70);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "1.00", "-1.00");

		//Call full refund_transaction/cancelrefund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "2");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued", "", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));

		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 60);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "2.00", "-2.00");

		//Call refund_transaction/cancelrefund_transaction for additional charge using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "2");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Refund FAILURE - Invalid amount", null, "105");

		//Call check_payment using mihpayid
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully", 
				transactionAmount, "", "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Perform a captured transaction, upload settlement file, verify the transaction data in webservices and update UTR through UpdateSettlement webservice", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_WS_UpdateSettlement(Config testConfig) {

		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		int transactionRowNum = 12;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;	

		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);

		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		//Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);
		String settlementFile = Helper.getExcelFile(testConfig, FileType.esscomSettlement);

		FileHandler.write_edit(settlementFile,"Sheet1", 1, 4, tranhelper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 6, tranhelper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 7, tranhelper.GetTxnUpdateData()[0], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 8, tranhelper.GetTxnUpdateData()[1], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 9, tranhelper.testResponse.actualResponse.get("amount"), 0);

		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 10, random_UTR, 0);

		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 11, "'"+current_date, 0);

		//Navigating to Merchant Settlement 		
		tranhelper.home = tranhelper.testResponse.ClickHomeLink();
		manualUpdatePage = tranhelper.home.ClickManualTransactionUpdate();

		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();
		merchantSettlement.esscomSettlementFileUpload(testConfig, settlementFile);

		//Webservices
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Call check_payment using mihpayid
		String actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully", 
				tranhelper.testResponse.actualResponse.get("amount"), random_UTR, "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", random_UTR, "0000-00-00 00:00:00");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", random_UTR, "0000-00-00 00:00:00");

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify check_offer_status for valid offer", dataProvider = "GetTestConfig", 
			timeOut=600000, groups = "1")
	public void test_WS_ValidOfferStatusTransaction(Config testConfig)
	{
		int merchantLoginRow = 1;
		int transactionRowNum = 12;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		// merchant login
		dashboardHelper.doMerchantLogin(merchantLoginRow);
		new OfferListPage(testConfig);
		OffersHelper offersHelper = new OffersHelper(testConfig);

		//set offer to use for transactions
		offersHelper.setOfferInfo("ValidOffers");
		String offerKey = testConfig.getRunTimeProperty("offerKey");
		String offerCount = testConfig.getRunTimeProperty("offerCount");
		double offerMaxCount = Double.parseDouble(offerCount);
		String offerRemainingCount = testConfig.getRunTimeProperty("offerRemainingCount");
		double offerLeftCount = Double.parseDouble(offerRemainingCount);
		double offerAvailedCount = offerMaxCount-offerLeftCount;
		String offerAvailed = String.valueOf(offerAvailedCount)+"0";
		if (offerAvailed.equals("0.00"))
		{
			offerAvailed = "0";
		}

		// login to admin
		tranhelper.DoLogin();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		TestDataReader paymentData = new TestDataReader(testConfig, "PaymentType");
		TestDataReader cardDetailsData = new TestDataReader(testConfig, "CardDetails");

		//Go to webservices to check offer api
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		String merchantKey = transactionData.GetData(transactionRowNum, "key");
		String amount = transactionData.GetData(transactionRowNum, "amount");
		String pg = paymentData.GetData(paymentTypeRowNum, "bankcode");
		String bankcode = paymentData.GetData(paymentTypeRowNum, "bankcode");
		String ccnum = cardDetailsData.GetData(cardDetailsRowNum, "cc");
		String ccname = cardDetailsData.GetData(cardDetailsRowNum, "Name");
		String phone = transactionData.GetData(transactionRowNum, "phone");
		String email = transactionData.GetData(transactionRowNum, "email");
		String availDiscount = "2";

		TestDataReader offerDataSheet = new TestDataReader(testConfig, "offerAPI");
		//verify check_offer_status for CC VISA pType
		String actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, pg, bankcode,ccnum, ccname, phone, email);
		wshelper.check_valid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "1", "Valid Offer", availDiscount, "creditcard", offerKey, "instant", offerAvailed, offerCount);

		//verify check_offer_status for CC AMEX pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, pg, "AMEX",ccnum, ccname, phone, email);
		wshelper.check_valid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "1", "Valid Offer", availDiscount, "creditcard", offerKey, "instant", offerAvailed, offerCount);

		//verify check_offer_status for CC DINR pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, pg, "DINR",ccnum, ccname, phone, email);
		wshelper.check_valid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "1", "Valid Offer", availDiscount, "creditcard", offerKey, "instant", offerAvailed, offerCount);

		//verify check_offer_status for CC pType with invalid bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "CC", Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for CC pType with blank bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "CC", "","","", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for DC MASTER pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "MAST",ccnum, ccname, phone, email);
		wshelper.check_valid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "1", "Valid Offer", availDiscount, "debitcard", offerKey, "instant", offerAvailed, offerCount);

		//verify check_offer_status for DC VISA pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "VISA",ccnum, ccname, phone, email);
		wshelper.check_valid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "1", "Valid Offer", availDiscount, "debitcard", offerKey, "instant", offerAvailed, offerCount);

		//verify check_offer_status for DC MAESTRO pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "MAES",ccnum, ccname, phone, email);
		wshelper.check_valid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "1", "Valid Offer", availDiscount, "debitcard", offerKey, "instant", offerAvailed, offerCount);

		//verify check_offer_status for DC pType with invalid bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for DC pType with blank bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "","","", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for NB Allahabad pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", "ALLB",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for NB Allahabad pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", "ALLB","", "", "", "");
		wshelper.check_valid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "1", "Valid Offer", availDiscount, "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for NB pType with invalid bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for NB pType with blank bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", "","","", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for valid offer with valid offer and invalid amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for valid offer with valid offerkey, amount and invalid pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, amount, Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4),
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for valid offer with valid offerkey, amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, "C0Dr8m", 
				offerKey,amount, pg, bankcode,ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for valid offer with valid offer and blank amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, "", "", "", "",	"", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "Unknown",  offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for valid offer with valid offerkey, amount and blank pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, amount, "", "", "", "",	"", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "Unknown",  offerKey, "instant", "Unknown", "Unknown");

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify check_offer_status for expired offer", dataProvider = "GetTestConfig", 
			timeOut=600000, groups = "1")
	public void test_WS_ExpiredOfferStatusTransaction(Config testConfig)
	{
		int transactionRowNum = 12;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);

		// login to admin
		tranhelper.DoLogin();
		String offerKey = "invalidoffer@689";
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		TestDataReader paymentData = new TestDataReader(testConfig, "PaymentType");
		TestDataReader cardDetailsData = new TestDataReader(testConfig, "CardDetails");

		//Go to webservices to check offer api
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		String merchantKey = transactionData.GetData(transactionRowNum, "key");
		String amount = transactionData.GetData(transactionRowNum, "amount");
		String pg = paymentData.GetData(paymentTypeRowNum, "bankcode");
		String bankcode = paymentData.GetData(paymentTypeRowNum, "bankcode");
		String ccnum = cardDetailsData.GetData(cardDetailsRowNum, "cc");
		String ccname = cardDetailsData.GetData(cardDetailsRowNum, "Name");
		String phone = transactionData.GetData(transactionRowNum, "phone");
		String email = transactionData.GetData(transactionRowNum, "email");

		TestDataReader offerDataSheet = new TestDataReader(testConfig, "offerAPI");
		//verify check_offer_status for expired offer on CC VISA pType
		String actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, pg, bankcode,ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on CC AMEX pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, pg, "AMEX",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on CC DINR pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, pg, "DINR",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on CC pType with invalid bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "CC", Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on CC pType with blank bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "CC", "","","", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on DC MASTER pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "MAST",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on DC VISA pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "VISA",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on DC MAESTRO pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "MAES",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on DC pType with invalid bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on DC pType with blank bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "","","", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on NB Allahabad pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", "ALLB",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on NB pType with invalid bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer on NB pType with blank bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", "","","", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer with invalid offer, amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				"SRT0", Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", null, "SRT0", "Unknown", "Unknown", "Unknown");

		//verify check_offer_status for expired offer with valid offer and invalid amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer with valid offerkey, amount and invalid pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, amount, Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4),
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for expired offer with valid offer and blank amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, "", "", "", "",	"", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify check_offer_status for invalid offer", dataProvider = "GetTestConfig", 
			timeOut=600000, groups = "1")
	public void test_WS_InvalidOfferStatusTransaction(Config testConfig)
	{
		int merchantLoginRow = 1;
		int transactionRowNum = 12;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		// merchant login
		dashboardHelper.doMerchantLogin(merchantLoginRow);
		new OfferListPage(testConfig);
		OffersHelper offersHelper = new OffersHelper(testConfig);

		//set offer to use for transactions
		offersHelper.setOfferInfo("OfferForCCwithoutBin");
		String offerKey = testConfig.getRunTimeProperty("offerKey");

		// login to admin
		tranhelper.DoLogin();
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		TestDataReader paymentData = new TestDataReader(testConfig, "PaymentType");
		TestDataReader cardDetailsData = new TestDataReader(testConfig, "CardDetails");

		//Go to webservices to check offer api
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		String merchantKey = transactionData.GetData(transactionRowNum, "key");
		String amount = transactionData.GetData(transactionRowNum, "amount");
		String pg = paymentData.GetData(paymentTypeRowNum, "bankcode");
		String bankcode = paymentData.GetData(paymentTypeRowNum, "bankcode");
		String ccnum = cardDetailsData.GetData(cardDetailsRowNum, "cc");
		String ccname = cardDetailsData.GetData(cardDetailsRowNum, "Name");
		String phone = transactionData.GetData(transactionRowNum, "phone");
		String email = transactionData.GetData(transactionRowNum, "email");
		String availDiscount = "2";

		TestDataReader offerDataSheet = new TestDataReader(testConfig, "offerAPI");
		//verify check_offer_status for invalid offer on DC MASTER pType
		String actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "MAST",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer on DC VISA pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "VISA",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer on DC MAESTRO pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "MAES",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer on DC pType with invalid bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer on DC pType with blank bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "","","", "", "");
		wshelper.check_valid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "1", "Valid Offer", availDiscount, "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer on NB Allahabad pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", "ALLB",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer on NB pType with invalid bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer on NB pType with blank bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", "","","", "", "");
		
		wshelper.check_valid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "1", "Valid Offer", availDiscount, "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer with invalid offer, amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				"SRT0", Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "", "SRT0", "Unknown", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer with valid offer and invalid amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer with valid offerkey, amount and invalid pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, amount, Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4),
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer with valid offerkey, amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, "C0Dr8m", 
				offerKey,amount, pg, bankcode,ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer with blank offer, amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				"", "", "", "", "",	"", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "", "", "Unknown", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer with valid offer and blank amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, "", "", "", "",	"", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for invalid offer with valid offerkey, amount and blank pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, amount, "", "", "", "",	"", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E002", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify check_offer_status for exhausted offer", dataProvider = "GetTestConfig", 
			timeOut=600000, groups = "1")
	public void test_WS_ExhaustedOfferStatusTransaction(Config testConfig)
	{
		int merchantLoginRow = 1;
		int transactionRowNum = 12;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		// merchant login
		dashboardHelper.doMerchantLogin(merchantLoginRow);
		new OfferListPage(testConfig);
		OffersHelper offersHelper = new OffersHelper(testConfig);

		//set offer to use for transactions
		offersHelper.setOfferInfo("CountExceededOffer");
		String offerKey = testConfig.getRunTimeProperty("offerKey");

		// login to admin
		tranhelper.DoLogin();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		TestDataReader paymentData = new TestDataReader(testConfig, "PaymentType");
		TestDataReader cardDetailsData = new TestDataReader(testConfig, "CardDetails");
		String merchantKey = transactionData.GetData(transactionRowNum, "key");
		String amount = transactionData.GetData(transactionRowNum, "amount");
		String pg = paymentData.GetData(paymentTypeRowNum, "bankcode");
		String bankcode = paymentData.GetData(paymentTypeRowNum, "bankcode");
		String ccnum = cardDetailsData.GetData(cardDetailsRowNum, "cc");
		String ccname = cardDetailsData.GetData(cardDetailsRowNum, "Name");
		String phone = transactionData.GetData(transactionRowNum, "phone");
		String email = transactionData.GetData(transactionRowNum, "email");

		// make transaction with created offer
		tranhelper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", "10.00");
		testConfig.putRunTimeProperty("discount", "0.00");

		TestDataReader offerDataSheet = new TestDataReader(testConfig, "offerAPI");
		//Go to webservices to check offer api
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		//verify check_offer_status for invalid offer on CC VISA pType

		String actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, pg, bankcode,ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on CC AMEX pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, pg, "AMEX",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on CC DINR pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, pg, "DINR",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on CC pType with invalid bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "CC", Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on CC pType with blank bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "CC", "","","", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "creditcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on DC MASTER pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "MAST",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on DC VISA pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "VISA",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on DC MAESTRO pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "MAES",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on DC pType with invalid bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on DC pType with blank bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "DC", "","","", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "debitcard", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on NB Allahabad pType
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", "ALLB",ccnum, ccname, phone, email);
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on NB pType with invalid bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer on NB pType with blank bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey,amount, "NB", "","","", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "netbanking", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer with valid offer and invalid amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4),Helper.generateRandomAlphaNumericString(4), 
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer with valid offerkey, amount and invalid pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, amount, Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4),
				Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(4), Helper.generateRandomAlphaNumericString(10), Helper.generateRandomAlphaNumericString(10));
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		//verify check_offer_status for exhausted offer with valid offer and blank amount, pg, bankcode, ccnum, ccname, phone and email
		actualWebServiceResponse = wshelper.check_offer_status_executeAdminPanel(tranhelper.home, merchantKey, 
				offerKey, "", "", "", "",	"", "", "");
		wshelper.check_invalid_offer_status_verify(actualWebServiceResponse, offerDataSheet, "0", "Invalid offer", "E001", "Unknown", offerKey, "instant", "Unknown", "Unknown");

		/*
		//make transaction with exhausted count
		tranhelper.GetTestTransactionPage();

		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
							ExpectedResponsePage.TestResponsePage);
		tranhelper.testResponse.overrideExpectedTransactionData = true;

		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Offer count exhausted.");
		tranhelper.testResponse.VerifyTransactionResponse(tranhelper.transactionData,transactionRowNum, tranhelper.paymentTypeData, paymentTypeRowNum);
		// make transaction with created expired offer
		tranhelper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offer_type", "");
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
		tranhelper.testResponse.overrideExpectedTransactionData = true;
		testConfig.putRunTimeProperty("Offer_Failure_Reason", "Invalid Offer for merchant.");
		tranhelper.testResponse.VerifyTransactionResponse(tranhelper.transactionData,transactionRowNum, tranhelper.paymentTypeData, paymentTypeRowNum);
		 */

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify if usercancelled transactions can be refunded/cancelled/captured", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_Usercancelled(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();

		int transactionRowNum = 23;
		int paymentTypeRowNum = 6;
		int cardDetailsRowNum = 1;
		tranhelper.GetTestTransactionPage();
		tranhelper.tryAgainPage = (TryAgainPage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
		tranhelper.tryAgainPage.clickTryAgainButton();
		tranhelper.testResponse = tranhelper.payment.clickGoBackToMerchantName("AutomationMerchant12");

		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Call refund_transaction using mihpayid, token and amount
		String	actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Refund failed", null, "120");

		//Call check_payment using mihpayid
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully", "", "", "");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "");

		//Call get_payment_status using txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("transactionId"));
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "");

		//Call capture_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.capture_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"));
		wshelper.capture_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Capture failed", "", "120");

		//Call cod_cancel using mihpayid, token and  amount
		actualWebServiceResponse = wshelper.cod_cancel_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "3");
		wshelper.cod_cancel_verify(actualWebServiceResponse, expectedTransactionResponse, "0","FAILURE - Transaction lock could not be obtained.", "","120");

		//Call cod_verify using mihpayid, token and  amount
		actualWebServiceResponse = wshelper.cod_verify_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"),Helper.generateRandomAlphaNumericString(7), "3");
		wshelper.cod_verify_verify(actualWebServiceResponse, expectedTransactionResponse, "0","FAILURE - Transaction lock could not be obtained.", "", "120");

		//Call cod_settled using mihpayid, token and  amount
		actualWebServiceResponse = wshelper.cod_settled_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "3");
		wshelper.cod_settled_verify(actualWebServiceResponse, expectedTransactionResponse, "0","FAILURE - Transaction lock could not be obtained.", "","120");

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify if bounced transactions can be refunded/cancelled/captured", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_Bounced(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		String txnId = wshelper.GetBouncedTransactionsTxnID();
		String merchantKey = wshelper.GetBouncedTransactions();

		//Get status using txnid
		String actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, txnId);
		Hashtable<String, String> parsedWebServiceResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
		Hashtable<String, String> parsedWebService = Helper.convertPhpArrayResponseToJavaList(parsedWebServiceResponse.get("transaction_details"));

		parsedWebService = Helper.convertPhpArrayResponseToJavaList(parsedWebService.get(txnId));

		Helper.compareEquals(testConfig, "Status of bounced transaction" , "failure", parsedWebService.get("status"));
		Helper.compareEquals(testConfig, "Unmappedstatus of bounced transaction" , "bounced", parsedWebService.get("unmappedstatus"));
		
		/*Commenting the code since presently refund of transaction with invalid status is causing exception 
		//Call refund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey,parsedWebService.get("mihpayid"), parsedWebService.get("amt"));
		wshelper.refund_transaction_verify(actualWebServiceResponse, parsedWebService, "0","Refund failed", null, "111");
		 */
		
		//Call cancel_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancel_transaction_executeAdminPanel(tranhelper.home, merchantKey,parsedWebService.get("mihpayid"), parsedWebService.get("amt"));
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, parsedWebService, "0","Cancelled failed", "","120");

		//Call capture_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.capture_transaction_executeAdminPanel(tranhelper.home, merchantKey,parsedWebService.get("mihpayid"));
		wshelper.capture_transaction_verify(actualWebServiceResponse, parsedWebService, "0","Capture failed", "", "120");

		testConfig.logComment("Test fails due to : http://redmine.ibibo.com/issues/23286");
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Verify if dropped transactions can be refunded/cancelled/captured", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_Dropped(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, false);
		tranhelper.DoLogin();

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		String txnId = wshelper.GetDroppedTransactionsTxnID();
		String merchantKey = wshelper.GetDroppedTransactions();

		//Get status using txnid
		String actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, txnId);
		Hashtable<String, String> parsedWebServiceResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
		Hashtable<String, String> parsedWebService = Helper.convertPhpArrayResponseToJavaList(parsedWebServiceResponse.get("transaction_details"));

		parsedWebService = Helper.convertPhpArrayResponseToJavaList(parsedWebService.get(txnId));

		Helper.compareEquals(testConfig, "Status of bounced transaction" , "failure", parsedWebService.get("status"));
		Helper.compareEquals(testConfig, "Unmappedstatus of bounced transaction" , "dropped", parsedWebService.get("unmappedstatus"));

		//Call refund_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey,parsedWebService.get("mihpayid"), parsedWebService.get("amt"));
		wshelper.refund_transaction_verify(actualWebServiceResponse, parsedWebService, "0","Refund failed", null, "111");

		//Call cancel_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancel_transaction_executeAdminPanel(tranhelper.home, merchantKey,parsedWebService.get("mihpayid"), parsedWebService.get("amt"));
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, parsedWebService, "0","Cancelled failed", "","111");

		//Call capture_transaction using mihpayid, token and amount
		actualWebServiceResponse = wshelper.capture_transaction_executeAdminPanel(tranhelper.home, merchantKey,parsedWebService.get("mihpayid"));
		wshelper.capture_transaction_verify(actualWebServiceResponse, parsedWebService, "0","Capture failed", "", "111");

		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Test webservices", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_Errors(Config testConfig)
	{

		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//Do a successful seamless transaction
		int transactionRowNum = 12;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);	

		//Call cancel_transaction entering captured transaction's mihpayid and invalid merchantkey
		String actualWebServiceResponse = wshelper.cancel_transaction_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse.get("mihpayid"), "3");
		wshelper.cancel_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "Cancelled failed", null, "116");

		//Call cancel_transaction entering captured transaction's mihpayid
		actualWebServiceResponse = wshelper.cancel_transaction_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "3");
		wshelper.cancel_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "Cancelled failed", null, "111");

		//Call capture_transaction entering captured transaction's mihpayid and invalid merchantkey
		actualWebServiceResponse = wshelper.capture_transaction_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse.get("mihpayid"));
		wshelper.capture_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "Capture failed", null, "116");

		//Call capture_transaction entering captured transaction's mihpayid
		actualWebServiceResponse = wshelper.capture_transaction_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.capture_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "Capture failed", null, "111");

		//Call check_payment entering invalid merchantkey
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "Invalid Merchant Key", null, null, null);

		//Call check_payment entering blank mihpayid
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, "");
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "Parameter missing", null, null, null);

		//Call check_payment entering invalid mihpayid
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, "$^%&");
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "Invalid Merchant Key", null, null, null);

		//Call verify_payment entering invalid merchantkey
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse.get("mihpayid"));
		wshelper.verify_payment_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "0 out of 1 Transactions Fetched Successfully", "");

		//Call verify_payment entering blank txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey, "");
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "Parameter missing", null, null);

		//Call verify_payment entering invalid txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey, Helper.generateRandomAlphaNumericString(5));
		wshelper.verify_payment_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "0 out of 1 Transactions Fetched Successfully", "");

		//Call get_payment_status entering invalid merchantkey
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse.get("mihpayid"));
		wshelper.get_payment_status_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "0 out of 1 Transactions Fetched Successfully", "");

		//Call get_payment_status entering blank txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, "");
		wshelper.get_payment_status_verify(actualWebServiceResponse, expectedTransactionResponse,  "0", "Parameter missing", null, null);

		//Call get_payment_status entering invalid txnid
		actualWebServiceResponse = wshelper.get_payment_status_executeAdminPanel(tranhelper.home, merchantKey, Helper.generateRandomAlphaNumericString(5));
		wshelper.get_payment_status_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "0 out of 1 Transactions Fetched Successfully", "");

		//Call refund_transaction entering valid mihpayid, token,amount and invalid merchantkey
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse.get("mihpayid"), Helper.generateRandomAlphabetsString(7),"3");
		wshelper.refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Refund failed", "","116");

		//Call refund_transaction entering blank mihpayid, token and amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey,"", "");
		wshelper.refund_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Invalid PayU ID");

		//Call refund_transaction entering valid mihpayid, blank token and amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "", "");
		wshelper.refund_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","token is empty");

		//Call refund_transaction entering valid mihpayid, token and blank amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "");
		wshelper.refund_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","amount is empty");

		//Call refund_transaction entering invalid mihpayid, token and amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey,Helper.generateRandomAlphabetsString(7), Helper.generateRandomAlphabetsString(7));
		wshelper.refund_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Invalid PayU ID");

		//Call refund_transaction entering valid mihpayid, invalid token and amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "$^^^%", Helper.generateRandomAlphabetsString(7));
		wshelper.refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Invalid Refund Amount or Refund already Queued", "","105");

		//Call refund_transaction entering valid mihpayid, token and invalid amount
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), Helper.generateRandomAlphaNumericString(7),Helper.generateRandomAlphabetsString(7));
		wshelper.refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Invalid Refund Amount or Refund already Queued", "","105");

		//Call check_action_status entering blank request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, "");
		wshelper.check_action_status_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "Parameter missing");

		//Call check_action_status entering invalid request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, Helper.generateRandomAlphaNumericString(5));
		wshelper.get_payment_status_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "0 out of 1 Transactions Fetched Successfully", "");

		//Call update_requests entering blank mihpayid, request_id, bank_ref_num, amount, action and new_status
		actualWebServiceResponse = wshelper.update_request_executeAdminPanel(tranhelper.home, merchantKey, "", "","","", "", "");
		wshelper.update_request_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "amount is empty");

		//Call update_requests entering valid mihpayid, but blank request_id, bank_ref_num, amount, action and new_status
		actualWebServiceResponse = wshelper.update_request_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "","","", "", "");
		wshelper.update_request_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "amount is empty");

		//Call update_requests entering valid mihpayid, bank_ref_num, but blank request_id, amount, action and new_status
		actualWebServiceResponse = wshelper.update_request_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), testConfig.getRunTimeProperty("request_id"),"","", "", "");
		wshelper.update_request_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "amount is empty");

		//Call update_requests entering valid mihpayid, amount, bank_ref_num, but blank request_id, action and new_status
		actualWebServiceResponse = wshelper.update_request_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), testConfig.getRunTimeProperty("request_id"),Helper.generateRandomAlphaNumericString(6),"1", "", "");
		wshelper.update_request_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "action is not valid");

		//TODO Raised http://redmine.ibibo.com/issues/17585
		//Call update_requests entering valid mihpayid, amount, bank_ref_num, action, but blank  request_id, new_status
		//actualWebServiceResponse = wshelper.update_request_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), testConfig.getRunTimeProperty("request_id"),Helper.generateRandomAlphaNumericString(6),"1", "refund", "");
		//wshelper.update_request_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "bank_ref_no is empty");

		//Call update_requests entering invalid mihpayid, request_id, bank_ref_num, amount, action and new_status
		actualWebServiceResponse = wshelper.update_request_executeAdminPanel(tranhelper.home, merchantKey, "GNBJGJG", "GHTFGDGFD","HG!@#","HT56456", "G6tygh76", "G6tygh76");
		wshelper.update_request_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "action is not valid");

		//Call update_requests entering valid mihpayid, but invalid request_id, bank_ref_num, amount, action and new_status
		actualWebServiceResponse = wshelper.update_request_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "G6tygh76","G6tygh76","G6tygh76", "G6tygh76", "hjhjhj");
		wshelper.update_request_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "action is not valid");

		//Call update_requests entering valid mihpayid, bank_ref_num, but invalid request_id, amount, action and new_status
		actualWebServiceResponse = wshelper.update_request_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), testConfig.getRunTimeProperty("request_id"),"G6tygh76",Helper.generateRandomAlphaNumericString(6), Helper.generateRandomAlphaNumericString(6), Helper.generateRandomAlphabetsString(7));
		wshelper.update_request_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "action is not valid");

		//Call update_requests entering valid mihpayid, amount, bank_ref_num, but invalid request_id, action and new_status
		actualWebServiceResponse = wshelper.update_request_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), testConfig.getRunTimeProperty("request_id"),"1", Helper.generateRandomAlphaNumericString(6), Helper.generateRandomAlphaNumericString(6), Helper.generateRandomAlphabetsString(7));
		wshelper.update_request_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "action is not valid");

		//TODO Raised http://redmine.ibibo.com/issues/17586
		//Call update_requests entering valid mihpayid, bank_ref_num, amount, action and invalid  request_id, new_status
		//actualWebServiceResponse = wshelper.update_request_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), testConfig.getRunTimeProperty("request_id"),"1",Helper.generateRandomAlphaNumericString(6), "refund", Helper.generateRandomAlphaNumericString(6));
		//wshelper.update_request_verify(actualWebServiceResponse, expectedTransactionResponse, "0", "action is not valid");

		//Call cancel_transaction entering blank mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancel_transaction_executeAdminPanel(tranhelper.home, merchantKey,"", "1");
		wshelper.cancel_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","transaction not exists");

		//Call cancel_transaction entering valid mihpayid, blank token and amount
		actualWebServiceResponse = wshelper.cancel_transaction_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "", "1");
		wshelper.cancel_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Cancelled failed", "","111");

		//Call cancel_transaction entering valid mihpayid, token and blank amount
		actualWebServiceResponse = wshelper.cancel_transaction_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "");
		wshelper.cancel_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Cancelled failed", "","111");

		//Call cancel_transaction entering invalid mihpayid, token and amount
		actualWebServiceResponse = wshelper.cancel_transaction_executeAdminPanel(tranhelper.home, merchantKey,Helper.generateRandomAlphabetsString(7), Helper.generateRandomAlphabetsString(7));
		wshelper.cancel_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","transaction not exists");

		//Call cancel_transaction entering valid mihpayid, token and invalid amount
		actualWebServiceResponse = wshelper.cancel_transaction_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), Helper.generateRandomAlphaNumericString(7));
		wshelper.cancel_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Cancelled failed", "","111");

		//Call capture_transaction entering blank mihpayid and token
		actualWebServiceResponse = wshelper.capture_transaction_executeAdminPanel(tranhelper.home, merchantKey, "");
		wshelper.cancel_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","transaction not exists");

		//Call capture_transaction entering invalid mihpayid, and token
		actualWebServiceResponse = wshelper.capture_transaction_executeAdminPanel(tranhelper.home, merchantKey, "^%%HG");
		wshelper.cancel_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","transaction not exists");

		//Call cod_cancel entering valid mihpayid, token,amount and invalid merchantkey
		actualWebServiceResponse = wshelper.cod_cancel_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse.get("mihpayid"), Helper.generateRandomAlphabetsString(7),"3");
		wshelper.cod_cancel_verify(actualWebServiceResponse, expectedTransactionResponse, "0","FAILURE - Transaction Not Found", "","116");

		//Call cod_cancel entering blank mihpayid, token and amount
		actualWebServiceResponse = wshelper.cod_cancel_executeAdminPanel(tranhelper.home, merchantKey,"", "", "");
		wshelper.cod_cancel_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Invalid PayU ID");

		//Call cod_cancel entering valid mihpayid, blank token and amount
		actualWebServiceResponse = wshelper.cod_cancel_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "", "");
		wshelper.cod_cancel_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","token is empty");

		//Call cod_cancel entering valid mihpayid, token and blank amount
		actualWebServiceResponse = wshelper.cod_cancel_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"),Helper.generateRandomAlphaNumericString(7), "");
		wshelper.cod_cancel_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","amount is empty");

		//Call cod_cancel entering invalid mihpayid, token and amount
		actualWebServiceResponse = wshelper.cod_cancel_executeAdminPanel(tranhelper.home, merchantKey,Helper.generateRandomAlphaNumericString(7), Helper.generateRandomAlphaNumericString(7), Helper.generateRandomAlphabetsString(7));
		wshelper.cod_cancel_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Invalid PayU ID");

		//Call cod_cancel entering valid mihpayid, token and invalid amount
		actualWebServiceResponse = wshelper.cod_cancel_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "^%&%^&");
		wshelper.cod_cancel_verify(actualWebServiceResponse, expectedTransactionResponse, "0","FAILURE - Invalid amount", "","105");

		//Call cod_verify entering valid mihpayid, token,amount and invalid merchantkey
		actualWebServiceResponse = wshelper.cod_verify_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse.get("mihpayid"), Helper.generateRandomAlphabetsString(7),"3");
		wshelper.cod_cancel_verify(actualWebServiceResponse, expectedTransactionResponse, "0","FAILURE - Transaction Not Found", "","116");

		//Call cod_verify entering blank mihpayid, token and amount
		actualWebServiceResponse = wshelper.cod_verify_executeAdminPanel(tranhelper.home, merchantKey,"", "", "");
		wshelper.cod_verify_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Invalid PayU ID");

		//Call cod_verify entering valid mihpayid, blank token and amount
		actualWebServiceResponse = wshelper.cod_verify_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "", "");
		wshelper.cod_verify_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","token is empty");

		//Call cod_verify entering valid mihpayid, token and blank amount
		actualWebServiceResponse = wshelper.cod_verify_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"),Helper.generateRandomAlphaNumericString(7), "");
		wshelper.cod_verify_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","amount is empty");

		//Call cod_verify entering invalid mihpayid, token and amount
		actualWebServiceResponse = wshelper.cod_verify_executeAdminPanel(tranhelper.home, merchantKey,Helper.generateRandomAlphabetsString(7), Helper.generateRandomAlphaNumericString(7), Helper.generateRandomAlphabetsString(7));
		wshelper.cod_verify_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Invalid PayU ID");

		//Call cod_verify entering valid mihpayid, token and invalid amount
		actualWebServiceResponse = wshelper.cod_verify_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "^%&%^&");
		wshelper.cod_verify_verify(actualWebServiceResponse, expectedTransactionResponse, "0","FAILURE - Invalid amount", "","105");

		//Call cod_settled entering blank mihpayid, token and amount
		actualWebServiceResponse = wshelper.cod_settled_executeAdminPanel(tranhelper.home, merchantKey,"", "", "");
		wshelper.cod_settled_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Invalid PayU ID");

		//Call cod_verify entering valid mihpayid, token,amount and invalid merchantkey
		actualWebServiceResponse = wshelper.cod_settled_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse.get("mihpayid"), Helper.generateRandomAlphabetsString(7),"3");
		wshelper.cod_settled_verify(actualWebServiceResponse, expectedTransactionResponse, "0","FAILURE - Transaction Not Found", "","116");

		//Call cod_settled entering valid mihpayid, blank token and amount
		actualWebServiceResponse = wshelper.cod_settled_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "", "");
		wshelper.cod_settled_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","token is empty");

		//Call cod_settled entering valid mihpayid, token and blank amount
		actualWebServiceResponse = wshelper.cod_settled_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"),Helper.generateRandomAlphaNumericString(7), "");
		wshelper.cod_settled_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","amount is empty");

		//Call cod_settled entering invalid mihpayid, token and amount
		actualWebServiceResponse = wshelper.cod_settled_executeAdminPanel(tranhelper.home, merchantKey,Helper.generateRandomAlphaNumericString(7), Helper.generateRandomAlphaNumericString(7), Helper.generateRandomAlphabetsString(7));
		wshelper.cod_settled_error_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Invalid PayU ID");

		//Call cod_settled entering valid mihpayid, token and invalid amount
		actualWebServiceResponse = wshelper.cod_settled_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "^%&%^&");
		wshelper.cod_settled_verify(actualWebServiceResponse, expectedTransactionResponse, "0","FAILURE - Invalid amount", "","105");

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Test Race Condition for successful transaction with multiple partial refunds, where amount exceeds the range", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_RC_Success(Config testConfig)
	{

		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//Do a successful seamless transaction
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);	

		//Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);
		
		//Call partial refund_transaction using mihpayid, token and amount
		String actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued","", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "queued", "1.00", "-1.00");

		//Call partial refund_transaction using mihpayid, token and amount for race condition
		actualWebServiceResponse = wshelper.refund_transaction_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Invalid Refund Amount or Refund already Queued", "","105");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 80);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "1.00", "-1.00");

		Assert.assertTrue(testConfig.getTestResult());
	}

	//public void test_RC_Auth(Config testConfig) - moved to LiveCard TestCase - 19

	@Test(description = "Test Race Condition for successful captured transaction with multiple partial cancel/refund, where amount exceeds the range", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_RC_Success_CR(Config testConfig)
	{

		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//Do a successful captured seamless transaction
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);	

		//Call the db query to get the data from txn_update table
		tranhelper.verify_txn_update_data(testConfig);

		//Call partial refund_transaction using mihpayid, token and amount
		String actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "1","Refund Request Queued","", "102");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 80);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "1.00", "-1.00");

		//Call partial refund_transaction using mihpayid, token and amount for race condition
		actualWebServiceResponse = wshelper.cancelrefund_transaction_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), "1");
		wshelper.cancelrefund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, "0","Refund FAILURE - Invalid amount", "","105");

		//Call check_action_status using request_id
		actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		while ((actualWebServiceResponse.contains("queued"))||(actualWebServiceResponse.contains("pending")))	{
			Browser.wait(testConfig, 80);
			actualWebServiceResponse = wshelper.check_action_status_executeAdminPanel(tranhelper.home, merchantKey, testConfig.getRunTimeProperty("request_id"));
		}
		wshelper.check_action_status_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully","refund", "", "success", "1.00", "-1.00");

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Transaction's recon data and after TDR rule is hit with rule for Rewards.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_ReconData(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int TDRrow = 1;

		float t_amount = 50;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		Hashtable<String, String> expectedTransactionResponse =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 4, 1);

		//verify tdr deducted
		tranhelper.verify_tdr_calculation(testConfig,TDRrow);

		//check reconData in webservices
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Call check_payment using mihpayid
		String actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully", 
				map.get("mer_net_amount"), "", "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,testConfig.getRunTimeProperty("transactionId"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", "", "0000-00-00 00:00:00");

		//Call getReconData using mihpayid
		actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.getReconData_verify(actualWebServiceResponse, expectedTransactionResponse, map, "Pending");

		//Call getReconData using invalid mihpayid
		String invalidData = Helper.generateRandomAlphaNumericString(8);
		actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, merchantKey, invalidData);
		wshelper.getReconData_error_verify(actualWebServiceResponse, invalidData, expectedTransactionResponse, "STATUS : FAILURE    Transaction requested for not found");

		//Call getReconData using mihpayid with different merchant
		actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse.get("mihpayid"));
		wshelper.getReconData_error_verify(actualWebServiceResponse, invalidData, expectedTransactionResponse, "STATUS : FAILURE    Transaction requested for not found");

		//Call getReconData using blank mihpayid
		actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, merchantKey, "");
		wshelper.getReconData_error_verify(actualWebServiceResponse, invalidData, expectedTransactionResponse, "STATUS : FAILURE    ()");

		//Call updateSettlement using payuid which is still not reconciled
		String new_utr = Helper.generateRandomAlphaNumericString(5);
		actualWebServiceResponse = wshelper.updateSettlement_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), new_utr);
		wshelper.updateSettlement_error_verify(actualWebServiceResponse, invalidData, expectedTransactionResponse, "STATUS = FAILURE    Error updating merchant utr in the Database");

		//Call updateSettlement using invalid mihpayid
		actualWebServiceResponse = wshelper.updateSettlement_executeAdminPanel(tranhelper.home, merchantKey, "ISWAFKUK", new_utr);
		wshelper.updateSettlement_error_verify(actualWebServiceResponse, invalidData, expectedTransactionResponse, " STATUS = FAILURE    ()");

		//Call updateSettlement using mihpayid with different merchant
		actualWebServiceResponse = wshelper.updateSettlement_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse.get("mihpayid"), new_utr);
		wshelper.updateSettlement_error_verify(actualWebServiceResponse, invalidData, expectedTransactionResponse, "STATUS = FAILURE    Transaction requested for not found");

		//Call updateSettlement using blank mihpayid
		actualWebServiceResponse = wshelper.updateSettlement_executeAdminPanel(tranhelper.home, merchantKey, "   ", new_utr);
		wshelper.updateSettlement_error_verify(actualWebServiceResponse, invalidData, expectedTransactionResponse, " STATUS = FAILURE    ()");

		//Do recon
		String tdrReconFile = Helper.getExcelFile(testConfig, FileType.reconTDR1);

		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 1,
				tranhelper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 4,
				tranhelper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 7,
				tranhelper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 8,
				new_utr, 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 11,
				tranhelper.testResponse.actualResponse.get("mode"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 12,
				"rewards", 0);

		// Navigating to Admin Recon
		tranhelper.GetTestTransactionPage();
		tranhelper.home = tranhelper.testResponse.ClickHomeLink();
		manualUpdatePage = tranhelper.home.ClickManualTransactionUpdate();
		ReconPage reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, tdrReconFile);
		Browser.wait(testConfig, 2);

		//open and read the downloaded file
		String reconFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,reconFilePath);
		String recon_fileName = file.getName();

		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", reconFilePath + recon_fileName);

		String result = tr.GetCurrentEnvironmentData(1, "result");
		Browser.wait(testConfig, 1);

		//verify the result field in the downloaded file
		Helper.compareEquals(testConfig, "reconcile TDR Transaction properly", "SUCCESS - Transaction reconciled successfully" , result);

		int rewards_row = 12;
		testConfig.putRunTimeProperty("mihpayid", expectedTransactionResponse.get("mihpayid"));
		tranhelper.verify_tdr_calculation(testConfig,rewards_row);

		wshelper = new WebServicesHelper(testConfig);
		map = DataBase.executeSelectQuery(testConfig, 4, 1);

		//Call check_payment using mihpayid
		actualWebServiceResponse = wshelper.check_payment_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.check_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "Transaction Fetched Successfully", map.get("mer_net_amount"), new_utr, "0000-00-00 00:00:00");

		//Call verify_payment using txnid
		actualWebServiceResponse = wshelper.verify_payment_executeAdminPanel(tranhelper.home, merchantKey,expectedTransactionResponse.get("txnid"));
		wshelper.verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, "1", "1 out of 1 Transactions Fetched Successfully", new_utr, "0000-00-00 00:00:00");

		//Call getReconData using mihpayid
		actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.getReconData_verify(actualWebServiceResponse, expectedTransactionResponse, map, "Reconciled Successfully");

		//Call updateSettlement using payuid which is still not settled
		actualWebServiceResponse = wshelper.updateSettlement_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), new_utr);
		wshelper.updateSettlement_error_verify(actualWebServiceResponse, invalidData, expectedTransactionResponse, "STATUS = FAILURE    Error updating merchant utr in the Database");

		//Do Settlement
		String settlementFile = Helper.getExcelFile(testConfig, FileType.esscomSettlement);

		FileHandler.write_edit(settlementFile,"Sheet1", 1, 4, tranhelper.testResponse.actualResponse.get("mihpayid"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 6, tranhelper.testResponse.actualResponse.get("amount"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 7, tranhelper.GetTxnUpdateData()[0], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 8, tranhelper.GetTxnUpdateData()[1], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 9, tranhelper.GetTxnUpdateData()[2], 0);

		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 10, random_UTR, 0);

		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 11, "'"+current_date, 0);

		//Navigating to Merchant Settlement 
		tranhelper.GetTestTransactionPage();
		tranhelper.home = tranhelper.testResponse.ClickHomeLink();
		manualUpdatePage = tranhelper.home.ClickManualTransactionUpdate();

		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();
		merchantSettlement.esscomSettlementFileUpload(testConfig, settlementFile);

		//Check getReconData after settlement
		wshelper = new WebServicesHelper(testConfig);
		map = DataBase.executeSelectQuery(testConfig, 4, 1);

		//Call getReconData using mihpayid
		actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"));
		wshelper.getReconData_verify(actualWebServiceResponse, expectedTransactionResponse, map, "Reconciled Successfully");

		//Call updateSettlement for settled transaction
		new_utr = Helper.generateRandomAlphaNumericString(6);
		actualWebServiceResponse = wshelper.updateSettlement_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse.get("mihpayid"), new_utr);
		//wshelper.updateSettlement_verify(actualWebServiceResponse, expectedTransactionResponse, map, new_utr, "STATUS = Success");
		testConfig.logComment("http://redmine.ibibo.com/issues/27154");

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Transactions' recon data and after TDR rule is hit with rule for Rewards.", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_MutipleReconData(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int TDRrow = 1;

		float t_amount = 50;
		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		Hashtable<String, String> expectedTransactionResponse1 =  tranhelper.testResponse.actualResponse;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");
		Map<String, String> map1 = DataBase.executeSelectQuery(testConfig, 4, 1);

		//verify tdr deducted
		tranhelper.verify_tdr_calculation(testConfig,TDRrow);

		testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
		tranhelper.GetTestTransactionPage();
		tranhelper.testResponse = (TestResponsePage) tranhelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		Hashtable<String, String> expectedTransactionResponse2 =  tranhelper.testResponse.actualResponse;
		merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");
		Map<String, String> map2 = DataBase.executeSelectQuery(testConfig, 4, 1);

		//verify tdr deducted
		tranhelper.verify_tdr_calculation(testConfig,TDRrow);

		//check reconData in webservices
		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Pipe separated payuids
		String mihpayuids= expectedTransactionResponse1.get("mihpayid")+"|"+expectedTransactionResponse2.get("mihpayid");

		//Call getReconData using mihpayid
		String actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, merchantKey, mihpayuids);
		wshelper.multiple_getReconData_verify(actualWebServiceResponse, expectedTransactionResponse1, map1,map2, "Pending");

		//Call getReconData using invalid mihpayid
		String invalidData1 = Helper.generateRandomAlphabetsString(8);
		String invalidData2 = Helper.generateRandomAlphabetsString(8);
		actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, merchantKey, invalidData1+"|"+invalidData2);
		wshelper.getmultipleReconData_error_verify(actualWebServiceResponse, invalidData1, invalidData2, expectedTransactionResponse1, expectedTransactionResponse2, "STATUS : FAILURE    Transaction requested for not found");

		//Call getReconData using mihpayid with different merchant
		actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, "C0Dr8m", mihpayuids);
		wshelper.getmultipleReconData_error_verify(actualWebServiceResponse, invalidData1,invalidData2, expectedTransactionResponse1, expectedTransactionResponse2, "STATUS : FAILURE    Transaction requested for not found");

		//Call getReconData using blank mihpayid
		actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, merchantKey, " | ");
		wshelper.getmultipleReconData_error_verify(actualWebServiceResponse, invalidData1, invalidData2, expectedTransactionResponse1, expectedTransactionResponse2, "STATUS : FAILURE    ()");

		//Call updateSettlement using payuid which is still not reconciled
		String new_utr = Helper.generateRandomAlphaNumericString(5);
		actualWebServiceResponse = wshelper.multiple_updateSettlement_executeAdminPanel(tranhelper.home, merchantKey, expectedTransactionResponse1.get("mihpayid"), expectedTransactionResponse2.get("mihpayid"), new_utr);
		wshelper.multipleupdateSettlement_error_verify(actualWebServiceResponse, invalidData1, invalidData2, expectedTransactionResponse1, expectedTransactionResponse2,  "STATUS = FAILURE    Error updating merchant utr in the Database");

		//Call updateSettlement using invalid mihpayid	                                                                                                                   
		actualWebServiceResponse = wshelper.multiple_updateSettlement_executeAdminPanel(tranhelper.home, merchantKey, invalidData1, invalidData2, new_utr);
		wshelper.multipleupdateSettlement_error_verify(actualWebServiceResponse, invalidData1, invalidData2, expectedTransactionResponse1, expectedTransactionResponse2, "STATUS = FAILURE    ()");

		//Call updateSettlement using mihpayid with different merchant
		actualWebServiceResponse = wshelper.multiple_updateSettlement_executeAdminPanel(tranhelper.home, "C0Dr8m", expectedTransactionResponse1.get("mihpayid"), expectedTransactionResponse2.get("mihpayid"), new_utr);
		wshelper.multipleupdateSettlement_error_verify(actualWebServiceResponse, invalidData1, invalidData2, expectedTransactionResponse1, expectedTransactionResponse2, "STATUS = FAILURE    Transaction requested for not found");

		//Call updateSettlement using blank mihpayid
		actualWebServiceResponse = wshelper.multiple_updateSettlement_executeAdminPanel(tranhelper.home, merchantKey, "", "", new_utr);
		wshelper.multipleupdateSettlement_error_verify(actualWebServiceResponse, invalidData1, invalidData2, expectedTransactionResponse1, expectedTransactionResponse2, " STATUS = FAILURE    ()");

		//Do recon
		String tdrReconFile = Helper.getExcelFile(testConfig, FileType.GetReconData);	

		//For first transaction
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 1,
				expectedTransactionResponse1.get("mihpayid"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 2, "capture", 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 4,
				expectedTransactionResponse1.get("amount"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 7,
				expectedTransactionResponse1.get("amount"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 11,
				expectedTransactionResponse1.get("mode"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 1, 12,
				"rewards", 0);
		//For second transaction
		FileHandler.write_edit(tdrReconFile, "Sheet1", 2, 1,
				expectedTransactionResponse2.get("mihpayid"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 2, 2, "capture", 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 2, 4,
				expectedTransactionResponse2.get("amount"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 2, 7,
				expectedTransactionResponse2.get("amount"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 2, 11,
				expectedTransactionResponse2.get("mode"), 0);
		FileHandler.write_edit(tdrReconFile, "Sheet1", 2, 12,
				"rewards", 0);

		// Navigating to Admin Recon
		tranhelper.GetTestTransactionPage();
		tranhelper.home = tranhelper.testResponse.ClickHomeLink();
		manualUpdatePage = tranhelper.home.ClickManualTransactionUpdate();
		ReconPage reconPage = manualUpdatePage.ClickRecon();
		reconPage.uploadRecon(testConfig, tdrReconFile);
		Browser.wait(testConfig, 2);

		//open and read the downloaded file
		String reconFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,reconFilePath);
		String recon_fileName = file.getName();

		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", reconFilePath + recon_fileName);

		String result1 = tr.GetData(1, "result");
		String result2 = tr.GetData(2, "result");

		//verify the result field in the downloaded file
		Helper.compareEquals(testConfig, "reconcile TDR Transaction properly", "SUCCESS - Transaction reconciled successfully" , result1);
		Helper.compareEquals(testConfig, "reconcile TDR Transaction properly", "SUCCESS - Transaction reconciled successfully" , result2);

		int rewards_row = 12;
		testConfig.putRunTimeProperty("mihpayid", expectedTransactionResponse1.get("mihpayid"));
		map1 = DataBase.executeSelectQuery(testConfig, 4, 1);
		tranhelper.verify_tdr_calculation(testConfig,rewards_row);
		testConfig.putRunTimeProperty("mihpayid", expectedTransactionResponse2.get("mihpayid"));
		map2 = DataBase.executeSelectQuery(testConfig, 4, 1);
		tranhelper.verify_tdr_calculation(testConfig,rewards_row);

		//Verify getReconData
		wshelper = new WebServicesHelper(testConfig);		

		//Call getReconData using mihpayid
		actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, merchantKey, mihpayuids);
		wshelper.multiple_getReconData_verify(actualWebServiceResponse, expectedTransactionResponse1, map1, map2, "Reconciled Successfully");

		//Do Settlement
		String settlementFile = Helper.getExcelFile(testConfig, FileType.esscomSettlement);

		//First Transaction
		testConfig.putRunTimeProperty("mihpayid", expectedTransactionResponse1.get("mihpayid"));
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 4, expectedTransactionResponse1.get("mihpayid"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 6, expectedTransactionResponse1.get("amount"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 7, tranhelper.GetTxnUpdateData()[0], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 8, tranhelper.GetTxnUpdateData()[1], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 9, tranhelper.GetTxnUpdateData()[2], 0);

		//Second Transaction
		testConfig.putRunTimeProperty("mihpayid", expectedTransactionResponse1.get("mihpayid"));
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 4, expectedTransactionResponse2.get("mihpayid"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 6, expectedTransactionResponse2.get("amount"), 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 7, tranhelper.GetTxnUpdateData()[0], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 8, tranhelper.GetTxnUpdateData()[1], 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 9, tranhelper.GetTxnUpdateData()[2], 0);

		String random_UTR = Helper.generateRandomAlphaNumericString(6);
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 10, random_UTR, 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 10, random_UTR, 0);

		String current_date = Helper.getCurrentDate("yyyy-MM-dd");
		FileHandler.write_edit(settlementFile,"Sheet1", 1, 11, "'"+current_date, 0);
		FileHandler.write_edit(settlementFile,"Sheet1", 2, 11, "'"+current_date, 0);

		//Navigating to Merchant Settlement 
		tranhelper.GetTestTransactionPage();
		tranhelper.home = tranhelper.testResponse.ClickHomeLink();
		manualUpdatePage = tranhelper.home.ClickManualTransactionUpdate();

		merchantSettlement = manualUpdatePage.ClickMerchantSettlement();
		merchantSettlement.esscomSettlementFileUpload(testConfig, settlementFile);

		//Check getReconData after settlement
		wshelper = new WebServicesHelper(testConfig);
		testConfig.putRunTimeProperty("mihpayid", expectedTransactionResponse1.get("mihpayid"));
		map1 = DataBase.executeSelectQuery(testConfig, 4, 1);
		testConfig.putRunTimeProperty("mihpayid", expectedTransactionResponse2.get("mihpayid"));
		map2 = DataBase.executeSelectQuery(testConfig, 4, 1);

		//Call getReconData using mihpayid
		actualWebServiceResponse = wshelper.getReconData_executeAdminPanel(tranhelper.home, merchantKey, mihpayuids);
		wshelper.multiple_getReconData_verify(actualWebServiceResponse, expectedTransactionResponse1, map1, map2, "Reconciled Successfully");

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Test webservices - Create Invoice transaction via copying generated URL", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_CreateInvoiceTransaction(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//For doing a transaction via copying generated email
		int transactionRowNum = 1;
		int cardDetailsRowNum = 1;
		int createInvoiceDataRowNum = 1;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Call create_invoice for creating invoice with non-mandatory fields
		TestDataReader CreateInvoiceData = new TestDataReader(testConfig,"CreateInvoiceData");
		String actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call create_invoice for duplicate txnid 
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
		testConfig.putRunTimeProperty("txnid", actualResponse.get("Transaction Id"));
		createInvoiceDataRowNum = 26;
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		String transactionURL= testConfig.getRunTimeProperty("invoiceurl");
		Browser.navigateToURL(testConfig, transactionURL);

		NewResponsePage newResponse = tranhelper.makePaymentViaCreditCard(cardDetailsRowNum);
		//verify for success transaction
		newResponse.verifyPageText(true);

		//Do the transaction again with the same URL to check the expired offer message
		Browser.navigateToURL(testConfig, transactionURL);
		createInvoiceDataRowNum = 24;
		String message = CreateInvoiceData.GetData(createInvoiceDataRowNum, "result");
		VerifyExpiredTransaction(testConfig, message);

		//Call create_invoice for used txnid
		createInvoiceDataRowNum = 27;
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call create_invoice for creating invoice without non-mandotory fields 
		createInvoiceDataRowNum = 25;
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		transactionURL= testConfig.getRunTimeProperty("invoiceurl");
		Browser.navigateToURL(testConfig, transactionURL);

		newResponse = tranhelper.makePaymentViaCreditCard(cardDetailsRowNum);
		//verify for success transaction
		newResponse.verifyPageText(true);

		//Do the transaction again with the same URL to check the expired offer message
		Browser.navigateToURL(testConfig, transactionURL);
		createInvoiceDataRowNum = 24;
		message = CreateInvoiceData.GetData(createInvoiceDataRowNum, "result");
		VerifyExpiredTransaction(testConfig, message);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Test webservices - Create Invoice transaction via sent email", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_CreateInvoiceSendEmail(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//For doing a transaction through sent email with non-mandatory fields
		int transactionRowNum = 1;
		int cardDetailsRowNum = 1;
		int createInvoiceDataRowNum = 1;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);

		//Call create_invoice for creating invoice
		TestDataReader CreateInvoiceData = new TestDataReader(testConfig,"CreateInvoiceData");
		String actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call create_invoice for duplicate txnid 
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
		testConfig.putRunTimeProperty("txnid", actualResponse.get("Transaction Id"));
		createInvoiceDataRowNum = 26;
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		tranhelper.payment = gmailLoginAndClickPaymentLink(testConfig);
		//make payment via credit card
		NewResponsePage newResponse = tranhelper.makePaymentViaCreditCard(cardDetailsRowNum);
		newResponse.verifyPageText(true);

		//Call create_invoice for used txnid
		createInvoiceDataRowNum = 27;
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call create_invoice for creating invoice without non-mandotory fields
		createInvoiceDataRowNum = 25;
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		/*tranhelper.payment = gmailLoginAndClickPaymentLink(testConfig);
		//make payment via credit card
		newResponse = tranhelper.makePaymentViaCreditCard(cardDetailsRowNum);
		newResponse.verifyPageText(true);

		//Do the transaction again with the same URL to check the expired offer message
		gmailLoginAndClickPaymentLink(testConfig);
		createInvoiceDataRowNum = 23;
		message = CreateInvoiceData.GetData(createInvoiceDataRowNum, "result");
		VerifyExpiredTransaction(testConfig, message);*/

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Test webservices - Create Invoice Error Messages", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_CreateInvoiceErrorMessages(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//For doing a transaction via copying generated email
		int transactionRowNum = 1;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		TestDataReader CreateInvoiceData = new TestDataReader(testConfig,"CreateInvoiceData");

		//Call creat_invoice for invalid data in amount
		int createInvoiceDataRowNum = 2;	
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		String actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for blank data in amount
		createInvoiceDataRowNum = 3;
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for 0 amount
		createInvoiceDataRowNum = 4;
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for blank txnId
		createInvoiceDataRowNum = 5;	
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid data in productinfo
		createInvoiceDataRowNum = 6;
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for blank data in productinfo
		createInvoiceDataRowNum = 7;	
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid data in firstname
		createInvoiceDataRowNum = 8;
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for blank data in firstname
		createInvoiceDataRowNum = 9;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid data in email
		createInvoiceDataRowNum = 10;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for blank data in email
		createInvoiceDataRowNum = 11;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid data in phone
		createInvoiceDataRowNum = 12;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid phone with number > 10 digits
		createInvoiceDataRowNum = 13;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid phone with number < 7 digits
		createInvoiceDataRowNum = 14;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid data format in address
		createInvoiceDataRowNum = 15;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid data in city
		createInvoiceDataRowNum = 16;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid data in state
		createInvoiceDataRowNum = 17;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid data in country
		createInvoiceDataRowNum = 18;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid data in zipcode
		createInvoiceDataRowNum = 19;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid template_id
		createInvoiceDataRowNum = 20;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid template_id
		createInvoiceDataRowNum = 21;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid validation_period
		createInvoiceDataRowNum = 22;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		//Call creat_invoice for invalid data
		createInvoiceDataRowNum = 23;		
		System.out.println(CreateInvoiceData.GetData(createInvoiceDataRowNum, "Comment"));
		actualWebServiceResponse = wshelper.create_invoice_executeAdminPanel(tranhelper.home, merchantKey, CreateInvoiceData, createInvoiceDataRowNum);
		wshelper.create_invoice_error_verify(actualWebServiceResponse, CreateInvoiceData, createInvoiceDataRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	//@FindBy(xpath="//a[contains(@href, 'https://www.google.com/url?q=https%3A%2F%2Fsecure.payu.in%2FprocessInvoice%3FinvoiceId')]")
	//private WebElement clickOnLink;

	/**
	 * @return Payment Option Page for transaction via Sending Email in Create Invoice API
	 */
	public PaymentOptionsPage gmailLoginAndClickPaymentLink(Config testConfig) {
		GmailLogin gmailLogin = new GmailLogin(testConfig);
		GmailVerification gmailVerification = gmailLogin.Login("payu.testing", "payu@123");     
		//wait for email
		Browser.wait(testConfig, 20);
		gmailVerification.FilterEmail("Invoice #"+testConfig.getRunTimeProperty("txnid"), testConfig.getRunTimeProperty("txnid"));
		gmailVerification.verifyEmailText("Your order at AutomationMerchant1: Invoice #"+testConfig.getRunTimeProperty("txnid"));
		gmailVerification.SelectEmail();
		gmailVerification.verifyEmailContent(testConfig.getRunTimeProperty("txnid"));
		Browser.wait(testConfig, 5);
		String xpath = "//a[contains(@href, 'https://www.google.com/url?q=https%3A%2F%2Fsecure.payu.in%2FprocessInvoice%3FinvoiceId')]";
		Element.getPageElement(testConfig, How.xPath, xpath).click();
		Browser.switchToNewWindow(testConfig);

		return new PaymentOptionsPage(testConfig);

	}

	/**
	 * @param testConfig
	 */
	public void VerifyExpiredTransaction(Config testConfig, String Message) {
		String xPath="//div[@id='content']/div/div";
		Helper.compareEquals(testConfig, "Expired Transaction Message", Message, (Element.getPageElement(testConfig, How.xPath, xPath).getText()));
	}
	//public void test_RC_Auth_CR(Config testConfig) - moved to LiveCard TestCase - 20
	
	
	@Test(description = "Test webservices - getEmiAmountAccordingToInterest", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_WS_getEmiAmountAccordingToInterest(Config testConfig)
	{
		TransactionHelper tranhelper = new TransactionHelper(testConfig, true);
		tranhelper.DoLogin();

		//Do a seamless AXIS transaction
		int transactionRowNum = 1;
		String merchantKey = tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "key");
		testConfig.putRunTimeProperty("merchantid", tranhelper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "merchantid"));

		WebServicesHelper wshelper = new WebServicesHelper(testConfig);
		
		//Call getEmiAmountAccordingToInterest using merchantkey and amount
		String amount = "1000";
		String actualWebServiceResponse = wshelper.getEmiAmountAccordingToInterest_executeAdminPanel(tranhelper.home, merchantKey, amount);
		wshelper.getEmiAmountAccordingToInterest_verify(actualWebServiceResponse);
	
		Assert.assertTrue(testConfig.getTestResult());
	}
}
