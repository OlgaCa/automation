package Test.AdminPanel.RiskRules.PerMerchantRules;
/*
 * Flow of automating test rule
 * @author: Himanshu
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.DataBase.DatabaseType;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestDenyRule extends TestBase{

	@Test(description = "MaxCount on Email: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=1)
	public void Maxcount_deny_email_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule = 1; // 1 for deny rule 
		int numOfTxns = 2;
		int RiskRuleRow = 1;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, 
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}	


	@Test(description = "MaxCount on Phone: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=2)
	public void Maxcount_deny_phone_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =2;
		int RiskRuleRow = 2;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(10)));
		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}	

	// need two IPs
	@Test(description = "MaxCount on IP: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=3)
	public void Maxcount_deny_ip_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 3;
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

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}	

	//need two cards: Get the other card
	@Test(description = "MaxCount on CardHash: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=4)
	public void Maxcount_deny_cardHash_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 4;
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

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}	

	//COD Phone does not work, redmine raised
	/*@Test(description = "MaxCount on Cod Phone: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_deny_codPhone_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; 
		int numOfTxns =1;
		int RiskRuleRow = 5;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);
		//COD Phone does not work, redmine raised
		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}	*/


	@Test(description = "MaxCountDistinct on Email: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=5)
	public void MaxcountDistinct_deny_email_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =2;
		int RiskRuleRow = 10;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 53, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, 
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "MaxCountDistinct on cardHash: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=6)
	public void MaxcountDistinct_deny_cardHash_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =2;
		int RiskRuleRow = 9;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 242;
		int cardDetailsRowNum = 19;
		int txnResponseRowNum = 18;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");
		
		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 78, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "MaxCountDistinct on email: Creating a Rule and do the transaction to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=7)
	public void MaxcountDistinct_deny_email_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 10;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 53, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}		

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Minamount: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=8)
	public void Minamount_deny_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 16;
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

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Maxamount: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=9)
	public void Maxamount_deny_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
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

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//This test case won't work 
	/*@Test(description = "CCHash: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void CCHash_deny_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; 
		int numOfTxns =1;
		int RiskRuleRow = 18;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}*/


	@Test(description = "Sumamount on cardhash: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=10)
	public void Sumamount_deny_cardHash_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 14;
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

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Sumamount on Email: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=11)
	public void Sumamount_deny_email_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =2;
		int RiskRuleRow = 11;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, 
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//need two IPs
	@Test(description = "Sumamount on IP: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=12)
	public void Sumamount_deny_IP_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 13;
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

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Sumamount on Phone: Creating a Rule and do the transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=13)
	public void Sumamount_deny_phone_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =2;
		int RiskRuleRow = 12;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(10)));
		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, 
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}


	//Negative Test Cases
	@Test(description = "Maxcount on Email: Creating a Rule and do the transaction only once to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=14)
	public void Maxcount_deny_email_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 1;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Maxcount on Phone: Creating a Rule and do the transaction only once to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=15)
	public void Maxcount_deny_phone_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 2;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(10)));

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Need two IPs
	@Test(description = "Maxcount on IP: Creating a Rule and do the transaction only once to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=16)
	public void Maxcount_deny_ip_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 3;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, 
				transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}

	//Card has been used multiple times, so rule will be always applicable
	/*@Test(description = "Maxcount on Card Hash: Creating a Rule and do the transaction only once to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
		public void Maxcount_deny_cardHash_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 4;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, 
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	//cod Phone doesn't work
	@Test(description = "Maxcount on Cod Phone (negative): Creating a Rule and do the transaction only once to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxcount_deny_codPhone_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; 
		int numOfTxns =1;
		int RiskRuleRow = 5;
		//	int MerchantParamRow = 1;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}*/

	@Test(description = "MaxCountDistinct on cardHash (negative): Creating a Rule and do the transaction to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=17)
	public void MaxcountDistinct_deny_cardHash_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 21;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		Map<String, String> txnMap = DataBase.executeSelectQuery(testConfig, 78, 1);
		String count = txnMap.get("count");
		testConfig.putRunTimeProperty("count", count);

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Minamount Negative Case: Creating a Rule and do the transaction and check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=18)
	public void Minamount_negative_deny_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 19;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}


	@Test(description = "Maxamount Negative Case: Creating a Rule and do the transaction and check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=19)
	public void Maxamount_negative_deny_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 20;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	/*
	 //Same card has been used mutiple times, so rule will be applicable
	@Test(description = "Sumamount on cardhash(negative): Creating a Rule and do the transaction to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Sumamount_deny_cardHash_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 14;
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

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	
	@Test(description = "Sumamount on IP(negative): Creating a Rule and do the transaction to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Sumamount_deny_IP_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; 
		int numOfTxns =1;
		int RiskRuleRow = 13;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 234;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}*/


	@Test(description = "Sumamount on Email: Creating a Rule and do the transaction to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=20)
	public void Sumamount_deny_email_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 11;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Sumamount on Phone (negative): Creating a Rule in all categories and do the transaction to check it should not hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=21)
	public void Sumamount_deny_phone_negative_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 12;
		int transactionRowNum = 36;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(10)));
		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}

	@Test(description = "Merchant scope: create Deny rule with domestic card, sumamount. Do transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=1)
	public void Merchant_sumamount_deny_rule_domestic(Config testConfig){
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int whichRule =1;
		int numOfTxns =2;
		Map<String,String> sumamount = new HashMap<String, String>();
//		sumamount.put("email", "34,111,5,349,1,58,");
//		sumamount.put("phone", "35,112,5,349,1,58,");
		sumamount.put("ip", "37,107,5,349,1,58,");
//		sumamount.put("card_hash", "36,114,5,349,1,58,");
		Iterator<String> mit=sumamount.keySet().iterator();
		while(mit.hasNext()){
			String paramater=mit.next();
			testConfig.logWarning("executing script for parameter : "+paramater);
			String[] inputDataRows=sumamount.get(paramater).split(",");
			int RiskRuleRow = Integer.parseInt(inputDataRows[0]);
			int transactionRowNum = Integer.parseInt(inputDataRows[1]);
			int failureresponcepaymentTypeRowNum = Integer.parseInt(inputDataRows[2]);
			int successresponcepaymentTyprRowNum=Integer.parseInt(inputDataRows[3]);
			int cardDetailsRowNum = Integer.parseInt(inputDataRows[4]);
			int txnResponseRowNum = Integer.parseInt(inputDataRows[5]);
		
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
		String rules_values[] = helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum,
				failureresponcepaymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true,true);
		testConfig.logComment("rule values:" + rules_values);
		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData,successresponcepaymentTyprRowNum );
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "2", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction",rules_values[2] , blacklist.get("ruleid"));
		}
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Merchant scope: create Deny rule with international card, sumamount. Do transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=2)
	public void Merchant_sumamount_deny_rule_international(Config testConfig){
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int whichRule =1;
		int numOfTxns =2;
		Map<String,String> sumamount = new HashMap<String, String>();
//		sumamount.put("email", "38,115,348,347,68,57,");
//		sumamount.put("phone", "39,116,5,349,68,58,");
//		sumamount.put("ip", "41,117,5,349,68,58,");
		sumamount.put("card_hash", "40,107,5,349,68,58");
		Iterator<String> mit=sumamount.keySet().iterator();
		while(mit.hasNext()){
			String paramater=mit.next();
			testConfig.logWarning("executing script for parameter : "+paramater);
			String[] inputDataRows=sumamount.get(paramater).split(",");
			int RiskRuleRow = Integer.parseInt(inputDataRows[0]);
			int transactionRowNum = Integer.parseInt(inputDataRows[1]);
			int successresponcepaymentTyprRowNum = Integer.parseInt(inputDataRows[2]);
			int failureresponcepaymentTypeRowNum =Integer.parseInt(inputDataRows[3]);
			int cardDetailsRowNum = Integer.parseInt(inputDataRows[4]);
			int txnResponseRowNum = Integer.parseInt(inputDataRows[5]);
			testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
			testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
		
		String RuleSheet = "DenyRule";

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum,
				successresponcepaymentTyprRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true,true);

		testConfig.logComment("rule values:" + rules_values);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData,failureresponcepaymentTypeRowNum );
		helper.testResponse.overrideExpectedTransactionData=true;
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "2", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction",rules_values[2] , blacklist.get("ruleid"));
		helper.GetTestMerchantTransactionPage();
		}
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Merchant scope: create Deny rule with domestic card, maxcount as checktype for phone. Do transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=3)
	public void Merchant_maxcount_deny_rule_domestic_phone(Config testConfig){
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		int whichRule =1;
		int numOfTxns =3;
		int RiskRuleRow = 42;
		int transactionRowNum = 107;
		int successresponcepaymentTyprRowNum = 5;
		int failureresponcepaymentTypeRowNum =349;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 58;
	
	String RuleSheet = "DenyRule";

	TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
	String merchant = dataReader.GetData(transactionRowNum, "Comments");
	testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
	String rules_values[] = helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum,
			successresponcepaymentTyprRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true,true);

	if (helper.testResponse.actualResponse.get("status").equals("failure")) {
		helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
	}
	helper.testResponse.overrideExpectedTransactionData=true;
	helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData,failureresponcepaymentTypeRowNum );
	Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
	Helper.compareEquals(testConfig, "riskaction", "2", transaction.get("riskAction"));
	
	Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
	Helper.compareEquals(testConfig, "riskaction",rules_values[2] , blacklist.get("ruleid"));
	Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Merchant scope: create Deny rule with international card, maxcount as checktype for card_hash. Do transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=4)
	public void Merchant_maxcount_deny_rule_international_cardhash(Config testConfig){
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		int whichRule =1;
		int numOfTxns =3;
		int RiskRuleRow = 46;
		int transactionRowNum = 108;
		int successresponcepaymentTyprRowNum = 348;
		int failureresponcepaymentTypeRowNum =347;
		int cardDetailsRowNum = 68;
		int txnResponseRowNum = 57;
	
	String RuleSheet = "DenyRule";
	testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
	testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

	TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
	String merchant = dataReader.GetData(transactionRowNum, "Comments");

	String rules_values[] = helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum,
			successresponcepaymentTyprRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true,true);

	if (helper.testResponse.actualResponse.get("status").equals("failure")) {
		helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
	}
	helper.testResponse.overrideExpectedTransactionData=true;
	helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData,failureresponcepaymentTypeRowNum );
	Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
	Helper.compareEquals(testConfig, "riskaction", "2", transaction.get("riskAction"));
	
	Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
	Helper.compareEquals(testConfig, "riskaction",rules_values[2] , blacklist.get("ruleid"));
	Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Merchant scope: create Deny rule with both card, maxcount as checktype for email . Do transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=5)
	public void Merchant_maxcount_deny_rule_both_email(Config testConfig){
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		int whichRule =1;
		int RiskRuleRow = 48;
		int transactionRowNum = 107;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 1;
		String RuleSheet = "DenyRule";
		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
		
		String homeUrl = testConfig.getRunTimeProperty("AdminPortalHome");
		Browser.navigateToURL(testConfig, homeUrl);
		helper.merchantListPage = helper.home.clickMerchantList();

		helper.merchantListPage.SearchMerchant(merchant);
		helper.rulePage = helper.merchantListPage.clickMerchantTxnRule();
		helper.rulePage.DeleteExistingRules(); 

		//To add switch case here to select option of rule 
		String rules_values[] = helper.rulePage.CreateRule(RiskRuleRow, whichRule, RuleSheet);

		//To verify both txn, First should be successful,
		String ruleid = helper.rulePage.SelectRule();

		rules_values[2] = ruleid;
		
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("phone" , Helper.generateRandomNumber(11));
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		paymentTypeRowNum = 347;
		cardDetailsRowNum = 68;
		txnResponseRowNum = 57;
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("phone" , Helper.generateRandomNumber(11));
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "2", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);//mihpayid
		Helper.compareEquals(testConfig, "riskaction",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description="Merchant scope: creating Deny rule with domestic card, maxamount as checktype and do transaction to check it should hit deny rule", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=6)
	public void Merchant_Maxamount_deny_domestic(Config testConfig) {		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =1; // 1 for deny rule
		int numOfTxns =1;
		int RiskRuleRow = 22;
		int transactionRowNum = 107;
		int paymentTypeRowNum = 349;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 12;
		String RuleSheet = "DenyRule";
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
		
		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");
		
		String rules_values[] = helper.CreateRuleDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}

		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Assert.assertTrue(testConfig.getTestResult());		
	}
	
	@Test(description = "Merchant scope: create Deny rule with international card, minamount as checktype and do transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=7)
	public void Merchant_minamount_deny_rule_international(Config testConfig){
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		int whichRule =1;
		int numOfTxns =1;
		int RiskRuleRow = 30;
		int transactionRowNum = 107;
		int paymentTypeRowNum = 347;
		int cardDetailsRowNum = 68;
		int txnResponseRowNum = 57;
		String RuleSheet = "DenyRule";
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");

		String rules_values[] = helper.CreateRuleAndDoTransaction(testConfig, helper, RiskRuleRow, transactionRowNum,
				paymentTypeRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true);

		testConfig.logComment("rule values:" + rules_values);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "2", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);//mihpayid
		Helper.compareEquals(testConfig, "riskaction",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Merchant scope: create Deny rule with both card(s), maxcountdistinct as checktype cardhash for email. Do transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=8)
	public void Merchant_maxcountdistinct_deny_rule_both_cardhashforemail(Config testConfig){
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		int whichRule =1;
		int RiskRuleRow = 67;
		int transactionRowNum = 109;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 1;
		String RuleSheet = "DenyRule";
		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
		
		String homeUrl = testConfig.getRunTimeProperty("AdminPortalHome");
		Browser.navigateToURL(testConfig, homeUrl);
		helper.merchantListPage = helper.home.clickMerchantList();

		helper.merchantListPage.SearchMerchant(merchant);
		helper.rulePage = helper.merchantListPage.clickMerchantTxnRule();
		helper.rulePage.DeleteExistingRules(); 

		//To add switch case here to select option of rule 
		String rules_values[] = helper.rulePage.CreateRule(RiskRuleRow, whichRule, RuleSheet);

		//To verify both txn, First should be successful,
		String ruleid = helper.rulePage.SelectRule();

		rules_values[2] = ruleid;
		
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		
		paymentTypeRowNum = 347;
		cardDetailsRowNum = 68;
		txnResponseRowNum = 57;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "2", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);//mihpayid
		Helper.compareEquals(testConfig, "riskaction",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Merchant scope: create Deny rule with domesticard, maxcountdistinct as checktype email for card_hash. Do transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=9)
	public void Merchant_maxcountdistinct_deny_rule_domestic_emailforcardhash(Config testConfig){

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		int whichRule =1;
		int numOfTxns =2;
		int RiskRuleRow = 61;
		int transactionRowNum = 108;
		int successresponcepaymentTyprRowNum = 5;
		int failureresponcepaymentTypeRowNum =349;
		int cardDetailsRowNum = 1;
		int txnResponseRowNum = 58;
		String RuleSheet = "DenyRule";
		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchant = dataReader.GetData(transactionRowNum, "Comments");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");

		String rules_values[] = helper.CreateRuleAndDoTransactionPay(testConfig, helper, RiskRuleRow, transactionRowNum,
				successresponcepaymentTyprRowNum, cardDetailsRowNum, merchant, numOfTxns, whichRule, RuleSheet, true,true);
		if (helper.testResponse.actualResponse.get("status").equals("failure")) {
			helper.VerifyTransactionAfterDenied(rules_values, txnResponseRowNum);
		}
		helper.testResponse.overrideExpectedTransactionData=true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, failureresponcepaymentTypeRowNum);
		Map<String, String> transaction=DataBase.executeSelectQuery(testConfig,100 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction", "2", transaction.get("riskAction"));
		
		Map<String, String> blacklist=DataBase.executeSelectQuery(testConfig,15 ,1 , DatabaseType.Offline);
		Helper.compareEquals(testConfig, "riskaction",rules_values[2] , blacklist.get("ruleid"));
		Assert.assertTrue(testConfig.getTestResult());
	}
}