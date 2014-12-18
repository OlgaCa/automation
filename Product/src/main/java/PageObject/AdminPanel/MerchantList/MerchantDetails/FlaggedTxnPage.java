package PageObject.AdminPanel.MerchantList.MerchantDetails;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;


public class FlaggedTxnPage {

	private Config testConfig;

	@FindBy(xpath="//table/tbody/tr/td[4]")
	private WebElement txnid;
	
    @FindBy(css = "div.floatLeft")
    private WebElement transactionresult;
    
    @FindBy(xpath="//form/div[3]/input")
	private WebElement search;
	
	@FindBy(css="[type='submit'][value='Go']")
	private WebElement clickGo;
	
	public FlaggedTxnPage(Config testConfig){
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		//Browser.waitForPageLoad(this.testConfig, search);
	}

    
    public String transactionno() {
           String resultno = transactionresult.getText();
           String[] num = resultno.split("\\s+");
           return num[5];
    }

	public void searchFlaggedTxnPresent(String transactionId){
		Element.enterData(testConfig, search,transactionId, "Search the transaction");
	Element.click(testConfig, clickGo, "Click on Go, to search the transaction");
	String str = transactionno();
	
	if(Integer.parseInt(str)>0)
		testConfig.logPass("Transaction " + transactionId + " is present in flagged transaction");
	else 
		testConfig.logFail("Transaction " + transactionId + " is not present in flagged transaction");
	}
	
	public void searchFlaggedTxnAbsent(String transactionId){
		Element.enterData(testConfig, search,transactionId, "Search the transaction");
	Element.click(testConfig, clickGo, "Click on Go, to search the transaction");
	String str = transactionno();
	
	if(Integer.parseInt(str)>0)
		testConfig.logFail("Transaction " + transactionId + " is present in flagged transaction");
	else 
		testConfig.logPass("Transaction " + transactionId + " is not present in flagged transaction");
	}
}