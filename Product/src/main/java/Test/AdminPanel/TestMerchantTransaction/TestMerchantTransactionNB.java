package Test.AdminPanel.TestMerchantTransaction;

import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantNBTab;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Utils.Config;
import Utils.Helper;
import Utils.Popup;
import Utils.TestBase;

public class TestMerchantTransactionNB extends TestBase{

	@Test(description = "Verify net banking transaction through CBK gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CBK(Config testConfig) 
	{

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		//AXIS Bank NetBanking
		int transactionRowNum = 1;
		int paymentTypeRowNum = 61;
		int cardDetailsRowNum = -1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.bankRedirectPage = (BankRedirectPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		merHelper.bankRedirectPage.VerifyRedirectedURL(merHelper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());	
	}


	@Test(description = "Verify net banking transaction through CCAVENUE gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CCAVENUE(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();
		//HDFC Bank
		int transactionRowNum = 2;
		int paymentTypeRowNum = 110;
		int cardDetailsRowNum = -1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.bankRedirectPage = (BankRedirectPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		merHelper.bankRedirectPage.VerifyRedirectedURL(merHelper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());		
	}



	@Test(description = "Verify net banking transaction through AXISNB gateway", 
			dataProvider="GetTestConfig",  timeOut=120000, groups="1")
	public void test_NB_AXISNB(Config testConfig) 
	{

		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();


		//Axis Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 60;
		int cardDetailsRowNum = -1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.bankRedirectPage = (BankRedirectPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		merHelper.bankRedirectPage.VerifyRedirectedURL(merHelper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}



	@Test(description = "Verify URL for net banking transaction through HDFCNB gateway", 
			dataProvider="GetTestConfig",  timeOut=600000, groups="1")
	public void test_NB_HDFCNB(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();
		//HDFC Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 113;
		int cardDetailsRowNum = -1;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.bankRedirectPage = (BankRedirectPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		merHelper.bankRedirectPage.VerifyRedirectedURL(merHelper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}
	/**
	 * verify error message when click on make payment without select any bank from list
	 * @param testConfig
	 */
	@Test(description = "Verify the error messages when alphabetical card details are entered in EMI Tab", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnEMITabWithAlphabeticalDetails(Config testConfig) 
	{
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();
		int transactionRowNum = 3;
		merHelper.merNBTab = (MerchantNBTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.netbanking);
		merHelper.merNBTab.clickPayNowToGetError();
		String txt = Popup.text(testConfig);

		Helper.compareEquals(testConfig, "select bank", "Please select your bank", txt);

		Assert.assertTrue(testConfig.getTestResult());	
	}

}


