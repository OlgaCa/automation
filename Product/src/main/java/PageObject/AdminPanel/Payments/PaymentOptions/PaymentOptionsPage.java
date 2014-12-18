package PageObject.AdminPanel.Payments.PaymentOptions;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

import org.openqa.selenium.Keys;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


//import MoneyPageObject.Payments.PaymentLoginPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.ErrorResponsePage;
import PageObject.AdminPanel.Payments.Response.NewResponsePage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Response.ThreeDSecurePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import PageObject.MerchantPanel.Offers.OfferRetryPage;
import Test.AdminPanel.Payments.FeedBackForm;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;

public class PaymentOptionsPage {

	protected Config testConfig;


	public PaymentOptionsPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);

		if(testConfig.isMobile) Browser.waitForPageLoad(this.testConfig, getCCTab());
		else Browser.waitForPageLoad(this.testConfig, amount);
	}

	//Header section
	//@FindBy(css="span")
	@FindBy(xpath="//p[@id='amountdiv']/span")
	private WebElement amount;
	public String getAmount() {
		return amount.getText();
	}

	@FindBy(css="#txndiv > span")
	private WebElement transactionID;
	public String getTransactionId() {
		return transactionID.getText();
	}

	@FindBy(id="FeebackButton")
	private WebElement feedbackHelpButton;
	public void clickFeedBackButton(){
		Element.click(testConfig, feedbackHelpButton, "FeedBack Button");


	}

	public void verifyFeedBackFormPresent(){
		Helper.compareTrue(testConfig, "FeedbackForm Button", Element.IsElementDisplayed(testConfig, feedbackHelpButton));

	}

	@FindBy(id="order-details")
	private WebElement orderDetails;

	@FindBy(css="h3.prepend-1.span-3")
	private WebElement billingLabel;
	public String getBillingLabel() {
		return billingLabel.getText();
	}

	@FindBy(css="span.hide-show-button")
	private WebElement showDetails;
	public void clickShowHideDetails(){
		Element.click(testConfig, showDetails, "Show and Hide Details");
		Browser.wait(testConfig, 1);
	}

	@FindBy(css="div.billing-name.span-5")
	private WebElement billingName;
	public String getBillingName(){
		return billingName.getText();
	}

	@FindBy(css="p.span-6.flushright")
	private WebElement billingAddress;
	public String getBillingAddress(){
		return billingAddress.getText();
	}

	@FindBy(css="div.payment_box_header p")
	private WebElement paymentTypeLabel;
	public String getChoosePaymentTypeLabel() {
		return paymentTypeLabel.getText();
	}

	//Tabs
	@FindBy(id="creditlink")
	private WebElement CCTab;
	public WebElement getCCTab() {
		return CCTab;
	}

	public CCTab clickCCTab() 
	{
		Element.click(testConfig, getCCTab(), "Credit Card Tab");
		if(testConfig.isMobile)
			return new CCMobileTab(testConfig);
		else
			return new CCTab(testConfig);
	}

	@FindBy(css="a[href='#debit']")
	private WebElement DCTab;
	public DCTab clickDCTab() {
		Element.click(testConfig, DCTab, "Debit Card Tab");
		return new DCTab(testConfig);
	}

	@FindBy(css="a[href='#netbanking']")
	private WebElement NBTab;
	public NBTab clickNBTab() {
		Element.click(testConfig, NBTab, "Net Banking Tab");
		return new NBTab(testConfig);
	}

	@FindBy(css="a[href='#cashcard']")
	private WebElement CashCardTab;
	public CashCardTab clickCashCardTab() {
		Element.click(testConfig, CashCardTab, "Cash Card Tab");
		return new CashCardTab(testConfig);
	}

	@FindBy(css="a[href='#emi']")
	private WebElement EMITab;
	public EMITab clickEMITab() {
		Element.click(testConfig, EMITab, "EMI Tab");
		return new EMITab(testConfig);
	}

	@FindBy(css="a[href='#cod']")
	private WebElement CODTab;
	public CODTab clickCODTab() {
		Element.click(testConfig, CODTab, "COD Tab");
		return new CODTab(testConfig);
	}	

	// PayUMoneyTab Tab

	@FindBy(css="a[href='#wallet']")
	private WebElement PayUMoneyTab;
	public PayUMoneyTab clickPayUMoneyTab(){
		Element.click(testConfig, PayUMoneyTab, "PayUMoney Tab");
		return new PayUMoneyTab(testConfig);
	}

	//Pay now
	@FindBy(id="pay_button")
	private WebElement payNow;

	//Pay using PayuMoney
	@FindBy(css="input.button")
	private WebElement payUsingPayuMoney;

	//NextButton
	@FindBy(css="input.next_btn")
	private WebElement nextButton;

	//Go Back text
	@FindBy(css="div.payment-buttons.append-bottom > span > a")
	private WebElement goBack;
	public String goBackText() {
		return goBack.getText();
	}	

	@FindBy(xpath="//a[contains(text(),'X')]")
	private WebElement closeFeedback;

	@FindBy(xpath="//*[@id='FeedbackPopup']/h1")
	private WebElement feedbackHeading;


	@FindBy(id="heading_para")
	private WebElement feedbackPara;

	@FindBy(css="strong")
	private WebElement phoneNo;

	@FindBy(id="FeedbackContent")
	private WebElement feedbackMessage;

	@FindBy(id="CharLeft")
	private WebElement charLeft;

	@FindBy(id="FeedbackSubmit")
	private WebElement feedbackSubmit;

	@FindBy(id="thanx_heading")
	private WebElement thanxHeading;

	@FindBy(xpath="//div[3]/p")
	private WebElement thanksMessage;


	@FindBy(xpath="//*[@id='FeedbackPopup']/a")
	private WebElement closeFeedBack;

	public boolean verifyPayNowButton(){
		Browser.waitForPageLoad(testConfig,payNow);
		return Element.IsElementDisplayed(testConfig, payNow);
	}

	/**
	 * Click Pay Now and get the Test Response Page
	 */
	public TestResponsePage clickPayNow()
	{	
		if (testConfig.getRunTimeProperty("verifyProcessingPage") != null) {
			clickPayNowButton();
			//	ProcessingPage process=new ProcessingPage(testConfig, false);
			//	process.verifyAmount();
		}
		else
			//Element.click(testConfig, payNow, "Pay Now");
			clickPayNowButton();
		return new TestResponsePage(testConfig);
	}

	/**
	 * Click Pay Now and stay on same Payment Options Page (in case of errors)
	 */
	public void clickPayNowToGetError() {
		if (testConfig.getRunTimeProperty("verifyProcessingPage") != null) {
			clickPayNowButton();
			//		ProcessingPage process=new ProcessingPage(testConfig, false);
			//	process.verifyAmount();
		}
		else
			clickPayNowButton();
	}

	/**
	 * Click Pay Now and get the Bank Redirect Page
	 */
	//@SuppressWarnings("null")
	public BankRedirectPage clickPayNowToGetBankRedirectPage(){
		if (testConfig.getRunTimeProperty("verifyProcessingPage") != null) {
			clickPayNowButton();
			//	ProcessingPage process=new ProcessingPage(testConfig, false);
		}
		else{
			if (testConfig.getRunTimeProperty("popup")!=null){
				clickPayNowButton();
				Alert alert = testConfig.driver.switchTo().alert();
				alert.accept();

			}
			else{
				clickPayNowButton();
			}
		}
		return new BankRedirectPage(testConfig);
	}

	/**
	 * Click Pay Now and get the PayU Money Page
	 */
	/*public PaymentLoginPage clickPayNowToGetPayUMoneyPage()
	{
		Element.click(testConfig, payUsingPayuMoney, "Pay Using PayUMoney");
		return new PaymentLoginPage(testConfig);
	}*/


	/**
	 * Click Pay Now and get the Try Again Page
	 */
	public TryAgainPage clickPayNowToGetTryAgainPage(){
		if (testConfig.getRunTimeProperty("verifyProcessingPage") != null) {
			clickPayNowButton();
			//	ProcessingPage process=new ProcessingPage(testConfig, false);
			//	process.verifyAmount();
		}
		else
			clickPayNowButton();

		return new TryAgainPage(testConfig);
	}

	public ThreeDSecurePage SubmitToGet3DSecurePage()
	{
		clickPayNowButton();
		return new ThreeDSecurePage(testConfig);
	}

	/**
	 * Click Pay Now and get the Error ResponsePage
	 */
	public ErrorResponsePage clickPayNowToGetErrorResponsePage(){
		if (testConfig.getRunTimeProperty("verifyProcessingPage") != null) {
			clickPayNowButton();
			//	ProcessingPage process=new ProcessingPage(testConfig, false);
			//	process.verifyAmount();
		}
		else
			clickPayNowButton();

		return new ErrorResponsePage(testConfig);
	}

	/**
	 * Click Pay Now and get the New ResponsePage
	 */
	public NewResponsePage clickPayNowToGetResponsePage(){
		if (testConfig.getRunTimeProperty("verifyProcessingPage") != null) {
			clickPayNowButton();
			//	ProcessingPage process=new ProcessingPage(testConfig, false);
			//	process.verifyAmount();
		}
		else
			clickPayNowButton();

		return new NewResponsePage(testConfig);
	}

	/**
	 * Click on Go Back to Merchant Name link to cancel the transaction
	 * @param merchant Name
	 * @return Test Response Page
	 */
	public TestResponsePage clickGoBackToMerchantName(String merchantName)
	{
		Helper.compareEquals(testConfig, "Go Back Text", "Go back to " + merchantName , "Go back to "+goBackText());
		WebElement merchantLink = Element.getPageElement(testConfig, How.linkText, merchantName);
		Element.click(testConfig, merchantLink, "Merchant Name");
		return new TestResponsePage(testConfig);
	}

	@FindBy(css=("div[id='processing'] h4"))
	private WebElement weAreProcessingText;

	@FindBy(css=("div[id='processing'] img"))
	private WebElement loadingGif;

	@FindBy(css=("div[id='processing'] p"))
	private WebElement doNotRefreshText;

	@FindBy(css=("p.prepend-1.append-1"))
	private WebElement noteLabelOutBox;

	public String getFooterNoteText() {
		return noteLabelOutBox.getText();
	}

	//Footer section
	@FindBy(css="div.footer-visa")
	private WebElement verifiedByVisaLink;

	public String clickVerifiedByVisaLink()
	{
		Element.click(testConfig, verifiedByVisaLink, "Verified By Visa");
		return Browser.switchToNewWindow(testConfig);
	}

	@FindBy(css="div.footer-mastercard")
	private WebElement masterCardLink;

	public String clickMasterCardLink()
	{
		Element.click(testConfig, masterCardLink, "Master Card Link");
		return Browser.switchToNewWindow(testConfig);
	}

	@FindBy(css="div.footer-verising")
	private WebElement veriSignSecuredLink;

	public String clickVeriSignSecuredLink()
	{
		Element.click(testConfig, veriSignSecuredLink, "VeriSign Secured Link");
		return Browser.switchToNewWindow(testConfig);
	}

	@FindBy(css="div.footer-pci") 
	private WebElement pciLink;

	public String clickPciLink()
	{
		Element.click(testConfig, pciLink, "PCI Link");
		return Browser.switchToNewWindow(testConfig);
	}

	@FindBy(css="div.footer-amex-safety") 
	private WebElement amexsafety;

	public Boolean IsAmexSafetyLinkPresent()
	{
		return amexsafety.isDisplayed();
	}

	@FindBy(css="div.footer-amex") 
	private WebElement amex;

	public Boolean IsAmexLinkpresent()
	{
		return amex.isDisplayed();
	}

	public void verifyTransactionProcessing()
	{
		String expectedProcessingNote = "We are processing your transaction...";
		Helper.compareEquals(testConfig, "Processing Note", expectedProcessingNote, weAreProcessingText.getText());

		String expectedLoadingGif = "/images/ajax-loader.gif";
		Helper.compareContains(testConfig, "Processing Note", expectedLoadingGif, weAreProcessingText.getAttribute("src"));

		String expectedDoNotRefreshNote = "Please be patient. This process might take some time, please do not hit refresh or browser back button or close this window.";
		Helper.compareEquals(testConfig, "Do Not Refresh Note", expectedDoNotRefreshNote, doNotRefreshText.getText());	
	}

	public void VerifyBillingTabAbsent() 
	{
		Element.verifyElementNotPresent(testConfig, orderDetails, "Order Details");
		Element.verifyElementNotPresent(testConfig, showDetails, "Show and Hide Details");
		Element.verifyElementNotPresent(testConfig, billingLabel, "Billing Label");
		Element.verifyElementNotPresent(testConfig, billingName, "Billing Name");
		Element.verifyElementNotPresent(testConfig, billingAddress, "Billing Address");
	}

	public void verifyCssPresent(){
		Browser.wait(testConfig, 2);
		WebElement amountDiv = Element.getPageElement(testConfig, How.xPath, "//*[@id=\"order-summary\"]");
		Helper.compareContains(testConfig, "comapring class present or not", "logo",amountDiv.getAttribute("class"));

	}

	public void verifystleChanged(String Value){
		Browser.wait(testConfig, 2);
		WebElement amountDiv = Element.getPageElement(testConfig, How.xPath, "//*[@id=\"amountdiv\"]");
		Helper.compareContains(testConfig, "comapring class present or not",amountDiv.getCssValue("font-family"),Value);

	}

	public void verifyStyleNotchanged(String Value){
		Browser.wait(testConfig, 2);
		WebElement amountDiv = Element.getPageElement(testConfig, How.xPath, "//*[@id=\"amountdiv\"]");
		if(Value.contains(amountDiv.getAttribute("class"))) {
			testConfig.logFail("Class present","not present", "present");
		}else {
			testConfig.logPass("class not present,test pass");
		}	
	}

	public void verifyCssNotPresent(){
		Browser.wait(testConfig, 2);
		WebElement amountDiv = Element.getPageElement(testConfig, How.xPath, "//*[@id=\"order-summary\"]");
		String test = amountDiv.getAttribute("class");
		if(test.contains("logo")) {
			testConfig.logFail("Class present","not present", "present");
		}else {
			testConfig.logPass("class not present,test pass");
		}


	}

	public String verifyDropDownValue(){
		Browser.wait(testConfig, 2);
		WebElement dropDown = Element.getPageElement(testConfig, How.id, "debit_card_select");
		WebElement test = Element.getFirstSelectedOption(testConfig, dropDown, "debit_card_select");
		return test.getText();
	}

	public String verifyErrorMessage(){
		Browser.wait(testConfig, 2);
		WebElement errorMessageLabel = Element.getPageElement(testConfig, How.css, "div#payment-tabs div#debit div#payment-box div.chargedDiv");	
		return errorMessageLabel.getText();
	}

	public String verifyErrorMessageChoosePaymentMethod(){
		Browser.wait(testConfig, 2);
		WebElement errorMessageLabel = Element.getPageElement(testConfig, How.css, "form#paymentForm h3.prepend-top");	
		return errorMessageLabel.getText();
	}

	public String verifyDropDownValueNBTab(){
		Browser.wait(testConfig, 2);
		WebElement dropDown = Element.getPageElement(testConfig, How.id, "netbanking_select");
		WebElement test = Element.getFirstSelectedOption(testConfig, dropDown, "netbanking_select");
		return test.getText();
	}

	public String verifyErrorMessageNBTab(){
		Browser.wait(testConfig, 2);
		WebElement errorMessageLabel = Element.getPageElement(testConfig, How.css, "div#payment-tabs div#netbanking div#payment-box div.chargedDiv");	
		return errorMessageLabel.getText();
	}

	public String verifyDropDownValueEMITab(){
		Browser.wait(testConfig, 2);
		WebElement dropDown = Element.getPageElement(testConfig, How.id, "bankEmiList");
		WebElement test = Element.getFirstSelectedOption(testConfig, dropDown, "bankEmiList");
		return test.getText();
	}

	public String verifyDropDownValueCashCardTab(){
		Browser.wait(testConfig, 2);
		WebElement dropDown = Element.getPageElement(testConfig, How.id, "cash_card_select");
		WebElement test = Element.getFirstSelectedOption(testConfig, dropDown, "cash_card_select");
		return test.getText();
	}

	public String verifyAllDropDownValueEMITab(int index){
		Browser.wait(testConfig, 2);
		WebElement dropDown = Element.getPageElement(testConfig, How.id, "bankEmiList");
		List<String> test = Element.getAllOptionsInSelect(testConfig, dropDown);
		return test.get(index).toString();
	}

	/**
	 * Do Seamless EMI test transaction using the given details (when on Transaction Page)
	 * @param transactionRow row number of 'TransactionDetails' sheet
	 * @param paymentTypeRow row number of 'PaymentType' sheet
	 * @param cardDetailsRow row number of 'CardDetails' sheet
	 * @param expResponse expected response page, one of the emum values
	 * @return Expected response page
	 */

	public Object DoEMITransaction(int transactionRow, int paymentTypeRow, int cardDetailsRow, ExpectedResponsePage expResponse)

	{

		Object returnPage = null;

		EMITab emiTab = new EMITab(testConfig);
		emiTab.FillCardDetails(cardDetailsRow);

		switch(expResponse)
		{

		case TestResponsePage:
			returnPage = clickPayNow();
			break;
		case BankRedirectPage:
			returnPage = clickPayNowToGetBankRedirectPage();
			break;
		default:
			returnPage = null;
			break;
		}
		return returnPage;

	}

	//verify cvv number and expiry date are not present in case of SMAE seamless
	@FindBy(id="dcvv_number")
	private WebElement CVVTextbox;
	@FindBy(id="dexpiry_date_month")
	private WebElement expiryMonth;
	@FindBy(partialLinkText="Click here")
	private WebElement clickHereLink;

	public void VerifyOptionlFieldsFunctionalityForSMAE() 
	{
		Element.verifyElementNotPresent(testConfig, CVVTextbox, "CVV field");
		Element.verifyElementNotPresent(testConfig, expiryMonth, "expiryMonth");

		Element.click(testConfig, clickHereLink, "click Here link to display CVV and Expiry field");

		Element.verifyElementPresent(testConfig, CVVTextbox, "CVV field");
		Element.verifyElementPresent(testConfig, expiryMonth, "expiryMonth");	
	}

	//verify COD page is displayed
	public String VerifyCODTabOpen() 
	{
		Browser.wait(testConfig, 2);
		WebElement messageCODPage = Element.getPageElement(testConfig, How.css, "div.shipping_formDiv center h2");
		return messageCODPage.getText();
	}

	public String verifyErrorMessageCODTab(){
		Browser.wait(testConfig, 2);
		WebElement errorMessageLabel = Element.getPageElement(testConfig, How.css, "div#allDetails div.chargedDiv");	
		return errorMessageLabel.getText();
	}

	public String verifySavedDetails(String fieldName){
		WebElement field = Element.getPageElement(testConfig, How.name, fieldName);
		switch(fieldName)
		{
		case "shipping_state":			
			return Element.getFirstSelectedOption(testConfig, field, "Dropdown value").getText();
		default: return field.getAttribute("value");
		}

	}

	/**
	 * Click Pay Now and get the Test Response Page
	 */
	public TestResponsePage clickNext()
	{
		Element.click(testConfig, nextButton, "Next");
		return new TestResponsePage(testConfig);
	}

	/**
	 * Click Pay Now to get OfferRetry Page
	 */
	public OfferRetryPage clickPayNowToGetOfferRetry() {
		clickPayNowButton();
		return new OfferRetryPage(testConfig);
	}
	//
	@FindBy(css=".error")
	private WebElement storeCardErrorLabel;
	//Only alphanumeric,spaces,* and _ allowed.
	public void verifyStoreCardErrorLabel(String message) throws InterruptedException{
		WebElement storeCardErrorLabel =Element.getPageElement(testConfig, How.css, "label.error");
		/*WebElement storeCardInputName=Element.getPageElement(testConfig, How.css, "#ccard_name");
				String text =storeCardInputName.getText();
				for (int i=0;i<text.length();i++){
					String str="";
					System.out.println(text.charAt(i));
					str=str+text.charAt(i);
					Element.enterData(testConfig, storeCardInputName,str, "Junk Character"+text.charAt(i));
					storeCardInputName.sendKeys(Keys.TAB);
				}*/
		System.out.println(storeCardErrorLabel.getText());
		Helper.compareContains(testConfig, "Error Message", message, storeCardErrorLabel.getText());
	}	

	@FindBy(xpath="//div[@id='failure_note']/strong")
	private WebElement cvvMissingtext;
	public String getcvvMissingText(){
		return cvvMissingtext.getText();
	}

	public void verifyDCTabPresent(){

		WebElement tabText=Element.getPageElement(testConfig,How.id, "creditlink");
		WebElement tabText1=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'EMI')]");
		WebElement tabText2=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Net Banking')]");
		WebElement tabText3=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Cash Card')]");
		WebElement tabText4=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'COD')]");

		if(Element.IsElementDisplayed(testConfig,tabText) || Element.IsElementDisplayed(testConfig,tabText1) || Element.IsElementDisplayed(testConfig,tabText2) || Element.IsElementDisplayed(testConfig,tabText3) || Element.IsElementDisplayed(testConfig,tabText4))
		{
			testConfig.logFail("Invalid tab present");
		}
		else
		{
			testConfig.logPass("Invalid tab not present");
		}
	}


	public void verifyEMITabPresent(){

		WebElement tabText=Element.getPageElement(testConfig,How.id, "creditlink");
		WebElement tabText1=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Debit Card')]");
		WebElement tabText2=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Net Banking')]");
		WebElement tabText3=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Cash Card')]");
		WebElement tabText4=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'COD')]");

		if(Element.IsElementDisplayed(testConfig,tabText) || Element.IsElementDisplayed(testConfig,tabText1) || Element.IsElementDisplayed(testConfig,tabText2) || Element.IsElementDisplayed(testConfig,tabText3) || Element.IsElementDisplayed(testConfig,tabText4))
		{
			testConfig.logFail("Invalid tab present");
		}
		else
		{
			testConfig.logPass("Invalid tab not present");
		}
	}


	public void verifyNBTabPresent(){

		WebElement tabText=Element.getPageElement(testConfig,How.id, "creditlink");
		WebElement tabText1=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'EMI')]");
		WebElement tabText2=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Debit Card')]");
		WebElement tabText3=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Cash Card')]");
		WebElement tabText4=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'COD')]");
		Element.verifyElementPresent(testConfig, tabText, "Credit Card");
		Element.verifyElementPresent(testConfig, tabText2, "Debit Card");

		if(Element.IsElementDisplayed(testConfig,tabText) || Element.IsElementDisplayed(testConfig,tabText1) || Element.IsElementDisplayed(testConfig,tabText2) || Element.IsElementDisplayed(testConfig,tabText3) || Element.IsElementDisplayed(testConfig,tabText4))
		{
			testConfig.logFail("Invalid tab present");
		}
		else
		{
			testConfig.logPass("Invalid tab not present");
		}
	}

	public void verifyNBDCPresent(){

		WebElement tabText=Element.getPageElement(testConfig,How.id, "creditlink");
		WebElement tabText1=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'EMI')]");
		WebElement tabText3=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Cash Card')]");
		WebElement tabText4=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'COD')]");

		if(Element.IsElementDisplayed(testConfig,tabText) || Element.IsElementDisplayed(testConfig,tabText1) || Element.IsElementDisplayed(testConfig,tabText3) || Element.IsElementDisplayed(testConfig,tabText4))
		{
			testConfig.logFail("Invalid tab present");
		}
		else
		{
			testConfig.logPass("Invalid tab not present");
		}
	}

	public void verifyCCTabPresent(){

		WebElement tabText5=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Net Banking')]");
		WebElement tabText1=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'EMI')]");
		WebElement tabText2=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Debit Card')]");
		WebElement tabText3=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Cash Card')]");
		WebElement tabText4=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'COD')]");

		if(Element.IsElementDisplayed(testConfig,tabText5) || Element.IsElementDisplayed(testConfig,tabText1) || Element.IsElementDisplayed(testConfig,tabText2) || Element.IsElementDisplayed(testConfig,tabText3) || Element.IsElementDisplayed(testConfig,tabText4))
		{
			testConfig.logFail("Invalid tab present");
		}
		else
		{
			testConfig.logPass("Invalid tab not present");
		}
	}

	public void verifyCashcardTabPresent(){

		WebElement tabText=Element.getPageElement(testConfig,How.id, "creditlink");
		WebElement tabText1=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Debit Card')]");
		WebElement tabText2=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Net Banking')]");
		WebElement tabText3=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'EMI')]");
		WebElement tabText4=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'COD')]");

		if(Element.IsElementDisplayed(testConfig,tabText) || Element.IsElementDisplayed(testConfig,tabText1) || Element.IsElementDisplayed(testConfig,tabText2) || Element.IsElementDisplayed(testConfig,tabText3) || Element.IsElementDisplayed(testConfig,tabText4))
		{
			testConfig.logFail("Invalid tab present");
		}
		else
		{
			testConfig.logPass("Invalid tab not present");
		}
	}

	public void verifyCODTabPresent(){

		WebElement tabText=Element.getPageElement(testConfig,How.id, "creditlink");
		WebElement tabText1=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Debit Card')]");
		WebElement tabText2=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Net Banking')]");
		WebElement tabText3=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'EMI')]");
		WebElement tabText4=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Cash Card')]");

		if(Element.IsElementDisplayed(testConfig,tabText) || Element.IsElementDisplayed(testConfig,tabText1) || Element.IsElementDisplayed(testConfig,tabText2) || Element.IsElementDisplayed(testConfig,tabText3) || Element.IsElementDisplayed(testConfig,tabText4))
		{
			testConfig.logFail("Invalid tab present");
		}
		else
		{
			testConfig.logPass("Invalid tab not present");
		}
	}

	public void verifyDC_option()
	{

		WebElement tabText5=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Net Banking')]");
		WebElement tabText1=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'EMI')]");
		WebElement tabText3=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'Cash Card')]");
		WebElement tabText4=Element.getPageElement(testConfig,How.xPath, "//a[contains(text(),'COD')]");

		if(Element.IsElementDisplayed(testConfig,tabText5) || Element.IsElementDisplayed(testConfig,tabText1) || Element.IsElementDisplayed(testConfig,tabText3) || Element.IsElementDisplayed(testConfig,tabText4))
		{
			testConfig.logFail("Invalid tab present");
		}
		else
		{
			testConfig.logPass("Invalid tab not present");
		}
		WebElement dropDown = Element.getPageElement(testConfig, How.id, "debit_card_select");
		List<String> test = Element.getAllOptionsInSelect(testConfig, dropDown);

		Helper.compareEquals(testConfig, "drop down value count", 2, test.size());
		testConfig.logPass("enforced payment option present");

		System.out.println(test);


	}
	public void verifymultipleDC_option()
	{
		WebElement dropDown = Element.getPageElement(testConfig, How.id, "debit_card_select");
		List<String> test = Element.getAllOptionsInSelect(testConfig, dropDown);

		Helper.compareEquals(testConfig, "drop down value count", 4, test.size());
		testConfig.logPass("enforced payment option present");

		System.out.println(test);


	}

	public void verifymultipleNB_option()
	{

		WebElement dropDown = Element.getPageElement(testConfig, How.id, "netbanking_select");
		List<String> test = Element.getAllOptionsInSelect(testConfig, dropDown);

		Helper.compareEquals(testConfig, "drop down value count", 5, test.size());
		testConfig.logPass("enforced payment option present");

		System.out.println(test);


	}

	public void verifymultipleCash_option()
	{

		WebElement dropDown = Element.getPageElement(testConfig, How.id, "cash_card_select");
		List<String> test = Element.getAllOptionsInSelect(testConfig, dropDown);

		Helper.compareEquals(testConfig, "drop down value count", 3, test.size());
		testConfig.logPass("enforced payment option present");

		System.out.println(test);


	}
	public void verifymultipleEMI_option()
	{

		WebElement dropDown = Element.getPageElement(testConfig, How.id, "bankEmiList");
		List<String> test = Element.getAllOptionsInSelect(testConfig, dropDown);

		Helper.compareEquals(testConfig, "drop down value count", 1, test.size());
		testConfig.logPass("enforced payment option present");

		System.out.println(test);


	}

	public ProcessingPage clickPayNowToGetProcessingPage() 
	{	
		Cookie cookie = testConfig.driver.manage().getCookieNamed("abStat-new_threed_popup");
		String cookieValue = cookie.getValue();

		clickPayNowButton();

		if(cookieValue.equals("new_threed_popup_1")) //3d popup will not open
		{
			return new ProcessingPage(testConfig, false);
		}
		else  //3d popup will open - new_threed_popup_0
		{
			return new ProcessingPage(testConfig, true);
		}
	}



	/**This function will identify the Pay Now element as per the tab representation and then click it
	 * @param testConfig
	 *  
	 */
	public void clickPayNowButton()
	{ 	
		Element.click(testConfig, getPayNow(), "Pay Now");
	}

	public WebElement getPayNow() 
	{	
		List<WebElement> getListOfPayNow=testConfig.driver.findElements(By.id("pay_button"));
		for (WebElement payButton:getListOfPayNow)
		{
			if(!payButton.isEnabled())
				continue;
			else{
				payNow =  payButton;
				break;
			}
		}
		return payNow;	
	}

	public void VerifyRedirectedURL_PaymentOptionPage()
	{   
		String publicKey=testConfig.getRunTimeProperty("publickey");
		if(publicKey!=null)
		{
			String expectedUrl = "https://secure.payu.in/_payment_options";
			Browser.verifyIntermediateURL(testConfig, expectedUrl);
		}
	}

	/**
	 * Verifies if tabs are present or absent on page
	 * @param tabsToBeVerified - name of the tabs that have to be verified for presence
	 * @param tabPresent- flag to check for presence/absence
	 */
	public void verifyPaymentTabsOnPage(List<String> tabsToBeVerified,boolean tabPresent) {
		for (String tabname : tabsToBeVerified) {
			WebElement tabOnPage = null;
			switch (tabname) {
			case "Credit Card" :
				tabOnPage = CCTab;
				break;
			case "Debit Card" :
				tabOnPage = DCTab;
				break;

			case "PayuMoney" :
				tabOnPage = PayUMoneyTab;
				break;

			case "NetBanking" :
				tabOnPage = NBTab;
				break;

			case "EMI" :
				tabOnPage = EMITab;
				break;
			case "Cash Card" :
				tabOnPage = CashCardTab;
				break;
			}
			if(tabPresent)
				Element.verifyElementPresent(testConfig, tabOnPage, tabname);
			else
				Element.verifyElementNotPresent(testConfig, tabOnPage, tabname);
		}

	}


	public void writeFeedBackMessage(String message)
	{

		Element.enterData(testConfig, feedbackMessage, message, "Enter feedback message");


	}

	public int feedbackMessageLength()
	{

		testConfig.logComment("Length entered in feedback message is "+feedbackMessage.getAttribute("value").length());

		return feedbackMessage.getAttribute("value").length();
	}

	public void submitFeedBackForm()
	{
		Element.click(testConfig, feedbackSubmit, "Click on Submit button");	
	}

	public String cutMessage()
	{
		String message=feedbackMessage.getAttribute("value");
		Element.click(testConfig, feedbackMessage, "feedback clicked");


		feedbackMessage.sendKeys(Keys.CONTROL + "a");

		feedbackMessage.sendKeys(Keys.CONTROL + "x");
		Helper.compareEquals(testConfig, "Cut Operation removed message ", 0, feedbackMessageLength());

		return message;
	}



	public void pasteMessage(String message)
	{
		Element.click(testConfig, feedbackMessage, "feedback clicked");

		feedbackMessage.sendKeys(Keys.CONTROL + "v");
		Helper.compareEquals(testConfig, "Paste Operation pasted message", message, feedbackMessage.getAttribute("value"));

	}

	public void verifyFeedbackForm()
	{
		Browser.wait(testConfig, 2);
		Element.verifyElementPresent(testConfig, thanxHeading, "Thanks Heading visible after submitting");
		Element.verifyElementPresent(testConfig, thanksMessage, "Thanks Message visible after submitting");



	}

	public void closeFeedBackForm()
	{
		Browser.wait(testConfig, 2);
		Element.click(testConfig, closeFeedBack, "Close the feedback form");
	}

	public void verifyPhoneNumber(String phoneNumber)
	{
		Browser.wait(testConfig, 2);

		Helper.compareEquals(testConfig,"feedback form phone No.", phoneNo.getText(), phoneNumber);
	}


	public String getPhoneNumber()
	{

		Map customerconfig = DataBase.executeSelectQuery(testConfig, 107, 1);

		return (String) customerconfig.get("value");
	}
	public void verifyHeadingFeedback()
	{
		Browser.wait(testConfig, 2);
		Helper.compareEquals(testConfig,"feedback heading", feedbackHeading.getText(), "Feedback");
		//Element.verifyElementPresent(testConfig, feedbackHeading,"Heading in the feedback form ");

	}

	public void verifyFeedbackMessage()
	{

		Browser.wait(testConfig, 2);
		Helper.compareEquals(testConfig,"feedback paragraph", feedbackPara.getText(), "Facing a problem in making payment? Leave us a message or give us a call on "+getPhoneNumber()+"");

	}

	public void verifyFeedbackSubmit()
	{
		Browser.wait(testConfig, 2);
		Element.verifyElementPresent(testConfig, feedbackSubmit, " Feedback Submit in the feedback form");
	}

	public void verifyFeedbackClose()
	{
		Browser.wait(testConfig, 2);
		Element.verifyElementPresent(testConfig, closeFeedBack, " Close button in the feedback form");


	}


	@FindBy(css="#invalid_card_error")
	private WebElement InvalidCardError;
	/**Returns Invalid card details error displayed on page
	 * @return
	 */
	public String getInvalidCardError() {
		return InvalidCardError.getText();
	}

}
