package Test.AdminPanel.Payments;

import org.testng.Assert;

import org.testng.annotations.Test;
import PageObject.AdminPanel.Payments.Response.CODChangeMobilePage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import PageObject.AdminPanel.Payments.PaymentOptions.CODTab;
import PageObject.AdminPanel.Payments.Response.CODMobileVerifyPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestTransactionCOD extends TestBase
{
	DashboardPage dashBoard;
	//TODO - when we stay on the page for some time, the retry page appears

	@Test(description="Verify COD transaction and redirection to Mobile verify page for Zip Dial Verification",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_COD_CODZipdial(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//DO COD with zipdial phone confirmation
		int transactionRowNum = 1;
		int paymentTypeRowNum = 55;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		helper.codMobileVerify =(CODMobileVerifyPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.CODMobileVerifyPage);

		// Verify Mobile verification message
		helper.codMobileVerify.VerifyMobileVerificationMessage(helper.codData,codRowNum);

		// Verify Mobile number 
		helper.codMobileVerify.VerifyShippingMobileNumber(helper.codData,codRowNum);

		// Verify Shipping Details
		helper.codMobileVerify.VerifyShippingDetails(helper.codData,codRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify COD transaction and redirection to Mobile verify page for Zip Dial Verification with convenience fee",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_COD_CODZipdial_CF(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//DO COD with zipdial phone confirmation
		int transactionRowNum = 68;
		int paymentTypeRowNum = 55;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		helper.codMobileVerify =(CODMobileVerifyPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.CODMobileVerifyPage);

		// Verify Mobile verification message
		helper.codMobileVerify.VerifyMobileVerificationMessage(helper.codData,codRowNum);

		// Verify Mobile number 
		helper.codMobileVerify.VerifyShippingMobileNumber(helper.codData,codRowNum);

		// Verify Shipping Details
		helper.codMobileVerify.VerifyShippingDetails(helper.codData,codRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify COD In-Progress transaction without Zip dial",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_COD_CODInProgress(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Do in-progress COD without Zipdial 
		int transactionRowNum = 23;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse =(TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, codRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify COD In-Progress transaction without Zip dial with convenience fee",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_COD_CODInProgress_CF(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		//Do in-progress COD without Zipdial 
		int transactionRowNum = 65;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		TestDataReader transactionData = new TestDataReader(testConfig, "TransactionDetails");
		String amnt = transactionData.GetData(transactionRowNum, "amount");
		double transactionamount = Double.parseDouble(amnt);

		String keyvalue = transactionData.GetData(transactionRowNum, "additionalCharges");
		String [] keyValue = keyvalue.split(":");
		keyvalue = keyValue[6];
		keyValue = keyvalue.split(",");
		String addCharge = keyValue[0].trim();

		//verify amount on processing page
		double additionalCharges = Double.parseDouble(addCharge);
		transactionamount = transactionamount+additionalCharges;	
		String transactionAmount = String.valueOf(transactionamount)+"0";
		testConfig.putRunTimeProperty("totalAmount", transactionAmount);
		testConfig.putRunTimeProperty("verifyProcessingPage", "true");

		helper.testResponse =(TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.TestResponsePage);
		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, codRowNum);

		testConfig.putRunTimeProperty("Amount", transactionAmount);
		testConfig.putRunTimeProperty("Amount to be Captured", transactionAmount);

		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);
		dashBoard.ClickCodVerify();
		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));
		dashboardHelper.transactionsPage.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verify that if user clicks on Go back link on COD payment option page, he/she is redirected to response page with user cancelled status
	 */
	@Test(description = "Verify that if user clicks on Go back link on COD payment option page, he/she is redirected to response page with user cancelled status",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CODUserCancelledTransaction(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 12;
		int paymentTypeRowNum = 55;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.GoBackResponse);
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verify that if user clicks on Go back link on COD mobile verify page, he/she is redirected to response page with user cancelled status
	 */
	@Test(description = "Verify that if user clicks on Go back link on COD mobile verify page, he/she is redirected to response page with user cancelled status",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CODUserCancelledOnMobileVerifyPage(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 12;
		int paymentTypeRowNum = 55;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		helper.codMobileVerify =(CODMobileVerifyPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.CODMobileVerifyPage);

		helper.testResponse = helper.codMobileVerify.clickGoBackToMerchantName(helper.transactionData.GetData(transactionRowNum, "Comments"));
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verify that if user clicks on Go back link on change mobile page, he/she is redirected to response page with user cancelled status
	 */
	@Test(description = "Verify that if user clicks on Go back link on COD mobile verify page, he/she is redirected to response page with user cancelled status",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CODUserCancelledOnChangeMobilePage(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 12;
		int paymentTypeRowNum = 55;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		helper.codMobileVerify =(CODMobileVerifyPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.CODMobileVerifyPage);
		CODChangeMobilePage codChangeMobile = helper.codMobileVerify.clickChangeMobileNumber();
		helper.testResponse = codChangeMobile.clickGoBackToMerchantName(helper.transactionData.GetData(transactionRowNum, "Comments"));
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify that user can change the COD Shipping details",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ChangeCODShippingDetails(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum = 55;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		helper.codMobileVerify =(CODMobileVerifyPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.CODMobileVerifyPage);
		CODTab codTab = helper.codMobileVerify.clickChangeDetailsButton();

		//Verify current details
		codTab.VerifyShippingDetailsOnCODEditPage(helper.codData,codRowNum);

		int newCodRowNum= 11;
		codTab.FillShippingDetails(newCodRowNum);
		helper.codMobileVerify = codTab.clickNext();

		// Verify Mobile verification message
		helper.codMobileVerify.VerifyMobileVerificationMessage(helper.codData,newCodRowNum);

		// Verify Mobile number 
		helper.codMobileVerify.VerifyShippingMobileNumber(helper.codData,newCodRowNum);

		// Verify Shipping Details
		helper.codMobileVerify.VerifyShippingDetails(helper.codData,newCodRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description="Verify that user can change the COD Mobile number",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ChangeCODMobileNumber(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum = 55;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		helper.codMobileVerify =(CODMobileVerifyPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.CODMobileVerifyPage);
		CODChangeMobilePage changeMobile= helper.codMobileVerify.clickChangeMobileNumber();

		// Verify current shipping details on Change mobile number page		            
		changeMobile.VerifyShippingDetailsOnMobileEditPage(helper.codData, codRowNum);

		changeMobile.verifyChangeMobileError(helper.codData);

		int newCodRowNum= 11;
		changeMobile.FillMobilenumber(helper.codData.GetData(newCodRowNum, "Mobile Number"));

		helper.codMobileVerify = changeMobile.ClickNext();

		// Verify Mobile verification message
		helper.codMobileVerify.VerifyMobileVerificationMessage(helper.codData,newCodRowNum);

		// Verify Mobile number 
		helper.codMobileVerify.VerifyShippingMobileNumber(helper.codData,newCodRowNum);

		// Verify Shipping Details
		helper.codMobileVerify.VerifyShippingDetails(helper.codData,codRowNum);

		//Change shipping details from change mobile page
		changeMobile = helper.codMobileVerify.clickChangeMobileNumber();
		CODTab codTab = changeMobile.ClickChangeDetails();

		codTab.FillShippingDetails(newCodRowNum);
		helper.codMobileVerify = codTab.clickNext();

		// Verify Mobile verification message
		helper.codMobileVerify.VerifyMobileVerificationMessage(helper.codData,newCodRowNum);

		// Verify Mobile number 
		helper.codMobileVerify.VerifyShippingMobileNumber(helper.codData,newCodRowNum);

		// Verify Shipping Details
		helper.codMobileVerify.VerifyShippingDetails(helper.codData,newCodRowNum);

		//click on change details again to get user cancelled
		helper.testResponse = helper.codMobileVerify.clickChangeDetailsButtonToGetUserCancelledResponse();
		System.out.println("Redmine ID for failure: 28996");
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Test if all label fields for COD are present on the payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_CODTabPageElements(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		CODTab codTab = (CODTab) helper.GetPaymentOptionsPage(1, PaymentMode.cod);

		//adding the wait since sometimes page is slow to load
		Browser.wait(testConfig, 2);

		// Verify COD page details
		String expected = helper.cardDetailsData.GetData(1, "COD");
		Helper.compareEquals(testConfig, "COD Tab text", expected, codTab.getCODTabText());

		expected = helper.cardDetailsData.GetData(10, "COD");
		Helper.compareEquals(testConfig, "COD Label", expected, codTab.getCODLabel());

		System.out.println("Redmine ID for failure: 28996");
		expected = helper.cardDetailsData.GetData(10, "Note");
		Helper.compareEquals(testConfig, "COD Note", expected, codTab.getNoteText());

		expected = helper.cardDetailsData.GetData(11, "COD");
		Helper.compareEquals(testConfig, "COD Verify Label", expected, codTab.getCODVerifyLabel());

		helper.codData = new TestDataReader(testConfig, "COD");
		expected = helper.codData.GetData(7, "Fname");
		Helper.compareEquals(testConfig, "Name Label", expected, codTab.getNameLabel());

		expected = helper.codData.GetData(7, "Address1");
		Helper.compareEquals(testConfig, "Address1 Label", expected, codTab.getAdd1Label());

		expected = helper.codData.GetData(7, "Address2");
		Helper.compareEquals(testConfig, "Address2 Label", expected, codTab.getAdd2Label());

		expected = helper.codData.GetData(7, "City");
		Helper.compareEquals(testConfig, "City Label", expected, codTab.getCityLabel());

		expected = helper.codData.GetData(7, "State");
		Helper.compareEquals(testConfig, "State Lable", expected, codTab.getStateLabel());

		expected = helper.codData.GetData(7, "Country");
		Helper.compareEquals(testConfig, "Country Label", expected, codTab.getCountryLabel());

		expected = helper.codData.GetData(7, "Zip Code");
		Helper.compareEquals(testConfig, "Zip code Label", expected, codTab.getZipLabel());

		expected = helper.codData.GetData(7, "Mobile Number");
		Helper.compareEquals(testConfig, "Phone Label", expected, codTab.getPhoneLabel());

		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * @author prashant.singh
	 * Verify that amount and transaction id displayed on Payment Option Page matches the one given in Test Transaction Page
	 */
	@Test(description="Verify that amount and transaction id displayed on Payment Option Page matches the one given in Test Transaction Page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_AmountAndTranasctionIdOnMobileVerifyPage(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum = 55;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		helper.codMobileVerify =(CODMobileVerifyPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.CODMobileVerifyPage);
		CODChangeMobilePage changeMobile= helper.codMobileVerify.clickChangeMobileNumber();

		String expectedTxnId = testConfig.getRunTimeProperty("transactionId");
		String expectedAmount = helper.transactionData.GetData(transactionRowNum, "amount");
		String expectedPaymentTypeLabel = "Choose a payment method";

		Helper.compareEquals(testConfig, "Choose PaymentType Label", expectedPaymentTypeLabel, helper.payment.getChoosePaymentTypeLabel());
		Helper.compareEquals(testConfig, "Transaction Amount", "Rs. " + expectedAmount, helper.payment.getAmount());
		Helper.compareEquals(testConfig, "Transaction Id", "ID: " + expectedTxnId, helper.payment.getTransactionId());

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description="Verify the links on footer of Mobile Verify Page",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_MobileVerifyPageFooterLinks(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum = 55;
		int cardDetailsRowNum = -1;
		int codRowNum = 1;

		helper.GetTestTransactionPage();
		helper.codMobileVerify =(CODMobileVerifyPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, codRowNum, ExpectedResponsePage.CODMobileVerifyPage);
		String expectedFooterNote = helper.codData.GetData(1, "Note");

		Helper.compareEquals(testConfig, "Footer Note Text", expectedFooterNote, helper.codMobileVerify.getFooterNoteText());

		String oldWindow = "";
		String expectedUrl = "";

		oldWindow = helper.codMobileVerify.clickVerifiedByVisaLink();
		expectedUrl = "https://verified.visa.com/aam/activation/landingpage.aam";
		VerifyUrlAndCloseThisWindowAndSwitchToOld(testConfig, expectedUrl, oldWindow);

		oldWindow = helper.codMobileVerify.clickMasterCardLink();
		expectedUrl = "http://www.mastercard.com/us/business/en/corporate/securecode/sc_popup.html?language=en";
		VerifyUrlAndCloseThisWindowAndSwitchToOld(testConfig, expectedUrl, oldWindow);

		oldWindow = helper.codMobileVerify.clickVeriSignSecuredLink();
		expectedUrl = "https://sealinfo.verisign.com/splash?form_file=fdf/splash.fdf&dn=www.payu.in&lang=en";
		VerifyUrlAndCloseThisWindowAndSwitchToOld(testConfig, expectedUrl, oldWindow);

		oldWindow = helper.codMobileVerify.clickPciLink();
		expectedUrl = "https://seal.controlcase.com/index.php?page=showCert&cId=3877025869";
		VerifyUrlAndCloseThisWindowAndSwitchToOld(testConfig, expectedUrl, oldWindow);

		helper.codMobileVerify.AmexSafetyLinkNotPresent();
		helper.codMobileVerify.AmexLinkNotpresent();

		Assert.assertTrue(testConfig.getTestResult());
	}

	private void VerifyUrlAndCloseThisWindowAndSwitchToOld(Config testConfig, String expectedUrl, String oldWindowHandle)
	{
		Browser.verifyURL(testConfig, expectedUrl);
		Browser.closeBrowser(testConfig);
		Browser.switchToGivenWindow(testConfig, oldWindowHandle);
	}

	@Test(description="Verify COD transaction with invalid input and Ltrim",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_ErrorMessagesOnCODTab(Config testConfig)  
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		int transactionRowNum = 1;

		helper.codTab =(CODTab) helper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.cod);
		helper.codData = helper.codTab.FillShippingDetails(1);

		// Input special Chars
		verifyErrors(helper, 2, 
				helper.codData.GetData(8, "Fname"), helper.codData.GetData(8, "Lname")
				, helper.codData.GetData(8, "Address1"), helper.codData.GetData(8, "Address2")
				, helper.codData.GetData(8, "City"), helper.codData.GetData(8, "Country")
				, helper.codData.GetData(8, "State"), helper.codData.GetData(8, "Zip Code")
				, helper.codData.GetData(8, "Mobile Number"));

		//Mandatory input
		verifyErrors(helper, 3, 
				helper.codData.GetData(10, "Fname"), helper.codData.GetData(10, "Lname")
				, helper.codData.GetData(10, "Address1"), helper.codData.GetData(10, "Address2")
				, helper.codData.GetData(10, "City"), helper.codData.GetData(10, "Country")
				, helper.codData.GetData(10, "State"), helper.codData.GetData(10, "Zip Code")
				, helper.codData.GetData(10, "Mobile Number"));

		//Wrong Input
		verifyErrors(helper, 4, 
				helper.codData.GetData(8, "Fname"), helper.codData.GetData(8, "Lname")
				, helper.codData.GetData(9, "Address1"), helper.codData.GetData(9, "Address2")
				, helper.codData.GetData(8, "City"), helper.codData.GetData(8, "Country")
				, helper.codData.GetData(8, "State"), helper.codData.GetData(9, "Zip Code")
				, helper.codData.GetData(9, "Mobile Number"));

		//Spaces Input
		verifyErrors(helper, 5, 
				helper.codData.GetData(10, "Fname"), helper.codData.GetData(10, "Lname")
				, helper.codData.GetData(10, "Address1"), helper.codData.GetData(10, "Address2")
				, helper.codData.GetData(10, "City"), helper.codData.GetData(10, "Country")
				, helper.codData.GetData(10, "State"), helper.codData.GetData(10, "Zip Code")
				, helper.codData.GetData(10, "Mobile Number"));

		//With Spaces
		verifyErrors(helper, 6, 
				helper.codData.GetData(9, "Fname"), helper.codData.GetData(9, "Lname")
				, helper.codData.GetData(9, "Address1"), helper.codData.GetData(9, "Address2")
				, helper.codData.GetData(9, "City"), helper.codData.GetData(9, "Country")
				, helper.codData.GetData(9, "State"), helper.codData.GetData(8, "Zip Code")
				, helper.codData.GetData(8, "Mobile Number"));

		Assert.assertTrue(testConfig.getTestResult());
	}

	private void verifyErrors(TransactionHelper helper, int inputCODRowNum, String FNameError, String LNameError, String Addr1Error, String Addr2Error, String CityError, String CountryError, String StateError, String ZipError, String MobileError)
	{
		helper.codData = helper.codTab.FillShippingDetails(inputCODRowNum);
		helper.codTab.clickNextGetError();

		String comments = helper.codData.GetData(inputCODRowNum, "Comments");

		Helper.compareEquals(helper.testConfig,  "First name '"+ comments +"' error", FNameError, helper.codTab.getShippingFNameError());

		Helper.compareEquals(helper.testConfig,  "Last name '"+ comments +"' error", LNameError, helper.codTab.getShippingLnameError());

		Helper.compareEquals(helper.testConfig,  "Address1 '"+ comments +"' error", Addr1Error, helper.codTab.getShippingAdd1Error());

		Helper.compareEquals(helper.testConfig,  "Address2 '"+ comments +"' error", Addr2Error, helper.codTab.getShippingAdd2Error());

		System.out.println("Redmine ID for this bug: 28998");
		Helper.compareEquals(helper.testConfig,  "City '"+ comments +"' error", CityError, helper.codTab.getShippingCityError());
		
		System.out.println("Redmine ID for this bug: 28998");
		Helper.compareEquals(helper.testConfig,  "Country '"+ comments +"' error", CountryError, helper.codTab.getShippingCountryError());

		helper.codTab.verifyShippingStateErrorAbsent();

		Helper.compareEquals(helper.testConfig,  "Zip Code'"+ comments +"' error", ZipError, helper.codTab.getShippingZipError());

		Helper.compareEquals(helper.testConfig,  "Mobile Number '"+ comments +"' error", MobileError, helper.codTab.getShippingPhoneError());

	}
}