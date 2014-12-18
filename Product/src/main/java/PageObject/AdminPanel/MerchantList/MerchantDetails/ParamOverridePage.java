package PageObject.AdminPanel.MerchantList.MerchantDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.Element.How;
import Utils.TestDataReader;

public class ParamOverridePage extends MerchantDetailsPage{

	Config testConfig;

	@FindBy(id="add_new_type")
	private  WebElement gateway;

	@FindBy(xpath="//div[5]/div[5]/div/div/form/div/select")
	private WebElement type;


	@FindBy(xpath="//div[@id='type_key_select_div']/select")
	private WebElement key;

	@FindBy(xpath="//div[@id='type_key_select_div']/input")
	private WebElement add_value;

	@FindBy(id="some")
	private WebElement submit;

	private String not_found = "Not Found";


	public ParamOverridePage(Config testConfig) {
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,submit );	
	}

	
	public TestDataReader addParamOverrideParameters(int RowNo)
	{
		TestDataReader data = new TestDataReader(testConfig,"Params");

		String value = "";

		value = data.GetCurrentEnvironmentData(RowNo, "gateway");
		Element.selectVisibleText(testConfig, gateway, value, "gateway");

		value = data.GetCurrentEnvironmentData(RowNo, "paymentoption");
		Element.selectVisibleText(testConfig, type, value, "type");

		value = data.GetCurrentEnvironmentData(RowNo, "key");
		Element.selectVisibleText(testConfig, key, value, "Add Key");

		value = data.GetData(RowNo, "value");
		Element.enterData(testConfig, add_value, value, "Value");

		Element.click(testConfig, submit, "Submit the query");

		return data;

	}

	public String readParamOverride(String gateway_string, String type_string, String key_string)
	{
		System.out.println("INSIDE READ PARAM OVERRIDE");
		int i;	

		String param_string = type_string +"\n" + key_string;
		gateway_string=gateway_string +":";

		int xpathCount= testConfig.driver.findElements(By.xpath("//div[5]/div[5]/div/div/form/div")).size();
		String keyString ="";

		WebElement span, param, param1;
		int flag = 0;
		String key_value=not_found;

		for( i=1;i<= xpathCount-2;i++){
			if(flag==1){        		
				break;
			}

			keyString = Element.getPageElement(testConfig, How.xPath, "//div["+i+"]/h4").getText();

			if(keyString.equals(gateway_string)){
				span =  Element.getPageElement(testConfig, How.xPath, "//div[5]/div[5]/div/div/form/div["+i+"]");
				Element.click(testConfig, span, "Selecting the gateway");        		
				param =  Element.getPageElement(testConfig, How.xPath, "//form/div["+ i +"]/div[2]");
				String type_name = param.getText();

				String type_name1[] = type_name.split("\n");

				param1 =  Element.getPageElement(testConfig, How.xPath, "//div["+ i +"]/div[2]/span");

				String key_namestring = param1.getText();
				String key_namestring1[] = key_namestring.split(":");  

				key_namestring = type_name1[0] + "\n" + key_namestring1[0];

				if(param_string.equalsIgnoreCase(key_namestring)){
					System.out.println("FOUND");
					flag=1;
					key_value = Element.getPageElement(testConfig, How.xPath, "//div["+ i +"]/div[2]/span/input[4]").getAttribute("value");			
				}		

			}

		}

		System.out.println("key_value is "+ key_value);
		return key_value;

	}


	public void removeGateway(int paramRowNo, TestDataReader data){
		String gateway="";
		String type="";
		String key="";

		gateway = data.GetData(paramRowNo, "gateway");
		type = data.GetData(paramRowNo, "paymentoption");	
		key = data.GetData(paramRowNo, "key");

		System.out.println(gateway + " "+ type + " " +key );
		removeGateway(gateway,type, key);		
	}

	public void removeGateway(String gateway, String type, String key){
		int i;	
		gateway=gateway +":";

		int xpathCount= testConfig.driver.findElements(By.xpath("//div[5]/div[5]/div/div/form/div")).size();
		System.out.println("laka count "+ xpathCount);
		String keyString ="";
		String param_string = type +"\n" + key;


		WebElement remove;
		WebElement span, param, param1;
		int flag = 0;
		for( i=1;i<= xpathCount-2;i++){
			if(flag==1)
				break;
			
			keyString = Element.getPageElement(testConfig, How.xPath, "//div["+i+"]/h4").getText();
			
			if(keyString.equals(gateway)){

				span =  Element.getPageElement(testConfig, How.xPath, "//div[5]/div[5]/div/div/form/div["+i+"]");
				Element.click(testConfig, span, "Selecting the gateway");        		
				param =  Element.getPageElement(testConfig, How.xPath, "//form/div["+ i +"]/div[2]");
				String type_name = param.getText();

				String type_name1[] = type_name.split("\n");

				param1 =  Element.getPageElement(testConfig, How.xPath, "//div["+ i +"]/div[2]/span");

				String key_namestring = param1.getText();
				String key_namestring1[] = key_namestring.split(":");  

				key_namestring = type_name1[0] + "\n" + key_namestring1[0];

				if(param_string.equalsIgnoreCase(key_namestring)){
					flag=1;
					remove =  Element.getPageElement(testConfig, How.xPath, "(//a[contains(text(),'Remove')])["+i+"]");
					Element.click(testConfig, remove, "removing the gateway");   		
					Element.click(testConfig, submit, "saving the removed gateway");
				}		

			}

		}   

	}


	/*To verify that the parameters are added/updated successfully for the category
	 *@param paramRowNo, row number of the Params sheet
	 *@param data, object of TestDataReader
	 */	
	public void verifyParamOverrideKeyValue(int testMerchantDataRow, TestDataReader data){
		String gateway = data.GetData(testMerchantDataRow, "gateway").trim();		
		String type = data.GetData(testMerchantDataRow, "paymentoption").trim();
		String key = data.GetData(testMerchantDataRow, "key").trim();
		String value =  data.GetData(testMerchantDataRow, "value").trim();
		String key_value = readParamOverride(gateway, type, key);

		Helper.compareEquals(testConfig, "key's value", value, key_value);

	}


	public void verifyParamOverrideRemove(int testMerchantDataRow, TestDataReader data){
		String gateway = data.GetData(testMerchantDataRow, "gateway").trim();		
		String type = data.GetData(testMerchantDataRow, "paymentoption").trim();
		String key = data.GetData(testMerchantDataRow, "key").trim();

		String key_value = readParamOverride(gateway, type, key);

		Helper.compareEquals(testConfig, "key's value", not_found, key_value);

	}


}
