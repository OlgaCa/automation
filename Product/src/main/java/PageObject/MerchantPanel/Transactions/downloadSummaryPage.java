package PageObject.MerchantPanel.Transactions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;

public class downloadSummaryPage {
	private Config testConfig;


	@ FindBy(id="cancel_button")
	private WebElement closeButton;

	@FindBy(id="general_popup_content")
	private WebElement refId;


	public downloadSummaryPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, closeButton);

	}

	public void ClickAction()
	{   

		String RefID=refId.getText();
		RefID = RefID.substring(55, 68);
		
		testConfig.putRunTimeProperty("token",RefID);
		testConfig.logComment("***********"+RefID);
		Element.click(testConfig, closeButton, "Close Button on request processing Popup");
		Browser.switchToNewWindow(this.testConfig);
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
	
}