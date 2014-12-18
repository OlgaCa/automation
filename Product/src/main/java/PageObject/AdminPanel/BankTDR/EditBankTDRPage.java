package PageObject.AdminPanel.BankTDR;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.TestDataReader;

public class EditBankTDRPage {

	private Config testConfig;
	
	@FindBy(xpath = ".//*[@id='editTDR']/form/select[1]")
	private WebElement selectPaymentGateway;

	@FindBy(xpath = ".//*[@id='MerchantCategory']")
	private WebElement selectMerchantCategory;
	
	@FindBy(css = "div#editTDR form [name='merchant']")
	private WebElement selectMerchant;
	
	@FindBy(css = "div#editTDR form [name='mode']")
	private WebElement selectModes;
	
	@FindBy(css = "div#editTDR form [name='cardtype']")
	private WebElement selectCardType;
	
	@FindBy(name="amount_lower_limit")
	private WebElement txtAmountLowerLimit;
	
	@FindBy(name="flatFee")
	private WebElement txtFlatFee;
	
	@FindBy(name="percentFee")
	private WebElement txtPercentFee;
	
	@FindBy(name="shareFee")
	private WebElement txtShareFee;
	
	@FindBy(css="[name='TDR'],[value='Add']")
	private WebElement btnTDR;
	
	@FindBy(css= "#RulesGetwayWise a")
	private WebElement viewRulesGatewayWise;
	
	@FindBy(css="#RulesGetwayWise input[value*='Remove']")
	private WebElement btnDelete;
	
	@FindBy(xpath="html")
	private WebElement calculateBankTDRPage;
		
	public EditBankTDRPage(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
	}
	
	public void CreateTDRRule(int row , String Sheet) {
		TestDataReader bankTDR = new TestDataReader(testConfig, Sheet);
 
		String paymentGatewayValue = bankTDR.GetData(row,"PaymentGateway");
		Element.selectVisibleText(testConfig, selectPaymentGateway, paymentGatewayValue, "payment gateway");
		
		String merchantCategoryValue = bankTDR.GetData(row, "MerchantCategory");
		if(merchantCategoryValue != "{skip}") {
			Element.selectVisibleText(testConfig, selectMerchantCategory, merchantCategoryValue, "merchant category");
			Browser.wait(testConfig, 3);
		}
		String merchantValue = bankTDR.GetData(row,"Merchant");
		if(merchantValue != "{skip}") {
		 Element.selectVisibleText(testConfig, selectMerchant, merchantValue, "merchant");
		}
		String paymentModesValue = bankTDR.GetData(row,"PaymentModes");
		Element.selectVisibleText(testConfig, selectModes, paymentModesValue, "payment Modes");
		
		String cardTypeValue = bankTDR.GetData(row, "CardType");
		Element.selectVisibleText(testConfig, selectCardType, cardTypeValue, "Select card type " + cardTypeValue);
		
		String amountLowerLimitValue = bankTDR.GetData(row, "AmountLowerLimit");
		Element.enterData(testConfig, txtAmountLowerLimit, amountLowerLimitValue, "Enter Lower Limit Amount");

		String flatFeeValue = bankTDR.GetData(row, "FlatFee");
		Element.enterData(testConfig, txtFlatFee, flatFeeValue, "Enter Flat fee value");

		String percentFeeValue = bankTDR.GetData(row, "PercentFee");
		Element.enterData(testConfig, txtPercentFee, percentFeeValue, "Enter percent fee");

		String shareFeeValue = bankTDR.GetData(row, "ShareFee");
		Element.enterData(testConfig, txtShareFee, shareFeeValue, "Enter share fee");

		Element.click(testConfig, btnTDR, "Click Add button to create bank TDR");

	}
	
	public void deleteExistingRules() {
		while(Element.IsElementDisplayed(testConfig,viewRulesGatewayWise)){
			Element.click(testConfig, viewRulesGatewayWise, "View Rules Gateway Wise");
			Element.click(testConfig, btnDelete, "Deleting Existing Rules");
		}
	}
	
	public void applyBankTDR(Config testConfig) {
		
		//Apply Bank TDR to transactions by running calculateBankTDR.php
		Browser.navigateToURL(testConfig, "http://cron.payu.in/");
		WebElement calculateBankTDRCron = Element.getPageElement(testConfig, How.linkText, "calculateBankTdr.php");
		Element.click(testConfig, calculateBankTDRCron, "Calculate Bank TDR Cron Link");
		Browser.wait(testConfig, 20);
		
	}	
}
