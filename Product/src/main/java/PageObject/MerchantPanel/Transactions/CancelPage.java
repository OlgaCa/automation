package PageObject.MerchantPanel.Transactions;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class CancelPage extends TransactionFilterPage{
	private Config testConfig;

	@FindBy(name="transactions[1]")
	private WebElement checkbox;

	@FindBy(css="div[class='list-div']>table>tbody")
	private WebElement firstTransaction;

	@FindBy(name="qterm")
	private WebElement search;

	@FindBy(css="input[type=\"submit\"]")
	private WebElement clickGo;

	@FindBy(id="cancel")
	private WebElement cancelButton; 

	@FindBy(name="bulkcancel")
	private WebElement bulkCancelBox; 

	@FindBy(id="cancel_button")
	private WebElement closeButton;

	@FindBy(css="div>div>div>div>div>div>div~div")
	private WebElement invalidFileMsg;

	@FindBy(css="body")
	private WebElement invalidFileExtMsg;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(4)")
	private WebElement txnId;

	@FindBy(css="div>table>tbody>tr>td:nth-child(5)")
	private WebElement payuId;

	@FindBy(css="div>table>tbody>tr>td:nth-child(6)")
	private WebElement customerName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(7)")
	private WebElement amount; 

	@FindBy(css="div>table>tbody>tr>td:nth-child(8)")
	private WebElement status;

	@FindBy(css="div>table>tbody>tr>td:nth-child(9)")
	private WebElement bankName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(10)")
	private WebElement reqAmount;

	@FindBy(css="div>table>tbody>tr>td:nth-child(11)")
	private WebElement discount;

	@FindBy(css="div>table>tbody>tr>td:nth-child(12)")
	private WebElement merchantName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(13)")
	private WebElement paymentGateway;

	@FindBy(css="div>table>tbody>tr>td:nth-child(14)")
	private WebElement bankRefNo;

	@FindBy(css="div>table>tbody>tr>td:nth-child(15)")
	private WebElement lastName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(16)")
	private WebElement eMail;

	@FindBy(css="div>table>tbody>tr>td:nth-child(17)")
	private WebElement ipAddress;

	@FindBy(css="div>table>tbody>tr>td:nth-child(18)")
	private WebElement cardNo;

	@FindBy(css="div>table>tbody>tr>td:nth-child(19)")
	private WebElement field2;

	@FindBy(css="div>table>tbody>tr>td:nth-child(20)")
	private WebElement paymentType;

	@FindBy(css="div>table>tbody>tr>td:nth-child(21)")
	private WebElement errorCode;

	@FindBy(css="div>table>tbody>tr>td:nth-child(22)")
	private WebElement offerKey;

	@FindBy(css="div>table>tbody>tr>td:nth-child(23)")
	private WebElement PGMID;

	@FindBy(css="div>table>tbody>tr>td:nth-child(24)")
	private WebElement offerFailurereason;

	@FindBy(css="div>table>tbody>tr>td:nth-child(25)")
	private WebElement offerType;
	
	@FindBy(xpath="//input[@name='request_ref_id']")
	private WebElement requestRefId;

	@FindBy(xpath="//input[@value='Download']")
	private WebElement downloadButton;

	@FindBy(linkText="Download summary")
	private WebElement downloadSummary;



	public CancelPage(Config testConfig)
	{   super(testConfig);
	this.testConfig = testConfig;
	PageFactory.initElements(this.testConfig.driver, this);
	Browser.waitForPageLoad(this.testConfig, cancelButton);

	}

	public void SearchTransaction(String transactionId) {
		Element.enterData(testConfig, search,transactionId, "Search the transaction");
		Element.click(testConfig, clickGo, "Click on Go, to search the transaction");
		RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
		confirm.ClickActionButton();
	}

	public void CancelFirstTransaction()
	{   
		Element.check(testConfig, checkbox, "Click on check box");
		Element.click(testConfig, cancelButton, "Click on Cancel Button");
		RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
		confirm.ClickActionButton();

	}
	public void CancelBulkTransaction()
	{   
		Element.click(testConfig, cancelButton, "Click on Refund Button");
		Boolean isPass = closeButton.isDisplayed();
		if(isPass) {		
			Element.click(testConfig, closeButton, "Close Button on request processing Popup");
			Browser.switchToNewWindow(this.testConfig);
		}
	}
	/* Get Expected transaction details from Data sheet
	 * Get refund transaction details from DB
	 * Compare actual details with expected
	 */
	public void VerifyCancelTransaction(int requestRow, int transactionRow)
	{	

		TestDataReader txnData = new TestDataReader(testConfig, "TransactionDetails");
		TestDataReader refundData = new TestDataReader(testConfig, "ActionRequest");
		String ExpectedStatus = refundData.GetData(requestRow, "Status");
		String ExpectedAction = refundData.GetData(requestRow, "Action");
		String ExpectedMerchantId= txnData.GetData(transactionRow, "merchantid");
		String ExpectedAmount=txnData.GetData(transactionRow, "amount");
		String transactionId=testConfig.getRunTimeProperty("transactionId");

		testConfig.putRunTimeProperty("txnid", transactionId);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 1, 1);
		String payUId =map.get("mihpayid");
		testConfig.putRunTimeProperty("payUid", payUId);
		Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 4, -1);
		if(map1.get("action").contentEquals("cancel"))
		{
			Helper.compareEquals(testConfig, "Cancel status", ExpectedStatus, map1.get("status"));
			Helper.compareEquals(testConfig, "Cancel action", ExpectedAction, map1.get("action"));
			Helper.compareEquals(testConfig, "Cancel amount", ExpectedAmount, map1.get("amount"));
			Helper.compareEquals(testConfig, "Cancel merchantId", ExpectedMerchantId, map1.get("merchant_id"));
		}
		else
		{
			testConfig.logFail("Cancel entry not found in txn_update");
		}

	}
	public void BulkFileupload(String fileName) {
		testConfig.logComment("upload bulk refund file");
		Element.enterFileName(testConfig, bulkCancelBox, "C:\\Users\\"+System.getProperty("user.name")+"\\Downloads\\"+fileName, "Bulk File upload");
	}

	public void BulkInvalidUpload(String fileName) {
		testConfig.logComment("upload csv file");
		Element.enterFileName(testConfig, bulkCancelBox, "C:\\Payu\\Product\\Parameters\\"+fileName, "Bulk Invalid Upload");
	}

	public void RefundBInvalidTransaction(){

		Element.click(testConfig, cancelButton, "Click on Refund Button");
		Browser.switchToNewWindow(this.testConfig);
	}

	public String getInvalidFileText(){

		return invalidFileMsg.getText();
	}

	public String getInvalidFileFormatText(){

		return invalidFileExtMsg.getText();
	}
	 public String getTxnId(){
		 return txnId.getText();
	 }
	 public String getPayuId(){
		 return payuId.getText();
	 }
	 public String getCustomerName(){
		 return customerName.getText();
	 }
	 public String getAmount(){
		 return amount.getText();
	 }
	 public String getField2(){
		 return field2.getText();
	 }
	 public String getcardNo(){
		 return cardNo.getText();
	 }
	 public String getStatus(){
		 return status.getText();
	 }
	 public String getBankname(){
		 return bankName.getText();
	 }
	 public String getReqamount(){
		 return reqAmount.getText();
	 }
	 public String getDiscount(){
		 return discount.getText();
	 }
	 public String getMerchantname(){
		 return merchantName.getText();
	 }
	 public String getPG(){
		 return paymentGateway.getText();
	 }
	 public String getBankRefno(){
		 return bankRefNo.getText();
	 }
	 public String getEmail(){
		 return eMail.getText();
	 }
	 public String getLastname(){
		 return lastName.getText();
	 }
	 public String getIPaddress(){
		 return ipAddress.getText();
	 }
	 public String getPaymenttype(){
		 return paymentType.getText();
	 }
	 public String getErrorcode(){
		 return errorCode.getText();
	 }
	 public String getOfferKey(){
		 return offerKey.getText();
	 }
	 public String getPGMID(){
		 return PGMID.getText();
	 }
	 public String getOfferfailReason(){
		 return offerFailurereason.getText();
	 }
	 public String getofferType(){
		 return offerType.getText();
	 }

	 public void VerifyAuthtransaction(int PaymentTypeRow, int TransactionRow, int PGinfoRow, String ExpectedAmount,TransactionHelper tranHelper)
	 {	
		 TestDataReader PGData = new TestDataReader(testConfig, "PGInfo");
		 String ExpectedMerchantName= tranHelper.transactionData.GetData(TransactionRow, "Comments");
		 String ExpectedReqAmt= tranHelper.transactionData.GetData(TransactionRow, "amount");
		 String ExpectedName= tranHelper.transactionData.GetData(TransactionRow, "firstname");
		 String ExpectedEmail= tranHelper.transactionData.GetData(TransactionRow, "email");
		 String ExpectedPaymenttype= tranHelper.paymentTypeData.GetData(PaymentTypeRow, "mode");
		 String ExpectedBankname= tranHelper.paymentTypeData.GetData(PaymentTypeRow, "Bank Name");
		 String ExpectedPGtype= tranHelper.paymentTypeData.GetData(PaymentTypeRow, "PG_TYPE");
		 String ExpectedPGMID= PGData.GetData(PGinfoRow, "PGMID");


		 switch(ExpectedPaymenttype)
		 {
		 case "creditcard": ExpectedPaymenttype = "Credit Card";
		 break;

		 case "debitcard": ExpectedPaymenttype = "Debit Card";
		 break;
		 }




		 String actualBankName= bankName.getText();
		 String actualAmount= amount.getText();
		 String actualDiscount= discount.getText();
		 String actualMerName= merchantName.getText();
		 String actualTxnId= txnId.getText();
		 String actualReqAmt= reqAmount.getText();
		 String actualFname= customerName.getText();
		 String actualLname= lastName.getText();
		 String actualEmail= eMail.getText();
		 String actualPaymentType= paymentType.getText();
		 String actualCardNo= cardNo.getText();
		 String actualpayuId= payuId.getText();
		 String actualPG= paymentGateway.getText();
		 String actualIP= ipAddress.getText();
		 String actualField2= field2.getText();
		 String actualPGMID= PGMID.getText();
		 String actualofferFailreason= offerFailurereason.getText();
		 String actualOfferType= offerType.getText();


		 String transactionId=testConfig.getRunTimeProperty("transactionId");
		 testConfig.putRunTimeProperty("txnid", transactionId);
		 Map<String,String> map = DataBase.executeSelectQuery(testConfig, 1, 1);
		 String payUId =map.get("mihpayid");
		 testConfig.putRunTimeProperty("payUid", payUId);

		 InetAddress ip;
		 String IP = null;
		 try {

			 ip = InetAddress.getLocalHost();
			 IP= (ip.getHostAddress()).toString();
		 } catch (UnknownHostException e) {

			 testConfig.logException(e);
		 }

		 Helper.compareEquals(testConfig, "Card no", tranHelper.testResponse.actualResponse.get("cardnum"),actualCardNo);
		 Helper.compareEquals(testConfig, "PayU id", payUId,actualpayuId);
		 Helper.compareEquals(testConfig, "Field2",tranHelper.testResponse.actualResponse.get("field2"), actualField2);
		 Helper.compareEquals(testConfig, "Bank Name", ExpectedBankname, actualBankName);
		 Helper.compareEquals(testConfig, "Amount", ExpectedAmount, actualAmount);
		 Helper.compareEquals(testConfig, "Req Amount", ExpectedReqAmt, actualReqAmt);
		 Helper.compareEquals(testConfig, "Discount", "0.00", actualDiscount);
		 Helper.compareEquals(testConfig, "Merchant name", ExpectedMerchantName, actualMerName);
		 Helper.compareEquals(testConfig, "transaction id", transactionId, actualTxnId);
		 Helper.compareEquals(testConfig, "First name", ExpectedName, actualFname);
		 Helper.compareEquals(testConfig, "Last name", "-", actualLname);
		 Helper.compareEquals(testConfig, "Email Id", ExpectedEmail, actualEmail);
		 Helper.compareEquals(testConfig, "Payment type", ExpectedPaymenttype, actualPaymentType);
		 Helper.compareEquals(testConfig, "Payment gateway", ExpectedPGtype, actualPG);
		 Helper.compareEquals(testConfig, "IP address", IP, actualIP);
		 Helper.compareEquals(testConfig, "PGMID", ExpectedPGMID, actualPGMID);
		 Helper.compareEquals(testConfig, "Offer failure reason", "-", actualofferFailreason);
		 Helper.compareEquals(testConfig, "Offer type", "-", actualOfferType);

	 }
	 
		
	 public void verifyRefundFailureExcelSheet(String refId)
	 {
		 Browser.wait(testConfig,10);
		 String refundFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		 //File file = Browser.lastFileModified(testConfig,refundFilePath);
		 String fileName = "cancel_summary_" + refId + ".xls";
		 TestDataReader tr = new TestDataReader(testConfig,"Sheet1", refundFilePath+fileName);
		 
		 //verify transaction id, reference number, amount and failure message
		 Helper.compareEquals(testConfig, "verify transaction id", testConfig.getRunTimeProperty("mihpayid"), tr.GetData(1, "transactionid"));
		 Helper.compareEquals(testConfig, "verify reference id in excel sheet", refId, tr.GetData(1, "reference_id"));
		 Helper.compareEquals(testConfig, "verify failure error message in excel sheet", "REJECT - Request rejected on reconfirmation", tr.GetData(1, "result"));
		 if(Element.IsElementDisplayed(testConfig, downloadSummary))
			{
				Element.click(testConfig, closeButton, "Close popup");
			}
			Browser.wait(testConfig, 1);


	 }
	 
	 public void verifyBulkRefundFailureExcelSheet(String refId)
	 {
		 Browser.wait(testConfig,10);
		 String refundFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		 //File file = Browser.lastFileModified(testConfig,refundFilePath);
		 String fileName = "refund_summary_" + refId + ".xls";

		 TestDataReader tr = new TestDataReader(testConfig,"Sheet1", refundFilePath+fileName);
		 //TestDataReader tr = new TestDataReader(testConfig,"Sheet1","C://Users//vidya.priyadarshini//Downloads//refund_summary_5138a47fa0793.xls");

		 //verify transaction id, reference number, amount and failure message
		 Helper.compareEquals(testConfig, "verify reference id in excel sheet", refId, tr.GetData(1, "reference_id"));
		 Helper.compareEquals(testConfig, "verify amount in excel sheet", "100.00",tr.GetData(1, "amount"));
		 Helper.compareEquals(testConfig, "verify failure error message in excel sheet", "FAILURE - Transaction Not Found", tr.GetData(1, "result"));

	 }
	 
	 /*public void RefundFailureTransaction()
		{   
		    Element.check(testConfig, checkbox, "Click on check box");
			Element.click(testConfig, cancelButton, "Click on cancel Button");
			Browser.wait(testConfig,5);
			downloadSummaryPage confirm = new downloadSummaryPage(testConfig);
			confirm.ClickAction();
			Browser.wait(testConfig,5);

		}*/
	 
	 public void RefundFailureTransaction()
		{
			Element.check(testConfig, checkbox, "First transaction");
			Element.click(testConfig, cancelButton, "Click on cancel Button");
			Browser.wait(testConfig,5);
			RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
			confirm.ClickActionButton();
			downloadSummaryPage confirm1 = new downloadSummaryPage(testConfig);
			confirm1.ClickAction();
			if(Element.IsElementDisplayed(testConfig, downloadSummary))
			{
				Element.click(testConfig, closeButton, "Close popup");
			}
			Browser.wait(testConfig, 1);

		}

		/**
		 * Download Excel sheet (Download Summary)
		 * @param referenceId
		 */
		public void downloadCancelSummary(String referenceId)
		{
			String flag = "false";
			int retries = 60;
			do {
				Element.enterData(testConfig, requestRefId, referenceId, "Reference Id of transaction");
				Element.click(testConfig, downloadButton, "download button");
				if(Element.IsElementDisplayed(testConfig, downloadSummary))
				{
					Element.click(testConfig, downloadSummary, "Download excel sheet");
					//Popup.saveFileToDownloadsFolder();

					flag = "true";
					break;
				}
				else {
					Element.click(testConfig, closeButton, "Close popup");
				}
				Browser.wait(testConfig, 2);
				retries --;
				if(retries == 0 &&  flag.contentEquals("false")) 
					{
					testConfig.logFail("Could not download cancel summary for reference id - " + referenceId);
					break;
					}
			} while(flag.contentEquals("false"));

		}
	


}
