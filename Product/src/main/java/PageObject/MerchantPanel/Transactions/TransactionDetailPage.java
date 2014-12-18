package PageObject.MerchantPanel.Transactions;

import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.MerchantPanel.Home.DashboardPage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Test.MerchantPanel.Payments.TestAutoRefund.AutoRefund;
import Test.MerchantPanel.Payments.TestAutoRefund.verifyReconResult;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class TransactionDetailPage {

	private Config testConfig;

	@FindBy(css="div[id='general_popup_content']")
	private WebElement info;

	@FindBy(css="div[class='transaction-heading']+div")
	private WebElement txnDetail;

	@FindBy(css="div[class='transaction-heading margin-transactionhead']+div")
	private WebElement usrDetail;

	@FindBy(css="div[class='transaction-heading margin-transactionhead']~div:nth-child(6)+div > div")
	private WebElement statusDetail1;

	@FindBy(css="div[class='transaction-heading margin-transactionhead']~div:nth-child(6)+div > div+div")
	private WebElement statusDetail2;

	@FindBy(css="div[class='transaction-heading']:nth-child(4)~div")
	private WebElement actionDetailTable;

	@FindBy(css="div[class='transaction-heading']:nth-child(4)~table > tbody >tr")
	private WebElement actionDetailHeader;

	@FindBy(css="div[class='transaction-heading']:nth-child(4)~table > tbody >tr+tr")
	private WebElement actionDetailRow1;

	@FindBy(css="div[class='transaction-heading']:nth-child(4)~table > tbody >tr+tr+tr")
	private WebElement actionDetailRow2;

	@FindBy(css="div[class='transaction-heading']:nth-child(4)~table > tbody >tr+tr+tr+tr")
	private WebElement actionDetailRow3;

	@FindBy(css="div[class='transaction-heading']~div[class='transaction-heading']~div")
	private WebElement rskDetail;

	@FindBy(css="div[class='transaction-heading margin-transactionhead']:nth-child(13)~div~div")
	private WebElement merchantParameterDetail1;

	@FindBy(css="div[class='transaction-heading margin-transactionhead']:nth-child(13)~div~div+div")
	private WebElement merchantParameterDetail2;

	@FindBy(xpath="//div[@id='general_popup_content']/div/div/div/div/div/div[2]/div[2]")
	private WebElement headerAmount;

	@FindBy(xpath="//div/div[2]/div[2]/div[2]/div[2]")
	private WebElement transfee;

	@FindBy(xpath="//div[2]/div[2]/div[4]")
	private WebElement discount;

	@FindBy(xpath="//div[@id='general_popup_content']/div/div/div/div/div[2]/div[2]/div[2]/div[6]")
	private WebElement transdetailAmount;

	//@FindBy(xpath="//div[@class='overflowhidden']/table/tbody/tr[2]/td[5]")
	@FindBy(xpath="//div[@id='general_popup_content']/div/div/div/div/div[2]/div[7]/div/div[2]")
	private WebElement amount;

	@FindBy(id = "cancel_button")
	private WebElement oK_button;

	private int rowCount=0;

	public Hashtable<String, String> transactionDetail;
	public Hashtable<String, String> userDetail;
	public Hashtable<String, String> statusDetails1;
	public Hashtable<String, String> statusDetails2;
	public Hashtable<String, String> actionsDoneDetail;
	public Hashtable<String, String> actionsDoneDetail1;
	public Hashtable<String, String> actionsDoneDetail2;

	public Hashtable<String, String> riskDetail;
	public Hashtable<String, String> merchantSpecificParametersDetail1;
	public Hashtable<String, String> merchantSpecificParametersDetail2;

	public boolean overrideExpectedTransactionData = false;

	private String [] expectedDetailKeys = {"Amount","Product Information", "Discount","Merchant Name", 
			"Payment Type",  "Name on Card", "Bank Name", "Transaction ID", "PayU ID","Status","Payment Gateway",
			"Bank Reference No","Card Number","Date of Transaction","Transaction Fee", "Amount to be Captured",
			"First Name", "Last Name", "City", "Billing Address","Error Message","Delivery Address","Zipcode", "Phone Number",
			"Shipping Address", "State", "Email ID", "Offer String", "Offer Key", "Offer Type","Offer Failure Reason",
			"BankRefNo","RequestID", "Risk action taken","UDF1","UDF2","UDF3","UDF4","UDF5"};

	public TransactionDetailPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.wait(this.testConfig, 5);

		transactionDetail = DashboardHelper.convertTransactionDetailToList(txnDetail.getText());
		userDetail = DashboardHelper.convertTransactionDetailToList(usrDetail.getText());
		statusDetails1 = DashboardHelper.convertTransactionDetailToList(statusDetail1.getText());
		statusDetails2 = DashboardHelper.convertTransactionDetailToList(statusDetail2.getText());
		Boolean pass = actionDetailTable.getText().equalsIgnoreCase("No Action performed.");
		if (!pass)
		{
			actionsDoneDetail = DashboardHelper.convertActionsDetailToList(actionDetailHeader.getText(),actionDetailRow1.getText());
		}
		riskDetail = DashboardHelper.convertRiskDetail(rskDetail.getText());
		merchantSpecificParametersDetail1 = DashboardHelper.convertTransactionDetailToList(merchantParameterDetail1.getText());
		merchantSpecificParametersDetail2 = DashboardHelper.convertTransactionDetailToList(merchantParameterDetail2.getText());

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
	public void VerifyTransactionResponse(TestDataReader transactionData, int transactionRowNum, TestDataReader paymentTypeData, int paymentTypeRowNum, TestDataReader cardDetailsData, int cardDetailsRowNum)
	{	
		//so that screenshot is not taken again and again, for each response key verification
		testConfig.enableScreenshot = false;

		for(String expectedKey:expectedDetailKeys)
		{
			String expectedValue = null;

			switch(expectedKey)
			{
			case "Amount":
				if(testConfig.getRunTimeProperty("Amount")!=null) {
					expectedValue = testConfig.getRunTimeProperty("Amount");
				}
				else
				expectedValue = transactionData.GetData(transactionRowNum, "amount");
				break;
			case "Product Information":
				expectedValue = transactionData.GetData(transactionRowNum, "productinfo");
				break;
			case "Discount":
				expectedValue = transactionData.GetData(transactionRowNum, "discount");
				break;
			case "Merchant Name":
				expectedValue = transactionData.GetData(transactionRowNum, "Comments");
				break;
			case "Payment Type":
			switch(paymentTypeData.GetData(paymentTypeRowNum, "mode"))
				{
				case "creditcard": expectedValue = "Credit Card";
				break;

				case "debitcard": expectedValue = "Debit Card";
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
			case "Name on Card":
				if (cardDetailsRowNum != -1)
					expectedValue = cardDetailsData.GetData(cardDetailsRowNum,
							"Name");
				else
					expectedValue = "NA";

				break;	
			case "Bank Name":
				expectedValue = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
				break;
			case "Transaction ID":
				expectedValue = transactionDetail.get("Transaction ID");
				break;
			case "PayU ID":
				expectedValue = transactionDetail.get("PayU ID");
				break;
			case "Status":
				expectedValue = transactionDetail.get("Status");
				break;
			case "Payment Gateway":
				expectedValue = transactionDetail.get("Payment Gateway");
				break;
			case "Bank Reference No":
				expectedValue = statusDetails1.get("Bank Reference No");
				break;
			case "Card Number":
				expectedValue = transactionDetail.get("Card Number");
				break;
			case "Date of Transaction":
				expectedValue = transactionDetail.get("Date of Transaction");
				break;
			case "Transaction Fee":
				expectedValue = transactionData.GetData(transactionRowNum, "amount");
				break;
			case "Amount to be Captured":
				if(testConfig.getRunTimeProperty("Amount to be Captured")!=null) {
					expectedValue = testConfig.getRunTimeProperty("Amount to be Captured");
				}
				else
				expectedValue = transactionData.GetData(transactionRowNum, "amount");
				break;
			case "First Name":
				expectedValue = transactionData.GetData(transactionRowNum, "firstname");
				break;
			case "Last Name":
				expectedValue = transactionData.GetData(transactionRowNum, "lastname");
				break;
			case "City":
				expectedValue = transactionData.GetData(transactionRowNum, "city");
				break;
			case "Billing Address":
			case "Delivery Address":
				expectedValue = transactionData.GetData(transactionRowNum, "address1");
				break;
			case "Zipcode":
				if (cardDetailsRowNum != -1)
					expectedValue = transactionData.GetData(transactionRowNum,
							"zipcode");
				else
					expectedValue = "NA";
				break;
			case "Phone Number":
				expectedValue = transactionData.GetData(transactionRowNum, "phone");
				break;
			case "Shipping Address":
				expectedValue = transactionData.GetData(transactionRowNum, "address2");
				break;
			case "State":
				expectedValue = transactionData.GetData(transactionRowNum, "state");
				break;
			case "Email ID":
				expectedValue = transactionData.GetData(transactionRowNum, "email");
				break;			
			case "Offer String":
				expectedValue = transactionData.GetData(transactionRowNum, "udf4");
				break;
			case "Offer Type":
				expectedValue = statusDetails1.get("Offer Type");
				break;
			case "Offer Key":
				expectedValue = statusDetails1.get("Offer Key");
				break;
			case "Offer Failure Reason":
				expectedValue = statusDetails1.get("Offer Failure Reason");
				break;
			case "Error Message":
				if(!transactionDetail.get("Status").contains("Captured")){
					expectedValue = statusDetails2.get("Error Message");
				}
				else
					expectedValue = "skip";
				break;
			case "BankRefNo":
				if(transactionDetail.get("Status").contains("Captured")){
					expectedValue = actionsDoneDetail.get("BankRefNo");
				}
				else
					expectedValue = "skip";
				break;

			case "RequestID":
				if(transactionDetail.get("Status").contains("Captured")){
					expectedValue = actionsDoneDetail.get("RequestID");
				}
				else
					expectedValue = "skip";
				break;

			case "Action":
				if(transactionDetail.get("Status").contains("Captured")){
					expectedValue = actionsDoneDetail.get("Action");
				}
				else
					expectedValue = "skip";
				break;	

			case "Status_1":
				if(transactionDetail.get("Status").contains("Captured")){
					expectedValue = actionsDoneDetail.get("Status");
				}
				else
					expectedValue = "skip";
				break;	
			case "Risk action taken":
				expectedValue = riskDetail.get("Risk action taken");
				break;
			case "UDF1":
				expectedValue = merchantSpecificParametersDetail1.get("UDF1");
				break;
			case "UDF2":
				expectedValue = merchantSpecificParametersDetail2.get("UDF2");
				break;
			case "UDF3":
				expectedValue = merchantSpecificParametersDetail1.get("UDF3");
				break;
			case "UDF4":
				expectedValue = merchantSpecificParametersDetail2.get("UDF4");
				break;
			case "UDF5":
				expectedValue = merchantSpecificParametersDetail1.get("UDF5");
				break;
			}
			if (cardDetailsRowNum != -1){
				if(expectedValue.equalsIgnoreCase("{skip}")) continue;

				if(expectedValue.startsWith("{contains}")){
					expectedValue = expectedValue.replace("{contains}", "");
					Helper.compareContains(testConfig,"Response Param '" + expectedKey + "'", expectedValue, transactionDetail.get(expectedKey));
				}
			}
			//Verifying Amount & Discount
			if (expectedKey.equals("Amount") || 
					expectedKey.equals("Discount") ||
					expectedKey.equals("Transaction Fee"))
			{
				Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, transactionDetail.get(expectedKey).substring(4)) ;
			}

			//Verifying Transaction Detail
			if (expectedKey.equals("Product Information") || 
					expectedKey.equals("Merchant Name") || 
					expectedKey.equals("Payment Type") || 
					expectedKey.equals("Name on Card") || 
					expectedKey.equals("Bank Name") || 
					expectedKey.equals("Transaction ID") || 
					expectedKey.equals("PayU ID") ||
					expectedKey.equals("Status") ||
					expectedKey.equals("Payment Gateway") ||
					expectedKey.equals("Card Number") ||
					expectedKey.equals("Date of Transaction"))
			{
				Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, transactionDetail.get(expectedKey)) ;
			}

			//Verifying User Detail
			if (expectedKey.equals("First Name") || 
					expectedKey.equals("Last Name") || 
					expectedKey.equals("City") || 
					expectedKey.equals("Billing Address") || 
					expectedKey.equals("Zipcode") || 
					expectedKey.equals("Phone Number") || 
					expectedKey.equals("Shipping Address") ||
					expectedKey.equals("State") ||
					expectedKey.equals("Email ID"))
			{
				Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, userDetail.get(expectedKey)) ;
			}

			//Verifying Status Detail in first tab
			if (expectedKey.equals("Amount to be Captured") || 
					expectedKey.equals("Bank Reference No") ||					
					expectedKey.equals("Offer String") || 
					expectedKey.equals("Offer Key") ||
					expectedKey.equals("Offer Type") ||
					expectedKey.equals("Offer Failure Reason") ||
					expectedKey.equals("Bank Reference No"))
			{
				Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, statusDetails1.get(expectedKey)) ;
			}		

			//Verifying Status Detail in Second tab
			if (expectedKey.equals("Delivery Address"))
			{
				Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, statusDetails2.get(expectedKey)) ;
			}

			//Verifying Status Detail in Second tab, when transaction has failed
			if(cardDetailsRowNum != -1)
				if ((expectedKey.equals("Error Recommendation") || 
						expectedKey.equals("Error Message")) && !transactionDetail.get("Status").contains("Captured"))
				{
					Helper.compareEquals(testConfig,"Response Param '" + expectedKey + "'", expectedValue, statusDetails2.get(expectedKey)) ;
				}

			//Verifying Action Detail 
			if ((expectedKey.equals("BankRefNo") ||
					expectedKey.equals("Action") ||
					expectedKey.equals("Status_1") ||
					expectedKey.equals("RequestID")) && transactionDetail.get("Status").contains("Captured") )
			{
				Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, actionsDoneDetail.get(expectedKey)) ;
			}

			//Verifying Risk Detail 
			if (expectedKey.equals("Risk action taken"))
			{
				Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, riskDetail.get(expectedKey)) ;
			}

			//Verifying Merchant Parameters Detail in First tab
			if (expectedKey.equals("UDF1") || 
					expectedKey.equals("UDF3") ||					
					expectedKey.equals("UDF5"))
			{
				Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, merchantSpecificParametersDetail1.get(expectedKey)) ;
			}

			//Verifying Merchant Parameters Detail in Second tab
			if (expectedKey.equals("UDF2") || 
					expectedKey.equals("UDF4"))
			{
				Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, merchantSpecificParametersDetail2.get(expectedKey)) ;
			}
		}
		//restore the screenshot value
		testConfig.enableScreenshot = true;

		Helper.compareTrue(testConfig, "Transaction Details", testConfig.getTestResult());
	}


	public void VerifyRuleHit(Config testConfig){
		String expectedValue = null;
		String expectedKey = "Risk action taken";
		if(testConfig.getRunTimeProperty("Risk action taken")!=null)
			expectedValue = testConfig.getRunTimeProperty("Risk action taken");

		Helper.compareEquals(testConfig, "Response Param '" + expectedKey + "'", expectedValue, riskDetail.get(expectedKey)) ;

	}


	public String getactualAmountDollar() {
		return headerAmount.getText();
	}

	public String getActualTransFeeDollar() {
		return transfee.getText();
	}

	public String getActualDiscountDollar() {
		return discount.getText();
	}

	public String getActualAmountOnTransDetail() {
		return transdetailAmount.getText();
	}

	public String getExpectedAmountOnTransDetail(){
		return amount.getText();
	}

	public void verifyDollarSignOnTransactionDetailPage(Config testConfig,DashboardPage dashBoard,String what){

		Helper.compareEquals(testConfig,what,"$ "+getExpectedAmountOnTransDetail(), getactualAmountDollar());
		//Helper.compareEquals(testConfig,what,"$ "+getExpectedAmountOnTransDetail(), getActualTransFeeDollar());
		Helper.compareEquals(testConfig,what,"$ "+getExpectedAmountOnTransDetail(), getActualAmountOnTransDetail());
		Helper.compareEquals(testConfig,what,"$ "+"0.00", getActualDiscountDollar());





	}



	public void convertActionList()
	{
		rowCount = testConfig.driver.findElements(By.xpath("//div[7]/table/tbody/tr")).size();
		actionsDoneDetail = DashboardHelper.convertActionsDetailToList(actionDetailHeader.getText(),actionDetailRow1.getText());
		if(rowCount==3){
			actionsDoneDetail1 = DashboardHelper.convertActionsDetailToList(actionDetailHeader.getText(),actionDetailRow2.getText());
		}
		if(rowCount ==4){
			actionsDoneDetail1 = DashboardHelper.convertActionsDetailToList(actionDetailHeader.getText(),actionDetailRow2.getText());
			actionsDoneDetail2 = DashboardHelper.convertActionsDetailToList(actionDetailHeader.getText(),actionDetailRow3.getText());
		}

	}	


	public void verifyCurrencyType(TestDataReader transactionData,int transactionRowNum, Config testConfig, String currencyType)
	{
		String expected =  transactionData.GetData(transactionRowNum, "amount");
		if(currencyType.equalsIgnoreCase("USD"))
			expected ="$ "+expected;			
		else if (currencyType.equalsIgnoreCase("INR"))
			expected ="Rs. "+expected;

		Helper.compareEquals(testConfig, "Verifying Currency ",expected, transactionDetail.get("Amount")) ;		
	}

	public String VerifyAutoRefund(TestDataReader transactionData, verifyReconResult verifyEnum, AutoRefund autoRefund, Config testConfig)
	{	
		//so that screenshot is not taken again and again, for each response key verification
		testConfig.enableScreenshot = false;

		String status="";
		String actual_action1="";
		String actual_action2="";
		String actual_status1="";
		String actual_status2="";
		String request_id="";

		switch(verifyEnum){

		case failed:

			switch(autoRefund){

			case on:

				convertActionList();
				status = transactionDetail.get("Status");

				if(transactionDetail.get("Payment Gateway").contains("CITI"))
				{
					Helper.compareEquals(testConfig, "Response Param status" , "Captured" , status) ;
				}
				else
				{
					Helper.compareEquals(testConfig, "Response Param status" , "Auto Refund In Progress" , status) ;
				}	

				actual_action1 = actionsDoneDetail.get("Action");
				actual_status1 =actionsDoneDetail.get("Status");

				actual_action2 = actionsDoneDetail1.get("Action");
				actual_status2 =actionsDoneDetail1.get("Status");

				request_id =  actionsDoneDetail1.get("RequestID");

				Helper.compareEquals(testConfig, "Response Param action_2" , "capture" , actual_action1) ;
				Helper.compareEquals(testConfig, "Response Param status_2" , "success" , actual_status1) ;

				Helper.compareEquals(testConfig, "Response Param action_1" , "refund" , actual_action2) ;
				Helper.compareEquals(testConfig, "Response Param status_1" , "queued" , actual_status2) ;

				break;

			case off:
				convertActionList();
				status = transactionDetail.get("Status");

				Helper.compareEquals(testConfig, "Response Param status" , "Captured" , status) ;
				actual_action1 = actionsDoneDetail.get("Action");
				actual_status1 =actionsDoneDetail.get("Status");


				Helper.compareEquals(testConfig, "Response Param action_1" , "capture" , actual_action1) ;
				Helper.compareEquals(testConfig, "Response Param status_1" , "success" , actual_status1) ;
				break;			
			}
			break;

		case userCancelled:

			status = transactionDetail.get("Status");			
			Helper.compareEquals(testConfig, "Response Param status" , "UserCancelled" , status) ;

			String expected_string  = "No Action performed.";
			String actual_string = actionDetailTable.getText();

			Helper.compareEquals(testConfig, "Response Action" , expected_string , actual_string) ;

			break;


		case dropped:
			switch(autoRefund)
			{
			case off:
				convertActionList();

				status = transactionDetail.get("Status");

				Helper.compareEquals(testConfig, "Response Param status" , "Captured" , status) ;
				actual_action1 = actionsDoneDetail.get("Action");
				actual_status1 =actionsDoneDetail.get("Status");


				Helper.compareEquals(testConfig, "Response Param action_1" , "capture" , actual_action1) ;
				Helper.compareEquals(testConfig, "Response Param status_1" , "success" , actual_status1) ;

				break;

			case on:
				convertActionList();
				status = transactionDetail.get("Status");

				if(transactionDetail.get("Payment Gateway").contains("CITI"))
				{
					Helper.compareEquals(testConfig, "Response Param status" , "Captured" , status) ;
				}
				else
				{
					Helper.compareEquals(testConfig, "Response Param status" , "Auto Refund In Progress" , status) ;
				}	
				actual_action1 = actionsDoneDetail.get("Action");
				actual_status1 =actionsDoneDetail.get("Status");

				actual_action2 = actionsDoneDetail1.get("Action");
				actual_status2 =actionsDoneDetail1.get("Status");					

				request_id =  actionsDoneDetail1.get("RequestID");

				Helper.compareEquals(testConfig, "Response Param action_1" , "capture" , actual_action1) ;
				Helper.compareEquals(testConfig, "Response Param status_1" , "success" , actual_status1) ;

				Helper.compareEquals(testConfig, "Response Param action_2" , "refund" , actual_action2) ;
				status = transactionDetail.get("Status");

				if(transactionDetail.get("Payment Gateway").contains("CITI"))
				{
					Helper.compareEquals(testConfig, "Response Param status_2" , "queued" , actual_status2) ;
				}
				else
				{
					Helper.compareEquals(testConfig, "Response Param status_2" , "requested" , actual_status2) ;
				}	
				break;
			}

			break;
		case captured:

			switch(autoRefund){

			case on:
			case off:

				convertActionList();
				status = transactionDetail.get("Status");
				
				Helper.compareEquals(testConfig, "Response Param status" , "Captured" , status) ;
								
				actual_action1 = actionsDoneDetail.get("Action");
				actual_status1 =actionsDoneDetail.get("Status");
				
				Helper.compareEquals(testConfig, "Response Param action_1" , "capture" , actual_action1) ;
				Helper.compareEquals(testConfig, "Response Param status_1" , "SUCCESS" , actual_status1) ;

				break;		
			}
			break;

		case auth:
			switch(autoRefund)
			{
			case on:
			case off:

				convertActionList();

				status = transactionDetail.get("Status");

				Helper.compareEquals(testConfig, "Response Param status" , "Authorized" , status) ;

				actual_action1 = actionsDoneDetail.get("Action");
				actual_status1 =actionsDoneDetail.get("Status");

				actual_action2 = actionsDoneDetail1.get("Action");
				actual_status2 =actionsDoneDetail1.get("Status");

				Helper.compareEquals(testConfig, "Response Param action_1" , "auth" , actual_action1) ;
				Helper.compareEquals(testConfig, "Response Param status_1" , "SUCCESS" , actual_status1) ;

				Helper.compareEquals(testConfig, "Response Param action_2" , "capture" , actual_action2) ;
				Helper.compareEquals(testConfig, "Response Param status_2" , "QUEUED" , actual_status2) ;

				break;					
			}
			break;

		case bounced:
			status = transactionDetail.get("Status");			
			Helper.compareEquals(testConfig, "Response Param status" , "Bounced" , status) ;

			expected_string  = "No Action performed.";
			actual_string = actionDetailTable.getText();

			Helper.compareEquals(testConfig, "Response Action" , expected_string , actual_string) ;

			break;

		default:
			System.out.println("Status not Handelled");
		}

		//restore the screenshot value
		testConfig.enableScreenshot = true;

		Helper.compareTrue(testConfig, "Transaction Details", testConfig.getTestResult());
		
		Element.click(testConfig, oK_button, "oK_button");
        return request_id;
	}


}
