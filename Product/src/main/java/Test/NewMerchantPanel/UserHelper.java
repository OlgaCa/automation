package Test.NewMerchantPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;

import PageObject.MerchantPanel.Home.PayUHomePage;
import PageObject.NewMerchantPanel.ManageUsers.CreateNewUsersPage;
import PageObject.NewMerchantPanel.ManageUsers.ManageUserProfilePage;
import PageObject.NewMerchantPanel.ManageUsers.ManageUsersProfilesAndRoles;
import PageObject.NewMerchantPanel.MyProfile.MyProfilePage;
import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage.subMenu;
import PageObject.NewMerchantPanel.SystemSettings.NotificationCenter;
import PageObject.NewMerchantPanel.SystemSettings.NotificationCenter.CustomerAlertType;
import PageObject.NewMerchantPanel.SystemSettings.NotificationCenter.MerchantAlertType;
import PageObject.NewMerchantPanel.SystemSettings.NotificationCenter.ToggleState;
import PageObject.NewMerchantPanel.SystemSettings.SystemSettingsPage;
import Test.AdminPanel.Payments.TransactionHelper.TransactionType;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.EmailHelper;
import Utils.Helper;
import Utils.TestDataReader;

/**
 * @author sharadjain
 *
 */
public class UserHelper {
	private Config testConfig;
	public MerchantPanelPage merchantPanel;
	public ManageUsersProfilesAndRoles manageUserPage;
	public DashboardHelper merchantPanelHelper;
	SystemSettingsPage systemSettingsPage;
	MyProfilePage myProfilePage;
	NotificationCenter notificationCenter;
	public UserHelper(Config testConfig){
		this.testConfig = testConfig;
		merchantPanelHelper = new DashboardHelper(testConfig);
	}

	/** Class for a new merchant user
	 * @author sharadjain
	 *Holds details of merchant and various utility methods for a user
	 */
	public class newMerchantUser{
		HashMap<String, String> UserDetails = new HashMap<String, String>();
		private ArrayList<String> accessRoles;
		public newMerchantUser(ArrayList<String> AccessRolesGranted){
			accessRoles= new ArrayList<String>(){};
			//If keyword ALL present then add all roles to access roles
			if (AccessRolesGranted.contains("ALL")){
				accessRoles.addAll(Arrays.asList("Billings", "Cod Verify", "Cod Settled",
						"Change Current Password", "Export to Excel",
						"email_invoice", "Generate Invoice",
						"View Transactions", "Manage Templates",
						"Allow underl...e password.", "Super",
						"Create Invoice", "Capture", "Cod Cancel",
						"Cancel/Refund", "Health Metrics", "Search",
						"Download Public Key", "Refund", "Hide Panel Fields",
						"View Activity", "View Analytics", "show card details",
						"Cancel Transactions", "View Dashboard",
						"Create Offer", "Change Password", "IVR Transaction"));
			}
			else	//else add roles that were passed
						accessRoles.addAll(AccessRolesGranted);
			
			UserDetails.put("Username", Helper.generateRandomAlphabetsString(12));
			UserDetails.put("Password", Helper.generateRandomAlphaNumericString(12));
			UserDetails.put("Name", Helper.generateRandomAlphabetsString(12));
			UserDetails.put("Email", Helper.generateRandomAlphabetsString(4)+"@"
										+Helper.generateRandomAlphabetsString(2)+"."
										+Helper.generateRandomAlphabetsString(3));
			//TODO get this value from product data
			UserDetails.put("Alias", "superuser");
			
		}
		
		/**Creates a custom user for user that already exists i.e. Superuser
		 * @param UserName 
		 * @param Name
		 * @param Password
		 * @param Alias
		 * @param AccessRolesGranted- Roles to be granted
		 */
		public newMerchantUser(String UserName, String Name,String Password, String Alias,
				ArrayList<String> AccessRolesGranted) {
			accessRoles= new ArrayList<String>(){};
			accessRoles.addAll(AccessRolesGranted);
			UserDetails.put("Username", UserName);
			UserDetails.put("Password", Password);
			UserDetails.put("Name", Name);
			//Get email
			UserDetails.put("Email", Helper.generateRandomAlphabetsString(4)+"@"
										+Helper.generateRandomAlphabetsString(2)+"."
										+Helper.generateRandomAlphabetsString(3));
			UserDetails.put("Alias", Alias);
		}
		//Modify values
		public void changeAccessRoles(ArrayList<String> AccessRolesGranted){
			this.accessRoles =AccessRolesGranted;
		}
		public void changeUserName(String newUserName){
			UserDetails.put("Username", newUserName);
		}
		public void changePassword(String newPassword){
			UserDetails.put("Password", newPassword);
		}
		
		public ArrayList<String> getAccessRolesList(){
			return this.accessRoles;
		}
		public String getUserName(){
			return this.UserDetails.get("Username");
		}
		
		/**Creates a new user with details generated from constructor
		 * When on dashboard page 
		 * Navigates to new user page
		 * Fills user details and clicks confirm
		 * @param NotificationText
		 * @return manager user page
		 */
		public ManageUsersProfilesAndRoles CreateANewUser(String NotificationText){
			CreateNewUsersPage newUserPage = manageUserPage.clickCreateNewUser();
			newUserPage.fillNewUserDetails(UserDetails,accessRoles);
			manageUserPage = newUserPage.clickConfirm();
			manageUserPage.verifyNotificationText(NotificationText);
	
			return manageUserPage;
		}
		
	}
	
	/**Creates a new user for mentioned fields when on dashboard page
	 * Navigates to manage users page
	 * Creates a new user by clicking on create new user and Fillings details
	 * Signs out
	 * If flag verifyUiForNewMerchant on then logins and checks UI
	 * Signs out 
	 * @param accessAndRolesToBeGranted - Roles to be granted
	 * @param verifyUiForNewMerchant - If on created user's Ui is verified 
	 * by logging with new credentials
	 * @return user object of created user
	 */ 
	public  newMerchantUser createNewUser(ArrayList<String> accessAndRolesToBeGranted
								,boolean verifyUiForNewMerchant){
		//Getting string displayed as notification on success
		String NotificationText = new TestDataReader(testConfig, "StringValues").GetData(1, "Value");
		
		manageUserPage = (ManageUsersProfilesAndRoles)merchantPanel.clickOnMyAccount(subMenu.ManageUsers);
		newMerchantUser newUser = new newMerchantUser(accessAndRolesToBeGranted);
		manageUserPage = newUser.CreateANewUser(NotificationText);
		merchantPanel.clickOnMyAccount(subMenu.SignOut);
		if(verifyUiForNewMerchant)	verifyUIforMerchant(newUser);
		verifyNewUserDetailsFromDatabase(newUser);
		return newUser;
	}
	
	/**Verifies user details from database after its creation
	 * @param newUser -object of user
	 */
	private void verifyNewUserDetailsFromDatabase(newMerchantUser newUser) {
		testConfig.putRunTimeProperty("username", newUser.UserDetails.get("Username"));
		Map<String, String> merchantLogins = DataBase.executeSelectQuery(testConfig, 142, 1);
		Helper.compareEquals(testConfig, "Active database status",newUser.UserDetails.get("Name"), merchantLogins.get("name"));
		Helper.compareEquals(testConfig, "Active database status",newUser.UserDetails.get("Username"), merchantLogins.get("username"));
		Helper.compareEquals(testConfig, "Active database status",newUser.UserDetails.get("Email"), merchantLogins.get("email"));
		Helper.compareEquals(testConfig, "Active database status","1", merchantLogins.get("active"));
		
	}

	/**Verifies UI for mentioned user when on payuhome page
	 * Logins with the credentials
	 * Verifies UI according to user roles
	 * @param user objects containing details
	 */
	public void verifyUIforMerchant(newMerchantUser user){
		merchantPanel = merchantPanelHelper.doMerchantLogin(user.UserDetails);
		merchantPanel.verifyPageUiBasedOnAccessRoles(user.accessRoles);
		if(user.accessRoles.contains("super")) 	verifySystemSettingOptionPageUI();
		merchantPanel.clickOnMyAccount(subMenu.SignOut);

	}
	
	/**Verifies UI for super user when on payuhomepage page
	 * Creates an object for superuser and logins merchant panel and verifies UI
	 * @param UserDetailsRow -Details for superuser
	 */
	public void verifyUIforSuperUser(int UserDetailsRow){
		//Get Details from test data spreadsheet
		TestDataReader userLoginDetails = new TestDataReader(testConfig, "UserLoginDetails");
		String Username = userLoginDetails.GetData(UserDetailsRow, "username");
		String Password = userLoginDetails.GetData(UserDetailsRow, "password");
		String Alias = userLoginDetails.GetData(UserDetailsRow, "name");
		String Name = userLoginDetails.GetData(UserDetailsRow, "Comments");
		//Create object
		ArrayList<String> AccessRolesGranted = new ArrayList<String>(Arrays.asList("super"));
		
		newMerchantUser superUser = new newMerchantUser
								(Username, Name, Password, Alias, AccessRolesGranted);
		//Login and verify its UI 
		verifyUIforMerchant(superUser);
	}
	
	/**Deletes existing user when on dashboard page
	 * Navigates to manage users
	 * Deletes user by clicking delete and then confirm delete
	 * Verifies user is deleted from database
	 * @param UserName - User name of the user to be deleted
	 */
	public void deleteExistingUser(String UserName){
		manageUserPage = (ManageUsersProfilesAndRoles)merchantPanel.clickOnMyAccount(subMenu.ManageUsers);
		manageUserPage.deleteUser(UserName);
		merchantPanel.clickOnMyAccount(subMenu.SignOut);
		//Database verification
		testConfig.putRunTimeProperty("username", UserName);
		Map<String, String> merchantLogins = DataBase.executeSelectQuery(testConfig, 142, 1);
		Helper.compareEquals(testConfig, "Active database status","0", merchantLogins.get("active"));
		Helper.compareEquals(testConfig, "Delete database status","true", merchantLogins.get("delete"));
	}
	
	enum modificationtype{AccessAndActionRoles,Password};
	
	/**Modifies values of existing user for mentioned field when on dashboard page
	 * Goes to manage users page
	 * Changes details
	 * Clicks confirm
	 * Signs out
	 * @param user - details of user
	 * @param typeOfModification AccessAndActionRoles,Password
	 * @return payuhome page
	 */
	public PayUHomePage modifyExistingUser(newMerchantUser user, modificationtype typeOfModification){
		manageUserPage = (ManageUsersProfilesAndRoles)merchantPanel.clickOnMyAccount(subMenu.ManageUsers);
		ManageUserProfilePage existingUserProfilePage =manageUserPage.manageUser(user.UserDetails.get("Username"));
		if(typeOfModification==modificationtype.AccessAndActionRoles){
			manageUserPage = existingUserProfilePage.modifyRolesAndActionAccess(user);
			
		}
		if(typeOfModification==modificationtype.Password){
			existingUserProfilePage.enterPasswordAndConfirmPassword(user.UserDetails.get("Password"));
			existingUserProfilePage.clickConfirm();
			
		}
		String UpdateNotificationText=
				"Username - '"+user.UserDetails.get("Name")+"' is updated successfully.";
		manageUserPage.verifyNotificationText(UpdateNotificationText);
		
		return (PayUHomePage)merchantPanel.clickOnMyAccount(subMenu.SignOut);
	}

	/**Deletes all existing users when on dashboard page
	 * Navigates to manage user page
	 * Clicks delete for all existing user
	 */
	public void deleteAllExistingUsers() {
		manageUserPage = (ManageUsersProfilesAndRoles)merchantPanel.clickOnMyAccount(subMenu.ManageUsers);
		manageUserPage.deleteAllUsers();
	}

	/**Verifies System Settings option page Elements when on dashboard page
	 * Navigates to system settings option page using my accounts dropdown
	 * Verifies UI elements on the page
	 */
	public void verifySystemSettingOptionPageUI(){
		systemSettingsPage = (SystemSettingsPage)merchantPanel.clickOnMyAccount(subMenu.SystemSettings);
		systemSettingsPage.verifyUIElementsDisplayed();
	}
	
	/**Verifies user role details when on dashboard page
	 * Goes to manage user page and hovers mentioned user
	 * Verifies all user roles are displayed for given user
	 * @param user
	 */
	public void verifyRolesAssignedOnHoveringCustomRights(newMerchantUser user){
		manageUserPage = (ManageUsersProfilesAndRoles)merchantPanel.clickOnMyAccount(subMenu.ManageUsers);
		manageUserPage.verifyUsersActionRolesDisplayedOnMouseHover(user);
	}

	/**Verifies duplicate Username error when on dashboard page
	 * Goes to manage users page
	 * Gets user name of already existing user
	 * clicks on create new user
	 * Fills detail and enters name of existing user
	 * Clicks confirm and verifies duplicate user error
	 * @param ExpectedErrorMessage
	 */
	public void verifyDuplicateUsernameError(String ExpectedErrorMessage) {
		manageUserPage = (ManageUsersProfilesAndRoles)merchantPanel.clickOnMyAccount(subMenu.ManageUsers);
		String existingUser = manageUserPage.getUsernameOfExistingUser();
		ArrayList<String> AccessRolesGranted = new ArrayList<String>();
		newMerchantUser newUser = new newMerchantUser(AccessRolesGranted);
		newUser.changeUserName(existingUser);
		CreateNewUsersPage newUserPage = manageUserPage.clickCreateNewUser();
		newUserPage.fillNewUserDetails(newUser.UserDetails,newUser.accessRoles);
		 newUserPage.clickConfirmAndVerifyNotificationError(ExpectedErrorMessage);
	}

	enum newUserFields{Password,Email,Name,Username};
	
	/**Verifies validations for input fields on new user page
	 * When on dashboard goes to Create new user page
	 * Does validations using valid/invalid inputs
	 * @param InputField -name of field on create new user page
	 * @param ErrorMessageText
	 */
	public void verifyFieldValidations(newUserFields InputField ,String ErrorMessageText) {
		ArrayList<String> AccessRolesGranted = new ArrayList<String>();
		//Create details for a new merchant user
		newMerchantUser newUser = new newMerchantUser(AccessRolesGranted);
		manageUserPage = (ManageUsersProfilesAndRoles)merchantPanel.clickOnMyAccount(subMenu.ManageUsers);
		CreateNewUsersPage newUserPage = manageUserPage.clickCreateNewUser();
		newUserPage.fillNewUserDetails(newUser.UserDetails,newUser.accessRoles);
		switch(InputField){
		case Password:
					newUserPage.verifyPasswordInputFieldValidations(ErrorMessageText);
			break;
		case Email :
					newUserPage.verifyEmailInputFieldValidations();
			break;
		case Name:
			newUserPage.verifyNameInputFieldValidations();
			break;
		case Username:
			newUserPage.verifyUsernameInputFieldValidations(ErrorMessageText);
			break;
		}
		Browser.wait(testConfig, 5);
		manageUserPage = newUserPage.clickConfirm();
		//TODO get notification text from data sheet
		manageUserPage.verifyNotificationText("New user is created successfully.");
	}
	
	/**Verifies valid/invalid values in alias input field
	 * @param validationMessage
	 */
	public void verifyAliasValidation(HashMap<String, String> validationMessage){
		 systemSettingsPage = (SystemSettingsPage)merchantPanel
				.clickOnMyAccount(subMenu.SystemSettings);
		systemSettingsPage.verifyAliasInputFieldValidations(validationMessage);
	}
	
	/**Downloads Seamless Encryption key when on dashboard page
	 * Goes to system settings
	 * Clicks on download public key
	 * @return filepath to downloaded public key
	 */
	public String downloadSeamlessEncryptionPublicKey() {
		systemSettingsPage = (SystemSettingsPage)merchantPanel
				.clickOnMyAccount(subMenu.SystemSettings);
		return systemSettingsPage.downloadSeamlessEncryptionPublicKey();
		
	}

	/**Uploads store card public key file
	 * When on dashboard page navigates to 
	 * system settings page
	 * Clicks on upload upload store card key button
	 * Enters file path and clicks confirm
	 * Also Ve
	 * @param publicKeyFilePath
	 * @param uploadSuccessFullMessage 
	 */
	public void uploadStoreCardPublicKey(String publicKeyFilePath, String uploadSuccessFullMessage) {

		systemSettingsPage = (SystemSettingsPage)merchantPanel
				.clickOnMyAccount(subMenu.SystemSettings);
		systemSettingsPage.uploadStoreCardPublicKey(publicKeyFilePath);
		systemSettingsPage.verifyNotificationText(uploadSuccessFullMessage);
	}

	/**Verifies validations for Password and confirm password fields on profile page
	 * When on dashboard page 
	 * Navigate to My profile Page
	 * Verifies errors and behavior for various valid and invalid data 
	 * for password and confirm password field
	 * @param userLoginDetailsRow
	 * @param passwordErrorMessages
	 */
	public void verifyFieldValidationForChangePassword(int userLoginDetailsRow, HashMap<String, String> passwordErrorMessages) {
		myProfilePage = (MyProfilePage)merchantPanel
				.clickOnMyAccount(subMenu.MyProfile);
		myProfilePage.verifyChangePasswordFieldValidations("Password",passwordErrorMessages );
		
	}

	/**Verifies alerts are on for specifies param values
	 * @param toggleState On/Off
	 * @param paramRows Row numbers from 'Param' Sheet
	 */
	public void verifyAlertsAreOn(ToggleState toggleState,
			ArrayList<Integer> paramRows) {
		notificationCenter = (NotificationCenter) merchantPanel
				.clickOnMyAccount(subMenu.SystemSettings);
		TestDataReader testDataReader = new TestDataReader(testConfig, "Params");
		ArrayList<MerchantAlertType> merchantAlerts = new ArrayList<MerchantAlertType>();
		for (Integer paramrow : paramRows) {
			String strAlertName = testDataReader.GetData(paramrow, "key");
			switch (strAlertName) {
			case "daily_report_email":
				merchantAlerts.add(MerchantAlertType.DailySettlementReport);
				break;
			case "merchant_email_refund":
				merchantAlerts.add(MerchantAlertType.RefundSuccessfulAlert);
				break;
			case "merc_bounced_email":
				merchantAlerts.add(MerchantAlertType.BouncedTransactionAlert);
				break;
			case "merc_cancelled_email":
				merchantAlerts
						.add(MerchantAlertType.UserCancelledTransactionAlert);
				break;
			case "merc_dropped_email":
				merchantAlerts.add(MerchantAlertType.DroppedTransactionAlert);
				break;
			case "merc_failed_email":
				merchantAlerts.add(MerchantAlertType.FailedTransactionAlert);
				break;
			case "merc_successful_email":
				merchantAlerts
						.add(MerchantAlertType.SuccessfulTransactionAlert);
				break;
			case "payu_productupdatesemail":
				merchantAlerts.add(MerchantAlertType.ProductUpdates);
				break;
			case "Success_By_Recon_Report":
				merchantAlerts
						.add(MerchantAlertType.TransactionStatusChangeReport);
				break;
			}

		}
		notificationCenter.verifyAlertToggleAre(toggleState, merchantAlerts);
	}

	/**Performs email validations on various merchant alert options present
	 * When on merchant alert page
	 * Goes to system settings page
	 * Performs validation by entering valid and invalid email strings for
	 * various merchant alerts options
	 */
	public void doNotificationCenterEmailValidations() {
		notificationCenter = (NotificationCenter) merchantPanel
				.clickOnMyAccount(subMenu.SystemSettings);
		notificationCenter.DoAlertEmailValidations();
		
	}

	/**Verifies that multiple emails are accepted by merchant alerts
	 * When on Merchant panel
	 * Navigates to system settings page
	 * Enters strings consisting of multiple emails separated by comma
	 * and verifies that email value gets accepted
	 */
	public void verifyMultipleEmailIdsAllowedForAlerts() {
		notificationCenter = (NotificationCenter) merchantPanel
				.clickOnMyAccount(subMenu.SystemSettings);
		notificationCenter.verifyMultipleEmailIdsAllowedForAlerts();
	}
	
	/**Sets Notification Alert for specified alerts
	 * @param merchantAlerts - Merchant alerts
	 * @param emailIdRowsForAlerts - Rows for email id information
	 */
	public void setNotificationAlertsFor(
			ArrayList<MerchantAlertType> merchantAlerts,
			ArrayList<Integer> emailIdRowsForAlerts) {
		notificationCenter = (NotificationCenter) merchantPanel
				.clickOnMyAccount(subMenu.SystemSettings);
		for (MerchantAlertType merchantAlertRow : merchantAlerts) {
			notificationCenter.setAlertFor(merchantAlertRow	, emailIdRowsForAlerts);
		}
		
	}

	/**
	 * Verifies email report received after specified action by merchant
	 * 
	 * @param transactionType
	 *            - Type of transaction done. Required to compute expected email
	 *            content
	 * @param SuggestedSearchPattern
	 *            - Pattern for searching mail. e.g. Transaction ID
	 */
	public void verifyMerchantEmailReportFor(TransactionType transactionType,
			String SuggestedSearchPattern, ArrayList<Integer> emailConfigRows) {
		for (Integer emaildIdRow : emailConfigRows) {

			try {
				EmailHelper emailHelper = new EmailHelper(testConfig,
						emaildIdRow);
				String expectedStatusInSubject = "";
				ArrayList<Message> messages = emailHelper.getMessagesBySearchTerm(SuggestedSearchPattern, 60);
				if (messages.size() > 1) {
					testConfig.logWarning(messages.size() + " were found for "
							+ SuggestedSearchPattern);
				} else {
					Message recievedEmail = messages.get(0);
					switch (transactionType) {
					case Successful:
						expectedStatusInSubject = "succeeded";
						break;
					case Failed:
						expectedStatusInSubject = "failed";
						break;
					case Bounced:
						expectedStatusInSubject = "bounced";
						break;
					case UserCancelled:
						expectedStatusInSubject = "userCancelled";
						break;
					default:
						break;
					}

					Helper.compareContains(testConfig,
							"Status string in email subject ",
							expectedStatusInSubject, recievedEmail.getSubject());
				}
			}catch(NullPointerException nullPointerException){
				testConfig.logFail("No Messages found");
			}
			catch (MessagingException messagingException) {
				testConfig.logException(messagingException);
			}
		}
	}
	
	/**Verifies email report received after specified action by customer
	 * @param transactionType - Type of transaction done. Required to compute expected email content
	 * @param SuggestedSearchPattern - Pattern for searching mail. e.g. Transaction ID
	 */
	public void verifyCustomerEmailReportFor(TransactionType transactionType,
			String SuggestedSearchPattern) {
		if (transactionType == TransactionType.Failed) {
			// execute cron
			Browser.navigateToURL(testConfig,
					testConfig.getRunTimeProperty("CronUrl"));
			Element.getPageElement(testConfig, How.linkText,
					"emailNotification.php").click();
		}
		try {
			EmailHelper emailHelper = new EmailHelper(testConfig, 1);
			String expectedStatusInSubject = "";
			ArrayList<Message> messages = emailHelper.getMessagesBySearchTermInContent(
					SuggestedSearchPattern, 60);
			if (messages.size() > 1) {
				testConfig.logWarning(messages.size() + " were found for "
						+ SuggestedSearchPattern);
			} else {
				Message recievedEmail = messages.get(0);
				switch (transactionType) {
				case Successful:
					expectedStatusInSubject = "Successful";
					break;
				case Failed:
					expectedStatusInSubject = "incomplete";
					break;
				default:
					break;
				}

				Helper.compareContains(testConfig,
						"Status string in email subject ",
						expectedStatusInSubject, recievedEmail.getSubject());
			}
		} catch (MessagingException e) {
			testConfig.logException(e);
		}

	}

	/**Sets Notification Alert for specified alerts
	 * @param merchantAlerts - Merchant alerts
	 * @param emailIdRowsForAlerts - Rows for email id information
	 */
	public void setNotificationAlertsFor(
			ArrayList<CustomerAlertType> customerAlerts) {
		notificationCenter = (NotificationCenter) merchantPanel
				.clickOnMyAccount(subMenu.SystemSettings);
		for (CustomerAlertType customerAlertType : customerAlerts) {
			notificationCenter.setAlertFor(customerAlertType, ToggleState.On);
		}
	}

	/**Turns off specified merchant alert
	 * @param merchantAlerts - list of merchant alerts
	 */
	public void TurnOffMerchantAlertsFor(
			ArrayList<MerchantAlertType> merchantAlerts) {
		notificationCenter = (NotificationCenter) merchantPanel
				.clickOnMyAccount(subMenu.SystemSettings);
		for (MerchantAlertType merchantAlertType : merchantAlerts) {
			notificationCenter.turnOffMerchantAlertFor(merchantAlertType);
		}
		
	}
}


