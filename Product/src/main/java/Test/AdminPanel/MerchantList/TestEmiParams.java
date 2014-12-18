package Test.AdminPanel.MerchantList;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsMerchantParamsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsOverrideForGatewayPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsOverrideForOptionPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsOverrideForPgPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsPage;
import PageObject.AdminPanel.Payments.PaymentOptions.EMITab;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.PaymentMode;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestEmiParams extends TestBase{

	MerchantDetailsPage merchantDetailsPage;
	ParamsPage params;
	ParamsMerchantParamsPage merchantParams;
	ParamsOverrideForPgPage overrideForPg;
	ParamsOverrideForPgPage overrideforpg;
	ParamsOverrideForOptionPage overrideforoption;
	ParamsOverrideForGatewayPage overrideforgateway;

	@Test(description = "Test the Emi charges by changing the parameters individually", 
			dataProvider="GetTwoBrowserTestConfig", timeOut=600000, groups="1")
	public void test_EmiCharge_Params(Config testConfig, Config transConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		TransactionHelper transHelper = new TransactionHelper(transConfig,false);

		//Login in 1 browser and then searching the merchant and then clicking the Params tab
		helper.DoLogin();
		int transactionRowNum =23;
		helper.transactionData = new TestDataReader(helper.testConfig,"TransactionDetails");
		String merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");
		MerchantListPage merchantListPage = helper.home.clickMerchantList();
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		params = merchantDetailsPage.ClickParams();

		verify_EmiCharge_Params(params, helper,transHelper);
		
		transHelper.testConfig.closeBrowser();

		Assert.assertTrue(testConfig.getTestResult());
		Assert.assertTrue(transConfig.getTestResult());
	}

	/*
	 * @param params, object of Param
	 * @param helper, TransactionHelper for changing parameters
	 * @param transHelper, TransactionHelper for checking the Emi changes in another bowser
	 * @param testConfig, for parameter change
	 * @param transConfig, for checking emi changes 
	 */
	public void verify_EmiCharge_Params(ParamsPage params, TransactionHelper helper,TransactionHelper transHelper)
	{
		TestDataReader data;
		int paramRow = 3 ;
		data = add_Merchant_Params(params,paramRow);

		verifyEmiChargeCITI3Months(transHelper);
		merchantParams.removeKey(paramRow, data);

		paramRow=4;
		int additional_charge=3;
		data = add_OverrideForPaymentType(params,paramRow);
		verifyEmiChargeHDFC4Percent(transHelper, additional_charge);
		overrideforpg = params.clickOverrideForPg();
		overrideforpg.clickOverrideForPg();
		overrideforpg.removePaymentTypeKeyValue(paramRow, data);

		paramRow=5;
		additional_charge = 4;
		data = add_OverrideForPgCategory(params,paramRow);
		verifyEmiChargeHDFC4Percent(transHelper, additional_charge);
		overrideforpg = params.clickOverrideForPg();
		overrideforpg.clickOverrideForPgCategory();
		overrideforpg.removePgCategoryKeyValue(paramRow, data);

		paramRow=6;
		additional_charge =5;
		data = add_OverrideForOption(params,paramRow);
		verifyEmiChargeHDFC4Percent(transHelper, additional_charge);
		overrideforoption = params.clickOverrideForOptions();
		overrideforoption.clickOverrideForOption();
		overrideforoption.removePaymentOptionKeyValue(paramRow, data);

		paramRow=7;
		additional_charge =6;
		data = add_OverrideForCategory(params,paramRow);
		verifyEmiChargeHDFC4Percent(transHelper, additional_charge);
		overrideforoption = params.clickOverrideForOptions();
		overrideforoption.clickOverrideForCategory();		
		overrideforoption.removePaymentCategoryKeyValue(paramRow, data);

		paramRow=8;
		additional_charge =7;
		data = add_OverrideForGateway(params,paramRow);
		verifyEmiChargeHDFC4Percent(transHelper, additional_charge);
		overrideforgateway = params.clickOverrideForGateway();
		overrideforgateway.removePaymentGatewayKeyValue(paramRow, data);
	}

	@Test(description = "Test emi change based on different params in generic to specific order", 
			dataProvider="GetTwoBrowserTestConfig", timeOut=600000, groups="1")
	public void test_EmiCharge_Override1(Config testConfig, Config transConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		TransactionHelper transHelper = new TransactionHelper(transConfig, false);

		helper.DoLogin();
		int transactionRowNum =23;
		helper.transactionData = new TestDataReader(helper.testConfig,"TransactionDetails");
		String merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");
		MerchantListPage merchantListPage = helper.home.clickMerchantList();
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		params = merchantDetailsPage.ClickParams();

		verify_EmiCharge_Override1(params, helper,transHelper);

		remove_all_emiParams(params, helper, 1);

		transHelper.testConfig.closeBrowser();

		Assert.assertTrue(testConfig.getTestResult());
		Assert.assertTrue(transConfig.getTestResult());
	}

	/*
	 * For generic to specific overriding
	 * @param params, object of Param
	 * @param helper, TransactionHelper for changing parameters
	 * @param transHelper, TransactionHelper for checking the Emi changes in another bowser
	 * @param testConfig, for parameter change
	 * @param transConfig, for checking emi changes 
	 */
	public void verify_EmiCharge_Override1(ParamsPage params, TransactionHelper helper, TransactionHelper transHelper)
	{
		TestDataReader data;

		int paramRow = 4 ;
		data = add_Merchant_Params(params,paramRow);

		paramRow=5;
		data = add_OverrideForPaymentType(params, paramRow);

		int transactionRowNum = 23;
		transHelper.DoLogin();
		transHelper.testConfig.putRunTimeProperty("amount", "1.00");		
		transHelper.emiTab = (EMITab) transHelper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.emi);

		int additional_fee= 4;	
		verifyEmiChargeHDFC4Percent(transHelper, additional_fee);

		paramRow=6;		
		data = add_OverrideForPgCategory(params, paramRow);
		verifyEmiChargeHDFC4Percent(transHelper, additional_fee);

		paramRow=7;
		data = add_OverrideForOption(params, paramRow);
		verifyEmiChargeHDFC4Percent(transHelper, additional_fee);

		paramRow=8;
		data =  add_OverrideForCategory(params, paramRow);
		verifyEmiChargeHDFC4Percent(transHelper, additional_fee);

		paramRow=9;
		data = add_OverrideForGateway(params, paramRow);
		verifyEmiChargeHDFC4Percent(transHelper, additional_fee);
	}

	@Test(description = "Test emi change based on different params in specific to generic order", 
			dataProvider="GetTwoBrowserTestConfig", timeOut=600000, groups="1")
	public void test_EmiCharge_Override2(Config testConfig, Config transConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		TransactionHelper transHelper = new TransactionHelper(transConfig, false);

		helper.DoLogin();
		int testMerchantDataRow =23;
		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		String merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");
		MerchantListPage merchantListPage = helper.home.clickMerchantList();
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		params = merchantDetailsPage.ClickParams();

		verify_EmiCharge_Override2(params, helper,transHelper);

		remove_all_emiParams(params, helper, 2 );

		transHelper.testConfig.closeBrowser();

		Assert.assertTrue(testConfig.getTestResult());
		Assert.assertTrue(transConfig.getTestResult());
	}

	/*
	 * For specific to generic overriding
	 * @param params, object of Param
	 * @param helper, TransactionHelper for changing parameters
	 * @param transHelper, TransactionHelper for checking the Emi changes in another bowser
	 * @param testConfig, for parameter change
	 * @param transConfig, for checking emi changes 
	 */
	public void verify_EmiCharge_Override2(ParamsPage params, TransactionHelper helper, TransactionHelper transHelper)
	{
		TestDataReader data;

		int paramRow = 8 ;
		data =  add_OverrideForCategory(params, paramRow);
		paramRow=9;
		data = add_OverrideForGateway(params, paramRow);

		int transactionRowNum = 23;
		transHelper.DoLogin();
		transHelper.testConfig.putRunTimeProperty("amount", "1.00");		
		transHelper.emiTab = (EMITab) transHelper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.emi);

		int additional_fee = 8;
		verifyEmiChargeHDFC4Percent(transHelper, additional_fee);

		paramRow=7;
		data = add_OverrideForOption(params, paramRow);
		additional_fee = 8;
		verifyEmiChargeHDFC4Percent(transHelper, additional_fee);

		paramRow=6;
		data = add_OverrideForPgCategory(params, paramRow);
		additional_fee=5;
		verifyEmiChargeHDFC4Percent(transHelper, additional_fee);

		paramRow=5;
		data = add_OverrideForPaymentType(params, paramRow);
		additional_fee = 4;
		verifyEmiChargeHDFC4Percent(transHelper, additional_fee);
	}


	/*
	 * login in the second browser then doing a test transaction for the automationMerchant
	 * and checking the EMi charges
	 * @param transHelper, TransactionHelper
	 * @param transConfig, Config
	 * @return emiTab, object of EmiTab
	 */
	public void verifyEmiChargeCITI3Months(TransactionHelper transHelper) 
	{
		int transactionRowNum = 23;
		int paymentTypeRowNum = 42;

		transHelper.DoLogin();
		
		transHelper.testConfig.putRunTimeProperty("amount", "1.00");
		transHelper.emiTab = (EMITab) transHelper.GetPaymentOptionsPage(transactionRowNum, PaymentMode.emi);

		transHelper.emiTab.SelectBankAndDuration(paymentTypeRowNum);

		//Verify details
		String expected = transHelper.cardDetailsData.GetData(1, "EMI");
		String msg1 = "An additional fee of 2% i.e. Rs. 0.02 (Bank processing charges) will be applicable.\n";
		String msg2 = "Your monthly EMI payment will be Rs. 0.33/month for the next 3 Months.";
		expected = msg1 + msg2;

		Helper.compareEquals(transHelper.testConfig, "EMI Charges", expected, transHelper.emiTab.getEmiChargesLabel());
	}

	/*
	 * test the emi charges based on the parameters being changed,
	 * @param emiTab, an object of EMITab
	 * @param transHelper, TransactionHelper
	 * @param transConfig, Config
	 * @param additional_fee, emiCharge percentage value to be tested
	 */
	public void verifyEmiChargeHDFC4Percent(TransactionHelper transHelper, int additional_fee) 
	{
		int paymentTypeRowNum = 49;

		Browser.browserRefresh(transHelper.testConfig);
		transHelper.emiTab = transHelper.payment.clickEMITab();		

		transHelper.emiTab.SelectBankAndDuration(paymentTypeRowNum);	

		//Verify details
		String msg="";
		switch (additional_fee) {
		case 3:
			msg = "An additional fee of 3% i.e. Rs. 0.03 (Bank processing charges) will be applicable.";
			break;

		case 4:
			msg = "An additional fee of 4% i.e. Rs. 0.04 (Bank processing charges) will be applicable.";
			break;

		case 5:
			msg = "An additional fee of 5% i.e. Rs. 0.05 (Bank processing charges) will be applicable.";
			break;

		case 6:
			msg = "An additional fee of 6% i.e. Rs. 0.06 (Bank processing charges) will be applicable.";
			break;

		case 7:
			msg = "An additional fee of 7% i.e. Rs. 0.07 (Bank processing charges) will be applicable.";
			break;

		case 8:
			msg = "An additional fee of 8% i.e. Rs. 0.08 (Bank processing charges) will be applicable.";
			break;

		default:
			msg = "An additional fee of 4% i.e. Rs. 0.04 (Bank processing charges) will be applicable.";
			break;
		}

		String expected = msg; 
		Helper.compareEquals(transHelper.testConfig, "EMI Charges", expected, Element.getPageElement(transHelper.testConfig, How.xPath, "//div[@id='emi_text_div_EMI9']/div/div/div[1]/strong").getText());

	}

	public TestDataReader add_Merchant_Params(ParamsPage params,int paramRow)
	{
		merchantParams = params.clickMerchantParams();

		/*adding the key value, then verifying*/
		TestDataReader data = merchantParams.addKeyValue(paramRow);		
		return data;
	}

	public TestDataReader add_OverrideForPaymentType(ParamsPage params,int paramRow)
	{
		overrideforpg = params.clickOverrideForPg();

		overrideforpg.clickOverrideForPaymentType();

		/*adding the key and value, then verifying*/
		TestDataReader data = overrideforpg.addPaymentTypeParameters(paramRow);

		return data;
	}

	public TestDataReader add_OverrideForPgCategory(ParamsPage params,int paramRow)
	{
		overrideforpg = params.clickOverrideForPg();

		overrideforpg.clickOverrideForPgCategory();

		/*adding the key and value, then verifying*/
		TestDataReader data = overrideforpg.addPgCategoryParameters(paramRow);

		return data;	
	}

	public TestDataReader add_OverrideForOption(ParamsPage params,int paramRow)
	{
		overrideforoption = params.clickOverrideForOptions();

		overrideforoption.clickOverrideForOption();

		/*adding the key and value, then verifying*/
		TestDataReader data = overrideforoption.addOptionNewParams(paramRow);		

		return data;
	}


	public TestDataReader add_OverrideForCategory(ParamsPage params,int paramRow)
	{

		overrideforoption = params.clickOverrideForOptions();

		overrideforoption.clickOverrideForCategory();

		/*adding the key and value, then verifying*/
		TestDataReader data = overrideforoption.addCategoryNewParams(paramRow);	

		return data;
	}

	public TestDataReader add_OverrideForGateway(ParamsPage params,int paramRow)
	{

		overrideforgateway = params.clickOverrideForGateway();

		/*adding the key and value, then verifying*/
		TestDataReader data = overrideforgateway.addGatewayParam(paramRow);	

		return data;
	}

	/*
	 * Removing all the added parameters
	 * @param params, object of Params
	 * @param helper, Transactionhelper 
	 * @param testConfig, Config 
	 * @param testCase, 1 for generic to specific and 2 for specific to generic
	 */	
	public void remove_all_emiParams(ParamsPage params, TransactionHelper helper, int testCase)
	{
		TestDataReader data = new TestDataReader(helper.testConfig,"Params");

		int paramRow;

		if(testCase == 1)
		{
			paramRow =4;
			merchantParams.removeKey(paramRow, data);
		}
		paramRow=5;
		overrideforpg = params.clickOverrideForPg();
		overrideforpg.clickOverrideForPg();
		overrideforpg.removePaymentTypeKeyValue(paramRow, data);

		paramRow=6;	
		overrideforpg = params.clickOverrideForPg();
		overrideforpg.clickOverrideForPgCategory();
		overrideforpg.removePgCategoryKeyValue(paramRow, data);

		paramRow=7;
		overrideforoption = params.clickOverrideForOptions();
		overrideforoption.clickOverrideForOption();
		overrideforoption.removePaymentOptionKeyValue(paramRow, data);

		paramRow=8;
		overrideforoption = params.clickOverrideForOptions();
		overrideforoption.clickOverrideForCategory();		
		overrideforoption.removePaymentCategoryKeyValue(paramRow, data);

		paramRow=9;
		overrideforgateway = params.clickOverrideForGateway();
		overrideforgateway.removePaymentGatewayKeyValue(paramRow, data);		
	}


}
