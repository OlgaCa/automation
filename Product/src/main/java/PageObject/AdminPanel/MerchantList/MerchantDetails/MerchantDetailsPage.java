package PageObject.AdminPanel.MerchantList.MerchantDetails;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;

public class MerchantDetailsPage {

	private Config testConfig;

	@FindBy(name="PayuNetbanking")
	private WebElement PayuNetbanking;

	@FindBy(linkText="Payment Options")
	private WebElement payment_options;

	@FindBy(linkText="Payment Types")
	private WebElement payment_types;

	@FindBy(linkText="Params")
	private WebElement params;

	@FindBy(linkText="Merchant Auth Creds")
	private WebElement merchant_auth;

	@FindBy(linkText="Edit Merchant")
	private WebElement edit_merchant;

	@FindBy(linkText="Param Override")
	private WebElement param_override;

	@FindBy(linkText="TDR List")
	private WebElement tdr_list;
	
	@FindBy(linkText="Refund TDR")
	private WebElement refund_tdr;

	@FindBy(linkText="Pg Keys")
	private WebElement pgKeysTab;
	
	public MerchantDetailsPage()	{
	}

	public MerchantDetailsPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, params);
	}

	/* Returns the object of Params */
	public ParamsPage ClickParams()
	{
		Element.click(testConfig, params, "Params tab");
		return new ParamsPage(testConfig);
	}

	/* Returns the object of Edit Merchant */
	public EditMerchantPage ClickEditMerchant()
	{
		Element.click(testConfig, edit_merchant, "Edit Merchant tab");
		return new EditMerchantPage(testConfig);
	}


	public ParamOverridePage ClickParamOverride()
	{
		Element.click(testConfig, param_override, "Param Override tab");
		return new ParamOverridePage(testConfig);
	}

	/**
	 * returns paymenttypes page object
	 */
	public PaymentTypesPage clickPaymentOptions()
	{
		Element.click(testConfig, payment_options, "Payment Options Page");
		Element.click(testConfig, payment_types, "Payment Types tab");
		return new PaymentTypesPage(testConfig);
	}


	/**
	 * returns PGKeys page object
	 */
	public PGKeysPage clickPGKeys()
	{
		Element.click(testConfig, payment_options, "Payment Options Page");
		Element.click(testConfig, pgKeysTab, "PG Keys tab");
		return new PGKeysPage(testConfig);
	}

	public TDRList ClickTDRList(Config testConfig)
	{
		this.testConfig = testConfig;
		Element.click(testConfig, tdr_list, "TDRList tab");
		return new TDRList(testConfig);
	}
	
	public RefundTDRList ClickRefundTDRList(Config testConfig)
	{
		this.testConfig = testConfig;
		Element.click(testConfig, tdr_list, "TDRList tab");
		Element.click(testConfig, refund_tdr, "Refund TDR tab");
		return new RefundTDRList(testConfig);
	}
	
	public String CheckNetBankingCount(Config testConfig)
	{
		this.testConfig = testConfig;
		String text = Element.getPageElement(testConfig, How.css, "div#tabs1 div#subtabs ul li:nth-child(1)").getText();
		return text;
		
	}

}
