package PageObject.AdminPanel.ManualUpdate;

import java.io.File;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.Settlement.InvalidFilePage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.Element.How;
import Utils.TestDataReader;

public class MerchantSettlement {
	
	private Config testConfig;

	@FindBy(css="h2+div")
	private WebElement settlementIdRemoved;
	
	@FindBy(name="esscomvalidation")
	private WebElement esscomFile;
	
	@FindBy(css="input[name='esscomvalidation']~input")
	private WebElement esscomFileSubmit;
	
	@FindBy(name="citibanknodal")
	private WebElement citiBankNodalFile;
	
	@FindBy(css="input[name='citibank_nodal_id']~ input[type='submit']")
	private WebElement citiBankNodalFileSubmit;

	@FindBy(name="citibank_nodal_id_summary")
	private WebElement citiBankNodalSummaryFile;
	
	@FindBy(css="input[name='citibank_nodal_id_summary']~input[value='Download']")
	private WebElement citiBankNodalSummaryFileSubmit;
	
	@FindBy(name="citibanknodaltid")
	private WebElement citiBankNodalTIDgeneration;
	
	@FindBy(css="input[name='citibanknodaltid']~input")
	private WebElement citiBankNodalTIDGenerationSubmit;
	
	@FindBy(name="tid_utr_summary")
	private WebElement tid_utr_file;
	
	@FindBy(css="input[name='tid_utr_summary']~input")
	private WebElement tid_utr_fileSubmit;
	
	@FindBy(name="clear_settlement_id")
	private WebElement clear_settlementId;
	
	@FindBy(css="input[name='clear_settlement_id']~input")
	private WebElement clear_settlementIdSubmit;
	
	@FindBy(css="h3")
	private WebElement misc_submit1;
	
	@FindBy (css="input[name*='utr_level_citibank_nodal_id_summary']")
	private WebElement GetUTRSummaryInput;
	
	@FindBy (css="input[name*='utr_level_citibank_nodal_id_summary']~input[value*='Download']")
	private WebElement GetUTRSummaryDownload;
	
	@FindBy(css="h2~div")
	private WebElement UploadFileErrorMessage;

	
	ManualUpdatePage manualUpdatePage;
		
	public MerchantSettlement(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.wait(testConfig, 1);
	}
	
	public void GoToMerchantSettlement(HomePage home)
	{
		home.navigateToAdminHome();
		home.ClickManualTransactionUpdate();
	}
	
	public void esscomSettlementFileUpload(Config testConfig,String fileName) {
		Element.enterFileName(testConfig, esscomFile, fileName, "Settlement File upload");
		Element.click(testConfig, esscomFileSubmit, "Click on submit");
	    Browser.wait(testConfig, 5);	
	}

	public void esscomClickSettlementButton() {
		Element.click(testConfig, esscomFileSubmit, "Settlement File Submit");
    }

	
	public void uploadEsscom(Config testConfig, String fileName){
		manualUpdatePage= new ManualUpdatePage(testConfig);
		manualUpdatePage.ClickMerchantSettlement();
		Element.enterFileName(testConfig, esscomFile, fileName, "Esscom File");
	    Element.click(testConfig, esscomFileSubmit, "Click on submit");
	    Browser.wait(testConfig, 5);	    
	}

	public InvalidFilePage uploadInvalidEsscom(Config testConfig, String fileName){
		manualUpdatePage= new ManualUpdatePage(testConfig);
		manualUpdatePage.ClickMerchantSettlement();
		Element.enterFileName(testConfig, esscomFile, fileName, "Esscom File");
	    Element.click(testConfig, esscomFileSubmit, "Click on submit");
	    Browser.wait(testConfig, 5);
	    
	    return new InvalidFilePage(testConfig);
	}

	public ManualUpdatePage uploadInvalidEsscom1(Config testConfig, String fileName){
		manualUpdatePage= new ManualUpdatePage(testConfig);
		manualUpdatePage.ClickMerchantSettlement();
		Element.enterFileName(testConfig, esscomFile, fileName, "Esscom File");
	    Element.click(testConfig, esscomFileSubmit, "Click on submit");
	    Browser.wait(testConfig, 5);
	    Browser.waitForPageLoad(testConfig, misc_submit1);
	    
	    return new ManualUpdatePage(testConfig);
	}
	
	public void citiSettlementFileUpload(Config testConfig,String fileName) {
		Element.enterFileName(testConfig, citiBankNodalFile, fileName, "CitiBankNodal Settlement File upload");
		Element.click(testConfig, citiBankNodalFileSubmit, "Click on submit");
	    Browser.wait(testConfig, 5);	
	}

	public void citiClickSettlementButton() {
		Element.click(testConfig, citiBankNodalFileSubmit, "CitiBankNodal Settlement File Submit");
    }
	
	public void uploadCitiBankNodalFile(Config testConfig, String fileName){
		Element.enterFileName(testConfig, citiBankNodalFile, fileName, "CitiBankNodal File");
	    Element.click(testConfig, citiBankNodalFileSubmit, "Click on submit");
	    Browser.wait(testConfig, 5);	    
	}
	
	public InvalidFilePage uploadInvalidCitiBankNodal(Config testConfig, String fileName){
		manualUpdatePage= new ManualUpdatePage(testConfig);
		manualUpdatePage.ClickMerchantSettlement();
		Element.enterFileName(testConfig, citiBankNodalFile, fileName, "Invalid CitiBankNodal File");
	    Element.click(testConfig, citiBankNodalFileSubmit, "Click on submit");
	    Browser.wait(testConfig, 5);
	    
	    return new InvalidFilePage(testConfig);
	}
	
	public ManualUpdatePage uploadInvalidCitiBankNodal1(Config testConfig, String fileName){
		manualUpdatePage= new ManualUpdatePage(testConfig);
		manualUpdatePage.ClickMerchantSettlement();
		Element.enterFileName(testConfig, citiBankNodalFile, fileName, "Esscom File");
	    Element.click(testConfig, citiBankNodalFileSubmit, "Click on submit");
	    Browser.wait(testConfig, 5);
	    Browser.waitForPageLoad(testConfig, misc_submit1);
	    
	    return new ManualUpdatePage(testConfig);
	}
	
	public void getCitiBankNodalSummary(Config testConfig, String SettlementId){
		Element.enterFileName(testConfig, citiBankNodalSummaryFile, SettlementId, "CitiBankNodal File");
	    Element.click(testConfig, citiBankNodalSummaryFileSubmit, "Click on submit");	    
	}
	
	public void settleCitiBankNodalFileForTIDGeneration(Config testConfig, String SummaryFileForTDR){
		SummaryFileForTDR=SummaryFileForTDR.replace("//", "\\"+"\\");
		Element.enterFileName(testConfig, citiBankNodalTIDgeneration, SummaryFileForTDR, "CitiBankNodal File for TID generation");
	    Element.click(testConfig, citiBankNodalTIDGenerationSubmit, "Click on submit");	    
	}
	
	public void cronRun(String cronName) {
		Browser.navigateToURL(testConfig, testConfig.getRunTimeProperty("CronUrl"));
		WebElement Cron = Element.getPageElement(testConfig, How.linkText, cronName);
		Element.click(testConfig, Cron, "Cron Link");
		Browser.wait(testConfig, 3);
	}
	
	public void uploadTIDUTRFIle(Config testConfig, String TDRInputFile){
		TDRInputFile=TDRInputFile.replace("//", "\\"+"\\");
		Element.enterFileName(testConfig, tid_utr_file, TDRInputFile, "TID UTR File for settlement");
	    Element.click(testConfig, tid_utr_fileSubmit, "Click on submit");	    
	}

	public void uploadInvalidCitiBankNodalFile(Config testConfig, String filepath){
		
		String invalidFileUploadError = new TestDataReader(testConfig, "StringValues").GetData(13, "Value");
		
		File file=new File(filepath);
		
		Element.enterFileName(testConfig, citiBankNodalFile, file.getAbsolutePath(), "CitiBankNodal File");
	    Element.click(testConfig, citiBankNodalFileSubmit, "Click On submit");
	    
	    Helper.compareExcelEquals(testConfig, "File upload Error Message",
				invalidFileUploadError, GetErrorMessageOnHeaderOfManualUpdate());
	}

	public ManualUpdatePage uploadInvalidCitiBankNodalFile1(Config testConfig, String fileName){
		manualUpdatePage= new ManualUpdatePage(testConfig);
		manualUpdatePage.ClickMerchantSettlement();
		Element.enterFileName(testConfig, citiBankNodalFile, fileName, "CitiBankNodal File");
	    Element.click(testConfig, citiBankNodalFileSubmit, "Click on submit");
	    Browser.waitForPageLoad(testConfig, misc_submit1);
	    
	    return new ManualUpdatePage(testConfig);
	}
	
	public ManualUpdatePage ClearSettlementId(Config testConfig, String settlementId){
		Element.enterFileName(testConfig, clear_settlementId, settlementId, "SettlementId to clear");
	    Element.click(testConfig, clear_settlementIdSubmit, "Click on submit");
	    Browser.waitForPageLoad(testConfig, misc_submit1);
	    
	    Helper.compareContains(testConfig, "Settlement Id removed message", "1 Settlements Cleared from merchant_settlemnt table for Settlement Id "+settlementId,
	    		settlementIdRemoved.getText());
	    return new ManualUpdatePage(testConfig);
	}

	public void UploadCitiNoadalFile(Config testConfig, String CitiNodalInputFileName)
	{
		
		CitiNodalInputFileName=CitiNodalInputFileName.replace("//", "\\"+"\\");
		System.out.println(CitiNodalInputFileName);
		Element.enterFileName(testConfig, citiBankNodalFile, CitiNodalInputFileName, "CitiBankNodal File");		
	    Element.click(testConfig, citiBankNodalFileSubmit, "Click on submit");
	    
	}
	public void getUTRLevelSettlementSummary(Config testConfig, String SettlementId)
	{
		Element.enterData(testConfig, GetUTRSummaryInput, SettlementId, "Settlement ID");	
	    Element.click(testConfig, GetUTRSummaryDownload, "Click on submit");   
	}
	
	public String GetErrorMessageOnHeaderOfManualUpdate(){
		return UploadFileErrorMessage.getText();	
	}
}
