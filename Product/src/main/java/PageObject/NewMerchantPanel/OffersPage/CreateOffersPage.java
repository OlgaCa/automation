package PageObject.NewMerchantPanel.OffersPage;

import org.openqa.selenium.support.PageFactory;

import Utils.Config;

public class CreateOffersPage {

	private Config testConfig;

	public CreateOffersPage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		
	}
}
