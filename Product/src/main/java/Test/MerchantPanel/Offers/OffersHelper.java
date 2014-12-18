package Test.MerchantPanel.Offers;

/**
 * Author: Vidya Priyadarshini
 */

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import PageObject.AdminPanel.Home.AdminPage;
import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.Home.LoginPage;
import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Offers.CreateNewOfferPage;
import PageObject.MerchantPanel.Offers.OfferListPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;
import Utils.Element.How;

public class OffersHelper {

	public Config testConfig;

	public DashboardPage dPage;
	public OfferListPage offerListPage;
	public CreateNewOfferPage OfferPage;
	public HomePage hPage;
	public TransactionPage tPage;
	public PaymentOptionsPage pOPage;
	public CCTab ccTabPage;
	public TestResponsePage tRPage;
	public AdminPage aPage;
	public LoginPage lPage;
	public String expectedAmount = null;

	public OffersHelper(Config testConfig) {
		this.testConfig = testConfig;
	}

	
	/** TODO
	 *  Login and create a new offer
	 * @param offerRow
	 * @param merchantLoginRow
	 * @param isExpired
	 * @return
	 
	 */
	public OfferListPage doLoginAndCreateOffer(int offerRow,
			int merchantLoginRow, Boolean isExpired)  {
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		// merchant login
		dPage = (DashboardPage) dHelper.doMerchantLogin(merchantLoginRow);
		
		offerListPage = new OfferListPage(testConfig);
		// click new offer link
		OfferPage = offerListPage.clickCreateNewOfferLink();
		if (merchantLoginRow == 47)
		{
		
			Helper.compareEquals(testConfig, "dollar sign appears instead of rupee", "$", OfferPage.discountUnit().substring(0,1));
		
		}
		// fill create new offer form
		OfferPage.fillOfferForm(offerRow, false, isExpired);
		// click submit
		offerListPage = OfferPage.clickSubmitButton();
		return offerListPage;
	}

	/**
	 * Login and view offer list table
	 * 
	 * @param merchantLoginRow
	 */
	public OfferListPage doLoginAndViewOfferList(int merchantLoginRow) {
		DashboardHelper dHelper = new DashboardHelper(testConfig);
		offerListPage = new OfferListPage(testConfig);
		// merchant login
		dPage = (DashboardPage) dHelper.doMerchantLogin(merchantLoginRow);
		// navigate to offer list page
		Browser.navigateToURL(testConfig,
				testConfig.getRunTimeProperty("OfferListUrl"));

		return new OfferListPage(testConfig);
	}

	/**
	 * Method to extract a given offer row entry
	 */
	public String[] getOfferRowEntry(String offerTitle){
		String xPath =null;
		String[] offerRowValue = new String[15];
		WebElement offerCell = null;
		for(int i =1;i<=14;i++)
		{
			xPath = "//html/body/div[1]/table/tbody/tr//td[.='" + offerTitle + "']/../td[" + i + "]";
			offerCell = Element.getPageElement(testConfig, How.xPath,xPath);
			offerRowValue[i] = offerCell.getText();
			
		}
		
		return offerRowValue;
	}
		/**
	 * Method to extract all offer rews from Offer List Table
	 */
	public List<WebElement> getOfferListTable() {
		WebElement offersTable = Element.getPageElement(testConfig, How.xPath,
				"//tbody");

		// get all rows in the offer list table
		List<WebElement> allRows = offersTable.findElements(By.tagName("tr"));
		return allRows;
	}

	/*
	 * Gets all the offer keys from Offer List table
	 */

	public String[] getOfferKeys(List<WebElement> allRows) {
		String[] offerKeys = new String[allRows.size() - 1];
		int i = 0;

		for (WebElement row : allRows) {
			if (!(row.getText().contains("Offer key"))) {
				List<WebElement> cells = row.findElements(By.tagName("td"));
				offerKeys[i] = cells.get(0).getText();
				i++;
			}
		}
		return offerKeys;
	}

	/**
	 * Return offer Id for given offer title
	 * 
	 * @return
	 */
	public String getOfferId(String offerTitle) {

		String[] offerEntryRow = getOfferRowEntry(offerTitle);
		String offerKey = offerEntryRow[1];
		// String[] offerKeys = getOfferKeys(offerTable);
		// int len = offerKeys.length - 1;
		Pattern pattern = Pattern.compile("@(.*)");
		Matcher matcher = pattern.matcher(offerKey);
		if (matcher.find())
			return matcher.group().substring(1);
		else
			return null;
	}
	
	/**
	 * Return offer type for given offer title
	 * 
	 * @return
	 */
	public String getOfferType(String offerTitle) {

		String[] offerEntryRow = getOfferRowEntry(offerTitle);
		return offerEntryRow[14];
	}
	
	/**
	 * Return offer type for given offer title
	 * 
	 * @return
	 */
	public String getOfferCount(String offerTitle) {

		String[] offerEntryRow = getOfferRowEntry(offerTitle);
		return offerEntryRow[6];
	}
	
	/**
	 * Return offer type for given offer title
	 * 
	 * @return
	 */
	public String getOfferRemainingCount(String offerTitle) {

		String[] offerEntryRow = getOfferRowEntry(offerTitle);
		return offerEntryRow[7];
	}

	/**
	 * Return offer Key for given offer title
	 * 
	 * @param offerRow
	 * @param offerId
	 */
	public String getOfferKey(String offerTitle) {
		String[] offerEntryRow = getOfferRowEntry(offerTitle);
		return offerEntryRow[1];
	}

	/**
	 * Click Edit offer link for a given offer
	 */
	public CreateNewOfferPage clickEditOfferLink(int offerRow, String offerId) {
		String xPath = "//a[@href='https://info.payu.in/merchant/newOffer?offerId="
				+ offerId + "']/font";
		// click edit offer link
		Element.doubleClick(testConfig,
				Element.getPageElement(testConfig, How.xPath, xPath),
				"Edit offer link");
		return OfferPage;
	}

	/**
	 * Login as admin
	 */
	public void doAdminLogin() {
		aPage = new AdminPage(testConfig);
		// login as admin
		lPage = aPage.ClickAdminLogin();
		hPage = lPage.Login();

	}

	/**
	 * Fill details on test transaction page for given offer and amount
	 * 
	 */
	public PaymentOptionsPage fillTransactionDetailsWithGivenAmountAndOffer(
			String offerKey, String amount) {

		// click Test Transaction link
		tPage = hPage.ClickTestTransaction();
		// Fill Test Transaction Details
		TestDataReader data = new TestDataReader(testConfig,
				"TransactionDetails");

		String value = "";

		// select merchant
		value = data.GetCurrentEnvironmentData(1, "Key");
		Element.selectValue(testConfig,
				Element.getPageElement(testConfig, How.name, "key"), value,
				"Merchant");
		// enter key
		Element.enterData(testConfig,
				Element.getPageElement(testConfig, How.name, "offer_key"),
				offerKey, "Enter valid offer key");
		// enter amount
		Element.enterData(testConfig, Element.getPageElement(testConfig,
				How.xPath, "//input[@name='amount']"), amount,
				"Enter given amount");
		// enter random email
		value = Helper.generateRandomAlphabetsString(5);
		Element.enterData(testConfig, Element.getPageElement(testConfig,
				How.xPath, "//input[@id='email']"), value, "fill random email");
		// enter random name
		value = Helper.generateRandomAlphabetsString(5);
		Element.enterData(testConfig, Element.getPageElement(testConfig,
				How.xPath, "//input[@id='firstname']"), value,
				"fill random first name");

		expectedAmount = Element.getPageElement(testConfig, How.xPath,
				"//input[@name='amount']").getText();
		pOPage = tPage.Submit();
		return pOPage;

	}

	/**
	 * Fill valid credit card details and click pay now
	 * @param cardRowNum
	 * @return
	 */
	public TestResponsePage makeValidPayment(int cardRowNum) {
		ccTabPage = pOPage.clickCCTab();
		ccTabPage.FillCardDetails(cardRowNum);

		tRPage = pOPage.clickPayNow();
		return tRPage;
	}
	
	public void setOfferInfo(String offerTitle) {
		testConfig.putRunTimeProperty("Offer Title", offerTitle);
		String offerKey = getOfferKey(offerTitle);
		String offerId = getOfferId(offerTitle);
		String offerType = getOfferType(offerTitle);
		String offerCount = getOfferCount(offerTitle);
		String offerRemainingCount = getOfferRemainingCount(offerTitle);
		testConfig.putRunTimeProperty("offerKey", offerKey);
		testConfig.putRunTimeProperty("offerId", offerId);
		testConfig.putRunTimeProperty("offerType", offerType);
		testConfig.putRunTimeProperty("offerCount", offerCount);
		testConfig.putRunTimeProperty("offerRemainingCount", offerRemainingCount);
	}

	/**
	 * Verify offer key appears in Transaction Details Page
	 * 
	 * @param offerKey
	 */
	public void verifyViewTransactionOfferKey(String offerKey) {
		WebElement viewTxOfferKey = Element
				.getPageElement(testConfig, How.xPath,
						"//div[@id='general_popup_content']/div/div/div/div/div[2]/div[7]/div/div[4]");
		Helper.compareEquals(testConfig,
				"Offer Key in TransactionDetails Page", offerKey,
				viewTxOfferKey.getText());
	}

	/*
	 * Verify discount, amount and offer key details in transaction post
	 * response page
	 */
	public void verifyOfferTransaction(String offerKey, int row) {
		TestDataReader offerData = new TestDataReader(testConfig,
				"OfferDetails");

		String expectedDiscount = offerData.GetData(row, "Discount");

		Helper.compareEquals(testConfig, "Amount", expectedAmount,
				tRPage.actualResponse.get("amount"));
		Helper.compareEquals(testConfig, "Discount", expectedDiscount,
				tRPage.actualResponse.get("discount"));
		Helper.compareEquals(testConfig, "offer key", offerKey,
				tRPage.actualResponse.get("offer"));
	}

	/*
	 * Verify discount, amount and offer key details in transaction post
	 * response page
	 */
	public void verifyOfferTransaction(String offerKey, String expectedAmount,
			String expectedDiscount) {

		Helper.compareEquals(testConfig, "Amount", expectedAmount,
				tRPage.actualResponse.get("amount"));
		Helper.compareEquals(testConfig, "Discount", expectedDiscount,
				tRPage.actualResponse.get("discount"));
		Helper.compareEquals(testConfig, "offer key", offerKey,
				tRPage.actualResponse.get("offer"));
	}

	/**
	 * 
	 * Verify discount,amount,offerKey and offer failure reason in transaction
	 * response page
	 */
	public void verifyTransactionForExpiredOffer() {
		String offerKey = testConfig.getRunTimeProperty("offerKey");
		Helper.compareEquals(testConfig, "Amount", expectedAmount,
				tRPage.actualResponse.get("amount"));
		Helper.compareEquals(testConfig, "Discount", "0",
				tRPage.actualResponse.get("discount"));
		Helper.compareEquals(testConfig, "offer key", offerKey,
				tRPage.actualResponse.get("offer"));
		// verify failure reason
		Helper.compareContains(testConfig, "offer failure reason",
				"Offer expired.",
				tRPage.actualResponse.get("offer_failure_reason"));

	}

	public void createOfferAndMakeTransaction(boolean isExpired,boolean isSeamless,boolean createOffer,String amount,String discount,int offerRow,int merchantLoginRow ){
		TransactionHelper helper = new TransactionHelper(testConfig, isSeamless);
		if (createOffer==true){
		// login and create offer with count 1
		doLoginAndCreateOffer(offerRow,merchantLoginRow, isExpired);
		String offerKey = getOfferKey(testConfig.getRunTimeProperty("Offer Title"));
		testConfig.putRunTimeProperty("offerKey", offerKey);
		}
		// login to admin
		helper.DoLogin();
		// make transaction with created offer
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("amount", amount);
		testConfig.putRunTimeProperty("discount", discount);
		
	}
}
