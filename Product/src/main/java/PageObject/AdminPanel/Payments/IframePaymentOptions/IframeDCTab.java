package PageObject.AdminPanel.Payments.IframePaymentOptions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class IframeDCTab {
	
	Config testConfig;
	
	public IframeDCTab(Config testConfig){
		this.testConfig=testConfig;
		
		PageFactory.initElements(this.testConfig.driver,this);
		Browser.waitForPageLoad(testConfig, debitCardMakePayment);
		
	}
	
	
	//Debit Card type
		@FindBy(css = "a[href*='DebitCard']")
		private WebElement DCTabtxt;

		public String getDCTabText() {
			return DCTabtxt.getText();	
		}
		
		//Label Text Fields
		@FindBy(xpath="//div[@id='DebitCard']//input[@name='card_number']")
		private WebElement debitCardNumber;

		@FindBy(xpath="//div[@id='DebitCard']//input[@name='name_on_card']")
		private WebElement debitCardName;

		@FindBy(xpath="//div[@id='DebitCard']//select[@name='expiry_month']")
		private WebElement debitCardExpiryMonth;

		@FindBy(xpath="//div[@id='DebitCard']//select[@name='expiry_year']")
		private WebElement debitCardExpiryYear;

		@FindBy(xpath="//div[@id='DebitCard']//input[@name='cvv']")
		private WebElement debitCardCvvNo;

		@FindBy(xpath="//div[@id='DebitCardForm']//input[@class='button']")
		private WebElement debitCardMakePayment;
		
		@FindBy(xpath="//div[@id='DebitCard']//input[@name='card_number']/following-sibling::span")
		private WebElement debitCardTypeIcon;
		
		@FindBy(xpath="//div[@id='DebitCardForm']//*[@class='HideCvvExpiry message']")
		private WebElement hideDCCVVandEXP;
		
		public void enterDCNumber(String strDCNumber){
			Element.enterData(testConfig, debitCardNumber, strDCNumber, "Card Number");
		}
		
		public void enterNameOnCard(String strNameOnCard){
			Element.enterData(testConfig, debitCardName, strNameOnCard, "Name on Card");
		}
		
		public void hideCVVAndEXP(){
			Element.click(testConfig, hideDCCVVandEXP, "Hide DC Cvv and Exp");
		}

		/**
		 * Reads the "CardDetails" sheet of Test Data excel and fill the debit card
		 * details
		 * 
		 * @param cardRow
		 *            Row number of excel sheet to be filled in
		 * @return Returns the Excel sheet data which was filled
		 */
		public TestDataReader data;
		public TestDataReader FillCardDetails(int cardRow) 
		{
			if(data==null)
			{
				data = new TestDataReader(testConfig,"CardDetails");
			}

			String value = "";

			value = data.GetData(cardRow, "CC");
			enterDCNumber(value);

			value = data.GetData(cardRow, "Name");
			enterNameOnCard(value);

			value = data.GetData(cardRow, "CVV");
			Element.enterData(testConfig, debitCardCvvNo, value, "CVV");

			value = data.GetData(cardRow, "Mon");
			Element.selectValue(testConfig, debitCardExpiryMonth, value, "Expiry month");

			value = data.GetData(cardRow, "Year");
			Element.selectValue(testConfig, debitCardExpiryYear, value, "Expiry year");

			return data;
		}
		
		public IframeDCTab clickOnMakepayment(){
			Element.KeyPress(testConfig, debitCardMakePayment, Keys.RETURN, "DebitCard MakePayment");
			return new IframeDCTab(testConfig);
		}

		/**
		 * Click make payment and get the Test Response Page
		 * @return 
		 */
		public TestResponsePage clickIframeDCPaymentToGetTestResponsePage() 
		{
			Element.click(testConfig, debitCardMakePayment, "DebitCard MakePayment");
			return new TestResponsePage(testConfig);
		}
		
		public String getCardType(){
			return debitCardTypeIcon.getAttribute("class").split(" ")[1];
		}
		
		String strTextBoxerrorClass="textbox validate error";
		
		
		public void verifyDCValidations(String[] fieldsToValidate){
			for(String fieldToValidate:fieldsToValidate){
				switch (fieldToValidate) {
				case "DCNUMBER":
					Helper.compareEquals(testConfig, "DC invalid card", strTextBoxerrorClass, debitCardNumber.getAttribute("class"));
					break;
				case "DCNAME":
					Helper.compareEquals(testConfig, "DC invalid name on card", strTextBoxerrorClass, debitCardName.getAttribute("class"));
					break;
				case "DCCVV":
					Helper.compareEquals(testConfig, "DC invalid CVV", strTextBoxerrorClass, debitCardCvvNo.getAttribute("class"));
					break;
				case "DCEXP":
					Helper.compareEquals(testConfig, "DC invalid Expiry month", "ExpiryMonth textbox validate error", debitCardExpiryMonth.getAttribute("class"));
					Helper.compareEquals(testConfig, "DC invalid Expiry year", "ExpiryYear textbox validate error", debitCardExpiryYear.getAttribute("class"));
					break;
				default:
					Helper.compareEquals(testConfig, "DC invalid card", strTextBoxerrorClass, debitCardNumber.getAttribute("class"));
					Helper.compareEquals(testConfig, "DC invalid name on card", strTextBoxerrorClass, debitCardName.getAttribute("class"));
					Helper.compareEquals(testConfig, "DC invalid CVV", strTextBoxerrorClass, debitCardCvvNo.getAttribute("class"));
					Helper.compareEquals(testConfig, "DC invalid Expiry month", "ExpiryMonth textbox validate error", debitCardExpiryMonth.getAttribute("class"));
					Helper.compareEquals(testConfig, "DC invalid Expiry year", "ExpiryYear textbox validate error", debitCardExpiryYear.getAttribute("class"));
					break;
				}
			}
		}
}
