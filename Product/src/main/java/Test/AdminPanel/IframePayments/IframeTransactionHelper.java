package Test.AdminPanel.IframePayments;

import PageObject.AdminPanel.Payments.IframePaymentOptions.IframeCCTab;
import PageObject.AdminPanel.Payments.IframePaymentOptions.IframeDCTab;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;
import Utils.Helper;
import Utils.TestDataReader;

public class IframeTransactionHelper extends TransactionHelper{
	
	public Config testConfig;
	
	public IframeCCTab iframeCCTab;
	
	public IframeDCTab iframeDCTab;

	public IframeTransactionHelper(Config testConfig, Boolean isSeamless)
	{
		super(testConfig, isSeamless);
		this.testConfig=super.testConfig;
	}

	public void GetTestMerchantTransactionPage() {
		try {
			home.navigateToAdminHome();
			trans = home.ClickTestMerchantTransaction();
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}
	}
	
	/**
	 * Do Iframe Test transaction using the given details (when on Transaction
	 * Page)
	 * 
	 * @param transactionRow
	 *            row number of 'TransactionDetails' sheet
	 * @param paymentTypeRow
	 *            row number of 'PaymentType' sheet
	 * @param cardDetailsRow
	 *            row number of 'CardDetails' sheet
	 * @param expResponse
	 *            expected response page, one of the emum values
	 * @return Expected response page
	 */
	public Object DoIframeTestTransaction(int transactionRow, int paymentTypeRow, int cardDetailsRow, ExpectedResponsePage expResponse) 
	{
		return DoIframeTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, -1, expResponse);

	}
	/**
	 * Do the Iframe test transaction using the given details (when on
	 * Transaction Page)
	 * 
	 * @param transactionRow
	 *            row number of 'TransactionDetails' sheet
	 * @param paymentTypeRow
	 *            row number of 'PaymentType' sheet
	 * @param cardDetailsRow
	 *            row number of 'CardDetails' sheet
	 * @param expResponse
	 *            expected response page, one of the emum values
	 * @return Expected response page
	 */
	Object DoIframeTestTransaction(int transactionRow, int paymentTypeRow, int cardDetailsRow, int codRow, ExpectedResponsePage expResponse) 
	{
		Object returnPage = null;
		this.transactionRow = transactionRow;
		this.paymentTypeRow = paymentTypeRow;

		// Verify if the required ibibo code is present or not
		VerifyIbiboCodePresentForMerchant(this.transactionData.GetCurrentEnvironmentData(this.transactionRow, "merchantid"), this.paymentTypeRow);
		transactionData = trans.FillTransactionDetails(transactionRow);
		try {
			// Fill Transaction Details
			transactionData = trans.FillTransactionDetails(transactionRow);
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}
           
		try {
			// get the payment options page
			iframePayment = trans.IframeTransactionSubmit();
			merchantName = transactionData.GetData(transactionRow, "Comments");
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}

		// fill the payment details
		returnPage = DoIframePayment(paymentTypeRow, cardDetailsRow, codRow, expResponse);
		return returnPage;
	}
	/**
	 * Fill the payment details (when on payment page)
	 * 
	 * @param paymentTypeRow
	 *            row number of 'PaymentType' sheet
	 * @param cardDetailsRow
	 *            row number of 'CardDetails' sheet
	 * @param codRow
	 *            row number of 'COD' sheet
	 * @param expResponse
	 *            expected response page, one of the emum values
	 * @return Expected response page
	 */

			Object DoIframePayment(int paymentTypeRow, int cardDetailsRow, int codRow, ExpectedResponsePage expResponse) {
			{
				Object returnPage = null;
				this.paymentTypeRow = paymentTypeRow;

				String mode = paymentTypeData.GetData(paymentTypeRow, "mode").toLowerCase();
				switch (mode) {
				case "creditcard": {
					iframeCCTab = iframePayment.clickCCTab();

					cardDetailsData = iframeCCTab.FillCardDetails(cardDetailsRow);
				}
				break;
				
				case "debitcard": {
					iframeDCTab = iframePayment.clickDCTab();

					cardDetailsData = iframeDCTab.FillCardDetails(cardDetailsRow);
					break;
				}
				
				}

				switch (expResponse) {
				case BankRedirectPage:
					if (mode.contentEquals("creditcard")) {
						returnPage = ClickCCPage(expResponse);
					} 
					break;
				case TryAgainPage:
					if (mode.contentEquals("creditcard")) {
						returnPage = ClickCCPage(expResponse);
					} 
					break;
				case PaymentOptionsPage:
					if (mode.contentEquals("creditcard")) {
						returnPage = ClickCCPage(expResponse);
					} 					break;
				case TestResponsePage:
					if (mode.contentEquals("creditcard")) {
						returnPage = ClickCCPage(expResponse);
					} 
					if (mode.contentEquals("debitcard")) {
						returnPage = ClickDCPage(expResponse);
					} break;
				case OfferPage:
					if (mode.contentEquals("creditcard")) {
						returnPage = ClickCCPage(expResponse);
					}
					break;
				case IframeTab:
					if(mode.contentEquals("debitcard"))
						returnPage=iframeDCTab.clickOnMakepayment();
					if(mode.contentEquals("creditcard"))
						returnPage=iframeCCTab.clickIframeCCPayment();
					break;
				default:
					returnPage = null;
					break;
				}

				return returnPage;
			} 
		}
	
	/**
	 * click CreditCard tab on payment page and verifies response page
	 * 
	 * @param expResponse
	 *            selects response page
	 * @return response page
	 */
	Object ClickCCPage(ExpectedResponsePage expResponse) {

		Object returnPage = null;
		switch (expResponse) {

		case TestResponsePage:
			returnPage = iframeCCTab.clickIframeCCPaymentToGetTestResponsePage();
			break;
		default:
			returnPage = null;
			break;

		}
		return returnPage;
	}
	
	Object ClickDCPage(ExpectedResponsePage expResponse) {

		Object returnPage = null;
		switch (expResponse) {

		case TestResponsePage:
			returnPage = iframeDCTab.clickIframeDCPaymentToGetTestResponsePage();
			break;
		default:
			returnPage = null;
			break;

		}
		return returnPage;
	}

	void enterCCNumberAndVerifyCardType(int row,TestDataReader transactionDetails){
		String strCardNumber=transactionDetails.GetData(row, "CC");
		String strCardType=transactionDetails.GetData(row, "Card_Type");
		iframeCCTab.enterCCNumber(strCardNumber);
		Helper.compareEquals(testConfig, "Card Type", strCardType, iframeCCTab.getCardType());
		
	}
	void enterDCNumberAndVerifyCardType(int row,TestDataReader transactionDetails){
		String strCardNumber=transactionDetails.GetData(row, "CC");
		String strCardType=transactionDetails.GetData(row, "Card_Type");
		iframeDCTab.enterDCNumber(strCardNumber);
		Helper.compareEquals(testConfig, "Card Type", strCardType, iframeDCTab.getCardType());
		
	}

}