package Test.AdminPanel.RiskRules.PerMerchantRules;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.MerchantList.MerchantDetails.FlaggedTxnPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestFlaggedTransactions extends TestBase{

	//Is not working, sent a mail to Gyanesh for confirmation
	/*@Test(description = "Perform a denied transaction on Mincount and check txn is searchable in flagged transactions",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Denied_txn_searchable(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; 
		int numOfTxns =1;
		int RiskRuleRow = 17;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");


		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, 
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.merchantListPage = helper.home.clickMerchantList();
		helper.merchantListPage.SearchMerchant(merchant);
		
		FlaggedTxnPage flaggedPage = helper.merchantListPage.clickFlaggedTxn();
		flaggedPage.searchFlaggedTxnPresent(testConfig.getRunTimeProperty("transactionId"));
		Assert.assertTrue(testConfig.getTestResult());
	}*/


	@Test(description = "Perform a denied transaction on Mincount and check txn is not searchable for other merchant in flagged transactions",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Denied_txn_not_searchable_other_merchant(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; 
		int numOfTxns =1;
		int RiskRuleRow = 17;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		helper.merchantListPage = helper.home.clickMerchantList();

		merchant = dataReader.GetData(transactionRowNum+1, "Comments");

		helper.merchantListPage.SearchMerchant(merchant);
		FlaggedTxnPage flaggedPage = helper.merchantListPage.clickFlaggedTxn();		
		flaggedPage.searchFlaggedTxnAbsent(testConfig.getRunTimeProperty("transactionId"));

		Assert.assertTrue(testConfig.getTestResult());
}


	@Test(description = "Minamount Case: Perform an auth transaction and Verify the transaction is not searchable in flagged transactions",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Auth_txn_not_searchable(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; 
		int numOfTxns =1;
		int RiskRuleRow = 19;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		helper.merchantListPage = helper.home.clickMerchantList();

		helper.merchantListPage.SearchMerchant(merchant);
		FlaggedTxnPage flaggedPage = helper.merchantListPage.clickFlaggedTxn();
		flaggedPage.searchFlaggedTxnAbsent(testConfig.getRunTimeProperty("transactionId"));

		Assert.assertTrue(testConfig.getTestResult());

	}
}
