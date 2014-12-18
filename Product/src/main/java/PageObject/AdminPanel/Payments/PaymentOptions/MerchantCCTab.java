package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.ErrorResponsePage;
import PageObject.AdminPanel.Payments.Response.OfferPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.TestDataReader;

public class MerchantCCTab {

	public static WebDriver driver;

	@FindBy(id="1")
	private WebElement creditCard;

	@FindBy(xpath="//select[@id='cc_select']/option[2]")
	private WebElement amexCard; 

	@FindBy(xpath="//select[@id='cc_select']/option[3]")
	private WebElement dinerCard; 

	@FindBy(id="cc_select")
	private WebElement creditCardType;

	@FindBy(id="CCnum")
	private WebElement creditCardNumber;

	@FindBy(id="CCname")
	private WebElement creditCardName;

	@FindBy(id="CCexpmon")
	private WebElement creditCardExpiryDate;

	@FindBy(id="CCexpyr")
	private WebElement creditCardExpiryYear;

	@FindBy(id="CCcvv")
	private WebElement creditCardCvvNo;

	@FindBy(xpath="//input[@id='payment1']")
	private WebElement creditCardMakePayment;

	@FindBy(xpath="//select[@id='cc_select']")
	private WebElement selectCard;


	@FindBy()
	private WebElement iframe;

	private Config testConfig;
	public MerchantCCTab(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);

	}	

	public enum MerCardType{ Amex, Diners, MasterCard};

	/**
	 * Choose one of the Card Type options
	 * @param merCardType CardType
	 */
	public void chooseCreditCardType(MerCardType merCardType){

		switch(merCardType)
		{
		case Amex:

			Element.selectValue(testConfig, creditCardType, "AMEX", "select ämex card");
			//Element.click(testConfig, amexCard, "Amex cards");
			break;
		case Diners:
			Element.selectValue(testConfig, creditCardType, "DINR", "select dinr card");
			//Element.click(testConfig, dinerCard, "Diners Card");
			break;
		case MasterCard:
			Element.selectValue(testConfig, creditCardType, "CC", "select master card");
			//Element.click(testConfig, creditCardType, " Master Card");
			break;
		}
		Browser.wait(testConfig, 5);
	}


	/**
	 * fill card details for credit card from sheet CardDetails
	 * @param cardRow
	 * @return data
	 */
	public TestDataReader FillCardDetails(int cardRow) 
	{
		TestDataReader data = new TestDataReader(testConfig,"CardDetails");

		String value = "";

		value = data.GetData(cardRow, "Name");
		Element.enterData(testConfig, creditCardName, value, "Name on Card");

		value = data.GetData(cardRow, "CC");
		Element.enterData(testConfig, creditCardNumber, value, "Card Number");

		value = data.GetData(cardRow, "CVV");
		Element.enterData(testConfig, creditCardCvvNo, value, "CVV");

		value = data.GetData(cardRow, "Mon");
		Element.selectValue(testConfig, creditCardExpiryDate, value, "Expiry month");

		value = data.GetData(cardRow, "Year");
		Element.selectValue(testConfig, creditCardExpiryYear, value, "Expiry year");

		return data;
	}


	/**
	 * Click make payment and get the Test Response Page
	 * @return 
	 */
	public TestResponsePage clickMerchantCCPaymentToGetTestResponsePage() 
	{
		Element.KeyPress(testConfig, creditCardMakePayment, Keys.RETURN, "CreditCard MakePayment");
		return new TestResponsePage(testConfig);

	}

	/**
	 * Click make payment  and stay on same Payment Options Page (in case of errors)
	 */
	public void clickPayNowToGetError() {
		Element.KeyPress(testConfig, creditCardMakePayment, Keys.RETURN, "CreditCard MakePayment");
	}

	/**
	 * Click make payment  to get Offer Page, in case offer key is entered in transaction
	 *
	 */
	public OfferPage clickPayNowToGetOfferPage(){
		Element.KeyPress(testConfig, creditCardMakePayment, Keys.RETURN, "CreditCard MakePayment");
		return new OfferPage(testConfig);
	}

	/**
	 * Click make payment  and get the Try Again Page
	 */
	public TryAgainPage clickMerchantCCPaymentToGetTryAgainPage(){
		Element.KeyPress(testConfig, creditCardMakePayment, Keys.RETURN, "CreditCard MakePayment");
		return new TryAgainPage(testConfig);
	}

	/**
	 * Click make payment  and get the Error ResponsePage
	 */
	public ErrorResponsePage clickMerchantCCPaymentToGetErrorResponsePage(){
		Element.KeyPress(testConfig, creditCardMakePayment, Keys.RETURN, "CreditCard MakePayment");
		return new ErrorResponsePage(testConfig);
	}

	/**
	 * Click make payment  and get the Bank Redirect Page
	 */
	public BankRedirectPage clickMerchantCCPaymentToGetBankRedirectPage(){
		Element.KeyPress(testConfig, creditCardMakePayment, Keys.RETURN, "CreditCard MakePayment");
		return new BankRedirectPage(testConfig);
	}

	@FindBy(id = "storedCC")
	private WebElement storedCreditCardDropdown;
	
	@FindBy(id = "CCstore")
	private WebElement storedCreditCardname;
		
	public WebElement selectStoredCard(String text) {
		try {
			Element.click(testConfig, storedCreditCardDropdown, "Stored Card Drop Down");
			Element.selectVisibleText(testConfig, storedCreditCardname, text, "storedcard: ");
		}
		catch(NullPointerException e){
			testConfig.logFail("Stored Card: "+text+" doesnot exist");
			return null;
		}
		return storedCreditCardname;
	}
	
	@FindBy(id = "storeCardFlagCC")
	private WebElement storeCardCheckBox;
	
	public void clickStoreCardCheckBox() {
		Element.click(testConfig, storeCardCheckBox, "Save Card CheckBox");
	}

	@FindBy(id = "removeCardFlagCC")
	private WebElement removeCard;
	public void clickRemoveCard() {
		Element.click(testConfig, removeCard, "Remove Link");
	}
	
	@FindBy(id = "cardnamecc")
	private WebElement storeCardName;

	public TestDataReader FillStoreCardDetails(int storeCardRow) {
		TestDataReader data = new TestDataReader(testConfig, "StoreCard");
		selectStoredCard("New Card");
		String value = "";
		
		if (Element.IsElementDisplayed(testConfig, storeCardCheckBox)) {
			clickStoreCardCheckBox();
			value = data.GetData(storeCardRow, "SavedCardName");
			Element.enterData(testConfig, storeCardName, value, "Name on Card");
		} else {
			testConfig
			.logFail("StoreCard functionality expected but it doesnot Exist");
			Assert.assertTrue(testConfig.getTestResult());
		}
		return data;
	}
	
	public TestDataReader FillSavedCardDetails(int storeCardRow)  {
		TestDataReader data = new TestDataReader(testConfig, "StoreCard");
		String value = "";
		String SavedCardName = data.GetData(storeCardRow, "SavedCardName");
		
		if (selectStoredCard(SavedCardName) != null) {
			value = data.GetData(storeCardRow, "CVV");
			Element.enterData(testConfig, creditCardCvvNo, value, "CVV");
		} else {
			testConfig
			.logFail("StoreCard " + SavedCardName+" expected but it doesnot Exist");
			Assert.assertTrue(testConfig.getTestResult());
		}
		return data;
	}

 
	
	public void removeCCStoredCard() { 
		while(!Element.IsElementDisplayed(testConfig, storeCardCheckBox))
		{
			clickRemoveCard();
		} 
		System.out.println("No more Saved card");
			return;
	}

	public boolean verifyStoreCardDoesNotExists() { 
		if ((Element.IsElementDisplayed(testConfig, storedCreditCardDropdown)) 
				&& (Element.IsElementDisplayed(testConfig, storeCardCheckBox))) 
			return false;	 
		else 
			return true; 
	}

}
