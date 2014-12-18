package Test.AdminPanel.IframePayments;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import Test.AdminPanel.IframePayments.IframeTransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Test.MerchantPanel.Dashboard.DashboardHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;
import PageObject.AdminPanel.Payments.IframePaymentOptions.IframeCCTab;
import PageObject.AdminPanel.Payments.IframePaymentOptions.IframeDCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.CCTab;
import PageObject.AdminPanel.Payments.PaymentOptions.CCTab.CardType;
import PageObject.AdminPanel.Payments.PaymentOptions.ProcessingPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.MerchantPanel.Home.DashboardPage;
import PageObject.MerchantPanel.Home.MerchantTransactionsPage;

public class TestIframeTransactionCC extends TestBase{

	DashboardPage dashBoard;
	
	@Test(description = "Verify iframe credit card transaction through AXIS gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
    public void test_IframeCC_AXIS(Config testConfig)
    {
        IframeTransactionHelper iframeTransactionHelper = new IframeTransactionHelper(testConfig, false);
        iframeTransactionHelper.DoLogin();
        
        //CC-AXIS
        int transactionRowNum = 105;
        int paymentTypeRowNum = 3;
        int cardDetailsRowNum = 1;
        iframeTransactionHelper.GetTestMerchantTransactionPage();
        iframeTransactionHelper.testResponse = (TestResponsePage) iframeTransactionHelper.DoIframeTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
        iframeTransactionHelper.testResponse.VerifyTransactionResponse(iframeTransactionHelper.transactionData, transactionRowNum, iframeTransactionHelper.paymentTypeData, paymentTypeRowNum);

        Assert.assertTrue(testConfig.getTestResult());	
    }
	
	@Test(description = "Verify iframe credit card transaction through AMEX gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_IframeCC_AMEX(Config testConfig)
	{	
		IframeTransactionHelper iframeTransactionHelper = new IframeTransactionHelper(testConfig, false);
		iframeTransactionHelper.DoLogin();
        
		//AMEX
		int transactionRowNum = 103;
		int paymentTypeRowNum = 345;
		int cardDetailsRowNum = 67;
		
		iframeTransactionHelper.GetTestMerchantTransactionPage();
		
		iframeTransactionHelper.testResponse = (TestResponsePage) iframeTransactionHelper.DoIframeTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		iframeTransactionHelper.testResponse.VerifyTransactionResponse(iframeTransactionHelper.transactionData, transactionRowNum, iframeTransactionHelper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify iframe credit card transaction through HDFC2 gateway", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_IframeCC_HDFC2(Config testConfig)
	{
		IframeTransactionHelper iframeTransactionHelper = new IframeTransactionHelper(testConfig, false);
		iframeTransactionHelper.DoLogin();
        
		//CC
		int transactionRowNum = 103;
		int paymentTypeRowNum = 5;
		int cardDetailsRowNum = 1;
		iframeTransactionHelper.GetTestMerchantTransactionPage();
		iframeTransactionHelper.testResponse = (TestResponsePage) iframeTransactionHelper.DoIframeTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.TestResponsePage);
		iframeTransactionHelper.testResponse.VerifyTransactionResponse(iframeTransactionHelper.transactionData, transactionRowNum, iframeTransactionHelper.paymentTypeData, paymentTypeRowNum);

		Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify Iframe Creditcard AutoDetection Of Different Types Of Card",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_IframeCC_AutoDetectionOfDfferentTypeOfCard(Config testConfig)
	{
        IframeTransactionHelper iframeTransactionHelper = new IframeTransactionHelper(testConfig, false);
        iframeTransactionHelper.DoLogin();
        int transactionRowNum = 105;
        //int paymentTypeRowNum = 22;
        iframeTransactionHelper.GetTestMerchantTransactionPage();
        try {
			// Fill Transaction Details
        	iframeTransactionHelper.transactionData = iframeTransactionHelper.trans.FillTransactionDetails(transactionRowNum);
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}
        try {
			// get the payment options page
        	iframeTransactionHelper.iframePayment = iframeTransactionHelper.trans.IframeTransactionSubmit();
		} catch (Exception e) {
			testConfig.logException(e);
			throw e;
		}
        iframeTransactionHelper.iframeCCTab=iframeTransactionHelper.iframePayment.clickCCTab();
    	int[] rows={36,56,55,64,63};
    	TestDataReader transactionDetails= new TestDataReader(testConfig,"CardDetails");
    	testConfig.endExecutionOnfailure=false;
    	for(int i=0; i<rows.length; i++){
    	iframeTransactionHelper.enterCCNumberAndVerifyCardType(rows[i],transactionDetails);
    	}
    	testConfig.endExecutionOnfailure=true;
        Assert.assertTrue(testConfig.getTestResult());	
	}

	@Test(description = "Verify iframe Debit card validations with Invalid data",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_IframeCC_Validations(Config testConfig){
		    System.out.println("Failing dou to issue #36792");
	        IframeTransactionHelper iframeTransactionHelper = new IframeTransactionHelper(testConfig, false);
	        iframeTransactionHelper.DoLogin();
	        Map<String, String> invalidData=new HashMap<String, String>();
	        invalidData.put("BLANK", "7");
	        invalidData.put("CCNUMBER", "32,33,34,35");
	        invalidData.put("CCNAME", "36");
	        invalidData.put("CCCVV", "38,39,40,41,42");
	        invalidData.put("CCEXP", "65");
	        int transactionRowNum = 104;
	        int paymentTypeRowNum = 3;
	        iframeTransactionHelper.GetTestMerchantTransactionPage();
	    	try {
	    		// Fill Transaction Details
	    		iframeTransactionHelper.transactionData = iframeTransactionHelper.trans.FillTransactionDetails(transactionRowNum);
	    	} catch (Exception e) {
	    	testConfig.logException(e);
	    		throw e;
	    	}
	    	try {
	    		// get the payment options page
	    		iframeTransactionHelper.iframePayment = iframeTransactionHelper.trans.IframeTransactionSubmit();
	    	} catch (Exception e) {
	    		testConfig.logException(e);
	    		throw e;
	    	}
	    	Iterator<String> mit=invalidData.keySet().iterator();
	    	while(mit.hasNext()){
	    	String key=mit.next();
	    	for(String strRow : invalidData.get(key).split(","))
	    	{
	    		testConfig.endExecutionOnfailure=false;
	    		// fill the payment details and verify fields
	    		iframeTransactionHelper.iframeCCTab = (IframeCCTab)iframeTransactionHelper.DoIframePayment(paymentTypeRowNum, Integer.parseInt(strRow), 7, ExpectedResponsePage.IframeTab);
	     		String[] fieldsToValidate={key};//{"CCNUMBER","CCNAME","CCCVV","CCEXP"};
	     		iframeTransactionHelper.iframeCCTab.verifyCCValidations(fieldsToValidate);
	    	}
	    }
	    testConfig.endExecutionOnfailure=true;
	    Assert.assertTrue(testConfig.getTestResult());	
	}
}