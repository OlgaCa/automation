package Test.MerchantPanel.Dashboard;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.text.DecimalFormat;
import java.util.Map;

import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import PageObject.MerchantPanel.Transactions.TransactionDetailPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Transactions.ViewTransactions;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestDashboard extends TestBase {

	private  DashboardPage dashBoard;

	@Test(description = "Verify dasboard data",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyDashboardData(Config testConfig) {

		int testRow = 1; 
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(testRow);

		//Data for today
		String from = Helper.getCurrentDate("yyyy-MM-dd");
		String to = Helper.getDateBeforeOrAfterDays(1,"yyyy-MM-dd");
		checkingRoutine(testConfig,from,to,dashBoard,"today",0);

		//Data for yesterday
		dashBoard.SelectTimePeriod(1);
		from = Helper.getDateBeforeOrAfterDays(-1,"yyyy-MM-dd");
		to = Helper.getCurrentDate("yyyy-MM-dd");
		checkingRoutine(testConfig,from,to,dashBoard,"yesterday",1);

		//Data for week
		dashBoard.SelectTimePeriod(2);
		from = Helper.getDateBeforeOrAfterDays(-7,"yyyy-MM-dd");
		to = Helper.getCurrentDate("yyyy-MM-dd");
		checkingRoutine(testConfig,from,to,dashBoard,"week",2);

		//Data for 4 week
		dashBoard.SelectTimePeriod(3);
		from = Helper.getFourWeekStartingDate("yyyy-MM-dd");
		to = Helper.getDateBeforeOrAfterDays(1,"yyyy-MM-dd");
		checkingRoutine(testConfig,from,to,dashBoard,"4 week",3);

		//Data for year
		dashBoard.SelectTimePeriod(4);
		from = Helper.getDateBeforeOrAfterYears(-1,"yyyy-MM-dd");
		to = Helper.getDateBeforeOrAfterDays(1,"yyyy-MM-dd");
		checkingRoutine(testConfig,from,to,dashBoard,"year",4);


		checkTodaysLink(testConfig,dashBoard);
		checkThisWeeksLink(testConfig,dashBoard);

		Assert.assertTrue(testConfig.getTestResult());

	}


	@Test(description = "Verify retry framework", 
			dataProvider="GetTwoBrowserTestConfig", timeOut=600000, groups="1")
	public void test_verifyFramework(Config testConfigDashboard,Config testConfigTransaction)
	{
		int testRow = 1; 
		DashboardHelper dashBoardHelper = new DashboardHelper(testConfigDashboard);
		dashBoard = dashBoardHelper.doMerchantLogin(testRow);

		//check retries, unique retries and retries successful count
		int retries = Integer.parseInt(dashBoard.getRetries());
		int retriesUniqueUsers = Integer.parseInt(dashBoard.getRetriesUniqueUsers());
		int retriesSuccessful = Integer.parseInt(dashBoard.getRetriesSuccesful());


		TransactionHelper transactionHelper = new TransactionHelper(testConfigTransaction, false);
		transactionHelper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum = 3;
		int wrongCardDetailsRowNum = 22;
		int rightCardDetailsRowNum = 1;

		//retry transaction once
		transactionHelper.GetTestTransactionPage();
		transactionHelper.tryAgainPage = (TryAgainPage) transactionHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum,wrongCardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
		Browser.wait(testConfigDashboard, 2);
		CCTab ccTab = (CCTab) transactionHelper.tryAgainPage.clickTryAgainButton();
		Browser.wait(testConfigDashboard, 2);

		Browser.browserRefresh(testConfigDashboard);
		//check retries, unique retries and retries successful count after one retry
		int newRetries = Integer.parseInt(dashBoard.getRetries());
		int newRetriesUniqueUsers = Integer.parseInt(dashBoard.getRetriesUniqueUsers());
		int newRetriesSuccessful = Integer.parseInt(dashBoard.getRetriesSuccesful());

		//compare retries, unique retries and retries successful count after one retry
		Helper.compareEquals(testConfigDashboard, "Data After one failed transaction : retries",retries,newRetries);
		Helper.compareEquals(testConfigDashboard, "Data After one failed transaction : retriesUniqueUsers",retriesUniqueUsers,newRetriesUniqueUsers);
		Helper.compareEquals(testConfigDashboard, "Data After one failed transaction : retriesSuccessful",retriesSuccessful,newRetriesSuccessful);

		retries = newRetries;
		retriesUniqueUsers = newRetriesUniqueUsers;
		retriesSuccessful =newRetriesSuccessful;

		// retry transaction 2 times
		transactionHelper.GetTestTransactionPage();
		transactionHelper.tryAgainPage = (TryAgainPage) transactionHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum,wrongCardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
		Browser.wait(testConfigDashboard, 2);
		ccTab = (CCTab) transactionHelper.tryAgainPage.clickTryAgainButton();
		Browser.wait(testConfigDashboard, 2);


		ccTab.FillCardDetails(wrongCardDetailsRowNum);
		transactionHelper.tryAgainPage = (TryAgainPage) transactionHelper.payment.clickPayNowToGetTryAgainPage();
		Browser.wait(testConfigDashboard, 2);
		ccTab = (CCTab) transactionHelper.tryAgainPage.clickTryAgainButton();
		Browser.wait(testConfigDashboard, 2);

		Browser.browserRefresh(testConfigDashboard);
		//check retries, unique retries and retries successful count after two retries
		newRetries = Integer.parseInt(dashBoard.getRetries());
		newRetriesUniqueUsers = Integer.parseInt(dashBoard.getRetriesUniqueUsers());
		newRetriesSuccessful = Integer.parseInt(dashBoard.getRetriesSuccesful());

		//compare retries, unique retries and retries successful count after one retry
		Helper.compareEquals(testConfigDashboard, "Data After one failed transaction and failed retry : retries",(retries+1),newRetries);
		Helper.compareEquals(testConfigDashboard, "Data After one failed transaction and failed retry: retriesUniqueUsers",(retriesUniqueUsers+1),newRetriesUniqueUsers);
		Helper.compareEquals(testConfigDashboard, "Data After one failed transaction and failed retry: retriesSuccessful",retriesSuccessful,newRetriesSuccessful);

		retries = newRetries;
		retriesUniqueUsers = newRetriesUniqueUsers;
		retriesSuccessful =newRetriesSuccessful;

		transactionHelper.GetTestTransactionPage();
		transactionHelper.tryAgainPage = (TryAgainPage) transactionHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum,wrongCardDetailsRowNum, ExpectedResponsePage.TryAgainPage);
		Browser.wait(testConfigDashboard, 2);
		ccTab = (CCTab) transactionHelper.tryAgainPage.clickTryAgainButton();
		Browser.wait(testConfigDashboard, 2);

		ccTab.FillCardDetails(rightCardDetailsRowNum);
		transactionHelper.testResponse = (TestResponsePage) transactionHelper.payment.clickPayNow();
		Browser.wait(testConfigDashboard, 2);

		Browser.browserRefresh(testConfigDashboard);
		newRetries = Integer.parseInt(dashBoard.getRetries());
		newRetriesUniqueUsers = Integer.parseInt(dashBoard.getRetriesUniqueUsers());
		newRetriesSuccessful = Integer.parseInt(dashBoard.getRetriesSuccesful());

		Helper.compareEquals(testConfigDashboard, "Data After one failed transaction and successful retry: retries",(retries+1),newRetries);
		Helper.compareEquals(testConfigDashboard, "Data After one failed transaction and successful retry: retriesUniqueUsers",(retriesUniqueUsers+1),newRetriesUniqueUsers);
		Helper.compareEquals(testConfigDashboard, "Data After one failed transaction and successful retry: retriesSuccessful",(retriesSuccessful+1),newRetriesSuccessful);

		testConfigTransaction.closeBrowser();

		Assert.assertTrue(testConfigDashboard.getTestResult());	
	}

	//Right hand side links

	private void checkTodaysLink(Config testConfig,DashboardPage dashBoard) {
		String href = dashBoard.getTransactionTodayLink().getAttribute("href");
		href = href.substring(href.indexOf("merchant"));
		String hrefFormed = "merchant/transactions?from="+Helper.getCurrentDate("dd/MM/yyyy")+"&to="+Helper.getCurrentDate("dd/MM/yyyy");
		Helper.compareEquals(testConfig, "Testing today's Link",hrefFormed, href);
		Element.click(testConfig, dashBoard.getTransactionTodayLink(), "Today's Transactions") ;
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		Element.click(testConfig, merchantTransaction.getDashboardLink(), "going back") ;
		dashBoard = new DashboardPage(testConfig);

	}

	private void checkThisWeeksLink(Config testConfig,DashboardPage dashBoard) {

		String href = dashBoard.getTransactionThisWeekLink().getAttribute("href");
		href = href.substring(href.indexOf("merchant"));
		String hrefFormed = "merchant/transactions?from="+Helper.getThisWeekStartingDate("dd/MM/yyyy")+"&to="+Helper.getThisWeekEndingDate("dd/MM/yyyy");
		Helper.compareEquals(testConfig, "Testing this week's Link",hrefFormed, href);
		Element.click(testConfig, dashBoard.getTransactionThisWeekLink(), "This Week's Transactions") ;
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		Element.click(testConfig, merchantTransaction.getDashboardLink(), "going back") ;
		dashBoard = new DashboardPage(testConfig);

	}

	// initiated Transactions link
	private void checkInitiatedTransactionLink(Config testConfig,DashboardPage dashBoard,String what,int timePeriod) {
		String initiatedTransactionsString =  dashBoard.getInitiatedTransactions();
		String initiatedTransactions ="";
		if(initiatedTransactionsString.length()>0)
			initiatedTransactions = initiatedTransactionsString.substring(0,initiatedTransactionsString.indexOf("\n"));
		if(!initiatedTransactions.equals("0")){
			Element.click(testConfig, dashBoard.getInititatedTransactionLink(), what+"'s initiated Transactions") ;
			MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
			Helper.compareEquals(testConfig, "InitiatedTransaction Link",initiatedTransactions,merchantTransaction.getNumberRecords());
			goBack(testConfig,merchantTransaction,timePeriod);
		}

	}


	private void checkBouncedTransactionLink(Config testConfig,DashboardPage dashBoard,String what,int timePeriod) {
		String bouncedTransactionsString =  dashBoard.getBouncedNumber();
		String bouncedTransactions = "";
		if(bouncedTransactionsString.length()>0)
			bouncedTransactions = bouncedTransactionsString;

		String bounced_overAll_TransactionsString =  dashBoard.getBouncedNumber_overAll().getText();
		String bounced_overAll_Transaction ="";
		if(bounced_overAll_TransactionsString.length()>0)
			bounced_overAll_Transaction = bounced_overAll_TransactionsString.substring(0,bounced_overAll_TransactionsString.indexOf("\n"));

		Helper.compareEquals(testConfig, "Equating overall and unsuccessful bouced",bouncedTransactions, bounced_overAll_Transaction);

		if(!bouncedTransactions.equals("0")){
			Element.click(testConfig, dashBoard.getBouncedTransactionLink(), what+"'s bounced Transactions") ;
			MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
			Helper.compareEquals(testConfig, "Testing BouncedTransaction Link",bouncedTransactions,merchantTransaction.getNumberRecords());
			goBack(testConfig,merchantTransaction,timePeriod);

		}
	}


	// dropped Transactions link
	private void checkDroppedTransactionLink(Config testConfig,DashboardPage dashBoard,String what,int timePeriod) {
		String droppedTransactionsString =  dashBoard.getDroppedNumber();
		String droppedTransactions = "";
		if(droppedTransactionsString.length()>0)
			droppedTransactions = droppedTransactionsString;

		String dropped_overAll_TransactionsString =  dashBoard.getDroppedNumber_overAll().getText();
		String dropped_overAll_Transaction ="";
		if(dropped_overAll_TransactionsString.length()>0)
			dropped_overAll_Transaction = dropped_overAll_TransactionsString.substring(0,dropped_overAll_TransactionsString.indexOf("\n"));

		Helper.compareEquals(testConfig, "Equating overall and unsuccessful dropped",droppedTransactions, dropped_overAll_Transaction);

		if(!droppedTransactions.equals("0")){
			Element.click(testConfig, dashBoard.getDroppedTransactionLink(), what+"'s dropped Transactions") ;
			MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
			Helper.compareEquals(testConfig, "Testing Dropped Link",droppedTransactions,merchantTransaction.getNumberRecords());
			goBack(testConfig,merchantTransaction,timePeriod);
		}
	}

	// failed Transactions link
	private void checkFailedTransactionLink(Config testConfig,DashboardPage dashBoard,String what, int timePeriod) {
		String failedTransactionsString =  dashBoard.getFailedNumber();
		String failedTransactions = "";
		if(failedTransactionsString.length()>0)
			failedTransactions = failedTransactionsString.substring(0, failedTransactionsString.indexOf("\n"));

		String failed_overAll_TransactionsString =  dashBoard.getFailedNumber_overAll().getText();
		String failed_overAll_Transaction ="";
		if(failed_overAll_TransactionsString.length()>0)
			failed_overAll_Transaction = failed_overAll_TransactionsString.substring(0,failed_overAll_TransactionsString.indexOf("\n"));

		Helper.compareEquals(testConfig, "Equating overall and unsuccessful failed",failedTransactions,failed_overAll_Transaction);

		if(!failedTransactions.equals("0")){
			Element.click(testConfig, dashBoard.getFailedTransactionLink(), what+"'s failed Transactions") ;
			MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
			Helper.compareEquals(testConfig, "Testing FailedLink",failedTransactions,merchantTransaction.getNumberRecords());
			goBack(testConfig,merchantTransaction,timePeriod);
		}
	}

	//go back to dashboard
	private void goBack(Config testConfig,
			MerchantTransactionsPage merchantTransaction, int timePeriod) {
		Element.click(testConfig, merchantTransaction.getDashboardLink(), "going back") ;
		dashBoard = new DashboardPage(testConfig);
		if(timePeriod!=0){
			dashBoard.SelectTimePeriod(timePeriod);
		}
	}


	//succesful Transactions link
	private void checkSuccesfulTransactionLink(Config testConfig,DashboardPage dashBoard,String what, int timePeriod) {

		String succesfulTransactionsString =  dashBoard.getSuccessfulNumber();
		String succesfulTransactions = "";
		if( succesfulTransactionsString.length()>0)
			succesfulTransactions =  succesfulTransactionsString.substring(0, succesfulTransactionsString.indexOf("\n"));

		if(!succesfulTransactions.equals("0")){
			Element.click(testConfig, dashBoard.getSuccessfulTransactionLink(), what+"'s succesful Transactions") ;
			MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
			Helper.compareEquals(testConfig, "Testing Successful Link",succesfulTransactions,merchantTransaction.getNumberRecords());
			goBack(testConfig,merchantTransaction,timePeriod);
		}
	}

	//unique Transactions link
	private void checkUniqueTransactionLink(Config testConfig,DashboardPage dashBoard,String what, int timePeriod) {
		String uniqueTransactionsString =  dashBoard.getUniqueNumber();
		String uniqueTransactions = "";
		if( uniqueTransactionsString.length()>0)
			uniqueTransactions =  uniqueTransactionsString.substring(0, uniqueTransactionsString.indexOf("\n"));


		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(1, "merchantid");
		testConfig.putRunTimeProperty("merchantid", value);
		testConfig.putRunTimeProperty("statusString", "'in progress','initiated'");
		testConfig.putRunTimeProperty("uniquenessString", "104,105,106");
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 58, 1);
		int count = Integer.parseInt(map.get("count"));

		testConfig.putRunTimeProperty("statusString", "'auth','captured','cancelled'");
		map = DataBase.executeSelectQuery(testConfig, 57, 1);
		count += Integer.parseInt(map.get("count"));

		testConfig.putRunTimeProperty("statusString", "'userCancelled','bounced'");
		testConfig.putRunTimeProperty("uniquenessString", "0");
		map = DataBase.executeSelectQuery(testConfig, 59, 1);
		count += Integer.parseInt(map.get("count"));

		Helper.compareEquals(testConfig, what, count+"", uniqueTransactions);	

		if(!uniqueTransactions.equals("0")){
			Element.click(testConfig, dashBoard.getUniqueTransactionLink(), what+"'s unique Transactions") ;
			MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
			Helper.compareEquals(testConfig, "Testing Unique Transaction Link",uniqueTransactions,merchantTransaction.getNumberRecords());
			goBack(testConfig,merchantTransaction,timePeriod);
		}
	}

	// unique bounced Transactions link
	private void checkUniqueBouncedTransactionLink(Config testConfig,DashboardPage dashBoard,String what, int timePeriod) {
		String bouncedUniqueTransactionsString =  dashBoard.getUniqueBouncesNumber();
		String bouncedUniqueTransactions = "";
		if(bouncedUniqueTransactionsString.length()>0)
			bouncedUniqueTransactions = bouncedUniqueTransactionsString.substring(0,bouncedUniqueTransactionsString.indexOf("\n"));

		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(1, "merchantid");
		testConfig.putRunTimeProperty("merchantid", value);
		testConfig.putRunTimeProperty("statusString", "'in progress','initiated'");
		testConfig.putRunTimeProperty("uniquenessString", "106");
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 58, 1);
		int count = Integer.parseInt(map.get("count"));

		testConfig.putRunTimeProperty("statusString", "'userCancelled','bounced'");
		testConfig.putRunTimeProperty("uniquenessString", "0");
		map = DataBase.executeSelectQuery(testConfig,59 , 1);
		count += Integer.parseInt(map.get("count"));
		Helper.compareEquals(testConfig, what, count+"",bouncedUniqueTransactions);

		if(!bouncedUniqueTransactions.equals("0")){
			Element.click(testConfig, dashBoard.getUniqueBounceTransactionLink(), what+"'s unique bounced Transactions") ;
			MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
			Helper.compareEquals(testConfig, "Testing Unique Bounced Link",bouncedUniqueTransactions,merchantTransaction.getNumberRecords());
			goBack(testConfig,merchantTransaction,timePeriod);
		}
	}

	//unique dropped Transactions link
	private void checkUniqueDroppedTransactionLink(Config testConfig,DashboardPage dashBoard,String what, int timePeriod) {
		String droppedUniqueTransactionsString =  dashBoard.getUniquedroppedNumber();
		String droppedUniqueTransactions = "";
		if(droppedUniqueTransactionsString.length()>0)
			droppedUniqueTransactions = droppedUniqueTransactionsString.substring(0,droppedUniqueTransactionsString.indexOf("\n"));

		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(1, "merchantid");
		testConfig.putRunTimeProperty("merchantid", value);
		testConfig.putRunTimeProperty("statusString", "'in progress','initiated'");
		testConfig.putRunTimeProperty("uniquenessString", "105");
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 58, 1);
		int count = Integer.parseInt(map.get("count"));
		Helper.compareEquals(testConfig, what, count+"",droppedUniqueTransactions);

		if(!droppedUniqueTransactions.equals("0")){
			Element.click(testConfig, dashBoard.getUniqueDroppedTransactionLink(), what+"'s unique dropped Transactions") ;
			MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
			Helper.compareEquals(testConfig, "Testing Unique Dropped Link",droppedUniqueTransactions,merchantTransaction.getNumberRecords());
			goBack(testConfig,merchantTransaction,timePeriod);
		}
	}

	// unique failed Transactions link
	private void checkUniqueFailedTransactionLink(Config testConfig,DashboardPage dashBoard,String what, int timePeriod) {
		String uniqueFailedTransactionsString =  dashBoard.getUniqueFailedNumber();
		String uniqueFailedTransactions = "";
		if(uniqueFailedTransactionsString.length()>0)
			uniqueFailedTransactions = uniqueFailedTransactionsString.substring(0,uniqueFailedTransactionsString.indexOf("\n"));

		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(1, "merchantid");
		testConfig.putRunTimeProperty("merchantid", value);
		testConfig.putRunTimeProperty("statusString", "'in progress','initiated'");
		testConfig.putRunTimeProperty("uniquenessString", "104");
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 58, 1);
		int count = Integer.parseInt(map.get("count"));
		Helper.compareEquals(testConfig, what, count+"",uniqueFailedTransactions);


		if(!uniqueFailedTransactions.equals("0")){
			Element.click(testConfig, dashBoard.getUniqueFailedTransactionLink(), what+"'s unique failed Transactions") ;
			MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
			Helper.compareEquals(testConfig, "Testing Unique Failed Link",uniqueFailedTransactions,merchantTransaction.getNumberRecords());
			goBack(testConfig,merchantTransaction,timePeriod);
		}
	}

	// unique succesful Transactions link
	private void checkUniqueSuccesfulTransactionLink(Config testConfig,DashboardPage dashBoard,String what, int timePeriod) {
		String uniqueSuccesfulTransactionsString =  dashBoard.getUniqueSuccessfulNumber();
		String succesfulUniqueTransactions = "";
		if( uniqueSuccesfulTransactionsString.length()>0)
			succesfulUniqueTransactions = uniqueSuccesfulTransactionsString.substring(0, uniqueSuccesfulTransactionsString.indexOf("\n"));

		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(1, "merchantid");
		testConfig.putRunTimeProperty("merchantid", value);
		testConfig.putRunTimeProperty("statusString", "'auth','captured','cancelled'");
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 57, 1);
		int count = Integer.parseInt(map.get("count"));
		Helper.compareEquals(testConfig, what, count+"",succesfulUniqueTransactions);


		if(!succesfulUniqueTransactions.equals("0")){
			Element.click(testConfig, dashBoard.getUniqueSuccessfulTransactionLink(), what+"'s unique succesful Transactions") ;
			MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
			Helper.compareEquals(testConfig, "Testing this week's Link",succesfulUniqueTransactions,merchantTransaction.getNumberRecords());
			goBack(testConfig,merchantTransaction,timePeriod);
		}
	}




	//Group of test for each of the time periods
	private void checkingRoutine(Config testConfig, String from, String to,
			DashboardPage dashBoard, String string,int timePeriod) {

		compareAmount(testConfig,from,to,dashBoard,"Checking "+ string +"'s amount");
		compareTransaction(testConfig,from,to,dashBoard,"Checking "+ string +"'s num");
		compareAvgAmountperTransaction(testConfig,from,to,dashBoard,"Checking "+ string +"'s num");
		compareSuccessRate(testConfig,from,to,dashBoard,"Checking "+ string +"'s successRate");
		compareBounceRate(testConfig, from, to, dashBoard, "Checking "+ string +"'s bounceRate");
		compareDroppedRate(testConfig, from, to, dashBoard, "Checking "+ string +"'s droppedRate");
		compareFailedByBankRate(testConfig, from, to, dashBoard, "Checking "+ string +"'s failedByBankRate");
		compareAmount2Sides(testConfig,dashBoard,"Checking "+ string +"'s amount left and right side");
		checkInitiatedTransactionLink(testConfig,dashBoard,"Checking "+ string +"'s Initiated transaction's link",timePeriod);
		checkBouncedTransactionLink(testConfig, dashBoard, "Checking "+ string +"'s Bounced transaction's link",timePeriod);
		checkDroppedTransactionLink(testConfig, dashBoard, "Checking "+ string +"'s Dropped transaction's link",timePeriod);
		checkFailedTransactionLink(testConfig, dashBoard, "Checking "+ string +"'s Failed transaction's link",timePeriod);
		checkSuccesfulTransactionLink(testConfig, dashBoard, "Checking "+ string +"'s Successful transaction's link",timePeriod);
		checkUniqueBouncedTransactionLink(testConfig, dashBoard, "Checking "+ string +"'s Unique Bounced transaction's link",timePeriod);
		checkUniqueDroppedTransactionLink(testConfig, dashBoard, "Checking "+ string +"'s Unique Dropped transaction's link",timePeriod);
		checkUniqueFailedTransactionLink(testConfig, dashBoard, "Checking "+ string +"'s Unique Failed transaction's link",timePeriod);
		checkUniqueSuccesfulTransactionLink(testConfig, dashBoard, "Checking "+ string +"'s Unique Successful transaction's link",timePeriod);
		checkUniqueTransactionLink(testConfig, dashBoard, "Checking "+ string +"'s Unique transaction's link",timePeriod);
	}

	public void compareAmount2Sides(Config testConfig,DashboardPage dashBoard,String what){
		Helper.compareEquals(testConfig, what,"Rs. "+dashBoard.getAmount().substring(0, dashBoard.getAmount().length()-2)+" /-", dashBoard.getRightSideAmount());
	}

	public void compareAmount(Config testConfig,String from,String to,DashboardPage dashBoard,String what){
		testConfig.putRunTimeProperty("from", from);
		testConfig.putRunTimeProperty("to", to);
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(1, "merchantid");
		testConfig.putRunTimeProperty("merchantid", value);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 9, 1);
		Helper.compareEquals(testConfig, what, (map.get("totalAmount")).toString()+"/-", dashBoard.getAmount());
	}


	public void compareTransaction(Config testConfig,String from,String to,DashboardPage dashBoard,String what){
		testConfig.putRunTimeProperty("from", from);
		testConfig.putRunTimeProperty("to", to);
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(1, "merchantid");
		testConfig.putRunTimeProperty("merchantid", value);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 10, 1);
		Helper.compareEquals(testConfig, what, map.get("totalNum"), dashBoard.getTransactionNumber());

	}

	public void compareAvgAmountperTransaction(Config testConfig,String from,String to,DashboardPage dashBoard,String what){
		String amountString =  dashBoard.getAmount();
		Double amount = 0.0;
		if(amountString.length()>2)
			amount = Double.parseDouble(amountString.substring(0,amountString.indexOf("/")));
		String numString =  dashBoard.getTransactionNumber();
		Double num = Double.parseDouble(numString);
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		Double avg = 0.0;
		if(num!=0)
			avg = Double.valueOf(twoDForm.format(amount/num));
		Helper.compareEquals(testConfig, what, avg.toString()+"/-", dashBoard.getAvgAmountperTransaction());

	}

	public void compareSuccessRate(Config testConfig,String from,String to,DashboardPage dashBoard,String what){

		String revertedTransactionsString =  dashBoard.getRevertedTransactions();
		Integer revertedTransactions = 0;
		if(revertedTransactionsString.length()>0)
			revertedTransactions = Integer.parseInt(revertedTransactionsString.substring(0,revertedTransactionsString.indexOf("\n")));

		String successTransactionsString =  dashBoard.getSuccessfulTransactions();
		Integer successTransactions = 0;
		if(successTransactionsString.length()>0)
			successTransactions = Integer.parseInt(successTransactionsString.substring(0,successTransactionsString.indexOf("\n")));

		testConfig.putRunTimeProperty("from", from);
		testConfig.putRunTimeProperty("to", to);
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(1, "merchantid");
		testConfig.putRunTimeProperty("merchantid", value);
		testConfig.putRunTimeProperty("statusString", "'auth','captured','cancelled'");
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 57, 1);
		Helper.compareEquals(testConfig, what+"from db", map.get("count"), successTransactions+"");



		Float calculatedPercentage = (float) 0.0;
		if(revertedTransactions!=0){
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			calculatedPercentage = Float.valueOf(twoDForm.format(((float)successTransactions*100/revertedTransactions)));
		}

		String dashboardStrings = (dashBoard.getSuccessfulPercentage().split("\n"))[1];
		Helper.compareEquals(testConfig, what, calculatedPercentage, Float.parseFloat(dashboardStrings.substring(0, dashboardStrings.length()-1)));

	}



	public void compareBounceRate(Config testConfig,String from,String to,DashboardPage dashBoard,String what){

		String initiatedTransactionsString =  dashBoard.getInitiatedTransactions();
		Integer initiatedTransactions = 0;
		if(initiatedTransactionsString.length()>0)
			initiatedTransactions = Integer.parseInt(initiatedTransactionsString.substring(0,initiatedTransactionsString.indexOf("\n")));

		String successTransactionsString =  dashBoard.getSuccessfulTransactions();
		Integer successTransactions = 0;
		if(successTransactionsString.length()>0)
			successTransactions = Integer.parseInt(successTransactionsString.substring(0,successTransactionsString.indexOf("\n")));

		Integer failedTransactions = initiatedTransactions - successTransactions;

		String bouncedTransactionsString =  dashBoard.getBouncedNumber();
		Integer bouncedTransactions = 0;
		if(bouncedTransactionsString.length()>0)
			bouncedTransactions = Integer.parseInt(bouncedTransactionsString);

		testConfig.putRunTimeProperty("from", from);
		testConfig.putRunTimeProperty("to", to);
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(1, "merchantid");
		testConfig.putRunTimeProperty("merchantid", value);
		testConfig.putRunTimeProperty("statusString", "'userCancelled','bounced'");
		testConfig.putRunTimeProperty("uniquenessString", "2,107");
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 56, 1);
		Helper.compareEquals(testConfig, what+"from db", map.get("count"), bouncedTransactions+"");

		Float calculatedPercentage = (float) 0.0;
		if(failedTransactions!=0){
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			calculatedPercentage = Float.valueOf(twoDForm.format(((float)bouncedTransactions*100/failedTransactions)));
		}

		Helper.compareEquals(testConfig, what, calculatedPercentage, Float.parseFloat(dashBoard.getBouncedPercentage().substring(0, dashBoard.getBouncedPercentage().length()-1)));
	}

	public void compareDroppedRate(Config testConfig,String from,String to,DashboardPage dashBoard,String what){
		String initiatedTransactionsString =  dashBoard.getInitiatedTransactions();
		Integer initiatedTransactions = 0;
		if(initiatedTransactionsString.length()>0)
			initiatedTransactions = Integer.parseInt(initiatedTransactionsString.substring(0,initiatedTransactionsString.indexOf("\n")));

		String successTransactionsString =  dashBoard.getSuccessfulTransactions();
		Integer successTransactions = 0;
		if(successTransactionsString.length()>0)
			successTransactions = Integer.parseInt(successTransactionsString.substring(0,successTransactionsString.indexOf("\n")));

		Integer failedTransactions = initiatedTransactions - successTransactions;

		String droppedTransactionsString =  dashBoard.getDroppedNumber();
		Integer droppedTransactions = 0;
		if(droppedTransactionsString.length()>0)
			droppedTransactions = Integer.parseInt(droppedTransactionsString);

		testConfig.putRunTimeProperty("from", from);
		testConfig.putRunTimeProperty("to", to);
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(1, "merchantid");
		testConfig.putRunTimeProperty("merchantid", value);
		testConfig.putRunTimeProperty("statusString", "'autoRefund','dropped'");
		testConfig.putRunTimeProperty("uniquenessString", "2,107");
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 56, 1);
		Helper.compareEquals(testConfig, what, map.get("count"), droppedTransactions+"");

		Float calculatedPercentage = (float) 0.0;
		if(failedTransactions!=0){
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			calculatedPercentage = Float.valueOf(twoDForm.format(((float)droppedTransactions*100/failedTransactions)));
		}

		Helper.compareEquals(testConfig, what, calculatedPercentage, Float.parseFloat(dashBoard.getDroppedPercentage().substring(0, dashBoard.getDroppedPercentage().length()-1)));
	}

	public void compareFailedByBankRate(Config testConfig,String from,String to,DashboardPage dashBoard,String what){
		String initiatedTransactionsString =  dashBoard.getInitiatedTransactions();
		Integer initiatedTransactions = 0;
		if(initiatedTransactionsString.length()>0)
			initiatedTransactions = Integer.parseInt(initiatedTransactionsString.substring(0,initiatedTransactionsString.indexOf("\n")));

		String successTransactionsString =  dashBoard.getSuccessfulTransactions();
		Integer successTransactions = 0;
		if(successTransactionsString.length()>0)
			successTransactions = Integer.parseInt(successTransactionsString.substring(0,successTransactionsString.indexOf("\n")));

		Integer failedTransactions = initiatedTransactions - successTransactions;

		String failedByBankTransactionsString =  dashBoard.getFailedByBankNumber();
		Integer failedByBankTransactions = 0;
		if(failedByBankTransactionsString.length()>0)
			failedByBankTransactions = Integer.parseInt(failedByBankTransactionsString);

		testConfig.putRunTimeProperty("from", from);
		testConfig.putRunTimeProperty("to", to);
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(1, "merchantid");
		testConfig.putRunTimeProperty("merchantid", value);
		testConfig.putRunTimeProperty("statusString", "'failed'");
		testConfig.putRunTimeProperty("uniquenessString", "2,107");
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 56, 1);
		Helper.compareEquals(testConfig, what, map.get("count"), failedByBankTransactions+"");	


		Float calculatedPercentage = (float) 0.0;
		if(failedTransactions!=0){
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			calculatedPercentage = Float.valueOf(twoDForm.format(((float)failedByBankTransactions*100/failedTransactions)));
		}

		Helper.compareEquals(testConfig, what, calculatedPercentage, Float.parseFloat(dashBoard.getFailedByBankPercentage().substring(0, dashBoard.getFailedByBankPercentage().length()-1)));
	}





}
