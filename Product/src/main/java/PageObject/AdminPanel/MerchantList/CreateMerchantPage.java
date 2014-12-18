package PageObject.AdminPanel.MerchantList;

import java.util.HashMap;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.AdminPanel.Home.HomePage;
import Utils.Browser;
import Utils.Element;
import Utils.Config;
import Utils.TestDataReader;

public class CreateMerchantPage {

	private Config testConfig;

	@FindBy(name="newMerchant[name]")
	private WebElement merchantName;

	@FindBy(name="newMerchant[phone]")
	private WebElement merchantPhoneNo;

	@FindBy(name="newMerchant[website]")
	private WebElement merchantWebsite;

	@FindBy(name="newMerchant[companyName]")
	private WebElement merchantCompanyName;

	@FindBy(name="newMerchant[address]")
	private WebElement merchantAddress;

	@FindBy(name="newMerchant[city]")
	private WebElement merchantCity;

	@FindBy(name="newMerchant[state]")
	private WebElement merchantState;

	@FindBy(name="newMerchant[email]")
	private WebElement merchantEmail;

	@FindBy(name="newMerchant[companyDesc]")
	private WebElement merchantCompanyDescription;

	@FindBy(name="newMerchant[productDesc]")
	private WebElement merchantProductDescription;

	@FindBy(name="newMerchant[accountNumber]")
	private WebElement merchantAccountNumber;

	@FindBy(name="newMerchant[accountHolder]")
	private WebElement merchantAccountHolderName;

	@FindBy(name="newMerchant[bankAccType]")
	private WebElement merchantBankAccountType;

	@FindBy(name="newMerchant[bankName]")
	private WebElement merchantBankName;

	@FindBy(name="newMerchant[branchName]")
	private WebElement merchantBranchName;

	@FindBy(name="newMerchant[ifscCode]")
	private WebElement merchantIfscCode;

	@FindBy(name="newMerchant[branchCity]")
	private WebElement merchantBranchCity;

	@FindBy(name="newMerchant[rtgsCode]")
	private WebElement merchantRtgsCode;
	
	@FindBy(name="newMerchant[preformaCode]")
	private WebElement merchantPerformaCode;
	
	@FindBy(css="css=button.submit_Div")
	private WebElement submit;

	public CreateMerchantPage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, merchantName);
	}

	/**
	 * Reads the "CreateMerchant" sheet of Product Test Data excel and fills the Merchant Registration  page
	 * @param rowNum Row number of excel sheet to be filled in
	 * @return Returns Merchant Details in key value pairs 
	 */
	public HashMap<String, String> FillMerchantDetails(int createMerchantRow) {
		TestDataReader data = new TestDataReader(testConfig, "CreateMerchant");
		//Gather merchant details	
		HashMap<String, String> MerchantDetails = new HashMap<>(6);

		String MerchantName = data.GetData(createMerchantRow, "MerchantName");
		MerchantDetails.put("MerchantName", MerchantName);
		Element.enterData(testConfig, merchantName, MerchantName,
				"Merchant name");

		String MerchantPhoneNo = data.GetData(createMerchantRow,
				"MerchantPhoneNo");
		Element.enterData(testConfig, merchantPhoneNo, MerchantPhoneNo.trim(),
				"Merchant Phone No");

		String MerchantWebsite = data.GetData(createMerchantRow,
				"MerchantWebsite");
		MerchantDetails.put("MerchantWebsite", MerchantWebsite);
		Element.enterData(testConfig, merchantWebsite, MerchantWebsite,
				"Merchant Website");

		String MerchantCompanyName = data.GetData(createMerchantRow,
				"MerchantCompanyName");
		MerchantDetails.put("MerchantCompanyName", MerchantCompanyName);
		Element.enterData(testConfig, merchantCompanyName, MerchantCompanyName,
				"Merchant Company Name");

		String MerchantAddress = data.GetData(createMerchantRow,
				"MerchantAddress");
		Element.enterData(testConfig, merchantAddress, MerchantAddress,
				"Merchant Address");

		String MerchantCity = data.GetData(createMerchantRow, "MerchantCity");
		Element.enterData(testConfig, merchantCity, MerchantCity,
				"Merchant City");

		String MerchantState = data.GetData(createMerchantRow, "MerchantState");
		Element.selectValue(testConfig, merchantState, MerchantState,
				"Merchant State");

		String MerchantEmail = data.GetData(createMerchantRow, "MerchantEmail");
		MerchantDetails.put("MerchantEmail", MerchantEmail);
		Element.enterData(testConfig, merchantEmail, MerchantEmail,
				"Merchant Email");

		String MerchantCompanyDescription = data.GetData(createMerchantRow,
				"MerchantCompanyDescription");
		Element.enterData(testConfig, merchantCompanyDescription,
				MerchantCompanyDescription, "Merchant Company Description");

		String MerchantProductDescription = data.GetData(createMerchantRow,
				"MerchantProductDescription");
		Element.enterData(testConfig, merchantProductDescription,
				MerchantProductDescription, "Merchant Product Description");

		String MerchantAccountNumber = data.GetData(createMerchantRow,
				"MerchantAccountNumber");
		Element.enterData(testConfig, merchantAccountNumber,
				MerchantAccountNumber.trim(), "Merchant Account Number");

		String MerchantAccountHolderName = data.GetData(createMerchantRow,
				"MerchantAccountHolderName");
		Element.enterData(testConfig, merchantAccountHolderName,
				MerchantAccountHolderName, "Merchant Account Holder Name");

		String MerchantBankAccountType = data.GetData(createMerchantRow,
				"MerchantBankAccountType");
		Element.selectValue(testConfig, merchantBankAccountType,
				MerchantBankAccountType, "Merchant Bank Account Type");

		String MerchantBankName = data.GetData(createMerchantRow,
				"MerchantBankName");
		Element.enterData(testConfig, merchantBankName, MerchantBankName,
				"Merchant Bank Name");

		String MerchantBranchName = data.GetData(createMerchantRow,
				"MerchantBranchName");
		Element.enterData(testConfig, merchantBranchName, MerchantBranchName,
				"Merchant Branch Name");

		String MerchantIfscCode = data.GetData(createMerchantRow,
				"MerchantIfscCode");
		Element.enterData(testConfig, merchantIfscCode, MerchantIfscCode,
				"Merchant Ifsc code");

		String MerchantBranchCity = data.GetData(createMerchantRow,
				"MerchantBranchCity");
		Element.enterData(testConfig, merchantBranchCity, MerchantBranchCity,
				"Merchant Branch city");

		String RTGSCode = data.GetData(createMerchantRow, "RTGSCode");
		Element.enterData(testConfig, merchantRtgsCode, RTGSCode, "RTGS code");

		String PeformaCode = data.GetData(createMerchantRow, "PreformaCode");
		Element.enterData(testConfig, merchantPerformaCode, PeformaCode,
				"Merchant Branch city");

		return MerchantDetails;
	}

	/** submits all details of registration page
	 */
	public HomePage Submit()
	{
		Element.submit(testConfig, merchantBranchCity, "Merchant details");
		return new HomePage(testConfig);
	}

}
