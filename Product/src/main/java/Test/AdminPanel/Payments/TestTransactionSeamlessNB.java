package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.NBTab;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;

public class TestTransactionSeamlessNB extends TestBase{

	@Test(description = "Verify seamless net banking transaction through CBK gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_CBK(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Axis Bank
		int transactionRowNum = 1;
		int paymentTypeRowNum = 61;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());					
	}

	//Verify net banking transaction through TECHPROCESS gateway
	@Test(description = "Verify net banking transaction through TECHPROCESS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_TECHPROCESS1(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Allahabad Bank
		int transactionRowNum = 7;
		int paymentTypeRowNum = 57;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());	
	}

	//Verify net banking transaction through CCAVENUE gateway
	@Test(description = "Verify net banking transaction through CCAVENUE gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_CCAVENUE(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Andhra Bank
		int transactionRowNum = 2;
		int paymentTypeRowNum = 59;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());		
	}

	//Verify net banking transaction through AXISNB gateway
	@Test(description = "Verify net banking transaction through AXISNB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_AXISNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Axis Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 60;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify net banking transaction through CITRUS gateway
	@Test(description = "Verify net banking transaction through CITRUS gateway", 
			dataProvider="GetTestConfig", timeOut=720000, groups="1")
	public void test_Seamless_NB_CITRUS(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Axis Bank
		int transactionRowNum = 5;
		int paymentTypeRowNum = 62;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify net banking transaction through EBS gateway
	@Test(description = "Verify net banking transaction through EBS gateway", 
			dataProvider="GetTestConfig", timeOut=720000, groups="1")
	public void test_Seamless_NB_EBS(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//AXIS Bank NetBanking
		int transactionRowNum = 4;
		int paymentTypeRowNum = 63;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());					
	}

	//Verify net banking transaction through BOI gateway
	@Test(description = "Verify net banking transaction through BOI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_BOI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Bank of India
		int transactionRowNum = 3;
		int paymentTypeRowNum = 73;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify net banking transaction through BOMNB gateway
	@Test(description = "Verify net banking transaction through BOMNB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_BOMNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Bank of Maharashtra
		int transactionRowNum = 3;
		int paymentTypeRowNum = 77;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify net banking transaction through CITI gateway
	@Test(description = "Verify net banking transaction through CITI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_CITI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		// Going to debit card tab

		//CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 85;
		int cardDetailsRowNum = 16;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through CORP gateway
	@Test(description = "Verify URL for net banking transaction through CORP gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_CORP(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Corporation Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 90;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify net banking transaction through SCOM gateway
	@Test(description = "Verify net banking transaction through SCOM gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_SCOM(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Deutsche Bank
		int transactionRowNum = 6;
		int paymentTypeRowNum = 97;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through FEDB gateway
	@Test(description = "Verify URL for net banking transaction through FEDB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_FEDB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Federal Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 107;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through HDFCNB gateway
	@Test(description = "Verify URL for net banking transaction through HDFCNB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_HDFCNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//HDFC Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 113;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through ICI gateway
	@Test(description = "Verify URL for net banking transaction through ICI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_ICI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//ICICI NetBanking
		int transactionRowNum = 3;
		int paymentTypeRowNum = 120;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through IOB gateway
	@Test(description = "Verify URL for net banking transaction through IOB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_IOB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Indian Overseas Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 130;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through INDUSIND gateway
	@Test(description = "Verify URL for net banking transaction through INDUSIND gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_INDUSIND(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//IndusInd Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 134;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through IDBINB gateway
	@Test(description = "Verify URL for net banking transaction through IDBINB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_IDBINB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Industrial Development Bank of India
		int transactionRowNum = 3;
		int paymentTypeRowNum = 137;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through JKNB gateway
	@Test(description = "Verify URL for net banking transaction through JKNB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_JKNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//IndusInd Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 144;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through KRKBNB gateway
	@Test(description = "Verify URL for net banking transaction through KRKBNB gateway",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_KRKBNB(Config testConfig)
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Karnataka Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 146;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;
		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through SIBNB gateway
	@Test(description = "Verify URL for net banking transaction through SIBNB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_SIBNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//South Indian Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 172;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through SBBJB gateway
	@Test(description = "Verify URL for net banking transaction through SBBJB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_SBBJB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//State Bank of Bikaner and Jaipur
		int transactionRowNum = 3;
		int paymentTypeRowNum = 177;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through SBHB gateway
	@Test(description = "Verify URL for net banking transaction through SBHB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_SBHB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//State Bank of Hyderabad
		int transactionRowNum = 3;
		int paymentTypeRowNum = 181;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through SBINB gateway
	@Test(description = "Verify URL for net banking transaction through SBINB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_SBINB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//State Bank of India
		int transactionRowNum = 3;
		int paymentTypeRowNum = 188;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through SBMB gateway
	@Test(description = "Verify URL for net banking transaction through SBMB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_SBMB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//State Bank of Mysore
		int transactionRowNum = 3;
		int paymentTypeRowNum = 194;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through SBTB gateway
	@Test(description = "Verify URL for net banking transaction through SBTB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_SBTB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//State Bank of Travancore
		int transactionRowNum = 3;
		int paymentTypeRowNum = 200;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through UBI gateway
	@Test(description = "Verify URL for net banking transaction through UBI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_UBI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Union Bank of India
		int transactionRowNum = 3;
		int paymentTypeRowNum = 212;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through UNIB gateway
	@Test(description = "Verify URL for net banking transaction through UNIB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_UNIB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//United Bank of India
		int transactionRowNum = 3;
		int paymentTypeRowNum = 232;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}


	//Verify URL for net banking transaction through YES gateway
	@Test(description = "Verify URL for net banking transaction through YES gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_YES(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Yes Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 221;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through DCB gateway
	@Test(description = "Verify URL for net banking transaction through DCB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_DCB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Yes Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 228;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through CBIB gateway
	@Test(description = "Verify URL for net banking transaction through CBIB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_CBIB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Central Bank Of India
		int transactionRowNum = 3;
		int paymentTypeRowNum = 254;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through CUBB gateway
	@Test(description = "Verify URL for net banking transaction through CUBB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_CUBB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//City Union Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 253;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through KRVB gateway
	@Test(description = "Verify URL for net banking transaction through KRVB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_KRVB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Karur Vysya 
		int transactionRowNum = 3;
		int paymentTypeRowNum = 256;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//Verify URL for net banking transaction through VJYB gateway
	@Test(description = "Verify URL for net banking transaction through VJYB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_NB_VJYB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Vijaya Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 255;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}


	//Verify transaction output with wrong ibibo code
	@Test(description = "Verify trasaction output with wrong ibibo code", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Wrong_IbiboCode(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 3;
		int paymentTypeRowNum = 291;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Helper.compareEquals(testConfig, "Error Message" ,"Note: Selected payment option is not available at this moment. Kindly try a different payment option or try again after some time.", helper.payment.verifyErrorMessageNBTab());
		helper.payment.verifyDropDownValueNBTab().equals("Select Bank");

		transactionRowNum = 3;
		paymentTypeRowNum = 290;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		helper.payment.verifyDropDownValueNBTab().equals("Select Bank");			
	}

//Verify transaction output with blank card details
@Test(description = "Verify trasaction output with blank card details", 
dataProvider="GetTestConfig", timeOut=600000, groups="1")
public void test_BlankCard(Config testConfig)
{
	TransactionHelper helper = new TransactionHelper(testConfig, true);
	helper.DoLogin();

	//Axis Bank
	int transactionRowNum = 1;
	int paymentTypeRowNum = 60;
	int cardDetailsRowNum = 7;
	helper.GetTestTransactionPage();
	helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
	helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

	Assert.assertTrue(testConfig.getTestResult());
}

}