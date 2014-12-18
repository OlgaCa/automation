package PageObject.AdminPanel.Payments.Response;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class ThreeDSecurePage {

	String actualAmount="";

	private Config testConfig;

	@FindBy(id="uid_tb_r")
	private WebElement citiIPINRadio;
	
	@FindBy(css="input.cancel_btn")
	private WebElement citiIPINCancel;
	
	@FindBy(id="submitButton")
	private WebElement submitButton;
	
	public ThreeDSecurePage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.wait(testConfig, 15);
	}
	
	public void VerifyCITI_IssuingBank()
	{
		Element.verifyElementPresent(testConfig, citiIPINRadio, "CITI IPIN radio button");
		Element.click(testConfig, citiIPINRadio, "CITI IPIN radio button");
		Element.verifyElementPresent(testConfig, citiIPINCancel, "CITI IPIN cancel");
	}

}

