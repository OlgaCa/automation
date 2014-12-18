package Test.Security;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Home.AdminPage;
import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.Home.LoginPage;
import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.DCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.ErrorResponsePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.PayUHomePage;
import PageObject.MerchantPanel.Payments.EmailInvoicePage;

import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Helper;
import Utils.Popup;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestSecurity extends TestBase {

	
	@Test(description = "Verify Security by Appending Url" , dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_Security_byURL_append(Config testConfig) {

		String append = "<script>alert('mistake')</script>";
		
		try {
			AdminPage admin = new AdminPage(testConfig);
			LoginPage login = admin.ClickAdminLogin();
			changeUrlAndCheck(append, testConfig);

			HomePage home = login.Login();

			home.navigateToAdminHome();
			TransactionPage trans = home.ClickTestTransaction();
			changeUrlAndCheck(append, testConfig);
			
			int transactionRowNum = 1;
			int cardDetailsRowNum = 1;
			PaymentOptionsPage pop = trans.Submit();
			changeUrlAndCheck(append, testConfig);

			CCTab cctab = pop.clickCCTab();
			cctab.FillCardDetails(cardDetailsRowNum);
			pop.clickPayNow();
			changeUrlAndCheck(append, testConfig);
			
			home.navigateToAdminHome();
			trans = home.ClickTestMerchantTransaction();
			changeUrlAndCheck(append, testConfig);

			PayUHomePage payuHome = new PayUHomePage(testConfig);
			changeUrlAndCheck(append, testConfig);

			payuHome.fillMerchantLogin(transactionRowNum);
			payuHome.clickMerchantLogin();
			DashboardPage dashBoard = new DashboardPage(testConfig);
			dashBoard.ClickClose();
			changeUrlAndCheck(append, testConfig);

			dashBoard.ClickViewAll();
			changeUrlAndCheck(append, testConfig);
			dashBoard.ClickThroughEmailInvoice();
			changeUrlAndCheck(append, testConfig);
			dashBoard.clickThroughIVR();
			changeUrlAndCheck(append, testConfig);
			Assert.assertTrue(testConfig.getTestResult());
			

		} catch (Exception e) {
			testConfig.logException(e);  
			throw e;
		}
	}
	

	@Test(description = "Verify Security by Text Boxes" , dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_Security_byTextBoxes(Config testConfig) {

		
		
		try {
			ErrorResponsePage errorResponse;
			PaymentOptionsPage pop;
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			helper.DoLogin();
			
			
			int transactionRowNum = 48;
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
			String checksumError = helper.transactionData.GetData(transactionRowNum, "ErrorMessage");
			errorResponse = helper.trans.SubmitToGetErrorResponsePage();
			Helper.compareEquals(testConfig, "Script in Email",checksumError, errorResponse.getReasonforFailure());
			Popup.confirmNoPopup(testConfig, "mistake");
			
			transactionRowNum = 49;
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
			checksumError = helper.transactionData.GetData(transactionRowNum, "ErrorMessage");
			errorResponse = helper.trans.SubmitToGetErrorResponsePage();
			Helper.compareEquals(testConfig, "Script in FirstName",checksumError, errorResponse.getReasonforFailure());
			Popup.confirmNoPopup(testConfig, "mistake");
			
			transactionRowNum = 50;
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
			checksumError = helper.transactionData.GetData(transactionRowNum, "ErrorMessage");
			errorResponse = helper.trans.SubmitToGetErrorResponsePage();
			Helper.compareEquals(testConfig, "Script in Amount",checksumError, errorResponse.getReasonforFailure());
			Popup.confirmNoPopup(testConfig, "mistake");
			
			transactionRowNum = 51;
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
			pop = helper.trans.Submit();
			Popup.confirmNoPopup(testConfig, "mistake");
			
			transactionRowNum = 52;
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
			pop = helper.trans.Submit();
			Popup.confirmNoPopup(testConfig, "mistake");
			
			transactionRowNum = 48;
			helper.GetTestMerchantTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
			helper.trans.MerTransactionSubmit();
			Popup.confirmNoPopup(testConfig, "mistake");
			
			transactionRowNum = 49;
			helper.GetTestMerchantTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
			helper.trans.MerTransactionSubmit();
			Popup.confirmNoPopup(testConfig, "mistake");
			
			transactionRowNum = 50;
			helper.GetTestMerchantTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
			helper.trans.MerTransactionSubmit();
			Popup.confirmNoPopup(testConfig, "mistake");
			
			transactionRowNum = 51;
			helper.GetTestMerchantTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
			helper.trans.MerTransactionSubmit();
			Popup.confirmNoPopup(testConfig, "mistake");
			
			transactionRowNum = 52;
			helper.GetTestMerchantTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
			helper.trans.MerTransactionSubmit();
			Popup.confirmNoPopup(testConfig, "mistake");
			
			transactionRowNum = 1;
			helper.GetTestTransactionPage();
			
			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
			CCTab ccTab = (CCTab) helper.GetPaymentOptionsPage(1, PaymentMode.creditcard);

			int cardDetailsRowNum = 23;
			ccTab.FillCardDetails(cardDetailsRowNum);
			helper.payment.clickPayNowToGetError();
			Helper.compareEquals(helper.testConfig, "Script in Card name ",helper.cardDetailsData.GetData(12, "Name"), ccTab.getCardNameErrorMessage());
			
			helper.GetTestTransactionPage();
			ccTab = (CCTab) helper.GetPaymentOptionsPage(1, PaymentMode.creditcard);
			Popup.confirmNoPopup(testConfig, "mistake");
			
			helper.GetTestTransactionPage();
			ccTab = (CCTab) helper.GetPaymentOptionsPage(1, PaymentMode.creditcard);
			cardDetailsRowNum = 24;
			ccTab .FillCardDetails(cardDetailsRowNum);
			helper.payment.clickPayNowToGetError();
			Helper.compareEquals(helper.testConfig, "Script in Card Num ",helper.cardDetailsData.GetData(11, "CC"), ccTab.getCardNumberErrorMessage());
			
			helper.GetTestTransactionPage();
			ccTab = (CCTab) helper.GetPaymentOptionsPage(1, PaymentMode.creditcard);
			cardDetailsRowNum = 25;
			ccTab .FillCardDetails(cardDetailsRowNum);
			helper.payment.clickPayNowToGetError();
			Helper.compareEquals(helper.testConfig, "Script in CVV ",helper.cardDetailsData.GetData(12, "CVV"), ccTab.getCvvNumberErrorMessage());
			
			
			helper.GetTestTransactionPage();
			pop = helper.trans.Submit();
			cardDetailsRowNum = 23;
			helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
			helper.dcTab.SelectDebitCard("Visa Debit Cards (All Banks)");
			helper.dcTab.FillCardDetails(cardDetailsRowNum);
			helper.payment.clickPayNowToGetError();
			Helper.compareEquals(helper.testConfig, "Script in Card name ",helper.cardDetailsData.GetData(12, "Name"), helper.dcTab.getCardNameErrorMessage());
			
			helper.GetTestTransactionPage();
			pop = helper.trans.Submit();
			cardDetailsRowNum = 24;
			helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
			helper.dcTab.SelectDebitCard("Visa Debit Cards (All Banks)");
			helper.dcTab.FillCardDetails(cardDetailsRowNum);
			helper.payment.clickPayNowToGetError();
			Helper.compareEquals(helper.testConfig, "Script in Card Num ",helper.cardDetailsData.GetData(12, "CC"), helper.dcTab.getCardNumberErrorMessage());
			
			helper.GetTestTransactionPage();
			pop = helper.trans.Submit();
			cardDetailsRowNum = 25;
			helper.dcTab = (DCTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
			helper.dcTab.SelectDebitCard("Visa Debit Cards (All Banks)");
			helper.dcTab.FillCardDetails(cardDetailsRowNum);
			helper.payment.clickPayNowToGetError();
			Helper.compareEquals(helper.testConfig, "Script in CVV ",helper.cardDetailsData.GetData(12, "CVV"), helper.dcTab.getCvvNumberErrorMessage());

			
			
			int testRow = 54;

			DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
			dashboardHelper.doMerchantLogin(testRow);
			DashboardPage dashBoard = new DashboardPage(testConfig);
			dashBoard.ClickClose();
			
			EmailInvoicePage emailInvoicePage = dashBoard.ClickThroughEmailInvoice();
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			TestDataReader transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			String value = transactionDetail.GetData(30, "amount");
				Helper.compareEquals(testConfig, "Amount Entered", value,
						emailInvoicePage
								.getAmountErrorMessage());

			testRow = 55;

			emailInvoicePage = dashBoard.ClickThroughEmailInvoice();
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			value = transactionDetail.GetData(30, "email");
			
				Helper.compareEquals(testConfig, "Email Entered", value,emailInvoicePage.getEmailErrorMessage());

			testRow = 56;
			emailInvoicePage = dashBoard.ClickThroughEmailInvoice();
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			value = transactionDetail.GetData(30, "firstname");
			
				Helper.compareEquals(testConfig, "First namet Entered", value,emailInvoicePage.getNameErrorMessage());

			testRow = 57;
			emailInvoicePage = dashBoard.ClickThroughEmailInvoice();
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			value = transactionDetail.GetData(30, "phone");
			
				Helper.compareEquals(testConfig, "Amount Entered", value,emailInvoicePage.getPhoneErrorMessage());

			testRow = 58;
			emailInvoicePage = dashBoard.ClickThroughEmailInvoice();
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			value = transactionDetail.GetData(30, "productinfo");
			
				Helper.compareEquals(testConfig, "Product Info Entered", value,emailInvoicePage.getProdInfoErrorMessage());

			testRow = 59;
			emailInvoicePage = dashBoard.ClickThroughEmailInvoice();
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			value = transactionDetail.GetData(30, "address1 ");
			
				Helper.compareEquals(testConfig, "Address Entered", value,emailInvoicePage.getAddressErrorMessage());
			
			testRow = 54;

			dashboardHelper = new DashboardHelper(testConfig);
			dashboardHelper.doMerchantLogin(testRow);
			dashBoard = new DashboardPage(testConfig);
			dashBoard.ClickClose();
			
			emailInvoicePage = dashBoard.clickThroughIVR();;
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			value = transactionDetail.GetData(30, "amount");
			
				Helper.compareEquals(testConfig, "Amount Entered", value,
						emailInvoicePage
								.getAmountErrorMessage());

			testRow = 55;

			emailInvoicePage = dashBoard.clickThroughIVR();
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			value = transactionDetail.GetData(30, "email");
			
				Helper.compareEquals(testConfig, "Email Entered", value,emailInvoicePage.getEmailErrorMessage());

			testRow = 56;
			emailInvoicePage = dashBoard.clickThroughIVR();
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			value = transactionDetail.GetData(30, "firstname");
			
				Helper.compareEquals(testConfig, "First namet Entered", value,emailInvoicePage.getNameErrorMessage());

			testRow = 57;
			emailInvoicePage = dashBoard.clickThroughIVR();
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			value = transactionDetail.GetData(30, "phone");
			
				Helper.compareEquals(testConfig, "Amount Entered", value,emailInvoicePage.getPhoneErrorMessage());

			testRow = 58;
			emailInvoicePage = dashBoard.clickThroughIVR();
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			value = transactionDetail.GetData(30, "productinfo");
			
				Helper.compareEquals(testConfig, "Product Info Entered", value,emailInvoicePage.getProdInfoErrorMessage());

			testRow = 59;
			emailInvoicePage = dashBoard.clickThroughIVR();
			emailInvoicePage.fillInvoiceForm(testRow);
			emailInvoicePage.clickConfirmButton();
			Browser.wait(testConfig, 2);
			// verify field level error messages
			transactionDetail = new TestDataReader(testConfig,
					"TransactionDetails");
			value = transactionDetail.GetData(30, "address1 ");
			
				Helper.compareEquals(testConfig, "Address Entered", value,emailInvoicePage.getAddressErrorMessage());
			
			
			Assert.assertTrue(testConfig.getTestResult());
			
			
			
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}
	}
	
	
	
	
	@Test(description = "Verify Security by access tokens" , dataProvider = "GetTestConfig", timeOut=600000, groups = "1")
	public void test_Security_byAccessTokens(Config testConfig) {
		try {
			int transactionRowNum = 1;
			AdminPage admin = new AdminPage(testConfig);
			LoginPage login = admin.ClickAdminLogin();
			HomePage home = login.Login();
			home.navigateToAdminHome();
			TransactionPage trans = home.ClickTestTransaction();
			trans.FillTransactionDetails(transactionRowNum);
			
			
			int cardDetailsRowNum = 1;
			PaymentOptionsPage pop = trans.Submit();
			String txnid = pop.getTransactionId().split(" ")[1];
			TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
			String merchantid = data.GetCurrentEnvironmentData(1, "merchantid");
			testConfig.putRunTimeProperty("merchantid", merchantid);
			testConfig.putRunTimeProperty("txnid",txnid);
			
			Map<String,String> map = DataBase.executeSelectQuery(testConfig, 60, 1);
			String payuid = map.get("id");
			testConfig.putRunTimeProperty("payuid",payuid);
			 map = DataBase.executeSelectQuery(testConfig, 61, 1);
			String accesstoken1 = map.get("access_token");
				
			CCTab cctab = pop.clickCCTab();
			cctab.FillCardDetails(cardDetailsRowNum);
			pop.clickPayNow();

			map = DataBase.executeSelectQuery(testConfig, 61, 1);
			String accesstoken2 = map.get("access_token");
			boolean notEqual=true;
			if(accesstoken1.equals(accesstoken2))
				notEqual=false;
			
			Helper.compareTrue(testConfig, "Access tokens before and after paynow", notEqual);	
			
			home.navigateToAdminHome();
			 trans = home.ClickTestTransaction();
			trans.FillTransactionDetails(transactionRowNum);
			
			
			 cardDetailsRowNum = 22;
			 pop = trans.Submit();

			txnid = pop.getTransactionId().split(" ")[1];
			data = new TestDataReader(testConfig,"TransactionDetails");
			merchantid = data.GetCurrentEnvironmentData(1, "merchantid");
			testConfig.putRunTimeProperty("merchantid", merchantid);
			testConfig.putRunTimeProperty("txnid",txnid);
			
			map = DataBase.executeSelectQuery(testConfig, 60, 1);
			payuid = map.get("id");
			testConfig.putRunTimeProperty("payuid",payuid);
			 map = DataBase.executeSelectQuery(testConfig, 61, 1);
			accesstoken1 = map.get("access_token");
				
			cctab = pop.clickCCTab();
			cctab.FillCardDetails(cardDetailsRowNum);
			TryAgainPage tryagain = (TryAgainPage)pop.clickPayNowToGetTryAgainPage();
			Browser.wait(testConfig, 2);
			 cardDetailsRowNum = 1;
			cctab = (CCTab)tryagain.clickTryAgainButton();

			cctab.FillCardDetails(cardDetailsRowNum);
			pop.clickPayNow();
			map = DataBase.executeSelectQuery(testConfig, 61, 1);
			accesstoken2 = map.get("access_token");
			notEqual=true;
			if(accesstoken1.equals(accesstoken2))
				notEqual=false;
			
			Helper.compareTrue(testConfig, "Access tokens before and after try_again", notEqual);	
			
			Assert.assertTrue(testConfig.getTestResult());

		} catch (Exception e) {
			testConfig.logException(e);  
			throw e;
		}
	}
		
		
		
	
	private void changeUrlAndCheck(String append, Config testConfig) {
		
		WebDriver webdriver = testConfig.driver;
		StringBuffer url;
		url = new StringBuffer();
		String curUrl = webdriver.getCurrentUrl();
		testConfig.logComment("Current URL is:-" + curUrl);
		url.append(curUrl);
		if(curUrl.indexOf("?")==-1)
			url.append("?");
		else
			url.append("&");
		url.append(append);
		testConfig.logComment("Changing URL to:-" + url.toString());
		webdriver.get(url.toString());
		Popup.confirmNoPopup(testConfig, "mistake");
	}

}
