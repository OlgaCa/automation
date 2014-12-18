package PageObject.AdminPanel.MerchantList;

import java.util.HashMap;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.MerchantList.MerchantDetails.FlaggedTxnPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsMerchantParamsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.NewMerchantPanel.Overview.MerchantPanelPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class MerchantListPage {

	private Config testConfig;
	
	@FindBy(xpath="//tr[2]/td[3]/a")
	private WebElement merchantKey;
	
	@FindBy(xpath="//a[contains(text(),'Per Transaction Risk Rules')]")
	private WebElement rule;

	@FindBy(xpath="//a[contains(text(),'Flagged Transactions')]")
	private WebElement flagtxn;
	
	@FindBy(css="form>input[type='text']:not([id])")
	private WebElement searchField;

	@FindBy(css="select[name='qtype']")
	private WebElement type;

	@FindBy(xpath="//table//td[3]")
	private WebElement merchantName;

	@FindBy(xpath="//table//td[4]")
	private WebElement merchantEmail;

	@FindBy(xpath="//table//td[5]")
	private WebElement companyName;
	
	@FindBy(xpath="//table//td[6]")
	private WebElement companyWebsite;
	
	@FindBy(name="ml_approve")
	private WebElement mhAprv;

	@FindBy(name="ml_active")
	private WebElement mhActive;

	@FindBy(linkText="Edit")
	private WebElement edit;

	@FindBy(linkText="Merchant Gateway")
	private WebElement mrchGateway;
	
	@FindBy(css="tr:nth-child(2)>td:nth-child(1)>a")
	private WebElement merchantId;
	
	@FindBy(css="tr:nth-child(2)>td:nth-child(9)>a:nth-child(5)")
	private WebElement viewDashboard;

	@FindBy(css="select[name='qtype']")
	private WebElement searchOptions;
	
	public MerchantListPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, searchField);
	}

	/**
	 * click on approve button of the first merchant in list
	 */
	public void ApproveFirstMerchant()
	{
		Element.click(testConfig, mhAprv, "Approve");
	}

	/**
	 * click on activate button of the first merchant in list
	 */
	public void ActivateFirstMerchant()
	{
		Element.click(testConfig, mhActive, "Activate");
		testConfig.driver.switchTo().alert().accept();
	}

	/**
	 * click on merchant gateway link of merchant on merchant gateway page
	 * @return returns the merchant gateway page
	 */
	public MerchantGatewayPage clickFirstMerchantGatewayLink()
	{
		Element.click(testConfig, mrchGateway, "Merchant Gateway");
		return new MerchantGatewayPage(testConfig);
	}

	/**
	 * click on edit merchant link of merchant on merchant gateway page
	 * @return returns the edit merchant page
	 */
	public EditMerchantPage clickFirstMerchantEditLink()
	{
		Element.click(testConfig, edit, "Edit Merchant Details");
		return new EditMerchantPage(testConfig);
	}

	/**
	 * Reads the "TestMerchantData" sheet of Test Data excel and verify creation of right merchant from data sheet
	 * @param rowNum Row number of excel sheet to be compared
	 * @return Returns if the values match or not
	 */
	public void VerifyMerchantDetails(TestDataReader data, int testMerchantDataRow)
	{	
		String mName = data.GetData(testMerchantDataRow, "MerchantName").trim();
		String mEmail =  data.GetData(testMerchantDataRow, "MerchantEmail").trim();

		SearchMerchant(mName);
		Helper.compareEquals(testConfig, "Merchant Name", mName, merchantName.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant Email", mEmail, merchantEmail.getAttribute("value"));

		EditMerchantPage editPage = clickFirstMerchantEditLink();
		editPage.VerifyMerchantEditDetails(data, testMerchantDataRow);   	
	}

	/**
	 * search for the specified merchant name
	 * @param merchantName
	 */
	public void SearchMerchant(String merchantName)
	{
		Element.enterData(testConfig, searchField, merchantName, "Merchant Name");
		Element.submit(testConfig, searchField, "Search Merchant");
	}
	
	/**
	 * search for the specified merchant key
	 * @param merchantKey
	 */
	public void SearchMerchantKey(String merchantKey)
	{
		Element.selectVisibleText(testConfig, type, "Merchant Key", "Merchant key");
		Element.enterData(testConfig, searchField, merchantKey, "Merchant key");
		Element.submit(testConfig, searchField, "Search Merchant");
	}
	
	/**
	 * Click on first merchantKey on merchantLiat page.
	 * @return merchantDetails page
	 */
	
	public MerchantDetailsPage clickFirstMerchantKey()
	{
		Element.click(testConfig,merchantKey , "Merchant Key");
		return new MerchantDetailsPage(testConfig);
	}

	
	/**
	 * click on "Per transaction Rule" mentioned against merchant
	 */	
	public RulePage clickMerchantTxnRule(){
		Element.click(testConfig, rule, "Risk Rule Link");
		return new RulePage(testConfig);
	}

	public FlaggedTxnPage clickFlaggedTxn(){
		Element.click(testConfig, flagtxn, "Flagged transaction link");
		return new FlaggedTxnPage(testConfig);
	}
	
	public String getMerchantId(){
		return merchantId.getText().trim();
	}
	
	/**
	 * Click on first View Dashboard on merchantList page.
	 * @return merchantDetails page
	 */
	
	public DashboardPage clickFirstViewDashboard()
	{
		Element.click(testConfig,viewDashboard , "View Dashboard");
		Browser.switchToNewWindow(testConfig);
		return new DashboardPage(testConfig);
	}

	/**Gets Merchant Panel when on merchant list page
	 * Gets Merchant Key from TestData
	 * Searches By Entering Key
	 * Clicks on View Dashboard Link
	 * @param TransactionDetailsRowNumber - Row number from TransactionDetails sheet
	 * @return new Merchant Panel sheet
	 */
	public MerchantPanelPage getMerchantPanel(int TransactionDetailsRowNumber) {
		//Get Merchant Key
		String MerchantKey = new TestDataReader(testConfig,
				"TransactionDetails").GetData(TransactionDetailsRowNumber,
				"key");
		//Choose Merchant Key As Option
		performSearch(searchParameter.MerchantKey,MerchantKey);
		
		//Choose first View Dashboard
		Element.click(testConfig,viewDashboard , "View Dashboard");
		Browser.switchToNewWindow(testConfig);
		return new MerchantPanelPage(testConfig);
	}
	
	
	public enum searchParameter{MerchantName,MerchantKey,MerchantEmail,CompanyName};
	
	/**Performs Search Action
	 * Selects Search option
	 * Enters search value
	 * Submits Query
	 * @param parameter - Merchant key,Merchant Name,Merchant Email,Company Name
	 * @param SearchValue - Search String to be entered
	 * @return - Merchant List Page
	 */
	public MerchantListPage performSearch(searchParameter parameter,
			String SearchValue) {
		String optionValue ="";
		
		switch (parameter) {
		case MerchantKey:
			optionValue = "2";
			break;
		case MerchantName:
			optionValue = "0";
			break;
		case MerchantEmail:
			optionValue = "1";
			break;
		case CompanyName:
			optionValue = "3";
			break;
		}
		
		Element.selectValue(testConfig, searchOptions, optionValue, "Merchant List Search Options");
		Element.enterData(testConfig, searchField, SearchValue, parameter.toString());
		Element.submit(testConfig, searchField, "Search Merchant");
		return new MerchantListPage(testConfig);
	}
	
	/**Gets Merchant Panel when on merchant list page
	 * Gets Merchant Key from TestData
	 * Searches By Entering Key
	 * Clicks on View Dashboard Link
	 * @param TransactionDetailsRowNumber - Row number from TransactionDetails sheet
	 * @return new Merchant Panel sheet
	 */
	public ParamsMerchantParamsPage getMerchantParamPage(int TransactionDetailsRowNumber) {
		//Get Merchant Key
		String MerchantKey = new TestDataReader(testConfig,
				"TransactionDetails").GetData(TransactionDetailsRowNumber,
				"key");
		//Choose Merchant Key As Option
		performSearch(searchParameter.MerchantKey,MerchantKey);
		
		//Choose first View Dashboard
		MerchantDetailsPage detailsPage = clickFirstMerchantKey();
		ParamsPage paramsPage = detailsPage.ClickParams();
		return paramsPage.clickMerchantParams();
	}

	public void VerifyMerchantDetails(HashMap<String, String> merchantDetails) {

		Helper.compareEquals(testConfig, "Merchant Name",
				merchantDetails.get("MerchantName"),
				merchantName.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant Email",
				merchantDetails.get("MerchantEmail"),
				merchantEmail.getAttribute("value"));
		Helper.compareEquals(testConfig, "Company Name",
				merchantDetails.get("CompanyName"),
				companyName.getAttribute("value"));
		Helper.compareEquals(testConfig, "Company Website",
				merchantDetails.get("CompanyWebsite"),
				companyWebsite.getAttribute("value"));
	}
}
