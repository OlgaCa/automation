package Test.MerchantPanel.Transactions;

import java.awt.AWTException;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.Response.NewResponsePage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import PageObject.MerchantPanel.Offers.OfferListPage;
import PageObject.MerchantPanel.Transactions.CODVerifyPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Test.MerchantPanel.Offers.OffersHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;

public class TestDollarGateway extends TestBase{
	
	private  DashboardPage dashBoard;
	
	@Test(description = "Verify dasboard data for dollar", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyDashboardData(Config testConfig) {
		
		int testRow = 47; 
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(testRow);
		 String string = null;
		 dashboardHelper.compareAmount2Sides(testConfig,dashBoard,"Checking "+ string +"'s amount left and right side");
	}

	
	
	
	@Test(description = "Verify transaction detail page for for all transactions for dollar merchant", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyTransactionDetailForAllTransactions(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int transactionRowNum = 47; 
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		 String string = null;
		helper.GetTestTransactionPage();
  
		//TransactionPage trans = new TransactionPage(testConfig);
		helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.verifyDollarSignOnTransactionDetailPage(testConfig, dashBoard, "check dollar sign"+ string +"'s amount left and right side");
		Assert.assertTrue(testConfig.getTestResult());
	
	}
	
	@Test(description = "Verify transaction detail page for refund transaction for dollar merchant", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyTransactionDetailForRefundTransaction(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int transactionRowNum = 47; 
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		 String string = null;
		helper.GetTestTransactionPage();
  
		//TransactionPage trans = new TransactionPage(testConfig);
		helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickRequestRefund();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.verifyDollarSignOnTransactionDetailPage(testConfig, dashBoard, "check dollar sign"+ string +"'s amount left and right side");
		Assert.assertTrue(testConfig.getTestResult());
	
	}
	
	@Test(description = "Verify transaction detail page for refund/cancel transaction for dollar merchant", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyTransactionDetailForRefundandCancelTransaction(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int transactionRowNum = 47; 
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		 String string = null;
		helper.GetTestTransactionPage();
  
		//TransactionPage trans = new TransactionPage(testConfig);
		helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickCancelRefund();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.verifyDollarSignOnTransactionDetailPage(testConfig, dashBoard, "check dollar sign"+ string +"'s amount left and right side");
		Assert.assertTrue(testConfig.getTestResult());
	
	}
	
	@Test(description = "Verify transaction detail page for transaction on view request page for dollar merchant", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyTransactionDetailForViewRequestTransactionPopUp(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int transactionRowNum = 47; 
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		 String string = null;
		helper.GetTestTransactionPage();
  
	//	TransactionPage trans = new TransactionPage(testConfig);
		helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		//String expectedTxnId = testConfig.getRunTimeProperty("transactionId");
	    //Browser.wait(testConfig, 10);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		//dashBoard.ClickViewAll();
		
		//dashBoard.ClickViewRequest();
		
		//String transactionId = merchantTransaction.firstTransaction();
		dashboardHelper.requestPage = dashBoard.ClickViewRequest();
		//MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = dashboardHelper.requestPage.clickFirstTrans();
		/*String transactionId = merchantTransaction.firstTransaction();
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(expectedTxnId);*/
		dashboardHelper.transactionsPage.verifyDollarSignOnTransactionDetailPage(testConfig, dashBoard, "check dollar sign"+ string +"'s amount left and right side");
		Assert.assertTrue(testConfig.getTestResult());
	
	}

	@Test(description="Verify that amount and transaction id displayed on Payment Option Page for dollar gateway matches the one given in Test Transaction Page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_AmountAndTranasctionIdOnPaymentOptionPageForDollar(Config testConfig) 
	{
		int transactionRowNum = 47;

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		helper.GetPaymentOptionsPage(transactionRowNum,PaymentMode.creditcard);

		String expectedTxnId = testConfig.getRunTimeProperty("transactionId");
		String expectedAmount = helper.transactionData.GetData(transactionRowNum, "amount");
		String expectedPaymentTypeLabel = "Choose a payment method";

		Helper.compareEquals(testConfig, "Choose PaymentType Label", expectedPaymentTypeLabel, helper.payment.getChoosePaymentTypeLabel());
		Helper.compareEquals(testConfig, "Transaction Amount", "$ " + expectedAmount, helper.payment.getAmount());
		Helper.compareEquals(testConfig, "Transaction Id", "ID: " + expectedTxnId, helper.payment.getTransactionId());

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify if Try again page is showing expected behaviour for dollar merchant with dollar sign on payment page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_TryAgainWithDollarSignOnPaymentPage(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 47;
		int paymentTypeRowNum = 329;
		int cardDetailsRowNum = 19;
		
		
		
		helper.GetTestTransactionPage();
		helper.tryAgainPage = (TryAgainPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
		helper.tryAgainPage.verifyPageElements();
		String expected = helper.transactionData.GetData(transactionRowNum, "Comments");
		Helper.compareEquals(testConfig, "automation merchant link", expected, helper.tryAgainPage.automationMerchantLinkForTransaction());
		
		Helper.compareEquals(testConfig, "Reason of Failure ", "Reason for failure: International cards are not accepted by the merchant", helper.tryAgainPage.getReasonforFailure());
		
		
		CCTab ccTab = (CCTab) helper.tryAgainPage.clickTryAgainButton();
		Browser.wait(testConfig, 2);
		 String expectedAmount = helper.transactionData.GetData(transactionRowNum, "amount");
		 Helper.compareEquals(testConfig, "Transaction Amount", "$ " + expectedAmount, helper.payment.getAmount());
		
		ccTab.FillCardDetails(cardDetailsRowNum);
		helper.tryAgainPage = (TryAgainPage) helper.payment.clickPayNowToGetTryAgainPage();
		helper.tryAgainPage.verifyPageElements();
		String expected1 = helper.transactionData.GetData(transactionRowNum, "Comments");
		Helper.compareEquals(testConfig, "automation merchant link", expected1, helper.tryAgainPage.automationMerchantLinkForTransaction());
		
		Helper.compareEquals(testConfig, "Reason of Failure ", "Reason for failure: International cards are not accepted by the merchant", helper.tryAgainPage.getReasonforFailure());
		
		ccTab = (CCTab) helper.tryAgainPage.clickTryAgainButton();
		Browser.wait(testConfig, 2);
		 Helper.compareEquals(testConfig, "Transaction Amount", "$ " + expectedAmount, helper.payment.getAmount());
			
		ccTab.FillCardDetails(cardDetailsRowNum);
		helper.testResponse = (TestResponsePage) helper.payment.clickPayNow();
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());	
	}
	


	@Test(description="Verify COD Verify transaction without Zip dial for dollar gateway",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_COD_CODVerifyTransactionForDollar(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Do in-progress COD without Zipdial 
		int transactionRowNum = 47;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;
        String string = null;
		helper.GetTestTransactionPage();
		helper.testResponse =(TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, codRowNum);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);		
		dashBoard.ClickCODVerify();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.verifyDollarSignOnTransactionDetailPage(testConfig, dashBoard, "check dollar sign"+ string +"'s amount left and right side");
		Assert.assertTrue(testConfig.getTestResult());
	
		
	}

	@Test(description="Verify COD Cancel transaction without Zip dial for dollar gateway",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_COD_CODCancelTransactionForDollar(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Do in-progress COD without Zipdial 
		int transactionRowNum = 47;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;
        String string = null;
		helper.GetTestTransactionPage();
		helper.testResponse =(TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, codRowNum);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);		
		dashBoard.ClickCODCancel();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.verifyDollarSignOnTransactionDetailPage(testConfig, dashBoard, "check dollar sign"+ string +"'s amount left and right side");
		Assert.assertTrue(testConfig.getTestResult());
	
		
	}
	 
		@Test(description = "Verify email invoice transaction via URL for dollar gateway", 
				dataProvider="GetTestConfig", timeOut=600000, groups="1")
		public void test_VerifyEmailInvoiceTransactionandVerifiesUSDforDollar(Config testConfig) {
			String baseURL = testConfig.getRunTimeProperty("MerchantPanelHome");
			int transactionRowNum = 47;
			int cardDetailsRowNum = 1;
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
			// login as merchant and fill email invoice form
			 dashboardHelper.loginAndFillEmailInvoice(transactionRowNum,"invoice");
			 
			 Browser.wait(testConfig, 3);
			 // verify close button present on pop up
			 dashboardHelper.invoiceTransactionConfirmationPage.verifyCloseButtonPresent();
			
			 // copy link in pop up
			String url2 = dashboardHelper.invoiceTransactionConfirmationPage.retInvoiceURL();
	      	Browser.navigateToURL(testConfig, url2);
	      	
			NewResponsePage newResponse = helper.makePaymentViaCreditCard(cardDetailsRowNum);
			// verify for success transaction
			newResponse.verifyPageText(true);
			Assert.assertTrue(testConfig.getTestResult());
				}
    
		@Test(description = "Verify offer creation", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void test_VerifyCreateOffer(Config testConfig)
				 {
			
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			int offerRow = 1;
			int merchantLoginRow = 47;
			int transactionRowNum = 47;
			int paymentTypeRowNum = 3;
			int cardDetailsRowNum = 1;
			Boolean isExpired = false;
			OffersHelper oHelper = new OffersHelper(testConfig);

			// login and create offer
			OfferListPage olPage = oHelper.doLoginAndCreateOffer(offerRow,
					merchantLoginRow, isExpired);
			String offerTitle = testConfig.getRunTimeProperty("Offer Title");
			oHelper.setOfferInfo(offerTitle);
			helper.DoLogin();
			// make transaction with created offer
			helper.GetTestTransactionPage();
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
					transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
					ExpectedResponsePage.TestResponsePage);
			Assert.assertTrue(testConfig.getTestResult());

		}
		@Test(description = "Verify offer creation", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
		public void test_VerifyDiscountInDollarOnOfferList(Config testConfig)
				 {
			
			
			
			int merchantLoginRow = 47;
			OffersHelper oHelper = new OffersHelper(testConfig);
			DashboardHelper dHelper = new DashboardHelper(testConfig);
			// merchant login
			DashboardPage dPage = (DashboardPage) dHelper.doMerchantLogin(merchantLoginRow);

			OfferListPage olPage = oHelper.doLoginAndViewOfferList(merchantLoginRow);
			  Helper.compareEquals(testConfig, "verify dollar instead of rupee", "dollar",  olPage.discount().substring( olPage.discount().lastIndexOf(' ')+1));
			  Assert.assertTrue(testConfig.getTestResult());
		
		
		}
		
		@Test(description="Verify COD settled transaction for dollar gateway",
				dataProvider="GetTestConfig", timeOut=600000, groups="1")
		public void test_COD_CODSettledTransactionForDollar(Config testConfig) 
		{
			
		
			int transactionRowNum = 47;
			int paymentTypeRowNum = 233;
			int cardDetailsRowNum = -1;
			int codRowNum = 1;
			DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
			
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			
			helper.DoLogin();
			helper.GetTestTransactionPage();
			helper.testResponse =(TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.TestResponsePage);
	  
			DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
			dashBoard.ClickCodVerify();
			dashBoard.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));  
			dashBoard.selectTransactionVerifyAction();
			dashBoard.ClickCodSettled();
			Browser.wait(testConfig, 60);
			 
			MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
			dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
			 String string = null;
			dashboardHelper.transactionsPage.verifyDollarSignOnTransactionDetailPage(testConfig, dashBoard, "check dollar sign"+ string +"'s amount left and right side");
			Assert.assertTrue(testConfig.getTestResult());
		
			
		}
	
}