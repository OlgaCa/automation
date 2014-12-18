package PageObject.AdminPanel.ManualUpdate;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.MerchantPanel.Billing.TestBilling.AdditionalChargeType;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.FileHandler;
import Utils.Helper;
import Utils.Element.How;
import Utils.Helper.FileType;

public class ReconPage {

	private Config testConfig;

	@FindBy(linkText="Recon")
	private WebElement recon;

	@FindBy(name="reconcile")
	private WebElement browse_recon;

	@FindBy(xpath="(//input[@value='Submit'])[8]")
	private WebElement submit_recon;

	@FindBy(name="reconcile")
	private WebElement reconFile;

	@FindBy(css="input[name='reconcile']~input")
	private WebElement reconFileSubmit;

	@FindBy(name="add_bank_fee")
	private WebElement bankFeeFile;

	@FindBy(css="input[name='add_bank_fee']~input")
	private WebElement bankFeeFileSubmit;

	@FindBy(tagName="body")
	private WebElement ErrorMessage;
	
	ManualUpdatePage manualUpdatePage;


	public ReconPage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, browse_recon);
	}

	public void ReconcileFileBrowseandSubmit(String fileName) {
		Element.enterFileName(testConfig, browse_recon,System.getProperty("user.home")+"\\Downloads\\"+fileName, "Browse_Recon");
		Element.click(testConfig, submit_recon, "Submit_Recon");
	}


	public String uploadRecon(Config testConfig, String fileName){

		Element.enterFileName(testConfig,reconFile,fileName, "Recon File");
		Element.click(testConfig, reconFileSubmit, "Click on submit");
		int retry=60;
		
		//Verifying the pop up appears in UI with reference ID
		for(int i=0;i<=retry;i++){
			Browser.wait(testConfig, 1);
			WebElement referenceID=Element.getPageElement(testConfig, How.id, "PopupMessage");
			if (Element.IsElementDisplayed(testConfig, referenceID)){
				break;
			}
			if (i==retry){
			testConfig.logFail("Reference Id is not appearing");
			return null;
			}
		}
		WebElement ReferenceIdText =Element.getPageElement(testConfig, How.css, "b"); 	
		String referenceIdTextString = ReferenceIdText.getText();
		//referenceIdTextString
		Element.getPageElement(testConfig, How.css, "span.ui-icon.ui-icon-closethick").click();
		testConfig.logComment("Reference Id is "+referenceIdTextString);
		//verify recon table from db
		verifyReconcilationIdFromDBTable(referenceIdTextString,"0");
		//Verifying the recon is completed for the uploaded file
		Browser.navigateToURL(testConfig, testConfig.getRunTimeProperty("CronUrl"));
		WebElement reconCron = Element.getPageElement(testConfig, How.linkText, "recon_daily.php");
		Element.click(testConfig, reconCron, "Recon Cron Link");
		Browser.wait(testConfig, 3);
		
		Map<String,String> map;
		for (int j=0 ;j<=retry;j++)
		{	Browser.wait(testConfig, 15);
		testConfig.putRunTimeProperty("referenceid", referenceIdTextString);
		map = DataBase.executeSelectQuery(testConfig, 95, 1);
		
		//Verify if reference id has been entered in DB
			if (map.isEmpty()){
				if (j==retry){
					testConfig.logFail("There is no entry in Recon_File for refernce id "+referenceIdTextString+" even after 15 minutes");
					return null;
				}
				continue;
			}	
			
			else {
				testConfig.logPass("Record for the reference id "+referenceIdTextString+" exists in Recon_File");
				
				//Verify if recon has been successfulfor the file 
				if(map.get("lockid").isEmpty() && map.get("completed").equals("1"))
					break;
				else
				{	
					Browser.navigateToURL(testConfig, testConfig.getRunTimeProperty("CronUrl"));
					reconCron = Element.getPageElement(testConfig, How.linkText, "recon_daily.php");
					Element.click(testConfig, reconCron, "Recon Cron Link");
					
					if (j==retry){
						testConfig.logFail("The recon of the file is not completed even after 15 minutes");
						return null;
						}
					continue;
				}
			}

		}
		
		//Verifying the recon output is ready to download on UI and download the same
		Browser.navigateToURL(testConfig, testConfig.getRunTimeProperty("AdminPortalHome").replaceAll("home", "manualUpdate"));
		Element.getPageElement(testConfig, How.css,"a[href='#recon']").click();
		Browser.wait(testConfig, 1);
		WebElement OutputFile=Element.getPageElement(testConfig, How.css, "a[href*='"+referenceIdTextString+"']");
		Element.click(testConfig, OutputFile, "Download for output reconfile");
		Browser.wait(testConfig, 30);
		return referenceIdTextString;
	}

	public void uploadBankFeeSettlement(Config testConfig, String fileName){

		Element.enterFileName(testConfig,bankFeeFile,fileName, "Bank Fee Settlement File");
		Element.click(testConfig, bankFeeFileSubmit, "Click on submit");
		Browser.wait(testConfig, 5);

	}

	/**
	 * Verifies values in recon_file for given reference id from db table
	 * @param strReferenceId
	 * @param strCompleted 
	 */
	public void verifyReconcilationIdFromDBTable(String strReferenceId, String strCompleted) {

		Map<String, String> map1 = null;
		map1 = DataBase.executeSelectQuery(testConfig, 115, 1);
		Helper.compareContains(testConfig, "reference id in file name",
				strReferenceId, map1.get("filename"));
		Helper.compareTrue(testConfig, "lock Id is null",map1.get("lockid").isEmpty());
		Helper.compareEquals(testConfig, "completed", strCompleted,  map1.get("completed"));
	}

	/**
	 * Enters file name on browse button and clicks on submit button
	 * @param fileName - full path of file to be uploaded
	 */
	public void ReconcileFileBrowseandSubmitFileWithFullPath(String fileName) {
		Element.enterFileName(testConfig, browse_recon, fileName, "Browse_Recon");
		Element.click(testConfig, submit_recon, "Submit_Recon");
	}

	/**
	 * Checks for error message when incorrect Adminrecon file is uploaded
	 * 
	 * @param reconcilationError
	 */
	public void verifyFileUploadError(String reconcilationError) {
		Helper.compareEquals(testConfig, "Reconcilation Error Message",
				reconcilationError, ErrorMessage.getText().trim());
	}
}
