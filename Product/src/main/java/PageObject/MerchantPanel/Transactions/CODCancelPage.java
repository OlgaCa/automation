package PageObject.MerchantPanel.Transactions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;

public class CODCancelPage extends TransactionFilterPage{
	
	
private Config testConfig;
	
	@FindBy(id="codCancel")
	private WebElement codCancelButton;
	
	
	
	public CODCancelPage(Config testConfig)
	{
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, codCancelButton);
	}

}
