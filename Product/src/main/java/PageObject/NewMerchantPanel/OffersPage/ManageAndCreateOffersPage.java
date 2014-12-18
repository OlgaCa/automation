package PageObject.NewMerchantPanel.OffersPage;

import org.openqa.selenium.support.PageFactory;

import Utils.Config;

public class ManageAndCreateOffersPage {

	private Config testConfig;

	public ManageAndCreateOffersPage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		
	}
}
