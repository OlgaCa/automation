package PageObject.AdminPanel.DownloadExcel;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Config;
import Utils.Element;



public class DownloadExcelPage {
	@FindBy(name="day")
	private WebElement daySelecter;
	
	@FindBy(name="monthnum")
	private WebElement monthSelecter;
	
	@FindBy(name="year")
	private WebElement yearSelecter;
	
	@FindBy(xpath="/html/body/form/input[1]")
	private WebElement successRefundListButton;
	
	@FindBy(xpath="/html/body/form/input[2]")
	private WebElement successChargebackListButton;
	
	@FindBy(xpath="/html/body/form/input[3]")
	private WebElement successChargebackCorrectionListButton;
	
	@FindBy(xpath="/html/body/form/input[4]")
	private WebElement successCaptureListButton;
	
	@FindBy(xpath="/html/body/form/input[5]")
	private WebElement transactionListButton;
	
	@FindBy(xpath="/html/body/form/input[6]")
	private WebElement internationalTransactionListButton;
	
	@FindBy(xpath="/html/body/form/input[7]")
	private WebElement submitButton;
	
	@FindBy(xpath="/html/body/form/font/b")
	private WebElement errorMsg;
	
	public WebElement getErrorMsg() {
		return errorMsg;
	}

	public WebElement getDaySelecter() {
		return daySelecter;
	}

	public WebElement getMonthSelecter() {
		return monthSelecter;
	}

	public WebElement getYearSelecter() {
		return yearSelecter;
	}


	
	private Config testConfig;
	
	public DownloadExcelPage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		
		
	}
	
	public void selectDate(int day,int month,int year){
		String dd="",mm="",yyyy="";
		if(day<10)
			dd = "0"+day;
		else
			dd = ""+day;
		if(month<10)
			mm = "0"+month;
		else
			mm = ""+month;
		
			yyyy = ""+year;
		Element.selectVisibleText(testConfig, daySelecter, dd, "day selected");
		Element.selectVisibleText(testConfig, monthSelecter, mm, "day selected");
		Element.selectVisibleText(testConfig, yearSelecter, yyyy, "day selected");
	}
	

	public WebElement getSuccessRefundListButton() {
		return successRefundListButton;
	}

	public WebElement getSuccessChargebackListButton() {
		return successChargebackListButton;
	}

	public WebElement getSuccessChargebackCorrectionListButton() {
		return successChargebackCorrectionListButton;
	}

	public WebElement getSuccessCaptureListButton() {
		return successCaptureListButton;
	}

	public WebElement getTransactionListButton() {
		return transactionListButton;
	}

	public WebElement getInternationalTransactionListButton() {
		return internationalTransactionListButton;
	}

	public WebElement getSubmitButton() {
		return submitButton;
	}



}
