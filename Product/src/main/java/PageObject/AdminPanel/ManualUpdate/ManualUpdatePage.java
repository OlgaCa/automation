package PageObject.AdminPanel.ManualUpdate;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Home.HomePage;
import Utils.Config;
import Utils.Element;

public class ManualUpdatePage {

	private Config testConfig;

	@FindBy(css="a[href*='merchantsettlement']")
	private WebElement merchantSettlement;

	@FindBy(css="a[href='#refund']")
	private WebElement refund;
	
	@FindBy(linkText="Recon")
	private WebElement recon;
	
	@FindBy(linkText="Chargeback")
	private WebElement chargebackTab;


	public ManualUpdatePage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);	
	}

	public MerchantSettlement ClickMerchantSettlement() {
		HomePage home = new HomePage(testConfig);
		GoToMerchantSettlement(home);
		Element.click(testConfig, merchantSettlement, "Click on Merchant Settlement");
		return new MerchantSettlement(testConfig);
	}

	public void GoToMerchantSettlement(HomePage home)
	{
		home.navigateToAdminHome();
		home.ClickManualTransactionUpdate();
	}

	public ReconPage ClickRecon() {
		Element.click(testConfig, recon, "Click on Recon Tab");
		return new ReconPage(testConfig);
	}

	public void GoToReconPage(HomePage home)
	{
		home.navigateToAdminHome();
		home.ClickManualTransactionUpdate();		
	}
	/**
	 * Takes to refund page
	 * @return 
	 */
	public RefundTab goToRefundTab()
	{
			Element.click(testConfig, refund, "Refund Tab");
			return new RefundTab(testConfig);
	}

	/**
	 *Takes to chargeback page 
	 * @return 
	 */
	public ChargebackTab clickOnChargebackTab() {
		Element.click(testConfig, chargebackTab, "Chargeback Tab");
		return new ChargebackTab(testConfig);
		
	}
}
