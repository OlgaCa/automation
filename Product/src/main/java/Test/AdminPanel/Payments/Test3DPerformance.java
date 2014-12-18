package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;
import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.CCTab.CardType;
import PageObject.AdminPanel.Payments.PaymentOptions.ProcessingPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;

public class Test3DPerformance extends TestBase{

	@Test(description = "Measure the difference between PayU Now click and Test Response with 3dPopup",  dataProvider="GetTestConfig", timeOut=600000, groups="1", invocationCount = 100)
	public void testPerfOf3DPopup(Config testConfig)
	{
		//Before running the test, enforce 3d pop-up
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		CCTab ccTab = (CCTab) helper.GetPaymentOptionsPage(43, PaymentMode.creditcard);
		
		//Read the cookie on payment options page, to be sure that 3d popup will appear
		//String cookieValue = Browser.getCookieValue(testConfig, "abStat-threed_new_popup");
		//Helper.compareEquals(testConfig, "3d popup enable cookie", "threed_new_popup_0", cookieValue);
		
		helper.DoPayment(3, 1, -1, ExpectedResponsePage.TestResponsePage);
		
		Assert.assertTrue(testConfig.getTestResult());		
	}
}