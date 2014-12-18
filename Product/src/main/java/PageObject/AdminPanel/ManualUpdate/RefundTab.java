package PageObject.AdminPanel.ManualUpdate;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Test.AdminPanel.ReconAndBankFeeSettlement.ReconHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.FileHandler;
import Utils.Helper;
import Utils.Helper.FileType;

public class RefundTab {
	
	private Config testConfig;
	
	@FindBy(name="refundreversal")
	private WebElement refundReversalButton;
	
	@FindBy(css="*[id='refund']>div:nth-child(1)>form>input:last-child")
	private WebElement submit;
	
	public RefundTab(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, refundReversalButton);
	}

	/**
	 * Creates and uploads refund reversal file 
	 * @param mihpayid - payuid for transaction
	 * @param RefundReversalRow
	 */
	public void createAndUploadRefundReversalFile(String mihpayid,int RefundReversalRow) {
		ReconHelper helper = new ReconHelper(testConfig);
		String refundFile = helper.createRefundReversalFile(mihpayid,
				RefundReversalRow);
		refundReversalButton.sendKeys(refundFile);
		Browser.wait(testConfig, 5);
		Element.click(testConfig, submit, "Submit button");			
	}

}
