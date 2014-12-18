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

public class Overdraft extends TestBase
{

	/**
	 * Product-2581:Verify that once OD is hit for a merchant having OD limit set to Some value, all the Refunds/cancel from there after are marked as queued.
	 * @param testConfig
	 */

	@Test(description = "Verify that once OD is hit for a merchant having OD limit set to 10, all the Refunds/cancel from there after are marked as queued", dataProvider = "GetTestConfig", groups = "1")
	void overdraft_hit(Config testConfig)
	{		
		TransactionHelper helper = new TransactionHelper(testConfig,true);
		RefundHelper refund = new RefundHelper(testConfig);
		
		String mihpayid[]=new String[10];
		String txnid[]=new String[10];
		int transactionRowNum = 166;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String refundAmount = "10.0";
			
		// Login to Admin Panel
		helper.DoLogin();
		
		// Perform 2 Test Transaction to get some Merchant Sale for Today
		for (int i =0;i<2;i++)
		{
			helper.GetTestTransactionPage();
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			mihpayid[i]=testConfig.getRunTimeProperty("mihpayid");
			txnid[i]=testConfig.getRunTimeProperty("txnid");
		}
		
		try
		{	
			// Verify that once a Refund is marked as queued it must be because Total Refund > Total Sale + OD
			refund.verifyODhit(transactionRowNum, refundAmount);
			
			// Test data Preparation for next day
			for (int i =0;i<2;i++)
			{
				helper.GetTestTransactionPage();
				helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			}
		}
		catch(Exception E)
		{
			// Test data Preparation for next day
			for (int i =0;i<2;i++)
			{
				helper.GetTestTransactionPage();
				helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			}
		}
		Assert.assertTrue(testConfig.getTestResult());

	}
	
	/**
	 * Product-2582:Verify that once OD is hit for a merchant having OD limit set to 0, all the Refunds/cancel from there after are marked as queued
	 * @param testConfig
	 */
	@Test(description = "Verify that OD is hit for a merchant for which there is no overdraft_limit", dataProvider = "GetTestConfig", groups = "1")
	void overdraft_hit_non_od_merchant(Config testConfig)
	{		

		TransactionHelper helper = new TransactionHelper(testConfig,true);
		RefundHelper refund = new RefundHelper(testConfig);
		
		// Variable Declaration and intialization
		int transactionRowNum = 167;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String refundAmount = "10.0";
		String mihpayid[]=new String[10];
		String txnid[]=new String[10];
		
		// Login to Admin Panel
		helper.DoLogin();
		// Perform 2 Test Transaction to get some Merchant Sale for Today
		for (int i =0;i<2;i++)
		{
			helper.GetTestTransactionPage();
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			mihpayid[i]=testConfig.getRunTimeProperty("mihpayid");
			txnid[i]=testConfig.getRunTimeProperty("txnid");
		}

		try
		{
			// Verify that once a Refund is marked as queued it must be because Total Refund > Total Sale + OD
			refund.verifyODhit(transactionRowNum, refundAmount);
			
			// Test data Preparation for next day
			for (int i =0;i<2;i++)
			{
				helper.GetTestTransactionPage();
				helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			}
		}
		catch(Exception E)
		{
			// Test data Preparation for next day
			for (int i =0;i<2;i++)
			{
				helper.GetTestTransactionPage();
				helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			}
		}
		Assert.assertTrue(testConfig.getTestResult());
		
	} // end of Method
	
	/**
	 * Product-2583:Verify that once a single Transaction is marked as queued due to od hit all the Transaction after it
	 * @param testConfig
	 */

	@Test(description = "Verify that once a single Transaction is marked as queued due to od hit all the Transaction after it should be marked as queued even if merchant has the required funds to refund it.", dataProvider = "GetTestConfig", groups = "1")
	void overdraft_hit_prevent_agging(Config testConfig)
	{		
		TransactionHelper helper = new TransactionHelper(testConfig,true);
		RefundHelper refund = new RefundHelper(testConfig);
		
		String mihpayid[]=new String[10];
		String txnid[]=new String[10];
		int transactionRowNum = 168;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int status [] = new int[10];
		int OD_hit_status[] = new int[10];
		String refundAmount [] = {"10.0","8.0","4.0","1.0"};
		String [] arrtxnID =  new String[10];
			
		// Login to Admin Panel
		helper.DoLogin();
		
		// Perform 2 Test Transaction to get some Merchant Sale for Today
		for (int i =0;i<2;i++)
		{
			helper.GetTestTransactionPage();
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			mihpayid[i]=testConfig.getRunTimeProperty("mihpayid");
			txnid[i]=testConfig.getRunTimeProperty("txnid");
		}
		
		try
		{
			/*Verify that once a refund is marked as queued due to OD hit all the preceding refunds will be marked as queued
			 * even if the merchant has the sufficient fund to refund that transaction
			*/
			refund.verifyAgingPrevented(transactionRowNum, refundAmount);
			
			// Test data Preparation for next day
			for (int i =0;i<2;i++)
			{
				helper.GetTestTransactionPage();
				helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			}
		}
		catch(Exception E)
		{
			// Test data Preparation for next day
			for (int i =0;i<2;i++)
			{
				helper.GetTestTransactionPage();
				helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			}
			Helper.compareTrue(testConfig, "once a single Transaction is marked as queued due to od hit all the Transaction after it should be marked as queued even if merchant has the required funds to refund it", false);
		}
		
		Assert.assertTrue(testConfig.getTestResult());

	}

}// end of Class
