package PageObject.MerchantPanel.Offers;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.MerchantPanel.Home.MerchantTransactionsPage;
import Test.MerchantPanel.Offers.OffersHelper;
import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;
import Utils.Element.How;

public class OfferListPage {

	@FindBy(xpath = "//tbody")
	WebElement offersTable;

	@FindBy(linkText = "Create New Offer")
	WebElement createNewOfferLink;
	
	@FindBy(xpath="//td[9]")
	WebElement discount;

	private Config testConfig;
    public OffersHelper oHelper;
    
	public OfferListPage(Config testConfig) {
		this.testConfig = testConfig;
		// navigate to offer list page
				Browser.navigateToURL(testConfig,
						testConfig.getRunTimeProperty("OfferListUrl"));
				
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, createNewOfferLink);
	}

	public CreateNewOfferPage clickCreateNewOfferLink() {
		Element.click(testConfig, createNewOfferLink, "Create New Offer Link");

		return new CreateNewOfferPage(testConfig);
	}
	
	public String discount()
	{
		return discount.getText();
		
	}

	/**
	 * Click view transaction link for a given offer
	 * 
	 * @param offerKey
	 */
	public MerchantTransactionsPage clickViewTransactionsLink(String offerKey) {
		String xPath = "//a[@href='https://info.payu.in/merchant/transactions?offer[]="
				+ offerKey + "']/font";
		WebElement viewTrans = Element.getPageElement(testConfig, How.xPath, xPath);
		Element.doubleClick(testConfig, viewTrans, "click view trans");
		return new MerchantTransactionsPage(testConfig);

	}

		public void verifyUniqueOfferKey(List<WebElement> allRows)
	{
	boolean result = true;
	String[] offerKeys = oHelper.getOfferKeys(allRows);
	for(int i=0;i<offerKeys.length-1;i++)
	{
		for(int j=i+1;j<offerKeys.length-1;j++)
		{
			if(offerKeys[i].contentEquals(offerKeys[j])){
				result = false;
			}
		}
	}
	Helper.compareTrue(testConfig,"Offer key as unique" , result);
		
	}

	/**
	 * Verify offer details of latest offer from Offer List Table
	 * 
	 * @param offerRow
	 */
	public void verifyOfferEntry(int offerRowNum) {
		TestDataReader offerData = new TestDataReader(testConfig,
				"OfferDetails");
        oHelper = new OffersHelper(testConfig);
    	String expCC = testConfig.getRunTimeProperty("CC");
    	String expDC = testConfig.getRunTimeProperty("DC");
    	String expNB = testConfig.getRunTimeProperty("NB");
		// get all rows in the offer list table
		//List<WebElement> allRows = oHelper.getOfferListTable();
		
		// get offer entry row
		String[] actualValue = oHelper.getOfferRowEntry(testConfig.getRunTimeProperty("Offer Title"));
		// verify unique offer key TODO: change to equals
		//verifyUniqueOfferKey(allRows);
		
		Helper.compareEquals(testConfig, "Offer Title",
				testConfig.getRunTimeProperty("Offer Title"), actualValue[2]);
		Helper.compareEquals(testConfig, "Offer Description",
				offerData.GetData(offerRowNum, "Description"), actualValue[3]);

		String expectedValidFromDate = Helper.getCurrentDate("MMM/dd/yyyy")+ " 00:00:00";
	//	String expectedValidFromDate = Helper.getCurrentDate("MMM/dd/yyyy")+ " " + testConfig.getRunTimeProperty("Time");
		Helper.compareEquals(testConfig, "Offer Valid From Date and time", expectedValidFromDate, actualValue[4]);

		String expectedValidToDate = Helper.getDateBeforeOrAfterDays(1, "MMM/dd/yyyy")+ " 00:00:00";
		//String expectedValidToDate = Helper.getDateBeforeOrAfterDays(1, "MMM/dd/yyyy")+ " " + testConfig.getRunTimeProperty("Time");
		Helper.compareEquals(testConfig, "Offer Valid From Date and time", expectedValidToDate , actualValue[5]);
      
        if(!expDC.isEmpty() || !expCC.isEmpty()) 
        {
		Helper.compareEquals(testConfig, "Max Offer Count",
				offerData.GetData(offerRowNum, "Offer Count"), actualValue[6]);
		
		Helper.compareEquals(testConfig, "Remaining",
				offerData.GetData(offerRowNum, "Offer Count"), actualValue[7]);
		Helper.compareEquals(testConfig, "Discount",
				offerData.GetData(offerRowNum, "Discount")+ " "+ offerData.GetData(offerRowNum, "Discount Unit"), actualValue[9]);
		
		if(!expCC.isEmpty())
		{
		
			
		Helper.compareContains(testConfig, " CC Payment Details",expCC , actualValue[10]);//9
		}
		else {
			String expCC1 = offerData.GetData(1, "Blank Credit Card");
			Helper.compareEquals(testConfig, "Blank CC", expCC1, actualValue[10]);//9
		}
		if(!expDC.isEmpty())
		{
		Helper.compareContains(testConfig, " DC Payment Details", expDC, actualValue[11]);//10
		}
		else {
			String expDC1 = offerData.GetData(1, "Blank Blank Card");
			Helper.compareEquals(testConfig, "Blank DC", expDC1, actualValue[10]);//9
	
		}
		String expCCCardTypes = testConfig.getRunTimeProperty("ccCardTypes");
		String expDCCardTypes = testConfig.getRunTimeProperty("dcCardTypes");
		
		if(!expCCCardTypes.isEmpty())
		{
			String[] cType = expCCCardTypes.split("[,]");
            for(int i =0;i<cType.length;i++)
            {
			Helper.compareContains(testConfig, "CC Card Types",cType[i] , actualValue[10]);//9
            }
		}
		if(!expDCCardTypes.isEmpty())
		{
			String[] cType = expDCCardTypes.split("[,]");
            for(int i =0;i<cType.length;i++)
            {
			Helper.compareContains(testConfig, "DC Card Types", cType[i], actualValue[11]);//10
            }
		}
		if(!expNB.isEmpty())
		{
			String[] bType = expNB.split("[,]");
			for(int i=0;i<bType.length;i++)
			{
				Helper.compareContains(testConfig, "NB Card Types", bType[i], actualValue[12]);//11
		          
			}
		} // verify blank NB
		else{
			String expNB1 = offerData.GetData(1, "Blank NetBanking");
			Helper.compareEquals(testConfig, "Blank NB", expNB1, actualValue[12]);//11
		}
		
			}
	}

}
