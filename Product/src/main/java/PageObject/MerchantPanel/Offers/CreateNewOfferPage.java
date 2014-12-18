package PageObject.MerchantPanel.Offers;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import Test.MerchantPanel.Offers.OffersHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;
import Utils.Element.How;

public class CreateNewOfferPage {

	private Config testConfig;

	@FindBy(xpath = "//input[@value='Submit']")
	WebElement submitButton;

	@FindBy(xpath = "//input[@name='offer[title]']")
	WebElement offerTitleInput;

	@FindBy(xpath = "//textarea[@name='offer[description]']")
	WebElement offerDescriptionTextArea;

	@FindBy(name = "offer[valid_from]")
	WebElement offerValidFromDate;

	@FindBy(xpath = "//input[@name='offer[from_time]']")
	WebElement offerValidFromTime;

	@FindBy(name = "offer[valid_to]")
	WebElement offerValidToDate;

	@FindBy(xpath = "//input[@name='offer[to_time]']")
	WebElement offerValidToTime;

	@FindBy(xpath = "//input[@name='offer[maximum]']")
	WebElement offerCountInput;

	@FindBy(xpath= "//input[@name='offer[cc_max_days_per_card]']")
	WebElement timeDuration;
	
	@FindBy(xpath = "//input[@name='offer[discount]']")
	WebElement offerdiscountInput;

	@FindBy(xpath = "//select[@name='offer[discount_unit]']")
	WebElement discountUnitDD;

	@FindBy(xpath = "//input[@name='offer[payment_type][]']")
	WebElement pTypeCreditCardCheckBox;

	@FindBy(xpath ="(//input[@name='offer[payment_type][]'])[2]")
	WebElement pTypeDebitCardCheckBox;

	@FindBy(xpath = "(//input[@name='offer[payment_type][]'])[3]")
	WebElement pTypeNetBankingCheckBox;

	@FindBy(css = "textarea[name*='cc_card_bin']")
	WebElement cardBinCCInput;

	// card bin for debit card type
	@FindBy(css = "textarea[name*='dc_card_bin']")
	WebElement cardBinDCInput;

	@FindBy(xpath = "//div[@id='cc_options']/input")
	WebElement maxCountCCInput;

	// max count for Debit Card type
	@FindBy(xpath = "//input[@name='offer[dc_max_per_card]']")
	WebElement maxCountDCInput;

	@FindBy(xpath = "//div[@id='cc_options']/input[3]")
	WebElement maxAmountCCInput;

	// max amount for Debit Card type
	@FindBy(xpath = "//input[@name='offer[dc_max_amount_per_card]']")
	WebElement maxAmountDCInput;

	//cc card Type
	@FindBy(name="offer[cc_types][]")
	WebElement ccCardType;
	
	//dc select cards
	@FindBy(name="offer[dc_options][]")
	WebElement dcSelectCards;
	
	//dc card Type
	@FindBy(name= "offer[dc_types][]")
	WebElement dcCardType;
	
	//NB select banks
	@FindBy(name="offer[nb_options][]")
	WebElement nbSelectBanks;
	
	// card type all option
	@FindBy(css = "option[value=\"all\"]")
	WebElement cardTypeCCAllOption;

	// card type visa option
	@FindBy(css = "option[value=\"visa\"]")
	WebElement cardTypeCCVisaOption;

	// card type mastercard option
	@FindBy(css = "option[value=\"mastercard\"]")
	WebElement cardTypeCCMasterCardOption;

	// card type amex option
	@FindBy(css = "option[value=\"amex\"]")
	WebElement cardTypeCCAmexOption;

	// card type diners option
	@FindBy(css = "option[value=\"diners\"]")
	WebElement cardTypeCCDinersOption;

	// debit card select cards option
	@FindBy(css = "select[name=\"offer[dc_options][]\"] > option[value=\"all\"]")
	WebElement selectCardsDCAllOption;

	// debit card type All option
	@FindBy(css = "select[name=\"offer[dc_types][]\"] > option[value=\"all\"]")
	WebElement cardTypeDCAllOption;

	// debit card type visa option
	@FindBy(css = "select[name=\"offer[dc_types][]\"] > option[value=\"visa\"]")
	WebElement cardTypeDCVisaOption;

	// debit card type master option
	@FindBy(css = "select[name=\"offer[dc_types][]\"] > option[value=\"mastercard\"]")
	WebElement cardTypeDCMasterOption;

	// debit card type maestro option
	@FindBy(css = "option[value=\"maestro\"]")
	WebElement cardTypeDCMaestroOption;
	
	// debit card type rupay option
	@FindBy(css = "option[value=\"rupay\"]")
	WebElement cardTypeDCRupayOption;

	public OffersHelper oHelper;
	public CreateNewOfferPage(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		// Browser.waitForPageLoad(testConfig, amounT);
	}

	/**
	 * Helper method to fill offer details
	 */
	public TestDataReader fillOfferDetails(int offerRowNum, Boolean expired) {
		TestDataReader offerData = new TestDataReader(testConfig,"OfferDetails");
		String value = "";

		value = offerData.GetData(offerRowNum, "Description");
		Element.enterData(testConfig, offerDescriptionTextArea, value,"offer description");

		// get current date
		value = Helper.getCurrentDate("dd-MM-yyyy");
		Element.enterData(testConfig, offerValidFromDate, value,"offer valid from date");

		// get current time
		//value = Helper.getCurrentTime("HH:mm:ss");
		//testConfig.putRunTimeProperty("Time", value);
		//Element.enterData(testConfig, offerValidFromTime, value,"offer valid from time");
		//Element.enterData(testConfig, offerValidToTime, value,"offer valid to time");

		if(expired == true)
		{
			// get past date
			value = Helper.getDateBeforeOrAfterDays(-1,"dd-MM-yyyy");
		}
		
		else {
			// get tomorrow's date
		value = Helper.getDateBeforeOrAfterDays(1, "dd-MM-yyyy");
		}
		
		Element.enterData(testConfig, offerValidToDate, value,"offer valid to date");

		value = offerData.GetData(offerRowNum, "Offer Count");
		Element.enterData(testConfig, offerCountInput, value, "offer count");

		value = offerData.GetData(offerRowNum, "Discount");
		Element.enterData(testConfig, offerdiscountInput, value,"offer discount");

		return offerData;
	}

	/**
	 * Fill create new offer form
	 * 
	 * @param offerRowNum
	 * @param isEditOffer
	 *            - true if offer is being edited
	 */
	public TestDataReader fillOfferForm(int offerRowNum, Boolean isEditOffer, Boolean isExpiredOffer)
			 {


		// fill offer details section
		TestDataReader offerData = fillOfferDetails(offerRowNum, isExpiredOffer);
		String value = "";
        if(!isEditOffer) {
        	// fill offer title
		value = offerData.GetData(offerRowNum, "Title")+ Helper.generateRandomAlphabetsString(10);
		testConfig.putRunTimeProperty("Offer Title", value);
		Element.enterData(testConfig, offerTitleInput, value, "offer title");
        }
		// fill payment details
        fillPaymentTypeDetails(offerData,offerRowNum);
        return offerData;

	}
	/**
	 * fill payment type details section in create new offer form
	 * @param offerData
	 * @param offerRowNum
	 */
	public void  fillPaymentTypeDetails(TestDataReader offerData,int offerRowNum)
	{
		Robot pressCtrlKey = null;
		try {
			pressCtrlKey = new Robot();
		} catch (AWTException e) {
			testConfig.logException(e);
		}
		
		String paymentType = offerData.GetData(offerRowNum, "Payment Type");
        String modeConfig = "";		
		String CC = "";
		String DC = "";
		String NB = "";
		TestDataReader pTypeData = new TestDataReader(testConfig,"PaymentType");
		if(!paymentType.isEmpty())
		{
			
			String[] pType = paymentType.split("[,]");
			String[] bCode = new String[pType.length];
			String[] mode =  new String[pType.length];
			String[] bankName = new String[pType.length];
			for(int i =0;i<pType.length;i++)
			{
				bCode[i] = pTypeData.GetData(Integer.parseInt(pType[i]), "bankcode");
				mode[i] = pTypeData.GetData(Integer.parseInt(pType[i]), "mode");
				bankName[i] = pTypeData.GetData(Integer.parseInt(pType[i]), "Bank Name");
				
			}
            String bCodes = "";
			
			for(int i=0;i<bCode.length;i++)
			{
				if(mode[i].equals("debitcard"))
				{
				bCodes = bCodes.concat(bCode[i]);
				bCodes = bCodes.concat("\n");
				}
			}
	
		
			for(int i=0;i<pType.length;i++)
			{
					
				switch(mode[i])
				{
				case "creditcard":
					modeConfig = modeConfig.concat(mode[i]);
					String cardBin = offerData.GetData(offerRowNum, "Bin");
					String maxCount = offerData.GetData(offerRowNum, "CC Max Count");
					String maxAmount = offerData.GetData(offerRowNum, "CC Max Amount");
					String timeDuration1 = offerData.GetData(offerRowNum, "CC Time Duration");
					String cardTypes = offerData.GetData(offerRowNum, "CC Card Types");
					
					Element.check(testConfig, pTypeCreditCardCheckBox,
							"credit card check box");
					Element.enterData(testConfig, cardBinCCInput, cardBin, "card bin");

					Element.enterData(testConfig, maxCountCCInput, maxCount,
							"max count");

					Element.enterData(testConfig, timeDuration, timeDuration1, "time duration");
					
					Element.enterData(testConfig, maxAmountCCInput, maxAmount,
							"max amount");
					
					//deselect all option
					Element.click(testConfig, cardTypeCCAllOption, "all option");
					
					pressCtrlKey.keyPress(KeyEvent.VK_CONTROL);

						if (cardTypes.contains("visa"))
						{
							Element.click(testConfig, cardTypeCCVisaOption, "visa option");


						}
						if (cardTypes.contains("all"))
						{
							Element.click(testConfig, cardTypeCCAllOption, "all option");

						}

						if (cardTypes.contains("amex"))
						{
							Element.click(testConfig, cardTypeCCAmexOption, "amex option");    

						}
						
						if (cardTypes.contains("diners"))
						{
							Element.click(testConfig, cardTypeCCDinersOption,
									"diners option");

						}
							if(cardTypes.contains("mastercard"))
							{
								Element.click(testConfig, cardTypeCCMasterCardOption, "mastercard option");

							}
						pressCtrlKey.keyRelease(KeyEvent.VK_CONTROL);
						
						CC ="Card Bin: "+ cardBin +"\nMax No. of transactions Per card: "+ maxCount+ " in " + timeDuration1 + " Days\nupto max amount: Rs. "+ maxAmount ;
						testConfig.putRunTimeProperty("CC", CC);
						testConfig.putRunTimeProperty("CCCardTypes", cardTypes);
						
					
					break;
				case "debitcard":
					modeConfig = modeConfig.concat(mode[i]);
						
					String css = "option[value=\""+ bCode[i]+ "\"]";
					cardBin = offerData.GetData(offerRowNum, "Bin");
					 maxCount = offerData.GetData(offerRowNum, "DC Max Count");
					maxAmount = offerData.GetData(offerRowNum, "DC Max Amount");
					 timeDuration1 = offerData.GetData(offerRowNum, "DC Time Duration");
					 cardTypes = offerData.GetData(offerRowNum, "DC Card Types");
					Element.check(testConfig, pTypeDebitCardCheckBox,
							"debit card check box");
					//deselect all option
					Element.click(testConfig, cardTypeDCAllOption, "all option");

					Element.enterData(testConfig, cardBinDCInput, cardBin, "Card Bin");
					Element.enterData(testConfig, maxCountDCInput, maxCount,
							"Max Count");
					Element.enterData(testConfig, maxAmountDCInput, maxAmount,
							"Max Amount");
					Element.enterData(testConfig, Element.getPageElement(testConfig, How.name, "offer[dc_max_days_per_card]"), timeDuration1, "time duration");
					
					//select cards
					Element.click(testConfig, Element.getPageElement(testConfig, How.css, css),"select cards");
					
					pressCtrlKey.keyPress(KeyEvent.VK_CONTROL);

					
					if (cardTypes.contains("visa"))
					{
						Element.click(testConfig, cardTypeDCVisaOption, "visa option");
					}

					if (cardTypes.contains("all"))
					{
						Element.click(testConfig, cardTypeDCAllOption, "all option");
					}
					if (cardTypes.contains("mastercard"))
					{
						Element.click(testConfig, cardTypeDCMasterOption,
								"mastercard option");
					}

					if (cardTypes.contains("maestro"))
					{
						Element.click(testConfig, cardTypeDCMaestroOption,
								"amex option");
					}
					if (cardTypes.contains("rupay"))
					{
						Element.click(testConfig, cardTypeDCRupayOption,
								"Rupay option");
					}
					pressCtrlKey.keyRelease(KeyEvent.VK_CONTROL);
				    DC = "Card Types: "+ cardTypes + "\nCard Bin: "+ cardBin +"\nMax No. of transactions Per card: "+ maxCount+" in " + timeDuration1 + " Days upto max amount: Rs. "+ maxAmount +"\nBank Codes: "+bCodes ;
					testConfig.putRunTimeProperty("DC", DC);
					testConfig.putRunTimeProperty("dcCardTypes", cardTypes);
				
					break;
				case "netbanking":
					modeConfig = modeConfig.concat(mode[i]);
					
					String css1 = "option[value=\""+ bCode[i]+ "\"]";
					NB = bCode[i];
					Element.check(testConfig,  pTypeNetBankingCheckBox, "net banking checkbox");
					//deselect all banks
					Element.click(testConfig, Element.getPageElement(testConfig, How.xPath,"//select[@name='offer[nb_options][]']/option[1]"),"deselect all option");
					//select banks
					Element.click(testConfig, Element.getPageElement(testConfig, How.css, css1),"select Banks");
					testConfig.putRunTimeProperty("NB", NB);
					break;
					
				}
			}
			testConfig.putRunTimeProperty("mode", modeConfig);					
		}

	}
	


	/**
	 * Click submit button to create new offer
	 * 
	 * @return
	 */
	public OfferListPage clickSubmitButton() {
		
		Element.click(testConfig, submitButton, "Offer Submit Button");
		oHelper = new OffersHelper(testConfig);
		
		WebElement offersKeyTitle = Element.getPageElement(testConfig, How.xPath, "//th");
		// wait for offer list table
		Browser.waitForPageLoad(testConfig, offersKeyTitle);

		return new OfferListPage(testConfig);
	}

	public void clearForm() {
		
		offerDescriptionTextArea.clear();
		offerValidFromDate.clear();
		offerValidFromTime.clear();
		offerValidToTime.clear();
		offerValidToDate.clear();
		offerCountInput.clear();
		offerdiscountInput.clear();
		
		cardBinCCInput.clear();
	    maxCountCCInput.clear();
		timeDuration.clear();
		maxAmountCCInput.clear();
		Select ccSelect = new Select(ccCardType);
		ccSelect.deselectAll();
		
		// uncheck payment type checkbox
		if (pTypeCreditCardCheckBox.isSelected())
			Element.uncheck(testConfig, pTypeCreditCardCheckBox,
					"uncheck credit checkbox");
	
		cardBinDCInput.clear();
        maxCountDCInput.clear();
        maxAmountDCInput.clear();
        Select dcSelect = new Select(dcCardType);
        dcSelect.deselectAll();
		
        Select dcSelect1 = new Select(dcSelectCards);
		dcSelect1.deselectAll();
		if (pTypeDebitCardCheckBox.isSelected())
			Element.uncheck(testConfig, pTypeDebitCardCheckBox,
					"uncheck debit checkbox");
		Select nbSelect = new Select(nbSelectBanks);
		nbSelect.deselectAll();
		if (pTypeNetBankingCheckBox.isSelected())
			Element.uncheck(testConfig, pTypeNetBankingCheckBox,
					"uncheck netbanking checkbox");
		
	}
	
	public void verifyEditOfferPage(int offerRowNum) {
		TestDataReader offerData = new TestDataReader(testConfig,
				"OfferDetails");

		Helper.compareContains(testConfig, "Offer Title",
				testConfig.getRunTimeProperty("Offer Title"),
				offerTitleInput.getAttribute("value"));

		String expectedDescription = offerData.GetData(offerRowNum,
				"Description");
		Helper.compareEquals(testConfig, "Offer Description",
				expectedDescription, offerDescriptionTextArea.getText());

		String expectedValidFromDate = Helper.getCurrentDate("dd-MM-yyyy");
		Helper.compareEquals(testConfig, "Valid From Date",
				expectedValidFromDate, offerValidFromDate.getAttribute("value"));

		String expectedValidToDate = Helper.getDateBeforeOrAfterDays(1, "dd-MM-yyyy");
		Helper.compareEquals(testConfig, "Offer Valid From Date and time",
				expectedValidToDate, offerValidToDate.getAttribute("value"));

		String expectedOfferCount = offerData.GetData(offerRowNum,
				"Offer Count");
		Helper.compareEquals(testConfig, "Offer Count", expectedOfferCount,
				offerCountInput.getAttribute("value"));

		String expectedDiscount = offerData.GetData(offerRowNum, "Discount");
		Helper.compareEquals(testConfig, "discount", expectedDiscount,
				offerdiscountInput.getAttribute("value"));

		String paymentType = offerData.GetData(offerRowNum, "Payment Type");

		if (paymentType.contentEquals("Credit Card")) {
			Helper.compareTrue(testConfig, "payment type check box",
					pTypeCreditCardCheckBox.isSelected());

			String expectedCardBin = offerData.GetData(offerRowNum, "Bin");
			Helper.compareEquals(testConfig, "Card Bin", expectedCardBin,
					cardBinCCInput.getText());

			String expectedMaxCount = offerData.GetData(offerRowNum,
					"Max Count");
			Helper.compareEquals(testConfig, "max count", expectedMaxCount,
					maxCountCCInput.getAttribute("value"));

			String expectedMaxAmount = offerData.GetData(offerRowNum,
					"Max Amount");
			Helper.compareEquals(testConfig, "max amount", expectedMaxAmount,
					maxAmountCCInput.getAttribute("value"));

			String expectedCardTypes = offerData.GetData(offerRowNum,
					"Card Types");

			if (expectedCardTypes.contains("visa")) {
				Helper.compareTrue(testConfig, "visa option selected",
						cardTypeCCVisaOption.isSelected());
			}
			if (expectedCardTypes.contains("mastercard")) {
				Helper.compareTrue(testConfig, "mastercard option selected",
						cardTypeCCMasterCardOption.isSelected());
			}
			if (expectedCardTypes.contains("amex")) {
				Helper.compareTrue(testConfig, "amex option selected",
						cardTypeCCAmexOption.isSelected());
			}
			if (expectedCardTypes.contains("diners")) {
				Helper.compareTrue(testConfig, "diners option selected",
						cardTypeCCDinersOption.isSelected());
			}

		}

	}
	
	public String discountUnit()
	{
		return discountUnitDD.getText();
		
	}

}
