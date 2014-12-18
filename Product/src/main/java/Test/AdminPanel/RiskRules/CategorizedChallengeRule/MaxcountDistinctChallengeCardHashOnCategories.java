package Test.AdminPanel.RiskRules.CategorizedChallengeRule;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.TestBase;
import Utils.TestDataReader;

public class MaxcountDistinctChallengeCardHashOnCategories extends TestBase{

	//All test cases will fail,  as two valid cards are needed for each category
	/*@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in all categories and do the transaction to check it should hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_Challenge_CardHash_rule_travel(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 1;
		//int MerchantParamRow = 1;
		int transactionRowNum = 37;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =2; // 2 for Challenge rule
		String RuleSheet = "ChallengeRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 53, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testConfig.logComment("rule values:" + rules_values);
		Browser.wait(testConfig, 3);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in all categories and do the transaction to check it should hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_Challenge_CardHash_rule_apparel(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 2;
		//int MerchantParamRow = 1;
		int transactionRowNum = 38;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =2; // 2 for Challenge rule
		String RuleSheet = "ChallengeRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 53, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testConfig.logComment("rule values:" + rules_values);
		Browser.wait(testConfig, 3);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in all categories and do the transaction to check it should hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_Challenge_CardHash_rule_electronics(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 3;
		//int MerchantParamRow = 1;
		int transactionRowNum = 39;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =2; // 2 for Challenge rule
		String RuleSheet = "ChallengeRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 53, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testConfig.logComment("rule values:" + rules_values);
		Browser.wait(testConfig, 3);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in all categories and do the transaction to check it should hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_Challenge_CardHash_rule_games(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 4;
		//int MerchantParamRow = 1;
		int transactionRowNum = 40;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =2; // 2 for Challenge rule
		String RuleSheet = "ChallengeRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 53, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testConfig.logComment("rule values:" + rules_values);
		Browser.wait(testConfig, 3);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in all categories and do the transaction to check it should hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_Challenge_CardHash_rule_digital(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 5;
		//int MerchantParamRow = 1;
		int transactionRowNum = 41;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =2; // 2 for Challenge rule
		String RuleSheet = "ChallengeRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 53, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testConfig.logComment("rule values:" + rules_values);
		Browser.wait(testConfig, 3);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "MaxcountDistinct on CardHash: Creating a Rule in all categories and do the transaction to check it should hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_Challenge_CardHash_rule_others(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 9;
		int RiskProfileRow = 6;
		//int MerchantParamRow = 1;
		int transactionRowNum = 42;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		int whichrule =2; // 2 for Challenge rule
		String RuleSheet = "ChallengeRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 53, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testConfig.logComment("rule values:" + rules_values);
		Browser.wait(testConfig, 3);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}*/

}