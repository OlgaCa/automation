package PageObject.AdminPanel.MerchantList.MerchantDetails;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;

public class EditMerchantPage extends MerchantDetailsPage{

	private Config testConfig;

	@FindBy(linkText="Risk Profile")
	protected WebElement risk_profile;

	@FindBy(linkText="Edit")
	protected WebElement edit;

	@FindBy(linkText="Misc")
	protected WebElement misc;


	public EditMerchantPage(Config testConfig) {
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,risk_profile );	
	}

	public EditMerchantRiskProfilePage clickRiskProfile(){
		Element.click(testConfig,risk_profile , "risk_profile tab");
		return new EditMerchantRiskProfilePage(testConfig);
	}
	
	public EditPage clickEdit(){
		Element.click(testConfig,edit , "edit tab");
		return new EditPage(testConfig);
	}

}
