package PageObject.NewMerchantPanel.Transactions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage.filterTypes;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestDataReader;

public class TransactionsPage {

	private Config testConfig;
	
	@FindBy(linkText = "Filter")
	private WebElement filter;
	
	@FindBy(css = "#TransactionTable>table>tbody>tr:nth-child(1)>td:nth-child(2)>div>span")
	private WebElement firstCheckBox;
	public void selectFirstCheckBox()
	{
		Element.check(testConfig, firstCheckBox, "check the first check box");
	}
	
	
	@FindBy(css = "#TransactionTable>table>thead:nth-child(1)>tr>th:nth-child(2)>span")
	private WebElement allCheckTransactions;
	public void selectAllTransactions()
	{
		Element.check(testConfig, allCheckTransactions, "check the all transactions check box");
	}
	@FindBy(css = "li.block.last > label > a.green")
	private WebElement applyFilter;

	@FindBy(css = "ul.items.pm > li.ng-scope > label.inner.ng-binding > span.chk")
	private WebElement payTypeAll;

	@FindBy(css = "div[id=TxnFilter]>ul>li>ul>li:nth-child(2)>label>span")
	private WebElement payTypeCC;

	@FindBy(css = "div[id=TxnFilter]>ul>li>ul>li:nth-child(3)>label>span")
	private WebElement payTypeDC;

	@FindBy(css = "div[id=TxnFilter]>ul>li>ul>li:nth-child(5)>label>span")
	private WebElement payTypeEMI;

	@FindBy(css = "div[id=TxnFilter]>ul>li>ul>li:nth-child(6)>label>span")
	private WebElement payTypeCash;

	@FindBy(css = "div[id=TxnFilter]>ul>li>ul>li:nth-child(4)>label>span")
	private WebElement payTypeNB;

	@FindBy(css = "div[id=TxnFilter]>ul>li>ul>li:nth-child(7)>label>span")
	private WebElement payTypeCOD;

	@FindBy(css = "li.block.sts > ul.items > li.ng-scope > label.inner.ng-binding > span.chk")
	private WebElement all;

	@FindBy(css = "div[id=TxnFilter]>ul>li:nth-child(2)>ul>li:nth-child(3)>label>span")
	private WebElement authourized;

	@FindBy(css = "div[id=TxnFilter]>ul>li:nth-child(2)>ul>li:nth-child(4)>label>span")
	private WebElement captured;

	@FindBy(css = "div[id=TxnFilter]>ul>li:nth-child(2)>ul>li:nth-child(5)>label>span")
	private WebElement cancelled;

	@FindBy(css = "div[id=TxnFilter]>ul>li:nth-child(2)>ul>li:nth-child(6)>label>span")
	private WebElement refunded;

	@FindBy(xpath = "//div[@id='TxnFilter']/ul/li[2]/ul[2]/li[2]/label/span")
	private WebElement failedAll;

	@FindBy(xpath = "//div[@id='TxnFilter']/ul/li[2]/ul[2]/li[3]/label/span")
	private WebElement orderBounced;

	@FindBy(xpath = "//div[@id='TxnFilter']/ul/li[2]/ul[2]/li[4]/label/span")
	private WebElement cancelledByUser;

	@FindBy(xpath = "//div[@id='TxnFilter']/ul/li[2]/ul[2]/li[5]/label/span")
	private WebElement failedByBank;

	@FindBy(xpath = "//div[@id='TxnFilter']/ul/li[2]/ul[2]/li[6]/label/span")
	private WebElement orderDropped;

	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr>td:nth-child(5)>span")
	private WebElement amount;

	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr>td:nth-child(6)>span")
	private WebElement status;
	
	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr>td:nth-child(4)>span")
	private WebElement firstName;

	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr:nth-child(1)>td:nth-child(7)>span")
	private WebElement payuID;
	
	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr>td:nth-child(8)>span")
	private WebElement bankName;

	@FindBy(css = "li:nth-child(3)>input")
	private WebElement toAmount;

	@FindBy(css = "li:nth-child(2)>input")
	private WebElement fromAmount;

	@FindBy(xpath = "(//input[@type='text'])[4]")
	private WebElement binList;

	@FindBy(xpath = "(//button[@type='submit'])[2]")
	private WebElement processCOD;
	public List<WebElement> clickProcessButton()
	{
		List<WebElement> list=Element.getListOfElements(testConfig, How.css, "[id=BulkActionModal]>table>tbody>tr>td:nth-child(1)");
		if(processCOD.isEnabled())
		{
		Element.click(testConfig, processCOD, "CLick Process Refund");
		}
		return list;
	}
	
	@FindBy(partialLinkText="Customize Columns")
	private WebElement CustomizeColumnButton;
	
	@FindBy(linkText = "COD Cancel")
	private WebElement codCancel;

	@FindBy(linkText = "COD Settle")
	private WebElement codSettle;
	
	@FindBy(linkText = "Generate Invoice")
	private WebElement generateInvoice;

	@FindBy(linkText = "COD Verify")
	private WebElement verify;

	@FindBy(linkText = "Capture")
	private WebElement capture;

	@FindBy(css = "li.disabled > a.ng-binding")
	private WebElement recordsCount;

	@FindBy(css="#Page>div>div>div>div:nth-child(2)>a>Strong")
	private WebElement exportExcel;
	public void exportToExcel()
	{
		Element.click(testConfig, exportExcel, "Click Export to excel");
	}

//	@FindBy(linkText = "NEW EMAIL INVOICE")
//	private WebElement emailInvoice;

	
	@FindBy(css="[id=TxnAllData]>div>div>div>div:nth-child(2)>input")
	private WebElement date;

	@FindBy(css = "div.hastooltip.ng-binding > span.chkbox")
	private WebElement checkBox;

	@FindBy(css = "#ButtonHeader>div>a>span")
	private WebElement actionDropDwn;

	@FindBy(partialLinkText = "Refund")
	private WebElement dropDwnRefund;

	@FindBy(name = "repForm.amount")
	private WebElement amountRefund;

	@FindBy(xpath = "(//button[@type='submit'])[2]")
	private WebElement refundTrans;

	@FindBy(css = "#CustomizeColumns>ul>li>ul>li>label")
	private List<WebElement> customizeLocators;
	
	@FindBy(linkText = "COD Verify")
	private WebElement codVerify;

	@FindBy(css = "html>body>div:nth-child(6)>div:nth-child(2)>div:nth-child(2)>div:nth-child(1)>form>div:nth-child(2)>div>div>div:nth-child(1)>strong")
	private WebElement actualMsg;

	@FindBy(css = ".ng-scope>form>div:nth-child(2)>div")
	private WebElement noSelectionError;
	
	@FindBy(linkText = "OK")
	private WebElement refundOk;


	@FindBy(linkText = "Refund")
	private WebElement refund;
	
	@FindBy(linkText = "Cancel")
	private WebElement cancel;
	
	@FindBy(css="label.hover.ng-binding")
	private WebElement colTransId;
	
	@FindBy(css="span.glyphicon.glyphicon-chevron-right")
	private WebElement moveRight;
	
	@FindBy(css="#LeftTrigger")
	private WebElement moveLeft;
	
	

	@FindBy(css="th.ng-scope.initial > a.ng-scope > label.hover.ng-binding")
	private WebElement colDate;

	@FindBy(css="[id=TransactionTable]>table>thead>tr>th:nth-child(4)>a>label")
	private WebElement colCustName;

	@FindBy(css="[id=TransactionTable]>table>tbody>tr:nth-child(1)>td:nth-child(2)")
	private WebElement txnId;

	@FindBy(css="th.ng-scope.initial > label.ng-scope.ng-binding")
	private WebElement colStatus;
	
	@FindBy(css="[id=TransactionTable]>table>thead>tr>th:nth-child(7)>a>label")
	private WebElement colPayuId;

	@FindBy(css="[id=TransactionTable]>table>thead>tr>th:nth-child(5)>a>label")
	private WebElement colAmount;
	
	@FindBy(css="[id=TransactionTable]>table>thead>tr>th:nth-child(8)>label")
	private WebElement colBankName;	

	@FindBy(partialLinkText = "Customize Columns")
	private WebElement customizeColumns;

	@FindBy(css="#TransactionTable>table>thead:nth-child(1)>tr>th:nth-child(2)>a>label>b")
	private WebElement sortByTxnId;	
	
	@FindBy(css="#TransactionTable>table>thead:nth-child(1)>tr>th:nth-child(3)>a>label>b")
	private WebElement sortByDate;	
	
	@FindBy(linkText = "APPLY")
	private WebElement apply;

	@FindBy(linkText = "COD Verify")
	private WebElement codverify;
	
	@FindBy(css = "[id=TransactionTable]>table>tbody>tr:nth-child(1)>td>span")
	private List<WebElement> transactionDetails;
	@FindBy(linkText = "CLOSE")
	private WebElement close;
	
	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(10)>td:nth-child(4)")
	private WebElement cardno;

	@FindBy(css = "div[id=TxnInfo]>div:nth-child(3)>table>tbody>tr:nth-child(5)>td:nth-child(4)")
	private WebElement transStatus;

	@FindBy(css = "div.PageHeader > label.ng-binding")
	private WebElement payUID;

	@FindBy(css = "div[id=TxnInfo]>div:nth-child(3)>table>tbody>tr:nth-child(4)>td:nth-child(4)")
	private WebElement transAmount;
	
	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(4)>td:nth-child(2)")
	private WebElement transid;

	@FindBy(css = "td.b.ng-binding")
	private WebElement transDate;

	
	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(6)>td:nth-child(2)")
	private WebElement prodinfo;

	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(2)>td:nth-child(4)")
	private WebElement transfee;
	

	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(3)>td:nth-child(4)")
	private WebElement transDiscount;

	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(8)>td:nth-child(4)")
	private WebElement transRefNo;

	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(9)>td:nth-child(4)")
	private WebElement nameoncard;

	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(13)>td:nth-child(2)")
	private WebElement firstname;

	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(14)>td:nth-child(2)")
	private WebElement lastname;

	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(14)>td:nth-child(4)")
	private WebElement City;

	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(15)>td:nth-child(4)")
	private WebElement State;

	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(16)>td:nth-child(4)")
	private WebElement zipCode;

	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(15)>td:nth-child(2)")
	private WebElement Email;

	@FindBy(css = "#TxnInfo>div:nth-child(3)>table>tbody>tr:nth-child(16)>td:nth-child(2)")
	private WebElement Phone;

	@FindBy(css = "div.hastooltip.ng-binding")
	private WebElement txnID;

	@FindBy(css = "#ResetFilter")
	private WebElement resetFilters;
	public void resetFilters()
	{
		Element.click(testConfig, filter, "Click On  Filters");
		Element.click(testConfig, resetFilters, "Click On Reset Filters");
		Element.click(testConfig, filter, "Click On  Filters");
	}
	
	
	@FindBy(linkText = "RESET")
	private WebElement reset;
	public void resetColumns()
	{
		Element.click(testConfig, customizeColumns, "Click customized columns");
		Element.click(testConfig, reset, "Click Reset");
		Element.click(testConfig, customizeColumns, "Click customized columns");
	}
	
	 String txnIds="";
	 String payuIds="";
	 
	public TransactionsPage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, firstCheckBox);
	}

	public enum filterModes{CC,DC,EMI,CASH,COD,NB,ALL};
	/**
	 * Method Used to check the given Payement Mode
	 * 
	 * @param filterType
	 */
	public void filterByModes(filterModes filtertype) {

		Element.click(testConfig, filter, "Clicking the filter");

		switch (filtertype) {

		case CC:

			Element.check(testConfig, payTypeCC, "Ckecking the CC Tab");
			testConfig.logComment("Credit Card Check Box is Selected");
			break;

		case DC:

			Element.check(testConfig, payTypeDC, "Ckecking the DC Tab");
			testConfig.logComment("Debit Card Check Box is Selected");
			break;

		case EMI:

			Element.check(testConfig, payTypeEMI, "Ckecking the EMI Tab");
			testConfig.logComment("EMI Check Box is Selected");
			break;

		case CASH:

			Element.check(testConfig, payTypeCash, "Ckecking the CASH Tab");
			testConfig.logComment("Cash Check Box is Selected");
			break;

		case COD:

			Element.check(testConfig, payTypeCOD, "Ckecking the COD Tab");
			testConfig.logComment("Cash On Delivery Check Box is Selected");
			break;

		case NB:

			Element.check(testConfig, payTypeNB, "Ckecking the NB Tab");
			testConfig.logComment("Net banking Check Box is Selected");
			break;

		case ALL:

			Element.check(testConfig, payTypeAll, "Ckecking the ALL Tab");
			testConfig.logComment("ALL Check Box is Selected");
			break;

		default:
			testConfig.logComment("Given Filter Option is not available");
			break;
		}

		Element.click(testConfig, applyFilter,
				"Filter the list with Selected Item");

	}

	public enum filterByOrderStatus{All,Authourized,Captured,Cancelled,Refunded,AllUnSuccess,Bounced,Dropped,CancelledByUser,CancelledByBank};
	/**
	 * Method used to filter order by status
	 * 
	 * @param status
	 */
	public void filterByOrderStatus(filterByOrderStatus status) {

		Element.click(testConfig, filter, "Clicking the Amount Range");

		switch (status) {

		case All:

			Element.check(testConfig, all,
					"Checking the Order Type All check box");
			testConfig.logComment("Order Type All is checked");
			break;

		case Authourized:

			Element.check(testConfig, authourized,
					"Checking the Authourized check box");
			testConfig.logComment("Order Authourized check box is selected");
			break;

		case Captured:

			Element.check(testConfig, captured, "Checking the CC check box");
			testConfig.logComment("EMI Check Box is Selected");
			break;

		case Cancelled:

			Element.check(testConfig, cancelled,
					"Checking the Cancelled check box");
			testConfig.logComment("Cash On Delivery Check Box is Selected");
			break;

		case Refunded:

			Element.check(testConfig, refunded,
					"Ckecking the Refunded Check Box");
			testConfig.logComment("Net banking Check Box is Selected");
			break;

		case AllUnSuccess:

			Element.check(testConfig, failedAll,
					"Ckecking the All Uncessful check Box");
			testConfig.logComment("ALL Check Box is Selected");
			break;

		case Bounced:

			Element.check(testConfig, orderBounced,
					"Ckecking the Bounced Check Box");
			testConfig.logComment("ALL Check Box is Selected");
			break;

		case CancelledByUser:

			Element.check(testConfig, cancelledByUser,
					"Ckecking the Cancelled By User Check Box");
			testConfig.logComment("ALL Check Box is Selected");
			break;

		case CancelledByBank:

			Element.check(testConfig, failedByBank,
					"Ckecking the Failed By Bank Check Box");
			testConfig.logComment("ALL Check Box is Selected");
			break;

		case Dropped:

			Element.check(testConfig, orderDropped,
					"Ckecking the Failed By Dropped");
			testConfig.logComment("ALL Check Box is Selected");
			break;

		default:
			testConfig.logComment("Given Filter Option is not available");
			break;
		}

		Element.click(testConfig, applyFilter,
				"Filter the list with Selected Item");

	}

	/**
	 * Filters the tables based on given range
	 * 
	 * @param min
	 * @param max
	 */
	public void filterByAmountRange(String from, String to) {
		Element.click(testConfig, filter, "Clicking the Amount Range");
		Element.enterData(testConfig, fromAmount, from, "Enter from Amount");
		Element.enterData(testConfig, toAmount, to, "Enter Minimum Amount");

		Element.click(testConfig, applyFilter,
				"Filter the list with Selected Item");

	}

	

	

	/**
	 * To verify data in the merchant panel on adding the filter of Bin List
	 * 
	 * @param bin
	 */
	public void filterByBinList(String bin) {
		Element.click(testConfig, filter, "Clicking the filter");
		Element.enterData(testConfig, binList, bin, "Enter Bin List");
		Element.click(testConfig, applyFilter, "Apply Filter");
	}

	
	/**
	 * Method used to check all customized columns
	 */
	public void clickAllCustomizedOptions() {
		
		Element.click(testConfig, customizeColumns,
				"Clicking the customize Columns");
		
		List<WebElement> list=customizeLocators;
		
		for(WebElement element:list)
		{
			element.click();
		}		
		Element.click(testConfig, apply, "Apply Button");
		Browser.wait(testConfig, 2);
		
		
	}
	
	/**
	 * Method used to verify balance amount after performing refund action
	 * @param remainingAmount
	 * @param count
	 */
	public void verifyBalanceAmount(String remainingAmount,int count)
	{
		Element.IsElementDisplayed(testConfig, amount);
		String balanceAmount ;

		for(int i=1;i<=count;i++)
		{
			balanceAmount=Element.getPageElement(testConfig, How.css, "div[id=TransactionTable]>table>tbody>tr:nth-child("+i+")>td:nth-child(5)>span").getText();
			Helper.compareEquals(testConfig, "Balance after refund", balanceAmount,
					remainingAmount);
		}
		
	}

	/**
	 * Verify Amount as Prefilled and Editable for Partial Refund.
	 * 
	 * @param transactionID
	 * @param refundMessage
	 * @param refundAmount
	 * @return
	 */
	public void FillRefundAmount(String transactionID,
			 String refundAmount) {
	

		MerchantPanelPage merchantPage=new MerchantPanelPage(testConfig);
		merchantPage.filterBasedOn(filterTypes.TransactionID);
		merchantPage.searchData(transactionID);
	
		Element.check(testConfig, checkBox, "Select the check box for refund");
		Element.click(testConfig, actionDropDwn,
				"Select the check box for refund");

		Element.click(testConfig, dropDwnRefund, "Selecting PayU ID");

		enterRefundAmount(refundAmount);

		
	}
	
	/**
	 * Method used to check weather Verify Amount as Prefilled and Editable for  Refund.
	 * @param refundAmount
	 */
	public void enterRefundAmount(String refundAmount)
	{
		Element.IsElementDisplayed(testConfig, amountRefund);

		Element.clear(testConfig, amountRefund,
				"Clear the text field to check if it is editable");

		Element.enterData(testConfig, amountRefund, refundAmount,
				"reset the value");

		Element.click(testConfig, refundTrans, "CLick refund trans");
	}

	//Method used to perform COD Verify or COD Settle actions and verify same with Requests page
	
	public void performCODActions( dropDownAction action,
			String cronFile,int count) {
		MerchantPanelPage merchantPage = new MerchantPanelPage(testConfig);
		Element.click(testConfig, checkBox, "Click the check box");

		selectDropDownAction(action);
		clickProcessButton();
		verifyRefundMessage(count);
		Helper.executeCron(testConfig,cronFile);
		RequestsPage requestPage = merchantPage.ClickRequestsTab();
		Browser.wait(testConfig, 2);
		requestPage.verifyRequestStatus("Success",count,139);
		merchantPage.ClickTransactionsTab();

	}
	
	public enum dropDownAction{Capture,Refund,Cancel,CODVerify,CODSettle,CODCancel};
	/**
	 * Method used to select Action
	 * @param action
	 */
	public void selectDropDownAction(dropDownAction action)
	{
		Element.click(testConfig, actionDropDwn,"Select the check box for refund");
		switch (action) {

		case Capture:
			Element.click(testConfig, capture, "Selecting Capture Action");
			break;

		case Refund:
			Element.click(testConfig, refund, "Selecting Refund Action");
			break;

		case Cancel:
			Element.click(testConfig, cancel,"Select Cancel Action");
			break;
			
		case CODVerify:
			Element.click(testConfig, codVerify, "Selecting COD Verify");
			break;

		case CODSettle:
			Element.click(testConfig, codSettle, "Selecting COD Settle");
			break;

		case CODCancel:
			Element.click(testConfig, codCancel,"Select the COD Cancel");
			break;	
				
		}
		
		
	}

	
	/**
	 * Method used to verify the refund message and status
	 * 
	 * @param refundMessage
	 */
	public String verifyRefundMessage(int count) {
		Browser.wait(testConfig, 1);
		String actualMessage = actualMsg.getText();
		String payuids ="";
		String[] message = actualMessage.split("\n");
		actualMessage = message[0];
		String refID = message[message.length - 1];
		message = refID.split(" ");
		refID = message[message.length - 1];
		List<WebElement> status=Element.getListOfElements(testConfig, How.css, "#BulkActionModal>table>tbody>tr>td:nth-child(3)");
		for(WebElement elements:status)
		{
			if(!elements.getText().equals("QUEUED"))
			{
				Helper.compareTrue(testConfig, "Status was not QUEUED", false);
			}
		}
		
		List<WebElement> payuIds=Element.getListOfElements(testConfig, How.css, "#BulkActionModal>table>tbody>tr>td:nth-child(1)");
		for(WebElement elements:payuIds)
		{
			payuids=payuids+","+elements.getText();
			if(payuids.charAt(0)==',')
			{
			payuids=payuids.replace(",", "");
			}
		}
		
		testConfig.putRunTimeProperty("mihpayid", payuids);
	
		Helper.compareEquals(testConfig, "Refund Message ",
				"Successfully queued "+count+" out of "+count+" request(s).", actualMessage);
		Element.click(testConfig, refundOk, "Click OK");
		return refID;

	}




	/**
	 * Verify status in All Transactions Tab for the Payu ID
	 * 
	 * @param transactionID
	 * @param reqStatus
	 * @return
	 */
	public void verifyTransactionStatus(String transactionID, String reqStatus,int count) {
		
		MerchantPanelPage merchantPage=new MerchantPanelPage(testConfig);
		merchantPage.filterBasedOn(filterTypes.TransactionID);
		merchantPage.searchData(transactionID);
		
		 Browser.wait(testConfig, 2);
		String status;
		for(int i=1;i<=count;i++)
		{
			status=Element.getPageElement(testConfig, How.css, "div[id=TransactionTable]>table>tbody>tr:nth-child("+i+")>td:nth-child(6)>span").getText();
		
			Helper.compareEquals(testConfig, "Status in Transactions tab",
					status, reqStatus);
		}
		

	}

	/** 
	 * Method used to test weather default columns are present or not
	 */
	public void verifyDefaultColumns()
	{
	
		Element.IsElementDisplayed(testConfig, colTransId);
		Element.IsElementDisplayed(testConfig, colDate);
		Element.IsElementDisplayed(testConfig, colCustName);
		Element.IsElementDisplayed(testConfig, colAmount);
		Element.IsElementDisplayed(testConfig, colStatus);
		for(int i=0;i<2;i++)
			Element.click(testConfig, moveRight, "move right");
		
		
		Element.IsElementDisplayed(testConfig, colPayuId);
	
		Element.IsElementDisplayed(testConfig, colBankName);
	}
	
	/**
	 * Method used to verify the transaction details with DB
	 * 
	 * @param rowNo
	 */
	public void compareTransactionDetailsWithDownloadedFile(int rowNo,TestDataReader reader) {
	
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", txnId.getText(),
				reader.GetData(rowNo, "Transaction ID"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", firstName.getText(),
				reader.GetData(rowNo, "Customer Name"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB",
				amount.getText().trim(),reader.GetData(rowNo, "Amount"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB",
				status.getText(),reader.GetData(rowNo, "Status"));
		
		for (int i = 0; i < 2; i++)
			Element.click(testConfig, moveRight, "Click the right slide");

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", payuID.getText(),
				reader.GetData(rowNo, "PayU ID"));		
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", bankName.getText(),
				reader.GetData(rowNo, "Bank Name"));
		
		
	}
	
	/**
	 * Method used to select one row from the table and put txnid in runtime property so that appropriate 
	 * query can be run 
	 * 
	 * @param searchData
	 * @param i
	 */
	public void selectTransactionRowToCompare(String[] searchData, int i) {
		String transID = Element.getPageElement(
				testConfig,
				How.xPath,
				"//div[@id='TransactionTable']/table/tbody/tr[" + i
						+ "]/td[2]/div").getText();
		Element.getPageElement(
				testConfig,
				How.xPath,
				"//div[@id='TransactionTable']/table/tbody/tr[" + i
						+ "]/td[2]/div").click();
		for (int j = 0; j < searchData.length; j++) {
			if (transID.equals(searchData[j])) {
				testConfig.putRunTimeProperty("txnId", transID);
				break;
			}
		}
	}
	
	/**
	 * Method used to verify the transaction status with the given array of expected status
	 * Ex: Refund or captured can appear when filtered with captured
	 * @param status
	 * @return
	 */
 public boolean verifyStatus(String[] status)
	{
	 Browser.wait(testConfig, 2);
		boolean result=false;
		List<WebElement> list=Element.getListOfElements(testConfig, How.css, "#TransactionTable>table>tbody>tr>td:nth-child(6)");
		for(WebElement element:list)
		{	
			//verify the current status with the string array
				for(String stringArray : status)
			    if(stringArray.contains(element.getText())) 
			    	{
			    	result=true;
			    	break;
			    	}
		}
		return result;
	}
 
	/**
	 * Method used to select one row from the table
	 * 
	 * @param searchData
	 * @param i
	 */
	public void selectPayUIDRowToCompare(String[] searchData, int i) {
		String transID = Element.getPageElement(
				testConfig,
				How.xPath,
				"//div[@id='TransactionTable']/table/tbody/tr[" + i
						+ "]/td[2]/div").getText();
		Element.click(testConfig, moveRight, "move right");
		String payuID = Element
				.getPageElement(
						testConfig,
						How.xPath,
						"//div[@id='TransactionTable']/table/tbody/tr[" + i
								+ "]/td[7]").getText();
		Element.getPageElement(testConfig, How.xPath,
				"//div[@id='TransactionTable']/table/tbody/tr[" + i + "]/td[7]")
				.click();
		for (int j = 0; j < searchData.length; j++) {
			if (payuID.equals(searchData[j])) {
				testConfig.putRunTimeProperty("txnId", transID);
				// testConfig.putRunTimeProperty("mihpayid", searchData[j]);
				break;
			}
		}
	}
	
	/**
	 * Method used to check only few columns
	 */
	public void clickSelectiveCustomizedOptions() {
			
		Element.click(testConfig, customizeColumns,	"Clicking the customize Columns");
		
		List<WebElement> list=customizeLocators;
		
		for(int i=0;i<15;i++)
		{
			list.get(i).click();
		}		
		Element.click(testConfig, apply, "Apply Button");
		Browser.wait(testConfig, 2);
	}
	
	public void verifySelectedColumnsDetails(int rowNo)
	{
		
	String elementText="";
	List<WebElement> list=transactionDetails;
	List<String> valuesList=new ArrayList<>();
		int j=0;
		for(int i=0;i<14;i++)
		{			
			list.get(0).getText();
			elementText=list.get(i).getText();
			if(elementText.equals(""))
			{
				Element.click(testConfig, moveRight, "description");
				elementText=list.get(i).getText();
			}
			if(elementText.equals("-"))
			{
				elementText="";
			}
			valuesList.add(elementText);
			testConfig.logComment(elementText+" J is "+j);
			j++;
		}

		Map<String, String> map = DataBase.executeSelectQuery(testConfig,
				rowNo, 1);
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", txnId.getText(),
				map.get("txnid"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(1),
				map.get("firstname"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB",
				valuesList.get(2), map.get("amount"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB",valuesList.get(4),
				map.get("id"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(6),
				map.get("productinfo"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(7),
				map.get("lastname"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(8),
				map.get("email"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(9),
				map.get("phone"));
	
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(10),
				map.get("ip"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(11),
				
				map.get("city"));
	
		String gateway=map.get("paymentgatewayid");
		if(gateway.equals("7"))
		{
			gateway="AXIS";
		}
		else{
			gateway="HDFC2";
		}
		if(valuesList.get(13).contains(gateway))
		{
			Helper.compareTrue(testConfig, "Comparing payement gateway value",true);
		}
		else{
			Helper.compareTrue(testConfig,  "Comparing payement gateway value", false);
		}
	}
	/**
	 * Method used to verify the transaction details with DB
	 * 
	 * @param rowNo
	 */
	public void verifyTransactionDetails(int rowNo) {

		
		String elementText="";
	List<WebElement>	list=transactionDetails;
	List<String> valuesList=new ArrayList<>();
		int j=0;
		for(WebElement element:list)
		{			
			list.get(0).getText();
			elementText=element.getText();
			if(elementText.equals(""))
			{
				Element.click(testConfig, moveRight, "description");
				elementText=element.getText();
			}
			if(elementText.equals("-"))
			{
				elementText="";
			}
			valuesList.add(elementText);
			
			System.out.println(elementText+" J is "+j);
			j++;
		}

		Map<String, String> map = DataBase.executeSelectQuery(testConfig,
				rowNo, 1);
		
		Map<String, String> updatemap = DataBase.executeSelectQuery(testConfig,
				4, 1);

		Map<String, String> issuingBank = DataBase.executeSelectQuery(testConfig,
				133, 1);
		
		Map<String, String> merchantTable = DataBase.executeSelectQuery(testConfig,
				140, 1);
		
		

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", txnId.getText(),
				map.get("txnid"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(1),
				map.get("firstname"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB",
				valuesList.get(2), map.get("amount"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(3)
						.toLowerCase(), map.get("status").toString()
						.toLowerCase());
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB",valuesList.get(4),
				map.get("id"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(6),
				map.get("productinfo"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(7),
				map.get("lastname"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(8),
				map.get("email"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(9),
				map.get("phone"));
	
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(10),
				map.get("ip"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(11),
				
				map.get("city"));
			
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB",valuesList.get(12), 
				merchantTable.get("name"));
				
		String gateway=map.get("paymentgatewayid");
		if(gateway.equals("7"))
		{
			gateway="AXIS";
		}
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(13),gateway
				);
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(14),
				updatemap.get("bank_ref_no"));
		
		elementText=issuingBank.get("is_domestic");
		if(elementText.equals("1"))
		{
			elementText="domestic";
		}
		else{
			elementText="international";
		}
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(15),
				elementText);
		
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB",valuesList.get(16),
				updatemap.get("bank_arn"));

		elementText=valuesList.get(20).replace(" ", "");
		String mapValue=map.get("field9").replace(" ","");
		if(elementText.equals(mapValue))
		{
		Helper.compareTrue(testConfig, "Comparing payement msg",true);	
		}
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB",valuesList.get(21),
				issuingBank.get("issuing_bank"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB",valuesList.get(22),
				map.get("payment_source"));
		Helper.compareEquals(testConfig,
		"Comapring transaction values with DB", valuesList.get(23),
		map.get("name_on_card"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(24),
				map.get("card_no"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(25),
				map.get("address1"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(26),
				map.get("address2"));
		
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(27),
				map.get("state"));
		

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(28),
				map.get("country"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(29),
				map.get("zipcode"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(39),
				map.get("amount"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB",valuesList.get(40)
						.trim(), map.get("discount"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(41),
				map.get("additional_charges"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(42),
				updatemap.get("inr_amount"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(43),
				map.get("amount"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(44),
				updatemap.get("bank_service_fee"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(45),
				updatemap.get("bank_service_tax"));
		elementText=updatemap.get("mer_settled");
		if(elementText.equals("2"))
		{
			elementText="settled";
		}
		else{
			elementText="unsettled";
		}
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(48),
				elementText);
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(54),
				map.get("udf1"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(55),
				map.get("udf2"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(56),
				map.get("udf3"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(57),
				map.get("udf4"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(58),
				map.get("udf5"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(59),
				map.get("field1"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(60),
				map.get("field2"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(61),
				map.get("field3"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(62),
				map.get("field4"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(63),
				map.get("field5"));

	}

	
	/**
	 * Method used to verify weather default columns are selected or not
	 */
	public void verifyDefaultCheckedColumns()
	{
		Element.click(testConfig, customizeColumns,"Clicking the customize Columns");
		
		
		List<WebElement> list=Element.getListOfElements(testConfig, How.xPath, ".//*[@id='CustomizeColumns']/ul/li/ul/li[@class='ng-scope on def']");
		for(WebElement element:list)
		{
			if(!(element.getText().equals("Customer Name") || element.getText().equals("Transaction ID") || element.getText().equals("Date") || element.getText().equals("PayU ID") ||
					element.getText().equals("Status") || element.getText().equals("Amount") || element.getText().equals("Bank Name") ) && (list.size()==7))
			{
				Helper.compareTrue(testConfig, "Comparing default checked items", false);
			}
		}
		Element.click(testConfig, customizeColumns,"Clicking the customize Columns");
	}
	
	/**
	 * Method used to click sorting elements and store txnid and payuid in properties
	 * @param sortBy
	 */
	public void clickSortTransactionTable(String sortBy)
	{
		
		switch (sortBy) {
		case "TransactionID":
			Element.click(testConfig, sortByTxnId, "Click Sort by TxnID");
			break;
		case "Date":
			Element.click(testConfig, sortByDate, "Click Sort by Date");
			break;	

		default:
			break;
		}
		Element.IsElementDisplayed(testConfig, txnId);
		testConfig.putRunTimeProperty("txnid", txnId.getText());
		Element.click(testConfig, moveRight, "click right to get payui id");
		testConfig.putRunTimeProperty("mihpayid", payuID.getText());
		Element.click(testConfig, moveLeft, "click right to get payui id");
			
	}
	
	/**
	 * Method used to verify dates with DB in transactions page
	 * @param rowNo
	 */
	public void verifySortedValuesWithDB(int rowNo)
	{
	
			Map<String, String> map = DataBase.executeSelectQuery(testConfig,
					rowNo, 1);

			Helper.compareEquals(testConfig, "Comparing the result with DB", txnId.getText(),
					map.get("txnid"));
			
			String Date=date.getText();
			String[] tempDate=Date.split(" ");
			String time=tempDate[1];
			Date=tempDate[0];
			Date=Helper.changeDateFormat(Date);
			Date=Date.replace("/", "-");
			String expectedDate=map.get("addedon");
			tempDate=expectedDate.split(" ");
			String expectedDay=tempDate[0];
			String expectedTime=tempDate[1];
			expectedTime=expectedTime.substring(0, expectedTime.length()-2);
			Helper.compareEquals(testConfig, "Comparing the result with DB",Date ,
					expectedDay);
			Helper.compareEquals(testConfig, "Comparing the result with DB",time ,
					expectedTime);
	}
	
	/**
	 * Mehtod used to compare error messgae when no column is selected and action is performed 
	 * @param expectedErrorMessage
	 */
	 public void getNoActionSelectedErrorMessage(String expectedErrorMessage)
	 {
		 
		 String actualError=noSelectionError.getText();
		 actualError=actualError.replace("\n","");
		 Helper.compareEquals(testConfig, "Comparing Error Message", expectedErrorMessage, actualError);
		 Element.getPageElement(testConfig, How.linkText, "CANCEL").click();
	 }
	 
	 /**
	  * Method used to compare wrong error messages on acitons
	  * @param performedAction
	  * @param actualMsg
	  */
	 public void verifyWrongActionMessage(String performedAction,String actualMsg)
	 {
		String action= Element.getPageElement(testConfig, How.css, ".ng-scope>form>div:nth-child(2)>div>div>p:nth-child(1)").getText();
		String errormsg=Element.getPageElement(testConfig, How.css, ".ng-scope>form>div:nth-child(2)>div>div>p:nth-child(4)").getText();
		if(!Element.getPageElement(testConfig, How.css, ".btn.btn-success.ng-scope.ng-binding").isEnabled())
		{
			Helper.compareTrue(testConfig, "Button is disabled", true);
			Element.getPageElement(testConfig, How.linkText, "CANCEL").click();
			
		}
		else{
			Helper.compareTrue(testConfig, "Button is not disabled", false);
		}
		Helper.compareEquals(testConfig, "Comparing performed Action", performedAction, action);
		Helper.compareEquals(testConfig, "Comparing performed Action", actualMsg, errormsg);
		 
	 }
	 
	 /**
	  * Method used to verfiy the error message when wrong amount is entered in refund amount
	  */
	 public void verifyInvalidAmountRefund()
	 {
		 if(!Element.getPageElement(testConfig, How.css, ".btn.btn-success.ng-scope.ng-binding").isEnabled())
			{
				Helper.compareTrue(testConfig, "Button is disabled", true);
				
			}
			else{
				Helper.compareTrue(testConfig, "Button is not disabled", false);
			}
	 }
	 
		/**
		 * Method used to verify action items present in transactions page
		 */
		public void verifyActionsItems()
		{
			Element.click(testConfig, actionDropDwn, "click action tab");
			Element.IsElementDisplayed(testConfig, codCancel);
			Element.IsElementDisplayed(testConfig, codSettle);
			Element.IsElementDisplayed(testConfig, generateInvoice);
			Element.IsElementDisplayed(testConfig, verify);
			Element.IsElementDisplayed(testConfig, refund);
			Element.IsElementDisplayed(testConfig, cancel);
			Element.IsElementDisplayed(testConfig, capture);
		} 
	 /**
	  * Method used to select multiple check boxes based on status
	  */
	 public void selectActionToPerform(List<String> status,int count,String operationType,String actionType)
	 {
		 Browser.wait(testConfig, 2);
		 List<WebElement> totalElements=Element.getListOfElements(testConfig, How.css,"[id=TransactionTable]>table>tbody>tr");
		 List<WebElement> listOfPages=Element.getListOfElements(testConfig, How.css, "#Pagination>ul>li>a");
		 WebElement element;
		 String currentStatus;
		 int j=0;
		 
		 for(int p=2;p<listOfPages.size()-3;p++)
			{
			 
			 for(int i=1;i<totalElements.size();i++)
			 {
			 element=Element.getPageElement(testConfig, How.css, "#TransactionTable>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div");
			 Element.mouseMove(testConfig, element, "Mouse move to get status");
			 Browser.wait(testConfig, 2);
			 currentStatus=Element.getPageElement(testConfig, How.css, "[id=TransactionTable]>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>div>div>table>tbody>tr:nth-child(5)>td:nth-child(2)").getText();
			 
			 if(status.contains(currentStatus))
			 {
			 switch (operationType) {
			 case "POPUP":
				performPopUpAction(element, i, actionType);
				j++;
				break;

			 case "CHECKBOX":
				performCheckBoxAction(element, i);
				j++;
				break;

			 case "MOUSEOVER":				
				performMouseMoveAction(element, i, actionType);
				
				j++;
				break;

			 default:
				break;
			}
			 
			 if(j==count)
			 {
				 return;
			 }
				
			 }
			//used to scroll down the page
			 if(i>=10)
			 {
				 Element.pageScroll(testConfig, "0", "500");
			 }
			 
			
			}
			 Element.getPageElement(testConfig, How.css, "#Pagination>ul>li:nth-child("+(p+2)+")>a").click();
			 Browser.wait(testConfig, 2);
			 if(j==count)
			 {
				 break;
			 }
		 }
		}
	 /**
		 * Method used to verify the columns which are selected
		 * 
		 * @param expectedCount
		 */
		public void verifyCheckedColumns(int expectedCount)
		{
			Element.click(testConfig, customizeColumns, "Click customized columns");
			List<WebElement> list=Element.getListOfElements(testConfig, How.xPath, "//*[@id='CustomizeColumns']/ul/li/ul//li[@class='ng-scope on']");
			if(list.isEmpty())
			{
				testConfig.logComment("only default columns are selected");
				return;
			}
			if(list.size()==expectedCount)
			{
				Helper.compareTrue(testConfig, "All checkboxes are selected",true);
			}
			else{
				Helper.compareTrue(testConfig, "All checkboxes are not selected",false);
			}
			Element.click(testConfig, customizeColumns, "clicking customized column");
		}
		
		/**
		 * Verifying transaction details result in DB
		 * 
		 * @param rowNo
		 * @return
		 * @throws InterruptedException
		 */
		public void compareTransactionDetailsWithDB(int rowNo) {

			// getting all the values of the transactions

			Element.waitForElementDisplay(testConfig, payUID);
			String payuID = payUID.getText();

			String[] str = payuID.split(" ");
			payuID = str[str.length - 1].trim();

				testConfig.putRunTimeProperty("mihpayid",payuID);	
			
			String amount = transAmount.getText();
			str = amount.split("\n");
			amount = str[0].trim();

			String status = transStatus.getText();

			// Transaction Details
			String transID = transid.getText();

			String date = transDate.getText();

			String ProdInfo = prodinfo.getText();

			String transFee = transfee.getText();
			String discount = transDiscount.getText();

			// Payment Details

			String bankRefNo = transRefNo.getText();

			if (bankRefNo.equals("NA")) {
				bankRefNo = "";
			}
			String nameOnCard = nameoncard.getText();
			String cardNo = cardno.getText();
			String firstName = firstname.getText();

			if (firstName.equals("NA")) {
				firstName = "";
			}
			String lastName = lastname.getText();
			if (lastName.equals("NA")) {
				lastName = "";
			}

			// Customer Details
			String city = City.getText();
			if (city.equals("NA")) {
				city = "";
			}

			String state = State.getText();
			if (state.equals("NA")) {
				state = "";
			}
			String zipcode = zipCode.getText();
			if (zipcode.equals("NA")) {
				zipcode = "";
			}
			String email = Email.getText();
			String phone = Phone.getText();

			Map<String, String> map = DataBase.executeSelectQuery(testConfig,
					rowNo, 1);

			Map<String, String> statusMap = DataBase.executeSelectQuery(testConfig,
					4, 1);

			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", payuID, map.get("id"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", amount.trim(),
					map.get("amount"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", transID,
					map.get("txnid"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", ProdInfo,
					map.get("productinfo"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", transFee.trim(),
					map.get("transaction_fee"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", discount.trim(),
					map.get("discount"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", nameOnCard,
					map.get("name_on_card"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", cardNo,
					map.get("card_no"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", firstName,
					map.get("firstname"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", lastName,
					map.get("lastname"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", city, map.get("city"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", state, map.get("state"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", zipcode,
					map.get("zipcode"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", email, map.get("email"));
			Helper.compareEquals(testConfig,
					"Comparing transaction values with DB", phone, map.get("phone"));

			if (map.get("addedon").toString().contains(date.concat(".0"))) {

				Helper.compareTrue(testConfig, "Comparing addedon value with DB",
						true);
			}
			if(!status.equals("Failed"))
			{
			if (status.toLowerCase().contains(
					statusMap.get("action").toString().toLowerCase())) {

				Helper.compareTrue(testConfig, "Comparing addedon value with DB",
						true);
			}
			}
			else
			{
				Helper.compareEquals(testConfig,
						"Comparing transaction values with DB", status, map.get("status"));
			}

			Element.click(testConfig, close, "Closing the popup");
		}

	 /**
	  * Method used to compare transaction status with DB
	  * @param expectedStatus
	  * @param rowNo
	  * @param count
	  */
	public void comparetransactionStatus(String expectedStatus,int rowNo,int count)
	{
		Browser.wait(testConfig, 2);
		String actualStatus;
		
		for(int i=1;i<=count;i++)
		{
			actualStatus=Element.getPageElement(testConfig, How.css, "[id=TransactionTable]>table>tbody>tr:nth-child("+i+")>td:nth-child(6)>span").getText();
			Helper.compareEquals(testConfig, "Comparing Transaction Status", expectedStatus, status.getText());
		Map<String, String> map = DataBase.executeSelectQuery(testConfig,
				rowNo, i);
		Helper.compareEquals(testConfig, "Comparing Count values ",
				actualStatus.toLowerCase(),map.get("status").toLowerCase());
		
		}
	}
	
	/**
	 * Method used to perform COD Verfiy/COD Settle/COD Cancel actions by cliking on element(popup)
	 * @param element
	 * @param i
	 * @param actionType
	 */
	public void performPopUpAction(WebElement element,int i,String actionType)
	{
		
		Element.mouseMove(testConfig, element, "Mouse move to get status");
		Browser.wait(testConfig, 1);
		txnIds=Element.getPageElement(testConfig, How.css,"[id=TransactionTable]>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>div>div>table>tbody>tr:nth-child(2)>td:nth-child(2)").getText();
		payuIds=Element.getPageElement(testConfig, How.css,"[id=TransactionTable]>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>div>div>table>tbody>tr:nth-child(3)>td:nth-child(2)").getText();
		testConfig.putRunTimeProperty("txnid", txnIds);
		testConfig.putRunTimeProperty("mihpayid", payuIds);
		element.click();
		element=Element.getPageElement(testConfig, How.css, "[id=TxnInfo]>div:nth-child(2)>div:nth-child(3)>a:nth-of-type(1)");
		if(element!=null)
		{
			if(element.getText().equals("COD Verify"))
			{
				testConfig.logComment("COD Verify is displayed");
			}
			else if(element.getText().equals("COD Settle")){
				testConfig.logComment("COD Settle is displayed");
			}
			
		}
		else{
			testConfig.logComment("COD Settle and COD Verify are not displayed");
			return;
		}
		
			element=Element.getPageElement(testConfig, How.css, "[id=TxnInfo]>div:nth-child(2)>div:nth-child(3)>a:nth-of-type(2)");
		if(element!=null)
		{
			if(element.getText().equals("COD Cancel"))
			{
				testConfig.logComment("COD Cancel is displayed");
			}
		}
		else{
			testConfig.logComment("COD Cancel is not displayed");
		}
		if(!actionType.equals("cancel"))
		{
		element=Element.getPageElement(testConfig, How.css, "[id=TxnInfo]>div:nth-child(2)>div:nth-child(3)>a:nth-of-type(1)");
		if(!element.getText().equals("Refund"))
		{
			element.click();
			
		}
		else{
			testConfig.logComment("breaking the loop as neither cancel ,settle or verify is displayed");
			return;
			
		}
		}
		else{
			Element.getPageElement(testConfig, How.css, "[id=TxnInfo]>div:nth-child(2)>div:nth-child(3)>a:nth-of-type(2)").click();
		}
		
		clickProcessButton();
	}
	
	/**
	 * Method used to perform COD Verfiy/COD Settle/COD Cancel actions by mouse over on element
	 * @param element
	 * @param i
	 * @param actionType
	 */
	public void performMouseMoveAction(WebElement element,int i, String actionType)
	{
		
		Element.mouseMove(testConfig, element, "Mouse move to get status");
		Browser.wait(testConfig, 1);
		txnIds=Element.getPageElement(testConfig, How.css,"[id=TransactionTable]>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>div>div>table>tbody>tr:nth-child(2)>td:nth-child(2)").getText();
		payuIds=Element.getPageElement(testConfig,How.css,"[id=TransactionTable]>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>div>div>table>tbody>tr:nth-child(3)>td:nth-child(2)").getText();
		testConfig.putRunTimeProperty("txnid", txnIds);
		testConfig.putRunTimeProperty("mihpayid", payuIds);
		Element.mouseMove(testConfig, element, "Mouse move to get status");
		Browser.wait(testConfig, 1);
		element=Element.getPageElement(testConfig, How.css, "#TransactionTable>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>div>p>a:nth-child(1)");
		if(element!=null)
		{
			if(element.getText().equals("COD Verify"))
			{
				testConfig.logComment("COD Verify is displayed");
			}
			else if(element.getText().equals("COD Settle"))
			{
				testConfig.logComment("COD Settle is displayed");
			}
			else{
				testConfig.logComment("COD Verify and COD Settle are not displayed");
				return;
			}

		}
		else{
			testConfig.logComment("COD Verify and COD Settle are not displayed");
		}
		element=Element.getPageElement(testConfig, How.css, "#TransactionTable>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>div>p>a:nth-child(2)");
		if(element!=null)
		{
			testConfig.logComment("COD Cancel is  displayed");
		}
		else{
			testConfig.logComment("COD Cancel is not displayed");
		}
		if(actionType.equals("verify") || actionType.equals("settle"))
		{
			Element.getPageElement(testConfig, How.css, "#TransactionTable>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>div>p>a:nth-child(1)").click();
		}
		else if(actionType.equals("cancel")){
			Element.getPageElement(testConfig, How.css, "#TransactionTable>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>div>p>a:nth-child(2)").click();
		}
		else{
			testConfig.logComment("Element does not contain verify or cancel button");
			return;
		}
		
		clickProcessButton();
	}
	
	/**
	 * Method used to perform COD Verfiy/COD Settle/COD Cancel actions by clicking checkbox
	 * @param element
	 * @param i
	 * @param actionType
	 */
	public void performCheckBoxAction(WebElement element,int i)
	{
		Element.mouseMove(testConfig, element, "Mouse move to get status");
		Browser.wait(testConfig, 1);
		txnIds=txnIds+","+Element.getPageElement(testConfig, How.css,"[id=TransactionTable]>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>div>div>table>tbody>tr:nth-child(2)>td:nth-child(2)").getText();
		payuIds=payuIds+","+Element.getPageElement(testConfig,How.css,"[id=TransactionTable]>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>div>div>table>tbody>tr:nth-child(3)>td:nth-child(2)").getText();
		if(txnIds.charAt(0)==',' && payuIds.charAt(0)==',' )
		{
			txnIds=txnIds.replaceFirst(",", "").trim();
			payuIds=payuIds.replaceFirst(",", "").trim();
		}
		testConfig.putRunTimeProperty("txnid", txnIds);
		testConfig.putRunTimeProperty("mihpayid", payuIds);		
		Element.getPageElement(testConfig, How.css,"[id=TransactionTable]>table>tbody>tr:nth-child("+i+")>td:nth-child(2)>div>span").click();
	}
	
	/** Verifies options present in Action Dropdown columns
	 * CLicks on action dropdown
	 * Verifies specified names of action options are present
	 * @param optionsToBePresntInActionDropdown - Name of the options to be dropdown
	 */
	public void verifySelectActionDropdown(
			List<String> optionsToBePresntInActionDropdown) {
		actionDropDwn.click();
		for (String ActionOption : optionsToBePresntInActionDropdown) {
			switch (ActionOption) {
			case "COD Cancel":
				Element.verifyElementPresent(testConfig, codCancel, "COD Cancel Action Option");
				break;
			case "Cancel Transactions":
				Element.verifyElementPresent(testConfig, cancel, "Cancel Action Option");
				break;
			case "COD Verify":
				Element.verifyElementPresent(testConfig, codverify, "COD Verify Action Option");
				break;
			case "Refund":
				Element.verifyElementPresent(testConfig, refund, "Refund Action Option");
				break;
			case "Capture":
				Element.verifyElementPresent(testConfig, capture, "Capture Action Option");
				break;
			case "COD Settled":
				Element.verifyElementPresent(testConfig, codSettle, "COD Settle Action Option");
				break;
			case "Generate Invoice":
				Element.verifyElementPresent(testConfig, generateInvoice, "Generate option Action Option");
				break;
			}
		}
	}
	
	
	/**Clicks on Customize Columns button
	 * @return Customize Columns Page
	 */
	public CustomizeColumnsPage clickOnCustomizeColumn(){
		Element.click(testConfig, CustomizeColumnButton, "Customize Column Button");
		return new CustomizeColumnsPage(testConfig);
	}
	/**Verifies action dropdown values to be present on transaction page
	 * @param accessRoles - Name of options that are to be present in Action Dropdown
	 */
	public void verifyActionDropdownValues(ArrayList<String> accessRoles) {
		List<String> OptionsToBePresntInActionDropdown = new ArrayList<>();
		for (String accessRole: accessRoles) {
			switch (accessRole) {
			case "COD Cancel":
				OptionsToBePresntInActionDropdown.add("COD Cancel");
				break;
			case "Cancel Transactions":
				OptionsToBePresntInActionDropdown.add("Cancel");
				break;
			case "COD Verify":
				OptionsToBePresntInActionDropdown.add("COD Verify");
				break;
			case "Refund":
				OptionsToBePresntInActionDropdown.add("Refund");
				break;
			case "Capture":
				OptionsToBePresntInActionDropdown.add("Capture");
				break;
			case "COD Settled":
				OptionsToBePresntInActionDropdown.add("COD Settle");
				break;
			case "Generate Invoice":
				OptionsToBePresntInActionDropdown.add("Generate Invoice");
				break;
			case "super":
				OptionsToBePresntInActionDropdown.addAll(Arrays.asList("COD Cancel","Cancel",
						"COD Verify","Refund","Capture","COD Settle","Generate Invoice"));
				
			}
			
		}
		TransactionsPage transactionsPage = new TransactionsPage(testConfig);
		transactionsPage.verifySelectActionDropdown(OptionsToBePresntInActionDropdown);
		
	}
	
	/**
	 *Verifies Certain Customization options are not present on filter options 
	 */
	public void verifyCustomizationOptionsNotPresent() {
		List<String> CheckboxesThatShouldNotBePesent = new ArrayList<String>(
				Arrays.asList("Last Name","Customer Name","Customer Email","Customer Phone","Name On Card"));
	
		clickOnCustomizeColumn().verifyOptionsNotPresentInTheList(
				CheckboxesThatShouldNotBePesent);
	}
	/**
	 * Verifying transaction details with test Data
	 * 
	 * @param rowNo
	 * @return
	 * @throws InterruptedException
	 */
	public void compareTransactionDetailsWithTestData(int rowNo) {

		// getting all the values of the transactions
		Element.click(testConfig, txnID, "Click element");
		Browser.wait(testConfig, 2);
		String amount = transAmount.getText();
		String[] temp = amount.split("\n");
		amount = temp[0].trim();
		String discount = transDiscount.getText();

		// Payment Details

		String bankRefNo = transRefNo.getText();

		if (bankRefNo.equals("NA")) {
			bankRefNo = "";
		}
		String nameOnCard = nameoncard.getText();
		String firstName = firstname.getText();

		if (firstName.equals("NA")) {
			firstName = "";
		}
		String lastName = lastname.getText();
		if (lastName.equals("NA")) {
			lastName = "";
		}

		// Customer Details
		String city = City.getText();
		if (city.equals("NA")) {
			city = "";
		}

		String state = State.getText();
		if (state.equals("NA")) {
			state = "";
		}
		String zipcode = zipCode.getText();
		if (zipcode.equals("NA")) {
			zipcode = "";
		}
		String email = Email.getText();
		String phone = Phone.getText();

		TestDataReader reader = new TestDataReader(testConfig,
				"TransactionDetails");
		TestDataReader cardreader = new TestDataReader(testConfig,
				"CardDetails");
		Browser.wait(testConfig, 2);
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", amount,
				reader.GetData(rowNo, "amount"));
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", email,
				reader.GetData(rowNo, "email"));
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", firstName,
				reader.GetData(rowNo, "firstname"));
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", phone,
				reader.GetData(rowNo, "phone"));
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", lastName,
				reader.GetData(rowNo, "lastname"));
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", city,
				reader.GetData(rowNo, "city"));
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", state,
				reader.GetData(rowNo, "state"));
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", discount.trim(),
				reader.GetData(rowNo, "discount"));
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", nameOnCard,
				cardreader.GetData(1, "Name"));
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", zipcode,
				reader.GetData(rowNo, "zipcode"));

	}
	/**
	 * Comparing the no of records present in DB with the result
	 * 
	 * @param rowNo in data sheet which is gets query to get data based on query
	 * @return
	 */
	public void compareRecordsWithDB(int rowNo) {
		
		
		String count=getNoOfRecords();
		if(!count.equals(""))
		{
			Map<String, String> map = DataBase.executeSelectQuery(testConfig,
					rowNo, 1);

			//if we are running query in transaction table then result will be coming in map.get("COUNT(*)") 
			if(map.get("COUNT(*)")!=null)
			{
			Helper.compareEquals(testConfig, "Comparing Count values ",
					map.get("COUNT(*)"), count);
			}
			//if we are running query in transaction update  table then result will be coming in map.get("count(*)")
			else{
				Helper.compareEquals(testConfig, "Comparing Count values ",
						map.get("count(*)"), count);
			}
			
		}
	}	

	/**
	 * Method used to get total no of records
	 * @return
	 */
	public String getNoOfRecords()
	{	
		String  count="";
		if (Element.IsElementDisplayed(testConfig, recordsCount)) {
			 count = recordsCount.getText();
			String[] splitValues = count.split(" ");
			count = splitValues[splitValues.length - 1].trim();

			return count;
		}
		return count;
	}
	
	/**
	 * All Transactions for Last 3 Days should be displayed.
	 */
	public void verifyDateRange(int expectedDateRange)
	{
		Element.click(testConfig, date, "");
		int range=0;	
		List<WebElement> list=Element.getListOfElements(testConfig, How.xPath, ".//*[@id='ng-app']/body/div[8]/div[2]/div/table/tbody/tr/td");

		for(int i=0;i<list.size();i++)
		{
			System.out.println(list.get(i).getAttribute("class"));
			if(list.get(i).getAttribute("class").contains("in-range"))
			{
				range++;
			}
		}
		
	Helper.compareEquals(testConfig, "Date range",expectedDateRange,range);
		
	}
	
	 /**
	  * Method used to click on pagination and count total no of transactions in table
	  */
	 public void clickPaginationAndCountTotalElements()
	 {
		 List<WebElement> list=Element.getListOfElements(testConfig, How.css, "#Pagination>ul>li>a");
		 List<WebElement> elementsList;
		 int totalNo=Integer.parseInt(getNoOfRecords());
		 int count=0;
		 int lastPageCount=totalNo%20;
			for(int i=2;i<list.size()-3;i++)
			{
				Element.getPageElement(testConfig, How.css, "#TransactionTable>table>tbody>tr").isDisplayed();
				 elementsList=Element.getListOfElements(testConfig, How.css, "#TransactionTable>table>tbody>tr");
				 if(i==list.size()-4)
				 {
					 if(elementsList.size()==lastPageCount)
					 {
						 count=count+lastPageCount;
						 break;
					 }
				 }
				 else{
					 if(elementsList.size()==20)
					 {
						 count=count+20;
					 }
					 else{
						 count=count+elementsList.size();
					 }
				 }
				Element.getPageElement(testConfig, How.css, "#Pagination>ul>li:nth-child("+(i+2)+")>a").click();
				Browser.wait(testConfig, 2);					 
			}
			Helper.compareEquals(testConfig, "Counting the total no of elements", totalNo,count);
	 }

}
