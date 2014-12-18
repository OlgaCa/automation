package PageObject.AdminPanel.MerchantList.MerchantDetails;

import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class EditMerchantRiskProfilePage extends EditMerchantPage{

	Config testConfig;

	@FindBy(name="merch_category")
	protected WebElement merchant_category;

	@FindBy(name="domain")
	protected WebElement international_domain;

	@FindBy(name="merch_ownership")
	protected WebElement merchant_ownership;

	@FindBy(name="age")
	protected WebElement age;

	@FindBy(name="balance_sheet")
	protected WebElement size_balancesheet;

	@FindBy(name="profitable")
	protected WebElement profitable;

	@FindBy(name="manual_risk_perception")
	protected WebElement manual_risk_perception;

	@FindBy(css= "div[name='merchant_risk']")
	protected WebElement submit;

	public EditMerchantRiskProfilePage(Config testConfig) {
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,submit );	
	}

	public void editRiskProfile(int riskProfileRow)
	{
		TestDataReader data = new TestDataReader(testConfig,"RiskProfile");
		Element.selectValue(testConfig, merchant_category, data.GetData(riskProfileRow, "merchantCategory"), "merchant category");
		Element.click(testConfig, submit, "Submit the query");
		Element.click(testConfig,risk_profile , "risk_profile tab");
	}
}
