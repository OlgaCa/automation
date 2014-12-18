package Test.MerchantPanel.Transactions;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Transactions.CancelRefundPage;
import PageObject.MerchantPanel.Transactions.RequestsPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage.SearchOn;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.TestBase;


public class TestRequestCancelRefund extends TestBase{

	
	private TransactionHelper helper;

	@Test(description = "Verify Refund ransaction on Cancel/Refund Page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyCapturedTransaction(Config testConfig)
	{
		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();

		//MasterCard Debit Cards
		int transactionRowNum = 1;
		int paymentTypeRowNum =17;
		int cardDetailsRowNum = 1;
		int requestDetailRowNum=1;
		int PGinfoRowNum=3;
		String amount="1.00";
		
		tranHelper.GetTestTransactionPage();

		String txnId=testConfig.getRunTimeProperty("transactionId");
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		CancelRefundPage cancelRefundTransaction = dashBoard.ClickCancelRefund();

		//Request refund 
		cancelRefundTransaction.SearchTransaction(txnId, SearchOn.TransactionId);
		cancelRefundTransaction.RefundFirstTransaction();
		
		//View refunded transaction on request page			
		RequestsPage request= dashBoard.ClickViewRequest();
		TransactionFilterPage filter= new TransactionFilterPage(testConfig);
		filter.SearchTransaction(txnId, SearchOn.TransactionId);
		
		request.VerifyRefundRequest(requestDetailRowNum,paymentTypeRowNum,transactionRowNum,PGinfoRowNum,amount,tranHelper);

		//Verify Refunded transaction details from Database
		cancelRefundTransaction.VerifyCancelRefundTransaction(requestDetailRowNum, transactionRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}
	//public void test_VerifyAuthTransaction(Config testConfig) - moved to LiveCard TestCase - 14 
	}



