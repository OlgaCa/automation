package Test.AdminPanel.RefundandOverdraft;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.NewMerchantPanel.DashBoard.DashboardPage;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import Test.NewMerchantPanel.Transactions.TransactionsHelper;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class AggNonAggCal extends TestBase
{
	/**
	 * Product-2577:Verify that Transaction done from aggregator PG are added to merchant's Today's Sale
	 * @param testConfig
	 */
	
	@Test(description = "Verify that Transaction from Aggregator PG is added in today's sale of merchant", dataProvider = "GetTestConfig", groups = "1")
	void MerchantTodaySale(Config testConfig)
	{
	
		TransactionHelper helper = new TransactionHelper(testConfig,true);
		RefundHelper refund = new RefundHelper(testConfig);
		
		
		int transactionRowNum = 164;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		// Calculate Merchants Today's Sale before doing transaction from Aggregator PG
		double sale1 = refund.getMerchantSale(transactionRowNum);
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		
		// Calculate Merchants Today's Sale after doing transaction from Aggregator PG
		double sale2 = refund.getMerchantSale(transactionRowNum);
		Helper.compareEquals(testConfig, "Transaction from Aggregator PG is added to Merchant Sale", Double.toString(sale1+10), Double.toString(sale2));
		Assert.assertTrue(testConfig.getTestResult());
				
	}
	
	/**
	 * Product-2578:Verify that Refund/Cancel of the Transaction passed through Aggregator PG is added in today's Refund/Cancel of merchant
	 * @param testConfig
	 */
	@Test(description = "Verify that Refund/Cancel of the Transaction passed through Aggregator PG is added in today's Refund/Cancel of merchant", dataProvider = "GetTestConfig", groups = "1")
	void MerchantTodaySuccessfulRefund(Config testConfig)
	{
	
		TransactionHelper helper = new TransactionHelper(testConfig,true);
		RefundHelper refund = new RefundHelper(testConfig);
		
		
		int transactionRowNum = 164;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String refundAmount = "1.0";
		String mihpayid[]=new String[1];
		String txnid[]=new String[1];
		String refundRefId = null;
		
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		mihpayid[0]=testConfig.getRunTimeProperty("mihpayid");
		txnid[0]=testConfig.getRunTimeProperty("txnid");
		
		// Extract Today's Successful Refund Total from DB
		double refund1  = refund.getMerchantRefundTotal(transactionRowNum);
		
		refundRefId = refund.doRefund(transactionRowNum,txnid[0],refundAmount);
		
		// Extract Today's Successful Refund Total from DB
		double refund2  = refund.getMerchantRefundTotal(transactionRowNum);
		Helper.compareEquals(testConfig, "Refund on a Transaction from Aggregator PG is added to Merchant Refund", Double.toString(refund1+1), Double.toString(refund2));
		Assert.assertTrue(testConfig.getTestResult());
				
	}
	
	/**
	 * Product-2579:Verify that Transaction done from Non aggregator PG are not added to merchant's Today's Sale
	 * @param testConfig
	 */

	@Test(description = "Verify that Transaction from Non Aggregator PG is not added in today's sale of merchant", dataProvider = "GetTestConfig", groups = "1")
	void MerchantTodaySale_nonAggregator(Config testConfig)
	{
	
		TransactionHelper helper = new TransactionHelper(testConfig,true);
		RefundHelper refund = new RefundHelper(testConfig);
		
		
		int transactionRowNum = 165;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		
		helper.DoLogin();
		helper.GetTestTransactionPage();
		// Calculate Merchants Today's Sale before doing transaction from Aggregator PG
		double sale1 = refund.getMerchantSale(transactionRowNum);
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		
		paymentTypeRowNum = 37;
		cardDetailsRowNum = 74;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		
		// Calculate Merchants Today's Sale after doing transaction from Aggregator PG
		double sale2 = refund.getMerchantSale(transactionRowNum);
		Helper.compareEquals(testConfig, "Transaction from Non Aggregator PG is not added to Merchant Sale", Double.toString(sale1+10), Double.toString(sale2));
		Assert.assertTrue(testConfig.getTestResult());
				
	}
	
	/**
	 * Product-2580:Verify that Refund/Cancel of the Transaction passed through non Aggregator PG is not added in today's Refund/Cancel of merchant
	 * @param testConfig
	 */

	@Test(description = "Verify that Refund/Cancel of the Transaction passed through non Aggregator PG is not added in today's Refund/Cancel of merchant", dataProvider = "GetTestConfig", groups = "1")
	void MerchantTodaySuccessfulRefund_NonAggregator(Config testConfig)
	{
	
		TransactionHelper helper = new TransactionHelper(testConfig,true);
		RefundHelper refund = new RefundHelper(testConfig);
		
		
		int transactionRowNum = 165;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String refundAmount = "1.0";
		String mihpayid[]=new String[2];
		String txnid[]=new String[2];
		String refundRefId = null;
		
		
		helper.DoLogin();
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		mihpayid[0]=testConfig.getRunTimeProperty("mihpayid");
		txnid[0]=testConfig.getRunTimeProperty("txnid");
		
		paymentTypeRowNum = 37;
		cardDetailsRowNum = 74;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		
		mihpayid[1]=testConfig.getRunTimeProperty("mihpayid");
		txnid[1]=testConfig.getRunTimeProperty("txnid");
		
		// Extract Today's Successful Refund Total from DB
		double refund1  = refund.getMerchantRefundTotal(transactionRowNum);
		
		refundRefId = refund.doRefund(transactionRowNum,txnid[0],refundAmount);
		System.out.println("Reference id:"+ refundRefId);
		
		refundRefId = refund.doRefund(transactionRowNum,txnid[1],refundAmount);
		System.out.println("Reference id:"+ refundRefId);
		
		// Extract Today's Successful Refund Total from DB
		double refund2  = refund.getMerchantRefundTotal(transactionRowNum);
		Helper.compareEquals(testConfig, "Refund on a Transaction from Non Aggregator PG is not added to Merchant Refund", Double.toString(refund1+1), Double.toString(refund2));
		Assert.assertTrue(testConfig.getTestResult());
				
	}
}
