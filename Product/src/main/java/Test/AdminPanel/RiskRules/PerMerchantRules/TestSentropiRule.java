package Test.AdminPanel.RiskRules.PerMerchantRules;

/*
 * Change the parameters in testcase as per rule 
 * you want to create
 */
import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestSentropiRule extends TestBase{
	/*
	 * Removing this code, as Sentropi is no longer supported.
	 * @Urvashi Ahuja
	 * 
	@Test(description = "Maxamount: Creating a Rule in all categories and do the transaction to check it should hit challenge rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Maxamount_challenge_rule(Config testConfig) {

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int whichRule =3; // 3 for Sentropi rule 
		int numOfTxns =1;
		int RiskRuleRow = 17;
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
		testConfig.logComment("It's a sentropi rule");
		helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
		Assert.assertTrue(testConfig.getTestResult());

	}
	*/
}