package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.EMITab;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.Element;
import Utils.TestDataReader;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestBase;

/**
 * @author prashant.singh
 *
 */

public class TestTransactionEMI extends TestBase{

	DashboardPage dashBoard;

	//HDFC2 has been removed for emi from DB
	/*@Test(description = "Verify URL for emi banking transaction for HDFC2 gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_HDFC2(Config testConfig)
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//12 months
		int transactionRowNum = 3;
		int paymentTypeRowNum = 40;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//3 months
		transactionRowNum = 3;
		paymentTypeRowNum = 43;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//6 months
		transactionRowNum = 3;
		paymentTypeRowNum = 46;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//9 months
		transactionRowNum = 3;
		paymentTypeRowNum = 48;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());				
	}

	@Test(description = "Verify emi banking transaction for HDFC2 gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_HDFC2_CF(Config testConfig)
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//12 months
		int transactionRowNum = 70;
		int paymentTypeRowNum = 40;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

		//3 months
		transactionRowNum = 70;
		paymentTypeRowNum = 43;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

		//6 months
		transactionRowNum = 70;
		paymentTypeRowNum = 46;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

		//9 months
		transactionRowNum = 70;
		paymentTypeRowNum = 48;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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
	}*/

	@Test(description = "Verify URL for emi banking transaction for AXIS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_AXIS(Config testConfig)
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//12 months
		int transactionRowNum = 1;
		int paymentTypeRowNum = 260;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//3 months
		transactionRowNum = 1;
		paymentTypeRowNum = 257;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//6 months
		transactionRowNum = 1;
		paymentTypeRowNum = 258;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//9 months
		transactionRowNum = 1;
		paymentTypeRowNum = 259;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());				
	}

	@Test(description = "Verify emi banking transaction for Axis gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_AXIS_CF(Config testConfig)
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//12 months
		int transactionRowNum = 68;
		int paymentTypeRowNum = 260;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

		//3 months
		transactionRowNum = 68;
		paymentTypeRowNum = 257;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

		//6 months
		transactionRowNum = 68;
		paymentTypeRowNum = 258;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

		//9 months
		transactionRowNum = 68;
		paymentTypeRowNum = 259;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

	@Test(description = "Verify URL for emi banking transaction for ICICITRAVEL gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_ICICITRAVEL(Config testConfig)
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//12 months
		int transactionRowNum = 6;
		int paymentTypeRowNum = 264;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//3 months
		transactionRowNum = 6;
		paymentTypeRowNum = 261;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//6 months
		transactionRowNum = 6;
		paymentTypeRowNum = 262;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//9 months
		transactionRowNum = 6;
		paymentTypeRowNum = 263;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());				
	}

	@Test(description = "Verify emi banking transaction for ICICITRAVEL gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_ICICITRAVEL_CF(Config testConfig)
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//12 months
		int transactionRowNum = 73;
		int paymentTypeRowNum = 264;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

		//3 months
		transactionRowNum = 73;
		paymentTypeRowNum = 261;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

		//6 months
		transactionRowNum = 73;
		paymentTypeRowNum = 262;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

		//9 months
		transactionRowNum = 73;
		paymentTypeRowNum = 263;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

	@Test(description = "Verify URL for emi banking transaction for HDFCEMI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_HDFCEMI(Config testConfig)
	{

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//12 months
		int transactionRowNum = 4;
		int paymentTypeRowNum = 41;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//3 months
		transactionRowNum = 4;
		paymentTypeRowNum = 44;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//6 months
		transactionRowNum = 4;
		paymentTypeRowNum = 47;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//9 months
		transactionRowNum = 4;
		paymentTypeRowNum = 49;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());				
	}


	@Test(description = "Verify emi banking transaction for HDFCEMI gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_HDFCEMI_CF(Config testConfig)
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//12 months
		int transactionRowNum = 71;
		int paymentTypeRowNum = 41;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";

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

		//3 months
		transactionRowNum = 71;
		paymentTypeRowNum = 44;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//view in dashboard
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		//6 months
		transactionRowNum = 71;
		paymentTypeRowNum = 47;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		//view in dashboard		
		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		//9 months
		transactionRowNum = 71;
		paymentTypeRowNum = 49;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		transactionAmount = String.valueOf(transactionamount)+"0";

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

	@Test(description = "Verify URL for emi banking transaction for CITI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_CITI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		//3 months
		int transactionRowNum = 2;
		int paymentTypeRowNum = 42;
		int cardDetailsRowNum = 16;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		//6 months
		transactionRowNum = 2;
		paymentTypeRowNum = 45;
		cardDetailsRowNum = 16;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());				
	}

	@Test(description = "Verify URL for emi banking transaction for CITI gateway with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_CITI_CF(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		//3 months
		int transactionRowNum = 69;
		int paymentTypeRowNum = 42;
		int cardDetailsRowNum = 16;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

		//6 months
		transactionRowNum = 69;
		paymentTypeRowNum = 45;
		cardDetailsRowNum = 16;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[4];
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

		Assert.assertTrue(testConfig.getTestResult());				
	}


	/**
	 * Verifying if labels appear on payment option page for EMI Banking displaying correct text
	 */	
	@Test(description = "Test if all label fields for emibanking are present on the payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EmiTabPageElements(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CITI 3 months
		int transactionRowNum = 2;
		int paymentTypeRowNum = 42;
		EMITab emiTab = (EMITab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		emiTab.SelectBankAndDuration(paymentTypeRowNum);

		//Verify details
		String expected = helper.cardDetailsData.GetData(1, "EMI");
		Helper.compareEquals(testConfig, "Emi Tab text", expected, emiTab.getEMITabText());

		expected = "Select your Bank:";
		//Un-comment below line if we make the label consistent with NB tab, i.e. 'Select your bank:'
		//expected = helper.cardDetailsData.GetData(10, "Bank");

		Helper.compareEquals(testConfig, "Select Your Bank", expected, emiTab.getSelectYourBankLabel());

		expected = helper.cardDetailsData.GetData(10, "EMI");
		Helper.compareEquals(testConfig, "Select EMI duration", expected, emiTab.getSelectEmiDurationLabel());

		expected = helper.cardDetailsData.GetData(10, "Name");
		Helper.compareEquals(testConfig, "Name on Card", expected, emiTab.getNameOnCardLabel());

		String msg1 = "An additional fee of 1.103% i.e. Rs. 0.02 (Bank processing charges) will be applicable.\n";
		String msg2 = "Your monthly EMI payment will be Rs. 0.67/month for the next 3 Months.";
		expected = msg1 + msg2;
		Helper.compareEquals(testConfig, "EMI Charges", expected, emiTab.getEmiChargesLabel());

		expected = helper.cardDetailsData.GetData(10, "CC");
		Helper.compareEquals(testConfig, "Card Number", expected, emiTab.getCardNumberLabel());

		expected = helper.cardDetailsData.GetData(10, "CVV");
		Helper.compareEquals(testConfig, "CVV Number", expected, emiTab.getCvvNumberLabel());

		expected = helper.cardDetailsData.GetData(10, "Mon");
		Helper.compareEquals(testConfig, "Expiry date", expected, emiTab.getExpiryDateLabel());

		expected = helper.cardDetailsData.GetData(10, "Note");
		Helper.compareEquals(testConfig, "Note", expected, emiTab.getNoteText());

		emiTab.clickWhatIsCVVLink();
		Helper.compareTrue(testConfig, "Card Image for what is CVV link", emiTab.isCardImageAppearing());

		//HDFC2 12 months
		transactionRowNum = 3;
		paymentTypeRowNum = 40;
		emiTab = (EMITab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		emiTab.SelectBankAndDuration(paymentTypeRowNum);

		msg1 = "Your monthly EMI payment will be Rs. 0.25/month for the next 12 Months.";
		expected = msg1;
		Helper.compareEquals(testConfig, "EMI Charges", expected, emiTab.getEmiChargesLabel());

		//HDFCEMI 6 months
		transactionRowNum = 4;
		paymentTypeRowNum = 47;

		// Verify if the required ibibo code is present or not
		helper.VerifyIbiboCodePresentForMerchant("5913", paymentTypeRowNum);
		emiTab = (EMITab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		emiTab.SelectBankAndDuration(paymentTypeRowNum);

		msg1 = "Your monthly EMI payment will be Rs. 0.23/month for the next 6 Months.";
		expected = msg1;
		Helper.compareEquals(testConfig, "EMI Charges", expected, Element.getPageElement(testConfig, How.css, "div[id='emi_text_div_EMI6']>div>div>div").getText());

		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verify the error messages when invalid inputs are entered in EMI Tab
	 */
	@Test(description = "Verify the error messages when invalid inputs are entered in EMI Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnEMITab(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 2;
		int paymentTypeRowNum = 42;
		helper.emiTab = (EMITab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		helper.emiTab.SelectBankAndDuration(paymentTypeRowNum);

		//Special char input 
		verifyErrors(helper, 4, 
				helper.cardDetailsData.GetData(11, "CC"), helper.cardDetailsData.GetData(11, "Name")
				, helper.cardDetailsData.GetData(11, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));

		//Input with spaces
		verifyErrors(helper, 5, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(14, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));

		//Spaces Only
		verifyErrors(helper, 6, 
				helper.cardDetailsData.GetData(14, "CC"), helper.cardDetailsData.GetData(11, "Name")
				, helper.cardDetailsData.GetData(13, "CVV"), helper.cardDetailsData.GetData(11, "Mon"));

		//Blank Input
	verifyErrors(helper, 7, 
				helper.cardDetailsData.GetData(14, "CC"), helper.cardDetailsData.GetData(14, "Name")
				, helper.cardDetailsData.GetData(13, "CVV"), helper.cardDetailsData.GetData(11, "Year"));

		//Wrong details
		verifyErrors(helper, 8, 
				helper.cardDetailsData.GetData(12, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(14, "CVV"), helper.cardDetailsData.GetData(12, "Year"));

		//Alphabet card details
		verifyErrors(helper, 9, 
				helper.cardDetailsData.GetData(14, "Name"), helper.cardDetailsData.GetData(14, "CC")
				, helper.cardDetailsData.GetData(11, "CVV"), helper.cardDetailsData.GetData(14, "Mon"));

		Assert.assertTrue(testConfig.getTestResult());

	}

	private void verifyErrors(TransactionHelper helper, int inputCardDetailsRowNum, String cardNameError, String cardNumberError, String cvvError, String expiryError)
	{
		String comments = helper.cardDetailsData.GetData(inputCardDetailsRowNum, "Comments");
		helper.emiTab.FillCardDetails(inputCardDetailsRowNum);
		helper.payment.clickPayNowToGetError();

		Helper.compareEquals(helper.testConfig, "Card name '"+ comments +"' error", cardNameError, helper.emiTab.getCardNameErrorMessage());

		Helper.compareEquals(helper.testConfig, "Card number '"+ comments +"' error", cardNumberError, helper.emiTab.getCardNumberErrorMessage());

		Helper.compareEquals(helper.testConfig, "CVV number '"+ comments +"' error", cvvError, helper.emiTab.getCvvNumberErrorMessage());

		Helper.compareEquals(helper.testConfig, "Expiry Date '"+ comments +"' error", expiryError, helper.emiTab.getExpiryDateErrorMessage());
	}

	/**
	 * Verifying bank EMI message with emiBankInterestFlag=1
	 */	
	@Test(description = "Test bank EMI message with emiBankInterestFlag=1", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_emiBankInterestFlag(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//CITI 3 months EMI message
		int transactionRowNum = 45;
		int paymentTypeRowNum = 42;
		EMITab emiTab = (EMITab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		emiTab.SelectBankAndDuration(paymentTypeRowNum);

		String msg = "An additional charge at the rate of 12% per annum would be charged on your credit card.  Indicative EMI is Rs. 3.4 and indicative total interest paid will be Rs. 0.2 .";
		String expected = msg;
		Helper.compareEquals(testConfig, "EMI Charges", expected, emiTab.getEmiChargesLabel());

		//AXIS 3 months EMI message
		transactionRowNum = 45;
		paymentTypeRowNum = 257;
		emiTab = (EMITab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.emi);
		emiTab.SelectBankAndDuration(paymentTypeRowNum);

		msg = "Your monthly EMI payment will be Rs. 3.33/month for the next 3 Months.";
		expected = msg;
		Helper.compareEquals(testConfig, "EMI Charges", expected, Element.getPageElement(testConfig, How.css, "div#emi_text_div_EMIA3").getText());

		Assert.assertTrue(testConfig.getTestResult());			
	}
}