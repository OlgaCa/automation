package PageObject.AdminPanel.Payments.Response;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;

public class ErrorResponsePage {

	private Config testConfig;

	public ErrorResponsePage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, HeaderImage);
	}

	@FindBy(xpath="//div[@id='content']/div/h4")
	private WebElement AmountErrorMessage;
	public String getAmountErrorMessage() {
		return AmountErrorMessage.getText();
	}

	@FindBy (xpath="//div[@id='content']/div/div")
	private WebElement ReasonforFailure;
	public String getReasonforFailure() {
		return ReasonforFailure.getText();
	}
	
	@FindBy (css="img")
	private WebElement HeaderImage;

	@FindBy (id="header")
	private WebElement CrossImage;
	

	@FindBy(css="div#footer ul#fotter_logo li.visa")
	private WebElement verifiedByVisaLink;

	public void VerifyVerifiedByVisaLinkUrl(String expected)
	{
		Element.click(testConfig, verifiedByVisaLink, "Verified By Visa");
		String handle = Browser.switchToNewWindow(testConfig);
		Browser.verifyURL(testConfig, expected);
		Browser.switchToGivenWindow(testConfig, handle);
	}

	@FindBy(css="div#footer ul#fotter_logo li.master")
	private WebElement masterCardLink;

	public void VerifyClickMasterCardLinkUrl(String expected)
	{
		Element.click(testConfig, masterCardLink, "Master Card Link");
		String handle = Browser.switchToNewWindow(testConfig);
		Browser.wait(testConfig, 15);
		Browser.switchToGivenWindow(testConfig, handle);
	}

	@FindBy(css="div#footer ul#fotter_logo li.verisign")
	private WebElement veriSignSecuredLink;

	public void VerifyClickVeriSignSecuredLinkUrl(String expected)
	{
		Element.click(testConfig, veriSignSecuredLink, "VeriSign Secured Link");
		String handle = Browser.switchToNewWindow(testConfig);
		Browser.verifyURL(testConfig, expected);
		Browser.switchToGivenWindow(testConfig, handle);
	}

	@FindBy(css="div#footer ul#fotter_logo li.pci") 
	private WebElement pciLink;

	public void VerifyClickPciLink(String expected)
	{
		Element.click(testConfig, pciLink, "PCI Link");
		String handle = Browser.switchToNewWindow(testConfig);
		Browser.wait(testConfig, 15);
		Browser.switchToGivenWindow(testConfig, handle);
	}

	public void verifyPageElements()
	{
		Helper.compareTrue(testConfig, "Header Image", HeaderImage.isDisplayed());
		Helper.compareTrue(testConfig, "Cross Image", CrossImage.isDisplayed());
		Helper.compareTrue(testConfig, "Verified By Visa Link", verifiedByVisaLink.isDisplayed()); 
		Helper.compareTrue(testConfig, "Master Card Link", masterCardLink.isDisplayed());
		Helper.compareTrue(testConfig, "VeriSign Secured Link", veriSignSecuredLink.isDisplayed());
		Helper.compareTrue(testConfig, "pci Link", pciLink.isDisplayed());
	}
}
