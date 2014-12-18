package PageObject.AdminPanel.Payments.Response;

import Utils.FileHandler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.TestDataReader;
import Utils.Element.How;

public class BankRedirectPage {

	private Config testConfig;
	public TestDataReader bankRedirectData;

	public BankRedirectPage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.wait(testConfig, 10);
	}

	/**
	 * Reads the URL from "BankRedirect" sheet and verifies if it matches the current browser URL
	 * @param bankRedirectRow row number of excel sheet to read
	 * @return if current URL matches the expected one
	 */
	public boolean VerifyRedirectedURL(int bankRedirectRow)
	{
		bankRedirectData = new TestDataReader(testConfig, "BankRedirect"); 
		String expectedURL = bankRedirectData.GetData(bankRedirectRow, "RedirectURL");
		return Browser.verifyURL(testConfig, expectedURL);
	}

	/**
	 * Clicks on submit button for successful transaction on Amex Page
	 */
	public TestResponsePage SubmitButtonForSuccessAMEX()
	{
		String element =null;
		WebElement select;
		element="selectAuthResult"; 
		select = Element.getPageElement(testConfig, How.id,element);
		Element.selectValue(testConfig, select, "AUTHENTICATED", "Select (Y) Authentication Successful for successful transaction");
		element = "input[type='submit']";
		select = Element.getPageElement(testConfig, How.css,element);
		Element.click(testConfig, select, "Click submit");
		return new TestResponsePage(testConfig);
		
	}
	
	/**
	 * Clicks on submit button for failed transaction on Amex Page
	 */
	public TestResponsePage SubmitButtonForFailedAMEX()
	{
		String element =null;
		WebElement select;
		element="selectAuthResult"; 
		select = Element.getPageElement(testConfig, How.id,element);
		Element.selectValue(testConfig, select, "UNAUTHENTICATED", "Select (N) Authentication Failed for failure transaction");
		element = "input[type='submit']";
		select = Element.getPageElement(testConfig, How.css,element);
		Element.click(testConfig, select, "Click submit");
		return new TestResponsePage(testConfig);

	}

	/**
	 * Reads the expected BankRedirectRow from "PaymentType" sheet and checks that if URL for that row in "BankRedirect" sheet, matches the current browser URL 
	 * @param paymentTypeData Payment Type Data which was used to do payment, the expected BankRedirectRow (of "BankRedirect" sheet) will be read using this
	 * @param paymentTypeRowNum Corresponding row number of payment data
	 * @return if current URL matches the expected one
	 */
	public boolean VerifyRedirectedURL(TestDataReader paymentTypeData, int paymentTypeRowNum) 
	{
		int bankRedirectRow = Integer.parseInt(paymentTypeData.GetData(paymentTypeRowNum, "BankRedirectRow"));	
		return VerifyRedirectedURL(bankRedirectRow);
	}
	
	public void Write3DSMerchantName(int transactionRowNum, TestDataReader bankRedirectData, int bankRedirectRow, boolean pageverified) 
	{
		String Merchant_Identifier_How = bankRedirectData.GetData(bankRedirectRow, "Merchant_Identifier_How");
		String Merchant_Identifier_What = bankRedirectData.GetData(bankRedirectRow, "Merchant_Identifier_What");
		FileHandler.write_editXLS(testConfig, testConfig.getRunTimeProperty("TestDataSheet"), "TransactionDetails", transactionRowNum, 5, Boolean.toString(pageverified) ,"Page Verifed");
		WebElement element = Element.getPageElement(testConfig, How.valueOf(Merchant_Identifier_How), Merchant_Identifier_What);
		if(element!=null)
		{
			System.out.println(element.getText());
			FileHandler.write_editXLS(testConfig, testConfig.getRunTimeProperty("TestDataSheet"), "TransactionDetails", transactionRowNum, 6, element.getText(),"Merchant Name");
		}
	}
}