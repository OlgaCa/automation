package PageObject.AdminPanel.MerchantCategories;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.TestDataReader;

public class CategoryRulePage{

	private Config testConfig;
	
	@FindBy(linkText="Test Transaction")
	private WebElement testTransaction;
	
	@FindBy(css="[id='challengeOnRisk'][value='challenge']")
	private WebElement challangeRule;
	
	@FindBy(css="[id='denyOnRisk'][value='RedShield']")
	private WebElement redShield;
	
	@FindBy(css="[id='denyOnRisk'][value='Sentropi']")
	private WebElement sentropi;
	
	@FindBy(name="basicRule[Category]")
	private WebElement category;
	
	@FindBy(css="[id='denyOnRisk'][value='deny']")
	private WebElement deny_transaction;

	@FindBy(id="countAttempts")
	private WebElement count_attempts;

	@FindBy(css="[type='radio'][value='both']")
	private WebElement applyToBoth;

	@FindBy(id="ruleName")
	private WebElement rule_name;

	@FindBy(id="ruleCheck")
	private WebElement check_type;

	@FindBy(id="ruleParam")
	private WebElement parameter;

	@FindBy(id="ruleforParam")
	private WebElement for_parameter;

	@FindBy(id="ruleLimit")
	private WebElement Limit;

	@FindBy(id="intervalUnit")
	private WebElement time_unit;

	@FindBy(id="timeRange")
	private WebElement time_range;

	@FindBy(id="description")
	private WebElement description;

	@FindBy(css="[type='submit'][value='submit']")
	private WebElement submit;


	public CategoryRulePage(Config testConfig){
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, category);
	}
	
	
	public String[] CreateCategroyRule(int row, int categoryRow, int whichRule, String RuleSheet)
	{
		switch(whichRule){
			case 1:
				Element.click(testConfig, deny_transaction, "click on deny radio button");
				break;
				
			case 2:
				Element.click(testConfig, challangeRule, "click on challenge radio button");
				break;
				
			case 3:
				Element.click(testConfig, redShield, "click on deny radio button");
				break;
				
			case 4:
				Element.click(testConfig, sentropi, "click on deny radio button");
				break;		
		}
		
		String values[] = new String[4];

		TestDataReader TDR = new TestDataReader(testConfig, RuleSheet);
		TestDataReader data = new TestDataReader(testConfig,"RiskProfile");
	
		Element.selectVisibleText(testConfig, category, data.GetData(categoryRow, "merchantCategory"), "merchant category");
	//	Element.selectValue(testConfig, category, "Travel", "Rule Category");
		Element.click(testConfig, count_attempts, "Attempt Counts");
		Element.click(testConfig, applyToBoth, "Apply to Both");
		
			
		String ruleName = TDR.GetData(row, "Rule").toString();
		values[0]= ruleName;
		
		String Desc = TDR.GetData(row, "description").toString();
		
		values[1]= Desc;
		Element.enterData(testConfig, rule_name, ruleName, "Rule Name");
		
		Element.selectValue(testConfig, check_type, TDR.GetData(row, "check_type"), "Maximum Count check");
		Element.selectValue(testConfig, parameter, TDR.GetData(row, "parameter"), "Parameter");

		if(TDR.GetData(row, "for_parameter")!=null){
		Element.selectValue(testConfig, for_parameter, TDR.GetData(row, "for_parameter"), "For Parameter");
		}
		
		String count  = testConfig.getRunTimeProperty("count");
		
		if(count!=null){
			int limit = Integer.parseInt(count) + 2;
			Element.enterData(testConfig, Limit, String.valueOf(limit) , "Threshold");
		}
		else{
		Element.enterData(testConfig, Limit, TDR.GetData(row, "Limit") , "Threshold");
		}
		
		Element.selectValue(testConfig, time_unit, TDR.GetData(row, "time_unit"),  "Time Unit");
		Element.enterData(testConfig, time_range, TDR.GetData(row, "time_range") , "Time Range");
		Element.enterData(testConfig, description, Desc , "description");

		Element.click(testConfig, submit, "Submit Button");
		return values;
	}
	
	
}