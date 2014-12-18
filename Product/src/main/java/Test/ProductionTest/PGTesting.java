package Test.ProductionTest;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsMerchantParamsPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class PGTesting extends TestBase{
	
	@Test(description = "Test the assigned PG",  dataProvider="GetTestConfig", timeOut=600000)
	public void testPG(Config testConfig)
	{	
		testConfig.setTestDataExcelDynamic("productproduction");
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		int numberOfMerchants = data.getRecordsNum();
		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		int cardDetailsRowNum = 1;
		int paymentTypeRowNum = 1;
		
		for (int i=1;i<numberOfMerchants; i++)
		{
			int transactionRowNum = i;
			if(!SetForcePGMerchantParam(testConfig, helper, transactionRowNum)) 
				break;
			
			helper.GetTestTransactionPage();
			helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
			boolean pageverified = helper.bankRedirectPage.VerifyRedirectedURL(helper.paymentTypeData, paymentTypeRowNum);
			int bankRedirectRow = Integer.parseInt(helper.paymentTypeData.GetData(paymentTypeRowNum, "BankRedirectRow"));
			helper.bankRedirectPage.Write3DSMerchantName(transactionRowNum, helper.bankRedirectPage.bankRedirectData, bankRedirectRow, pageverified) ;
			RemoveForcePGMerchantParam(testConfig, helper, transactionRowNum);
		}
		
		Assert.assertTrue(testConfig.getTestResult());	
	}

	private Boolean SetForcePGMerchantParam(Config testConfig, TransactionHelper helper,  int transactionRow) 
	{
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(transactionRow, "Key");
		
		if(value.equals("{skip}")) return false;
		
		helper.GetMerchantListPage();
		helper.merchantListPage.SearchMerchantKey(value);
		helper.merchantDetailsPage=helper.merchantListPage.clickFirstMerchantKey();
		helper.paramPage= helper.merchantDetailsPage.ClickParams();
		
		ParamsMerchantParamsPage paramPage=new ParamsMerchantParamsPage(testConfig);
		paramPage.addKeyValue(1);
		return true;
	}
	
	private void RemoveForcePGMerchantParam(Config testConfig, TransactionHelper helper,  int transactionRow) 
	{
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetCurrentEnvironmentData(transactionRow, "Key");
		helper.GetMerchantListPage();
		helper.merchantListPage.SearchMerchantKey(value);
		helper.merchantDetailsPage=helper.merchantListPage.clickFirstMerchantKey();
		helper.paramPage= helper.merchantDetailsPage.ClickParams();
		
		ParamsMerchantParamsPage paramPage=new ParamsMerchantParamsPage(testConfig);
		paramPage.removeKey("force_pgid");
	}
}
