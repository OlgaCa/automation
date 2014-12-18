/**
 * 
 */
/**
 * @author atul.jain
 *
 */
package Test.ProductionTest;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.ThreeDSecurePage;
import PageObject.NewMerchantPanel.DashBoard.DashboardPage;
import Test.NewMerchantPanel.Dashboard.DashboardHelper;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;

public class ProdCases extends TestBase{


	@Test(description = "Test admin.payu.in & secure.payu.in website",  dataProvider="GetTestConfig", timeOut=600000, invocationCount=3)
	public void testSecure(Config testConfig) throws IOException
	{
		testConfig.setTestDataExcelDynamic("productproduction");
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		for(int i=0;i<10;i++)
		{
			//verify 3d secure page is displayed after transaction is initiated
			int transactionRowNum = 2;
			int paymentTypeRowNum = 1;
			int cardDetailsRowNum = 1;
			helper.GetTestTransactionPage();
			helper.threeDSecurePage = (ThreeDSecurePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.ThreeDSecurePage);
			helper.threeDSecurePage.VerifyCITI_IssuingBank();
		}
		
		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Test info.payu.in website",  dataProvider="GetTestConfig", timeOut=600000, invocationCount=2)
	public void testInfo(Config testConfig) throws IOException
	{
		//login on merchant panel and verify transatcion details page is opened
		int transactionRowNum = 2;
		testConfig.setTestDataExcelDynamic("productproduction");
		MerchantPanelPage merechantPage;

		DashboardHelper helper = new DashboardHelper(testConfig);
		merechantPage=helper.doMerchantLogin(transactionRowNum);
		merechantPage.ClickTransactionsTab();
		
		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Test static.payu.in and payu website",  dataProvider="GetTestConfig", timeOut=600000)
	public void testStatic(Config testConfig) throws IOException
	{
		testConfig.setTestDataExcelDynamic("productproduction");
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		//Navigate to www.payu.in and verify images of signin and signup buttons
		dashboardHelper.navigateToPayuWebsiteAndVerifyImages(testConfig);

		Assert.assertTrue(testConfig.getTestResult());	
	}

}