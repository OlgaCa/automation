package PageObject.MerchantPanel.Settings;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


//import PageObject.MerchantPanel.Payments.EmailInvoicePage;
import Utils.Browser;
import Utils.Element;
import Utils.Config;
import Utils.TestDataReader;

public class NewMerchantLoginPage {
	
	
	private Config testConfig;
/*	@FindBy(name="dropbox")
	private WebElement dropBox;
	*/
	
	@FindBy(id="login_created")
	private WebElement Submit;
	public NewMerchantLoginPage(Config testConfig)
	{
		Browser.wait(testConfig, 2);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, Submit);
	}
	//Browser.wait(testConfig, 2);
	
	@FindBy(name="merchant[name]")
	private WebElement merchantName;
	
	@FindBy(name="merchant[username]")
	private WebElement userName;
	
	@FindBy(name="merchant[password]")
	private WebElement passworD;
	
	@FindBy(name="merchant[confirm]")
	private WebElement confirmPassword;
	
	@FindBy(css="b#admin_created")
	private WebElement createdMsg;
	
	//Error Messages
	@FindBy(css="b")
	private WebElement errorMessages;
	
	public String getErrorMessage() {
		return errorMessages.getText();
	}
	
	public String getconfirmationMessage() {
		return createdMsg.getText();
	}

	public void clickSaveButton() {
		Element.click(testConfig, Submit, "Click on Submit");

	}
	
	
	// Create Login through login form
			// Entering data from Excel sheet "UserLoginDetails"
			public TestDataReader fillCreateLoginForm(int loginRowNum) {
				TestDataReader userLoginDetails= new TestDataReader(testConfig,
						"UserLoginDetails");
				String value = "";
				String value1="";
				String value2="";
			
				value = userLoginDetails.GetData(loginRowNum, "name");
				Element.enterData(testConfig, merchantName, value, "Enter name");
				
				testConfig.putRunTimeProperty("name",value);
				
				value1 = userLoginDetails.GetData(loginRowNum, "username");
				Element.enterData(testConfig, userName, value1, "Enter username");
				
				testConfig.putRunTimeProperty("usrName",value1);
				
				value2 = userLoginDetails.GetData(loginRowNum, "password");
				Element.enterData(testConfig, passworD, value2, "Enter password");
				
				testConfig.putRunTimeProperty("usrPassword", value2);
				
				value = userLoginDetails.GetData(loginRowNum, "rePassword");
				Element.enterData(testConfig, confirmPassword, value, "Enter confirm password");

				return userLoginDetails;
			}

	
	
	
}
