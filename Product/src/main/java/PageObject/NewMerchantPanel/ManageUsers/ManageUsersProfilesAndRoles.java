package PageObject.NewMerchantPanel.ManageUsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Test.NewMerchantPanel.UserHelper.newMerchantUser;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;

public class ManageUsersProfilesAndRoles {

	private Config testConfig;
	
	
	@FindBy (css= ".btn.btn-success")
	private WebElement CreateNewUser;
	
	@FindBy (css= ".alert>span")
	private WebElement AlertNotification;
	
	@FindBy (css= ".popover-content")
	private WebElement popup;
	
	@FindBy (css= ".popover-content>div>button:nth-of-type(1)")
	private WebElement popupConfirm;
	
	@FindBy (css= ".popover-content>div>button:nth-of-type(2)")
	private WebElement popupCancel;
	
	@FindBy (css= "li.usersListLi>.divProfileDetails")
	private List<WebElement> userProfiles;
	
	HashMap<String, UserProfile> userProfileList;
	
	public enum popupDeleteButtons{Confirm,Cancel};
	
	public ManageUsersProfilesAndRoles(Config testConfig){
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, CreateNewUser);
		initializeUserProfileObjects();
	}
	
	/**Initializes values for all objects present on page
	 * 
	 */
	private void initializeUserProfileObjects() {
		userProfileList = new HashMap<String, ManageUsersProfilesAndRoles.UserProfile>();
		for (WebElement userParentDiv : userProfiles) {
			UserProfile user = new UserProfile(userParentDiv);
			userProfileList.put(user.UserName, user);
		}
		
	}

	/** User profiles present on manage user page
	 * @author sharadjain
	 *
	 */
	private class UserProfile{
		
		//properties
		public String Name;
		private String UserName;
		private String email;
		private String Rights;
		private ArrayList<String> roles;
		private WebElement AccessRightsLink;
		private WebElement DeleteButton;
		private WebElement ManageButton;
		//behavior
		
		/**Clicks on manage button
		 * @return Existing user profile page
		 */
		public ManageUserProfilePage clickManage(){
			Element.click(testConfig, ManageButton, "Manage Button");
			return new ManageUserProfilePage(testConfig,this.UserName);
		}
		
		/**Clicks on delete button 
		 * Waits for delete pop up to appear
		 * Clicks on Confirm/Cancel
		 * @param buttonToBeSelected Confirm/Cancel
		 */
		public void delete(popupDeleteButtons buttonToBeSelected){
			Element.click(testConfig, DeleteButton, "Delete Button");
			Element.waitForElementDisplay(testConfig, popup);
			if(buttonToBeSelected==popupDeleteButtons.Confirm){
				Element.click(testConfig, popupConfirm, "Confirm Delete for " + UserName);
				Browser.wait(testConfig, 5);
			}
			else if(buttonToBeSelected==popupDeleteButtons.Cancel)
				Element.click(testConfig, popupCancel, "Cancel Delete");
		}
		
		/** Gets roles assigned to the user
		 * @return returns a list consisting of roles assigned
		 */
		public ArrayList<String> getRoleDetails(){
			Element.mouseMove(testConfig, AccessRightsLink, "Access Rights Link");
			List<WebElement>roleColumn =Element.getListOfElements(testConfig, How.css, "#rolesColumns>ul>li");
			roles = new ArrayList<String>();
			for (WebElement role : roleColumn) {
				roles.add(role.getText());
			}
			return roles;
		}
		
		
		public UserProfile(WebElement parentDiv){
			Name = parentDiv.findElement(By.cssSelector(".nameProfileDetails ")).getText();
			UserName = parentDiv.findElement(By.cssSelector("tr:nth-of-type(2)>td>.spanOtherProfileDetails")).getText();
			email = parentDiv.findElement(By.cssSelector("tr:nth-of-type(3)>td>.spanOtherProfileDetails")).getText();
			AccessRightsLink =parentDiv.findElement(By.cssSelector(".ancRoleProfileDetails "));
			Rights = AccessRightsLink.getText();
			//assign button values
			DeleteButton = parentDiv.findElement(By.cssSelector(".btn-manage.btn-delete"));
			ManageButton = parentDiv.findElement(By.cssSelector( ".btn-manage.btn-manage-user"));
		}
	}

	/** Clicks on create a new User button
	 * @return New User Page
	 */
	public CreateNewUsersPage clickCreateNewUser(){
		Element.click(testConfig, CreateNewUser, "Create New User");
		return new CreateNewUsersPage(testConfig);
	}

	/** Verifies Notification text displayed on the top of page
	 * @param NotificationText
	 */
	public void  verifyNotificationText(String NotificationText){
		testConfig.logComment(AlertNotification.getText());
		Helper.compareEquals(testConfig, "Alert Notification",NotificationText,AlertNotification.getText());
	}
	
	/**Deletes all existing users on page
	 * 
	 */
	public void deleteAllUsers(){
		//while there is a delete button present
		List<WebElement> deleteButtons = Element.getListOfElements(testConfig, How.css, ".btn-manage.btn-delete");
		while(deleteButtons.size()>0){
			deleteButtons.get(0).click();
			Element.waitForElementDisplay(testConfig, popup);
			Element.click(testConfig, popupConfirm, "Confirm Delete");
			Browser.wait(testConfig, 5);
			deleteButtons = Element.getListOfElements(testConfig, How.css, ".btn-manage.btn-delete");
		}
		testConfig.logComment("All users were deleted");
	}
	
	/**Deletes user with specified user name
	 * @param UserName
	 */
	public void deleteUser(String UserName){
		UserProfile userToBeDeleted = userProfileList.get(UserName);
		userToBeDeleted.delete(popupDeleteButtons.Cancel);//Step added for incorporating
														//cancel verification
		userToBeDeleted.delete(popupDeleteButtons.Confirm);
	}
	
	/**Clicks on manage button for specified user
	 * @param UserName
	 */
	public ManageUserProfilePage manageUser(String UserName){
		return userProfileList.get(UserName).clickManage();
	}

	/**Verifies Action and Roles displayed when Hovering rights Link on user's Profile
	 * Takes out list of roles assigned to user and verifies with strings displayed on
	 * pop up when hovered
	 * @param user - Object of user
	 */
	public void verifyUsersActionRolesDisplayedOnMouseHover(newMerchantUser user) {
		
		ArrayList<String> actualRolesAssigned = userProfileList.
										get(user.getUserName()).getRoleDetails();
		testConfig.logComment(actualRolesAssigned.toString());
		for (String expectedRoleAssigned : user.getAccessRolesList()) {
			//This particular string changes
			if (expectedRoleAssigned=="Allow underl...e password."){	
				expectedRoleAssigned="Allow underling to change password.";
			}
			if (actualRolesAssigned.contains(expectedRoleAssigned)) {
				testConfig.logComment(expectedRoleAssigned + " is displayed");
			}
			else testConfig.logComment(expectedRoleAssigned + " is  not displayed");
		}
	}

	/**
	 * @return Username of any existing user on the manage user page
	 */
	public String getUsernameOfExistingUser() {
		String UserName = userProfileList.entrySet().iterator().next().getKey();
		return UserName;
	}
	
}

