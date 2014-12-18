package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.ErrorResponsePage;
import PageObject.AdminPanel.Payments.Response.OfferPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.TestDataReader;

public class MerchantNBTab {

	public static WebDriver driver;

	@FindBy(xpath="//select[@id='online_select']")
	private WebElement selectBank;

	@FindBy(xpath = "//input[@id='payment3']")
	private WebElement nbPayment;

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

	private Config testConfig;

	public MerchantNBTab(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,selectBank);
	}

	/**
	 * selects bank for NetBanking from drop down list
	 * @param bankName
	 */
	public void SelectMerchantBank(String bankName) 
	{
		Element.selectVisibleText(testConfig, selectBank, bankName, "Bank Name - " + bankName);
		Browser.wait(testConfig, 1);
	}
    
	public MerchantDCTab SelectCitiNetbanking(String bankName)
	{
		
		Element.selectVisibleText(testConfig, selectBank, bankName, "Bank Name - " + bankName);
		Browser.wait(testConfig, 1);
		return new MerchantDCTab(testConfig);
	}
	/**
	 * fill card details for Netbanking for citi bank from sheet CardDetails
	 * @param cardRow
	 * @return data
	 */

	public TestDataReader FillNetbankingCardDetails(int cardRow) 
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
	 * clicks on netbanking make payment 
	 * @return Test Response Page
	 */
	public TestResponsePage clickMerchantNBPaymentToGetTestResponsePage() 
	{
		Element.KeyPress(testConfig, nbPayment, Keys.RETURN, "NetBanking Make Payment");
		return new TestResponsePage(testConfig);

	}
	/**
	 * Click netbanking make payment and get the Try Again Page
	 */
	public TryAgainPage clickMerchantNBPaymentToGetTryAgainPage(){
		Element.KeyPress(testConfig, nbPayment, Keys.RETURN, "NetBanking Make Payment");
		return new TryAgainPage(testConfig);
	}

	/**
	 * Click netbanking make payment and get the Error ResponsePage
	 */
	public ErrorResponsePage clickMerchantNBPaymentToGetErrorResponsePage(){
		Element.KeyPress(testConfig, nbPayment, Keys.RETURN, "NetBanking Make Payment");
		return new ErrorResponsePage(testConfig);
	}

	/**
	 * Click netbanking make payment and get the Bank Redirect Page
	 */
	public BankRedirectPage clickMerchantNBPaymentToGetBankRedirectPage(){
		Element.KeyPress(testConfig, nbPayment, Keys.RETURN, "NetBanking Make Payment");
		return new BankRedirectPage(testConfig);
	}
	/**
	 * Click make payment  to get Offer Page, in case offer key is entered in transaction
	 *
	 */
	public OfferPage clickPayNowToGetOfferPage(){
		Element.KeyPress(testConfig, nbPayment, Keys.RETURN, "NetBanking Make Payment");
		return new OfferPage(testConfig);
	}
	/**
	 * clicks on NB make payment
	 */
	public void clickPayNowToGetError() {
		Element.KeyPress(testConfig, nbPayment, Keys.RETURN, "NetBanking Make Payment");
	}


}
