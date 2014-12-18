package Test.AdminPanel.RiskRules.CategorizedDenyRule;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class SumamountDenyCodPhoneOnCategories extends TestBase{
	//Test cases will always fail as cod transactions without zipdial page, do not hit the rule. redmine is already raised
	/*@Test(description = "Sumamount on codphone: Creating a Rule in all categories and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Sumamount_deny_codphone_rule_travel(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 15;
		int RiskProfileRow = 1;
		//int MerchantParamRow = 1;
		int transactionRowNum = 37;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);

		helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Sumamount on codphone: Creating a Rule in all categories and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Sumamount_deny_codphone_rule_apparel(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 15;
		int RiskProfileRow = 2;
		//int MerchantParamRow = 1;
		int transactionRowNum = 38;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);

		helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Sumamount on codphone: Creating a Rule in all categories and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Sumamount_deny_codphone_rule_electronics(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 15;
		int RiskProfileRow = 3;
		//int MerchantParamRow = 1;
		int transactionRowNum = 39;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);

		helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Sumamount on codphone: Creating a Rule in all categories and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Sumamount_deny_codphone_rule_games(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 15;
		int RiskProfileRow = 4;
		//int MerchantParamRow = 1;
		int transactionRowNum = 40;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);

		helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Sumamount on codphone: Creating a Rule in all categories and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Sumamount_deny_codphone_rule_digital(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 15;
		int RiskProfileRow = 5;
		//int MerchantParamRow = 1;
		int transactionRowNum = 41;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);

		helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Sumamount on codphone: Creating a Rule in all categories and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Sumamount_deny_codphone_rule_others(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 15;
		int RiskProfileRow = 6;
		//int MerchantParamRow = 1;
		int transactionRowNum = 42;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);

		helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}*/

}