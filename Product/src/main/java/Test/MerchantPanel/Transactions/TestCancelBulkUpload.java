package Test.MerchantPanel.Transactions;

import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Transactions.CancelPage;
import PageObject.MerchantPanel.Transactions.CapturePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.FileHandler;
import Utils.Helper;
import Utils.Helper.FileType;
import Utils.TestBase;

public class TestCancelBulkUpload extends TestBase {

	//public void TestValidBulkUpload(Config testConfig) - moved to LiveCard TestCases - 4
	
	@Test(description = "Verify one transaction upload for  request", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestBlankBulkUpload(Config testConfig)  {

		
		int transactionRowNum = 2;
		
		String bulkUploadFile = Helper.getExcelFile(testConfig, FileType.refund);

		// Navigating to Merchant panel to cancel request
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		CancelPage cancelTransaction = dashBoard.ClickCancel();

		// Upload bulk cancel file
		cancelTransaction.BulkFileupload(bulkUploadFile);
		cancelTransaction.CancelBulkTransaction();

		//Verify msg for blank bulk captured transaction			
				String msgtext=cancelTransaction.getInvalidFileText();
				Helper.compareEquals(testConfig,"Invaild Message text" , "cancel Error: Invalid File", msgtext);
				Assert.assertTrue(testConfig.getTestResult());				
		
	}
	
	@Test(description = "Verify csv file upload in bulk refund", 
			dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestCSVBulkUpload(Config testConfig)  {
		
		int transactionRowNum = 1;
		
		// Navigating to Merchant panel to refund request
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		CancelPage cancelTransaction = dashBoard.ClickCancel();
        String invalidFile= "InvalidBulkRefund.csv";
        
		// Upload CSV file
        cancelTransaction.BulkInvalidUpload(invalidFile);
        cancelTransaction.RefundBInvalidTransaction();
		
		// Verify invalid file msg
		String invalidFileMsg=cancelTransaction.getInvalidFileFormatText();
		Helper.compareEquals(testConfig,"Invaild Message text" , "The file uploaded is not a valid excel (.xls) file", invalidFileMsg);
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
		filePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//"+ filePath;

		FileHandler.write_toedit(filePath,"Sheet1", row, 2, amt, mihpayid, 0);
		FileHandler.write_toedit(filePath,"Sheet1", row, 1, status, mihpayid, 0);
		FileHandler.write_toedit(filePath,"Sheet1", row, 0, mihpayid, mihpayid, 0);

	}	
}

