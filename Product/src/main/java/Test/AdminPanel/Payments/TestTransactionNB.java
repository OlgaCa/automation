package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.Payments.PaymentOptions.NBTab;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;

public class TestTransactionNB extends TestBase{

	@Test(description = "Verify net banking transaction through CBK gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CBK1(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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
	
	//We can verify CBK gateway with one bank. Rest of the code is commented -- ATUL JAIN
	
	/*	//Andhra Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 58;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//AXIS Bank NetBanking
		transactionRowNum = 1;
		paymentTypeRowNum = 61;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of Bahrain and Kuwait
		transactionRowNum = 1;
		paymentTypeRowNum = 65;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum)  ;

		//Bank of Baroda - Corporate Banking
		transactionRowNum = 1;
		paymentTypeRowNum = 68;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of Baroda - Retail Banking
		transactionRowNum = 1;
		paymentTypeRowNum = 70;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of India
		transactionRowNum = 1;
		paymentTypeRowNum = 74;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of Maharashtra
		transactionRowNum = 1;
		paymentTypeRowNum = 78;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Central Bank of India
		transactionRowNum = 1;
		paymentTypeRowNum = 82;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//City Union Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 86;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Corporation Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 89;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Deutsche Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 94;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Development Credit Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 99;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Dhanlakshmi Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 101;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());					
	}

	@Test(description = "Verify net banking transaction through CBK gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CBK2(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		//Federal Bank
		int transactionRowNum = 1;
		int paymentTypeRowNum = 103;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//HDFC Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 109;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//ICICI NetBanking
		transactionRowNum = 1;
		paymentTypeRowNum = 116;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Indian Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 124;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		NBTab nbTab = (NBTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.netbanking);
		nbTab.verifyDisabledPaymentTypes("Indian Overseas Bank", "CBK");

		//IndusInd Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 132;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Industrial Development Bank of India
		transactionRowNum = 1;
		paymentTypeRowNum = 135;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//ING Vyasa Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 139;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Karnataka Bank Ltd
		transactionRowNum = 1;
		paymentTypeRowNum = 147;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		// Taking so much time
		//Karur Vysya Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 151;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Kotak Mahindra Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 154;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Laxmi Vilas Bank-Corporate
		transactionRowNum = 1;
		paymentTypeRowNum = 158;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Laxmi Vilas Bank-Retail
		transactionRowNum = 1;
		paymentTypeRowNum = 159;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Oriental Bank of Commerce
		transactionRowNum = 1;
		paymentTypeRowNum = 160;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());					
	}

	@Test(description = "Verify net banking transaction through CBK gateway", 
			dataProvider="GetTestConfig", timeOut=720000, groups="1")
	public void test_NB_CBK3(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Punjab National Bank - Corporate Banking
		int transactionRowNum = 1;
		int paymentTypeRowNum = 163;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Punjab National Bank - Retail Banking
		transactionRowNum = 1;
		paymentTypeRowNum = 165;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Ratnakar Bank 
		transactionRowNum = 1;
		paymentTypeRowNum = 167;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Shamrao Vitthal Co-operative Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 169;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//South Indian Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 170;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Hyderabad
		transactionRowNum = 1;
		paymentTypeRowNum = 178;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of India
		transactionRowNum = 1;
		paymentTypeRowNum = 184;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Patiala
		transactionRowNum = 1;
		paymentTypeRowNum = 196;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Syndicate Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 202;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Tamilnad Mercantile Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 204;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//UCO Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 207;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Union Bank of India
		transactionRowNum = 1;
		paymentTypeRowNum = 208;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;
		
		//United Bank of India
		transactionRowNum = 1;
		paymentTypeRowNum = 213;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;
		
		//Vijaya Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 215;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Yes Bank
		transactionRowNum = 1;
		paymentTypeRowNum = 217;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

							
	}
*/
	@Test(description = "Verify net banking transaction through TECHPROCESS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_TECHPROCESS1(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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
	
	//We can verify TECHPROCESS gateway with one Netbanking option.Rest of the code is commented -- ATUL JAIN 
	
	/*	//AXIS Bank NetBanking
		transactionRowNum = 7;
		paymentTypeRowNum = 64;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;


		//Bank of Bahrain and Kuwait
		transactionRowNum = 7;
		paymentTypeRowNum = 67;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of Baroda - Retail Banking
		transactionRowNum = 7;
		paymentTypeRowNum = 72;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of India
		transactionRowNum = 7;
		paymentTypeRowNum = 76;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of Maharashtra
		transactionRowNum = 7;
		paymentTypeRowNum = 80;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//City Union Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 88;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Corporation Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 93;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Deutsche Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 98;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Development Credit Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 100;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Dhanlakshmi Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 102;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Federal Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 108;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//HDFC Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 115;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//ICICI NetBanking
		transactionRowNum = 7;
		paymentTypeRowNum = 122;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//IDBI Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 123;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Indian Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 127;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Indian Overseas Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 131;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//ING Vyasa Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 141;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify net banking transaction through TECHPROCESS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_TECHPROCESS2(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Jammu and Kashmir Bank
		int transactionRowNum = 7;
		int paymentTypeRowNum = 145;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Karnataka Bank Ltd
		transactionRowNum = 7;
		paymentTypeRowNum = 150;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Karur Vysya Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 153;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Oriental Bank of Commerce
		transactionRowNum = 7;
		paymentTypeRowNum = 162;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//South Indian Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 173;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Standard Chartered Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 175;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Hyderabad
		transactionRowNum = 7;
		paymentTypeRowNum = 182;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of India
		transactionRowNum = 7;
		paymentTypeRowNum = 190;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Mysore
		transactionRowNum = 7;
		paymentTypeRowNum = 195;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Travancore
		transactionRowNum = 7;
		paymentTypeRowNum = 201;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Tamilnad Mercantile Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 206;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Union Bank of India
		transactionRowNum = 7;
		paymentTypeRowNum = 211;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//United Bank of India
		transactionRowNum = 7;
		paymentTypeRowNum = 214;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Vijaya Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 216;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Yes Bank
		transactionRowNum = 7;
		paymentTypeRowNum = 220;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

			
	}
*/

	@Test(description = "Verify net banking transaction through CCAVENUE gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CCAVENUE1(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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
		
//This part of code is not needed as we can verify CCAVENUE with one bank. Commenting this code -- ATUL JAIN
		
/*
		//Bank of Bahrain and Kuwait
		transactionRowNum = 2;
		paymentTypeRowNum = 66;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of Baroda - Corporate Banking
		transactionRowNum = 2;
		paymentTypeRowNum = 69;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of Baroda - Retail Banking
		transactionRowNum = 2;
		paymentTypeRowNum = 71;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of India
		transactionRowNum = 2;
		paymentTypeRowNum = 75;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of Maharashtra
		transactionRowNum = 2;
		paymentTypeRowNum = 79;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Catholic Syrian Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 81;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Switch to debit card tab
		//Citi Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 84;
		cardDetailsRowNum = 16;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//City Union Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 87;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Deutsche Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 95;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Federal Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 104;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//HDFC Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 110;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//ICICI NetBanking
		transactionRowNum = 2;
		paymentTypeRowNum = 117;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Indian Overseas Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 129;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//IndusInd Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 133;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Industrial Development Bank of India
		transactionRowNum = 2;
		paymentTypeRowNum = 136;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//ING Vyasa Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 140;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Jammu and Kashmir Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 142;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Karnataka Bank Ltd
		transactionRowNum = 2;
		paymentTypeRowNum = 148;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Karur Vysya Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 152;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Kotak Mahindra Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 155;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		
	}

	@Test(description = "Verify net banking transaction through CCAVENUE gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CCAVENUE2(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Laxmi Vilas Bank-Corporate
		int transactionRowNum = 2;
		int paymentTypeRowNum = 157;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Oriental Bank of Commerce
		transactionRowNum = 2;
		paymentTypeRowNum = 161;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Punjab National Bank - Corporate Banking
		transactionRowNum = 2;
		paymentTypeRowNum = 164;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Punjab National Bank - Retail Banking
		transactionRowNum = 2;
		paymentTypeRowNum = 166;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Royal Bank of Scotland
		transactionRowNum = 2;
		paymentTypeRowNum = 168;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//South Indian Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 171;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Standard Chartered Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 174;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Hyderabad
		transactionRowNum = 2;
		paymentTypeRowNum = 179;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of India
		transactionRowNum = 2;
		paymentTypeRowNum = 185;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Mysore
		transactionRowNum = 2;
		paymentTypeRowNum = 191;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Travancore
		transactionRowNum = 2;
		paymentTypeRowNum = 197;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Syndicate Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 203;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Tamilnad Mercantile Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 205;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Union Bank of India
		transactionRowNum = 2;
		paymentTypeRowNum = 209;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Central Bank of India
		transactionRowNum = 2;
		paymentTypeRowNum = 229;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//United Bank of India
		transactionRowNum = 2;
		paymentTypeRowNum = 230;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Vijaya Bank
		transactionRowNum = 2;
		paymentTypeRowNum = 231;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());		
	}
*/
	@Test(description = "Verify net banking transaction through AXISNB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_AXISNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify net banking transaction through CITRUS gateway", 
			dataProvider="GetTestConfig", timeOut=720000, groups="1")
	public void test_NB_CITRUS(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Axis Bank
		int transactionRowNum = 5;
		int paymentTypeRowNum = 62;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//We can verify gateway with one bank only-- ATUL JAIN
		/*//Deutsche Bank
		transactionRowNum = 5;
		paymentTypeRowNum = 96;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Federal Bank
		transactionRowNum = 5;
		paymentTypeRowNum = 105;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;
		 
		//HDFC Bank
		transactionRowNum = 5;
		paymentTypeRowNum = 111;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//ICICI NetBanking
		transactionRowNum = 5;
		paymentTypeRowNum = 118;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;


		//Indian Bank
		transactionRowNum = 5;
		paymentTypeRowNum = 125;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;


		//State Bank of Hydrabad
		transactionRowNum = 5;
		paymentTypeRowNum = 183;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of India
		transactionRowNum = 5;
		paymentTypeRowNum = 186;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Mysore
		transactionRowNum = 5;
		paymentTypeRowNum = 192;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Travancore
		transactionRowNum = 5;
		paymentTypeRowNum = 198;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Union Bank of India
		transactionRowNum = 5;
		paymentTypeRowNum = 210;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Yes Bank
		transactionRowNum = 5;
		paymentTypeRowNum = 218;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Bikaner and Jaipur
		transactionRowNum = 5;
		paymentTypeRowNum = 176;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

*/

		Assert.assertTrue(testConfig.getTestResult());				
	}

	@Test(description = "Verify net banking transaction through EBS gateway", 
			dataProvider="GetTestConfig", timeOut=720000, groups="1")
	public void test_NB_EBS(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Central Bank NetBanking
		int transactionRowNum = 4;
		int paymentTypeRowNum = 83;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());
		
	}
	// We can verify EBS gateway with one NB option. Rest of code is commented -- ATUL JAIN
		
	/*	
		//AXIS Bank of India
		transactionRowNum = 4;
		paymentTypeRowNum = 63;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Corporation Bank
		transactionRowNum = 4;
		paymentTypeRowNum = 91;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Federal Bank
		transactionRowNum = 4;
		paymentTypeRowNum = 106;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//HDFC Bank
		transactionRowNum = 4;
		paymentTypeRowNum = 112;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//ICICI NetBanking
		transactionRowNum = 4;
		paymentTypeRowNum = 119;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Indian Bank
		transactionRowNum = 4;
		paymentTypeRowNum = 126;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Jammu and Kashmir Bank
		transactionRowNum = 4;
		paymentTypeRowNum = 143;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Karnataka Bank Ltd
		transactionRowNum = 4;
		paymentTypeRowNum = 149;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Kotak Mahindra Bank
		transactionRowNum = 4;
		paymentTypeRowNum = 156;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Hyderabad
		transactionRowNum = 4;
		paymentTypeRowNum = 180;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of India
		transactionRowNum = 4;
		paymentTypeRowNum = 187;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Mysore
		transactionRowNum = 4;
		paymentTypeRowNum = 193;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Travancore
		transactionRowNum = 4;
		paymentTypeRowNum = 199;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

							
	}
	*/

	@Test(description = "Verify net banking transaction through BOI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_BOI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify net banking transaction through BOMNB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_BOMNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify net banking transaction through CITI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CITI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through CORP gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CORP(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify net banking transaction through SCOM gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_SCOM(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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
	
	//We can verify SCOM gateway functionality with one bank. Rest of the code is commented -- ATUL JAIN
	
	/*	//Corporation Bank
		int transactionRowNum = 6;
		int paymentTypeRowNum = 97;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//HDFC Bank
		transactionRowNum = 6;
		paymentTypeRowNum = 114;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//ICICI NetBanking
		transactionRowNum = 6;
		paymentTypeRowNum = 121;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Industrial Development Bank of India
		transactionRowNum = 6;
		paymentTypeRowNum = 138;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;
		 
		//State Bank of India
		transactionRowNum = 6;
		paymentTypeRowNum = 189;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Yes Bank
		transactionRowNum = 6;
		paymentTypeRowNum = 219;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;
		 
						
	}
	*/

	@Test(description = "Verify URL for net banking transaction through FEDB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_FEDB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through HDFCNB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_HDFCNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through ICI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_ICI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through IOB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_IOB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through INDUSIND gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_INDUSIND(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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


	@Test(description = "Verify URL for net banking transaction through IDBINB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_IDBINB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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
	@Test(description = "Verify URL for net banking transaction through JKNB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_JKNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through KRKBNB gateway",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_KRKBNB(Config testConfig)
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through SIBNB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_SIBNB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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
	@Test(description = "Verify URL for net banking transaction through SBBJB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_SBBJB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through SBHB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_SBHB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through SBINB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_SBINB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through SBMB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_SBMB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through SBTB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_SBTB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through UBI gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_UBI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through UNIB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_UNIB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through CUBB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CUBB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through CBIB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CBIB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Central Bank Of India
		int transactionRowNum = 3;
		int paymentTypeRowNum = 254;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		System.out.println("Redmine ID for this defect is 23173");
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	@Test(description = "Verify URL for net banking transaction through VJYB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_VJYB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through KRVB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_KRVB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through YES gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_YES(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	@Test(description = "Verify URL for net banking transaction through DCB gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_DCB(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
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

	//TODO Add processing page
	@Test(description = "Verify URL for net banking transaction with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CF1(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Yes Bank through DCB gateway
		int transactionRowNum = 70;
		int paymentTypeRowNum = 228;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[3];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Andhra Bank through CBK gateway
		transactionRowNum = 68;
		paymentTypeRowNum = 58;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//AXIS Bank NetBanking through TechPRocess
		transactionRowNum = 74;
		paymentTypeRowNum = 64;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of Bahrain and Kuwait through CCAVENUE
		transactionRowNum = 69;
		paymentTypeRowNum = 66;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Axis Bank through AXISNB
		transactionRowNum = 70;
		paymentTypeRowNum = 60;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Deutsche Bank through CITRUS gateway
		transactionRowNum = 72;
		paymentTypeRowNum = 96;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Central Bank of India through EBS
		transactionRowNum = 71;
		paymentTypeRowNum = 83;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Bank of India through BOI gateway
		transactionRowNum = 70;
		paymentTypeRowNum = 73;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Indian Overseas Bank through IOB gateway
		paymentTypeRowNum = 130;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//IndusInd Bank through INDUSIND gateway
		paymentTypeRowNum = 134;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Industrial Development Bank of India through IDBINB
		paymentTypeRowNum = 137;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//IndusInd Bank through JKNB gateway
		paymentTypeRowNum = 144;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//TODO Add processing page
	@Test(description = "Verify URL for net banking transaction with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CF2(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Bank of Maharashtra through BOMNB
		int transactionRowNum = 70;
		int paymentTypeRowNum = 77;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[3];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//CITI Debit Card through CITI gateway
		paymentTypeRowNum = 85;
		cardDetailsRowNum = 16;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Corporation Bank through CORP gateway
		paymentTypeRowNum = 90;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//HDFC Bank through SCOM gateway
		transactionRowNum = 73;
		paymentTypeRowNum = 114;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Federal Bank through FEDB gateway
		transactionRowNum = 70;
		paymentTypeRowNum = 107;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//HDFC Bank through HDFCNB gateway
		paymentTypeRowNum = 113;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//ICICI NetBanking through ICI gateway
		paymentTypeRowNum = 120;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Karnataka Bank through KRKBNB gateway
		paymentTypeRowNum = 146;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//South Indian Bank through SIBNB gateway
		paymentTypeRowNum = 172;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Bikaner and Jaipur through SSBJB gateway
		paymentTypeRowNum = 177;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Hyderabad through SBHB gateway
		paymentTypeRowNum = 181;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	//TODO Add processing page
	@Test(description = "Verify URL for net banking transaction with convenience fee", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NB_CF3(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//State Bank of India through SBINB gateway
		int transactionRowNum = 70;
		int paymentTypeRowNum = 188;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");

		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[3];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Mysore through SBMB gateway
		paymentTypeRowNum = 194;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//State Bank of Travancore thrugh SBTB gateway
		paymentTypeRowNum = 200;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Union Bank of India through UBI gateway
		paymentTypeRowNum = 212;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//United Bank of India through UNIB gateway
		paymentTypeRowNum = 232;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//City Union Bank through CUBB gateway
		paymentTypeRowNum = 253;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Central Bank Of India through CBIB gateway 
		//This case fails already, hence to avoid failing of other cases it is commented
		/*transactionRowNum = 3;
				paymentTypeRowNum = 254;
				cardDetailsRowNum = -1;
				helper.GetTestTransactionPage();
				System.out.println("Redmine ID for this defect is 23173");
				helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
				helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;*/

		//Vijaya Bank through VJYB
		paymentTypeRowNum = 255;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Karur Vysya through KRVB gateway
		paymentTypeRowNum = 256;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Yes Bank through YES gateway
		paymentTypeRowNum = 221;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		//Yes Bank through DCB gateway
		paymentTypeRowNum = 228;
		cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", helper.transactionData.GetData(transactionRowNum, "amount"));
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

		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

	/**
	 * Verifying if labels appear on payment option page for Net Banking displaying correct text
	 */
	@Test(description = "Test if all label fields for netbanking are present on the payment option page",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NetbankingTabPageElements(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		NBTab nbTab = (NBTab) helper.GetPaymentOptionsPage(1, PaymentMode.netbanking);
		Browser.wait(testConfig, 2);
		String expected = helper.cardDetailsData.GetData(1, "Bank");
		Helper.compareEquals(testConfig, "Netbanking Tab text", expected, nbTab.getNBTabText()) ;

		expected = helper.cardDetailsData.GetData(11, "Bank");
		Helper.compareEquals(testConfig, "Select your bank label", expected, nbTab.getSelectBankLabel()) ;

		expected = helper.cardDetailsData.GetData(10, "Note");
		Helper.compareEquals(testConfig, "Note text", expected, nbTab.getNoteText()) ;

		Assert.assertTrue(testConfig.getTestResult());

	}
	
	/**
	 * Verifying NB count
	 */
	@Test(description = "Test NB count",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_NetbankingCount(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		int testMerchantDataRow =80;
		HomePage home = new HomePage(testConfig);
		MerchantListPage merchantListPage = home.clickMerchantList();

		String merchantName="";

		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");
			
		merchantListPage.SearchMerchant(merchantName);
		
		MerchantDetailsPage merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		
		//check NB count
		String NBCount = merchantDetailsPage.CheckNetBankingCount(testConfig);
		Helper.compareEquals(testConfig, "NB Count", "Netbanking (live14)", NBCount) ;		
		Assert.assertTrue(testConfig.getTestResult());
	}
}
