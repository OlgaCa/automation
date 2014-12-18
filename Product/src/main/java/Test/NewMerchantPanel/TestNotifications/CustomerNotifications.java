package Test.NewMerchantPanel.TestNotifications;

import java.util.ArrayList;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.NewMerchantPanel.SystemSettings.NotificationCenter.CustomerAlertType;
import PageObject.NewMerchantPanel.SystemSettings.NotificationCenter.MerchantAlertType;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.TransactionType;
import Test.NewMerchantPanel.UserHelper;
import Utils.Browser;
import Utils.Config;
import Utils.TestBase;

public class CustomerNotifications extends TestBase {

	// Product-2273
	@Test(description = "Verifying Successful Transaction Alert", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingSuccessfulTransactionAlert(Config testConfig) {

		// Declarations
		UserHelper userHelper = new UserHelper(testConfig);
		int TransactionDetailsRowNumber = 160;
		int PaymentDetailsRowNumber = 366;
		ArrayList<CustomerAlertType> CustomerAlerts = new ArrayList<>(
				Arrays.asList(CustomerAlertType.SuccessfulTransactionAlert));
		
		ArrayList<MerchantAlertType> MerchantAlerts = new ArrayList<>(
				Arrays.asList(MerchantAlertType.SuccessfulTransactionAlert));
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		// Test Flow
		// Section 1 Steps 1-2
		userHelper.merchantPanel = userHelper.merchantPanelHelper
				.doMerchantLogin(TransactionDetailsRowNumber);
		// Step 2 Go To System Settings Page and Turn on Notifications for customer
		userHelper.setNotificationAlertsFor(CustomerAlerts);
		
		//Extra Step turn off notification for merchant
		userHelper.TurnOffMerchantAlertsFor(MerchantAlerts);
		
		// Step 3 Do a test transaction
		String SuggestedSearchPattern = helper
				.loginAdminPanelAndDoATransactionOfType(
						TransactionType.Successful,
						TransactionDetailsRowNumber, PaymentDetailsRowNumber);

		Browser.wait(testConfig, 5);
		// Step 4 check email and verify its contents
		userHelper.verifyCustomerEmailReportFor(TransactionType.Successful,
				SuggestedSearchPattern);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Product-2274
	@Test(description = "Verifying Failed Transaction Alert", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingFailedTransactionAlert(Config testConfig) {

		// Declarations
		UserHelper userHelper = new UserHelper(testConfig);
		int TransactionDetailsRowNumber = 160;
		int PaymentDetailsRowNumber = 366;
		ArrayList<CustomerAlertType> CustomerAlerts = new ArrayList<>(
				Arrays.asList(CustomerAlertType.FailedTransactionAlert));
		ArrayList<MerchantAlertType> MerchantAlerts = new ArrayList<>(
				Arrays.asList(MerchantAlertType.FailedTransactionAlert));
		TransactionHelper helper = new TransactionHelper(testConfig, true);

		// Test Flow
		// Section 1 Steps 1-2
		userHelper.merchantPanel = userHelper.merchantPanelHelper
				.doMerchantLogin(TransactionDetailsRowNumber);
		// Step 2 Go To System Settings Page and Turn on Notifications for
		userHelper.setNotificationAlertsFor(CustomerAlerts);
		//Extra Step turn off notification for merchant
		userHelper.TurnOffMerchantAlertsFor(MerchantAlerts);
		// Step 3 Do a test transaction
		String SuggestedSearchPattern = helper
				.loginAdminPanelAndDoATransactionOfType(
						TransactionType.Failed,
						TransactionDetailsRowNumber, PaymentDetailsRowNumber);

		Browser.wait(testConfig, 5);
		// Step 4 check email and verify its contents
		userHelper.verifyCustomerEmailReportFor(TransactionType.Failed,
				SuggestedSearchPattern);
		Assert.assertTrue(testConfig.getTestResult());
	}

}
