package Test.AdminPanel.TDR;

import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.ManualUpdate.ReconPage;
import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.RefundTDRList;
import PageObject.AdminPanel.MerchantList.MerchantDetails.TDRList;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Settlement.InvalidFilePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Transactions.RefundPage;
import PageObject.MerchantPanel.Transactions.RequestsPage;
import PageObject.MerchantPanel.Transactions.TransactionFilterPage.SearchOn;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestRefundTDR extends TestBase{

	MerchantDetailsPage merchantDetailsPage;
	TDRList tdrList;
	RefundTDRList refundTDRList;
	ManualUpdatePage manualTransactionUpdate;
	ReconPage reconPage;
	InvalidFilePage invalidFilepage;

	@Test(description = "Verify add refundTDR rule in UI and DB", 
			dataProvider="GetTestConfig", timeOut=600000, groups ="1")
	public void test_AddRefundTDR(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int testMerchantDataRow = 44;
		int paramRow=1;

		//Navigate to Adminpanel
		HomePage home = new HomePage(testConfig);
		MerchantListPage merchantListPage = home.clickMerchantList();

		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		String merchantid = helper.transactionData.GetData(testMerchantDataRow, "merchantid");

		//Search the merchant
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		//Add refund TDR rule through refund TDR and verify in DB
		addRefundTDR(testConfig, paramRow);
		Browser.wait(testConfig, 10);		
		TestDataReader testDataReader = new TestDataReader(testConfig,"RefundTDR");
		testConfig.putRunTimeProperty("merchantid", merchantid);
		verify_refundTDRRule_DB(testConfig, paramRow, testDataReader);		

		//Verify Refund TDR rule added successfully
		refundTDRList = merchantDetailsPage.ClickRefundTDRList(testConfig);
		refundTDRList.verifyRefundTDR(paramRow);

		//Remove added refund TDR rule
		refundTDRList.removeRefundTDR(testConfig);	

		//Verify Refund TDR rule deleted successfully
		refundTDRList = merchantDetailsPage.ClickRefundTDRList(testConfig);		
		refundTDRList.verifyRefundTDRnotPresent(testConfig);
		
		//Another Refund TDR rule case
		//Add invalid Refund TDR rule containing > 7 daysRange
		paramRow = 4;
		addRefundTDR(testConfig, paramRow);
		Browser.wait(testConfig, 2);

		//verify added refund TDR rule
		refundTDRList = merchantDetailsPage.ClickRefundTDRList(testConfig);
		refundTDRList.verifyRefundTDR(paramRow);
		verify_refundTDRRule_DB(testConfig, paramRow, testDataReader);

		//Remove added refund TDR rule
		refundTDRList.removeRefundTDR(testConfig);

		//Verify Refund TDR rule deleted successfully
		refundTDRList = merchantDetailsPage.ClickRefundTDRList(testConfig);		
		refundTDRList.verifyRefundTDRnotPresent(testConfig);

		Assert.assertTrue(testConfig.getTestResult());  
	}	

	@Test(description = "Verify add invalid TDR rule", 
			dataProvider="GetTestConfig", timeOut=600000, groups ="1")
	public void test_AddInvalidTDR(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int testMerchantDataRow = 44;
		int paramRow=2;

		//Navigate to Admin Panel
		HomePage home = new HomePage(testConfig);
		MerchantListPage merchantListPage = home.clickMerchantList();

		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		String merchantid = helper.transactionData.GetData(testMerchantDataRow, "merchantid");

		//Search the merchant
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		//Remove Refund TDR rule if any
		refundTDRList = merchantDetailsPage.ClickRefundTDRList(testConfig);
		int cssLocator = testConfig.driver.findElements(By.cssSelector("#refundTDR > div.tdr_ctgry > table > tbody > tr:nth-child(2)")).size();
		if (cssLocator > 0) {
		refundTDRList.removeRefundTDR(testConfig);
		}
		
		//Add invalid Refund TDR rule containing alphabets in daysRange
		addRefundTDR(testConfig, paramRow);
		Browser.wait(testConfig, 2);

		Browser.navigateToURL(testConfig, "https://admin.payu.in/release/home");
		home = new HomePage(testConfig);
		merchantListPage = home.clickMerchantList();

		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		refundTDRList = merchantDetailsPage.ClickRefundTDRList(testConfig);
		refundTDRList.verifyRefundTDRnotPresent(testConfig);
		TestDataReader testDataReader = new TestDataReader(testConfig,"RefundTDR");
		testConfig.putRunTimeProperty("merchantid", merchantid);
		verify_refundTDRRule_DB(testConfig, paramRow, testDataReader);	

		//Add invalid Refund TDR rule containing -ve daysRange
		paramRow = 3;
		addRefundTDR(testConfig, paramRow);
		Browser.wait(testConfig, 2);

		Browser.navigateToURL(testConfig, "https://admin.payu.in/release/home");
		home = new HomePage(testConfig);
		merchantListPage = home.clickMerchantList();

		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		refundTDRList = merchantDetailsPage.ClickRefundTDRList(testConfig);
		refundTDRList.verifyRefundTDRnotPresent(testConfig);
		verify_refundTDRRule_DB(testConfig, paramRow, testDataReader);

		//Add invalid Refund TDR rule containing < 0 daysRange
		paramRow = 5;
		addRefundTDR(testConfig, paramRow);
		Browser.wait(testConfig, 2);

		Browser.navigateToURL(testConfig, "https://admin.payu.in/release/home");
		home = new HomePage(testConfig);
		merchantListPage = home.clickMerchantList();

		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		refundTDRList = merchantDetailsPage.ClickRefundTDRList(testConfig);
		refundTDRList.verifyRefundTDRnotPresent(testConfig);
		verify_refundTDRRule_DB(testConfig, paramRow, testDataReader);

		//Add invalid Refund TDR rule containing alphanumeric daysRange
		paramRow = 6;
		addRefundTDR(testConfig, paramRow);
		Browser.wait(testConfig, 2);

		Browser.navigateToURL(testConfig, "https://admin.payu.in/release/home");
		home = new HomePage(testConfig);
		merchantListPage = home.clickMerchantList();

		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		refundTDRList = merchantDetailsPage.ClickRefundTDRList(testConfig);		
		refundTDRList.verifyRefundTDRnotPresent(testConfig);
		verify_refundTDRRule_DB(testConfig, paramRow, testDataReader);

		//Add invalid Refund TDR rule containing special characters in daysRange
		paramRow = 7;
		addRefundTDR(testConfig, paramRow);
		Browser.wait(testConfig, 2);

		Browser.navigateToURL(testConfig, "https://admin.payu.in/release/home");
		home = new HomePage(testConfig);
		merchantListPage = home.clickMerchantList();

		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		refundTDRList = merchantDetailsPage.ClickRefundTDRList(testConfig);
		refundTDRList.verifyRefundTDRnotPresent(testConfig);
		verify_refundTDRRule_DB(testConfig, paramRow, testDataReader);

		Assert.assertTrue(testConfig.getTestResult());  
	}

	@Test(description = "Refund TDR rule transaction: Test that Refund TDR rule is working when TDR rule is hit", 
			dataProvider="GetTestConfig", timeOut=1200000, groups="1")
	public void test_Transaction_RefundTDR_NormalFlow(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 10 amount applied on transaction's refund if refund is 
		 of less than 100 amount and transaction is of more than 100 
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount) 
		 ****************
		 *****************/

		//Do a transaction 
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int PGinfoRowNum = 45;
		int TDRRow = 2;
		int requestRow = 1;

		helper.GetTestTransactionPage();

		float entered_amount = 110;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to adminPanel to add refund TDR rule
		MerchantListPage merchantListPage = helper.home.clickMerchantList();
		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");
		String merchantid = helper.transactionData.GetData(transactionRowNum, "merchantid");

		//Search the merchant
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		//Add refund TDR rule  for AXIS through refund TDR 
		addRefundTDR(testConfig, 1);
		Browser.wait(testConfig, 10);
		TestDataReader testDataReader = new TestDataReader(testConfig,"RefundTDR");
		testConfig.putRunTimeProperty("merchantid", merchantid);
		verify_refundTDRRule_DB(testConfig, 1, testDataReader);	

		//Navigate to dashboard to refund the transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request partial refund of amount say 11
		String partialAmount = "11.00";
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(partialAmount);
		RequestsPage requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"10":{"flatFee":"15","percentFee":"10"})
		TDRRow = 1;
		Float refundAmount = new Float(partialAmount);
		verify_partialRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 10 amount applied on transaction's refund if refund is 
		 of Rs. 100 and transaction is of more than 100 
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount) 
		 ****************
		 *****************/

		//Do a transaction with additional charge
		TDRRow = 2;

		helper.GetTestTransactionPage();

		entered_amount = 110;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to dashboard to refund the transaction
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		refundTransaction = dashBoard.ClickRequestRefund();

		//Request partial refund of Rs. 100
		partialAmount = "100.00";
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(partialAmount);
		requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"10":{"flatFee":"15","percentFee":"10"})
		TDRRow = 1;
		refundAmount = new Float(partialAmount);
		verify_partialRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 100 amount applied on transaction's refund if partial refund is 
		 of Rs. 100+ and transaction is of more than 100 
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount) 
		 ****************
		 *****************/

		//Do a transaction with additional charge
		TDRRow = 2;

		helper.GetTestTransactionPage();

		entered_amount = 110;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to dashboard to refund the transaction
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		refundTransaction = dashBoard.ClickRequestRefund();

		//Request partial refund of Rs. 100
		partialAmount = "105.00";
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(partialAmount);
		requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		refundAmount = new Float(partialAmount);
		verify_partialRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 100 amount applied on transaction's refund if full refund is 
		 of Rs. 100+ and transaction is of more than 100 
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount) 
		 ****************
		 *****************/

		//Do a transaction with additional charge
		TDRRow = 2;

		helper.GetTestTransactionPage();

		entered_amount = 110;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to dashboard to refund the transaction
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		refundTransaction = dashBoard.ClickRequestRefund();

		//Request full refund
		partialAmount = String.format("%.2f", entered_amount);
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(partialAmount);
		requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		refundAmount = new Float(partialAmount);
		verify_fullRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);
		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Refund TDR rule transaction: Test that Refund TDR rule is working when TDR rule is hit", 
			dataProvider="GetTestConfig", timeOut=1200000, groups="1")
	public void test_Transaction_RefundTDR_PayuMoneyFlow_RefundAdditionalCharge_ON(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 10 amount applied on transaction's refund if refund is 
		 of less than 100 amount and transaction is of less than 100 such that (amount+additionalCharge<100)
		 RefundAdditionalCharge param is ON
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount) 
		 ****************
		 *****************/

		//Do a transaction with additional charge
		int transactionRowNum = 32;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int PGinfoRowNum = 45;
		int TDRRow = 1;
		int requestRow = 1;

		helper.GetTestTransactionPage();

		float entered_amount = 90;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));
		testConfig.putRunTimeProperty("additionalCharges", String.valueOf("CC:10"));

		String amount = testConfig.getRunTimeProperty("amount");
		double transactionamount = Double.parseDouble(amount);

		//Get additional value to be charged
		String keyvalue = testConfig.getRunTimeProperty("additionalCharges");
		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[1];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"10":{"flatFee":"15","percentFee":"10"})
		testConfig.putRunTimeProperty("amount", transactionAmount);
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to adminPanel to add refund TDR rule
		MerchantListPage merchantListPage = helper.home.clickMerchantList();
		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");
		String merchantid = helper.transactionData.GetData(transactionRowNum, "merchantid");

		//Search the merchant
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		//Add refund TDR rule  for AXIS through refund TDR 
		addRefundTDR(testConfig, 1);
		Browser.wait(testConfig, 10);
		TestDataReader testDataReader = new TestDataReader(testConfig,"RefundTDR");
		testConfig.putRunTimeProperty("merchantid", merchantid);
		verify_refundTDRRule_DB(testConfig, 1, testDataReader);	

		//Navigate to dashboard to refund the transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request partial/full refund
		String partialAmount = transactionAmount;
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(partialAmount);
		RequestsPage requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"10":{"flatFee":"15","percentFee":"10"})
		Float refundAmount = new Float(partialAmount);
		verify_fullRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 100 amount applied on transaction's refund if refund is 
		 of more than 100 amount and transaction is of more than 100 such that (amount+additionalCharge>100)
		 RefundAdditionalCharge param is ON
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount) 
		 ****************
		 *****************/

		//Do a transaction with additional charge
		TDRRow = 2;
		helper.GetTestTransactionPage();

		entered_amount = 100;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));
		testConfig.putRunTimeProperty("additionalCharges", String.valueOf("CC:10"));

		amount = testConfig.getRunTimeProperty("amount");
		transactionamount = Double.parseDouble(amount);

		//Get additional value to be charged
		keyvalue = testConfig.getRunTimeProperty("additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[1];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");		

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		testConfig.putRunTimeProperty("amount", transactionAmount);
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to dashboard to refund the transaction
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		refundTransaction = dashBoard.ClickRequestRefund();

		//Request full refund of Rs. 110
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(transactionAmount);
		requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		refundAmount = new Float(transactionAmount);
		verify_fullRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 0 amount applied on transaction's refund if refund is 
		 of Rs. 100 and transaction is of more than 100 such that (amount+additionalCharge>100)
		 RefundAdditionalCharge param is ON
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount)  
		 ****************
		 *****************/

		//Do a transaction with additional charge
		TDRRow = 2;
		helper.GetTestTransactionPage();

		entered_amount = 100;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));
		testConfig.putRunTimeProperty("additionalCharges", String.valueOf("CC:10"));

		amount = testConfig.getRunTimeProperty("amount");
		transactionamount = Double.parseDouble(amount);

		//Get additional value to be charged
		keyvalue = testConfig.getRunTimeProperty("additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[1];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		testConfig.putRunTimeProperty("amount", transactionAmount);
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to dashboard to refund the transaction
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		refundTransaction = dashBoard.ClickRequestRefund();

		//Request partial refund of Rs. 100
		partialAmount = "100.00";
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(partialAmount);
		requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"10":{"flatFee":"15","percentFee":"10"})
		TDRRow = 1;
		refundAmount = new Float(partialAmount);
		verify_partialRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 100 amount applied on transaction's refund if refund is 
		 of more than 100 amount and transaction is of more than 100 such that (amount+additionalCharge>100)
		 RefundAdditionalCharge param is ON
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount)  
		 ****************
		 *****************/

		//Do a transaction with additional charge
		TDRRow = 2;
		helper.GetTestTransactionPage();

		entered_amount = 100;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));
		testConfig.putRunTimeProperty("additionalCharges", String.valueOf("CC:10"));

		amount = testConfig.getRunTimeProperty("amount");
		transactionamount = Double.parseDouble(amount);

		//Get additional value to be charged
		keyvalue = testConfig.getRunTimeProperty("additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[1];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		testConfig.putRunTimeProperty("amount", transactionAmount);
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to dashboard to refund the transaction
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		refundTransaction = dashBoard.ClickRequestRefund();

		//Request full refund
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(transactionAmount);
		requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		refundAmount = new Float(transactionAmount);
		verify_fullRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Refund TDR rule transaction: Test that Refund TDR rule is working when TDR rule is hit", 
			dataProvider="GetTestConfig", timeOut=1200000, groups="1")
	public void test_Transaction_Various_RefundTDR_PayuMoneyFlow_RefundAdditionalCharge_OFF(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();
		
		int transactionRowNum = 44;
		int paymentTypeRowNum = 3;
		int cardDetailsRowNum = 1;
		int PGinfoRowNum = 45;
		int TDRRow = 1;
		int requestRow = 1;

		//Add TDR rule for amount more than 0
		HomePage home = new HomePage(testConfig);
		MerchantListPage merchantListPage = home.clickMerchantList();

		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");

		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		tdrList.addParameters(TDRRow);
		Browser.wait(testConfig, 10);

		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		tdrList.verifyTDR(TDRRow);

		//Add another TDR rule for amount more than 100
		TDRRow = 2;
		tdrList.addParameters(TDRRow);
		Browser.wait(testConfig, 2);

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 10 amount applied on transaction's refund if refund is 
		 of less than 100 amount and transaction is of less than 100 such that (amount+additionalCharge<100)
		 RefundAdditionalCharge param is OFF
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount) 
		 ****************
		 *****************/

		//Do a transaction with additional charge
		TDRRow = 1;
		helper.GetTestTransactionPage();

		float entered_amount = 90;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));
		testConfig.putRunTimeProperty("additionalCharges", String.valueOf("CC:10"));

		String amount = testConfig.getRunTimeProperty("amount");
		double transactionamount = Double.parseDouble(amount);

		//Get additional value to be charged
		String keyvalue = testConfig.getRunTimeProperty("additionalCharges");
		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[1];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"10":{"flatFee":"15","percentFee":"10"})
		testConfig.putRunTimeProperty("amount", transactionAmount);
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to adminPanel to add refund TDR rule
		merchantListPage = helper.home.clickMerchantList();

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");
		String merchantid = helper.transactionData.GetData(transactionRowNum, "merchantid");

		//Search the merchant
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		//Add refund TDR rule  for AXIS through refund TDR 
		addRefundTDR(testConfig, 1);
		Browser.wait(testConfig, 10);
		TestDataReader testDataReader = new TestDataReader(testConfig,"RefundTDR");
		testConfig.putRunTimeProperty("merchantid", merchantid);
		verify_refundTDRRule_DB(testConfig, 1, testDataReader);	

		//Navigate to dashboard to refund the transaction
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		DashboardPage dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		RefundPage refundTransaction = dashBoard.ClickRequestRefund();

		//Request partial/full refund
		String partialAmount = "90.00";
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(partialAmount);
		RequestsPage requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"10":{"flatFee":"15","percentFee":"10"})
		TDRRow = 1;
		Float refundAmount = new Float(partialAmount);
		verify_partialRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 0 amount applied on transaction's refund if refund is 
		 of less than 100 amount and transaction is of more than 100 such that (amount+additionalCharge>100)
		 RefundAdditionalCharge param is OFF
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount) 
		 ****************
		 *****************/

		//Do a transaction with additional charge
		TDRRow = 2;
		helper.GetTestTransactionPage();

		entered_amount = 100;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));
		testConfig.putRunTimeProperty("additionalCharges", String.valueOf("CC:10"));

		amount = testConfig.getRunTimeProperty("amount");
		transactionamount = Double.parseDouble(amount);

		//Get additional value to be charged
		keyvalue = testConfig.getRunTimeProperty("additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[1];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");		

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		testConfig.putRunTimeProperty("amount", transactionAmount);
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to dashboard to refund the transaction
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		refundTransaction = dashBoard.ClickRequestRefund();

		//Request partial refund
		partialAmount = "100";
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(partialAmount);
		requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"10":{"flatFee":"15","percentFee":"10"})
		TDRRow = 1;
		refundAmount = new Float(partialAmount);
		verify_partialRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 0 amount applied on transaction's refund if refund is 
		 of Rs. 100 and transaction is of more than 100 such that (amount+additionalCharge>100)
		 RefundAdditionalCharge param is OFF
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount)  
		 ****************
		 *****************/

		//Do a transaction with additional charge
		TDRRow = 2;
		helper.GetTestTransactionPage();

		entered_amount = 100;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));
		testConfig.putRunTimeProperty("additionalCharges", String.valueOf("CC:10"));

		amount = testConfig.getRunTimeProperty("amount");
		transactionamount = Double.parseDouble(amount);

		//Get additional value to be charged
		keyvalue = testConfig.getRunTimeProperty("additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[1];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");		

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		testConfig.putRunTimeProperty("amount", transactionAmount);
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to dashboard to refund the transaction
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		refundTransaction = dashBoard.ClickRequestRefund();

		//Request partial refund of Rs. 90
		partialAmount = "90.00";
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(partialAmount);
		requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"10":{"flatFee":"15","percentFee":"10"})
		TDRRow = 1;
		refundAmount = new Float(partialAmount);
		verify_partialRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);

		/****************
		 ****************
		 Verifying correct refund tdr rule of more than 100 amount applied on transaction's refund if refund is 
		 of more than 100 amount and transaction is of more than 100 such that (amount+additionalCharge>100)
		 RefundAdditionalCharge param is ON
		 (Rule I : more than 10 amount)
		 (Rule II : more than 100 amount)  
		 ****************
		 *****************/

		//Do a transaction with additional charge
		TDRRow = 2;
		helper.GetTestTransactionPage();

		entered_amount = 110;
		testConfig.putRunTimeProperty("amount", String.valueOf(entered_amount));
		testConfig.putRunTimeProperty("additionalCharges", String.valueOf("CC:10"));

		amount = testConfig.getRunTimeProperty("amount");
		transactionamount = Double.parseDouble(amount);

		//Get additional value to be charged
		keyvalue = testConfig.getRunTimeProperty("additionalCharges");
		keyValue = keyvalue.split(":");
		keyvalue = keyValue[1];
		keyValue = keyvalue.split(",");
		addCharge = keyValue[0].trim();

		//verify amount on processing page
		additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;		
		transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");		

		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);

		//verify TDR is calculated on amount and additionalCharge (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		testConfig.putRunTimeProperty("amount", transactionAmount);
		helper.verify_tdr_calculation(testConfig,TDRRow);

		//Navigate to dashboard to refund the transaction
		dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = (DashboardPage) dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickClose();
		refundTransaction = dashBoard.ClickRequestRefund();

		//Request partial refund
		partialAmount = "110.00";
		refundTransaction.SearchTransaction(helper.testResponse.actualResponse.get("txnid"), SearchOn.TransactionId);
		refundTransaction.RefundFirstTransaction(partialAmount);
		requestsPage = dashBoard.ClickViewRequest();

		//Verify Refund_TDR rule (Should hit rule default":{"100":{"flatFee":"20","percentFee":"4"})
		TDRRow = 2;
		refundAmount = new Float(partialAmount);
		verify_partialRefundTDR_calculation(testConfig, TDRRow, refundAmount);

		//Verify refunded transactions data from DB, when RefundAdditionalCharges param is set 1
		requestsPage.VerifyRefundRequest_CF(requestRow, paymentTypeRowNum, transactionRowNum, PGinfoRowNum, refundAmount, helper, helper.testResponse);

		//Remove TDR rule
		//helper.home.clickMerchantList(); = new HomePage(testConfig);
		helper.GetTestTransactionPage();
		merchantListPage = helper.home.clickMerchantList();

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");

		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		tdrList = merchantDetailsPage.ClickTDRList(testConfig);
		tdrList.removeTDR(testConfig);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	public void addRefundTDR(Config testConfig, int paramRow)
	{
		//Add refund TDR rule through refund TDR 
		refundTDRList = merchantDetailsPage.ClickRefundTDRList(testConfig);
		refundTDRList.addRefundTDRRule(paramRow);
	}

	public void verify_refundTDRRule_DB(Config testConfig, int paramRow, TestDataReader testDataReader) {

		String pg_id = testDataReader.GetData(paramRow, "Value");
		String result = testDataReader.GetData(paramRow, "DaysRange");
		testConfig.putRunTimeProperty("pg_id", pg_id);
		Map<String, String> map = DataBase.executeSelectQuery(testConfig, 90, 1);		

		if (paramRow == 1 || paramRow == 4) {
			Helper.compareEquals(testConfig, "Refund Rule in DB is updated", result, map.get("value"));
		}
		else if (paramRow == 2 || paramRow == 3 || paramRow == 5 || paramRow == 6 || paramRow == 7)
		{
			Helper.compareEquals(testConfig, "Refund Rule in DB not updated", "0", map.get("value"));
		}

	}

	public void verify_fullRefundTDR_calculation(Config testConfig, int dataRow, Float refundAmount){
		//Read TDR rule from excel sheet
		TestDataReader data = new TestDataReader(testConfig,"TDR");

		Map<String,String> map1 = null;
		map1 = DataBase.executeSelectQuery(testConfig, 66, 1);
		Float service_tax = new Float(map1.get("value"));

		int retries = 60;
		Map<String,String> map2 = null;

		while(retries>0){
			Browser.wait(testConfig, 2);
			map2 = DataBase.executeSelectQuery(testConfig, 4, -1);
			if(map2 != null)
				retries = 0;
			else retries--;
		}

		Float netAmount = new Float(map2.get("mer_net_amount"));
		String ExpectedNetAmount = String.format("%.2f", netAmount);

		Float payuCharge = new Float(map2.get("mer_service_fee"));
		String ExpectedPayuCharge = String.format("%.1f", payuCharge);

		Float mer_service_tax = new Float(map2.get("mer_service_tax"));
		String ExpectedServiceTax=String.format("%.2f", mer_service_tax);

		Float fee_per = new Float(data.GetData(dataRow, "Fee (in %)"));
		Float fee_flat = new Float(data.GetData(dataRow, "Flat Fee"));

		Float t_amount = new Float(refundAmount);
		Float ActualPayuCharge = (t_amount*fee_per)/100  + fee_flat;

		Float service_tax_calc = ActualPayuCharge*service_tax/100;
		String ActualServiceTax=String.format("%.2f", service_tax_calc);

		Float mer_net_amount = t_amount-(ActualPayuCharge+service_tax_calc);
		String ActualNetAmount=String.format("%.2f", mer_net_amount);

		//Compare the PayuCharge, Service_Tax and Mer_net_amount
		Helper.compareEquals(testConfig, "TDR Payu Charge", ExpectedPayuCharge, "-"+ActualPayuCharge);
		Helper.compareEquals(testConfig, "TDR Service tax", ExpectedServiceTax, "-"+ActualServiceTax);
		Helper.compareEquals(testConfig, "Merchant Net Amount", ExpectedNetAmount, "-"+ActualNetAmount);
	}

	public void verify_partialRefundTDR_calculation(Config testConfig, int dataRow, Float refundAmount){
		//Read TDR rule from excel sheet
		TestDataReader data = new TestDataReader(testConfig,"TDR");

		Map<String,String> map1 = null;
		map1 = DataBase.executeSelectQuery(testConfig, 66, 1);
		Float service_tax = new Float(map1.get("value"));

		int retries = 60;
		Map<String,String> map2 = null;

		while(retries>0){
			Browser.wait(testConfig, 2);
			map2 = DataBase.executeSelectQuery(testConfig, 4, -1);
			if(map2 != null)
				retries = 0;
			else retries--;
		}

		Float netAmount = new Float(map2.get("mer_net_amount"));
		String ActualNetAmount = String.format("%.2f", netAmount);

		Float payuCharge = new Float(map2.get("mer_service_fee"));
		String  ActualPayuCharge  = String.format("%.1f", payuCharge);

		Float mer_service_tax = new Float(map2.get("mer_service_tax"));
		String ActualServiceTax=String.format("%.2f", mer_service_tax);

		Float fee_per = new Float(data.GetData(dataRow, "Fee (in %)"));
		//Float fee_flat = new Float(data.GetData(dataRow, "Flat Fee"));

		Float t_amount = new Float(refundAmount);
		System.out.println(t_amount);
		Float ExpectedPayuCharge = (t_amount*fee_per)/100;

		Float service_tax_calc = ExpectedPayuCharge*service_tax/100;
		String ExpectedServiceTax=String.format("%.2f", service_tax_calc);

		Float mer_net_amount = t_amount-(ExpectedPayuCharge+service_tax_calc);
		String ExpectedNetAmount =String.format("%.2f", mer_net_amount);

		//Compare the PayuCharge, Service_Tax and Mer_net_amount
		Helper.compareEquals(testConfig, "TDR Payu Charge", "-"+ExpectedPayuCharge, ActualPayuCharge);
		Helper.compareEquals(testConfig, "TDR Service tax", "-"+ExpectedServiceTax, ActualServiceTax);
		Helper.compareEquals(testConfig, "Merchant Net Amount", "-"+ExpectedNetAmount, ActualNetAmount);
	}
}
