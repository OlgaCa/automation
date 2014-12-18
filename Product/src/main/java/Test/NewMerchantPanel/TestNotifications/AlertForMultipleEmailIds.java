package Test.NewMerchantPanel.TestNotifications;

import java.util.ArrayList;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.NewMerchantPanel.SystemSettings.NotificationCenter.MerchantAlertType;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.TransactionType;
import Test.NewMerchantPanel.UserHelper;
import Utils.Browser;
import Utils.Config;
import Utils.TestBase;

public class AlertForMultipleEmailIds extends TestBase {

	// Product-2296
	@Test(description = "Verifying UserCancelled Transaction Alert", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingUserCancelledTransactionAlert(Config testConfig) {

		// Declarations
		UserHelper userHelper = new UserHelper(testConfig);
		int TransactionDetailsRowNumber = 160;
		int PaymentDetailsRowNumber = 366;
		ArrayList<Integer> emailIdRowsForAlerts = new ArrayList<>(
				Arrays.asList(1,3));
		ArrayList<MerchantAlertType> MerchantAlerts = new ArrayList<>(
				Arrays.asList(MerchantAlertType.UserCancelledTransactionAlert));
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		// Test Flow
		// Section 1 Steps 1-2
		userHelper.merchantPanel = userHelper.merchantPanelHelper
				.doMerchantLogin(TransactionDetailsRowNumber);
		// Step 2 Go To System Settings Page and Turn on Notifications for
		// provided email id
		userHelper.setNotificationAlertsFor(MerchantAlerts,
				emailIdRowsForAlerts);
		// Step 3 Do a test transaction
		String SuggestedSearchPattern = helper
				.loginAdminPanelAndDoATransactionOfType(
						TransactionType.UserCancelled,
						TransactionDetailsRowNumber, PaymentDetailsRowNumber);

		Browser.wait(testConfig, 5);
		// Step 4 check email and verify its contents
		userHelper.verifyMerchantEmailReportFor(TransactionType.UserCancelled,
				SuggestedSearchPattern, emailIdRowsForAlerts);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	// Product-2292
	@Test(description = "Verifying Failed Transaction Alert", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingFailedTransactionAlert(Config testConfig) {

		// Declarations
		UserHelper userHelper = new UserHelper(testConfig);
		int TransactionDetailsRowNumber = 160;
		int PaymentDetailsRowNumber = 366;
		ArrayList<Integer> emailIdRowsForAlerts = new ArrayList<>(
				Arrays.asList(1,3));
		ArrayList<MerchantAlertType> MerchantAlerts = new ArrayList<>(
				Arrays.asList(MerchantAlertType.FailedTransactionAlert));
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		// Test Flow
		// Section 1 Steps 1-2
		userHelper.merchantPanel = userHelper.merchantPanelHelper
				.doMerchantLogin(TransactionDetailsRowNumber);
		// Step 2 Go To System Settings Page and Turn on Notifications for
		// provided email id
		userHelper.setNotificationAlertsFor(MerchantAlerts,
				emailIdRowsForAlerts);
		// Step 3 Do a test transaction
		String SuggestedSearchPattern = helper
				.loginAdminPanelAndDoATransactionOfType(
						TransactionType.Failed,
						TransactionDetailsRowNumber, PaymentDetailsRowNumber);

		Browser.wait(testConfig, 5);
		// Step 4 check email and verify its contents
		userHelper.verifyMerchantEmailReportFor(TransactionType.Failed,
				SuggestedSearchPattern, emailIdRowsForAlerts);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	// Product-2290
	@Test(description = "Verifying Success Transaction Alert", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingSuccessTransactionAlert(Config testConfig) {

		// Declarations
		UserHelper userHelper = new UserHelper(testConfig);
		int TransactionDetailsRowNumber = 160;
		int PaymentDetailsRowNumber = 366;
		ArrayList<Integer> emailIdRowsForAlerts = new ArrayList<>(
				Arrays.asList(1,3));
		ArrayList<MerchantAlertType> MerchantAlerts = new ArrayList<>(
				Arrays.asList(MerchantAlertType.SuccessfulTransactionAlert));
		TransactionHelper helper = new TransactionHelper(testConfig, false);

		// Test Flow
		// Section 1 Steps 1-2
		userHelper.merchantPanel = userHelper.merchantPanelHelper
				.doMerchantLogin(TransactionDetailsRowNumber);
		// Step 2 Go To System Settings Page and Turn on Notifications for
		// provided email id
		userHelper.setNotificationAlertsFor(MerchantAlerts,
				emailIdRowsForAlerts);
		// Step 3 Do a test transaction
		String SuggestedSearchPattern = helper
				.loginAdminPanelAndDoATransactionOfType(
						TransactionType.Successful,
						TransactionDetailsRowNumber, PaymentDetailsRowNumber);

		Browser.wait(testConfig, 5);
		// Step 4 check email and verify its contents
		userHelper.verifyMerchantEmailReportFor(TransactionType.Successful,
				SuggestedSearchPattern, emailIdRowsForAlerts);
		Assert.assertTrue(testConfig.getTestResult());
	}
}
