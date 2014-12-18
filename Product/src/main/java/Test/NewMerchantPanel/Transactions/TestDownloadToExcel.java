package Test.NewMerchantPanel.Transactions;

import java.io.File;
import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.NewMerchantPanel.DashBoard.DashboardPage;
import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import PageObject.NewMerchantPanel.Transactions.RequestsPage;
import PageObject.NewMerchantPanel.Transactions.RequestsPage.requestType;
import Test.NewMerchantPanel.Transactions.TransactionsHelper;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage.filterModes;

import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestDownloadToExcel extends TestBase {

	private TransactionsPage transactionPage;
	private RequestsPage requests;
	private DashboardPage dashboarPage;
	private MerchantPanelPage merchantPage;
	private TransactionsHelper tranHelper;

	/**
	 * Product-2146:Verify_transaction_filter_Selective payment method
	 * @param testConfig
	 */
	@Test(description = "Verify Downloaded on All Transactions", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyDownloadedTransactions(Config testConfig) {

		int transactionRowNum = 157;
		// login into the Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		tranHelper=new TransactionsHelper(testConfig);
		merchantPage = helper.doMerchantLogin(transactionRowNum);
		transactionPage = merchantPage.ClickTransactionsTab();
		testConfig.putRunTimeProperty("mode", "CC");
		transactionPage.filterByModes(filterModes.CC);
		transactionPage.exportToExcel();
		// Filter By Credit Card

		File file = Browser	.lastFileModifiedWithDesiredName(testConfig, "C:\\Users\\"
						+ System.getProperty("user.name") + "\\Downloads",
						"Transactions");
		TestDataReader reader = new TestDataReader(testConfig, "PayU",
				file.getAbsolutePath());
	
		//Comparing the no of records available after filtering
		Helper.compareEquals(testConfig, "Comparing the rows", (reader.getRecordsNum()-1), Integer.parseInt(transactionPage.getNoOfRecords()));
		//comparing individual records
		transactionPage.compareTransactionDetailsWithDownloadedFile(1, reader);
		//deleting the file after execution
		file.delete();
		Assert.assertTrue(testConfig.getTestResult());	
	}
	
	/**
	 * Product-2162:Verify_request_filter_selective Request type
	 * @param testConfig
	 */
	@Test(description = "Verify Downloaded on Requests Page", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyDownloadedRequests(Config testConfig) {

		int transactionRowNum = 157;
		// login into the Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		merchantPage = helper.doMerchantLogin(transactionRowNum);
		requests=merchantPage.ClickRequestsTab();		
		requests.filterByRequestType(requestType.Capture);
		requests.exportToExcel();
		// Filter By Credit Card

		File file = Browser	.lastFileModifiedWithDesiredName(testConfig, "C:\\Users\\"
						+ System.getProperty("user.name") + "\\Downloads",
						"Transactions");
		Browser.wait(testConfig, 2);
		TestDataReader reader = new TestDataReader(testConfig, "PayU",
				file.getAbsolutePath());
	
		//Comparing the no of records available after filtering
		Helper.compareEquals(testConfig, "Comparing the rows", (reader.getRecordsNum()-1), requests.getNoOfRecords());
		//comparing individual records
		requests.compareTransactionDetailsWithDownloadedFile(1, reader);
		//deleting the file after execution
		file.delete();
		Assert.assertTrue(testConfig.getTestResult());	
	}
}
