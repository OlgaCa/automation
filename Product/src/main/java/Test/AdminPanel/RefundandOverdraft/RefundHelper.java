package Test.AdminPanel.RefundandOverdraft;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import PageObject.AdminPanel.ManualUpdate.ChargebackTab;
import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.ManualUpdate.ReconPage;
import PageObject.AdminPanel.ManualUpdate.RefundTab;
import PageObject.AdminPanel.ManualUpdate.ChargebackTab.ChargebackFileType;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import PageObject.NewMerchantPanel.DashBoard.DashboardPage;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage.filterTypes;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ManualUpdateTab;
import Test.AdminPanel.ReconAndBankFeeSettlement.ReconHelper.ReconType;
import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import Test.NewMerchantPanel.Transactions.TransactionsHelper;
import Test.NewMerchantPanel.Transactions.TransactionsHelper.filterBy;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.FileHandler;
import Utils.Helper;
import Utils.TestDataReader;
import Utils.Helper.FileType;

public class RefundHelper 
{
	public Config testConfig;
	
	private  DashboardPage dashboarPage;
	private TransactionsPage transactionPage;
	private MerchantPanelPage merchantPage;
	private ManualUpdatePage manualUpdatePage;
	private ReconPage reconPage;
	private MerchantTransactionsPage merchantTransactionsPage;
	
	TestDataReader bankfeesettlementdata;
	DashboardHelper dashhelper;


	public Hashtable<String, String> actualReconData;
	
	public RefundHelper(Config testConfig)
	{
		this.testConfig = testConfig;
		
	}
	
	
	public void verifyAgingPrevented(int transactionRowNum,String[] refundAmount)
	{
		int status [] = new int[10];
		int OD_hit_status[] = new int[10];
		String [] arrtxnID =  new String[10];
		
		// Fetch Previous day Transaction which can be refunded
		arrtxnID = getPreviousdayTransaction(transactionRowNum);
		
		// Refund the Previous day Transaction
		for(int i =0;i<4;i++)
		{
			doRefund(transactionRowNum, arrtxnID[i], refundAmount[i]);
			status[i] = getRefundStatus(arrtxnID[i]);
			OD_hit_status[i] = isODHit(transactionRowNum);
		}
					
		/*Verify that once a refund is marked as queued due to OD hit all the preceding refunds will be marked as queued
		 * even if the merchant has the sufficient fund to refund that transaction
		*/
		for(int i =0;i<4;i++)
		{
			if (status[i] == 1 && i<2)
			{
				testConfig.logPass(
				"Transaction: " + arrtxnID[i] + "refunded successfully");
			}
			else if (status[i] == 0)
			{
				testConfig.logPass("Transaction: " + arrtxnID[i] + " is queued for refund");
				double totalSale = getMerchantSale(transactionRowNum);
				double refund2  = getMerchantRefundTotal(transactionRowNum);
				if((totalSale-refund2-Double.valueOf(refundAmount[i]))<0)
				{
					Helper.compareTrue(testConfig, "Refunds are queued if total refund + Queued refund amount is greater than total", true);
				}
				else
				{
					if (status[i-1] == 0)
					{
						Helper.compareTrue(testConfig, "once a single Transaction is marked as queued due to od hit all the Transaction after it should be marked as queued even if merchant has the required funds to refund it", true);
					}
					else
					{
						Helper.compareTrue(testConfig, "once a single Transaction is marked as queued due to od hit all the Transaction after it should be marked as queued even if merchant has the required funds to refund it", false);
					}
				}				
			}			
			else if (status[i] ==2)
			{
				testConfig.logFail("Transaction: " + arrtxnID[i] + " not refunded");
				Helper.compareTrue(testConfig, "once a single Transaction is marked as queued due to od hit all the Transaction after it should be marked as queued even if merchant has the required funds to refund it", false);
			}
			else
			{
				Helper.compareTrue(testConfig, "once a single Transaction is marked as queued due to od hit all the Transaction after it should be marked as queued even if merchant has the required funds to refund it", false);	
			}
		}
	}

	public void verifyODhit(int transactionRowNum,String refundAmount)
	{
		double sale1 = getMerchantSale(transactionRowNum);
		double OD_limit = getMerchantODlimit(transactionRowNum);
		double Total = sale1+OD_limit;
		int status = 0;
		int OD_hit_status = 0;
		String [] arrtxnID =  new String[10];
		
		// Fetch Previous day Transaction which can be refunded
		arrtxnID = getPreviousdayTransaction(transactionRowNum);
		
		// Refund the fetched Transaction
		for (int i=0;i<4;i++)
		{
			// Verify Refund queued once OD is breached
			doRefund(transactionRowNum,arrtxnID[i],refundAmount);
			status = getRefundStatus(arrtxnID[i]);
			System.out.println("Status:" + status);
			if (status == 1)
			{
				testConfig.logPass(
						"Transaction: " + arrtxnID[i] + "refunded successfully");
			}
			else if (status == 0)
			{
				testConfig.logPass("Transaction: " + arrtxnID[i] + " is queued for refund");
				double refund2  = getMerchantRefundTotal(transactionRowNum);
				Helper.compareEquals(testConfig, "Refunds are queued when total refund amount is equal to Total sale + OD limit ", Double.toString(Total), Double.toString(refund2));				
			}			
			else if (status ==5)
			{
				testConfig.logFail("Transaction: " + arrtxnID[i] + " not refunded");
			}
			else
			{
				testConfig.logFail("Transaction: " + arrtxnID[i] + " refund failed");
			}
		
			// Verify OD hit Status
			OD_hit_status = isODHit(transactionRowNum);
			if (OD_hit_status == 1)
			{
				double refund3  = getMerchantRefundTotal(transactionRowNum);
				if (refund3>=sale1)
				{
					Helper.compareTrue(testConfig, "OD is Hit when total refund amount is more than Total sale ", true);
				}
				else
				{
					Helper.compareTrue(testConfig, "OD is Hit when total refund amount is more than Total sale ", false);
				}
			}
			else
			{
				testConfig.logPass("OD not hit");
			}
		}
	}
	
	public int isODHit (int transactionRowNum)
	{
		
		TestDataReader reader = new TestDataReader(testConfig, "TransactionDetails");
		String merchantid = reader.GetData(transactionRowNum, "merchantid");
		int OD_hit = 0;
		testConfig.putRunTimeProperty("merchantid", merchantid);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 147, 1);
		if(!map.get("ODHit").equalsIgnoreCase(null))
		{
			testConfig.logPass(
					"Merchant OD limit hit: "+ map.get("ODHit"));
			OD_hit =  Integer.valueOf(map.get("ODHit"));
			
		}
		else
		{
			testConfig.logFail(
					"Error in Reading Merchant OD Limit");
		}

		testConfig.removeRunTimeProperty("ODHit");
		return OD_hit;
	}
	
	public int getRefundStatus(String txnid)
	{
		testConfig.putRunTimeProperty("txnid", txnid);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 148, 1);
		testConfig.removeRunTimeProperty("txnid");
		if(!map.get("status").equalsIgnoreCase(null))
		{
			testConfig.logPass("Refund Status fetched successfully");
			if(map.get("status").equals("success"))
			{
				return 1;
			}
			else if (map.get("status").equals("queued"))
			{
				return 0;
			}
			else
			{
				return 2;
			}
		}
		else
		{
			testConfig.logFail(
					"Error in Reading Merchant Successful Refund for Today");
			return 5;
		}

		
	}
	
	public double getMerchantODlimit(int transactionRowNum)
	{
		TestDataReader reader = new TestDataReader(testConfig, "TransactionDetails");
		String merchantid = reader.GetData(transactionRowNum, "merchantid");
		double OD = 0.0;
		testConfig.putRunTimeProperty("merchantid", merchantid);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 146, 1);
		if(!map.get("ODlimit").equalsIgnoreCase(null))
		{
			testConfig.logPass(
					"Merchant OD limit Fetched successfully as: "+ map.get("ODlimit"));
			OD =  Double.valueOf(map.get("ODlimit"));
			
		}
		else
		{
			testConfig.logFail(
					"Error in Reading Merchant OD Limit");
		}

		testConfig.removeRunTimeProperty("ODlimit");
		return OD;
	}
	
	public String[] getPreviousdayTransaction(int transactionRowNum)
	{
		String [] arrtxnID =  new String[10];
		String [] payuId = new String[10];
	
		TestDataReader reader = new TestDataReader(testConfig, "TransactionDetails");
		String merchantid = reader.GetData(transactionRowNum, "merchantid");
		testConfig.putRunTimeProperty("merchantid", merchantid);
		Map<String,String> map;
		for (int i=0;i<4;i++)
		{
			map = DataBase.executeSelectQuery(testConfig, 145, i+1);
			if(!map.get("txnid").equalsIgnoreCase(null))
			{
				arrtxnID[i] =  map.get("txnid");
				payuId[i] = map.get("payuid");
				testConfig.logPass(
						"Transaction " + i + " fetched Successfully");
				
			}
			else
			{
				testConfig.logFail(
						"Error in fetching previous days Transaction");
			}
		}
		testConfig.removeRunTimeProperty("txnid");
		testConfig.removeRunTimeProperty("payuid");
		return arrtxnID;
	}

	
	public String doRefund(int transactionRowNum,String txnid, String refundAmount)
	{
		dashhelper = new DashboardHelper(testConfig);
		merchantPage=dashhelper.doMerchantLogin(transactionRowNum);
		transactionPage=merchantPage.ClickTransactionsTab();
		transactionPage.FillRefundAmount(txnid,refundAmount);
		String refID[] ={transactionPage.verifyRefundMessage(1)};
		Helper.executeCron(testConfig,"processRefund.php");
		return refID[0];
		
	}
	
	public double getMerchantRefundTotal(int transactionRowNum)
	{
		TestDataReader reader = new TestDataReader(testConfig, "TransactionDetails");
		String merchantid = reader.GetData(transactionRowNum, "merchantid");
		double refund = 0.0;
		testConfig.putRunTimeProperty("merchantid", merchantid);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 144, 1);
		if(map.get("amount") != null)
		{
			testConfig.logPass(
					"Today's Successful Refund for Merchant" + merchantid + "Till now: " + map.get("amount"));
			refund =  Double.valueOf(map.get("amount"));
			
		}
		else
		{
			testConfig.logFail(
					"Error in Reading Merchant Successful Refund for Today");
		}

		testConfig.removeRunTimeProperty("amount");
		return refund;
	}

	
	
	public double getMerchantSale(int transactionRowNum)
	{
		TestDataReader reader = new TestDataReader(testConfig, "TransactionDetails");
		String merchantid = reader.GetData(transactionRowNum, "merchantid");
		double sale = 0.0;
		testConfig.putRunTimeProperty("merchantid", merchantid);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 143, 1);
		if(map.get("amount") != null)
		{
			testConfig.logPass(
					"Today's Sale of Merchant" + merchantid + "Till now: " + map.get("amount"));
			sale =  Double.valueOf(map.get("amount"));
			
		}
		else
		{
			testConfig.logFail(
					"Error in Reading Merchant Sale for Today");
		}

		testConfig.removeRunTimeProperty("amount");
		return sale;
	}
	
}
