package PageObject.AdminPanel.Settlement;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Helper;


public class InvalidFilePage {

	private Config testConfig;

	@FindBy(xpath="/html/body")
	private WebElement message;

	public InvalidFilePage (Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.wait(testConfig, 3);
	}

	public void verify_message(Config testConfig){
		Helper.compareEquals(testConfig, "Invalid File upload message", "The file uploaded is not a valid excel (.xls) file", message.getText());
	}
}
