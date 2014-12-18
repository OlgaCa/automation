package PageObject.AdminPanel.MerchantList.MerchantDetails;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;

public class PGKeysPage {

	
	private Config testConfig;
	
	@FindBy(xpath="//div[@id='tabs2']/h3")
	private WebElement pggatewayText;
	
	@FindBy(id="add_new_key")
	private WebElement selectGatewayDD;
	
	@FindBy(xpath="//div[@id='key_select_div']/select")
	private WebElement selectKeyDD;
	
	@FindBy(xpath="//div[@id='key_select_div']/input")
	private WebElement keyValueTextBox;
	
	@FindBy(css="#tabs2 > form > input[name=\"pgKeyChange\"]")
	private WebElement addButton;
	
	public PGKeysPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, pggatewayText);
		
	}
	
	public void addPGKey(String pgId, String key, String value)
	{
		Element.selectValue(testConfig, selectGatewayDD, pgId, "gateway value");
		Browser.wait(testConfig, 1);
		Element.selectValue(testConfig, selectKeyDD, key, "key");
		Element.enterData(testConfig, keyValueTextBox, value, "key value");
		Element.click(testConfig, addButton, "Add PG Key");
	}
}
