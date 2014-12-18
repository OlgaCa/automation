package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;

public class TestTopLevelRoutingAltPay extends TestBase{

	//TODO Top Level Routing on the basis of health
	
	@Test(description = "Verify top level routed transaction through TechProcess gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_TechProcess(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 11;
		int paymentTypeRowNum = 225;
		
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTopLevelRoutingTestTransaction(transactionRowNum, paymentTypeRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());	
	}
	
	@Test(description = "Verify top level routed transaction through CCAVENUE gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CCAVENUE(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 8;
		int paymentTypeRowNum = 222;
		
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTopLevelRoutingTestTransaction(transactionRowNum, paymentTypeRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());	
	}
	
	@Test(description = "Verify top level routed transaction through PAYU gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_PAYU(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//HDFC2 transaction through PAYU 
		int transactionRowNum = 10;
		int paymentTypeRowNum = 241;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}
	
	@Test(description = "Verify altpay NB transaction through CCAVENUE gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CCAVENUE(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//AltPay page check for Central Bank of India, CCAVENUE Netbanking transaction
		int transactionRowNum = 2;
		int paymentTypeRowNum = 229;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());	
	}

}
