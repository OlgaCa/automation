package PageObject.AdminPanel.Home;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Element;
import Utils.Config;


public class LoginPage {

	private Config testConfig;

	@FindBy(id="username")
	private WebElement userName;

	@FindBy(id="password")
	private WebElement password;

	@FindBy(id="Submit")
	private WebElement submit;

	LoginPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, userName);
	}

	/**
	 * Do login on Admin Panel
	 * @return Admin home page
	 */
	public HomePage Login()
	{
		String user = testConfig.getRunTimeProperty("AdminUser");
		String pwd = testConfig.getRunTimeProperty("AdminPassword");

		Element.enterData(testConfig, userName, user, "User name");
		Element.enterData(testConfig, password, pwd, "Password");

		Element.click(testConfig, submit, "Submit Button");

		return new HomePage(testConfig);
	}
}
