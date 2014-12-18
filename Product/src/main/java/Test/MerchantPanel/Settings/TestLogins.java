package Test.MerchantPanel.Settings;


import org.testng.annotations.Test;

import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Settings.NewMerchantLoginPage;
import PageObject.MerchantPanel.Settings.MerchantLoginsPage;
import Test.AdminPanel.Payments.TransactionHelper;

import Test.MerchantPanel.Dashboard.DashboardHelper;
import Test.MerchantPanel.Dashboard.DashboardHelper.ExpectedLandingPage;
import Utils.Config;
import Utils.TestBase;


public class TestLogins extends TestBase{
	

	@Test(description = "Verify New Login", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CreateLogin(Config testConfig)  {
		
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.dbPage = (DashboardPage) dashboardHelper.doMerchantLogin(1);
		dashboardHelper.dbPage = dashboardHelper.clickLoginAndFillForm(1);
		dashboardHelper.assignRoelsToLogin();
		dashboardHelper.payuHome = dashboardHelper.dbPage.clickMerchantSignout();
		
		dashboardHelper.dbPage = dashboardHelper.loginAndCheckAssignedRoles(1);
		dashboardHelper.payuHome = dashboardHelper.dbPage.clickMerchantSignout();
		
		testConfig.putRunTimeProperty("MerchantUser","super");
		testConfig.putRunTimeProperty("MerchantPassword","Password");
		dashboardHelper.doMerchantLogin(1);			
			
		dashboardHelper.activeORDeactivateLogin();
		dashboardHelper.payuHome = dashboardHelper.dbPage.clickMerchantSignout();
		
		String name = testConfig.getRunTimeProperty("name");
		testConfig.putRunTimeProperty("MerchantUser",name);
		testConfig.putRunTimeProperty("MerchantPassword","password");
		dashboardHelper.doMerchantLogin(1, ExpectedLandingPage.PayUHomePage);	
		dashboardHelper.payuHome.verifyLoginFaliure();
		
		testConfig.putRunTimeProperty("MerchantUser","super");
		testConfig.putRunTimeProperty("MerchantPassword","Password");
		dashboardHelper.doMerchantLogin(1);	
		dashboardHelper.deleteMerchantLogin();
		
		
		}
	/*@Test(description = "Verify New Login with wrong data", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
   public void test_CreateLoginwithWorngData(Config testConfig)  {
		
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.doMerchantLogin(1);		
		DashboardPage dashBoard = new DashboardPage(testConfig);
		dashBoard.ClickClose();
		dashBoard.ClickCreateLogin();
		int loginRowNum = 3;
		dashboardHelper.filluserDetail(loginRowNum);
	
		}
	
	@Test(description = "Verify Manage Login", 
	dataProvider="GetTestConfig", timeOut=600000, groups="1")
    public void test_DeoManageLogin(Config testConfig)  {
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.doMerchantLogin(1);
		DashboardPage dashBoard = new DashboardPage(testConfig);
		dashBoard.ClickManageLogin();
		dashBoard.ClickClose();
		dashboardHelper.editManageLogin();
		//dashboardHelper.activeORDeactivateLogin();
		dashboardHelper.assignRoelsToLogin();
		dashBoard.ClickManageLogin();
		
		}
	
	@Test(description = "Verify Edit Login", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_DiatedLogin(Config testConfig)  {
		
		testConfig.putRunTimeProperty("MerchantUser","Akhtar");
		testConfig.putRunTimeProperty("MerchantPassword","password");
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.doMerchantLogin(1);
		DashboardPage dashBoard = new DashboardPage(testConfig);
		dashBoard.ClickClose();
		dashBoard.ClickViewAll();
		dashBoard.ClickRequestRefund();
		dashBoard.ClickCancel();
		
	}
	
	@Test(description = "Verify Edit Login", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EdisableLogin(Config testConfig)  {
		
		
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.doMerchantLogin(1);
		DashboardPage dashBoard = new DashboardPage(testConfig);
		dashBoard.ClickManageLogin();
		dashBoard.ClickClose();
		dashboardHelper.editManageLogin();
		dashboardHelper.activeORDeactivateLogin();
		dashBoard.ClickManageLogin();
	}
	
	@Test(description = "Verify Edit Login", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_EditLogin(Config testConfig)  {
		
		testConfig.putRunTimeProperty("MerchantUser","Akhtar");
		testConfig.putRunTimeProperty("MerchantPassword","password");
				DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
				dashboardHelper.doMerchantLogin(1);
					
	}
	
*/	
}
