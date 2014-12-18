package PageObject.AdminPanel.Home;

import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;

public class EditDefaultGatewayPage {

	private Config testConfig;

	@FindBy(xpath="//h2[.='Payment Gateway Global Defaults']")
	private WebElement pageLabel;

	public EditDefaultGatewayPage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, pageLabel);
	}

	public void markGatewayStatusAsUp(String pg_type)
	{		
		WebElement statusUp = Element.getPageElement(testConfig, How.xPath, "//label[@for='gatewayStatusUp' and text()='" + pg_type + "']/following-sibling::input[@id='StatusUp']");	
		if(null == statusUp) return; //gateway might have been turned ON by some parallel running test
		Element.click(testConfig, statusUp, "Mark the status of Gateway -"+ pg_type +" as UP.");
	}
}
