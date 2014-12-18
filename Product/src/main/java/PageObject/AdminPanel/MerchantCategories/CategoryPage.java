package PageObject.AdminPanel.MerchantCategories;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;


public class CategoryPage{
	
	private Config testConfig;
	
	@FindBy(className="selectForToggling")
	private WebElement selectBox;
	
	@FindBy(xpath="//tbody/tr[4]/td[2]")
	private WebElement rule_id;
	
	@FindBy(id ="combinedRuleFormSubmit")
	private WebElement select_button;

//	@FindBy(xpath="//tbody/tr[4]/td[5]/input")
	private WebElement select_checkbox;
	
	private WebElement select_box;
	//@FindBy(xpath="//tbody/tr[4]/td[6]/input")
	//private WebElement delete_button;
	
	@FindBy(css="[type='submit'][value='Delete']")
	private WebElement delete_button;
	
	public CategoryPage(Config testConfig)
	{
		//super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
	//	Browser.waitForPageLoad(this.testConfig, refundButton);
	}
	
	public void DeleteExistingRules(){
	//	delete_button = Element.getPageElement(testConfig, How.xPath, "//tbody/tr[4]/td[6]/input");
	//	select_checkbox = Element.getPageElement(testConfig, How.xPath, "//tbody/tr[4]/td[5]/input");
			while(Element.IsElementDisplayed(testConfig,delete_button)){
				Element.click(testConfig, selectBox, "Clicking Active Checkbox");
				Element.click(testConfig, delete_button, "Deleting Existing Rules");
			//	delete_button = Element.getPageElement(testConfig, How.xPath, "//tbody/tr[4]/td[6]/input");
			//	select_checkbox = Element.getPageElement(testConfig, How.xPath, "//tbody/tr[4]/td[5]/input");
			}
	}
	
	public String SelectRule(){
		
	//select_box = Element.getPageElement(testConfig, How.xPath, "//tbody/tr[4]/td[5]/input");
	Element.click(testConfig, selectBox, "Clicking Active Checkbox");
	Element.click(testConfig, select_button, "Selecting a Rule");
	Element.verifyElementPresent(testConfig, rule_id, "Get Rule ID");
	String ruleid=rule_id.getText().toString(); 
	return ruleid;
	}
}