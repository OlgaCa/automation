package Test.AdminPanel.MerchantList;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.EditMerchantPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.EditPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsMerchantParamsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsPage;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Test.MerchantPanel.Payments.TestAutoRefund.AutoRefund;
import Test.MerchantPanel.Payments.TestAutoRefund.verifyReconResult;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.GmailLogin;
import Utils.GmailVerification;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestEditMerchant extends TestBase{
	

	MerchantDetailsPage merchantDetailsPage;
	ParamsPage params;
	EditMerchantPage editMerchantPage;
	ParamsMerchantParamsPage merchantParams;
	
	@Test(description = "Test Edit Merchant", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_editMerchant(Config testConfig) 
	{

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		int testMerchantDataRow = 23;
		HomePage home = new HomePage(testConfig);
		MerchantListPage merchantListPage = home.clickMerchantList();

		String merchantName="";

		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");
			
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		
		editMerchantPage = merchantDetailsPage.ClickEditMerchant();
		EditPage editPage;
		editPage = editMerchantPage.clickEdit();
	
		TestDataReader data = new TestDataReader(testConfig,"CreateMerchant");

		int paramRowNo = editPage.getParamRowtoModify(data);				
		editPage.EditMerchantDetails(data, paramRowNo);		
		editPage.VerifyMerchantEditDetails(data, paramRowNo);
	
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Test email for the failed transaction mail on merchant mailID ", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_failedTransactionEmail(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int testMerchantDataRow =3; //automationMerchant3
	
		String merchantName="";
		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 17;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(testMerchantDataRow, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		testConfig.putRunTimeProperty("MerchantName", merchantName);
		verifyEmailForFailedTransaction(testConfig);
				
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Test changing currency type  ", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_changedCurrencyType(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		int transactionRowNum =102;  //AutomationMerchant3
		
		String currencyType="USD";
		
		changeCurrencyType(transactionRowNum, helper, currencyType);
				
		int paymentTypeRowNum = 12;
		int cardDetailsRowNum = 17;
		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum,ExpectedResponsePage.TestResponsePage);
		
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.doMerchantLogin(transactionRowNum);		

		DashboardPage dashBoard = new DashboardPage(testConfig);
		dashBoard.ClickClose();
		
		String amount = dashBoard.getRightSideAmount();
		System.out.println("RightSide Amount "+ amount);
		
		String amt[] = amount.split(" ");
		String currency_symbol ="";
		
		if(currencyType.equalsIgnoreCase("USD"))
			currency_symbol="$";
		else if(currencyType.equalsIgnoreCase("USD"))
			currency_symbol="Rs.";
			
		Helper.compareEquals(testConfig, "Amount currency is dashboard right side", currency_symbol, amt[0]);
		dashBoard.ClickViewAll();
		
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);

		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		dashboardHelper.transactionsPage.verifyCurrencyType(helper.transactionData,transactionRowNum,testConfig, currencyType);
		
		String homeUrl = testConfig.getRunTimeProperty("AdminPortalHome");
		Browser.navigateToURL(testConfig, homeUrl+"/home");
		
		currencyType = "INR";
		changeCurrencyType(transactionRowNum, helper, currencyType);
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	public void changeCurrencyType(int transactionRowNum, TransactionHelper helper, String currencyType)
	{
		helper.transactionData = new TestDataReader(helper.testConfig,"TransactionDetails");
		String merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");

		MerchantListPage merchantListPage = helper.home.clickMerchantList();
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		editMerchantPage = merchantDetailsPage.ClickEditMerchant();
		EditPage editPage;
		editPage = editMerchantPage.clickEdit();

		editPage.changeCurrencyType(currencyType);
		editPage.submit_changes();
	}
	
	
	public void verifyEmailForFailedTransaction(Config testConfig)
	{	
		GmailLogin gmailLogin = new GmailLogin(testConfig);
		GmailVerification gmailVerification = gmailLogin.Login("payu.testing","payu@123");   
		// wait for email
		Browser.wait(testConfig, 10);
		gmailVerification.FilterEmail("Transaction ID : "+testConfig.getRunTimeProperty("txnId")+" failed", testConfig.getRunTimeProperty("txnId"));
		gmailVerification.verifyEmailText("Transaction ID : "+testConfig.getRunTimeProperty("txnId")+" failed");
		gmailVerification.SelectEmail();
		String expected_mail_content = "Merchant Name "+testConfig.getRunTimeProperty("MerchantName")+"\n"+"PayU Id "+ testConfig.getRunTimeProperty("mihpayid")+"\n"+ "Status failed\n";
		gmailVerification.verifyEmailContent(expected_mail_content);
		Browser.wait(testConfig, 5);
		
	}
	
	
	
}
