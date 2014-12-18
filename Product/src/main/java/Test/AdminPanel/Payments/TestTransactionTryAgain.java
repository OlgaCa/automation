package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.Response.ErrorResponsePage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestTransactionTryAgain extends TestBase {
	@Test(description = "Verify if Try again page is showing expected behaviour", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_TryAgain(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 23;
		int paymentTypeRowNum = 244;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();
		helper.tryAgainPage = (TryAgainPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
		helper.tryAgainPage.verifyPageElements();
		String expected = helper.transactionData.GetData(transactionRowNum, "Comments");
		Helper.compareEquals(testConfig, "automation merchant link", expected, helper.tryAgainPage.automationMerchantLinkForTransaction());
		
		Helper.compareEquals(testConfig, "Reason of Failure ", "Reason for failure: Error at the Bank Server end", helper.tryAgainPage.getReasonforFailure());
		CCTab ccTab = (CCTab) helper.tryAgainPage.clickTryAgainButton();
		Browser.wait(testConfig, 2);
		
		ccTab.FillCardDetails(cardDetailsRowNum);
		helper.tryAgainPage = (TryAgainPage) helper.payment.clickPayNowToGetTryAgainPage();
		helper.tryAgainPage.verifyPageElements();
		String expected1 = helper.transactionData.GetData(transactionRowNum, "Comments");
		Helper.compareEquals(testConfig, "automation merchant link", expected1, helper.tryAgainPage.automationMerchantLinkForTransaction());
		
		Helper.compareEquals(testConfig, "Reason of Failure ", "Reason for failure: Error at the Bank Server end", helper.tryAgainPage.getReasonforFailure());
		
		Assert.assertTrue(testConfig.getTestResult());	
	}
	
	@Test(description = "Verify if Try again page is showing expected behaviour when Zero amount is entered on test merchant page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void testErrorResponsePageforNegativeAmount(Config testConfig) {	
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int transactionRowNum = 15;
		helper.GetTestTransactionPage();
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.errorResponsePage = (ErrorResponsePage) helper.trans.SubmitToGetErrorResponsePage();
		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		
		helper.errorResponsePage.verifyPageElements();
		
		String expected = helper.transactionData.GetData(transactionRowNum, "ErrorMessage");
		Helper.compareEquals(testConfig, "Reason of Failure ", expected, helper.errorResponsePage.getReasonforFailure());
		
		//expected = helper.transactionData.GetData(transactionRowNum, "FailureReason");
		Helper.compareEquals(testConfig, "Error message for invalid amount ", "Error. We are sorry we are unable to process your payment.", helper.errorResponsePage.getAmountErrorMessage());
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify footer links on error response page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void testFooterLinksOnErrorResponsePage(Config testConfig) {	
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int transactionRowNum = 15;
		helper.GetTestTransactionPage();
		helper.trans.FillTransactionDetails(transactionRowNum);
		helper.errorResponsePage = (ErrorResponsePage) helper.trans.SubmitToGetErrorResponsePage();
		helper.footerLinkData = new TestDataReader(testConfig,"BankRedirect");

		String expected ="https://usa.visa.com/personal/security/vbv/index.jsp?ep=v_sym_/verified";
		helper.errorResponsePage.VerifyVerifiedByVisaLinkUrl(expected);
		

		//expected = helper.footerLinkData.GetData(77, "RedirectURL");
		expected = "http://www.mastercard.com/us/business/en/corporate/securecode/sc_popup.html?language=en";
		helper.errorResponsePage.VerifyClickMasterCardLinkUrl(expected);
		

		expected = "https://sealinfo.verisign.com/splash?form_file=fdf/splash.fdf&dn=www.payu.in&lang=en";
		helper.errorResponsePage.VerifyClickVeriSignSecuredLinkUrl(expected);
		

		//expected = helper.footerLinkData.GetData(79, "RedirectURL");
		expected = "https://seal.controlcase.com/index.php?page=showCert&cId=3877025869";
		helper.errorResponsePage.VerifyClickPciLink(expected);
		
		Assert.assertTrue(testConfig.getTestResult());
	}
}
