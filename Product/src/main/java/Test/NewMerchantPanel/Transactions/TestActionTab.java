package Test.NewMerchantPanel.Transactions;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage.filterTypes;
import Test.NewMerchantPanel.Transactions.TransactionsHelper;
import Test.NewMerchantPanel.Transactions.TransactionsHelper.filterBy;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage.dropDownAction;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage.filterByOrderStatus;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Browser;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestActionTab extends TestBase {

	private MerchantPanelPage merchantPage;
	private TransactionsPage transactionPage;
	private TransactionsHelper transactionHelper;
	String refId;

	/**
	 * Verify Transaction Refund
	 * @param testConfig
	 */
	@Test(description = "Verify Transaction Refund", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyTransacionRefund(Config testConfig) {

		int transactionRowNum = 157;
		int paymentTypeRowNum =3;
		int cardDetailsRowNum = 1;
		String amount="1.00";
		
	
		// perform the transaction and verify the status
		TransactionHelper transHelper = new TransactionHelper(testConfig, false);
		transHelper.DoLogin();
		transHelper.GetTestTransactionPage();

		transHelper.testResponse = (TestResponsePage) transHelper
				.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
						ExpectedResponsePage.TestResponsePage);
		transHelper.testResponse.VerifyTransactionResponse(new TestDataReader(
				testConfig, "TransactionDetails"), transactionRowNum, new TestDataReader(
				testConfig, "PaymentType"), paymentTypeRowNum);

		String txnID = testConfig.getRunTimeProperty("txnId");
		TestDataReader reader=new TestDataReader(testConfig, "StringValues");
		DashboardHelper helper = new DashboardHelper(testConfig);
		merchantPage = helper.doMerchantLogin(transactionRowNum);
		testConfig.putRunTimeProperty("merchantID", "6912");
		transactionPage = merchantPage.ClickTransactionsTab();
		transactionHelper=new TransactionsHelper(testConfig);
		transactionHelper.performRefundAction(amount, txnID,reader.GetData(16, "Value"),"Refunded","0.00");
				
		//popup message for already refunded transactons
		Browser.wait(testConfig, 2);
		transactionHelper.searchDataByFilterType(filterTypes.TransactionID, txnID);
		transactionPage.selectFirstCheckBox();
		transactionPage.selectDropDownAction(dropDownAction.Refund);
		//transactionPage.clickProcessButton();
		transactionPage.verifyWrongActionMessage(reader.GetData(16, "Value"),reader.GetData(17, "Value"));
		
		Assert.assertTrue(testConfig.getTestResult());

	}

	/**
	 * COD verify transaction in merchant panel
	 * @param testConfig
	 */
	@Test(description = "COD verify transaction in merchant panel", dataProvider = "GetTestConfig", groups = "1")
	public void verifyCOD(Config testConfig)  {


		int transactionRowNum = 159;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = -1;
		int requestQueryRowNum=4;
		int codRowNum = 1;		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		helper.testResponse =(TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.TestResponsePage);
		String transactionID=testConfig.getRunTimeProperty("txnId");
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		merchantPage=dashboardHelper.doMerchantLogin(transactionRowNum);
		transactionPage=merchantPage.ClickTransactionsTab();
		transactionHelper=new TransactionsHelper(testConfig);
		transactionPage.verifyTransactionStatus(transactionID, "In Progress",1);
		TestDataReader reader=new TestDataReader(testConfig, "StringValues");
		
		//checking the refund message on In Progress
		transactionHelper.searchDataByFilterType(filterTypes.TransactionID, transactionID);
		transactionHelper.selectCheckBoxActionType(dropDownAction.Refund);
		transactionPage.verifyWrongActionMessage(reader.GetData(16, "Value"),reader.GetData(17, "Value"));
		
		//perform settle actions
		transactionHelper.searchDataByFilterType(filterTypes.TransactionID, transactionID);
		transactionPage.performCODActions( dropDownAction.CODVerify, "processCodVerify.php",1);
		transactionPage.verifyTransactionStatus(transactionID, "Pending",1);
		
		//checking the refund message on Pending		
		transactionHelper.searchDataByFilterType(filterTypes.TransactionID, transactionID);
		transactionHelper.selectCheckBoxActionType(dropDownAction.Refund);
		transactionPage.verifyWrongActionMessage(reader.GetData(16, "Value"),reader.GetData(17, "Value"));
		
		transactionHelper.searchDataByFilterType(filterTypes.TransactionID, transactionID);
		transactionPage.performCODActions(dropDownAction.CODSettle, "processCodSettled.php",1);
		transactionHelper.verifyStatusInDB(requestQueryRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	/**
	 * COD verify transaction in merchant panel
	 * @param testConfig
	 */
	@Test(description = "COD verify transaction in merchant panel", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void verifyCODCancel(Config testConfig)  {


		int transactionRowNum = 159;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = -1;
		int requestQueryRowNum=4;
		int codRowNum = 1;		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		helper.testResponse =(TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.TestResponsePage);
		String transactionID=testConfig.getRunTimeProperty("txnId");
		
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		merchantPage=dashboardHelper.doMerchantLogin(transactionRowNum);
	
		
		transactionPage=merchantPage.ClickTransactionsTab();
		transactionHelper=new TransactionsHelper(testConfig);
		transactionPage.verifyTransactionStatus(transactionID, "In Progress",1);
		transactionPage.performCODActions(dropDownAction.CODCancel, "processCodCancel.php",1);
		transactionHelper.verifyStatusInDB(requestQueryRowNum);
		
		//verify refund message on failed transaction
		TestDataReader reader=new TestDataReader(testConfig, "StringValues");
		transactionHelper.searchDataByFilterType(filterTypes.TransactionID, transactionID);
		transactionPage.selectFirstCheckBox();
		transactionPage.selectDropDownAction(dropDownAction.Refund);
		transactionPage.verifyWrongActionMessage(reader.GetData(16, "Value"),reader.GetData(17, "Value"));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	/**
	 * To verify Transaction details based on testdata
	 * @param testConfig
	 */
	@Test(description = "To verify Transaction details based on testdata", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyTransactionDetailsWithTestData(Config testConfig) {
		
		int transactionRowNum = 157;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;

		// perform the transaction and verify the status
		TransactionHelper transHelper = new TransactionHelper(testConfig, false);
		transHelper.DoLogin();
		transHelper.GetTestTransactionPage();

		transHelper.testResponse = (TestResponsePage) transHelper
				.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
						ExpectedResponsePage.TestResponsePage);
		transHelper.testResponse.VerifyTransactionResponse(new TestDataReader(
				testConfig, "TransactionDetails"), transactionRowNum, new TestDataReader(
				testConfig, "PaymentType"), paymentTypeRowNum);
		// login into the Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		merchantPage = helper.doMerchantLogin(transactionRowNum);
		transactionHelper=new TransactionsHelper(testConfig);
		testConfig.putRunTimeProperty("merchantID", "6912");
		transactionPage = merchantPage.ClickTransactionsTab();
		transactionHelper.filterAndCompareDetailsWithTestData(filterTypes.TransactionID, transactionRowNum, testConfig.getRunTimeProperty("txnId"));
		Assert.assertTrue(testConfig.getTestResult());

	}

	/**
	 * To verify Transaction refund status 
	 * @param testConfig
	 */
	@Test(description = "To verify Transaction refund status ", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyTransactionsRefundStatus(Config testConfig) {

		int transactionRowNum = 157;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int requestQueryRowNum=120;
		// perform the transaction and verify the status
		TransactionHelper transHelper = new TransactionHelper(testConfig, false);
		transHelper.DoLogin();
		transHelper.GetTestTransactionPage();

		transHelper.testResponse = (TestResponsePage) transHelper
				.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
						ExpectedResponsePage.TestResponsePage);
		transHelper.testResponse.VerifyTransactionResponse(new TestDataReader(
				testConfig, "TransactionDetails"), transactionRowNum, new TestDataReader(
				testConfig, "PaymentType"), paymentTypeRowNum);

		String[] txnID = {testConfig.getRunTimeProperty("txnId")};

		// login into the Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		merchantPage = helper.doMerchantLogin(transactionRowNum);

		testConfig.putRunTimeProperty("merchantID", "6912");
		transactionPage = merchantPage.ClickTransactionsTab();
		TransactionsHelper tranHelper=new TransactionsHelper(testConfig);
		
		merchantPage.filterBasedOn(filterTypes.TransactionID);
		tranHelper.filterDataAndVerifyWithDB(txnID,filterBy.TransactionID,requestQueryRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());

	}
	

	/**
	 * Product-1629:Verify_Select Action_All Transactions_Action_Incorret Refunds_Popup messages
	 * Product-1568:Verify_Select Action_Popup_ No_Selection
	 *   Product-1570:Verify_Select Action_Popup_Wrong_Action for Wrong_Selections
	 * @param testConfig
	 */
	@Test(description = "Verifying Popup when no transactions are selected.", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyActionNoSelection(Config testConfig) {

		int transactionRowNum = 157;
	
		
		// login into the Dashboard
		DashboardHelper helper = new DashboardHelper(testConfig);
		merchantPage = helper.doMerchantLogin(transactionRowNum);

		testConfig.putRunTimeProperty("merchantID", "6912");
		transactionPage = merchantPage.ClickTransactionsTab();
		
		transactionPage.selectDropDownAction(dropDownAction.Capture);
		//transactionPage.clickProcessButton();
		transactionPage.getNoActionSelectedErrorMessage("Sorry! Unable to process your request.There is no transaction selected");
	
		//filter page by capture
		transactionPage.filterByOrderStatus(filterByOrderStatus.Captured);
		
		transactionPage.selectFirstCheckBox();
		transactionPage.selectDropDownAction(dropDownAction.Capture);
		TestDataReader reader=new TestDataReader(testConfig, "StringValues");
		transactionPage.verifyWrongActionMessage(reader.GetData(18, "Value"),reader.GetData(19, "Value"));
		
		transactionPage.selectDropDownAction(dropDownAction.Cancel);
		//transactionPage.clickProcessButton();
		transactionPage.verifyWrongActionMessage(reader.GetData(20, "Value"),reader.GetData(21, "Value"));
		
		Assert.assertTrue(testConfig.getTestResult());
	}
}
