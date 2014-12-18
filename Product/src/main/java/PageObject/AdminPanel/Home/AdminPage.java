package PageObject.AdminPanel.Home;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Element;
import Utils.Config;

public class AdminPage {

	@FindBy(linkText="Admin Login")
	private WebElement adminLogin;

	private Config testConfig;

	public AdminPage(Config testConfig)
	{
		this.testConfig = testConfig;
		String homeUrl = testConfig.getRunTimeProperty("AdminPortalHome");

		Browser.navigateToURL(testConfig, homeUrl);

		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, adminLogin);
	}

	/***
	 * Click on Admin Login Link
	 * @return Login page
	 */
	public LoginPage ClickAdminLogin()
	{
		Element.click(testConfig, adminLogin, "Admin Login link");
		return new LoginPage(testConfig);		
	}

}
