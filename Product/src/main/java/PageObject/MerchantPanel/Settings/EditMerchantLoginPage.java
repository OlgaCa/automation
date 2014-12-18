package PageObject.MerchantPanel.Settings;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.MerchantPanel.Transactions.InvoiceTransactionConfirmationPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.TestDataReader;
import Utils.Element.How;

public class EditMerchantLoginPage {
	private Config testConfig;
	
	
	@FindBy(css="img#change_passwd")
	private WebElement changePasswd;
	
	@FindBy(css="img.delete")
	private WebElement delete;
	
	@FindBy(id="action_button")
	private WebElement actionButton;
	
	@FindBy(id="edited")
	private WebElement save;
	
	@FindBy(name="active")
	private WebElement active;
	
	@FindBy(xpath="//form[@id='edit_form']/div/table/tbody/tr/td[3]/table/tbody/tr/td/input")
	private WebElement viewTrans;
	
	@FindBy(xpath="xpath=(//input[@name='role[]'])[2]")
	private WebElement capture;
	
	@FindBy(xpath="(//input[@name='role[]'])[3]")
	private WebElement refund;
	
	@FindBy(xpath="(//input[@name='role[]'])[4]")
	private WebElement cancelTrans;
	
	@FindBy(xpath="(//input[@name='role[]'])[5]")
	private WebElement viewDboard;
	
	@FindBy(xpath="(//input[@name='role[]'])[6]")
	private WebElement createOffer;
	
	@FindBy(xpath="(//input[@name='role[]'])[7]")
	private WebElement changePwd;
	
	@FindBy(xpath="(//input[@name='role[]'])[8]")
	private WebElement ivrTrans;
	
	@FindBy(xpath="(//input[@name='role[]'])[9]")
	private WebElement billings;
	
	@FindBy(xpath="(//input[@name='role[]'])[10]")
	private WebElement codVerify;
	
	@FindBy(xpath="(//input[@name='role[]'])[11]")
	private WebElement codSettle;
	
	@FindBy(xpath="(//input[@name='role[]'])[13]")
	private WebElement exportExcel;
	
	//"xpath="//td[3]/table/tbody/tr/td"
	
	@FindBy(xpath="//input[@type='checkbox'])[position()=2]")
	private WebElement viewTransaction;
	
	public void clickSaveButton() {
		Element.click(testConfig, save, "Click on Save");

	}
	
	public void clickDeleteButton() {
		Element.click(testConfig, delete, "Delete Login");
		if(Element.IsElementDisplayed(testConfig,Element.getPageElement(testConfig, How.id, "action_button")))
		{
			Element.click(testConfig, actionButton, "Click on yes");
		}

	}
		public EditMerchantLoginPage(Config testConfig)
		{
			Browser.wait(testConfig, 2);
			this.testConfig = testConfig;
			PageFactory.initElements(this.testConfig.driver, this);
			Browser.waitForPageLoad(this.testConfig, changePasswd);
		}
		
		public void MarkActiveCheckbox(){
			
			Element.uncheck(testConfig, active, "Mark Active/Deactive ");
		}
		
		public void AssignRoles() {   	
		     Element.check(testConfig, viewTrans, "Assign view Transaction ");  
		     Element.check(testConfig, refund, "Assign Refund ");
		     Element.check(testConfig, cancelTrans, "Assign cancel transaction ");
		     Element.check(testConfig, viewDboard, "Assign view Dashboard ");
		     Element.check(testConfig, codSettle, "Assign COD settle ");
		     Element.check(testConfig, codVerify, "Assign COD verify ");
		     Element.check(testConfig, exportExcel, "Assign Export to excel");
		 }  
		 public void RevokeRole() {   	
		     Element.uncheck(testConfig, exportExcel, "Assign Export to excel");
		 }      
		
		// Create Login through login form
					// Entering data from Excel sheet "UserLoginDetails"
				/*	public TestDataReader fillEditLoginForm(int loginRowNum) {
						TestDataReader userLoginDetails= new TestDataReader(testConfig,
								"TransactionDetails");
						String value = "";

						value = userLoginDetails.GetData(loginRowNum, "key");
						//String value="mariam";
						Element.enterData(testConfig, merchantName, value, "Enter name");
						//String value1="mariam";
						
						value = userLoginDetails.GetData(loginRowNum, "key");
						Element.enterData(testConfig, userName, value, "Enter username");
						//String value2="mariam1";
						
						value = userLoginDetails.GetData(loginRowNum, "key-PPHotfix");
						Element.enterData(testConfig, passworD, value, "Enter password");
						//String value3="mariam1";
						
						value = userLoginDetails.GetData(loginRowNum, "key-PPHotfix");
						Element.enterData(testConfig, confirmPassword, value, "Enter confirm password");

						return userLoginDetails;
					}
					
					*/

	

}
