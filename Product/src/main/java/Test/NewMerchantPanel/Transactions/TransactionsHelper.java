package Test.NewMerchantPanel.Transactions;

import java.util.Map;

import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage.filterTypes;
import PageObject.NewMerchantPanel.Transactions.RequestsPage;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage.dropDownAction;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.FileHandler;
import Utils.Helper;

public class TransactionsHelper {
	
	private Config testConfig;

	private TransactionsPage transactionPage;
	private RequestsPage requestPage;
	
	public TransactionsHelper(Config testConfig) {
	
		this.testConfig = testConfig;
	}

	
	/**
	 * 
	 * @param filePath
	 *            is the path where sheet has to be modified
	 * @param row
	 *            of the excel file
	 * @param mihpayid
	 *            is the payu id
	 * @param amt
	 *            is the amount
	 */
	public void writeToExcel(String filePath, int row, String mihpayid,
			String status, String amt) {
		FileHandler.write_toedit(filePath, "Sheet1", row, 2, amt, mihpayid, 0);
		FileHandler.write_toedit(filePath, "Sheet1", row, 1, status, mihpayid,
				0);
		FileHandler.write_toedit(filePath, "Sheet1", row, 0, mihpayid,
				mihpayid, 0);

	}	

	
	
	public enum filterBy{TransactionID,RequestID,ReferenceID,CustomerName,CustomerEmail,PayUID};
	/**
	 * Method used to filter the data based on given search strings
	 * 
	 * @param searchData
	 * @param rowNo
	 */
	public void filterDataAndVerifyWithDB(String[] searchData, filterBy filterType,int requestQueryRowNum) {
		String searchKey = "";
	
		if(searchData.length==1)
		{
			searchKey=searchData[0];
		}
		else{
		for (int i = 0; i < searchData.length; i++)
			searchKey = searchKey + "," + searchData[i];
		}
		TransactionsPage transactionPage=new TransactionsPage(testConfig);
		MerchantPanelPage merchantPage=new MerchantPanelPage(testConfig);
		merchantPage.searchData(searchKey);
		for (int i = 1; i <= searchData.length; i++)

		{
			switch (filterType) {
			case TransactionID:
				transactionPage=new TransactionsPage(testConfig);
				transactionPage.selectTransactionRowToCompare(searchData, i);
				break;
			case RequestID:
				requestPage=new RequestsPage(testConfig);
				requestPage.selectRequestedRowToCompare(searchData, i);
				break;	

			case ReferenceID:
				requestPage=new RequestsPage(testConfig);
				requestPage.selectRequestedRowToCompare(searchData, i);
				break;

			case PayUID:
				transactionPage=new TransactionsPage(testConfig);
				transactionPage.selectPayUIDRowToCompare(searchData, i);
				break;
				
			case CustomerName:
				transactionPage=new TransactionsPage(testConfig);
				transactionPage.selectTransactionRowToCompare(searchData, i);
				break;
				
			case CustomerEmail:
				transactionPage=new TransactionsPage(testConfig);
				transactionPage.selectTransactionRowToCompare(searchData, i);
				break;	

			default:
				break;
			}

			Browser.wait(testConfig, 2);
			
			transactionPage.compareTransactionDetailsWithDB(requestQueryRowNum);

		}
	}

	/**
	 * Method used to select First check box and perform drop dowm action
	 * @param actionType
	 */
	public void selectCheckBoxActionType(dropDownAction actionType)
	{
		TransactionsPage transactionPage=new TransactionsPage(testConfig);
		transactionPage.selectFirstCheckBox();
		transactionPage.selectDropDownAction(actionType);
	}
	/**
	 * Method used to vewrify the DB Status
	 * rowNo in data sheet which has query which returns the status
	 * @param rowNo
	 */
	public void verifyStatusInDB(int rowNo) {
		Map<String, String> map = DataBase.executeSelectQuery(testConfig,
				rowNo, 1);

		Helper.compareEquals(testConfig, "Comparing the DB status", "SUCCESS",
				map.get("status"));
	}

	// Note: The Below code shoud be un commented when we are about to merge
		// email invoice test case
		// /**
		// * Logs in as a merchant and fills email invoice form and submits
		// *
		// * @return InvoiceTransactionConfirmationPage
		// * @param transactionRowNum
		// */
//		 public void FillEmailInvoiceAndDoTransaction(int transactionRowNum,
//		 String testType,int cardDetailsRowNum) {
//		
//		 EmailInvoicePage emailInvoicePage = new EmailInvoicePage(
//		 testConfig);
//		
//		 Element.click(testConfig, emailInvoice, "Click email invoice");
//		 // fill email invoice form
//		 emailInvoicePage.fillInvoiceForm(transactionRowNum);
//		
//		 // click confirm
//		 emailInvoicePage.clickConfirmButton();
//		 Browser.wait(testConfig, 2);
//		 String url2 = emailInvoicePage.retInvoiceURL();
//		 Browser.navigateToURL(testConfig, url2);
//		 //dashboardHelper.invoiceTransactionConfirmationPage.CopyLink();
//		 TransactionHelper helper=new TransactionHelper(testConfig, false);
//		 NewResponsePage newResponse =
//		 helper.makePaymentViaCreditCard(cardDetailsRowNum);
//		
//		 //String txnId = testConfig.getRunTimeProperty("mihpayid");
//		 // verify for success transaction
//		 newResponse.verifyTransactionStatus(true);
//		 }
		 
		 
		 	/**
			 * Method is basically used to rest the date from one week to current date
			 */
			public void setCurrentDate()
			{
				String currentUrl=testConfig.driver.getCurrentUrl();
				String currentDate=Helper.getCurrentDate("yyyy-MM-dd");
				currentUrl=currentUrl+"?from="+currentDate+"&to="+currentDate;
				Browser.navigateToURL(testConfig, currentUrl);
				
			}
					 
			/**
			 * Verifying Refund Action on Transactions
			 * 
			 * @param refundAmount
			 * @param transactionID
			 * @param refundMessage
			 * @return
			 */
			public void performRefundAction(String refundAmount, String transactionID,
					String refundMessage,String status,String remainingAmount) {

				// Verify Amount as Prefilled and Editable for Partial Refund.
				TransactionsPage transactionPage=new TransactionsPage(testConfig);
				
				transactionPage.FillRefundAmount(transactionID, refundAmount);
				String refId= transactionPage.verifyRefundMessage(1);

				//execute cron
				Helper.executeCron(testConfig,"processRefund.php");

				// Verify Request Status in All Requests Tab
				RequestsPage requestPage=new RequestsPage(testConfig);
				requestPage.verifyRequestedRefunds(refId,1);

				// verify Stauts in txn_update table which should be updated as Refund
				Map<String, String> map = DataBase.executeSelectQuery(testConfig, 141,
						1);

				Helper.compareEquals(testConfig, "Status in DB", map.get("action")
						.toString(), "refund");

				// Verify status in All Transactions Tab for the Payu ID
				transactionPage.verifyTransactionStatus(transactionID, status,1);

				// Verify amount for Refunded Transaction.
				transactionPage.verifyBalanceAmount(remainingAmount, 1);

			}

		 /**
		  * Method used to set hte given date based on the no of days in difference
		  * @param format
		  * @param noOfDays
		  */
		 public void setGivenDate(String format,int noOfDays)
		 {
			 String expectedValidDate = Helper.getDateBeforeOrAfterDays(noOfDays, format);
				String currentUrl = testConfig.driver.getCurrentUrl();
				currentUrl=currentUrl+"?from="+expectedValidDate+"&to="+expectedValidDate;
				Browser.navigateToURL(testConfig, currentUrl);
				
			 
		 }
		 
		 /**text with given filter type
		  * Method used to search 
		  * @param type
		  * @param searchKey
		  */
		 public void searchDataByFilterType(filterTypes type,String searchKey)
		 {
			 
			 MerchantPanelPage merchantPage=new MerchantPanelPage(testConfig);
			 merchantPage.filterBasedOn(type);
			 merchantPage.searchData(searchKey);
		 }
		 
		 /**
		  * Method used to search data with filtertype and compare details with testdata
		  * @param filterBy
		  * @param transactionRowNum
		  * @param searchKey
		  */
		 public void filterAndCompareDetailsWithTestData(filterTypes filterBy,int transactionRowNum,String searchKey)
		 {
			 searchDataByFilterType(filterBy, searchKey);
			 TransactionsPage transactionPage=new TransactionsPage(testConfig);
			 transactionPage.compareTransactionDetailsWithTestData(transactionRowNum);
		 }
		 
		 /**
		  * Method used to click few customize coulmns and compare same details with DB
		  * @param requestQueryRowNum
		  */
		 public void clickSelectiveCoulmnsAndCompareDetailsWithDB(int requestQueryRowNum)
		 {
			 TransactionsPage transactionPage=new TransactionsPage(testConfig);
			 transactionPage.clickSelectiveCustomizedOptions();
			 transactionPage.verifySelectedColumnsDetails(requestQueryRowNum);
		 }
}

