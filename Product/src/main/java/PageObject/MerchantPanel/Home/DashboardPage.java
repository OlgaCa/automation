package PageObject.MerchantPanel.Home;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.MerchantPanel.Billing.BillingsPage;
import PageObject.MerchantPanel.Payments.EmailInvoicePage;
import PageObject.MerchantPanel.Settings.MerchantCheckoutPage;
import PageObject.MerchantPanel.Settings.MerchantLoginsPage;
import PageObject.MerchantPanel.Settings.NewMerchantLoginPage;
import PageObject.MerchantPanel.Settings.PreviewPage;
import PageObject.MerchantPanel.Settings.SetLookPage;
import PageObject.MerchantPanel.Transactions.CODCancelPage;
import PageObject.MerchantPanel.Transactions.CODSettledPage;
import PageObject.MerchantPanel.Transactions.CODVerifyPage;
import PageObject.MerchantPanel.Transactions.CancelPage;
import PageObject.MerchantPanel.Transactions.CancelRefundPage;
import PageObject.MerchantPanel.Transactions.CapturePage;
import PageObject.MerchantPanel.Transactions.RefundPage;
import PageObject.MerchantPanel.Transactions.RequestsPage;
import PageObject.MerchantPanel.Transactions.TransactionsPage;
import PageObject.MerchantPanel.Transactions.ViewInvoicesPage;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.TestDataReader;

public class DashboardPage {

                private Config testConfig;

                public DashboardPage(Config testConfig)
                {
                                this.testConfig = testConfig;
                                PageFactory.initElements(this.testConfig.driver, this);
                                Browser.waitForPageLoad(this.testConfig, dropBox);
                            //    Element.click(testConfig, UIPopUpCloseButton, "Close UI popup");
                                ClickClose();
                }

                //Closing the queued refund request
                @FindBy(id="cancel_button")
                private WebElement closeRefundRequest;
                
                                /**
                 * clicks on close for queued refund request button on dashboard
                 * @return 
                 * @return dashboard
                 */
                public void ClickClose()
                {
                	
                                Boolean isPass = closeRefundRequest.isDisplayed();
                                if(isPass) {                           
                                                Element.click(testConfig, closeRefundRequest, "Closing Queued Refund Requests");
                                }
                }

                //Dashboard tabs
                @FindBy(css="a.dashboard")
                private WebElement dashboardTab;

                @FindBy(css="a.transaction")
                private WebElement transactionTab;
                
                @FindBy(css="a.billing")
                private WebElement billingTab;
                
                @FindBy(css="a.settings")
                private WebElement settingTab;
                
            	@FindBy(css="div#ip_whitelisting div div div a")
            	private WebElement UIPopUpCloseButton;

                @FindBy(partialLinkText="Advanced Customization")
                private WebElement AdvCustomization ;

                @FindBy(partialLinkText="Customize your page")
                private WebElement customizePage ;
                
                @FindBy(linkText="Checkout Page")
                private WebElement checkoutPage;

                @FindBy(linkText="View All")
                private WebElement viewAll;

                @FindBy(partialLinkText="Create Login")
                private WebElement createLogin;
                
                @FindBy(partialLinkText="Manage Logins")
                private WebElement manageLogin;

                
                @FindBy(linkText="Request Capture")
                private WebElement requestCapture;

                @FindBy(linkText="Request Refund")
                private WebElement requestRefund;

                @FindBy(linkText="Request Cancel")
                private WebElement requestCancel;
                
                @FindBy(linkText="Cancel/Refund")
                private WebElement requestCancelRefund;

                @FindBy(linkText="View Requests")
                private WebElement viewRequests;

                @FindBy(linkText="Cod Verify")
                private WebElement codVerify;
                
                @FindBy(linkText="Cod Cancel")
                private WebElement codCancel;
                
                @FindBy(linkText="Sign Out")
                private WebElement signOut;

                @FindBy(linkText="Cod Settled")
                private WebElement codSettled;

                @FindBy(css="a.new_transaction")
                private WebElement newTransactionTab;

                @FindBy(id="new-txn-active")
                private WebElement newTransaction;

                @FindBy(linkText="Through Email Invoice")
                private WebElement throughEmailInvoice;
                
                @FindBy(linkText="Through IVR")
                private WebElement throughIVR;

                @FindBy(name="dropbox")
                private WebElement dropBox;
                
                @FindBy(linkText="View Invoices")
            	private WebElement viewInvoices;

                @FindBy(name="reconcile")
                private WebElement reconcile;

                @FindBy(css="div > span > input[value=\"Upload\"]")
                private WebElement reconcileButton;


                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div[3]/div[2]/table/tbody/tr[1]/td[2]")
                private WebElement retries;
                
                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div[3]/div[2]/table/tbody/tr[2]/td[2]")
                private WebElement retriesUniqueUsers;

                
                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div[3]/div[2]/table/tbody/tr[3]/td[2]")
                private WebElement retriesSuccessful;

                

                public String getAmount(){
                                return(totAmount.getText());   
                }
                
                public String getRetries(){
                                return(retries.getText());             
                }
                
                
                public String getRetriesUniqueUsers(){
                                return(retriesUniqueUsers.getText());  
                }
                
                
                public String getRetriesSuccesful(){
                                return(retriesSuccessful.getText());        
                }
                
                public String getTransactionNumber(){
                                return(totTransaction.getText());             
                }
                
                @FindBy(xpath="//div[@id='amount']/following-sibling::div")
                private WebElement totAmount;
                
                @FindBy(xpath="//div[@id='transaction']/following-sibling::div")
                private WebElement totTransaction;
                
                @FindBy(xpath="//div[@id='avgTxnAmt']/following-sibling::div")
                private WebElement avgAmountperTransaction;
                
                @FindBy(xpath="//div[2]/div/div[3]/div[@class='blue floatLeft']")
                private WebElement revertedTransactions;
                
                
                @FindBy(xpath="//div/div[4]/div[@class='blue floatLeft']")
                private WebElement successfulTransactions;
                

                @FindBy(xpath="//div[@class='successful-unique']")
                private WebElement successfulPercentage;
                
                @FindBy(css="div.blue")
                private WebElement initiatedTransactions;
                
                                

                @FindBy(css="div.amount-value")
                private WebElement rightSideAmount;
                
                
                public WebElement getInititatedTransactionLink() {
                                WebElement a =  Element.getPageElement(testConfig, How.xPath,"//div[@id='container']"); ;
                                List<WebElement> LinkPair = a.findElements(By.xpath("//div[@class='overall-transaction-right']/a"));
                                
                                WebElement inititatedTransactionLink = LinkPair.get(0) ;
                                return inititatedTransactionLink;
                }

                public WebElement getBouncedTransactionLink() {
                                WebElement a =  Element.getPageElement(testConfig, How.xPath,"//div[@id='container']"); ;
                                List<WebElement> LinkPair = a.findElements(By.xpath("//div[@class='overall-transaction-right']/div[1]/a"));
                                WebElement bouncedTransactionLink = LinkPair.get(0) ;
                                return bouncedTransactionLink;
                }

                public WebElement getDroppedTransactionLink() {
                                WebElement a =  Element.getPageElement(testConfig, How.xPath,"//div[@id='container']"); ;
                                List<WebElement> LinkPair = a.findElements(By.xpath("//div[@class='overall-transaction-right']/div[2]/a"));
                                WebElement droppedTransactionLink = LinkPair.get(0) ;
                                return droppedTransactionLink;
                }

                public WebElement getFailedTransactionLink() {
                                WebElement a =  Element.getPageElement(testConfig, How.xPath,"//div[@id='container']"); ;
                                List<WebElement> LinkPair = a.findElements(By.xpath("//div[@class='overall-transaction-right']/div[3]/a"));
                                WebElement failedTransactionLink = LinkPair.get(0) ;
                                return failedTransactionLink;
                }

                public WebElement getSuccessfulTransactionLink() {
                                WebElement a =  Element.getPageElement(testConfig, How.xPath,"//div[@id='container']"); ;
                                List<WebElement> LinkPair = a.findElements(By.xpath("//div[@class='overall-transaction-right']/div[3]"));
                                WebElement successfulTransactionLink = LinkPair.get(0) ;
                                return successfulTransactionLink;
                }

                public WebElement getInititatedSecondTransactionLink() {
                                WebElement a =  Element.getPageElement(testConfig, How.xPath,"//div[@id='container']"); ;
                                List<WebElement> LinkPair = a.findElements(By.xpath("//div[@class='overall-transaction-right']/a"));
                                 WebElement inititatedSecondTransactionLink = LinkPair.get(1);
                                return inititatedSecondTransactionLink;
                }

                public WebElement getUniqueTransactionLink() {
                                WebElement a =  Element.getPageElement(testConfig, How.xPath,"//div[@id='container']"); ;
                                List<WebElement> LinkPair = a.findElements(By.xpath("//div[@class='overall-transaction-right']/div[1]/a/span"));
                                WebElement uniqueTransactionLink = LinkPair.get(1) ;
                                return uniqueTransactionLink;
                }

                public WebElement getUniqueBounceTransactionLink() {
                                WebElement a =  Element.getPageElement(testConfig, How.xPath,"//div[@id='container']"); ;
                                List<WebElement> LinkPair = a.findElements(By.xpath("//div[@class='overall-transaction-right']/div[2]/a"));
                                WebElement uniqueBounceTransactionLink = LinkPair.get(1) ;
                                return uniqueBounceTransactionLink;
                }


                public WebElement getUniqueDroppedTransactionLink() {
                                WebElement a =  Element.getPageElement(testConfig, How.xPath,"//div[@id='container']"); ;
                                List<WebElement> LinkPair = a.findElements(By.xpath("//div[@class='overall-transaction-right']/div[3]/a"));
                                WebElement uniqueDroppedTransactionLink = LinkPair.get(1) ;
                                return uniqueDroppedTransactionLink;
                }

                public WebElement getUniqueSuccessfulTransactionLink() {
                                WebElement a =  Element.getPageElement(testConfig, How.xPath,"//div[@id='container']"); ;
                                List<WebElement> LinkPair = a.findElements(By.xpath("//div[@class='overall-transaction-right']/div[4]"));
                                WebElement uniqueSuccessfulTransactionLink = LinkPair.get(0) ;
                                return uniqueSuccessfulTransactionLink;
                }

                public WebElement getUniqueFailedTransactionLink() {
                                WebElement a =  Element.getPageElement(testConfig, How.xPath,"//div[@id='container']"); ;
                                List<WebElement> LinkPair = a.findElements(By.xpath("//div[@class='overall-transaction-right']/div[4]/a"));
                                WebElement uniqueFaliedTransactionLink = LinkPair.get(0) ;
                                return uniqueFaliedTransactionLink;
                }

                public String getRightSideAmount() {
                                return rightSideAmount.getText();
                }


                public void setRightSideAmount(WebElement rightSideAmount) {
                                this.rightSideAmount = rightSideAmount;
                }

                @FindBy(xpath="//div[@id='container']/div/div/div[3]/div/div[2]/div[2]/table/tbody/tr[1]/td[2]")
                private WebElement bouncedNumber;
                

                @FindBy(xpath="//div[@id='container']/div/div[2]/div[2]/a[1]")
                private WebElement transactionTodayLink;
                
                @FindBy(xpath="//div[@id='container']/div/div[2]/div[2]/a[2]")
                private WebElement transactionThisWeekLink;
                
                
                @FindBy(linkText="contact@payu.in")
                private WebElement emailLink;
                
                @FindBy(css="div.grey > a > div.grey")
                private WebElement newToPayment;
                
                public WebElement getNewToPayment() {
                                return newToPayment;
                }
                
                public BillingsPage clickBilling() {
                                Element.click(testConfig, billingTab, "BillingTab");
                                return new BillingsPage(testConfig);
                }

                public void setNewToPayment(WebElement newToPayment) {
                                this.newToPayment = newToPayment;
                }

                public WebElement getTransactionTodayLink() {
                                return transactionTodayLink;
                }

                public void setTransactionTodayLink(WebElement transactionTodayLink) {
                                this.transactionTodayLink = transactionTodayLink;
                }

                public WebElement getTransactionThisWeekLink() {
                                return transactionThisWeekLink;
                }

                public String getBouncedNumber() {
                                return bouncedNumber.getText();
                }




                public String getBouncedPercentage() {
                                return bouncedPercentage.getText();
                }


                public String getDroppedNumber() {
                                return droppedNumber.getText();
                }


                public String getDroppedPercentage() {
                                return droppedPercentage.getText();
                }

                public String getFailedByBankNumber() {
                                return failedByBankNumber.getText();
                }

                public String getFailedByBankPercentage() {
                                return failedByBankPercentage.getText();
                }

                
                
                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div/div/div[2]/div/div[4]/div")
                private WebElement successfulNumber;
                
                @FindBy(xpath="//div[@id='container']/div/div/div[3]/div/div/div[2]/div/div[4]/div[2]")
                private WebElement failedNumber;
                
                public String getFailedNumber() {
                                return failedNumber.getText();
                }

                
                public String getSuccessfulNumber() {
                                return successfulNumber.getText();
                }

                
                @FindBy(xpath="//div[@id='container']/div/div/div[3]/div/div[2]/div[2]/table/tbody/tr[3]/td[2]")
                private WebElement failedByBankNumber;
                
                @FindBy(xpath="//div[@id='container']/div/div/div[3]/div/div[2]/div[2]/table/tbody/tr[3]/td[3]")
                private WebElement failedByBankPercentage;
                
                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div[2]/div[2]/div/div[2]/div")
                private WebElement uniqueNumber;
                
                @FindBy(xpath="//div[@id='container']/div/div/div[3]/div/div[2]/div[2]/table/tbody/tr[1]/td[3]")
                private WebElement bouncedPercentage;
                
                @FindBy(xpath="//div[@id='container']/div/div/div[3]/div/div[2]/div[2]/table/tbody/tr[2]/td[2]")
                private WebElement droppedNumber;
                
                @FindBy(xpath="//div[@id='container']/div/div/div[3]/div/div[2]/div[2]/table/tbody/tr[2]/td[3]")
                private WebElement droppedPercentage;
                
                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div/div/div[2]/div/div[2]/div[2]")
                private WebElement bouncedNumber_overAll;
                
                public WebElement getBouncedNumber_overAll() {
                                return bouncedNumber_overAll;
                }

                public WebElement getDroppedNumber_overAll() {
                                return droppedNumber_overAll;
                }

                public WebElement getFailedNumber_overAll() {
                                return failedNumber_overAll;
                }

                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div/div/div[2]/div/div[3]/div[2]")
                private WebElement droppedNumber_overAll;
                
                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div/div/div[2]/div/div[4]/div[2]")
                private WebElement failedNumber_overAll;
                
                
                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div[2]/div[2]/div/div[2]/div[2]")
                private WebElement retryNumber;
                
                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div[2]/div[2]/div/div[3]/div[2]")
                private WebElement uniqueBouncesNumber;
                
                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div[2]/div[2]/div/div[4]/div[2]")
                private WebElement uniquedroppedNumber;
                
                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div[2]/div[2]/div/div[5]/div")
                private WebElement uniqueSuccessfulNumber;
                
                @FindBy(xpath="/html/body/div/div[2]/div/div/div[3]/div[2]/div[2]/div/div[5]/div[2]")
                private WebElement uniqueFailedNumber;
                                
                public String getRetryNumber() {
                                return retryNumber.getText();
                }

                public String getUniqueNumber() {
                                return uniqueNumber.getText();
                }

                public String getUniqueBouncesNumber() {
                                return uniqueBouncesNumber.getText();
                }

                public String getUniquedroppedNumber() {
                                return uniquedroppedNumber.getText();
                }

                public String getUniqueSuccessfulNumber() {
                                return uniqueSuccessfulNumber.getText();
                }

                public String getUniqueFailedNumber() {
                                return uniqueFailedNumber.getText();
                }

                public String getInitiatedTransactions() {
                                return  initiatedTransactions.getText();
                }
                
                public String getRevertedTransactions() {
                                return revertedTransactions.getText();
                }

                public String getSuccessfulTransactions() {
                                return successfulTransactions.getText();
                }

                public String getSuccessfulPercentage() {
                                return successfulPercentage.getText();
                }

                public String getAvgAmountperTransaction() {
                                return avgAmountperTransaction.getText();
                }

                /**
                 * Reads the "merchantDetails" sheet of Test Data excel and select the time period
                 * @param timePeriodRow Row number of excel sheet to be filled in
                 * @return Returns the Excel sheet data which was filled
                 */
                public TestDataReader SelectTimePeriod(int timePeriodRow) 
                {
                                TestDataReader data = new TestDataReader(testConfig,"merchantDetails");
                                String value = "";
                                value = data.GetData(timePeriodRow, "Label");
                                Element.selectVisibleText(testConfig, dropBox, value, "Time period selected selected");

                                return data;
                } 


                /**
                 * Reads the URL from "merchantDetails" sheet and verifies if it matches the current browser URL
                 * @param timePeriodRow row number of excel sheet to read
                 * @return if current URL matches the expected one
                 */
                public void VerifyRedirectedURL(int timePeriodRow)
                {
                                TestDataReader timePeriod = new TestDataReader(testConfig, "merchantDetails"); 
                                String expectedURL = timePeriod.GetData(timePeriodRow, "URL");
                                Browser.verifyURL(testConfig, expectedURL);
                }

                /*public TransactionsPage ClickViewAll() {
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, viewAll, "View All");
								return new TransactionsPage(testConfig);
                }*/
                
                public void ClickViewAll() {
                    Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                    Element.click(testConfig, viewAll, "View All");
					
    }
                public TransactionsPage ClickViewAllTxn() {
                    Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                    Element.click(testConfig, viewAll, "View All");
                    return new TransactionsPage(testConfig);
					
    }
                

                public EmailInvoicePage clickThroughIVR() {
                                Element.mouseMove(testConfig, newTransactionTab, "New Transaction Tab");
                                Element.click(testConfig, throughIVR, "Through IVR");
                                return new EmailInvoicePage(testConfig);
                }
                
                public EmailInvoicePage ClickThroughEmailInvoice() {
                                Element.mouseMove(testConfig, newTransactionTab, "New Transaction Tab");
                                Element.click(testConfig, throughEmailInvoice, "Through Email Invoice");
                                return new EmailInvoicePage(testConfig);
                }
                public CapturePage ClickRequestCapture() {
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, requestCapture, "Request Capture");
                                return new CapturePage(testConfig);
                }
                public RefundPage ClickRequestRefund() {
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, requestRefund, "Request Refund");
                                return new RefundPage(testConfig);
                }
                public RequestsPage ClickViewRequest() {
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, viewRequests, "View Requests");
                                return new RequestsPage(testConfig);
                }

                public CancelRefundPage ClickCancelRefund() {
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, requestCancelRefund, "Cancel/Refund");
                                return new CancelRefundPage(testConfig);
                }
                
                public CancelPage ClickCancel() {
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, requestCancel, "Request Cancel");
                                return new CancelPage(testConfig);
                }
                
                
                public NewMerchantLoginPage ClickCreateLogin() {
                                Element.mouseMove(testConfig, settingTab, "Setting Tab");
                                Element.click(testConfig, createLogin, "Create Login");
                                Browser.wait(testConfig, 2);
                                return new NewMerchantLoginPage(testConfig);
                }
                
                public CODVerifyPage ClickCODVerify()
                {
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, codVerify, "COD Verify");
                                Browser.wait(testConfig, 2);
                                return new CODVerifyPage(testConfig);
                }
                
                public CODCancelPage ClickCODCancel()
                {
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, codCancel, "COD Cancel");
                                Browser.wait(testConfig, 2);
                                return new CODCancelPage(testConfig);
                }
                
                public CODSettledPage ClickCODSettel()
                {
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, codSettled, "COD Settle");
                                Browser.wait(testConfig, 2);
                                return new CODSettledPage(testConfig);
                }
                public ViewInvoicesPage ClickViewInvoices()
            	{
            		Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
            		Element.click(testConfig, viewInvoices, "view invoice");
            		Browser.wait(testConfig, 2);
            		return new ViewInvoicesPage(testConfig);
            	}
                
                
                
                /**
                 * Manage login 
                 * @return
                 */
                public MerchantLoginsPage ClickManageLogin() {
                                Element.mouseMove(testConfig, settingTab, "Setting Tab");
                                Element.click(testConfig, manageLogin, "Manage Login");
                                Browser.wait(testConfig, 2);
                                return new MerchantLoginsPage(testConfig);
                }

                public MerchantCheckoutPage ClickCheckoutPage() {
                                Element.mouseMove(testConfig, settingTab, "Setting Tab");
                                Element.click(testConfig, checkoutPage, "Checkout Page");
                                Browser.wait(testConfig, 2);
                                return new MerchantCheckoutPage(testConfig);
                }
                
                public SetLookPage clickAdvCustomizeLink(){
                                Element.mouseMove(testConfig, settingTab, "Setting Tab");
                                Element.click(testConfig, AdvCustomization, "Advance Customization");
                                return new SetLookPage(testConfig);
                }
                
                public PreviewPage clickCustomizeLink(){
                                Element.mouseMove(testConfig, settingTab, "Setting Tab");
                                Element.click(testConfig, customizePage, "Customize your Page");
                                return new PreviewPage(testConfig);
                }
                /**

                 * click Sign out button
                 * @return same page
                 */
                public PayUHomePage clickMerchantSignout()
                {
                                Element.click(testConfig, signOut, "Sign out");
                                return new PayUHomePage(testConfig);
                                
                }
                public void ReconcileFileupload(String fileName) {
                                Element.enterFileName(testConfig, reconcile, fileName, "Reconcile");
                }
                
                
                public void ClickReconcileButton() {
                                Element.click(testConfig, reconcileButton, "Reconcile Button");
                                
                /*           Robot robot = new Robot();
                                Robot robot = new Robot();
                                robot.delay(5000);

                                robot.keyPress(KeyEvent.VK_CLEAR);

                                // press CTRL+S
                                robot.keyPress(KeyEvent.VK_ALT);
                                robot.keyPress(KeyEvent.VK_S);
                                robot.keyRelease(KeyEvent.VK_S);
                                robot.keyRelease(KeyEvent.VK_ALT);

                                // press CTRL+S
                                robot.keyPress(KeyEvent.VK_ALT);
                                robot.keyPress(KeyEvent.VK_O);
                                robot.keyRelease(KeyEvent.VK_O);
                                robot.keyRelease(KeyEvent.VK_ALT);

                                // press CTRL+S
                                robot.keyPress(KeyEvent.VK_ALT);
                                robot.keyPress(KeyEvent.VK_S);
                                robot.keyRelease(KeyEvent.VK_S);
                                robot.keyRelease(KeyEvent.VK_ALT);
                                robot.delay(5000);

                                // Press Enter
                                robot.keyPress(KeyEvent.VK_ENTER);
                                // Release Enter
                                robot.keyRelease(KeyEvent.VK_ENTER);*/         
                }

                @FindBy(xpath = "//form[@id='filter_form']//div[contains(text(), 'Amount Range')]/following-sibling::div")
                private WebElement amounttab;

                @FindBy(name = "mna")
                private WebElement minamt;

                @FindBy(name = "mxa")
                private WebElement maxamt;

                @FindBy(id = "applyFilter")
                private WebElement applyButton;

                @FindBy(css = "div>table>tbody>tr>td:nth-child(7)")
                private WebElement amount;

                @FindBy(linkText = "Amount")
                private WebElement amountsort;

                @FindBy(xpath = "//form[@id='filter_form']//div[contains(text(), 'Order Status:')]/following-sibling::div")
                private WebElement statustab;

                @FindBy(css = "div.floatLeft")
                private WebElement transactionresult;

                @FindBy(id = "All")
                private WebElement allCheckbox;

                @FindBy(id = "Captured")
                private WebElement capturedCheckbox;

                @FindBy(id = "Authorized (awaiting capture)")
                private WebElement authCheckbox;

                @FindBy(id = "Cancelled")
                private WebElement CancelledCheckbox;

                @FindBy(id = "Refunded")
                private WebElement RefundedCheckbox;

                public void Status() {
                                Element.click(testConfig, statustab, "Order Status Tab");
                }

                public void allCheckboxStatus() {
                                Element.click(testConfig, allCheckbox, "'All' Order Status checkbox");
                }

                public void Authstatus() {
                                Element.click(testConfig, authCheckbox, "'Authorized' Order Status checkbox");
                }

                public void Cancelledstatus() {
                                Element.click(testConfig, CancelledCheckbox, "'Cancelled' Order Status checkbox");
                }

                public void Refundedstatus() {
                                Element.click(testConfig, RefundedCheckbox, "'Refunded' Order Status checkbox");
                }

                public void Capturedstatus() {
                                Element.click(testConfig, capturedCheckbox, "'Captured' Order Status checkbox");

                }

                public void AmountTab() {
                                Element.click(testConfig, amounttab, "Amount Range Tab");
                }

                public void SubmitMaxAmount(String maxamount) {
                                Element.enterData(testConfig, maxamt, maxamount,
                                                                "maximum amount");
                }

                public void SubmitMinAmount(String minamount) {
                                Element.enterData(testConfig, minamt, minamount,
                                                                "minimum amount");
                }

                public String FetchAmount() {
                                Element.click(testConfig, amountsort, "Amount Sort");
                                return amount.getText();
                }

                public void apply() {
                                Element.click(testConfig, applyButton, "Apply Filter");
                }

                public String transactionno() {
                                String resultno = transactionresult.getText();
                                String[] num = resultno.split("\\s+");
                                System.out.println(num[5]);
                                return num[5];
                }

                @FindBy(xpath = "//form[@id='filter_form']//div[contains(text(), 'Payment Type:')]/following-sibling::div")
                private WebElement paymenttypetab;
                
                @FindBy(xpath = "//form[@id='filter_form']/div[4]/div")
                private WebElement refundPaymenttypetab;
                
                

                @FindBy(id = "pt_0")
                private WebElement allpaymenttypeCheckbox;

                @FindBy(id = "pt_1")
                private WebElement CCpaymenttypeCheckbox;

                @FindBy(id = "pt_2")
                private WebElement DCpaymenttypeCheckbox;

                @FindBy(id = "pt_3")
                private WebElement EMIpaymenttypeCheckbox;

                @FindBy(id = "pt_4")
                private WebElement NBpaymenttypeCheckbox;

                @FindBy(id = "pt_5")
                private WebElement CODpaymenttypeCheckbox;

                public void paymentType() {
                                Element.click(testConfig, paymenttypetab, "Payment Type Tab");
                }

                public void ALLpaymentType() {
                                Element.click(testConfig, allpaymenttypeCheckbox, "'All' Payment Type checkbox");
                }

                public void CCpaymentType() {
                                Element.click(testConfig, CCpaymenttypeCheckbox, "'CC' Payment Type checkbox");
                }

                public void DCpaymentType() {
                                Element.click(testConfig, DCpaymenttypeCheckbox, "'DC' Payment Type checkbox");
                }

                public void CODpaymentType() {
                                Element.click(testConfig, CODpaymenttypeCheckbox, "'COD' Payment Type checkbox");
                }

                public void NBpaymentType() {
                                Element.click(testConfig, NBpaymenttypeCheckbox, "'NB' Payment Type checkbox");
                }

                public void EMIpaymentType() {
                                Element.click(testConfig, EMIpaymenttypeCheckbox, "'EMI' Payment Type checkbox");
                }

                @FindBy(xpath = "//form[@id='filter_form']//div[contains(text(), 'Unsuccessful Transactions:')]/following-sibling::div")
                private WebElement unsucessfultransaction;

                @FindBy(name = "uss[]")
                private WebElement allunsucessfultransaction;

                @FindBy(id = "Bounced")
                private WebElement BouncedCheckbox;

                @FindBy(id = "Cancelled By User")
                private WebElement usercancelledCheckbox;

                @FindBy(id = "Failed By Bank")
                private WebElement failedbybankCheckbox;

                @FindBy(id = "Dropped")
                private WebElement DroppedCheckbox;

                public void UnsucessfulTransaction() {
                                Element.click(testConfig, unsucessfultransaction,
                                                                "Unsucessful Transactions Tab");

                }

                public void allUnsucessfulTransaction() {

                                Element.click(testConfig, allunsucessfultransaction, "'All' Unsuccessful Transactions checkbox");
                }

                public void BouncedTransaction() {
                                Element.click(testConfig, BouncedCheckbox, "'Bounced' Unsuccessful Transactions checkbox");
                }

                public void failedbybankTransaction() {
                                Element.click(testConfig, failedbybankCheckbox,
                                                                "Check failed by bank Checkbox");
                }

                public void usercancelledTransaction() {
                                Element.click(testConfig, usercancelledCheckbox,
                                                                "Check user cancelled checkbox");
                }

                public void DroppedTransaction() {
                                Element.click(testConfig, DroppedCheckbox, "'Dropped' Unsuccessful Transactions checkbox");
                }

                @FindBy(id = "dateinputto")
                private WebElement dateinputto;

                @FindBy(id = "dateinputfrom")
                private WebElement dateinputfrom;
                
                @FindBy(id = "dateinputtos")
            	private WebElement capturedateinputto;

            	@FindBy(id = "dateinputfroms")
            	private WebElement capturedateinputfrom;

                public void Filldateinputfrom(String datefrom) {
                                JavascriptExecutor js = (JavascriptExecutor) this.testConfig.driver;
                                js.executeScript("document.getElementById('dateinputfrom').value=('"+ datefrom + "');");
                }

                public void Filldateinputto(String dateto) {
                                JavascriptExecutor js = (JavascriptExecutor) this.testConfig.driver;
                                js.executeScript("document.getElementById('dateinputto').value=('"+ dateto + "');");
                }
                
                public void FillCaptureddatefrom(String datefrom) {
            		JavascriptExecutor js = (JavascriptExecutor) this.testConfig.driver;
            		js.executeScript("document.getElementById('dateinputfroms').value=('"+ datefrom + "');");
            	}

            	public void FillCaptureddateto(String dateto) {
            		JavascriptExecutor js = (JavascriptExecutor) this.testConfig.driver;
            		js.executeScript("document.getElementById('dateinputtos').value=('"+ dateto + "');");
            	}

                @FindBy(xpath = "//form[@id='filter_form']//div[contains(text(), 'Edit Fields:')]/following-sibling::div")
                private WebElement editfieldtab;

                @FindBy(id = "edit_field_0")
                private WebElement MerchantNameCheckbox;

                @FindBy(id = "edit_field_1")
                private WebElement TXNidCheckbox;

                @FindBy(id = "edit_field_2")
                private WebElement PaymenttypeCheckbox;

                @FindBy(id = "edit_field_3")
                private WebElement PaymentgatewayCheckbox;

                @FindBy(id = "edit_field_4")
                private WebElement StatusCheckbox;

                @FindBy(id = "edit_field_5")
                private WebElement CustomerNameCheckbox;

                @FindBy(id = "edit_field_6")
                private WebElement LastnameCheckbox;

                @FindBy(id = "edit_field_7")
                private WebElement EmailIDCheckbox;

                @FindBy(id = "edit_field_8")
                private WebElement ErrorcodeCheckbox;

                @FindBy(id = "edit_field_13")
                private WebElement CustomerIPCheckbox;

                @FindBy(id = "edit_field_14")
                private WebElement RequestamountCheckbox;

                @FindBy(id = "edit_field_15")
                private WebElement DiscountCheckbox;

                @FindBy(id = "edit_field_17")
                private WebElement BankrefnoCheckbox;

                @FindBy(id = "edit_field_18")
                private WebElement CardnumberCheckbox;

                @FindBy(id = "edit_field_21")
                private WebElement field2Checkbox;

                @FindBy(id = "edit_field_24")
                private WebElement BankNameCheckbox;

                public void editFields() {
                                Element.click(testConfig, editfieldtab, "Edit Fields Tab");
                }

                @FindBy(xpath = "//form[@id='filter_form']//div[contains(text(), 'Unique:')]/following-sibling::div")
                private WebElement uniquetab;

                @FindBy(xpath = "//form[@id='filter_form']//div[contains(text(), 'Retry:')]/following-sibling::div")
                private WebElement retrytab;

                @FindBy(id = "re_1")
                private WebElement retryCheckbox;

                @FindBy(id = "un_1")
                private WebElement uniqueCheckbox;

                public void uniquetransaction() {
                                Element.click(testConfig, uniquetab, "Unique Tab");
                                Element.click(testConfig, uniqueCheckbox, "'Unique' checkbox");
                }

                public void retrytransaction() {
                                Element.click(testConfig, retrytab, "Retry Tab");
                                Element.click(testConfig, retryCheckbox, "'Not ReAttempts' checkbox");
                }

                @FindBy(xpath = "//form[@id='filter_form']//div[contains(text(), 'Offers:')]/following-sibling::div")
                private WebElement offertab;

                @FindBy(id = "offer_field_0")
                private WebElement offerCheckbox;

                public void offerTab() {
                                Element.click(testConfig, offertab, "Offer Tab");
                                Element.click(testConfig, offerCheckbox, "Offer checkbox");
                }

                public void ClickCodVerify() {
                                Browser.wait(testConfig, 5);
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, codVerify, "COD Verify");
                }
                
                public void ClickCodCancel() {
                                Browser.wait(testConfig, 10);
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, codCancel, "COD Cancel");
                }
                
                public void ClickCodSettled() {
                                Browser.wait(testConfig, 5);
                                Element.mouseMove(testConfig, transactionTab, "Transactions Tab");
                                Element.click(testConfig, codSettled, "COD Settled");
                }
                


                @FindBy(name="qterm")
                private WebElement search;

                @FindBy(css="input[type=\"submit\"]")
                private WebElement clickGo;

                public void SearchTransaction(String transactionId) {
                                Element.enterData(testConfig, search,transactionId, "Transaction id to Search");
                                Element.click(testConfig, clickGo, "Go, to search the transaction");
                                                                }
                
                @FindBy(linkText="Export to Excel")
                private WebElement exportExcel;
                
                public void ClickExportExcelButton()   {
                                Element.click(testConfig, exportExcel, "Export Excel");
                }              
                
                public String transactionRow()
                {
                                Browser.wait(testConfig, 5);
                return   transactionresult.getText();        
                }
                
                @FindBy(linkText="Next")
                private WebElement nextLink;
                
                public boolean ClickNextLink()  {
                
                                Boolean isPass = Element.IsElementDisplayed(testConfig, nextLink);
                                
                                if(isPass) {
                                                Element.click(testConfig, nextLink, "Next Link");
                                return true;
                                
                                }
                                else
                                                return false;

                }
                
                @FindBy(name = "transactions[1]")
                private WebElement Transaction1;
                
                @FindBy(id = "codVerify")
                private WebElement codVerifyButton;
                
                @FindBy(id = "action_button")
                private WebElement codActionButton;
                
                @FindBy(id = "cancel_button")
                private WebElement cancel_button;
                
                @FindBy(xpath = "//div[@id='general_popup_content']/b[4]")
                private WebElement confirmbuttonqueued;
                
                public String selectTransactionVerifyAction()
                {
                                Element.click(testConfig, Transaction1, "Transaction");
                                Element.click(testConfig, codVerifyButton, "Cod Verify Button");
                                Element.click(testConfig, codActionButton, "Cod Verify Action Button");
                                Boolean isPass = Element.IsElementDisplayed(testConfig, confirmbuttonqueued);
                                                
                                if(isPass) {
                                                Element.click(testConfig, codActionButton, "Cod Verify Action Confirm Button");
                                }
                                
                                String Reqid =Element.getPageElement(testConfig, How.css, "b").getText();
                                Element.click(testConfig, cancel_button, "Cancel button");
                                return Reqid;
                }
                                
                @FindBy(name = "request_ref_id")
                private WebElement RequestRefId;

                @FindBy(css = "#filter_form > div.overflowHidden > div.dropdown > span > input[type='submit']")
                private WebElement DownloadButton;
                
                @FindBy(linkText = "Refresh cod_verify status now!")
                private WebElement refreshlink;
                
                @FindBy(linkText = "Download summary")
                private WebElement downloadsummary;
                
                @FindBy(linkText = "Refresh cod_settled status now!")
                private WebElement refreshlinkSettled;
                

                @FindBy(linkText = "Refresh cod_cancel status now!")
                private WebElement refreshlinkCancel;
                

                public void downloadSettledSummary(String ReqId) {
                                Element.enterData(testConfig, RequestRefId, ReqId,"Entering the value of reqid");
                                Element.click(testConfig, DownloadButton, "Download Button");

                                Browser.wait(testConfig, 20);
                                
                                int retries = 5;
                                while(Element.IsElementDisplayed(testConfig, refreshlinkSettled) && retries > 0)
                                {
                                                Element.click(testConfig, refreshlinkSettled, "Refresh Link Settled");
                                                Browser.wait(testConfig,50);
                                                retries --;
                                }
                                
                                if(retries == 0) 
                                {
                                testConfig.logFail("Could not download settled summary for reference id - " + ReqId);
                                }
                                
                                Element.click(testConfig, downloadsummary, "Download Summary");
                }

                public void downloadVerifySummary(String ReqId) {
                                Element.enterData(testConfig, RequestRefId, ReqId,"Entering the value of reqid");
                                Element.click(testConfig, DownloadButton, "Download Button");

                                Browser.wait(testConfig, 20);
                                
                                int retries = 5;
                                while(Element.IsElementDisplayed(testConfig, refreshlink) && retries > 0)
                                {
                                                Element.click(testConfig, refreshlink, "Refresh Link");
                                                Browser.wait(testConfig,50);
                                                retries --;
                                }
                                
                                if(retries == 0) 
                                {
                                testConfig.logFail("Could not download verify summary for reference id - " + ReqId);
                                }
                                
                                Element.click(testConfig, downloadsummary, "Download Summary");
                }

                public void downloadCancelSummary(String ReqId) {
                                Element.enterData(testConfig, RequestRefId, ReqId,"Entering the value of reqid");
                                Element.click(testConfig, DownloadButton, "Download Button");

                                Browser.wait(testConfig, 20);
                                
                                int retries = 5;
                                while(Element.IsElementDisplayed(testConfig, refreshlinkCancel) && retries > 0)
                                {
                                                Element.click(testConfig, refreshlinkCancel, "Refresh Link cancel");
                                                Browser.wait(testConfig,50);
                                                retries --;
                                }
                                
                                if(retries == 0) 
                                {
                                testConfig.logFail("Could not download cancel summary for reference id - " + ReqId);
                                }
                                
                                Element.click(testConfig, downloadsummary, "Download Summary");
                }

                
                @FindBy(id = "codSettled")
                private WebElement codSettledButton;
                

                
                public String selectTransactionSettledAction()
                {
                                Element.click(testConfig, Transaction1, "Transaction");
                                Element.click(testConfig, codSettledButton, "Cod Settled Button");
                                Element.click(testConfig, codActionButton, "Cod Settled Action Button");
                                Boolean isPass = Element.IsElementDisplayed(testConfig, confirmbuttonqueued);
                                                
                                if(isPass) {
                                                Element.click(testConfig, codActionButton, "Cod Settled Action Confirm Button");
                                }
                                
                                String Reqid =Element.getPageElement(testConfig, How.css, "b").getText();
                                Element.click(testConfig, cancel_button, "cancel_button");
                                return Reqid;
                }
                
                @FindBy(id = "codCancel")
                private WebElement codCancelButton;
                
                public String selectTransactionCancelAction()
                {
                                Element.click(testConfig, Transaction1, "Transaction");
                                Element.click(testConfig, codCancelButton, "Cod Cancel Button");
                                Element.click(testConfig, codActionButton, "Cod Cancel Action Button");
                                Boolean isPass = Element.IsElementDisplayed(testConfig, confirmbuttonqueued);
                                                
                                if(isPass) {
                                                Element.click(testConfig, codActionButton, "Cod Cancel Action Confirm Button");
                                }
                                
                                String Reqid =Element.getPageElement(testConfig, How.css, "b").getText();
                                Element.click(testConfig, cancel_button, "cancel_button");
                                return Reqid;
                }
                
                public String VerifyExportExcelButton()   {
              		 return(exportExcel.getText());
              	 }
              	 
              	 public boolean VerifyExportExcelNot()   {
              		 Boolean isPass = Element.IsElementDisplayed(testConfig, exportExcel);

              		 if(isPass) {
              			return false;
              		 }
              		 else
              			 return true;
              		 }
                
                public void SearchTransactionMerchantPanel(String transactionId) {
                
                                int retries = 5;
                                do{
                                Element.enterData(testConfig, search,transactionId, "Search the transaction");
                                Element.click(testConfig, clickGo, "Go, to search the transaction");
                                Browser.wait(testConfig, 50);
                                retries --;
                                if(retries == 0 &&  transactionno().equals("0")) 
                                                {
                                                testConfig.logFail("Could not find the transaction with transaction id- " + transactionId);
                                                break;
                                                }
                                }
                                while(transactionno().equals("0"));
                }
                
                @FindBy(id = "exportExcelFile")
                private WebElement exportExcelFileName;
                
                public void ClickExportExcelViewAll(String FileName)  {
                                Element.click(testConfig, exportExcel, "Export Excel");
                                Element.enterData(testConfig, exportExcelFileName ,FileName, "Enter the File Name");
                                Element.click(testConfig, codActionButton, "Cod Action Button");
                                
                }
                
                @FindBy(css = "span.dwnload_value")
                private WebElement DownloadExcelfile;
                
                @FindBy(css = "li.pndng_excl")
                private WebElement Downloadfile;
                
                public Boolean PendingWaitDownloadExcel() {
                
                                ClickViewAll();
                                Browser.wait(testConfig, 20);
                                Element.mouseMove(testConfig, DownloadExcelfile,"Select Download Excel file Tab");
                                return Element.IsElementDisplayed(testConfig, Downloadfile);


                }
}
