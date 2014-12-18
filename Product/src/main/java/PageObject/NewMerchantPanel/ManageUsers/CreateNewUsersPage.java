package PageObject.NewMerchantPanel.ManageUsers;

import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.support.Color;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;

/**Class contains methods for page displayed when clicked on create new user
 * @author sharadjain
 *
 */
public class CreateNewUsersPage extends UserProfilePage{

	private Config testConfig;
	
	public CreateNewUsersPage(Config testConfig) {
		super(testConfig);
		this.testConfig =testConfig;
	}
	
	/**Fills details in new user fields :-
	 *  Username
	 *  Password and Confirm Password
	 *  Name and Email
	 *  Marks all accessroles granted to user
	 * @param userDetails - Details of user
	 * @param accessRoles - Access roles granted to user
	 */
	public void fillNewUserDetails(HashMap<String, String> userDetails,
			ArrayList<String> accessRoles) {
		Element.enterData(testConfig, UserName, userDetails.get("Username"),"User Name");
		enterPasswordAndConfirmPassword(userDetails.get("Password"));
		Element.enterData(testConfig, Name, userDetails.get("Name"),"Name");
		Element.enterData(testConfig, Email, userDetails.get("Email"),"Email");
		for (String AccessRoleName : accessRoles) {
			Element.click(testConfig, getUserAccessRole(AccessRoleName), AccessRoleName +" checkbox");
		}
	}

	/**Verifies following textboxes are present and enabled
	 * Username,Name,Email and Password
	 */
	public void VerifyAllTextboxesPresentAndEnabled(){
		Helper.compareTrue(testConfig, "Username textbox Enabled", UserName.isEnabled());
		Helper.compareTrue(testConfig, "Name textbox Enabled", Name.isEnabled());
		Helper.compareTrue(testConfig, "Email textbox Enabled", Email.isEnabled());
		Helper.compareTrue(testConfig, "Password textbox Enabled", Password.isEnabled());
	}

	/**Verifies validations for password on Password and Confirm Password
	 *  input field for following scenarios
	 * 1)Password is short
	 * 2)Different String in password and confirm password
	 * 3) When valid password and confirm password is provided
	 * @param errorMessageText - Error message displayed when password is too short
	 */
	public void verifyPasswordInputFieldValidations(String errorMessageText) {
		//Check for when password is short
		enterPasswordAndConfirmPassword(Helper.generateRandomAlphaNumericString(3));
		clickConfirmAndVerifyNotificationError(errorMessageText);
		//enter different string in password and confirm password
		Element.enterData(testConfig, Password, Helper.generateRandomAlphaNumericString(8),"Password");
		Element.enterData(testConfig, ConfirmPassword, Helper.generateRandomAlphaNumericString(8),"Confirm Password");
		//verify red border color is displayed for confirm password
		String borderColor = Color.fromString(ConfirmPassword.getCssValue("border-bottom-color")).asHex().toString();
		Helper.compareEquals(testConfig, "Border color", "#d58a8a", borderColor);
		//Valid Values
		enterPasswordAndConfirmPassword(Helper.generateRandomAlphaNumericString(12));
	}

	/**Verifies Email validation for email input field for following scenarios
	 * 1)When Invalid email string is provided
	 * 2)When invalid email string is provided
	 */
	public void verifyEmailInputFieldValidations() {
		String InvalidEmail = Helper.generateRandomAlphabetsString(20);
		Element.enterData(testConfig, Email, InvalidEmail,"Email");
		String borderColor = Color.fromString(Email.getCssValue("border-bottom-color"))
											.asHex().toString();
		Helper.compareEquals(testConfig, "Border color", "#d58a8a", borderColor);
		String Validemail = Helper.generateRandomAlphaNumericString(4) + "@"
				+ Helper.generateRandomAlphabetsString(2) + "."
				+ Helper.generateRandomAlphabetsString(3);
		Element.enterData(testConfig, Email, Validemail,"Email");
	}

	/**Verifies name validation for name field validations for following scenarios
	 * 1) Greater than 30 characters
	 * 2) Valid name is provided
	 */
	public void verifyNameInputFieldValidations() {
		String InvalidName = Helper.generateRandomAlphaNumericString(20);
		Element.enterData(testConfig, Name, InvalidName,"Name");
		String borderColor = Color.fromString(Name.getCssValue("border-bottom-color")).asHex().toString();
		Helper.compareEquals(testConfig, "Border color", "#d58a8a", borderColor);
		String ValidName = Helper.generateRandomAlphabetsString(20);
		Element.enterData(testConfig, Name, ValidName,"Name");
		
	}

	/**Verifies Username validations for Username input field for following scenarios:-
	 * 1)Special characters in username
	 * 2)Short name in username
	 * 3)Valid user name string is provided
	 * @param errorMessageText - Error message when user name provided is short
	 */
	public void verifyUsernameInputFieldValidations(String errorMessageText) {
		String InvalidUsername = Helper.generateRandomAlphaNumericString(20)+"!@#$$";
		Element.enterData(testConfig, UserName, InvalidUsername,"UserName");
		String borderColor = Color.fromString(UserName.getCssValue("border-bottom-color")).asHex().toString();
		Browser.wait(testConfig, 5);
		Helper.compareEquals(testConfig, "Border color", "#d58a8a", borderColor);
		
		String shortUserName = Helper.generateRandomAlphabetsString(3);
		Element.enterData(testConfig, UserName, shortUserName,"Username");
		clickConfirmAndVerifyNotificationError(errorMessageText);
		Browser.wait(testConfig, 5);
		String validUserName = Helper.generateRandomAlphabetsString(20);
		Element.enterData(testConfig, UserName, validUserName,"Username");
		
		
	}
	
}
