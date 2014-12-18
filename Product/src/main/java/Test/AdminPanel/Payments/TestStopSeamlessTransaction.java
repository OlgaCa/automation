package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.MerchantDCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.AdminPanel.TestMerchantTransaction.TransactionMerchantHelper;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;

public class TestStopSeamlessTransaction extends TestBase{

	/*Stop seamless: Transaction with visa/master card without expiry/cvv will be redirected to payment option page 
	 */

	@Test(description = "Verify msg in case of VISA card transaction without cvv redirected to payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stoSeamless_VISA(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//enter Visa card details without CVV 
		int transactionRowNum = 1;
		int paymentTypeRowNum = 31;
		int cardDetailsRowNum = 56;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Visa Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","Visa Debit Cards (All Banks)" , text);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify msg in case of Master card transaction without cvv redirected to payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopSeamless_Master(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Enter master card details without CVV
		int transactionRowNum = 1;
		int paymentTypeRowNum = 31;
		int cardDetailsRowNum = 45;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Master Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","MasterCard Debit Cards (All Banks)" , text);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify msg in case of Master card transaction without expiry and cvv redirected to payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopSeamless_withoutexpiry(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Enter Master card details without expiry date
		int transactionRowNum = 1;
		int paymentTypeRowNum = 31;
		int cardDetailsRowNum = 30;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Master Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","MasterCard Debit Cards (All Banks)" , text);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify msg in case of VISA card transaction without expiry redirected to payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopSeamless_VISA(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//enter Visa card details without CVV 
		int transactionRowNum = 1;
		int paymentTypeRowNum = 31;
		int cardDetailsRowNum = 57;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Visa Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","Visa Debit Cards (All Banks)" , text);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify msg in case of VISA card transaction without cvv redirected to payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopSeamless_MaesVISA(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//enter Visa card details without CVV 
		int transactionRowNum = 3;
		int paymentTypeRowNum = 320;
		int cardDetailsRowNum = 56;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Visa Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","Visa Debit Cards (All Banks)" , text);
		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify msg in case of Master card transaction without cvv redirected to payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopSeamless_MAES(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Enter master card details without CVV
		int transactionRowNum = 3;
		int paymentTypeRowNum = 320;
		int cardDetailsRowNum = 45;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Master Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","MasterCard Debit Cards (All Banks)" , text);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify msg in case of Master card transaction without expiry and cvv redirected to payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Maes_withoutexpiry(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//Enter Master card details without expiry date
		int transactionRowNum = 3;
		int paymentTypeRowNum = 320;
		int cardDetailsRowNum = 30;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */

		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Master Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","MasterCard Debit Cards (All Banks)" , text);
		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify msg in case of VISA card transaction without expiry redirected to payment option page", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopSeamless_Maes(Config testConfig)
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//enter Visa card details without CVV 
		int transactionRowNum = 3;
		int paymentTypeRowNum = 320;
		int cardDetailsRowNum = 57;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Visa Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","Visa Debit Cards (All Banks)" , text);
		Assert.assertTrue(testConfig.getTestResult());	
	}


	@Test(description = "Verify redirection to paymentoption page when Master card details without cvv/expiry is entered inSBI Maestro Iframe transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopSeamlessIframewithoutExpiryCVV(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 1;

		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("State Bank Maestro Cards");

		merHelper.merDCTab.FillDebitCardDetails(3);
		merHelper.merDCTab.clickNoCVV();

		helper.payment=merHelper.merDCTab.clickMerchantDCPaymentToGetPaymentoptionsPage();

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Master Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","MasterCard Debit Cards (All Banks)" , text);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify redirection to paymentoption page when Master card details without expiry is entered inSBI Maestro Iframe transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopSeamlessIframewithoutExpiryDetails(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 1;

		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("State Bank Maestro Cards");

		merHelper.merDCTab.FillDebitCardDetails(45);
		merHelper.merDCTab.clickNoCVV();

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		helper.payment=merHelper.merDCTab.clickMerchantDCPaymentToGetPaymentoptionsPage();
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Master Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","MasterCard Debit Cards (All Banks)" , text);

		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Verify redirection to paymentoption page when VISA card details without expiry is entered inSBI Maestro Iframe transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopSeamlessIframe(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 1;

		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("State Bank Maestro Cards");

		merHelper.merDCTab.FillDebitCardDetails(56);
		merHelper.merDCTab.clickNoCVV();

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		helper.payment=merHelper.merDCTab.clickMerchantDCPaymentToGetPaymentoptionsPage();
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Visa Card and did not enter CVV/Expiry.",cvvmissingText);


		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify redirection to paymentoption page when VISA card details without cvv/expiry is entered inSBI Maestro Iframe transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopSeamlessIframecwithoutCVVExpiry(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 1;

		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("State Bank Maestro Cards");

		merHelper.merDCTab.FillDebitCardDetails(58);
		merHelper.merDCTab.clickNoCVV();

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		helper.payment=merHelper.merDCTab.clickMerchantDCPaymentToGetPaymentoptionsPage();
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Visa Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","Visa Debit Cards (All Banks)" , text);
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify redirection to paymentoption page when Master card details without cvv/expiry is entered inSBI Maestro Iframe transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopMAESIframewithoutExpiryCVV(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 3;

		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("Other Maestro Cards");

		merHelper.merDCTab.FillDebitCardDetails(3);
		merHelper.merDCTab.clickNoCVV();

		helper.payment=merHelper.merDCTab.clickMerchantDCPaymentToGetPaymentoptionsPage();

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */
		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Master Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","MasterCard Debit Cards (All Banks)" , text);

		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify redirection to paymentoption page when Master card details without expiry is entered inSBI Maestro Iframe transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopMAESIframewithoutExpiryDetails(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 3;

		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("Other Maestro Cards");

		merHelper.merDCTab.FillDebitCardDetails(45);
		merHelper.merDCTab.clickNoCVV();

		helper.payment=merHelper.merDCTab.clickMerchantDCPaymentToGetPaymentoptionsPage();
		String cvvmissingText=helper.payment.getcvvMissingText();

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */

		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Master Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","MasterCard Debit Cards (All Banks)" , text);

		Assert.assertTrue(testConfig.getTestResult());
	}


	@Test(description = "Verify redirection to paymentoption page when VISA card details without expiry is entered inSBI Maestro Iframe transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopMAESIframe(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 3;

		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("Other Maestro Cards");

		merHelper.merDCTab.FillDebitCardDetails(56);
		merHelper.merDCTab.clickNoCVV();

		helper.payment=merHelper.merDCTab.clickMerchantDCPaymentToGetPaymentoptionsPage();

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */

		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Visa Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","Visa Debit Cards (All Banks)" , text);
		Assert.assertTrue(testConfig.getTestResult());
	}

	@Test(description = "Verify redirection to paymentoption page when VISA card details without cvv/expiry is entered inSBI Maestro Iframe transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_stopMAESIframecwithoutCVVExpiry(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		TransactionMerchantHelper merHelper = new TransactionMerchantHelper(testConfig, false);
		//admin login
		merHelper.DoLogin();

		int transactionRowNum = 3;

		merHelper.merDCTab = (MerchantDCTab) merHelper.GetMerchantPaymentOptionsPage(transactionRowNum, PaymentMode.debitcard);
		merHelper.merDCTab.ChoseDebitCard("Other Maestro Cards");

		merHelper.merDCTab.FillDebitCardDetails(58);
		merHelper.merDCTab.clickNoCVV();

		helper.payment=merHelper.merDCTab.clickMerchantDCPaymentToGetPaymentoptionsPage();

		/*Verify message on payment option page
		 * verify Default DC type selection * 
		 */

		String cvvmissingText=helper.payment.getcvvMissingText();
		Helper.compareContains(testConfig,"Message on payment options page","It seems that you used a Visa Card and did not enter CVV/Expiry.",cvvmissingText);

		String text=helper.payment.verifyDropDownValue();
		Helper.compareEquals(testConfig, "drop down value","Visa Debit Cards (All Banks)" , text);
		Assert.assertTrue(testConfig.getTestResult());
	}

}