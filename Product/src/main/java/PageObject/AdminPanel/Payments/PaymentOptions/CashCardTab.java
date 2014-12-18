package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.TestDataReader;

public class CashCardTab {

	private Config testConfig;
	CashCardTab(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, selectCashCard);
	}

	@FindBy(css="a[href='#cashcard']")
	private WebElement CashCardTabtxt;

	public String getCashCardTabText() {
		return CashCardTabtxt.getText();
	}	

	@FindBy(id="cash_card_select")
	private WebElement selectCashCard;

	/**
	 * Selects the given Cash card
	 * @param cashCardName Name of the cash card
	 */
	public void SelectCashCard(String cashCardName) 
	{
		Browser.wait(testConfig, 3);
		Element.selectVisibleText(testConfig, selectCashCard, cashCardName, "Cash card");
		Browser.wait(testConfig, 1);
	}

	@FindBy(id="chname_on_card")
	private WebElement NameOnCard;

	@FindBy(id="chcard_number")
	private WebElement CardNumber;

	@FindBy(id="chcvv_number")
	private WebElement Cvv;

	@FindBy(id="chexpiry_date_month")
	private WebElement ExpiryMonth;

	@FindBy(id="chexpiry_date_year")
	private WebElement ExpiryYear;

	//Label text
	@FindBy(xpath="//label[@for='cash_card_select']")
	private WebElement SelectCashCardLabel;
	public String getCashCardLabel() {
		return SelectCashCardLabel.getText();
	}

	@FindBy(css="div[id=cashcard] p[class='p-endnote prepend-top prepend-1']")
	private WebElement NoteLabelInBox;
	public String getNoteText() {
		return NoteLabelInBox.getText();
	}
	
	/**
	 * Reads the "CardDetails" sheet of Test Data excel and fill the credit card details
	 * @param cardDetailsRow Row number of excel sheet to be filled in
	 * @return Returns the Excel sheet data which was filled
	 */
	public TestDataReader FillCardDetails(int cardDetailsRow) 
	{
		TestDataReader data = new TestDataReader(testConfig, "CardDetails");

		String value = "";

		value = data.GetData(cardDetailsRow, "Name");
		Element.enterData(testConfig, NameOnCard, value, "Name on Card");

		value = data.GetData(cardDetailsRow, "CC");
		Element.enterData(testConfig, CardNumber, value, "Card Number");

		value = data.GetData(cardDetailsRow, "CVV");
		Element.enterData(testConfig, Cvv, value, "CVV");

		value = data.GetData(cardDetailsRow, "Mon");
		Element.selectValue(testConfig, ExpiryMonth, value, "Expiry month");

		value = data.GetData(cardDetailsRow, "Year");
		Element.selectValue(testConfig, ExpiryYear, value, "Expiry year");

		return data;	
	}

	//Error messages
	@FindBy(css="#chname_on_card+label")
	private WebElement CardNameErrorMessage;
	public String getCardNameErrorMessage() {
		return CardNameErrorMessage.getText();
	}

	@FindBy(css="#chcard_number+label")
	private WebElement CardNumberErrorMessage;
	public String getCardNumberErrorMessage() {
		return CardNumberErrorMessage.getText();
	}

	@FindBy(css="#chcvv_number+label")
	private WebElement CvvNumberErrorMessage;
	public String getCvvNumberErrorMessage() {
		return CvvNumberErrorMessage.getText();
	}

	@FindBy(css="#chexpiry_date_month+label")
	private WebElement ExpiryDateErrorMessage;
	public String getExpiryDateErrorMessage() {
		return ExpiryDateErrorMessage.getText();
	}

	
	@FindBy(xpath = "//*[@id='cashcard']//a[contains(text(),'Save this card')]")
	private WebElement storeCardLink;

	@FindBy(xpath = "//*[@id='cashcard']//a[contains(text(),'Save card number')]")
	private WebElement storeCardCheckbox;

	@FindBy(xpath = "//*[@id='cashcard']//a[contains(text(),'Manage this card')]")
	private WebElement ManageCardLink;
	
	public void verifyStoreCardFeatureNotDisplayedIn_CashCard(){
		Element.verifyElementNotPresent(testConfig, ManageCardLink, "Manage Link for Store Card");
		Element.verifyElementNotPresent(testConfig, storeCardCheckbox, "Store Card Check Box");
		Element.verifyElementNotPresent(testConfig, storeCardLink, "Save Card Link");
	}
	
	@FindBy(id = "checkOffercash")
	private WebElement CheckOfferLink;

	@FindBy(id = "checkOfferTagcash")
	private WebElement CheckOfferMessage;

	public void clickOnCheckOfferLink() {
		String strOfferMessage_actual;

		// click on check offer link
		Element.click(testConfig, CheckOfferLink, "Check Offer Link");
		// wait for error message to be displayed
		Element.waitForElementToDisappear(testConfig, CheckOfferLink);
		// verify data displayed
		strOfferMessage_actual = CheckOfferMessage.getText().trim();
		// set property
		testConfig.putRunTimeProperty("checkOfferMessage_actual",
				strOfferMessage_actual);
	}
	
	@FindBy(xpath = "//input[@value='Pay Now']")
	private WebElement payNowButton;
	
	public void clickOnPayNow(){
		Browser.waitForPageLoad(testConfig, payNowButton);
		Element.click(testConfig, payNowButton, "Pay Now Button");
	}
}
