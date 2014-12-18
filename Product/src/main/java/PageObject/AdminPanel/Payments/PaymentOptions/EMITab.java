package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.TestDataReader;

public class EMITab {

	private Config testConfig;
	EMITab(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, selectBank);
	}

	@FindBy(css="a[href='#emi']")
	private WebElement EMITabtxt;
	public String getEMITabText() {
		return EMITabtxt.getText();
	}	

	@FindBy(id="bankEmiList")
	private WebElement selectBank;

	@FindBy(id="bank_emi_list_20")
	private WebElement emiPeriodCiti;

	@FindBy(id="bank_emi_list_15")
	private WebElement emiPeriodHdfcEmi;

	@FindBy(id="bank_emi_list_8")
	private WebElement emiPeriodHdfc2;
	
	@FindBy(id="bank_emi_list_21")
	private WebElement emiPeriodIcici;
	
	@FindBy(id="bank_emi_list_7")
	private WebElement emiPeriodAxis;

	/**
	 * Row number of 'PaymentType' excel sheet to be used for selecting the bank and duration
	 * @param emiRow emiRow
	 * @return expected EMI redirect URL
	 */
	public TestDataReader SelectBankAndDuration(int paymentTypeRow) 
	{
		TestDataReader data = new TestDataReader(testConfig,"PaymentType");

		String bank = data.GetData(paymentTypeRow, "PG_TYPE");;
		String duration = data.GetData(paymentTypeRow, "Bank Name");

		SelectBankAndDuration(bank,duration);

		return data;
	}

	/**
	 * Selects the given bank name and duration
	 * @param bank PG_Type
	 * @param duration visible duration
	 */
	public void SelectBankAndDuration(String bank, String duration) 
	{
		switch(bank)
		{
		case "HDFC2":
			bank ="";
			break;
		case "HDFCEMI":
			bank ="HDFC Bank";
			break;
		case "CITI": 
			bank="CITI Bank";
			break;
		case "AXIS": 
			bank="AXIS Bank";
			break;
		case "ICICITRAVEL": 
			bank="ICICI Bank";
			break;
		}

		Element.selectVisibleText(testConfig, selectBank, bank, "Bank Name - " + bank);
		Browser.wait(testConfig, 1);
		switch(bank)
		{
		case "":
			Element.selectVisibleText(testConfig, emiPeriodHdfc2, duration, "EMI Duration - " + duration);	
			break;
		case "HDFC Bank":
			Element.selectVisibleText(testConfig, emiPeriodHdfcEmi, duration, "EMI Duration - " + duration);
			break;
		case "CITI Bank":
			Element.selectVisibleText(testConfig, emiPeriodCiti, duration, "EMI Duration - " + duration);
			break;
		case "AXIS Bank":
			Element.selectVisibleText(testConfig, emiPeriodAxis, duration, "EMI Duration - " + duration);
			break;
		case "ICICI Bank":
			Element.selectVisibleText(testConfig, emiPeriodIcici, duration, "EMI Duration - " + duration);
			break;
		}
		Browser.wait(testConfig, 1);
	}

	//Label text
	@FindBy(xpath="//div[@id='emi']/div//label")   
	private WebElement selectYourBankLabel;
	public String getSelectYourBankLabel() {
		return selectYourBankLabel.getText();
	}

	@FindBy(xpath="//div[@id='emi']//fieldset/div[2]/div[2]/label") 
	private WebElement emiDurationLabel;
	public String getSelectEmiDurationLabel() {
		return emiDurationLabel.getText();
	}

	@FindBy(css="div#emi_text_div_EMI03 div.emiMessage div:nth-child(1) div strong") 
	private WebElement emiChargesLabel;
	public String getEmiChargesLabel() {
		return emiChargesLabel.getText();
	}
	
	@FindBy(xpath="//fieldset[@id='emi-form']/div/label")  
	private WebElement nameOnCardLabel;
	public String getNameOnCardLabel() {
		return nameOnCardLabel.getText();
	}

	@FindBy(xpath="//fieldset[@id='emi-form']/div[2]/label")
	private WebElement cardNumberLabel;
	public String getCardNumberLabel() {
		return cardNumberLabel.getText();
	}

	@FindBy(xpath="//fieldset[@id='emi-form']/div[3]/label")
	private WebElement cvvNumberLabel;
	public String getCvvNumberLabel() {
		return cvvNumberLabel.getText();
	}

	@FindBy(xpath="//fieldset[@id='emi-form']/p/label")
	private WebElement expiryDateLabel;
	public String getExpiryDateLabel() {
		return expiryDateLabel.getText();
	}

	//Payment Fields
	@FindBy(id="ename_on_card")
	private WebElement NameOnCard;

	@FindBy(id="ecard_number")
	private WebElement CardNumber;

	@FindBy(id="ecvv_number")
	private WebElement Cvv;

	@FindBy(id="eexpiry_date_month")
	private WebElement ExpiryMonth;

	@FindBy(id="eexpiry_date_year")
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
		for(char c : value.toCharArray())
		{
			Element.enterDataWithoutClear(testConfig, CardNumber, String.valueOf(c), "Card Number");
		}

		value = data.GetData(cardRow, "CVV");
		Element.enterData(testConfig, Cvv, value, "CVV");

		value = data.GetData(cardRow, "Mon");
		Element.selectValue(testConfig, ExpiryMonth, value, "Expiry month");

		value = data.GetData(cardRow, "Year");
		Element.selectValue(testConfig, ExpiryYear, value, "Expiry year");

		return data;
	}

	//Error messages
	@FindBy(css="label.error")
	private WebElement emiCardNameErrorMessage;
	public String getCardNameErrorMessage() {
		return emiCardNameErrorMessage.getText();
	}

	@FindBy(xpath="//fieldset[@id='emi-form']/div[2]/label[2]") 
	private WebElement emiCardNumberErrorMessage;
	public String getCardNumberErrorMessage() {
		return emiCardNumberErrorMessage.getText();
	}

	@FindBy(xpath="//fieldset[@id='emi-form']/div[3]/label[2]")  
	private WebElement emiCvvNumberErrorMessage;
	public String getCvvNumberErrorMessage() {
		return emiCvvNumberErrorMessage.getText();
	}

	@FindBy(css="p.dropdowns > label.error")     
	private WebElement emiExpiryDateErrorMessage;
	public String getExpiryDateErrorMessage() {
		return emiExpiryDateErrorMessage.getText();
	}

	@FindBy(css="a[href='#ecvv_help']")
	private WebElement whatIsCVV;
	public void clickWhatIsCVVLink() {
		Element.click(testConfig, whatIsCVV, "What Is CVV Link");
	}

	@FindBy(id="ecvv_help")
	private WebElement cardImage;
	public boolean isCardImageAppearing() {
		return cardImage.isEnabled();
	}

	@FindBy(css="div[id='emi']>div>p")
	private WebElement NoteLabelInBox;
	public String getNoteText() {
		return NoteLabelInBox.getText();
	}
	
	
	@FindBy(xpath = "//*[@id='emi']//a[contains(text(),'Save this card')]")
	private WebElement storeCardLink;

	@FindBy(xpath = "//*[@id='emi']//a[contains(text(),'Save card number')]")
	private WebElement storeCardCheckbox;

	@FindBy(xpath = "//*[@id='emi']//a[contains(text(),'Manage this card')]")
	private WebElement ManageCardLink;
	
	public void verifyStoreCardFeatureNotDisplayedIn_EMI(){
		Element.verifyElementNotPresent(testConfig, ManageCardLink, "Manage Link for Store Card");
		Element.verifyElementNotPresent(testConfig, storeCardCheckbox, "Store Card Check Box");
		Element.verifyElementNotPresent(testConfig, storeCardLink, "Save Card Link");
	}
	
	
}
