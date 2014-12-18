package PageObject.AdminPanel.MerchantList.MerchantDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class TDRList extends MerchantDetailsPage{
	
Config testConfig;
	
	@FindBy(id="category")
	private  WebElement category;
	
	@FindBy(xpath="//div[@id='editTDR']/form/select[2]")
	private WebElement payment_option;
	
	
	@FindBy(xpath="//div[@id='editTDR']/form/select[3]")
	private WebElement gateways;
	
	@FindBy(xpath="//div[@id='editTDR']/form/select[4]")
	private WebElement card_type;
	
	@FindBy(name="range")
	private WebElement amount;
		
	@FindBy(name="percentFee")
	private WebElement fee;
	
	@FindBy(name="flatFee")
	private WebElement flatFee;

	
	@FindBy(name="TDR")
	private WebElement add;

	@FindBy(linkText="Edit TDR")
	private WebElement edit_tdr_list;

	@FindBy(linkText="View TDR List")
	private WebElement view_tdr_list;

	//  Elements on View TDR List Page
	@FindBy(xpath="//*[@id=\"viewTDR\"]/div/h4/strong")
	private WebElement view_category;

	@FindBy(xpath="//*[@id=\"viewTDR\"]/div/div/h4")
	private WebElement view_paymentOption;

	@FindBy(xpath="//*[@id=\"viewTDR\"]/div/div/table/tbody/tr[2]/td[1]")
	private WebElement view_gateway;
	
	@FindBy(xpath="//*[@id=\"viewTDR\"]/div/div/table/tbody/tr[2]/td[2]")
	private WebElement view_minAmount;
	
	@FindBy(xpath="//*[@id=\"viewTDR\"]/div/div/table/tbody/tr[2]/td[3]")
	private WebElement view_feePer;
	
	@FindBy(xpath="//*[@id=\"viewTDR\"]/div/div/table/tbody/tr[2]/td[4]")
	private WebElement view_feeFlat;
	
	@FindBy(xpath="//*[@id=\"viewTDR\"]/div/div/table/tbody/tr[2]/td[5]")
	private WebElement view_cardType;
	
	//Remove TDR
	@FindBy(xpath="//*[@id=\"editTDR\"]/div/table/tbody/tr[2]/td[5]/form/input[6]")
	private WebElement removeTDR;

	
	
	

	public TDRList(){
		
	}
	
	public TDRList(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,add );	

	}
	
	public void addParameters(int RowNo)
	{
		
		Element.click(testConfig, edit_tdr_list, "Click on Edit TDR List");
		
		TestDataReader data = new TestDataReader(testConfig,"TDR");

		String value = "";

		value = data.GetCurrentEnvironmentData(RowNo, "Category");
		Element.selectVisibleText(testConfig, category, value, "Category");
		
		value = data.GetCurrentEnvironmentData(RowNo, "Payment Option");
		Element.selectVisibleText(testConfig, payment_option, value, "Payment Option");
		
		value = data.GetCurrentEnvironmentData(RowNo, "Gateway");
		Element.selectVisibleText(testConfig, gateways, value, "Add gateway");
		
		value = data.GetCurrentEnvironmentData(RowNo, "Card Type");
		Element.selectValue(testConfig, card_type, value, "Add Key");
		
		value = data.GetData(RowNo, "Amount(min)");
		Element.enterData(testConfig, amount, value, "Range");
		
		value = data.GetData(RowNo, "Fee (in %)");
		Element.enterData(testConfig, fee, value, "Fee");
		
		value = data.GetData(RowNo, "Flat Fee");
		Element.enterData(testConfig, flatFee, value, "flatFee");
		
		
		Element.click(testConfig, add, "Add the Parameters of TDR");
				
	}

	public void verifyTDR(int RowNo)
	{
		TestDataReader data = new TestDataReader(testConfig,"TDR");
		
		Element.click(testConfig, view_tdr_list, "Click on View TDR List");
		
		Element.click(testConfig, view_category, "Click on Category");

		Helper.compareEquals(testConfig, "category", data.GetCurrentEnvironmentData(RowNo, "Category"), view_category.getText());
		
		Helper.compareEquals(testConfig, "payment_option", data.GetCurrentEnvironmentData(RowNo, "Payment Option"), view_paymentOption.getText());
		
		Helper.compareEquals(testConfig, "Gateway", data.GetCurrentEnvironmentData(RowNo, "Gateway"), view_gateway.getText());
		
		Helper.compareEquals(testConfig, "Card Type", data.GetCurrentEnvironmentData(RowNo, "Card Type"), view_cardType.getText());
		
		Helper.compareEquals(testConfig, "Amount(min)", data.GetCurrentEnvironmentData(RowNo, "Amount(min)"), view_minAmount.getText());
		
		Helper.compareEquals(testConfig, "Fee (in %)", data.GetCurrentEnvironmentData(RowNo, "Fee (in %)"), view_feePer.getText());
		
		Helper.compareEquals(testConfig, "Flat Fee", data.GetCurrentEnvironmentData(RowNo, "Flat Fee"), view_feeFlat.getText());				
	}
	
	public void removeTDR(Config testConfig){
		Element.click(testConfig, edit_tdr_list, "Click on Edit TDR List");
		
		Element.click(testConfig, removeTDR, "Remove a TDR rule");
	}
	
	public void verifyTDRnotPresent(Config testConfig){
		
		Element.click(testConfig, view_tdr_list, "Click on View TDR List");
		
		Element.click(testConfig, view_category, "Click on Category");
		int size=testConfig.driver.findElements(By.xpath("//*[@id=\"viewTDR\"]/div/div/table/tbody/tr[2]/td[1]")).size();
		Helper.compareEquals(testConfig, "TDR rule not present-Size of TDR rule", size,0);
				
	}
		
}
