package PageObject.NewMerchantPanel.Overview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.MerchantPanel.Home.PayUHomePage;
import PageObject.NewMerchantPanel.Activity.ActivityPage;
import PageObject.NewMerchantPanel.Billings.BillingsPage;
import PageObject.NewMerchantPanel.DashBoard.DashboardPage;
import PageObject.NewMerchantPanel.ManageUsers.ManageUsersProfilesAndRoles;
import PageObject.NewMerchantPanel.MyProfile.MyProfilePage;
import PageObject.NewMerchantPanel.OffersPage.ManageAndCreateOffersPage;
import PageObject.NewMerchantPanel.SystemSettings.NotificationCenter;
import PageObject.NewMerchantPanel.Transactions.RequestsPage;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage;
import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;

public class MerchantPanelPage {

	@FindBy(css = "ul>li:nth-of-type(1)>a.first.ng-binding")
	private WebElement transactionsTab;

	@FindBy(linkText = "Activity")
	private WebElement activityTab;

	@FindBy(linkText = "Requests")
	private WebElement requestsTab;

	@FindBy(linkText = "Billings")
	private WebElement billingTab;
	
	@FindBy(linkText = "Offers")
	private WebElement offers;
	
	@FindBy(css = "#ng-app>body>div:nth-child(6)>div:nth-child(2)>div:nth-child(2)>div:nth-child(1)>div:nth-child(2)>div:nth-child(1)")
	private WebElement statusMessage;
	
	@FindBy(linkText = "BULK UPLOAD")
	private WebElement bulkUpload;
	
	@FindBy(css = "span.ng-binding")
	private WebElement filterType;

	@FindBy(css = "#BulkAction>div>div>select")
	private WebElement selectAction;

	@FindBy(css = ".relative>input")
	private WebElement fileToUpload;
	
	@FindBy(linkText = "Sign Out")
	private WebElement signOut;

	@FindBy(css = "div.navbar-right > div.dropdown > a.dropdown-toggle > span")
	private WebElement myAccount;
	
	@FindBy(linkText = "OK")
	private WebElement ok;

	@FindBy(linkText = "UPLOAD & PROCESS")
	private WebElement uploadProcess;

	@FindBy(linkText = "Click to Download Response File")
	private WebElement dwnloadFile;
	
	@FindBy(css=".footer.ng-scope>a")
	private WebElement notificationClose;
	
	@FindBy(linkText = "Transactions")
	private WebElement transTab;
	
	@FindBy(linkText = "PayU ID")
	private WebElement payID;

	@FindBy(css = "li.ng-scope > a.ng-binding")
	private WebElement transactionID;

	@FindBy(linkText = "Request ID")
	private WebElement requestID;

	@FindBy(css = "Customer Name")
	private WebElement customerName;

	@FindBy(linkText = "Customer Email ID")
	private WebElement customerEmail;

	@FindBy(linkText = "Reference ID")
	private WebElement referenceID;

	@FindBy(css = "#SearchBarDiv>div>div.search_input>form>input")
	private WebElement search;

	@FindBy(css = "button.btn")
	private WebElement searchButton;
	
	@FindBy(css="div.navbar-right > div.dropdown > a.dropdown-toggle > span")
	private WebElement MyAccountDropDown;
	
	@FindBy(css=".dropdown.open")
	private WebElement expandedMenu;
	
	@FindBy(linkText="My Profile")
	private WebElement MyProfileOption;
	
	@FindBy(linkText="Manage Users")
	private WebElement ManageUsersOption;
	
	@FindBy(linkText="System Settings")
	private WebElement SystemSettingsOption;
	
	@FindBy(linkText="Sign Out")
	private WebElement SignOutOption;
	
	@FindBy(css=".divProfileDetails")
	private WebElement profileDetails;
	
	@FindBy(css="#SideBar>div>.buttons>a")
	private List<WebElement> sidebarNavigationButtons;

	@FindBy(css="#SideBar>div>ul>li>a")
	private List<WebElement> sidebarNavigationTabs;
	
	

	private Config testConfig;

	public MerchantPanelPage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		if(Element.IsElementDisplayed(testConfig, notificationClose)){
			Element.click(testConfig, notificationClose, "Close button For Notification");
		}
		Browser.waitForPageLoad(this.testConfig, MyAccountDropDown);
	}
	
	/**
	 * Method used to go to Transactions page
	 * 
	 * @return
	 */
	public TransactionsPage ClickTransactionsTab() {
		Browser.wait(testConfig, 2);
		Element.click(testConfig, transTab, "Transactions Tab");
		Element.click(testConfig, transactionsTab, "Click On Transaction tab");
		return new TransactionsPage(testConfig);

	}

	/**
	 * Method used to go to Transactions page
	 * 
	 * @return
	 */
	public RequestsPage ClickRequestsTab() {
		Element.click(testConfig, transTab, "Transactions Tab");
		Element.click(testConfig, requestsTab, "Click Requests tab");
		return new RequestsPage(testConfig);
	}
	
	/**
	 * Method used to navigate to Billings Page
	 * @return
	 */
	 public BillingsPage clickBilling() {
	 Element.click(testConfig, billingTab, "BillingTab");
	 return new BillingsPage(testConfig);
	 }
	
	 /**
	  * Method used to Navigate To Activity Page
	  * @return
	  */
	 public ActivityPage clickActivity() {
	 Element.click(testConfig, activityTab, "Activity Tab");
	 return new ActivityPage(testConfig);
	 }

	 /**
	 * Method used to go to Offers page
	 *
	 * @return
	 */
	 public ManageAndCreateOffersPage ClickOffersTab() {
	
	 Element.click(testConfig, offers, "Click offers tab");
	 return new ManageAndCreateOffersPage(testConfig);
	 }
	 
	 public enum filterTypes {TransactionID, PayUID, ReferenceID, RequestedID,CustomerName,CustomerEmail}; 
		
	 /**
		 * Method used to verify weather transaction ID id listed in Merchant Panel
		 * or not.
		 * 
		 * @param transID
		 * @return
		 * @throws InterruptedException
		 */
		public void filterBasedOn(filterTypes filterBy) {

			Element.click(testConfig, filterType, "Select the Filter type");

			switch (filterBy) {

			case TransactionID:

				Element.click(testConfig, transactionID, "Selecting Transaction ID");

				break;

			case PayUID:

				Element.click(testConfig, payID, "Selecting PayU ID");

				break;

			case ReferenceID:

				Element.click(testConfig, referenceID, "Selecting REFERENCE ID");

				break;

			case RequestedID:

				Element.click(testConfig, requestID, "Filter by Request ID");

				break;

			case CustomerName:

				Element.click(testConfig, customerName, "Selecting Customer Name");

				break;

			case CustomerEmail:

				Element.click(testConfig, customerEmail,
						"Selecting Customer Email ID");

				break;

			default:
				testConfig.logComment("Filter type is not avaialble");
				break;
			}

		}
		/**Verifies transaction menu on page
		 * Verifies top link transaction is present
		 * Verifies submenu options Transactions and Requests 
		 * are also present 
		 * 
		 */
		private void verifyTransactionTab() {
			Element.verifyElementPresent(testConfig,
					transTab,
					"Transactions Sidebar Link");
			transTab.click();
			Element.verifyElementPresent(testConfig,
					requestsTab,
					"Requests Sub Menu");
			Element.verifyElementPresent(testConfig,
					transactionsTab,
					"Transactions Sub Menu");
			
		}
		
		
		/**
		 * Method used to enter search text and click search button
		 * @param searchKey
		 */
		public void searchData(String searchKey) {
			Element.enterData(testConfig, search, searchKey, "Enter search key");
			Element.click(testConfig, searchButton, "Click on GO Button");
		}
		
		public enum subMenu {MyProfile, ManageUsers, SystemSettings, SignOut}; 
		
		/**Clicks on my account dropdown present on top right corner
		 * Selects option specified
		 * @param menuOption - Name of the option to be clicked
		 * @return page object corresponding to menu option
		 */
		public Object clickOnMyAccount(subMenu menuOption){
			MyAccountDropDown.click();
			Element.waitForElementDisplay(testConfig, expandedMenu);
			Object returnpage = null;
			switch (menuOption) {
			case MyProfile:
							Element.click(testConfig, MyProfileOption, "My Profile option");
							returnpage =  new MyProfilePage(testConfig);
				break;
			case ManageUsers:
							Element.click(testConfig, ManageUsersOption, "Manage User option");
							returnpage = new ManageUsersProfilesAndRoles(testConfig);
				break;
			case SystemSettings:
							Element.click(testConfig, SystemSettingsOption, "System Settings option");
							returnpage = new NotificationCenter(testConfig);
				break;
			case SignOut: Element.click(testConfig, SignOutOption, "Sign Out option");
							returnpage = new PayUHomePage(testConfig);
				break;
				default:
					testConfig.logComment("Drop Down option is not available");

			}
			return returnpage;
		}
		/**Verifies My Account dropdown and its values present on top-right corner
		 * @param menuOptionsToBeVisible - name of the options that should be displayed 
		 * in the dropdown
		 */
		public void verifyMyAccountDropDown(ArrayList<subMenu> menuOptionsToBeVisible) {
			MyAccountDropDown.click();
			Element.waitForElementDisplay(testConfig, expandedMenu);
			for (subMenu option : menuOptionsToBeVisible) {
				switch (option) {
				case MyProfile:
					Element.waitForVisibility(testConfig, MyProfileOption,
							"My Profile option");
					break;
				case ManageUsers:
					Element.waitForVisibility(testConfig, ManageUsersOption,
							"Manage User option");
					break;
				case SystemSettings:
					Element.waitForVisibility(testConfig, SystemSettingsOption,
							"System Settings option");
					break;
				case SignOut:
					Element.waitForVisibility(testConfig, SignOutOption,
							"Sign Out option");
					break;
					default:
						testConfig.logComment("DropDpwn Option is not available");
				}
			}
			//closing dropdown
			MyAccountDropDown.click();
		}
		

		/**Gets Webelemnt for specified navigation link that is present on navigation bar
		 * @param NavigationName
		 * @return Webelemnt of menu link
		 */
		private WebElement getSidebarNavigationLink(String NavigationName){
			for (WebElement NavigationMenu : sidebarNavigationTabs) {
				String sideBarName =NavigationMenu.getText();
			if(sideBarName.contains(NavigationName))	{
					return NavigationMenu;
				}
			}
			testConfig.logFail("No Sidebar Menu found by the name of "+ NavigationName);
			return null;
		}
		
		/**Verifies Page UI of a user based on access roles it possesses
		 * @param accessRoles - Name of access roles
		 */
		public void verifyPageUiBasedOnAccessRoles(ArrayList<String> accessRoles) {
			// default menu options but in case of super
			ArrayList<subMenu> menuOptionsToBeVisible = new ArrayList<MerchantPanelPage.subMenu>();
			menuOptionsToBeVisible.addAll(Arrays.asList(subMenu.SignOut,
					subMenu.MyProfile) );
			// verify tabs
			if (accessRoles.isEmpty()) {
				Element.verifyElementPresent(testConfig, profileDetails,
						"Profile Details");
			}
			boolean checkActionRoles =false;
			for (String accessRole : accessRoles) {
				// check for corresponding columns
				switch (accessRole) {
				case "Hide Panel Fields":
					if(accessRoles.contains("View Transactions")){
						TransactionsPage transactionPage=new TransactionsPage(testConfig);
						transactionPage.verifyCustomizationOptionsNotPresent();
					}
					else{
					Element.verifyElementPresent(testConfig, profileDetails,
							"Profile Details");
					}
					break;
				case "Export To Excel": 
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Downloads"),
							"Downloads navigation Link");
					break;
				case "View Dashboard": 
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Overview"),
							"Overview navigation Link");
					DashboardPage dashboardPage=new DashboardPage(testConfig);
					dashboardPage.verifyAmountHeaders();
					break;
				case "Manage Templates":
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Customization"),
							"Customization navigation Link");
					break;
				case "IVR Transacton":
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationButton("INITIATE BY IVR "),
							"Initiate By IVR button");
					break;
				case "email_invoice":
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationButton("Bulk Upload"),
							"Bulk Upload button");
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Downloads"),
							"Downloads navigation Link");
					break;
				case "search":
				case "View Transactions":
						verifyTransactionTab();
						Element.verifyElementPresent(testConfig, search, "Search Bar");
					break;
				case "Billings":
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Billings"),
							"Billings navigation Link");
					break;
				case "View Activity":
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Activity"),
							"Activity navigation Link");
					break;
				case "Create Offer":
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Offer"),
							"Offer navigation Link");
					break;
				case "Create Invoice":
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationButton("New Email Invoice"),
							"New Email Invoice navigation Link");
					break;
				case "Generate Invoice":
					verifyTransactionTab();
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Downloads"),
							"Downloads navigation Link");
					break;
				case "COD Cancel":
				case "Cancel Transactions":
				case "COD Verify":
				case "Refund":
				case "Capture":
				case "COD Settled":
					verifyTransactionTab();	
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationButton("Bulk Upload"),
							"Bulk Upload button");
					//Flag on for function verifyActionDropdownValues
					//Executed later in the flow
					checkActionRoles=true;
					break;
				case "super":
					menuOptionsToBeVisible.addAll(Arrays.asList(
							subMenu.ManageUsers,subMenu.SystemSettings));
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Overview"),
							"Overview navigation Link");
					verifyTransactionTab();	
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Billings"),
							"Billings navigation Link");
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Offer"),
							"Offer navigation Link");
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Customization"),
							"Customization navigation Link");
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Activity"),
							"Activity navigation Link");
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationLink("Downloads"),
							"Downloads navigation Link");
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationButton("New Email Invoice"),
							"New Email Invoice navigation Link");
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationButton("INITIATE BY IVR"),
							"Initiate By IVR button");
					Element.verifyElementPresent(testConfig,
							getSidebarNavigationButton("Bulk Upload"),
							"Bulk Upload button");
					//Flag on for function verifyActionDropdownValues
					//Executed later in the flow
					checkActionRoles =true;
					break;
				}
			}
			verifyMyAccountDropDown(menuOptionsToBeVisible);
			if(checkActionRoles){
				TransactionsPage transactionPage=new TransactionsPage(testConfig);
				transactionPage.verifyActionDropdownValues(accessRoles);
			}
		}
		
		
		
		
		/** Returns element for specified button name to be present in sidebar
		 * @param ButtonName - Name of the button on side bar navigation menu
		 * @return Element of button
		 */
		private WebElement getSidebarNavigationButton(String ButtonName){
			for (WebElement Button : sidebarNavigationButtons) {
				String sideBarName =Button.getText().toLowerCase();
			if(sideBarName.contains(ButtonName.toLowerCase()))	{
					return Button;
				}
			}
			testConfig.logFail("No Sidebar Button found by the name of "+ ButtonName);
			return null;
		}	
		
		/**
		 * Sets used to upload a file,verify message and validate the status
		 * 
		 * @param fileName
		 * @return
		 */
		public String uploadRefundFile(String fileName) {
			Element.click(testConfig, bulkUpload, "CLick bulk upload");

			Browser.wait(testConfig, 2);
			/*
			 * Element.enterData(testConfig, fileToUpload, fileName,
			 * "Enter file name");
			 */
			/*
			 * Element.enterFileName(testConfig, fileToUpload, fileName,
			 * "Enter file name");
			 */
			Browser.uploadFileWithJS(
					testConfig,
					"document.getElementsByClassName(\"relative\")[0].getElementsByTagName(\"input\")[0]",
					fileName, fileToUpload);
			Browser.wait(testConfig, 1);
			// Element.click(testConfig,selectAction, "CLickselect action");
			Element.selectVisibleText(testConfig, selectAction, "Refund",
					"click on refund");
			Browser.wait(testConfig, 1);
			Element.click(testConfig, uploadProcess, "CLick Process Refund");
			Browser.wait(testConfig, 2);
			// Element.click(testConfig, uploadProcess, "CLick Process Refund");
			verifyMessage();
			Element.click(testConfig, dwnloadFile, "CLick to download file");
			String requestId = verifyDownloadedFile();
			Element.click(testConfig, ok, "Click OK");
			return requestId;
		}
		/**
		 * Method used to validate refund message
		 * 
		 * @return
		 */
		public String verifyMessage() {
			Browser.wait(testConfig, 2);
			String message = statusMessage.getText();
			String msg[] = message.split("\n");
			Helper.compareEquals(testConfig, "Comparing message",
					"Successfully queued 1 out of 1 request(s).", msg[0]);
			String reference = msg[msg.length - 1];
			msg = reference.split(" ");
			reference = msg[msg.length - 1];
			return reference;

		}

		/**
		 * Method used to download file and validate status
		 * 
		 * @return
		 */
		public String verifyDownloadedFile() {
			DashboardHelper helper = new DashboardHelper(testConfig);
			String requestedId = helper.readExcelData("refund_summary",
					"request_id", 1);
			Helper.compareEquals(testConfig, "Comparing status",
					helper.readExcelData("refund_summary", "result", 1), "QUEUED");
			return requestedId;
		}
		
		/**
		 * Method used to logout of merchant
		 */
		public void doMerchantLogOut() {
			Element.click(testConfig, myAccount, "Click My Account ");
			Element.click(testConfig, signOut, "Click Sign Out Link");
		}
}
