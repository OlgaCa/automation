package PageObject.AdminPanel.Filters;

import java.io.File;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.asserts.SoftAssert;

import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class RiskHomePage {

	Config testConfig;

	@FindBy(xpath = "//input[@value='Upload']")
	private WebElement lnkUpload;

	@FindBy(name = "filters")
	private WebElement lnkBrowse;

	@FindBy(linkText = "Upload Filters")
	public WebElement lnkUploadFilters;

	public RiskHomePage(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.wait(this.testConfig, 2);
	}

	/**
	 * Method used to upload filters
	 * @param fileName
	 */
	public void uploadFilters(String fileName) {

		Element.enterFileName(testConfig, lnkBrowse, fileName,"Enter the file location");
		Element.click(testConfig, lnkUpload, "Click on Upload Button");

	}

	/**
	 * Method used to Add and Remove Filters and compares the result in download file
	 * @param rowResult
	 * @param filePath
	 */
	public void AddNRemoveFilters(String rowResult, String path) {
		uploadFilters(path); 
		String resultMessage = readExcelData("filterResult", "row_result", 1);
		Helper.compareEquals(testConfig,"Verifying the result after adding filter", rowResult,resultMessage);

	}

	/**
	 * Method used to check if the filter is active or not
	 * Query used is: SELECT * FROM filter WHERE id={$idvalue};
	 * @param id
	 * @param rowNo
	 */
	public boolean isActiveFilter(String id, int rowNo) {
		testConfig.putRunTimeProperty("idvalue", id);
		SoftAssert soft=new SoftAssert();
		Map map = DataBase.executeSelectQuery(testConfig, rowNo, 1);

		if(map.get("isactive").toString().equals("1"))
		{
			return true;
		}
		
		Helper.compareEquals(testConfig,"Weather Filter is active ot not", "1",map.get("isactive").toString());
		soft.assertTrue(false, "Sending boolean value false at risk page to test soft assert.");
		return false;
	}

	/**
	 * Executes the query which gets the FilterId used in transaction and compares it with the given FilterID
	 * Query used is :SELECT * FROM filterLog WHERE txnid='{$mihpayid}';
	 * @param rowNo
	 * @param filterId
	 */
	public boolean verifyFilterId(int rowNo, String filterId) {
		Map map = DataBase.executeSelectQuery(testConfig, rowNo, 1);
		SoftAssert soft=new SoftAssert();
		if(map==null)
		{
			return false;
		}
		if(filterId.equals(map.get("filterId").toString()))
		{
			return true;
		}
		
		Helper.compareEquals(testConfig,"Filter ID of transaction and FIlterID", filterId,map.get("filterId").toString());
		soft.assertTrue(false, "Sending boolean value false to test soft assert.");
		return false;
	}

	/**
	 * Checks if the Filter exists or not in DB
	 * used Query is: SELECT * FROM filter WHERE parameter_value='{$paramvalue}' AND isactive=1 ;
	 * @param filePath
	 * @param isActive
	 * @param rowNo
	 * @return
	 */
	public boolean isFilterExists(String path, String isActive, int rowNo) {
		TestDataReader treader = new TestDataReader(testConfig, "Sheet1",
				path);
		String paramValue = treader.GetData(1, "parameter_value");
		testConfig.putRunTimeProperty("paramvalue", paramValue);
		Map map = DataBase.executeSelectQuery(testConfig, rowNo, 1);
		if (map == null) {
			return false;
		}
		return true;
	}

	/**
	 * This Function is used to check weather the uploaded file has performed operation successfully or not.
	 * we are getting column value of param_value of the downloaded file and checking if it is active in DB or not.
	 * @param isActive
	 * @param rowNo
	 * @return
	 */
	public boolean verifyFilter(String isActive, int rowNo) {
		
		//getting value of column "param_value" from the downloaded file 
		String paramValue = readExcelData("filter", "param_value", 1);
		testConfig.putRunTimeProperty("paramvalue", paramValue);
		Map map = DataBase.executeSelectQuery(testConfig, rowNo, 1);
		if (isActive.equals(map.get("isactive").toString())) {
			return true;
		}
		return false;
	}

	public String readExcelData(String fileName, String columnName, int rowNo) {
		File file = Browser.lastFileModifiedWithDesiredName(testConfig, "C:\\Users\\"+ System.getProperty("user.name") + "\\Downloads",fileName);
		TestDataReader treader = new TestDataReader(testConfig, "PayU",file.getAbsolutePath());
		return treader.GetData(rowNo, columnName);

	}

	public TransactionHelper getTransactionPage(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		helper.GetTestTransactionPage();
		return helper;

	}

	/**
	 * This method is used to perform transaction and verify the result from
	 * response page
	 * 
	 * @param testConfig
	 * @param transactionRowNo
	 * @param cardDetails
	 * @param payementType
	 */

	public void performTransaction(Config testConfig, int transactionRowNo,
			int cardDetails, int payementType, TransactionHelper helper) {

		// Navigate till Payment Option page
		helper.transactionData = helper.trans
				.FillTransactionDetails(transactionRowNo);
		helper.payment = helper.trans.Submit();

		helper.ccTab = helper.payment.clickCCTab();
		helper.ccTab.FillCardDetails(cardDetails);
		helper.testResponse = helper.payment.clickPayNow();

		helper.testResponse.overrideExpectedTransactionData = true;
		helper.testResponse.VerifyTransactionResponse(new TestDataReader(
				testConfig, "TransactionDetails"), transactionRowNo,
				new TestDataReader(testConfig, "PaymentType"), payementType);

	}
}
