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

public class MerchantEMITab {

	public static WebDriver driver;


	@FindBy(id="emi_select")
	private WebElement emiSelect;

	@FindBy(id="ECnum")
	private WebElement emiCardNum;

	@FindBy(id="ECname")
	private WebElement emiCardName;

	@FindBy(id="ECexpmon")
	private WebElement emiExpiryMonth;

	@FindBy(id="ECexpyr")
	private WebElement emiExpiryYear;

	@FindBy(id="ECcvv")
	private WebElement emiCvv;

	@FindBy(xpath="//select[@id='emi_select']/option[2]")
	private WebElement citiEmi;

	@FindBy(xpath="//select[@id='emi_select']/option[2]")
	private WebElement hdfcEmi;

	@FindBy(xpath = "//input[@id='payment4']")
	private WebElement emiPayment;

	@FindBy(id="emi-options3")
	private WebElement emi3Months;

	@FindBy(id="emi-options6")
	private WebElement emi6Months;

	private Config testConfig;

	public MerchantEMITab(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,emiCardNum);
	}
	/**
	 * select Bank and duration for EMI
	 * @param paymentTypeRow
	 * @return
	 */
	public TestDataReader SelectMerchantBankAndDuration(int paymentTypeRow) 
	{
		TestDataReader data = new TestDataReader(testConfig,"PaymentType");

		String bank = data.GetData(paymentTypeRow, "PG_TYPE");;
		String duration = data.GetData(paymentTypeRow, "Bank Name");

		SelectMerchantBankAndDuration(bank,duration);

		return data;
	}

	/**
	 * Selects the given bank name and duration
	 * @param bank PG_Type
	 * @param duration visible duration
	 */
	public void SelectMerchantBankAndDuration(String bank, String duration) 
	{
		TestDataReader data = new TestDataReader(testConfig,"PaymentType");

		switch(bank)
		{
		case "HDFC2":
			bank ="HDFC";
			break;
		case "HDFCEMI":
			bank ="HDFC Bank";
			break;
		case "CITI": 
			bank="CITI";
			break;
		}

		Element.selectVisibleText(testConfig, emiSelect, bank, "Bank Name - " + bank);
		Browser.wait(testConfig, 1);
		switch(bank)
		{
		case "HDFC":
			if (duration.contentEquals("3 Months"))
			{
				Element.click(testConfig, emi3Months, "emi for 3 Months");
			}
			else if(duration.contentEquals("6 Months"))
			{

				Element.click(testConfig, emi6Months, "emi for 6 months");

			}	
			break;
		case "HDFC Bank":
			if (duration.contentEquals("3 Months"))
			{
				Element.click(testConfig, emi3Months, "emi for 3 Months");
			}
			else if(duration.contentEquals("6 Months"))
			{

				Element.click(testConfig, emi6Months, "emi for 6 months");

			}
			break;
		case "CITI":
			if (duration.contentEquals("3 Months"))
			{
				Element.click(testConfig, emi3Months, "emi for 3 Months");
			}
			else if(duration.contentEquals("6 Months"))
			{

				Element.click(testConfig, emi6Months, "emi for 6 months");

			}
			break;
		}
		Browser.wait(testConfig, 1);
	}


	/**
	 * fill card details for EMI card from sheet CardDetails
	 * @param cardRow
	 * @return data
	 */
	public TestDataReader FillEMICardDetails(int cardRow) 
	{
		TestDataReader data = new TestDataReader(testConfig,"CardDetails");

		String value = "";

		value = data.GetData(cardRow, "Name");
		Element.enterData(testConfig, emiCardName, value, "Name on Card");

		value = data.GetData(cardRow, "CC");
		Element.enterData(testConfig, emiCardNum, value, "Card Number");

		value = data.GetData(cardRow, "CVV");
		Element.enterData(testConfig, emiCvv, value, "CVV");

		value = data.GetData(cardRow, "Mon");
		Element.selectValue(testConfig, emiExpiryMonth, value, "Expiry month");

		value = data.GetData(cardRow, "Year");
		Element.selectValue(testConfig, emiExpiryYear, value, "Expiry year");

		return data;
	}
	/**
	 * clicks on EMI make payment
	 * @return Test Response Page
	 */
	public TestResponsePage clickMerchantEMIPaymentToGetTestResponsePage() 
	{
		Element.KeyPress(testConfig, emiPayment, Keys.RETURN, "EMI Make Payment");
		return new TestResponsePage(testConfig);
	}

	/**
	 * clicks on EMI make payment
	 */
	public void clickPayNowToGetError() {
		Element.KeyPress(testConfig, emiPayment, Keys.RETURN, "EMI Make Payment");
	}

	/**
	 * Click EMI make payment and get the Try Again Page
	 */
	public TryAgainPage clickMerchantEMIPaymentToGetTryAgainPage(){
		Element.KeyPress(testConfig, emiPayment, Keys.RETURN, "EMI Make Payment");
		return new TryAgainPage(testConfig);
	}

	/**
	 * Click EMI make payment and get the Error ResponsePage
	 */
	public ErrorResponsePage clickMerchantEMIPaymentToGetErrorResponsePage(){
		Element.KeyPress(testConfig, emiPayment, Keys.RETURN, "EMI Make Payment");
		return new ErrorResponsePage(testConfig);
	}

	/**
	 * Click EMI make payment and get the Bank Redirect Page
	 */
	public BankRedirectPage clickMerchantEMIPaymentToGetBankRedirectPage(){
		Element.KeyPress(testConfig, emiPayment, Keys.RETURN, "EMI Make Payment");
		return new BankRedirectPage(testConfig);
	}
	/**
	 * Click make payment  to get Offer Page, in case offer key is entered in transaction
	 *
	 */
	public OfferPage clickPayNowToGetOfferPage(){
		Element.KeyPress(testConfig, emiPayment, Keys.RETURN, "EMI Make Payment");
		return new OfferPage(testConfig);
	}

}
