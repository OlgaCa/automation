package PageObject.AdminPanel.ManualUpdate;

import java.io.File;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Test.AdminPanel.ReconAndBankFeeSettlement.ReconHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.FileHandler;
import Utils.Helper;
import Utils.TestDataReader;
import Utils.Helper.FileType;

public class ChargebackTab {
	private Config testConfig;

	public enum ChargebackFileType{chargeback,chargebackReversal};
	
	@FindBy(name = "chargeback")
	private WebElement chargebackBrowse;

	@FindBy(name = "chargebackreversal")
	private WebElement chargebackReversalBrowse;

	@FindBy(css = "*[id='chargeback']>div:nth-child(1)>form>input:last-child")
	private WebElement submitChargeback;

	@FindBy(css = "*[id='chargeback']>div:nth-child(2)>form>input:last-child")
	private WebElement submitChargebackReversal;

	public ChargebackTab(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, chargebackBrowse);
	}

	
	/**
	 * Creates and uploads chargeback file for chargeback and chargeback reversal
	 * @param mihpayid-payuid of transaction
	 * @param refundReversalRow- row number from test data
	 * @param filetype - chargeback or chargeback reversal from chargeback tab
	 */
	public void createAndUploadChargebackfile(String mihpayid,
			int refundReversalRow, ChargebackFileType filetype) {
		// refund reversal file and chargeback file has same data
		ReconHelper helper = new ReconHelper(testConfig);
		String refundFile = helper.createRefundReversalFile(mihpayid,
				refundReversalRow);
		//chargeback
		if (filetype == ChargebackFileType.chargeback) {
			chargebackBrowse.sendKeys(refundFile);
			Browser.wait(testConfig, 5);
			Element.click(testConfig, submitChargeback, "Submit button");
		}
		//chargeback reversal
		if (filetype == ChargebackFileType.chargebackReversal) {
			chargebackReversalBrowse.sendKeys(refundFile);
			Browser.wait(testConfig, 5);
			Element.click(testConfig, submitChargebackReversal, "Submit button");
		}
	}
	
	/**
	 * Verifies chargeback file for status
	 * 
	 * @param Status
	 */
	public void verifyChargebackFileDownloaded(String Status) {
		String downloadedfilepath = System.getProperty("user.home") + "//Downloads//";
		File file = Browser.DesiredFileDownload(testConfig, downloadedfilepath,
				"chargeback");
		String fileName = file.getName();
		downloadedfilepath = downloadedfilepath + fileName;
		TestDataReader downloadedfile = new TestDataReader(testConfig,
				"PayU", downloadedfilepath);
		String ActualStatus = downloadedfile.GetData(1, "result");
		Helper.compareEquals(testConfig, "Status from Chargebackfile",
				Status, ActualStatus);

	}
	
}
