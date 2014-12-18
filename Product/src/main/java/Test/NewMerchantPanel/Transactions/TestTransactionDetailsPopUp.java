package Test.NewMerchantPanel.Transactions;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.NewMerchantPanel.DashBoard.DashboardPage;
import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage.filterTypes;
import Test.NewMerchantPanel.Transactions.TransactionsHelper;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage;
import Test.NewMerchantPanel.Transactions.TransactionsHelper.filterBy;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestTransactionDetailsPopUp extends TestBase {

		private  DashboardPage dashboarPage;
		private TransactionsPage transactionPage;
		private MerchantPanelPage merchantPage;
		
		/**
		 *  Product-1548:Verify_Transaction Details_All Transactions
		 * 
		 * @param testConfig
		 * @throws InterruptedException 
		 */
		@Test(description = "Verify Capture Transaction on Merchant Panel", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
		public void verifyTransactionsDetailsWithDB(Config testConfig) throws InterruptedException {

			int transactionRowNum = 157;
			int paymentTypeRowNum =3;
			int cardDetailsRowNum = 1;
			int requestQueryRowNum=120;

			//perform the transaction and verify the status
			TransactionHelper transHelper = new TransactionHelper(testConfig, false);
			transHelper.DoLogin();
			transHelper.GetTestTransactionPage();

			transHelper.testResponse=(TestResponsePage) transHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			transHelper.testResponse.VerifyTransactionResponse(new TestDataReader(
					testConfig, "TransactionDetails"), transactionRowNum,
					new TestDataReader(testConfig, "PaymentType"), paymentTypeRowNum);
			
			String[] txnID={testConfig.getRunTimeProperty("txnId")};
					
			 //login into the  Dashboard
			
			DashboardHelper helper = new DashboardHelper(testConfig);
			merchantPage=helper.doMerchantLogin(transactionRowNum);
			transactionPage=merchantPage.ClickTransactionsTab();
			TransactionsHelper tranHelper=new TransactionsHelper(testConfig);
			//verify weather Transaction Details in Dashboard
			
			merchantPage.filterBasedOn(filterTypes.TransactionID);
			tranHelper.filterDataAndVerifyWithDB(txnID,filterBy.TransactionID,requestQueryRowNum);
			Assert.assertTrue(testConfig.getTestResult());	

		}
		
		/**
		 * Product-1947:Verify_Transaction Details_All Requests
		 * 
		 * @param testConfig
		 * @throws InterruptedException 
		 */
		@Test(description = "Verify Requested Transaction on Merchant Panel", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
		public void verifyRequestDetailsWithDB(Config testConfig) throws InterruptedException {

			int transactionRowNum = 157;
			int paymentTypeRowNum =3;
			int cardDetailsRowNum = 1;
			int requestQueryRowNum=120;

			//perform the transaction and verify the status
			TransactionHelper transHelper = new TransactionHelper(testConfig, false);
			transHelper.DoLogin();
			transHelper.GetTestTransactionPage();

			transHelper.testResponse=(TestResponsePage) transHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			transHelper.testResponse.VerifyTransactionResponse(new TestDataReader(
					testConfig, "TransactionDetails"), transactionRowNum,
					new TestDataReader(testConfig, "PaymentType"), paymentTypeRowNum);
			
			String txnID=testConfig.getRunTimeProperty("txnId");
					
			TransactionsPage allTransPage;
			//login into the  Dashboard
			
			DashboardHelper helper = new DashboardHelper(testConfig);
			TransactionsHelper tranHelper=new TransactionsHelper(testConfig);
			merchantPage=helper.doMerchantLogin(transactionRowNum);
			allTransPage=merchantPage.ClickTransactionsTab();
			
			allTransPage.FillRefundAmount(txnID,"1.00");
			String[] refID={allTransPage.verifyRefundMessage(1)};
			Helper.executeCron(testConfig,"processRefund.php");
			//verify weather Transaction Details in Dashboard
			merchantPage.filterBasedOn(filterTypes.ReferenceID);
			tranHelper.filterDataAndVerifyWithDB(refID,filterBy.ReferenceID,requestQueryRowNum);
		
			Assert.assertTrue(testConfig.getTestResult());	

		}
		
		
}
