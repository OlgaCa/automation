package Test.AdminPanel.RiskRules.CategorizedChallengeRule;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class MaxcountChallengeIPOnCategoriesNegative extends TestBase{	

	//All test cases will fail as IP is same
	/*@Test(description = "Maxcount on IP: Creating a Rule in all categories and do the transaction only once to check it should not hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_Challenge_IP_travel_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 3;
		int RiskProfileRow = 1;
		//int MerchantParamRow = 1;
		int transactionRowNum = 37;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2  for Challenge rule
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}
	@Test(description = "Maxcount on IP: Creating a Rule in all categories and do the transaction only once to check it should not hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_Challenge_IP_apparel_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 3;
		int RiskProfileRow = 2;
		//int MerchantParamRow = 1;
		int transactionRowNum = 38;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2  for Challenge rule
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Maxcount on IP: Creating a Rule in all categories and do the transaction only once to check it should not hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_Challenge_IP_electronics_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 3;
		int RiskProfileRow = 3;
		//int MerchantParamRow = 1;
		int transactionRowNum = 39;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2  for Challenge rule
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");
		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Maxcount on IP: Creating a Rule in all categories and do the transaction only once to check it should not hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_Challenge_IP_games_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 3;
		int RiskProfileRow = 4;
		//int MerchantParamRow = 1;
		int transactionRowNum = 40;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2  for Challenge rule
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Maxcount on IP: Creating a Rule in all categories and do the transaction only once to check it should not hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_Challenge_IP_digital_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 3;
		int RiskProfileRow = 5;
		//int MerchantParamRow = 1;
		int transactionRowNum = 41;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2  for Challenge rule
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");
		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Maxcount on IP: Creating a Rule in all categories and do the transaction only once to check it should not hit Challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_Challenge_IP_other_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 3;
		int RiskProfileRow = 6;
		//int MerchantParamRow = 1;
		int transactionRowNum = 42;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2  for Challenge rule
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}*/

}