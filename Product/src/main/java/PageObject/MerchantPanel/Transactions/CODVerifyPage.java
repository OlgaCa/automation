package PageObject.MerchantPanel.Transactions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.MerchantPanel.Home.DashboardPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;

public class CODVerifyPage extends TransactionFilterPage {
	
	private Config testConfig;
	
	@FindBy(id="codVerify")
	private WebElement codVerifyButton;
	
	@FindBy(name="transactions[1]")
	private WebElement transCheckbox;
	
	
	
	public CODVerifyPage(Config testConfig)
	{
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, codVerifyButton);
	}
	
	public void verifyCODFirstTransaction()
	{   
		Element.check(testConfig, transCheckbox, "Click on check box");
		Element.click(testConfig, codVerifyButton, "Click on Capture Button");
		RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
		confirm.ClickActionButton();
		
		
	}
	
	public DashboardPage clickCODVerifyButton()
	{
		Element.click(testConfig, codVerifyButton, "click cod verify");
		return new DashboardPage(testConfig);
		
	}
	

}
