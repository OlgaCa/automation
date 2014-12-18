package Test.MerchantPanel.Transactions;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;



public class ViewTransactions extends TestBase{


	public TransactionPage trans;

	/**
	 * @author jyoti.patial
	 * Verify URL in dashboard for selected time periods
	 */
	@Test(description = "Verify selected time period's URL", 
			dataProvider="GetTestConfig", timeOut=600000, groups="2")
	public void test_TimePeriod(Config testConfig)
	{ 
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(1);

		//For Yesterday
		dashBoard.SelectTimePeriod(1);
		dashBoard.VerifyRedirectedURL(1);

		//For Last Week
		dashBoard.SelectTimePeriod(2);
		dashBoard.VerifyRedirectedURL(2);

		//For last 4 Weeks
		dashBoard.SelectTimePeriod(3);
		dashBoard.VerifyRedirectedURL(3);

		//For This Year
		dashBoard.SelectTimePeriod(4);
		dashBoard.VerifyRedirectedURL(4);

		//For Today
		dashBoard.SelectTimePeriod(5);
		dashBoard.VerifyRedirectedURL(5);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	/**
	 * @author jyoti.patial
	 * Verify the debit card transaction through AXIS gateway on dashboard
	 */
	@Test(description = "Verify debit card transaction through AXIS gateway on Dashboard", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyAXISTransaction(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 1;
		int paymentTypeRowNum = 17;
		int cardDetailsRowNum = 1;
		helper.GetTestTransactionPage();

		trans = new TransactionPage(testConfig);
		helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify Export to excel exports all transaction details of the week by default", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void exportExcelbyWeek(Config testConfig)   {

		int transactionRowNum = 65;

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
	
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		dashBoard.ClickExportExcelButton();
	
		Browser.wait(testConfig,30);
		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);
		Helper.compareEquals(testConfig, "result count of transaction in excel", dashBoard.transactionno(),String.valueOf(tr.getRecordsNum()-1));	
		Assert.assertTrue(testConfig.getTestResult());

	}

	
	@Test(description = "Verify Export to excel exports all transaction details count greater than 1000", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void exportExcelbyWeekCount(Config testConfig) throws ParseException   {
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String dt = dateFormat.format(date);  // today's date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(dt));
		c.add(Calendar.DATE, -90);  // number of days to add
		dt = sdf.format(c.getTime());  // dt is now the date before 3 months
		
		int transactionRowNum = 3;
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickViewAll();
		
		dashBoard.Filldateinputfrom(dt);
		dashBoard.apply();
		Browser.wait(testConfig, 5);
		dashBoard.ClickExportExcelViewAll("test");	
		
		Helper.compareTrue(testConfig, "Pending file available", dashBoard.PendingWaitDownloadExcel());
		Assert.assertTrue(testConfig.getTestResult());

	}
	
}
