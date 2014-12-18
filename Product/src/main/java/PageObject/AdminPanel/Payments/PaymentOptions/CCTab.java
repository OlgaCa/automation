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
import Utils.Element.How;

public class CCTab {

	protected Config testConfig;

	public CCTab(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, getNameOnCard());
	}
	
	@FindBy(id = "creditlink")
	private WebElement CCTabtxt;

	public String getCCTabText() {
		return CCTabtxt.getText();
	}

	// Credit Card type
	@FindBy(id = "credit-card")
	private WebElement visaMasterCard;

	@FindBy(id = "amex-cards")
	private WebElement amexCard;

	@FindBy(id = "diners")
	private WebElement dinerCard;

	// Credit Card type images
	@FindBy(css = "#credit-card + img")
	private WebElement visaMasterCardImg;

	@FindBy(css = "#amex-cards + img")
	private WebElement amexCardImg;

	@FindBy(css = "#diners + img")
	private WebElement dinerCardImg;

	public enum CardType {
		VisaMasterCard, Amex, Diners
	};

	/**
	 * Choose one of the Card Type radio options
	 * 
	 * @param cardType
	 *            CardType
	 */
	public void chooseCardType(CardType cardType) {
		switch (cardType) {
		case Amex:
			Element.click(testConfig, amexCard, "AMEX Card");
			break;
		case Diners:
			Element.click(testConfig, dinerCard, "Diners Card");
			break;
		case VisaMasterCard:
			Element.click(testConfig, visaMasterCard, "Visa Master Card");
			break;
		}
		Browser.wait(testConfig, 1);
	}

	// Label Text Fields
	@FindBy(xpath = "//p[@id='card-radios']/label")
	private WebElement cardTypeLabel;

	public String getCardTypeLabel() {
		return cardTypeLabel.getText();
	}

	@FindBy(xpath = "//input[@id='cname_on_card']/../label")
	private WebElement cardNameLabel;

	public String getCardNameLabel() {
		return cardNameLabel.getText();
	}

	@FindBy(xpath = "//input[@id='ccard_number']/../label")
	private WebElement cardNumberLabel;

	public String getCardNumberLabel() {
		return cardNumberLabel.getText();
	}

	@FindBy(xpath = "//input[@id='ccvv_number']/../label")
	private WebElement cvvNumberLabel;

	public String getCVVNumberLabel() {
		return cvvNumberLabel.getText();
	}

	@FindBy(xpath = "//select[@id='cexpiry_date_month']/../label")
	private WebElement expiryDateLabel;

	public String getExpiryDateLabel() {
		return expiryDateLabel.getText();
	}

	@FindBy(xpath = "//div[@id='payment-box']/p")
	private WebElement NoteLabelInBox;

	public String getNoteText() {
		return NoteLabelInBox.getText();
	}

	// Payment Fields
	@FindBy(id = "cname_on_card")
	private WebElement NameOnCard;

	@FindBy(id = "ccard_number")
	private WebElement CardNumber;

	@FindBy(id = "ccvv_number")
	private WebElement Cvv;

	@FindBy(id = "cexpiry_date_month")
	private WebElement ExpiryMonth;

	@FindBy(id = "cexpiry_date_year")
	private WebElement ExpiryYear;

	@FindBy(css = ".cc-form > div > label")
	private WebElement FirstLabelByABTest;

	public String getFirstLabelByABTest()
	{
		return FirstLabelByABTest.getText();
	}
	
	/**
	 * Reads the "CardDetails" sheet of Test Data excel and fill the credit card
	 * details
	 * 
	 * @param cardRow
	 *            Row number of excel sheet to be filled in
	 * @return Returns the Excel sheet data which was filled
	 */
	public TestDataReader FillCardDetails(int cardRow) {
		TestDataReader data = new TestDataReader(testConfig, "CardDetails");

		String value = "";

		value = data.GetData(cardRow, "Name");
		Element.enterData(testConfig, getNameOnCard(), value, "Name on Card");

		value = data.GetData(cardRow, "CC");
		for(char c : value.toCharArray())
		{
			Element.enterDataWithoutClear(testConfig, getCardNumber(), String.valueOf(c), "Card Number");
		}
		value = data.GetData(cardRow, "CVV");
		Element.enterData(testConfig, getCvv(), value, "CVV");

		value = data.GetData(cardRow, "Mon");
		Element.selectValue(testConfig, getExpiryMonth(), value, "Expiry month");

		value = data.GetData(cardRow, "Year");
		Element.selectValue(testConfig, getExpiryYear(), value, "Expiry year");

		return data;
	}

	// Error messages
	@FindBy(css = "#cname_on_card+label")
	private WebElement CardNameErrorMessage;

	public String getCardNameErrorMessage() {
		return CardNameErrorMessage.getText();
	}

	@FindBy(css = "#ccard_number+label")
	private WebElement CardNumberErrorMessage;

	public String getCardNumberErrorMessage() {
		return CardNumberErrorMessage.getText();
	}

	@FindBy(css = "#ccvv_number+label")
	private WebElement CvvNumberErrorMessage;

	public String getCvvNumberErrorMessage() {
		return CvvNumberErrorMessage.getText();
	}

	@FindBy(css = "#cexpiry_date_month+label")
	private WebElement ExpiryDateErrorMessage;

	public String getExpiryDateErrorMessage() {
		return ExpiryDateErrorMessage.getText();
	}

	@FindBy(css = "span.cvv_help")
	private WebElement whatIsCVV;

	public void clickWhatIsCVVLink() {
		Element.click(testConfig, whatIsCVV, "What Is CVV Link");
	}

	@FindBy(id = "ccvv_help")
	private WebElement cardImage;

	public boolean isCardImageAppearing() {
		return cardImage.isEnabled();
	}

	public void VerifyCardTypeImages() {
		Helper.compareContains(testConfig, "Visa Master Card Image",
				"/images/visa-master_card.jpg",
				visaMasterCardImg.getAttribute("src"));
		Helper.compareContains(testConfig, "Amex Card Image",
				"/images/pay-american.jpg", amexCardImg.getAttribute("src"));
		Helper.compareContains(testConfig, "Diners Card Image",
				"images/pay-diners.jpg", dinerCardImg.getAttribute("src"));
	}

	@FindBy(id = "storeCardLink")
	private WebElement storeCardLink;

	public void clickStoreCardLink() {
		Element.click(testConfig, storeCardLink, "Save Card Link");
	}

	@FindBy(id = "storeCard")
	private WebElement storeCardCheckBox;

	public void clickStoreCardCheckBox() {
		Element.click(testConfig, storeCardCheckBox, "Save Card CheckBox");
	}

	@FindBy(id = "manageCardLink")
	private WebElement manageThisCardLink;

	public void clickmanageThisCardLink() {
		Element.click(testConfig, manageThisCardLink, "Manage Card Link");
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

	@FindBy(id = "ccard_name")
	private WebElement storeCardName;

	@FindBy(id = "drop_image_1")
	private WebElement creditCardDropdown;

	public void clickCreditCardDropdown() {
		Element.click(testConfig, creditCardDropdown,
				"Click Credit Card Dropdown");
	}

	@FindBy(css = "a.payment_title > img[src*='visa']")
	private WebElement visaCardOption;

	public void selectVisaCard() {
		clickCreditCardDropdown();
		Element.click(testConfig, visaCardOption, "Select Visa Card");
	}

	@FindBy(css = "a.payment_title > img[src*='american']")
	private WebElement amexCardOption;

	public void selectAmexCard() {
		clickCreditCardDropdown();
		Element.click(testConfig, amexCardOption, "Select Amex Card");
	}

	@FindBy(css = "a.payment_title > img[src*='diners']")
	private WebElement dinersCardOption;

	public void selectDinersCard() {
		clickCreditCardDropdown();
		Element.click(testConfig, dinersCardOption, "Select Diners Card");
	}

	public WebElement selectStoredCard(String Linktext) {
		clickCreditCardDropdown();
		WebElement storedCard = null;
		if (!testConfig.driver.findElement(By.linkText(Linktext)).isDisplayed())
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
			rnd= Helper.generateRandomAlphaNumericString(5);
			value = data.GetData(cardRow, "SavedCardName");
			if (testConfig.getRunTimeProperty("UseStoredDetails").equals("2"))
			{
				Element.enterData(testConfig, storeCardName, value+rnd, "Store CardName");
				testConfig.putRunTimeProperty("StoreCardName", value+rnd);
			}
			else
				Element.enterData(testConfig, storeCardName, value, "Name on Card");
		} 
		else if(testConfig.getRunTimeProperty("SI").contains("1"))
		{
			String rnd;
			rnd= Helper.generateRandomAlphaNumericString(5);
			value = data.GetData(cardRow, "SavedCardName");
			if (testConfig.getRunTimeProperty("UseStoredDetails").equals("2"))
			{
				Element.enterData(testConfig, storeCardName, value+rnd, "Store CardName");
				testConfig.putRunTimeProperty("StoreCardName", value+rnd);
			}
			else
				Element.enterData(testConfig, storeCardName, value, "Name on Card");
		}
		else {
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

	public void selectCardType(CardType cardType) {
		if (!creditCardDropdown.isDisplayed()) {
			switch (cardType) {
			case Amex:
				Element.click(testConfig, amexCard, "AMEX Card");
				break;
			case Diners:
				Element.click(testConfig, dinerCard, "Diners Card");
				break;
			case VisaMasterCard:
				Element.click(testConfig, visaMasterCard, "Visa Master Card");
				break;
			}
		} else {
			switch (cardType) {
			case Amex:
				selectAmexCard();
				break;
			case Diners:
				selectDinersCard();
				break;
			case VisaMasterCard:
				selectVisaCard();
				break;
			}
		}
		Browser.wait(testConfig, 1);
	}

	public void removeCCStoredCard() {

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

	@FindBy(css = "div[id='credit'] a:contains('Learn more')")
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

	public void verifyStoreCardDoesNotExists(){ 	
		if ((Element.IsElementDisplayed(testConfig,storeCardLink)) || (Element.IsElementDisplayed(testConfig,storeCardCheckBox))) 
			testConfig.logFail("Saved Card Options displayed without user Credential"); 
		else 
			testConfig.logPass("Saved Card Option doesnot display without user Credential"); 	}

	
	@FindBy(css = "div#payment-box fieldset:nth-child(2) div p")
	private WebElement storeCardFooterMessage;
	
	@FindBy(css = "div.store_card_SI span img")
	private WebElement GrayedOutCheckBoxForSI;
	
	@FindBy(css = "div.store_card_SI span")
	private WebElement SIOneMessage;
	
	public void verifyStoreCardFooterMessage()
	{
		if(testConfig.getRunTimeProperty("SI").equals("2"))
		{
			Helper.compareEquals(testConfig, "store card footer message", "I agree to save card details for Standing Instruction. Learn more", storeCardFooterMessage.getText());
		}
		else if(testConfig.getRunTimeProperty("SI").equals("1"))
		{
			Element.verifyElementNotPresent(testConfig, storeCardCheckBox, "checkbox to save card");
			Element.verifyElementNotPresent(testConfig, storeCardLink, "checkbox to save card");
			Element.verifyElementPresent(testConfig, cardNameLabel, "Card name label text box");
			Element.verifyElementPresent(testConfig, GrayedOutCheckBoxForSI, "grayed out check box image");
			Helper.compareEquals(testConfig, "Message in case SI=1", "I agree to save card details for Standing Instruction.", SIOneMessage.getText());
			Helper.compareEquals(testConfig, "store card footer message", "Type a custom label to save this card (optional)", storeCardFooterMessage.getText());
		}
		else
		{
			Helper.compareEquals(testConfig, "store card footer message", "Save card number and expiry for next transaction. Learn more", storeCardFooterMessage.getText());
		}
	}
	@FindBy(id = "checkOffercc")
	private WebElement CheckOfferLink;

	@FindBy(id = "checkOfferTagcc")
	private WebElement CheckOfferMessage;

	public void clickOnCheckOfferLink() {
		String strOfferMessage_actual;

		// click on check offer link
		Element.click(testConfig, CheckOfferLink, "Check Offer Link");
		// wait for error message to be displayed
		Element.waitForElementToDisappear(testConfig, CheckOfferLink);
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
		clickCreditCardDropdown();
		TestDataReader data = new TestDataReader(testConfig, "StoreCard");
		String value = "";
		String storecardName = data.GetData(cardRowNum, "SavedCardName");
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

	public WebElement getCardNumber() {
		return CardNumber;
	}

	public WebElement getNameOnCard() {
		return NameOnCard;
	}

	public WebElement getCvv() {
		return Cvv;
	}

	public WebElement getExpiryMonth() {
		return ExpiryMonth;
	}

	public WebElement getExpiryYear() {
		return ExpiryYear;
	}
	
	@FindBy(css=".check_box_btn>img")
	private WebElement checkedStoreCardBox_Image;

	/**
	 * Verifies stored card is present
	 */
	public void verifyStoreCardDisplayed() {
		if ((Element.IsElementDisplayed(testConfig, storeCardLink))
				&& (Element.IsElementDisplayed(testConfig, storeCardCheckBox)))
			testConfig.logPass("Store Card is Displayed");
		else
			testConfig.logFail("Store Card is not Displayed");
	}
	
	/**
	 *Verify stored card is set as unchecked 
	 */
	public void verifyStoreCardCheckboxSetAsUnchecked() {
		if (!storeCardCheckBox.isSelected()) {
			testConfig.logPass("Store card checkbox is set as unchecked");
		} else
			testConfig.logFail("Store card checkbox is set as checked");

	}
	
	/**
	 *Verify stored card checkbox is enabled
	 */
	public void verifyStoreCardCheckboxIsEnabled() {
		if (storeCardCheckBox.isEnabled()) {
			testConfig.logPass("Store card checkbox is enabled");
		} else
			testConfig.logFail("Store card checkbox is not enabled");

	}
	
	/**
	 *Verify stored card checkbox is set as checked
	 */
	public void verifyStoreCardCheckboxSetAsChecked() {
		if (checkedStoreCardBox_Image.isDisplayed()) {
			testConfig.logPass("Store card checkbox is set as checked and appears an an image, hence can not be unchecked");
		} else
			testConfig.logFail("Expected Store card checkbox as checked actual is unchecked");

	}
}
