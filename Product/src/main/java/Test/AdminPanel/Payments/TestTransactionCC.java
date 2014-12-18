package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;
import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.CCTab.CardType;
import PageObject.AdminPanel.Payments.PaymentOptions.ProcessingPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;

public class TestTransactionCC extends TestBase{

	DashboardPage dashBoard;


	@Test(description = "Verify a/b test division",  dataProvider="GetTestConfig", timeOut=600000, groups="1", invocationCount = 1)
	public void testPaymentOptionsABDivision(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		CCTab ccTab = (CCTab) helper.GetPaymentOptionsPage(1, PaymentMode.creditcard);

		String firstLabelByABTest = ccTab.getFirstLabelByABTest();

		String cookieValue = Browser.getCookieValue(testConfig, "abStat-_payment_options");

		switch(firstLabelByABTest)
		{
		case "Name on Card:":
			Helper.compareEquals(testConfig, "Name on card on top cookie", "_payment_options_0", cookieValue);
			break;
		case "Card Number:":
			Helper.compareEquals(testConfig, "Card number on top cookie", "_payment_options_1", cookieValue);
			break;
		}

		Assert.assertTrue(testConfig.getTestResult());	

	}

	@Test(description = "Verify a/b test division",  dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void testPaymentOptionsABDivisionSingleUser(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		CCTab ccTab = (CCTab) helper.GetPaymentOptionsPage(1, PaymentMode.creditcard);

		String firstLabelByABTest = ccTab.getFirstLabelByABTest();

		String cookieValue = Browser.getCookieValue(testConfig, "abStat-_payment_options");

		for (int i = 0; i <2 ; i++)
		{
			helper.home.navigateToAdminHome();
			ccTab = (CCTab) helper.GetPaymentOptionsPage(1, PaymentMode.creditcard);

			String firstLabelByABTest_nextTime = ccTab.getFirstLabelByABTest();

			String cookieValue_nextTime = Browser.getCookieValue(testConfig, "abStat-_payment_options");

			Helper.compareEquals(testConfig, "Field placement next time", firstLabelByABTest, firstLabelByABTest_nextTime);
			Helper.compareEquals(testConfig, "Cookie value next time", cookieValue, cookieValue_nextTime);
		}

		Assert.assertTrue(testConfig.getTestResult());	

	}

	@Test(description = "Verify a/b test division",  dataProvider="GetTestConfig", timeOut=60000, groups="1" , invocationCount = 1)
	public void test3DPopupABDivision(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		CCTab ccTab = (CCTab) helper.GetPaymentOptionsPage(1, PaymentMode.creditcard);

		//Read the cookie on payment options page, to be compared with processing page cookie
		String cookieValue = Browser.getCookieValue(testConfig, "abStat-new_threed_popup");

		ProcessingPage processingPage = (ProcessingPage)helper.DoPayment(3, 1, -1, ExpectedResponsePage.ProcessingPage);
		processingPage.verifyThreeDPopupcookie(cookieValue);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify correct additional charge is applied on transactions according to the ibibo code", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_CFIbiboCode(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying correct additional charge is applied DINR:2.00 when transaction is done through
		 DINERS via CITI gateway 
		 (Rule- CC:1.00,DINR:2.00,AMEX:3.00)
		 First preference is of Ibibo code then payment mode 
		 ****************
		 *****************/
		int transactionRowNum = 78;
		int paymentTypeRowNum = 9;
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
		 Verifying correct additional charge is applied AMEX:3.00 when transaction is done through
		 AMEX via AMEX gateway 
		 (Rule- CC:1.00,DINR:2.00,AMEX:3.00) 
		 First preference is of Ibibo code then payment mode
		 ****************
		 *****************/

		paymentTypeRowNum = 1;
		cardDetailsRowNum = 2;
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
		 DINERS via CITI gateway and no specified ibibocode rule is present 
		 (Rule- CC:1.00,AMEX:3.00) 
		 First preference is of Ibibo code then payment mode
		 ****************
		 *****************/

		paymentTypeRowNum = 9;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();

		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		testConfig.putRunTimeProperty("additionalCharges", "CC:1.00,AMEX:3.00");

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

	@Test(description = "Verify credit card transaction through AMEX gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_AMEX(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//AMEX
		int transactionRowNum = 2;
		int paymentTypeRowNum = 1;
		int cardDetailsRowNum = 2;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//AMEX successful transaction with payu_aggregator=0
		paymentTypeRowNum = 326;
		cardDetailsRowNum = 55;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.testResponse = (TestResponsePage)helper.bankRedirectPage.SubmitButtonForSuccessAMEX();
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//check dashboard with payment gateway as AMEX only
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		Helper.compareEquals(testConfig, "Payment Gateway", "AMEX", merchantTransaction.getPaymentGateway());
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		//AMEX successful transaction with payu_aggregator=1
		transactionRowNum = 64;
		paymentTypeRowNum = 326;
		cardDetailsRowNum = 55;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "");
		helper.bankRedirectPage = (BankRedirectPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.testResponse = (TestResponsePage)helper.bankRedirectPage.SubmitButtonForSuccessAMEX();
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//check dashboard with payment gateway as PAYU/AMEX only
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		Helper.compareEquals(testConfig, "Payment Gateway", "PAYU / AMEX", merchantTransaction.getPaymentGateway());
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify failed credit card transaction through AMEX gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_AMEXFailed(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//AMEX failed transaction with payu_aggregator=0
		int transactionRowNum = 2;
		int paymentTypeRowNum = 327;
		int cardDetailsRowNum = 55;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		helper.testResponse = (TestResponsePage)helper.bankRedirectPage.SubmitButtonForFailedAMEX();
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//check dashboard with payment gateway as AMEX only
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		Helper.compareEquals(testConfig, "Payment Gateway", "AMEX", merchantTransaction.getPaymentGateway());
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		//AMEX successful transaction with payu_aggregator=1
		transactionRowNum = 64;
		paymentTypeRowNum = 327;
		cardDetailsRowNum = 55;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("additionalCharges", "");
		helper.bankRedirectPage = (BankRedirectPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.testResponse = (TestResponsePage)helper.bankRedirectPage.SubmitButtonForFailedAMEX();
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//check dashboard with payment gateway as PAYU/AMEX only
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		Helper.compareEquals(testConfig, "Payment Gateway", "PAYU / AMEX", merchantTransaction.getPaymentGateway());
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify AMEX  card transaction through AMEX gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_AMEX_CF(Config testConfig)
	{	

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Credit card
		int transactionRowNum = 69;
		int paymentTypeRowNum = 1;
		int cardDetailsRowNum = 2;
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
		transactionamount = transactionamount+additionalCharges;		
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Verify on Dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	

	}

	@Test(description = "Verify credit card transaction through CBK gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_CBK(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//DINR
		int transactionRowNum = 23;
		int paymentTypeRowNum = 8;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//AMEX
		transactionRowNum = 1;
		paymentTypeRowNum = 2;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify credit card transaction through CBK gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_CBK_CF(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//AMEX
		int transactionRowNum = 68;
		int paymentTypeRowNum = 2;
		int cardDetailsRowNum = -1;
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
		transactionamount = transactionamount+additionalCharges;			
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");	

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}


	@Test(description = "Verify credit card transaction with preinit mode = 1", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_preInitMode_One(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("preInitMode", "1");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify credit card transaction with preinit mode = 0", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_preInitMode_Zero(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CC
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("preInitMode", "0");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}


	@Test(description = "Verify credit card transaction through AXIS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_AXIS(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CC
		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify credit card transaction through AXIS gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_AXIS_CF(Config testConfig)
	{	

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Credit card
		int transactionRowNum = 68;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
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
		transactionamount = transactionamount+additionalCharges;		
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Verify on Dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());	

	}

	@Test(description = "Verify credit card transaction through CITI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="2")
	public void test_CC_CITI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CC - moved to LiveCard TestCases - 6
		//DINR
		int transactionRowNum = 2;
		int paymentTypeRowNum = 9;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify credit card transaction through HDFC2 gateway with additional charge", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_HDFC2(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CC
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//DINR
		transactionRowNum = 3;
		paymentTypeRowNum = 227;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify credit card and diner transaction through HDFC gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_HDFC_CF(Config testConfig)
	{	

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Credit card
		int transactionRowNum = 70;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
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
		transactionamount = transactionamount+additionalCharges;			
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Verify on Dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		//DINR
		transactionRowNum = 70;
		paymentTypeRowNum = 227;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
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
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//Verify on Dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());		

	}

	//public void test_CC_ICICITRAVEL(Config testConfig) - moved to LiveCard TestCase 15
	//	public void test_CC_SBIPG(Config testConfig) - moved to LiveCard TestCase 16

	/**
	 * @author jyoti.patial
	 * Test case for Verifying if Label fields are present on the credit card payment page 
	 */
	@Test(description = "Test if all label fields for credit card are present on the payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CreditCardTabPageElements(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		CCTab ccTab = (CCTab) helper.GetPaymentOptionsPage(1, PaymentMode.creditcard);

		String expected = helper.cardDetailsData.GetData(1, "Credit");
		Helper.compareEquals(testConfig, "Credit Card Tab text", expected, ccTab.getCCTabText());

		ccTab.VerifyCardTypeImages();

		expected = helper.cardDetailsData.GetData(10, "Credit");
		Helper.compareEquals(testConfig, "Card Type label", expected, ccTab.getCardTypeLabel());

		expected = helper.cardDetailsData.GetData(10, "CC");
		Helper.compareEquals(testConfig, "Name on Card label", expected, ccTab.getCardNameLabel());

		expected = helper.cardDetailsData.GetData(10, "Name");
		Helper.compareEquals(testConfig, "Card Number label", expected, ccTab.getCardNumberLabel());

		expected = helper.cardDetailsData.GetData(10, "CVV");
		Helper.compareEquals(testConfig, "CVV Number label", expected, ccTab.getCVVNumberLabel());

		expected = helper.cardDetailsData.GetData(10, "Mon");
		Helper.compareEquals(testConfig, "Expiry Date label", expected, ccTab.getExpiryDateLabel());

		expected = helper.cardDetailsData.GetData(10, "Note");
		Helper.compareEquals(testConfig, "Note text", expected, ccTab.getNoteText());

		ccTab.clickWhatIsCVVLink();

		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * @author jyoti.patial
	 * Verify the error messages when invalid inputs are entered in Credit Card Tab
	 */
	@Test(description = "Verify the error messages when invalid inputs are entered in Credit Card Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnCreditCardTab(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 2;
		helper.ccTab = (CCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.creditcard);

		//Special char input 
		verifyErrors(helper, 4, 
			helper.cardDetailsData.GetData(11, "Name"), helper.cardDetailsData.GetData(11, "CC")
				, helper.cardDetailsData.GetData(11, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));

		//Input with spaces
	verifyErrors(helper, 5, 
		helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(14, "CC")
			, helper.cardDetailsData.GetData(14, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));

		//Spaces Only
		verifyErrors(helper, 6, 
				helper.cardDetailsData.GetData(11, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(13, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));

		//Blank Input
		verifyErrors(helper, 7, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(13, "CVV"), helper.cardDetailsData.GetData(11, "Year"));

		//Wrong details
		verifyErrors(helper, 8, 
				helper.cardDetailsData.GetData(12, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(14, "CVV"), helper.cardDetailsData.GetData(12, "Year"));

		//Alphabet card details
		verifyErrors(helper, 9, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(11, "CVV"), helper.cardDetailsData.GetData(14, "Mon"));

		//Amex card
		helper.ccTab.chooseCardType(CardType.Amex);
		verifyErrors(helper, 1, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(14, "CC")
			, helper.cardDetailsData.GetData(11, "CVV"), helper.cardDetailsData.GetData(9, "Mon"));

		Assert.assertTrue(testConfig.getTestResult());
	}

	private void verifyErrors(TransactionHelper helper, int inputCardDetailsRowNum, String cardNameError, String cardNumberError, String cvvError, String expiryError)
	{
		String comments = helper.cardDetailsData.GetData(inputCardDetailsRowNum, "Comments");
		helper.ccTab.FillCardDetails(inputCardDetailsRowNum);
		helper.payment.clickPayNowToGetError();

		Helper.compareEquals(helper.testConfig, "Card name '"+ comments +"' error", cardNameError, helper.ccTab.getCardNameErrorMessage());

		Helper.compareEquals(helper.testConfig, "Card number '"+ comments +"' error", cardNumberError, helper.ccTab.getCardNumberErrorMessage());

		Helper.compareEquals(helper.testConfig, "CVV number '"+ comments +"' error", cvvError, helper.ccTab.getCvvNumberErrorMessage());

		Helper.compareEquals(helper.testConfig, "Expiry Date '"+ comments +"' error", expiryError, helper.ccTab.getExpiryDateErrorMessage());
	}
	@Test(description = "Verify post params issuing bank and card type in credit card transaction for merchnat where enable_iss_bank_card_type is on", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_HDFC_IssBank(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CC
		int transactionRowNum = 4;
		int paymentTypeRowNum = 332;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("cardType", "1");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);


		Assert.assertTrue(testConfig.getTestResult());	

	}

	@Test(description = "Verify post params issuing bank and card type in credit card transaction for merchnat where enable_iss_bank_card_type is on", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_AMEX_IssBank(Config testConfig)
	{	

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();


		//AMEX
		int transactionRow = 4;
		int paymentTypeRow = 335;
		int cardDetailsRow = 16;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("cardType", "1");
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRow, helper.paymentTypeData, paymentTypeRow);

		Assert.assertTrue(testConfig.getTestResult());	
	}
}