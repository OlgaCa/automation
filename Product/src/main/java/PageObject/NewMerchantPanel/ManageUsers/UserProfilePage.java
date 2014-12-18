package PageObject.NewMerchantPanel.ManageUsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;

/**Class contains elements and generic methods for new user and existing user page
 * @author sharadjain
 *
 */
public class UserProfilePage {

	private Config testConfig;
	
	@FindBy(css="input[name='username']")
	protected WebElement UserName;
	
	@FindBy(css="input[name='password']")
	protected WebElement Password;
	
	@FindBy(css="input[name='confirmPassword']")
	protected WebElement ConfirmPassword;
	
	@FindBy(css="input[name='name']")
	protected WebElement Name;
	
	@FindBy(css="input[name='email']")
	protected WebElement Email;
	
	@FindBy(xpath="//span[@class='chkbox']/parent::li")
	protected List<WebElement> rolesAndActionsCheckboxes;
	
	@FindBy(css="button.btn.btn-success[type='Submit']")
	protected WebElement Submit;
	
	@FindBy (css= ".alert>span")
	private WebElement AlertNotification;
	
	
	public UserProfilePage(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, UserName);
		storeRoleAndActionCheckboxes();
	}
	
	/**
	 *Contains names of all checkboxes on page 
	 */
	HashMap<String,WebElement> AccessRoleCheckboxes;
	
	/**Stores all action checkboxes
	 */
	private void storeRoleAndActionCheckboxes(){
		String checkboxText; 
		AccessRoleCheckboxes = new HashMap<String, WebElement>();
		for (WebElement checkbox : rolesAndActionsCheckboxes) {
			checkboxText=	checkbox.getText().toLowerCase().replaceAll(" ", "");
			AccessRoleCheckboxes.put(checkboxText,checkbox);
		}
		testConfig.logComment(AccessRoleCheckboxes.size()+" Access and User role checkboxes stored");
	}
	
	/**Gets Webelement for User Access/Role check box for specified name
	 * @param AccessRoleName
	 * @return
	 */
	public WebElement getUserAccessRole(String AccessRoleName){
		String AccessRoleNameModified=	AccessRoleName.toLowerCase().replaceAll(" ", "");
		WebElement checkboxTarget = AccessRoleCheckboxes.get(AccessRoleNameModified);
		if(checkboxTarget!= null) {
			testConfig.logPass("Checkbox found for " +AccessRoleName);
			checkboxTarget = checkboxTarget.findElement(By.tagName("span"));
			return checkboxTarget;
		}
		
		testConfig.logFail("Checkbox Not found for " +AccessRoleName);
		return null;
	}
	
	/**Displays name of all checkboxes on page
	 * 
	 */
	public void displayNamesOfAllCheckboxes(){
		for (Entry<String, WebElement> entry : AccessRoleCheckboxes.entrySet()) {
			testConfig.logComment("Value for key "+entry.getKey()+" is "+entry.getValue());
		}
	}
	
	/** Verifies all specified checkboxes are present on page
	 * @param nameOfCheckboxes Names of checkboxes that are required to be present
	 */
	public void verifyNamesOfAllCheckboxes(){
		ArrayList<String> rolesNotPresent = new ArrayList<>();
		String tempRoleName;
		ArrayList<String> nameOfCheckboxes = new ArrayList<String>(
				Arrays.asList("Billings", "Cod Verify", "Cod Settled",
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
		for (String roleName : nameOfCheckboxes) {
			tempRoleName = roleName.toLowerCase().replaceAll(" ", "");
			if(AccessRoleCheckboxes.containsKey(tempRoleName)){
				testConfig.logComment(roleName + " checkbox is  present");
			}
			else {
				testConfig.logComment(roleName + " checkbox is not present");
				rolesNotPresent.add(roleName);
			}
			
		}
		if(rolesNotPresent.isEmpty())	testConfig.logPass("All checkboxes are present");
		else	testConfig.logFail(rolesNotPresent.size() + " checkboxes are not present");
	}
	
	/** Clicks Confirm 
	 * @return Manage User Page
	 */
	public ManageUsersProfilesAndRoles clickConfirm(){
		Element.click(testConfig, Submit, "Confirm button");
		return new ManageUsersProfilesAndRoles(testConfig);
	}
	
	/**Clicks confirm and verifies notification message displayed
	 * @param ExpectedErrorMessage Error message string expected
	 */
	public void clickConfirmAndVerifyNotificationError(String ExpectedErrorMessage){
		Element.click(testConfig, Submit, "Confirm button");
		Browser.waitForPageLoad(testConfig, AlertNotification);
		Helper.compareEquals(testConfig, "Error Notification",
				ExpectedErrorMessage, AlertNotification.getText().trim());
	}

	/**Enters data in Password and confirm password page
	 * @param password - Password string
	 */
	public void enterPasswordAndConfirmPassword(String password){
		Element.enterData(testConfig, Password, password,"Password");
		Element.enterData(testConfig, ConfirmPassword, password,"Confirm Password");
	}
}
