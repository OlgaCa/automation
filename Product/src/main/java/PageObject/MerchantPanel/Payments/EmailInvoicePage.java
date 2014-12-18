package PageObject.MerchantPanel.Payments;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.GmailLogin;
import Utils.GmailVerification;
import Utils.Helper;
import Utils.TestDataReader;

public class EmailInvoicePage {

	private Config testConfig;

	public EmailInvoicePage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, amounT);
	}

	@FindBy(id="amount")
	private WebElement amounT;
	public void IsAmountDisplayed() {
		Element.verifyElementPresent(testConfig, amounT, "element field");
	}

	@FindBy(id="txnid")
	private WebElement txnID;

	@FindBy(id="productinfo")
	private WebElement productInfo;

	@FindBy(id="firstname")
	private WebElement firstName;

	@FindBy(id="email")
	private WebElement eMail;

	@FindBy(id="phone")
	private WebElement Phone;

	@FindBy(id="address1")
	private WebElement address;

	@FindBy(id="city")
	private WebElement City;

	@FindBy(id="state")
	private WebElement State;

	@FindBy(id="country")
	private WebElement Country;

	@FindBy(id="zipcode")
	private WebElement zipCode;

	@FindBy(name="submit")
	private WebElement Submit;
	
	@FindBy(xpath="//form[@id='invoice']/div[2]/div[2]/span/strong")
	private WebElement USD;
	public String getUSDText(){
		return USD.getText();
	}
	
	//Error Messages
	@FindBy(css="input#amount~label.error")
	private WebElement amountError;
	public String getAmountErrorMessage() {
		return amountError.getText();
	}
	
	@FindBy(css="input#txnid~label.error")
	private WebElement txnIdError;
	public String getTxnIDErrorMessage() {
		return txnIdError.getText();
	}
	
	@FindBy(css="div>textarea~label")
	private WebElement prodInfoError;
	public String getProdInfoErrorMessage() {
		return prodInfoError.getText();
	}
	
	@FindBy(css="input#firstname~label.error")
	private WebElement nameError;
	public String getNameErrorMessage() {
		return nameError.getText();
	}
	
	@FindBy(css="input#email~label.error")
	private WebElement emailError;
	public String getEmailErrorMessage() {
		return emailError.getText();
	}
	
	@FindBy(css="input#phone~label.error")
	private WebElement phoneError;
	public String getPhoneErrorMessage() {
		return phoneError.getText();
	}
	
	@FindBy(css="input#address~label.error")
	private WebElement addressError;
	public String getAddressErrorMessage() {
		return addressError.getText();
	}
	
	@FindBy(css="input#city~label.error")
	private WebElement cityError;
	public String getCityErrorMessage() {
		return cityError.getText();
	}
	
	@FindBy(css="input#state~label.error")
	private WebElement stateError;
	public String getStateErrorMessage() {
		return stateError.getText();
	}
	
	@FindBy(css="input#country~label.error")
	private WebElement countryError;
	public String getCountryErrorMessage() {
		return countryError.getText();
	}
	
	@FindBy(css="input#zipcode~label.error")
	private WebElement zipCodeError;
	public String getZipCodeErrorMessage() {
		return zipCodeError.getText();
	}
	
	public String set_transactionId()
	{
		String txnid = Helper.generateRandomAlphaNumericString(9);
		testConfig.putRunTimeProperty("txnId", txnid);
		return txnid;
	}

	// Do transaction through Email invoice
		// Entering data from Excel sheet "EmailInvoice"
		public TestDataReader fillInvoiceForm(int transactionRowNum) {
			TestDataReader transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			String value = "";

			value = transactionDetail.GetData(transactionRowNum, "amount");
			Element.enterData(testConfig, amounT, value, "Enter amount");

			if(testConfig.getRunTimeProperty("invalidTxnId")!= null)
				value = set_transactionId() + testConfig.getRunTimeProperty("invalidTxnId");
			else
			value = set_transactionId();
			Element.enterData(testConfig, txnID, value, "Enter Transaction Id");

			value = transactionDetail.GetData(transactionRowNum, "productinfo");
			Element.enterData(testConfig, productInfo, value, "Enter product info");

			if(testConfig.getRunTimeProperty("invalidName")!=null)
				value = testConfig.getRunTimeProperty("invalidName");
			else
			value = transactionDetail.GetData(transactionRowNum, "firstname");
			Element.enterData(testConfig, firstName, value, "Enter Name");

			value = transactionDetail.GetData(transactionRowNum, "email");
			Element.enterData(testConfig, eMail, value, "Enter email id");

			value = transactionDetail.GetData(transactionRowNum, "phone");
			Element.enterData(testConfig, Phone, value, "Enter Phone");

			value = transactionDetail.GetData(transactionRowNum, "address1");
			Element.enterData(testConfig, address, value, "Enter Address");

			value = transactionDetail.GetData(transactionRowNum, "city");
			Element.enterData(testConfig, City, value, "Enter City");

			value = transactionDetail.GetData(transactionRowNum, "state");
			Element.enterData(testConfig, State, value, "Enter State");

			value = transactionDetail.GetData(transactionRowNum, "country");
			Element.enterData(testConfig, Country, value, "Enter Country");

			value = transactionDetail.GetData(transactionRowNum, "zipcode");
			Element.enterData(testConfig, zipCode, value, "Enter Zipcode");

			return transactionDetail;
		}

		
		public void clickConfirmButton() {
			Element.click(testConfig, Submit, "Click on Submit");

		}
		/**
		 * Verifies that valid error messages are displayed for invalid input in
		 * Email invoice page
		 */
		public void verifyErrorMessages(int errorRowNum) {
			TestDataReader transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			
			String value = transactionDetail.GetData(errorRowNum, "amount");
			if(value.isEmpty()) 
				Element.verifyElementNotPresent(testConfig, amountError, "Amount Error");
			else
				Helper.compareEquals(testConfig, "Amount Entered", value,
						getAmountErrorMessage());
			
			value = transactionDetail.GetData(errorRowNum, "productinfo");
			if(value.isEmpty()) 
				Element.verifyElementNotPresent(testConfig, prodInfoError, "Product Info Error");
			else
			Helper.compareEquals(testConfig, "Product Desc Entered", value,
					getProdInfoErrorMessage());
			
			value = transactionDetail.GetData(errorRowNum, "firstname");
			if(value.isEmpty()) 
				Element.verifyElementNotPresent(testConfig, nameError, "Name Error");
			else
			Helper.compareEquals(testConfig, "Name Entered",
					value,getNameErrorMessage());
			
			value = transactionDetail.GetData(errorRowNum, "email");
			if(value.isEmpty()) 
				Element.verifyElementNotPresent(testConfig, emailError, "Email Error");
			else
			Helper.compareEquals(testConfig, "Email Entered",
					value,getEmailErrorMessage());
			
			value = transactionDetail.GetData(errorRowNum, "phone");
			
			Helper.compareEquals(testConfig, "Phone Entered",
				value,getPhoneErrorMessage());
			
		/*	value = transactionDetail.GetData(errorRowNum, "address1");
			if(value.isEmpty()) 
				Element.verifyElementNotPresent(testConfig, addressError, "Address Error");
			else
			Helper.compareEquals(testConfig, "Address Entered",
					value,getAddressErrorMessage());
			*/
			value = transactionDetail.GetData(errorRowNum, "city");
			if(value.isEmpty()) 
				Element.verifyElementNotPresent(testConfig, cityError, "City Error");
			else
			Helper.compareEquals(testConfig, "City Entered",
				value,getCityErrorMessage());
			
			value = transactionDetail.GetData(errorRowNum, "state");
			if(value.isEmpty()) 
				Element.verifyElementNotPresent(testConfig, stateError, "State Error");
			else
			Helper.compareEquals(testConfig, "State Entered",
					value,getStateErrorMessage());
			
			value = transactionDetail.GetData(errorRowNum, "country");
			if(value.isEmpty()) 
				Element.verifyElementNotPresent(testConfig, countryError, "Country Error");
			else
			Helper.compareEquals(testConfig, "Country Entered",
					value,getCountryErrorMessage());
			
			value = transactionDetail.GetData(errorRowNum, "zipcode");
			if(value.isEmpty()) 
				Element.verifyElementNotPresent(testConfig, zipCodeError, "ZipCode Error");
			else
			Helper.compareEquals(testConfig, "Zipcode Entered",
					value,getZipCodeErrorMessage());
			
		}

		 @FindBy(css = "div.uploadDiv form input:nth-child(3)")
		 private WebElement chooseFileButton;
		 
		 @FindBy(css = "form > input[type=\"submit\"]")
		 private WebElement submitButton;
		 
		 public void uploadMassInvoiceExcel(Config testConfig, String filePath) {

		Browser.wait(testConfig, 5);
		Element.enterFileName(testConfig, chooseFileButton, filePath, "MassInvoice Upload");
	    Element.click(testConfig, submitButton, "Click on submit");
		 }
		 
			
			//Verify Link from Gmail
			public PaymentOptionsPage gmailLoginAndClickPaymentLink(String TxnID) {
				GmailLogin gmailLogin = navigateToGmail(testConfig);
				GmailVerification gmailVerification =  gmailLogin.Login("payu.testing","payu@123");
				// wait for email
				Browser.wait(testConfig, 5);
				gmailVerification.searchMail(TxnID);
				gmailVerification.SelectEmail();
				Browser.wait(testConfig,2);
				Element.click(testConfig, Element.getPageElement(testConfig, How.partialLinkText, "https://secure.payu.in/processInvoice?invoiceId"), "Click on the link");
				Browser.switchToNewWindow(testConfig);

				return new PaymentOptionsPage(testConfig);

			}
			
			public GmailLogin navigateToGmail(Config testConfig)
			 {
				 	String url = "https://mail.google.com/mail/u/0/?shva=1#inbox";
					Browser.navigateToURL(testConfig,url);
					return new GmailLogin(testConfig);
			 }
			
			/**
			 * Verifies amount error message
			 */
			public void verifyAmountErrorMessage(int errorRowNum) {
				TestDataReader transactionDetail = new TestDataReader(testConfig,
						"TransactionDetails");
				
				String value = transactionDetail.GetData(errorRowNum, "amount");
				if(value.isEmpty()) 
					Element.verifyElementNotPresent(testConfig, amountError, "Amount Error");
				else
					Helper.compareEquals(testConfig, "Amount Entered", value,
							getAmountErrorMessage());
				
			}

}
