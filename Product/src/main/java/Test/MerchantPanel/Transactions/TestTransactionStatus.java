package Test.MerchantPanel.Transactions;

import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.DataBase;
import Utils.Helper;
import Utils.TestBase;

public class TestTransactionStatus extends TestBase{
	
		@Test(description = "Verify status for failed transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestFailedTransactionStatus(Config testConfig)
			{

		// Perform failed transaction through CITI Debit Card
		int transactionRowNum = 3;
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 17;
		int ExcelSQLRowNum= 62;

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
				
		testConfig.putRunTimeProperty("merchant_id", "5912");	
		verifyTransactionStatus(testConfig,testConfig.getRunTimeProperty("mihpayid"),ExcelSQLRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify status for User Cancelled transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestUserCancelledTransactionStatus(Config testConfig)
			{

		// Perform user cancelled transaction through CITI Debit Card
		int ExcelSQLRowNum= 63;

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 12;
		int paymentTypeRowNum = 226;
		int cardDetailsRowNum = -1;
	
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,
				ExpectedResponsePage.TestResponsePage);
				
		testConfig.putRunTimeProperty("merchant_id", "5910");
		verifyTransactionStatus(testConfig,testConfig.getRunTimeProperty("mihpayid"),ExcelSQLRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}
	

	
	@Test(description = "Verify status for in progress transaction through NB", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestinProgressTransactionStatus(Config testConfig)
			{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//IndusInd Bank
		int transactionRowNum = 3;
		int paymentTypeRowNum = 134;
		int cardDetailsRowNum = -1;
		int ExcelSQLRowNum= 64;

		helper.GetTestTransactionPage();
		helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		testConfig.putRunTimeProperty("merchant_id", "5912");
		verifyTransactionStatus(testConfig,testConfig.getRunTimeProperty("transactionId") ,ExcelSQLRowNum);		
		Assert.assertTrue(testConfig.getTestResult());


		}
	
	
	@Test(description = "Verify status for Initiated transaction", dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void TestInitiatedTransactionStatus(Config testConfig)
			{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		int transactionRowNum = 12;
		int paymentTypeRowNum = 226;
		int cardDetailsRowNum = -1;
		int ExcelSQLRowNum= 65;

		helper.GetTestTransactionPage();

		// Perform initiated transaction 
		helper.DoInitiatedTransaction(transactionRowNum,paymentTypeRowNum);
				
		testConfig.putRunTimeProperty("merchant_id", "6582");
		
		verifyTransactionStatus(testConfig,testConfig.getRunTimeProperty("transactionId"),ExcelSQLRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());
	}	
	
	public void verifyTransactionStatus(Config testConfig, String payuID, int sqlRowNum){
	
		
		Map<String, String> transaction_Map = DataBase.executeSelectQuery(testConfig,sqlRowNum, 1);

		Helper.compareEquals(testConfig, "Transaction Status", transaction_Map.get("total"),"1");

			
	}
}
