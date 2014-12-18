package Test.AdminPanel.Home;

import java.util.HashMap;

import PageObject.AdminPanel.MerchantList.CreateMerchantPage;
import PageObject.AdminPanel.MerchantList.MerchantListPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Config;

public class MerchantHelper {
	private Config testConfig;
	TransactionHelper transactionHelper;
	CreateMerchantPage createMerchant;
	
	public MerchantHelper(Config testConfig) {
		this.testConfig = testConfig;
		transactionHelper = new TransactionHelper(testConfig, true);
	}

	public HashMap<String, String> createMerchant(int CreateMerchantRow){
		createMerchant = transactionHelper.home.ClickCreateNewMerchant();
		HashMap<String, String>MerchantDetails = createMerchant.FillMerchantDetails(CreateMerchantRow);
		transactionHelper.home = createMerchant.Submit();
		MerchantListPage list = transactionHelper.home.SearchMerchant(MerchantDetails.get("MerchantName"));
		list.ApproveFirstMerchant();
		list.SearchMerchant(MerchantDetails.get("MerchantName"));
		list.ActivateFirstMerchant();
		return MerchantDetails;
	}
	
	public void verifyNewlyCreatedMerchantDetails(
			HashMap<String, String> merchantDetails) {
		MerchantListPage merchantListPage = transactionHelper.home
				.SearchMerchant(merchantDetails.get("MerchantName"));
		merchantListPage.VerifyMerchantDetails(merchantDetails);
		
	}
}
