package Test.AdminPanel.RiskRules.PerMerchantRules;
/*
 * Flow of automating test rule
 * @author: Himanshu
 */

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;
import Utils.DataBase.DatabaseType;

public class TestChallengeRule extends TestBase{

	@Test(description = "MaxCount on Email: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=1)
	public void Maxcount_challenge_email_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for Challenge Rule
		int numOfTxns =2;
		int RiskRuleRow = 1;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}	

	@Test(description = "MaxCount on Phone: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=2)
	public void Maxcount_challenge_phone_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =2;
		int RiskRuleRow = 2;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(10)));

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}	

	// need two IPs
	@Test(description = "MaxCount on IP: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=3)
	public void Maxcount_challenge_ip_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 3;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}	

	//Card is already being used, so transaction will always hit the rule
	@Test(description = "MaxCount on CardHash: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=4)
	public void Maxcount_challenge_cardHash_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 4;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}	

	//Cod Phone for Challenge does not work, so response will be No risk
	@Test(description = "MaxCount on Cod Phone: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=5)
	public void Maxcount_challenge_codPhone_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 5;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}	

	@Test(description = "MaxCountDistinct on Email: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=6)
	public void MaxcountDistinct_challenge_email_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =2;
		int RiskRuleRow = 10;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 53, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	//need two valid cards
	/*@Test(description = "MaxCountDistinct on cardHash: Creating a Rule in all categories and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void MaxcountDistinct_challenge_cardHash_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =2;
		int RiskRuleRow = 9;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 74, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testConfig.logComment("rule values:" + rules_values);
		Browser.wait(testConfig, 10);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}*/


	@Test(description = "Minamount: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=7)
	public void Minamount_challenge_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 16;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());


	}


	@Test(description = "Maxamount: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=8)
	public void Maxamount_challenge_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 17;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	//This test case won't work 
	/*@Test(description = "CCHash: Creating a Rule in all categories and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void CCHash_challenge_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =2;
		int RiskRuleRow = 18;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");


		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		testConfig.logComment("rule values:" + rules_values);

		Browser.wait(testConfig, 10);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());


	}*/


	
	//Card has been used multiple times, so transaction will always hit challenge rule
	@Test(description = "Sumamount on cardhash: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=9)
	public void Sumamount_challenge_cardHash_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 14;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}


	@Test(description = "Sumamount on Email: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=10)
	public void Sumamount_challenge_email_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();


		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =2;
		int RiskRuleRow = 11;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}


	//need two IPs
	@Test(description = "Sumamount on IP: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=11)
	public void Sumamount_challenge_IP_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 13;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}


	@Test(description = "Sumamount on Phone: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=12)
	public void Sumamount_challenge_phone_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns = 2;
		int RiskRuleRow = 12;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(10)));

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());


	}


	//Negative Test Cases
	@Test(description = "Maxcount on Email: Creating a Rule and do the transaction only once to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=13)
	public void Maxcount_challenge_email_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 1;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}


	@Test(description = "Maxcount on Phone: Creating a Rule and do the transaction only once to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=14)
	public void Maxcount_challenge_phone_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 2;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(10)));

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//won't work need two IPs as current IP is already in use. So response will Be Risk Hit

	/*
	@Test(description = "Maxcount on IP: Creating a Rule and do the transaction only once to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
<<<<<<< HEAD
 
 		public void Maxcount_challenge_ip_negative_rule(Config testConfig) {
=======
	public void Maxcount_challenge_ip_negative_rule(Config testConfig) {
>>>>>>> fe7ce3c4ee3eeaa556c672990386dbb25faef351

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 3;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Card hash has been used multiple times, so challenge rule will be hit everytime
	@Test(description = "Maxcount on Card Hash: Creating a Rule and do the transaction only once to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_challenge_cardHash_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 4;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	//cod Phone doesn't work for Challenge Rule
<<<<<<< HEAD
<<<<<<< HEAD
	@Test(description = "Maxcount on Cod Phone (negative): Creating a Rule in all categories and do the transaction only once to check it should not hit challenge rule",
			dataProvider="GetTestConfig", groups="1")
=======
	/*@Test(description = "Maxcount on Cod Phone (negative): Creating a Rule in all categories and do the transaction only once to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
>>>>>>> fe7ce3c4ee3eeaa556c672990386dbb25faef351
=======
	/*@Test(description = "Maxcount on Cod Phone (negative): Creating a Rule in all categories and do the transaction only once to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
>>>>>>> fe7ce3c4ee3eeaa556c672990386dbb25faef351
	public void Maxcount_challenge_codPhone_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 5;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());


	}
	 */

	@Test(description = "MaxCountDistinct on email: Creating a Rule and do the transaction to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=15)
	public void MaxcountDistinct_challenge_email_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 10;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 53, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	//Card has been used multiple times, so rule will be hit everytime
	@Test(description = "MaxCountDistinct on cardHash (negative): Creating a Rule and do the transaction to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=16)
	public void MaxcountDistinct_challenge_cardHash_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 9;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Minamount Negative Case: Creating a Rule in all categories and do the transaction and check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=17)
	public void Minamount_negative_challenge_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 19;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());


	}


	@Test(description = "Maxamount Negative Case: Creating a Rule and do the transaction and check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=18)
	public void Maxamount_negative_challenge_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 20;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, 
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());


	}

	/*
	//Card has been used multiple times, so rule will be hit everytime
	@Test(description = "Sumamount on cardhash(negative): Creating a Rule in all categories and do the transaction to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Sumamount_challenge_cardHash_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 14;
		//	int MerchantParamRow = 1;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");



		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	//On one machine, IP is same so Rule will be hit everytime
	@Test(description = "Sumamount on IP(negative): Creating a Rule in all categories and do the transaction to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Sumamount_challenge_IP_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 13;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");



		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}*/

	@Test(description = "Sumamount on Email: Creating a Rule and do the transaction to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=19)
	public void Sumamount_challenge_email_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 11;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, 
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Sumamount on Phone (negative): Creating a Rule and do the transaction to check it should not hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=20)
	public void Sumamount_challenge_phone_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 12;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(10)));

		helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Browser.wait(testConfig, 10);

		Assert.assertTrue(testConfig.getTestResult());

		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Sumamount on card_hash : Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=21)
	public void Merchant_Sumamount_challenge_rule_cardhash_International(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =2;
		int RiskRuleRow = 22;
		int transactionRowNum = 129;
		int paymentTypeRowNum = 348;
		int cardDetailsRowNum = 68;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

		String[] rules_values=helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false,true);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "maxcountDistinct as check type cardhash for email: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=22)
	public void Merchant_MaxCountDistinct_challenge_rule_Both_cardhashforemail(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int RiskRuleRow = 28;
		int transactionRowNum = 130;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

		Browser.navigateToURL(testConfig, testConfig.getRunTimeProperty("AdminPortalHome"));
		helper.merchantListPage = helper.home.clickMerchantList();

		helper.merchantListPage.SearchMerchant(merchant);
		helper.rulePage = helper.merchantListPage.clickMerchantTxnRule();
		helper.rulePage.DeleteExistingRules(); 

		//To add switch case here to select option of rule 
		String rules_values[] = helper.rulePage.CreateRule(RiskRuleRow, whichRule, RuleSheet);

		//To verify both txn, First should be successful,
		rules_values[2] = helper.rulePage.SelectRule();
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		
		paymentTypeRowNum = 348;
		cardDetailsRowNum = 68;
		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));
//		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "maxcountDistinct as check type email for card_hash: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=23)
	public void Merchant_MaxCountDistinct_challenge_rule_Domestic_emailforcardhash(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =2;
		int RiskRuleRow = 29;
		int transactionRowNum = 131;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));
//		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

		String[] rules_values=helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false,true);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "maxcount as check type for card_hash: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=24)
	public void Merchant_MaxCount_challenge_rule_Domestic_cardhash(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =2;
		int RiskRuleRow = 26;
		int transactionRowNum = 129;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

//		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));
//		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

		String[] rules_values=helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false,true);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Sumamount on Phone : Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=25)
	public void Merchant_Sumamount_challenge_rule_phone_Domestic(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =2;
		int RiskRuleRow = 21;
		int transactionRowNum = 129;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

		String[] rules_values=helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false,true);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "minamount as check type : Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=26)
	public void Merchant_MinAmount_challenge_rule_Domestic(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 23;
		int transactionRowNum = 129;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

		String[] rules_values=helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false,true);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "maxamount as check type : Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=27)
	public void Merchant_MaxAmount_challenge_rule_International(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =1;
		int RiskRuleRow = 24;
		int transactionRowNum = 129;
		int paymentTypeRowNum = 348;
		int cardDetailsRowNum = 68;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

		String[] rules_values=helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false,true);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "maxcount as check type for phone: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=28)
	public void Merchant_MaxCount_challenge_rule_International_Phone(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int numOfTxns =2;
		int RiskRuleRow = 25;
		int transactionRowNum = 129;
		int paymentTypeRowNum = 348;
		int cardDetailsRowNum = 68;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));
//		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

		String[] rules_values=helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum,
				cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, false,true);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		testConfig.putRunTimeProperty("Risk action taken", "Transaction challenged by Risk");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "maxcount as check type for email: Creating a Rule and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=29)
	public void Merchant_MaxCount_challenge_rule_Both_email(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =2; // 2 for challenge rule 
		int RiskRuleRow = 27;
		int transactionRowNum = 119;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		String RuleSheet = "ChallengeRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

		Browser.navigateToURL(testConfig, testConfig.getRunTimeProperty("AdminPortalHome"));
		helper.merchantListPage = helper.home.clickMerchantList();

		helper.merchantListPage.SearchMerchant(merchant);
		helper.rulePage = helper.merchantListPage.clickMerchantTxnRule();
		helper.rulePage.DeleteExistingRules(); 

		//To add switch case here to select option of rule 
		String rules_values[] = helper.rulePage.CreateRule(RiskRuleRow, whichRule, RuleSheet);

		//To verify both txn, First should be successful,
		rules_values[2] = helper.rulePage.SelectRule();
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
//		testConfig.putRunTimeProperty("Risk action taken", "No risk");
//		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		
		paymentTypeRowNum = 348;
		cardDetailsRowNum = 68;
		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));
//		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
//		testConfig.putRunTimeProperty("Risk action taken", "No risk");
//		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	
	private final int whichRule = 2;
	private final String ruleSheet = "ChallengeRule";
	// private final String paymentTypeSheet="PaymentType";
	private final String tnsdetailsSheet = "TransactionDetails";

	@Test(description = "Category scope: create Deny rule with both card, maxcountdistinct as checktype card_hash for email. Do transaction to check it should hit deny rule", dataProvider = "GetTestConfig", groups = { "1" }, timeOut = 600000, priority = 30)
	public void category_Maxcountdistinct_Deny_Rule_Both_cardhashforemail(
			Config testConfig) {
		int transactionRow = 131;
		int paymentTypeRow = 348;
		int cardDetailsRow = 68;
		TransactionHelper helpers = new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		helpers.merchantListPage = helpers.home.clickMerchantList();
		TestDataReader transactionDetails = new TestDataReader(testConfig,
				tnsdetailsSheet);
		String merchantName = transactionDetails.GetData(transactionRow,
				"Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage = helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values = helpers.rulePage.CreateRule(79, whichRule,
				ruleSheet);
		rules_values[2] = helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		transactionRow = 132;
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		transactionRow = 131;
		paymentTypeRow = 349;
		cardDetailsRow = 1;
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		// String txnResponxe=new TestDataReader(testConfig,
		// paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken",
				"Transaction challenged by Risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		Map<String, String> transaction = DataBase.executeSelectQuery(
				testConfig, 100, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1",
				transaction.get("riskAction"));

		Map<String, String> blacklist = DataBase.executeSelectQuery(testConfig,
				15, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied", rules_values[2],
				blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
		// helpers.VerifyTransactionAfterDenied(rules_values,
		// Integer.parseInt(txnResponxe));
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Category scope: create challenge rule with domestic card, maxcountdistinct as checktype for email and card_hash. Do transaction to check it should hit deny rule", dataProvider = "GetTestConfig", groups = { "1" }, timeOut = 600000, priority = 31)
	public void category_Maxcountdistinct_Deny_Rule_Domestic_emailforcardhash(
			Config testConfig) {
		int transactionRow = 127;
		int paymentTypeRow = 5;
		int cardDetailsRow = 1;
		TransactionHelper helpers = new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		helpers.merchantListPage = helpers.home.clickMerchantList();
		TestDataReader transactionDetails = new TestDataReader(testConfig,
				tnsdetailsSheet);
		String merchantName = transactionDetails.GetData(transactionRow,
				"Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage = helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values = helpers.rulePage.CreateRule(37, whichRule,
				ruleSheet);
		rules_values[2] = helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		transactionRow = 128;
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		transactionRow = 127;
		paymentTypeRow = 349;
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		testConfig.putRunTimeProperty("Risk action taken",
				"Transaction challenged by Risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		Map<String, String> transaction = DataBase.executeSelectQuery(
				testConfig, 100, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1",
				transaction.get("riskAction"));

		Map<String, String> blacklist = DataBase.executeSelectQuery(testConfig,
				15, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied", rules_values[2],
				blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "maxcount as check type for card_hash: Creating a Rule and do the transaction to check it should hit challenge rule", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1", priority = 32)
	public void category_Sumamount_challenge_rule_cardhash_International(
			Config testConfig) {
		int transactionRow = 129;
		int paymentTypeRow = 348;
		int cardDetailsRow = 68;
		TransactionHelper helpers = new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.merchantListPage = helpers.home.clickMerchantList();
		TestDataReader transactionDetails = new TestDataReader(testConfig,
				tnsdetailsSheet);
		String merchantName = transactionDetails.GetData(transactionRow,
				"Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage = helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values = helpers.rulePage.CreateRule(36, whichRule,
				ruleSheet);
		rules_values[2] = helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		transactionRow = 130;
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		transactionRow = 129;
		// paymentTypeRow=349;
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		// String txnResponxe=new TestDataReader(testConfig,
		// paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		// helpers.VerifyTransactionAfterDenied(rules_values,
		// Integer.parseInt(txnResponxe));
		testConfig.putRunTimeProperty("Risk action taken",
				"Transaction challenged by Risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		Map<String, String> transaction = DataBase.executeSelectQuery(
				testConfig, 100, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1",
				transaction.get("riskAction"));

		Map<String, String> blacklist = DataBase.executeSelectQuery(testConfig,
				15, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied", rules_values[2],
				blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "maxcount as check type for card_hash: Creating a Rule and do the transaction to check it should hit challenge rule", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1", priority = 33)
	public void category_MaxCount_challenge_rule_international_cardhash(
			Config testConfig) {
		int transactionRow = 127;
		int paymentTypeRow = 348;
		int cardDetailsRow = 68;
		TransactionHelper helpers = new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.merchantListPage = helpers.home.clickMerchantList();
		TestDataReader transactionDetails = new TestDataReader(testConfig,
				tnsdetailsSheet);
		String merchantName = transactionDetails.GetData(transactionRow,
				"Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage = helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values = helpers.rulePage.CreateRule(33, whichRule,
				ruleSheet);
		rules_values[2] = helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		transactionRow = 128;
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		transactionRow = 127;
		// paymentTypeRow=349;
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		// String txnResponxe=new TestDataReader(testConfig,
		// paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		// helpers.VerifyTransactionAfterDenied(rules_values,
		// Integer.parseInt(txnResponxe));
		testConfig.putRunTimeProperty("Risk action taken",
				"Transaction challenged by Risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		Map<String, String> transaction = DataBase.executeSelectQuery(
				testConfig, 100, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1",
				transaction.get("riskAction"));

		Map<String, String> blacklist = DataBase.executeSelectQuery(testConfig,
				15, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied", rules_values[2],
				blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Category scope: create Challenge rule with domestic card, maxcount as checktype for email and do transaction to check it should hit deny rule", dataProvider = "GetTestConfig", groups = { "1" }, timeOut = 600000, priority = 34)
	public void category_Maxcount_Challenge_Rule_Domestic_email(
			Config testConfig) {
		int transactionRow = 127;
		int paymentTypeRow = 5;
		int cardDetailsRow = 1;
		TransactionHelper helpers = new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.merchantListPage = helpers.home.clickMerchantList();
		TestDataReader transactionDetails = new TestDataReader(testConfig,
				tnsdetailsSheet);
		String merchantName = transactionDetails.GetData(transactionRow,
				"Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage = helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values = helpers.rulePage.CreateRule(30, whichRule,
				ruleSheet);
		rules_values[2] = helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		transactionRow = 128;
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		transactionRow = 127;
		// paymentTypeRow=349;
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		// String txnResponxe=new TestDataReader(testConfig,
		// paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		// helpers.VerifyTransactionAfterDenied(rules_values,
		// Integer.parseInt(txnResponxe));
		testConfig.putRunTimeProperty("Risk action taken",
				"Transaction challenged by Risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		Map<String, String> transaction = DataBase.executeSelectQuery(
				testConfig, 100, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1",
				transaction.get("riskAction"));

		Map<String, String> blacklist = DataBase.executeSelectQuery(testConfig,
				15, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied", rules_values[2],
				blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Category scope: create Challenge rule with domestic card, minamount as checktype and do transaction to check it should hit deny rule", dataProvider = "GetTestConfig", groups = { "1" }, timeOut = 600000, priority = 35)
	public void category_Minamount_Challenge_Rule_Domestic(Config testConfig) {
		int transactionRow = 127;
		int paymentTypeRow = 5;
		int cardDetailsRow = 1;
		TransactionHelper helpers = new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.merchantListPage = helpers.home.clickMerchantList();
		TestDataReader transactionDetails = new TestDataReader(testConfig,
				tnsdetailsSheet);
		String merchantName = transactionDetails.GetData(transactionRow,
				"Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage = helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values = helpers.rulePage.CreateRule(31, whichRule,
				ruleSheet);
		rules_values[2] = helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		transactionRow = 128;
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		transactionRow = 127;
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		// String txnResponxe=new TestDataReader(testConfig,
		// paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		// helpers.VerifyTransactionAfterDenied(rules_values,
		// Integer.parseInt(txnResponxe));
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken",
				"Transaction challenged by Risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		Map<String, String> transaction = DataBase.executeSelectQuery(
				testConfig, 100, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1",
				transaction.get("riskAction"));

		Map<String, String> blacklist = DataBase.executeSelectQuery(testConfig,
				15, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied", rules_values[2],
				blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Category scope: create Challenge rule with international card, maxamount as checktype and do transaction to check it should hit deny rule", dataProvider = "GetTestConfig", groups = { "1" }, timeOut = 600000, priority = 36)
	public void category_Maxamount_Challenge_Rule_international(
			Config testConfig) {
		int transactionRow = 127;
		int paymentTypeRow = 348;
		int cardDetailsRow = 68;
		TransactionHelper helpers = new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.merchantListPage = helpers.home.clickMerchantList();
		TestDataReader transactionDetails = new TestDataReader(testConfig,
				tnsdetailsSheet);
		String merchantName = transactionDetails.GetData(transactionRow,
				"Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage = helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values = helpers.rulePage.CreateRule(32, whichRule,
				ruleSheet);
		rules_values[2] = helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		transactionRow = 128;
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		transactionRow = 127;
		paymentTypeRow = 347;
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		// String txnResponxe=new TestDataReader(testConfig,
		// paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken",
				"Transaction challenged by Risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		Map<String, String> transaction = DataBase.executeSelectQuery(
				testConfig, 100, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1",
				transaction.get("riskAction"));

		Map<String, String> blacklist = DataBase.executeSelectQuery(testConfig,
				15, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied", rules_values[2],
				blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "maxcount as check type for card_hash: Creating a Rule and do the transaction to check it should hit challenge rule", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1", priority = 37)
	public void category_MaxCount_challenge_rule_Both_email(Config testConfig) {
		int transactionRow = 127;
		int paymentTypeRow = 348;
		int cardDetailsRow = 68;
		TransactionHelper helpers = new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.merchantListPage = helpers.home.clickMerchantList();
		TestDataReader transactionDetails = new TestDataReader(testConfig,
				tnsdetailsSheet);
		String merchantName = transactionDetails.GetData(transactionRow,
				"Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage = helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values = helpers.rulePage.CreateRule(34, whichRule,
				ruleSheet);
		rules_values[2] = helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		// testConfig.putRunTimeProperty("email",
		// Helper.generateRandomAlphabetsString(6)+"@gmail.com");
		transactionRow = 128;
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		transactionRow = 127;
		paymentTypeRow = 5;
		cardDetailsRow = 1;
		// paymentTypeRow=349;
		// testConfig.putRunTimeProperty("email",
		// Helper.generateRandomAlphabetsString(6)+"@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		// String txnResponxe=new TestDataReader(testConfig,
		// paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		// helpers.VerifyTransactionAfterDenied(rules_values,
		// Integer.parseInt(txnResponxe));
		testConfig.putRunTimeProperty("Risk action taken",
				"Transaction challenged by Risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		Map<String, String> transaction = DataBase.executeSelectQuery(
				testConfig, 100, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1",
				transaction.get("riskAction"));

		Map<String, String> blacklist = DataBase.executeSelectQuery(testConfig,
				15, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied", rules_values[2],
				blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "maxcount as check type for card_hash: Creating a Rule and do the transaction to check it should hit challenge rule", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1", priority = 38)
	public void category_Sumamount_challenge_rule_phone_Domestic(
			Config testConfig) {
		int transactionRow = 127;
		int paymentTypeRow = 5;
		int cardDetailsRow = 1;
		TransactionHelper helpers = new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.merchantListPage = helpers.home.clickMerchantList();
		TestDataReader transactionDetails = new TestDataReader(testConfig,
				tnsdetailsSheet);
		String merchantName = transactionDetails.GetData(transactionRow,
				"Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage = helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values = helpers.rulePage.CreateRule(35, whichRule,
				ruleSheet);
		rules_values[2] = helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		// testConfig.putRunTimeProperty("phone",
		// Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		transactionRow = 128;
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		testConfig.putRunTimeProperty("Risk action taken", "No risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		helpers.GetTestTransactionPage();
		transactionRow = 127;
		// paymentTypeRow=349;
		testConfig.putRunTimeProperty("email",
				Helper.generateRandomAlphabetsString(6) + "@gmail.com");
		// testConfig.putRunTimeProperty("phone",
		// Helper.generateRandomNumber(11));
		helpers.testResponse = (TestResponsePage) helpers.DoTestTransaction(
				transactionRow, paymentTypeRow, cardDetailsRow,
				ExpectedResponsePage.TestResponsePage);
		// String txnResponxe=new TestDataReader(testConfig,
		// paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.testResponse.overrideExpectedTransactionData = true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData,
				transactionRow, helpers.paymentTypeData, paymentTypeRow);
		// helpers.VerifyTransactionAfterDenied(rules_values,
		// Integer.parseInt(txnResponxe));
		testConfig.putRunTimeProperty("Risk action taken",
				"Transaction challenged by Risk");
		helpers.verifyChallengedTxn(helpers, testConfig, transactionRow,
				paymentTypeRow, cardDetailsRow);
		Map<String, String> transaction = DataBase.executeSelectQuery(
				testConfig, 100, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "1",
				transaction.get("riskAction"));

		Map<String, String> blacklist = DataBase.executeSelectQuery(testConfig,
				15, 1, DatabaseType.Offline);
		Helper.compareEquals(testConfig, "Rule applied", rules_values[2],
				blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
}
