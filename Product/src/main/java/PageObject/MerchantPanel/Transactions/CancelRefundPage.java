package PageObject.MerchantPanel.Transactions;

import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class CancelRefundPage extends TransactionFilterPage{
	private Config testConfig;

	@FindBy(name="transactions[1]")
	private WebElement checkbox;

	@FindBy(css="div[class='list-div']>table>tbody")
	private WebElement firstTransaction;

	@FindBy(name="qterm")
	private WebElement search;

	@FindBy(name="amount[1]")
	private WebElement amountInputbox;

	@FindBy(css="input[type=\"submit\"]")
	private WebElement clickGo;

	@FindBy(id="cancelRefund")
	private WebElement cancelRefundButton; 

	public CancelRefundPage(Config testConfig)
	{
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, cancelRefundButton);

	}

	public CancelRefundPage SearchTransaction(String transactionId) {
		Element.enterData(testConfig, search,transactionId, "Search the transaction");
		Element.click(testConfig, clickGo, "Click on Go, to search the transaction");
		Element.click(testConfig, firstTransaction, "Select transaction");
		return new CancelRefundPage(testConfig);
	}

	public void RefundFirstTransaction()
	{   
		Element.check(testConfig, checkbox, "Click on check box");
		Element.click(testConfig, cancelRefundButton, "Click on Refund Button");
		RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
		confirm.ClickActionButton();
		
	}
	/* Get Expected transaction details from Data sheet
	 * Get cancel/refund transaction details from DB
	 * Compare actual details with expected
	 */
	public void VerifyCancelRefundTransaction(int requestRow, int transactionRow)
	{	

		TestDataReader txnData = new TestDataReader(testConfig, "TransactionDetails");
		TestDataReader refundData = new TestDataReader(testConfig, "ActionRequest");
		String ExpectedStatus = refundData.GetData(requestRow, "Status");
		String ExpectedAction = refundData.GetData(requestRow, "Action");
		String ExpectedMerchantId= txnData.GetData(transactionRow, "merchantid");
		String transactionId=testConfig.getRunTimeProperty("transactionId");
		testConfig.putRunTimeProperty("txnid", transactionId);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 1, 1);
		String payUId =map.get("mihpayid");
		testConfig.putRunTimeProperty("payUid", payUId);
		Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 4, -1);

		if(map1.get("action").contentEquals("cancel") || map1.get("action").contentEquals("refund"))
		{
			Helper.compareEquals(testConfig, "Cancel/Refund status", ExpectedStatus, map1.get("status"));
			Helper.compareEquals(testConfig, "Cancel/Refund action", ExpectedAction, map1.get("action"));
			Helper.compareEquals(testConfig, "Refund merchantId", ExpectedMerchantId, map1.get("merchant_id"));
			Helper.compareEquals(testConfig, "Bank ref id", "", map1.get("bank_ref_no"));	
		}
		else
		{
			testConfig.logFail("Cancel/Refund entry not found in txn_update");
		}
	}

}
