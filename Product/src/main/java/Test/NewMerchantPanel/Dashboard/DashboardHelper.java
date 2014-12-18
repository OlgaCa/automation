package Test.NewMerchantPanel.Dashboard;

import java.io.File;
import java.util.HashMap;

import org.openqa.selenium.support.PageFactory;

import PageObject.MerchantPanel.Home.PayUHomePage;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import Utils.Browser;
import Utils.Config;
import Utils.TestDataReader;

public class DashboardHelper {

	private Config testConfig;
	public PayUHomePage payuHome;



	public TestDataReader testData;

	public DashboardHelper(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
	}

	/**
	 * method used to login into merchant panel by reading transactions rowno
	 * @param transactionRowNum
	 * @return
	 */
	public MerchantPanelPage doMerchantLogin(int transactionRowNum) {

		try {
			payuHome = new PayUHomePage(testConfig);
			testData = payuHome.fillMerchantLogin(transactionRowNum);
			payuHome.clickMerchantLogin();

		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}

		return new MerchantPanelPage(testConfig);

	}

	/**
	 * Method used to log in with given user name,password and alisa name
	 * @param user
	 * @param password
	 * @param alias
	 * @return
	 */
	public MerchantPanelPage doMerchantLogin(String user, String password,
			String alias) {

		try {
			payuHome = new PayUHomePage(testConfig);
			payuHome.fillMerchantLogin(user, password, alias);
			payuHome.clickMerchantLogin();

		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}

		return new MerchantPanelPage(testConfig);

	}

	
	/**
	 * Method used to get the latest modified file and read the content form it
	 * @param fileName
	 * @param columnName
	 * @param rowNo
	 * @return
	 */
	public String readExcelData(String fileName, String columnName, int rowNo) {
		File file = Browser.lastFileModifiedWithDesiredName(testConfig, "C:\\Users\\"+ System.getProperty("user.name") + "\\Downloads",fileName);
		TestDataReader treader = new TestDataReader(testConfig, "PayU",file.getAbsolutePath());
		return treader.GetData(rowNo, columnName);

	}


	/**Login merchant panel using provided details
	 * @param userDetails
	 * @return Dashboard Page
	 */
	public MerchantPanelPage doMerchantLogin(HashMap<String, String> userDetails) {
		
		payuHome.fillMerchantLogin(userDetails.get("Username"),userDetails.get("Password"),userDetails.get("Alias"));
		payuHome.clickMerchantLogin();
		return new MerchantPanelPage(testConfig);
	}
	
	/**Logins Merchant panel for super user
	 * @param UserLoginDetailsRow Row number from sheet UserLoginDetails
	 * @return Dashboard Page
	 */
	public MerchantPanelPage doSuperUserLogin(int UserLoginDetailsRow) {
		payuHome = new PayUHomePage(testConfig);
		TestDataReader userLoginDetails = new TestDataReader(testConfig, "UserLoginDetails");
		HashMap<String, String> LoginDetails= new HashMap<String, String>();
		
		LoginDetails.put("Username",userLoginDetails.GetData(UserLoginDetailsRow, "username"));
		LoginDetails.put("Password",userLoginDetails.GetData(UserLoginDetailsRow, "password"));
		LoginDetails.put("Alias",userLoginDetails.GetData(UserLoginDetailsRow, "name"));
		
		return doMerchantLogin(LoginDetails);
	}

	/**When on Payu login page verifies merchant login failure
	 * Clicks on sign in link
	 * Enters Login credentials and click on login
	 * Verifies error message displayed on sign in div
	 * @param userDetails Userdetails containing login credentials
	 * @param FailureMessage - Message displayed on clicking login
	 * @return
	 */
	public PayUHomePage doMerchantLoginAndVerifyFailure(
			HashMap<String, String> userDetails, String FailureMessage) {
		payuHome.fillMerchantLogin(userDetails.get("Username"),userDetails.get("Password"),userDetails.get("Alias"));
		payuHome.clickMerchantLogin();
		payuHome.VerifyFailureMessage(FailureMessage);
		return payuHome;
	}
	
	public void navigateToPayuWebsiteAndVerifyImages(Config testConfig)
	{
		try
		{
			payuHome = new PayUHomePage(testConfig);
			Browser.wait(testConfig, 2);
			payuHome.verifyElementsPresent(testConfig);		
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}

	}

	

}