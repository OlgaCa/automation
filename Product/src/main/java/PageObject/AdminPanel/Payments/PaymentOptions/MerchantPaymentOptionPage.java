package PageObject.AdminPanel.Payments.PaymentOptions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;

public class MerchantPaymentOptionPage {
	public static WebDriver driver;

	/**
	 * Find webelement for credit card tab and clicks on credit card tab
	 * @return MerchantTransactionPaymentCCPage
	 */

	public MerchantCCTab clickCCTab() {
		WebElement creditCard;
		creditCard = Element.getiFrameElement(testConfig, How.xPath, "//li[@id=1]");
		Element.click(testConfig, creditCard, "Credit Card Tab");
		return new MerchantCCTab(testConfig);
	}

	/**
	 * Find webelement for debit card tab and clicks on debit card tab
	 * @return MerchantTransactionPaymentDCPage
	 */
	public MerchantDCTab clickDCTab() {
		WebElement debitCard;
		debitCard = Element.getiFrameElement(testConfig, How.xPath, "//li[@id=2]");
		Element.click(testConfig, debitCard, "Debit Card Tab");
		return new MerchantDCTab(testConfig);
	}

	/**
	 * Find webelement for Net Banking tab and clicks on Net Banking tab
	 * @return MerchantTransactionPaymentNBPage
	 */

	public MerchantNBTab clickNBTab() {
		WebElement NetBanking;
		NetBanking = Element.getiFrameElement(testConfig, How.xPath, "//li[@id=3]");
		Element.click(testConfig, NetBanking, "NetBanking Tab");
		return new MerchantNBTab(testConfig);
	}

	/**
	 * Find webelement for EMI tab and clicks on EMI tab
	 * @return MerchantTransactionPaymentEMIPage
	 */

	public MerchantEMITab clickEMITab() {
		WebElement EMI;
		EMI = Element.getiFrameElement(testConfig, How.xPath, "//li[@id=4]");
		Element.click(testConfig, EMI, "EMI Tab");
		return new MerchantEMITab(testConfig);
	}


	@FindBy(id="totalAmount")
	private WebElement amount;
	
	@FindBy(css=".pu_cont ul")
	private WebElement iframe;
	
	private Config testConfig;
	public MerchantPaymentOptionPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Element.getiFrameElement(testConfig, How.css, ".pu_cont ul");
	}	

}
