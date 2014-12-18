package PageObject.NewMerchantPanel.ManageUsers;


import java.util.List;

import org.openqa.selenium.WebElement;

import Test.NewMerchantPanel.UserHelper.newMerchantUser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;

public class ManageUserProfilePage extends UserProfilePage {
	private Config testConfig;
	
	/**Initializes page object
	 * Verifies username
	 * Verifies Certain textboxes are disabled for existing user
	 * @param testConfig
	 * @param userName - is compared with actual value in username field in 
	 * username input field to check if correct user profile is displayed
	 */
	public ManageUserProfilePage(Config testConfig, String userName) {
		super(testConfig);
		this.testConfig =testConfig;
		Helper.compareEquals(testConfig, "Username in Manage User Page", userName, 
										UserName.getAttribute("value"));
		verifyTextboxesDisabled();
	}
	
	/**Verifies following textboxes are disabled
	 * Username, Name and Email input fields
	 */
	public void verifyTextboxesDisabled() {
		Helper.compareTrue(testConfig, "Username textbox disabled", (!UserName.isEnabled()));
		Helper.compareTrue(testConfig, "Name textbox disabled", (!Name.isEnabled()));
		Helper.compareTrue(testConfig, "Email textbox disabled", (!Email.isEnabled()));	
	}
	

	/**Modifies Roles and Action checkboxes for the user in following steps
	 * Unchecks all roles and action checkboxes
	 * Checks roles and action checkboxes as specified in Existing user object
	 * Clicks Confirm
	 * @param ExistingUser - Object of Merchant User
	 * @return Manage User Page
	 */
	public ManageUsersProfilesAndRoles modifyRolesAndActionAccess(newMerchantUser ExistingUser){
		List<WebElement> listOfcheckedRoles = Element.getListOfElements(
				testConfig, How.css, "li.on span");
		for (WebElement checkedRole : listOfcheckedRoles) {
			Element.click(testConfig, checkedRole,
					"Roles and action checkbox");
		}
		for (String AccessRoleName : ExistingUser.getAccessRolesList()) {
			Element.click(testConfig, getUserAccessRole(AccessRoleName),
					AccessRoleName + " checkbox");
		}
		return clickConfirm();
	}
}
