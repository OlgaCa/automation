
package PageObject.MerchantPanel.Transactions;

import java.util.Map;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

public class TransactionsPage extends TransactionFilterPage{
	private Config testConfig;

	public TransactionsPage(Config testConfig)
	{
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, FirstTxn);

	}

	@FindBy(id="invoice_button")
	private WebElement generateInvoice;
	
	@FindBy(name="transactions[1]")
	private WebElement FirstTxn;
	
	@FindBy(id="supercheck")
	private WebElement multiCheck;
	
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(5)")
	private WebElement payuId;

	@FindBy(css="div>table>tbody>tr>td:nth-child(6)")
	private WebElement name;

	@FindBy(css="div>table>tbody>tr>td:nth-child(8)")
	private WebElement amount;

	@FindBy(css="div>table>tbody>tr>td:nth-child(9)")
	private WebElement reqStatus;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(10)")
	private WebElement bankName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(11)")
	private WebElement reqAmount;

	@FindBy(css="div>table>tbody>tr>td:nth-child(12)")
	private WebElement discount;

	@FindBy(css="div>table>tbody>tr>td:nth-child(14)")
	private WebElement merchantName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(15)")
	private WebElement paymentGateway;

	@FindBy(css="div>table>tbody>tr>td:nth-child(16)")
	private WebElement bankRefNum;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(7)")
	private WebElement lastName;

	@FindBy(css="div>table>tbody>tr>td:nth-child(17)")
	private WebElement eMail;

	@FindBy(css="div>table>tbody>tr>td:nth-child(18)")
	private WebElement ipAddress;

	@FindBy(css="div>table>tbody>tr>td:nth-child(19)")
	private WebElement cardNo;

	@FindBy(css="div>table>tbody>tr>td:nth-child(21)")
	private WebElement field2;

	@FindBy(css="div>table>tbody>tr>td:nth-child(22)")
	private WebElement paymentType;

	@FindBy(css="div>table>tbody>tr>td:nth-child(25)")
	private WebElement offerKeyPGMID;

	@FindBy(css="div>table>tbody>tr>td:nth-child(24)")
	private WebElement PGMID;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(20)")
	private WebElement cardCategory;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(27)")
	private WebElement offerFailurereason;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(26)")
	private WebElement offerType;
	
	@FindBy(css="div[class='list-div']>table>tbody")
	private WebElement firstTransaction;

	@FindBy(id="edit_field_phone")
	private WebElement custPhone;
	
	@FindBy(id="edit_field_field9")
	private WebElement pgresponse;
	
	@FindBy(id="edit_field_city")
	private WebElement city;
	
	@FindBy(id="edit_field_country")
	private WebElement country;
	
	@FindBy(id="edit_field_state")
	private WebElement state;
	
	@FindBy(id="edit_field_productinfo")
	private WebElement prodInfo;
	
	@FindBy(id="edit_field_zipcode")
	private WebElement zipCode;
	
	@FindBy(id="edit_field_name_on_card")
	private WebElement nameonCard;
	
	@FindBy(id="edit_field_udf2")
	private WebElement udf2;
	
	@FindBy(id="edit_field_udf3")
	private WebElement udf3;
	
	@FindBy(id="edit_field_udf4")
	private WebElement udf4;
	
	@FindBy(id="edit_field_udf5")
	private WebElement udf5;
	
	@FindBy(id="edit_field_address1")
	private WebElement udf1;
	
	@FindBy(id="edit_field_address1")
	private WebElement address1;
	
	@FindBy(id="edit_field_address2")
	private WebElement address2;
	
	@FindBy(id="edit_field_shipping_phone")
	private WebElement shippingPhone;
	
	@FindBy(id="edit_field_shipping_city")
	private WebElement shippingCity;
	
	@FindBy(id="edit_field_shipping_country")
	private WebElement shippingCountry;
	
	@FindBy(id="edit_field_shipping_firstname")
	private WebElement shippingFName;
	
	@FindBy(id="edit_field_shipping_lastname")
	private WebElement shippingLName;
	
	@FindBy(id="edit_field_shipping_address1")
	private WebElement shippingAdd1;
	
	@FindBy(id="edit_field_shipping_address2")
	private WebElement shippingAdd2;
	
	@FindBy(id="edit_field_shipping_zipcode")
	private WebElement shippingZipcode;
	
	@FindBy(id="edit_field_shipping_state")
	private WebElement shippingState;
	
	@FindBy(name="qterm")
	private WebElement search;

	@FindBy(css="input[type=\"submit\"]")
	private WebElement clickGo;
	
	
	@FindBy(xpath ="//td[9]")
	//@FindBy(xpath ="//td[8]")
	private WebElement first_status;

	@FindBy(xpath ="//td[8]")
	private WebElement first_action;
	
	@FindBy(xpath = "//td[2]")
	private WebElement firstTrans;
	
	public RequestsPage SearchTransaction(String transactionId) {

		Element.enterData(testConfig, search,transactionId, "Search the transaction");
		Element.click(testConfig, clickGo, "Click on Go, to search the transaction");
		return new RequestsPage(testConfig);
	}
	/*
	 * Get Expected request details data from excel sheet
	 * Get actual refund request details
	 * Execute queries 
	 * Compare expected result with actual result 
	 */
	
	
	public void ClickGenerateInvoice()
	{
		Element.click(testConfig, generateInvoice, "Click on generate invoice button");
		
	}
	public GenerateInvoiceTransactionPage ClickGInvoice()
	{
		Element.click(testConfig, generateInvoice, "Click on generate invoice button");
		return new GenerateInvoiceTransactionPage(testConfig);
	}
	public void VerifyTransaction(int requestRow, int PaymentTypeRow, int TransactionRow, int PGinfoRow, String ExpectedAmount,TransactionHelper tranHelper)
	{	
		TestDataReader refundData = new TestDataReader(testConfig, "ActionRequest");
		TestDataReader PGData = new TestDataReader(testConfig, "PGInfo");
		String ExpectedStatus = refundData.GetData(requestRow, "Status");
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
		String actualBankName= bankName.getText();
		String actualAmount= amount.getText();
		String actualDiscount= discount.getText();
		String actualMerName= merchantName.getText();
		String actualReqAmt= reqAmount.getText();
		String actualFname= name.getText();
		String actualLname= lastName.getText();
		String actualEmail= eMail.getText();
		String actualPaymentType= paymentType.getText();
		String actualCardNo= cardNo.getText();
		String actualpayuId= payuId.getText();
		String actualPG= paymentGateway.getText();
		String actualIP= ipAddress.getText();
		String actualField2= field2.getText();
		String actualCardCategory= cardCategory.getText();
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
		Helper.compareEquals(testConfig, "Refund status", ExpectedStatus, actualStatus);
		Helper.compareEquals(testConfig, "Bank Name", ExpectedBankname, actualBankName);
		Helper.compareEquals(testConfig, "Amount", ExpectedAmount, actualAmount);
		Helper.compareEquals(testConfig, "Req Amount", ExpectedReqAmt, actualReqAmt);
		Helper.compareEquals(testConfig, "Discount", "0.00", actualDiscount);
		Helper.compareEquals(testConfig, "Merchant name", ExpectedMerchantName, actualMerName);
		Helper.compareEquals(testConfig, "First name", ExpectedName, actualFname);
		Helper.compareEquals(testConfig, "Last name", "-", actualLname);
		Helper.compareEquals(testConfig, "Email Id", ExpectedEmail, actualEmail);
		Helper.compareEquals(testConfig, "Payment type", ExpectedPaymenttype, actualPaymentType);
		Helper.compareEquals(testConfig, "Payment gateway", ExpectedPGtype, actualPG);
		Helper.compareEquals(testConfig, "IP address", IP, actualIP);
		Helper.compareEquals(testConfig, "PGMID", ExpectedPGMID, actualPGMID);
		Helper.compareEquals(testConfig, "Card category", "domestic", actualCardCategory);
		Helper.compareEquals(testConfig, "Offer failure reason", "-", actualofferFailreason);
		Helper.compareEquals(testConfig, "Offer type", "-", actualOfferType);

	}
	

	public void verifyFirstRowStatus(String expected_action, String expected_status)
	{
		Helper.compareEquals(testConfig, "status", expected_action, first_action.getText());
		Helper.compareEquals(testConfig, "status", expected_status, first_status.getText());
		
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
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(34)")
	private WebElement fieldNameonCard;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(37)")
	private WebElement field3;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(38)")
	private WebElement field4;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(39)")
	private WebElement field5;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(31)")
	private WebElement prodinfo;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(28)")
	private WebElement Fieldphone;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(43)")
	private WebElement SFname;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(44)")
	private WebElement SLname;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(45)")
	private WebElement SAdd1;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(46)")
	private WebElement SAdd2;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(47)")
	private WebElement SCity;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(48)")
	private WebElement SState;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(49)")
	private WebElement SCountry;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(50)")
	private WebElement Szipcode;
	
	@FindBy(css="div>table>tbody>tr>td:nth-child(51)")
	private WebElement Sphone;
	
	
	

    public TransactionDetailPage clickFirstTrans()
    {
    	Element.click(testConfig, firstTrans, "click on first transaction");
    	return new TransactionDetailPage(testConfig);
    }
    public void clickFirstTxn()
    {
    	Element.check(testConfig, FirstTxn, "first transaction");
    	
    }
    public void clickMulticheck()
    {
    	Element.check(testConfig, multiCheck, "multi select checkbox");
    	
    }
    
    public void transactionPage()
	{
		Boolean isTrue=Element.IsElementDisplayed(testConfig, multiCheck);
		Helper.compareTrue(testConfig, "Navigation to View all transaction page", isTrue);
	}
    public void CheckEditfields()
	{
		Element.check(testConfig,custPhone ,"Phone");
		Element.check(testConfig, pgresponse,"PG response");
		Element.check(testConfig, udf2 ,"udf2");
		Element.check(testConfig, udf3 ,"udf3");
		Element.check(testConfig, udf4,"udf4");
		Element.check(testConfig, udf5,"udf5");
		Element.check(testConfig, city,"city");
		Element.check(testConfig, address1,"Address 1");
		Element.check(testConfig, address2,"Address 2");
		Element.check(testConfig, pgresponse,"PG response");
		Element.check(testConfig, prodInfo,"Prod info");
		Element.check(testConfig, nameonCard,"Name on card");
		Element.check(testConfig, zipCode,"zipcode");
		Element.check(testConfig, country,"Country");
		Element.check(testConfig, state,"State");
		Element.check(testConfig, shippingFName,"shipping Fname");
		Element.check(testConfig, shippingLName,"shipping Lname");
		Element.check(testConfig, shippingZipcode,"shipping zip code");
		Element.check(testConfig, shippingCity,"shipping City");
		Element.check(testConfig, shippingState,"shipping state");
		Element.check(testConfig, shippingAdd1,"shipping Address1");
		Element.check(testConfig, shippingAdd2,"shipping Address2");
		Element.check(testConfig, shippingCountry,"shipping Fname");
		Element.check(testConfig, shippingPhone,"shipping Fname");
		
		
	}
    
   
	public String getEcardName()
	{ return (fieldNameonCard.getText());
	}
	public String getPhone()
	{ return (Fieldphone.getText());
	}
	public String getFname()
	{ return (SFname.getText());
	}
	public String getLname()
	{ return (SLname.getText());
	}
	public String getEcity()
	{ return (SCity.getText());
	}
	public String getEstate()
	{return (SState.getText());
	}
	public String getProdInfo()
	{return (prodinfo.getText());
	}
	public String getECountry()
	{return (SCountry.getText());
	}
	public String getEPhone()
	{ return (Sphone.getText());
	}
	public String getAdd1()
	{ return (SAdd1.getText());
	}
	public String getAdd2()
	{ return (SAdd2.getText());
	}
	
	public String getEzipcode()
	{ return (Szipcode.getText());
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
	

}

