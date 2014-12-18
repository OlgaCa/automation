package PageObject.AdminPanel.Payments.Response;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;

public class EnforceWrongPage {
	
	private Config testConfig;

	@FindBy(id="content")
	WebElement  WrongEnforcePage;
	
	
	public EnforceWrongPage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, WrongEnforcePage);
	}
	
	public TestResponsePage verifyMessage()
	{
		Helper.compareContains(testConfig, "message Text on processing page", "Wrong Payment Method Selected.",WrongEnforcePage.getText());
		return new TestResponsePage(testConfig);
	
	}

	

}
