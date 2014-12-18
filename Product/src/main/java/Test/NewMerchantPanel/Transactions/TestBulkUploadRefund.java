package Test.NewMerchantPanel.Transactions;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import Test.NewMerchantPanel.Transactions.TransactionsHelper;
import PageObject.NewMerchantPanel.Transactions.TransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.Helper;
import Utils.Helper.FileType;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestBulkUploadRefund extends TestBase{

	private TransactionsPage transactionsPage;
	private MerchantPanelPage merchantPage;

	/**
	 * Product-2248:Verify_Bulk Upload_Refund Action
	 * Verify Filters based on Mode
	 * @param testConfig
	 */
	@Test(description = "Verify Bulk Upload on refund", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void verifyingBulkUploadRefund(Config testConfig) {

		TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
		tranHelper.DoLogin();
		DashboardHelper helper = new DashboardHelper(testConfig);
		TransactionsHelper transHelper=new TransactionsHelper(testConfig);
		//MasterCard Debit Cards
		int transactionRowNum = 157;
		int paymentTypeRowNum =3;
		int cardDetailsRowNum = 1;
		int requestQueryRowNum=120;
		tranHelper.GetTestTransactionPage();
		tranHelper.testResponse = (TestResponsePage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		tranHelper.testResponse.VerifyTransactionResponse(new TestDataReader(
				testConfig, "TransactionDetails"), transactionRowNum,
				new TestDataReader(testConfig, "PaymentType"), paymentTypeRowNum);
		String txnID=testConfig.getRunTimeProperty("txnId");
		String bulkUploadFile = Helper.getExcelFile(testConfig, FileType.refund);
		transHelper.writeToExcel(bulkUploadFile,1, testConfig.getRunTimeProperty("mihpayid"), "captured",tranHelper.testResponse.actualResponse.get("amount"));
		// Navigating to Merchant panel to refund request
		testConfig.putRunTimeProperty("txnId", txnID);
		merchantPage = helper.doMerchantLogin(transactionRowNum);
		merchantPage.uploadRefundFile(bulkUploadFile);
		Helper.executeCron(testConfig,"processRefund.php");
		transactionsPage=merchantPage.ClickTransactionsTab();
		transactionsPage.verifyTransactionStatus(txnID, "Refunded",1);
		TransactionsHelper transactionHelper=new TransactionsHelper(testConfig);
		transactionHelper.clickSelectiveCoulmnsAndCompareDetailsWithDB(requestQueryRowNum);
		
		Assert.assertTrue(testConfig.getTestResult());			
	}
}
