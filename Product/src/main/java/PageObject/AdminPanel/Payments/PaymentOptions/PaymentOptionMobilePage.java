package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import Utils.Config;


public class PaymentOptionMobilePage extends PaymentOptionsPage 
{

	public PaymentOptionMobilePage(Config testConfig) {
		super(testConfig);
	}

	//Pay now
	@FindBy(xpath = "//form[@id='CreditCardDetailsForm']/div/div[2]/input")
	WebElement payNow;
	public WebElement getPayNow() {
		return payNow;
	}

	//Tabs
	@FindBy(linkText="Credit Card")
	private WebElement CCTab;
	public WebElement getCCTab() {
		return CCTab;
	}

}
