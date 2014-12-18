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

public class CODChangeMobilePage {

	private Config testConfig;

	//Please provide a vaild mobile number
	@FindBy(xpath="//div[@id='mobileDetails']/center/h2")
	private WebElement ValidMobileNumberText;

	//Mobile*
	@FindBy(xpath="//div[@id='mobileDetails']/label")
	private WebElement MobileLabel;

	//Change Mobile text box
	@FindBy(id="shipping_mob")
	private WebElement CODChangeMobile;

	//Change Mobile error
	@FindBy(css="#shipping_mob+label")
	private WebElement CODChangeMobileError;
	public void verifyChangeMobileError(TestDataReader codData)
	{
		// Input special Chars
		FillMobilenumber(codData.GetData(2, "Mobile Number"));
		Element.click(testConfig, ChangeMobileNextButton, "Change Mobile Next Button");
		Helper.compareEquals(testConfig, "Mobile Error", codData.GetData(8, "Mobile Number"), CODChangeMobileError.getText());

		//Mandatory input
		FillMobilenumber(codData.GetData(3, "Mobile Number"));
		Element.click(testConfig, ChangeMobileNextButton, "Change Mobile Next Button");
		Helper.compareEquals(testConfig, "Mobile Error", codData.GetData(10, "Mobile Number"), CODChangeMobileError.getText());

		//Wrong Input
		FillMobilenumber(codData.GetData(4, "Mobile Number"));
		Element.click(testConfig, ChangeMobileNextButton, "Change Mobile Next Button");
		Helper.compareEquals(testConfig, "Mobile Error", codData.GetData(9, "Mobile Number"), CODChangeMobileError.getText());

		//Spaces Input
		FillMobilenumber(codData.GetData(5, "Mobile Number"));
		Element.click(testConfig, ChangeMobileNextButton, "Change Mobile Next Button");
		Helper.compareEquals(testConfig, "Mobile Error", codData.GetData(10, "Mobile Number"), CODChangeMobileError.getText());

		//With Spaces
		FillMobilenumber(codData.GetData(6, "Mobile Number"));
		Element.click(testConfig, ChangeMobileNextButton, "Change Mobile Next Button");
		Helper.compareEquals(testConfig, "Mobile Error", codData.GetData(8, "Mobile Number"), CODChangeMobileError.getText());
	}

	//Next button
	@FindBy(xpath="//div[@id='mobileDetails']/div[3]/input")
	private WebElement ChangeMobileNextButton;

	//Go back to merchant
	@FindBy(css = "div[class='go_badkDiv_form bottom']")
	private WebElement goBack;

	//In next step we will verify your mobile number.
	@FindBy(css = "span[class='step_v left_algn']")
	private WebElement CODVerifyLabel;

	//Your registered shipping address for COD.
	@FindBy(css = "div[class='shippingDiv fr_change_dtl']")
	private WebElement ValidShippingText;
	
	@FindBy(id="changeDetails_cod")
	private WebElement ChangeDetailsbutton;

	//Note text
	@FindBy(xpath = "//div[@id='cod-box']/p")
	private WebElement NoteLabelInBox;

	CODChangeMobilePage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, MobileLabel);
	}

	public CODMobileVerifyPage ClickNext()
	{
		Element.click(testConfig, ChangeMobileNextButton, "Click Next to Change Mobile Number");
		return new CODMobileVerifyPage(testConfig);
	}

	public CODTab ClickChangeDetails()
	{
		Element.click(testConfig, ChangeDetailsbutton, "Click Change Details to change Shipping details");
		return new CODTab(testConfig);
	}

	/**
	 * Click on Go Back to Merchant Name link to cancel the transaction
	 * @param merchant Name
	 * @return Test Response Page
	 */
	public TestResponsePage clickGoBackToMerchantName(String merchantName)
	{
		Helper.compareEquals(testConfig, "Go Back Text", "or Go back to " + merchantName , goBack.getText());
		WebElement merchantLink = Element.getPageElement(testConfig, How.linkText, merchantName);
		Element.click(testConfig, merchantLink, "Merchant Name");
		return new TestResponsePage(testConfig);
	}

	public void FillMobilenumber(String newMobNumber) 
	{
		Element.enterData(testConfig, CODChangeMobile, newMobNumber, "Changed Mobile number");
	}

	public void VerifyShippingDetailsOnMobileEditPage(TestDataReader codData, int codRow)
	{   
		TestDataReader cardDetailsData = new TestDataReader(testConfig, "CardDetails");
		Helper.compareEquals(testConfig, "Valid Mobile Text", codData.GetData(12, "Mobile Number"), ValidMobileNumberText.getText());
		Helper.compareEquals(testConfig, "Mobile Number Text", codData.GetData(7, "Mobile Number"), MobileLabel.getText());
		Helper.compareEquals(testConfig, "Mobile Number", codData.GetData(codRow, "Mobile Number"), CODChangeMobile.getAttribute("value"));
		Helper.compareEquals(testConfig, "COD Verify Label", cardDetailsData.GetData(11, "COD"), CODVerifyLabel.getText());

		String value0  = codData.GetData(codRow, "Fname");
		String value1 = codData.GetData(codRow, "Lname");
		String value2 = codData.GetData(codRow, "Address1");
		String value3 = codData.GetData(codRow, "Address2");
		String value4 = codData.GetData(codRow, "City");
		String value5 = codData.GetData(codRow, "State");
		String value6 = codData.GetData(codRow, "Country");
		String value7 = codData.GetData(codRow, "Zip Code");

		String expectedShippingDetail=codData.GetData(12, "Fname") + "\n" + "Shipping address\n" + value0+" "+value1+"\n"+value2+"\n"+value3+"\n"+value4+", "+value5+",\n"+value6+"\n"+value7;

		Helper.compareEquals(testConfig, "Valid Shipping Text", expectedShippingDetail, ValidShippingText.getText());

	//	Helper.compareEquals(testConfig, "COD Note", cardDetailsData.GetData(10, "Note"), NoteLabelInBox.getText());
	}
}
