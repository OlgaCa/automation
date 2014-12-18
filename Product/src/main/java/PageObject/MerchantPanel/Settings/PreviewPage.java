package PageObject.MerchantPanel.Settings;

import org.openqa.selenium.WebElement;

import java.awt.AWTException;
import java.io.IOException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.Element.How;

public class PreviewPage {
	
	private Config testConfig;
	
	@FindBy(name="changetolive")
	private WebElement changetolive;
	
	@FindBy(name="invotemplate")
	private WebElement invotemplate;
	
	@FindBy(name="surl")
	private WebElement sUrl;
	
	@FindBy(css="div.fiLt.login > input[name=\"Submit\"]")
	private WebElement submitTemplate;
	
	@FindBy(name="sectionId")
	private WebElement sectionId;
	
	@FindBy(name="color")
	private WebElement color;
	
	@FindBy(name="style")
	private WebElement Style;
	
	@FindBy(name="refresh")
	private WebElement goToDefault;
			
	@FindBy(name="font")
	private WebElement font;
	
	@FindBy(name="Submit")
	private WebElement submitImage;
	
	@FindBy(xpath="//input[@value='Apply Preview']")
	private WebElement preview;
	
	@FindBy(xpath="//input[@name=\"image\"]")
	private WebElement fileInput;
	
	@FindBy(id="logo")
	private WebElement amountDiv;
	
	public PreviewPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, changetolive);
	}
	
	
	public void verifyClassAddedForID(){
		Browser.wait(testConfig, 2);
		testConfig.driver.switchTo().frame(1);
		if(Element.IsElementDisplayed(testConfig, amountDiv)){
	   WebElement amountDiv = Element.getPageElement(testConfig, How.xPath, "//*[@id=\"order-summary\"]");
		System.out.println(amountDiv.getAttribute("class"));
		Helper.compareContains(testConfig, "comapring class present or not", "logo",amountDiv.getAttribute("class"));
		
		}
			
	}
	
	public void verifyStyleChange(String value){
		Browser.wait(testConfig, 2);
		testConfig.driver.switchTo().frame(1);
		WebElement amountDiv = Element.getPageElement(testConfig, How.xPath, "//*[@id=\"amountdiv\"]");
		Helper.compareContains(testConfig, "comapring style changed or not", amountDiv.getCssValue("font-family"),value);
			
	}
	public void verifyStyleNotChange(String value){
		Browser.wait(testConfig, 2);
		testConfig.driver.switchTo().frame(1);
		WebElement amountDiv = Element.getPageElement(testConfig, How.xPath, "//*[@id=\"amountdiv\"]");
		if(amountDiv.getCssValue("font-family").contains(value)) {
			testConfig.logFail("style change","not changed", "chnaged");
		}else {
			testConfig.logPass("Style Not changed,test pass");
		}	
	}
	public void changeStyle(String value){
		Browser.wait(testConfig, 2);
		Element.selectValue(testConfig, sectionId, "amountdiv", "Select Amount box section");
		Element.enterData(testConfig, Style, value, "Style ");		
	}
	
	public void preview(){
		Browser.wait(testConfig, 2);
		Element.click(testConfig, preview, "check Preview");
	}
	
	public void makeChangesLive(){
		Browser.wait(testConfig, 2);
		Element.click(testConfig, changetolive, "Make changes Live");
	}
	
	public void GotoDefualt(){
		Browser.wait(testConfig, 2);
		Element.click(testConfig, goToDefault , "Got bakc to default");
	}
	
	public void verifyClassNotPresentForID(){
		Browser.wait(testConfig, 2);
		testConfig.driver.switchTo().frame(1);
		WebElement amountDiv = Element.getPageElement(testConfig, How.xPath, "//*[@id=\"amountdiv\"]");
		if(amountDiv.getAttribute("class").contains("logo")) {
			testConfig.logFail("Class present","not present", "present");
		}else {
			testConfig.logPass("class not present,test pass");
		}	
	}
	
	public void uploadImage(){
		Browser.wait(testConfig, 2);
		Element.enterFileName(testConfig, fileInput, "C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg", "Upload Image");
		// Runtime.getRuntime().exec("Upload_IE.exe");
		ClickUploadButton();
		//Element.click(testConfig, submitImage , "upload Image");
	}
	
	public void ClickUploadButton() {
	//	Runtime.getRuntime().exec("Upload_IE.exe");
		Element.click(testConfig, submitImage, "Upload Image");
		
	/*	Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CLEAR);

		// press CTRL+S
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_ALT);

		// press CTRL+S
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_O);
		robot.keyRelease(KeyEvent.VK_O);
		robot.keyRelease(KeyEvent.VK_ALT);

		// press CTRL+S
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.delay(5000);

		// Press Enter
		robot.keyPress(KeyEvent.VK_ENTER);
		// Release Enter
		robot.keyRelease(KeyEvent.VK_ENTER);*/	
	}
	
	public void enteremialTemplate(String html){
		Browser.wait(testConfig, 2);
		Element.enterData(testConfig, invotemplate, html, "enter html for email template");
	}
	public void entersuccessUrl(String s_url){
		Browser.wait(testConfig, 2);
		Element.enterData(testConfig, sUrl, s_url, "enter success url for email template");
	}
	public void submitEmailTemplate(){
		Browser.wait(testConfig, 2);
		Element.click(testConfig, submitTemplate, "submit the html And/OR succes url");
	}
	
}
