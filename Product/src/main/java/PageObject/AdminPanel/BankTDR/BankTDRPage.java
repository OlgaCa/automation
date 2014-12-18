package PageObject.AdminPanel.BankTDR;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Config;
import Utils.Element;

public class BankTDRPage {

	private Config testConfig;
	
	@FindBy (linkText="Edit Bank TDR")
	private WebElement lnkEditBankTDR;
	
	@FindBy (linkText="Download Bank TDR Reports")
	private WebElement lnkDownloadBankTDRReports;
	
	public BankTDRPage(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
	}
	
	public EditBankTDRPage ClickEditBankTDR()
	{
		Element.click(testConfig, lnkEditBankTDR, "Test Edit Bank TDR Link");
		return new EditBankTDRPage(testConfig);
	}
	
	
}
