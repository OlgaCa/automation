package PageObject.MerchantPanel.Transactions;

import java.util.Map;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class RequestsPage extends TransactionFilterPage{
	private Config testConfig;

	public RequestsPage(Config testConfig)
	{
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, requestedAction);

	}

	@FindBy(id="Unsettled")
	private WebElement unsettledCheckBox;
	
	@FindBy(className="floatLeft")
	private WebElement ShowingRecords;
	
	@FindBy(css="div>table>thead>tr>th:nth-child(7)")
	private WebElement requestedAction;

	@FindBy(css="div>table>tbody>tr>td:nth-child(2)")
	private WebElement reqId;

	@FindBy(css="div>table>tbody>tr>td:nth-child(4)")
	private WebElement payuId;

	@FindBy(css="div>table>tbody>tr>td:nth-child(5)")
	private WebElement name;

	@FindBy(css="div>table>tbody>tr>td:nth-child(6)")
	private WebElement amount;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(7)")
	private WebElement INRAmount;

	@FindBy(css="div>table>tbody>tr>td:nth-child(8)")
	private WebElement reqAction;

	@FindBy(css="div>table>tbody>tr>td:nth-child(9)")
	private WebElement reqStatus; 

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
	private WebElement txnId;

	@FindBy(css="div>table>tbody>tr>td:nth-child(16)")
	private WebElement paymentGateway;

	@FindBy(css="div>table>tbody>tr>td:nth-child(18)")
	private WebElement lastName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(19)")
	private WebElement eMail;

	@FindBy(css="div>table>tbody>tr>td:nth-child(20)")
	private WebElement ipAddress;

	@FindBy(css="div>table>tbody>tr>td:nth-child(21)")
	private WebElement cardNo;

	@FindBy(css="div>table>tbody>tr>td:nth-child(23)")
	private WebElement field2;

	@FindBy(css="div>table>tbody>tr>td:nth-child(24)")
	private WebElement paymentType;

	@FindBy(css="div>table>tbody>tr>td:nth-child(25)")
	private WebElement udf1;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(26)")
	private WebElement udf2;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(27)")
	private WebElement netAmount;

	@FindBy(css="div>table>tbody>tr>td:nth-child(28)")
	private WebElement serviceFee;

	@FindBy(css="div>table>tbody>tr>td:nth-child(29)")
	private WebElement serviceTax;

	@FindBy(css="div>table>tbody>tr>td:nth-child(31)")
	private WebElement mer_utr;

	@FindBy(css="div>table>tbody>tr>td:nth-child(32)")
	private WebElement token;

	@FindBy(css="div>table>tbody>tr>td:nth-child(33)")
	private WebElement settle;

	@FindBy(css="div>table>tbody>tr>td:nth-child(34)")
	private WebElement offerKey;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(35)")
	private WebElement PGMID;

	@FindBy(css="div>table>tbody>tr>td:nth-child(36)")
	private WebElement cardCategory;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(37)")
	private WebElement offerFailurereason;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(38)")
	private WebElement offerType;

	@FindBy(css="div[class='list-div']>table>tbody")
	private WebElement firstTransaction;

	@FindBy(name="qterm")
	private WebElement search;

	@FindBy(css="input[type=\"submit\"]")
	private WebElement clickGo;
	
	
	@FindBy(xpath ="//td[9]")
	private WebElement first_status;

	@FindBy(xpath ="//td[8]")
	private WebElement first_action;
	
	@FindBy(xpath = "//td[2]")
	private WebElement firstTrans;
	
	@FindBy(id="edit_field_phone")
	private WebElement custPhone;
	
	@FindBy(id="edit_field_field9")
	private WebElement pgresponse;
	
	@FindBy(id="edit_field_city")
	private WebElement city;
	
	@FindBy(id="edit_field_country")
	private WebElement country;
	
	@FindBy(id="edit_field_productinfo")
	private WebElement prodInfo;
	
	@FindBy(id="edit_field_zipcode")
	private WebElement zipCode;
	
	@FindBy(id="edit_field_name_on_card")
	private WebElement nameonCard;
	
	@FindBy(id="edit_field_udf3")
	private WebElement udf3;
	
	@FindBy(id="edit_field_udf4")
	private WebElement udf4;
	
	@FindBy(id="edit_field_udf5")
	private WebElement udf5;
	
	@FindBy(id="edit_field_address2")
	private WebElement address2;
	
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(35)")
	private WebElement Fieldphone;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(36)")
	private WebElement FieldCity;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(37)")
	private WebElement FieldCountry;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(38)")
	private WebElement FieldzipCode;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(39)")
	private WebElement FieldprofInfo;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(40)")
	private WebElement fieldNameonCard;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(42)")
	private WebElement field3;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(43)")
	private WebElement field4;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(44)")
	private WebElement field5;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(45)")
	private WebElement Fieldaddress2;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(46)")
	private WebElement editPGMID;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(47)")
	private WebElement editCardCategory;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(48)")
	private WebElement editOfferFailReason;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(49)")
	private WebElement editOffertype;
	
	public RequestsPage SearchTransaction(String transactionId) {

		Element.enterData(testConfig, search,transactionId, "Search the transaction");
		Element.click(testConfig, clickGo, "Click on Go, to search the transaction");
		return new RequestsPage(testConfig);
	}
	
	/*
	 * Get Number of records
	 */
	
	public String getNumberRecords() {
		System.out.println(ShowingRecords.getText());
		String[] recordStringParts = ShowingRecords.getText().split(" ");
		String number = recordStringParts[recordStringParts.length -2 ];
		return number;
	}
	
	
	/*
	 * Get Expected transaction data from dashboard
	 * Get actual transaction data from DB
	 * Execute queries 
	 * Compare expected result with actual result 
	 */
	public void VerifyUnsettledTransactionData(String merchantName)
	{	
		//query to get merchant id, and unsettled transactions 
		testConfig.putRunTimeProperty("name", merchantName);
		Map<String,String> config = DataBase.executeSelectQuery(testConfig, 67, 1);
		String merchant_id = config.get("merchantid");
		
		testConfig.putRunTimeProperty("merchant_id", merchant_id);
		Map<String,String> txn_update = DataBase.executeSelectQuery(testConfig, 70, 1);
		String ExpectedRequestId = txn_update.get("id");
		String ExpectedStatus = txn_update.get("status");
		String ExpectedAction = txn_update.get("action");
		String ExpectedAmount= txn_update.get("amount");
		String ExpectedINRAmount= txn_update.get("inr_amount");
		String ExpectedNetAmount= txn_update.get("mer_net_amount");
		String ExpectedServiceFee= txn_update.get("mer_service_fee");
		String ExpectedServiceTax= txn_update.get("mer_service_tax");
		String ExpectedToken= txn_update.get("token");
		String ExpectedUTR= txn_update.get("mer_utr");
		String ExpectedSettlementStatus= txn_update.get("mer_settled");
		String ExpectedPayuId= txn_update.get("txnid");

		/*String transactionId=testConfig.getRunTimeProperty("transactionId");
		testConfig.putRunTimeProperty("txnid", transactionId);
		Map<String,String> transaction = DataBase.executeSelectQuery(testConfig, 1, 1);
		String ExpectedUDF1= transaction.get("udf1");
		String ExpectedUDF2= transaction.get("udf2");*/

		String actualStatus=reqStatus.getText();
		String actualAction=reqAction.getText();
		String actualAmount= amount.getText();
		String actualINRAMount = INRAmount.getText();
		String actualReqId= reqId.getText();
		String actualPayuId= payuId.getText();
		String actualUDF1= udf1.getText();
		if (actualUDF1.contains("-"))
			actualUDF1 = "";
		String actualUDF2= udf2.getText();
		if (actualUDF2.contains("-"))
			actualUDF2 = "";
		String actualNetAmount= netAmount.getText();
		String actualServicefee= serviceFee.getText();
		String actualServiceTax= serviceTax.getText();
		String actualMerchantUTR= mer_utr.getText();
		if (actualMerchantUTR.contains("-"))
			actualMerchantUTR = "";
		String actualToken= token.getText();
		if (actualToken.contains("-"))
			actualToken = "";
		String actualSettle= settledStatus();


		Helper.compareEquals(testConfig, "PayU id", ExpectedPayuId,actualPayuId);
		Helper.compareEquals(testConfig, "RequestId", ExpectedRequestId,actualReqId);
		Helper.compareEquals(testConfig, "Status", ExpectedStatus, actualStatus);
		Helper.compareEquals(testConfig, "Action", ExpectedAction, actualAction);
		Helper.compareEquals(testConfig, "Amount", ExpectedAmount, actualAmount);
		Helper.compareEquals(testConfig, "INR Amount", ExpectedINRAmount, actualINRAMount);
		/*Helper.compareEquals(testConfig, "UDF1", ExpectedUDF1, actualUDF1);
		Helper.compareEquals(testConfig, "UDF2", ExpectedUDF2, actualUDF2);*/
		Helper.compareEquals(testConfig, "Net amount", ExpectedNetAmount, actualNetAmount);
		Helper.compareEquals(testConfig, "Service fee", ExpectedServiceFee, actualServicefee);
		Helper.compareEquals(testConfig, "Service tax", ExpectedServiceTax, actualServiceTax);
		Helper.compareEquals(testConfig, "Merchant UTR", ExpectedUTR, actualMerchantUTR);
		Helper.compareEquals(testConfig, "Token", ExpectedToken, actualToken);
		Helper.compareEquals(testConfig, "Settle", ExpectedSettlementStatus, actualSettle);

	}
	
	public void VerifyRefundRequest(int requestRow, int PaymentTypeRow, int TransactionRow, int PGinfoRow, String ExpectedAmount,TransactionHelper tranHelper)
	{	
		TestDataReader refundData = new TestDataReader(testConfig, "ActionRequest");
		TestDataReader PGData = new TestDataReader(testConfig, "PGInfo");
		String ExpectedStatus = refundData.GetData(requestRow, "Status");
		String ExpectedAction = refundData.GetData(requestRow, "Action");
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

		String actualStatus=reqStatus.getText();
		String actualAction=reqAction.getText();
		String actualBankName= bankName.getText();
		String actualAmount= amount.getText();
		String actualDiscount= discount.getText();
		String actualMerName= merchantName.getText();
		String actualTxnId= txnId.getText();
		String actualReqAmt= reqAmount.getText();
		String actualFname= name.getText();
		String actualLname= lastName.getText();
		String actualEmail= eMail.getText();
		String actualPaymentType= paymentType.getText();
		String actualCardNo= cardNo.getText();
		String actualReqId= reqId.getText();
		String actualpayuId= payuId.getText();
		String actualPG= paymentGateway.getText();
		String actualIP= ipAddress.getText();
		String actualUDF1= udf1.getText();
		String actualUDF2= udf2.getText();
		String actualNamount= netAmount.getText();
		String actualField2= field2.getText();
		String actualServicefee= serviceFee.getText();
		String actualServiceTax= serviceTax.getText();
		String actualMerchantTDR= mer_utr.getText();
		String actualToken= token.getText();
		String actualCardCategory= cardCategory.getText();
		String actualPGMID= PGMID.getText();
		String actualSettle= settle.getText();
		String actualofferFailreason= offerFailurereason.getText();
		String actualOfferType= offerType.getText();
		
		String transactionId=testConfig.getRunTimeProperty("transactionId");
		testConfig.putRunTimeProperty("txnid", transactionId);
		Map<String,String> transaction = DataBase.executeSelectQuery(testConfig, 1, 1);
		String payUId = transaction.get("mihpayid");
		testConfig.putRunTimeProperty("payUid", payUId);
		Map<String,String> txn_update = DataBase.executeSelectQuery(testConfig, 4, -1);
		
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
		Helper.compareEquals(testConfig, "RequestId", txn_update.get("id"),actualReqId);
		Helper.compareEquals(testConfig, "Field2",tranHelper.testResponse.actualResponse.get("field2"), actualField2);
		Helper.compareEquals(testConfig, "Refund status", ExpectedStatus, actualStatus);
		Helper.compareEquals(testConfig, "Action", ExpectedAction, actualAction);
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
		Helper.compareEquals(testConfig, "UDF1", "-", actualUDF1);
		Helper.compareEquals(testConfig, "UDF2", "-", actualUDF2);
		Helper.compareEquals(testConfig, "Payment gateway", ExpectedPGtype, actualPG);
		Helper.compareEquals(testConfig, "IP address", IP, actualIP);
		Helper.compareEquals(testConfig, "Net amount", txn_update.get("mer_net_amount"), actualNamount);
		Helper.compareEquals(testConfig, "Service fee", "0.00", actualServicefee);
		Helper.compareEquals(testConfig, "Service tax", "0.00", actualServiceTax);
		Helper.compareEquals(testConfig, "Merchant UTR", "-", actualMerchantTDR);
		Helper.compareEquals(testConfig, "PGMID", ExpectedPGMID, actualPGMID);
		Helper.compareEquals(testConfig, "Card category", "domestic", actualCardCategory);
		Helper.compareEquals(testConfig, "Settle", "unsettled", actualSettle);
		Helper.compareEquals(testConfig, "token", testConfig.getRunTimeProperty("token"), actualToken);
		Helper.compareEquals(testConfig, "Offer failure reason", "-", actualofferFailreason);
		Helper.compareEquals(testConfig, "Offer type", "-", actualOfferType);
		

	}
	
	public void VerifyRefundRequest_CF(int requestRow, int PaymentTypeRow, int TransactionRow, int PGinfoRow, Float RefundAmount,
			TransactionHelper tranHelper, TestResponsePage testResponsePage)
	{	
		TestDataReader refundData = new TestDataReader(testConfig, "ActionRequest");
		TestDataReader PGData = new TestDataReader(testConfig, "PGInfo");
		
		String ExpectedStatus = refundData.GetData(requestRow, "Status");
		String ExpectedAction = refundData.GetData(requestRow, "Action");
		String ExpectedMerchantName= tranHelper.transactionData.GetData(TransactionRow, "Comments");
		String amnt = testResponsePage.actualResponse.get("amount");
		String ExpectedAmount = String.format("%.2f", RefundAmount);
		String ExpectedReqAmt = amnt;
		String ExpectedName= tranHelper.transactionData.GetData(TransactionRow, "firstname");
		String ExpectedEmail= tranHelper.transactionData.GetData(TransactionRow, "email");
		String ExpectedPaymenttype= tranHelper.paymentTypeData.GetData(PaymentTypeRow, "mode");
		String ExpectedBankname= tranHelper.paymentTypeData.GetData(PaymentTypeRow, "Bank Name");
		String ExpectedPGtype= PGData.GetData(PGinfoRow, "PG_TYPE");
		String ExpectedPGMID= PGData.GetData(PGinfoRow, "PGMID");
		
		switch(ExpectedPaymenttype)
		{
		case "creditcard": ExpectedPaymenttype = "Credit Card";
		break;

		case "debitcard": ExpectedPaymenttype = "Debit Card";
		break;
		}

		String actualStatus=reqStatus.getText();
		String actualAction=reqAction.getText();
		String actualBankName= bankName.getText();
		String actualAmount= amount.getText();
		String actualDiscount= discount.getText();
		String actualMerName= merchantName.getText();
		String actualTxnId= txnId.getText();
		String actualReqAmt= reqAmount.getText();
		String actualFname= name.getText();
		String actualLname= lastName.getText();
		String actualEmail= eMail.getText();
		String actualPaymentType= paymentType.getText();
		String actualCardNo= cardNo.getText();
		String actualReqId= reqId.getText();
		String actualpayuId= payuId.getText();
		String actualPG= paymentGateway.getText();
		String actualIP= ipAddress.getText();
		String actualUDF1= udf1.getText();
		String actualUDF2= udf2.getText();
		String actualNamount= netAmount.getText();
		String actualField2= field2.getText();
		String actualServicefee= serviceFee.getText();
		String actualServiceTax= serviceTax.getText();
		String actualMerchantTDR= mer_utr.getText();
		String actualToken= token.getText();
		String actualCardCategory= cardCategory.getText();
		String actualPGMID= PGMID.getText();
		String actualSettle= settle.getText();
		String actualofferFailreason= offerFailurereason.getText();
		String actualOfferType= offerType.getText();
		
		String transactionId=testConfig.getRunTimeProperty("transactionId");
		testConfig.putRunTimeProperty("mihpayid", testResponsePage.actualResponse.get("mihpayid"));
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 4, -1);
		
		InetAddress ip;
		String IP = null;
		try {

			ip = InetAddress.getLocalHost();
			IP= (ip.getHostAddress()).toString();
		} catch (UnknownHostException e) {

			testConfig.logException(e);
		}

		Helper.compareEquals(testConfig, "Card no", tranHelper.testResponse.actualResponse.get("cardnum"),actualCardNo);
		Helper.compareEquals(testConfig, "PayU id", map.get("txnid"),actualpayuId);
		Helper.compareEquals(testConfig, "RequestId", map.get("id"),actualReqId);
		Helper.compareEquals(testConfig, "Field2",tranHelper.testResponse.actualResponse.get("field2"), actualField2);
		Helper.compareEquals(testConfig, "Refund status", ExpectedStatus, actualStatus);
		Helper.compareEquals(testConfig, "Action", ExpectedAction, actualAction);
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
		Helper.compareEquals(testConfig, "UDF1", "-", actualUDF1);
		Helper.compareEquals(testConfig, "UDF2", "-", actualUDF2);
		Helper.compareEquals(testConfig, "Payment gateway", ExpectedPGtype, actualPG);
		Helper.compareEquals(testConfig, "IP address", IP, actualIP);
		Helper.compareEquals(testConfig, "Net amount", map.get("mer_net_amount"), actualNamount);
		Helper.compareEquals(testConfig, "Service fee", map.get("mer_service_fee"), actualServicefee);
		Helper.compareEquals(testConfig, "Service tax", map.get("mer_service_tax"), actualServiceTax);
		Helper.compareEquals(testConfig, "Merchant UTR", "-", actualMerchantTDR);
		Helper.compareEquals(testConfig, "PGMID", ExpectedPGMID, actualPGMID);
		Helper.compareEquals(testConfig, "Card category", "domestic", actualCardCategory);
		Helper.compareEquals(testConfig, "Settle", "unsettled", actualSettle);
		Helper.compareEquals(testConfig, "token", testConfig.getRunTimeProperty("token"), actualToken);
		Helper.compareEquals(testConfig, "Offer failure reason", "-", actualofferFailreason);
		Helper.compareEquals(testConfig, "Offer type", "-", actualOfferType);
		

	}
	
	private String settledStatus() {
		String settledstatus= settle.getText();
		if(settledstatus.equals("unsettled")){
			settledstatus = "0".toString();			
		}
		else {
			settledstatus ="1".toString();
		}

		return settledstatus;
	}
	
	public void unsettledBox() {
		String check = unsettledCheckBox.getCssValue("checked");
		Helper.compareEquals(testConfig, "Unsettled Checkbox is selected", "", check);
	}
	

	public void verifyFirstRowStatus(String expected_action, String expected_status)
	{
		Helper.compareEquals(testConfig, "status", expected_action, first_action.getText());
		Helper.compareEquals(testConfig, "status", expected_status, first_status.getText());
		
	}

	public String getreqID()
	{ return (reqId.getText());
	}
	public String getPayuId()
	{ return (payuId.getText());
	}
	public String getName()
	{ return (name.getText());
	}
	public String getAmount()
	{ return (amount.getText());
	}
	public String getAction()
	{ return (reqAction.getText());
	}
	public String getStatus()
	{ return (reqStatus.getText());
	}
	public String getBankname()
	{ return (bankName.getText());
	}
	public String getReqAmount1()
	{ return (reqAmount.getText());
	}
	public String getDiscount()
	{ return (discount.getText());
	}
	public String getMerchantname()
	{ return (merchantName.getText());
	}
	public String getTxnId()
	{ return (txnId.getText());
	}
	public String getPG()
	{ return (paymentGateway.getText());
	}
	public String getLastname()
	{ return (lastName.getText());
	}
	public String geteMail()
	{ return (eMail.getText());
	}
	public String getipAddress()
	{ return (ipAddress.getText());
	}
	public String getCardno()
	{ return (cardNo.getText());
	}
	public String getField2()
	{ return (field2.getText());
	}
	public String getPaymenttype()
	{ return (paymentType.getText());
	}
	public String getNamount()
	{ return (netAmount.getText());
	}
	public String getServiceFee()
	{ return (serviceFee.getText());
	}
	public String getServicetax()
	{ return (serviceTax.getText());
	}
	public String getMerchantTdr()
	{ return (mer_utr.getText());
	}
	public String getToken()
	{ return (token.getText());
	}
	public String getSettle()
	{ return (settle.getText());
	}
	public String getPgmid()
	{ return (PGMID.getText());
	}
	public String getCardCategory()
	{ return (cardCategory.getText());
	}
	public String getOfferFailreason()
	{ return (offerFailurereason.getText());
	}
	public String getOfferType()
	{ return (offerType.getText());
	}
	public String getEOfferType()
	{ return (editOffertype.getText());
	}	
	public String getEPgmid()
	{ return (editPGMID.getText());
	}
	public String getECardCategory()
	{ return (editCardCategory.getText());
	}
	public String getEOfferFailreason()
	{ return (editOfferFailReason.getText());
	}
	public String getEcardName()
	{ return (fieldNameonCard.getText());
	}
	public String getEAddress2()
	{ return (Fieldaddress2.getText());
	}
	public String getEcity()
	{ return (FieldCity.getText());
	}
	public String getECountry()
	{ return (FieldCountry.getText());
	}
	public String getEPhone()
	{ return (Fieldphone.getText());
	}
	public String getEProdInfo()
	{ return (FieldprofInfo.getText());
	}
	public String getEzipcode()
	{ return (FieldzipCode.getText());
	}
	public String getField3()
	{ return (field3.getText());
	}
	public String getField4()
	{ return (field4.getText());
	}
	public String getField5()
	{ return (field5.getText());
	}
			
	
	public TransactionDetailPage clickFirstTrans()
    {
    	Element.click(testConfig, firstTrans, "click on first transaction");
    	return new TransactionDetailPage(testConfig);
    }
    
	public void CheckEditfields()
	{
		Element.check(testConfig,custPhone ,"Phone");
		Element.check(testConfig, pgresponse,"PG response");
		Element.check(testConfig, udf3 ,"udf3");
		Element.check(testConfig, udf4,"udf4");
		Element.check(testConfig, udf5,"udf5");
		Element.check(testConfig, city,"city");
		Element.check(testConfig, address2,"Address 2");
		Element.check(testConfig, pgresponse,"PG response");
		Element.check(testConfig, prodInfo,"Prod info");
		Element.check(testConfig, nameonCard,"Name on card");
		Element.check(testConfig, zipCode,"zipcode");
		Element.check(testConfig, country,"Country");
		
	}
    

}
