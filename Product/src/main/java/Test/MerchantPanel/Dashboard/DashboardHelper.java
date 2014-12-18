package Test.MerchantPanel.Dashboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.PayUHomePage;
import PageObject.MerchantPanel.Payments.EmailInvoicePage;
import PageObject.MerchantPanel.Settings.EditMerchantLoginPage;
import PageObject.MerchantPanel.Settings.MerchantCheckoutPage;
import PageObject.MerchantPanel.Settings.MerchantLoginsPage;
import PageObject.MerchantPanel.Settings.NewMerchantLoginPage;
import PageObject.MerchantPanel.Settings.SetLookPage;
import PageObject.MerchantPanel.Transactions.CancelPage;
import PageObject.MerchantPanel.Transactions.CancelRefundPage;
import PageObject.MerchantPanel.Transactions.CapturePage;
import PageObject.MerchantPanel.Transactions.InvoiceTransactionConfirmationPage;
import PageObject.MerchantPanel.Transactions.RefundPage;
import PageObject.MerchantPanel.Transactions.RequestsPage;
import PageObject.MerchantPanel.Transactions.TransactionDetailPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestDataReader;


public class DashboardHelper {

	private Config testConfig;

	public PayUHomePage payuHome;
	public TestDataReader testData;
	public TransactionDetailPage transactionsPage;
	public DashboardPage dbPage;
	public RefundPage refundPage;
	public CancelRefundPage cancelrefundPage;
	public CancelPage cancelPage;
	public CapturePage capturePage;
	public RequestsPage requestPage;
	public EmailInvoicePage emailInvoicePage;
	public EditMerchantLoginPage editmerchantLoginPage;
	public MerchantCheckoutPage merchantcheckoutPage;

	public enum ExpectedLandingPage {
		DashboardPage, PayUHomePage
	};

	public InvoiceTransactionConfirmationPage invoiceTransactionConfirmationPage;

	//@FindBy(id="htmlSource")
	private WebElement HtmlSource;

	@FindBy(css="h1")
	private WebElement popUpText;

	@FindBy(css="b#admin_created")
	private WebElement createdMsg;

	@FindBy(css="a.settings")
	private WebElement settingTab;

	@FindBy(partialLinkText="Manage Logins")
	private WebElement manageLogin;

	@FindBy(linkText="Advanced Customization ")
	private WebElement AdvCustomization ;

	public enum timePeriod {toDay, yesterDay, lastWeek, last4Weeks, thisYear};

	public DashboardHelper(Config testConfig)
	{
		this.testConfig = testConfig;
	}


	/**
	 * Logs in to the Merchant Dashboard site
	 * @return 
	 */

	public DashboardPage doMerchantLogin(int transactionRowNum)
	{
		return (DashboardPage) doMerchantLogin(transactionRowNum, ExpectedLandingPage.DashboardPage);
	}

	public Object doMerchantLogin(int transactionRowNum, ExpectedLandingPage ePage)
	{
		Object returnPage = null;
		try
		{
			payuHome = new PayUHomePage(testConfig);
			testData = payuHome.fillMerchantLogin(transactionRowNum);
			payuHome.clickMerchantLogin();
			switch (ePage) {
			case DashboardPage:
				returnPage = new DashboardPage(testConfig);
				break;
			case PayUHomePage:
				returnPage = new PayUHomePage(testConfig);
				break;
			}
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}

		return returnPage;
	}



	/**
	 * Navigates the current browser window to the transaction page, for doing a new transaction
	 */
	public Object GetTimePeriod(int timePeriodRow)
	{
		return GetTimePeriod(timePeriodRow);
	}

	/**
	 * @param phpResponse PHP Response Array returned by some transaction or webservice
	 * @return Key-Value pairs of the response array
	 */
	public static Hashtable<String, String> convertTransactionDetailToList(String phpResponse)
	{
		Hashtable<String, String> detail = null;

		if(phpResponse != null && !phpResponse.isEmpty())
		{
			detail = new Hashtable<String, String>();
			String [] responseArray = phpResponse.split("\n");

			for (int i =0;i<responseArray.length; i+=2)
			{
				detail.put(responseArray[i].substring(0,responseArray[i].lastIndexOf(":")), responseArray[i+1]);
			}
		}
		return detail;
	}

	/**
	 * @param phpResponse PHP Response Array returned by some some transaction
	 * @return Key-Value pairs of the actions done array
	 */
	public static Hashtable<String, String> convertActionsDetailToList(String phpResponse, String phString)
	{
		Hashtable<String, String> detail = null;

		if(phpResponse != null && !phpResponse.isEmpty())
		{

			detail = new Hashtable<String, String>();
			String [] responseHeaderArray = phpResponse.split(" ");
			String [] responseDetailArray = phString.split(" ");

			for (int i =0;i<responseHeaderArray.length; i++)
			{

				switch(i)
				{
				case 0:
					detail.put(responseHeaderArray[i].concat(responseHeaderArray[i+1]), responseDetailArray[i]);
					break;
				case 2:
					detail.put(responseHeaderArray[i].concat(responseHeaderArray[i+1]), responseDetailArray[i-1]);
					break;
				case 4:
					detail.put((responseHeaderArray[i].concat(responseHeaderArray[i+1])).concat(responseHeaderArray[i+2]), responseDetailArray[i-2]);
					break;
				case 7:
					detail.put(responseHeaderArray[i], responseDetailArray[i-4]);
					break;
				case 8:
					detail.put(responseHeaderArray[i], responseDetailArray[i-4]);
					break;
				case 9:
					detail.put(responseHeaderArray[i], responseDetailArray[i-4]);
					break;
				case 10:
					detail.put(responseHeaderArray[i], (responseDetailArray[i-4].concat(":")).concat(responseDetailArray[i-3]));
					break;
				}

			}
		}

		return detail;
	}

	/**
	 * @param phpResponse PHP Response Array returned by some transaction
	 * @return Key-Value pairs of the risk found array
	 */
	public static Hashtable<String, String> convertRiskDetail(String phpResponse)
	{
		Hashtable<String, String> detail = null;

		if(phpResponse != null && !phpResponse.isEmpty())
		{
			detail = new Hashtable<String, String>();
			String [] responseArray = phpResponse.split("\n");

			detail.put(responseArray[0].substring(0,responseArray[0].lastIndexOf(":")), responseArray[1]);
		}
		return detail;
	}

	/**
	 * Logs in as a merchant and fills email invoice form and submits
	 * @return InvoiceTransactionConfirmationPage
	 * @param transactionRowNum
	 */
	public void loginAndFillEmailInvoice(int transactionRowNum,String testType) {
		doMerchantLogin(transactionRowNum);
		DashboardPage dashBoard = new DashboardPage(testConfig);
	//	dashBoard.ClickClose();

		if(testType!=null)
			if(testType.contentEquals("invoice"))
				emailInvoicePage = dashBoard.ClickThroughEmailInvoice();
		if (transactionRowNum == 36)
		{
			Helper.compareEquals(testConfig, "verifies USD is present for Dollar gateway", "USD", emailInvoicePage.getUSDText());
		}

		else if(testType.contentEquals("ivr"))
			emailInvoicePage = dashBoard.clickThroughIVR();

		// fill email invoice form
		emailInvoicePage.fillInvoiceForm(transactionRowNum);

		// click confirm
		emailInvoicePage.clickConfirmButton();
		Browser.wait(testConfig, 2);
		// if pop up appears
		if(Element.IsElementDisplayed(testConfig,Element.getPageElement(testConfig, How.css, "h1")))
		{
			invoiceTransactionConfirmationPage = new InvoiceTransactionConfirmationPage(testConfig);
		}
		else
		{
			testConfig.logComment("Confirmation popup of send email and copy hyperlink not appeared");
		}

	}
	/**
	 * click create login and fill details & submit
	 * @param loginRowNum
	 */
	public DashboardPage clickLoginAndFillForm(int loginRowNum){
		//	DashboardPage dashBoard = new DashboardPage(testConfig);
		dbPage.ClickCreateLogin();
		filluserDetail(loginRowNum);
		return dbPage;

	}
	/**
	 *click manage login and  then click the edit link
	 */
	public DashboardPage clickManageLoginAndEdit(){
		dbPage.ClickManageLogin();
		editManageLogin();
		return dbPage;

	}

	/**
	 * click checkout page and add Html
	 * @param loginRowNum
	 * @return 
	 */
	public MerchantCheckoutPage clickCheckoutPageAndAddHtml(){
		merchantcheckoutPage = dbPage.ClickCheckoutPage();
		//EditHtmlCode();
		//Browser.wait(testConfig, 30);
		//String text = Element.getPageElement(testConfig, How.id, "tinymce").getText();
		//System.out.println(text);
		merchantcheckoutPage.EditHtmlSource();
		testConfig.driver.switchTo().frame(1);
		//driver.switchTo().frame("topframe"); 
		WebElement UpdateHtml = Element.getPageElement(testConfig, How.id, "insert");
		HtmlSource = Element.getPageElement(testConfig, How.id, "htmlSource");

		// if pop up appears
		if(Element.IsElementDisplayed(testConfig,HtmlSource))
		{
			HtmlSource.clear();
			File f = new File(testConfig.getRunTimeProperty("checkoutPage"));
			String text = readDoc(f);
			Element.enterData(testConfig, HtmlSource, text, "enter html into frame");
			Browser.wait(testConfig, 2);
			Element.click(testConfig, UpdateHtml, "Click on Update");
		}

		Browser.wait(testConfig, 2);
		merchantcheckoutPage.Submit();
		Browser.wait(testConfig, 2);
		return merchantcheckoutPage;

	}

	public MerchantCheckoutPage VerifyHtmlUpdate() {
		WebElement contents = Element.getPageElement(testConfig, How.id, "contents");

		Helper.compareEquals(testConfig, "Html content", "adding soem text", contents.getText());
		return merchantcheckoutPage;

	}

	public void verifyEdit(String text){
		Browser.wait(testConfig, 2);
		if(merchantcheckoutPage.getHtmlText() != null){
			Helper.compareEquals(testConfig, "comparing edited html values", text, merchantcheckoutPage.getHtmlText());
		}

	}



	/**
	 * click checkout page and delete Html
	 * @param loginRowNum
	 * @return 
	 */
	public MerchantCheckoutPage clickCheckoutPageAndDeleteHtml(){
		merchantcheckoutPage = dbPage.ClickCheckoutPage();
		merchantcheckoutPage.EditHtmlSource();		
		testConfig.driver.switchTo().frame(1);
		//driver.switchTo().frame("topframe"); 
		WebElement UpdateHtml = Element.getPageElement(testConfig, How.id, "insert");
		HtmlSource = Element.getPageElement(testConfig, How.id, "htmlSource");

		// if pop up appears
		if(Element.IsElementDisplayed(testConfig,HtmlSource))
		{
			HtmlSource.clear();
			Browser.wait(testConfig, 2);
			Element.click(testConfig, UpdateHtml, "Click on Update");


		}


		Browser.wait(testConfig, 2);
		merchantcheckoutPage.Submit();
		Browser.wait(testConfig, 2);
		return merchantcheckoutPage;

	}
	/**
	 * click checkout page and delete Html
	 * @param loginRowNum
	 * @return 
	 */
	public MerchantCheckoutPage SubmitWithoutUpdateHtml(){
		merchantcheckoutPage = dbPage.ClickCheckoutPage();
		merchantcheckoutPage.EditHtmlSource();
		testConfig.driver.switchTo().frame(1);
		WebElement UpdateHtml = Element.getPageElement(testConfig, How.id, "insert");
		HtmlSource = Element.getPageElement(testConfig, How.id, "htmlSource");

		// if pop up appears
		if(Element.IsElementDisplayed(testConfig,HtmlSource))
		{
			Element.enterData(testConfig, HtmlSource, "adding some text", "Html Source");
			Browser.wait(testConfig, 2);


		}


		testConfig.driver.switchTo().defaultContent();
		merchantcheckoutPage.Submit();

		return merchantcheckoutPage;

	}
	/**
	 * click checkout page and delete Html
	 * @param loginRowNum
	 * @return 
	 */
	public MerchantCheckoutPage SubmitHtmlAfetrCancel(){
		merchantcheckoutPage = dbPage.ClickCheckoutPage();
		merchantcheckoutPage.EditHtmlSource();		
		testConfig.driver.switchTo().frame(1);
		//driver.switchTo().frame("topframe"); 
		WebElement cancelHtml = Element.getPageElement(testConfig, How.id, "cancel");
		HtmlSource = Element.getPageElement(testConfig, How.id, "htmlSource");

		// if pop up appears
		if(Element.IsElementDisplayed(testConfig,HtmlSource))
		{
			Element.enterData(testConfig, HtmlSource, "adding some text", "Html Source");
			Browser.wait(testConfig, 2);
			Element.click(testConfig,cancelHtml, "Click on cancel");


		}


		Browser.wait(testConfig, 2);
		merchantcheckoutPage.Submit();
		Browser.wait(testConfig, 2);
		return merchantcheckoutPage;

	}
	/**
	 * rEADING A TEXT FILE
	 * @param File pointer
	 * @return output in string
	 */
	public String readDoc(File f) {
		String text = "";
		int read, N = 1024 * 1024;
		char[] buffer = new char[N];

		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			while(true) {
				read = br.read(buffer, 0, N);
				text += new String(buffer, 0, read);

				if(read < N) {
					break;
				}
			}
		} catch(Exception ex) {
			testConfig.logException(ex);
		}

		return text;
	}

	public DashboardPage loginAndCheckAssignedRoles(int loginRowNum){
		getMerchantCredentials(loginRowNum);
		doMerchantLogin(1);	
		dbPage.ClickViewAll();
		dbPage.ClickRequestRefund();
		dbPage.ClickCancel();
		return dbPage;
	}

	public TestDataReader  getMerchantCredentials(int merchantRow){

		TestDataReader data = new TestDataReader(testConfig,"UserLoginDetails");
		String user = data.GetData(merchantRow, "username");
		String pwd = data.GetData(merchantRow, "password");
		testConfig.putRunTimeProperty("MerchantUser",user);
		testConfig.putRunTimeProperty("MerchantPassword",pwd);
		return data;
	}
	/**
	 * @param transactionRowNum - 
	 */
	public void filluserDetail(int transactionRowNum) {
		//doMerchantLogin(transactionRowNum);
		//DashboardPage dashBoard = new DashboardPage(testConfig);
		//dashBoard.ClickClose();
		//Browser.wait(testConfig, 2);
		NewMerchantLoginPage newMerchantLoginPage = new NewMerchantLoginPage(testConfig);
		// fill email invoice form
		newMerchantLoginPage.fillCreateLoginForm(transactionRowNum);

		// click confirm
		newMerchantLoginPage.clickSaveButton();
		Browser.wait(testConfig, 2);
		// if sucess
		/*if(Element.IsElementDisplayed(testConfig,Element.getPageElement(testConfig, How.css, "b#admin_created")))
		{
			//MerchantLoginsPage(testConfig);
		}*/

	}

	/**
	 * Click on edit manage login
	 */
	public void editManageLogin() {
		Browser.wait(testConfig, 2);
		MerchantLoginsPage merchantLoginsPage = new MerchantLoginsPage(testConfig);
		merchantLoginsPage.ClickEditLink();

	}

	public void activeORDeactivateLogin() {
		dbPage = clickManageLoginAndEdit();
		Browser.wait(testConfig, 2);
		EditMerchantLoginPage editmerchantLoginPage = new EditMerchantLoginPage(testConfig);
		editmerchantLoginPage.MarkActiveCheckbox();
		editmerchantLoginPage.clickSaveButton();

	}

	public void assignRoelsToLogin() {
		dbPage = clickManageLoginAndEdit();
		Browser.wait(testConfig, 2);
		EditMerchantLoginPage editmerchantLoginPage = new EditMerchantLoginPage(testConfig);
		editmerchantLoginPage.AssignRoles();
		editmerchantLoginPage.clickSaveButton();

	}

	public void deleteMerchantLogin() {
		dbPage = clickManageLoginAndEdit();
		Browser.wait(testConfig, 2);
		EditMerchantLoginPage editmerchantLoginPage = new EditMerchantLoginPage(testConfig);
		editmerchantLoginPage.clickDeleteButton();

	}

	public DashboardPage CheckExportExcelRole(int loginRowNum){
		String name=testConfig.getRunTimeProperty("usrName");
		String pwd=testConfig.getRunTimeProperty("usrPassword");
		TestDataReader data = new TestDataReader(testConfig,"TransactionDetails");
		String value = data.GetData(loginRowNum, "key");

		//Login with sub user credentials 
		payuHome.fillMerchantLogin(name, pwd, value);
		payuHome.clickMerchantLogin();
		dbPage.ClickClose();
		dbPage.ClickViewAll();
		String exportExcel=dbPage.VerifyExportExcelButton();
		Helper.compareContains(testConfig,"Presence of Export to excel button", "Export to Excel", exportExcel);
		dbPage.ClickRequestRefund();
		String exportExcel1=dbPage.VerifyExportExcelButton();
		Helper.compareContains(testConfig,"Presence of Export to excel button", "Export to Excel", exportExcel1);
		dbPage.ClickCancel();
		String exportExcel2=dbPage.VerifyExportExcelButton();
		Helper.compareContains(testConfig,"Presence of Export to excel button", "Export to Excel", exportExcel2);
		dbPage.ClickCODSettel();
		String exportExcel3=dbPage.VerifyExportExcelButton();
		Helper.compareContains(testConfig,"Presence of Export to excel button", "Export to Excel", exportExcel3);
		dbPage.ClickCODVerify();
		String exportExcel4=dbPage.VerifyExportExcelButton();
		Helper.compareContains(testConfig,"Presence of Export to excel button", "Export to Excel", exportExcel4);
		//Verify export to excel functionality
		dbPage.ClickExportExcelButton();
		Browser.wait(testConfig,30);
		String exportexcelfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,exportexcelfilepath);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", exportexcelfilepath+fileName);
		Helper.compareEquals(testConfig, "result count of transaction in excel", dbPage.transactionno(),String.valueOf(tr.getRecordsNum()-1));

		return dbPage;

	}

	public DashboardPage CheckExportExcelNotPresent(int loginRowNum){
		String name=testConfig.getRunTimeProperty("usrName");
		String pwd=testConfig.getRunTimeProperty("usrPassword");
		String value="QBKSbB";
		payuHome.fillMerchantLogin(name, pwd, value);
		payuHome.clickMerchantLogin();
		dbPage.ClickClose();
		dbPage.ClickViewAll();
		Boolean pass=dbPage.VerifyExportExcelNot();
		Helper.compareTrue(testConfig, "export to excel not present", pass);
		dbPage.ClickRequestRefund();
		Boolean pass1=dbPage.VerifyExportExcelNot();
		Helper.compareTrue(testConfig, "export to excel not present", pass1);
		dbPage.ClickCancel();
		Boolean pass2=dbPage.VerifyExportExcelNot();
		Helper.compareTrue(testConfig, "export to excel not present", pass2);
		dbPage.ClickCODSettel();
		Boolean pass3=dbPage.VerifyExportExcelNot();
		Helper.compareTrue(testConfig, "export to excel not present", pass3);
		dbPage.ClickCODVerify();
		Boolean pass4=dbPage.VerifyExportExcelNot();
		Helper.compareTrue(testConfig, "export to excel not present", pass4);
		return dbPage;
	}

	public void assignRolesToLogin() {
		dbPage = clickManageLoginAndEdit();
		EditMerchantLoginPage editmerchantLoginPage = new EditMerchantLoginPage(testConfig);
		editmerchantLoginPage.AssignRoles();
		editmerchantLoginPage.clickSaveButton();

	}
	public void revokeRolesToLogin() {
		dbPage = clickManageLoginAndEdit();
		EditMerchantLoginPage editmerchantLoginPage = new EditMerchantLoginPage(testConfig);
		editmerchantLoginPage.RevokeRole();
		editmerchantLoginPage.clickSaveButton();

	}

	/**
	 * Manage login 
	 * @return
	 */
	public MerchantLoginsPage ClickManageLogin() {
		Element.mouseMove(testConfig, settingTab, "Select setting Tab");
		Element.click(testConfig, manageLogin, "Click on Manage Login");
		Browser.wait(testConfig, 2);
		return new MerchantLoginsPage(testConfig);
	}


	public SetLookPage clickAdvCustomizeLink(){
		Element.mouseMove(testConfig, settingTab, "Select setting Tab");
		Element.click(testConfig, AdvCustomization, "Click advance Customization Link");
		return new SetLookPage(testConfig);
	}

	public void compareAmount2Sides(Config testConfig,DashboardPage dashBoard,String what){
		Helper.compareEquals(testConfig,what,"$ "+dashBoard.getAmount().substring(0, dashBoard.getAmount().length()-2)+" /-", dashBoard.getRightSideAmount());

	}

	/**
	 * Logs in as a merchant and fills email invoice form and submits
	 * @return InvoiceTransactionConfirmationPage
	 * @param transactionRowNum
	 */
	public void loginAndNavigateToMassInvoice(int transactionRowNum) {
		doMerchantLogin(transactionRowNum);
		DashboardPage dashBoard = new DashboardPage(testConfig);
		dashBoard.ClickClose();
		emailInvoicePage = dashBoard.ClickThroughEmailInvoice();
	}

	public void UploadMassInvoiceSheet(Config testConfig, String fileName)
	{
		emailInvoicePage.uploadMassInvoiceExcel(testConfig, fileName);
	}

	public String verifyErrorMessageMassInvoice()
	{
		return Element.getPageElement(testConfig, How.tagName, "body").getText();

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
