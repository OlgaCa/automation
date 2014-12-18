package PageObject.AdminPanel.WebServices;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class TestwsPage {

	private Config testConfig;

	@FindBy(name="merchant_key")
	private WebElement merchantKey;

	@FindBy(name="command")
	private WebElement merchantCommand;

	@FindBy(name="var1")
	private WebElement inputVar1;

	@FindBy(name="var2")
	private WebElement inputVar2;

	@FindBy(name="var3")
	private WebElement inputVar3;

	@FindBy(name="var4")
	private WebElement inputVar4;

	@FindBy(name="var5")
	private WebElement inputVar5;

	@FindBy(name="var6")
	private WebElement inputVar6;

	@FindBy(name="var7")
	private WebElement inputVar7;

	@FindBy(name="var8")
	private WebElement inputVar8;
	
	@FindBy(name="var9")
	private WebElement inputVar9;

	@FindBy(css="input[type=\"submit\"]")
	private WebElement submit;

	public TestwsPage(Config testConfig) 
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, merchantKey);
	}

	/**
	 * Executes the WebService using 'WebServices' excel sheet, the required parameters must be set in run time property before calling this
	 * @param testConfig
	 * @param merchantkey key on which service is to be invoked
	 * @param webServiceRow excel row to be used to fill the details
	 * @return Webservice response page
	 */
	public TestwsResponsePage ExecuteWebService(String merchantkey, int webServiceRow)
	{
		TestDataReader webServiceData = new TestDataReader(testConfig,"WebServices");
		String comments = webServiceData.GetData(webServiceRow, "Comments");
		String command = webServiceData.GetData(webServiceRow, "Command");
		String parameters = Helper.replaceArgumentsWithRunTimeProperties(testConfig, webServiceData.GetData(webServiceRow, "Parameters").trim());

		//Execute the WebService
		testConfig.logComment("Executing '" + comments.toUpperCase() + "' web service on key - '" + merchantkey + "' with parameters '" + parameters + "'");

		Element.selectValue(testConfig, merchantKey, merchantkey, "Merchant Key");
		Element.selectValue(testConfig, merchantCommand, command, "Merchant Command");
		String utr = testConfig.getRunTimeProperty("utr");

		String [] params = parameters.split("&");
		String varValue = "";
		String varValue1ForUpdatesettlement = "";
		String varValue2ForUpdatesettlement = "";
		
		for(int i=1; i<=params.length; i++)
		{
			try
			{
				varValue = params[i-1].split("=")[1];
				if (webServiceRow==19){
					String varValues[] = params[i-1].split(",");
					varValue2ForUpdatesettlement = varValues[1];
					varValues = varValues[0].split("=");
					varValue1ForUpdatesettlement = varValues[1];
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				varValue = "";
			}
			switch(i)
			{
			case 1: 
				Element.enterData(testConfig, inputVar1, varValue, "Var1");
				
				if (webServiceRow==18){
					Element.enterData(testConfig, inputVar1, "{\""+varValue+"\":\""+utr+"\"}", "Var1");
				}
				if (webServiceRow==19){
					Element.enterData(testConfig, inputVar1, "{\""+varValue1ForUpdatesettlement+"\":\""+utr+"\",\""+varValue2ForUpdatesettlement+"\":\""+utr+"\"}", "Var1");
				}
				break;
			case 2: 
				Element.enterData(testConfig, inputVar2, varValue, "Var2");
				break;
			case 3: Element.enterData(testConfig, inputVar3, varValue, "Var3");
			break;
			case 4: Element.enterData(testConfig, inputVar4, varValue, "Var4");
			break;
			case 5: Element.enterData(testConfig, inputVar5, varValue, "Var5");
			break;
			case 6: Element.enterData(testConfig, inputVar6, varValue, "Var6");
			break;
			case 7: Element.enterData(testConfig, inputVar7, varValue, "Var7");
			break;
			case 8: Element.enterData(testConfig, inputVar8, varValue, "Var8");
			break;
			case 9: Element.enterData(testConfig, inputVar9, varValue, "Var9");
			break;
			}
		}

		Element.click(testConfig, submit, "Submit");
		return new TestwsResponsePage(testConfig);
	}
}
