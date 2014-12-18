package PageObject.NewMerchantPanel.Notifications;

import org.openqa.selenium.support.PageFactory;

import Utils.Config;

public class OtherNotificationsPage {

	private Config testConfig;

	public OtherNotificationsPage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		
	}
}
