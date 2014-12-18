package PageObject.AdminPanel.Payments.Response;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Home.HomePage;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;
import Utils.Element.How;

public class TestResponsePage {

	private Config testConfig;

	@FindBy(linkText="Home")
	private WebElement homeLink;

	@FindBy(tagName="pre")
	private WebElement responseField;

	public Hashtable<String, String> actualResponse;

	public boolean overrideExpectedTransactionData = false;

	private String [] expectedResponseKeys;

	private String [] expectedCCResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon", "unmappedstatus","mihpayid", "address2", "address1", "city", 
			"key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message","udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status", "cardhash", "cardnum",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9","country", "hash","firstname", "zipcode" , "state", "name_on_card", "net_amount_debit", "payment_source"}; 

	private String[] expectedCCOfferResponseKeys = {"mihpayid","mode","status","unmappedstatus","key","txnid", "amount", "discount", "addedon","productinfo","firstname","lastname" ,
			"address1","address2","city","state","country","zipcode","email","phone","udf1","udf2","udf3","udf4","udf5","udf6","udf7","udf8","udf9","udf10","hash","field1",
			"field2","field3","field4","field5","field6","field7","field8","field9","PG_TYPE","bank_ref_num","bankcode","error","error_Message","offer","offer_availed","offer_type", 
			"name_on_card","cardhash", "cardnum", "net_amount_debit", "payment_source"};

	private String [] expectedUserCancelledforOfferKeys = {"mihpayid","mode","status","unmappedstatus","key","txnid","amount", "addedon","productinfo",
			"discount","firstname","lastname","address1" ,"address2","city","state","country","zipcode","email","phone","udf1","udf2","udf3","udf4","udf5","udf6","udf7",
			"udf8","udf9","udf10","hash","field1","field2","field3","field4","field5","field6","field7","field8", "field9","PG_TYPE","bank_ref_num","bankcode",
			"error","error_Message", "offer","offer_failure_reason","name_on_card","cardnum","cardhash","offer_type", "net_amount_debit", "payment_source"}; 

	private String [] expectedUserCancelledConvenienceFeeforOfferKeys = {"mihpayid","mode","status","unmappedstatus","key","txnid","amount","discount", "addedon","productinfo","additionalCharges",
			"firstname","lastname","address1" ,"address2","city","state","country","zipcode","email","phone","udf1"  ,"udf2","udf3","udf4","udf5","udf6","udf7","udf8",
			"udf9","udf10","hash","field1","field2","field3","field4","field5","field6","field7","field8", "field9","PG_TYPE",
			"bank_ref_num","bankcode","error","error_Message","cardToken","name_on_card","cardnum","cardhash", "net_amount_debit", "payment_source"}; 

	private String[] expectedCCConvenienceFeeResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo","additionalCharges", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status", "cardhash", "cardnum",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9","country", "hash","firstname", "zipcode" , "state", "name_on_card", "net_amount_debit", "payment_source"}; 

	private String[] expectedCCFailedOfferResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon", "unmappedstatus","mihpayid", "address2",
			"address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message","udf1", "udf2", 
			"udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status","offer",
			"offer_failure_reason","cardhash", "cardnum","lastname", "field1", "field2", "field3", "field4", "field5", "field6",
			"field7", "field8", "field9","country", "hash","firstname", "zipcode" , "state","offer_type", "name_on_card", "net_amount_debit", "payment_source"}; 

	private String[] expectedCCFailedConvenienceFeeOfferResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo","additionalCharges", "discount", "addedon", "unmappedstatus","mihpayid", "address2",
			"address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message","udf1", "udf2", 
			"udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status","offer",
			"offer_failure_reason","cardhash", "cardnum","lastname", "field1", "field2", "field3", "field4", "field5", "field6",
			"field7", "field8", "field9","country", "hash","firstname", "zipcode" , "state","offer_type", "name_on_card", "net_amount_debit", "payment_source"}; 

	private String [] expectedCCStoredResponseKeys = {"mihpayid","mode","status","unmappedstatus","key","txnid","amount","discount", "addedon","productinfo",
			"firstname","lastname","address1" ,"address2","city","state","country","zipcode","email","phone","udf1"  ,"udf2","udf3","udf4","udf5","udf6","udf7","udf8",
			"udf9","udf10","hash","field1","field2","field3","field4","field5","field6","field7","field8", "field9","PG_TYPE",
			"bank_ref_num","bankcode","error","error_Message","cardToken","name_on_card","cardnum","cardhash", "net_amount_debit", "payment_source"};

	private String [] expectedDCResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status", "cardhash", "cardnum",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9","country", "hash","firstname", "zipcode" , "state", "name_on_card", "net_amount_debit", "payment_source"}; 

	private String [] expectedDCStoredResponseKeys = {"mihpayid","mode","status","unmappedstatus","key","txnid","amount","discount", "addedon","productinfo",
			"firstname","lastname","address1" ,"address2","city","state","country","zipcode","email","phone","udf1"  ,"udf2","udf3","udf4","udf5","udf6","udf7","udf8",
			"udf9","udf10","hash","field1","field2","field3","field4","field5","field6","field7","field8", "field9","PG_TYPE",
			"bank_ref_num","bankcode","error","error_Message","cardToken","name_on_card","cardnum","cardhash", "net_amount_debit", "payment_source"}; 

	private String[] expectedDCConvenienceFeeResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "additionalCharges", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status", "cardhash", "cardnum",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9", "country", "hash","firstname", "zipcode" , "state", "name_on_card", "net_amount_debit", "payment_source"}; 

	private String [] expectedSpecialCardNumberResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9","country", "hash","firstname", "zipcode" , "state", "name_on_card", "net_amount_debit", "payment_source"};

	private String [] expectedUserCancResponseKeys = {"amount", "mode", "PG_TYPE", "phone", "productinfo", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8","field9", "country", "hash","firstname", "zipcode" , "state", "net_amount_debit", "payment_source"}; 

	private String [] expectedNBResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8","field9", "country", "hash","firstname", "zipcode" , "state","name_on_card", "net_amount_debit", "payment_source"}; 

	private String [] expectedEMIResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status","cardhash", "cardnum",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9","country", "hash","firstname", "zipcode" , "state", "name_on_card", "net_amount_debit", "payment_source"}; 

	private String [] expectedEMIConvenienceFeeResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "additionalCharges", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status","cardhash", "cardnum",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8","field9", "country", "hash","firstname", "zipcode" , "state", "name_on_card", "net_amount_debit", "payment_source"}; 

	private String [] expectedCashCardResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status", "cardhash", "cardnum",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9","country", "hash","firstname", "zipcode" , "state", "name_on_card", "net_amount_debit", "payment_source"}; 

	private String [] expectedCashConvenienceFeeResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "additionalCharges", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status", "cardhash", "cardnum",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9", "country", "hash","firstname", "zipcode" , "state", "name_on_card", "net_amount_debit", "payment_source"}; 

	private String [] expectedCODResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9", "country", "hash","firstname", "zipcode" , "state", 
			"shipping_firstname", "shipping_lastname", "shipping_address1", "shipping_address2", "shipping_city", "shipping_country", "shipping_state", "shipping_zipcode", "shipping_phone", "shipping_phoneverified", "net_amount_debit", "payment_source"}; 

	private String [] expectedCODConvenienceFeeResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "additionalCharges", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9", "country", "hash","firstname", "zipcode" , "state", 
			"shipping_firstname", "shipping_lastname", "shipping_address1", "shipping_address2", "shipping_city", "shipping_country", "shipping_state", "shipping_zipcode", "shipping_phone", "shipping_phoneverified", "net_amount_debit", "payment_source"};

	private String[] expectedCCOfferConvenienceFeeResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo","additionalCharges", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status","offer", "cardhash", "cardnum","offer_availed",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9", "country", "hash","firstname", "zipcode" , "state","offer_type", "name_on_card", "net_amount_debit", "payment_source"}; 

	private String [] expectedSeamlessStoredResponseKeys = {"mihpayid","mode","status","unmappedstatus","key","txnid","amount","discount", "addedon","productinfo",
			"firstname","lastname","address1" ,"address2","city","state","country","zipcode","email","phone","udf1"  ,"udf2","udf3","udf4","udf5","udf6","udf7","udf8",
			"udf9","udf10","hash","field1","field2","field3","field4","field5","field6","field7","field8", "field9","PG_TYPE",
			"bank_ref_num","bankcode","error","error_Message","name_on_card","cardnum","cardhash"		
			, "net_amount_debit", "payment_source"};

	private String [] expectedCCIssuingBankResponseKeys = {"mihpayid","mode","status","unmappedstatus","key","txnid", "amount", "discount", "addedon","productinfo","firstname","lastname" ,
			"address1","address2","city","state","country","zipcode","email","phone","udf1","udf2","udf3","udf4","udf5","udf6","udf7","udf8","udf9","udf10","hash","field1",
			"field2","field3","field4","field5","field6","field7","field8","field9","PG_TYPE","bank_ref_num","bankcode","error","error_Message","name_on_card","cardnum", "cardhash","issuing_bank","card_type", "net_amount_debit", "payment_source"};

	private String [] expectedEMIOfferResponseKeys = {"amount", "PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon", "unmappedstatus", 
			"mihpayid", "address2", "address1", "city", "key", "bank_ref_num", "txnid", "email", "bankcode", "error","error_Message",
			"udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7", "udf8", "udf9", "udf10", "status","cardhash", "cardnum",
			"lastname", "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9","country", "hash","firstname", "zipcode" , "state", "name_on_card", "net_amount_debit", "payment_source","offer","offer_availed","offer_type"}; 

	private String [] expectedCCStoredWithoutCardTokenResponseKeys = {"mihpayid","mode","status","unmappedstatus","key","txnid","amount","discount", "addedon","productinfo",
			"firstname","lastname","address1" ,"address2","city","state","country","zipcode","email","phone","udf1"  ,"udf2","udf3","udf4","udf5","udf6","udf7","udf8",
			"udf9","udf10","hash","field1","field2","field3","field4","field5","field6","field7","field8", "field9","PG_TYPE",
			"bank_ref_num","bankcode","error","error_Message","name_on_card","cardnum","cardhash", "net_amount_debit", "payment_source"};

	private String[] expectedEMIOfferFailureResponseKeys = {"amount",
			"PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon",
			"unmappedstatus", "mihpayid", "address2", "address1", "city",
			"key", "bank_ref_num", "txnid", "email", "bankcode", "error",
			"error_Message", "udf1", "udf2", "udf3", "udf4", "udf5", "udf6",
			"udf7", "udf8", "udf9", "udf10", "status", "cardhash", "cardnum",
			"lastname", "field1", "field2", "field3", "field4", "field5",
			"field6", "field7", "field8", "field9", "country", "hash",
			"firstname", "zipcode", "state", "name_on_card",
			"net_amount_debit", "payment_source", "offer",
			"offer_failure_reason", "offer_type"};


	private String[] expectedSuccessPayUWalletResponseKeys = {"amount",
			"PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon",
			"unmappedstatus", "mihpayid", "address2", "address1", "city",
			"key", "bank_ref_num", "txnid", "email", "bankcode", "error",
			"error_Message", "udf1", "udf2", "udf3", "udf4", "udf5", "udf6",
			"udf7", "udf8", "udf9", "udf10", "status", "lastname", "field1",
			"field2", "field3", "field4", "field5", "field6", "field7",
			"field8", "field9", "country", "hash", "firstname", "zipcode",
			"state", "name_on_card", "net_amount_debit", "payment_source",
			"wallet_amount","additionalCharges"};


	private String[] expectedFailurePayUWalletResponseKeys = {"amount",
			"PG_TYPE", "mode", "phone", "productinfo", "discount", "addedon",
			"unmappedstatus", "mihpayid", "address2", "address1", "city",
			"key", "bank_ref_num", "txnid", "email", "bankcode", "error",
			"error_Message", "udf1", "udf2", "udf3", "udf4", "udf5", "udf6",
			"udf7", "udf8", "udf9", "udf10", "status", "lastname", "field1",
			"field2", "field3", "field4", "field5", "field6", "field7",
			"field8", "field9", "country", "hash", "firstname", "zipcode",
			"state", "name_on_card", "net_amount_debit", "payment_source",
	"wallet_amount"};
	private String[] compulsiveWalletMerchantResponseKeys = {"amount", "PG_TYPE",
			"mode", "phone", "productinfo", "discount", "addedon",
			"unmappedstatus", "mihpayid", "address2", "address1", "city",
			"key", "bank_ref_num", "txnid", "email", "bankcode", "error",
			"error_Message", "udf1", "udf2", "udf3", "udf4", "udf5", "udf6",
			"udf7", "udf8", "udf9", "udf10", "status", "lastname", "field1",
			"field2", "field3", "field4", "field5", "field6", "field7",
			"field8", "field9", "country", "hash", "firstname", "zipcode",
			"state", "name_on_card", "net_amount_debit", "payment_source",};



	private String[] expectedCCOfferStoredResponseKeys = {"mihpayid", "mode",
			"status", "unmappedstatus", "key", "txnid", "amount", "discount",
			"addedon", "productinfo", "firstname", "lastname", "address1",
			"address2", "city", "state", "country", "zipcode", "email",
			"phone", "udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "udf7",
			"udf8", "udf9", "udf10", "hash", "field1", "field2", "field3",
			"field4", "field5", "field6", "field7", "field8", "field9",
			"PG_TYPE", "bank_ref_num", "bankcode", "error", "error_Message",
			"cardToken", "name_on_card", "cardnum", "cardhash",
			"net_amount_debit", "payment_source", "offer", "offer_availed",
	"offer_type"};
	private String[] expectedStoredOfferFailureResponseKeys = {"mihpayid",
			"mode", "status", "unmappedstatus", "key", "txnid", "amount",
			"discount", "addedon", "productinfo", "firstname", "lastname",
			"address1", "address2", "city", "state", "country", "zipcode",
			"email", "phone", "udf1", "udf2", "udf3", "udf4", "udf5", "udf6",
			"udf7", "udf8", "udf9", "udf10", "hash", "field1", "field2",
			"field3", "field4", "field5", "field6", "field7", "field8",
			"field9", "PG_TYPE", "bank_ref_num", "bankcode", "error",
			"error_Message", "cardToken", "name_on_card", "cardnum",
			"cardhash", "offer", "offer_type", "offer_failure_reason",
			"net_amount_debit", "payment_source"};
	private String[] expectedStoredOfferFailureDCResponseKeys = {"mihpayid",
			"mode", "status", "unmappedstatus", "key", "txnid", "amount",
			"discount", "addedon", "productinfo", "firstname", "lastname",
			"address1", "address2", "city", "state", "country", "zipcode",
			"email", "phone", "udf1", "udf2", "udf3", "udf4", "udf5", "udf6",
			"udf7", "udf8", "udf9", "udf10", "hash", "field1", "field2",
			"field3", "field4", "field5", "field6", "field7", "field8",
			"field9", "PG_TYPE", "bank_ref_num", "bankcode", "error",
			"error_Message", "name_on_card", "cardnum", "cardhash", "offer",
			"offer_type", "offer_failure_reason", "net_amount_debit",
	"payment_source"};

	public TestResponsePage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.wait(testConfig, 15);
		Browser.waitForPageLoad(this.testConfig, responseField);

		actualResponse = Helper.convertPhpArrayResponseToJavaList(responseField.getText());

		//Storing it so that it can be used globally
		testConfig.putRunTimeProperty("mihpayid", actualResponse.get("mihpayid"));
	}

	/**
	 * Verifies the COD transaction Response
	 * @param transactionData Transaction Data which was used to do transaction
	 * @param transactionRowNum Corresponding row number of Transaction data
	 * @param transactionId Transaction Id of this transaction in transaction page
	 * @param paymentTypeData Payment Type Data which was used to do payment, the expected TestResponseRow will also be read using this
	 * @param paymentTypeRowNum Corresponding row number of payment data
	 * @param codRow COD input data row
	 * @return if test response matches or not 
	 */
	public void VerifyTransactionResponse(TestDataReader transactionData, int transactionRowNum, TestDataReader paymentTypeData, int paymentTypeRowNum, int codRow)
	{
		if(null == paymentTypeData)
		{
			paymentTypeData = new TestDataReader(testConfig, "PaymentType");
		}

		TestDataReader codData = null;
		if(codRow != -1)
		{
			codData = new TestDataReader(testConfig, "COD");
		}
		String mode = paymentTypeData.GetData(paymentTypeRowNum, "mode").toLowerCase();
		//Variables to Calculate NetAmountDebit
		double amountToCalculateNetAmountDebit = 0.00;
		double discountToCalculateNetAmountDebit = 0.00;
		double additionalChargesToCalculateNetAmountDebit = 0.00;
		String statusToNetAmountDebit = null;

		switch(mode)
		{
		case "wallet" :
			if(testConfig.getRunTimeProperty("wallet_amount") != null){
				//In case the additional charges are set in PayU we use "additionalCharges". In case the additional charges are set in PayuMoney we use "convenienceFee"
				if(testConfig.getRunTimeProperty("additionalCharges") != null || testConfig.getRunTimeProperty("convenienceFee") != null){
					expectedResponseKeys = expectedSuccessPayUWalletResponseKeys;
				}
				else
					expectedResponseKeys = expectedFailurePayUWalletResponseKeys;
			}
			else
				expectedResponseKeys = compulsiveWalletMerchantResponseKeys;
			break;
		case "creditcard":
			expectedResponseKeys = expectedCCResponseKeys;

			if(testConfig.getRunTimeProperty("offerKey")!=null) 
				expectedResponseKeys = expectedCCOfferResponseKeys;
			if(testConfig.getRunTimeProperty("Offer_Failure_Reason")!=null)	{
				if(testConfig.getRunTimeProperty("CancelRetryOffer")!=null)
					expectedResponseKeys=expectedUserCancelledforOfferKeys;		
				else
					expectedResponseKeys = expectedCCFailedOfferResponseKeys;
			}
			if(testConfig.getRunTimeProperty("payment_using_wallet")!=null){
				expectedResponseKeys = compulsiveWalletMerchantResponseKeys;
			}

			//If stored card details are used, then card token is also returned
			String useStoredDetails = testConfig.getRunTimeProperty("UseStoredDetails");
			if (useStoredDetails != null
					&& (useStoredDetails.equals("1") || useStoredDetails
							.equals("2"))) {
				expectedResponseKeys = expectedCCStoredResponseKeys;
				if (testConfig.getRunTimeProperty("offerKey") != null){
					expectedResponseKeys = expectedCCOfferStoredResponseKeys;
				}



				if (testConfig.getRunTimeProperty("Offer_Failure_Reason") != null) {

					expectedResponseKeys = expectedStoredOfferFailureResponseKeys;
				}
				if(testConfig.getRunTimeProperty("no_cardToken")!=null){

					expectedResponseKeys = expectedCCStoredWithoutCardTokenResponseKeys;

				}
				if (testConfig.getRunTimeProperty("seamlessSavedCard") != null) {
					expectedResponseKeys = expectedSeamlessStoredResponseKeys;
					testConfig.removeRunTimeProperty("seamlessSavedCard");
				}
			}

			//Expected response array in case of SI merchant
			String SIvalue = testConfig.getRunTimeProperty("SI");
			if(SIvalue!=null)
			{	
				expectedResponseKeys = expectedCCStoredResponseKeys;

				if(testConfig.getRunTimeProperty("no_cardToken")!=null){
					expectedResponseKeys = expectedCCStoredWithoutCardTokenResponseKeys;

				}
			}

			//If enable_iss_bank_card_type param is enable for a merchant, then card_type and issuing_bank are also returned
			String cardType_bankName = testConfig.getRunTimeProperty("cardType");
			if(cardType_bankName!=null)
			{	
				expectedResponseKeys = expectedCCIssuingBankResponseKeys;

			}



			if(actualResponse.get("additionalCharges")!=null)
			{
				expectedResponseKeys = expectedCCConvenienceFeeResponseKeys;
				if(testConfig.getRunTimeProperty("offerKey")!=null)
				{
					expectedResponseKeys = expectedCCOfferConvenienceFeeResponseKeys;
					if(testConfig.getRunTimeProperty("Offer_Failure_Reason")!=null)	{
						if(testConfig.getRunTimeProperty("CancelRetryOffer")!=null)
							expectedResponseKeys=expectedUserCancelledConvenienceFeeforOfferKeys;		
						else
							expectedResponseKeys = expectedCCFailedConvenienceFeeOfferResponseKeys;
					}
				}		
			}

			break;

		case "goback":
			expectedResponseKeys = expectedUserCancResponseKeys;
			break;

		case "cod":
			expectedResponseKeys = expectedCODResponseKeys;
			if(actualResponse.get("additionalCharges")!=null)
			{
				expectedResponseKeys = expectedCODConvenienceFeeResponseKeys;
			}
			break;


		case "debitcard": 
			expectedResponseKeys = expectedDCResponseKeys;
			if(testConfig.getRunTimeProperty("offerKey")!=null) 
				expectedResponseKeys = expectedCCOfferResponseKeys;
			if(testConfig.getRunTimeProperty("Offer_Failure_Reason")!=null)	{
				if(testConfig.getRunTimeProperty("CancelRetryOffer")!=null)
					expectedResponseKeys=expectedUserCancelledforOfferKeys;		
				else
					expectedResponseKeys = expectedCCFailedOfferResponseKeys;
			}

			String cardType_bankname = testConfig.getRunTimeProperty("cardType");
			if(cardType_bankname!=null)
			{	
				expectedResponseKeys = expectedCCIssuingBankResponseKeys;

			}
			if(testConfig.getRunTimeProperty("payment_using_wallet")!=null){
				expectedResponseKeys = compulsiveWalletMerchantResponseKeys;
			}
			//If stored card details are used, then card token is also returned
			useStoredDetails = testConfig.getRunTimeProperty("UseStoredDetails");

			if (useStoredDetails != null
					&& (useStoredDetails.equals("1") || useStoredDetails
							.equals("2"))) {
				expectedResponseKeys = expectedCCStoredResponseKeys;

				if (testConfig.getRunTimeProperty("Offer_Failure_Reason") != null) {
					expectedResponseKeys = expectedStoredOfferFailureResponseKeys;
				}
				if (testConfig.getRunTimeProperty("usingStoredCard") != null) {
					expectedResponseKeys = expectedStoredOfferFailureDCResponseKeys;
				}

			}
			if(testConfig.getRunTimeProperty("no_cardToken")!=null){

				expectedResponseKeys = expectedCCStoredWithoutCardTokenResponseKeys;

			}
			if (testConfig.getRunTimeProperty("seamlessSavedCard") != null) {
				expectedResponseKeys = expectedSeamlessStoredResponseKeys;
				testConfig.removeRunTimeProperty("seamlessSavedCard");
			}

			if(actualResponse.get("additionalCharges")!=null)
			{
				expectedResponseKeys = expectedDCConvenienceFeeResponseKeys;
				if(testConfig.getRunTimeProperty("offerKey")!=null)
				{
					expectedResponseKeys = expectedCCOfferConvenienceFeeResponseKeys;
					if(testConfig.getRunTimeProperty("Offer_Failure_Reason")!=null)	{
						if(testConfig.getRunTimeProperty("CancelRetryOffer")!=null)
							expectedResponseKeys=expectedUserCancelledConvenienceFeeforOfferKeys;		
						else
							expectedResponseKeys = expectedCCFailedConvenienceFeeOfferResponseKeys;
					}
				}
				//If card number is special characters
				if(testConfig.getRunTimeProperty("InvalidCard")!=null)
				{	
					expectedResponseKeys = expectedSpecialCardNumberResponseKeys; 
				}
				//if(testConfig.getRunTimeProperty("additionalCharges")!=null && 
			}

			if(testConfig.getRunTimeProperty("IsSeamlessDCTxn")!=null){
				expectedResponseKeys=expectedCCResponseKeys;
			}
			break;

		case "netbanking":
			expectedResponseKeys = expectedNBResponseKeys; 
			if(testConfig.getRunTimeProperty("offerKey")!=null)
			{
				expectedResponseKeys = expectedCCOfferResponseKeys;
			}
			if(testConfig.getRunTimeProperty("Offer_Failure_Reason")!=null)	
				if(!testConfig.getRunTimeProperty("Offer_Failure_Reason").contentEquals("invalid key")){ 
					expectedResponseKeys = expectedCCFailedOfferResponseKeys;
				}
			break;


		case "emi" :
			expectedResponseKeys = expectedEMIResponseKeys;

			if (testConfig.getRunTimeProperty("offerKey") != null) {
				expectedResponseKeys = expectedEMIOfferResponseKeys;
			}

			if (actualResponse.get("additionalCharges") != null) {
				expectedResponseKeys = expectedEMIConvenienceFeeResponseKeys;
			}
			if (testConfig.getRunTimeProperty("Offer_Failure_Reason") != null) {
				expectedResponseKeys = expectedEMIOfferFailureResponseKeys;
			}
			break;

		case "cashcard":
			expectedResponseKeys = expectedCashCardResponseKeys; 
			if(actualResponse.get("additionalCharges")!=null)
			{
				expectedResponseKeys = expectedCashConvenienceFeeResponseKeys;
			}
			break;
		}

		//so that screenshot is not taken again and again, for each response key verification
		testConfig.enableScreenshot = false;

		int testResponseRowNum = Integer.parseInt(paymentTypeData.GetData(paymentTypeRowNum, "TestResponseRow"));
		if(testResponseRowNum == 3) //overriding for COD case 
		{
			paymentTypeRowNum = 226; 
			expectedResponseKeys = expectedUserCancResponseKeys;
		}

		Helper.compareEquals(testConfig, "Count of Response Parameters", expectedResponseKeys.length, actualResponse.size());

		TestDataReader testResponseData = new TestDataReader(testConfig, "TestResponse");

		testConfig.putRunTimeProperty("txnid", testConfig.getRunTimeProperty("transactionId"));
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 1, 1);
		Map<String,String> map1 = DataBase.executeSelectQuery(testConfig, 94, 1);

		for(String expectedKey:expectedResponseKeys)
		{
			String expectedValue = null;

			switch(expectedKey)
			{
			case "key": 
				expectedValue = transactionData.GetCurrentEnvironmentData(transactionRowNum, expectedKey);

				break;

			case "offer":
				expectedValue = testConfig.getRunTimeProperty("offerKey");
				break;
			case "offer_availed":
				if (testConfig.getRunTimeProperty("isMultiple")==null){
					expectedValue = testConfig.getRunTimeProperty("offerKey");	
				}

				if(testConfig.getRunTimeProperty("offeravailed")!=null)
					expectedValue=testConfig.getRunTimeProperty("offeravailed");
				break;
			case "offer_failure_reason":
				if(testConfig.getRunTimeProperty("Offer_Failure_Reason")!=null)
					expectedValue = testConfig.getRunTimeProperty("Offer_Failure_Reason");
				break;
			case "offer_type":
				//Sometimes the response can have truncated data, in such cases we can verify against runtime property value
				if(overrideExpectedTransactionData) 
				{
					String val = testConfig.getRunTimeProperty(expectedKey);
					if(val!=null) expectedValue = val;
					else expectedValue = "instant";
				}
				else
					expectedValue = "instant";
				break;
			case "amount":
			case "email":
			case "firstname":
			case "phone":
			case "productinfo":
			case "discount":
			case "lastname":
			case "address1":
			case "address2":
			case "city":
			case "state":
			case "zipcode":
			case "country":
			case "udf1":
			case "udf2":
			case "udf3":
			case "udf4":
			case "name_on_card":
			case "payment_source":
			case "udf5":

				expectedValue = transactionData.GetData(transactionRowNum, expectedKey);

				//Sometimes the response can have truncated data, in such cases we can verify against runtime property value
				if(overrideExpectedTransactionData) 
				{
					String val = testConfig.getRunTimeProperty(expectedKey);
					if(val!=null) expectedValue = val;
				}
				//amountToCalculateNetAmountDebit
				if(expectedKey.equals("amount")){
					if(expectedValue.equalsIgnoreCase("{skip}"))
					{
						amountToCalculateNetAmountDebit = 0.00;
					}
					else
					{
						amountToCalculateNetAmountDebit = Double.parseDouble(expectedValue);
					}
				}

				//discountToCalculateNetAmountDebit
;				if(expectedKey.equals("discount")){
					if(expectedValue.equalsIgnoreCase("{skip}"))
					{
						discountToCalculateNetAmountDebit = 0.00;
					}
					else
					{
						discountToCalculateNetAmountDebit = Double.parseDouble(expectedValue);
					}
				}
				//payment source in case of SI
				if(expectedKey.equals("payment_source")){
					String si_value =  testConfig.getRunTimeProperty("SI");
					if(si_value != null){
						if(si_value.equals("1")) expectedValue="sist";
						else if (si_value.equals("2")) expectedValue="sinst";
						else expectedValue = "payu";
					}
				}
				break;

			case "additionalCharges":

				expectedValue = transactionData.GetData(transactionRowNum, expectedKey);

				String keyvalue = null;
				if (paymentTypeData.GetData(paymentTypeRowNum, "mode").equals("creditcard")) 
				{
					if (testConfig.getRunTimeProperty("additionalCharges")==null){
						keyvalue = transactionData.GetData(transactionRowNum, expectedKey);
						String [] keyValue = keyvalue.split(":");
						keyvalue = keyValue[1];
						keyValue = keyvalue.split(",");
						expectedValue = keyValue[0].trim();
					}
					else {
						keyvalue = testConfig.getRunTimeProperty("additionalCharges");
						String [] keyValue = keyvalue.split(":");
						expectedValue = keyValue[1].trim();
					}
				}
				else if (paymentTypeData.GetData(paymentTypeRowNum, "mode").equals("debitcard")) 
				{
					if (testConfig.getRunTimeProperty("additionalCharges")==null){
						keyvalue = transactionData.GetData(transactionRowNum, expectedKey);
						String [] keyValue = keyvalue.split(":");
						keyvalue = keyValue[2];
						keyValue = keyvalue.split(",");
						expectedValue = keyValue[0].trim();
					}
					else {
						keyvalue = testConfig.getRunTimeProperty("additionalCharges");
						String [] keyValue = keyvalue.split(":");
						expectedValue = keyValue[1].trim();
					}
				}
				else if (paymentTypeData.GetData(paymentTypeRowNum, "mode").equals("netbanking")) 
				{
					if (testConfig.getRunTimeProperty("additionalCharges")==null){
						keyvalue = transactionData.GetData(transactionRowNum, expectedKey);

						String [] keyValue = keyvalue.split(":");
						keyvalue = keyValue[3];
						keyValue = keyvalue.split(",");
						expectedValue = keyValue[0].trim();
					}
					else {
						keyvalue = testConfig.getRunTimeProperty("additionalCharges");
						String [] keyValue = keyvalue.split(":");
						expectedValue = keyValue[1].trim();
					}
				}
				else if (paymentTypeData.GetData(paymentTypeRowNum, "mode").equals("emi")) 
				{
					if (testConfig.getRunTimeProperty("additionalCharges")==null){
						keyvalue = transactionData.GetData(transactionRowNum, expectedKey);
						String [] keyValue = keyvalue.split(":");
						keyvalue = keyValue[4];
						keyValue = keyvalue.split(",");
						expectedValue = keyValue[0].trim();
					}
					else {
						keyvalue = testConfig.getRunTimeProperty("additionalCharges");
						String [] keyValue = keyvalue.split(":");
						expectedValue = keyValue[1].trim();
					}
				}
				else if (paymentTypeData.GetData(paymentTypeRowNum, "mode").equals("cashcard")) 
				{
					if (testConfig.getRunTimeProperty("additionalCharges")==null){
						keyvalue = transactionData.GetData(transactionRowNum, expectedKey);
						String [] keyValue = keyvalue.split(":");
						keyvalue = keyValue[5];
						keyValue = keyvalue.split(",");
						expectedValue = keyValue[0].trim();
					}
					else {
						keyvalue = testConfig.getRunTimeProperty("additionalCharges");
						String [] keyValue = keyvalue.split(":");
						expectedValue = keyValue[1].trim();
					}
				}
				else if (paymentTypeData.GetData(paymentTypeRowNum, "mode").equals("cod")) 
				{
					if (testConfig.getRunTimeProperty("additionalCharges")==null){
						keyvalue = transactionData.GetData(transactionRowNum, expectedKey);
						String [] keyValue = keyvalue.split(":");
						keyvalue = keyValue[6];
						keyValue = keyvalue.split(",");
						expectedValue = keyValue[0].trim();
					}
					else {
						keyvalue = testConfig.getRunTimeProperty("additionalCharges");
						String [] keyValue = keyvalue.split(":");
						expectedValue = keyValue[1].trim();
					}
				}
				//additionalChargesToCalculateNetAmountDebit

				String convenienceFee = testConfig.getRunTimeProperty("convenienceFee");
				//if conv fee is applied on Payumoney for Wallet merchant
				if (convenienceFee != null){
					expectedValue = convenienceFee;
				}
				
				if(expectedValue.equalsIgnoreCase("{skip}"))
				{
					//if wallet amount set, then 
						additionalChargesToCalculateNetAmountDebit = 0.00;
				}
				else
				{
					additionalChargesToCalculateNetAmountDebit = Double.parseDouble(expectedValue);
				}
				break;


			case "mode":
				switch(paymentTypeData.GetData(paymentTypeRowNum, expectedKey))
				{
				case "creditcard": expectedValue = "CC";
				break;

				case "debitcard": expectedValue = "DC";
				break;

				case "netbanking": expectedValue ="NB";
				break;

				case "emi": expectedValue ="EMI";
				break;

				case "goback": expectedValue = "";
				break;

				case "cod": expectedValue = "COD";
				break;

				case "cashcard": expectedValue ="CASH";
				break;

				default: expectedValue = paymentTypeData.GetData(paymentTypeRowNum, expectedKey);
				break;
				}
				break;

			case "bankcode":
				expectedValue = paymentTypeData.GetData(paymentTypeRowNum, expectedKey);
				break;

			case "PG_TYPE":
				String pgtype = paymentTypeData.GetData(paymentTypeRowNum, expectedKey);
				TestDataReader pgInfoData = new TestDataReader(testConfig, "PGInfo");
				expectedValue = pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgtype, "PG_Label");
				break;

			case "status":
			case "unmappedstatus":
			case "error":
			case "error_Message":
			case "field1":
			case "field2":
			case "field3":
			case "field4":
			case "field5":
			case "field6":
			case "field7":
			case "field8":
				//case "additionalCharges":
			case "cardnum":
			case "cardhash":
				expectedValue = testResponseData.GetData(testResponseRowNum, expectedKey);
				if(overrideExpectedTransactionData) 
				{
					String val = testConfig.getRunTimeProperty(expectedKey);
					if(val!=null) expectedValue = val;
				}
				if(expectedKey.equals("status"))
				{
					statusToNetAmountDebit = expectedValue;
					if(expectedValue == "success")
						statusToNetAmountDebit = expectedValue;
				}
				break;
			case "cardToken":
				if(testConfig.getRunTimeProperty("card_token")!=null){
					expectedValue = testConfig.getRunTimeProperty("card_token");
				}else{
					//System.out.println(expectedKey+ " is expected. But actual is --"+map1.get(expectedKey));
					expectedValue = map1.get("card_token");
				}
				testConfig.putRunTimeProperty("StoreCardToken",expectedValue);
				break;
			case "issuing_bank":
				expectedValue = testResponseData.GetData(testResponseRowNum, expectedKey);
				if(overrideExpectedTransactionData) 
				{
					String val = testConfig.getRunTimeProperty(expectedKey);
					if(val!=null) expectedValue = val;
				}
				break;
			case "card_type":
				expectedValue = testResponseData.GetData(testResponseRowNum, expectedKey);
				if(overrideExpectedTransactionData) 
				{
					String val = testConfig.getRunTimeProperty(expectedKey);
					if(val!=null) expectedValue = val;
					//expectedCCIssuingBankResponseKeys
				}
				break;
			case "udf6":
			case "udf7":
			case "udf8":
			case "udf9":
			case "udf10":
				expectedValue = "";
				break;

			case "txnid":
				expectedValue = testConfig.getRunTimeProperty("transactionId");
				break;

			case "addedon":
				String addedon = testConfig.getRunTimeProperty("addedon");
				expectedValue = "{contains}" + addedon.substring(0,addedon.indexOf("-"));
				break;

			case "mihpayid":
			case "bank_ref_num":
				expectedValue = map.get(expectedKey);
				break;

				//No Verification since this is generated dynamically by the transaction
			case "orderid":
			case "field9":
			case "hash":	
				expectedValue = "{skip}";
				break;

			case "shipping_firstname":
				expectedValue = codData.GetData(codRow, "Fname");
				break;
			case "shipping_lastname":
				expectedValue = codData.GetData(codRow, "Lname");
				break;
			case "shipping_address1":
				expectedValue = codData.GetData(codRow, "Address1");
				break;
			case "shipping_address2":
				expectedValue = codData.GetData(codRow, "Address2");
				break;
			case "shipping_city":
				expectedValue = codData.GetData(codRow, "City");
				break;
			case "shipping_country":
				expectedValue = codData.GetData(codRow, "Country");
				break;
			case "shipping_state":
				expectedValue = codData.GetData(codRow, "State");
				break;
			case "shipping_zipcode":
				expectedValue = codData.GetData(codRow, "Zip Code");
				break;
			case "shipping_phone":
				expectedValue = codData.GetData(codRow, "Mobile Number");
				break;
			case "shipping_phoneverified":
				expectedValue = "";
				break;
			case "net_amount_debit":
				expectedValue = "0.00";
				if(statusToNetAmountDebit.equals("success"))
				{
					if (actualResponse.get("additionalCharges")!=null){
						additionalChargesToCalculateNetAmountDebit = Double.valueOf(actualResponse.get("additionalCharges")); 
					}

					double netAmountDebit = amountToCalculateNetAmountDebit - discountToCalculateNetAmountDebit + additionalChargesToCalculateNetAmountDebit;
					expectedValue = Double.toString(netAmountDebit);
					if(expectedValue.endsWith(".00"))
						expectedValue = expectedValue.substring(0, expectedValue.length() - 3);
					if(expectedValue.endsWith(".0"))
						expectedValue = expectedValue.substring(0, expectedValue.length() - 2);
				}
				break;
			case "wallet_amount" :
				expectedValue = testConfig.getRunTimeProperty("wallet_amount");
				break;
			default:
				expectedValue = expectedKey;
				break;
			}

			if(expectedValue == null)
			{
				Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, actualResponse.get(expectedKey));
				continue;
			}

			//No need to verify if we did not entered this value
			if(expectedValue.equalsIgnoreCase("{skip}")) 
			{
				testConfig.logWarning("Skipping the verification of Response Param '" + expectedKey + "' as '" + actualResponse.get(expectedKey) + "'");
				continue;
			}

			if(expectedValue.startsWith("{contains}"))
			{
				expectedValue = expectedValue.replace("{contains}", "");
				Helper.compareContains(testConfig, "Response Param '" + expectedKey + "'", expectedValue, actualResponse.get(expectedKey));
			}

			else
			{
				Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, actualResponse.get(expectedKey));
			}
		}

		//restore the screenshot value
		testConfig.enableScreenshot = true;

		Helper.compareTrue(testConfig, "Test Response", testConfig.getTestResult());

	}

	/**
	 * Verifies the transaction Response
	 * @param transactionData Transaction Data which was used to do transaction
	 * @param transactionRowNum Corresponding row number of Transaction data
	 * @param transactionId Transaction Id of this transaction in transaction page
	 * @param paymentTypeData Payment Type Data which was used to do payment, the expected TestResponseRow will also be read using this
	 * @param paymentTypeRowNum Corresponding row number of payment data
	 * @return if test response matches or not 
	 */
	public void VerifyTransactionResponse(TestDataReader transactionData, int transactionRowNum, TestDataReader paymentTypeData, int paymentTypeRowNum)
	{
		VerifyTransactionResponse(transactionData, transactionRowNum, paymentTypeData, paymentTypeRowNum, -1);
	}

	/**
	 * Verify the transaction response after customization
	 * @return
	 */
	public String getHtmlText(){
		String htmlText = null;
		if(Element.IsElementDisplayed(testConfig, Element.getPageElement(testConfig, How.id, "contents"))){
			htmlText =  Element.getPageElement(testConfig, How.id, "contents").getText().replaceAll("<[^>]+>", "");
		}
		return htmlText;
	}

	public HomePage ClickHomeLink()
	{
		Element.click(testConfig, homeLink, "Click Home Page Link");
		return new HomePage(testConfig);
	}

	public void verifyDetailsFromDatabase_ForMultipleOffers(int transactionRowNum){
		String payuId = actualResponse.get("mihpayid");

		TestDataReader data = new TestDataReader(testConfig, "TransactionDetails");

		String merchantId = data.GetData(transactionRowNum, "merchantid");
		testConfig.putRunTimeProperty("merchantid", merchantId);
		testConfig.putRunTimeProperty("payu_id", payuId);
		String status =actualResponse.get("status");
		if(status.equals("failure")){
			status ="failed";
		}
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 105, 1);

		Helper.compareEquals(testConfig, "Response Param '" + "Status" + "'", status ,map.get("status"));
		Helper.compareEquals(testConfig, "Response Param '" + "Offer type" + "'", actualResponse.get("offer_type"),map.get("offer_type"));
		if(testConfig.getRunTimeProperty("nonExistantOfferKey")!=null){
			Helper.compareEquals(testConfig, "Response Param '" + "Offer Key" + "'", actualResponse.get("offer"),map.get("offer_key"));
		}else{
			Helper.compareEquals(testConfig, "Response Param '" + "Offer Key" + "'", actualResponse.get("offer_availed"),map.get("offer_key"));
		}

		Helper.compareEquals(testConfig, "Response Param '" + "Discount" + "'", actualResponse.get("discount"),map.get("discount"));
	}

	/**
	 * Verifies following details from database
	 * Merchantid,status, mode,ibibo code,payment source and discount from transaction table
	 * @param transactionRowNum
	 */
	public void verifyDetailsFromDatabase_ForWalletMoney(int transactionRowNum) {
		String payuId = actualResponse.get("mihpayid");

		TestDataReader data = new TestDataReader(testConfig,
				"TransactionDetails");
		String modeForWallet = testConfig.getRunTimeProperty("modeForWallet");
		String ibiboCode = modeForWallet;
		if(testConfig.getRunTimeProperty("ibiboCode")!=null){
			ibiboCode = testConfig.getRunTimeProperty("ibiboCode");
		}
		testConfig.putRunTimeProperty("payu_id", payuId);
		String merchantid = data.GetData(transactionRowNum, "merchantid");
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 118,
				1);

		Helper.compareEquals(testConfig, "Response Param '" + "Merchant ID"
				+ "'", merchantid, map.get("merchantid"));

		if (testConfig.getRunTimeProperty("transactionFailedMessageExpected") != null) {

			Helper.compareEquals(testConfig, "Response Param '"
					+ "Unmapped Status for Wrong Payment Type" + "'",
					actualResponse.get("unmappedstatus"), map.get("status"));
		} else {

			Helper.compareEquals(testConfig, "Response Param '" + "Status"
					+ "'", actualResponse.get("unmappedstatus"), map.get("status"));

		}
		Helper.compareEquals(testConfig, "Response Param '" + "mode" + "'",
				modeForWallet, map.get("mode"));

		Helper.compareEquals(testConfig, "Response Param '" + "Ibibo Code"
				+ "'", ibiboCode, map.get("ibibo_code"));

		Helper.compareEquals(testConfig, "Response Param '" + "Payment Source"
				+ "'", "wallet", map.get("payment_source"));

		Helper.compareEquals(testConfig, "Response Param '" + "Discount" + "'",
				actualResponse.get("discount"), map.get("discount"));

	}

}
