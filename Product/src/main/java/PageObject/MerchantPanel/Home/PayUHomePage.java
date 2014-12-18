package PageObject.MerchantPanel.Home;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Element;
import Utils.Config;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestDataReader;

public class PayUHomePage {

	private Config testConfig;

	@FindBy(name="login[username]")
	private WebElement merchantName;

	@FindBy(name="login[alias]")
	private WebElement merchantAlias;

	@FindBy(id="passwd1")
	private WebElement password;

	@FindBy(id="sign_in")
	private WebElement signin;

	@FindBy(id="sign_in_button")
	private WebElement click;
	
	@FindBy(css="input.signup_btn")
	private WebElement signUpButton;
	

	public PayUHomePage(Config testConfig)
	{
		this.testConfig = testConfig;
		String homeUrl = testConfig.getRunTimeProperty("MerchantPanelHome");
		Browser.navigateToURL(testConfig, homeUrl);
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, signin);
	}

/*	*//**
	 * Do login on Merchant Panel
	 * @return Dashboard page
	 *//*
	public TestDataReader  ClickMerchantLogin(int merchantRow)
	{
		String user = testConfig.getRunTimeProperty("MerchantUser");
		String pwd = testConfig.getRunTimeProperty("MerchantPassword");

		Element.click(testConfig, signin, "Click Signin");

		Element.enterData(testConfig, merchantName, user, "User name");

		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetData(merchantRow, "key");


		Element.enterData(testConfig, merchantAlias, value, "Select merchant alias");		
		Element.enterData(testConfig, password, pwd, "Password");

		Element.click(testConfig, click, "Click on Signin");
		return data;

	}	*/
	
	/**
	 * Fill login details on Merchant Panel
	 */
	public TestDataReader  fillMerchantLogin(int merchantRow)
	{
		String user = testConfig.getRunTimeProperty("MerchantUser");
		String pwd = testConfig.getRunTimeProperty("MerchantPassword");

		Element.click(testConfig, signin, "Signin");

		Element.enterData(testConfig, merchantName, user, "User name");

		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetData(merchantRow, "key");


		Element.enterData(testConfig, merchantAlias, value, "Merchant alias");	
		if((testConfig.getRunTimeProperty("isWallet")==null)) {
		Element.enterData(testConfig, password, pwd, "Password");
		}
		
		else{
			pwd= "Password~1";
			Element.enterData(testConfig, password, pwd, "Password");
		}

		return data;

	}

	
	/**
	 * click login button
	 * @return DashboardPage
	 */
	public void clickMerchantLogin()
	{
		Element.click(testConfig, click, "Signin");
	}
	
	public DashboardPage clickDashboard() 
	{
		//Element.click(testConfig,dashboardTab, "Click on DashboardTab");
		return new DashboardPage(testConfig);
		
	}
	
	public void verifyLoginFaliure() {
		Browser.wait(testConfig, 6);
		WebElement loginError = Element.getPageElement(testConfig, How.id, "show_error");
		//Browser.waitForPageLoad(testConfig, loginError);
		if(Element.IsElementDisplayed(testConfig, loginError))
			Helper.compareEquals(testConfig, "verifying login error", "Your account is not active, please check back later", loginError.getText().trim());
		}
	
	public void fillMerchantLogin(String name, String pwd, String value)
	{
		
		Element.click(testConfig, signin, "Signin");

		Element.enterData(testConfig, merchantName, name, "User name");
		Element.enterData(testConfig, merchantAlias, value, "Merchant alias");		
		Element.enterData(testConfig, password, pwd, "Password");
		

	}
	
	public void verifyElementsPresent(Config testConfig)
	{
		Element.verifyElementPresent(testConfig, signin, "signin button");
		//Element.verifyElementPresent(testConfig, signUpButton, "sign up button");
		
	}
	
	public void VerifyFailureMessage(String FailureMessage) {
		WebElement loginError = Element.getPageElement(testConfig, How.id, "show_error");
		Browser.waitForPageLoad(testConfig, loginError);
		Helper.compareEquals(testConfig, "Login Failure Message", FailureMessage, loginError.getText().trim());
	}

}

