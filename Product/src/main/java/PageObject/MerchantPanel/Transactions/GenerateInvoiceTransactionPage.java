package PageObject.MerchantPanel.Transactions;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class GenerateInvoiceTransactionPage{
	private Config testConfig;
	
	@FindBy(id="invoice_amount_button")
	private WebElement submit; 
	
	@FindBy(id="invoiceAmount")
	private WebElement invoiceAmount; 
	
	@FindBy(id="invoice_template_id")
	private WebElement invoiceTemplate;
	
	@FindBy(id="message_cancel_button")
	private WebElement invalidAmount;
	
	@FindBy(xpath="//input[@value='OK']")
	private WebElement ok;
	
	@FindBy(id="amount_cancel_button")
	private WebElement goBack;
	
	

	public GenerateInvoiceTransactionPage(Config testConfig)
	{   this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, submit);
	}
	
	public void EnterAmount(String amount)
	{
		Element.enterData(testConfig, invoiceAmount, amount, "amount");
	}
	
	public void selectTemplate(String template)
	{
		Element.selectValue(testConfig, invoiceTemplate, template, "template");
	}
	
	public void Submit()
	{
		Element.click(testConfig, submit, "submit button");
		
	}
	
	public void invalidAmount()
	{
		Boolean isTrue=Element.IsElementDisplayed(testConfig, invalidAmount);
		Helper.compareTrue(testConfig, "invalid amount message box", isTrue);
	}
	
	public TransactionsPage ClickOk()
	{
		Element.click(testConfig, invalidAmount, "Ok button on invalid amount");
		return new TransactionsPage(testConfig);
	}
	
	public TransactionsPage Ok()
	{
		Element.click(testConfig, ok, "Ok button on invalid amount");
		return new TransactionsPage(testConfig);
	}
	
	public TransactionsPage clickGoback()
	{
		Element.click(testConfig, goBack, "Go Back button on generate invoice");
		return new TransactionsPage(testConfig);
	}
	
	
}
