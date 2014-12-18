package Test.AdminPanel.Filters;

import java.io.IOException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import PageObject.AdminPanel.Filters.RiskHomePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestBlackListing extends TestBase {

	/**
	 * Add annd Remove the filter Test case no:Product-1099
	 * 
	 * @param testConfig
	 * @throws IOException
	 */
	@Test(enabled = false, description = "AddAndRemoveBlackListingForMerchantScope", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void addAndRemoveBlackListingForMerchantScope(Config testConfig)
			throws IOException {

		// log into the application
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();

		// steps to add the filter get the file path and click on risk link
		String path = System.getProperty("user.dir")
				+ "\\Parameters\\filter.xls";
		RiskHomePage riskPage = helper.home.ClickonlnkRisk();

		Element.click(testConfig, riskPage.lnkUploadFilters,
				"Click on Upload Filters Link");
		TestDataReader treader = new TestDataReader(testConfig, "Sheet1", path);

		if (!treader.GetData(1, "directive").equals("add")) {
			// change the filter data sheet directive to remove
			treader.setCellData(1, "directive", "add",
					"Changes directive value to remove");

		}
		// before adding check if the filter already exists if yes then skip
		// adding the filter
		if (!riskPage.isFilterExists(path, "1", 111)) {
			riskPage.AddNRemoveFilters("ADDED", path);
			boolean result = riskPage.verifyFilter("1", 111);
			Helper.compareTrue(testConfig, "that given filter in DB is active",
					result);

		}
		// steps to remove the filter
		treader = new TestDataReader(testConfig, "Sheet1", path);
		// change the filter data sheet directive to remove
		if (!treader.GetData(1, "directive").equals("remove")) {

			treader.setCellData(1, "directive", "remove",
					"Changes directive value to remove");
		}
		// remove the filter
		riskPage.AddNRemoveFilters("REMOVED", path);
		boolean result = riskPage.verifyFilter("", 112);
		Helper.compareTrue(testConfig, "that given filter is not active",
				result);
		Assert.assertTrue(testConfig.getTestResult());

	}

	/**
	 * Test case for Verifying error message on setting same rule twice Test
	 * case no: Product-1094:
	 * 
	 * @param testConfig
	 * @throws IOException
	 */
	@Test(enabled = false, description = "errorMessageOnSettingSameRule", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void errorMessageOnSettingSameRule(Config testConfig)
			throws IOException {

		// log into the application
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		String path = System.getProperty("user.dir")
				+ "\\Parameters\\filter.xls";

		TestDataReader treader = new TestDataReader(testConfig, "Sheet1", path);
		// check if filter testdata has directive value as remove if yes then
		// change it to add
		if (treader.GetData(1, "directive").equals("remove")) {
			treader.setCellData(1, "directive", "add",
					"Changes directive value to remove");
		}

		String paramValue = treader.GetData(1, "parameter_value");
		String expectedRowResult = "SQLSTATE[23000]: Integrity constraint violation: 1062 Duplicate entry 'email-"
				+ paramValue + "-2-6740-1' for key 'uniqueness'";
		RiskHomePage riskPage = helper.home.ClickonlnkRisk();

		Element.click(testConfig, riskPage.lnkUploadFilters,
				"Click on Upload Filters Link");

		// check if filter already exists if yes then add once again else add
		// twice to check error message
		if (riskPage.isFilterExists(path, "1", 111)) {
			riskPage.AddNRemoveFilters(expectedRowResult, path);
			boolean result = riskPage.verifyFilter("1", 111);
			Helper.compareTrue(testConfig, "that given filter in DB is active",
					result);
		} else {
			riskPage.AddNRemoveFilters("ADDED", path);
			boolean result = riskPage.verifyFilter("1", 111);
			Helper.compareTrue(testConfig, "that given filter in DB is active",
					result);
			riskPage.AddNRemoveFilters(expectedRowResult, path);
		}
		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verify that a card number 531746XXXXXX6253 gets blacklisted for global
	 * scope
	 * 
	 * @param testConfig
	 */
	@Test(enabled = false, description = "Verify that a card number gets blacklisted for global scope", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void blackListedCardGlobally(Config testConfig) {

		// Navigate till Transaction Page
		RiskHomePage riskHome = new RiskHomePage(testConfig);
		testConfig.putRunTimeProperty("cardnum", "531746XXXXXX6253");
		TransactionHelper helper = riskHome.getTransactionPage(testConfig);
		// perform the transaction
		String filterId = "114";
		// verify if the filter is active or not

		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 145, 69, 372, helper);
		}

		// verify if same filterId is present in DB

		if (riskHome.verifyFilterId(113, filterId)) {
			Reporter.log("Filter Rule was Executed and an entry has been created in the DB");
		}
		Assert.assertTrue(testConfig.getTestResult());
		// we have not automated Steps to perform Transaction with other card
		// number because it is covered in other test cases
	}

	/**
	 * Verify that the phone number 9090909090 gets blaclisted on category scope
	 * 
	 * @param testConfig
	 */
	@Test(enabled = false, description = "Verifying blacklisted phone no.for category scope", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void blackListedPhoneNoOnCategory(Config testConfig) {
		RiskHomePage riskHome = new RiskHomePage(testConfig);
		// Navigate till Transaction Page

		TransactionHelper helper = riskHome.getTransactionPage(testConfig);

		// perform the transaction
		String filterId = "108";

		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 140, 1, 372, helper);
		}
		// verify if same filterId is present or not in DB

		if (riskHome.verifyFilterId(113, filterId)) {
			Reporter.log("Filter Rule was Executed and an entry has been created in the DB");
		}

		// Steps to perform Transaction with other category merchant key and
		// blacklisted phone number.
		helper.GetTestTransactionPage();
		testConfig.putRunTimeProperty("phone", "9090909090");

		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 138, 1, 5, helper);
		}
		if (!riskHome.verifyFilterId(113, "")) {
			Reporter.log("Filter Rule was not Executed and no entry was  created in the DB");
		}
		Assert.assertTrue(testConfig.getTestResult());
		// we have not automated Steps to perform Transaction with other phone
		// number as it covers in other test cases
	}

	/**
	 * Verify that an email block@example.com gets blacklisted for merchant
	 * scope
	 * 
	 * @param testConfig
	 */
	@Test(enabled = false, description = "Verify that an email gets blacklisted for merchant scope", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void blackListedEmailOnMerchant(Config testConfig) {
		// Navigate till Transaction Page

		RiskHomePage riskHome = new RiskHomePage(testConfig);
		testConfig.putRunTimeProperty("email", "block@example.com");

		TransactionHelper helper = riskHome.getTransactionPage(testConfig);
		// perform the transaction
		String filterId = "108";

		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 140, 1, 372, helper);
		}

		// verify if same filterId is present or not in DB

		if (riskHome.verifyFilterId(113, filterId)) {
			Reporter.log("Filter Rule was Executed and an entry has been created in the DB");
		}

		// Steps to perform Transaction with other merchant key and blacklisted
		// email id

		helper.GetTestTransactionPage();
		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 139, 1, 5, helper);
		}
		if (!riskHome.verifyFilterId(113, "")) {
			Reporter.log("Filter Rule was not Executed and no entry was been created in the DB");
		}
		Assert.assertTrue(testConfig.getTestResult());
		// we have not automated Steps to perform Transaction with other email
		// id as it covers in other test cases
	}

	/**
	 * Verify that an email testblack@global.com gets blacklisted for global
	 * scope
	 * 
	 * @param testConfig
	 */
	@Test(enabled = false, description = "Verify that an email gets blacklisted for global scope", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void blackListedEmailGlobal(Config testConfig) {
		// Navigate till Transaction Page

		RiskHomePage riskHome = new RiskHomePage(testConfig);
		testConfig.putRunTimeProperty("email", "testblack@global.com");
		TransactionHelper helper = riskHome.getTransactionPage(testConfig);
		// perform the transaction
		String filterId = "124";

		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 141, 1, 372, helper);
		}

		// verify if same filterId is present or not in DB

		if (riskHome.verifyFilterId(113, filterId)) {
			Reporter.log("Filter Rule was Executed and an entry has been created in the DB");
		}

		Assert.assertTrue(testConfig.getTestResult());
		// we have not automated Steps to perform Transaction with other email
		// id as it is covered in other test cases
	}

	/**
	 * Verifying blacklisted email category@blacklist.com for category scope
	 * 
	 * @param testConfig
	 */
	@Test(enabled = false, description = "Verifying blacklisted email for category scope", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void blackListedEmailOnCategory(Config testConfig) {
		// Navigate till Transaction Page

		RiskHomePage riskHome = new RiskHomePage(testConfig);
		testConfig.putRunTimeProperty("email", "category@blacklist.com");
		TransactionHelper helper = riskHome.getTransactionPage(testConfig);
		// perform the transaction
		String filterId = "126";

		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 142, 1, 372, helper);
		}

		// verify if same filterId is present or not in DB

		if (riskHome.verifyFilterId(113, filterId)) {
			Reporter.log("Filter Rule was Executed and an entry has been created in the DB");
		}
		helper.GetTestTransactionPage();
		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 139, 1, 5, helper);

		}
		if (!riskHome.verifyFilterId(113, "")) {
			Reporter.log("Filter Rule was not Executed and no entry was been created in the DB");
		}
		Assert.assertTrue(testConfig.getTestResult());
		// we have not automated Steps to perform Transaction with other email
		// id as it is covered in other test cases
	}

	/**
	 * Verify that a phone 9000000009 gets blacklisted for merchant scope
	 * 
	 * @param testConfig
	 */
	@Test(enabled = false, description = "Verify that a phone gets blacklisted for merchant scope", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void blackListedPhoneNoOnMerchant(Config testConfig) {
		RiskHomePage riskHome = new RiskHomePage(testConfig);

		// Navigate till Transaction Page
		testConfig.putRunTimeProperty("phone", "9000000009");
		TransactionHelper helper = riskHome.getTransactionPage(testConfig);
		// perform the transaction
		String filterId = "130";
		// verify if the filter is active or not

		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 143, 1, 372, helper);
		}

		// verify if same filterId is present or not in DB

		if (riskHome.verifyFilterId(113, filterId)) {
			Reporter.log("Filter Rule was Executed and an entry has been created in the DB");
		}
		helper.GetTestTransactionPage();

		// Steps to perform Transaction with other merchant key and blacklisted
		// phone number.

		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 139, 1, 5, helper);
		}
		if (!riskHome.verifyFilterId(113, "")) {
			Reporter.log("Filter Rule was not Executed and no entry was been created in the DB");
		}
		Assert.assertTrue(testConfig.getTestResult());

		// we have not automated Steps to perform Transaction with other phone
		// number as it is covered in other test cases
	}

	/**
	 * Verify that a phone Number 9000000001 gets blacklisted for global scope
	 * 
	 * @param testConfig
	 */
	@Test(enabled = true, description = " Verify that a phone Number gets blacklisted for global scope", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void blackListedPhoneNoGlobal(Config testConfig) {
		// Navigate till Transaction Page

		RiskHomePage riskHome = new RiskHomePage(testConfig);
		testConfig.putRunTimeProperty("phone", "9000000001");
		TransactionHelper helper = riskHome.getTransactionPage(testConfig);
		// perform the transaction
		String filterId = "132";
		// verify if the filter is active or not
		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 139, 1, 372, helper);
		}

		// verify if same filterId is present or not in DB
		if (riskHome.verifyFilterId(113, filterId)) {
			Reporter.log("Filter Rule was Executed and an entry has been created in the DB");
		}
		Assert.assertTrue(testConfig.getTestResult());
		// we have not automated Steps to perform Transaction with other phone
		// number as it is covered in other test cases
		// Testing soft assert.

		Utils.SoftAssert.getSoftAssert().assertAll();
	}

	/**
	 * Verify that a card number 630493XXXXXX2581 gets blacklisted for merchant
	 * scope
	 * 
	 * @param testConfig
	 */
	@Test(enabled = false, description = " Verify that a card number gets blacklisted for merchant scope", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void blackListedCardNumberOnMerchant(Config testConfig) {

		// Navigate till Transaction Page

		RiskHomePage riskHome = new RiskHomePage(testConfig);
		testConfig.putRunTimeProperty("cardnum", "630493XXXXXX2581");
		TransactionHelper helper = riskHome.getTransactionPage(testConfig);
		// perform the transaction
		String filterId = "344";
		// verify if the filter is active or not

		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 144, 71, 372, helper);
		}

		// verify if same filterId is present or not in DB
		if (riskHome.verifyFilterId(113, filterId)) {
			Reporter.log("Filter Rule was Executed and an entry has been created in the DB");
		}
		// Steps to perform Transaction with other merchant key and blacklisted
		// card number.
		helper.GetTestTransactionPage();
		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 139, 71, 5, helper);
		}

		if (!riskHome.verifyFilterId(113, "")) {
			Reporter.log("Filter Rule was not Executed and no entry was been created in the DB");
		}
		Assert.assertTrue(testConfig.getTestResult());
		// we have not automated Steps to perform Transaction with other card
		// number as it is covered in other test cases
	}

	/**
	 * "Verify that a card number 630493XXXXXX2581 gets blacklisted for category
	 * scope
	 * 
	 * @param testConfig
	 */
	@Test(enabled = false, description = "Verify that a card number gets blacklisted for category scope", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void blackListedCardNumberOnCategory(Config testConfig) {
		// Navigate till Transaction Page

		RiskHomePage riskHome = new RiskHomePage(testConfig);
		testConfig.putRunTimeProperty("cardnum", "677151XXXXXX6531");
		TransactionHelper helper = riskHome.getTransactionPage(testConfig);
		// perform the transaction
		String filterId = "356";
		// verify if the filter is active or not
		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 145, 70, 372, helper);
		}

		// verify if same filterId is present or not in DB
		if (riskHome.verifyFilterId(113, filterId)) {
			Reporter.log("Filter Rule was Executed and an entry has been created in the DB");
		}

		// Steps to perform Transaction with other category merchant key and
		// blacklisted card number.
		helper.GetTestTransactionPage();
		// verify if the filter is active or not
		if (riskHome.isActiveFilter(filterId, 114)) {
			riskHome.performTransaction(testConfig, 138, 70, 5, helper);
		}

		if (!riskHome.verifyFilterId(113, "")) {
			Reporter.log("Filter Rule was not Executed and no entry was been created in the DB");
		}

		Assert.assertTrue(testConfig.getTestResult());
		// we have not automates Steps to perform Transaction with other card
		// number as it is covered in other test cases
	}
}
