package PageObject.AdminPanel.uploadTDR;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.MerchantList.MerchantListPage;
import Utils.Element;
import Utils.Browser;
import Utils.Config;


public class uploadTDRpage {

	private Config testConfig;

	@FindBy(name="tdrUpload")
	private WebElement tdrUpload;

	@FindBy(css="input[type=\"submit\"]")
	private WebElement Submit;

	@FindBy(linkText="Merchant List")
	private WebElement merchant_list;

	public uploadTDRpage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, tdrUpload);
	}

	public void uploadTDR(Config testConfig, String fileName){
	    Element.enterFileName(testConfig, tdrUpload, fileName, "TDR Upload");
	    Element.click(testConfig, Submit, "Click on submit");
	    Browser.wait(testConfig, 5);
	    
		/*Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CLEAR);

		// press CTRL+S
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_ALT);


		// Press Enter
		robot.keyPress(KeyEvent.VK_ENTER);
		// Release Enter
		robot.keyRelease(KeyEvent.VK_ENTER);
*/
	}
	
	public MerchantListPage clickMerchantList(){
		Element.click(testConfig, merchant_list, "Merchant List");
		return new MerchantListPage(testConfig);
		}
}
