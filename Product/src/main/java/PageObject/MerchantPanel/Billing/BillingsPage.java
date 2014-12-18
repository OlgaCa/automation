package PageObject.MerchantPanel.Billing;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.ManualUpdate.MerchantSettlement;
import PageObject.MerchantPanel.Home.AboutPayment;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import PageObject.MerchantPanel.Transactions.RequestsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.DataBase;
import Utils.Element;
import Utils.Config;
import Utils.Helper;
import Utils.Element.How;

public class BillingsPage {

	private Config testConfig;

	@FindBy(css="h1")
	private WebElement settlement_id;

	public String SettlementId() {
		String settlementid = settlement_id.getText();
		return settlementid;
	}

	public BillingsPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);	
		Browser.waitForPageLoad(testConfig, settlement_id);
	}	

	@FindBy(css="div.table-div>table>tbody>tr>td.headingreport>a")
	private WebElement utr;

	public String UTR() {
		String uTR = utr.getText();
		return uTR;
	}

	public MerchantTransactionsPage clickUTR(String utr) {
		WebElement utrValue = Element.getPageElement(testConfig, How.linkText, utr);
		Element.click(testConfig, utrValue,"Click on Last Settled transaction");
		//Element.click(testConfig, utr,"Click on Last Settled transaction");
		return new MerchantTransactionsPage(testConfig);
	}

	//Payment Details of recent settled transaction
	@FindBy(css="div.table-div>table>tbody>tr.odd:nth-child(2)>td~td")
	private WebElement transactionAmount;

	public String getTransactionAmount() {
		String transaction_amount = transactionAmount.getText();
		return transaction_amount;
	}

	@FindBy(css="div.table-div>table>tbody>tr.even:nth-child(3)>td~td")
	private WebElement tdrFee;

	public String getTDRFee() {
		String tdr_fee = tdrFee.getText();
		return tdr_fee;
	}

	@FindBy(css="div.table-div>table>tbody>tr.odd:nth-child(4)>td~td")
	private WebElement serviceTax;

	public String getServiceTax() {
		String service_tax = serviceTax.getText();
		return service_tax;
	}

	@FindBy(css="div.table-div>table>tbody>tr.even:nth-child(5)>td~td")
	private WebElement netAmount;

	public String getNetAmount() {
		String net_amount = netAmount.getText();
		return net_amount;
	}

	@FindBy(css="div.table-div>table>tbody>tr.odd:nth-child(8)>td~td")
	private WebElement totalSettlement;

	public String getTotalSettlement() {
		String total_settlement = totalSettlement.getText();
		return total_settlement;
	}

	//Excel
	@FindBy(css="div.export-excel>a")
	private WebElement exportToExcel;

	public void ExportToExcel() {
		Element.click(testConfig, exportToExcel,"Click on Export to Excel");
	}

	//No of transactions against most recent settled paymnet
	@FindBy(css="div.table-div:nth-child(7)>table>tbody>tr:nth-child(2)>td:nth-child(4)")
	private WebElement nooftransactions;

	public String getNoOfTransactions() {
		String transactions = nooftransactions.getText();
		return transactions;
	}

	//Links
	@FindBy(linkText="contact@payu.in")
	private WebElement emailLink;

	public String getEmailLink() {
		String link = emailLink.getAttribute("href");
		return link;
	}

	@FindBy(css="a > div.grey")
	private WebElement newToPayment;

	public AboutPayment getNewToPayment() {
		Element.click(testConfig, newToPayment, "Click on New to payments");
		return new AboutPayment(testConfig);
	}

	//Filters
	@FindBy(css="div.arrow-right.arrow-down")
	private WebElement selectDateFilter;

	@FindBy(css="input[type='submit']")
	private WebElement submitDateFilter;

	public void applyDateFilter() {
		Element.click(testConfig, selectDateFilter, "Select date filter");
		fillDateInputFrom(Helper.getFourWeekStartingDate("dd/MM/YYYY"));
		fillDateInputTo(Helper.getCurrentDate("dd/MM/YYYY"));
		Element.click(testConfig, submitDateFilter, "Submit Date Filter");
	}

	@FindBy(linkText="Pending Payments")
	private WebElement pendingPayments;

	public RequestsPage clickPendingPayments() {
		Element.click(testConfig, pendingPayments, "Click on pending payments");
		return new RequestsPage(testConfig);
	}

	@FindBy(css = "div.table-div:nth-child(7)")
	private WebElement totalSettledTransactions;

	public int settledTransactionNo() {
		List<WebElement> table = totalSettledTransactions.findElements(By.tagName("tr")); 
		int length =table.size();
		length= length-1;

		return length;
	}

	public void fillDateInputFrom(String fromDate) {
		JavascriptExecutor js = (JavascriptExecutor) this.testConfig.driver;
		js.executeScript("document.getElementById('datefrom').value=('"+ fromDate + "');");
	}

	public void fillDateInputTo(String toDate) {
		JavascriptExecutor js = (JavascriptExecutor) this.testConfig.driver;
		js.executeScript("document.getElementById('dateto').value=('"+ toDate + "');");
	}


	/**Get the starting row data of settled transactions from DB
	 * @return array of settled transaction's data
	 */
	public String[] GetSettledTransactions(String merchantName) 
	{
		//query to get merchant id, and then settled transactions 
		testConfig.putRunTimeProperty("name", merchantName);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 67, 1);
		String merchant_id = map.get("merchantid");
		testConfig.putRunTimeProperty("merchant_id", merchant_id);

		map = DataBase.executeSelectQuery(testConfig, 68, 1);
		String[] settledTxnData = new String[5];
		settledTxnData[0] = map.get("settlement_id");
		settledTxnData[1] = map.get("mer_utr");
		settledTxnData[2] = map.get("value_date");
		settledTxnData[3] = map.get("amount");
		settledTxnData[4] = map.get("mer_net_amount");

		return settledTxnData;
	}

	public String GetCountOfUnSettledTransactions(String merchantName) 
	{
		//query to get merchant id, and count of unsettled transactions 
		testConfig.putRunTimeProperty("name", merchantName);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 67, 1);
		String merchant_id = map.get("merchantid");
		testConfig.putRunTimeProperty("merchant_id", merchant_id);

		map = DataBase.executeSelectQuery(testConfig, 69, 1);

		return map.toString();
	}

	/**
	 * @param testConfig
	 * @param helper
	 * @return Manual Update Transaction Page
	 * To clear settlement Id, so to get latest Most recent Payment in Billing page 
	 */
	public ManualUpdatePage DeleteSettlementId(Config testConfig, TransactionHelper helper){

		if (Element.IsElementDisplayed(testConfig,utr)==true){

			String css = "tr.p.odd > td.headingreport+td.headingreport";
			WebElement viewTrans = Element.getPageElement(testConfig, How.css, css);
			String settlementId = viewTrans.getText();

			helper.DoLogin();		
			ManualUpdatePage manualUpdatePage = helper.home.ClickManualTransactionUpdate();
			
			MerchantSettlement merchantSettlement = manualUpdatePage.ClickMerchantSettlement();

			merchantSettlement.ClearSettlementId(testConfig, settlementId);			
		}
		else {
		helper.DoLogin();
		}
		return new ManualUpdatePage(testConfig);
				
	}
}
