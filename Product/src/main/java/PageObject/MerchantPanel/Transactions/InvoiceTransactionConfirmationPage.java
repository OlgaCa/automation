package PageObject.MerchantPanel.Transactions;

import org.openqa.selenium.WebElement;
import java.awt.AWTException;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.GmailLogin;
import Utils.GmailVerification;

public class InvoiceTransactionConfirmationPage {

	private Config testConfig;
	
	@FindBy(css="h1")
	private WebElement info;

	@FindBy(xpath="//span[@class='fL']")
	private WebElement CopyLinkText;

	@FindBy(css="input.sendmail")
	private WebElement sendEmail;

	@FindBy(id="linkPop")
	private WebElement popUp;

	@FindBy(css="img")
	private WebElement clickOk;

	//@FindBy(xpath="//p[contains(text(),'Click on the link below to intiate transaction ')]/a")
	@FindBy(xpath="//a[contains(@href, 'https://www.google.com/url?q=https%3A%2F%2Fsecure.payu.in%2FprocessInvoice%3FinvoiceId')]")
	private WebElement clickOnLink;
	
	@FindBy(xpath="//div[@id='close_but']/img")
	private WebElement closeButton;
	
	
	////a:contains('payu.in/_payment_')]
	////a[contains(text(),'payu.in/_payment_')]
	public InvoiceTransactionConfirmationPage(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, info);
	}

	/**
	 * @return Payment Option Page for transaction via Copy Link

		//Clearing data in Clipboard
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			clipboard.setContents(new Transferable() {
				public DataFlavor[] getTransferDataFlavors() {
					return new DataFlavor[0];
				}

				public boolean isDataFlavorSupported(DataFlavor flavor) {
					return false;
				}

				public Object getTransferData(DataFlavor flavor)  {
					throw new UnsupportedFlavorException(flavor);
				}
			}, null);
		} 
		catch (IllegalStateException e) {} 

		//Clicking on Copy Link
		Element.click(testconfig, CopyLink, "CopyLink");
		

		//Pasting the copied URL
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		Transferable contents = clipboard.getContents(CopyLink);
		boolean hasTransferableText = (contents != null) &&	contents.isDataFlavorSupported(DataFlavor.stringFlavor);

		if ( hasTransferableText ) {
			try {
				String url;
				try {
					url = (String)contents.getTransferData(DataFlavor.stringFlavor);
					Browser.navigateToURL(testConfig, url);
					System.out.println(url);
				} catch (UnsupportedFlavorException e) {
					testconfig.logexception(e);
				}
			}
			catch (IOException ex) {
				testconfig.logexception(e);
			}
		}

		return new PaymentOptionsPage(testConfig);
	}*/

	/**
	 * Method to get link text
	 * @return
	 */
    public String retInvoiceURL() 
    {
    		
	    String retURL = null;
	    retURL = CopyLinkText.getText();
    	return retURL;
    	
    }
	/**
	 * @return Gmail Page for email verification
	 */
	public void SendEmail() {

		Element.click(testConfig, sendEmail, "Send Email");
		Element.click(testConfig, clickOk, "OK");
    }
	
	/**
	 * verify close button is present
	 */
	public void verifyCloseButtonPresent() 
	{
		Element.verifyElementPresent(testConfig, closeButton, "Close button");
	}

	/**
	 * @return Payment Option Page for transaction via Sending Email
	 */
	public PaymentOptionsPage gmailLoginAndClickPaymentLink() {
		GmailLogin gmailLogin = new GmailLogin(testConfig);
		GmailVerification gmailVerification = gmailLogin.Login("payu.testing", "payu@123");     
		// wait for email
		Browser.wait(testConfig, 10);
		gmailVerification.FilterEmail("invoice for "+testConfig.getRunTimeProperty("txnId"), testConfig.getRunTimeProperty("txnId"));
		gmailVerification.verifyEmailText("Your order at AutomationMerchant1: Invoice #"+testConfig.getRunTimeProperty("txnId"));
		gmailVerification.SelectEmail();
		gmailVerification.verifyEmailContent(testConfig.getRunTimeProperty("txnId"));
		Browser.wait(testConfig, 5);
		Element.click(testConfig, clickOnLink, "Click on the link");
		Browser.switchToNewWindow(testConfig);
		
		return new PaymentOptionsPage(testConfig);
		
	}
	
	/**
	 * @return Error message on mass invoice page
	 */
	@FindBy(css = "h1")
	private WebElement errorMessage;
	public String errorMessage() {
		return errorMessage.getText();
    }
}

