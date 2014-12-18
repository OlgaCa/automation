package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

import PageObject.AdminPanel.Payments.PaymentOptions.DCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.ProcessingPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.WebServices.WebServicesHelper;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;

public class TestTransactionDC extends TestBase{

	DashboardPage dashBoard;

	@Test(description = "Verify correct additional charge is applied on transactions according to the ibibo code", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_CFIbiboCode(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying correct additional charge is applied MAST:2.00 when transaction is done through
		 MASTERS card via HDFC2 gateway 
		 (Rule- DC:1.00,MAST:2.00,VISA:3.00)
		 First preference is of Ibibo code then payment mode 
		 ****************
		 *****************/
		int transactionRowNum = 79;
		int paymentTypeRowNum = 19;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();

		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		/****************
		 ****************
		 Verifying correct additional charge is applied VISA:3.00 when transaction is done through
		 VISA card via HDFC2 gateway 
		 (Rule- DC:1.00,MAST:2.00,VISA:3.00) 
		 First preference is of Ibibo code then payment mode
		 ****************
		 *****************/

		paymentTypeRowNum = 37;
		helper.GetTestTransactionPage();

		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		addCharge = keyValue[3].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		/****************
		 ****************
		 Verifying correct additional charge is applied CC:1.00 when transaction is done through
		 MASTER card via HDFC2 gateway and no specified ibibocode rule is present 
		 (Rule- DC:1.00,VISA:2.00) 
		 First preference is of Ibibo code then payment mode
		 ****************
		 *****************/

		paymentTypeRowNum = 19;
		helper.GetTestTransactionPage();

		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		testConfig.putRunTimeProperty("additionalCharges", "DC:1.00,VISA:2.00");

		keyvalue = testConfig.getRunTimeProperty("additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[1];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		Assert.assertTrue(testConfig.getTestResult());	
	}


	@Test(description = "Verify debit card transaction through CBK gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_CBK(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//BOI Debit Card
		int transactionRowNum = 1;
		int paymentTypeRowNum = 10;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//We can test it with one bank only, so commenting code for rest of banks -- ATUL
		/*	//IOB Debit Card
		transactionRowNum = 1;
		paymentTypeRowNum = 15;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//Laxmi Vilas Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 16;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//SBI Debit Card
		transactionRowNum = 1;
		paymentTypeRowNum = 28;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//UBI Debit Card
		transactionRowNum = 1;
		paymentTypeRowNum = 33;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
		 */
		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify debit card transaction through CCAVENUE", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_CCAVENUE(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Canara Bank Debit Card
		int transactionRowNum = 2;
		int paymentTypeRowNum = 11;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//We can test it with one bank only, so commenting code for rest of banks -- ATUL
		/*	//Indian Overseas Bank Debit Card
		transactionRowNum = 2;
		paymentTypeRowNum = 14;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//Punjab National Bank Debit Card
		transactionRowNum = 2;
		paymentTypeRowNum = 27;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//SBI Maestro Card
		transactionRowNum = 2;
		paymentTypeRowNum = 29;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//State Bank of India Debit Card
		transactionRowNum = 2;
		paymentTypeRowNum = 32;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//Union Bank of India Debit Card
		transactionRowNum = 2;
		paymentTypeRowNum = 34;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
		 */
		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify debit card transaction through CITI" , 
			dataProvider="GetTestConfig", timeOut=600000, groups="2")
	public void test_DC_CITI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CITI Debit Card - moved to LiveCard TestCase - 17

		//MasterCard Debit Cards
		int transactionRowNum = 2;
		int paymentTypeRowNum = 18;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Visa Debit Cards
		transactionRowNum = 2;
		paymentTypeRowNum = 36;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify visa and master debit card transactions through CITI gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_CITI_CF(Config testConfig)
	{	

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 69;
		int paymentTypeRowNum = 18;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		//Visa Debit Cards
		transactionRowNum = 69;
		paymentTypeRowNum = 36;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//view in dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());		

	}

	@Test(description = "Verify debit card transaction through SBIPG", 
			dataProvider="GetTestConfig", timeOut=600000, groups="2")
	public void test_DC_SBIPG(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CITI Debit Card
		int transactionRowNum = 5;
		int paymentTypeRowNum = 13;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//We can test it with one bank only, so commenting code for rest of banks -- ATUL
		/* //MasterCard Debit Cards
		transactionRowNum = 5;
		paymentTypeRowNum = 21;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Other Maestro Card (with only expiry)
		transactionRowNum = 5;
		paymentTypeRowNum = 25;
		cardDetailsRowNum = 15;
		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.testResponse = (TestResponsePage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.TestResponsePage);
		//since our test card is not a maestro card, so it will not be redirected to bank site for password
		helper.testResponse.overrideExpectedTransactionData = true;
		testConfig.putRunTimeProperty("error", "E712");
		testConfig.putRunTimeProperty("field6", "Required parameter missing");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Other Maestro Card (without cvv and expiry)
		transactionRowNum = 5;
		paymentTypeRowNum = 25;
		cardDetailsRowNum = 3;
		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.SelectDebitCard("Other Maestro Cards");
		helper.dcTab.verifyOtherMaestroLinks();
		helper.dcTab.DisableCvvExp();
		helper.testResponse = (TestResponsePage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.TestResponsePage);
		//since our test card is not a maestro card, so it will not be redirected to bank site for password
		helper.testResponse.overrideExpectedTransactionData = true;
		testConfig.putRunTimeProperty("error", "E712");
		testConfig.putRunTimeProperty("field6", "Required parameter missing");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//SBI Maestro Card (without cvv and expiry)
		transactionRowNum = 5;
		paymentTypeRowNum = 31;
		cardDetailsRowNum = 3;
		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.testResponse = (TestResponsePage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.TestResponsePage);
		//since our test card is not a maestro card, so it will not be redirected to bank site for password
		helper.testResponse.overrideExpectedTransactionData = true;
		testConfig.putRunTimeProperty("error", "E712");
		testConfig.putRunTimeProperty("field6", "Required parameter missing");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//SBI Maestro Card (with only expiry)
		transactionRowNum = 5;
		paymentTypeRowNum = 31;
		cardDetailsRowNum = 15;
		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.SelectDebitCard("SBI Maestro Card");
		helper.dcTab.verifySBIMaestroLinks();
		helper.dcTab.EnableCvvExpSBI();
		helper.testResponse = (TestResponsePage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.TestResponsePage);
		//since our test card is not a maestro card, so it will not be redirected to bank site for password
		helper.testResponse.overrideExpectedTransactionData = true;
		testConfig.putRunTimeProperty("error", "E712");
		testConfig.putRunTimeProperty("field6", "Required parameter missing");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Visa Debit Cards - moved to LiveCard TestCase - 18
		 */
		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify debit card transaction through SBIPG with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="2")
	public void test_DC_SBIPG_CF(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CITI Debit Card
		int transactionRowNum = 72;
		int paymentTypeRowNum = 13;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//view in dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		//We can test it with one bank only, so commenting code for rest of banks -- ATUL
		/* //MasterCard Debit Cards
		transactionRowNum = 72;
		paymentTypeRowNum = 21;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//view in dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		//Other Maestro Card (with only expiry)
		transactionRowNum = 72;
		paymentTypeRowNum = 25;
		cardDetailsRowNum = 15;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.testResponse = (TestResponsePage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.TestResponsePage);
		//since our test card is not a maestro card, so it will not be redirected to bank site for password
		helper.testResponse.overrideExpectedTransactionData = true;
		testConfig.putRunTimeProperty("error", "E712");
		testConfig.putRunTimeProperty("field6", "Required parameter missing");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Other Maestro Card (without cvv and expiry)
		transactionRowNum = 72;
		paymentTypeRowNum = 25;
		cardDetailsRowNum = 3;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.SelectDebitCard("Other Maestro Cards");
		helper.dcTab.verifyOtherMaestroLinks();
		helper.dcTab.DisableCvvExp();
		helper.testResponse = (TestResponsePage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.TestResponsePage);
		//since our test card is not a maestro card, so it will not be redirected to bank site for password
		helper.testResponse.overrideExpectedTransactionData = true;
		testConfig.putRunTimeProperty("error", "E712");
		testConfig.putRunTimeProperty("field6", "Required parameter missing");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//SBI Maestro Card (without cvv and expiry)
		transactionRowNum = 72;
		paymentTypeRowNum = 31;
		cardDetailsRowNum = 3;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.testResponse = (TestResponsePage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.TestResponsePage);
		//since our test card is not a maestro card, so it will not be redirected to bank site for password
		helper.testResponse.overrideExpectedTransactionData = true;
		testConfig.putRunTimeProperty("error", "E712");
		testConfig.putRunTimeProperty("field6", "Required parameter missing");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//SBI Maestro Card (with only expiry)
		transactionRowNum = 72;
		paymentTypeRowNum = 31;
		cardDetailsRowNum = 15;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.SelectDebitCard("SBI Maestro Card");
		helper.dcTab.verifySBIMaestroLinks();
		helper.dcTab.EnableCvvExpSBI();
		helper.testResponse = (TestResponsePage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.TestResponsePage);
		//since our test card is not a maestro card, so it will not be redirected to bank site for password
		helper.testResponse.overrideExpectedTransactionData = true;
		testConfig.putRunTimeProperty("error", "E712");
		testConfig.putRunTimeProperty("field6", "Required parameter missing");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Visa Debit Cards - moved to LiveCard TestCase - 18
		 */
		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify debit card transaction through AXIS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_AXIS(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 1;
		int paymentTypeRowNum = 17;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		DCTab dcTab = (DCTab) helper.GetPaymentOptionsPage(1, PaymentMode.debitcard);
		//dcTab.verifyDisabledPaymentTypes("Other Maestro Cards", "AXIS");

		//Visa Debit Cards
		transactionRowNum = 1;
		paymentTypeRowNum = 35;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify visa and master debit card transactions through AXIS gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_AXIS_CF(Config testConfig)
	{	

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 68;
		int paymentTypeRowNum = 17;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//view in dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		//Visa Debit Cards
		transactionRowNum = 68;
		paymentTypeRowNum = 35;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//view in dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());		

	}


	@Test(description = "Verify debit card transaction through HDFC2 gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_HDFC2(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 3;
		int paymentTypeRowNum = 19;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Visa Debit Cards
		transactionRowNum = 3;
		paymentTypeRowNum = 37;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		DCTab dcTab = (DCTab) helper.GetPaymentOptionsPage(2, PaymentMode.debitcard);
		//dcTab.verifyDisabledPaymentTypes("Other Maestro Cards", "HDFC2");

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify visa and master debit card transactions through HDFC2 gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_HDFC2_CF(Config testConfig)
	{	

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 70;
		int paymentTypeRowNum = 19;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//view in dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		//Visa Debit Cards
		transactionRowNum = 70;
		paymentTypeRowNum = 37;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//view in dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());		

	}

	@Test(description = "Verify debit card transaction through ICICITRAVEL gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_ICICITRAVEL(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 6;
		int paymentTypeRowNum = 340;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify debit card transactions through ICICITRAVEL gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_ICICITRAVEL_CF(Config testConfig)
	{	

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 73;
		int paymentTypeRowNum = 340;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//view in dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		//We can test it with one bank only, so commenting code for rest of banks -- ATUL
		/* //Visa Debit Cards
		transactionRowNum = 73;
		paymentTypeRowNum = 38;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//view in dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);
		 */
		Assert.assertTrue(testConfig.getTestResult());		

	}

	@Test(description = "Verify debit card transaction through TECHPROCESS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_TECHPROCESS(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Other Maestro Card (with only expiry)
		int transactionRowNum = 7;
		int paymentTypeRowNum = 26;
		int cardDetailsRowNum = 62;
		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.bankRedirectPage = (BankRedirectPage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

	/*	//Other Maestro Card (without cvv and expiry)
		transactionRowNum = 7;
		paymentTypeRowNum = 26;
		cardDetailsRowNum = 3;
		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.SelectDebitCard("Other Maestro Cards");
		helper.dcTab.verifyOtherMaestroLinks();
		helper.dcTab.DisableCvvExp();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
*/
		//TODO - getting invalid referer url on hotfix
		if(testConfig.getRunTimeProperty("Environment").toLowerCase().contains("hotfix"))
			Assert.assertTrue(false,"TODO - check refrer url invalid error on hotfix");

		Assert.assertTrue(testConfig.getTestResult());	
	}


	@Test(description = "Verify debit card transaction redirecting to bank page with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_CF(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//BOI Debit Card through CBK
		int transactionRowNum = 68;
		int paymentTypeRowNum = 10;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//Canara Bank Debit Card through CCAVENUE
		transactionRowNum = 69;
		paymentTypeRowNum = 11;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//Other Maestro Card (with cvv and expiry) through TECHPROCESS
		transactionRowNum = 74;
		paymentTypeRowNum = 26;
		cardDetailsRowNum = 63;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.bankRedirectPage = (BankRedirectPage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

	/*	//Other Maestro Card (without cvv and expiry) through TECHPROCESS
		transactionRowNum = 74;
		paymentTypeRowNum = 26;
		cardDetailsRowNum = 62;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[2];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.SelectDebitCard("Other Maestro Cards");
		helper.dcTab.verifyOtherMaestroLinks();
		helper.dcTab.DisableCvvExp();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoPayment(paymentTypeRowNum, cardDetailsRowNum, -1, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
*/
		Assert.assertTrue(testConfig.getTestResult());	
	}

	/**
	 * @author jyoti.patial
	 * Test case for Verifying if Label fields are present on the debit card payment page 
	 */
	@Test(description = "Test if all label fields for debit card are present on the payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DebitCardTabPageElements(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		DCTab dcTab = (DCTab) helper.GetPaymentOptionsPage(1, PaymentMode.debitcard);

		//dcTab.verifyDisabledPaymentTypes("Other Maestro Cards", "AXIS");

		Element.verifyElementNotPresent(testConfig, dcTab.getRedirectElement(), "Redirection Note when no bank selected");
		dcTab.SelectDebitCard("Visa Debit Cards (All Banks)");

		Helper.compareEquals(testConfig, "Debit Card Redirect text", helper.cardDetailsData.GetData(10, "Note"), dcTab.getRedirectText());

		String expected = helper.cardDetailsData.GetData(1, "Debit");
		Helper.compareEquals(testConfig, "Debit Card Tab text", expected, dcTab.getDCTabText());

		expected = helper.cardDetailsData.GetData(10, "Debit");
		Helper.compareEquals(testConfig, "Card Type label", expected, dcTab.getDebitCardTypeLabel());

		expected = helper.cardDetailsData.GetData(10, "Name");
		Helper.compareEquals(testConfig, "Name on Card label", expected, dcTab.getCardNameLabel());

		expected = helper.cardDetailsData.GetData(10, "CC");
		Helper.compareEquals(testConfig, "Card Number label", expected, dcTab.getCardNumberLabel());

		expected = helper.cardDetailsData.GetData(10, "CVV");
		Helper.compareEquals(testConfig, "CVV Number label", expected, dcTab.getCVVNumberLabel());

		expected = helper.cardDetailsData.GetData(10, "Mon");
		Helper.compareEquals(testConfig, "Expiry Date label", expected, dcTab.getExpiryDateLabel());

		expected = helper.cardDetailsData.GetData(10, "Note");
		Helper.compareEquals(testConfig, "Note text", expected, dcTab.getNoteText());

		Helper.compareTrue(testConfig, "Card Image for what is CVV link", dcTab.isCardImageAppearing());

		Assert.assertTrue(testConfig.getTestResult());

	}

	/**
	 * @author jyoti.patial
	 * Verify the error messages when invalid inputs are entered in Debit Card Tab
	 */
	@Test(description = "Verify the error messages when invalid inputs are entered in Debit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnDebitCardTab(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 1;
		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.SelectDebitCard("Visa Debit Cards (All Banks)");

		//Special char input 
		verifyErrors(helper, 4, 
				helper.cardDetailsData.GetData(11, "Name"), helper.cardDetailsData.GetData(12, "CC")
				, helper.cardDetailsData.GetData(11, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));

		//Input with spaces
		verifyErrors(helper, 5, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(14, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));

		//Spaces Only
		verifyErrors(helper, 6, 
				helper.cardDetailsData.GetData(11, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(13, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));

		//Wrong details
		verifyErrors(helper, 8, 
				helper.cardDetailsData.GetData(12, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(14, "CVV"), helper.cardDetailsData.GetData(12, "Year"));

		//Alphabet card details
		verifyErrors(helper, 9, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(11, "CVV"), helper.cardDetailsData.GetData(14, "Mon"));

		//Blank Input
		verifyErrors(helper, 7, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(13, "CVV"), helper.cardDetailsData.GetData(11, "Year"));

		//Check for Maestro only Expiry is mandatory (CVV is optional)
		transactionRowNum = 6;
		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.SelectDebitCard("State Bank Maestro Cards");
		helper.dcTab.EnableCvvExpSBI();
		//No CVV Expiry card details
		verifyErrors(helper, 3, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(13, "CVV"), helper.cardDetailsData.GetData(11, "Year"));

		transactionRowNum = 6;
		helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		helper.dcTab.SelectDebitCard("Other Maestro Cards");
		//No CVV Expiry card details
		verifyErrors(helper, 3, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(13, "CVV"), helper.cardDetailsData.GetData(11, "Year"));

		Assert.assertTrue(testConfig.getTestResult());
	}

	private void verifyErrors(TransactionHelper helper, int inputCardDetailsRowNum, String cardNameError, String cardNumberError, String cvvError, String expiryError)
	{
		String comments = helper.cardDetailsData.GetData(inputCardDetailsRowNum, "Comments");
		helper.dcTab.FillCardDetails(inputCardDetailsRowNum);
		helper.payment.clickPayNowToGetError();

		Helper.compareEquals(helper.testConfig, "Card name '"+ comments +"' error", cardNameError, helper.dcTab.getCardNameErrorMessage());

		Helper.compareEquals(helper.testConfig, "Card number '"+ comments +"' error", cardNumberError, helper.dcTab.getCardNumberErrorMessage());

		Helper.compareEquals(helper.testConfig, "CVV number '"+ comments +"' error", cvvError, helper.dcTab.getCvvNumberErrorMessage());

		Helper.compareEquals(helper.testConfig, "Expiry Date '"+ comments +"' error", expiryError, helper.dcTab.getExpiryDateErrorMessage());
	}

	@Test(description = "Verify Post params Issuing bank and card type in debit card transaction through HDFC2 gateway for merchnat where enable_iss_bank_card_type is on", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DC_HDFC_IssBank(Config testConfig)
	{

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//DC transaction 
		int transactionRowNum = 4;
		int paymentTypeRowNum = 333;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("cardType", "1");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);


		//Visa Debit Cards
		transactionRowNum = 4;
		paymentTypeRowNum = 334;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("cardType", "1");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}
}