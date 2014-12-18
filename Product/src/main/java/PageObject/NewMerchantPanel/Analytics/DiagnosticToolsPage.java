package PageObject.NewMerchantPanel.Analytics;

import org.openqa.selenium.support.PageFactory;

import Utils.Config;

public class DiagnosticToolsPage {

	private Config testConfig;

	public DiagnosticToolsPage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		
	}
}

