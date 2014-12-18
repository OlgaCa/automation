package PageObject.AdminPanel.Payments.Response;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantCCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionMobilePage;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;

public class TryAgainPage {

	private Config testConfig;

	public TryAgainPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, HeaderImage);
	}

	@FindBy(css="div.card-note")
	private WebElement AmountErrorMessage;
	public String getAmountErrorMessage() {
		return AmountErrorMessage.getText();
	}

	@FindBy (css="p.reason-for-failure")
	private WebElement ReasonforFailure;
	public String getReasonforFailure() {
		return ReasonforFailure.getText();
	}
	
    @FindBy(xpath="//div[@id='retry']/div/div[2]/p")
    private WebElement reasonForInternationalFailure;
    public String getReasonForInternationalFailure(){
    	return reasonForInternationalFailure.getText();
    }
	@FindBy (id="header")
	private WebElement HeaderImage;

	@FindBy (id="try-again-button")
	private WebElement TryAgain;
	public CCTab clickTryAgainButton() {
		Element.click(testConfig, TryAgain, "Try Again Button");
		return new CCTab(testConfig);
	}
	

	@FindBy (linkText="Cash on delivery")
	private WebElement codButton;
	
	
	@FindBy(xpath="//div[@id='retry']/div[2]/p[2]/span/a")
	private WebElement Cancel;
	
	public String automationMerchantLinkForTransaction(){
		return Cancel.getText();
	}
	
	public PaymentOptionsPage ClickCOD(){
		Element.click(testConfig, codButton, "COD button");
		if(testConfig.isMobile)
			return new PaymentOptionMobilePage(testConfig);
		else
			return new PaymentOptionsPage(testConfig);
	}
	
	public BankRedirectPage ClickCODBank(){
		Element.click(testConfig, codButton, "COD button");
		return new BankRedirectPage(testConfig);
	}
	
	public BankRedirectPage ClickCancel(){
		Element.click(testConfig, Cancel, "Cancel link");
		return new BankRedirectPage(testConfig);
	}


	public void verifyPageElements()
	{
		Helper.compareTrue(testConfig, "Header Image", HeaderImage.isDisplayed());
		Helper.compareTrue(testConfig, "Try Again Button", TryAgain.isDisplayed());
		Helper.compareTrue(testConfig, "COD Button", codButton.isDisplayed());
	}
	
	public void verifyRetryForPageElements()
	{
		Helper.compareTrue(testConfig, "Header Image", HeaderImage.isDisplayed());
		Helper.compareTrue(testConfig, "Try Again Button", TryAgain.isDisplayed());
		Helper.compareTrue(testConfig, "COD Button", codButton.isDisplayed());
	}

	/**
	 * Clicks on merchant name present on tryagain page and returns response page
	 * @return
	 */
	public TestResponsePage clickOnMerchant() {
		Element.click(testConfig, Cancel, Cancel.getText());
		return new TestResponsePage(testConfig);
	}
}
