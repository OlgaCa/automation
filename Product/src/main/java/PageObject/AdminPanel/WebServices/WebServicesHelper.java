package PageObject.AdminPanel.WebServices;

import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.GmailLogin;
import Utils.GmailVerification;
import Utils.Helper;
import Utils.TestDataReader;

public class WebServicesHelper {

	private Config testConfig;

	public WebServicesHelper(Config testConfig) 
	{
		this.testConfig = testConfig;
	}

	public TestDataReader paymentTypeData;

	/**Get the bounced transaction
	 * @return merchant key
	 */
	public String GetBouncedTransactions() 
	{
		//query to get transaction id of latest bounced transaction 
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 6, 1);
		String txn_id = map.get("txnid");
		testConfig.putRunTimeProperty("txn_id", txn_id);

		map = DataBase.executeSelectQuery(testConfig, 1, 1);
		String merchantid = map.get("merchantid");
		testConfig.putRunTimeProperty("key", merchantid);

		map = DataBase.executeSelectQuery(testConfig, 8, 1);
		String key = map.get("key");
		testConfig.putRunTimeProperty("key", key);

		return key;
	}

	/**Get the bounced transaction's txn id
	 * @return txn_id
	 */
	public String GetBouncedTransactionsTxnID() 
	{
		//query to get transaction id of latest bounced transaction 
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 6, 1);
		String txn_id = map.get("txnid");
		testConfig.putRunTimeProperty("txn_id", txn_id);

		return txn_id;
	}

	/**Get the dropped transaction
	 * @return merchant key
	 */
	public String GetDroppedTransactions() 
	{
		//query to get transaction id of latest bounced transaction 
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 7, 1);
		String txn_id = map.get("txnid");
		testConfig.putRunTimeProperty("txn_id", txn_id);

		map = DataBase.executeSelectQuery(testConfig, 1, 1);
		String merchantid = map.get("merchantid");
		testConfig.putRunTimeProperty("key", merchantid);

		map = DataBase.executeSelectQuery(testConfig, 8, 1);
		String key = map.get("key");
		testConfig.putRunTimeProperty("key", key);

		return key;
	}

	/**Get the dropped transaction's txn id
	 * @return txn_id
	 */
	public String GetDroppedTransactionsTxnID() 
	{
		//query to get transaction id of latest bounced transaction 
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 7, 1);
		String txn_id = map.get("txnid");
		testConfig.putRunTimeProperty("txn_id", txn_id);

		return txn_id;
	}

	//***********Store Card API functions****************
	
//Get_User_card
	private String [] expected_get_user_card_header_Keys= {"status", "msg", "user_cards"};
	private String [] expected_get_user_card_intermediate_Keys= {"23b1b658c3683da34040379f03042a3589a8d3c2"};
	private String [] expected_get_user_card_respKeys= {"name_on_card", "card_name", "expiry_year", "expiry_month", "card_type", "card_token", "is_expired", "card_mode", "card_no", "card_brand", "card_bin"};
	private String [] expected_get_user_card_error_respKeys= {"status", "msg"};

	//Definition get_user_card_executeAdminPanel
	public String get_user_cards_executeAdminPanel(HomePage home, String merchantKey, String userCrendential)
	{
		testConfig.putRunTimeProperty("user_credentials", userCrendential);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 10);

		return webServiceResponse;
	}
	    
	//Definition get_user_card_verify
	public void get_user_card_verify(String actualWebServiceResponse, String status, String msg, String name_on_card, String card_name, String expiry_year, String expiry_month, String card_type, String card_token, String is_expired, String card_mode, String card_no, String card_brand, String card_bin)
	{
		switch(msg)
		{
		case "Cards fetched Succesfully":
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
				
			//Verify parent status and msg
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("msg", msg);
			VerifyWebServiceResponse(expected_get_user_card_header_Keys, null, actualResponse);
				
			//Verify intermediate key
			Hashtable<String, String> webResponseIntermediate = Helper.convertPhpArrayResponseToJavaList(actualResponse.get("user_cards"));
			VerifyWebServiceResponse(expected_get_user_card_intermediate_Keys, null, webResponseIntermediate);
	            			
			//Verify card details
			Hashtable<String, String> webResponseCard_details = Helper.convertPhpArrayResponseToJavaList(webResponseIntermediate.get("23b1b658c3683da34040379f03042a3589a8d3c2"));
				
			//populate the expected values in run time variables for VerifyWebServiceResponse function call
			testConfig.putRunTimeProperty("name_on_card", name_on_card);
			testConfig.putRunTimeProperty("card_name", card_name);
			testConfig.putRunTimeProperty("expiry_year", expiry_year);
			testConfig.putRunTimeProperty("expiry_month", expiry_month);
			testConfig.putRunTimeProperty("card_type", card_type);
			testConfig.putRunTimeProperty("card_token", card_token);
			testConfig.putRunTimeProperty("is_expired", is_expired);
			testConfig.putRunTimeProperty("card_mode", card_mode);
			testConfig.putRunTimeProperty("card_no", card_no);
			testConfig.putRunTimeProperty("card_brand", card_brand);
			testConfig.putRunTimeProperty("card_bin", card_bin);
			VerifyWebServiceResponse(expected_get_user_card_respKeys, null, webResponseCard_details);
			break;
				
		default:
	        actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
				
			//Verify status and msg
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("msg", msg);
			VerifyWebServiceResponse(expected_get_user_card_error_respKeys, null, actualResponse);
			break;
		}
	}

//save_user_card
	private String [] expected_save_user_card_respKeys = {"status", "msg", "cardToken", "card_number", "card_label"};
	private String [] invalid_credentials_expected_save_user_card_respKeys = {"status", "msg"};

	//Definition save_user_card_executeAdminPanel	
	public String save_user_card_executeAdminPanel(HomePage home, String merchantKey, String userCredentials, String cardName, String mode, String cardType, String nameOnCard, String cardNo, String cardExpMon, String cardExpYr)
	{
		
		String cardMode = "";
		switch (mode)
		{
		case "creditcard": cardMode = "CC";
		break;
		case "debitcard": cardMode = "DC";
		break;
		case "emi": cardMode = "EMI";
		break;
		}
			
		testConfig.putRunTimeProperty("userCredentials", userCredentials);
		testConfig.putRunTimeProperty("cardName", cardName);
		testConfig.putRunTimeProperty("cardMode", cardMode);
		testConfig.putRunTimeProperty("cardType", cardType);
		testConfig.putRunTimeProperty("nameOnCard", nameOnCard);
		testConfig.putRunTimeProperty("cardNo", cardNo);
		testConfig.putRunTimeProperty("cardExpMon", cardExpMon);
		testConfig.putRunTimeProperty("cardExpYr", cardExpYr);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 23);
		return webServiceResponse;
	}
	
	//Definition save_user_card_verify 
	public void save_user_card_verify(String actualWebServiceResponse, String status, String msg, String cardToken, String card_number, String card_label)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
		
		switch(msg)
		{
		case "Card Stored Successfully.":
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("msg", msg);
			testConfig.putRunTimeProperty("cardToken", cardToken);
			testConfig.putRunTimeProperty("card_number", card_number);
			testConfig.putRunTimeProperty("card_label", card_label);
			VerifyWebServiceResponse(expected_save_user_card_respKeys, null, actualResponse);
			testConfig.removeRunTimeProperty("cardToken");
			break;
		
		default:
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("msg", msg);
			VerifyWebServiceResponse(invalid_credentials_expected_save_user_card_respKeys, null, actualResponse);
			break;
		}
		
}
	
	//edit_user_card
		private String [] expected_edit_user_card_respKeys = {"status", "msg", "cardToken", "card_number", "card_label"};
		private String [] invalid_credentials_expected_edit_user_card_respKeys = {"status", "msg"};
		
		//Definition edit_user_card_executeAdminPanel
		public String edit_user_card_executeAdminPanel(HomePage home, String merchantKey, String userCredentials, String cardToken, String cardName, String mode, String cardType, String nameOnCard, String cardNo, String cardExpMon, String cardExpYr)
		{
			
			String cardMode = "";
			switch (mode)
			{
			case "creditcard": cardMode = "CC";
			break;
			case "debitcard": cardMode = "DC";
			break;
			case "emi": cardMode = "EMI";
			break;
			}
				
			testConfig.putRunTimeProperty("userCredentials", userCredentials);
			testConfig.putRunTimeProperty("cardToken", cardToken);
			testConfig.putRunTimeProperty("cardName", cardName);
			testConfig.putRunTimeProperty("cardMode", cardMode);
			testConfig.putRunTimeProperty("cardType", cardType);
			testConfig.putRunTimeProperty("nameOnCard", nameOnCard);
			testConfig.putRunTimeProperty("cardNo", cardNo);
			testConfig.putRunTimeProperty("cardExpMon", cardExpMon);
			testConfig.putRunTimeProperty("cardExpYr", cardExpYr);
			String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 24);
			return webServiceResponse;
		}

		//Definition edit_user_card_verify 
		public void edit_user_card_verify(String actualWebServiceResponse, String status, String msg, String cardToken, String card_number, String card_label)
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
			
			switch(msg)
			{
			case "Test CardQBKSbB-CC edited Successfully.":
				testConfig.putRunTimeProperty("status", status);
				testConfig.putRunTimeProperty("msg", msg);
				testConfig.putRunTimeProperty("cardToken", cardToken);
				testConfig.putRunTimeProperty("card_number", card_number);
				testConfig.putRunTimeProperty("card_label", card_label);
				VerifyWebServiceResponse(expected_edit_user_card_respKeys, null, actualResponse);
				testConfig.removeRunTimeProperty("cardToken");
				break;
			
			default:
				testConfig.putRunTimeProperty("status", status);
				testConfig.putRunTimeProperty("msg", msg);
				VerifyWebServiceResponse(invalid_credentials_expected_edit_user_card_respKeys, null, actualResponse);
				break;
			}
	}

//delete_user_card
		private String [] expected_delete_user_card_respKeys = {"status", "msg"};
		private String [] invalid_credentials_expected_delete_user_card_respKeys = {"status", "msg"};
		
		//Definition delete_user_card_executeAdminPanel
		public String delete_user_card_executeAdminPanel(HomePage home, String merchantKey, String userCredentials, String cardToken)
		{		
			testConfig.putRunTimeProperty("userCredentials", userCredentials);
			testConfig.putRunTimeProperty("cardToken", cardToken);
			String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 25);
			return webServiceResponse;
		}

		//Definition edit_user_card_verify 
		public void delete_user_card_verify(String actualWebServiceResponse, String status, String msg)
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
					
			switch(msg)
			{
			case "Test CardQBKSbB-CC deleted successfully":
				testConfig.putRunTimeProperty("status", status);
				testConfig.putRunTimeProperty("msg", msg);
				VerifyWebServiceResponse(expected_delete_user_card_respKeys, null, actualResponse);
				break;
					
			default:
				testConfig.putRunTimeProperty("status", status);
				testConfig.putRunTimeProperty("msg", msg);
				VerifyWebServiceResponse(invalid_credentials_expected_delete_user_card_respKeys, null, actualResponse);
				break;
			}
					
	    }
		
//get_usercard_details
		private String [] expected_get_usercard_details_header_Keys= {"status", "msg", "card_details"};
		private String [] expected_get_usercard_details_respKeys= {"ccard_number", "cc_exp_yr", "cc_exp_mon", "card_type", "cname_on_card", "card_token", "card_mode"};
		private String [] expected_get_usercard_details_error_respKeys= {"status", "msg"};
		
		//Definition get_usercard_details_executeAdminPanel
		public String get_usercard_details_executeAdminPanel(HomePage home, String merchantKey, String userCredentials, String cardToken)
		{		
			testConfig.putRunTimeProperty("userCredentials", userCredentials);
			testConfig.putRunTimeProperty("cardToken", cardToken);
			String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 26);
			return webServiceResponse;
		}
		//Definition get_user_card_verify
		public void get_usercard_details_verify(String actualWebServiceResponse, String status, String msg, String cc_exp_yr, String cc_exp_mon, String card_type, String cname_on_card, String card_token, String card_mode)
		{
			switch(msg)
			{
			case "Card details fetched Succesfully":
				Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
					
				//Verify parent status and msg
				testConfig.putRunTimeProperty("status", status);
				testConfig.putRunTimeProperty("msg", msg);
				VerifyWebServiceResponse(expected_get_usercard_details_header_Keys, null, actualResponse);
					     			
				//Verify card details
				Hashtable<String, String> webResponseCard_details = Helper.convertPhpArrayResponseToJavaList(actualResponse.get("card_details"));
					
				//populate the expected values in run time variables for VerifyWebServiceResponse function call
				testConfig.putRunTimeProperty("cc_exp_yr", cc_exp_yr);
				testConfig.putRunTimeProperty("cc_exp_mon", cc_exp_mon);
				testConfig.putRunTimeProperty("card_type", card_type);
				testConfig.putRunTimeProperty("cname_on_card", cname_on_card);
				testConfig.putRunTimeProperty("card_token", card_token);
				testConfig.putRunTimeProperty("card_mode", card_mode);
				VerifyWebServiceResponse(expected_get_usercard_details_respKeys, null, webResponseCard_details);
				break;
					
			default:
		        actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
					
				//Verify status and msg
				testConfig.putRunTimeProperty("status", status);
				testConfig.putRunTimeProperty("msg", msg);
				VerifyWebServiceResponse(expected_get_usercard_details_error_respKeys, null, actualResponse);
				break;
			}
		}
		
	//////CHECK_PAYMENT
	/**
	 * Execute the check_payment webservice from admin panel, and stores the returned request_id in runtimeproperty
	 * @param home
	 * @param merchantKey
	 * @param mihpayid
	 * @return
	 */
	public String check_payment_executeAdminPanel(HomePage home, String merchantKey, String mihpayid)
	{
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 1);

		try //store the requestid if present
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(webServiceResponse);
			Hashtable<String, String> webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(actualResponse.get("transaction_details"));

			//request id is given out when we call this webservice, storing it for future verification
			testConfig.putRunTimeProperty("request_id", webResponseTran_details.get("request_id"));
		}
		catch(Exception e)
		{
			//request id is not present in case wrong input is given
		}

		return webServiceResponse;
	}

	private String [] expected_header_Keys= {"status", "msg", "transaction_details"};

	private String [] expected_error_header_Keys= {"status", "msg"};

	private String [] expected_check_payment_respKeys= {"request_id", "bank_ref_num", "net_amount", "mihpayid", "amt", 
			"disc", "mode" , "txnid", "amount", "amount_paid",	"discount", "udf1", "udf2", "udf3", "udf4", "udf5", 
			"field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9", "status", 
			"unmappedstatus","firstname","bankcode","productinfo","name_on_card","card_no", "PG_TYPE", "Merchant_UTR", "Settled_At", "additional_charges", "net_amount_debit"};

	private String [] expected_cod_check_payment_respKeys= {"request_id", "bank_ref_num", "net_amount", "mihpayid", "amt", 
			"disc", "mode" , "txnid", "amount", "amount_paid",	"discount", "additional_charges", "udf1", "udf2", "udf3", "udf4", "udf5", 
			"field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9", "status", "net_amount_debit", 
			"unmappedstatus", "firstname", "bankcode", "productinfo", "PG_TYPE","shipping_firstname","shipping_lastname", "shipping_address1","shipping_address2",
			"shipping_city","shipping_phone", "shipping_state", "shipping_zipcode", "shipping_country",  "Merchant_UTR", "Settled_At"};

	/**
	 * Verifies the result of check_payment webservice
	 * @param actualWebServiceResponse
	 * @param expectedTransactionResponse
	 * @param parentStatus
	 * @param parentMsg
	 * @param net_amount
	 */
	
	
	public void check_payment_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String parentStatus, String parentMsg, String net_amount, String Merchant_UTR, String Settled_At)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
		testConfig.putRunTimeProperty("status", parentStatus);
		testConfig.putRunTimeProperty("msg", parentMsg);

		
		if(parentStatus.equals("0"))
		{
			//Verify only parent status and msg
			VerifyWebServiceResponse(expected_error_header_Keys, expectedTransactionResponse, actualResponse);
		}

		else if (expectedTransactionResponse.get("mode").equals("COD")) {

			//Verify parent status and msg
			VerifyWebServiceResponse(expected_header_Keys, expectedTransactionResponse, actualResponse);

			//Verify transaction details
			Hashtable<String, String> webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(actualResponse.get("transaction_details"));

			//populate the expected values in run time variables for VerifyWebServiceResponse function call
			testConfig.putRunTimeProperty("bank_ref_num", expectedTransactionResponse.get("bank_ref_num"));
			testConfig.putRunTimeProperty("net_amount", net_amount);
			testConfig.putRunTimeProperty("status", "pending");
			testConfig.putRunTimeProperty("amt", webResponseTran_details.get("amt"));
			testConfig.putRunTimeProperty("amount_paid", webResponseTran_details.get("amount_paid"));
			testConfig.putRunTimeProperty("Merchant_UTR", Merchant_UTR);
			testConfig.putRunTimeProperty("Settled_At", Settled_At);
			testConfig.putRunTimeProperty("net_amount_debit", expectedTransactionResponse.get("net_amount_debit"));
			if(expectedTransactionResponse.get("additionalCharges")!=null)
				testConfig.putRunTimeProperty("additional_charges", expectedTransactionResponse.get("additionalCharges"));
			else 
				testConfig.putRunTimeProperty("additional_charges", "0.00");	

			VerifyWebServiceResponse(expected_cod_check_payment_respKeys, expectedTransactionResponse, webResponseTran_details);
		}

		else {

			//Verify parent status and msg
			VerifyWebServiceResponse(expected_header_Keys, expectedTransactionResponse, actualResponse);

			//Verify transaction details
			Hashtable<String, String> webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(actualResponse.get("transaction_details"));

			//populate the expected values in run time variables for VerifyWebServiceResponse function call
			testConfig.putRunTimeProperty("bank_ref_num", expectedTransactionResponse.get("bank_ref_num"));
			testConfig.putRunTimeProperty("net_amount", net_amount);
			testConfig.putRunTimeProperty("status", expectedTransactionResponse.get("status"));
			testConfig.putRunTimeProperty("amt", webResponseTran_details.get("amt"));
			testConfig.putRunTimeProperty("amount_paid", webResponseTran_details.get("amount_paid"));
			testConfig.putRunTimeProperty("Merchant_UTR", Merchant_UTR);
			testConfig.putRunTimeProperty("Settled_At", Settled_At);
			
			if(expectedTransactionResponse.get("additionalCharges")!=null)
				testConfig.putRunTimeProperty("additional_charges", expectedTransactionResponse.get("additionalCharges"));
			else 
				testConfig.putRunTimeProperty("additional_charges", "0.00");
				
			testConfig.putRunTimeProperty("net_amount_debit", expectedTransactionResponse.get("net_amount_debit"));

			VerifyWebServiceResponse(expected_check_payment_respKeys, expectedTransactionResponse, webResponseTran_details);
		}


	}

	//////CHECK_OFFER_STATUS
	/**
	 * Execute the check_offer_status webservice from admin panel, and stores the returned request_id in runtimeproperty
	 * @param home
	 * @param merchantKey
	 * @param offerKey
	 * @param amount
	 * @param pg
	 * @param bankcode
	 * @param ccnum
	 * @param ccname
	 * @param phone
	 * @param email
	 * @return
	 */
	public String check_offer_status_executeAdminPanel(HomePage home, String merchantKey, String offerKey, String amount, 
			String pg, String bankcode, String ccnum, String ccname, String phone, String email)
	{
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("amount", amount);
		testConfig.putRunTimeProperty("pg", pg);
		testConfig.putRunTimeProperty("bankcode", bankcode);
		testConfig.putRunTimeProperty("ccnum", ccnum);
		testConfig.putRunTimeProperty("ccname", ccname);
		testConfig.putRunTimeProperty("phone", phone);
		testConfig.putRunTimeProperty("email", email);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 16);

		return webServiceResponse;
	}


	public void check_valid_offer_status_verify(String actualWebServiceResponse, TestDataReader offerApi, String parentStatus, String parentMsg, String discount, String mode, String offerKey, String offerType, String offerAvailedCount, String offerRemainingCount)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		//Verify parent status and msg
		Helper.compareEquals(testConfig, "Check Offer Status", offerApi.GetData(1, "OfferStatus"), parentStatus);
		Helper.compareEquals(testConfig, "Check Offer Message", offerApi.GetData(1, "OfferMsg"), parentMsg);
		Helper.compareEquals(testConfig, "Discount on Offer", discount, actualResponse.get("discount"));
		Helper.compareEquals(testConfig, "Category", mode, actualResponse.get("category"));
		Helper.compareEquals(testConfig, "Offer Key", offerKey, actualResponse.get("offer_key"));
		Helper.compareEquals(testConfig, "Offer Type", offerType, actualResponse.get("offer_type"));
		Helper.compareEquals(testConfig, "Offer Availed Count", offerAvailedCount, actualResponse.get("offer_availed_count"));
		Helper.compareEquals(testConfig, "Offer Remaining Count", offerRemainingCount, actualResponse.get("offer_remaining_count"));

	}

	public void check_invalid_offer_status_verify(String actualWebServiceResponse, TestDataReader offerApi, String parentStatus, String parentMsg, String errorCode, String mode, String offerKey, String offerType, String offerAvailedCount, String offerRemainingCount)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		//Verify parent status and msg
		Helper.compareEquals(testConfig, "Check Offer Status", offerApi.GetData(2, "OfferStatus"), parentStatus);
		Helper.compareEquals(testConfig, "Check Offer Message", offerApi.GetData(2, "OfferMsg"), parentMsg);
		Helper.compareEquals(testConfig, "Error on Offer", errorCode, actualResponse.get("error_code"));
		if(mode!="")
		Helper.compareEquals(testConfig, "Category", mode, actualResponse.get("category"));
		Helper.compareEquals(testConfig, "Offer Key", offerKey, actualResponse.get("offer_key"));
		Helper.compareEquals(testConfig, "Offer Type", offerType, actualResponse.get("offer_type"));
		Helper.compareEquals(testConfig, "Offer Availed Count", offerAvailedCount, actualResponse.get("offer_availed_count"));
		Helper.compareEquals(testConfig, "Offer Remaining Count", offerRemainingCount, actualResponse.get("offer_remaining_count"));	

	}


	//////CANCEL_REFUND TRANSACTION

	public String refund_transaction_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String token, String amount)
	{
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		testConfig.putRunTimeProperty("token", token);
		testConfig.putRunTimeProperty("amount", amount);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 3);

		try //store the requestid if present
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(webServiceResponse);

			//request id is given out when we call this webservice, storing it for future verification
			testConfig.putRunTimeProperty("txn_update_id", actualResponse.get("txn_update_id"));
		}
		catch(Exception e)
		{
			//request id is not present in case wrong input is given
		}

		return webServiceResponse;
	}

	public String refund_transaction_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String amount)
	{
		return refund_transaction_executeAdminPanel(home, merchantKey, mihpayid, Helper.generateRandomAlphaNumericString(10), amount);
	}

	public String cancel_transaction_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String token, String amount)
	{
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		testConfig.putRunTimeProperty("token", Helper.generateRandomAlphaNumericString(10));
		testConfig.putRunTimeProperty("amount", amount);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 2);

		try //store the requestid if present
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(webServiceResponse);

			//request id is given out when we call this webservice, storing it for future verification
			testConfig.putRunTimeProperty("request_id", actualResponse.get("request_id"));
		}
		catch(Exception e)
		{
			//request id is not present in case wrong input is given
		}

		return webServiceResponse;
	}

	public String cancel_transaction_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String amount)
	{
		return cancel_transaction_executeAdminPanel(home, merchantKey, mihpayid, Helper.generateRandomAlphaNumericString(10), amount);
	}

	public String cod_cancel_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String token, String amount)
	{
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		testConfig.putRunTimeProperty("token", token);
		testConfig.putRunTimeProperty("amount", amount);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 12);

		try //store the requestid if present
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(webServiceResponse);

			//request id is given out when we call this webservice, storing it for future verification
			testConfig.putRunTimeProperty("request_id", actualResponse.get("request_id"));
		}
		catch(Exception e)
		{
			//request id is not present in case wrong input is given
		}

		return webServiceResponse;
	}

	public String cod_cancel_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String amount)
	{
		return cod_cancel_executeAdminPanel(home, merchantKey, mihpayid, Helper.generateRandomAlphaNumericString(10), amount);
	}

	public String cod_verify_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String token, String amount)
	{
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		testConfig.putRunTimeProperty("token", token);
		testConfig.putRunTimeProperty("amount", amount);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 13);

		try //store the requestid if present
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(webServiceResponse);

			//request id is given out when we call this webservice, storing it for future verification
			testConfig.putRunTimeProperty("request_id", actualResponse.get("request_id"));
		}
		catch(Exception e)
		{
			//request id is not present in case wrong input is given
		}

		return webServiceResponse;
	}

	public String cod_verify_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String amount)
	{
		return cod_verify_executeAdminPanel(home, merchantKey, mihpayid, Helper.generateRandomAlphaNumericString(10), amount);
	}

	public String cod_settled_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String token, String amount)
	{
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		testConfig.putRunTimeProperty("token", token);
		testConfig.putRunTimeProperty("amount", amount);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 14);

		try //store the requestid if present
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(webServiceResponse);

			//request id is given out when we call this webservice, storing it for future verification
			testConfig.putRunTimeProperty("request_id", actualResponse.get("request_id"));
		}
		catch(Exception e)
		{
			//request id is not present in case wrong input is given
		}

		return webServiceResponse;
	}

	public String cod_settled_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String amount)
	{
		return cod_cancel_executeAdminPanel(home, merchantKey, mihpayid, Helper.generateRandomAlphaNumericString(10), amount);
	}

	public String cancelrefund_transaction_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String amount)
	{
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		testConfig.putRunTimeProperty("token", Helper.generateRandomAlphaNumericString(10));
		testConfig.putRunTimeProperty("amount", amount);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 7);

		try //store the requestid if present
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(webServiceResponse);

			//request id is given out when we call this webservice, storing it for future verification
			testConfig.putRunTimeProperty("request_id", actualResponse.get("request_id"));
		}
		catch(Exception e)
		{
			//request id is not present in case wrong input is given
		}

		return webServiceResponse;
	}

	public String capture_transaction_executeAdminPanel(HomePage home, String merchantKey, String mihpayid)
	{
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		testConfig.putRunTimeProperty("token", Helper.generateRandomAlphaNumericString(10));
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 6);

		try //store the requestid if present
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(webServiceResponse);

			//request id is given out when we call this webservice, storing it for future verification
			testConfig.putRunTimeProperty("request_id", actualResponse.get("request_id"));
		}
		catch(Exception e)
		{
			//request id is not present in case wrong input is given
		}

		return webServiceResponse;
	}

	private String [] expected_cancel_transaction_respKeys= {"status", "msg", "txn_update_id","bank_ref_num", "error_code"};

	private String [] expected_cancelrefund_transaction_respKeys= {"status", "msg", "mihpayid", "txn_update_id","bank_ref_num", "error_code"};

	public void cancel_transaction_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg, String bank_ref_num, String error_code)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		if(actualResponse.get("msg").equals("Cancelled failed"))
		{
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("error_code", error_code);			
			testConfig.putRunTimeProperty("msg", msg);
			VerifyWebServiceResponse(expected_cancel_refund_failed_transaction_respKeys, expectedTransactionResponse, actualResponse);
		}
		else if(actualResponse.get("msg").equals("Cancel failed")) {

			//will be used when we check the status of this action
			//for cancel_transaction command 
			testConfig.putRunTimeProperty("bank_ref_num", bank_ref_num);
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("error_code", error_code);			
			testConfig.putRunTimeProperty("msg", msg);
			testConfig.putRunTimeProperty("request_id", actualResponse.get("request_id"));
			VerifyWebServiceResponse(expected_cancel_transaction_respKeys, expectedTransactionResponse, actualResponse);
		}

		else if(actualResponse.get("msg").equals("Cancelled Request Queued")) {

			//will be used when we check the status of this action
			//for cancel_transaction command 
			testConfig.putRunTimeProperty("bank_ref_num", bank_ref_num);
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("error_code", error_code);			
			testConfig.putRunTimeProperty("msg", msg);
			testConfig.putRunTimeProperty("txn_update_id", actualResponse.get("txn_update_id"));
			VerifyWebServiceResponse(expected_cancel_transaction_respKeys, expectedTransactionResponse, actualResponse);
		}

		else {		
			testConfig.putRunTimeProperty("bank_ref_num", bank_ref_num);
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("error_code", error_code);			
			testConfig.putRunTimeProperty("msg", msg);
			testConfig.putRunTimeProperty("txn_update_id", actualResponse.get("txn_update_id"));
			VerifyWebServiceResponse(expected_cancelrefund_transaction_respKeys, expectedTransactionResponse, actualResponse);
		}

	}



	public void cancel_transaction_error_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg)
	{
		refund_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, status, msg);
	}

	public void cod_cancel_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg, String bank_ref_num, String error_code)
	{
		refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, status, msg, bank_ref_num, error_code);
	}

	public void cod_cancel_error_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg)
	{
		refund_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, status, msg);
	}

	public void cod_verify_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg, String bank_ref_num, String error_code)
	{
		refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, status, msg, bank_ref_num, error_code);
	}

	public void cod_verify_error_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg)
	{
		refund_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, status, msg);
	}

	public void cod_settled_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg, String bank_ref_num, String error_code)
	{
		refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, status, msg, bank_ref_num, error_code);
	}

	public void cod_settled_error_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg)
	{
		refund_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, status, msg);
	}

	public void cancelrefund_transaction_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg, String amount, String error_code)
	{
		refund_transaction_verify(actualWebServiceResponse, expectedTransactionResponse, status, msg, amount, error_code);
	}

	public void cancelrefund_transaction_error_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg)
	{
		refund_transaction_error_verify(actualWebServiceResponse, expectedTransactionResponse, status, msg);
	}

	private String [] expected_capture_transaction_respKeys= {"status", "msg", "request_id", "bank_ref_num", "error_code"};

	public void capture_transaction_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg, String bank_ref_num, String error_code)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		//will be used when we check the status of this action
		if(status.equals("1"))
		{
			testConfig.putRunTimeProperty("bank_ref_num", bank_ref_num);
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("error_code", error_code);			
			testConfig.putRunTimeProperty("msg", msg);
			VerifyWebServiceResponse(expected_capture_transaction_respKeys, expectedTransactionResponse, actualResponse);
		}		
		else 
		{
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("error_code", error_code);			
			testConfig.putRunTimeProperty("msg", msg);
			VerifyWebServiceResponse(expected_cancel_refund_failed_transaction_respKeys, expectedTransactionResponse, actualResponse);
		}
	}		

	private String [] expected_cancel_refund_transaction_respKeys= {"status", "msg", "request_id", "mihpayid", "bank_ref_num", "error_code"};
	private String [] expected_cancel_refund_failed_transaction_respKeys= {"status", "msg", "error_code"};
	private String [] expected_cancel_refund_transaction_error_respKeys= {"status", "msg"};

	public void refund_transaction_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg, String amount, String error_code)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		//will be used when we check the status of this action
		if(actualResponse.get("msg").contains("Refund Request Queued") || actualResponse.get("msg").contains("Capture is done today, please check for  refund status tomorrow"))
		{
			testConfig.putRunTimeProperty("request_id", actualResponse.get("request_id"));
			testConfig.putRunTimeProperty("bank_ref_num", "");
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("error_code", error_code);			
			testConfig.putRunTimeProperty("msg", msg);
			VerifyWebServiceResponse(expected_cancel_refund_transaction_respKeys, expectedTransactionResponse, actualResponse);
		}
		else if (actualResponse.get("error_code").equals("225")){
			testConfig.putRunTimeProperty("request_id", actualResponse.get("request_id"));
			testConfig.putRunTimeProperty("bank_ref_num", "");
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("error_code", actualResponse.get("error_code"));			
			testConfig.putRunTimeProperty("msg", msg);
			VerifyWebServiceResponse(expected_cancel_refund_transaction_respKeys, expectedTransactionResponse, actualResponse);
		}

		else
		{
			testConfig.putRunTimeProperty("status", status);
			testConfig.putRunTimeProperty("error_code", error_code);			
			testConfig.putRunTimeProperty("msg", msg);
			VerifyWebServiceResponse(expected_cancel_refund_failed_transaction_respKeys, expectedTransactionResponse, actualResponse);
		}


	}

	public void refund_transaction_error_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		//will be used when we check the status of this action
		testConfig.putRunTimeProperty("status", status);
		testConfig.putRunTimeProperty("msg", msg);
		VerifyWebServiceResponse(expected_cancel_refund_transaction_error_respKeys, expectedTransactionResponse, actualResponse);

	}


		//////GetReconData

	public String getReconData_executeAdminPanel(HomePage home, String merchantKey, String mihpayid)
	{
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 17);

		return webServiceResponse;
	}


	/**
	 * Verifies the result of get_recondata webservice
	 * @param actualWebServiceResponse
	 * @param expectedTransactionResponse
	 * @param Map
	 * @param status
	 */
	public void getReconData_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, Map<String, String>map, String status)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);


		if(!actualResponse.isEmpty()) {
			String mihpayid = expectedTransactionResponse.get("mihpayid");
			//Verify recon data
			Hashtable<String, String> webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(actualResponse.get(mihpayid));

			//Verify the data from db
			Helper.compareEquals(testConfig, "Merchant_id",map.get("merchant_id"), webResponseTran_details.get("merchant_id"));
			Helper.compareEquals(testConfig, "Amount",map.get("amount"), webResponseTran_details.get("amount"));
			Helper.compareEquals(testConfig, "Bank UTR",map.get("bank_utr"), webResponseTran_details.get("bank_utr"));
			Helper.compareEquals(testConfig, "Merchant Service Fee",map.get("mer_service_fee"), webResponseTran_details.get("mer_service_fee"));
			Helper.compareEquals(testConfig, "Merchant Service Tax",map.get("mer_service_tax"), webResponseTran_details.get("mer_service_tax"));
			Helper.compareEquals(testConfig, "Merchant Net Amount",map.get("mer_net_amount"), webResponseTran_details.get("mer_net_amount"));
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, webResponseTran_details.get("status"));

			testConfig.putRunTimeProperty("txn_update_id", map.get("id"));
			map = DataBase.executeSelectQuery(testConfig, 5, 1);
			if (status.equals("Pending")) {
				Helper.compareEquals(testConfig, "Recon ID","", webResponseTran_details.get("recon_id"));
			}
			else {
				Helper.compareEquals(testConfig, "Recon ID",map.get("reconciliation_id"), webResponseTran_details.get("recon_id"));
			}


		}
	}

	public void getReconData_error_verify(String actualWebServiceResponse, String invalidData, Hashtable<String, String> expectedTransactionResponse, String status)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		if((!actualResponse.isEmpty()) && actualResponse.containsKey(expectedTransactionResponse.get("mihpayid"))) {
			//Verify invalid data response
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, actualResponse.get(expectedTransactionResponse.get("mihpayid")));

		}		
		else if((!actualResponse.isEmpty() && actualResponse.containsKey(invalidData))) { 
			//Verify invalid data response
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, actualResponse.get(invalidData));
		}

		else if(!actualResponse.isEmpty()) {	
			//Verify invalid data response
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, actualResponse.get(""));
		}
	}

	public void multiple_getReconData_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, Map<String, String> map1, Map<String, String> map2, String status)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		if(!actualResponse.isEmpty()) {
			String mihpayid = expectedTransactionResponse.get("mihpayid");
			//Verify recon data
			Hashtable<String, String> webResponseTran_details = Helper.convertIstPhpGetUserCardResponseToJavaList(actualResponse.get(mihpayid));

			//Verify the data from db
			Helper.compareEquals(testConfig, "Merchant_id",map1.get("merchant_id"), webResponseTran_details.get("merchant_id"));
			Helper.compareEquals(testConfig, "Amount",map1.get("amount"), webResponseTran_details.get("amount"));
			Helper.compareEquals(testConfig, "Bank UTR",map1.get("bank_utr"), webResponseTran_details.get("bank_utr"));
			Helper.compareEquals(testConfig, "Merchant Service Fee",map1.get("mer_service_fee"), webResponseTran_details.get("mer_service_fee"));
			Helper.compareEquals(testConfig, "Merchant Service Tax",map1.get("mer_service_tax"), webResponseTran_details.get("mer_service_tax"));
			Helper.compareEquals(testConfig, "Merchant Net Amount",map1.get("mer_net_amount"), webResponseTran_details.get("mer_net_amount"));
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, webResponseTran_details.get("status"));

			testConfig.putRunTimeProperty("txn_update_id", map1.get("id"));
			map1 = DataBase.executeSelectQuery(testConfig, 5, 1);
			if (status.equals("Pending")) {
				Helper.compareEquals(testConfig, "Recon ID","", webResponseTran_details.get("recon_id"));
			}
			else {
				Helper.compareEquals(testConfig, "Recon ID",map1.get("reconciliation_id"), webResponseTran_details.get("recon_id"));
			}

			actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

			webResponseTran_details = Helper.convert2ndPhpArrayGetUserCardToJavaList(actualResponse.get(mihpayid));
			//Verify the data from db
			Helper.compareEquals(testConfig, "Merchant_id",map2.get("merchant_id"), webResponseTran_details.get("merchant_id"));
			Helper.compareEquals(testConfig, "Amount",map2.get("amount"), webResponseTran_details.get("amount"));
			Helper.compareEquals(testConfig, "Bank UTR",map2.get("bank_utr"), webResponseTran_details.get("bank_utr"));
			Helper.compareEquals(testConfig, "Merchant Service Fee",map2.get("mer_service_fee"), webResponseTran_details.get("mer_service_fee"));
			Helper.compareEquals(testConfig, "Merchant Service Tax",map2.get("mer_service_tax"), webResponseTran_details.get("mer_service_tax"));
			Helper.compareEquals(testConfig, "Merchant Net Amount",map2.get("mer_net_amount"), webResponseTran_details.get("mer_net_amount"));
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, webResponseTran_details.get("status"));

			testConfig.putRunTimeProperty("txn_update_id", map2.get("id"));
			map2 = DataBase.executeSelectQuery(testConfig, 5, 1);
			if (status.equals("Pending")) {
				Helper.compareEquals(testConfig, "Recon ID","", webResponseTran_details.get("recon_id"));
			}
			else {
				Helper.compareEquals(testConfig, "Recon ID",map2.get("reconciliation_id"), webResponseTran_details.get("recon_id"));
			}

		}
	}

	public void getmultipleReconData_error_verify(String actualWebServiceResponse, String invalidData1, String invalidData2, Hashtable<String, String> expectedTransactionResponse1, Hashtable<String, String> expectedTransactionResponse2, String status)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		if((!actualResponse.isEmpty()) && actualResponse.containsKey(expectedTransactionResponse1.get("mihpayid")) && actualResponse.containsKey(expectedTransactionResponse2.get("mihpayid"))) {
			//Verify invalid data response
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, actualResponse.get(expectedTransactionResponse1.get("mihpayid")));
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, actualResponse.get(expectedTransactionResponse2.get("mihpayid")));

		}		
		else if((!actualResponse.isEmpty() && actualResponse.containsKey(invalidData1) && actualResponse.containsKey(invalidData2))) { 
			//Verify invalid data response
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, actualResponse.get(invalidData1));
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, actualResponse.get(invalidData2));
		}

		else if(!actualResponse.isEmpty()) {	
			//Verify invalid data response
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, actualResponse.get(""));
		}
	}

	//////UpdateSettlementData

	public String updateSettlement_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String utr)
	{
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		testConfig.putRunTimeProperty("utr", utr);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 18);

		return webServiceResponse;
	}


	/**
	 * Verifies the result of get_recondata webservice
	 * @param actualWebServiceResponse
	 * @param expectedTransactionResponse
	 * @param Map
	 * @param status
	 */
	public void updateSettlement_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, Map<String, String>map, String utr, String status)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		if(!actualResponse.isEmpty()) {

			Helper.compareEquals(testConfig, "Transaction Recon Status",status, actualResponse.get(expectedTransactionResponse.get("mihpayid")));

		}
	}

	public void updateSettlement_error_verify(String actualWebServiceResponse, String invalidData, Hashtable<String, String> expectedTransactionResponse, String status)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		if((!actualResponse.isEmpty()) && actualResponse.containsKey(expectedTransactionResponse.get("mihpayid"))) {
			//Verify invalid data response
			Helper.compareEquals(testConfig, "Transaction Update Settlement Status",status, actualResponse.get(expectedTransactionResponse.get("mihpayid")));

		}		
		else if((!actualResponse.isEmpty() && actualResponse.containsKey(invalidData))) { 
			//Verify invalid data response
			Helper.compareEquals(testConfig, "Transaction Update Settlement Status",status, actualResponse.get(invalidData));
		}

		else if(!actualResponse.isEmpty()) {	
			//Verify invalid data response
			String[] errorResponse = actualWebServiceResponse.split("=>");
			errorResponse = errorResponse[1].split("\n");
			Helper.compareEquals(testConfig, "Transaction Update Settlement Status",status, errorResponse[0]);
		}
	}

	public String multiple_updateSettlement_executeAdminPanel(HomePage home, String merchantKey, String mihpayid1, String mihpayid2, String utr)
	{
		testConfig.putRunTimeProperty("mihpayid1", mihpayid1);
		testConfig.putRunTimeProperty("mihpayid2", mihpayid2);
		testConfig.putRunTimeProperty("mer_utr", utr);		
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 19);

		return webServiceResponse;
	}
	public void multiple_updateSettlement_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, Map<String, String> map1, Map<String, String> map2, String status)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		if(!actualResponse.isEmpty()) {
			String mihpayid = expectedTransactionResponse.get("mihpayid");
			//Verify recon data
			Helper.compareEquals(testConfig, "Transaction Recon Status",status, actualResponse.get(expectedTransactionResponse.get("mihpayid")));

		}
	}

	public void multipleupdateSettlement_error_verify(String actualWebServiceResponse, String invalidData1, String invalidData2, Hashtable<String, String> expectedTransactionResponse1, Hashtable<String, String> expectedTransactionResponse2, String status)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		if((!actualResponse.isEmpty()) && actualResponse.containsKey(expectedTransactionResponse1.get("mihpayid")) && actualResponse.containsKey(expectedTransactionResponse2.get("mihpayid"))) {
			//Verify invalid data response
			Helper.compareEquals(testConfig, "Update Settlement Status",status, actualResponse.get(expectedTransactionResponse1.get("mihpayid")));
			Helper.compareEquals(testConfig, "Update Settlement Status",status, actualResponse.get(expectedTransactionResponse2.get("mihpayid")));

		}		
		else if((!actualResponse.isEmpty() && actualResponse.containsKey(invalidData1) && actualResponse.containsKey(invalidData2))) { 
			//Verify invalid data response
			Helper.compareEquals(testConfig, "Update Settlement Status",status, actualResponse.get(invalidData1));
			Helper.compareEquals(testConfig, "Update Settlement Status",status, actualResponse.get(invalidData2));
		}

		else if(!actualResponse.isEmpty()) {	
			//Verify invalid data response
			String[] errorResponse = actualWebServiceResponse.split("=>");
			errorResponse = errorResponse[1].split("\n");
			Helper.compareEquals(testConfig, "Update Settlement Status",status, errorResponse[0]);
		}
	}

	//////VERIFY_GET PAYMENT

	public String get_payment_status_executeAdminPanel(HomePage home, String merchantKey, String transactionId)
	{
		testConfig.putRunTimeProperty("transactionId", transactionId);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 8);

		try //store the requestid if present
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(webServiceResponse);
			Hashtable<String, String> webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(actualResponse.get("transaction_details"));
			webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(webResponseTran_details.get(testConfig.getRunTimeProperty("txnid")));

			//request id is given out when we call this webservice, storing it for future verification
			testConfig.putRunTimeProperty("request_id", webResponseTran_details.get("request_id"));
		}
		catch(Exception e)
		{
			//request id is not present in case wrong input is given
		}

		return webServiceResponse;
	}

	public String verify_payment_executeAdminPanel(HomePage home, String merchantKey, String transactionId)
	{
		testConfig.putRunTimeProperty("transactionId", transactionId);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 4);

		try //store the requestid if present
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(webServiceResponse);
			Hashtable<String, String> webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(actualResponse.get("transaction_details"));
			webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(webResponseTran_details.get(testConfig.getRunTimeProperty("txnid")));

			//request id is given out when we call this webservice, storing it for future verification
			testConfig.putRunTimeProperty("request_id", webResponseTran_details.get("request_id"));
		}
		catch(Exception e)
		{
			//request id is not present in case wrong input is given
		}

		return webServiceResponse;
	}
	private String [] expected_verifyget_payment_respKeys= {"mihpayid", "request_id","bank_ref_num","amt","txnid","productinfo","firstname",
			"bankcode","udf1","udf3","udf4","udf5","field9","disc","mode","PG_TYPE","card_no","name_on_card",
			"udf2","addedon","status","unmappedstatus","Merchant_UTR","Settled_At" ,"net_amount_debit", "additional_charges", "error_code"};

	private String [] expected_cod_verifyget_payment_respKeys= {"mihpayid", "request_id", "bank_ref_num", "amt", "txnid", "productinfo", "firstname", "bankcode",  
			"udf1", "udf3", "udf4", "udf5", "field9", "net_amount_debit", "disc", "mode", "PG_TYPE", "udf2", "addedon", "shipping_firstname", 
			"shipping_lastname", "shipping_address1", "shipping_address2", "shipping_city","shipping_phone", "shipping_state", "shipping_zipcode", "shipping_country", "status", 
			"unmappedstatus", "Merchant_UTR", "Settled_At", "additional_charges", "card_no", "error_code"};

	public void get_payment_status_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String parentStatus, String parentMsg, String Merchant_UTR, String Settled_At)
	{
		verify_payment_verify(actualWebServiceResponse, expectedTransactionResponse, parentStatus,parentMsg,Merchant_UTR, Settled_At);
	}

	public void get_payment_status_error_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String parentStatus, String parentMsg, String net_amount)
	{
		verify_payment_error_verify(actualWebServiceResponse, expectedTransactionResponse, parentStatus,parentMsg,net_amount);
	}

	public void verify_payment_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String parentStatus, String parentMsg, String Merchant_UTR, String Settled_At )
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
		testConfig.putRunTimeProperty("status", parentStatus);
		testConfig.putRunTimeProperty("msg", parentMsg);
		
        //Get error code from transaction table
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 119, 1);
		String errorCode = map.get("error_code");
		
		if(parentStatus.equals("0")) {

			//Verify parent status and msgexpected_error_header_Keys
			VerifyWebServiceResponse(expected_error_header_Keys, expectedTransactionResponse, actualResponse);
		}

		else if (expectedTransactionResponse.get("mode").equals("COD")) {

			//Verify parent status and msg
			VerifyWebServiceResponse(expected_header_Keys, expectedTransactionResponse, actualResponse);

			//Verify transaction details
			Hashtable<String, String> webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(actualResponse.get("transaction_details"));
			webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(webResponseTran_details.get(expectedTransactionResponse.get("txnid")));

			//populate the expected values in run time variables for VerifyWebServiceResponse function call
			testConfig.putRunTimeProperty("request_id", webResponseTran_details.get("request_id"));
			testConfig.putRunTimeProperty("amt", webResponseTran_details.get("amt"));
			testConfig.putRunTimeProperty("status", "pending");
			testConfig.putRunTimeProperty("disc", expectedTransactionResponse.get("discount"));
			testConfig.putRunTimeProperty("Merchant_UTR", Merchant_UTR);
			testConfig.putRunTimeProperty("Settled_At", Settled_At);
			testConfig.putRunTimeProperty("error_code", errorCode);
			//testConfig.putRunTimeProperty("card_no", "");
			if(expectedTransactionResponse.get("additionalCharges")!=null)
				testConfig.putRunTimeProperty("additional_charges", expectedTransactionResponse.get("additionalCharges"));
			else 
				testConfig.putRunTimeProperty("additional_charges", "0.00");
						
			VerifyWebServiceResponse(expected_cod_verifyget_payment_respKeys, expectedTransactionResponse, webResponseTran_details);
		}
		else 
		{
			//Verify parent status and msg 
			VerifyWebServiceResponse(expected_header_Keys, expectedTransactionResponse, actualResponse);

			//Verify transaction details
			Hashtable<String, String> webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(actualResponse.get("transaction_details"));
			webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(webResponseTran_details.get(expectedTransactionResponse.get("txnid")));

			//populate the expected values in run time variables for VerifyWebServiceResponse function call
			testConfig.putRunTimeProperty("request_id", webResponseTran_details.get("request_id"));
			testConfig.putRunTimeProperty("bank_ref_num", expectedTransactionResponse.get("bank_ref_num"));
			testConfig.putRunTimeProperty("amt", webResponseTran_details.get("amt"));
			testConfig.putRunTimeProperty("disc", expectedTransactionResponse.get("discount"));
			testConfig.putRunTimeProperty("mode", expectedTransactionResponse.get("mode"));
			testConfig.putRunTimeProperty("PG_TYPE", expectedTransactionResponse.get("PG_TYPE"));
			testConfig.putRunTimeProperty("status", expectedTransactionResponse.get("status"));
			testConfig.putRunTimeProperty("unmappedstatus", expectedTransactionResponse.get("unmappedstatus"));
			testConfig.putRunTimeProperty("card_no", expectedTransactionResponse.get("cardnum"));
			testConfig.putRunTimeProperty("name_on_card", expectedTransactionResponse.get("name_on_card"));
			testConfig.putRunTimeProperty("udf2", expectedTransactionResponse.get("udf2"));
			testConfig.putRunTimeProperty("addedon", expectedTransactionResponse.get("addedon"));
			testConfig.putRunTimeProperty("Merchant_UTR", Merchant_UTR);
			testConfig.putRunTimeProperty("Settled_At", Settled_At);
			testConfig.putRunTimeProperty("net_amount_debit", expectedTransactionResponse.get("net_amount_debit"));
			testConfig.putRunTimeProperty("error_code", errorCode);
			if(expectedTransactionResponse.get("additionalCharges")!=null)
				testConfig.putRunTimeProperty("additional_charges", expectedTransactionResponse.get("additionalCharges"));
			else 
				testConfig.putRunTimeProperty("additional_charges", "0.00");	

			VerifyWebServiceResponse(expected_verifyget_payment_respKeys, expectedTransactionResponse, webResponseTran_details);
		}
	}

	public void verify_payment_error_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String parentStatus, String parentMsg, String net_amount)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
		testConfig.putRunTimeProperty("status", parentStatus);
		testConfig.putRunTimeProperty("msg", parentMsg);

		//Verify parent status and msg
		VerifyWebServiceResponse(expected_header_Keys, expectedTransactionResponse, actualResponse);
	}

	//////UPDATE REQUEST
	public String update_request_executeAdminPanel(HomePage home, String merchantKey, String mihpayid, String request_id, String bank_ref_num, String amount, String action, String new_status)
	{
		testConfig.putRunTimeProperty("mihpayid", mihpayid);
		testConfig.putRunTimeProperty("request_id", request_id);
		testConfig.putRunTimeProperty("bank_ref_num", bank_ref_num);
		testConfig.putRunTimeProperty("amount", amount);
		testConfig.putRunTimeProperty("action", action);
		testConfig.putRunTimeProperty("new_status", new_status);

		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 9);

		return webServiceResponse;
	}

	private String [] expected_update_requests_respKeys= {"status", "msg"};

	public void update_request_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String status, String msg)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		testConfig.putRunTimeProperty("status", status);
		testConfig.putRunTimeProperty("msg", msg);

		VerifyWebServiceResponse(expected_update_requests_respKeys, actualResponse, actualResponse);
	}

	//////CHECK ACTION STATUS
	public String check_action_status_executeAdminPanel(HomePage home, String merchantKey, String request_id)
	{
		testConfig.putRunTimeProperty("request_id", request_id);

		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 5);

		return webServiceResponse;	
	}


	//////CHECK ACTION STATUS FOR CANCEL
	public String check_action_status_cancel_executeAdminPanel(HomePage home, String merchantKey, String txn_update_id)
	{
		testConfig.putRunTimeProperty("txn_update_id", txn_update_id);

		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 15);

		return webServiceResponse;	
	}

	private String [] expected_check_action_status_respKeys= {"mihpayid", "bank_ref_num", "request_id", "amt", 
			"mode", "action", "token", "status", "settlement_id", "amount_settled",  "UTR_no", "value_date", "bank_arn"};

	public void check_action_status_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String parentStatus, String parentMsg, String action, String bank_ref_num, String status, String amount, String amount_settled)  
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
		testConfig.putRunTimeProperty("status", parentStatus);
		testConfig.putRunTimeProperty("msg", parentMsg);
        
	    //Verify parent status and msg
		VerifyWebServiceResponse(expected_header_Keys, expectedTransactionResponse, actualResponse);

		Hashtable<String, String> webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(actualResponse.get("transaction_details"));
		webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(webResponseTran_details.get(testConfig.getRunTimeProperty("request_id")));
		webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(webResponseTran_details.get(testConfig.getRunTimeProperty("request_id")));

		//Verify action details	if status gets captured	
		testConfig.putRunTimeProperty("mihpayid", expectedTransactionResponse.get("mihpayid"));
		testConfig.putRunTimeProperty("bank_ref_num", webResponseTran_details.get("bank_ref_num"));
		testConfig.putRunTimeProperty("amt", webResponseTran_details.get("amt"));
		testConfig.putRunTimeProperty("mode", expectedTransactionResponse.get("mode"));			
		testConfig.putRunTimeProperty("action", action);
		testConfig.putRunTimeProperty("status", status);
		testConfig.putRunTimeProperty("amount_settled", amount_settled);
		
		VerifyWebServiceResponse(expected_check_action_status_respKeys, expectedTransactionResponse, webResponseTran_details);
	}

	public void check_action_status_cancel_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String parentStatus, String parentMsg, String action, String bank_ref_num, String status, String amount, String amount_settled)  
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
		testConfig.putRunTimeProperty("status", parentStatus);
		testConfig.putRunTimeProperty("msg", parentMsg);

		//Verify parent status and msg
		VerifyWebServiceResponse(expected_header_Keys, expectedTransactionResponse, actualResponse);

		Hashtable<String, String> webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(actualResponse.get("transaction_details"));
		webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(webResponseTran_details.get(testConfig.getRunTimeProperty("txn_update_id")));
		webResponseTran_details = Helper.convertPhpArrayResponseToJavaList(webResponseTran_details.get(testConfig.getRunTimeProperty("txn_update_id")));

		//Verify action details	if status gets captured	
		testConfig.putRunTimeProperty("mihpayid", expectedTransactionResponse.get("mihpayid"));
		testConfig.putRunTimeProperty("bank_ref_num", webResponseTran_details.get("bank_ref_num"));
		testConfig.putRunTimeProperty("request_id", webResponseTran_details.get("request_id"));
		testConfig.putRunTimeProperty("amt", webResponseTran_details.get("amt"));
		testConfig.putRunTimeProperty("mode", expectedTransactionResponse.get("mode"));			
		testConfig.putRunTimeProperty("action", action);
		testConfig.putRunTimeProperty("status", status);
		testConfig.putRunTimeProperty("amount_settled", amount_settled);

		VerifyWebServiceResponse(expected_check_action_status_respKeys, expectedTransactionResponse, webResponseTran_details);
	}


	public void check_action_status_error_verify(String actualWebServiceResponse, Hashtable<String, String> expectedTransactionResponse, String parentStatus, String parentMsg)
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);
		testConfig.putRunTimeProperty("status", parentStatus);
		testConfig.putRunTimeProperty("msg", parentMsg);

		//Verify parent status and msg
		VerifyWebServiceResponse(expected_error_header_Keys, expectedTransactionResponse, actualResponse);
	}

	/**
	 * Execute create_invoice webservice from admin panel, and stores the returned request_id in runtimeproperty
	 * @param home
	 * @param merchantKey
	 * @param createInvoice
	 * @param rowNum
	 * @return
	 */
	public String create_invoice_executeAdminPanel(HomePage home, String merchantKey, TestDataReader createInvoice, int rowNum)
	{

		String amount = createInvoice.GetData(rowNum, "amount");
		String duplicateTxnId = testConfig.getRunTimeProperty("txnid");
		String txnid = Helper.generateRandomAlphaNumericString(5);
		String productinfo = createInvoice.GetData(rowNum, "productinfo");
		String firstname = createInvoice.GetData(rowNum, "firstname");
		String email = createInvoice.GetData(rowNum, "email");
		String phone = createInvoice.GetData(rowNum, "phone");
		String address1 = createInvoice.GetData(rowNum, "address1");
		String city = createInvoice.GetData(rowNum, "city");
		String state = createInvoice.GetData(rowNum, "state");
		String country = createInvoice.GetData(rowNum, "country");
		String zipcode = createInvoice.GetData(rowNum, "zipcode");
		String template_id = createInvoice.GetData(rowNum, "template_id");
		String validation_period = createInvoice.GetData(rowNum, "validation_period");
		String send_email_now = createInvoice.GetData(rowNum, "send_email_now");

		if (rowNum == 4)
		{
			testConfig.putRunTimeProperty("amount", " ");
		}
		else {
			testConfig.putRunTimeProperty("amount", amount);
		}
		if (rowNum == 5)
		{
			testConfig.putRunTimeProperty("txnid", " ");
		}
		else if (rowNum == 26 || rowNum ==27) {
			//used txnid
			testConfig.putRunTimeProperty("txnid", duplicateTxnId);
		}
		else {
			testConfig.putRunTimeProperty("txnid", txnid);
		}
		if (rowNum == 7)
		{
			testConfig.putRunTimeProperty("productinfo", " ");
		}
		else {
			testConfig.putRunTimeProperty("productinfo", productinfo);
		}
		if (rowNum == 9)
		{
			testConfig.putRunTimeProperty("firstname", " ");
		}
		else {
			testConfig.putRunTimeProperty("firstname", firstname);
		}
		if (rowNum == 11)
		{
			testConfig.putRunTimeProperty("email", " ");
		}
		else {
			testConfig.putRunTimeProperty("email", email);
		}	
		testConfig.putRunTimeProperty("phone", phone);
		testConfig.putRunTimeProperty("address1", address1);
		testConfig.putRunTimeProperty("city", city);
		testConfig.putRunTimeProperty("state", state);
		testConfig.putRunTimeProperty("country", country);
		testConfig.putRunTimeProperty("zipcode", zipcode);
		testConfig.putRunTimeProperty("template_id", template_id);
		testConfig.putRunTimeProperty("validation_period", validation_period);
		testConfig.putRunTimeProperty("send_email_now", send_email_now);

		String webServiceResponse = "";
		if(rowNum == 25) {
			webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 21);
		}
		else {
			webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 20);
		}

		String generatedURL = "";

		try //store the URL
		{
			Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(webServiceResponse);

			//URL is given out when we call this webservice, storing it for making payment
			generatedURL = actualResponse.get("URL");
			testConfig.putRunTimeProperty("invoiceurl", generatedURL);
		}
		catch(Exception e)
		{

		}

		return webServiceResponse;
	}

	public void create_invoice_verify(String actualWebServiceResponse, TestDataReader createInvoiceData, int RowNum)  
	{
		Hashtable<String, String> actualResponse = Helper.convertPhpArrayResponseToJavaList(actualWebServiceResponse);

		//Get data from invoice table
		Map<String, String> invoice = DataBase.executeSelectQuery(testConfig, 93, 1);	

		//Verify webservice output from DB 
		Helper.compareEquals(testConfig, "Transaction Id", testConfig.getRunTimeProperty("txnid"), actualResponse.get("Transaction Id"));
		Helper.compareEquals(testConfig, "Email Id", invoice.get("email"), actualResponse.get("Email Id"));
		Helper.compareEquals(testConfig, "Phone", invoice.get("phone"), actualResponse.get("Phone"));
		Helper.compareEquals(testConfig, "Invoice Status", createInvoiceData.GetData(RowNum, "result"), actualResponse.get("Status"));
		Helper.compareEquals(testConfig, "Email Id", createInvoiceData.GetData(RowNum, "email"), actualResponse.get("Email Id"));

		//Verify data input for create_invoice 
		Helper.compareEquals(testConfig, "Amount", createInvoiceData.GetData(RowNum,"amount"), invoice.get("amount"));
		Helper.compareEquals(testConfig, "Product Info", createInvoiceData.GetData(RowNum,"productinfo"), invoice.get("productinfo"));
		Helper.compareEquals(testConfig, "First Name", createInvoiceData.GetData(RowNum,"firstname"), invoice.get("firstname"));

		if (!(RowNum == 25)) {
			Helper.compareEquals(testConfig, "Address", createInvoiceData.GetData(RowNum,"address1"), invoice.get("address1"));
			Helper.compareEquals(testConfig, "City", createInvoiceData.GetData(RowNum,"city"), invoice.get("city"));
			Helper.compareEquals(testConfig, "State", createInvoiceData.GetData(RowNum,"state"), invoice.get("state"));
			Helper.compareEquals(testConfig, "Country", createInvoiceData.GetData(RowNum,"country"), invoice.get("country"));
			Helper.compareEquals(testConfig, "ZIP Code", createInvoiceData.GetData(RowNum,"zipcode"), invoice.get("zipcode"));
			Helper.compareEquals(testConfig, "Template Id", createInvoiceData.GetData(RowNum,"template_id"), invoice.get("template_id"));
			Helper.compareEquals(testConfig, "Validation Period", createInvoiceData.GetData(RowNum,"validation_period"), invoice.get("validation_period"));
		}
		if (createInvoiceData.GetData(RowNum,"send_email_now").equals("1")) {
			String send_email = createInvoiceData.GetData(RowNum,"send_email_now");
			send_email = "Y";
			Helper.compareEquals(testConfig, "Send Email", send_email, invoice.get("email_sent"));
		}
		else {
			Helper.compareEquals(testConfig, "Send Email", "N", invoice.get("email_sent"));
		}
	}

	public void create_invoice_error_verify(String actualWebServiceResponse, TestDataReader createInvoiceData, int RowNum)  
	{
		//Verify webservice output as specified in Excel 
		Helper.compareEquals(testConfig, "Invoice Status", createInvoiceData.GetData(RowNum, "result"), actualWebServiceResponse.trim());

	}

	/**
	 * Executes the WebService using 'WebServices' excel sheet, the required parameters must be set in run time property before calling this
	 * @param HomePage instance to be used to executing the webservice
	 * @param merchantKey merchantKey to be used for calling the webserive
	 * @param webServiceRow excel row to be used to fill the details
	 * @return Webservice response
	 */
	public String ExecuteWebServiceUI(HomePage home, String merchantKey, int webServiceRow)
	{
		home.navigateToAdminHome();
		TestwsPage wsPage = home.ClickTestWebService();
		TestwsResponsePage wsRespPage = wsPage.ExecuteWebService(merchantKey, webServiceRow);
		return wsRespPage.getResponse();
	}

	/**
	 * Verifies that the web service response is as per the parameters passed
	 * @param expected_webservice_respKeys expected keys in the web service response
	 * @param expectedTransactionResponse testresponse received after the test transaction was done, will be used for comparison
	 * @param actualWebServiceResponse the webservive response to be verified
	 */
	private void  VerifyWebServiceResponse(String[] expected_webservice_respKeys, Hashtable<String, String> expectedTransactionResponse, Hashtable<String, String> actualWebServiceResponse) 
	{
		//so that screenshot is not taken again and again, for each response key verification
		testConfig.enableScreenshot = false;
		//mode will be use to skip card number comparison in case of COD  
		String mode = "";
		Helper.compareEquals(testConfig, "Count of WebSerive Response Parameters", expected_webservice_respKeys.length, actualWebServiceResponse.size());

		for(String expectedKey:expected_webservice_respKeys)
		{
			String expectedValue = null;

			switch(expectedKey)
			{
			case "field1":
			case "field2": 
			case "field3": 
			case "field4": 
			case "field5": 
			case "field6":
			case "field7":
			case "field8":
			case "udf1": 
			case "udf2": 
			case "udf3":
			case "udf4": 
			case "udf5":
			case "amount":
			case "discount":
			case "mihpayid":
			case "mode": 
			case "PG_TYPE":
			case "firstname":
			case "productinfo":
			case "txnid": 	 
			case "unmappedstatus":
			case "name_on_card": 	 
			case "addedon":
			case "shipping_firstname":
			case "shipping_lastname":
			case "shipping_address1":
			case "shipping_address2":
			case "shipping_city":
			case "shipping_phone":
			case "shipping_state":
			case "shipping_zipcode":
			case "shipping_country":
				if(expectedTransactionResponse != null)
				    expectedValue = expectedTransactionResponse.get(expectedKey);
				else
					expectedValue = testConfig.getRunTimeProperty(expectedKey);
				//mode will be use to skip card number comparison in case of COD
				if (expectedKey.equals("mode"))
					mode = expectedValue;
				break;
			case "card_token":
				if(expectedTransactionResponse != null)
				    expectedValue = expectedTransactionResponse.get("cardToken");
				else
					expectedValue = testConfig.getRunTimeProperty(expectedKey);
				break;
				
			case "card_mode":
				if(expectedTransactionResponse != null)
				    expectedValue = expectedTransactionResponse.get("mode");
				else
				    expectedValue = testConfig.getRunTimeProperty(expectedKey);
				break;	
			
			case "card_type":
				if(expectedTransactionResponse != null)
				    expectedValue = expectedTransactionResponse.get("bankcode");
				else
					expectedValue = testConfig.getRunTimeProperty(expectedKey);
				break;	

			case "disc": 
				expectedValue = expectedTransactionResponse.get("discount");
				break;

			case "card_no":
				if(mode.equals("COD"))
					expectedValue = "{skip}";
				else if(expectedTransactionResponse != null)
				    expectedValue = expectedTransactionResponse.get("cardnum");
				else
					expectedValue = testConfig.getRunTimeProperty(expectedKey);
				break;

			case "request_id":
			case "txn_update_id":
			case "action":
			case "amount_paid":
			case "amount_settled":
			case "net_amount":	
			case "Merchant_UTR":
			case "Settled_At":
			case "amt":
			case "status":
			case "msg":
			case "error_code":
			case "bank_ref_num":
			case "cardToken":
			case "card_number":
			case "card_label":
			case "bankcode":
			case "card_name":	
			case "expiry_year":
			case "expiry_month":
			case "is_expired":
			case "card_brand":
			case "card_bin":
			case "cc_exp_yr":
			case "cc_exp_mon":
			case "cname_on_card":
			case "additional_charges":
			case "net_amount_debit":
				expectedValue = testConfig.getRunTimeProperty(expectedKey);
				break;
			case "ccard_number":
				expectedValue = actualWebServiceResponse.get("ccard_number");
				break;
				
				//are not be verified
			case "settlement_id":
			case "UTR_no":
			case "value_date":
			case "field9":				 
			case "token":
			case "transaction_details": //we will verify the detailed array later
			case "user_cards":
			case "23b1b658c3683da34040379f03042a3589a8d3c2":
			case "card_details":
			case "bank_arn":
				expectedValue = "{skip}"; 
				break;

			default:
				expectedValue = expectedKey;
				break;
			}

			if(expectedValue == null)
			{
				Helper.compareEquals(testConfig, "WebService Response Param '" + expectedKey + "'", expectedValue, actualWebServiceResponse.get(expectedKey));
				continue;
			}

			if(expectedValue.equalsIgnoreCase("{skip}")) 
			{
				testConfig.logWarning("Skipping the verification of WebService Response Param '" + expectedKey + "' as '" + actualWebServiceResponse.get(expectedKey) + "'");
				continue;
			}

			if(expectedValue.startsWith("{contains}"))
			{
				expectedValue = expectedValue.replace("{contains}", "");
				Helper.compareContains(testConfig, "WebService Response Param '" + expectedKey + "'", expectedValue, actualWebServiceResponse.get(expectedKey));
			}
			else
			{
				Helper.compareEquals(testConfig, "WebService Response Param '" + expectedKey + "'", expectedValue, actualWebServiceResponse.get(expectedKey));
			}
		}

		//restore the screenshot value
		testConfig.enableScreenshot = true;

		Helper.compareTrue(testConfig, "Web Service Response", testConfig.getTestResult());
	}

	public String getEmiAmountAccordingToInterest_executeAdminPanel(HomePage home, String merchantKey, String amount) 
	{	
		testConfig.putRunTimeProperty("amount", amount);
		String webServiceResponse = ExecuteWebServiceUI(home, merchantKey, 22);
		return webServiceResponse;
	}

	public void getEmiAmountAccordingToInterest_verify(String actualWebServiceResponse) 
	{
		//Get the Total EMI options active on this merchant
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 96, 1);
		int totalEMIibiboCodes = Integer.parseInt(map.get("total"));
		if (totalEMIibiboCodes <1) 
			{
			testConfig.logFail("EMI option not active on this  mmerchant");
			return;
			}
		
		for(int i =1 ; i<= totalEMIibiboCodes; i ++)
		{
			//Get the ibibo code and PG active for EMI
			Map<String,String> mpt = DataBase.executeSelectQuery(testConfig, 97, i);
			String term = mpt.get("title");
			
			//Get the emiBankInterest for that payment type
			Map<String,String> emiBankInterestM = DataBase.executeSelectQuery(testConfig, 98, 1);
			String expectedEmiBankInterest = (emiBankInterestM == null)? "" : emiBankInterestM.get("emiBankInterest");
			double emi  = calculateEMI(testConfig.getRunTimeProperty("amount"), expectedEmiBankInterest, term);
			double interest = calculateInterest(testConfig.getRunTimeProperty("amount"), emi, term);
		
			//Get the emiBankCharge for that payment type
			Map<String,String> emiBankChargeM = DataBase.executeSelectQuery(testConfig, 99, 1);
			String expectedBankRate = (emiBankChargeM == null)? "0" : emiBankChargeM.get("emiBankCharge");
			String expectedBankCharge = calculateBankCharge(testConfig.getRunTimeProperty("amount"), expectedBankRate);
			String expectedPerMonthAmount = calculatePerMonthAmount(testConfig.getRunTimeProperty("amount"), term);
				
			Hashtable<String, String> actualResponse = Helper.convertgetEmiAmountResponseToJavaList(actualWebServiceResponse, testConfig.getRunTimeProperty("pg_id"), testConfig.getRunTimeProperty("ibibo_code"));
			
			//Compare emiBankInterest
			Helper.compareEquals(testConfig, "emiBankInterest", expectedEmiBankInterest, actualResponse.get("emiBankInterest"));
			
			//Compare bankRate
			Helper.compareEquals(testConfig, "bank Rate", (expectedBankRate=="0")?"":expectedBankRate, actualResponse.get("bankRate"));
			
			//Compare bankCharge
			Helper.compareEquals(testConfig, "bank Charge", expectedBankCharge, actualResponse.get("bankCharge"));
			
			//Compare amount
			Helper.compareEquals(testConfig, "Per month amount", expectedPerMonthAmount, actualResponse.get("amount"));
			
			//Compare emi_value
			Helper.compareEquals(testConfig, "emi_value", (emi == 0)? null:String.valueOf(emi), actualResponse.get("emi_value"));
			
			//Compare emi_interest_paid
			Helper.compareEquals(testConfig, "emi_interest_paid", (interest==0)? null : String.valueOf(interest), actualResponse.get("emi_interest_paid"));
			
		}
	}
	
	private double calculateInterest(String amount, double emi, String term) 
	{
		if(emi == 0) return 0;
		double amountD = Double.parseDouble(amount);
		double termD = Double.parseDouble(term.split(" ")[0]); //eg. '6 months' , will return 6
		double totalInterest = (emi * termD) - amountD;
		double finalValue = Math.round( totalInterest * 100.0 ) / 100.0;
		return finalValue;
	}

	private String calculatePerMonthAmount(String amount, String term)
	{
		double amountD = Double.parseDouble(amount);
		double termD = Double.parseDouble(term.split(" ")[0]); //eg. '6 months' , will return 6
		
		double amountPerMonth = amountD/termD;
		DecimalFormat df = new DecimalFormat("###.##");
		return df.format(amountPerMonth);
	}

	private String calculateBankCharge(String amount, String emiBankCharge) 
	{
		double amountD = Double.parseDouble(amount);
		double rateD = (emiBankCharge=="") ? 0 : Double.parseDouble(emiBankCharge);
		double charges = (rateD == 0) ? 0 : (amountD*rateD)/100;
		
		DecimalFormat df = new DecimalFormat("###");
		return df.format(charges);
	}

	private double calculateEMI(String amount, String inter, String month)
	{
		if (inter == "0" || inter == "") return 0;
		
		double loanAmount = Double.parseDouble(amount);
        int rateOfInterest = Integer.parseInt(inter);
        int numberOfMonths = Integer.parseInt(month.split(" ")[0]); //eg. '6 months' , will return 6
 
          double temp = 1200;           //100*numberofmonths(12))
          double interestPerMonth = rateOfInterest/temp;
          
          double onePlusInterestPerMonth = 1 + interestPerMonth;
          
          double powerOfOnePlusInterestPerMonth = Math.pow(onePlusInterestPerMonth,numberOfMonths);
          
          double powerofOnePlusInterestPerMonthMinusOne = powerOfOnePlusInterestPerMonth-1;
          
          double divides = powerOfOnePlusInterestPerMonth/powerofOnePlusInterestPerMonthMinusOne;
 
          double principleMultiplyInterestPerMonth = loanAmount * interestPerMonth;
          
          double totalEmi =  principleMultiplyInterestPerMonth*divides;
          
          double finalValue = Math.round( totalEmi * 100.0 ) / 100.0;
 
          return finalValue;
	}
	
}
