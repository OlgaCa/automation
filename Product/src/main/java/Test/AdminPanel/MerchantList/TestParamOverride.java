package Test.AdminPanel.MerchantList;

import org.testng.annotations.Test;

import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.ParamOverridePage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.Config;
import Utils.TestBase;
import Utils.TestDataReader;

public class TestParamOverride extends TestBase{

	MerchantDetailsPage merchantDetailsPage;
	ParamOverridePage paramOverride;

	@Test(description = "Test Param Override add and remove", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_paramOverride(Config testConfig) 
	{

		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		
		int testMerchantDataRow =23;

		HomePage home = new HomePage(testConfig);
		MerchantListPage merchantListPage = home.clickMerchantList();

		String merchantName="";

		helper.transactionData = new TestDataReader(testConfig,"TransactionDetails");
		merchantName = helper.transactionData.GetData(testMerchantDataRow, "Comments");

		merchantListPage.SearchMerchant(merchantName);

		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		paramOverride = merchantDetailsPage.ClickParamOverride();
		
		TestDataReader data;
		int paramRowNo =10;
		data = paramOverride.addParamOverrideParameters(paramRowNo);
		paramOverride.verifyParamOverrideKeyValue(paramRowNo, data);
		Browser.browserRefresh(testConfig);
	
		paramOverride.removeGateway(paramRowNo, data);
		Browser.browserRefresh(testConfig);

		paramOverride.verifyParamOverrideRemove(paramRowNo, data);
		
		//TODO verify that the parameters overridden have an effect while doing transaction
	
	}
}