package Test.MerchantPanel.Settings;

import java.awt.AWTException;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Settings.PreviewPage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Config;
import Utils.TestBase;

public class TestCustomizeTransaction  extends TestBase{
	@Test(description = "Verify Style changes", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifystyleChange(Config testConfig)  {
		String value = "font-family:arial;color:red;font-size:20px;";
		SettingsHelper shelper = new SettingsHelper(testConfig);
		PreviewPage prevPg = shelper.doLoginAndChangeStyle(1, value);
		prevPg.preview();
		prevPg.verifyStyleChange(value);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify Style changes after making chnages live", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifystyleChangeOnTransPage(Config testConfig)  {
		String value = "font-family:arial;color:red;font-size:20px;";
		SettingsHelper shelper = new SettingsHelper(testConfig);
		PreviewPage prevPg = shelper.doLoginAndChangeStyle(1, value);
		prevPg.preview();
		prevPg.makeChangesLive();
		prevPg.verifyStyleChange(value);
		shelper.verifyStyelChangeonTransacctionPage(1, value);
		Assert.assertTrue(testConfig.getTestResult());
	}
	@Test(description = "Verify Style not changes after making chnages live without preview", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifystyleNotChangeOnTransPage(Config testConfig)  {
		String value = "font-family:arial;color:red;font-size:20px;";
		SettingsHelper shelper = new SettingsHelper(testConfig);
		PreviewPage prevPg = shelper.doLoginAndChangeStyle(1, value);
		prevPg.makeChangesLive();
		prevPg.verifyStyleNotChange(value);
		shelper.verifyStyelChangeonTransacctionPage(0, value);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify Style changed to default in both the place", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyDefaultstyle(Config testConfig)  {
		String value = "font-family:arial;color:red;font-size:20px;";
		SettingsHelper shelper = new SettingsHelper(testConfig);
		PreviewPage prevPg = shelper.doLoginAndChangeStyle(1, value);
		prevPg.preview();
		prevPg.GotoDefualt();
		prevPg.verifyStyleNotChange(value);
		shelper.verifyStyelChangeonTransacctionPage(0, value);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify worng css deosnot change Style in both the place", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifWrongCssstyle(Config testConfig)  {
		String value = "font-family:ariadfds;";
		SettingsHelper shelper = new SettingsHelper(testConfig);
		PreviewPage prevPg = shelper.doLoginAndChangeStyle(1, value);
		prevPg.preview();
		prevPg.verifyStyleNotChange(value);
		shelper.verifyStyelChangeonTransacctionPage(0, value);
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	@Test(description = "Verify empty css deosnot change Style in both the place", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifemptyCssstyle(Config testConfig)  {
		String value = "";
		SettingsHelper shelper = new SettingsHelper(testConfig);
		PreviewPage prevPg = shelper.doLoginAndChangeStyle(1, value);
		prevPg.preview();
		prevPg.verifyStyleNotChange("font-family:arial");
		shelper.verifyStyelChangeonTransacctionPage(0, "font-family:arial");
		Assert.assertTrue(testConfig.getTestResult());
	}
	//uploadImage
	@Test(description = "Verify empty css deosnot change Style in both the place", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyImageUpload(Config testConfig)  {
		SettingsHelper shelper = new SettingsHelper(testConfig);
		PreviewPage prevPg = shelper.doLoginAndClickCustomizeLink(1);
		prevPg.uploadImage();
	}
	
	@Test(description = "Verify empty css deosnot change Style in both the place", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyEmailTemplate(Config testConfig)  {
		SettingsHelper shelper = new SettingsHelper(testConfig);
		PreviewPage prevPg = shelper.doLoginAndClickCustomizeLink(1);
		String html="<p class=MsoNormal><font size=2 face=Arial><span lang=EN-US style='font-size:10.0pt;font-family:Arial'>Thanks a lot for keeping track of the latest developments in the world of Internet Marketing through the <span class=SpellE>Mool</span> Blog. We at <span class=SpellE>Mool</span> sincerely believe in innovation and providing value to our patrons. Get your dope on the happenings through our brand new weekly newsletter. <o:p></o:p></span></font></p>";
		prevPg.enteremialTemplate(html);
		prevPg.submitEmailTemplate();
	}

}


