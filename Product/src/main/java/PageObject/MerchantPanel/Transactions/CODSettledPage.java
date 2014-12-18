package PageObject.MerchantPanel.Transactions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;

public class CODSettledPage extends TransactionFilterPage {

	
private Config testConfig;
	
	@FindBy(id="codSettled")
    //@FindBy(xpath="//input[@id='codSettled']")
	private WebElement codSettleButton;
	
	
	
	public CODSettledPage(Config testConfig)
	{
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, codSettleButton);
	}
}
