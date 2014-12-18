package Test.AdminPanel.Payments;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.PaymentOptions.PaymentOptionsPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;

public class TestTransactionSeamlessCOD extends TestBase{
	
	@Test(description="Verify seamless cod transaction through AXIS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_COD(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		//AXIS
		int transactionRowNum = 1;
		int paymentTypeRowNum = 55;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.VerifyCODTabOpen().equals("Please confirm your shipping address for cash on delivery payment"));
	}
	
	@Test(description="Verify seamless cod transaction using blank Ibibo code", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_COD_BlankIbiboCode(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum = 323;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.VerifyCODTabOpen().equals("Please confirm your shipping address for cash on delivery payment"));
	}
	
	@Test(description="Verify seamless cod transaction using blank Ibibo code", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_COD_WrongIbiboCode(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 1;
		int paymentTypeRowNum = 324;
		int cardDetailsRowNum = -1;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		Assert.assertTrue(helper.payment.verifyErrorMessageCODTab().equals("Note: Selected payment option is not available at this moment. Kindly try a different payment option or try again after some time."));
	}
	
	//verify that details are saved on COD page in seamless transaction
	@Test(description="Verify saved shipping details in seamless cod transaction", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_Seamless_COD_SaveDetails(Config testConfig) 
	{	
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		helper.DoLogin();

		int transactionRowNum = 65;
		int paymentTypeRowNum = 233;
		int cardDetailsRowNum = -1;
		int codRowNum = 13;
		helper.GetTestTransactionPage();
		helper.payment = (PaymentOptionsPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.PaymentOptionsPage);
		
		helper.testResponse =(TestResponsePage) helper.payment.clickNext();
		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum, codRowNum);

		Assert.assertTrue(testConfig.getTestResult());
		}

}
