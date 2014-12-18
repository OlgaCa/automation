package PageObject.AdminPanel.MerchantList;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Helper;
import Utils.TestDataReader;

public class EditMerchantPage {

	private Config testConfig;

	@FindBy(css="input[name='editMerchant[name]']")
	private WebElement editMerchantName;

	@FindBy(css="input[name='editMerchant[email]']")
	private WebElement editMerchantEmail;

	@FindBy(css="textarea[name='editMerchant[address]']")
	private WebElement editMerchantAddress;

	@FindBy(css="input[name='editMerchant[city]']")
	private WebElement editMerchantCity;

	@FindBy(css="select[name='editMerchant[state]']")
	private WebElement editMerchantState;

	@FindBy(css="input[name='editMerchant[phone]']")
	private WebElement editMerchantPhone;

	@FindBy(css="input[name='editMerchant[url]']")
	private WebElement editMerchantUrl;

	@FindBy(css="input[name='editMerchant[company_name]']")
	private WebElement editMerchantCompanyName;

	@FindBy(css="textarea[name='editMerchant[company_desc]']")
	private WebElement editMerchantCompanyDescription;

	@FindBy(css="textarea[name='editMerchant[product_desc]']")
	private WebElement editMerchantProductDescription;

	@FindBy(css="select[name='editMerchant[account_type]']")
	private WebElement editMerchantAccountType;

	@FindBy(css="input[name='editMerchant[account_num]']")
	private WebElement editMerchantAccountNum;

	@FindBy(css="input[name='editMerchant[account_holder]']")
	private WebElement editMerchantAccountHolder;

	@FindBy(css="select[name='editMerchant[bank_acc_type]']")
	private WebElement editMerchantBankAccount;

	@FindBy(css="input[name='editMerchant[bank_name]']")
	private WebElement editMerchantBankName;

	@FindBy(css="input[name='editMerchant[branch_name]']")
	private WebElement editMerchantBranchName;

	@FindBy(css="input[name='editMerchant[ifsc_code]']")
	private WebElement editMerchantIfscCode;

	@FindBy(css="input[name='editMerchant[branch_city]']")
	private WebElement editMerchantBranchCity;

	@FindBy(name="submit_merch_details")
	private WebElement submitMerchantDetails;

	EditMerchantPage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, editMerchantName);
	}

	/**
	 * Reads the "CreateMerchant" sheet of Test Data excel and verify merchant details on the Edit merchant page
	 * @param rowNum Row number of excel sheet to be read
	 * @return Returns if the values match or not
	 */
	void VerifyMerchantEditDetails(TestDataReader data, int createMerchantRow)
	{
		String merchantName = data.GetData(createMerchantRow, "MerchantName").trim();
		String merchantEmail = data.GetData(createMerchantRow, "MerchantEmail").trim();
		String merchantAddress = data.GetData(createMerchantRow, "MerchantAddress").trim();
		String merchantCity = data.GetData(createMerchantRow, "MerchantCity").trim();
		String merchantState = data.GetData(createMerchantRow, "MerchantState").trim();
		String merchantWebsite = data.GetData(createMerchantRow, "MerchantWebsite").trim();
		String merchantCompanyName = data.GetData(createMerchantRow, "MerchantCompanyName").trim();
		String merchantCompanyDescription = data.GetData(createMerchantRow, "MerchantCompanyDescription").trim();
		String merchantProductDescription = data.GetData(createMerchantRow, "MerchantProductDescription").trim();
		String merchantAccountHolder = data.GetData(createMerchantRow, "MerchantAccountHolderName").trim();
		String merchantBankAccount = data.GetData(createMerchantRow, "MerchantBankAccountType").trim();
		String merchantBankName = data.GetData(createMerchantRow, "MerchantBankName").trim();
		String merchantBranchName = data.GetData(createMerchantRow, "MerchantBranchName").trim();
		String merchantIfscCode = data.GetData(createMerchantRow, "MerchantIfscCode").trim();
		String merchantBranchCity = data.GetData(createMerchantRow, "MerchantBranchCity").trim();

		Helper.compareEquals(testConfig, "Merchant name", merchantName, editMerchantName.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant email", merchantEmail, editMerchantEmail.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant address", merchantAddress, editMerchantAddress.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant city", merchantCity, editMerchantCity.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant state", merchantState, editMerchantState.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant website", merchantWebsite, editMerchantUrl.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant company name", merchantCompanyName, editMerchantCompanyName.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant company description", merchantCompanyDescription, editMerchantCompanyDescription.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant product description", merchantProductDescription, editMerchantProductDescription.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant account holder", merchantAccountHolder, editMerchantAccountHolder.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant bank account", merchantBankAccount, editMerchantBankAccount.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant bank name", merchantBankName, editMerchantBankName.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant branch name", merchantBranchName, editMerchantBranchName.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant bank ifsc", merchantIfscCode, editMerchantIfscCode.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant branch city", merchantBranchCity, editMerchantBranchCity.getAttribute("value"));
	}

}

