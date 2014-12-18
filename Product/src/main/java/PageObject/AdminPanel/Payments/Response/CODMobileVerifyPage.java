package PageObject.AdminPanel.Payments.Response;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.PaymentOptions.CODTab;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;
import Utils.Element.How;

public class CODMobileVerifyPage {

	private Config testConfig;

	public CODMobileVerifyPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, ChangeMobileButton);
	}

	@FindBy(css="div.automatedDiv")
	private WebElement MobileVerifyText;

	@FindBy(css="div.callNumdiv img")
	private WebElement zipDialLink;

	@FindBy(xpath="//div[2]/p/span")
	private WebElement MobileNumber;

	@FindBy(css="div.shipping_address p")
	private WebElement ShippingDetails;

	@FindBy(id="changeDetails_cod")
	private WebElement ChangeDetailsButton;

	@FindBy(id="changeNumber_cod")
	private WebElement ChangeMobileButton;

	//Go back to merchant
	@FindBy(css = "div[class='go_badkDiv_form brdr']")
	private WebElement goBack;
	public String goBackText() {
		return goBack.getText();
	}

	//Footer section
	
	@FindBy(css="div.noteValue")
	private WebElement noteLabelOutBox;

	public String getFooterNoteText() {
		return noteLabelOutBox.getText();
	}
	
	@FindBy(css="div.footer-visa1")
	private WebElement verifiedByVisaLink;

	public String clickVerifiedByVisaLink()
	{
		Element.click(testConfig, verifiedByVisaLink, "Verified By Visa");
		return Browser.switchToNewWindow(testConfig);
	}

	@FindBy(css="div.footer-mastercard1")
	private WebElement masterCardLink;

	public String clickMasterCardLink()
	{
		Element.click(testConfig, masterCardLink, "Master Card Link");
		return Browser.switchToNewWindow(testConfig);
	}

	@FindBy(css="div.footer-verising1")
	private WebElement veriSignSecuredLink;

	public String clickVeriSignSecuredLink()
	{
		Element.click(testConfig, veriSignSecuredLink, "VeriSign Secured Link");
		return Browser.switchToNewWindow(testConfig);
	}

	@FindBy(css="div.footer-pci1") 
	private WebElement pciLink;

	public String clickPciLink()
	{
		Element.click(testConfig, pciLink, "PCI Link");
		return Browser.switchToNewWindow(testConfig);
	}

	@FindBy(css="div.footer-amex-safety1") 
	private WebElement amexsafety;

	public void AmexSafetyLinkNotPresent()
	{
		Element.verifyElementNotPresent(testConfig, amexsafety, "Amext Safety Link");
	}

	@FindBy(css="div.footer-amex1") 
	private WebElement amex;

	public void AmexLinkNotpresent()
	{
		Element.verifyElementNotPresent(testConfig, amex, "Amex Link");
	}

	/**
	 * Click on Go Back to Merchant Name link to cancel the transaction
	 * @param merchant Name
	 * @return Test Response Page
	 */
	public TestResponsePage clickGoBackToMerchantName(String merchantName)
	{
		Helper.compareEquals(testConfig, "Go Back Text", "Go back to " + merchantName , goBackText());
		WebElement merchantLink = Element.getPageElement(testConfig, How.linkText, merchantName);
		Element.click(testConfig, merchantLink, "Merchant Name");
		return new TestResponsePage(testConfig);
	}

	public void VerifyMobileVerificationMessage(TestDataReader codData, int codRow)
	{
		String expectedMobile = codData.GetData(codRow, "Mobile Number");
		expectedMobile = "To verify your mobile number " + expectedMobile + " , please give a call at the toll-free number below.\nIt's an automated system and your call will\ndisconnect just after two rings";
		Helper.compareEquals(testConfig, "Mobile Verify Text", expectedMobile, MobileVerifyText.getText());
		Helper.compareContains(testConfig, "Zip Dial Link", "https://www.zipdial.com/zip2auth/images/access/", zipDialLink.getAttribute("src"));
	}

	public void VerifyShippingMobileNumber(TestDataReader codData, int codRow)
	{   
		String expectedMobile = codData.GetData(codRow, "Mobile Number");
		Helper.compareEquals(testConfig, "Mobile Number", "Mobile : " + expectedMobile, MobileNumber.getText());
	}

	public void VerifyShippingDetails(TestDataReader codData, int codRow)
	{   
		String value0  = codData.GetData(codRow, "Fname");
		String value1 = codData.GetData(codRow, "Lname");
		String value2 = codData.GetData(codRow, "Address1");
		String value3 = codData.GetData(codRow, "Address2");
		String value4 = codData.GetData(codRow, "City");
		String value5 = codData.GetData(codRow, "State");
		String value6 = codData.GetData(codRow, "Country");
		String value7 = codData.GetData(codRow, "Zip Code");

		String expectedShippingDetail="Shipping address\n" + value0+" "+value1+"\n"+value2+" "+value3+"\n"+value4+", "+value5+", "+value6+"\n"+value7;

		String actualShippingDetail=ShippingDetails.getText();

		Helper.compareEquals(testConfig, "Shipping Details", expectedShippingDetail, actualShippingDetail);
	}

	public CODTab clickChangeDetailsButton() {
		Element.click(testConfig, ChangeDetailsButton, "Change Details");
		return new CODTab(testConfig);
	}
	
	public TestResponsePage clickChangeDetailsButtonToGetUserCancelledResponse() {
		Element.click(testConfig, ChangeDetailsButton, "Change Details to get User cancelled");
		return new TestResponsePage(testConfig);
	}

	public CODChangeMobilePage clickChangeMobileNumber() {
		Element.click(testConfig, ChangeMobileButton, "Change Mobile");
		return new CODChangeMobilePage(testConfig);
	}
}
