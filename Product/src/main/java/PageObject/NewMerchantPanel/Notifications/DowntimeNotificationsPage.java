package PageObject.NewMerchantPanel.Notifications;

import org.openqa.selenium.support.PageFactory;

import Utils.Config;

public class DowntimeNotificationsPage {

	private Config testConfig;

	public DowntimeNotificationsPage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		
	}
}
