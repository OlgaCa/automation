package PageObject.AdminPanel.Payments.Response;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;

public class OfferPage {
	
	private Config testConfig;

	@FindBy(css="div.pu_cnt input[id=payment1]")
	WebElement bookButton;
	
	@FindBy(xpath="//div[@id='offer1']/span[2]")
	WebElement discountText;
	
	public OfferPage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, bookButton);
	}
	
	public TestResponsePage clickBookButton()
	{
		Element.doubleClick(testConfig, bookButton, "Book button");
		int retries=5;
	
		while (retries>0){
			Browser.wait(testConfig, 1);
			if (Element.IsElementDisplayed(testConfig, bookButton)){
			Element.doubleClick(testConfig, bookButton, "Book button");
			Browser.wait(testConfig, 1);
			retries--;
		}
			else
				break;
		}
		return new TestResponsePage(testConfig);
	}

	

}
