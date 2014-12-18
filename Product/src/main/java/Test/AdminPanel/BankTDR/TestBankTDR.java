package Test.AdminPanel.BankTDR;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.BankTDR.BankTDRPage;
import PageObject.AdminPanel.BankTDR.EditBankTDRPage;
import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;

public class TestBankTDR extends TestBase{
	
	
	@Test(description = "Verify add bank TDR rule", 
			dataProvider="GetTestConfig", timeOut=600000, groups ="1")
	public void addBankTDR(Config testConfig)
	{
		int BankTDRrow = 1;
		String Sheet = "BankTDR";
		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		HomePage home = new HomePage(testConfig);
		BankTDRPage bankTDRPage = home.ClickBankTDR();
		EditBankTDRPage editBankTDRPage = bankTDRPage.ClickEditBankTDR();
	
		editBankTDRPage.CreateTDRRule(BankTDRrow, Sheet);
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "CC Bank TDR rule transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void CCbankTDRRuleHit(Config testConfig)
	{
		int transactionRowNum = 103;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int BankTDRrow = 2;
		String Sheet = "BankTDR";
		
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		HomePage home = new HomePage(testConfig);
		BankTDRPage bankTDRPage = home.ClickBankTDR();
		EditBankTDRPage editBankTDRPage = bankTDRPage.ClickEditBankTDR();
	
		//bankTDRPage.deleteExistingRules();
		int idBefore=helper.verifyBankTDRruleID(testConfig);
		editBankTDRPage.CreateTDRRule(BankTDRrow, Sheet);	
		int idAfter=helper.verifyBankTDRruleID(testConfig);
		
		if (idAfter > idBefore)
		{
			//Bank TDR rule created
			System.out.println("Bank TDR rule created " + idAfter);
			
			Float t_amount = 100.00f; // for CC
			testConfig.putRunTimeProperty("amount", t_amount.toString());
			String t_firstname = "Bank TDR Automation";
			testConfig.putRunTimeProperty("firstname", t_firstname);
			helper.GetTestTransactionPage();
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		}
		else {
			testConfig.logFail("Bank TDR rule could not be created");
		}
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "DC Bank TDR rule transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void DCBankTDRRuleHit(Config testConfig)
	{
		int transactionRowNum = 103;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int BankTDRrow = 3;
		String Sheet = "BankTDR";
		
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		HomePage home = new HomePage(testConfig);
		BankTDRPage bankTDRPage = home.ClickBankTDR();
		EditBankTDRPage editBankTDRPage = bankTDRPage.ClickEditBankTDR();
	
		//bankTDRPage.deleteExistingRules();
		int idBefore=helper.verifyBankTDRruleID(testConfig);
		editBankTDRPage.CreateTDRRule(BankTDRrow, Sheet);	
		int idAfter=helper.verifyBankTDRruleID(testConfig);
		
		if (idAfter > idBefore)
		{		
			//Bank TDR rule created
			System.out.println("Bank TDR rule created " + idAfter);

			float t_amount = 200; // for DC
			testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
			String t_firstname = "Bank TDR Automation";
			testConfig.putRunTimeProperty("firstname", t_firstname);
			helper.GetTestTransactionPage();
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		}
		else {
			testConfig.logFail("Bank TDR rule could not be created");
		}
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify if bank TDR rule is applied to CC transactions of previous days", 
			dataProvider="GetTestConfig", timeOut=600000, groups ="1")
	public void verifyCCBankTDRAppliedOnPreviousDayTransactions(Config testConfig) {

		int BankTDRrow = 2;
		float t_amount = 100; //for CC
		
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		EditBankTDRPage editBankTDRPage = new EditBankTDRPage(testConfig);
		
		editBankTDRPage.applyBankTDR(testConfig);
		
		helper.verifyBankTDRCalculation(testConfig, t_amount, BankTDRrow);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify if bank TDR rule is applied to DC transactions of previous days", 
			dataProvider="GetTestConfig", timeOut=600000, groups ="1")
	public void verifyDCBankTDRAppliedOnPreviousDayTransactions(Config testConfig) {

		int BankTDRrow = 3;
		float t_amount = 200; //for DC
		
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		EditBankTDRPage editBankTDRPage = new EditBankTDRPage(testConfig);
		editBankTDRPage.applyBankTDR(testConfig);
	
		helper.verifyBankTDRCalculation(testConfig, t_amount, BankTDRrow);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Bank TDR rule transaction with share fee of merchant TDR", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void ShareFeeBankTDRRule(Config testConfig)
	{
		int transactionRowNum = 3;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		int BankTDRrow = 5;
		String Sheet = "BankTDR";
		
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		HomePage home = new HomePage(testConfig);
		BankTDRPage bankTDRPage = home.ClickBankTDR();
		EditBankTDRPage editBankTDRPage = bankTDRPage.ClickEditBankTDR();
	
		int idBefore=helper.verifyBankTDRruleID(testConfig);
		editBankTDRPage.CreateTDRRule(BankTDRrow, Sheet);	
		int idAfter=helper.verifyBankTDRruleID(testConfig);
		
		if (idAfter > idBefore)
		{		
			//Bank TDR rule created
			System.out.println("Bank TDR rule created " + idAfter);		
			
			float t_amount = 100; // for share fee
			testConfig.putRunTimeProperty("amount", String.valueOf(t_amount));
			String t_firstname = "Bank TDR Automation";
			testConfig.putRunTimeProperty("firstname", t_firstname);
			helper.GetTestTransactionPage();
			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		}
		else {
			testConfig.logFail("Bank TDR rule could not be created");
		}
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Deleting Bank TDR rules", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void zdeletebankTDRRules(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		
		HomePage home = new HomePage(testConfig);
		BankTDRPage bankTDRPage = home.ClickBankTDR();
		EditBankTDRPage editBankTDRPage = bankTDRPage.ClickEditBankTDR();
		
		editBankTDRPage.deleteExistingRules();
		
		Assert.assertTrue(testConfig.getTestResult());
	}
}
