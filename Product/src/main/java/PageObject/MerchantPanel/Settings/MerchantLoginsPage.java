package PageObject.MerchantPanel.Settings;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;

public class MerchantLoginsPage {
	
	private Config testConfig;
	public EditMerchantLoginPage editMerchantLoginPage;
	
	//private WebElement getPageElement(xpath="//td[.='akhtar']/..//a");		
	@FindBy(xpath="//td[.='akhtar']/..//a")
	private WebElement editLink;
	
	@FindBy(linkText="Next")
	private WebElement nextLink;
	
	@FindBy(css="div.fiLt.login")
	private WebElement createLogin;
	
		public MerchantLoginsPage(Config testConfig)
		{
			this.testConfig = testConfig;
			PageFactory.initElements(this.testConfig.driver, this);
			Browser.waitForPageLoad(this.testConfig, createLogin);
		}
		
		public void nextPage() {
			ClickEditLink();
			
		}
		
		public EditMerchantLoginPage ClickEditLink() {
			String name = "//td[.='";
			name += testConfig.getRunTimeProperty("name");
			name += "']/..//a";
			WebElement edit = Element.getPageElement(testConfig, How.xPath, name);
			if(Element.IsElementDisplayed(testConfig, edit)) {
				Element.click(testConfig, edit, "Click on Edit Link");
				Browser.wait(testConfig, 2);
			}	else {
				if(Element.IsElementDisplayed(testConfig, nextLink)) {
					Element.click(testConfig, nextLink, "Click on Next Link");
					nextPage();
				
					} 
			}
			
			return new EditMerchantLoginPage(testConfig);
		}
		
		

}
