package Test.MerchantPanel.Payments;


import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

import PageObject.AdminPanel.Payments.Response.NewResponsePage;
import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import PageObject.MerchantPanel.Transactions.InvoiceTransactionConfirmationPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestBase;

public class TestTransactionEmail extends TestBase{

	public TransactionPage trans;
			
	
	
	/**
	 * @author jyoti.patial
	 * Verify the Email Invoice transaction via URL
	 */
	@Test(description = "Verify email invoice transaction via URL", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyEmailInvoiceTransactionSuccess(Config testConfig) {
		String baseURL = testConfig.getRunTimeProperty("MerchantPanelHome");
		
				//baseURL = baseURL.concat(url1[i]);
		int transactionRowNum = 12;
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
		//dashboardHelper.invoiceTransactionConfirmationPage.CopyLink();
		
		NewResponsePage newResponse = helper.makePaymentViaCreditCard(cardDetailsRowNum);
		// verify for success transaction
		newResponse.verifyPageText(true);
		Assert.assertTrue(testConfig.getTestResult());
			}

	
	@Test(description = "Verify transaction failure response by entering invalid card details", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	//TODO: refer bug 18827
	public void test_VerifyTransactionFailure(Config testConfig) {
		int transactionRowNum = 75;
		int cardDetailsRowNum = 17;

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		// login as merchant and fill email invoice form
		 dashboardHelper.loginAndFillEmailInvoice(transactionRowNum,"invoice");
		 Browser.wait(testConfig, 3);
		 // verify close button present on pop up
		 dashboardHelper.invoiceTransactionConfirmationPage.verifyCloseButtonPresent();
		
		 // copy link in pop up
		String url = dashboardHelper.invoiceTransactionConfirmationPage.retInvoiceURL();
      	Browser.navigateToURL(testConfig, url);
		//dashboardHelper.invoiceTransactionConfirmationPage.CopyLink();
		
		NewResponsePage newResponse = helper.makePaymentViaCreditCard(cardDetailsRowNum);
		// verify for failure transaction
		newResponse.verifyPageText(false);
		Assert.assertTrue(testConfig.getTestResult());
	}	
	/**
	 * @author jyoti.patial
	 * Verify the Email Invoice transaction via Copy Link. Not. this transaction fails sometimes due to gmail delays.
	 */
	@Test(description = "Verify email invoice transaction via Send Email", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyEmailInvoiceTransactionSendEmail(Config testConfig) {
		int transactionRowNum = 12;
		int cardDetailsRowNum = 1;

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		// login as merchant and fill email invoice form and submit
		dashboardHelper.loginAndFillEmailInvoice(transactionRowNum,"invoice");

		dashboardHelper.invoiceTransactionConfirmationPage.SendEmail();
		// navigate to gmail and click link
		helper.payment = dashboardHelper.invoiceTransactionConfirmationPage.gmailLoginAndClickPaymentLink();
		//make payment via credit card
		NewResponsePage newResponse = helper.makePaymentViaCreditCard(cardDetailsRowNum);
		newResponse.verifyPageText(true);
		Assert.assertTrue(testConfig.getTestResult());
	}	


	/*
	 
	 * 
	 * @author jyoti.patial
	 * Verify the error messages when invalid inputs are entered
	 */
	@Test(description = "Verify the error messages when blank inputs are entered", dataProvider = "GetUrlTestConfig", timeOut=600000, groups = "1")
	public void test_ErrorMessages(Config testConfig, String[] testType) {
		//blank input
		int transactionRowNum = 24;
        int errorRowNum = 30;
        if(testType!=null)
        	for(int i =0; i<testType.length;i++)
        	{
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		// login as merchant and fill email invoice form with blank input
		dashboardHelper.loginAndFillEmailInvoice(transactionRowNum,testType[i]);
		Browser.wait(testConfig, 2);
		//verify field level error messages
		dashboardHelper.emailInvoicePage.verifyErrorMessages(errorRowNum);

		// Special Characters
		transactionRowNum = 25;
		errorRowNum = 28;
		// fill email invoice form with special characters
		dashboardHelper.emailInvoicePage.fillInvoiceForm(transactionRowNum);
		   
		// click confirm
		dashboardHelper.emailInvoicePage.clickConfirmButton();
		Browser.wait(testConfig, 2);

	    //verify field level error messages
		dashboardHelper.emailInvoicePage.verifyErrorMessages(errorRowNum);
	
		// Minimum Input
		transactionRowNum = 27;
	    errorRowNum = 31;
	   // fill email invoice form with minimum input
	 		dashboardHelper.emailInvoicePage.fillInvoiceForm(transactionRowNum);
	 		   
	 	// click confirm
	 	dashboardHelper.emailInvoicePage.clickConfirmButton();	
		Browser.wait(testConfig, 2);

		// verify field level error messages
		dashboardHelper.emailInvoicePage.verifyErrorMessages(errorRowNum);
        	}
		Assert.assertTrue(testConfig.getTestResult());
        	
	}

	
	@Test(description = "Verify the tarnsactions get unsuccessful when zero anount is entered", dataProvider = "GetUrlTestConfig", timeOut=600000, groups = "1")
	public void test_ZeroAmountForInvoice(Config testConfig,String[] testType) {

		int transactionRowNum = 26;
        int errorRowNum = 30;
		 if(testType!=null)
	        	for(int i =0; i<testType.length;i++)
	        	{
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		// login as merchant and fill 0 as amount
		dashboardHelper.loginAndFillEmailInvoice(transactionRowNum,testType[i]);
		

		dashboardHelper.emailInvoicePage.IsAmountDisplayed();
	        	
		// verify field level error messages
			dashboardHelper.emailInvoicePage.verifyAmountErrorMessage(errorRowNum);
	        	}
			Assert.assertTrue(testConfig.getTestResult());
	        
	        	
	}
	
	@Test(description = "Verify the transactions get unsuccessful when negative anount is entered", dataProvider = "GetUrlTestConfig", timeOut=600000, groups = "1")
	public void test_NegativeAmountAndErorCasesForInvoice(Config testConfig,String[] testType) {

		;
		 if(testType!=null)
	        	for(int i =0; i<testType.length;i++)
	        	{
	        		int transactionRowNum = 16;
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		// login as merchant and fill -1 as amount
		dashboardHelper.loginAndFillEmailInvoice(transactionRowNum,testType[i]);
		
		// verify field level error message
		Helper.compareEquals(testConfig, "amount error message", "Please enter a valid amount", dashboardHelper.emailInvoicePage.getAmountErrorMessage());

		//special characters in transaction id
		transactionRowNum=27;
		testConfig.putRunTimeProperty("invalidTxnId", "^%&%^&");
		dashboardHelper.emailInvoicePage.fillInvoiceForm(transactionRowNum);
		
		// verify field level error messages
		Helper.compareEquals(testConfig, "txnId error message", "Please enter a valid transaction id", dashboardHelper.emailInvoicePage.getTxnIDErrorMessage());
		
		//number in name case
		if(testType[i].equals("invoice"))
		{
		testConfig.putRunTimeProperty("invalidName", "123456");
        dashboardHelper.emailInvoicePage.fillInvoiceForm(transactionRowNum);
		
		// verify field level error messages
		Helper.compareEquals(testConfig, "name error message", "Please enter a valid name", dashboardHelper.emailInvoicePage.getNameErrorMessage());
		} 
		}
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify that transactions are successful when user fills only mandatory fields", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_NonMandatoryFieldsForEmailInvoice(Config testConfig) {
		int transactionRowNum = 29;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		
		// fill only mandatory fields in invoice form
		dashboardHelper.loginAndFillEmailInvoice(transactionRowNum,"invoice");
		
		// verify whether pop up with link appears
		WebElement copyLink = Element.getPageElement(testConfig, How.id,"crDivSpan");

		Element.verifyElementPresent(testConfig, copyLink, "copy link");
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify that transactions are unsuccessful when user fills duplicate transaction id", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_DuplicateTransactionIdForEmailInvoice(Config testConfig) {
		int transactionRowNum = 29;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.loginAndFillEmailInvoice(transactionRowNum,"invoice");
		

		String dupTxId = testConfig.getRunTimeProperty("txnId");
		// verify first transaction generates invoice link
		WebElement copyLink = Element.getPageElement(testConfig, How.id,
				"crDivSpan");
		Element.verifyElementPresent(testConfig, copyLink, "copy link");
		// click close button
		Element.click(testConfig, Element.getPageElement(testConfig, How.xPath, "//div[@id='close_but']/img"),"Close");
		// repeat email invoice with duplicate TxId
		dashboardHelper.emailInvoicePage.fillInvoiceForm(transactionRowNum);
		// enter duplicate Transaction Id
		Element.enterData(testConfig, Element.getPageElement(testConfig, How.id, "txnid"), dupTxId, "Enter Transaction Id");
		//click confirm
		dashboardHelper.emailInvoicePage.clickConfirmButton();
		
		if(Element.IsElementDisplayed(testConfig,Element.getPageElement(testConfig, How.css, "h1")))
		{
			dashboardHelper.invoiceTransactionConfirmationPage = new InvoiceTransactionConfirmationPage(testConfig);
		}

		Assert.assertTrue(dashboardHelper.invoiceTransactionConfirmationPage.errorMessage().equals("Error: Invoice for this transaction id already exists."));

}
}