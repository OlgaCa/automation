package PageObject.AdminPanel.WebServices;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;

public class TestwsResponsePage {

	private Config testConfig;

	@FindBy(css="pre")
	private WebElement response;

	public TestwsResponsePage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, response);
	}

	public String getResponse()
	{
		return response.getText();
	}

}
