package PageObject.NewMerchantPanel.MyProfile;

import java.util.HashMap;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;

public class MyProfilePage {

	private Config testConfig;
	
	@FindBy (css= ".divChangePassword>button")
	private WebElement ChangePassword;
	
	@FindBy (css= ".formChangePass")
	private WebElement ChangePasswordPopUpForm;
	
	@FindBy (css= ".formChangePass>div:nth-of-type(1)>input")
	private WebElement OldPasswordInput;
	
	@FindBy (css= ".formChangePass>div:nth-of-type(2)>input")
	private WebElement NewPasswordInput;
	
	@FindBy (css= ".formChangePass>div:nth-of-type(3)>input")
	private WebElement ConfirmPasswordInput;
	
	@FindBy (css= ".footer>button")
	private WebElement ConfirmButton;
	
	@FindBy (css= ".errorMessage")
	private WebElement ErrorMessage;
	
	public MyProfilePage(Config testConfig){
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, ChangePassword);
	}
	
	/**Clicks on password change button and waits for pop up to appear
	 * 
	 */
	public void clickOnChangePasswordButton(){
		Element.click(testConfig, ChangePassword, "Change Password Button");
		Element.waitForElementDisplay(testConfig, ChangePasswordPopUpForm);
	}
	
	/** Verifies Password validations for Password and Confirm password input field
	 * for following scenarios
	 * 1) Password is simple
	 * 2) Less than 8 character password
	 * 3) Filling current password in new password field
	 * 4) String mismatch in password and confirm password input fields
	 * @param OldPassword
	 * @param passwordErrorMessages
	 */
	public void verifyChangePasswordFieldValidations(String OldPassword, HashMap<String, String> passwordErrorMessages){
		clickOnChangePasswordButton();
		Element.enterData(testConfig, OldPasswordInput, OldPassword, "Old Password");
		String inputPassword = "";
		//Scenario for Simple Password
		inputPassword = "password";
		Element.enterData(testConfig, NewPasswordInput, inputPassword, "New Password");
		Element.enterData(testConfig, ConfirmPasswordInput, inputPassword, "Confirm New Password");
		Element.click(testConfig, ConfirmButton, "Confirm Button");
		Browser.waitForPageLoad(testConfig, ErrorMessage);
		Helper.compareEquals(testConfig, "Error Message", 
				passwordErrorMessages.get("PasswordSimpleErrorMessage"), ErrorMessage.getText());
		//Scenario For less than 8 character password
		inputPassword = Helper.generateRandomAlphabetsString(6);
		Element.enterData(testConfig, NewPasswordInput, inputPassword, "New Password");
		Element.enterData(testConfig, ConfirmPasswordInput, inputPassword, "Confirm New Password");
		Element.click(testConfig, ConfirmButton, "Confirm Button");
		Browser.waitForPageLoad(testConfig, ErrorMessage);
		Helper.compareEquals(testConfig, "Error Message", 
				passwordErrorMessages.get("PasswordTooShortErrorMessage"), ErrorMessage.getText());
		//Scenario for current Password
		inputPassword = OldPassword;
		Element.enterData(testConfig, NewPasswordInput, inputPassword, "New Password");
		Element.enterData(testConfig, ConfirmPasswordInput, inputPassword, "Confirm New Password");
		Helper.compareTrue(testConfig, "Confirm Button Disabled", (!ConfirmButton.isEnabled()));
		
		//scenario for password and confirm password input mismatch
		Element.enterData(testConfig, NewPasswordInput, Helper.generateRandomAlphaNumericString(10)
														, "New Password");
		Element.enterData(testConfig, ConfirmPasswordInput, Helper.generateRandomAlphaNumericString(10)
												, "Confirm New Password");
		Helper.compareTrue(testConfig, "Confirm Button Disabled", (!ConfirmButton.isEnabled()));
	}
}
