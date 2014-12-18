package PageObject.NewMerchantPanel.Customization;

import org.openqa.selenium.support.PageFactory;

import Utils.Config;

public class IframePage {

	private Config testConfig;

	public IframePage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		
	}
}
