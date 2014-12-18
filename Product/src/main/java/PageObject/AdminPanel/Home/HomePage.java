package PageObject.AdminPanel.Home;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.BankTDR.BankTDRPage;
import PageObject.AdminPanel.DownloadExcel.DownloadExcelPage;
import PageObject.AdminPanel.Filters.RiskHomePage;
import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.MerchantCategories.MerchantCategoryPage;
import PageObject.AdminPanel.MerchantList.CreateMerchantPage;
import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import PageObject.AdminPanel.WebServices.TestwsPage;
import PageObject.AdminPanel.uploadTDR.uploadTDRpage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.Popup;

public class HomePage {

	private Config testConfig;
	
	@FindBy(linkText="Merchant List")
	private WebElement merchant_list;
	
	@FindBy(linkText="Merchant Categories")
	private WebElement merchant_categories;

	@FindBy(linkText="Test Transaction")
	private WebElement testTransaction;

	@FindBy(linkText="Download Excel")
	private WebElement downloadExcelLink;

	public WebElement getDownloadExcelLink() {
		return downloadExcelLink;
	}


	@FindBy(linkText="Test Merchant Transaction")
	private WebElement testMerchantTransaction;
	
	@FindBy(linkText="Test Web Service")
	private WebElement testWebService;

	@FindBy(linkText="Create New Merchant")
	private WebElement lnkCreateMerchant;

	@FindBy(linkText="Risk")
	private WebElement lnkRisk;

	@FindBy(linkText="Upload Merchant TDR")
	private WebElement uploadTDR;

	@FindBy(name="qterm")
	private WebElement merchantSearch;
	
	@FindBy(linkText="Manual Transaction Update")
	private WebElement manualTransactionUpdate;
	
	@FindBy(linkText="Edit Default Gateway")
	private WebElement editDefaultGateway;

	@FindBy(linkText="Bank TDR")
	private WebElement linkBankTDR;
	
	public HomePage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, testTransaction);
	}

	/**
	 * clicks on test transaction link on home page
	 * @return transaction page
	 */
	public TransactionPage ClickTestTransaction()
	{
		Element.click(testConfig, testTransaction, "Test Transaction Link");
		return new TransactionPage(testConfig);
	}
	/**
	 * clicks on test merchant transaction link on home page
	 * @return merchant transaction page
	 */
	public TransactionPage ClickTestMerchantTransaction()
	{
		Element.click(testConfig, testMerchantTransaction, "Test Merchant Transaction Link");
		return new TransactionPage(testConfig);
	}

	/**
	 * clicks on test web service link on home page
	 * @return create web services page
	 */
	public TestwsPage ClickTestWebService()
	{
		//Browser.navigateToURL(testConfig, "http://p1.api.payu.in:8001/");
		Element.click(testConfig, testWebService, "Click Test Web Service Link");
		return new TestwsPage(testConfig);
	}
	
	/**
	 * clicks on manual transaction update link on home page
	 * @return Manual Update Page
	 */
	public ManualUpdatePage ClickManualTransactionUpdate()
	{
		Element.click(testConfig, manualTransactionUpdate, "Click Manual Transaction Update Link");
		return new ManualUpdatePage(testConfig);
	}
	
	/**
	 * clicks on Edit Default Gateway link on home page
	 * @return Edit Default Gateway Page
	 */
	public EditDefaultGatewayPage ClickEditDefaultGateway()
	{
		Element.click(testConfig, editDefaultGateway, "Click Edit Default Gateway Link");
		return new EditDefaultGatewayPage(testConfig);
	}

	/**
	 * clicks on Risk link on home page
	 * @return Edit Default Gateway Page
	 */
	public RiskHomePage ClickonlnkRisk()
	{
		Element.click(testConfig, lnkRisk, "Click on Risk Link");
		return new RiskHomePage(testConfig);
	}
	
	
	/**
	 * clicks on create new merchant link on home page
	 * @return create new merchant page
	 */
	public CreateMerchantPage ClickCreateNewMerchant()
	{
		Element.click(testConfig, lnkCreateMerchant, "Create New merchant Link");
		return new CreateMerchantPage(testConfig);
	}


	/**
	 * clicks on test transaction link on home page
	 * @return transaction page
	 */
	public DownloadExcelPage ClickDownloadExcel()
	{
		Element.click(testConfig, downloadExcelLink, "Test Download Excel Link");
		return new DownloadExcelPage(testConfig);
	}
	

	/**
	 * search for the specified merchant name
	 * @param merchantName merchant to be searched
	 * @return merchant list page, having list of merchants matching this name 
	 */
	public MerchantListPage SearchMerchant(String merchantName)
	{
		Element.enterData(testConfig, merchantSearch, merchantName, "Merchant Search box");
		Element.submit(testConfig, merchantSearch, "Merchant Search box");
		return new MerchantListPage(testConfig);
	}

	public void navigateToAdminHome() 
	{
		Boolean acceptPopup = false;
		String homeUrl = testConfig.getRunTimeProperty("AdminPortalHome");

		String currentUrl = testConfig.driver.getCurrentUrl();
		if(currentUrl.contains("pg.onlinesbi.com")) acceptPopup = true;

		Browser.navigateToURL(testConfig, homeUrl);
		if(acceptPopup)
		{
			//Accept any popup which appeared (this can appear while navigating from some of the bank redirect page)
			Popup.ok(testConfig);
		}
		Browser.waitForPageLoad(testConfig, testTransaction);
	}

	/**	
	 * clicks on 'merchant categories' link on homepage
	 */
	public MerchantCategoryPage clickMerchantCategories(){
		Element.click(testConfig, merchant_categories, "Merchant Categories");
		return new MerchantCategoryPage(testConfig);
		
		}
	
	/**	
	 * clicks on 'merchant list' link on homepage
	 */
	public MerchantListPage clickMerchantList(){
		Element.click(testConfig, merchant_list, "Merchant List");
		return new MerchantListPage(testConfig);
		}

	/**
	 * clicks on upload merchant tdr on home page
	 * @return create new merchant page
	 */
	public uploadTDRpage ClickUploadTDR()
	{
		Element.click(testConfig, uploadTDR, "Upload Merchant TDR Link");
		return new uploadTDRpage(testConfig);
	}
	
	/**
	 * Executes the specified cron.php file
	 * @param cronName
	 */
	public void executeCron(String cronFileName)
	{
		//Save the current URL
		String currentUrl = testConfig.driver.getCurrentUrl();
		
		//GO to cron URL
		String cronUrl = testConfig.getRunTimeProperty("CronUrl");
		Browser.navigateToURL(testConfig, cronUrl);
		Element.click(testConfig, Element.getPageElement(testConfig, How.linkText, cronFileName), "Executing the cron " + cronFileName);
		Browser.wait(testConfig, 5);
		
		//Restore the original URL
		Browser.navigateToURL(testConfig, currentUrl);
	}

	public BankTDRPage ClickBankTDR()
	{
		Element.click(testConfig, linkBankTDR, "Test Bank TDR Link");
		return new BankTDRPage(testConfig);
	}
	
}