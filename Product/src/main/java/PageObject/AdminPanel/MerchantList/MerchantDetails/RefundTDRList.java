package PageObject.AdminPanel.MerchantList.MerchantDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class RefundTDRList extends MerchantDetailsPage{

	Config testConfig;

	@FindBy(id="refundgateway")
	private WebElement refundGateway;

	@FindBy(name="daysrange")
	private WebElement refundDaysRange;

	@FindBy(name="refundTDR")
	private WebElement refundTDRAdd;

	//@FindBy(css="tr[class='hdng']+tr>td")
	@FindBy(css="#refundTDR > div.tdr_ctgry > table >tbody >tr + tr >td")
	private WebElement gatewayAdded;

	//@FindBy(css="tr[class='hdng']+tr>td+td")
	@FindBy(css="#refundTDR > div.tdr_ctgry > table >tbody >tr + tr >td + td")
	private WebElement daysRangeAdded;

	//@FindBy(css="tr[class='hdng']+tr>td+td+td>form>input:nth-child(6)")
	@FindBy(css="#refundTDR>div.tdr_ctgry>table>tbody>tr+tr>td:nth-child(3)>form>input[value='Remove']")
	private WebElement refundTDRRemove;

	public RefundTDRList(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, refundTDRAdd);	
	}

	public void addRefundTDRRule(int RowNo){		
		TestDataReader data = new TestDataReader(testConfig,"RefundTDR");
		String value = "";

		value = data.GetCurrentEnvironmentData(RowNo, "Value");
		//value for AXIS gateway is 7
		Element.selectValue(testConfig, refundGateway, value, "Select gateway");

		value = data.GetCurrentEnvironmentData(RowNo, "DaysRange");
		Element.enterData(testConfig, refundDaysRange, value, "Select days range");

		Element.click(testConfig, refundTDRAdd , "Add the Parameters of Refund TDR");				
	}

	public void verifyRefundTDR(int RowNo){
		TestDataReader data = new TestDataReader(testConfig,"RefundTDR");

		Helper.compareEquals(testConfig, "Gateway", data.GetCurrentEnvironmentData(RowNo, "Gateway"), gatewayAdded.getText());
		Helper.compareEquals(testConfig, "No. of Days", data.GetCurrentEnvironmentData(RowNo, "DaysRange"), daysRangeAdded.getText());

	}

	public void removeRefundTDR(Config testConfig){
		Element.click(testConfig, refundTDRRemove, "Remove a Refund TDR rule");
	}

	public void verifyRefundTDRnotPresent(Config testConfig){		
		int size=testConfig.driver.findElements(By.cssSelector("#refundTDR > div.tdr_ctgry > table > tbody > tr:nth-child(2)")).size();
		Helper.compareEquals(testConfig, "Refund TDR rule not present-Size of Refund TDR rule", 0,size);				
	}

}
