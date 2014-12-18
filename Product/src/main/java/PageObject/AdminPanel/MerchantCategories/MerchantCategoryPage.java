package PageObject.AdminPanel.MerchantCategories;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;

public class MerchantCategoryPage{
	
	private Config testConfig;
	
	@FindBy(linkText="Travel")
	private WebElement travel;
	
	@FindBy(linkText="Apparel")
	private WebElement apparel;
	
	@FindBy(linkText="Electronics")
	private WebElement electronics ;
	
	@FindBy(linkText="Games")
	private WebElement games;
	
	@FindBy(linkText="Digital product/services")
	private WebElement digital;
	
	@FindBy(linkText="Other")
	private WebElement other;
	 
	@FindBy(linkText="Add Transaction based Rule to Category")
	private WebElement Category_rule;
	
	public MerchantCategoryPage(Config testConfig){
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, Category_rule);
	}
	
	public CategoryRulePage clickAddTxnRuleToCategory(Config testConfig){
		Element.click(testConfig, Category_rule, "clicking on Add Transaction based Rule to Category ");
		return new CategoryRulePage(testConfig);
	}
	
	
	public CategoryPage clickOnCategory(Config testConfig, int riskProfile){
		
		switch(riskProfile){
		
		case 1:
		Element.click(testConfig, travel, "click on travel");
		
		break;
		
		case 2:
			Element.click(testConfig, apparel, "click on apparel");
			break;
			
		case 3:
			Element.click(testConfig, electronics, "click on electronics");
			break;
			
		case 4:
			Element.click(testConfig, games, "click on games");
			break;
			
		case 5:
			Element.click(testConfig, digital, "click on digital");
			break;
			
		case 6:
			Element.click(testConfig, other, "click on others");
			break;
		}
		return new CategoryPage(testConfig);
	}
	
}