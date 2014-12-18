package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.EMITab;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestBase;
import Utils.Element.How;

public class TestTransactionSeamlessEMI extends TestBase{

	@Test(description = "Verify seamless EMI transaction for AXIS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_EMI_AXIS(Config testConfig)
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//3 months
		int transactionRowNum = 1;
		int paymentTypeRowNum = 257;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
	
		Assert.assertTrue(testConfig.getTestResult());				
	}

	@Test(description = "Verify seamless EMI transaction for ICICITravel gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_EMI_ICICITRAVEL(Config testConfig)
	{		
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//6 months
		int transactionRowNum = 6;
		int paymentTypeRowNum = 262;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());				
	}


	@Test(description = "Verify seamless EMI transaction for HDFCEMI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_EMI_HDFCEMI(Config testConfig)
	{

		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//9 months
		int transactionRowNum = 4;
		int paymentTypeRowNum = 49;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());				
	}

	@Test(description = "Verify seamless EMI transaction for CITI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_EMI_CITI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		//3 months
		int transactionRowNum = 2;
		int paymentTypeRowNum = 42;
		int cardDetailsRowNum = 16;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		//6 months
		transactionRowNum = 2;
		paymentTypeRowNum = 45;
		cardDetailsRowNum = 16;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify Payment option page when PGCODE:EMI and ibiboCode: ABC
	@Test(description = "Verify output when ibiboCode is blank", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_EMI_WrongIbiboCode(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		int transactionRowNum = 63;
		int paymentTypeRowNum = 316;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValueEMITab().equals("Select"));
		Assert.assertTrue(helper.payment.verifyAllDropDownValueEMITab(1).equals("CITI Bank"));
		Assert.assertTrue(helper.payment.verifyAllDropDownValueEMITab(2).equals("AXIS Bank"));

		transactionRowNum = 63;
		paymentTypeRowNum = 315;
		cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyDropDownValueEMITab().equals("Select"));
	}
	
	/**
	 * Verifying bank EMI message with emiBankInterestFlag=1 in seamless flow
	 */	
	@Test(description = "Test bank EMI message with emiBankInterestFlag=1", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_emiBankInterestFlagSeamlessFlow(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//CITI 3 months EMI message
		int transactionRowNum = 45;
		int paymentTypeRowNum = 42;
		int cardDetailsRowNum = 43;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);

		String msg = "An additional charge at the rate of 12% per annum would be charged on your credit card. This will be calculated on the basis of your EMI tenure.";
		String expected = msg;
		Browser.wait(testConfig, 2);
		Helper.compareEquals(testConfig, "EMI Charges",expected, Element.getPageElement(testConfig, How.css, "div.pnote div strong").getText() );

		//AXIS 3 months EMI message
		transactionRowNum = 45;
		paymentTypeRowNum = 257;
		cardDetailsRowNum = 43;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
	
		msg = "Your monthly EMI payment will be Rs. 3.33/month for the next 3 Months.";
		expected = msg;
		Browser.wait(testConfig, 2);
		Helper.compareEquals(testConfig, "EMI Charges", expected, Element.getPageElement(testConfig, How.css, "div#emi_text_div_EMIA3").getText());

		Assert.assertTrue(testConfig.getTestResult());			
	}
	
	

}