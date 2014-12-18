package Test.AdminPanel.RiskRules.CategorizedDenyRule;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class MinamountDenyOnCategoriesNegative extends TestBase{	
	@Test(description = "Minamount: Creating a Rule in travel category and do the transaction only once to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Minamount_deny_travel_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 19;
		int RiskProfileRow = 1;
		int transactionRowNum = 37;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Minamount: Creating a Rule in apparel category and do the transaction only once to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Minamount_deny_apparel_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 19;
		int RiskProfileRow = 2;
		int transactionRowNum = 38;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Minamount: Creating a Rule in electronics category and do the transaction only once to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Minamount_deny_electronics_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 19;
		int RiskProfileRow = 3;
		int transactionRowNum = 39;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Minamount: Creating a Rule in games category and do the transaction only once to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Minamount_deny_games_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 19;
		int RiskProfileRow = 4;
		int transactionRowNum = 40;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Minamount: Creating a Rule in digital category and do the transaction only once to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Minamount_deny_digital_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 19;
		int RiskProfileRow = 5;
		int transactionRowNum = 41;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Minamount: Creating a Rule in others category and do the transaction only once to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Minamount_deny_other_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 19;
		int RiskProfileRow = 6;
		int transactionRowNum = 42;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =1; // 1 for deny rule
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, true);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

}