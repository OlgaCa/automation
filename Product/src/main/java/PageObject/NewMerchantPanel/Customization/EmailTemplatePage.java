package PageObject.NewMerchantPanel.Customization;

import org.openqa.selenium.support.PageFactory;

import Utils.Config;

public class EmailTemplatePage {

	private Config testConfig;

	public EmailTemplatePage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		
	}
}
