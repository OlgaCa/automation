package PageObject.MerchantPanel.Transactions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;

public class RequestConfirmationPage {
	private Config testConfig;

	@ FindBy(id="action_button")
	private WebElement actionButton;

	@ FindBy(id="cancel_button")
	private WebElement closeButton;
	
	@ FindBy(id="general_popup_content")
	private WebElement failureMessage;

	@FindBy(css="b")
	private WebElement refId;

	@ FindBy(css=".popup-id")
	private WebElement pop_up_id_message;
	
	public RequestConfirmationPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, closeButton);

	}
	public void ClickActionButton()
	{   
		Element.click(testConfig, actionButton, "Action Button");
		//Switch to Confirmation and Close Window
		//Browser.switchToNewWindow(this.testConfig);
			testConfig.putRunTimeProperty("token", refId.getText());
			Element.click(testConfig, closeButton, "Close Button on request processing Popup");
			//Browser.switchToNewWindow(this.testConfig);
	}
	
	/**
	 * Executes the specified cron.php file
	 * @param cronName
	 */
	public void executeCron(String cronFileName)
	{
		//Save the current URL
		String currentUrl = testConfig.driver.getCurrentUrl();
		
		//GO to cron URL
		String cronUrl = testConfig.getRunTimeProperty("CronUrl");
		Browser.navigateToURL(testConfig, cronUrl);
		Element.click(testConfig, Element.getPageElement(testConfig, How.linkText, cronFileName), "Executing the cron " + cronFileName);
		Browser.wait(testConfig, 5);
		
		//Restore the original URL
		Browser.navigateToURL(testConfig, currentUrl);
	}

	public String ClickActionButtonToGetFailure()
	{   
		Element.click(testConfig, actionButton, "Action Button");
		//Switch to Confirmation and Close Window
		Browser.switchToNewWindow(this.testConfig);
			String error = failureMessage.getText();
			Element.click(testConfig, closeButton, "Close Button on request processing Popup");
			Browser.switchToNewWindow(this.testConfig);
			
			return error;
	}
	
	public void ClickAction()
	{   
		
		//Switch to Confirmation and Close Window
		Browser.switchToNewWindow(this.testConfig);
			testConfig.putRunTimeProperty("token", refId.getText());
			Element.click(testConfig, closeButton, "Close Button on request processing Popup");
			Browser.switchToNewWindow(this.testConfig);
		}
		
	public void ClickActionconfirmButton()
	{   
		Element.click(testConfig, actionButton, "Action Button");
		//Switch to Confirmation and Close Window
		Browser.switchToNewWindow(this.testConfig);
			
	}
	public void ClickOKButton()
	{   
		Element.click(testConfig, actionButton, "Action Button");
	}

}