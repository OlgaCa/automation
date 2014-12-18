package Test.AdminPanel.TestMerchantTransaction;

import org.openqa.selenium.WebElement;

import PageObject.AdminPanel.Payments.PaymentOptions.MerchantCCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantCCTab.MerCardType;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantDCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantEMITab;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantNBTab;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;
import Utils.TestDataReader;

public class TransactionMerchantHelper extends TransactionHelper {

	public MerchantCCTab merCCTab;
	public MerchantDCTab merDCTab;
	public MerchantNBTab merNBTab;
	public MerchantEMITab merEMITab;

	public TransactionMerchantHelper(Config testConfig, Boolean isSeamless) {
		super(testConfig, isSeamless);
	}

	/**
	 * Do Merchant Test transaction using the given details (when on Transaction
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
	public Object DoTestMerchantTransaction(int transactionRow,
			int paymentTypeRow, int cardDetailsRow,
			ExpectedResponsePage expResponse) {
		return DoTestMerchantTransaction(transactionRow, paymentTypeRow,
				cardDetailsRow, -1, expResponse);

	}

	/**
	 * Do the Merchant test transaction using the given details (when on
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
	Object DoTestMerchantTransaction(int transactionRow, int paymentTypeRow,
			int cardDetailsRow, int codRow, ExpectedResponsePage expResponse) {
		Object returnPage = null;
		this.transactionRow = transactionRow;
		this.paymentTypeRow = paymentTypeRow;

		// Verify if the required ibibo code is present or not
		VerifyIbiboCodePresentForMerchant(
				this.transactionData.GetCurrentEnvironmentData(
						this.transactionRow, "merchantid"), this.paymentTypeRow);

		// will be used to fill up user credentials (of test transaction) in
		// case of stored card feature
		testConfig.putRunTimeProperty("bankcode",
				this.paymentTypeData.GetData(paymentTypeRow, "bankcode"));

		try {
			// Fill Transaction Details
			transactionData = trans.FillTransactionDetails(transactionRow);
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}

		try {
			// get the payment options page
			merpayment = trans.MerTransactionSubmit();
			merchantName = transactionData.GetData(transactionRow, "Comments");
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}

		// fill the payment details
		returnPage = DoMerchantPayment(paymentTypeRow, cardDetailsRow, codRow,
				expResponse);
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

	Object DoMerchantPayment(int paymentTypeRow, int cardDetailsRow,
			int codRow, ExpectedResponsePage expResponse) {
		try {
			Object returnPage = null;
			this.paymentTypeRow = paymentTypeRow;

			String mode = paymentTypeData.GetData(paymentTypeRow, "mode")
					.toLowerCase();
			switch (mode) {
			case "creditcard": {
				merCCTab = merpayment.clickCCTab();

				String bankcode = paymentTypeData.GetData(paymentTypeRow,
						"bankcode").toLowerCase();
				String pgType = paymentTypeData.GetData(paymentTypeRow,
						"PG_TYPE");
				switch (bankcode) {
				case "amex":
					merCCTab.chooseCreditCardType(MerCardType.Amex);
					break;
				case "cc":
					merCCTab.chooseCreditCardType(MerCardType.MasterCard);
					cardDetailsData = merCCTab.FillCardDetails(cardDetailsRow);
					break;

				case "dinr":
					merCCTab.chooseCreditCardType(MerCardType.Diners);
					if (pgType.contentEquals("CITI")) {
						merCCTab.FillCardDetails(cardDetailsRow);

					}

					break;
				}

			}
				break;

			case "debitcard": {
				merDCTab = merpayment.clickDCTab();

				String debitCardName = paymentTypeData.GetData(paymentTypeRow,
						"Bank Name");
				merDCTab.ChoseDebitCard(debitCardName);

				String cardDetailsToBeFilled = paymentTypeData.GetData(
						paymentTypeRow, "CardDetailsToBeFilled");
				switch (cardDetailsToBeFilled) {
				case "1":
					cardDetailsData = merDCTab
							.FillDebitCardDetails(cardDetailsRow);
					break;
				case "18":
					cardDetailsData = merDCTab
							.FillDebitCardDetails(cardDetailsRow);
					break;
				default: // do nothing, it will redirect to bank
					break;
				}
			}
				break;

			case "emi": {
				merEMITab = merpayment.clickEMITab();

				String bank = paymentTypeData
						.GetData(paymentTypeRow, "PG_TYPE");
				;
				String duration = paymentTypeData.GetData(paymentTypeRow,
						"Bank Name");
				merEMITab.SelectMerchantBankAndDuration(bank, duration);

				cardDetailsData = merEMITab.FillEMICardDetails(cardDetailsRow);
			}
				break;

			case "netbanking": {
				merNBTab = merpayment.clickNBTab();

				String bankName = paymentTypeData.GetData(paymentTypeRow,
						"Bank Name");
				String cardDetailsToBeFilled = paymentTypeData.GetData(
						paymentTypeRow, "CardDetailsToBeFilled");

				switch (cardDetailsToBeFilled) {
				case "1":

					merNBTab.SelectMerchantBank(bankName);
					cardDetailsData = merNBTab
							.FillNetbankingCardDetails(cardDetailsRow);

					break;
				default:
					merNBTab.SelectMerchantBank(bankName);

					break;
				}

			}
				break;

			}

			switch (expResponse) {
			case BankRedirectPage:
				if (mode.contentEquals("creditcard")) {
					returnPage = ClickCCPage(expResponse);
				} else if (mode.contentEquals("debitcard")) {

					returnPage = ClickDCPage(expResponse);
				} else if (mode.contentEquals("emi")) {
					returnPage = ClickEMIPage(expResponse);

				} else if (mode.contentEquals("netbanking")) {

					returnPage = ClickNBPage(expResponse);

				}

				break;
			case TryAgainPage:
				if (mode.contentEquals("creditcard")) {
					returnPage = ClickCCPage(expResponse);
				} else if (mode.contentEquals("debitcard")) {

					returnPage = ClickDCPage(expResponse);
				} else if (mode.contentEquals("emi")) {
					returnPage = ClickEMIPage(expResponse);

				} else if (mode.contentEquals("netbanking")) {

					returnPage = ClickNBPage(expResponse);

				}

				break;
			case PaymentOptionsPage:
				if (mode.contentEquals("creditcard")) {
					returnPage = ClickCCPage(expResponse);
				} else if (mode.contentEquals("debitcard")) {

					returnPage = ClickDCPage(expResponse);
				} else if (mode.contentEquals("emi")) {
					returnPage = ClickEMIPage(expResponse);

				} else if (mode.contentEquals("netbanking")) {

					returnPage = ClickNBPage(expResponse);

				}
				break;
			case TestResponsePage:
				if (mode.contentEquals("creditcard")) {
					returnPage = ClickCCPage(expResponse);
				} else if (mode.contentEquals("debitcard")) {

					returnPage = ClickDCPage(expResponse);
				} else if (mode.contentEquals("emi")) {
					returnPage = ClickEMIPage(expResponse);

				} else if (mode.contentEquals("netbanking")) {
					returnPage = ClickNBPage(expResponse);

				}
				break;
			case OfferPage:
				if (mode.contentEquals("creditcard")) {
					returnPage = ClickCCPage(expResponse);
				}
				break;
			default:
				returnPage = null;
				break;
			}

			return returnPage;
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}

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
			returnPage = merCCTab.clickMerchantCCPaymentToGetTestResponsePage();
			break;
		case TryAgainPage:
			returnPage = merCCTab.clickMerchantCCPaymentToGetTryAgainPage();
			break;
		case BankRedirectPage:
			returnPage = merCCTab.clickMerchantCCPaymentToGetBankRedirectPage();
			break;
		case OfferPage:
			returnPage = merCCTab.clickPayNowToGetOfferPage();
			break;
		default:
			returnPage = null;
			break;

		}
		return returnPage;
	}

	/**
	 * click DebitCard tab on payment page and verifies response page
	 * 
	 * @param expResponse
	 *            selects response page
	 * @return response page
	 */
	Object ClickDCPage(ExpectedResponsePage expResponse) {

		Object returnPage = null;
		switch (expResponse) {

		case TestResponsePage:
			returnPage = merDCTab.clickMerchantDCPaymentToGetTestResponsePage();
			break;
		case TryAgainPage:
			returnPage = merDCTab.clickMerchantDCPaymentToGetTryAgainPage();
			break;
		case BankRedirectPage:
			returnPage = merDCTab.clickMerchantDCPaymentToGetBankRedirectPage();
			break;
		case PaymentOptionsPage:
			returnPage = payment;
			break;
		case OfferPage:
			returnPage = merDCTab.clickPayNowToGetOfferPage();
			break;
		default:
			returnPage = null;
			break;

		}
		return returnPage;
	}

	/**
	 * click EMI tab on payment page and verifies response page
	 * 
	 * @param expResponse
	 *            selects response page
	 * @return response page
	 */
	public Object ClickEMIPage(ExpectedResponsePage expResponse) {

		Object returnPage = null;
		switch (expResponse) {
		case TestResponsePage:
			returnPage = merEMITab
					.clickMerchantEMIPaymentToGetTestResponsePage();
			break;
		case TryAgainPage:
			returnPage = merEMITab.clickMerchantEMIPaymentToGetTryAgainPage();
			break;
		case PaymentOptionsPage:
			returnPage = payment;
			break;
		case OfferPage:
			returnPage = merEMITab.clickPayNowToGetOfferPage();
			break;
		case BankRedirectPage:
			returnPage = merEMITab
					.clickMerchantEMIPaymentToGetBankRedirectPage();
			break;
		default:
			returnPage = null;
			break;

		}
		return returnPage;

	}

	/**
	 * click NetBanking tab on payment page and verifies response page
	 * 
	 * @param expResponse
	 *            selects response page
	 * @return response page
	 */
	Object ClickNBPage(ExpectedResponsePage expResponse) {

		Object returnPage = null;
		switch (expResponse) {
		case BankRedirectPage:
			returnPage = merNBTab.clickMerchantNBPaymentToGetBankRedirectPage();
			break;
		case OfferPage:
			returnPage = merNBTab.clickPayNowToGetOfferPage();
			break;
		case TestResponsePage:
			returnPage = merNBTab.clickMerchantNBPaymentToGetTestResponsePage();
			break;
		case TryAgainPage:
			returnPage = merNBTab.clickMerchantNBPaymentToGetTryAgainPage();
			break;
		default:
			returnPage = null;
			break;

		}
		return returnPage;

	}

	public Object GetMerchantPaymentOptionsPage(int transactionRow,
			PaymentMode mode) {
		GetTestMerchantTransactionPage();
		this.transactionRow = transactionRow;
		try {
			transactionData = trans.FillTransactionDetails(transactionRow);
			merpayment = trans.MerTransactionSubmit();
			merchantName = transactionData.GetData(transactionRow, "Comments");

			Object MerchantPaymentOption = null;
			switch (mode) {
			case creditcard:
				MerchantPaymentOption = merpayment.clickCCTab();
				break;
			case debitcard:
				MerchantPaymentOption = merpayment.clickDCTab();
				break;
			case emi:
				MerchantPaymentOption = merpayment.clickEMITab();
				break;
			case netbanking:
				MerchantPaymentOption = merpayment.clickNBTab();
				break;
			default:
				break;
			}
			cardDetailsData = new TestDataReader(testConfig, "CardDetails");
			return MerchantPaymentOption;
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}
	}

	public TestResponsePage autoGetResponse(Config tConfig) {
		return new TestResponsePage(tConfig);
	}

	/**
	 * Do the Merchant test transaction using the given details with save card(when on
	 * Transaction Page)
	 * 
	 * @param transactionRow
	 *            row number of 'TransactionDetails' sheet
	 * @param paymentTypeRow
	 *            row number of 'PaymentType' sheet
	 * @param cardDetailsRow
	 *            row number of 'CardDetails' sheet
	 * @param StoreCardDetailsRow
	 *            row number of 'StoreCard' sheet
	 * @param expResponse
	 *            expected response page, one of the enum values
	 * @return Expected response page
	 */

	/**
	 * Do the Merchant test transaction using the given details with save card(when on
	 * Transaction Page)
	 * 
	 * @param transactionRow
	 *            row number of 'TransactionDetails' sheet
	 * @param paymentTypeRow
	 *            row number of 'PaymentType' sheet
	 * @param cardDetailsRow
	 *            row number of 'CardDetails' sheet
	 * @param StoreCardDetailsRow
	 *            row number of 'StoreCard' sheet
	 * @param expResponse
	 *            expected response page, one of the enum values
	 * @return Expected response page
	 */

	Object DoTestMerchantTransactionwithStoreCard(int transactionRow,
			int paymentTypeRow, int cardDetailsRow, int storeCardRow,boolean isSavedCard,
			ExpectedResponsePage expResponse)  {
		Object returnPage = null;
		this.transactionRow = transactionRow;
		this.paymentTypeRow = paymentTypeRow;

		// Verify if the required ibibo code is present or not
		VerifyIbiboCodePresentForMerchant(
				this.transactionData.GetCurrentEnvironmentData(
						this.transactionRow, "merchantid"), this.paymentTypeRow);
		// will be used to fill up user credentials (of test transaction) in
		// case of stored card feature
		testConfig.putRunTimeProperty("bankcode",
				this.paymentTypeData.GetData(paymentTypeRow, "bankcode"));
		try {
			// Fill Transaction Details
			transactionData = trans.FillTransactionDetails(transactionRow);
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}

		try {
			// get the payment options page
			merpayment = trans.MerTransactionSubmit();
			merchantName = transactionData.GetData(transactionRow, "Comments");
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}
		// fill the payment details
		if (testConfig.getRunTimeProperty("UseStoredDetails") != null) {
			returnPage = DoMerchantPaymentandSaveCard(paymentTypeRow,
					cardDetailsRow, storeCardRow, isSavedCard,expResponse);
			return returnPage;	
		}
		returnPage = DoMerchantPayment(paymentTypeRow, cardDetailsRow, -1,
				expResponse);
		return returnPage;
	}

	/**
	 * Do the Merchant Payment using the given details with save card(when on
	 * Transaction Page)
	 * 
	 * @param transactionRow
	 *            row number of 'TransactionDetails' sheet
	 * @param paymentTypeRow
	 *            row number of 'PaymentType' sheet
	 * @param cardDetailsRow
	 *            row number of 'CardDetails' sheet
	 * @param StoreCardDetailsRow
	 *            row number of 'StoreCard' sheet
	 * @param expResponse
	 *            expected response page, one of the enum values
	 * @return Expected response page
	 */

	Object DoMerchantPaymentandSaveCard(int paymentTypeRow, int cardDetailsRow,
			int storeCardDetailsRow, boolean isSavedCard,ExpectedResponsePage expResponse)
			 {
			String value="";
		try {
			Object returnPage = null;
			this.paymentTypeRow = paymentTypeRow;
			if (testConfig.getRunTimeProperty("RemoveStoreCard").equals("true")) {
				RemoveExistingStoreCards();
			}
			String mode = paymentTypeData.GetData(paymentTypeRow, "mode")
					.toLowerCase();
			switch (mode) {
			
			case "creditcard": {
			merCCTab = merpayment.clickCCTab();
				
				String bankcode = paymentTypeData.GetData(paymentTypeRow,
						"bankcode").toLowerCase();
				String pgType = paymentTypeData.GetData(paymentTypeRow,
						"PG_TYPE");
				switch (bankcode) {
				case "amex":
					merCCTab.chooseCreditCardType(MerCardType.Amex);
					if(isSavedCard){
						cardDetailsData = merCCTab.FillSavedCardDetails(storeCardDetailsRow);
						break;
					}if (storeCardDetailsRow !=-1)
					storeCardDetailsData = merCCTab
							.FillStoreCardDetails(storeCardDetailsRow);
					cardDetailsData = merCCTab.FillCardDetails(cardDetailsRow);
					break;

				case "cc":
					merCCTab.chooseCreditCardType(MerCardType.MasterCard);
					if(isSavedCard){
						cardDetailsData = merCCTab.FillSavedCardDetails(storeCardDetailsRow);
						break;
					}
					if (storeCardDetailsRow !=-1)
					storeCardDetailsData = merCCTab
							.FillStoreCardDetails(storeCardDetailsRow);
					cardDetailsData = merCCTab.FillCardDetails(cardDetailsRow);
					break;
				case "dinr":
					merCCTab.chooseCreditCardType(MerCardType.Diners);
					if(isSavedCard){
						cardDetailsData = merCCTab.FillSavedCardDetails(storeCardDetailsRow);
						break;
					}if (pgType.contentEquals("CITI")) {
						if (storeCardDetailsRow !=-1)
						merCCTab.FillStoreCardDetails(storeCardDetailsRow);
						merCCTab.FillCardDetails(cardDetailsRow);
					}
					break;
				}
			}
				break;
			case "debitcard": {
				merDCTab = merpayment.clickDCTab();
				
				String debitCardName = paymentTypeData.GetData(paymentTypeRow,
						"Bank Name");
				merDCTab.ChoseDebitCard(debitCardName);
				if(isSavedCard){
					cardDetailsData = merDCTab.FillSavedCardDetails(storeCardDetailsRow);
					break;
				}
				String cardDetailsToBeFilled = paymentTypeData.GetData(
						paymentTypeRow, "CardDetailsToBeFilled");
				switch (cardDetailsToBeFilled) {
				case "1":
					if (storeCardDetailsRow !=-1)
						storeCardDetailsData = merDCTab.FillStoreCardDetails(storeCardDetailsRow);
					cardDetailsData = merDCTab.FillDebitCardDetails(cardDetailsRow);
					break;
				case "18":
					if (storeCardDetailsRow !=-1)
						storeCardDetailsData = merDCTab.FillStoreCardDetails(storeCardDetailsRow);
					cardDetailsData = merDCTab.FillDebitCardDetails(cardDetailsRow);
					break;
				default: // do nothing, it will redirect to bank
					break;
				}
			}
				break;
			}
			switch (expResponse) {
			case BankRedirectPage:
				if (mode.contentEquals("creditcard")) {
					returnPage = ClickCCPage(expResponse);
				} else if (mode.contentEquals("debitcard")) {
					returnPage = ClickDCPage(expResponse);
				}
				break;
			case TryAgainPage:
				if (mode.contentEquals("creditcard")) {
					returnPage = ClickCCPage(expResponse);
				} else if (mode.contentEquals("debitcard")) {
					returnPage = ClickDCPage(expResponse);
				}
				break;
			case PaymentOptionsPage:
				if (mode.contentEquals("creditcard")) {
					returnPage = ClickCCPage(expResponse);
				} else if (mode.contentEquals("debitcard")) {
					returnPage = ClickDCPage(expResponse);
				} 
				break;
			case TestResponsePage:
				if (mode.contentEquals("creditcard")) {
					returnPage = ClickCCPage(expResponse);
				} else if (mode.contentEquals("debitcard")) {
					returnPage = ClickDCPage(expResponse);
				}
				break;
			case OfferPage:
				if (mode.contentEquals("creditcard")) {
					returnPage = ClickCCPage(expResponse);
				}
				break;
			default:
				returnPage = null;
				break;
			}
			return returnPage;
		} catch (Exception e) {
			testConfig.logException(e);
			System.out.println(e.getStackTrace());
			throw e;
		}
	}
	
	/* 
	 * Removes Store Cards from CC and DC
	 */
	public void RemoveExistingStoreCards() {
		merCCTab = merpayment.clickCCTab();
		merCCTab.removeCCStoredCard();
		merDCTab = merpayment.clickDCTab();
		merDCTab.removeDCStoredCard();
	}
	public void verifyStoreCard(int transactionRow,int paymentTypeRow,int storecardRow,boolean verifySavedCard){
		this.transactionRow = transactionRow;
		this.paymentTypeRow=paymentTypeRow;
		WebElement SavedCard=null;
		//Verify if the required ibibo code is present or not
		VerifyIbiboCodePresentForMerchant(this.transactionData.GetCurrentEnvironmentData(this.transactionRow, "merchantid"), paymentTypeRow);
		GetTestMerchantTransactionPage();
		try {
			// Fill Transaction Details
			transactionData = trans.FillTransactionDetails(transactionRow);
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}

		try {
			// get the payment options page
			merpayment = trans.MerTransactionSubmit();
			merchantName = transactionData.GetData(transactionRow, "Comments");
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}

		String mode = paymentTypeData.GetData(paymentTypeRow, "mode").toLowerCase();
		switch(mode)
		{
		case "creditcard": 
			merCCTab = merpayment.clickCCTab();
			if (verifySavedCard){
				TestDataReader data = new TestDataReader(testConfig,"StoreCard");
				String value = "";
				value = data.GetData(storecardRow, "SavedCardName");
				SavedCard=merCCTab.selectStoredCard(value);
				if (SavedCard!=null){
					testConfig.logPass("Saved Card "+value+" exists");
				}
				}
			else
			merCCTab.verifyStoreCardDoesNotExists();
			break;

		case "debitcard": 
			merDCTab = merpayment.clickDCTab();
			if (verifySavedCard){
				TestDataReader data = new TestDataReader(testConfig,"StoreCard");
				String value = "";
				value = data.GetData(storecardRow, "SavedCardName");
				SavedCard=merDCTab.selectStoredCard(value);
				if (SavedCard!=null){
					testConfig.logPass("Saved Card "+value+" exists");
				}
			}
			else
				merDCTab.verifyStoreCardDoesNotExists();
			break;
		}
		
}

}
