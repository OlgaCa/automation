package Test.MerchantPanel.Settings;

import org.testng.Assert;

import org.testng.annotations.Test;
import PageObject.MerchantPanel.Home.DashboardPage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.TestBase;

public class TestsubUserRights extends TestBase{

	@Test(description = "Verify export to excel rights to sub user New Login", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_exportExcelRight(Config testConfig)  {
		
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.dbPage = (DashboardPage) dashboardHelper.doMerchantLogin(1);
		
		//Create sub user login
		dashboardHelper.clickLoginAndFillForm(1);
		
		//Assign role to sub user on manage login
		dashboardHelper.assignRolesToLogin();
		dashboardHelper.dbPage.clickMerchantSignout();
		
		//Verify export to excel on different tabs
		dashboardHelper.CheckExportExcelRole(1);
				
		dashboardHelper.dbPage.clickMerchantSignout();
		
		//revoke rights from sub user
		dashboardHelper.doMerchantLogin(1);	
		dashboardHelper.revokeRolesToLogin();
		dashboardHelper.dbPage.clickMerchantSignout();
		
		//Verify export to excel not present
		
		dashboardHelper.CheckExportExcelNotPresent(1);
		
		//Delete sub user
		
		dashboardHelper.doMerchantLogin(1);	
		dashboardHelper.deleteMerchantLogin();
		Assert.assertTrue(testConfig.getTestResult());
				
		}
	
	}
