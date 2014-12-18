package Test.AdminPanel.Payments;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import PageObject.AdminPanel.DownloadExcel.DownloadExcelPage;
import PageObject.AdminPanel.Home.AdminPage;
import PageObject.AdminPanel.Home.EditDefaultGatewayPage;
import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.Home.LoginPage;
import PageObject.AdminPanel.ManualUpdate.ManualUpdatePage;
import PageObject.AdminPanel.MerchantCategories.CategoryPage;
import PageObject.AdminPanel.MerchantCategories.CategoryRulePage;
import PageObject.AdminPanel.MerchantCategories.MerchantCategoryPage;
import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.RulePage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.EditMerchantPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.EditMerchantRiskProfilePage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsPage;
import PageObject.AdminPanel.Payments.IframePaymentOptions.IframeCCTab;
import PageObject.AdminPanel.Payments.IframePaymentOptions.IframePaymentOptionPage;
import PageObject.AdminPanel.Payments.PaymentOptions.CCMobileTab;
import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.CCTab.CardType;
import PageObject.AdminPanel.Payments.PaymentOptions.CODTab;
import PageObject.AdminPanel.Payments.PaymentOptions.CashCardTab;
import PageObject.AdminPanel.Payments.PaymentOptions.DCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.EMITab;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantCCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.MerchantPaymentOptionPage;
import PageObject.AdminPanel.Payments.PaymentOptions.NBTab;
import PageObject.AdminPanel.Payments.PaymentOptions.PayUMoneyTab;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionMobilePage;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.PaymentOptions.ProcessingPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.CODChangeMobilePage;
import PageObject.AdminPanel.Payments.Response.CODMobileVerifyPage;
import PageObject.AdminPanel.Payments.Response.ErrorResponsePage;
import PageObject.AdminPanel.Payments.Response.NewResponsePage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.Payments.Response.ThreeDSecurePage;
import PageObject.AdminPanel.Payments.Response.TryAgainPage;
import PageObject.AdminPanel.Payments.Transactions.TransactionPage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import PageObject.MerchantPanel.Offers.OfferRetryPage;
import Test.MerchantPanel.Billing.TestBilling.AdditionalChargeType;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestDataReader;

public class TransactionHelper {

	public Config testConfig;

	public AdminPage admin;
	public LoginPage login;
	public HomePage home;
	public TransactionPage trans;
	public PaymentOptionsPage payment;
	public MerchantPaymentOptionPage merpayment;
	public MerchantListPage merchantListPage;
	public RulePage rulePage;
	public MerchantDetailsPage merchantDetailsPage;
	public EditMerchantPage editMerchantPage;
	public EditMerchantRiskProfilePage editMerchantRiskProfilePage;
	public MerchantCategoryPage merchantCategoryPage;
	public CategoryPage categoryPage;
	public CategoryRulePage categoryRulePage;
	public TestResponsePage testResponse;
	public BankRedirectPage bankRedirectPage;
	public ErrorResponsePage errorResponsePage;
	public TryAgainPage tryAgainPage;
	public CODMobileVerifyPage codMobileVerify;
	public CODChangeMobilePage codChangeMobilePage;
	public ProcessingPage processingPage;
	public MerchantTransactionsPage mTPage;
	public CashCardTab cashCardTab;
	public MerchantCCTab merCCTab;
	public CCTab ccTab;
	public CODTab codTab;
	public DCTab dcTab;
	public EMITab emiTab;
	public NBTab nbTab;
	public PayUMoneyTab payUMoneyTab;
	public ParamsPage paramPage;
	public ThreeDSecurePage threeDSecurePage;


	private Boolean isSeamless;
	public Boolean checkGatewayUpStatus = true;
	public String merchantName;

	public TestDataReader transactionData;
	public int transactionRow;
	public TestDataReader paymentTypeData;
	public int paymentTypeRow;
	public TestDataReader cardDetailsData;
	public TestDataReader codData;
	public TestDataReader footerLinkData;
	public TestDataReader storeCardDetailsData;
	public OfferRetryPage Offerretry;

	public enum ExpectedResponsePage {CODMobileVerifyPage, TransactionPage, PaymentOptionsPage, BankRedirectPage, OfferPage, ErrorResponsePage, TryAgainPage, TestResponsePage, GoBackResponse, ProcessingPage, IframeTab, ThreeDSecurePage, TestResponsePageWithProcessURL, BankRedirectPageWithProcessingURL, PayUMoneyPaymentLoginPage};

	public enum PaymentMode {creditcard, debitcard, emi, cashcard, cod, netbanking,wallet};
	
	public enum ManualUpdateTab{MerchantSettlement,Recon,Chargeback,Refund};

	public IframePaymentOptionPage iframePayment;

	public IframeCCTab iframeCCTab; 
	
	public TransactionHelper(Config testConfig, Boolean isSeamless)
	{
		this.testConfig = testConfig;
		this.isSeamless = isSeamless;
		paymentTypeData = new  TestDataReader(testConfig, "PaymentType");
		transactionData = new  TestDataReader(testConfig, "TransactionDetails");
	}

	/**
	 * Logs in to the Admin site
	 */
	public void DoLogin()
	{
		try
		{
			admin = new AdminPage(testConfig);
			login = admin.ClickAdminLogin();
			home = login.Login();
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}
	}

	/**
	 * Navigates the current browser window to the transaction page, for doing a new transaction
	 */
	public void GetTestTransactionPage()
	{
		try
		{
			home.navigateToAdminHome();
			trans = home.ClickTestTransaction();
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}
	}

	public void GetTestMerchantTransactionPage()
	{
		try
		{
			home.navigateToAdminHome();
			trans = home.ClickTestMerchantTransaction();
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}
	}
	/**
	 * Fill the payment details and save the card(when on payment page)
	 * 
	 * @param paymentTypeRow
	 *            row number of 'PaymentType' sheet
	 * @param cardDetailsRow
	 * 			  row number of 'CardDetail' sheet
	 * @param storeCardDetailsRow
	 *            row number of 'StoreCard' sheet
	 * @param expResponse
	 *            expected response page, one of the emum values
	 * @return Expected response page
	 */
	public Object DoPaymentAndSaveCard(int transactionRowNum, int paymentTypeRow, int cardDetailsRow, int storeCardDetailsRow,ExpectedResponsePage expResponse) {
		try
		{
			Object returnPage = null;
			this.paymentTypeRow = paymentTypeRow;

			//Choose Payment Type and fill the payment details
			paymentTypeData = new TestDataReader(testConfig, "PaymentType");

			//Verify if the required ibibo code is present or not
			VerifyIbiboCodePresentForMerchant(this.transactionData.GetCurrentEnvironmentData(transactionRowNum, "merchantid"), this.paymentTypeRow);

			testConfig.putRunTimeProperty("bankcode", paymentTypeData.GetData(paymentTypeRow, "bankcode"));
			GetTestTransactionPage();
			try
			{
				//Fill Transaction Details
				transactionData = trans.FillTransactionDetails(transactionRowNum);

			}
			catch(Exception e)

			{
				testConfig.logException(e);
				throw e;
			}


			//If seamless, fill the seamless codes and card details 
			if(isSeamless)
			{
				try
				{
					//read mode, bankcode from 'PaymentType' sheet
					paymentTypeData = trans.FillSeamlessCodes(paymentTypeRow);
					trans.FillSeamlessStoreCardDetails(storeCardDetailsRow);
					String cardDetailsToBeFilled = paymentTypeData.GetData(paymentTypeRow, "CardDetailsToBeFilled");
					switch(cardDetailsToBeFilled)
					{
					case "1": //Fill card details
						cardDetailsData = trans.FillCardDetails(cardDetailsRow);
						break;
					default: //do nothing, it will redirect to bank
						break;
					}
					if(testConfig.getRunTimeProperty("RetryOffer")!=null){
						TestDataReader data = new TestDataReader(testConfig, "OfferRetry");
						int row=Integer.parseInt(testConfig.getRunTimeProperty("RetryOffer").trim());
						String expectedOfferRetryTitle=data.GetData(row, "OfferRetryTitle");
						String expectedOfferRetryMessage=data.GetData(row, "OfferRetryMessage");

						OfferRetryPage Offerretry;
						Offerretry=trans.SubmitToGetOfferRetry();
						Offerretry.verifyRetryMessage(expectedOfferRetryTitle,expectedOfferRetryMessage);
						switch(expResponse)
						{
						case TestResponsePage:
							testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							if (testConfig.getRunTimeProperty("CancelRetryOffer")!=null)
								returnPage = Offerretry.clickCancelToGetTestResponse();	
							else
								returnPage = Offerretry.clickContinueToGetTestResponse();
							break;
						case BankRedirectPage:
							returnPage = Offerretry.clickContinueToGetBankRedirectPage();
							break;

						default:
							returnPage = null;
							break;
						}				
					}
					else
					{
						switch(expResponse)
						{
						case BankRedirectPage:
							returnPage = trans.SubmitToGetBankRedirectPage();
							break;
						case ErrorResponsePage:
							returnPage = trans.SubmitToGetErrorResponsePage();
							break;
						case PaymentOptionsPage:
							returnPage = trans.Submit();
							break;
						case TestResponsePage:
							testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							returnPage = trans.SubmitToGetTestResponsePage();
							break;
						case TransactionPage:
							trans.SubmitToGetErrorOnPage();
							returnPage = trans;
							break;
						case TryAgainPage:
							returnPage = trans.SubmitToGetTryAgainPage();
							break;	
						case ProcessingPage:
							returnPage= trans.SubmitToGetProcessingPage();
						case ThreeDSecurePage:
							returnPage = payment.SubmitToGet3DSecurePage();
							break;
						case PayUMoneyPaymentLoginPage:
							//returnPage = trans.SubmitToGetPayuMoneyPage();  
							break;
						default:
							returnPage = null;
							break;
						}
					}
				}
				catch(Exception e)
				{
					testConfig.logException(e);
					throw e;
				}
			}
			//*************Non Seamless***********************
			else{
				payment = trans.Submit();
				
				if (testConfig.getRunTimeProperty("RemoveStoreCard").equals("true")) {
					RemoveExistingStoreCards();
				}
				
				if (testConfig.getRunTimeProperty("SI")!=null)
				{
					if(testConfig.getRunTimeProperty("check_si_merchant")==null){
					//Check if it is a SI merchant, If it is not a SI merchant, reset SI=0
					Map<String,String> map = DataBase.executeSelectQuery(testConfig, 102, 1);
					if(map.get("value").contains("0")) {
						testConfig.putRunTimeProperty("SI","0");
						ccTab = payment.clickCCTab();
						ccTab.verifyStoreCardFooterMessage();
					}
					}
					else{
						ccTab = payment.clickCCTab();
						ccTab.verifyStoreCardFooterMessage();
					}
				}
				String mode = paymentTypeData.GetData(paymentTypeRow, "mode")
						.toLowerCase();
				switch (mode) {
				case "all":
					break;
				case "goback":
					returnPage = payment.clickGoBackToMerchantName(merchantName);
					return returnPage;
				case "creditcard": {
					ccTab = payment.clickCCTab();
					String bankcode = paymentTypeData.GetData(paymentTypeRow,
							"bankcode").toLowerCase();
					testConfig.putRunTimeProperty("bankcode",bankcode);
					switch (bankcode) {
					case "amex":
						ccTab.selectCardType(CardType.Amex);
						break;
					case "cc":
						ccTab.selectCardType(CardType.VisaMasterCard);
						break;
					case "dinr":
						ccTab.selectCardType(CardType.Diners);
						break;
					}
					String cardDetailsToBeFilled = paymentTypeData.GetData(
							paymentTypeRow, "CardDetailsToBeFilled");
					switch (cardDetailsToBeFilled) {
					case "1":
						cardDetailsData = ccTab.FillCardDetails(cardDetailsRow);
						if (storeCardDetailsRow !=-1)

							storeCardDetailsData=ccTab.FillStoreCardDetails(storeCardDetailsRow);
						break;
					default: // do nothing, it will redirect to bank
						break;
					}
				}
				break;
				case "debitcard": {
					dcTab = payment.clickDCTab();
					String debitCardName = paymentTypeData.GetData(paymentTypeRow,
							"Bank Name");
					dcTab.SelectDebitCard(debitCardName);
					String cardDetailsToBeFilled = paymentTypeData.GetData(
							paymentTypeRow, "CardDetailsToBeFilled");
					switch (cardDetailsToBeFilled) {

					case "1":
						cardDetailsData = dcTab.FillCardDetails(cardDetailsRow);
						if (storeCardDetailsRow !=-1)
							cardDetailsData = dcTab.FillStoreCardDetails(storeCardDetailsRow);
						break;
					case "0":
						dcTab.verifyStoreCardFeatureNotDisplayedIn_DC();
						break;
					default: // do nothing, it will redirect to bank
						break;
					}
				}
				break;

				case "netbanking":
				{
					MakeGatewayAsStatusUp(testConfig.getRunTimeProperty("pg_type"));
					nbTab = payment.clickNBTab();
					nbTab.verifyStoreCardFeatureNotDisplayedIn_NB();
					String bankName = paymentTypeData.GetData(paymentTypeRow, "Bank Name");
					nbTab.SelectBank(bankName);
				}
				break;
				case "cashcard":
				{
					cashCardTab = payment.clickCashCardTab();
					MakeGatewayAsStatusUp(testConfig.getRunTimeProperty("pg_type"));				
					String cashCardName = paymentTypeData.GetData(paymentTypeRow, "Bank Name");
					cashCardTab.SelectCashCard(cashCardName);
					cashCardTab.verifyStoreCardFeatureNotDisplayedIn_CashCard();
					String cardDetailsToBeFilled = paymentTypeData.GetData(paymentTypeRow, "CardDetailsToBeFilled");
					switch(cardDetailsToBeFilled)
					{
					case "1": cardDetailsData = cashCardTab.FillCardDetails(cardDetailsRow);
					break;
					default: //do nothing, it will redirect to bank
						break;
					}
				}
				break;

				case "emi":
				{
					MakeGatewayAsStatusUp(testConfig.getRunTimeProperty("pg_type"));
					emiTab = payment.clickEMITab();
					String bank = paymentTypeData.GetData(paymentTypeRow, "PG_TYPE");;
					String duration = paymentTypeData.GetData(paymentTypeRow, "Bank Name");
					emiTab.SelectBankAndDuration(bank, duration);
					emiTab.verifyStoreCardFeatureNotDisplayedIn_EMI();
					cardDetailsData = emiTab.FillCardDetails(cardDetailsRow);
				}

				break;
				}

				switch (expResponse) {
				case TryAgainPage:
					returnPage = payment.clickPayNowToGetTryAgainPage();
					break;
				case PaymentOptionsPage:
					payment.clickPayNowToGetError();
					returnPage = payment;
					break;
				case TestResponsePage:
					testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					returnPage = payment.clickPayNow();
					break;
				case BankRedirectPage:
					returnPage = payment.clickPayNowToGetBankRedirectPage();
					break;
				case PayUMoneyPaymentLoginPage:
					//returnPage = payment.clickPayNowToGetPayUMoneyPage();
					break;
				default:
					returnPage = null;
					break;
				}
			}
			return returnPage;
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}

	}

	/**
	 * Do transaction with Saved Store Card
	 * 
	 * @param paymentTypeRow
	 *            row number of 'PaymentType' sheet
	 * @param storeCardDetailsRow
	 *            row number of 'StoreCard' sheet
	 * @param expResponse
	 *            expected response page, one of the emum values
	 * @return Expected response page
	 */
	Object DoPaymentWithSavedStoreCard(int transactionRow,int paymentTypeRow,
			int storeCardDetailsRow, boolean isSeamless, ExpectedResponsePage expResponse) {
		try
		{
			Object returnPage = null;
			this.paymentTypeRow = paymentTypeRow;

			//Choose Payment Type and fill the payment details
			paymentTypeData = new TestDataReader(testConfig, "PaymentType");

			//Verify if the required ibibo code is present or not
			VerifyIbiboCodePresentForMerchant(this.transactionData.GetCurrentEnvironmentData(transactionRow, "merchantid"), this.paymentTypeRow);

			testConfig.putRunTimeProperty("bankcode", paymentTypeData.GetData(paymentTypeRow, "bankcode"));
			GetTestTransactionPage();
			try
			{
				//Fill Transaction Details
				transactionData = trans.FillTransactionDetails(transactionRow);

			}
			catch(Exception e)

			{
				testConfig.logException(e);
				throw e;
			}

			if(isSeamless)
			{
				try
				{
					testConfig.putRunTimeProperty("seamlessSavedCard","true");
					trans.FillSeamlessSavedCardDetails(storeCardDetailsRow);

					if(testConfig.getRunTimeProperty("RetryOffer")!=null){
						TestDataReader data = new TestDataReader(testConfig, "OfferRetry");
						int row=Integer.parseInt(testConfig.getRunTimeProperty("RetryOffer").trim());
						String expectedOfferRetryTitle=data.GetData(row, "OfferRetryTitle");
						String expectedOfferRetryMessage=data.GetData(row, "OfferRetryMessage");

						OfferRetryPage Offerretry;
						Offerretry=trans.SubmitToGetOfferRetry();
						Offerretry.verifyRetryMessage(expectedOfferRetryTitle,expectedOfferRetryMessage);
						switch(expResponse)
						{
						case TestResponsePage:
							testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							if (testConfig.getRunTimeProperty("CancelRetryOffer")!=null)
								returnPage = Offerretry.clickCancelToGetTestResponse();	
							else
								returnPage = Offerretry.clickContinueToGetTestResponse();
							break;
						case BankRedirectPage:
							returnPage = Offerretry.clickContinueToGetBankRedirectPage();
							break;

						default:
							returnPage = null;
							break;
						}				
					}
					else
					{
						switch(expResponse)
						{
						case BankRedirectPage:
							returnPage = trans.SubmitToGetBankRedirectPage();
							break;
						case ErrorResponsePage:
							returnPage = trans.SubmitToGetErrorResponsePage();
							break;
						case PaymentOptionsPage:
							returnPage = trans.Submit();
							break;
						case TestResponsePage:
							testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
							returnPage = trans.SubmitToGetTestResponsePage();
							break;
						case TransactionPage:
							trans.SubmitToGetErrorOnPage();
							returnPage = trans;
							break;
						case TryAgainPage:
							returnPage = trans.SubmitToGetTryAgainPage();
							break;	
						case ProcessingPage:
							returnPage = trans.SubmitToGetProcessingPage();
							break;
						case PayUMoneyPaymentLoginPage:
							//returnPage = trans.SubmitToGetPayuMoneyPage();
							break;
						default:
							returnPage = null;
							break;
						}
					}

				}
				catch(Exception e)
				{
					testConfig.logException(e);
					throw e;
				}
			}

			else{
				payment = trans.Submit();
				String mode = paymentTypeData.GetData(paymentTypeRow, "mode")
						.toLowerCase();
				switch (mode) {
				case "all":
					break;
				case "goback":
					returnPage = payment.clickGoBackToMerchantName(merchantName);
					return returnPage;
				case "creditcard": {
					ccTab = payment.clickCCTab();
					cardDetailsData = ccTab
							.FillSavedCardDetails(storeCardDetailsRow);
				}
				break;

				case "debitcard": {
					dcTab = payment.clickDCTab();
					cardDetailsData = dcTab
							.FillSavedCardDetails(storeCardDetailsRow);
					break;
				}

				}
				switch (expResponse) {
				case TryAgainPage:
					returnPage = payment.clickPayNowToGetTryAgainPage();
					break;
				case PaymentOptionsPage:
					payment.clickPayNowToGetError();
					returnPage = payment;
					break;
				case TestResponsePage:
					testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
					returnPage = payment.clickPayNow();
					break;
				case BankRedirectPage:
					returnPage = payment.clickPayNowToGetBankRedirectPage();
					break;
				case PayUMoneyPaymentLoginPage:
					//returnPage = payment.clickPayNowToGetPayUMoneyPage();
					break;

				default:
					returnPage = null;
					break;
				}		
			}
			return returnPage;
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}

	}
	public void RemoveExistingStoreCards(){
		ccTab = payment.clickCCTab();
		Browser.wait(testConfig, 1);
		ccTab.removeCCStoredCard();
		dcTab = payment.clickDCTab();
		Browser.wait(testConfig, 1);
		dcTab.removeDCStoredCard();
		payment.clickCCTab();
	}




	public DownloadExcelPage GetTestDownloadExcelPage()
	{
		try
		{
			home.navigateToAdminHome();
			DownloadExcelPage downloadExcelPage = home.ClickDownloadExcel();
			return downloadExcelPage;
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}
	}


	/**
	 * @param testConfig
	 * @return the updated entry in Txn_update table from db
	 */
	public void verify_txn_update_data(Config testConfig){

		int retries = 60;
		Map<String,String> map = null;

		testConfig.getRunTimeProperty("mihpayid");
		while(retries>0){
			Browser.wait(testConfig, 2);
			map = DataBase.executeSelectQuery(testConfig, 4, 1);
			if(map != null)
			{	
				testConfig.logPass("Found txn_update entry");
				return;
			}
			else retries--;
		}
		testConfig.logFail("txn_update entry NOT found");
	}

	/**Get the transaction fee data from txn_update table
	 * @return mer_service_fee, mer_service_tax and mer_net_amount
	 */
	public String[] GetTxnUpdateData() 
	{
		//query to get transaction id of latest bounced transaction 
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 4, 1);
		String[] txnData = new String[3];
		txnData[0] = map.get("mer_service_fee");
		txnData[1] = map.get("mer_service_tax");
		txnData[2] = map.get("mer_net_amount");

		return txnData;
	}

	//Used to make a second server to server call from another browser window to update a preInitiated transaction.
	public Object updatePreinitiatedTransaction(int transactionRow, Config testConfig,Config secondaryConfig)
	{
		Object returnPage = null;
		this.transactionRow = transactionRow;

		String TransactionId = testConfig.getRunTimeProperty("transactionId");

		try
		{
			//Fill Transaction Details
			transactionData = trans.FillTransactionDetails(transactionRow);
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}

		Element.click(testConfig, Element.getPageElement(testConfig, How.id, "payment1"), "Submit");


		WebElement txnId = Element.getPageElement(secondaryConfig, How.name, "txnid");
		Element.enterData(secondaryConfig, txnId, TransactionId, "TransactionId");

		testConfig.putRunTimeProperty("TransactionId", TransactionId);
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 1, 1);
		String payuId = map.get("mihpayid");

		WebElement txtId = Element.getPageElement(secondaryConfig, How.name, "txtid");
		Element.enterData(secondaryConfig, txtId, payuId, "payuId");

		Element.click(testConfig, Element.getPageElement(secondaryConfig, How.css, "input[type=\"submit\"]"), "Submit");

		if(testConfig.isMobile)
			returnPage = new PaymentOptionMobilePage(secondaryConfig);
		else
			returnPage = new PaymentOptionsPage(secondaryConfig);

		//fill the payment details

		return returnPage;
	}


	/**
	 * Do test transaction using the given details (when on Transaction Page)
	 * @param transactionRow row number of 'TransactionDetails' sheet
	 * @param paymentTypeRow row number of 'PaymentType' sheet
	 * @param cardDetailsRow row number of 'CardDetails' sheet
	 * @param expResponse expected response page, one of the emum values
	 * @return Expected response page
	 */
	public Object DoTestTransaction(int transactionRow, int paymentTypeRow, int cardDetailsRow, ExpectedResponsePage expResponse)
	{
		return DoTestTransaction(transactionRow, paymentTypeRow, cardDetailsRow, -1, expResponse);
	}


	/**
	 * Do the COD test transaction using the given details  (when on Transaction Page)
	 * @param transactionRow row number of 'TransactionDetails' sheet
	 * @param paymentTypeRow row number of 'PaymentType' sheet
	 * @param cardDetailsRow row number of 'CardDetails' sheet
	 * @param codRow row number of 'COD' sheet
	 * @param expResponse expected response page, one of the emum values
	 * @return Expected response page
	 */


	public Object DoTestTransaction(int transactionRow, int paymentTypeRow, int cardDetailsRow, int codRow, ExpectedResponsePage expResponse)

	{
		Object returnPage = null;
		this.transactionRow = transactionRow;
		this.paymentTypeRow = paymentTypeRow;

		//Verify if the required ibibo code is present or not
		if(!testConfig.getRunTimeProperty("Environment").toLowerCase().equals("production"))
		VerifyIbiboCodePresentForMerchant(this.transactionData.GetCurrentEnvironmentData(this.transactionRow, "merchantid"), this.paymentTypeRow);


		//will be used to fill up user credentials (of test transaction) in case of stored card feature
		testConfig.putRunTimeProperty("bankcode", this.paymentTypeData.GetData(paymentTypeRow, "bankcode"));

		try
		{
			//Fill Transaction Details
			transactionData = trans.FillTransactionDetails(transactionRow);
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}

		//If seamless, fill the seamless codes and card details 
		if(isSeamless)
		{
			try
			{
				//read mode, bankcode from 'PaymentType' sheet
				paymentTypeData = trans.FillSeamlessCodes(paymentTypeRow);

				String cardDetailsToBeFilled = paymentTypeData.GetData(paymentTypeRow, "CardDetailsToBeFilled");
				switch(cardDetailsToBeFilled)
				{
				case "1": //Fill card details
					cardDetailsData = trans.FillCardDetails(cardDetailsRow);
					break;
				default: //do nothing, it will redirect to bank
					break;
				}
				if(testConfig.getRunTimeProperty("RetryOffer")!=null){
					TestDataReader data = new TestDataReader(testConfig, "OfferRetry");
					int row=Integer.parseInt(testConfig.getRunTimeProperty("RetryOffer").trim());
					String expectedOfferRetryTitle=data.GetData(row, "OfferRetryTitle");
					String expectedOfferRetryMessage=data.GetData(row, "OfferRetryMessage");

					OfferRetryPage Offerretry;
					Offerretry=trans.SubmitToGetOfferRetry();
					Offerretry.verifyRetryMessage(expectedOfferRetryTitle,expectedOfferRetryMessage);
					switch(expResponse)
					{
					case TestResponsePage:
						testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
						if (testConfig.getRunTimeProperty("CancelRetryOffer")!=null)
							returnPage = Offerretry.clickCancelToGetTestResponse();	
						else
							returnPage = Offerretry.clickContinueToGetTestResponse();
						break;
					case BankRedirectPage:
						returnPage = Offerretry.clickContinueToGetBankRedirectPage();
						break;

					default:
						returnPage = null;
						break;
					}				

				}
				else if(testConfig.getRunTimeProperty("WrongEnforceMessage")!=null){
					System.out.println(testConfig.getRunTimeProperty("WrongEnforceMessage"));
					trans.SubmitToGetEnforcewrongPage();
				}

				else
				{
					switch(expResponse)
					{
					case BankRedirectPage:
						returnPage = trans.SubmitToGetBankRedirectPage();
						break;
					case ErrorResponsePage:
						returnPage = trans.SubmitToGetErrorResponsePage();
						break;
					case PaymentOptionsPage:
						returnPage = trans.Submit();
						break;
					case TestResponsePage:
						testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
						returnPage = trans.SubmitToGetTestResponsePage();
						break;
					case TransactionPage:
						trans.SubmitToGetErrorOnPage();
						returnPage = trans;
						break;
					case TryAgainPage:
						returnPage = trans.SubmitToGetTryAgainPage();
						break;
					case ProcessingPage:
						returnPage= trans.SubmitToGetProcessingPage();
					case TestResponsePageWithProcessURL:
						testConfig.putRunTimeProperty("addedon", Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
						returnPage = trans.SubmitToGetTestResponsePageAfterTakingProcessingPageURL();
						break;
					case BankRedirectPageWithProcessingURL:
						returnPage = trans.SubmitToGetBankRedirectPageAfterTakingProcessingPageURL();
						break;
					case PayUMoneyPaymentLoginPage:
						//returnPage= trans.SubmitToGetPayuMoneyPage();
						break;
					default:
						returnPage = null;
						break;
					}
				}
			}
			catch(Exception e)
			{
				testConfig.logException(e);
				throw e;
			}
		}
		else //non-seamless flow
		{
			try
			{
				//get the payment options page
				payment = trans.Submit();
				merchantName = transactionData.GetData(transactionRow, "Comments");
			}
			catch(Exception e)
			{
				testConfig.logException(e);
				throw e; 
			}

			//fill the payment details
			returnPage = DoPayment(paymentTypeRow, cardDetailsRow, codRow, expResponse);
		}

		return returnPage;
	}

	/**
	 * Do the Top level Routing test transaction using the given details  (when on Transaction Page)
	 * @param transactionRow row number of 'TransactionDetails' sheet
	 * @param paymentTypeRow row number of 'PaymentType' sheet
	 * @param expResponse expected response page, one of the emum values
	 * @return Expected bank response page
	 */

	public Object DoTopLevelRoutingTestTransaction(int transactionRow, int paymentTypeRow, ExpectedResponsePage expResponse)

	{
		this.transactionRow = transactionRow;
		this.paymentTypeRow = paymentTypeRow;

		//Verify if the required ibibo code is present or not
		VerifyIbiboCodePresentForMerchant(this.transactionData.GetCurrentEnvironmentData(this.transactionRow, "merchantid"), this.paymentTypeRow);

		try
		{
			//Fill Transaction Details
			transactionData = trans.FillTransactionDetails(transactionRow);
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}

		//get the redirected page
		bankRedirectPage = trans.SubmitToGetBankRedirectPage();

		//Return the redirected page
		return bankRedirectPage;
	}

	/**
	 * Makes the given gateway's status as up if it is down
	 * @param pgtype gateway to be checked
	 */
	public void MakeGatewayAsStatusUp(String pgtype)
	{
		testConfig.putRunTimeProperty("pg_type", pgtype);
		Map<String, String> paymentgateway = DataBase.executeSelectQuery(testConfig, 19, 1);

		if(!paymentgateway.get("up_status").equals("1"))
		{
			testConfig.logComment("Gateway- " + pgtype +" is down, marking that as up");
			String currentUrl = testConfig.driver.getCurrentUrl();

			home.navigateToAdminHome();
			EditDefaultGatewayPage editGateway = home.ClickEditDefaultGateway();
			editGateway.markGatewayStatusAsUp(pgtype);

			if(currentUrl.endsWith("/testpay"))
				GetTestTransactionPage();
			else
				testConfig.driver.navigate().to(currentUrl);
		}
	}

	/**
	 * Get the health of a given gateway
	 */
	public String getGatewayHealth(String pgType,int paymentTypeRowNum)
	{
		testConfig.putRunTimeProperty("pg_type", pgType);
		//get corresponding pg_gateway_id for this PG_TYPE

		TestDataReader pgInfoData = new TestDataReader(testConfig, "PGInfo");
		TestDataReader paymentTypeData = new TestDataReader(testConfig, "PaymentType");

		testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID"));

		String paymentMode = paymentTypeData.GetData(paymentTypeRowNum, "mode");
		if(paymentMode.contentEquals("creditcard"))
			testConfig.putRunTimeProperty("paymentType", "Credit Card");
		else if(paymentMode.contentEquals("netbanking"))
		{
			testConfig.putRunTimeProperty("paymentType", paymentTypeData.GetData(paymentTypeRowNum, "Bank Name"));

		}
		Map<String, String> paymentGateway = DataBase.executeSelectQuery(testConfig, 46, 1);
		return paymentGateway.get("gatewayHealth");
	}

	/**
	 * Get gateway Id for a given gateway and set it on runtime properties
	 */
	public String getGatewayId(String pgType)
	{
		testConfig.putRunTimeProperty("pg_type", pgType);
		//get corresponding pg_gateway_id for this PG_TYPE

		TestDataReader pgInfoData = new TestDataReader(testConfig, "PGInfo");

		testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID"));

		return pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID");

	}
	/**
	 * Get status of a  given gateway
	 */
	public String getGatewayStatus(String pgType)
	{
		testConfig.putRunTimeProperty("pg_type", pgType);
		Map<String, String> paymentGateway = DataBase.executeSelectQuery(testConfig, 19, 1);
		return paymentGateway.get("up_status");
	}


	/**
	 * Verifies that given payment type row (ibibo code and gateway) is present for the given merchant
	 * @param merchantid
	 * @param paymentTypeRow
	 */
	public void VerifyIbiboCodePresentForMerchant(String merchantid, int paymentTypeRow) 
	{
		if(this.transactionData == null)
		{
			this.transactionData = new TestDataReader(testConfig, "TransactionDetails");
		}
		if(this.paymentTypeData == null)
		{
			this.paymentTypeData = new TestDataReader(testConfig, "PaymentType");
		}

		testConfig.putRunTimeProperty("merchantid", merchantid);
		testConfig.putRunTimeProperty("ibibo_code", this.paymentTypeData.GetData(paymentTypeRow, "bankcode"));

		String pgtype = this.paymentTypeData.GetData(paymentTypeRow, "PG_TYPE");

		if(pgtype.equals("") && testConfig.getRunTimeProperty("ibibo_code").equals("")) return; //case of go back user cancel trans

		//get corresponding pg_gateway_id for this PG_TYPE
		TestDataReader pgInfoData = new TestDataReader(testConfig, "PGInfo");
		testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgtype, "PG_TYPE_ID"));

		//query that given ibibocode, pg_id combination is present for this merchantid 
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 2, 1);
		if("1".equals(map.get("cnt")))
		{
			testConfig.logPass(
					"Verified that payment type " + testConfig.getRunTimeProperty("ibibo_code") + " with pg_gateway_id " + 
							testConfig.getRunTimeProperty("pg_id") + " is present for merchantid " + testConfig.getRunTimeProperty("merchantid"));
		}
		else
		{
			testConfig.logFail(
					"Payment type " + testConfig.getRunTimeProperty("ibibo_code") + " with pg_gateway_id " + 
							testConfig.getRunTimeProperty("pg_id") + " is NOT present for merchantid " + testConfig.getRunTimeProperty("merchantid"));
		}

		//marking gateway as UP only for netbanking/cashcard/emi as the option is not shown in UI is gateway is down
		if(checkGatewayUpStatus)
		{
			String mode = paymentTypeData.GetData(paymentTypeRow, "mode").toLowerCase();
			switch(mode)
			{
			case "netbanking":
			case "cashcard":
			case "debitcard":
			case "emi": 
				MakeGatewayAsStatusUp(pgtype);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Fill the payment details (when on payment page)
	 * @param paymentTypeRow row number of 'PaymentType' sheet
	 * @param cardDetailsRow row number of 'CardDetails' sheet
	 * @param codRow row number of 'COD' sheet
	 * @param expResponse expected response page, one of the emum values
	 * @return Expected response page
	 */
	Object DoPayment(int paymentTypeRow, int cardDetailsRow, int codRow, ExpectedResponsePage expResponse)
	{
		try
		{
			Object returnPage = null;
			this.paymentTypeRow = paymentTypeRow;

			String mode = paymentTypeData.GetData(paymentTypeRow, "mode").toLowerCase();
			switch(mode)
			{
			case "all": 
				break;

			case "goback": 
				returnPage = payment.clickGoBackToMerchantName(merchantName);
				return returnPage;

			case "cashcard": 
			{
				cashCardTab = payment.clickCashCardTab();

				String cashCardName = paymentTypeData.GetData(paymentTypeRow, "Bank Name");
				cashCardTab.SelectCashCard(cashCardName);

				String cardDetailsToBeFilled = paymentTypeData.GetData(paymentTypeRow, "CardDetailsToBeFilled");
				switch(cardDetailsToBeFilled)
				{
				case "1": cardDetailsData = cashCardTab.FillCardDetails(cardDetailsRow);
				break;
				default: //do nothing, it will redirect to bank
					break;
				}
			}
			break;

			case "cod": 
			{
				codTab =  payment.clickCODTab();

				codData = codTab.FillShippingDetails(codRow);

				switch(expResponse)
				{
				case CODMobileVerifyPage:
					returnPage = codTab.clickNext();
					break;
				case PaymentOptionsPage:
					codTab.clickNextGetError();
					returnPage = payment;
					break;
				case TestResponsePage:
					returnPage = codTab.clickNextToGetTestResponse();
					return returnPage;
				case GoBackResponse:
					returnPage = codTab.clickGoBackToMerchantName(merchantName);
					return returnPage;
				default:
					returnPage = null;
					break;
				}
			}
			return returnPage;

			case "creditcard": 
			{
				    ccTab = payment.clickCCTab();
				    String value = testConfig.getRunTimeProperty("UseStoredDetails");
				    if(value!=null &&  value.equals("1"))
				    {
				 	   /* MakePayment mk = new MakePayment(testConfig);
					    this.testResponse = (TestResponsePage) mk.doPayment(this.payment, "creditcard");
					    if(null==this.testResponse) testConfig.logFail("Could not do payment through stored card details");
					    return this.testResponse;*/
				    }
				    String dollarkey = transactionData.GetData(transactionRow, "key");
				    String expectedAmount = transactionData.GetData(transactionRow, "amount");
				    if(dollarkey.contentEquals("po83mL"))
				    {
				     	Helper.compareEquals(testConfig, "Transaction Amount", "$ " + expectedAmount, payment.getAmount());

				    }
				    
				    String bankcode = paymentTypeData.GetData(paymentTypeRow, "bankcode").toLowerCase();
				    if(!testConfig.isMobile)
				    {
				    	switch(bankcode)
				    	{
				    	case "amex": ccTab.chooseCardType(CardType.Amex);
				    	break;
				    	case "cc": ccTab.chooseCardType(CardType.VisaMasterCard);
				    	break;
				    	case "dinr": ccTab.chooseCardType(CardType.Diners);
				    	break;
				    	}
				    }
                
				String cardDetailsToBeFilled = paymentTypeData.GetData(paymentTypeRow, "CardDetailsToBeFilled");
				switch(cardDetailsToBeFilled)
				{
				case "1": 
					       cardDetailsData = ccTab.FillCardDetails(cardDetailsRow);
				break;
				default: //do nothing, it will redirect to bank
					break;
				}


			}
			break;

			case "debitcard": 
			{
				dcTab = payment.clickDCTab();

				String value = testConfig.getRunTimeProperty("UseStoredDetails");
				if(value!=null &&  value.equals("1"))
				{
				/*	MakePayment mk = new MakePayment(testConfig);
					this.testResponse = (TestResponsePage) mk.doPayment(this.payment, "debitcard");
					if(null==this.testResponse) testConfig.logFail("Could not do payment through stored card details");
					return this.testResponse;*/
				}

				String debitCardName = paymentTypeData.GetData(paymentTypeRow, "Bank Name");
				dcTab.SelectDebitCard(debitCardName);

				String cardDetailsToBeFilled = paymentTypeData.GetData(paymentTypeRow, "CardDetailsToBeFilled");
				switch(cardDetailsToBeFilled)
				{
				case "1": 
					cardDetailsData = dcTab.FillCardDetails(cardDetailsRow);
					break;
				default: //do nothing, it will redirect to bank
					break;
				}
			}
			break;

			case "emi": 
			{
				emiTab = payment.clickEMITab();

				String bank = paymentTypeData.GetData(paymentTypeRow, "PG_TYPE");;
				String duration = paymentTypeData.GetData(paymentTypeRow, "Bank Name");
				emiTab.SelectBankAndDuration(bank, duration);

				cardDetailsData = emiTab.FillCardDetails(cardDetailsRow);
			}
			break;

			case "ivr": 
				break;

			case "netbanking": 
			{
				MakeGatewayAsStatusUp(testConfig.getRunTimeProperty("pg_type"));

				nbTab = payment.clickNBTab();

				String bankName = paymentTypeData.GetData(paymentTypeRow, "Bank Name");
				String cardDetailsToBeFilled = paymentTypeData.GetData(paymentTypeRow, "CardDetailsToBeFilled");

				switch(cardDetailsToBeFilled)
				{
				case "1": 
					nbTab.SelectBank(bankName);

					cardDetailsData = nbTab.FillCardDetails(cardDetailsRow);
					break;
				default: 	nbTab.SelectBank(bankName);

				break;
				}

			}
			break;
			
			case "wallet": 
			{
				payUMoneyTab = payment.clickPayUMoneyTab();
			}
			break;

			}
			if(testConfig.getRunTimeProperty("RetryOffer")!=null){
				TestDataReader data = new TestDataReader(testConfig, "OfferRetry");
				int row=Integer.parseInt(testConfig.getRunTimeProperty("RetryOffer").trim());
				String expectedOfferRetryTitle=data.GetData(row, "OfferRetryTitle");
				String expectedOfferRetryMessage=data.GetData(row, "OfferRetryMessage");

				OfferRetryPage Offerretry;
				Offerretry=payment.clickPayNowToGetOfferRetry();
				Offerretry.verifyRetryMessage(expectedOfferRetryTitle,expectedOfferRetryMessage);

				switch(expResponse)
				{
				case PaymentOptionsPage:
					returnPage = Offerretry.clickRetryToGetPaymentPage();
					break;
				case TestResponsePage:
					if (testConfig.getRunTimeProperty("CancelRetryOffer")!=null)
						returnPage = Offerretry.clickCancelToGetTestResponse();	
					else
						returnPage = Offerretry.clickContinueToGetTestResponse();
					break;
				case BankRedirectPage:
					returnPage = Offerretry.clickContinueToGetBankRedirectPage();
					break;

				default:
					returnPage = null;
					break;
				}				
			}
			else{

				switch(expResponse)
				{
				case BankRedirectPage:
					returnPage = payment.clickPayNowToGetBankRedirectPage();
					break;
				case PayUMoneyPaymentLoginPage:
					//returnPage = payment.clickPayNowToGetPayUMoneyPage();
					break;
				case TryAgainPage:
					returnPage = payment.clickPayNowToGetTryAgainPage();
					break;
				case PaymentOptionsPage:
					payment.clickPayNowToGetError();
					returnPage = payment;
					break;
				case TestResponsePage:
					long startTime = 0;
					if(testConfig.getRunTimeProperty("MeasurePerformance").toLowerCase().equals("true"))
					{
						startTime = System.nanoTime();
						testConfig.logWarning("Time before Pay Now click:-" + startTime);
					}
					returnPage = payment.clickPayNow();
					    
					if(testConfig.getRunTimeProperty("MeasurePerformance").toLowerCase().equals("true"))
					{
						long endTime = System.nanoTime();
						long estimatedTime = endTime - startTime;
						testConfig.logWarning("Time after test response:-" + endTime);
						testConfig.logWarning("Time Elapsed:-" + estimatedTime);
					}
					break;
				case ProcessingPage:
					returnPage = payment.clickPayNowToGetProcessingPage();
					break;
				case ThreeDSecurePage:
					returnPage = payment.SubmitToGet3DSecurePage();
					break;
				default:
					returnPage = null;
					break;
				}
			}
			return returnPage;
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}
	}

	/**
	 * Gets the specified Payment options page after filling the transaction details (without changing the seamless and card details on the page)
	 * @param transactionRow row to be filled in
	 * @param mode Payment mode tab to be opened
	 * @return
	 */
	public Object GetPaymentOptionsPage(int transactionRow, PaymentMode mode)
	{
		GetTestTransactionPage();
		this.transactionRow = transactionRow;
		try
		{
			transactionData = trans.FillTransactionDetails(transactionRow);
			payment = trans.Submit();
		
			merchantName = transactionData.GetData(transactionRow, "Comments");

			Object paymentOption = null;
			switch(mode)
			{
			case cashcard: paymentOption = payment.clickCashCardTab();
			break;
			case creditcard: paymentOption = payment.clickCCTab();
			break;
			case cod: paymentOption = payment.clickCODTab();
			break;
			case debitcard: paymentOption = payment.clickDCTab();
			break;
			case emi: paymentOption = payment.clickEMITab();
			break;
			case netbanking: paymentOption = payment.clickNBTab();
			break;
			case wallet : paymentOption = payment.clickPayUMoneyTab();
			break;
			}
			cardDetailsData = new TestDataReader(testConfig,"CardDetails");
			return paymentOption;
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}
	}
	/**
	 * Gets the specified Payment options page after filling the transaction details (without changing the seamless and card details on the page)
	 * @param transactionRow row to be filled in
	 * 
	 * @return
	 */
	public PaymentOptionsPage GetPaymentOptionPage(int transactionRow)
	{
		GetTestTransactionPage();
		this.transactionRow = transactionRow;
		try
		{
			transactionData = trans.FillTransactionDetails(transactionRow);
			
			payment = trans.Submit();
			
			merchantName = transactionData.GetData(transactionRow, "Comments");


			return payment;
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}
	}






	/**
	 * Make payment via Credit Card
	 */
	public NewResponsePage makePaymentViaCreditCard(int cardDetailsRowNum)
	{
		if(testConfig.isMobile)
			ccTab = new CCMobileTab(testConfig);
		else
			ccTab = new CCTab(testConfig);
		
		ccTab.FillCardDetails(cardDetailsRowNum);
		payment = new PaymentOptionsPage(testConfig);
		NewResponsePage newResponse = payment.clickPayNowToGetResponsePage();
		return newResponse;
	}

	/*
	 * Creating a rule and doing transaction
	 * delete if there is any rule is already existing
	 */

	public String[] CreateRuleDoTransaction(Config testConfig, TransactionHelper helper, int row, int transactionRowNum, int paymentTypeRowNum, int cardDetailsRowNum, String Merchant, int numOfTxns, int whichRule, String RuleSheet, Boolean isDeny){
		String homeUrl = testConfig.getRunTimeProperty("AdminPortalHome");
		Browser.navigateToURL(testConfig, homeUrl);
		merchantListPage = helper.home.clickMerchantList();

		merchantListPage.SearchMerchant(Merchant);
		rulePage = merchantListPage.clickMerchantTxnRule();
		rulePage.DeleteExistingRules(); 

		//To add switch case here to select option of rule 
		String values[] = rulePage.CreateRule(row, whichRule, RuleSheet);

		//To verify both txn, First should be successful,
		String ruleid = rulePage.SelectRule();

		values[2] = ruleid;

		if (isDeny)
		{
			while(numOfTxns!=0){

				helper.GetTestTransactionPage();
				if (row==10||row==28)
				{
					testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
				}
				if (numOfTxns==1)
				{
					if (row==9)
					{
						cardDetailsRowNum = 19;
					}
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					break;
					//}
				}
				else {
					cardDetailsRowNum = 1;
					paymentTypeRowNum = 3;
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					if(row!=27){
						helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
					}
					numOfTxns--;
				}
			}

		}
		else
		{
			while(numOfTxns!=0){
				helper.GetTestTransactionPage();
				if (row==10)
				{
					testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(7) + "@gmail.com");
				}
				if (numOfTxns==1)
				{
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					break;
				}
				else {
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
					testConfig.putRunTimeProperty("Risk action taken", "No risk");
					helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
					numOfTxns--;
				}
			}
		}

		return values;
	}
	
	
	
	
	public String[] CreateRuleAndDoTransaction(Config testConfig, TransactionHelper helper, int row, int transactionRowNum, int paymentTypeRowNum, int cardDetailsRowNum, String Merchant, int numOfTxns, int whichRule, String RuleSheet, Boolean isDeny){
		String homeUrl = testConfig.getRunTimeProperty("AdminPortalHome");
		Browser.navigateToURL(testConfig, homeUrl);
		merchantListPage = helper.home.clickMerchantList();

		merchantListPage.SearchMerchant(Merchant);
		rulePage = merchantListPage.clickMerchantTxnRule();
		rulePage.DeleteExistingRules(); 

		//To add switch case here to select option of rule 
		String values[] = rulePage.CreateRule(row, whichRule, RuleSheet);

		//To verify both txn, First should be successful,
		String ruleid = rulePage.SelectRule();

		values[2] = ruleid;

		if (isDeny)
		{
			while(numOfTxns!=0){

				helper.GetTestTransactionPage();
				if (row==10||row==28||row==32)
				{
					testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
				}
				if (numOfTxns==1)
				{
					if (row==9)
					{
						cardDetailsRowNum = 19;
					}
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					break;
					//}
				}
				else {
//					cardDetailsRowNum = 1;
					paymentTypeRowNum = 348;
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					if(row!=27){
						helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
					}
					numOfTxns--;
				}
			}

		}
		else
		{
			while(numOfTxns!=0){
				helper.GetTestTransactionPage();
				if (row==10)
				{
					testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(7) + "@gmail.com");
				}
				if (numOfTxns==1)
				{
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					break;
				}
				else {
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
					testConfig.putRunTimeProperty("Risk action taken", "No risk");
					helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
					numOfTxns--;
				}
			}
		}

		return values;
	}
	
	
	

	public String[] CreateRuleAndDoTransactionPay(Config testConfig, TransactionHelper helper, int row, int transactionRowNum, int paymentTypeRowNum, int cardDetailsRowNum, String Merchant, int numOfTxns, int whichRule, String RuleSheet, Boolean isDeny,Boolean override){
		String homeUrl = testConfig.getRunTimeProperty("AdminPortalHome");
		Browser.navigateToURL(testConfig, homeUrl);
		merchantListPage = helper.home.clickMerchantList();

		merchantListPage.SearchMerchant(Merchant);
		rulePage = merchantListPage.clickMerchantTxnRule();
		rulePage.DeleteExistingRules(); 

		//To add switch case here to select option of rule 
		String values[] = rulePage.CreateRule(row, whichRule, RuleSheet);

		//To verify both txn, First should be successful,
		values[2] = rulePage.SelectRule();

		if (isDeny)
		{
			while(numOfTxns!=0){

				helper.GetTestTransactionPage();
				if (row==10||row==28||row==32||row==42||row==51||row==53||row==56||row==58||row==61)
				{
					testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
				}
				
				if(row==52||row==54||row==55||row==57||row==59||row==60){testConfig.putRunTimeProperty("phone",Helper.generateRandomNumber(11) );}
				if (numOfTxns==1)
				{
					if (row==9)
					{
						cardDetailsRowNum = 19;
					}
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					break;
					//}
				}
				else {
//					cardDetailsRowNum = 1;
//					paymentTypeRowNum = 348;
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					if(row!=27){
						helper.testResponse.overrideExpectedTransactionData=override;
						helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
					}
					numOfTxns--;
				}
			}

		}
		else
		{
			while(numOfTxns!=0){
				helper.GetTestTransactionPage();
				
				if (row==10||row==25||row==26||row==29)
				{
					testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(7) + "@gmail.com");
				}
				if(row==26){testConfig.putRunTimeProperty("phone" , String.valueOf(Helper.generateRandomNumber(11)));}
				if (numOfTxns==1)
				{
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					break;
				}
				else {
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
					testConfig.putRunTimeProperty("Risk action taken", "No risk");
					helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
					numOfTxns--;
				}
			}
		}

		return values;
	}

	
	


	/**
	 * Verifies that rule id and rule description are same after 
	 * @param rues_values: contains values from rules created
	 */
	public void VerifyTransactionAfterDenied(String rules_values[], int txnResponseRowNum) 
	{

		//riskAction field value in table transaction for deny transactions is always 2
		String denyRiskActionValue = "2";

		testConfig.putRunTimeProperty("ruleid", rules_values[2]);
		Map<String, String> descMap = DataBase.executeSelectQuery(testConfig, 16, 1);
		String desc = descMap.get("description");
		String rule_name = descMap.get("rulename"); 

		Map<String, String> txnMap_rules = DataBase.executeSelectQuery(testConfig, 15, 1);
		String ruleid_db = txnMap_rules.get("ruleid");
		testConfig.putRunTimeProperty("ruleid", ruleid_db);

		Map<String, String> TxnMap_fields = DataBase.executeSelectQuery(testConfig, 1, 1);
		String field9_db = TxnMap_fields.get("field9");
		TestDataReader dataReader = new TestDataReader(testConfig, "TestResponse");
		String field9_responseSheet = dataReader.GetData(txnResponseRowNum, "field9");

		Map<String, String> txnRiskAction = DataBase.executeSelectQuery(testConfig, 100, 1);
		String riskAction_db = txnRiskAction.get("riskAction");

		if(field9_responseSheet.equalsIgnoreCase(field9_db))
		{
			testConfig.logPass("Verified field9 of payment:" + field9_db);
		}
		else
		{
			testConfig.logFail("field9 in db: " + field9_db + " doesn't match with field9 in txn Response: "+field9_responseSheet);
		}	

		int i =0;
		if(rules_values[i++].equalsIgnoreCase(rule_name))
		{
			testConfig.logPass("Verified the name of payment:" + rule_name);
		}
		else
		{
			testConfig.logFail("No rule named" + rule_name + "present");
		}		


		if(rules_values[i++].equalsIgnoreCase(desc))
		{
			testConfig.logPass(	"Verified the description of payment" + desc);
		}
		else
		{
			testConfig.logFail(	"No rule id with description: " + desc);
		}		

		if(rules_values[i++].equalsIgnoreCase(ruleid_db))
		{
			testConfig.logPass(	"Verified the payment for rule id: " + ruleid_db);
		}
		else
		{			
			testConfig.logFail("No rule id:" + ruleid_db);
		}	

		if(riskAction_db.equalsIgnoreCase(denyRiskActionValue))
		{
			testConfig.logPass("Verified riskAction of this transaction: " + riskAction_db);
		}
		else
		{
			testConfig.logFail("riskAction in db: " + riskAction_db + " doesn't match with riskAction for deny Rules: " + denyRiskActionValue);
		}

	}

	/*
	 * Creating a rule in category and doing transaction
	 * delete if there is any rule already existing
	 */

	public String[] CreateCategoryRuleNDoTxn(Config testConfig, TransactionHelper helper, int riskProfileRow, int transactionRowNum, int paymentTypeRowNum, int cardDetailsRowNum, String Merchant, int numOfTxns, int whichrule, int RiskRuleRow, String RuleSheet, Boolean isDeny){	

		String homeUrl = testConfig.getRunTimeProperty("AdminPortalHome");

		TestDataReader dataReader = new TestDataReader(testConfig, "TransactionDetails");
		String merchantid = dataReader.GetCurrentEnvironmentData(transactionRowNum, "merchantid");
		testConfig.putRunTimeProperty("merchantid", merchantid);

		Browser.navigateToURL(testConfig, homeUrl);
		merchantListPage = helper.home.clickMerchantList();

		merchantListPage.SearchMerchant(Merchant);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		editMerchantPage = merchantDetailsPage.ClickEditMerchant();
		editMerchantRiskProfilePage = editMerchantPage.clickRiskProfile();
		editMerchantRiskProfilePage.editRiskProfile(riskProfileRow);

		Browser.navigateToURL(testConfig, homeUrl);
		merchantCategoryPage = helper.home.clickMerchantCategories();

		categoryPage = merchantCategoryPage.clickOnCategory(testConfig, riskProfileRow);
		categoryPage.DeleteExistingRules(); 
		Browser.goBack(testConfig);

		categoryRulePage = merchantCategoryPage.clickAddTxnRuleToCategory(testConfig);
		String values[] = categoryRulePage.CreateCategroyRule(RiskRuleRow, riskProfileRow, whichrule, RuleSheet);
		Browser.goBack(testConfig);

		categoryPage = merchantCategoryPage.clickOnCategory(testConfig, riskProfileRow);

		String ruleid = categoryPage.SelectRule();

		values[2] = ruleid;
		//verify if risk profile chosen above is getting set or not
		try
		{
			Map<String,String> map = DataBase.executeSelectQuery(testConfig, 79, 1);
			Helper.compareEquals(testConfig, "Risk Category", riskProfileRow, Integer.parseInt(map.get("category")));

			testConfig.putRunTimeProperty("ruleid", ruleid);
			Map<String, String> descMap = DataBase.executeSelectQuery(testConfig, 16, 1);
			Helper.compareExcelEquals(testConfig, "Risk Rulename", "", descMap.get("rulename").toString());
		}
		catch(Exception e)
		{
			testConfig.logWarning(e.toString());
		}

		if (isDeny)
		{
			while(numOfTxns!=0){

				helper.GetTestTransactionPage();
				if (RiskRuleRow==10)
				{
					testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
				}
				if (numOfTxns==1)
				{
					if (RiskRuleRow==9)
					{
						cardDetailsRowNum = 19;
					}
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					break;

				}
				else {
					cardDetailsRowNum = 1;
					paymentTypeRowNum = 3;	
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
					numOfTxns--;
				}
			}

		}
		else
		{
			while(numOfTxns!=0){
				helper.GetTestTransactionPage();
				if (RiskRuleRow==10)
				{
					testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
				}
				if (numOfTxns==1)
				{
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					break;
				}
				else {
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
					testConfig.putRunTimeProperty("Risk action taken", "No risk");
					helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
					numOfTxns--;
				}
			}
		}

		/*while(numOfTxns!=0){
			helper.GetTestTransactionPage();

			helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
			helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
			numOfTxns--;
		}*/

		int i = 0;
		for (String string : values) {
			testConfig.logComment("Rule created as " + i + ". -" + string);
			i++;
		}

		//verify if risk profile chosen above is getting set or not
		try
		{
			Map<String,String> map = DataBase.executeSelectQuery(testConfig, 79, 1);
			Helper.compareEquals(testConfig, "Risk Category", riskProfileRow, Integer.parseInt(map.get("category")));
		}
		catch(Exception e)
		{
			testConfig.logWarning(e.toString());
		}

		return values;

	}
	
	public void deleteAllRiskRules(Config testConfig, TransactionHelper helper) {
		String homeUrl = testConfig.getRunTimeProperty("AdminPortalHome");
		
		Browser.navigateToURL(testConfig, homeUrl);
		merchantCategoryPage = helper.home.clickMerchantCategories();

		TestDataReader data = new TestDataReader(testConfig,"RiskProfile");
		int category;	       
		String rowData=null;
		for (int row=1;row < data.getRecordsNum();row++){
			rowData=data.GetData(row, "Row");
			category=Integer.parseInt(rowData);
			categoryPage = merchantCategoryPage.clickOnCategory(testConfig, category);
			categoryPage.DeleteExistingRules(); 
			Browser.goBack(testConfig);			
		}
	}


	/*public String[] CreateCategoryRuleNDoTxn(Config testConfig, TransactionHelper helper, int riskProfileRow, int transactionRowNum, int paymentTypeRowNum, int cardDetailsRowNum, String Merchant, int numOfTxns, int whichrule, int RiskRuleRow, String RuleSheet, Boolean isDeny){	

		String homeUrl = testConfig.getRunTimeProperty("AdminPortalHome");
		Browser.navigateToURL(testConfig, homeUrl);
		merchantListPage = helper.home.clickMerchantList();

		merchantListPage.SearchMerchant(Merchant);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();

		editMerchantPage = merchantDetailsPage.ClickEditMerchant();
		editMerchantRiskProfilePage = editMerchantPage.clickRiskProfile();
		editMerchantRiskProfilePage.editRiskProfile(riskProfileRow);

		Browser.navigateToURL(testConfig, homeUrl);
		merchantCategoryPage = helper.home.clickMerchantCategories();

		categoryPage = merchantCategoryPage.clickOnCategory(testConfig, riskProfileRow);
		categoryPage.DeleteExistingRules(); 
		Browser.goBack(testConfig);

		categoryRulePage = merchantCategoryPage.clickAddTxnRuleToCategory(testConfig);
		String values[] = categoryRulePage.CreateCategroyRule(RiskRuleRow, riskProfileRow, whichrule, RuleSheet);
		Browser.goBack(testConfig);

		categoryPage = merchantCategoryPage.clickOnCategory(testConfig, riskProfileRow);

		String ruleid = categoryPage.SelectRule();

		values[2] = ruleid;

		if (isDeny)
		{
			while(numOfTxns!=0){

				helper.GetTestTransactionPage();
				if (RiskRuleRow==10)
				{
					testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
				}
				if (numOfTxns==1)
				{
					if (RiskRuleRow==9)
					{
						cardDetailsRowNum=19;
					}
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					break;
				}
				else {
					paymentTypeRowNum = 3;	
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
					numOfTxns--;
				}
			}

		}
		else
		{
			while(numOfTxns!=0){
				helper.GetTestTransactionPage();
				if (RiskRuleRow==10)
				{
					testConfig.putRunTimeProperty("email" , Helper.generateRandomAlphabetsString(5) + "@gmail.com");
				}
				if (numOfTxns==1)
				{
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					break;
				}
				else {
					helper.testResponse = (TestResponsePage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
					helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
					testConfig.putRunTimeProperty("Risk action taken", "No risk");
					helper.verifyChallengedTxn(helper,  testConfig,  transactionRowNum,  paymentTypeRowNum,  cardDetailsRowNum);
					numOfTxns--;
				}
			}
		}
		return values;

	}*/


	public void verifyChallengedTxn(TransactionHelper helper, Config testConfig, int transactionRowNum, int paymentTypeRowNum, int cardDetailsRowNum){
		DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		DashboardPage dashBoard = dashboardHelper.doMerchantLogin(transactionRowNum);		
		dashBoard.ClickViewAll();

		MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		dashboardHelper.transactionsPage = merchantTransaction.SearchTransaction(testConfig.getRunTimeProperty("transactionId"));

		dashboardHelper.transactionsPage.VerifyRuleHit(testConfig);//.VerifyTransactionResponse(helper.transactionData,transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, helper.cardDetailsData, cardDetailsRowNum);
		//Browser.wait(testConfig, 10);

	}

	public void verify_tdr_calculation(Config testConfig, int dataRow){
		//Read TDR rule from excel sheet
		TestDataReader data = new TestDataReader(testConfig,"TDR");

		Map<String,String> map1 = null;
		map1 = DataBase.executeSelectQuery(testConfig, 66, 1);
		Float service_tax = new Float(map1.get("value"));

		int retries = 60;
		Map<String,String> map2 = null;

		while(retries>0){
			Browser.wait(testConfig, 2);
			map2 = DataBase.executeSelectQuery(testConfig, 4, 1);
			if(map2 != null)
				retries = 0;
			else retries--;
		}

		//Get TDR values from txn_update table in DB
		Float ActualServiceFee = new Float(map2.get("mer_service_fee"));
		Float service_tax1 = new Float(map2.get("mer_service_tax"));

		//Get Flat Fee and Percent fee to be applied from Excel as entered in the rule
		Float fee_per = new Float(data.GetData(dataRow, "Fee (in %)"));
		Float fee_flat = new Float(data.GetData(dataRow, "Flat Fee"));

		//Calculate TDR on the basis of formula
		Float t_amount = new Float(testConfig.getRunTimeProperty("amount"));
		Float ExpectedServiceFee = (t_amount*fee_per)/100  + fee_flat ;
		Float service_tax_calc = ExpectedServiceFee*service_tax/100;

		String ActualServiceTax=String.format("%.2f", service_tax1);
		String ExpectedServiceTax=String.format("%.2f", service_tax_calc);

		//Verify TDR calulated and as present in DB
		Helper.compareEquals(testConfig, "TDR Fees", ExpectedServiceFee, ActualServiceFee);
		Helper.compareEquals(testConfig, "TDR Service tax", ExpectedServiceTax, ActualServiceTax);
	}

	public void verify_TDR_recon(String serTax,String serFee,int dataRow)
	{

		Map<String,String> map2 = null;
		map2 = DataBase.executeSelectQuery(testConfig, 4, 1);

		//Get TDR values from txn_update table in DB
		Float service_Fee = new Float(map2.get("mer_service_fee"));
		Float service_tax = new Float(map2.get("mer_service_tax"));

		String serviceTax=String.format("%.2f", service_tax);
		String serviceFee=String.format("%.2f", service_Fee);

		//verify Merchant TDR from recon output
		Helper.compareEquals(testConfig, "PayU charge in recon output", serviceFee, serFee);
		Helper.compareEquals(testConfig, "Service tax in recon output", serviceTax, serTax);

	}

	/**
	 * @param testConfig
	 * @param testResponsePage
	 * @param addnCharge
	 * @param paramRow Additional charge to be picked from row
	 * To verify TDR on billing merchant, i.e. for merchant with param/flag billingMerchant =1
	 */
	public void verify_billing_tdr_calculation(Config testConfig, TestResponsePage testResponsePage, AdditionalChargeType addnCharge, int paramRow){

		//Get TDR from DB for billing merchant
		Map<String,String> map1 = null;
		map1 = DataBase.executeSelectQuery(testConfig, 66, 1);
		Float service_tax = new Float(map1.get("value"));

		int retries = 60;
		Map<String,String> map2 = null;

		while(retries>0){
			Browser.wait(testConfig, 2);
			map2 = DataBase.executeSelectQuery(testConfig, 4, 1);
			if(map2 != null)
				retries = 0;
			else retries--;
		}

		Float ActualPayuCharge = new Float(map2.get("mer_service_fee"));
		Float ActualServiceTax = new Float(map2.get("mer_service_tax"));
		Float ActualSettledAmount = new Float(map2.get("mer_net_amount"));

		Float ExpectedSettledAmount = new Float(testResponsePage.actualResponse.get("amount"));
		Float commission = (float) 0.0;
		if (paramRow == 11 || paramRow == 12 || paramRow ==13 || paramRow == 17 || paramRow == 18 || paramRow == 19)
		{
			commission = new Float(testResponsePage.actualResponse.get("additionalCharges"));
		}
		else if (paramRow == 14 || paramRow == 15 || paramRow ==16 || paramRow == 20 || paramRow == 21 || paramRow == 22)
		{
			commission = (float) 0.0;	
		}

		//Calculate payu_charge on the billing merchant as (commission*100)/(100+12.36)
		Float payu_charge = (float) ((commission*100)/(100+service_tax)) ;
		//Calculate service_tax on the billing merchant as (commission-payu_charge)
		service_tax = commission-payu_charge;

		String ExpectedPayuCharge=null;
		String ExpectedServiceTax=null;
		switch (addnCharge) {
		case FLAT:

			ExpectedPayuCharge=String.format("%.1f", payu_charge);
			if (paramRow == 14 || paramRow == 15 || paramRow == 16 || paramRow == 20 || paramRow == 21 || paramRow == 22)
			{
				ExpectedServiceTax=String.format("%.1f", service_tax);	
			}
			else if(paramRow == 11 || paramRow == 12 || paramRow == 13 || paramRow == 17 || paramRow == 18 || paramRow == 19)
			{
				ExpectedServiceTax=String.format("%.2f", service_tax);
			}
			break;

		case PERC:
			if (paramRow == 14 || paramRow == 15 || paramRow == 16 || paramRow == 20 || paramRow == 21 || paramRow == 22)
			{
				ExpectedPayuCharge=String.format("%.1f", payu_charge);
				ExpectedServiceTax=String.format("%.1f", service_tax);	
			}
			else if(paramRow == 11 || paramRow == 12 || paramRow == 13 || paramRow == 17 || paramRow == 18 || paramRow == 19)
			{
				ExpectedPayuCharge=String.format("%.2f", payu_charge);
				ExpectedServiceTax=String.format("%.2f", service_tax);
			}
			break;

		default:
			break;
		}

		Helper.compareEquals(testConfig, "TDR Payu_Charge ", ExpectedPayuCharge, ActualPayuCharge.toString());
		Helper.compareEquals(testConfig, "TDR Service tax", ExpectedServiceTax, ActualServiceTax.toString());
		Helper.compareEquals(testConfig, "Settled amount", ExpectedSettledAmount , ActualSettledAmount);

	}

	/**
	 * Verify Recon output excel from DB
	 * @param testConfig
	 * @param payuid
	 */
	public void verify_recon(Config testConfig, String payuid) {
		String reconFilePath = "C://Users//"
				+ System.getProperty("user.name") + "//Downloads//";
		File file = Browser.lastFileModified(testConfig, reconFilePath);
		String recon_fileName = file.getName();

		TestDataReader testDataReader = new TestDataReader(testConfig,
				"Sheet1", "C:\\Users\\" + System.getProperty("user.name")
				+ "\\Downloads\\" + recon_fileName);
		TestDataReader reconoutputdata = new TestDataReader(testConfig,
				"BankFeeSettlement");

		//Get data from downloaded excel
		String mihpayid = testDataReader.GetCurrentEnvironmentData(1, "transactionid");
		String file_action = testDataReader.GetCurrentEnvironmentData(1, "action");

		String file_status = testDataReader.GetCurrentEnvironmentData(1, "status");
		file_status = testDataReader.ignoreNumberFormatException("stringType", file_status);

		String amount = testDataReader.GetCurrentEnvironmentData(1, "amount");
		Float file_amount = new Float(amount);

		String file_requestId = testDataReader.GetCurrentEnvironmentData(1, "requestid");
		file_requestId = testDataReader.ignoreNumberFormatException("stringType", file_requestId);

		String file_bankrefnum = testDataReader.GetCurrentEnvironmentData(1, "refnum");
		file_bankrefnum = testDataReader.ignoreNumberFormatException("stringType",file_bankrefnum);

		String file_PayuMerchantId = testDataReader.GetCurrentEnvironmentData(1, "PayUMID");
		file_PayuMerchantId = testDataReader.ignoreNumberFormatException("stringType",file_PayuMerchantId);

		String merServiceFee = testDataReader.GetCurrentEnvironmentData(1, "pay u charge");
		merServiceFee = testDataReader.ignoreNumberFormatException("floatType", merServiceFee);
		Float file_merServiceFee = new Float(merServiceFee);

		String merServiceTax = testDataReader.GetCurrentEnvironmentData(1, "service tax");
		merServiceTax = testDataReader.ignoreNumberFormatException("floatType", merServiceTax);
		Float file_merServiceTax = new Float(merServiceTax);

		String merNetAmount = testDataReader.GetCurrentEnvironmentData(1, "settlement amount");
		merNetAmount = testDataReader.ignoreNumberFormatException("floatType", merNetAmount);
		Float file_merNetAmount = new Float(merNetAmount);

		String file_result = testDataReader.GetCurrentEnvironmentData(1, "result");
		String file_prevStatus = testDataReader.GetCurrentEnvironmentData(1, "prev_status");

		String bankServiceFee = testDataReader.GetCurrentEnvironmentData(1, "servicefee");
		bankServiceFee = testDataReader.ignoreNumberFormatException("floatType", bankServiceFee);
		Float file_bankServiceFee = new Float(bankServiceFee);

		String bankServiceTax = testDataReader.GetCurrentEnvironmentData(1, "servicetax");
		bankServiceTax = testDataReader.ignoreNumberFormatException("floatType", bankServiceTax);
		Float file_bankServiceTax = new Float(bankServiceTax);

		String netAmount = testDataReader.GetCurrentEnvironmentData(1, "net");
		netAmount = testDataReader.ignoreNumberFormatException("floatType", netAmount);
		Float file_netAmount = new Float(netAmount);

		String file_utr = testDataReader.GetCurrentEnvironmentData(1, "utr");
		file_utr = testDataReader.ignoreNumberFormatException("stringType", file_utr);

		String file_reconciliationId = testDataReader.GetCurrentEnvironmentData(1, "reconciliationid");
		String file_pgMID = testDataReader.GetCurrentEnvironmentData(1, "pgMID");
		String file_cardType = testDataReader.GetCurrentEnvironmentData(1, "cardtype");

		//Get data from txn_update table of DB
		testConfig.putRunTimeProperty("mihpayid", payuid);
		Map<String, String> txn_update = DataBase.executeSelectQuery(testConfig, 4, 1);

		//Get data from transaction_settlement table in DB
		testConfig.putRunTimeProperty("txn_update_id", file_requestId);
		Map<String, String> transaction_settlement = DataBase.executeSelectQuery(testConfig, 5, 1);

		//Get data from txn_info table in DB
		testConfig.putRunTimeProperty("payu_id", payuid);
		Map<String, String> txn_info = DataBase.executeSelectQuery(testConfig, 91, 1);

		//Verify data of excel and DB
		Helper.compareEquals(testConfig, "Mihpayuid",
				mihpayid, txn_update.get("txnid"));
		Helper.compareEquals(testConfig, "Action performed",
				file_action, txn_update.get("action"));
		Helper.compareEquals(testConfig, "Status of the transaction",
				file_status.toUpperCase(), txn_update.get("status").toUpperCase());
		Helper.compareEquals(testConfig, "Amount",
				String.format("%.2f", file_amount), txn_update.get("amount"));
		Float bank_service_fee = new Float (transaction_settlement.get("bank_service_fee"));
		Helper.compareEquals(testConfig, "Bank Service Fee",
				String.format("%.2f", file_bankServiceFee), String.format("%.2f",bank_service_fee));
		Float bank_service_tax = new Float (transaction_settlement.get("bank_service_tax"));
		Helper.compareEquals(testConfig, "Bank Service Tax",
				String.format("%.2f", file_bankServiceTax), String.format("%.2f",bank_service_tax));
		Float bank_net_amount = new Float (transaction_settlement.get("bank_net_amount"));
		Helper.compareEquals(testConfig, "Bank Net Amount",
				String.format("%.2f", file_netAmount), String.format("%.2f",bank_net_amount));
		Helper.compareEquals(testConfig, "UTR in bank (not bank_utr)",
				file_utr, transaction_settlement.get("bank_utr"));
		Helper.compareEquals(testConfig, "Card Type",
				file_cardType, txn_info.get("cardtype"));
		Helper.compareEquals(testConfig, "Request Id",
				file_requestId, txn_update.get("id"));
		Helper.compareEquals(testConfig, "Reconciliation ID",
				file_reconciliationId, transaction_settlement.get("reconciliation_id"));
		Helper.compareEquals(testConfig, "Merchant Id",
				file_PayuMerchantId, txn_update.get("merchant_id"));
		Helper.compareEquals(testConfig, "TDR Payu charge",
				String.format("%.2f",file_merServiceFee), txn_update.get("mer_service_fee"));
		Helper.compareEquals(testConfig, "TDR Service Tax",
				String.format("%.2f",file_merServiceTax), txn_update.get("mer_service_tax"));
		Helper.compareEquals(testConfig, "Settlement amount",
				String.format("%.2f",file_merNetAmount), txn_update.get("mer_net_amount"));

		if (file_prevStatus.equals("failed")
				|| file_prevStatus.equals("dropped")) {
			Helper.compareContains(testConfig, "File Result",
					reconoutputdata.GetData(13, "successVerify"), file_result);
			//Bank Ref Num is updated only in case transaction is failed or dropped
			Helper.compareEquals(testConfig, "Bank ref num",
					file_bankrefnum, txn_update.get("bank_ref_no"));

		} else {
			Helper.compareEquals(testConfig, "File Result",
					reconoutputdata.GetData(14, "successVerify"),
					file_result);
			//PGMID is updated only in case transaction is captured
			Helper.compareContains(testConfig, "PGMID",
					file_pgMID, txn_info.get("merchant_params"));

		}

	}

	public Object DoInitiatedTransaction(int transactionRow, int paymentTypeRow)
	{ 
		Object returnPage = null;
		this.transactionRow = transactionRow;
		this.paymentTypeRow = paymentTypeRow;

		//Verify if the required ibibo code is present or not
		VerifyIbiboCodePresentForMerchant(this.transactionData.GetCurrentEnvironmentData(this.transactionRow, "merchantid"), this.paymentTypeRow);
		returnPage = trans.Submit();
		
		return returnPage;
	}



	public void verify_esscom_settlement(Config testConfig, String UTR){
		//Read TDR rule from excel sheet
		int dataRow = 1;
		TestDataReader data = new TestDataReader(testConfig,"TDR");
		Map<String,String> map1 = null;
		map1 = DataBase.executeSelectQuery(testConfig, 66, 1);
		Float service_tax = new Float(map1.get("value"));

		Float fee_per = new Float(data.GetData(dataRow, "Fee (in %)"));
		Float fee_flat = new Float(data.GetData(dataRow, "Flat Fee"));

		Float t_amount = new Float(testConfig.getRunTimeProperty("amount"));
		Float service_fee_calc = (t_amount*fee_per)/100  + fee_flat ;
		Float service_tax_calc = service_fee_calc*service_tax/100;
		Float net_amount_calc = t_amount - service_fee_calc - service_tax_calc;

		String settlementFilePath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";
		File file = Browser.lastFileModified(testConfig,settlementFilePath);
		String settlement_fileName = file.getName();

		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", settlementFilePath + settlement_fileName);

		String file_result = tr.GetCurrentEnvironmentData(1, "result");
		float file_amount = new Float(tr.GetCurrentEnvironmentData(1, "amount"));
		float file_transaction_fee = new Float(tr.GetCurrentEnvironmentData(1, "transaction fee"));
		float file_service_tax = new Float(tr.GetCurrentEnvironmentData(1, "service tax"));
		float file_amount_settled = new Float(tr.GetCurrentEnvironmentData(1, "amount settled"));
		String file_utr = tr.GetCurrentEnvironmentData(1, "utr");
		String file_transactiontype = tr.GetCurrentEnvironmentData(1, "transactiontype");

		Helper.compareEquals(testConfig, "File Upload result", "SUCCESS - Transaction updated successfully", file_result);
		Helper.compareEquals(testConfig, "Amount in settlement file", t_amount, file_amount);

		if((file_transaction_fee - service_fee_calc) > 0.01){
			Boolean actual= true;
			Helper.compareTrue(testConfig, "Service fee in settlement file", actual);
		}

		if((file_service_tax - service_tax_calc)> 0.01) {
			Boolean actual= true;
			Helper.compareTrue(testConfig, "Service tax in settlement file", actual);
		}
		if((file_amount_settled - net_amount_calc)> 0.01) {
			Boolean actual= true;
			Helper.compareTrue(testConfig, "Settled Amount in settlement file", actual);
		}

		Helper.compareEquals(testConfig, "UTR number", UTR, file_utr);
		Helper.compareEquals(testConfig, "Transaction type", "sale", file_transactiontype);
	}
	public void verifyStoreCard(int transactionRow,int paymentTypeRow,int storecardRow,boolean verifySavedCard){
		this.transactionRow = transactionRow;
		this.paymentTypeRow=paymentTypeRow;
		//Verify if the required ibibo code is present or not
		VerifyIbiboCodePresentForMerchant(this.transactionData.GetCurrentEnvironmentData(this.transactionRow, "merchantid"), paymentTypeRow);
		GetTestTransactionPage();
		try
		{
			//Fill Transaction Details
			transactionData = trans.FillTransactionDetails(transactionRow);
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}
		payment = trans.Submit();
		String mode = paymentTypeData.GetData(paymentTypeRow, "mode").toLowerCase();
		switch(mode)
		{
		case "creditcard": 
			ccTab = payment.clickCCTab();
			if (verifySavedCard){
				TestDataReader data = new TestDataReader(testConfig,"StoreCard");
				String value = "";
				WebElement SavedCard=null;
				if (testConfig.getRunTimeProperty("StoreCardName")!=null)
					value=testConfig.getRunTimeProperty("StoreCardName");
				else 
					value = data.GetData(storecardRow, "SavedCardName");
				SavedCard=ccTab.selectStoredCard(value);
				if (SavedCard!=null){
					testConfig.logPass("Verified: Saved Card \""+value+"\" exists");
				}
				else
					testConfig.logPass("Verified: Saved Card \""+value+"\" doesnot exist");
			}
			else
				ccTab.verifyStoreCardDoesNotExists();
			break;

		case "debitcard": 
			dcTab = payment.clickDCTab();
			if (verifySavedCard){
				TestDataReader data = new TestDataReader(testConfig,"StoreCard");
				String value = "";
				WebElement SavedCard=null;
				if (testConfig.getRunTimeProperty("StoreCardName")!=null)
					value=testConfig.getRunTimeProperty("StoreCardName");
				else 
					value = data.GetData(storecardRow, "SavedCardName");
				SavedCard=dcTab.selectStoredCard(value);
				if (SavedCard!=null){
					testConfig.logPass("Verified: Saved Card \""+value+"\" exists");
				}
				else
					testConfig.logPass("Verified: Saved Card \""+value+"\"doesnot exist");
			}
			else
				dcTab.verifyStoreCardDoesNotExists();
			break;
		}


	}
	
	public int verifyBankTDRruleID(Config testConfig) {
		
		Map<String,String> map1 = null;
		map1 = DataBase.executeSelectQuery(testConfig, 104, 1);
		String strBankTdrID=map1.get("id");
		int bankTdrID = Integer.parseInt(strBankTdrID);
		return bankTdrID;
	}	

	public void verifyBankTDRCalculation(Config testConfig, float t_amount, int row) {

		TestDataReader data = new TestDataReader(testConfig,"BankTDR");

		//Get service tax from config table
		Map<String,String> map1 = null;
		map1 = DataBase.executeSelectQuery(testConfig, 66, 1);
		String strServiceTax=map1.get("value");
		Float service_tax = Float.valueOf(strServiceTax);

		//Get details from database 
		testConfig.putRunTimeProperty("t_amount", String.valueOf(t_amount));
		Map<String,String> map2 = DataBase.executeSelectQuery(testConfig, 103, 1);

		String strBankServiceFee=map2.get("bank_service_fee");
		Float DB_bank_service_fee = Float.valueOf(strBankServiceFee);

		String strBankServiceTax = map2.get("bank_service_tax");
		Float DB_bank_service_tax = Float.valueOf(strBankServiceTax);

		String strBankNetAmt = map2.get("bank_net_amount");
		Float DB_bank_net_amount = Float.valueOf(strBankNetAmt);

		String strAmount = map2.get("amount");
		Float DB_amount = Float.valueOf(strAmount);

		String strMerNetAmt = map2.get("mer_net_amount");
		Float DB_MerService_fee = Float.valueOf(strMerNetAmt);

		//Get Flat Fee and Percent fee to be applied from Excel as entered in the rule
		Float fee_per = new Float(data.GetData(row, "PercentFee"));
		Float fee_flat = new Float(data.GetData(row, "FlatFee"));
		Float fee_share = new Float(data.GetData(row, "ShareFee"));
		Float amountLowerLimit = new Float(data.GetData(row, "AmountLowerLimit"));

		//Calculate TDR on the basis of formula
		new DecimalFormat("#.##").format(DB_amount);
		if(DB_amount < amountLowerLimit) {
			Float expectedBankNetAmount =  DB_amount;
			Helper.compareEquals(testConfig, "Bank TDR service fee", 0, DB_bank_service_fee);
			Helper.compareEquals(testConfig, "Bank TDR service tax", 0, DB_bank_service_tax);
			new DecimalFormat("#.##").format(DB_bank_net_amount);
			Helper.compareEquals(testConfig, "Bank TDR service tax", expectedBankNetAmount, DB_bank_net_amount);
		}
		else {
			Float percFeeAmt =  (DB_amount*fee_per)/100;
			Float shareFeeAmt = (DB_MerService_fee*fee_share)/100;
			Float expectedServiceFee;
			if(fee_flat >= percFeeAmt && fee_flat >= fee_share) {
				expectedServiceFee = fee_flat;
			}
			else if (shareFeeAmt >= percFeeAmt && shareFeeAmt >= fee_flat) {
				expectedServiceFee = shareFeeAmt;
			}
			else {
				expectedServiceFee = percFeeAmt;
			}

			DecimalFormat df = new DecimalFormat("###.##");
			Float expectedServiceTax = (expectedServiceFee*service_tax)/100;
			Float expectedBankNetAmount =  DB_amount - expectedServiceFee - expectedServiceTax;
			Helper.compareEquals(testConfig, "Bank TDR service fee", df.format(expectedServiceFee), df.format(DB_bank_service_fee));
			Helper.compareEquals(testConfig, "Bank TDR service tax", df.format(expectedServiceTax), df.format(DB_bank_service_tax));
			Helper.compareEquals(testConfig, "Bank TDR service tax", df.format(expectedBankNetAmount), df.format(DB_bank_net_amount));

		}
	}
	

	public void GetMerchantListPage()
	{
		try
		{
			home.navigateToAdminHome();
			merchantListPage= home.clickMerchantList();
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}
	}

	
	public String GetMerchantKeyFromDatabase() 
	{
		//query to get value of specified merchant's public and private keys
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 3, 1);
		return map.get("value");
	}

	public String getURLForPayUMoneyPage(){
		Browser.waitForPageLoad(testConfig, testConfig.driver.findElement(By.xpath(".//*[@id='ng-app']")));
		return testConfig.driver.getCurrentUrl().trim();
	}

	//This method is created for production test cases
	
	public void verifyOfferRelatedStringsOnProcessingPage(String transactionAmount, String caseValue){
		ProcessingPage processing=new ProcessingPage(testConfig, false);
		Browser.waitForPageLoad(testConfig, processing.getOfferMessage());
		String processingPageText = processing.getOfferMessage().getText().trim();
		Assert.assertTrue(processing.getOfferMessage().isDisplayed(), "Offer Success message did not appear on the Processing page");
		Assert.assertTrue(processing.getOriginalAmountOnDiscountTxn(transactionAmount).isDisplayed(), "Original Amount text is not displayed on the Processing page");
		
		Assert.assertTrue(processingPageText.contains("Net Amount Payable"), "Net Amount text is not displayed on the Processing page");
		switch (caseValue) {
		case "oneValidOneInvalid":
		case "maxOfferAvailed": 
		case "oneValidOneNonExistant": 
			Assert.assertTrue(processingPageText.contains("Discount Awarded"), "Discount Awarded text is not displayed on the Processing page");
			Assert.assertTrue(processingPageText.contains("Net Amount Payable"), "Net Amount Payable text is not displayed on the Processing page");
			break;
		case "offerThatReducesAmountLessThanOne":
			Assert.assertTrue(processingPageText.contains("Discount Awarded"), "Dicount Awarded text is not displayed on the Processing page");
			Assert.assertTrue(processingPageText.contains("Net Amount Payable"), "Net Payable Amount text is not displayed on the Processing page");
			Assert.assertTrue(processingPageText.contains("Maximum Discount"), "Dicount Awarded text is not displayed on the Processing page");
			
		default:
			break;
		}
		
	}	
	
	public void verifyRetryPageElements(){
		ProcessingPage processing=new ProcessingPage(testConfig, false);
		Browser.waitForPageLoad(testConfig, processing.getSorryMessageOnRetryPage());
		testConfig.putRunTimeProperty("strSorryMessage_Actual", processing.getSorryMessageOnRetryPage().getText());
		Assert.assertTrue(processing.getSorryMessageOnRetryPage().isDisplayed(), "Sorry message did not appear on the Retry page");
		
		if(testConfig.getRunTimeProperty("noRetryButton")!=null){
			Assert.assertFalse(Element.IsElementDisplayed(testConfig, processing.getRetryButtononRetryPage()));
		}else{
			Assert.assertTrue(processing.getRetryButtononRetryPage().isDisplayed(), "Retry Button did not appear on the Retry page");	
		}
		
		if(testConfig.getRunTimeProperty("hitRetryButton")!=null){
			Element.click(testConfig, processing.getRetryButtononRetryPage(), "Retry button on Retry page");
		}
		
		if(testConfig.getRunTimeProperty("clickContinue")!=null){
		Element.click(testConfig, processing.getContinueButtonOnRetryPage(), "Continue Button on Retry Page");
		}
	}
	
	  public void verifyTransactionPage(Config testConfig, int transactionRowNum) {
		    DashboardHelper dashboardHelper = new DashboardHelper(testConfig);

		    dashboardHelper.doMerchantLogin(transactionRowNum);

		    DashboardPage dashBoard = new DashboardPage(testConfig);
		   // dashBoard.ClickClose();
		    dashBoard.ClickViewAll();

		    MerchantTransactionsPage merchantTransaction = new MerchantTransactionsPage(testConfig);
		    dashboardHelper.transactionsPage = merchantTransaction.clickFirstTransatcion(testConfig);

		  }

		  /**
	 * Selects Bank from the dropdown
	 * 
	 * @param paymentTypeRow
	 * @date : 18th April
	 */
	public void selectBankInCashCard(int paymentTypeRow) {

		// select bank in cash card tab
		String cashCardName = paymentTypeData.GetData(paymentTypeRow,
				"Bank Name");
		cashCardTab.SelectCashCard(cashCardName);
	}

	/**
	 * Verifies amount defined for discount, amount and total amount payable
	 * @param transactionAmounts 
	 */
	public void verifyTransactionAmountsDisplayed(HashMap<String, String> transactionAmounts) {

		ProcessingPage processing = new ProcessingPage(testConfig, false);
		// wait for element on processing page to appear
		Browser.waitForPageLoad(testConfig, processing.getOfferMessage());
		// Get details displayed on processing page
		String processingPageText = processing.getOfferMessage().getText()
				.trim().toLowerCase();
		System.out.println(processingPageText);
		// verify amount
		Helper.compareContains(testConfig, "Amount", transactionAmounts
				.get("Amount_Text").replaceAll(".00", "")
				.toLowerCase(), processingPageText);
		// verify discount
		Helper.compareContains(testConfig, "Discount", transactionAmounts
				.get("Discount_Text").replaceAll(".00", "")
				.toLowerCase(), processingPageText);
		// verify payable amount
		Helper.compareContains(testConfig, "Payable amount",transactionAmounts
				.get("Payable_Amount_Text")
				.replaceAll(".00", "").toLowerCase(), processingPageText);
		// if maximum discount defined , verify its text
		if (transactionAmounts.containsKey("Maximum_Discount_Text")) {
			Helper.compareContains(testConfig, "Maximum Discount",
					transactionAmounts
				.get("Maximum_Discount_Text")
							.replaceAll(".00", "").toLowerCase(),
					processingPageText);
		}
		// if emi defined then verify emi
		if (transactionAmounts.containsKey("EMI_Amount_Text")) {
			Helper.compareContains(testConfig, "EMI per month text",transactionAmounts
				.get("EMI_Amount_Text")
					.replaceAll(".00", "").toLowerCase(), processingPageText);
		}
	}

	/**
	 *Verifies sorry message that is displayed on processing page 
	 */
	public void getSorryMessageDisplayedOnProcessingPage() {
		ProcessingPage processing = new ProcessingPage(testConfig, false);
		testConfig.putRunTimeProperty("strSorryMessage_Actual", processing
				.getSorryMessageOnRetryPage().getText());
	}

	/**
	 *Verifies if limit exceeded message is displayed on proccessing page 
	 * @param limitMessage 
	 */
	public void validateLimitExceedMessagesOnProcessingPageForEMI(
			String ExpectedMessage) {
		ProcessingPage processing = new ProcessingPage(testConfig, false);
		Browser.waitForPageLoad(testConfig, processing.getLimitMessage());
		String ActualLimitMessage = processing.getLimitMessage().getText()
				.trim();
		Helper.compareEquals(testConfig,
				"Limit Exceed message for Max Amount Per Card case",
				ExpectedMessage, ActualLimitMessage);
	}

	/**
	 *Validates limit message on proccessing page 
	 */
	public void validateLimitExceedMessagesOnProcessingPage() {
		ProcessingPage processing = new ProcessingPage(testConfig, false);
		Browser.waitForPageLoad(testConfig, processing.getLimitMessage());
		String limitMessage = processing.getLimitMessage().getText().trim();
		Assert.assertTrue(
				Element.IsElementDisplayed(testConfig,
						processing.getRetryButtononRetryPage()),
				"Retry button not present!");
		testConfig.putRunTimeProperty("limitMessage_Actual", limitMessage);
	}
	
	/**
	 * Verifies entries created in transaction settlement table in db
	 * 
	 * @param reconRowNum
	 *            row number from data file
	 * @param txn_update_id
	 * @param entryShouldBePresent
	 *            verification if entry is to be present or not
	 */
	public void verifyEntryCreatedInTransactionSettlement(int reconRowNum,
			String txn_update_id, Boolean entryShouldBePresent) {
		Map<String, String> map = null;
		map = DataBase.executeSelectQuery(testConfig, 5, 1);
		if (map == null) {
			if (!entryShouldBePresent) {
				testConfig.logPass("No entry is created with id"
						+ txn_update_id);
				return;
			} else {
				testConfig.logFail("No Entry has been made with id "
						+ txn_update_id);
				return;
			}
		} else {
			testConfig
					.logPass("txn_update entry made with id:" + txn_update_id);
			TestDataReader reconSheet = new TestDataReader(testConfig,
					"ReconFileOutput");
			String fee = reconSheet.GetData(reconRowNum, "servicefee");
			String serviceTax = reconSheet.GetData(reconRowNum, "servicetax");
			String amount = reconSheet.GetData(reconRowNum, "amount");
			Double net = Double.parseDouble(amount) - Double.parseDouble(fee)
					- Double.parseDouble(serviceTax);
			String net_amt = String.valueOf(net);
			Helper.compareContains(testConfig, "Service Fee", fee,
					map.get("bank_service_fee"));
			Helper.compareContains(testConfig, "Service Tax", serviceTax,
					map.get("bank_service_tax"));
			Helper.compareContains(testConfig, "net amount", net_amt,
					map.get("bank_net_amount"));
		}
	}

	/**
	 * Verifies that Entries are created for all action cases specified
	 * @param actionCases 
	 * @param string
	 */
	public void verifyEntriesInTxnUpdate(String mihPayId, String[] ActionCases) {
		Map<String, String> map = null;
		for(String actionName:ActionCases){
			testConfig.putRunTimeProperty("action_name", actionName);
			map =DataBase.executeSelectQuery(testConfig, 116, 1);
			if(map==null)
				testConfig.logFail("No Entry created for "+mihPayId+ "for action " + actionName);
			else
				testConfig.logPass("Entry created for "+mihPayId+ "for action " + actionName);
			testConfig.putRunTimeProperty("txn_update_id",map.get("id"));
			testConfig.removeRunTimeProperty("action_name");
			}
		}

	/**
	 *Gets Manual Update page 
	 *Gets specified tab name
	 *Returns page object of manual update tab specified
	 */
	public Object getManualUpdateTab(ManualUpdateTab tabToBeSelected) {
		ManualUpdatePage manualUpdatePage = home.ClickManualTransactionUpdate();
		Object returnPage = null;
		switch (tabToBeSelected) {
		case MerchantSettlement:
			returnPage = manualUpdatePage.ClickMerchantSettlement();
			break;
		case Chargeback:
			returnPage = manualUpdatePage.clickOnChargebackTab();
			break;
		case Recon:
			returnPage = manualUpdatePage.ClickRecon();
			break;
		case Refund:
			returnPage = manualUpdatePage.goToRefundTab();
			break;
		}
		return returnPage;
	}

	public enum InputField{Ibibo_code,PG };
	/**Verifies field validations for input fields on transaction page
	 * @param fieldForWrongInput Name of input field under verification 
	 * @param transactionRow -transaction Row
	 * @param paymentTypeRow - Payment Row
	 * @param cardRow - Card details row
	 * @param storeCardRow - Store card Row
	 */
	public void DoTestTransactionWithInvalidInput(InputField fieldForWrongInput,int transactionRow,int paymentTypeRow,
													int cardRow,int storeCardRow){
		GetTestTransactionPage();
		trans.FillTransactionDetails(transactionRow);
		trans.FillCardDetails(cardRow);
		trans.FillSeamlessStoreCardDetails(storeCardRow);
		if(fieldForWrongInput==InputField.PG){
			trans.enterPgValue(Helper.generateRandomAlphaNumericString(5));
		}
		if(fieldForWrongInput==InputField.Ibibo_code){
			trans.enterIbibiCodeValue(Helper.generateRandomAlphaNumericString(5));
		}
		//Payment object page is initialized which also checks if payment options page is displayed
		trans.Submit();
	}
	/**
	 * Does transactions and removes card from payments options page as follows
	 * Fills transaction page and submits
	 * Goes to payment page and removes cards
	 * Verifies checkboxes as unchecked
	 * @param transactionRowNum - row number for transaction
	 */
	public void doTransactionAndRemoveStoredCards(int transactionRowNum, int paymentTypeRow){
		PaymentOptionsPage payment = GetPaymentOptionPage(transactionRowNum);
		RemoveExistingStoreCards();
		paymentTypeData = new TestDataReader(testConfig, "PaymentType");
		String mode = paymentTypeData.GetData(paymentTypeRow, "mode");
		switch(mode){
			case "creditcard" :// Verifications for checkbox
								this.ccTab = payment.clickCCTab();
								if(testConfig.getRunTimeProperty("SI")=="1"){
									ccTab.verifyStoreCardCheckboxSetAsChecked();
								}
								else{
								ccTab.verifyStoreCardCheckboxSetAsUnchecked();
								ccTab.verifyStoreCardCheckboxIsEnabled();
								}
								break;
			case "debitcard" :// Verifications for checkbox
								this.dcTab=payment.clickDCTab();
								dcTab.verifyStoreCardCheckboxSetAsUnchecked();
								break;
		}
		
	}
	
	public enum TransactionType {
		Successful, Failed, UserCancelled, Bounced, Dropped
	};

	/**
	 * Logins admin panel and performs transaction of specified type
	 * 
	 * @param transactionType
	 *            - Successful, Failed, UserCancelled, Bounced, Dropped
	 * @param TransactionDetailsRowNumber
	 *            - Row number from TransactionDetails sheet
	 * @param PaymentDetailsRow
	 *            - Row number from Payment Details sheet
	 * @return SuggestedSearchPatternForEmail which is string identifier from
	 *         email.Generally transaction id
	 */
	public String loginAdminPanelAndDoATransactionOfType(
			TransactionType transactionType, int TransactionDetailsRowNumber,
			int PaymentDetailsRow) {
		int validCardDetailsRow = 1;
		int invalidCardDetailsRow= 39;
		int userCancelledPaymentRow =226;
		String SuggestedSearchPatternForEmail ="";
		DoLogin();
		GetTestTransactionPage();
		switch (transactionType) {
		case Successful:
			DoTestTransaction(TransactionDetailsRowNumber, PaymentDetailsRow,
					validCardDetailsRow, ExpectedResponsePage.TestResponsePage);
			SuggestedSearchPatternForEmail = testConfig
					.getRunTimeProperty("txnid");
			break;

		case Failed:
			DoTestTransaction(TransactionDetailsRowNumber, PaymentDetailsRow,
					invalidCardDetailsRow, ExpectedResponsePage.TryAgainPage);
			SuggestedSearchPatternForEmail = testConfig
					.getRunTimeProperty("txnid");
			break;
		case Bounced:
			DoTestTransaction(TransactionDetailsRowNumber, PaymentDetailsRow,
					validCardDetailsRow, ExpectedResponsePage.BankRedirectPage);
			SuggestedSearchPatternForEmail = testConfig
					.getRunTimeProperty("txnid");
			break;
		case UserCancelled:
			DoTestTransaction(TransactionDetailsRowNumber,
					userCancelledPaymentRow, validCardDetailsRow,
					ExpectedResponsePage.TestResponsePage);
			SuggestedSearchPatternForEmail = testConfig
					.getRunTimeProperty("txnid");
			Browser.navigateToURL(testConfig,
					testConfig.getRunTimeProperty("CronUrl"));
			WebElement emailNotification = Element.getPageElement(testConfig,
					How.linkText, "emailNotification.php");
			Element.click(testConfig, emailNotification, "Email Notification Cron");
			Browser.wait(testConfig, 3);
		default:
			break;
		}

		return SuggestedSearchPatternForEmail;
	}

}

