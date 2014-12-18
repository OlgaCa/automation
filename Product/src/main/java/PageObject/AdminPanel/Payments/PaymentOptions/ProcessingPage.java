package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestDataReader;

public class ProcessingPage {

	String actualAmount="";

	
	@FindBy(xpath="//div[@id='offer_success']")
	private WebElement 	offerSuccess_Message;
	
	public WebElement getOfferMessage(){
		return offerSuccess_Message;
	}
	
	String originalAmountOnDiscountTxn="//*[contains(text(),'Original Amount:  TestValue')]";
	
	
	public WebElement getOriginalAmountOnDiscountTxn(String val){
		return testConfig.driver.findElement(By.xpath(originalAmountOnDiscountTxn.replaceAll("TestValue",val)));
	}
	
	@FindBy(xpath="//*[contains(text(),'Discount Awarded:')]")
	private WebElement discountText;
	
	public WebElement getDiscountText(){
		return discountText;
	}
	
	@FindBy(xpath="//*[contains(text(),'Net Amount Payable:')]")
	private WebElement netAmountPayableText;
	
	public WebElement getNetAmountPayableText(){
		return netAmountPayableText;
	}
	
	@FindBy(xpath=".//*[@id='continue-button']")
	private WebElement continueButtonOnRetryPage;
	
	public WebElement getContinueButtonOnRetryPage(){
		return continueButtonOnRetryPage;
	}
	
	@FindBy(xpath=".//*[@id='retry-button']")
	private WebElement retryButtononRetryPage;
	
	public WebElement getRetryButtononRetryPage(){
		return retryButtononRetryPage;
	}

	@FindBy(xpath=".//*[@id='offer_success']/div[1]/span[contains(text(),'Sorry')]")
	private WebElement sorryMessageOnRetryPage;
	
	public WebElement getSorryMessageOnRetryPage(){
		return sorryMessageOnRetryPage;
	}
	
	@FindBy(xpath="//span[contains(text(),'Maximum Discount')]")
	private WebElement maxmimumDiscount;
	
	public WebElement getMaxmimumDiscount(){
		return maxmimumDiscount;
	}
	
	
	
	
	
	private Config testConfig;

	public ProcessingPage(Config testConfig, boolean expectingThreeDPopup) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);

		if(!expectingThreeDPopup) //if 3d pop-up is not opened, then only processing page showing the amount is displayed 
		{
			String process=testConfig.getRunTimeProperty("publickey");
			if(process!=null)
			{
				Browser.verifyIntermediateURL(testConfig, "https://secure.payu.in/_seamless_payment");		
			}
			else
				//ensure that we are on pay page
				Browser.verifyIntermediateURL(testConfig, testConfig.getRunTimeProperty("PaymentUrl") + "pay");				
		}
		else
		{
			//switch to 3d popup window
			Browser.switchToNewWindow(testConfig);

			//ensure that we are on pay page
			Browser.verifyIntermediateURL(testConfig, testConfig.getRunTimeProperty("PaymentUrl") + "pay");	
		}
	}


	public void verifyThreeDPopupcookie(String expectedCookieValue)
	{
		//Read the cookie on processsing page
		String cookieValue = Browser.getCookieValue(testConfig, "abStat-new_threed_popup");
		switch(expectedCookieValue)
		{
		case "new_threed_popup_0":
			Helper.compareEquals(testConfig, "3d popup enable cookie", "new_threed_popup_0", cookieValue);
			break;

		case "new_threed_popup_1":
			Helper.compareEquals(testConfig, "3d popup disable cookie", "new_threed_popup_1", cookieValue);
			break;
		}
	}


	@FindBy(id = "amountdiv")
	private WebElement amount;

	public WebElement getAmountElementOnProcessingpage(){
		return amount;
	}

	public String getAmount (){
		String amounttext=amount.getText();
		return amounttext;
	}
	public boolean verifyAmount() {

		actualAmount=getAmount();
		boolean isPass=false;
		String expectedAmount=testConfig.getRunTimeProperty("totalAmount");
		if(expectedAmount.contains(".00")){
			String[] expectedAmountarray = new String[2];
			expectedAmountarray=expectedAmount.split(".00");
			expectedAmount=expectedAmountarray[0];
		}
		else if(expectedAmount.matches(".*\\.\\d+[0]")){
			String[] expectedAmountarray = new String[2];
			expectedAmountarray=expectedAmount.split("[0]$");
			expectedAmount=expectedAmountarray[0];
		}

		try{
			int retry=100;


			while(actualAmount!=expectedAmount && retry>0){

				if (actualAmount.contains(expectedAmount)){
					isPass=true;
					testConfig.logPass("amount on processing page", expectedAmount);
					break;
				}
				else
					actualAmount=getAmount();	
				retry--;
			}
		}
		catch (Exception e)
		{	actualAmount=actualAmount.trim();
		if(!actualAmount.contains(expectedAmount.trim())){
			testConfig.logFail("amount",expectedAmount, actualAmount);
			isPass=false;
		}
		else{
			testConfig.logPass("amount", actualAmount);
			isPass=true;
		}
		}
		return isPass;
	}

	@FindBy(css = "div#loader span")
	private WebElement redirectionMessage;

	public void verifyRedirectionMessage()
	{
		Helper.compareEquals(testConfig, "Redirection Message", "You will be redirected to your bank's website for 3D secure authentication.", 
				redirectionMessage.getText());
	}

	public void VerifyRedirectedURL()
	{   String publicKey=testConfig.getRunTimeProperty("publickey");
	if(publicKey!=null)
	{
		String expectedUrl = "https://secure.payu.in/_seamless_payment";
		Browser.verifyIntermediateURL(testConfig, expectedUrl);
	}
	}
	
	@FindBy(xpath = "//span[@class='offer_message_new']")
	public WebElement limitMessage;

	public WebElement getLimitMessage() {
		return limitMessage;
	}

}
