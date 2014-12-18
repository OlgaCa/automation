package Test.AdminPanel.RiskRules.CategorizedChallengeRule;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class MaxcountChallengeCardHashOnCategories extends TestBase{
	@Test(description = "MaxCount on CardHash: Creating a Rule in travel category and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_challenge_CardHash_rule_travel(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 4;
		int RiskProfileRow = 1;
		int transactionRowNum = 37;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2 for challenge rule
		String RuleSheet = "ChallengeRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 3);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "MaxCount on CardHash: Creating a Rule in apparel category and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_challenge_CardHash_rule_apparel(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 4;
		int RiskProfileRow = 2;
		int transactionRowNum = 38;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2 for challenge rule
		String RuleSheet = "ChallengeRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 3);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "MaxCount on CardHash: Creating a Rule in electronic category and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_challenge_CardHash_rule_electronics(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 4;
		int RiskProfileRow = 3;
		int transactionRowNum = 39;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2 for challenge rule
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 3);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "MaxCount on CardHash: Creating a Rule in games category and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_challenge_CardHash_rule_games(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 4;
		int RiskProfileRow = 4;
		int transactionRowNum = 40;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2 for challenge rule
		String RuleSheet = "ChallengeRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 3);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	/*@Test(description = "MaxCount on CardHash: Creating a Rule in digital category and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_challenge_CardHash_rule_digital(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =2;
		int RiskRuleRow = 4;
		int RiskProfileRow = 5;
		int transactionRowNum = 41;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2 for challenge rule
		String RuleSheet = "ChallengeRule";


		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 3);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}*/

	@Test(description = "MaxCount on CardHash: Creating a Rule in other category and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_challenge_CardHash_rule_others(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int numOfTxns =1;
		int RiskRuleRow = 4;
		int RiskProfileRow = 6;
		int transactionRowNum = 42;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int whichrule =2; // 2 for challenge rule
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateCategoryRuleNDoTxn(testConfig, helper, RiskProfileRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichrule, RiskRuleRow, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 3);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

}