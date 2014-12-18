package Test.NewMerchantPanel.TestNotifications;

import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsMerchantParamsPage;
import PageObject.NewMerchantPanel.SystemSettings.NotificationCenter.MerchantAlertType;
import PageObject.NewMerchantPanel.SystemSettings.NotificationCenter.ToggleState;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.TransactionType;
import Test.NewMerchantPanel.UserHelper;
import Utils.Browser;
import Utils.Config;
import Utils.EmailHelper;
import Utils.TestBase;

public class MerchantNotifications extends TestBase{

	
	// Product-2300
	@Test(description = "Verifying Alerts should get enabled for notifications when param for them are set from params tab.", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingAlertsShouldGetEnabledForNotificationsWhenParamAreSet(
			Config testConfig) {

		UserHelper userHelper = new UserHelper(testConfig);
		TransactionHelper transactionHelper = new TransactionHelper(testConfig,
				false);

		int TransactionDetailsRowNumber = 160;

		ArrayList<Integer> paramRows = new ArrayList<Integer>(Arrays.asList(23,
				24, 25, 26, 27, 28, 29, 30, 31));
		// Section 1 Steps 1-5
		// Login
		transactionHelper.DoLogin();
		transactionHelper.merchantListPage = transactionHelper.home//Step 1
				.clickMerchantList();
		ParamsMerchantParamsPage merchantParamsPage = transactionHelper.merchantListPage
				.getMerchantParamPage(TransactionDetailsRowNumber); //Step 2-3-4
		merchantParamsPage.addMultipleKeys(paramRows);				//Step 5
		
		// Section 2 Steps 1-2
		userHelper.merchantPanel = userHelper.merchantPanelHelper
				.doMerchantLogin(TransactionDetailsRowNumber); //Step 1
		userHelper.verifyAlertsAreOn(ToggleState.On, paramRows); //Step 2

		Assert.assertTrue(testConfig.getTestResult());
	}
	
	// Product-2269 
	@Test(description = "Verifying email field validation for each notifications", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingEmailFieldValidationsForEachNotification(
			Config testConfig) {

		UserHelper userHelper = new UserHelper(testConfig);		
		int TransactionDetailsRowNumber = 160;
		//Scenario 1 Step1-2
		userHelper.merchantPanel = userHelper.merchantPanelHelper
				.doMerchantLogin(TransactionDetailsRowNumber);
		//Scenario 2-3-4-5-6
		userHelper.doNotificationCenterEmailValidations();
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	// Product-2270
	@Test(description = "Verifying Multiple email id for notifications is allowed", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingMultipleEmailIdForNotificationIsAllowed(
			Config testConfig) {

		UserHelper userHelper = new UserHelper(testConfig);		
		int TransactionDetailsRowNumber = 160;
		//Scenario 1 Step1-2
		userHelper.merchantPanel = userHelper.merchantPanelHelper
				.doMerchantLogin(TransactionDetailsRowNumber);
		//Scenario 2-3-4-5-6
		userHelper.verifyMultipleEmailIdsAllowedForAlerts();
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	// Product-2278
	@Test(description = "Verifying Successful Transaction Alert", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingSuccessfulTransactionAlert(Config testConfig) {

		// Declarations
		int EmailIdRow = 1;
		UserHelper userHelper = new UserHelper(testConfig);
		int TransactionDetailsRowNumber = 160;
		int PaymentDetailsRowNumber = 366;
		ArrayList<Integer> emailIdRowsForAlerts = new ArrayList<>(
				Arrays.asList(EmailIdRow));
		ArrayList<MerchantAlertType> MerchantAlerts = new ArrayList<>(
				Arrays.asList(MerchantAlertType.SuccessfulTransactionAlert));
		TransactionHelper helper = new TransactionHelper(testConfig, true);

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
				SuggestedSearchPattern,emailIdRowsForAlerts);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Product-2279
	@Test(description = "Verifying Failed Transaction Alert", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingFailedTransactionAlert(Config testConfig) {

		// Declarations
		int EmailIdRow = 1;
		UserHelper userHelper = new UserHelper(testConfig);
		int TransactionDetailsRowNumber = 160;
		int PaymentDetailsRowNumber = 366;
		ArrayList<Integer> emailIdRowsForAlerts = new ArrayList<>(
				Arrays.asList(EmailIdRow));
		ArrayList<MerchantAlertType> MerchantAlerts = new ArrayList<>(
				Arrays.asList(MerchantAlertType.FailedTransactionAlert));
		TransactionHelper helper = new TransactionHelper(testConfig, true);

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
				SuggestedSearchPattern,emailIdRowsForAlerts);
		Assert.assertTrue(testConfig.getTestResult());
	}
		
	// Product-2283
	@Test(description = "Verifying UserCancelled Transaction Alert", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingUserCancelledTransactionAlert(Config testConfig) {

		// Declarations
		int EmailIdRow = 1;
		UserHelper userHelper = new UserHelper(testConfig);
		int TransactionDetailsRowNumber = 160;
		int PaymentDetailsRowNumber = 366;
		ArrayList<Integer> emailIdRowsForAlerts = new ArrayList<>(
				Arrays.asList(EmailIdRow));
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
				SuggestedSearchPattern,emailIdRowsForAlerts);
		Assert.assertTrue(testConfig.getTestResult());
	}
}
