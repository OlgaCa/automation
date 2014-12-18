package PageObject.NewMerchantPanel.Activity;

import org.openqa.selenium.support.PageFactory;

import Utils.Config;

public class ActivityPage {

	private Config testConfig;

	public ActivityPage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		
	}
}
