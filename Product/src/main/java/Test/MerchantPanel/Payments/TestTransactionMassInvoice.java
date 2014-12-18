package Test.MerchantPanel.Payments;

import java.io.File;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.Response.NewResponsePage;
import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import PageObject.MerchantPanel.Transactions.InvoiceTransactionConfirmationPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.FileHandler;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;
import Utils.Element.How;
import Utils.Helper.FileType;

public class TestTransactionMassInvoice extends TestBase{

	public TransactionPage trans;

	@Test(description = "Verify mass invoice transaction with sheet containing blank column", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestMassInvoiceSheetWithBlankCoulumn(Config testConfig){

		int transactionRowNum = 12;

		// Create sheet to upload
		String massInvoiceFile = Helper.getExcelFile(testConfig, FileType.massInvoice);
		String filePath = massInvoiceFile;
		Browser.wait(testConfig, 2);
		FileHandler.write_edit(filePath, "Sheet1", 0, 10, " ");
		FileHandler.write_edit(filePath, "Sheet1", 1, 10, " ");
		FileHandler.write_edit(filePath, "Sheet1", 0, 11, "CustomerZipCode");
		FileHandler.write_edit(filePath, "Sheet1", 1, 11, "12345");

		// login as merchant and upload Mass Invoice Sheet
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.loginAndNavigateToMassInvoice(transactionRowNum);
		Browser.wait(testConfig, 2);
		dashboardHelper.UploadMassInvoiceSheet(testConfig, filePath);

		//read the downloaded file
		Browser.wait(testConfig, 5);
		String massInvoiceResult = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,massInvoiceResult);
		String fileName = file.getName();
		String SheetName = (String) fileName.subSequence(0, 28);
		TestDataReader tr = new TestDataReader(testConfig,SheetName, massInvoiceResult+fileName);

		Assert.assertTrue(tr.GetData(1, "invoice_status").endsWith("success"));
	}

	@Test(description = "Verify mass invoice transaction with sheet containing blank Row", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestMassInvoiceSheetWithBlankRow(Config testConfig){

		int transactionRowNum = 12;
		//Create file to upload
		String massInvoiceFile = Helper.getExcelFile(testConfig, FileType.massInvoice);
		String filePath = massInvoiceFile;
		FileHandler.setRowBlank(filePath, "Sheet1", 1, 11);
		TestDataReader data = new TestDataReader(testConfig,"MassInvoice");
		FileHandler.write_edit(filePath, "Sheet1", 2, 0, data.GetData(1, "Amount"));
		FileHandler.write_edit(filePath, "Sheet1", 2, 1, data.GetData(1, "TransactionID"));
		FileHandler.write_edit(filePath, "Sheet1", 2, 2, data.GetData(1, "ProductDescription"));
		FileHandler.write_edit(filePath, "Sheet1", 2, 3, data.GetData(1, "CustomerName"));
		FileHandler.write_edit(filePath, "Sheet1", 2, 4, data.GetData(1, "CustomerEmail"));
		FileHandler.write_edit(filePath, "Sheet1", 2, 5, data.GetData(1, "CustomerMobile"));
		FileHandler.write_edit(filePath, "Sheet1", 2, 6, data.GetData(1, "CustomerAddress"));
		FileHandler.write_edit(filePath, "Sheet1", 2, 7, data.GetData(1, "CustomerCity"));
		FileHandler.write_edit(filePath, "Sheet1", 2, 8, data.GetData(1, "CustomerState"));
		FileHandler.write_edit(filePath, "Sheet1", 2, 9, data.GetData(1, "CustomerCountry"));
		FileHandler.write_edit(filePath, "Sheet1", 2, 10, data.GetData(1, "CustomerZipCode"));

		// login as merchant and upload Mass Invoice Sheet
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.loginAndNavigateToMassInvoice(transactionRowNum);
		Browser.wait(testConfig, 2);
		dashboardHelper.UploadMassInvoiceSheet(testConfig, filePath);

		//read the downloaded file
		Browser.wait(testConfig, 5);
		String massInvoiceResult = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,massInvoiceResult);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"PayU", massInvoiceResult+fileName);
		
		//Assert.assertTrue(tr.GetData(1, "invoice_status").endsWith("Invalid amount"));
		Assert.assertTrue(tr.GetData(1, "invoice_status").endsWith("success"));
	}

	@Test(description = "Verify mass invoice transaction with blank sheet", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestMassInvoiceBlankFile(Config testConfig){

		int transactionRowNum = 12;
		//Create file to upload
		String massInvoiceFile = Helper.getExcelFile(testConfig, FileType.massInvoice);
		String filePath = massInvoiceFile;
		FileHandler.setRowBlank(filePath, "Sheet1", 0, 11);
		FileHandler.setRowBlank(filePath, "Sheet1", 1, 11);

		// login as merchant and upload Mass Invoice Sheet
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.loginAndNavigateToMassInvoice(transactionRowNum);
		Browser.wait(testConfig, 2);
		dashboardHelper.UploadMassInvoiceSheet(testConfig, filePath);

		if(Element.IsElementDisplayed(testConfig,Element.getPageElement(testConfig, How.css, "h1")))
		{
			dashboardHelper.invoiceTransactionConfirmationPage = new InvoiceTransactionConfirmationPage(testConfig);
		}

		Assert.assertTrue(dashboardHelper.invoiceTransactionConfirmationPage.errorMessage().equals("Error: Mandatory column missing."));

	}

	@Test(description = "Verify mass invoice transaction with sheet containing headers only", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestMassInvoiceSheetWithoutRecords(Config testConfig){

		int transactionRowNum = 12;
		//Create file to upload
		String massInvoiceFile = Helper.getExcelFile(testConfig, FileType.massInvoice);
		String filePath = massInvoiceFile;
		FileHandler.setRowBlank(filePath, "Sheet1", 1, 11);

		// login as merchant and upload Mass Invoice Sheet
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.loginAndNavigateToMassInvoice(transactionRowNum);
		Browser.wait(testConfig, 2);
		dashboardHelper.UploadMassInvoiceSheet(testConfig, filePath);

		if(Element.IsElementDisplayed(testConfig,Element.getPageElement(testConfig, How.css, "h1")))
		{
			dashboardHelper.invoiceTransactionConfirmationPage = new InvoiceTransactionConfirmationPage(testConfig);
		}

		Assert.assertTrue(dashboardHelper.invoiceTransactionConfirmationPage.errorMessage().equals("Error: Mandatory column missing."));
		
	}

	@Test(description = "Verify mass invoice transaction with sheet containing negative test cases", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestMassInvoiceNegativeTestCases(Config testConfig){

		int transactionRowNum = 12;
		//Create file to upload
		String massInvoiceFile = Helper.getExcelFile(testConfig, FileType.massInvoiceInvalid);
		System.out.println(massInvoiceFile+"*****");
		// login as merchant and upload Mass Invoice Sheet
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.loginAndNavigateToMassInvoice(transactionRowNum);
		Browser.wait(testConfig, 2);
		dashboardHelper.UploadMassInvoiceSheet(testConfig, massInvoiceFile);
		Browser.wait(testConfig, 5);

		//read the downloaded file
		String massInvoiceResult = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,massInvoiceResult);
		String fileName = file.getName();
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", massInvoiceResult+fileName);
		TestDataReader data = new TestDataReader(testConfig,"MassInvoice");
		
		Helper.compareEquals(testConfig, "", data.GetData(1, "Status"), tr.GetData(1, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(2, "Status"), tr.GetData(2, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(3, "Status"), tr.GetData(3, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(4, "Status"), tr.GetData(4, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(5, "Status"), tr.GetData(5, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(6, "Status"), tr.GetData(6, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(7, "Status"), tr.GetData(7, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(8, "Status"), tr.GetData(8, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(9, "Status"), tr.GetData(9, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(10, "Status"), tr.GetData(10, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(11, "Status"), tr.GetData(11, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(12, "Status"), tr.GetData(12, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(13, "Status"), tr.GetData(13, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(14, "Status"), tr.GetData(14, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(15, "Status"), tr.GetData(15, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(16, "Status"), tr.GetData(16, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(17, "Status"), tr.GetData(17, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(18, "Status"), tr.GetData(18, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(19, "Status"), tr.GetData(19, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(20, "Status"), tr.GetData(20, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(21, "Status"), tr.GetData(21, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(22, "Status"), tr.GetData(22, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(23, "Status"), tr.GetData(23, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(24, "Status"), tr.GetData(24, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(25, "Status"), tr.GetData(25, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(26, "Status"), tr.GetData(26, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(27, "Status"), tr.GetData(27, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(28, "Status"), tr.GetData(28, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(29, "Status"), tr.GetData(29, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(30, "Status"), tr.GetData(30, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(31, "Status"), tr.GetData(31, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(32, "Status"), tr.GetData(32, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(33, "Status"), tr.GetData(33, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(34, "Status"), tr.GetData(34, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(35, "Status"), tr.GetData(35, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(36, "Status"), tr.GetData(36, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(37, "Status"), tr.GetData(37, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(38, "Status"), tr.GetData(38, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(39, "Status"), tr.GetData(39, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(40, "Status"), tr.GetData(40, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(41, "Status"), tr.GetData(41, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(42, "Status"), tr.GetData(42, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(43, "Status"), tr.GetData(43, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(44, "Status"), tr.GetData(44, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(45, "Status"), tr.GetData(45, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(46, "Status"), tr.GetData(46, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(47, "Status"), tr.GetData(47, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(48, "Status"), tr.GetData(48, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(49, "Status"), tr.GetData(49, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(50, "Status"), tr.GetData(50, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(51, "Status"), tr.GetData(51, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(52, "Status"), tr.GetData(52, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(53, "Status"), tr.GetData(53, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(54, "Status"), tr.GetData(54, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(55, "Status"), tr.GetData(55, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(56, "Status"), tr.GetData(56, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(57, "Status"), tr.GetData(57, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(58, "Status"), tr.GetData(58, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(59, "Status"), tr.GetData(59, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(60, "Status"), tr.GetData(60, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(61, "Status"), tr.GetData(61, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(62, "Status"), tr.GetData(62, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(63, "Status"), tr.GetData(63, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(64, "Status"), tr.GetData(64, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(65, "Status"), tr.GetData(65, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(66, "Status"), tr.GetData(66, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(67, "Status"), tr.GetData(67, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(68, "Status"), tr.GetData(68, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(69, "Status"), tr.GetData(69, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(70, "Status"), tr.GetData(70, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(71, "Status"), tr.GetData(71, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(72, "Status"), tr.GetData(72, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(72, "Status"), tr.GetData(72, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(73, "Status"), tr.GetData(73, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(74, "Status"), tr.GetData(74, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(75, "Status"), tr.GetData(75, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(76, "Status"), tr.GetData(76, "invoice_status"));
		Helper.compareEquals(testConfig, "", data.GetData(77, "Status"), tr.GetData(77, "invoice_status"));
				
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify Mass invoice transaction via URL in mail", 
			dataProvider="GetTestConfig", timeOut=6000000, groups="1")
	public void TestMassInvoiceEmailLink(Config testConfig){

		int transactionRowNum = 12;
		int cardDetailsRowNum = 1;

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		String massInvoiceFile = Helper.getExcelFile(testConfig, FileType.massInvoice);
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		// login as merchant and upload Mass Invoice Sheet
		String filePath = massInvoiceFile;
		dashboardHelper.loginAndNavigateToMassInvoice(transactionRowNum);
		String randomtxnID = RandomStringUtils.randomAlphanumeric(10);
		FileHandler.write_edit(filePath, "Sheet1", 1, 1, randomtxnID);
		Browser.wait(testConfig, 2);
		dashboardHelper.UploadMassInvoiceSheet(testConfig, filePath);
		Browser.wait(testConfig, 5);
		
		TestDataReader data = new TestDataReader(testConfig,"MassInvoice");
		
		//run the emailInvoice cron--navigate to admin.payu.in		
		Browser.navigateToURL(testConfig, "admin.payu.in");
		Browser.wait(testConfig, 2);
		Element.getPageElement(testConfig, How.linkText, "emailInvoice.php").click();
		Browser.wait(testConfig, 2);
		
		// navigate to gmail and click link
		helper.payment = dashboardHelper.emailInvoicePage.gmailLoginAndClickPaymentLink(randomtxnID);
		//make payment via credit card
		NewResponsePage newResponse = helper.makePaymentViaCreditCard(cardDetailsRowNum);
		newResponse.verifyPageText(true);
		
		//Generate TxnID to be used for next day test case
		FileHandler.write_edit(testConfig.getRunTimeProperty("TestDataSheet"), "MassInvoice", 1, 12, randomtxnID);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify Mass invoice functionality when uploading wrong file type", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestMassInvoiceWrongFileType(Config testConfig){

		int transactionRowNum = 12;
		//Create file to upload
		String massInvoiceFile = testConfig.getRunTimeProperty("Esscom1_invalid");

		// login as merchant and upload Mass Invoice Sheet
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);
		dashboardHelper.loginAndNavigateToMassInvoice(transactionRowNum);
		Browser.wait(testConfig, 2);
		dashboardHelper.UploadMassInvoiceSheet(testConfig, massInvoiceFile);
		Browser.wait(testConfig, 5);

		Assert.assertTrue(dashboardHelper.verifyErrorMessageMassInvoice().equals("The file uploaded is not a valid excel (.xls) file"));
	}
	
	@Test(description = "Verify Mass invoice functionality whensubmit button is clicked without sheet", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void TestMassInvoiceClickSubmitWithoutSheet(Config testConfig){

		int transactionRowNum = 12;
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		// login as merchant and click on Submit button
		String filePath = "";
		dashboardHelper.loginAndNavigateToMassInvoice(transactionRowNum);
		dashboardHelper.UploadMassInvoiceSheet(testConfig, filePath);
		Browser.wait(testConfig, 5);
		
		//Need to update this test case once bug will be fixed
		System.out.println("Redmine ID for bug is 22937");
		
		Assert.assertTrue(false);
	}
}