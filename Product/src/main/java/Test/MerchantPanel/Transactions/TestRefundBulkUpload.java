package Test.MerchantPanel.Transactions;

import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Transactions.RefundPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.FileHandler;
import Utils.Helper;
import Utils.Helper.FileType;
import Utils.TestBase;

public class TestRefundBulkUpload extends TestBase {

	@Test(description = "Verify valid refund via bulk upload", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestValidBulkUpload(Config testConfig)  {

		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 1;
		int paymentTypeRowNum =17;
		int cardDetailsRowNum = 1;
		int requestRowNum=1;

		tranHelper.GetTestTransactionPage();

		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		String bulkUploadFile = Helper.getExcelFile(testConfig, FileType.refund);

		writeToExcel(bulkUploadFile,1, testConfig.getRunTimeProperty("mihpayid"), "captured",tranHelper.testResponse.actualResponse.get("amount"));
		
		// Navigating to Merchant panel to refund request
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		// Upload bulk refund file
		refundTransaction.BulkFileupload(bulkUploadFile);
		refundTransaction.RefundBulkTransaction();
        
		//Verify bulk refunded transaction status			
		refundTransaction.VerifyRefundTransaction(requestRowNum);
		Assert.assertTrue(testConfig.getTestResult());			
	}
	
	@Test(description = "Verify blank refund upload", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestBlankBulkUpload(Config testConfig)  {

		int transactionRowNum = 1;
		String bulkUploadFile = Helper.getExcelFile(testConfig, FileType.refund);
		// Navigating to Merchant panel to refund request
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		// Upload blank bulk refund file
		refundTransaction.BulkFileupload(bulkUploadFile);
		refundTransaction.RefundBulkTransaction();

		//Verify message on refund page			
		String msgtext=refundTransaction.getInvalidFileText();
		Helper.compareEquals(testConfig,"Invaild Message text" , "refund Error: Invalid File", msgtext);
		Assert.assertTrue(testConfig.getTestResult());	
	}
	@Test(description = "Verify csv file upload in bulk refund", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestCSVBulkUpload(Config testConfig)  {
		
		int transactionRowNum = 1;
		
		// Navigating to Merchant panel to refund request
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();
        String invalidFile= "InvalidBulkRefund.csv";
        
		// Upload CSV file
		refundTransaction.BulkInvalidUpload(invalidFile);
		refundTransaction.RefundBInvalidTransaction();
		
		// Verify invalid file msg
		String invalidFileMsg=refundTransaction.getInvalidFileFormatText();
		Helper.compareEquals(testConfig,"Invaild Message text" , "The file uploaded is not a valid excel (.xls) file", invalidFileMsg);
		Assert.assertTrue(testConfig.getTestResult());			
	}
	
	@Test(description = "Verify worng format file upload for Refund request", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestwrongBulkUpload(Config testConfig)  {
		
		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		int transactionRowNum=1;
		String bulkUploadFile = Helper.getExcelFile(testConfig, FileType.refund);

		writeToExcel(bulkUploadFile,1, "jhdgs", "1234","767");
		
		// Navigating to Merchant panel to refund request
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		// Upload bulk refund file
		refundTransaction.BulkFileupload(bulkUploadFile);
		refundTransaction.RefundBulkTransaction();

		//Verify message on refund page			
		String msgtext=refundTransaction.getInvalidFileText();
		Helper.compareEquals(testConfig,"Invaild Message text" , "refund Error: Invalid File", msgtext);
		testConfig.logComment("test failed due to http://redmine.ibibo.com/issues/18065");
		Assert.assertTrue(testConfig.getTestResult());			
	}
	
	@Test(description = "Verify valid refund via bulk upload", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestsummaryresultBulkUpload(Config testConfig)  {

		

		//MasterCard Debit Cards
		int transactionRowNum = 1;
				
		String bulkUploadFile = Helper.getExcelFile(testConfig, FileType.refund);

		//writeToExcel(bulkUploadFile,1, testConfig.getRunTimeProperty("mihpayid"), "captured",tranHelper.testResponse.actualResponse.get("amount"));
		writeToExcel(bulkUploadFile,1, "403993715507925921", "auth","1");
		// Navigating to Merchant panel to refund request
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		// Upload bulk refund file
		refundTransaction.BulkFileupload(bulkUploadFile);
		refundTransaction.RefundFailureTransaction();
		String refId = testConfig.getRunTimeProperty("token");
		testConfig.logComment("***"+refId);
		refundTransaction.downloadRefundSummary(refId);	

		// verify failure data in excel sheet
		refundTransaction.verifyBulkRefundFailureExcelSheet(refId);

		Assert.assertTrue(testConfig.getTestResult());			
	}
	
	
	@Test(description = "Verify worng format file upload for Refund request", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestmultipleBulkUpload(Config testConfig)  {
		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 3;
		int paymentTypeRowNum =5;
		int cardDetailsRowNum = 1;
		
		tranHelper.GetTestTransactionPage();

		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		String bulkUploadFile = Helper.getExcelFile(testConfig, FileType.refund);

		writeToExcel(bulkUploadFile,1, testConfig.getRunTimeProperty("mihpayid"), "captured",tranHelper.testResponse.actualResponse.get("amount"));
		
		writeToExcel(bulkUploadFile,2, "403993715507925921", "auth","1.00");
		// Navigating to Merchant panel to refund request
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		// Upload bulk refund file
		refundTransaction.BulkFileupload(bulkUploadFile);
		refundTransaction.RefundMultipleBulkTransaction();
		String refId = testConfig.getRunTimeProperty("token");
		testConfig.logComment("***"+refId);
		Browser.wait(testConfig, 100);
		refundTransaction.downloadRefundSummary(refId);	

		// verify failure data in excel sheet
		refundTransaction.verifymultipleBulkRefundExcelsheet(refId);

		Assert.assertTrue(testConfig.getTestResult());		
	}
	/**
	 * 
	 * @param filePath is the path where sheet has to be modified
	 * @param row of the excel file
	 * @param mihpayid is the payu id
	 * @param amt is the amount 
	 */
	public void writeToExcel(String filePath, int row, String mihpayid, String status,String amt)
	{
		FileHandler.write_toedit(filePath,"Sheet1", row, 2, amt, mihpayid, 0);
		FileHandler.write_toedit(filePath,"Sheet1", row, 1, status, mihpayid, 0);
		FileHandler.write_toedit(filePath,"Sheet1", row, 0, mihpayid, mihpayid, 0);

	}	
}

