package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class PayUMoneyTab {

	private Config testConfig;
	public enum PayuMoneyCredentialInputs{email,phonenumber};
	
	public PayUMoneyTab(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, walletInfoText);
	}

	
	@FindBy(css=".wallet_info")
	private WebElement walletInfoText;
	

	// Credit Card type
	@FindBy(id = "wallet_email_input")
	private WebElement emailTxtBox;

	@FindBy(id = "wallet_mobile_input")
	private WebElement phoneTxtBox;

	@FindBy(className = "button")
	private WebElement payUsingPayUMoneyButton;

	
	/**
	 * Reads the "TransactionDetails" sheet of Test Data excel and fill the credit card
	 * details
	 * 
	 * @param cardRow
	 *            Row number of excel sheet to be filled in
	 * @return Returns the Excel sheet data which was filled
	 */
	public TestDataReader FillDetails(int TransactionDetailsRow) {
		TestDataReader data = new TestDataReader(testConfig,
				"TransactionDetails");

		String email = data.GetData(TransactionDetailsRow, "email");

		Element.enterData(testConfig, emailTxtBox, email, "Email Id");

		String phoneNumber = data.GetData(TransactionDetailsRow, "phone");

		Element.enterData(testConfig, phoneTxtBox, phoneNumber, "Phone Number");

		return data;
	}

	// Error messages
	@FindBy(css=".wallet_email.wallet_error")
	WebElement emailBoxInError;

	public void checkErrorOnEmailBox(){
		if(Element.IsElementDisplayed(testConfig, emailBoxInError)){
			String ErrorColor = emailBoxInError.getCssValue("color");
			Helper.compareEquals(testConfig, "Email Textbox color", "#bf0303", Color.fromString(ErrorColor).asHex());
			testConfig.logPass("Error on clicking Pay Button with Wrong Email Text box");
		}else{
			testConfig.logFail("No error message on clicking Pay Button with Wrong Email text box");
		}
	}
	
	@FindBy(css=".wallet_mobile.wallet_error")
	WebElement phoneBoxInError;
	
	public void checkErrorOnPhoneNoTextBox(){
		
		if(Element.IsElementDisplayed(testConfig, phoneBoxInError)){
			String ErrorColor = phoneBoxInError.getCssValue("color");
			Helper.compareEquals(testConfig, "Email Textbox color", "#bf0303", Color.fromString(ErrorColor).asHex());
			testConfig.logPass("Error on clicking Pay Button with Wrong Phone number Text box");
		}else{
			testConfig.logFail("No error message on clicking Pay Button with Wrong Phone number text box");
		}
	}

	public String getDefaultEmailValue(){
		
		return emailTxtBox.getAttribute("value").trim();
	}
	
	public String getDefaultPhoneValue(){

		return phoneTxtBox.getAttribute("value").trim();
	}
	
	/*public PaymentLoginPage clickPayUsingPayUMoneyButtonAndReturnPaymentLoginPage(){
		Element.click(testConfig, payUsingPayUMoneyButton, "Pay Using PayUMoney");
		return new PaymentLoginPage(testConfig);
	}*/
	
	public void clickPayUsingPayUMoneyButtonAndVerifyError(PayuMoneyCredentialInputs input){
		Element.click(testConfig, payUsingPayUMoneyButton, "Pay Using PayUMoney");
		if(input==PayuMoneyCredentialInputs.phonenumber)
			checkErrorOnPhoneNoTextBox();
		else if(input==PayuMoneyCredentialInputs.email)
					checkErrorOnEmailBox();
	}	
	public void clickPayUsingPayUMoneyButton(){
		Element.click(testConfig, payUsingPayUMoneyButton, "Pay Using PayUMoney");
	}

	@FindBy(css = ".wallet_mark")
	public WebElement offerIcon;
	
	@FindBy(css = "#PayuPaisaWallet>h1")
	public WebElement offerHeading;
	@FindBy(css = "#InfoDivTrigger")
	public WebElement RewardLink;

	@FindBy(css = ".form_label")
	public WebElement formLabel;

	
	@FindBy(css = ".left_heading")
	public WebElement leftHeading;

	}

