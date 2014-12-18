package Test.NewMerchantPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.MerchantPanel.Home.PayUHomePage;
import PageObject.NewMerchantPanel.ManageUsers.CreateNewUsersPage;
import PageObject.NewMerchantPanel.ManageUsers.ManageUsersProfilesAndRoles;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage.subMenu;

import Test.AdminPanel.Payments.TransactionHelper;
import Test.NewMerchantPanel.UserHelper;
import Test.NewMerchantPanel.UserHelper.modificationtype;
import Test.NewMerchantPanel.UserHelper.newMerchantUser;
import Test.NewMerchantPanel.UserHelper.newUserFields;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class MyAccount extends TestBase {
	
	//Test Case ID: Product-2209
	@Test(description = "Verifying an error message on creating a user with username already existing.", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingAnErrorMessageOnCreatingAUserWithUsernameAlreadyExisting(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		String ExpectedErrorMessage = new TestDataReader(testConfig, "StringValues").GetData(3, "Value");
		
		//Section Steps 1-6
		//Login and create a new user using super user
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>();
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,false);
		
		//Section 2 Steps 1-2
		//verify duplicate user name error functionality
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		accountsHelper.verifyDuplicateUsernameError(ExpectedErrorMessage);
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	//Test Case ID: Product-2208
	@Test(description = "Verifying field validations for Password and Confirm Password field on' Create New user' page.", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingFieldValidationsForPasswordAndConfirmPasswordFieldOnCreateNewUserPage(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		String ExpectedErrorMessage = new TestDataReader(testConfig, "StringValues").GetData(2, "Value");
		
		//Section 1-4
		//Login and Do validations
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		accountsHelper.verifyFieldValidations(newUserFields.Password ,ExpectedErrorMessage);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	//Test Case ID: Product-2207
	@Test(description = "Verifying field validations for Email field on' Create New user' page.", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingFieldValidationsForEmailFieldOnCreateNewUserPage(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		String ExpectedErrorMessage = "";
		//Section 1-3 
		//Login and Do validations
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		accountsHelper.verifyFieldValidations(newUserFields.Email ,ExpectedErrorMessage);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	//Test Case ID: Product-2206
	@Test(description = "Verifying field validations for Name field on' Create New user' page.", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingFieldValidationsForNameFieldOnCreateNewUserPage(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		String ExpectedErrorMessage = "";
		//Section 1-3 
		//Login and Do validations
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		accountsHelper.verifyFieldValidations(newUserFields.Name ,ExpectedErrorMessage);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	//Test Case ID: Product-2205
	@Test(description = "Verifying field validations for Username field on' Create New user' page.", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingFieldValidationsForUsernameFieldOnCreateNewUserPage(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		String ExpectedErrorMessage = new TestDataReader(testConfig, "StringValues").GetData(4, "Value");;
		//Section 1-3 
		//Login and Do validations
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		accountsHelper.verifyFieldValidations(newUserFields.Username ,ExpectedErrorMessage);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product-2200
	@Test(description = "Verify user having super rights is able to Change Passwords for other users", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyUserHavingSuperRightsIsAbleToChangePasswordsForOtherUsers(
			Config testConfig) {

		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		
		//Section 1 Steps 1-3
		//Create a new user with super rights
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(
				Arrays.asList("super"));
		newMerchantUser userWithSuperAccess =	accountsHelper.createNewUser(
													AccessAndRolesToBeGranted, false);
		
		//Section 2 Steps 1-3
		//Section 3 Step 1
		//login with the new user with super rights
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doMerchantLogin(userWithSuperAccess.UserDetails);
		AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("Billings"));
		newMerchantUser UserCreatedBySuperAccess =	
					accountsHelper.createNewUser(AccessAndRolesToBeGranted, true);
		
		//Section 4 Steps 1-6
		//login with the super right user and navigate to manage users page
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.
									doMerchantLogin(userWithSuperAccess.UserDetails);
		UserCreatedBySuperAccess.changePassword(Helper.generateRandomAlphaNumericString(20));
		accountsHelper.modifyExistingUser(UserCreatedBySuperAccess,modificationtype.Password);
		
		//Section 5 Step 1-2
		accountsHelper.verifyUIforMerchant(UserCreatedBySuperAccess);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product-2199
	@Test(description = "Verify super user is able to change password for other users", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifySuperUserIsAbleToChangePasswordForOtherUsers(
			Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		
		//Section 1 Step 1-6
		//Section 2 Step 1
		//Create a new user with custom rights and Login to verify
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(
				Arrays.asList("Billings"));
		newMerchantUser userCreatedBySuper =accountsHelper.createNewUser
												(AccessAndRolesToBeGranted, true);
		
		//Section 3 Steps 1-3
		//login with the super user and navigate to manage users page
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		//Verifications from Section 3
		//Section 4 Steps 1-3
		userCreatedBySuper.changePassword(Helper.generateRandomAlphaNumericString(20));
		accountsHelper.modifyExistingUser(userCreatedBySuper,modificationtype.Password);
		
		//Section 5 Step 1-2
		accountsHelper.verifyUIforMerchant(userCreatedBySuper);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Test Case ID: Product-2198
	@Test(description = "Verifying user for no roles/actions Assigned", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingUserForNoRolesActionsAssigned(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>();
		accountsHelper.deleteAllExistingUsers();
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,true);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	//Test Case ID: Product-2197
	@Test(description = "Verifying user for roles/actions as Export to Excel", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingUserForRolesActionsAsExportAsExcel(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("Export To Excel"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,true
										);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Test Case ID: Product-2196
	@Test(description = "Verifying user for roles/actions as Billing, View Transactions and Export to Excel", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingUserForRolesActionsAsExportAsExcelViewTransactionBilling(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("Export To Excel"
																				,"Billings","View Transactions"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,true);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Test Case ID: Product-2194
	@Test(description = "Verifying user for roles/actions as View Dashboard", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingUserForRolesActionsAsViewDashboard(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("View Dashboard"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,true);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Test Case ID: Product-2193
	@Test(description = "Verifying user for roles/actions as View Dashboard", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingUserForRolesActionsAsHidePanelFields(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("Hide Panel Fields"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,true);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	//Test Case ID: Product-2192
	@Test(description = "Verifying user for roles/actions as View Transactions and Hide Panel Fields", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingUserForRolesActionsAsViewTransactionsAndHidePanelFields(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("View Transactions","Hide Panel Fields"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,true);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	//Test Case ID: Product-2191
	@Test(description = "Verifying user for roles/actions as Manage Tempelates", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingUserForRolesActionsAsManageTemplates(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("Manage Templates"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,true);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	//Test Case ID: Product-2190
	@Test(description = "Verifying user for roles/actions as View Transactions", dataProvider = "GetTestConfig", timeOut = 600000)
	public void VerifyingUserForRolesActionsAsViewTransactions(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("View Transactions"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,true);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Test Case ID: Product-2189
	@Test(description = "Verifying user for roles/actions as IVR Transaction", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyingUserForRolesActionsAsIVRTRansaction(Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("IVR Transaction"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,true);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	//Test Case ID: Product-2188
	@Test(description = "Verifying user for roles/actions as search", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyingUserForRolesActionsAsSearch(Config testConfig) {
		
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("search"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,true);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Test Case ID: Product-2187
	@Test(description = "Verifying user for roles/actions as email_invoice", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyingUserForRolesActionsAsEmail_Invoice(Config testConfig) {
		
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow =5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("email_invoice"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted,true);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	// Test Case ID: Product-2186
	@Test(description = "Verifying user for roles/action as Billing,View Activity,Create offer and Create Invoice", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyingUserForRolesActionsAsBillingViewActivityCreateOfferCreateInvoice(
			Config testConfig) {

		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(
				Arrays.asList("Billings","View Activity","Create Offer","Create Invoice"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted, true);
		Assert.assertTrue(testConfig.getTestResult());
	}
		
	// Test Case ID: Product-2185
	@Test(description = "Verifying user for roles as Capture,Refund,Cancel,COD Verify,COD Cancel,COD Settle,Generate Invoice", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyingUserForRolesActionsAsCaptureRefundCancelCODVerifyCODCancelCODSettleGenerateInvoice(
			Config testConfig) {

		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(
				Arrays.asList("Capture", "Refund", "COD Verify","Cancel Transactions",
						"COD Cancel", "COD Settled", "Generate Invoice"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted, true);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product-2144
	@Test(description = "Verify user having super rights have all actions/roles displayed on merchant panel", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyUserHavingSuperRightsHaveAllActionsRolesDisplayedOnMerchantPanel(
			Config testConfig) {

		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		
		
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(
				Arrays.asList("super"));
		accountsHelper.createNewUser(AccessAndRolesToBeGranted, true);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product-2142
	@Test(description = "Verify user having super rights is able to delete other users", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyUserHavingSuperRightsIsAbleToDeleteOtherUsers(
			Config testConfig) {
		
		String LoginFailure = new TestDataReader(testConfig, "StringValues").GetData(5, "Value");
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		
		//Section 1 Step 1
		//Create a new user with super rights
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(
				Arrays.asList("super"));
		newMerchantUser userWithSuperAccess =	accountsHelper.
													createNewUser(AccessAndRolesToBeGranted, false
															);
		
		//Section 2 Steps 1
		//login with the new user with super rights
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doMerchantLogin(userWithSuperAccess.UserDetails);
		AccessAndRolesToBeGranted = new ArrayList<String>();
		
		//Section 2 Step 2
		//Section 3 Step1
		//Create a new user and verify a new user is created by logging in
		newMerchantUser UserCreatedBySuperAccess =	accountsHelper.createNewUser
														(AccessAndRolesToBeGranted, true);
		
		//Section 4 Steps 1-2
		//login with the super right user and delete created user
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doMerchantLogin(userWithSuperAccess.UserDetails);
		accountsHelper.deleteExistingUser(UserCreatedBySuperAccess.UserDetails.get("Username"));
		
		//Section 5 Step 1
		//Do a login with credentials of deleted user
		accountsHelper.merchantPanelHelper.doMerchantLoginAndVerifyFailure(
																UserCreatedBySuperAccess.UserDetails,LoginFailure);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product-2141
	@Test(description = "Verify user having super rights is able to manage other users", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyUserHavingSuperRightsIsAbleToManageOtherUsers(
			Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		
		//Section 1 Step 1
		//Create a new user with super rights
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(
				Arrays.asList("super"));
		newMerchantUser userWithSuperAccess =	accountsHelper.createNewUser(
													AccessAndRolesToBeGranted, false
													);
		
		//Section 2 Steps 1
		//login with the new user with super rights
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.doMerchantLogin(userWithSuperAccess.UserDetails);
		
		//Section 2 Step 2
		//Section 3 Step1
		//Create a new user and verify a new user with billings access is created by logging in
		AccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("Billings"));
		newMerchantUser UserCreatedBySuperAccess =	
											accountsHelper.createNewUser(AccessAndRolesToBeGranted
														, true);
		
		//Section 4 Steps 1-3
		//login with the super right user and navigate to manage users page
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper.
									doMerchantLogin(userWithSuperAccess.UserDetails);
		//Section 5 Steps 1-3
		ArrayList<String> newAccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("Create Offer"));
		UserCreatedBySuperAccess.changeAccessRoles(newAccessAndRolesToBeGranted);
		accountsHelper.modifyExistingUser(UserCreatedBySuperAccess,modificationtype.AccessAndActionRoles);
		
		//Section 6 Step 1-2
		accountsHelper.verifyUIforMerchant(UserCreatedBySuperAccess);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product-2062
	@Test(description = "Verify UI elements of Create New User Page", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyUIElementsOfCreateNewUserPage(
			Config testConfig) {

		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		//Login with super user
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		//Navigate to new User page
		ManageUsersProfilesAndRoles manageUserPage = (ManageUsersProfilesAndRoles) accountsHelper.merchantPanel
				.clickOnMyAccount(subMenu.ManageUsers);
		CreateNewUsersPage newUserPage =manageUserPage.clickCreateNewUser();
		//Verify textboxes
		newUserPage.VerifyAllTextboxesPresentAndEnabled();
		//Verify checkboxes
		newUserPage.verifyNamesOfAllCheckboxes();
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product-2065
	@Test(description = "Verifying change Alias password field validation", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyingChangeAliasPasswordFieldValidation(
			Config testConfig) {

		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		TestDataReader testDataReader = new TestDataReader(testConfig, "StringValues");
	
		HashMap<String, String> validationMessage = new HashMap<String, String>();
		validationMessage.put("LongAliasErrorMessage", testDataReader.GetData(6, "Value"));
		validationMessage.put("SpecialCharacterInAliasErrorMessage", testDataReader.GetData(7, "Value"));
		validationMessage.put("AliasChangeSuccessful", testDataReader.GetData(8, "Value"));
		//Login with super user
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		//Section 1-5 Check alias input fields for various valid invalid inputs
		accountsHelper.verifyAliasValidation(validationMessage);
		
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	// Test Case ID: Product-2067
	@Test(description = "Verify download for seamless Encryption publickey .", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyingDownloadForSeamlessEncryptionPublicKey(
			Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		//Section 1 Step 1 Login with super user
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		//Section 1 Step 1
		//Section 2
		accountsHelper.downloadSeamlessEncryptionPublicKey();
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	// Test Case ID: Product-2068
	@Test(description = "Verify Store Card Public Key' excel file gets uploaded for the merchant.", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyingStoreCardPublicKeyExcelFileGetsUploadedForTheMerchant(
			Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		//Section 1 Steps 1-3
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		String DownloadedFilePath = accountsHelper.downloadSeamlessEncryptionPublicKey();
		//Section 2 Steps 1-3
		
		String uploadSuccessFullMessage = new TestDataReader(testConfig, "StringValues").GetData(9, "Value");
		accountsHelper.uploadStoreCardPublicKey(DownloadedFilePath,uploadSuccessFullMessage);
		//Section 3 Steps 1-2
		accountsHelper.systemSettingsPage.verifyCancelButtonOnUploadPublicKey();
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	// Test Case ID: Product-2069
	@Test(description = "Verifying field validations for change password functionality", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyingFieldValidationsForChangePasswordFunctionality(
			Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		TestDataReader testDataReader =new TestDataReader(testConfig, "StringValues");
		HashMap<String, String> PasswordErrorMessages = new HashMap<String, String>();
		PasswordErrorMessages.put("PasswordTooShortErrorMessage",testDataReader.GetData(10, "Value"));
		PasswordErrorMessages.put("PasswordSimpleErrorMessage",testDataReader.GetData(11, "Value"));
		
		//Section 1 Steps 1-3
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		
		//Section 2-3-4-5
		accountsHelper.verifyFieldValidationForChangePassword(userLoginDetailsRow,PasswordErrorMessages);
		Assert.assertTrue(testConfig.getTestResult());
	}
		
	// Test Case ID: Product-2138
	@Test(description = "Verify that super user is able to delete other users", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyThatSuperUserIsAbleToDeleteOtherUsers(
			Config testConfig) {
		
		String LoginFailure =  new TestDataReader(testConfig, "StringValues").GetData(5, "Value");
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		
		//Section 1 Step 1-4
		//Create a new user 
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>();
		newMerchantUser userCreatedBySuper=	accountsHelper.createNewUser(AccessAndRolesToBeGranted,
												true);
		
		//Section 2 Step 1
		//Section 3 Step 1
		//Section 4 Steps 1-2
		//login with the super user and delete the above created user
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		accountsHelper.deleteExistingUser(userCreatedBySuper.UserDetails.get("Username"));
		
		//Section 5 Step 1
		//Do a login with credentials of deleted user
		accountsHelper.merchantPanelHelper.doMerchantLoginAndVerifyFailure(
				userCreatedBySuper.UserDetails,LoginFailure);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product-2139
	@Test(description = "Verify super user is able to manage other users", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifySuperUserIsAbleToManageOtherUsers(
			Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		
		//Section 1 Step 1-6
		//Section 2 Step 1
		//Create a new user with custom rights and Login to verify
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(
				Arrays.asList("Billings"));
		newMerchantUser userCreatedBySuper =	accountsHelper.createNewUser(
													AccessAndRolesToBeGranted, true);
		
		//Section 3 Steps 1-3
		//login with the super user and navigate to manage users page
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		
		//Section 4 Steps 1-3
		ArrayList<String> newAccessAndRolesToBeGranted = new ArrayList<String>(Arrays.asList("Create Offer"));
		userCreatedBySuper.changeAccessRoles(newAccessAndRolesToBeGranted);
		accountsHelper.modifyExistingUser(userCreatedBySuper,modificationtype.AccessAndActionRoles);
		
		//Section 5 Step 1-2
		accountsHelper.verifyUIforMerchant(userCreatedBySuper);
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product-2140
	@Test(description = "Verifying on hovering mouse over Custom rights action/roles assgined to that user are displayed", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyingOnHoveringMouseOverCustomRightsActionRolesAssginedToThatUserAreDisplayed(
			Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		int userLoginDetailsRow = 5;
		
		
		ArrayList<String> AccessAndRolesToBeGranted = new ArrayList<String>(
				Arrays.asList("ALL"));
		//Section 1 Step 1-6
		//Create a new user with custom rights and Login to verify
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		newMerchantUser userCreatedBySuper = accountsHelper.
												createNewUser(AccessAndRolesToBeGranted, false);
		
		//Section 2 Steps 1
		//login with the super user and navigate to manage users page
		//Verify roles assigned
		accountsHelper.merchantPanel = accountsHelper.merchantPanelHelper
				.doSuperUserLogin(userLoginDetailsRow);
		accountsHelper.verifyRolesAssignedOnHoveringCustomRights(userCreatedBySuper);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product-2143
	@Test(description = "Verify super user have all actions/roles displayed on merchant panel", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifySuperUserHaveAllActionsRolesDisplayedOnMerchantPanel(
			Config testConfig) {
		UserHelper accountsHelper = new UserHelper(testConfig);
		accountsHelper.merchantPanelHelper.payuHome = new PayUHomePage(testConfig);
		int userLoginDetailsRow = 5;
		
		//Section 1 Steps1-2
		accountsHelper.verifyUIforSuperUser(userLoginDetailsRow);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	// Test Case ID: Product-2272
	@Test(description = "Verifying My Accounts,Systemsettings and My profile for Admin user", dataProvider = "GetTestConfig", timeOut = 60000000)
	public void VerifyMyAccountsSystemSettingsAndMyProfilePageForAdminUser(
			Config testConfig) {
		int TransactionDetailsRowNumber =157;
		
		UserHelper accountsHelper = new UserHelper(testConfig);
		TransactionHelper transactionHelper = new TransactionHelper(testConfig, false);
		
		//Section 1 Steps 1-3
		//Login
		transactionHelper.DoLogin();
		transactionHelper.merchantListPage =transactionHelper.home.clickMerchantList();
		accountsHelper.merchantPanel = transactionHelper.merchantListPage.getMerchantPanel(TransactionDetailsRowNumber);
		
		//Section 2	
		//Verify UI of Merchant Panel
		ArrayList<String> AccessRolesGranted = new ArrayList<String>(Arrays.asList("super"));
		accountsHelper.merchantPanel.verifyPageUiBasedOnAccessRoles(AccessRolesGranted);
		
		//Section 3 
		//Verify System Settings Page
		accountsHelper.verifySystemSettingOptionPageUI();
		
		Assert.assertTrue(testConfig.getTestResult());
	}

}	
