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
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestMultipleFilterOptions extends TestBase {

	private MerchantPanelPage merchantPage;
	private TransactionsPage transactionPage;
	private TransactionsHelper tranHelper;
	
	/**
	 * 
	 * Product-2102:Verify_Search_TransactionID_validations
	 * @param testConfig
	 * @throws InterruptedException 
	 */
	@Test(description = "Verifying multiple transactions with DB", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyMultipleTransactionsIDsWithDB(Config testConfig) throws InterruptedException {

		int transactionRowNum = 157;
		int paymentTypeRowNum =3;
		int cardDetailsRowNum = 1;
		int requestQueryRowNum=120;

		String[] txnIds=new String[3];
		//perform the transaction and verify the status
		TransactionHelper transHelper = new TransactionHelper(testConfig, false);
		transHelper.DoLogin();
		//perform multiple transactions and store it in payuID
		for(int i=0;i<3;i++)
		{
		transHelper.GetTestTransactionPage();
		transHelper.testResponse=(TestResponsePage) transHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		transHelper.testResponse.VerifyTransactionResponse(new TestDataReader(
				testConfig, "TransactionDetails"), transactionRowNum,
				new TestDataReader(testConfig, "PaymentType"), paymentTypeRowNum);
		txnIds[i]=testConfig.getRunTimeProperty("txnId");
		}
		
	
		//login into the  Dashboard
		
		DashboardHelper helper = new DashboardHelper(testConfig);
		merchantPage=helper.doMerchantLogin(transactionRowNum);
		transactionPage=merchantPage.ClickTransactionsTab();
		tranHelper=new TransactionsHelper(testConfig);
		//verify weather Transaction Details in Dashboard	
		merchantPage.filterBasedOn(filterTypes.TransactionID);
		tranHelper.filterDataAndVerifyWithDB(txnIds,filterBy.TransactionID,requestQueryRowNum);
		Assert.assertTrue(testConfig.getTestResult());	
	}
	
	/**
	 * Product-2108:Verify_Search_PayUID_validations
	 * 
	 * @param testConfig
	 * @throws InterruptedException 
	 */
	@Test(description = "Verify Multiple PayU IDs with DB", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyMultiplePayUIDsWithDB(Config testConfig) throws InterruptedException {


		int transactionRowNum = 157;
		int paymentTypeRowNum =3;
		int cardDetailsRowNum = 1;
		int requestQueryRowNum=120;

		String[] payuIDs=new String[3];
		//perform the transaction and verify the status
		TransactionHelper transHelper = new TransactionHelper(testConfig, false);
		transHelper.DoLogin();

		//perform multiple transactions and store it in payuID
		for(int i=0;i<3;i++)
		{
		transHelper.GetTestTransactionPage();
		transHelper.testResponse=(TestResponsePage) transHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		transHelper.testResponse.VerifyTransactionResponse(new TestDataReader(
				testConfig, "TransactionDetails"), transactionRowNum,
				new TestDataReader(testConfig, "PaymentType"), paymentTypeRowNum);
		payuIDs[i]=testConfig.getRunTimeProperty("mihpayid");
		
		}
	
		//login into the  Dashboard
		
		DashboardHelper helper = new DashboardHelper(testConfig);
		merchantPage=helper.doMerchantLogin(transactionRowNum);
		transactionPage=merchantPage.ClickTransactionsTab();
		TransactionsHelper tranHelper=new TransactionsHelper(testConfig);
		//verify weather Transaction Details in Dashboard
		merchantPage.filterBasedOn(filterTypes.PayUID);
		
		tranHelper.filterDataAndVerifyWithDB(payuIDs,filterBy.PayUID,requestQueryRowNum);
		Assert.assertTrue(testConfig.getTestResult());	
	}

}
