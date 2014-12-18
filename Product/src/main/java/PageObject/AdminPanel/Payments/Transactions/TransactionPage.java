package PageObject.AdminPanel.Payments.Transactions;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.IframePaymentOptions.IframePaymentOptionPage;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantPaymentOptionPage;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionMobilePage;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.PaymentOptions.ProcessingPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.EnforceWrongPage;
import PageObject.AdminPanel.Payments.Response.ErrorResponsePage;
import PageObject.AdminPanel.Payments.Response.NewResponsePage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import PageObject.MerchantPanel.Offers.OfferRetryPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestDataReader;

public class TransactionPage {

	private Config testConfig;

	public String addedOn;
	
	@FindBy(xpath="//input[@type='search']")
	private WebElement merchantSelect;

	@FindBy(id="selectMerchant")
	private WebElement merchantKeySelect;


	@FindBy(name="amount")
	private WebElement amount;

	@FindBy(name="additional_charges")
	private WebElement convenienceFee;

	@FindBy(name="email")
	private WebElement email;

	@FindBy(name="surl")
	private WebElement sURL;

	@FindBy(name="furl")
	private WebElement fURL;

	@FindBy(name="codurl")
	private WebElement codURL;

	@FindBy(name="pubkey")
	private WebElement publicKey;

	@FindBy(name="offer_key")
	private WebElement offer_key;

	@FindBy(name="firstname")
	private WebElement firstName;

	@FindBy(name="lastname")
	private WebElement lastname;

	@FindBy(name="phone")
	private WebElement phone;

	@FindBy(name="productinfo")
	private WebElement productinfo;

	@FindBy(name="user_credentials")
	private WebElement user_credentials;

	@FindBy(name="address1")
	private WebElement address1;

	@FindBy(name="address2")
	private WebElement address2;

	@FindBy(name="city")
	private WebElement city;

	@FindBy(name="state")
	private WebElement state;

	@FindBy(name="country")
	private WebElement country;

	@FindBy(name="zipcode")
	private WebElement zipcode;

	@FindBy(name="enforce_paymethod")
	private WebElement enforcePayment;

	@FindBy(name="drop_category")
	private WebElement dropCategory;

	@FindBy(name="udf1")
	private WebElement udf1;

	@FindBy(name="udf2")
	private WebElement udf2;

	@FindBy(name="udf3")
	private WebElement udf3;

	@FindBy(name="udf4")
	private WebElement udf4;

	@FindBy(name="udf5")
	private WebElement udf5;

	@FindBy(name="udf6")
	private WebElement udf6;

	@FindBy(name="pg")
	private WebElement pg;

	@FindBy(name="bankcode")
	private WebElement ibiboCode;

	@FindBy(name = "ccnum")
	private WebElement CardNumber;

	@FindBy(name = "ccname")
	private WebElement CardName;

	@FindBy(name = "ccvv")
	private WebElement CVV;

	@FindBy(name = "ccexpmon")
	private WebElement ExpMonth;

	@FindBy(name = "ccexpyr")
	private WebElement ExpYear;

	@FindBy(css="span")
	private WebElement ErrorMessage;

	@FindBy(id="footer")
	private WebElement Footer;

	@FindBy(name="txnid")
	private WebElement txnId;

	@FindBy(name="pre_init_mode")
	private WebElement preInitMode;

	@FindBy(name="shipping_firstname")
	private WebElement shippingFirstName;

	@FindBy(name="shipping_lastname")
	private WebElement shippingLastName;

	@FindBy(name="shipping_address1")
	private WebElement shippingAddress1;

	@FindBy(name="shipping_address2")
	private WebElement shippingAddress2;

	@FindBy(name="shipping_city")
	private WebElement shippingCity;

	@FindBy(name="shipping_state")
	private WebElement shippingState;

	@FindBy(name="shipping_country")
	private WebElement shippingCountry;

	@FindBy(name="shipping_zipcode")
	private WebElement shippingZipcode;

	@FindBy(name="shipping_phone")
	private WebElement shippingPhone;

	@FindBy(name="store_card")
	private WebElement storeCardFlag;

	@FindBy(name="card_name")
	private WebElement storeCardName;

	@FindBy(name="store_card_token")
	private WebElement storeCardToken;

	@FindBy(name="si")
	private WebElement SI;
	
	@FindBy(name="force_pgid")
	private WebElement force_pgid;
	
	@FindBy(xpath="//input[@value='Submit']")
	private WebElement submitButton;

	public TransactionPage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, amount);

		//Storing it so that it can be used globally
		testConfig.putRunTimeProperty("transactionId", txnId.getAttribute("value"));
		testConfig.putRunTimeProperty("txnid", txnId.getAttribute("value"));
	}

	/**
	 * Get Error Message in submitting the transaction
	 * @return Error String
	 */
	public String getErrorMessage() 
	{
		return ErrorMessage.getText();
	}

	@FindBy(xpath="//input[@type='search']")
	WebElement inp_keyword_merchant;
	
	@FindBy(xpath="//button[@type='button']")
	private WebElement merchantKey;

	/**
	 * Select the Merchant from drop down
	 * @param key Merchant to be selected
	 */
	public void selectMerchant(String key)
	{
		Element.click(testConfig, merchantKey, "Merchant Drop Down");
		Browser.wait(testConfig, 1);
		String searchText = " - " + key;
		Element.enterData(testConfig, inp_keyword_merchant, searchText , "Search Merchant Key");
		Browser.wait(testConfig, 2); 

		String xpath="//label/span[contains(text(),'"+searchText+"')]";
		Element.click(testConfig, Element.getPageElement(testConfig, How.xPath,xpath),"Merchant");
	}

	/**
	 * Reads the "TransactionDetails" sheet of Test Data excel and fill the transaction details
	 * @param transactionRow Row number of excel sheet to be filled in
	 * @return Returns the Excel sheet data which was filled
	 */
	public TestDataReader FillTransactionDetails(int transactionRow)
	{ 
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = "";

		//If environment specific key is present then use that
		value = data.GetCurrentEnvironmentData(transactionRow, "Key");
		
		selectMerchant(value);
		
		String amount1 = testConfig.getRunTimeProperty("amount");
		if(null != amount1)
			value = amount1;
		else 
			value = data.GetData(transactionRow, "Amount");

		Element.enterData(testConfig, amount, value, "Amount");

		String Convenience_fee = testConfig.getRunTimeProperty("additionalCharges");
		if(Convenience_fee!=null){
			Element.enterData(testConfig, convenienceFee, Convenience_fee, "Convenience Fee");
		}
		else{
			value = data.GetData(transactionRow, "additionalCharges");
			Element.enterData(testConfig, convenienceFee, value, "Convenience Fee");
		}

		String emailid = testConfig.getRunTimeProperty("email");
		if(emailid!=null){
			Element.enterData(testConfig, email, emailid, "Email");
		}
		else{
			value = data.GetData(transactionRow, "Email");
			Element.enterData(testConfig, email, value, "Email");
		}

		value = data.GetData(transactionRow, "FirstName");
		Element.enterData(testConfig, firstName, value, "First Name");

		String phoneNum = testConfig.getRunTimeProperty("phone");
		if(phoneNum!=null){
			Element.enterData(testConfig, phone, phoneNum, "Phone");
		}
		else{
			value = data.GetData(transactionRow, "Phone");
			Element.enterData(testConfig, phone, value, "Phone");
		}

		value = data.GetData(transactionRow, "Productinfo");
		Element.enterData(testConfig, productinfo, value, "Product Info");

		value = data.GetData(transactionRow, "Surl");
		Element.enterData(testConfig, sURL, value, "success URL");

		value = data.GetData(transactionRow, "Furl");
		Element.enterData(testConfig, fURL, value, "Failure URL");
		
		value = data.GetData(transactionRow, "force_pgid");
		Element.enterData(testConfig, force_pgid, value, "force pgid");

		value = data.GetData(transactionRow, "CODurl");
		Element.enterData(testConfig, codURL, value, "COD URL");

		value = testConfig.getRunTimeProperty("publickey");
		if(value!=null)
			Element.enterData(testConfig, publicKey, value, "Public Key");

		value = testConfig.getRunTimeProperty("UseStoredDetails");
		if(value!=null){
			if(value.equals("1"))
				Element.enterData(testConfig, user_credentials, testConfig.getRunTimeProperty("merchantKey") + ":" + testConfig.getRunTimeProperty("bankcode"), "User Credentials");
			String rnd;
			if(value.equals("2")){
				if (testConfig.getRunTimeProperty("userString")!=null)
					Element.enterData(testConfig,user_credentials,testConfig.getRunTimeProperty("userString"),"User Credentials");
				else{
					rnd= Helper.generateRandomAlphaNumericString(4);
					value=testConfig.getRunTimeProperty("merchantKey") + ":" + testConfig.getRunTimeProperty("bankcode")+rnd;
					Element.enterData(testConfig, user_credentials, value, "User Credentials");
					testConfig.putRunTimeProperty("userString", value);
				}
			}
		}	

		//Pass SI value and user credential in case of SI transaction
		value = testConfig.getRunTimeProperty("SI");
		if(value!=null)
		{
			if (testConfig.getRunTimeProperty("userString")!=null){
				Element.enterData(testConfig, SI, value, "SI");
				Element.enterData(testConfig,user_credentials,testConfig.getRunTimeProperty("userString"),"User Credentials");
			}

			else{				
				Element.enterData(testConfig, SI, value, "SI");
				String rnd= Helper.generateRandomAlphaNumericString(6);
				testConfig.putRunTimeProperty("storeCardUserCredential", testConfig.getRunTimeProperty("UseStoredDetails") + ":" + rnd);
				Element.enterData(testConfig, user_credentials, testConfig.getRunTimeProperty("storeCardUserCredential"), "User Credentials");
				testConfig.putRunTimeProperty("userString", testConfig.getRunTimeProperty("storeCardUserCredential"));
			}
		}

		value = testConfig.getRunTimeProperty("offerKey");
		if(value!=null)
			Element.enterData(testConfig, offer_key, value, "Offer Key");

		value = data.GetData(transactionRow, "LastName");
		Element.enterData(testConfig, lastname, value, "Last Name");

		value = data.GetData(transactionRow, "Address1");
		Element.enterData(testConfig, address1, value, "Address1");

		value = data.GetData(transactionRow, "Address2");
		Element.enterData(testConfig, address2, value, "Address2");

		value = data.GetData(transactionRow, "City");
		Element.enterData(testConfig, city, value, "City");

		value = data.GetData(transactionRow, "State");
		Element.enterData(testConfig, state, value, "State");

		value = data.GetData(transactionRow, "ZipCode");
		Element.enterData(testConfig, zipcode, value, "Zip Code");

		value = data.GetData(transactionRow, "Country");
		Element.enterData(testConfig, country, value, "Country");

		value = data.GetData(transactionRow, "EnforcePayMethod");
		Element.enterData(testConfig, enforcePayment, value, "EnforcePayMethod");

		value = data.GetData(transactionRow, "DropCategory");
		if(testConfig.getRunTimeProperty("DropCategory") != null){
			value = testConfig.getRunTimeProperty("DropCategory");
		}
		Element.enterData(testConfig, dropCategory, value, "DropCategory");

		value = data.GetData(transactionRow, "UDF1");
		Element.enterData(testConfig, udf1, value, "UDF1");

		value = data.GetData(transactionRow, "UDF2");
		Element.enterData(testConfig, udf2, value, "UDF2");

		value = data.GetData(transactionRow, "UDF3");
		Element.enterData(testConfig, udf3, value, "UDF3");

		value = data.GetData(transactionRow, "UDF4");
		Element.enterData(testConfig, udf4, value, "UDF4");

		value = data.GetData(transactionRow, "UDF5");
		Element.enterData(testConfig, udf5, value, "UDF5");

		value = data.GetData(transactionRow, "ShippingFirtsname");
		Element.enterData(testConfig, shippingFirstName, value, "Shipping Firtsname");

		value = data.GetData(transactionRow, "ShippingLatsname");
		Element.enterData(testConfig, shippingLastName, value, "Shipping Latsname");

		value = data.GetData(transactionRow, "ShippingAddress1");
		Element.enterData(testConfig, shippingAddress1, value, "Shipping Address1");

		value = data.GetData(transactionRow, "ShippingAddress2");
		Element.enterData(testConfig, shippingAddress2, value, "Shipping Address2");

		value = data.GetData(transactionRow, "ShippingCity");
		Element.enterData(testConfig, shippingCity, value, "Shipping City");

		value = data.GetData(transactionRow, "ShippingState");
		Element.enterData(testConfig, shippingState, value, "Shipping State");

		value = data.GetData(transactionRow, "ShippingCountry");
		Element.enterData(testConfig, shippingCountry, value, "Shipping Country");

		value = data.GetData(transactionRow, "ShippingZipcode");
		Element.enterData(testConfig, shippingZipcode, value, "Shipping zip code");

		value = data.GetData(transactionRow, "ShippingPhone");
		Element.enterData(testConfig, shippingPhone, value, "Shipping Phone");

		String preInitMode1 = testConfig.getRunTimeProperty("preInitMode");
		if(null != preInitMode1)
			value = preInitMode1;
		else 
			value = data.GetData(transactionRow, "preInitMode");
		Element.enterData(testConfig, preInitMode, value, "preInitMode");

		return data;
	}

	/**
	 * Reads the "CardDetails" sheet of Test Data excel and fill the credit card details needed for 'Seamless' transaction
	 * @param cardRow Row number of excel sheet to be filled in
	 * @return Returns the Excel sheet data which was filled
	 */
	public TestDataReader FillCardDetails(int cardRow) 
	{
		TestDataReader data = new TestDataReader(testConfig,"CardDetails");

		String value = "";

		value = data.GetData(cardRow, "Name");
		Element.enterData(testConfig, CardName, value, "Name on Card");

		value = data.GetData(cardRow, "CC");
		Element.enterData(testConfig, CardNumber, value, "Card Number");

		value = data.GetData(cardRow, "CVV");
		Element.enterData(testConfig, CVV, value, "CVV");

		value = data.GetData(cardRow, "Mon");
		Element.enterData(testConfig, ExpMonth, value, "Expiry month");

		value = data.GetData(cardRow, "Year");
		Element.enterData(testConfig, ExpYear, value, "Expiry year");

		return data;
	}

	/**
	 * Reads the sheet specified and fills the data specified in 'mode' and 'Ibibo_Code' columns
	 * @param sheetName can be 'CardCodes' or 'NetBanking' excel sheet
	 * @param seamlessCodeRow Row number of excel sheet to be filled in
	 * @return Returns the Excel sheet data which was filled
	 */
	/**
	 * Reads the "PaymentType" sheet of Test Data excel and fills the PG(mode) and Ibibo Code(bankcode) fields
	 * @param paymentTypeRow Row number of excel sheet to be filled in
	 * @return Returns the Excel sheet data which was filled
	 */
	public TestDataReader FillSeamlessCodes(int paymentTypeRow) 
	{
		TestDataReader data = new TestDataReader(testConfig, "PaymentType");

		String pgCodeValue = "";

		//Put the correct Seamless codes
		pgCodeValue = data.GetData(paymentTypeRow, "mode");
		switch(pgCodeValue)
		{
		case "all": pgCodeValue = "all";
		break;
		case "cashcard": pgCodeValue = "cash";
		break;
		case "cod": pgCodeValue = "cod";
		break;
		case "creditcard": pgCodeValue = "CC";
		break;
		case "debitcard": pgCodeValue = "DC";
		break;
		case "emi": pgCodeValue = "emi";
		break;
		case "ivr": pgCodeValue = "ivr";
		break;
		case "netbanking": pgCodeValue = "NB";
		break;
		case "wallet" : pgCodeValue = "Wallet";
		break;
		}

		Element.enterData(testConfig, pg, pgCodeValue, "PG Code");

		String IbiboCodeValue = data.GetData(paymentTypeRow, "bankcode");
		Element.enterData(testConfig, ibiboCode, IbiboCodeValue, "Ibibo Code");
		return data;
	}

	/**
	 * Reads the "StoreCard" sheet of Test Data excel and fills the SavedCardName field
	 * and sets "the this is store card" field
	 * @param storeCardRow Row number of excel sheet to be filled in
	 * @return Returns the Excel sheet data which was filled
	 */
	public TestDataReader FillSeamlessStoreCardDetails(int storeCardRow) 
	{
		TestDataReader data = new TestDataReader(testConfig, "StoreCard");
		String value = "";
		value = data.GetData(storeCardRow, "SavedCardName");
		String rnd="";
		if (testConfig.getRunTimeProperty("UseStoredDetails").equals("2"))
		{
			rnd= Helper.generateRandomAlphaNumericString(5);
			Element.enterData(testConfig, storeCardName, value+rnd, "Store CardName");
			testConfig.putRunTimeProperty("StoreCardName", value+rnd);
		}
		else
			Element.enterData(testConfig, storeCardName, value, "Store CardName");
		String storeCardFlagInput=data.GetData(storeCardRow, "StoreCardFlag");
		if(storeCardFlagInput=="{skip}")
				Element.enterData(testConfig,storeCardFlag , "1", "SavedCard flag field");
		else
			Element.enterData(testConfig,storeCardFlag , storeCardFlagInput, "SavedCard flag field");
		return data;
	}

	public TestDataReader FillSeamlessSavedCardDetails(int storeCardRow) 
	{
		TestDataReader data = new TestDataReader(testConfig, "StoreCard");
		String value = "";
		if (testConfig.getRunTimeProperty("StoreCardToken")!=null){
			value=testConfig.getRunTimeProperty("StoreCardToken");
			Element.enterData(testConfig, storeCardToken, value, "StoreCard Token");
			value = data.GetData(storeCardRow, "CVV");
			Element.enterData(testConfig, CVV, value, "Saved Card CVV");					
		}
		else{
			testConfig.logFail("Saved Card Token is missing");
		}
		testConfig.removeRunTimeProperty("StoreCardToken");
		return data;
	}



	/**
	 * Do Submit to get the PaymentOptionsPage
	 * @return Payment Options Page
	 */
	public PaymentOptionsPage Submit()
	{
		Element.submit(testConfig, amount, "Transaction");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		if(testConfig.isMobile)
			return new PaymentOptionMobilePage(testConfig);
		else
			return new PaymentOptionsPage(testConfig);
	}
	
	/**
	 * Submits Iframe transaction
	 * @return Iframe Transaction Payment Page
	 */

	public IframePaymentOptionPage IframeTransactionSubmit()
	{
		Element.submit(testConfig, amount, "Transaction");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		return new IframePaymentOptionPage(testConfig);
	}
	
	/**
	 * Submits merchant transaction
	 * @return Merchant Transaction Payment Page
	 */

	public MerchantPaymentOptionPage MerTransactionSubmit()
	{
		Element.submit(testConfig, amount, "Transaction");
		testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		return new MerchantPaymentOptionPage(testConfig);
	}
	/**
	 * Do Submit to get the Bank Redirect Page
	 * @return BankRedirectPagesss
	 */
	public BankRedirectPage SubmitToGetBankRedirectPage()
	{
		if (testConfig.getRunTimeProperty("popup")!=null){
			Element.submit(testConfig, amount, "Transaction");
			Alert alert = testConfig.driver.switchTo().alert();
			alert.accept();

		}
		else
		{
			Element.submit(testConfig, amount, "Transaction");
		}
		return new BankRedirectPage(testConfig);
	}

	/**
	 * Do Submit to get the Test Response Page
	 * @return TestResponsePage
	 */
	public TestResponsePage SubmitToGetTestResponsePage()
	{
		Element.submit(testConfig, amount, "Transaction");
		return new TestResponsePage(testConfig);
	}

	/**
	 * Do Submit to get the Test Response Page
	 * @return TestResponsePage
	 */
	public NewResponsePage SubmitToGetNewResponsePage()
	{
		Element.submit(testConfig, Footer, "Transaction");
		return new NewResponsePage(testConfig);
	}

	/**
	 * Do Submit to get the Error Response Page
	 * @return ErrorResponsePage
	 */
	public ErrorResponsePage SubmitToGetErrorResponsePage()
	{
		Element.submit(testConfig, amount, "Transaction");
		return new ErrorResponsePage(testConfig);
	}

	/**
	 * Do Submit to get error on page
	 */
	public void SubmitToGetErrorOnPage()
	{
		Element.submit(testConfig, amount, "Transaction");
	}

	public EnforceWrongPage SubmitToGetEnforcewrongPage() {
		Element.submit(testConfig, amount, "Transaction");
		return new EnforceWrongPage(testConfig);
	}


	/**
	 * Do Submit to get the Error Response Page
	 * @return TryAgainPage
	 */
	public TryAgainPage SubmitToGetTryAgainPage()
	{
		Element.submit(testConfig, amount, "Transaction");
		return new TryAgainPage(testConfig);
	}
	public OfferRetryPage SubmitToGetOfferRetry() {
		Element.submit(testConfig, amount, "Transaction");
		return new OfferRetryPage(testConfig);
	}
	public ProcessingPage SubmitToGetProcessingPage()
	{
		Element.submit(testConfig, amount, "Processing Transaction");
		return new ProcessingPage(testConfig, false);
	}
	
	public TestResponsePage SubmitToGetTestResponsePageAfterTakingProcessingPageURL()
	{
		Element.click(testConfig, submitButton, "Transaction");
		setProcessingPageURLProperty();
		return new TestResponsePage(testConfig);
	}
	
	public void setProcessingPageURLProperty(){
	//	ProcessingPage processingUrl=new ProcessingPage(testConfig, false);
	//	Browser.waitForPageLoad(testConfig, processingUrl.getAmountElementOnProcessingpage());
		String URL= testConfig.driver.getCurrentUrl().trim();
		testConfig.putRunTimeProperty("Processing_Page_URL_Actual", URL);
	}
	
	public BankRedirectPage SubmitToGetBankRedirectPageAfterTakingProcessingPageURL()
	{
		if (testConfig.getRunTimeProperty("popup")!=null){
			Element.click(testConfig, submitButton, "Transaction");
			setProcessingPageURLProperty();
			Alert alert = testConfig.driver.switchTo().alert();
			alert.accept();

		}
		else
		{
			Element.click(testConfig, submitButton, "Transaction");
			setProcessingPageURLProperty();
		}
		return new BankRedirectPage(testConfig);
	}
	
	
	public void clickSubmitButton(){
		Element.click(testConfig, submitButton, "Submitting Form");
	}
	/**
	 * Submits transaction in case seamless transaction for payumwallet 
	 * @return GuestCheckout Page
	 */
	/*public PaymentLoginPage SubmitToGetPayuMoneyPage()
	{
		Element.submit(testConfig, amount, "Processing Transaction");
		return new PaymentLoginPage(testConfig);
	}*/

	/**Enters value of PG when on transaction Page
	 * @param Value
	 */
	public void enterPgValue(String Value){
		Element.enterData(testConfig, pg, Value, "PG Code");
	}
	
	/**Enters value of Ibibo code when on transaction Page
	 * @param Value
	 */
	public void enterIbibiCodeValue(String Value){
		Element.enterData(testConfig, ibiboCode, Value, "IBIBo Code");
	}

}
