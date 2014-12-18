package Test.AdminPanel.RiskRules.PerMerchantRules;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;


public class TestCategoryScopeDenyRules extends TestBase{
	
	private final int whichRule=1;
	private final String ruleSheet="DenyRule";
	private final String paymentTypeSheet="PaymentType";
	private final String tnsdetailsSheet="TransactionDetails";

	
	
	@Test(description = "Category scope: create Deny rule with domestic card, sumamount as checktype. Do transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=1)
	public void category_sumamount_Deny_Rule_domestic(Config testConfig){
		TransactionHelper helpers = new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		
		Map<String,Integer> sumamount = new LinkedHashMap<String, Integer>();
		sumamount.put("ip",71);
//		sumamount.put("email", 79);
//		sumamount.put("phone", 80);
//		sumamount.put("card_hash", 81);
		Iterator<String> mit=sumamount.keySet().iterator();
		while(mit.hasNext()){
			String paramater=mit.next();
			testConfig.logWarning("executing sumamount rule for parameter : "+paramater);
			testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(6)+"@gmail.com");
			testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
			int RiskRuleRow = sumamount.get(paramater);
			int transactionRowNum = 111;//144
			int transactionRowNum2 = 112;
			int successresponcepaymentTyprRowNum = 5;
			int failureresponcepaymentTypeRowNum=349;
			int cardDetailsRowNum = 1;
			helpers.merchantListPage=helpers.home.clickMerchantList();
			TestDataReader transactionDetails= new TestDataReader(testConfig, tnsdetailsSheet);
			String merchantName=transactionDetails.GetData(transactionRowNum, "Comments");
			helpers.merchantListPage.SearchMerchant(merchantName);
			helpers.rulePage=helpers.merchantListPage.clickMerchantTxnRule();
			helpers.rulePage.DeleteExistingRules();
			String[] rules_values=helpers.rulePage.CreateRule(RiskRuleRow, whichRule, ruleSheet);
			rules_values[2]=helpers.rulePage.SelectRule();
			helpers.GetTestTransactionPage();
			helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRowNum, successresponcepaymentTyprRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			helpers.testResponse.overrideExpectedTransactionData=true;
			helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRowNum, helpers.paymentTypeData, successresponcepaymentTyprRowNum);
			helpers.GetTestTransactionPage();
			helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRowNum2, successresponcepaymentTyprRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRowNum2, helpers.paymentTypeData, successresponcepaymentTyprRowNum);
			helpers.GetTestTransactionPage();
			helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRowNum, failureresponcepaymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			String txnResponxe=new TestDataReader(testConfig, paymentTypeSheet).GetData(failureresponcepaymentTypeRowNum, "TestResponseRow");
			helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRowNum, helpers.paymentTypeData, failureresponcepaymentTypeRowNum);
			helpers.VerifyTransactionAfterDenied(rules_values, Integer.parseInt(txnResponxe));
		}
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Category scope: create Deny rule with international card, sumamount as checktype. Do transaction to check it should hit deny rule",
			dataProvider="GetTestConfig", timeOut=600000, groups="1",priority=2)
	public void category_sumamount_Deny_Rule_international(Config testConfig){
		TransactionHelper helpers = new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		
		Map<String,Integer> sumamount = new LinkedHashMap<String, Integer>();
//		sumamount.put("ip",86);
//		sumamount.put("email", 83);
//		sumamount.put("phone", 84);
		sumamount.put("card_hash", 72);
		Iterator<String> mit=sumamount.keySet().iterator();
		while(mit.hasNext()){
			String paramater=mit.next();
			testConfig.logWarning("executing sumamount rule for parameter : "+paramater);
			testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(6)+"@gmail.com");
			testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
			int RiskRuleRow = sumamount.get(paramater);
			int transactionRowNum = 111;//144
			int transactionRowNum2 = 112;
			int successresponcepaymentTyprRowNum = 348;
			int failureresponcepaymentTypeRowNum=347;
			int cardDetailsRowNum = 68;
			helpers.merchantListPage=helpers.home.clickMerchantList();
			TestDataReader transactionDetails= new TestDataReader(testConfig, tnsdetailsSheet);
			String merchantName=transactionDetails.GetData(transactionRowNum, "Comments");
			helpers.merchantListPage.SearchMerchant(merchantName);
			helpers.rulePage=helpers.merchantListPage.clickMerchantTxnRule();
			helpers.rulePage.DeleteExistingRules();
			String[] rules_values=helpers.rulePage.CreateRule(RiskRuleRow, whichRule, ruleSheet);
			rules_values[2]=helpers.rulePage.SelectRule();
			helpers.GetTestTransactionPage();
			helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRowNum, successresponcepaymentTyprRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			helpers.testResponse.overrideExpectedTransactionData=true;
			helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRowNum, helpers.paymentTypeData, successresponcepaymentTyprRowNum);
			helpers.GetTestTransactionPage();
			helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRowNum2, successresponcepaymentTyprRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRowNum2, helpers.paymentTypeData, successresponcepaymentTyprRowNum);
			helpers.GetTestTransactionPage();
			helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRowNum, failureresponcepaymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			String txnResponxe=new TestDataReader(testConfig, paymentTypeSheet).GetData(failureresponcepaymentTypeRowNum, "TestResponseRow");
			helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRowNum, helpers.paymentTypeData, failureresponcepaymentTypeRowNum);
			helpers.VerifyTransactionAfterDenied(rules_values, Integer.parseInt(txnResponxe));
		}
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Category scope: create Deny rule with domestic card, maxcount as checktype for email and do transaction to check it should hit deny rule", 
			dataProvider = "GetTestConfig", groups = { "1" },timeOut=600000,priority=3)
	public void category_Maxcount_Deny_Rule_Domestic_email(Config testConfig) {
		int transactionRow=111;
		int paymentTypeRow=5;
		int cardDetailsRow=1;
		TransactionHelper helpers=new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(6)+"@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.merchantListPage=helpers.home.clickMerchantList();
		TestDataReader transactionDetails= new TestDataReader(testConfig, tnsdetailsSheet);
		String merchantName=transactionDetails.GetData(transactionRow, "Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage=helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values=helpers.rulePage.CreateRule(73, whichRule, ruleSheet);
		rules_values[2]=helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData=true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		helpers.GetTestTransactionPage();
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		transactionRow=112;
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		helpers.testResponse.overrideExpectedTransactionData=true;
		helpers.GetTestTransactionPage();
		transactionRow=111;
		paymentTypeRow=349;
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		String txnResponxe=new TestDataReader(testConfig, paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		helpers.VerifyTransactionAfterDenied(rules_values, Integer.parseInt(txnResponxe));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test( description = "Category scope: create Deny rule with domestic card, minamount as checktype and do transaction to check it should hit deny rule",
			dataProvider = "GetTestConfig", groups = { "1" },timeOut=600000,priority=4)
	public void category_Minamount_Deny_Rule_Domestic(Config testConfig) {
		int transactionRow=111;
		int paymentTypeRow=5;
		int cardDetailsRow=1;
		TransactionHelper helpers=new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(6)+"@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.merchantListPage=helpers.home.clickMerchantList();
		TestDataReader transactionDetails= new TestDataReader(testConfig, tnsdetailsSheet);
		String merchantName=transactionDetails.GetData(transactionRow, "Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage=helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values=helpers.rulePage.CreateRule(74, whichRule, ruleSheet);
		rules_values[2]=helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		transactionRow=112;
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData=true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		helpers.GetTestTransactionPage();
		transactionRow=111;
		paymentTypeRow=349;
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData=true;
		String txnResponxe=new TestDataReader(testConfig, paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.VerifyTransactionAfterDenied(rules_values, Integer.parseInt(txnResponxe));
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test( description = "Category scope: create Deny rule with international card, maxamount as checktype and do transaction to check it should hit deny rule",
			dataProvider = "GetTestConfig", groups = { "1" },timeOut=600000,priority=5)
	public void category_Maxamount_Deny_Rule_international(Config testConfig) {
		int transactionRow=111;
		int paymentTypeRow=348;
		int cardDetailsRow=68;
		TransactionHelper helpers=new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(6)+"@gmail.com");
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.merchantListPage=helpers.home.clickMerchantList();
		TestDataReader transactionDetails= new TestDataReader(testConfig, tnsdetailsSheet);
		String merchantName=transactionDetails.GetData(transactionRow, "Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage=helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values=helpers.rulePage.CreateRule(75, whichRule, ruleSheet);
		rules_values[2]=helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		transactionRow=112;
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData=true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		helpers.GetTestTransactionPage();
		transactionRow=111;
		paymentTypeRow=347;
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData=true;
		String txnResponxe=new TestDataReader(testConfig, paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.VerifyTransactionAfterDenied(rules_values, Integer.parseInt(txnResponxe));
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Category scope: create Deny rule with both card, maxcount as checktype for phone. Do transaction to check it should hit deny rule", 
			dataProvider = "GetTestConfig", groups = { "1" },timeOut=600000,priority=6)
	public void category_Maxcount_Deny_Rule_Both_phone(Config testConfig) {
		int transactionRow=111;
		int paymentTypeRow=348;
		int cardDetailsRow=68;
		TransactionHelper helpers=new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		helpers.merchantListPage=helpers.home.clickMerchantList();
		TestDataReader transactionDetails= new TestDataReader(testConfig, tnsdetailsSheet);
		String merchantName=transactionDetails.GetData(transactionRow, "Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage=helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values=helpers.rulePage.CreateRule(76, whichRule, ruleSheet);
		rules_values[2]=helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData=true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		helpers.GetTestTransactionPage();
		transactionRow=112;
		paymentTypeRow=5;
		cardDetailsRow=1;
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData=true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		helpers.GetTestTransactionPage();
		transactionRow=111;
		paymentTypeRow=347;
		cardDetailsRow=68;
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		String txnResponxe=new TestDataReader(testConfig, paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.testResponse.overrideExpectedTransactionData=true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		helpers.VerifyTransactionAfterDenied(rules_values, Integer.parseInt(txnResponxe));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Category scope: create Deny rule with domestic card, maxcountdistinct as checktype for email and card_hash. Do transaction to check it should hit deny rule", 
		dataProvider = "GetTestConfig", groups = { "1" },timeOut=600000,priority=7)
	public void category_Maxcountdistinct_Deny_Rule_Domestic_emailforcardhash(Config testConfig) {
		int transactionRow=113;
		int paymentTypeRow=5;
		int cardDetailsRow=1;
		TransactionHelper helpers=new TransactionHelper(testConfig, false);
		helpers.DoLogin();
		helpers.merchantListPage=helpers.home.clickMerchantList();
		TestDataReader transactionDetails= new TestDataReader(testConfig, tnsdetailsSheet);
		String merchantName=transactionDetails.GetData(transactionRow, "Comments");
		helpers.merchantListPage.SearchMerchant(merchantName);
		helpers.rulePage=helpers.merchantListPage.clickMerchantTxnRule();
		helpers.rulePage.DeleteExistingRules();
		String[] rules_values=helpers.rulePage.CreateRule(77, whichRule, ruleSheet);
		rules_values[2]=helpers.rulePage.SelectRule();
		helpers.GetTestTransactionPage();
		testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData=true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		helpers.GetTestTransactionPage();
		transactionRow=114;
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		helpers.testResponse.overrideExpectedTransactionData=true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		helpers.GetTestTransactionPage();
		transactionRow=113;
		paymentTypeRow=349;
		testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
		helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
		String txnResponxe=new TestDataReader(testConfig, paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
		helpers.testResponse.overrideExpectedTransactionData=true;
		helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
		helpers.VerifyTransactionAfterDenied(rules_values, Integer.parseInt(txnResponxe));
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Category scope: create Deny rule with international card, maxcount as checktype for card_hash. Do transaction to check it should hit deny rule", 
			dataProvider = "GetTestConfig", groups = { "1" },timeOut=600000,priority=8)
	public void category_Maxcount_Deny_Rule_International_cardhash(Config testConfig) {
			int transactionRow=113;
			int paymentTypeRow=348;
			int cardDetailsRow=68;
			TransactionHelper helpers=new TransactionHelper(testConfig, false);
			helpers.DoLogin();
			helpers.merchantListPage=helpers.home.clickMerchantList();
			TestDataReader transactionDetails= new TestDataReader(testConfig, tnsdetailsSheet);
			String merchantName=transactionDetails.GetData(transactionRow, "Comments");
			helpers.merchantListPage.SearchMerchant(merchantName);
			helpers.rulePage=helpers.merchantListPage.clickMerchantTxnRule();
			helpers.rulePage.DeleteExistingRules();
			String[] rules_values=helpers.rulePage.CreateRule(78, whichRule, ruleSheet);
			rules_values[2]=helpers.rulePage.SelectRule();
			helpers.GetTestTransactionPage();
			testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
			testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
			helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
			helpers.testResponse.overrideExpectedTransactionData=true;
			helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
			helpers.GetTestTransactionPage();
			transactionRow=114;
			testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
			helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
			helpers.testResponse.overrideExpectedTransactionData=true;
			helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
			helpers.GetTestTransactionPage();
			transactionRow=113;
			paymentTypeRow=347;
			testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
			helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
			String txnResponxe=new TestDataReader(testConfig, paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
			helpers.testResponse.overrideExpectedTransactionData=true;
			helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
			helpers.VerifyTransactionAfterDenied(rules_values, Integer.parseInt(txnResponxe));
			Assert.assertTrue(testConfig.getTestResult());
		}
	
	@Test(description = "Category scope: create Deny rule with both card, maxcountdistinct as checktype card_hash for email. Do transaction to check it should hit deny rule", 
			dataProvider = "GetTestConfig", groups = { "1" },timeOut=600000,priority=9)
	public void category_Maxcountdistinct_Deny_Rule_Both_cardhashforemail(Config testConfig) {
			int transactionRow=115;
			int paymentTypeRow=348;
			int cardDetailsRow=68;
			TransactionHelper helpers=new TransactionHelper(testConfig, false);
			helpers.DoLogin();
			helpers.merchantListPage=helpers.home.clickMerchantList();
			TestDataReader transactionDetails= new TestDataReader(testConfig, tnsdetailsSheet);
			String merchantName=transactionDetails.GetData(transactionRow, "Comments");
			helpers.merchantListPage.SearchMerchant(merchantName);
			helpers.rulePage=helpers.merchantListPage.clickMerchantTxnRule();
			helpers.rulePage.DeleteExistingRules();
			String[] rules_values=helpers.rulePage.CreateRule(79,whichRule, ruleSheet);
			rules_values[2]=helpers.rulePage.SelectRule();
			helpers.GetTestTransactionPage();
			transactionRow=116;
			testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
			testConfig.putRunTimeProperty("email", Helper.generateRandomAlphabetsString(5)+"@gmail.com");
			helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
			helpers.testResponse.overrideExpectedTransactionData=true;
			helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
			helpers.GetTestTransactionPage();
			transactionRow=115;
			paymentTypeRow=349;
			cardDetailsRow=1;
			testConfig.putRunTimeProperty("phone", Helper.generateRandomNumber(11));
			helpers.testResponse=(TestResponsePage)helpers.DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, ExpectedResponsePage.TestResponsePage);
			String txnResponxe=new TestDataReader(testConfig, paymentTypeSheet).GetData(paymentTypeRow, "TestResponseRow");
			helpers.testResponse.overrideExpectedTransactionData=true;
			helpers.testResponse.VerifyTransactionResponse(helpers.transactionData, transactionRow, helpers.paymentTypeData, paymentTypeRow);
			helpers.VerifyTransactionAfterDenied(rules_values, Integer.parseInt(txnResponxe));
			Assert.assertTrue(testConfig.getTestResult());
		}
}