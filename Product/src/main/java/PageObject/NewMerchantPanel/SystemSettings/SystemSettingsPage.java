package PageObject.NewMerchantPanel.SystemSettings;

import java.util.HashMap;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;

public class SystemSettingsPage {

	private Config testConfig;
	
	@FindBy (css= "div.SystemHeader:nth-of-type(2)>label")
	private WebElement SaltIdLabel;
	
	@FindBy (css= "div.SystemHeader:nth-of-type(2)>span")
	private WebElement SaltId;
	
	@FindBy (css= "div.SystemHeader:nth-of-type(3)>div>span")
	private WebElement LoginAlias;
	
	@FindBy (css= "div.SystemHeader:nth-of-type(3)>div>button")
	private WebElement ChangeAliasButton;
	
	@FindBy (css= "div.SystemHeader:nth-of-type(4)>div>button")
	private WebElement SeamlessEncryptionKeyDownloadButton;
	
	@FindBy (css= "div.SystemHeader:nth-of-type(5)>div>button")
	private WebElement StoreCardPublicKeyUploadButton;
	
	@FindBy (css= ".formChangeAlias>div:nth-of-type(1)>input")
	private WebElement OldAliasInput;
	
	@FindBy (css= ".formChangeAlias>div:nth-of-type(2)>input")
	private WebElement NewAliasInput;
	
	@FindBy (css= ".errorMessage")
	private WebElement ErrorMessageOnPopUp;
	
	@FindBy (css= ".footer>button")
	private WebElement uploadAndProcess;
	
	@FindBy (css= ".footer>a")
	private WebElement cancelButton;
	
	@FindBy (css= ".alert>span")
	private WebElement AlertNotification;
	
	@FindBy (css= "div.uploadFileInput>div>input[type='file']")
	private WebElement browseFileButton;
	
	@FindBy (css= ".formUploadFile")
	private WebElement browseFileForm;
	
	public  SystemSettingsPage(Config testConfig){
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, SaltId);
	}
	
	/**Verifies presence of following elements
	 * Saltid,Change Alias button, Seamless Encryption Public Key Download Button
	 * Store Card Public Key Upload Button
	 */
	public void verifyUIElementsDisplayed(){
		Element.verifyElementPresent(testConfig, SaltId, "Salt ID");
		Element.verifyElementPresent(testConfig, LoginAlias, "Login Alias");
		Element.verifyElementPresent(testConfig, ChangeAliasButton, "Change Alias Button");
		Element.verifyElementPresent(testConfig, SeamlessEncryptionKeyDownloadButton,
										"Seamless Encryption Public Key Download Button");
		Element.verifyElementPresent(testConfig, StoreCardPublicKeyUploadButton,
				"Store Card Public Key Upload Button");
	}
	
	/**Clicks on Change alias button and waits for pop up to appear
	 * 
	 */
	public void clickChangeAliasButton(){
		Element.click(testConfig, ChangeAliasButton, "Change Alias Button");
		Element.waitForElementDisplay(testConfig, OldAliasInput);
	}

	/**Verifies Alias input as follows
	 * Stores current alias for future
	 * Checks error message for more than 20 characters Alias
	 * Checks error message for special characters Alias
	 * Checks confirm button disabled for blank/current alias
	 * Reverts alias to previous value
	 * @param validationMessage
	 */
	public void verifyAliasInputFieldValidations(
			HashMap<String, String> validationMessage) {
		clickChangeAliasButton();
		// Store the current Alias
		String currentAlias = OldAliasInput.getAttribute("value");
		// Enter a new alias
		String NewAlias = Helper.generateRandomAlphabetsString(6);
		Element.enterData(testConfig, NewAliasInput, NewAlias,
				"New Alias Input");
		Element.click(testConfig, uploadAndProcess, "Submit Alias Changes");
		
		verifyNotificationText(validationMessage.get("AliasChangeSuccessful"));
		clickChangeAliasButton();
		// Verify alias was changed
		Helper.compareEquals(testConfig, "Old Alias Value", NewAlias,
				OldAliasInput.getAttribute("value"));
		// More than 20 characters
		Element.enterData(testConfig, NewAliasInput,
				Helper.generateRandomAlphaNumericString(30), "New Alias Input");
		Element.click(testConfig, uploadAndProcess, "Submit Alias Changes");
		Element.waitForElementDisplay(testConfig, ErrorMessageOnPopUp);
		Helper.compareEquals(testConfig, "Error Message",
				validationMessage.get("LongAliasErrorMessage"),
				ErrorMessageOnPopUp.getText());
		// Special Characters
		Element.enterData(testConfig, NewAliasInput, "!@##$$$$",
				"New Alias Input");
		Element.click(testConfig, uploadAndProcess, "Submit Alias Changes");
		Helper.compareEquals(testConfig, "Error Message",
				validationMessage.get("SpecialCharacterInAliasErrorMessage"),
				ErrorMessageOnPopUp.getText());
		// Existing Alias
		String ExistingAlias = OldAliasInput.getAttribute("value");
		Element.enterData(testConfig, NewAliasInput, ExistingAlias,
				"New Alias Input");
		Helper.compareTrue(testConfig, "Confirm button is disabled",
				(!uploadAndProcess.isEnabled()));

		// Blank Value
		Element.enterData(testConfig, NewAliasInput, "", "New Alias Input");
		Helper.compareTrue(testConfig, "Confirm button is disabled",
				(!uploadAndProcess.isEnabled()));

		// Enter Older value of alias and save changes
		Element.enterData(testConfig, NewAliasInput, currentAlias,
				"New Alias Input");
		Element.click(testConfig, uploadAndProcess, "Submit Alias Changes");
		verifyNotificationText(validationMessage.get("AliasChangeSuccessful"));
	}
	
	/** Verifies Notification text displayed on the top of page
	 * @param NotificationText
	 */
	public void  verifyNotificationText(String NotificationText){
		Browser.waitForPageLoad(testConfig, AlertNotification);
		Helper.compareEquals(testConfig, "Alert Notification",NotificationText,AlertNotification.getText());
		Browser.wait(testConfig, 10);
	}

	/**Clicks on download public key
	 * @return path to downloaded public key file
	 */
	public String downloadSeamlessEncryptionPublicKey() {
		Element.click(testConfig, SeamlessEncryptionKeyDownloadButton, 
										"Seamless Encryption Public Key Download Button");
		String DownloadPath =  System.getProperty("user.home")+"\\Downloads";
		String PublicKeyPath = Browser.DesiredFileDownload(testConfig, DownloadPath, "PublicKey").getAbsolutePath();
		return PublicKeyPath;
		
	}

	/**Uploads store card public key 
	 * Clicks on Store Card Public Key Button
	 * Enters file name
	 * Clicks on upload and process button
	 * @param FilePathToPublicKey
	 */
	public void uploadStoreCardPublicKey(String FilePathToPublicKey){
		clickOnUploadStorecardButton();
		Element.enterFileName(testConfig, browseFileButton, FilePathToPublicKey, "Browse file button");
		Element.click(testConfig, uploadAndProcess, "upload and process button");
	}
	
	/**Clicks on Store Card Public Key Button
	 * Waits for browse file form
	 */
	public void clickOnUploadStorecardButton(){
		Element.click(testConfig, StoreCardPublicKeyUploadButton, "Store Card Public Key Button");
		Browser.waitForPageLoad(testConfig, browseFileForm);
	}
	
	/**Verifies browse file form disappears when clicked on cancel
	 * Clicks on Store Card Public Key Button
	 * Clicks on Cancel Button
	 * Verifies Form is no longer present
	 */
	public void verifyCancelButtonOnUploadPublicKey(){
		clickOnUploadStorecardButton();
		Element.click(testConfig, cancelButton, "cancel button");
		Element.verifyElementNotPresent(testConfig, browseFileForm, "Browse and upload "
																		+ "public key form");
	}
	
	public NotificationCenter initializeNotificationCentre(){
		
		return new NotificationCenter(testConfig);
	}
}