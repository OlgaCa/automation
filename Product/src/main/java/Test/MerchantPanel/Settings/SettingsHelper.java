package Test.MerchantPanel.Settings;

/**
 *
 */
import java.awt.AWTException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import PageObject.AdminPanel.Home.AdminPage;
import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.Home.LoginPage;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Settings.*;
import PageObject.MerchantPanel.Offers.OfferListPage;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import PageObject.MerchantPanel.Settings.*;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;
import Utils.Element.How;

public class SettingsHelper {

	public Config testConfig;

	public DashboardPage dPage;
	public SetLookPage setLookPage;
	public HomePage hPage;
	public PreviewPage previewPage;
	public PaymentOptionsPage pOPage;
	public TestResponsePage tRPage;
	public AdminPage aPage;
	public LoginPage lPage;
	public TransactionHelper thelper;
	public String expectedAmount = null;

	public SettingsHelper(Config testConfig) {
		this.testConfig = testConfig;
	}

	
	/** TODO
	 *  Login and create a new offer
	 * @param offerRow
	 * @param merchantLoginRow
	 * @param isExpired
	 * @return
	 
	 */
	public SetLookPage doLoginAndAddCss( int merchantLoginRow,int cssRow) {
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dPage = (DashboardPage) dHelper.doMerchantLogin(merchantLoginRow);
		
		//setLookPage = new SetLookPage(testConfig);
		
		// click new offer link
		setLookPage = dPage.clickAdvCustomizeLink();
		setLookPage.RemoveCss();
		setLookPage.fillCssForm(cssRow);
		setLookPage.clickSubmit();
		
		// fill create new offer form
		
		//offerListPage = OfferPage.clickSubmitButton();
		return setLookPage;
	}
	
	
	
	public SetLookPage doLoginAndApplytoID( int merchantLoginRow,int cssRow) {
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dPage = (DashboardPage) dHelper.doMerchantLogin(merchantLoginRow);
		
		//setLookPage = new SetLookPage(testConfig);
		
		// click new offer link
		setLookPage = dPage.clickAdvCustomizeLink();
		setLookPage.RemoveCss();
		setLookPage.addCssFroID();
		
		
		return setLookPage;
	}
	
	public SetLookPage doLoginAndRemoveCssFromID( int merchantLoginRow,int cssRow) {
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dPage = (DashboardPage) dHelper.doMerchantLogin(merchantLoginRow);
		
		//setLookPage = new SetLookPage(testConfig);
		
		// click new offer link
		setLookPage = dPage.clickAdvCustomizeLink();
		setLookPage.RemoveCss();
		setLookPage.makeChangesLive();
		
		
		return setLookPage;
	}
	
	public PreviewPage doLoginAndChangeStyle( int merchantLoginRow,String value) {
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dPage = (DashboardPage) dHelper.doMerchantLogin(merchantLoginRow);
		previewPage = dPage.clickCustomizeLink();
		//previewPage.GotoDefualt();
		previewPage.changeStyle(value);
		return previewPage;
	}
	
	public PreviewPage doLoginAndClickCustomizeLink( int merchantLoginRow) {
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dPage = (DashboardPage) dHelper.doMerchantLogin(merchantLoginRow);
		previewPage = dPage.clickCustomizeLink();
		return previewPage;
	}
	
	public void verifyTransacctionPageChange(int option){
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		
		helper.GetTestTransactionPage();
		PaymentOptionsPage pOPage = (PaymentOptionsPage) helper.GetPaymentOptionPage(1);
		if(option==1) {
		pOPage.verifyCssNotPresent();
		}
		else{
		pOPage.verifyCssPresent();
		}
		
		
	}
	
	
	public void verifyStyelChangeonTransacctionPage(int option,String Value){
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		helper.GetTestTransactionPage();
		PaymentOptionsPage pOPage = (PaymentOptionsPage) helper.GetPaymentOptionPage(1);
		if(option==1) {
			pOPage.verifystleChanged(Value);
		}
		else{
			pOPage.verifyStyleNotchanged(Value);
		}
	}
	
	public String getHtmlText(){
		String htmlText = null;
		if(Element.IsElementDisplayed(testConfig, Element.getPageElement(testConfig, How.id, "contents"))){
		 htmlText =  Element.getPageElement(testConfig, How.id, "contents").getText().replaceAll("<[^>]+>", "");
		}
		return htmlText;
	}
	

	
	

}
