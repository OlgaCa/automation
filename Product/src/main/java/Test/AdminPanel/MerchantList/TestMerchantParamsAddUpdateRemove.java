
package Test.AdminPanel.MerchantList;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.EditMerchantPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsMerchantParamsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsOverrideForGatewayPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsOverrideForOptionPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsOverrideForPgPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamsPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;


public class TestMerchantParamsAddUpdateRemove extends TestBase{ 

	MerchantDetailsPage merchantDetailsPage;
	ParamsPage params;

	ParamsMerchantParamsPage merchantParams;
	ParamsOverrideForPgPage overrideForPg;
	EditMerchantPage editMerchant;
	ParamsOverrideForPgPage overrideforpg;
	ParamsOverrideForOptionPage overrideforoption;
	ParamsOverrideForGatewayPage overrideforgateway;


	@Test(description = "Test Merchant Details Page: add, update and remove parameters  ", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_paramOptionAddUpdateRemove(Config testConfig) 
	{
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int testMerchantDataRow =23;

		MerchantListPage merchantListPage = helper.home.clickMerchantList();

		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		params = merchantDetailsPage.ClickParams();

		testMerchantParams(params);
		testOverrideForPaymentType(params);
		testOverrideForPgCategory(params);
		testOverrideForOptions(params);
		testOverrideForCategory(params);		
		testOverrideForGateway(params);
		
		Assert.assertTrue(testConfig.getTestResult());
	}
	

	/*
	 * testing the "Merchant Params" tab
	 *@param params, object of Params
	 *@param testConfig, Config
	 */
	public void testMerchantParams(ParamsPage params){
		int paramRow=1;
		int paramRow1 =2;
		merchantParams = params.clickMerchantParams();

		/*adding the key value, then verifying*/
		TestDataReader data = merchantParams.addKeyValue(paramRow);
		merchantParams.verifyKeyValue(paramRow, data);

		/*updating the key value, then verifying*/		
		merchantParams.updateKeyValue(paramRow1, data);
		merchantParams.verifyKeyValue(paramRow1, data);

		/*removing the key and value, then verifying*/
		merchantParams.removeKey(paramRow1,data);
		merchantParams.verifyRemove(paramRow1, data);
	}

	/*
	 *testing the "Override for Option" tab
	 *@param params, object of Params
	 *@param testConfig, Config
	 */
	public void testOverrideForOptions(ParamsPage params)
	{
		int paramRow=1;
		int paramRow1 =2;
		overrideforoption = params.clickOverrideForOptions();

		/*adding the key and value, then verifying*/
		TestDataReader data = overrideforoption.addOptionNewParams(paramRow);
		overrideforoption = params.clickOverrideForOptions();
		overrideforoption.verifyPaymentOptionKeyValue(paramRow, data);

		/*updating then verifying*/
		overrideforoption.updatePaymentOptionKeyValue(paramRow1, data);
		overrideforoption = params.clickOverrideForOptions();
		overrideforoption.verifyPaymentOptionKeyValue(paramRow1, data);

		/*removing then verifying*/
		overrideforoption.removePaymentOptionKeyValue(paramRow1, data);
		overrideforoption = params.clickOverrideForOptions();
		overrideforoption.verifyPaymentOptionRemove(paramRow1, data);
	}

	/*
	 *testing the "Override for Category" tab
	 *@param params, object of Params
	 *@param testConfig, Config
	 */
	public void testOverrideForCategory(ParamsPage params)
	{
		int paramRow=1;
		int paramRow1 =2;

		overrideforoption = params.clickOverrideForOptions();
		overrideforoption.clickOverrideForCategory();

		/*adding the key and value, then verifying*/
		TestDataReader data = overrideforoption.addCategoryNewParams(paramRow);
		overrideforoption = params.clickOverrideForOptions();
		overrideforoption.clickOverrideForCategory();		
		overrideforoption.verifyPaymentCategoryKeyValue(paramRow, data);

		/*updating then verifying*/
		overrideforoption.updatePaymentCategoryKeyValue(paramRow1, data);
		overrideforoption = params.clickOverrideForOptions();
		overrideforoption.clickOverrideForCategory();		
		overrideforoption.verifyPaymentCategoryKeyValue(paramRow1, data);

		/*removing then verifying*/
		overrideforoption.removePaymentCategoryKeyValue(paramRow1, data);
		overrideforoption = params.clickOverrideForOptions();
		overrideforoption.clickOverrideForCategory();		
		overrideforoption.verifyPaymentCategoryRemove(paramRow1, data);

	}


	/*
	 *testing the "Override for Payment Type" tab
	 *@param params, object of Params
	 *@param testConfig, Config
	 */
	public void testOverrideForPaymentType(ParamsPage params)
	{
		int paramRow=1;
		int paramRow1 =2;

		overrideforpg = params.clickOverrideForPg();

		overrideforpg.clickOverrideForPaymentType();

		/*adding the key and value, then verifying*/
		TestDataReader data = overrideforpg.addPaymentTypeParameters(paramRow);
		overrideforpg = params.clickOverrideForPg();
		overrideforpg.clickOverrideForPaymentType();		
		overrideforpg.verifyPaymentTypeKeyValue(paramRow, data);

		/*updating then verifying*/
		overrideforpg.updatePaymentTypeKeyValue(paramRow1, data);
		overrideforpg = params.clickOverrideForPg();
		overrideforpg.clickOverrideForPaymentType();		
		overrideforpg.verifyPaymentTypeKeyValue(paramRow1, data);

		/*removing then verifying*/
		overrideforpg.removePaymentTypeKeyValue(paramRow1, data);
		overrideforpg = params.clickOverrideForPg();
		overrideforpg.clickOverrideForPaymentType();		
		overrideforpg.verifyPaymentTypeRemove(paramRow1, data);

	}

	
	/*
	 *testing the "Override for Pg Category" tab
	 *@param params, object of Params
	 *@param testConfig, Config
	 */
	public void testOverrideForPgCategory(ParamsPage params)
	{
		int paramRow=1;
		int paramRow1 =2;

		overrideforpg = params.clickOverrideForPg();

		overrideforpg.clickOverrideForPgCategory();

		/*adding the key and value, then verifying*/
		TestDataReader data = overrideforpg.addPgCategoryParameters(paramRow);
		overrideforpg = params.clickOverrideForPg();
		overrideforpg.clickOverrideForPgCategory();		
		overrideforpg.verifyPgCategoryKeyValue(paramRow, data);

		/*updating then verifying*/
		overrideforpg.updatePgCategoryKeyValue(paramRow1, data);
		overrideforpg = params.clickOverrideForPg();
		overrideforpg.clickOverrideForPgCategory();		
		overrideforpg.verifyPgCategoryKeyValue(paramRow1, data);

		/*removing then verifying*/
		overrideforpg.removePgCategoryKeyValue(paramRow1, data);
		overrideforpg = params.clickOverrideForPg();
		overrideforpg.clickOverrideForPgCategory();		
		overrideforpg.verifyPgCategoryRemove(paramRow1, data);

	}

	/*
	 *testing the "Override for Gateway" tab
	 *@param params, object of Params
	 *@param testConfig, Config
	 */
	public void testOverrideForGateway(ParamsPage params)
	{
		int paramRow=1;
		int paramRow1 =2;

		overrideforgateway = params.clickOverrideForGateway();

		/*adding the key and value, then verifying*/

		TestDataReader data = overrideforgateway.addGatewayParam(paramRow);
		overrideforgateway = params.clickOverrideForGateway();
		overrideforgateway.verifyPaymentGatewayKeyValue(paramRow, data);

		/*updating then verifying*/
		overrideforgateway.updateGatewayKeyValue(paramRow1, data);
		overrideforgateway = params.clickOverrideForGateway();
		overrideforgateway.verifyPaymentGatewayKeyValue(paramRow1, data);

		/*removing then verifying*/
		overrideforgateway.removePaymentGatewayKeyValue(paramRow1, data);
		overrideforgateway = params.clickOverrideForGateway();
		overrideforgateway.verifyPaymentGatewayRemove(paramRow1, data);

	}
	
}
