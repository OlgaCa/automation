package PageObject.AdminPanel.MerchantList;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import Utils.Browser;
import Utils.Element;
import Utils.Config;
import Utils.TestDataReader;

public class MerchantGatewayPage {

	private Config testConfig;

	@FindBy(id="category_list")
	private WebElement selectCategory;

	@FindBy(xpath="//div[@id='pg_select_div']/select")
	private WebElement selectPaymentGateway;

	@FindBy(xpath="//div[@id='pt_select_div']/select")
	private WebElement selectPaymentOption;

	@FindBy(css="input[name='addOption']")
	private WebElement addGateway;

	@FindBy(id="add_new_key")
	private WebElement selectGateway;

	@FindBy(name="mKeys[]")
	private WebElement selectGatewayKey;

	@FindBy(name="mValues[]")
	private WebElement GatewayKeyValue;

	@FindBy(name="changeMerchantKey")
	private WebElement saveGatewayKey;

	public TestDataReader MerchantGateway;
	public TestDataReader MerchantGatewayKey;

	MerchantGatewayPage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, selectCategory);
		MerchantGateway = new TestDataReader(testConfig,"MerchantGateway");
		MerchantGatewayKey = new TestDataReader(testConfig,"MerchantGatewayKey");
	}

	/**
	 * Reads the "MerchantGateway" sheet of Product Test Data excel and adds the Merchant Gateway
	 * @param rowNum Row number of excel sheet for the gateway to be added
	 */
	public void AddGateway(int merchantGatewayRow)
	{ 
		String value = "";
		value = MerchantGateway.GetData(merchantGatewayRow, "Category");
		Element.selectValue(testConfig, selectCategory, value, "Payment Category");

		value = MerchantGateway.GetData(merchantGatewayRow, "PaymentGateway");
		Element.selectVisibleText(testConfig, selectPaymentGateway, value, "Payment Gateway");

		value = MerchantGateway.GetData(merchantGatewayRow, "PaymentOption");
		Element.selectVisibleText(testConfig, selectPaymentOption, value, "Payment Option");

		Element.click(testConfig, addGateway, "Add Gateway");
	}

	/**
	 * Reads the "MerchantGatewayKey" sheet of Product Test Data excel and add the specified Merchant Gateway Key and value
	 * @param rowNum Row number of excel sheet for the gateway key to be added
	 */
	public void AddGatewayKey(int merchantGatewayKeyRow)
	{ 
		String value = "";

		value = MerchantGatewayKey.GetData(merchantGatewayKeyRow, "Gateway");
		Element.selectVisibleText(testConfig, selectGateway, value, "Gateway");

		value = MerchantGatewayKey.GetData(merchantGatewayKeyRow, "Key");
		Element.selectVisibleText(testConfig, selectGatewayKey, value, "Gateway Key");

		value = MerchantGatewayKey.GetData(merchantGatewayKeyRow, "Value");
		Element.selectVisibleText(testConfig, GatewayKeyValue, value, "Gateway Key Value");

		Element.click(testConfig, saveGatewayKey, "Save Gateway Key Value");
	}

}
