package Test.NewMerchantPanel.Transactions;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import Test.NewMerchantPanel.Transactions.TransactionsHelper;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage.filterByOrderStatus;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage.filterModes;
import Utils.Config;
import Utils.TestBase;

public class TestFilters extends TestBase {

	private TransactionsPage transactionPage;
	private MerchantPanelPage merchantPage;
	private TransactionsHelper tranHelper;

	/**
	 * Test Case ID Product-1975 :Verify Filters based on Mode
	 * @param testConfig
	 */
	@Test(description = "Verify Filters based on Mode", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void filterByPayementMode(Config testConfig) {

		int requestQueryRowNum=122;
		int transactionRowNum = 157;
		// login into the Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		tranHelper=new TransactionsHelper(testConfig);
		merchantPage = helper.doMerchantLogin(transactionRowNum);

		testConfig.putRunTimeProperty("merchantID", "6912");
		transactionPage = merchantPage.ClickTransactionsTab();

		tranHelper.setCurrentDate();
		// Filter By Net Banking Card
		testConfig.putRunTimeProperty("mode", "'NB'");
		transactionPage.filterByModes(filterModes.NB);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// Filter By EMI
		transactionPage.resetFilters();
		testConfig.putRunTimeProperty("mode", "'EMI'");
		transactionPage.filterByModes(filterModes.EMI);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// Filter By Cash On Delivery
		transactionPage.resetFilters();
		testConfig.putRunTimeProperty("mode", "'COD'");
		transactionPage.filterByModes(filterModes.COD);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// Filter By Card
		transactionPage.resetFilters();
		testConfig.putRunTimeProperty("mode", "'CASH'");
		transactionPage.filterByModes(filterModes.CASH);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// Filter By Debit Card
		transactionPage.resetFilters();
		testConfig.putRunTimeProperty("mode", "'DC'");
		transactionPage.filterByModes(filterModes.DC);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// Filter By Credit Card
		transactionPage.resetFilters();
		testConfig.putRunTimeProperty("mode", "'CC'");
		transactionPage.filterByModes(filterModes.CC);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// FIlter by All
		transactionPage.resetFilters();
		testConfig.putRunTimeProperty("mode", "'CC','DC','CASH','COD','NB','EMI'");
		transactionPage.filterByModes(filterModes.ALL);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 *  Test Case ID Product-1977 :To verify data in the merchant panel after adding filters of Status.
	 *  
	 * To verify data in the merchant panel after adding filters of Status.
	 * @param testConfig
	 */
	@Test(description = "To verify data in the merchant panel after adding filters of Status.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void filterByStatus(Config testConfig) {

		int requestQueryRowNum=123;
		int transactionRowNum = 157;
		
		// login into the Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		TransactionsHelper tranHelper=new TransactionsHelper(testConfig);
		merchantPage = helper.doMerchantLogin(transactionRowNum);

		testConfig.putRunTimeProperty("merchantID", "6912");
		transactionPage = merchantPage.ClickTransactionsTab();
		tranHelper.setCurrentDate();
		// Filter By Status Authourized
		testConfig.putRunTimeProperty("filterBy", "'auth'");
		transactionPage.filterByOrderStatus(filterByOrderStatus.Authourized);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// Filter By status captured

		testConfig.putRunTimeProperty("filterBy", "'captured'");
		transactionPage.resetFilters();
		transactionPage.filterByOrderStatus(filterByOrderStatus.Captured);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// Filter By Status cancelled
		testConfig.putRunTimeProperty("filterBy", "'cancelled'");
		transactionPage.resetFilters();
		transactionPage.filterByOrderStatus(filterByOrderStatus.Cancelled);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// Filter By status Bounced
		testConfig.putRunTimeProperty("filterBy", "'bounced'");
		transactionPage.resetFilters();
		transactionPage.filterByOrderStatus(filterByOrderStatus.Bounced);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// Filter By status Cancelled By User
		testConfig.putRunTimeProperty("filterBy", "'userCancelled'");
		transactionPage.resetFilters();
		transactionPage.filterByOrderStatus(filterByOrderStatus.CancelledByUser);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// Filter By Status Failed
		testConfig.putRunTimeProperty("filterBy", "'failed'");
		transactionPage.resetFilters();
		transactionPage.filterByOrderStatus(filterByOrderStatus.CancelledByBank);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		// Filter By status Dropped
		testConfig.putRunTimeProperty("filterBy", "'dropped'");
		transactionPage.resetFilters();
		transactionPage.filterByOrderStatus(filterByOrderStatus.Dropped);
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}


	/**
	 *Product-1982:To verify data in the merchant panel on adding the filter of Bin List.
	 * To verify data in the merchant panel on adding the filter of Bin List.
	 * @param testConfig
	 */
	@Test(description = "To verify data in the merchant panel on adding the filter of Bin List.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void filterByBinList(Config testConfig) {

		int requestQueryRowNum=125;
		int transactionRowNum = 157;
		// login into the Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		TransactionsHelper tranHelper=new TransactionsHelper(testConfig);
		merchantPage = helper.doMerchantLogin(transactionRowNum);

		testConfig.putRunTimeProperty("merchantID", "6912");
		transactionPage = merchantPage.ClickTransactionsTab();
		tranHelper.setCurrentDate();
		//filter by bin list and compare it with DB
		transactionPage.filterByBinList("512345");
		transactionPage.compareRecordsWithDB(requestQueryRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	
}
