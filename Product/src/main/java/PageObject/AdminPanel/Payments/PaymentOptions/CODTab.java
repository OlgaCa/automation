package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.Response.CODMobileVerifyPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;
import Utils.Element.How;

public class CODTab {

	private Config testConfig;

	public CODTab(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, ShippingFname);
	}

	@FindBy(css="a[href='#cod']")
	private WebElement CODTabtxt;
	public String getCODTabText() {
		return CODTabtxt.getText();
	}

	@FindBy(css="div.shipping_formDiv h2")
	private WebElement CODLabel;
	public String getCODLabel() {
		return CODLabel.getText();
	}

	@FindBy(css="span.step_v")
	private WebElement CODVerifyLabel;
	public String getCODVerifyLabel() {
		return CODVerifyLabel.getText();
	}

	//Address Fields
	@FindBy(id = "shipping_firstname")
	private WebElement ShippingFname;
	public String getShippingFname() {
		return ShippingFname.getAttribute("value");
	}

	@FindBy(id = "shipping_lastname")
	private WebElement ShippingLname;
	public String getShippingLname() {
		return ShippingLname.getAttribute("value");
	}

	@FindBy(id = "shipping_address1")
	private WebElement ShippingAdd1;
	public String getShippingAdd1() {
		return ShippingAdd1.getAttribute("value");
	}

	@FindBy(id = "shipping_address2")
	private WebElement ShippingAdd2;
	public String getShippingAdd2() {
		return ShippingAdd2.getAttribute("value");
	}

	@FindBy(id = "shipping_city")
	private WebElement ShippingCity;
	public String getShippingCity() {
		return ShippingCity.getAttribute("value");
	}

	@FindBy(id = "shipping_country")
	private WebElement ShippingCountry;
	public String getShippingCountry() {
		return ShippingCountry.getAttribute("value");
	}

	@FindBy(id = "shipping_state")
	private WebElement ShippingState;
	public String getShippingState() {
		return ShippingState.getAttribute("value");
	}

	@FindBy(id = "shipping_zipcode")
	private WebElement ShippingZip;
	public String getShippingZip() {
		return ShippingZip.getAttribute("value");
	}

	@FindBy(id = "shipping_phone")
	private WebElement ShippingPhone;
	public String getShippingPhone() {
		return ShippingPhone.getAttribute("value");
	}

	/**
	 * Reads the "COD" sheet of Test Data excel and fill the shipping address details
	 * @param cardRow Row number of excel sheet to be filled in
	 * @return Returns the Excel sheet data which was filled
	 */
	public TestDataReader FillShippingDetails(int codRow) {
		String value = "";

		TestDataReader codData = new TestDataReader(testConfig, "COD");

		value = codData.GetData(codRow, "Fname");
		Element.enterData(testConfig, ShippingFname, value, "Shipping First name");

		value = codData.GetData(codRow, "Lname");
		Element.enterData(testConfig, ShippingLname, value, "Shipping Last name");

		value = codData.GetData(codRow, "Address1");
		Element.enterData(testConfig, ShippingAdd1, value, "Shipping Address1");

		value = codData.GetData(codRow, "Address2");
		Element.enterData(testConfig, ShippingAdd2, value, "Shipping Address2");

		value = codData.GetData(codRow, "City");
		Element.enterData(testConfig, ShippingCity, value, "Shipping City");

		value = codData.GetData(codRow, "State");
		Element.selectVisibleText(testConfig, ShippingState, value, "Shipping State");

		value = codData.GetData(codRow, "Country");
		Element.enterData(testConfig, ShippingCountry, value, "Shipping Country");

		value = codData.GetData(codRow, "Zip Code");
		Element.enterData(testConfig, ShippingZip, value, "Shipping Zip Code");

		value = codData.GetData(codRow, "Mobile Number");
		Element.enterData(testConfig, ShippingPhone, value, "Shipping Mobile number");

		return codData;
	}

	//Next Button
	@FindBy(css = "input.next_btn")
	private WebElement NextButton;

	/**
	 * Click Next and get the COD MobileVerify Page
	 */
	public CODMobileVerifyPage clickNext()
	{
		Element.click(testConfig, NextButton, "Next");
		return new CODMobileVerifyPage(testConfig);
	}

	/**
	 * Click Next and get in-Progress COD test response page
	 * @return Test Response Page
	 */
	public TestResponsePage clickNextToGetTestResponse()
	{
		Element.click(testConfig, NextButton, "Next");
		return new TestResponsePage(testConfig);
	}


	/**
	 * Click Next and stay on same COD Tab (in case of errors)
	 */
	public void clickNextGetError() {
		Element.click(testConfig, NextButton, "Next");
	}

	//Go back to merchant
	@FindBy(css = "div.go_badkDiv_form")
	private WebElement goBack;
	public String goBackText() {
		return goBack.getText();
	}

	/**
	 * Click on Go Back to Merchant Name link to cancel the transaction
	 * @param merchant Name
	 * @return Test Response Page
	 */
	public TestResponsePage clickGoBackToMerchantName(String merchantName)
	{
		Helper.compareEquals(testConfig, "Go Back Text", "or Go back to " + merchantName , goBackText());
		WebElement merchantLink = Element.getPageElement(testConfig, How.linkText, merchantName);
		Element.click(testConfig, merchantLink, "Merchant Name");
		return new TestResponsePage(testConfig);
	}

	//Label Text
	@FindBy(xpath = "//div[@id='allDetails']/div/p/label")
	private WebElement CODPageNameLabel;
	public String getNameLabel() {
		return CODPageNameLabel.getText();
	}

	@FindBy(xpath = "//div[@id='allDetails']/div/p[2]/label")
	private WebElement CODPageAdd1Label;
	public String getAdd1Label() {
		return CODPageAdd1Label.getText();
	}

	@FindBy(xpath = "//div[@id='allDetails']/div/p[3]/label")
	private WebElement CODPageAdd2Label;
	public String getAdd2Label() {
		return CODPageAdd2Label.getText();
	}

	@FindBy(xpath = "//div[@id='allDetails']/div/p[4]/label")
	private WebElement CODPageCityLabel;
	public String getCityLabel() {
		return CODPageCityLabel.getText();
	}

	@FindBy(xpath = "//div[@id='allDetails']/div/p[5]/label")
	private WebElement CODPageCountryLabel;
	public String getCountryLabel() {
		return CODPageCountryLabel.getText();
	}

	@FindBy(xpath = "//div[@id='allDetails']/div/p[6]/label")
	private WebElement CODPageStateLabel;
	public String getStateLabel() {
		return CODPageStateLabel.getText();
	}

	@FindBy(xpath = "//div[@id='allDetails']/div/p[7]/label")
	private WebElement CODPageZipLabel;
	public String getZipLabel() {
		return CODPageZipLabel.getText();
	}

	@FindBy(xpath = "//div[@id='allDetails']/div/p[8]/label")
	private WebElement CODPageMobileLabel;
	public String getPhoneLabel() {
		return CODPageMobileLabel.getText();
	}

	//Mandatory errors
	@FindBy(css="#shipping_firstname+label")
	private WebElement ShippingFNameError;
	public String getShippingFNameError() {
		return ShippingFNameError.getText();
	}

	@FindBy(css="#shipping_lastname+label")
	private WebElement ShippingLnameError;
	public String getShippingLnameError() {
		return ShippingLnameError.getText();
	}

	@FindBy(css="#shipping_address1+label")
	private WebElement ShippingAdd1Error;
	public String getShippingAdd1Error() {
		return ShippingAdd1Error.getText();
	}

	@FindBy(css="#shipping_address2+label")
	private WebElement ShippingAdd2Error;
	public String getShippingAdd2Error() {
		return ShippingAdd2Error.getText();
	}

	@FindBy(css="#shipping_city+label")
	private WebElement ShippingCityError;
	public String getShippingCityError() {
		return ShippingCityError.getText();
	}

	@FindBy(css="#shipping_country+label")
	private WebElement ShippingCountryError;
	public String getShippingCountryError() {
		return ShippingCountryError.getText();
	}

	@FindBy(css="#shipping_state+label")
	private WebElement ShippingStateError;
	public void verifyShippingStateErrorAbsent() {
		Element.verifyElementNotPresent(testConfig, ShippingStateError, "Shipping State Error");
	}

	@FindBy(css="#shipping_zipcode+label")
	private WebElement ShippingZipError;
	public String getShippingZipError() {
		return ShippingZipError.getText();
	}

	@FindBy(css="#shipping_phone+label")
	private WebElement ShippingPhoneError;
	public String getShippingPhoneError() {
		return ShippingPhoneError.getText();
	}

	//MobileVerifyText
	@FindBy(xpath = "//div[@id='allDetails']/div/span")
	private WebElement MobileVerifyText;
	public String getMobileVerifyText() {
		return MobileVerifyText.getText();
	}

	//Note text
	@FindBy(xpath = "//div[@id='cod-box']/p")
	private WebElement NoteLabelInBox;
	public String getNoteText() {
		return NoteLabelInBox.getText();
	}

	public void VerifyShippingDetailsOnCODEditPage(TestDataReader codData, int codRow)
	{   
		Helper.compareEquals(testConfig, "First Name", codData.GetData(codRow, "Fname"), ShippingFname.getAttribute("value"));
		Helper.compareEquals(testConfig, "Last Name", codData.GetData(codRow, "Lname"), ShippingLname.getAttribute("value"));
		Helper.compareEquals(testConfig, "Mobile Number", codData.GetData(codRow, "Mobile Number"), ShippingPhone.getAttribute("value"));
		Helper.compareEquals(testConfig, "Address1", codData.GetData(codRow, "Address1"), ShippingAdd1.getAttribute("value"));
		Helper.compareEquals(testConfig, "Address2", codData.GetData(codRow, "Address2"), ShippingAdd2.getAttribute("value"));
		Helper.compareEquals(testConfig, "City", codData.GetData(codRow, "City"), ShippingCity.getAttribute("value"));
		Helper.compareEquals(testConfig, "State", codData.GetData(codRow, "State"), Element.getFirstSelectedOption(testConfig, ShippingState, "State").getText());
		Helper.compareEquals(testConfig, "Country", codData.GetData(codRow, "Country"), ShippingCountry.getAttribute("value"));
		Helper.compareEquals(testConfig, "Zip Code", codData.GetData(codRow, "Zip Code"), ShippingZip.getAttribute("value"));
	}

}
