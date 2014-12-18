package PageObject.NewMerchantPanel.Billings;

import org.openqa.selenium.support.PageFactory;

import Utils.Config;

public class BillingsPage {
	
	private Config testConfig;

	public BillingsPage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		
	}

}
