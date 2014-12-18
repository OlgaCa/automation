package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class DCTab {
	private Config testConfig;
	DCTab(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);		
		Browser.wait(testConfig, 1); 
		if (Element.IsElementDisplayed(testConfig, debitCardDropdown)); 
			else if (Element.IsElementDisplayed(testConfig, RedirectNote)); 
			else if (Element.IsElementDisplayed(testConfig, debitCardType)); 
			else if (Element.IsElementDisplayed(testConfig, RedirectNote)); 
	}

	@FindBy(css="a[href='#debit']")
	private WebElement DCTabtxt;
	public String getDCTabText() {
		return DCTabtxt.getText();
	}	

	@FindBy(id="debit_card_select")
	private WebElement debitCardType;

	@FindBy(css="div[class='payuCardDropDown select']")
	private WebElement storeddebitCardType;

	@FindBy(css="div[id='disableCvvExpDivSBI']>a")
	private WebElement disabledCvvExpDivSBI;
	public void DisableCvvExpSBI()
	{
		Element.click(testConfig, disabledCvvExpDivSBI, "Disable SBI CVV Expiry fields");	
	}

	@FindBy(css="div[id='enableCvvExpDivSBI']>a")
	private WebElement enabledCvvExpDivSBI;
	public void EnableCvvExpSBI()
	{
		Element.click(testConfig, enabledCvvExpDivSBI, "Enable SBI CVV Expiry fields");	
	}

	@FindBy(css="div[id='disableCvvExpDiv']>a")
	private WebElement disabledCvvExpDiv;
	public void DisableCvvExp()
	{
		Element.click(testConfig, disabledCvvExpDiv, "Disable CVV Expiry fields");	
	}

	@FindBy(css="div[id='enableCvvExpDiv']>a")
	private WebElement enabledCvvExpDiv;
	public void EnableCvvExp()
	{
		Element.click(testConfig, enabledCvvExpDiv, "Enable CVV Expiry fields");	
	}

	/**
	 * Selects the given Debit card
	 * @param debitCardName Name of the debit card
	 */
	public void SelectDebitCard(String debitCardName) 
	{
		if (debitCardType.isDisplayed())
			Element.selectVisibleText(testConfig, debitCardType, debitCardName, "Debit card -" + debitCardName);
		else{
		 String bankcode= testConfig.getRunTimeProperty("bankcode");
				switch (bankcode.trim()) {
				case "MAST":
					selectMasterDebitCard();
					break;
				case "VISA":
					selectVisaDebitCard();
					break;
				case "MAES":
					selectMaestroDebitCard();
					break;
				default:
					selectStoredCard(debitCardName);
					break;
				}
		}
		
		Browser.wait(testConfig, 1);
	}

	//Label Text Fields
	@FindBy(xpath="//div[2]/div/fieldset/p/label")
	private WebElement debitCardTypeLabel;
	public String getDebitCardTypeLabel() {
		return debitCardTypeLabel.getText();
	}

	@FindBy(xpath="//div[@id='payment-box']/fieldset/fieldset/fieldset/div/label")
	private WebElement debitCardNameLabel;
	public String getCardNameLabel() {
		return debitCardNameLabel.getText();
	}

	@FindBy(xpath="//div[@id='payment-box']/fieldset/fieldset/fieldset/div[2]/label")
	private WebElement debitCardNumberLabel;
	public String getCardNumberLabel() {
		return debitCardNumberLabel.getText();
	}

	@FindBy(xpath="//div[@id='payment-box']/fieldset/fieldset/fieldset/div[3]/div/label")
	private WebElement debitCvvNumberLabel;
	public String getCVVNumberLabel() {
		return debitCvvNumberLabel.getText();
	}

	@FindBy(xpath="//div[@id='payment-box']/fieldset/fieldset/fieldset/div[3]/p/label")
	private WebElement debitExpiryDateLabel;
	public String getExpiryDateLabel() {
		return debitExpiryDateLabel.getText();
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

	@FindBy(id="dc_msg_disp")
	private WebElement RedirectNote;
	public String getRedirectText() {
		return RedirectNote.getText();
	}
	
	public WebElement getRedirectElement() {
		return RedirectNote;
	}

	@FindBy(css="#debit p[class*='p-endnote prepend-top prepend']")
	private WebElement NoteLabelInBox;
	public String getNoteText() {
		return NoteLabelInBox.getText();
	}

	//Error messages
	@FindBy(css="#dname_on_card+label")
	private WebElement debitCardNameErrorMessage;
	public String getCardNameErrorMessage() {
		if(!Element.IsElementDisplayed(testConfig, debitCardNameErrorMessage)) 
			return "";
		return debitCardNameErrorMessage.getText();
	}

	@FindBy(css="#dcard_number+label")
	private WebElement debitCardNumberErrorMessage;
	public String getCardNumberErrorMessage() {
		if(!Element.IsElementDisplayed(testConfig, debitCardNumberErrorMessage)) 
			return "";
		return debitCardNumberErrorMessage.getText();
	}

	@FindBy(css="#dcvv_number+label")
	private WebElement debitCvvNumberErrorMessage;
	public String getCvvNumberErrorMessage() {
		if(!Element.IsElementDisplayed(testConfig, debitCvvNumberErrorMessage)) 
			return "";
		return debitCvvNumberErrorMessage.getText();
	}

	@FindBy(css="#dexpiry_date_month+label")
	private WebElement debitExpiryDateErrorMessage;
	public String getExpiryDateErrorMessage() {
		if(!Element.IsElementDisplayed(testConfig, debitExpiryDateErrorMessage)) 
			return "";
		return debitExpiryDateErrorMessage.getText();
	}

	@FindBy(css="span.cvv_help")
	private WebElement whatIsCVV;
	public void clickWhatIsCVVLink() {
		Element.click(testConfig, whatIsCVV, "What Is CVV Link");
	}

	@FindBy(css="span.cvv_help")
	private WebElement cardImage;
	public boolean isCardImageAppearing() {
		return cardImage.isEnabled();
	}

	/**
	 * Verifies that given debit card type is not present in the list
	 * @param dcOption
	 */
	public void verifyDisabledPaymentTypes(String dcOption, String gateway)
	{
		Element.verifySelectVisibleTextNotPresent(testConfig, debitCardType, dcOption, "Debit Card Type for " + gateway);
	}

	public void verifySBIMaestroLinks()
	{
		String haveCVVExpiryText = "Click here if you do have a CVV number and expiry date on card";
		Helper.compareEquals(testConfig, "'SBI Maestro' have CVV and Expiry text", haveCVVExpiryText ,enabledCvvExpDivSBI.getText());
		Element.verifyElementNotPresent(testConfig, Cvv, "CVV field");
		Element.verifyElementNotPresent(testConfig, ExpiryMonth, "Expiry Month field");
		Element.verifyElementNotPresent(testConfig, ExpiryYear, "Expiry Year field");
		EnableCvvExpSBI();

		String dontHaveCVVExpiryText = "Undo . I don't have a CVV number and expiry date on card";
		Helper.compareEquals(testConfig, "'SBI Maestro' don't have CVV and Expiry text", dontHaveCVVExpiryText ,disabledCvvExpDivSBI.getText());
		Element.verifyElementPresent(testConfig, Cvv, "CVV field");
		Element.verifyElementPresent(testConfig, ExpiryMonth, "Expiry Month field");
		Element.verifyElementPresent(testConfig, ExpiryYear, "Expiry Year field");
		DisableCvvExpSBI();

		Helper.compareEquals(testConfig, "'SBI Maestro' have CVV and Expiry text", haveCVVExpiryText ,enabledCvvExpDivSBI.getText());
		Element.verifyElementNotPresent(testConfig, Cvv, "CVV field");
		Element.verifyElementNotPresent(testConfig, ExpiryMonth, "Expiry Month field");
		Element.verifyElementNotPresent(testConfig, ExpiryYear, "Expiry Year field");
	}

	public void verifyOtherMaestroLinks()
	{
		String dontHaveCVVExpiryText = "Click here . I don't have a CVV number and expiry date on card";
		Helper.compareEquals(testConfig, "'Other Maestro' don't have CVV and Expiry text", dontHaveCVVExpiryText ,disabledCvvExpDiv.getText());
		Element.verifyElementPresent(testConfig, Cvv, "CVV field");
		Element.verifyElementPresent(testConfig, ExpiryMonth, "Expiry Month field");
		Element.verifyElementPresent(testConfig, ExpiryYear, "Expiry Year field");
		DisableCvvExp();

		String haveCVVExpiryText = "Undo if you do have a CVV number and expiry date on card";
		Helper.compareEquals(testConfig, "'Other Maestro' have CVV and Expiry text", haveCVVExpiryText ,enabledCvvExpDiv.getText());
		Element.verifyElementNotPresent(testConfig, Cvv, "CVV field");
		Element.verifyElementNotPresent(testConfig, ExpiryMonth, "Expiry Month field");
		Element.verifyElementNotPresent(testConfig, ExpiryYear, "Expiry Year field");
		EnableCvvExp();

		Helper.compareEquals(testConfig, "'Other Maestro' don't have CVV and Expiry text", dontHaveCVVExpiryText ,disabledCvvExpDiv.getText());
		Element.verifyElementPresent(testConfig, Cvv, "CVV field");
		Element.verifyElementPresent(testConfig, ExpiryMonth, "Expiry Month field");
		Element.verifyElementPresent(testConfig, ExpiryYear, "Expiry Year field");
	}


	@FindBy(id = "storeDebitCardLink")
	private WebElement storeCardLink;

	public void clickStoreCardLink() {
		Element.click(testConfig, storeCardLink, "Save Card Link");
	}

	@FindBy(id = "storeDebitCard")
	private WebElement storeCardCheckBox;

	public void clickStoreCardCheckBox() {
		Element.click(testConfig, storeCardCheckBox, "Save Card CheckBox");
	}

	@FindBy(id = "manageDebitCardLink")
	private WebElement manageThisCardLink;

	public void clickmanageThisCardLink() {
		Element.click(testConfig, manageThisCardLink, "Manage Card Link");
		Browser.waitForPageLoad(testConfig, removeCard);
	}

	@FindBy(css = "#manageCardRemove>span")
	private WebElement removeCard;

	public void clickRemoveCard() {
		clickmanageThisCardLink();
		Element.click(testConfig, removeCard, "Remove StoreCard Link");
		if (!Element.IsElementDisplayed(testConfig, storeCardCheckBox)) {
			Browser.wait(testConfig, 1);
		}
		return;
	}

	@FindBy(css = "#manageCardCancel>span")
	private WebElement cancelRemovingCard;

	public void clickCancelRemovingCard() {
		clickmanageThisCardLink();
		Element.click(testConfig, cancelRemovingCard, "Cancel Link");
	}

	@FindBy(id = "dcard_name")
	private WebElement storeCardName;

	@FindBy(id = "drop_image_2")
	private WebElement debitCardDropdown;
	public void clickDebitCardDropdown() {
		Element.click(testConfig, debitCardDropdown,
				"Click Debit Card Dropdown");
		Browser.wait(testConfig, 1);
		
	}

	@FindBy (css="a.payment_title[rel*='debitcard_MAST']")
	private WebElement masterCardOption;
	public void selectMasterDebitCard() {
		clickDebitCardDropdown();
		Element.click(testConfig, masterCardOption, "Select Master Debit Card");
	}

	@FindBy (css="a.payment_title[rel*='debitcard_VISA']")
	private WebElement visaCardOption;
	public void selectVisaDebitCard() {
		clickDebitCardDropdown();
		Element.click(testConfig, visaCardOption, "Select Visa Card");
	}

	@FindBy (css="a.payment_title[rel*='debitcard_MAES']")
	private WebElement maesCardOption;
	public void selectMaestroDebitCard() {
		clickDebitCardDropdown();
		Element.click(testConfig, maesCardOption, "Select Maestro Card");
	}

	public WebElement selectStoredCard(String Linktext) {
		clickDebitCardDropdown();
		WebElement storedCard = null;
		if (!Element.IsElementDisplayed(testConfig, testConfig.driver.findElement(By.linkText(Linktext))))
			testConfig.logFail("Store Card:" + Linktext + " is not available");
		else {
			storedCard = testConfig.driver.findElement(By.linkText(Linktext));
			Element.click(testConfig, storedCard, "Select " + Linktext);
		}
		return storedCard;
	}


	public TestDataReader FillStoreCardDetails(int cardRow) {
		TestDataReader data = new TestDataReader(testConfig, "StoreCard");

		String value = "";
		String storecardOption = data.GetData(cardRow, "StoreCardOption");

		if (Element.IsElementDisplayed(testConfig, storeCardLink)) {

			switch (storecardOption) {
			case "storecardcheckbox":
				clickStoreCardCheckBox();
				break;
			case "storecardlink":
				clickStoreCardLink();
				break;
			default: // do nothing, it will redirect to bank
				testConfig
						.logFail("Store Card Option not specified as storecardcheckbox or storecardlink");
				break;
			}
			String rnd;
			value = data.GetData(cardRow, "SavedCardName");
			if (testConfig.getRunTimeProperty("UseStoredDetails").equals("2"))
			{
				rnd= Helper.generateRandomAlphaNumericString(5);
				value = data.GetData(cardRow, "SavedCardName");
				Element.enterData(testConfig, storeCardName, value+rnd, "Store CardName");
				testConfig.putRunTimeProperty("StoreCardName", value+rnd);
			}
			else
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
		String SavedCardName; 
		if(testConfig.getRunTimeProperty("StoreCardName")!=null)
			SavedCardName=testConfig.getRunTimeProperty("StoreCardName");
		else
		SavedCardName = data.GetData(storeCardRow, "SavedCardName");
		
		if (selectStoredCard(SavedCardName) != null) {
			value = data.GetData(storeCardRow, "CVV");
			Element.enterData(testConfig, Cvv, value, "CVV Number");
		} else {
			testConfig
					.logFail("StoreCard functionality expected but it doesnot Exist");
			Assert.assertTrue(testConfig.getTestResult());
		}
		return data;
	}

	public void removeDCStoredCard() {


		while (Element.IsElementDisplayed(testConfig, manageThisCardLink)) {
			if (Element.IsElementDisplayed(testConfig, storeCardCheckBox)) {
				testConfig.logComment("No Saved Card Exists");
				break;
			}
			clickRemoveCard();
		}

		if (Element.IsElementDisplayed(testConfig, storeCardCheckBox)) {
			testConfig.logComment("No Saved Card Exists");
			return;
		}
	}
	
	@FindBy(css = "div[id='debit'] a:contains('Learn more')") 
	private WebElement learnMoreLink; 
	 
	public void clickLearnMoreLink() { 
		Element.click(testConfig, learnMoreLink, "Click Learn More Link"); 
	} 
	 
	@FindBy(css = "a:contains('contact@payu.in')") 
	private WebElement payUContact; 
	 
	@FindBy(css = "#learn-more_close-btn > span") 
	private WebElement okOnLearnMorePopUp; 
	 
	public void clickOKtoCloseLearnMorePopUp() { 
		Element.verifyElementPresent(testConfig, payUContact, 
				"PayU Contact mentioned in pop up"); 
		Element.click(testConfig, okOnLearnMorePopUp, 
				"Ok to Close Learn More pop up"); 
	}
	public boolean verifyStoreCardDoesNotExists() { 
		if ((Element.IsElementDisplayed(testConfig, storeCardLink)) 
				&& (Element.IsElementDisplayed(testConfig, storeCardCheckBox))) 
			return false; 
		else 
			return true; 
 	}

	public void verifyStoreCardFeatureNotDisplayedIn_DC(){
		Element.verifyElementNotPresent(testConfig, manageThisCardLink, "Manage Link for Store Card");
		Element.verifyElementNotPresent(testConfig, storeCardCheckBox, "Store Card Check Box");
		Element.verifyElementNotPresent(testConfig, storeCardLink, "Save Card Link");
	}
	public void verifyStoreCardDisplayed(){ 	
		if ((Element.IsElementDisplayed(testConfig,storeCardLink))&& (Element.IsElementDisplayed(testConfig,storeCardCheckBox))) 
			testConfig.logPass("Store Card is Displayed"); 
		else 
			testConfig.logFail("Store Card is not Displayed"); 	}
	
	/**
	 *Verify Stored card Checkbox is set as unchecked 
	 */
	public void verifyStoreCardCheckboxSetAsUnchecked() {

		if (!storeCardCheckBox.isSelected()) {
			testConfig.logPass("Store card checkbox is set as unchecked");
		} else
			testConfig.logFail("Store card checkbox is set as checked");

	}
	/**
	 *Verify Stored Card checkbox is enabled 
	 */
	public void verifyStoreCardCheckboxIsEnabled() {
		if (storeCardCheckBox.isEnabled()) {
			testConfig.logPass("Store card checkbox is enabled");
		} else
			testConfig.logFail("Store card checkbox is not enabled");

	}
	

	@FindBy(id = "checkOfferdc")
	private WebElement CheckOfferLink;

	@FindBy(id = "checkOfferTagdc")
	private WebElement CheckOfferMessage;

	public void clickOnCheckOfferLink() {
		String strOfferMessage_actual;

		// click on check offer link
		Element.click(testConfig, CheckOfferLink, "Check Offer Link");
		// wait for error message to be displayed
		// Element.waitForElementDisplay(testConfig, CheckOfferMessage);
		Element.waitForElementToDisappear(testConfig, CheckOfferLink);
		Browser.waitForPageLoad(testConfig, CheckOfferMessage);
		// verify data displayed
		strOfferMessage_actual = CheckOfferMessage.getText().trim();
		// set property
		testConfig.putRunTimeProperty("checkOfferMessage_actual",
				strOfferMessage_actual);

	}

	/**
	 * verifies stored card is present in the list
	 * 
	 * @return
	 */
	public void verifyStoredCardIsPresent(int cardRowNum) {
		clickDebitCardDropdown();
		TestDataReader data = new TestDataReader(testConfig, "StoreCard");
		String storecardName = data.GetData(cardRowNum, "SavedCardName");
		//value put by function fillTransactionDetails at runtime
		String storeCardNameRunTimeValue= testConfig.getRunTimeProperty("StoreCardName");
		if(storeCardNameRunTimeValue != null)
			storecardName = storeCardNameRunTimeValue;
		WebElement storedCard = null;
		if (!testConfig.driver.findElement(By.linkText(storecardName))
				.isDisplayed())
			testConfig.logFail("Store Card:" + storecardName
					+ " is not available");
		else {
			storedCard = testConfig.driver.findElement(By
					.linkText(storecardName));
			Element.click(testConfig, storedCard, "Select " + storecardName);
		}

	}
}

