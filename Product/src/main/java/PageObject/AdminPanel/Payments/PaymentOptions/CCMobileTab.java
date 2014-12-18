package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import Utils.Config;

public class CCMobileTab extends CCTab
{
	public CCMobileTab(Config testConfig) 
	{
		super(testConfig);
	}

	@FindBy(name = "ccnum")
	WebElement CardNumber;
	@Override
	public WebElement getCardNumber() {
		return CardNumber;
	}

	@FindBy(name = "ccname")
	WebElement NameOnCard;
	@Override
	public WebElement getNameOnCard() {
		return NameOnCard;
	}

	@FindBy(name = "cccvv")
	WebElement Cvv;
	@Override
	public WebElement getCvv() {
		return Cvv;
	}

	@FindBy(name = "ccexpmon")
	WebElement ExpiryMonth;
	@Override
	public WebElement getExpiryMonth() {
		return ExpiryMonth;
	}

	@FindBy(name = "ccexpyr")
	WebElement ExpiryYear;
	@Override
	public WebElement getExpiryYear() {
		return ExpiryYear;
	}
}

