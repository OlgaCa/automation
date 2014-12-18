package PageObject.AdminPanel.MerchantList;

/*
 * Risk Rules Page
 * @author Himanshu
 */


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Config;
import Utils.Element;
import Utils.TestDataReader;


public class RulePage {
	
	private Config testConfig;
	
	@FindBy(xpath="//tbody/tr[4]/td[2]")
	private WebElement rule_id;
	
	@FindBy(className="selectForToggling")
	private WebElement select_checkbox;
	
	@FindBy(id ="combinedRuleFormSubmit")
	private WebElement select_button;
	
	@FindBy(css="[id='denyOnRisk'][value='deny']")
	private WebElement deny_transaction;
	
	@FindBy(css="[id='challengeOnRisk'][value='challenge']")
	private WebElement challangeRule;
	
	@FindBy(css="[id='denyOnRisk'][value='RedShield']")
	private WebElement redShield;
	
	@FindBy(css="[id='denyOnRisk'][value='Sentropi']")
	private WebElement sentropi;
	
	@FindBy(id="countAttempts")
	private WebElement count_attempts;
	
	@FindBy(css="[type='radio'][value='both']")
	private WebElement applyToBoth;
	
	@FindBy(css="[type='radio'][value='international']")
	private WebElement international_rdnButton;
	
	@FindBy(css="[type='radio'][value='domestic']")
	private WebElement domestic_rdnButton;
		
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
	
	@FindBy(css="[type='submit'][value='Delete']")
	private WebElement delete_button;
	
	@FindBy(css="[type='radio'][value='merchant']")
	private WebElement merchantScope_rdnButton;
	
	@FindBy(css="[type='radio'][value='category']")
	private WebElement categoryScope_rdnButton;
	
	@FindBy(css="[type='radio'][value='global']")
	private WebElement globalScope_rdnButton;
	
	RulePage(Config testConfig){
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
	}
	
	
	public String[] CreateRule(int row ,int whichRule, String RuleSheet)
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

		Element.click(testConfig, count_attempts, "Attempt Counts");
		
		if(TDR.GetData(row,"Cardtype").equals("Both")){
			Element.click(testConfig, applyToBoth, "Apply to Both");
		}
		else if (TDR.GetData(row, "Cardtype").equals("domestic")){
			Element.click(testConfig, domestic_rdnButton, "Apply to Domestic Cards only");
		}
		else if (TDR.GetData(row, "Cardtype").equals("international")){
			Element.click(testConfig, international_rdnButton, "Apply to International Cards only");
		}
						
		if(TDR.GetData(row, "Scope").equals("Merchant")){
			Element.click(testConfig, merchantScope_rdnButton, "Merchant Scope");
		}
		else if(TDR.GetData(row, "Scope").equals("Category")){
			Element.click(testConfig, categoryScope_rdnButton, "Category Scope");			
		}
		else if(TDR.GetData(row, "Scope").equals("Global")){
			
			Element.click(testConfig, globalScope_rdnButton, "Global Scope");
		}
			
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
	
	public void DeleteExistingRules(){

		while(Element.IsElementDisplayed(testConfig,delete_button)){
	Element.click(testConfig, select_checkbox, "Clicking Active Checkbox");
	Element.click(testConfig, delete_button, "Deleting Existing Rules");
	}
		
}
	
	
	public String SelectRule(){
	
	Element.click(testConfig, select_checkbox, "Clicking Active Checkbox");
	Element.click(testConfig, select_button, "Selecting a Rule");
	Element.verifyElementPresent(testConfig, rule_id, "Get Rule ID");
	String ruleid=rule_id.getText().toString(); 
	return ruleid;
	}
	
}