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
import Utils.Element.How;

public class RefundPage extends TransactionFilterPage {
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

	@FindBy(id="refund_button")
	private WebElement refundButton; 

	@FindBy(name="bulkrefund")
	private WebElement bulkRefundBox; 

	@FindBy(id="cancel_button")
	private WebElement closeButton;

	@FindBy(xpath="//input[@name='request_ref_id']")
	private WebElement requestRefId;

	@FindBy(xpath="//input[@value='Download']")
	private WebElement downloadButton;

	@FindBy(linkText="Download summary")
	private WebElement downloadSummary;

	@FindBy(css="div>div>div>div>div>div>div~div")
	private WebElement invalidFileMsg;

	@FindBy(css="body")
	private WebElement invalidFileExtMsg;

	@FindBy(linkText="Download summary")
	private WebElement summaryDownloadLink;

	@FindBy(id="general_popup_content")
	private WebElement genPopup;

	@FindBy(css="div>table>tbody>tr>td:nth-child(4)")
	private WebElement date;

	@FindBy(css="div>table>tbody>tr>td:nth-child(5)")
	private WebElement txnId;

	@FindBy(css="div>table>tbody>tr>td:nth-child(6)")
	private WebElement payuId;

	@FindBy(css="div>table>tbody>tr>td:nth-child(7)")
	private WebElement customerName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(8)")
	private WebElement amount; 

	@FindBy(css="div>table>tbody>tr>td:nth-child(9)")
	private WebElement status;

	@FindBy(css="div>table>tbody>tr>td:nth-child(10)")
	private WebElement bankName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(11)")
	private WebElement reqAmount;

	@FindBy(css="div>table>tbody>tr>td:nth-child(12)")
	private WebElement discount;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(13)")
	private WebElement additionalCharge;

	@FindBy(css="div>table>tbody>tr>td:nth-child(14)")
	private WebElement merchantName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(15)")
	private WebElement paymentGateway;

	@FindBy(css="div>table>tbody>tr>td:nth-child(16)")
	private WebElement bankRefNo;

	@FindBy(css="div>table>tbody>tr>td:nth-child(17)")
	private WebElement lastName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(18)")
	private WebElement eMail;

	@FindBy(css="div>table>tbody>tr>td:nth-child(19)")
	private WebElement ipAddress;

	@FindBy(css="div>table>tbody>tr>td:nth-child(20)")
	private WebElement cardNo;

	@FindBy(css="div>table>tbody>tr>td:nth-child(22)")
	private WebElement field2;

	@FindBy(css="div>table>tbody>tr>td:nth-child(23)")
	private WebElement paymentType;

	@FindBy(css="div>table>tbody>tr>td:nth-child(24)")
	private WebElement errorCode;

	@FindBy(css="div>table>tbody>tr>td:nth-child(26)")
	private WebElement offerKey;

	@FindBy(css="div>table>tbody>tr>td:nth-child(25)")
	private WebElement PGMID;

	@FindBy(css="div>table>tbody>tr>td:nth-child(27)")
	private WebElement offerFailurereason;

	@FindBy(css="div>table>tbody>tr>td:nth-child(28)")
	private WebElement offerType;

	public RefundPage(Config testConfig)
	{
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, refundButton);
	}
	public void RefundBulkTransaction()
	{   
		Element.click(testConfig, refundButton, "Click on Refund Button");
		Boolean isPass = closeButton.isDisplayed();
		if(isPass) {		
			Element.click(testConfig, closeButton, "Close Button on request processing Popup");
		//	Browser.switchToNewWindow(this.testConfig);
		}

	}
	
	public void RefundFirstTransaction(String amount)
	{   
		Element.check(testConfig, checkbox, "First transaction");
		Element.enterData(testConfig, amountInputbox, amount, "Amount to refund");
		Element.click(testConfig, refundButton, "Click on Refund Button");
		RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
		confirm.ClickActionButton();
		confirm.executeCron("processRefund.php");
	}

	/**
	 * Does a failed refund and returns the failure reason
	 * @param amount
	 */
	public String RefundTransactionToGetFailure(String amount)
	{   
		Element.check(testConfig, checkbox, "First transaction");
		Element.enterData(testConfig, amountInputbox, amount, "Amount to refund");
		Element.click(testConfig, refundButton, "Click on Refund Button");
		RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
		String text =  confirm.ClickActionButtonToGetFailure();
		confirm.executeCron("processRefund.php");
		return text;
	}

	/* Get Expected transaction details from Data sheet
	 * Get refund transaction details from DB
	 * Compare actual details with expected
	 */
	public void VerifyRefundTransaction(int requestRow, String ExpectedAmount)
	{	
		TestDataReader txnData = new TestDataReader(testConfig, "TransactionDetails");
		TestDataReader refundData = new TestDataReader(testConfig, "ActionRequest");
		String ExpectedStatus = refundData.GetData(requestRow, "Status");
		String ExpectedAction = refundData.GetData(requestRow, "Action");
		String ExpectedMerchantId= txnData.GetData(requestRow, "merchantid");

		String transactionId=testConfig.getRunTimeProperty("transactionId");
		testConfig.putRunTimeProperty("txnid", transactionId);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 1, 1);
		String payUId =map.get("mihpayid");
		testConfig.putRunTimeProperty("payUid", payUId);
		Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 4, -1);

		if(map1.get("action").contentEquals("refund"))
		{
			Helper.compareEquals(testConfig, "Refund status", ExpectedStatus, map1.get("status"));
			Helper.compareEquals(testConfig, "Refund action", ExpectedAction, map1.get("action"));
			Helper.compareEquals(testConfig, "Refund amount", ExpectedAmount, map1.get("amount"));
			Helper.compareEquals(testConfig, "Refund merchantId", ExpectedMerchantId, map1.get("merchant_id"));
			Helper.compareEquals(testConfig, "Bank ref id", "", map1.get("bank_ref_no"));	
		}
		else
		{
			testConfig.logFail("Refund entry not found in txn_update");
		}
	}

	public void fullRefundFirstTransaction()
	{
		Element.check(testConfig, checkbox, "First transaction");
		Element.click(testConfig, refundButton, "Click on Refund Button");
		RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
		confirm.ClickActionButton();
		confirm.executeCron("processRefund.php");

	}
	public void RefundMultipleBulkTransaction()
	{

		Element.click(testConfig, refundButton, "Click on Refund Button");

		RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
		confirm.ClickAction();
		confirm.executeCron("processRefund.php");

	}

	public void RefundFailureBulkTransaction()
	{   

		Element.click(testConfig, refundButton, "Click on Refund Button");
		Browser.wait(testConfig,5);
		Boolean isPass = closeButton.isDisplayed();
		if(isPass) {		
			Element.click(testConfig, closeButton, "Close Button on request processing Popup");
		//	Browser.switchToNewWindow(this.testConfig);

		}

		downloadSummaryPage confirm = new downloadSummaryPage(testConfig);

		confirm.ClickAction();
		confirm.executeCron("processRefund.php");

	}
	public void RefundFailureTransaction()
	{   

		Element.click(testConfig, refundButton, "Click on Refund Button");
		downloadSummaryPage confirm = new downloadSummaryPage(testConfig);

		confirm.ClickAction();
		confirm.executeCron("processRefund.php");

	}

	/**
	 * Download Excel sheet (Download Summary)
	 * @param referenceId
	 */
	public void downloadRefundSummary(String referenceId)
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
			}
			else {
				Element.click(testConfig, closeButton, "Close popup");
			}
			Browser.wait(testConfig, 2);
			retries --;
			if(retries == 0 &&  flag.contentEquals("false")) 
			{
				testConfig.logFail("Could not download refund summary for reference id - " + referenceId);
				break;
			}
		} while(flag.contentEquals("false"));

	}

	/**
	 * 
	 * @param requestRow
	 * @param ExpectedAmount
	 * @param oD
	 */ public void verifyRefundFailureExcelSheet(String refId)
	 {
		 Browser.wait(testConfig,10);
		 String refundFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		 //File file = Browser.lastFileModified(testConfig,refundFilePath);
		 String fileName = "refund_summary_" + refId + ".xls";

		 TestDataReader tr = new TestDataReader(testConfig,"Sheet1", refundFilePath+fileName);

		 //verify transaction id, reference number, amount and failure message
		 Helper.compareEquals(testConfig, "verify transaction id", testConfig.getRunTimeProperty("mihpayid"), tr.GetData(1, "transactionid"));
		 Helper.compareEquals(testConfig, "verify reference id in excel sheet", refId, tr.GetData(1, "reference_id"));
		 Helper.compareEquals(testConfig, "verify amount in excel sheet", testConfig.getRunTimeProperty("failureAmount"),tr.GetData(1, "amount"));
		 Helper.compareEquals(testConfig, "verify failure error message in excel sheet", "FAILURE - Invalid amount", tr.GetData(1, "result"));

	 }

	 public void verifyBulkRefundFailureExcelSheet(String refId)
	 {
		 Browser.wait(testConfig,10);
		 String refundFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		 String fileName = "refund_summary_" + refId + ".xls";

		 TestDataReader tr = new TestDataReader(testConfig,"refund_summary_" + refId, refundFilePath+fileName);
		 //verify reference number, amount and failure message

		 Helper.compareEquals(testConfig, "verify reference id in excel sheet", refId, tr.GetData(1, "reference_id"));
		 Helper.compareEquals(testConfig, "verify amount in excel sheet", "1.00",tr.GetData(1, "amount"));
		 Helper.compareEquals(testConfig, "verify failure error message in excel sheet", "FAILURE - Transaction Not Found", tr.GetData(1, "result"));

	 }
	 public void verifymultipleBulkRefundExcelsheet(String refId)
	 {
		 Browser.wait(testConfig,10);
		 String refundFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		 String fileName = "refund_summary_" + refId + ".xls";

		 TestDataReader tr = new TestDataReader(testConfig,"refund_summary_" + refId, refundFilePath+fileName);

		 //verify transaction id, reference number, amount and failure message
		 Helper.compareEquals(testConfig, "verify transaction id", testConfig.getRunTimeProperty("mihpayid"), tr.GetData(1, "transactionid"));
		 Helper.compareEquals(testConfig, "verify reference id in excel sheet", refId, tr.GetData(1, "reference_id"));
		 Helper.compareEquals(testConfig, "verify failure error message in excel sheet", "SUCCESS", tr.GetData(1, "result"));
		 Helper.compareEquals(testConfig, "verify reference id in excel sheet", refId, tr.GetData(2, "reference_id"));
		 Helper.compareEquals(testConfig, "verify amount in excel sheet", "1.00",tr.GetData(2, "amount"));
		 Helper.compareEquals(testConfig, "verify failure error message in excel sheet", "FAILURE - Transaction Not Found", tr.GetData(2, "result"));

	 }


	 /* Get Expected transaction details from Data sheet
	  * Get refund transaction details from DB
	  * Compare actual details with expected
	  */
	 public void VerifyRefundTransaction(int transactionRowNum,int requestRow, String ExpectedAmount, String oD)
	 {	
		 TestDataReader txnData = new TestDataReader(testConfig, "TransactionDetails");
		 TestDataReader refundData = new TestDataReader(testConfig, "ActionRequest");
		 String ExpectedStatus = refundData.GetData(requestRow, "Status");
		 String ExpectedAction = refundData.GetData(requestRow, "Action");
		 String ExpectedMerchantId= txnData.GetData(transactionRowNum, "merchantid");

		 if(oD.contentEquals("false"))
		 {	
			 String transactionId=testConfig.getRunTimeProperty("transactionId");
			 testConfig.putRunTimeProperty("txnid", transactionId);
			 Map<String,String> map = DataBase.executeSelectQuery(testConfig, 1, 1);
			 String payUId =map.get("mihpayid");
			 testConfig.putRunTimeProperty("payUid", payUId);
		 }
		 Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 4, -1);

		 if(map1.get("action").contentEquals("refund"))
		 {
			 String amount = map1.get("amount");
			 Double amt = Double.parseDouble(amount);
			 Helper.compareEquals(testConfig, "Refund status", ExpectedStatus, map1.get("status"));
			 Helper.compareEquals(testConfig, "Refund action", ExpectedAction, map1.get("action"));
			 Helper.compareEquals(testConfig, "Refund amount", ExpectedAmount, String.format("%.2f", amt));
			 Helper.compareEquals(testConfig, "Refund merchantId", ExpectedMerchantId, map1.get("merchant_id"));
			// Helper.compareEquals(testConfig, "Bank ref id", "", map1.get("bank_ref_no"));		
		 }
		 else
		 {
			 testConfig.logFail("Refund entry not found in txn_update");
		 }

	 }

	 public void VerifyRefundTransaction(int requestRow)
	 {	
		 TestDataReader refundData = new TestDataReader(testConfig, "ActionRequest");
		 String ExpectedStatus = refundData.GetData(requestRow, "Status");
		 String ExpectedAction = refundData.GetData(requestRow, "Action");

		 String transactionId=testConfig.getRunTimeProperty("transactionId");
		 testConfig.putRunTimeProperty("txnid", transactionId);
		 Map<String,String> map = DataBase.executeSelectQuery(testConfig, 1, 1);
		 String payUId =map.get("mihpayid");
		 testConfig.putRunTimeProperty("payUid", payUId);
		 Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 4, -1);

		 if(map1.get("action").contentEquals("refund"))
		 {
			 Helper.compareEquals(testConfig, "Refund status", ExpectedStatus, map1.get("status"));
			 Helper.compareEquals(testConfig, "Refund action", ExpectedAction, map1.get("action"));
			 //Helper.compareEquals(testConfig, "Bank ref id", "", map1.get("bank_ref_no"));	
		 }
		 else
		 {
			 testConfig.logFail("Refund entry not found in txn_update");
		 }

	 }

	 public void BulkFileupload(String fileName) {
		 testConfig.logComment("upload bulk refund file");
		 Element.enterFileName(testConfig, bulkRefundBox, fileName, "Bulk Refund");
	 }

	 public void BulkInvalidUpload(String fileName) {
		 testConfig.logComment("upload csv file");
		 Element.enterFileName(testConfig, bulkRefundBox, "C:\\Payu\\Product\\Parameters\\"+fileName, "Bulk Refund");
	 }

	 public void RefundBInvalidTransaction(){

		 Element.click(testConfig, refundButton, "Click on Refund Button");
		// Browser.switchToNewWindow(this.testConfig);
	 }

	 public String getInvalidFileText(){

		 return invalidFileMsg.getText();
	 }

	 public String getInvalidFileFormatText(){

		 return invalidFileExtMsg.getText();
	 }
	 public String getPopupText(){
		 Element.check(testConfig, checkbox, "First transaction");
		 Element.click(testConfig, refundButton, "Click on Refund Button");
		 RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
		 confirm.ClickActionconfirmButton();
		 return genPopup.getText();
	 }

	 public void RefundBulkdownload(){

		 System.out.println(invalidFileExtMsg.getText());
		 Element.click(testConfig, summaryDownloadLink, "Click on download summary link");
		// Browser.switchToNewWindow(this.testConfig); 
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

	 public void VerifyRefundRequest(int PaymentTypeRow, int TransactionRow, int PGinfoRow, String ExpectedAmount,TransactionHelper tranHelper)
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

	 public void VerifyRefundRequest_CF(int PaymentTypeRow, int TransactionRow, int PGinfoRow, String ExpectedAmount, Float ExpectedReqAmount, TransactionHelper tranHelper)
	 {	
		 TestDataReader PGData = new TestDataReader(testConfig, "PGInfo");
		 String ExpectedMerchantName= tranHelper.transactionData.GetData(TransactionRow, "Comments");
		 String ExpectedReqAmt= String.format("%.1f", ExpectedReqAmount);
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
		public void RefundFullFirstTransaction()
		{
			Element.check(testConfig, checkbox, "First transaction");
			Element.click(testConfig, refundButton, "Click on Refund Button");
			RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
			confirm.ClickOKButton();
			confirm.executeCron("processRefund.php");

		}
		
		/**
		 *Selects checkbox of first transaction available
		 *Clicks on refund button 
		 *Waits for refund button
		 *Gets Reference ID
		 */
		public String RefundFirstTransactionAndGetReferenceId()
		{
			Element.check(testConfig, checkbox, "First transaction");
			Element.click(testConfig, refundButton, "Click on Refund Button");
			RequestConfirmationPage confirm = new RequestConfirmationPage(testConfig);
			confirm.ClickActionButton();
			String referenceGenerated = testConfig.getRunTimeProperty("token");
			return referenceGenerated;
		}	 
}
