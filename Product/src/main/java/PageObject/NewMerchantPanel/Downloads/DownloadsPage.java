package PageObject.NewMerchantPanel.Downloads;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;

public class DownloadsPage {

	private Config testConfig;

	@FindBy(css = "[id=Page]>div:nth-child(3)>div>table>tbody>tr:nth-child(1)>td:nth-child(3)")
	private WebElement	moveOnFirstAvailableFile;

	@FindBy(css = "[id=Page]>div:nth-child(3)>div>table>tbody>tr:nth-child(1)>td:nth-child(3)>p>a")
	private WebElement	clickFirstAvailableFile;

	@FindBy(css = "[id=Page]>div:nth-child(2)>table>tbody>tr:nth-child(1)>td:nth-child(1)")
	private WebElement	moveOnFirstPendingFile;

	
	public DownloadsPage(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, moveOnFirstAvailableFile);
	}

	/**
	 * Method used to download the first file and returns expected downloaded file name
	 */
	public String downloadFirstAvailableFile()
	{
		Element.mouseMove(testConfig, moveOnFirstAvailableFile, "Mouse move on first file");
		Element.click(testConfig, clickFirstAvailableFile, "CLick download file");
		return Element.getPageElement(testConfig, How.css, "[id=Page]>div:nth-child(3)>div>table>tbody>tr:nth-child(1)>td:nth-child(1)").getText();
	}
	
	public void verifyPendingDownloads(String expectedRefId)
	{
		String actualRefId=moveOnFirstPendingFile.getText();
		actualRefId=actualRefId.substring(3, actualRefId.length()-4);
		Helper.compareEquals(testConfig, "Comparing reference id in pending downloads", expectedRefId, actualRefId);
	}
}
