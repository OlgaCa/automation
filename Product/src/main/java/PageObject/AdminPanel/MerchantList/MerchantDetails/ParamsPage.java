package PageObject.AdminPanel.MerchantList.MerchantDetails;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;

public class ParamsPage extends MerchantDetailsPage{

	private Config testConfig;
	
	@FindBy(linkText="Merchant Params")
	protected WebElement merchant_params;
	
	@FindBy(linkText="Override for Pg")
	protected WebElement override_for_pg;
	
	@FindBy(linkText="Override For Option")
	protected WebElement override_for_options;
	
	@FindBy(linkText="Override For Gateway")
	protected WebElement override_for_gateway;
	
	@FindBy(name="key")
	private WebElement paramKey;
	
	@FindBy(name="value")
	private WebElement paramValue; 
	
	@FindBy(linkText="Test Merchant Transaction")
	protected WebElement test_transaction;
	
	@FindBy(linkText="Home")
	WebElement home_link;
	
	public ParamsPage(Config testConfig) {
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,merchant_params );	
	}
	
	/* @return Object of MerchantParams*/
	public ParamsMerchantParamsPage clickMerchantParams(){
		Element.click(testConfig, merchant_params, "Merchant Params tab");
		return new ParamsMerchantParamsPage(testConfig);
	}
	
	/* @return Object of OverrideForPg*/	
	public ParamsOverrideForPgPage clickOverrideForPg(){
		Element.click(testConfig, override_for_pg, "Override for pg tab");
		return new ParamsOverrideForPgPage(testConfig);
	}
	
	/* @return Object of OverrideForOption*/	
	public ParamsOverrideForOptionPage clickOverrideForOptions(){
		Element.click(testConfig, override_for_options, "Override for Options tab");
		return new ParamsOverrideForOptionPage(testConfig);
	}
	
	/* @return Object of OverrideForGateway*/	
	public ParamsOverrideForGatewayPage clickOverrideForGateway(){
		Element.click(testConfig, override_for_gateway, "Override for Gateway tab");
		return  new ParamsOverrideForGatewayPage(testConfig);
	}
	
}
