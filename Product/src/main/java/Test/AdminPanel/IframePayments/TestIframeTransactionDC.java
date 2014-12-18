package Test.AdminPanel.IframePayments;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Payments.IframePaymentOptions.IframeDCTab;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestIframeTransactionDC extends TestBase{

	@Test(description = "Verify iframe Debit card validations with Invalid data",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Verify_IframeDC_Validations(Config testConfig){
		    System.out.println("Failing dou to issue #36792");
		    IframeTransactionHelper helper = new IframeTransactionHelper(testConfig, false);
	        helper.DoLogin();
	        Map<String, String> invalidData=new HashMap<String, String>();
	        invalidData.put("BLANK", "7");
	        invalidData.put("DCNUMBER", "32,33,34,35");
	        invalidData.put("DCNAME", "36");
	        invalidData.put("DCCVV", "38,39,40,41,42");
	        invalidData.put("DCEXP", "65");
	        int transactionRowNum = 105;
	        int paymentTypeRowNum = 22;
	        helper.GetTestMerchantTransactionPage();
	    	{
	    		
	    		// Verify if the required ibibo code is present or not
	    		helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
	    		try {
	    			// Fill Transaction Details
	    			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
	    		} catch (Exception e) {
	    			testConfig.logException(e);
	    			throw e;
	    		}
	    		try {
	    			// get the payment options page
	    			helper.iframePayment = helper.trans.IframeTransactionSubmit();
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
	    				helper.iframeDCTab = (IframeDCTab)helper.DoIframePayment(paymentTypeRowNum, Integer.parseInt(strRow), 7, ExpectedResponsePage.IframeTab);
	    	    		String[] fieldsToValidate={key};//{"DCNUMBER","DCNAME","DCCVV","DCEXP"};
	    	    		helper.iframeDCTab.verifyDCValidations(fieldsToValidate);
	    			}
	    		}
	    		testConfig.endExecutionOnfailure=true;
	        Assert.assertTrue(testConfig.getTestResult());	
	    }
	}
	
	@Test(description = "Verify Iframe Debitcard AutoDetection Of Different Types Of Card",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Verify_IframeDC_AutoDetectionOfDfferentTypeOfCard(Config testConfig){

        IframeTransactionHelper helper = new IframeTransactionHelper(testConfig, false);
        helper.DoLogin();
        int transactionRowNum = 105;
        helper.GetTestMerchantTransactionPage();
    	{
    		// Verify if the required ibibo code is present or not
    		helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
    		try {
    			// Fill Transaction Details
    			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
    		} catch (Exception e) {
    			testConfig.logException(e);
    			throw e;
    		}
    		try {
    			// get the payment options page
    			helper.iframePayment = helper.trans.IframeTransactionSubmit();
    		} catch (Exception e) {
    			testConfig.logException(e);
    			throw e;
    		}
    		helper.iframeDCTab=helper.iframePayment.clickDCTab();
    		int[] rows={55,3,56,64,63};
    		TestDataReader transactionDetails= new TestDataReader(testConfig,"CardDetails");
    		testConfig.endExecutionOnfailure=false;
    		for(int i=0; i<rows.length; i++){
    			helper.enterDCNumberAndVerifyCardType(rows[i],transactionDetails);
    		}
    		testConfig.endExecutionOnfailure=true;
        Assert.assertTrue(testConfig.getTestResult());	
    }
	}
	
	@Test(description = "Verify debitcard transaction using AXIS gateway",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Verify_transaction_using_Axis_gateway(Config testConfig){
		
        IframeTransactionHelper helper = new IframeTransactionHelper(testConfig, false);
        helper.DoLogin();
        //DC-AXIS
        int transactionRowNum = 105;
        int paymentTypeRowNum = 22;
        int cardDetailsRowNum = 1;
        helper.GetTestMerchantTransactionPage();
    		
    		// Verify if the required ibibo code is present or not
    		helper.VerifyIbiboCodePresentForMerchant(helper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "merchantid"), paymentTypeRowNum);
    		helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
    		try {
    			// Fill Transaction Details
    			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
    		} catch (Exception e) {
    			testConfig.logException(e);
    			throw e;
    		}
               
    		try {
    			// get the payment options page
    			helper.iframePayment = helper.trans.IframeTransactionSubmit();
//    			String merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");
    		} catch (Exception e) {
    			testConfig.logException(e);
    			throw e;
    		}

    		// fill the payment details
    		helper.testResponse = (TestResponsePage)helper.DoIframePayment(paymentTypeRowNum, cardDetailsRowNum, 7, ExpectedResponsePage.TestResponsePage);
    		helper.testResponse.VerifyTransactionResponse(helper.transactionData, 105, helper.paymentTypeData, 341);
        Assert.assertTrue(testConfig.getTestResult());	
	}
	
	@Test(description = "Verify debitcard transaction using HDFC2 gateway",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Verify_transaction_using_HDFC2_gateway(Config testConfig){
		
        IframeTransactionHelper helper = new IframeTransactionHelper(testConfig, false);
        helper.DoLogin();
        //DC-HDFC
        int transactionRowNum = 103;
        int paymentTypeRowNum = 342;
        int cardDetailsRowNum = 1;
        helper.GetTestMerchantTransactionPage();
    		
    		// Verify if the required ibibo code is present or not
    		helper.VerifyIbiboCodePresentForMerchant(helper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "merchantid"), paymentTypeRowNum);
    		helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
    		try {
    			// Fill Transaction Details
    			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
    		} catch (Exception e) {
    			testConfig.logException(e);
    			throw e;
    		}
               
    		try {
    			// get the payment options page
    			helper.iframePayment = helper.trans.IframeTransactionSubmit();
    		} catch (Exception e) {
    			testConfig.logException(e);
    			throw e;
    		}

    		// fill the payment details
    		helper.testResponse = (TestResponsePage)helper.DoIframePayment(paymentTypeRowNum, cardDetailsRowNum, 7, ExpectedResponsePage.TestResponsePage);
    		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
        Assert.assertTrue(testConfig.getTestResult());	
	}
	
	@Test(description = "Test Transation with Maestro cards(other maestro)",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Transation_with_Maestro_cards_withCVV(Config testConfig){
		
        IframeTransactionHelper helper = new IframeTransactionHelper(testConfig, false);
        helper.DoLogin();
        //DC-HDFC
        int transactionRowNum = 103;
        int paymentTypeRowNum = 343;
        int cardDetailsRowNum = 66;
        helper.GetTestMerchantTransactionPage();
    		// Verify if the required ibibo code is present or not
    		helper.VerifyIbiboCodePresentForMerchant(helper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "merchantid"), paymentTypeRowNum);
    		helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
    		try {
    			// Fill Transaction Details
    			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
    		} catch (Exception e) {
    			testConfig.logException(e);
    			throw e;
    		}
               
    		try {
    			// get the payment options page
    			helper.iframePayment = helper.trans.IframeTransactionSubmit();
    		} catch (Exception e) {
    			testConfig.logException(e);
    			throw e;
    		}

    		// fill the payment details
    		helper.testResponse = (TestResponsePage)helper.DoIframePayment(paymentTypeRowNum, cardDetailsRowNum, 7, ExpectedResponsePage.TestResponsePage);
    		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
        Assert.assertTrue(testConfig.getTestResult());	
	}
	
	
	@Test(description = "Test Transation with Maestro cards without CVV and Exp(other maestro)",
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void Transation_with_Maestro_cards_withoutCVV_and_Exp(Config testConfig){
		
        IframeTransactionHelper helper = new IframeTransactionHelper(testConfig, false);
        helper.DoLogin();
        //DC-HDFC
        int transactionRowNum = 103;
        int paymentTypeRowNum = 344;
        int cardDetailsRowNum = 66;
        helper.GetTestMerchantTransactionPage();
    		// Verify if the required ibibo code is present or not
    		helper.VerifyIbiboCodePresentForMerchant(helper.transactionData.GetCurrentEnvironmentData(transactionRowNum, "merchantid"), paymentTypeRowNum);
    		helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
    		try {
    			// Fill Transaction Details
    			helper.transactionData = helper.trans.FillTransactionDetails(transactionRowNum);
    		} catch (Exception e) {
    			testConfig.logException(e);
    			throw e;
    		}
               
    		try {
    			// get the payment options page
    			helper.iframePayment = helper.trans.IframeTransactionSubmit();
    		} catch (Exception e) {
    			testConfig.logException(e);
    			throw e;
    		}

    		// fill the payment details
    		helper.iframeDCTab=helper.iframePayment.clickDCTab();
    		if(helper.iframeDCTab.data==null){
    			helper.iframeDCTab.data=new TestDataReader(testConfig, "CardDetails");
    		}
    		helper.iframeDCTab.enterDCNumber(helper.iframeDCTab.data.GetData(cardDetailsRowNum, "CC"));
    		helper.iframeDCTab.enterNameOnCard(helper.iframeDCTab.data.GetData(cardDetailsRowNum, "Name"));
    		helper.iframeDCTab.hideCVVAndEXP();
    		helper.testResponse=helper.iframeDCTab.clickIframeDCPaymentToGetTestResponsePage();
    		helper.testResponse.VerifyTransactionResponse(helper.transactionData, transactionRowNum, helper.paymentTypeData, paymentTypeRowNum);
        Assert.assertTrue(testConfig.getTestResult());	
	}
}
