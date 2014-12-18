package PageObject.MerchantPanel.Settings;

import java.awt.AWTException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class SetLookPage {
	private Config testConfig;
	
	@FindBy(css="button")
	private WebElement Preview;
	
	@FindBy(name="nclass")
	private WebElement namE;
	
	@FindBy(name="refresh")
	private WebElement reset;
	
	@FindBy(name="content")
	private WebElement contenT;
	
	@FindBy(name="active")
	private WebElement submitQuery;
	
	@FindBy(name="addclass")
	private WebElement addClassDD;
	
	@FindBy(xpath="(//select[@name='addclass'])[2]")
	private WebElement amountdiv;
	
	@FindBy(xpath="(//input[@value='Add'])[2]")
	private WebElement submitCss;
	
	@FindBy(name="changetolive")
	private WebElement changetolive;
	
	@FindBy(xpath="(//input[@name='active'])[3]")
	private WebElement Deactivate;
	
	@FindBy(xpath="(//input[@name='remove'])[2]")
	private WebElement remove;
	
	public SetLookPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, contenT);
	}
	
	public TestDataReader fillCssForm(int offerRowNum) {


		TestDataReader offerData = new TestDataReader(testConfig,"OfferDetails");
		String value = "";
		
		value = offerData.GetData(offerRowNum, "Title");
		Element.enterData(testConfig, namE, value,"offer description");
		testConfig.putRunTimeProperty("cssName", value);

		value = offerData.GetData(offerRowNum, "Description");
		Element.enterData(testConfig, contenT, value,"offer description");
		
        return offerData;

	}
	
	public void clickSubmit() {
		Browser.wait(testConfig, 2);
		Element.click(testConfig, submitQuery, "Click on Submit Query");
	}
	
	public void clickDeactivate() {
		Browser.wait(testConfig, 2);
		Element.click(testConfig, Deactivate, "Click on Deactivate");
	}

	public void verifyClassAdded(String text){
		Browser.wait(testConfig, 2);
		if(!text.isEmpty()){
			Helper.compareContains(testConfig, "comparing added css class", text, addClassDD.getText());
		}
		
	}
	
	public void verifyClassNotAdded(String text){
		Browser.wait(testConfig, 2);
		if(addClassDD.getText().contains(text)) {
			testConfig.logFail("Class added","should not be added", "present");
		}else {
			testConfig.logPass("class not added,test pass");
		}
	}
	
	public void addCssFroID(){
		Browser.wait(testConfig, 2);
		Element.selectValue(testConfig, amountdiv, "logo", "select the css name");
		Element.click(testConfig, submitCss, "add the css");
		
	}
	
	public void makeChangesLive(){
		Browser.wait(testConfig, 2);
		Element.click(testConfig, changetolive, "Make change live");
	}
	
	public void Reset(){
		Browser.wait(testConfig, 2);
		Element.click(testConfig, reset, "reset the chnages");
	}
	
	public void RemoveCss(){
		Browser.wait(testConfig, 2);
		if(Element.IsElementDisplayed(testConfig,remove)) {
			Element.click(testConfig, remove, "Remove css from id");
			makeChangesLive();
		}
	}
	
	public PreviewPage Preview()
	{
		Browser.wait(testConfig, 2);
		Element.click(testConfig, Preview, "see preview");
		return new PreviewPage(testConfig);
		
	}

}
