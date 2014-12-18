package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestTransactionSeamless extends TestBase{

	@Test(description = "Verify correct additional charge is applied on transactions according to the ibibo code", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EMI_CASH_CFIbiboCode(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying correct additional charge is applied EMIA3:1.00 when transaction is done through
		 EMI 3 months via AXIS gateway 
		 (Rule- EMI:10.00,EMIA3:1.00,EMIA6:2.00,EMIA9:3.00,EMIA12:4.00,CASH:5.00,AMON:6.00,ITZC:7.00)
		 First preference is of Ibibo code then payment mode 
		 ****************
		 *****************/
		int transactionRowNum = 77;
		int paymentTypeRowNum = 257;
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

		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		helper.testResponse = (TestResponsePage) helper.payment.DoEMITransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		
		/****************
		 ****************
		 Verifying correct additional charge is applied EMIA6:2.00 when transaction is done through
		 EMI 6 months via AXIS gateway 
		 (Rule- EMI:10.00,EMIA3:1.00,EMIA6:2.00,EMIA9:3.00,EMIA12:4.00,CASH:5.00,AMON:6.00,ITZC:7.00) 
		 First preference is of Ibibo code then payment mode
		 ****************
		 *****************/

		paymentTypeRowNum = 258;
		helper.GetTestTransactionPage();

		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[3];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		helper.testResponse = (TestResponsePage) helper.payment.DoEMITransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		
		/****************
		 ****************
		 Verifying correct additional charge is applied EMIA9:3.00 when transaction is done through
		 EMI 9 months via AXIS gateway 
		 (Rule- EMI:10.00,EMIA3:1.00,EMIA6:2.00,EMIA9:3.00,EMIA12:4.00,CASH:5.00,AMON:6.00,ITZC:7.00) 
		 First preference is of Ibibo code then payment mode
		 ****************
		 *****************/

		paymentTypeRowNum = 259;
		helper.GetTestTransactionPage();

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

		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		helper.testResponse = (TestResponsePage) helper.payment.DoEMITransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		
		/****************
		 ****************
		 Verifying correct additional charge is applied EMIA12:4.00 when transaction is done through
		 EMI 12 months via AXIS gateway 
		 (Rule- EMI:10.00,EMIA3:1.00,EMIA6:2.00,EMIA9:3.00,EMIA12:4.00,CASH:5.00,AMON:6.00,ITZC:7.00) 
		 First preference is of Ibibo code then payment mode
		 ****************
		 *****************/

		paymentTypeRowNum = 260;
		helper.GetTestTransactionPage();

		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[5];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		helper.testResponse = (TestResponsePage) helper.payment.DoEMITransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		
		/****************
		 ****************
		 Verifying correct additional charge is applied EMI:10.00 when transaction is done through
		 EMI 6 months via AXIS gateway and no specific ibibo_code additional charge is provided
		 (Rule- EMI:10.00,EMIA3:1.00,EMIA9:3.00) 
		 First preference is of Ibibo code then payment mode
		 ****************
		 *****************/

		paymentTypeRowNum = 258;
		helper.GetTestTransactionPage();

		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		testConfig.putRunTimeProperty("additionalCharges", "EMI:10.00,EMIA3:1.00,EMIA9:3.00");

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

		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		helper.testResponse = (TestResponsePage) helper.payment.DoEMITransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		
		/****************
		 ****************
		 Verifying correct additional charge is applied AMON:6.00 when transaction is done through
		 Airtel Money CashCard 
		 (Rule- EMI:10.00,EMIA3:1.00,EMIA6:2.00,EMIA9:3.00,EMIA12:4.00,CASH:5.00,AMON:6.00,ITZC:7.00) 
		 First preference is of Ibibo code then payment mode
		 ****************
		 *****************/
		transactionRowNum = 77;
		paymentTypeRowNum = 52;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();

		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[7];
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

		/****************
		 ****************
		 Verifying correct additional charge is applied ITZC:7.00 when transaction is done through
		 ITZ CashCard 
		 (Rule- EMI:10.00,EMIA3:1.00,EMIA6:2.00,EMIA9:3.00,EMIA12:4.00,CASH:5.00,AMON:6.00,ITZC:7.00) 
		 First preference is of Ibibo code then payment mode
		 ****************
		 *****************/
		paymentTypeRowNum = 54;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();

		transactionData = new TestDataReader(testConfig, "TransactionDetails");
		amnt = transactionData.GetData(transactionRowNum, "amount");
		transactionamount = Double.parseDouble(amnt);

		keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		keyValue = keyvalue.split(":");
		addCharge = keyValue[8].trim();

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

	@Test(description = "Verify credit card transaction through AMEX gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_AMEX(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//AMEX
		int transactionRowNum = 2;
		int paymentTypeRowNum = 1;
		int cardDetailsRowNum = 2;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify credit card transaction through CBK gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_CBK(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//AMEX
		int transactionRowNum = 23;
		int paymentTypeRowNum = 8;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify credit card transaction through AXIS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_AXIS(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
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

	@Test(description = "Verify seamless transaction with preinitmode 0", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_seamless_preinit_zero(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
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


	@Test(description = "Verify credit card transaction through CITI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="2")
	public void test_CC_CITI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//DINR
		int transactionRowNum = 2;
		int paymentTypeRowNum = 9;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify credit card transaction through HDFC2 gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_HDFC2(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify credit card transaction through ICICITRAVEL gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_ICICITRAVEL(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 6;
		int paymentTypeRowNum = 244;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify credit card transaction through SBIPG gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CC_SBIPG(Config testConfig)
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CC
		int transactionRowNum = 5;
		int paymentTypeRowNum = 245;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

}