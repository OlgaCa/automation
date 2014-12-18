package Test.MerchantPanel.Settings;
import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.MerchantPanel.Home.DashboardPage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.TestBase;

public class TestCheckout extends TestBase{
	
	@Test(description = "Verify add html code", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Add_htmlCode_CheckoutPage(Config testConfig)  {
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
	    dashboardHelper.dbPage = (DashboardPage) dashboardHelper.doMerchantLogin(4);
	    dashboardHelper.merchantcheckoutPage = dashboardHelper.clickCheckoutPageAndAddHtml();
	    dashboardHelper.verifyEdit("test edit");
	    Assert.assertTrue(testConfig.getTestResult());
	    
	    dashboardHelper.merchantcheckoutPage = dashboardHelper.clickCheckoutPageAndDeleteHtml();
	    dashboardHelper.verifyEdit("");
	    Assert.assertTrue(testConfig.getTestResult());
	    dashboardHelper.merchantcheckoutPage = dashboardHelper.SubmitHtmlAfetrCancel();
	    dashboardHelper.verifyEdit("");
	    Assert.assertTrue(testConfig.getTestResult());
	    dashboardHelper.merchantcheckoutPage = dashboardHelper.SubmitWithoutUpdateHtml();
	    dashboardHelper.verifyEdit("");
	    Assert.assertTrue(testConfig.getTestResult());
	  // dashboardHelper.VerifyHtmlUpdate();
	   
	    

	}

}