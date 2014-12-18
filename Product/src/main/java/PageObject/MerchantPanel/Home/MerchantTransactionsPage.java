package PageObject.MerchantPanel.Home;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.MerchantPanel.Transactions.TransactionDetailPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;

public class MerchantTransactionsPage {

	private Config testConfig;

	//@FindBy(css="td.txnpopup")
	@FindBy(xpath="//td[4]")
	private WebElement firstTransaction;

	@FindBy(css="table.expnd_width>tbody>tr>td")
	private WebElement billingFirstTransaction;

	@FindBy(name="qterm")
	private WebElement search;

	@FindBy(css="input[type=\"submit\"]")
	private WebElement clickGo;

	@FindBy(className="floatLeft")
	private WebElement ShowingRecords;

	@FindBy(className="dashboard")
	private WebElement dashboardLink;

	@FindBy(linkText="Export to Excel")
	private WebElement exporttoexcel;

	@FindBy(id="Unsettled")
	private WebElement unsettledCheckBox;

	@FindBy(css="div>table>tbody>tr>td:nth-child(3)")
	private WebElement transId;

	@FindBy(css="div>table>tbody>tr>td:nth-child(4)")
	private WebElement payuId;

	@FindBy(css="div>table>tbody>tr>td:nth-child(6)")
	private WebElement amount;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(7)")
	private WebElement reqStatus;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(8)")
	private WebElement bankName;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(9)")
	private WebElement reqAmount;

	@FindBy(css="div>table>tbody>tr>td:nth-child(10)")
	private WebElement discount;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(11)")
	private WebElement additionalCharge;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(12)")
	private WebElement merchantName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(14)")
	private WebElement paymentGateway;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(15)")
	private WebElement bankRefnum;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(15)")
	private WebElement lastName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(16)")
	private WebElement eMail;

	@FindBy(css="div>table>tbody>tr>td:nth-child(17)")
	private WebElement ipAddress;

	@FindBy(css="div>table>tbody>tr>td:nth-child(18)")
	private WebElement cardNo;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(19)")
	private WebElement cardType;

	@FindBy(css="div>table>tbody>tr>td:nth-child(20)")
	private WebElement field2;

	@FindBy(css="div>table>tbody>tr>td:nth-child(21)")
	private WebElement paymentType;

	@FindBy(css="div>table>tbody>tr>td:nth-child(22)")
	private WebElement errorCode;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(23)")
	private WebElement pgMID;

	@FindBy(css="div>table>tbody>tr>td:nth-child(24)")
	private WebElement offerKey;

	@FindBy(css="div>table>tbody>tr>td:nth-child(25)")
	private WebElement offerType;

	@FindBy(css="div>table>tbody>tr>td:nth-child(26)")
	private WebElement offerFailurereason;

	@FindBy(css="div>table>tbody>tr>td:nth-child(32)")
	private WebElement settle;

	@FindBy(xpath ="//td[8]")
	private WebElement status;

	@FindBy(xpath ="//td[7]")
	private WebElement action;

	public WebElement getDashboardLink() {
		return dashboardLink;
	}

	public MerchantTransactionsPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, search);

	}

	public TransactionDetailPage SearchTransaction(String transactionId) {
		Element.enterData(testConfig, search,transactionId, "Search the transaction");
		Element.click(testConfig, clickGo, "Click on Go, to search the transaction");
		Browser.waitForPageLoad(testConfig, firstTransaction);
		Element.click(testConfig,firstTransaction, "Select transaction");

		return new TransactionDetailPage(testConfig);
	}


	public String getNumberRecords() {
		String[] recordStringParts = ShowingRecords.getText().split(" ");
		String number = recordStringParts[recordStringParts.length -2 ];
		return number;
	}

	public String firstTransaction(){

		return firstTransaction.getText();
	}

	public TransactionDetailPage selectBillingTransaction(){

		Element.click(testConfig,billingFirstTransaction, "Select transaction");
		return new TransactionDetailPage(testConfig);
	}

	public void ClickExportToExcel() {
		Element.click(testConfig, exporttoexcel, "Click On Export To Excel");
	}

	
	public String getPaymentGateway() {
		return paymentGateway.getText();
	}

	//This method is for production test cases
	public TransactionDetailPage clickFirstTransatcion(Config testConfig) {
		Browser.waitForPageLoad(testConfig, firstTransaction);
		Element.click(testConfig,firstTransaction, "Select transaction");
		return new TransactionDetailPage(testConfig);
	}

	
	
	
}




