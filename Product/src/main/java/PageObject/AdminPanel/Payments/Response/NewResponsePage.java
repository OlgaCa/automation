package PageObject.AdminPanel.Payments.Response;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Helper;

public class NewResponsePage {

	private Config testConfig;

	public NewResponsePage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.wait(this.testConfig, 5);
	}

	@FindBy(css="span.failed")
	private WebElement failResponse;
	public String getFailresponseMessage() {
		return failResponse.getText();
	}

	@FindBy(css="li")
	private WebElement responseText;
	public String getResponseText() {
		return responseText.getText();
	}

	@FindBy(css="span.congrats")
	private WebElement successResponse;
	public String getSuccessresponseMessage() {
		return successResponse.getText();
	}

	public void verifyPageText(boolean testType)
	{
		if(testType==true)
		{
			Helper.compareContains(testConfig, "Success Response text verification", "Congratulations", getSuccessresponseMessage());
			Helper.compareTrue(testConfig, "Success response Message", successResponse.isDisplayed());
		}
		else if(testType==false)
		{
			Helper.compareContains(testConfig, "Failure Response text verification", "Transaction Failed", getFailresponseMessage());
			Helper.compareTrue(testConfig, "Failure response Message", failResponse.isDisplayed());
		}
	}
}
