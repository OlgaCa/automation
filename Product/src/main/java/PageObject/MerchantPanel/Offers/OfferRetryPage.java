package PageObject.MerchantPanel.Offers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.MerchantPanel.Offers.OffersHelper;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.TestDataReader;

public class OfferRetryPage{

	@FindBy(css="#retry-button")
	WebElement offerRetryButton;

	@FindBy(id = "continue-button")
	WebElement offerContinueButton;

	@FindBy(css="#offer_success>span")
	WebElement offerFailureMessage;

	@FindBy(css=".prepend-top>a")
	WebElement offerCancelButton;


	@FindBy(css="#offer_success>div>span")
	WebElement offerTitle;


	private Config testConfig;
	public OffersHelper oHelper;

	public OfferRetryPage(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		if(Element.IsElementDisplayed(testConfig, offerContinueButton));
		else if(Element.IsElementDisplayed(testConfig, offerRetryButton));
	}

	public String getOfferFailureTitleMessage(){
		return offerTitle.getText();
	}

	public String getFailureMessageDetails(){
		return offerFailureMessage.getText();
	}


	public void clickRetryButton(){
		Element.click(testConfig, offerRetryButton, "Retry Button");
	}

	public void clickContinueButton(){
		Element.click(testConfig, offerContinueButton, "Continue Button");
	}

	public void clickCancelButton(){
		Element.click(testConfig, offerCancelButton, "Cancel Button");
	}

	/**
	 * Click Continue and get the Test Response Page
	 */
	public TestResponsePage clickContinueToGetTestResponse()
	{
		Element.click(testConfig, offerContinueButton, "Continue");
		return new TestResponsePage(testConfig);
	}

	/**
	 * Click Continue and get the Bank Redirect Page
	 */
	public BankRedirectPage clickContinueToGetBankRedirectPage()
	{
		Element.click(testConfig, offerContinueButton, "Continue");
		return new BankRedirectPage(testConfig);
	}
	
	/**
	 * Click Retry to get Payment Option page
	 */
	public PaymentOptionsPage clickRetryToGetPaymentPage(){
		Element.click(testConfig, offerRetryButton, "Retry");
		return new PaymentOptionsPage(testConfig);
	}


	/**
	 * Click on Cancel to cancel the transaction
	 * @return Test Response Page
	 */
	public TestResponsePage clickCancelToGetTestResponse()
	{
		Element.click(testConfig, offerCancelButton, "Cancel Transaction");
		return new TestResponsePage(testConfig);
	}

	public void verifyRetryErrorMessage(String expectedMessage)
	{
		String actualMessage;
		/*if (testConfig.getRunTimeProperty("NewOfferRetryPage")!=null)
		{
			WebElement messageDetail=Element.getPageElement(testConfig, How.css, "#offer_success>span");
			actualMessage=messageDetail.getText();
		}
		else */
			actualMessage=getFailureMessageDetails();
		if (!actualMessage.contains(expectedMessage))
			testConfig.logFail("Retry Message", expectedMessage, actualMessage);
		else
			testConfig.logPass("Retry Message", actualMessage);

	}
	public void  verifyRetryErrorTitle(String expectedTitle)
	{	String actualTitle;
/*		if (testConfig.getRunTimeProperty("NewOfferRetryPage")!=null){
		WebElement title=Element.getPageElement(testConfig, How.css, "#offer_success>div>span");
		actualTitle=title.getText();
	}
	else*/
		actualTitle=getOfferFailureTitleMessage();
	if (!actualTitle.equals(expectedTitle))
		testConfig.logFail("Retry Title", expectedTitle, actualTitle);
	else
		testConfig.logPass("Retry Title", actualTitle);
	}
	
	public void verifyRetryMessage(String expectedOfferRetryTitle, String expectedOfferRetryMessage){
		verifyRetryErrorTitle(expectedOfferRetryTitle);	
		verifyRetryErrorMessage(expectedOfferRetryMessage);
	}
}
