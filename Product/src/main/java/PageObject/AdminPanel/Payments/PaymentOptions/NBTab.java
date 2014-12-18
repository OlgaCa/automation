package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.TestDataReader;

public class NBTab {

	private Config testConfig;
	NBTab(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, selectBank);
	}

	@FindBy(css="a[href='#netbanking']")
	private WebElement NBTabtxt;
	public String getNBTabText() {
		return NBTabtxt.getText();
	}	

	@FindBy(id="netbanking_select")
	private WebElement selectBank;

	/**
	 * Selects the given Bank from the Bank list drop down
	 * @param bankName visible bank name
	 */
	public void SelectBank(String bankName) 
	{
		Element.selectVisibleText(testConfig, selectBank, bankName, "Bank Name - " + bankName);
		Browser.wait(testConfig, 1);
	}

	//Label Text Fields
	@FindBy(css="fieldset[class='netbanking-form prepend-1 prepend-top'] p label")
	private WebElement selectBankLabel;
	public String getSelectBankLabel() {
		return selectBankLabel.getText();
	}

	@FindBy(css="div#netbanking div p.prepend-top")
	private WebElement NoteLabelInBox;
	public String getNoteText() {
		return NoteLabelInBox.getText();
	}
	
	/**
	 * Verifies that given net banking option is not present in the list
	 * @param nbOption
	 */
	public void verifyDisabledPaymentTypes(String nbOption, String gateway)
	{
		Element.verifySelectVisibleTextNotPresent(testConfig, selectBank, nbOption, "Net Banking option for " + gateway);
	}
	
	
	//Payment Fields
	@FindBy(id="dname_on_card")
	private WebElement NameOnCard;

	@FindBy(id="dcard_number")
	private WebElement CardNumber;

	@FindBy(id="dcvv_number")
	private WebElement Cvv;

	@FindBy(id="dexpiry_date_month")
	private WebElement ExpiryMonth;

	@FindBy(id="dexpiry_date_year")
	private WebElement ExpiryYear;
	
	

	/**
	 * Reads the "CardDetails" sheet of Test Data excel and fill the credit card details
	 * @param cardRow Row number of excel sheet to be filled in
	 * @return Returns the Excel sheet data which was filled
	 */
	

	public TestDataReader FillCardDetails(int cardRow) 
	{
		TestDataReader data = new TestDataReader(testConfig,"CardDetails");

		String value = "";

		value = data.GetData(cardRow, "Name");
		Element.enterData(testConfig, NameOnCard, value, "Name on Card");

		value = data.GetData(cardRow, "CC");
		Element.enterData(testConfig, CardNumber, value, "Card Number");

		value = data.GetData(cardRow, "CVV");
		Element.enterData(testConfig, Cvv, value, "CVV");

		value = data.GetData(cardRow, "Mon");
		Element.selectValue(testConfig, ExpiryMonth, value, "Expiry month");

		value = data.GetData(cardRow, "Year");
		Element.selectValue(testConfig, ExpiryYear, value, "Expiry year");

		return data;
	}
	
	@FindBy(xpath = "//*[@id='netbanking']//a[contains(text(),'Save this card')]")
	private WebElement storeCardLink;

	@FindBy(xpath = "//*[@id='netbanking']//a[contains(text(),'Save card number')]")
	private WebElement storeCardCheckbox;

	@FindBy(xpath = "//*[@id='netbanking']//a[contains(text(),'Manage this card')]")
	private WebElement ManageCardLink;
	
	public void verifyStoreCardFeatureNotDisplayedIn_NB(){
		Element.verifyElementNotPresent(testConfig, ManageCardLink, "Manage Link for Store Card");
		Element.verifyElementNotPresent(testConfig, storeCardCheckbox, "Store Card Check Box");
		Element.verifyElementNotPresent(testConfig, storeCardLink, "Save Card Link");
	}
	
	/**
	   * Fills details in net banking tab ie select bank name from the bank list
	   * 
	   * @param paymentTypeRow Date:22nd April
	   */
	  public void fillNetBankingPaymentDetails(int paymentTypeRow) {
	    // Choose Payment Type and fill the payment details
	    TestDataReader paymentTypeData = new TestDataReader(testConfig, "PaymentType");
	    // Select bank from the list
	    String bankName = paymentTypeData.GetData(paymentTypeRow, "Bank Name");
	    SelectBank(bankName);

	  }

	  @FindBy(id = "checkOffernb")
	  private WebElement CheckOfferLink;

	  @FindBy(id = "checkOfferTagnb")
	  private WebElement CheckOfferMessage;

	/**
	 * clicks on check offer link date: 22nd April
	 */
	public void clickOnCheckOfferLink() {
		String strOfferMessage_actual;

		// click on check offer link
		Element.click(testConfig, CheckOfferLink, "Check Offer Link");
		// wait for link to disappear
		Element.waitForElementToDisappear(testConfig, CheckOfferLink);
		// Element.waitForStaleness(testConfig, CheckOfferLink,
		// "Chekout Offer link");

		// wait for error message to be displayed
		// Element.waitForElementDisplay(testConfig, CheckOfferMessage);
		Browser.waitForPageLoad(testConfig, CheckOfferMessage);

		// verify data displayed
		strOfferMessage_actual = CheckOfferMessage.getText().trim();
		// set property
		testConfig.putRunTimeProperty("checkOfferMessage_actual",
				strOfferMessage_actual);
	}
	
	
	
}
