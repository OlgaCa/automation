package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.CODTab;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;


public class TestPostURI extends TestBase{
	@Test(description = "Verify non seamless transaction redirection to SURL", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_SuccessPostURI(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Axis Bank
		int transactionRowNum = 60;
		int paymentTypeRowNum = 247;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

		Assert.assertTrue(testConfig.getTestResult());				
	}

		@Test(description = "Verify Failed non seamless transaction redirection to FURL", 
				dataProvider="GetTestConfig", timeOut=600000, groups="1")
		public void test_FailPostURI(Config testConfig)
		{
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			helper.DoLogin();

			//Axis Bank
			int transactionRowNum = 61;
			int paymentTypeRowNum = 248;
			int cardDetailsRowNum = 17;
			helper.GetTestTransactionPage();
			helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
			helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

			Assert.assertTrue(testConfig.getTestResult());				
		}
	
	
		@Test(description = "Verify successful seamless transaction redirection to SURL", 
				dataProvider="GetTestConfig", timeOut=600000, groups="1")
		public void test_SuccessPostURI_seamless(Config testConfig)
		{
			TransactionHelper helper = new TransactionHelper(testConfig, true);
			helper.DoLogin();

			//Axis Bank
			int transactionRowNum = 60;
			int paymentTypeRowNum = 247;
			int cardDetailsRowNum = 1;
			helper.GetTestTransactionPage();
			helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
			helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

			Assert.assertTrue(testConfig.getTestResult());				
		}
	
			@Test(description = "Verify failed seamless transaction redirection to FURL", 
					dataProvider="GetTestConfig", timeOut=600000, groups="1")
			public void test_FailPostURI_seamless(Config testConfig)
			{
				TransactionHelper helper = new TransactionHelper(testConfig, true);
				helper.DoLogin();

				//Axis Bank
				int transactionRowNum = 61;
				int paymentTypeRowNum = 248;
				int cardDetailsRowNum = 17;
				helper.GetTestTransactionPage();
				helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
				helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

				Assert.assertTrue(testConfig.getTestResult());				
			}
			
			@Test(description = "Verify redirection to FURL in case of retry transaction", 
					dataProvider="GetTestConfig", timeOut=600000, groups="1")
			public void test_failurePostURI_retry(Config testConfig)
			{
				TransactionHelper helper = new TransactionHelper(testConfig, false);
				helper.DoLogin();

				//Axis Bank
				int transactionRowNum = 62;
				int paymentTypeRowNum = 251;
				int cardDetailsRowNum = 17;
										
				helper.GetTestTransactionPage();
				helper.tryAgainPage = (TryAgainPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
				CCTab ccTab = (CCTab) helper.tryAgainPage.clickTryAgainButton();
				Browser.wait(testConfig, 2);
				
				ccTab.FillCardDetails(cardDetailsRowNum);
			   
				helper.bankRedirectPage = (BankRedirectPage)  helper.payment.clickPayNowToGetBankRedirectPage();
				helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

				Assert.assertTrue(testConfig.getTestResult());				
			}
			@Test(description = "Verify redirection to SURL in case of retry transaction", 
					dataProvider="GetTestConfig", timeOut=600000, groups="1")
			public void test_successPostURI_retry(Config testConfig)
			{
				TransactionHelper helper = new TransactionHelper(testConfig, false);
				helper.DoLogin();

				//Axis Bank
				int transactionRowNum = 60;
				int paymentTypeRowNum = 247;
				int cardDetailsRowNum = 17;
				int validCardDetailsRowNum= 1;
						
				helper.GetTestTransactionPage();
				helper.tryAgainPage = (TryAgainPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
				CCTab ccTab = (CCTab) helper.tryAgainPage.clickTryAgainButton();
				Browser.wait(testConfig, 2);
				
				ccTab.FillCardDetails(validCardDetailsRowNum);
			   
				helper.bankRedirectPage = (BankRedirectPage)  helper.payment.clickPayNowToGetBankRedirectPage();
				helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

				Assert.assertTrue(testConfig.getTestResult());				
			}
			
			@Test(description = "Verify redirection to FURL in case of user cancelled transaction", 
					dataProvider="GetTestConfig", timeOut=600000, groups="1")
			public void test_FaliurePostURI_Cancel(Config testConfig)
			{
				TransactionHelper helper = new TransactionHelper(testConfig, false);
				helper.DoLogin();

				//Axis Bank
				int transactionRowNum = 60;
				int paymentTypeRowNum = 249;
				int cardDetailsRowNum = 17;
										
				helper.GetTestTransactionPage();
				helper.tryAgainPage = (TryAgainPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
				helper.bankRedirectPage = (BankRedirectPage) helper.tryAgainPage.ClickCancel();
				Browser.wait(testConfig, 2);
			
				helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;

				Assert.assertTrue(testConfig.getTestResult());				
			}
			
			@Test(description = "Verify redirection to COD tab in case of retry transaction", 
					dataProvider="GetTestConfig", timeOut=600000, groups="1")
			public void test_CODTab(Config testConfig)
			{
				TransactionHelper helper = new TransactionHelper(testConfig, false);
				helper.DoLogin();

				//Axis Bank
				int transactionRowNum = 60;
				int paymentTypeRowNum = 249;
				int cardDetailsRowNum = 17;
										
				helper.GetTestTransactionPage();
				helper.tryAgainPage = (TryAgainPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
				helper.payment= (PaymentOptionsPage)helper.tryAgainPage.ClickCOD();
				CODTab codTab = (CODTab) helper.GetPaymentOptionsPage(1, PaymentMode.cod);

				//adding the wait since sometimes page is slow to load
				Browser.wait(testConfig, 2);
				
				// Verify COD page details
				String expected = helper.cardDetailsData.GetData(1, "COD");
				Helper.compareEquals(testConfig, "COD Tab text", expected, codTab.getCODTabText());
				
				Assert.assertTrue(testConfig.getTestResult());				
			}	
			
			@Test(description = "Verify redirection to CODURL in case of retry transaction", 
					dataProvider="GetTestConfig", timeOut=600000, groups="1")
			public void test_CODPostURI(Config testConfig)
			{
				TransactionHelper helper = new TransactionHelper(testConfig, false);
				helper.DoLogin();

				//Axis Bank
				int transactionRowNum = 62;
				int paymentTypeRowNum = 250;
				int cardDetailsRowNum = 17;
										
				helper.GetTestTransactionPage();
				helper.tryAgainPage = (TryAgainPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
				helper.bankRedirectPage= (BankRedirectPage)helper.tryAgainPage.ClickCODBank();
				helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum) ;
				
				Assert.assertTrue(testConfig.getTestResult());				
			}		
}
