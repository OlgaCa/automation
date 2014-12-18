package PageObject.NewMerchantPanel.Customization;

import org.openqa.selenium.support.PageFactory;

import Utils.Config;

public class PaymentFlowPage {

	private Config testConfig;

	public PaymentFlowPage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		
	}
}
