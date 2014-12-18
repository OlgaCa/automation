package PageObject.AdminPanel.MerchantList.MerchantDetails;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Test.MerchantPanel.Payments.TestAutoRefund.verifyReconResult;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class EditPage extends EditMerchantPage{

	Config testConfig;

	@FindBy(xpath="//div[@id='merchantEdit']/h2")
	protected WebElement merhant_edit;
	
	@FindBy(name="editMerchant[name]")
	private WebElement merchant_name;	
	
	@FindBy(name="editMerchant[phone]")
	private WebElement merchant_phone;
	
	@FindBy(name="editMerchant[email]")
	private WebElement merchant_email;
					
	@FindBy(name="editMerchant[address]")
	private WebElement merchant_address;
	
	@FindBy(name="editMerchant[city]")
	private WebElement merchant_city;
	
	@FindBy(name="editMerchant[state]")
	private WebElement merchant_state;

	@FindBy(name="editMerchant[url]")
	private WebElement merchant_url;
	
	@FindBy(name="editMerchant[fax]")
	private WebElement merchant_fax;
	
	@FindBy(name="editMerchant[company_name]")
	private WebElement company_name;
	
	@FindBy(name="editMerchant[company_desc]")
	private WebElement company_desc;

	@FindBy(name="editMerchant[product_desc]")
	private WebElement product_desc;
	
	@FindBy(name="editMerchant[account_type]")
	private WebElement account_type;
	
	@FindBy(name="editMerchant[account_num]")
	private WebElement account_num;
	
	@FindBy(name="editMerchant[account_holder]")
	private WebElement account_holder;
	
	@FindBy(name="editMerchant[bank_acc_type]")
	private WebElement bank_acc_type;

	@FindBy(name="editMerchant[bank_name]")
	private WebElement bank_name;
	
	@FindBy(name="editMerchant[branch_name]")
	private WebElement branch_name;

	@FindBy(name="editMerchant[ifsc_code]")
	private WebElement ifsc_code;
	
	@FindBy(name="editMerchant[preforma_code]")
	private WebElement preforma_code;
			
	@FindBy(name="editMerchant[rtgs_code]")
	private WebElement rtgs_code;
	
	@FindBy(name="editMerchant[branch_city]")
	private WebElement branch_city;
	
	@FindBy(name="editMerchant[owners_desc]")
	private WebElement owners_desc;
	
	@FindBy(name="editMerchant[funding_desc]")
	private WebElement funding_desc;
	
	@FindBy(name="editMerchant[logistics_desc]")
	private WebElement logistics_desc;

	@FindBy(name="editMerchant[min_price]")
	private WebElement min_price;
	
	@FindBy(name="editMerchant[max_price]")
	private WebElement max_price;
	
	@FindBy(name="editMerchant[txn_num_pm]")
	private WebElement txn_num_pm;
	
	@FindBy(name="editMerchant[cc_fee]")
	private WebElement cc_fee;
	
	@FindBy(name="editMerchant[nb_fee]")
	private WebElement nb_fee;
	
	@FindBy(name="editMerchant[dc_fee]")
	private WebElement dc_fee;

	@FindBy(name="editMerchant[emi_fee]")
	private WebElement emi_fee;
	
	@FindBy(name="editMerchant[ivr_fee]")
	private WebElement ivr_fee;
	
	@FindBy(name="editMerchant[retryallowed]")
	private WebElement retry_allowed;
	
	@FindBy(name="editMerchant[auto_capture_limit]")
	private WebElement auto_capture_limit;

	@FindBy(name="editMerchant[autorefund]")
	private WebElement autorefund;
	
	@FindBy(name="email[failed_email]")
	private WebElement failed_email;
	
	@FindBy(name="email[dropped_email]")
	private WebElement dropped_email;
	
	@FindBy(name="email[bounced_email]")
	private WebElement bounced_email;
	
	@FindBy(name="editMerchant[currency_type]")
	private WebElement currancy_type;

	@FindBy(name="submit_merch_details")
	private WebElement submit_details;
		
	MerchantDetailsPage merchantDetailsPage;
	EditMerchantPage editMerchantPage;
	

	public EditPage(Config testConfig) {
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,submit_details );	
	}
	
	public void EditMerchantDetails(TestDataReader data, int merchantDetailsRowNo)
	{ 

			String value = "";

			value = data.GetData(merchantDetailsRowNo, "MerchantName");
			Element.enterData(testConfig, merchant_name, value, "Merchant name");

			value = data.GetData(merchantDetailsRowNo, "MerchantEmail");
			Element.enterData(testConfig, merchant_email, value, "Merchant Email");

			value = data.GetData(merchantDetailsRowNo, "MerchantAddress");
			Element.enterData(testConfig, merchant_address, value, "Merchant Address");

			value = data.GetData(merchantDetailsRowNo, "MerchantCity");
			Element.enterData(testConfig, merchant_city, value, "Merchant City");

			value = data.GetData(merchantDetailsRowNo, "MerchantState");
			Element.selectVisibleText(testConfig, merchant_state, value, "Merchant State");
			
			value = data.GetData(merchantDetailsRowNo, "MerchantPhoneNo");
			Element.enterData(testConfig, merchant_phone, value.trim(), "Merchant Phone No");

			value = data.GetData(merchantDetailsRowNo, "MerchantWebsite");
			Element.enterData(testConfig, merchant_url, value, "Merchant Website");

			value = data.GetData(merchantDetailsRowNo, "FaxNumber");
			Element.enterData(testConfig, merchant_fax, value, "Merchant fax number");

			value = data.GetData(merchantDetailsRowNo, "MerchantCompanyName");
			Element.enterData(testConfig, company_name, value, "Merchant Company Name");			
			
			value = data.GetData(merchantDetailsRowNo, "MerchantCompanyDescription");
			Element.enterData(testConfig, company_desc, value, "Merchant Company Description");

			value = data.GetData(merchantDetailsRowNo, "MerchantProductDescription");
			Element.enterData(testConfig, product_desc, value, "Merchant Product Description");

			value = data.GetData(merchantDetailsRowNo, "PayuAccountType");
			Element.selectVisibleText(testConfig, account_type, value, "Add Key");

			value = data.GetData(merchantDetailsRowNo, "MerchantAccountNumber");
			Element.enterData(testConfig, account_num, value.trim(), "Merchant Account Number");

			value = data.GetData(merchantDetailsRowNo, "MerchantAccountHolderName");
			Element.enterData(testConfig, account_holder, value, "Merchant Account Holder Name");

			value = data.GetData(merchantDetailsRowNo, "MerchantBankAccountType");
			Element.selectVisibleText(testConfig, bank_acc_type, value, "Merchant Bank Account Type");

			value = data.GetData(merchantDetailsRowNo, "MerchantBankName");
			Element.enterData(testConfig, bank_name, value, "Merchant Bank Name");

			value = data.GetData(merchantDetailsRowNo, "MerchantBranchName");
			Element.enterData(testConfig, branch_name, value, "Merchant Branch Name");

			value = data.GetData(merchantDetailsRowNo, "MerchantIfscCode");
			Element.enterData(testConfig, ifsc_code, value, "Merchant Ifsc code");

			value = data.GetData(merchantDetailsRowNo, "PreformaCode");
			Element.enterData(testConfig, preforma_code, value, "Preforma code");

			value = data.GetData(merchantDetailsRowNo, "RTGSCode");
			Element.enterData(testConfig, rtgs_code, value, "RTGS code");
			
			value = data.GetData(merchantDetailsRowNo, "MerchantBranchCity");
			Element.enterData(testConfig, branch_city, value, "Merchant Branch city");
	
			value = data.GetData(merchantDetailsRowNo, "OwnersDescription");
			Element.enterData(testConfig, owners_desc, value, "Owner Description");

			value = data.GetData(merchantDetailsRowNo, "FundingDescription");
			Element.enterData(testConfig, funding_desc, value, "Funding Description");

			value = data.GetData(merchantDetailsRowNo, "LogisticsDesciption");
			Element.enterData(testConfig, logistics_desc, value, "Logistic Description");

			value = data.GetData(merchantDetailsRowNo, "MinPrice");
			Element.enterData(testConfig, min_price, value, "Minimum Price");
	
			value = data.GetData(merchantDetailsRowNo, "MaxPrice");
			Element.enterData(testConfig, max_price, value, "Maximum Price");
			
			value = data.GetData(merchantDetailsRowNo, "TxnPerMonth");
			Element.enterData(testConfig, txn_num_pm, value, "Number of Transaction Per Month");
			
			value = data.GetData(merchantDetailsRowNo, "CCFee");
			Element.enterData(testConfig, cc_fee, value, "CC Fees");
			
			value = data.GetData(merchantDetailsRowNo, "NBFee");
			Element.enterData(testConfig, nb_fee, value, "NB Fees");
			
			value = data.GetData(merchantDetailsRowNo, "DCFee");
			Element.enterData(testConfig, dc_fee, value, "DC Fees");
			
			value = data.GetData(merchantDetailsRowNo, "EMIFee");
			Element.enterData(testConfig, emi_fee, value, "EMI Fees");
						
			value = data.GetData(merchantDetailsRowNo, "IVRFee");
			Element.enterData(testConfig, ivr_fee, value, "IVR Fee");
			
			value = data.GetData(merchantDetailsRowNo, "RetryAllowed");
			Element.enterData(testConfig, retry_allowed, value, "Retry Allowed Per Transaction");
			
			value = data.GetData(merchantDetailsRowNo, "AutoCaptureLimit");
			Element.enterData(testConfig, auto_capture_limit, value, "Auto Capture Limit");
			
			value = data.GetData(merchantDetailsRowNo, "AutoRefund");
			Element.enterData(testConfig, autorefund, value, "Auto Refund");
			
			value = data.GetData(merchantDetailsRowNo, "Currency");
			Element.selectVisibleText(testConfig, currancy_type, value, "Currency");
						
			Element.click(testConfig, submit_details, "Editing the merchant Details");
			
	}
		
	public void VerifyMerchantEditDetails(TestDataReader data, int editMerchantRowNo)
	{
		String merchantName = data.GetData(editMerchantRowNo, "MerchantName").trim();
		String merchantEmail = data.GetData(editMerchantRowNo, "MerchantEmail").trim();
		String merchantAddress = data.GetData(editMerchantRowNo, "MerchantAddress").trim();
		String merchantCity = data.GetData(editMerchantRowNo, "MerchantCity").trim();
		String merchantState = data.GetData(editMerchantRowNo, "MerchantState").trim();
		String merchantPhoneNumber = data.GetData(editMerchantRowNo, "MerchantPhoneNo");
		String merchantWebsite = data.GetData(editMerchantRowNo, "MerchantWebsite").trim();
		String merchantFaxNumber = data.GetData(editMerchantRowNo, "FaxNumber").trim();		
		String merchantCompanyName = data.GetData(editMerchantRowNo, "MerchantCompanyName").trim();
		String merchantCompanyDescription = data.GetData(editMerchantRowNo, "MerchantCompanyDescription").trim();
		String merchantProductDescription = data.GetData(editMerchantRowNo, "MerchantProductDescription").trim();		
		String merchantPayuAccountType = data.GetData(editMerchantRowNo, "PayuAccountType").trim();	
		String merchantAccountNumber =  data.GetData(editMerchantRowNo, "MerchantAccountNumber").trim();	
		String merchantAccountHolder = data.GetData(editMerchantRowNo, "MerchantAccountHolderName").trim();
		String merchantBankAccountType = data.GetData(editMerchantRowNo, "MerchantBankAccountType").trim();
		String merchantBankName = data.GetData(editMerchantRowNo, "MerchantBankName").trim();
		String merchantBranchName = data.GetData(editMerchantRowNo, "MerchantBranchName").trim();
		String merchantIfscCode = data.GetData(editMerchantRowNo, "MerchantIfscCode").trim();
		String merchantPreformaCode = data.GetData(editMerchantRowNo, "PreformaCode").trim();
		String merchantRtgsCode = data.GetData(editMerchantRowNo, "RTGSCode").trim();
		String merchantBranchCity = data.GetData(editMerchantRowNo, "MerchantBranchCity").trim();
		String ownersDescription =  data.GetData(editMerchantRowNo, "OwnersDescription").trim();		
		String fundingDescription =  data.GetData(editMerchantRowNo, "FundingDescription").trim();		
		String logisticsDesciption =  data.GetData(editMerchantRowNo, "LogisticsDesciption").trim();		
		String minPrice =  data.GetData(editMerchantRowNo, "MinPrice").trim();		
		String maxPrice =  data.GetData(editMerchantRowNo, "MaxPrice").trim();		
		String txnPerMonth =  data.GetData(editMerchantRowNo, "TxnPerMonth").trim();						
		String ccFee =  data.GetData(editMerchantRowNo, "CCFee").trim();		
		String nbFee =  data.GetData(editMerchantRowNo, "NBFee").trim();		
		String dcFee =  data.GetData(editMerchantRowNo, "DCFee").trim();	
		String emiFee =  data.GetData(editMerchantRowNo, "EMIFee").trim();		
		String ivrFee =  data.GetData(editMerchantRowNo, "IVRFee").trim();		
		String retryAllowed =  data.GetData(editMerchantRowNo, "RetryAllowed").trim();		
		String autoCaptureLimit =  data.GetData(editMerchantRowNo, "AutoCaptureLimit").trim();		
		String autoRefund =  data.GetData(editMerchantRowNo, "AutoRefund").trim();		
		String currency =  data.GetData(editMerchantRowNo, "Currency").trim();
			
			
		Helper.compareEquals(testConfig, "Merchant name", merchantName, merchant_name.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant email", merchantEmail, merchant_email.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant address", merchantAddress, merchant_address.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant city", merchantCity, merchant_city.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant state", merchantState, merchant_state.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant phone number", merchantPhoneNumber, merchant_phone.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant website", merchantWebsite, merchant_url.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant Fax Number", merchantFaxNumber, merchant_fax.getAttribute("value"));		
		Helper.compareEquals(testConfig, "Merchant company name", merchantCompanyName, company_name.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant company description", merchantCompanyDescription, company_desc.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant product description", merchantProductDescription, product_desc.getAttribute("value"));
		Helper.compareEquals(testConfig, "Payu Account Type", merchantPayuAccountType, Element.getFirstSelectedOption(testConfig, account_type, "Payu Account Type").getText());		
		Helper.compareEquals(testConfig, "Merchant Account Number", merchantAccountNumber, account_num.getAttribute("value"));		
		Helper.compareEquals(testConfig, "Merchant account holder", merchantAccountHolder, account_holder.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant bank account", merchantBankAccountType,Element.getFirstSelectedOption(testConfig, bank_acc_type, "Merchant bank account").getText());
		Helper.compareEquals(testConfig, "Merchant bank name", merchantBankName, bank_name.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant branch name", merchantBranchName, branch_name.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant bank ifsc", merchantIfscCode, ifsc_code.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant preforma code", merchantPreformaCode, preforma_code.getAttribute("value"));
		Helper.compareEquals(testConfig, "Merchant RTGS code", merchantRtgsCode, rtgs_code.getAttribute("value"));		
		Helper.compareEquals(testConfig, "Merchant branch city", merchantBranchCity, branch_city.getAttribute("value"));
		Helper.compareEquals(testConfig, "Owner Description", ownersDescription, owners_desc.getAttribute("value"));
		Helper.compareEquals(testConfig, "Funding Description", fundingDescription, funding_desc.getAttribute("value"));
		Helper.compareEquals(testConfig, "Logistics Desciption", logisticsDesciption, logistics_desc.getAttribute("value"));
		Helper.compareEquals(testConfig, "Minimum Price", minPrice, min_price.getAttribute("value"));
		Helper.compareEquals(testConfig, "Maximum Price", maxPrice, max_price.getAttribute("value"));
		Helper.compareEquals(testConfig, "Number of Transaction Per Month", txnPerMonth, txn_num_pm.getAttribute("value"));
		Helper.compareEquals(testConfig, "CC Fee", ccFee, cc_fee.getAttribute("value"));
		Helper.compareEquals(testConfig, "NB Fee", nbFee, nb_fee.getAttribute("value"));
		Helper.compareEquals(testConfig, "DC Fee", dcFee, dc_fee.getAttribute("value"));
		Helper.compareEquals(testConfig, "EMI Fee", emiFee, emi_fee.getAttribute("value"));
		Helper.compareEquals(testConfig, "IVR Fee", ivrFee, ivr_fee.getAttribute("value"));
		Helper.compareEquals(testConfig, "Retry Allowed Per Transaction", retryAllowed, retry_allowed.getAttribute("value"));
		Helper.compareEquals(testConfig, "Auto Capture Limit", autoCaptureLimit, auto_capture_limit.getAttribute("value"));
		Helper.compareEquals(testConfig, "Auto Refund", autoRefund, autorefund.getAttribute("value"));
		Helper.compareEquals(testConfig, "Currency Type", currency,Element.getFirstSelectedOption(testConfig, currancy_type, "Currency Type").getText());
		
	}
		
	public String getMerchantWebsite()
	{
		System.out.println("Inside getMerchantWebSite");
		return merchant_url.getAttribute("value");
	}

	public String getCurrentCurrency()
	{
		return Element.getFirstSelectedOption(testConfig, currancy_type, "Currency Type").getText();	
	}
	
	
	public int getParamRowtoModify(TestDataReader data){
		
		System.out.println("Inside getParamRowtoModify");
		int paramRowNo = 2;
		
		String current_mechant_site = getMerchantWebsite();
		System.out.println("current_mechant_site "+current_mechant_site);
		System.out.println("checking with  "+data.GetData(paramRowNo, "MerchantWebsite"));
		
		if(current_mechant_site.equalsIgnoreCase(data.GetData(paramRowNo, "MerchantWebsite"))){
			paramRowNo=3;
		}
		
		System.out.println("returning paramRowNo as "+paramRowNo );
		return paramRowNo;
	}
	
	public void AutoRefundOn()
	{ 			
		Element.enterData(testConfig, autorefund, "1", "Auto Refund");						
	}
		
	public void AutoRefundOff( ){
		Element.enterData(testConfig, autorefund, "0", "Auto Refund");						
		
	}
	
	public void changeCurrencyType(String currencyType)
	{
		Element.selectVisibleText(testConfig, currancy_type, currencyType, "Currency");
	}
	
	public void submit_changes()
	{
		Element.click(testConfig, submit_details, "Editing the merchant Details");	
	}
	
	public void setEmailNotification(verifyReconResult status, int setBit)
	{	
		String status_check="";
		if(setBit==0)
			status_check = "off"; // on/off
		else
			status_check="on";
		
		String currentStatus = failed_email.getAttribute("checked");
		if ((status_check.equalsIgnoreCase("on") && currentStatus==null) || (status_check.equalsIgnoreCase("off") && currentStatus != null)){
			Element.click(testConfig, failed_email, "failed email");
		}
	}

	
}



