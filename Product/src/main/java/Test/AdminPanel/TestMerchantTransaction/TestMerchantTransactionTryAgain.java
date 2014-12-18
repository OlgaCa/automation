package Test.AdminPanel.TestMerchantTransaction;

import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.Response.ErrorResponsePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestMerchantTransactionTryAgain extends TestBase {
	
	@Test(description = "Verify if Try again page is showing expected behaviour", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Merchant_TryAgain(Config testConfig) 
	{
		
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		merHelper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum = 239;
		int cardDetailsRowNum = 19;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.tryAgainPage = (TryAgainPage) merHelper.DoTestMerchantTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
		merHelper.tryAgainPage.verifyRetryForPageElements();
		String expected = merHelper.transactionData.GetData(transactionRowNum, "Comments");
		Helper.compareEquals(testConfig, "automation merchant link", expected, merHelper.tryAgainPage.automationMerchantLinkForTransaction());
		
		Helper.compareEquals(testConfig, "Reason of Failure ", "Reason for failure: Bank denied transaction on the card.", merHelper.tryAgainPage.getReasonforFailure());
		Assert.assertTrue(testConfig.getTestResult());	
		
	}
	
	@Test(description = "Verify if Try again page is showing expected behaviour when Zero amount is entered on test merchant page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void testErrorResponsePageforNegativeAmount(Config testConfig) {	
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		merHelper.DoLogin();

		int transactionRowNum = 15;
		merHelper.GetTestMerchantTransactionPage();
		merHelper.trans.FillTransactionDetails(transactionRowNum);
		merHelper.errorResponsePage = (ErrorResponsePage) merHelper.trans.SubmitToGetErrorResponsePage();
		merHelper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		
		merHelper.errorResponsePage.verifyPageElements();
		
		String expected = merHelper.transactionData.GetData(transactionRowNum, "ErrorMessage");
		Helper.compareEquals(testConfig, "Reason of Failure ", expected, merHelper.errorResponsePage.getReasonforFailure());
		
		//expected = helper.transactionData.GetData(transactionRowNum, "FailureReason");
		Helper.compareEquals(testConfig, "Error message for invalid amount ", "Error. We are sorry we are unable to process your payment.", merHelper.errorResponsePage.getAmountErrorMessage());
		
		Assert.assertTrue(testConfig.getTestResult());
	}

}
