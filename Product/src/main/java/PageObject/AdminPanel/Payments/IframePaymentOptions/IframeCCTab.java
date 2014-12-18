package PageObject.AdminPanel.Payments.IframePaymentOptions;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import PageObject.AdminPanel.Payments.PaymentOptions.MerchantCCTab.MerCardType;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;
import Utils.Element.How;

public class IframeCCTab {
	
	private Config testConfig;
	
	public IframeCCTab(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, creditCardMakePayment);
	}
    
	//Credit Card type
	@FindBy(css = "a[href*='CreditCard']")
	private WebElement CCTabtxt;

	public String getCCTabText() {
		return CCTabtxt.getText();	
	}
	
	//Label Text Fields
	@FindBy(name="card_number")
	private WebElement creditCardNumber;

	@FindBy(name="name_on_card")
	private WebElement creditCardName;

	@FindBy(name="expiry_month")
	private WebElement creditCardExpiryMonth;

	@FindBy(name="expiry_year")
	private WebElement creditCardExpiryYear;

	@FindBy(name="cvv")
	private WebElement creditCardCvvNo;

	@FindBy(xpath="//input[@value='Make Payment']")
	private WebElement creditCardMakePayment;

	@FindBy(xpath="//div[@id='CreditCard']//input[@name='card_number']/following-sibling::span")
	private WebElement creditCardTypeIcon;
		
	public void enterCCNumber(String strCCNumber){
		Element.enterData(testConfig, creditCardNumber, strCCNumber, "Card Number");
	}
	
	public String getCardType(){
		return creditCardTypeIcon.getAttribute("class").split(" ")[1];
	}
	
	String strTextBoxerrorClass="textbox validate error";
	
	public void verifyCCValidations(String[] fieldsToValidate){
		for(String fieldToValidate:fieldsToValidate){
			switch (fieldToValidate) {
			case "CCNUMBER":
				Helper.compareEquals(testConfig, "CC invalid card", strTextBoxerrorClass, creditCardNumber.getAttribute("class"));
				break;
			case "CCNAME":
				Helper.compareEquals(testConfig, "CC invalid name on card", strTextBoxerrorClass, creditCardName.getAttribute("class"));
				break;
			case "CCCVV":
				Helper.compareEquals(testConfig, "CC invalid CVV", strTextBoxerrorClass, creditCardCvvNo.getAttribute("class"));
				break;
			case "CCEXP":
				Helper.compareEquals(testConfig, "CC invalid Expiry month", "ExpiryMonth textbox validate error", creditCardExpiryMonth.getAttribute("class"));
				Helper.compareEquals(testConfig, "CC invalid Expiry year", "ExpiryYear textbox validate error", creditCardExpiryYear.getAttribute("class"));
				break;
			default:
				Helper.compareEquals(testConfig, "CC invalid card", strTextBoxerrorClass, creditCardNumber.getAttribute("class"));
				Helper.compareEquals(testConfig, "CC invalid name on card", strTextBoxerrorClass, creditCardName.getAttribute("class"));
				Helper.compareEquals(testConfig, "CC invalid CVV", strTextBoxerrorClass, creditCardCvvNo.getAttribute("class"));
				Helper.compareEquals(testConfig, "CC invalid Expiry month", "ExpiryMonth textbox validate error", creditCardExpiryMonth.getAttribute("class"));
				Helper.compareEquals(testConfig, "CC invalid Expiry year", "ExpiryYear textbox validate error", creditCardExpiryYear.getAttribute("class"));
				break;
			}
		}
	}

	
	/**
	 * Reads the "CardDetails" sheet of Test Data excel and fill the credit card
	 * details
	 * 
	 * @param cardRow
	 *            Row number of excel sheet to be filled in
	 * @return Returns the Excel sheet data which was filled
	 */
	public TestDataReader FillCardDetails(int cardRow) 
	{
		TestDataReader data = new TestDataReader(testConfig,"CardDetails");

		String value = "";

		value = data.GetData(cardRow, "CC");
		Element.enterData(testConfig, creditCardNumber, value, "Card Number");

		value = data.GetData(cardRow, "Name");
		Element.enterData(testConfig, creditCardName, value, "Name on Card");

		value = data.GetData(cardRow, "CVV");
		Element.enterData(testConfig, creditCardCvvNo, value, "CVV");

		value = data.GetData(cardRow, "Mon");
		Element.selectValue(testConfig, creditCardExpiryMonth, value, "Expiry month");

		value = data.GetData(cardRow, "Year");
		Element.selectValue(testConfig, creditCardExpiryYear, value, "Expiry year");

		return data;
	}

	/**
	 * Click make payment and get the Test Response Page
	 * @return 
	 */
	public TestResponsePage clickIframeCCPaymentToGetTestResponsePage() 
	{
		Element.KeyPress(testConfig, creditCardMakePayment, Keys.RETURN, "CreditCard MakePayment");
		return new TestResponsePage(testConfig);
	}
	
	public IframeCCTab clickIframeCCPayment() 
	{
		Element.KeyPress(testConfig, creditCardMakePayment, Keys.RETURN, "CreditCard MakePayment");
		return new IframeCCTab(testConfig);
	}
}
