package Test.AdminPanel.RiskRules.CategorizedDenyRule;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;
import Utils.DataBase;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class MaxcountDistinctDenyCardHashOnCategories extends TestBase{
	@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in travel category and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_deny_CardHash_rule_travel(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 1;
		int transactionRowNum = 37;
		int paymentTypeRowNum = 242;
		int cardDetailsRowNum = 19;
		int txnResponseRowNum = 18;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 78, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in apparel category and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_deny_CardHash_rule_apparel(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 2;
		int transactionRowNum = 38;
		int paymentTypeRowNum = 242;
		int cardDetailsRowNum = 19;
		int txnResponseRowNum = 18;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 78, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in electronics category and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_deny_CardHash_rule_electronics(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 3;
		int transactionRowNum = 39;
		int paymentTypeRowNum = 242;
		int cardDetailsRowNum = 19;
		int txnResponseRowNum = 18;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 78, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in games category and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_deny_CardHash_rule_games(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 4;
		int transactionRowNum = 40;
		int paymentTypeRowNum = 242;
		int cardDetailsRowNum = 19;
		int txnResponseRowNum = 18;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 78, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	/*@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in digital category and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_deny_CardHash_rule_digital(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 5;
		int transactionRowNum = 41;
		int paymentTypeRowNum = 242;
		int cardDetailsRowNum = 19;
		int txnResponseRowNum = 18;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 78, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}*/

	@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in others category and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_deny_CardHash_rule_others(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 6;
		int transactionRowNum = 42;
		int paymentTypeRowNum = 242;
		int cardDetailsRowNum = 19;
		int txnResponseRowNum = 18;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 78, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

}