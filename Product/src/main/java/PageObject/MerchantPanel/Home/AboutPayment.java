package PageObject.MerchantPanel.Home;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;

public class AboutPayment {
	
	private Config testConfig;

	@FindBy(linkText="New to Payments")
	private WebElement newToPayments;
	
	public AboutPayment(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, newToPayments);
	}	
	
}
