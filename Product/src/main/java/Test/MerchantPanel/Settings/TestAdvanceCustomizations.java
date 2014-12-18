package Test.MerchantPanel.Settings;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.awt.AWTException;
import java.text.DecimalFormat;
import java.util.Map;

import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import PageObject.MerchantPanel.Settings.*;
import Test.MerchantPanel.Settings.*;
import Utils.Element;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;


import Utils.Config;

public class TestAdvanceCustomizations extends TestBase {
	
	@Test(description = "Verify css class added", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyCssAdded(Config testConfig)  {
		
		SettingsHelper shelper = new SettingsHelper(testConfig);
		SetLookPage setlook = shelper.doLoginAndAddCss(1,1);
		String text=testConfig.getRunTimeProperty("cssName");
		setlook.verifyClassAdded(text);
		Assert.assertTrue(testConfig.getTestResult());
		
	}
	
	@Test(description = "Verify css class not  added", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyCssNotAdded(Config testConfig)  {
	
		SettingsHelper shelper = new SettingsHelper(testConfig);
		SetLookPage setlook = shelper.doLoginAndAddCss(1,6);
		String text=testConfig.getRunTimeProperty("cssName");
		setlook.verifyClassNotAdded(text);
		Assert.assertTrue(testConfig.getTestResult());
		
	}
	
	@Test(description = "Verify css class added for id", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyCssAppledtoID(Config testConfig)  {
	
		SettingsHelper shelper = new SettingsHelper(testConfig);
		SetLookPage setlook = shelper.doLoginAndApplytoID(1,7);
		setlook.makeChangesLive();
		PreviewPage previewpage = setlook.Preview();
		previewpage.verifyClassAddedForID();
		shelper.verifyTransacctionPageChange(0);
		Assert.assertTrue(testConfig.getTestResult());
	
		
	}
	
	@Test(description = "Verify css class added for id but deactivated", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyCssAppledtoIDDeactivated(Config testConfig)  {
	
		SettingsHelper shelper = new SettingsHelper(testConfig);
		SetLookPage setlook = shelper.doLoginAndApplytoID(1,7);
		setlook.clickDeactivate();
		setlook.makeChangesLive();
		PreviewPage previewpage = setlook.Preview();
		previewpage.verifyClassNotPresentForID();
		shelper.verifyTransacctionPageChange(1);
		Assert.assertTrue(testConfig.getTestResult());
	
		
	}
	
	@Test(description = "Verify css class removed for Id", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyCssRemovedtoID(Config testConfig)  {
	
		SettingsHelper shelper = new SettingsHelper(testConfig);
		SetLookPage setlook = shelper.doLoginAndRemoveCssFromID(1,7);
		PreviewPage previewpage = setlook.Preview();
		previewpage.verifyClassNotPresentForID();
		shelper.verifyTransacctionPageChange(1);
		Assert.assertTrue(testConfig.getTestResult());
	
		
	}
	
	@Test(description = "Verify css changes reset for Id both the place", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyCssResettoID(Config testConfig)  {
	
		SettingsHelper shelper = new SettingsHelper(testConfig);
		SetLookPage setlook = shelper.doLoginAndAddCss(1,7);
		setlook.Reset();
		PreviewPage previewpage = setlook.Preview();
		previewpage.verifyClassNotPresentForID();
		shelper.verifyTransacctionPageChange(1);
		Assert.assertTrue(testConfig.getTestResult());
	
		
	}
	@Test(description = "Verify css class added for id in preview but not in tranaction page", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyCssAppledtoIDForPreview(Config testConfig)  {
	
		SettingsHelper shelper = new SettingsHelper(testConfig);
		SetLookPage setlook = shelper.doLoginAndApplytoID(1,7);
		PreviewPage previewpage = setlook.Preview();
		previewpage.verifyClassAddedForID();
		shelper.verifyTransacctionPageChange(1);
		Assert.assertTrue(testConfig.getTestResult());
	
		
	}
	
	@Test(description = "Verify css class deactivated for in  preview, but not in transaction page ", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyCssAppledtoIDForLiveOnly(Config testConfig)  {
	
		SettingsHelper shelper = new SettingsHelper(testConfig);
		SetLookPage setlook = shelper.doLoginAndApplytoID(1,7);
		setlook.makeChangesLive();
		setlook.clickDeactivate();
		PreviewPage previewpage = setlook.Preview();
		previewpage.verifyClassNotPresentForID();
		shelper.verifyTransacctionPageChange(0);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	
}
