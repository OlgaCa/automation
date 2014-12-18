package PageObject.AdminPanel.Payments.IframePaymentOptions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import Utils.Config;
import Utils.Element;
import Utils.Element.How;

public class IframePaymentOptionPage {
	public static WebDriver driver;

	/**
	 * Find webelement for credit card tab and clicks on credit card tab
	 * @return IframeTransactionPaymentCCPage
	 */

	public IframeCCTab clickCCTab() {
		WebElement creditCard;
		creditCard = Element.getiFrameElement(testConfig, How.css, "a");
		Element.click(testConfig, creditCard, "Credit Card Tab");
		return new IframeCCTab(testConfig);
	}
	
	/**
	 * Find webelement for Debit card tab and clicks on Debit card tab
	 * @return IframeTransactionPaymentDCPage
	 */
	public IframeDCTab clickDCTab() {
		WebElement debitCard = Element.getiFrameElement(testConfig, How.linkText, "Debit Card");
		Element.click(testConfig, debitCard, "Debit Card Tab");
		return new IframeDCTab(testConfig);
	}
	private Config testConfig;
	public IframePaymentOptionPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Element.getiFrameElement(testConfig, How.css, "input[type='submit']");
	}	

}