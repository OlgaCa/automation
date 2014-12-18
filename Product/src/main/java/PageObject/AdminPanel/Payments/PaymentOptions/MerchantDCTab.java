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

public class MerchantDCTab {

	public static WebDriver driver;

	@FindBy(id="debitcard_select")
	private WebElement debitCardType;

	@FindBy(id="DCnum")
	private WebElement debitCardNum;

	@FindBy(id="DCname")
	private WebElement debitCardName;

	@FindBy(id="DCexpmon")
	private WebElement debitCardExpiryMonth;

	@FindBy(id="DCexpyr")
	private WebElement debitCardExpiryYear;

	@FindBy(id="DCcvv")
	private WebElement debitCardCvv;

	@FindBy(xpath="//input[@id='payment2']")
	private WebElement debitCardPayment;


	private Config testConfig;

	public MerchantDCTab(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,debitCardNum);
	}

	/**
	 * fill card details for debit card from sheet CardDetails
	 * @param cardRow
	 * @return data
	 */
	public TestDataReader FillDebitCardDetails(int cardRow) 
	{
		TestDataReader data = new TestDataReader(testConfig,"CardDetails");

		String value = "";

		value = data.GetData(cardRow, "Name");
		Element.enterData(testConfig, debitCardName, value, "Name on Card");

		value = data.GetData(cardRow, "CC");
		Element.enterData(testConfig, debitCardNum, value, "Card Number");

		value = data.GetData(cardRow, "CVV");
		Element.enterData(testConfig, debitCardCvv, value, "CVV");

		value = data.GetData(cardRow, "Mon");
		Element.selectValue(testConfig, debitCardExpiryMonth, value, "Expiry month");

		value = data.GetData(cardRow, "Year");
		Element.selectValue(testConfig, debitCardExpiryYear, value, "Expiry year");

		return data;
	}
	/**
	 * chose Debit card from Debit card drop down list
	 * @param debitCardName
	 */
	public void ChoseDebitCard(String debitCardName) 
	{
		Browser.wait(testConfig, 3);
		Element.selectVisibleText(testConfig, debitCardType, debitCardName, "Debit card -" + debitCardName);
		Browser.wait(testConfig, 1);
	}
	/**
	 * Click Debit card make payment and get the Test Response Page
	 */
	public TestResponsePage clickMerchantDCPaymentToGetTestResponsePage() 
	{
		Element.KeyPress(testConfig, debitCardPayment, Keys.RETURN, "DebitCard Make Payment");
		return new TestResponsePage(testConfig);
		

	}
	/**
	 * Click Debit card make payment and get the Error Page
	 */
	public void clickPayNowToGetError() {
		Element.KeyPress(testConfig, debitCardPayment, Keys.RETURN, "DebitCard Make Payment");
	}

	/**
	 * Click Debit card make payment and get the Try Again Page
	 */
	public TryAgainPage clickMerchantDCPaymentToGetTryAgainPage(){
		Element.KeyPress(testConfig, debitCardPayment, Keys.RETURN, "DebitCard Make Payment");
		return new TryAgainPage(testConfig);
	}

	/**
	 * Click Debit card make payment and get the Error ResponsePage
	 */
	public ErrorResponsePage clickMerchantDCPaymentToGetErrorResponsePage(){
		Element.KeyPress(testConfig, debitCardPayment, Keys.RETURN, "DebitCard Make Payment");
		return new ErrorResponsePage(testConfig);
	}

	/**
	 * Click  Debit card make payment and get the Bank Redirect Page
	 */
	public BankRedirectPage clickMerchantDCPaymentToGetBankRedirectPage(){
		Element.KeyPress(testConfig, debitCardPayment, Keys.RETURN, "DebitCard Make Payment");
		return new BankRedirectPage(testConfig);
	}
	/**
	 * Click make payment  to get Offer Page, in case offer key is entered in transaction
	 *
	 */
	public OfferPage clickPayNowToGetOfferPage(){
		Element.KeyPress(testConfig, debitCardPayment, Keys.RETURN, "DebitCard Make Payment");
		return new OfferPage(testConfig);
	}
	@FindBy(id = "storedDC") 
	private WebElement storedDebitCardDropdown; 
	 
	@FindBy(id = "DCstore") 
	private WebElement storedDebitCardname; 
	 
	public WebElement selectStoredCard(String text) { 
		try { 
			//System.out.println("action"); 
			//Actions action = new Actions(testConfig.driver); 
			//action.moveToElement(storedDebitCardname).clickAndHold().perform(); 
			//System.out.println("you are here"); 
			Element.click(testConfig, storedDebitCardDropdown, 
					"Stored Card Drop Down"); 
			 
			Element.selectVisibleText(testConfig, storedDebitCardname, text, "storedcard: "); 
		} 
		catch(NullPointerException e){ 
			testConfig.logFail("Stored Card: "+text+" doesnot exist"); 
			return null; 
		} 
		return storedDebitCardname; 
	} 
	 	@FindBy(id = "storeCardFlagDC") 
	private WebElement storeCardCheckBox; 	 
	public void clickStoreCardCheckBox() { 
		Element.click(testConfig, storeCardCheckBox, "Save Card CheckBox"); 	} 
	 
	@FindBy(id = "removeCardFlagDC") 
	private WebElement removeCard; 
	 
	public void clickRemoveCard() { 
		Element.click(testConfig, removeCard, "Remove Link"); 
	} 
	 
	@FindBy(id = "cardnamedc") 
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
	 
	public TestDataReader FillSavedCardDetails(int storeCardRow) { 
		TestDataReader data = new TestDataReader(testConfig, "StoreCard"); 
		String value = ""; 
		String SavedCardName = data.GetData(storeCardRow, "SavedCardName"); 
		if (selectStoredCard(SavedCardName) != null) { 
			value = data.GetData(storeCardRow, "CVV"); 
			Element.enterData(testConfig, debitCardCvv, value, "CVV"); 
		} else { 
			testConfig 
			.logFail("StoreCard " + SavedCardName+" expected but it doesnot Exist"); 
			Assert.assertTrue(testConfig.getTestResult()); 
		} 
		return data; 
	} 
 
	public void removeDCStoredCard() { 
		while(!Element.IsElementDisplayed(testConfig, storeCardCheckBox)) 
		{ 
			clickRemoveCard(); 
		}  
		System.out.println("No more Saved card"); 
			return; 
	} 
	 
	 
	public boolean verifyStoreCardDoesNotExists() { 
		if ((Element.IsElementDisplayed(testConfig, storedDebitCardDropdown)) 
				&& (Element.IsElementDisplayed(testConfig, storeCardCheckBox))) 
			return false; 
		else 
			return true; 
	} 
	
	@FindBy(id="nocvv")
	private WebElement optionalCVV;
	
	public void clickNoCVV()
	{
			Element.click(testConfig, optionalCVV,"optional cvv/expiry link");
	}
	
	public PaymentOptionsPage clickMerchantDCPaymentToGetPaymentoptionsPage(){
				Element.KeyPress(testConfig, debitCardPayment, Keys.RETURN, "DebitCard Make Payment");
				if(testConfig.isMobile)
					return new PaymentOptionMobilePage(testConfig);
				else
					return new PaymentOptionsPage(testConfig);
					
			}

}
