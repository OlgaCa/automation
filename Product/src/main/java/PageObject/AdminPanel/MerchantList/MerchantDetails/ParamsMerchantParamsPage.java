package PageObject.AdminPanel.MerchantList.MerchantDetails;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestDataReader;


public class ParamsMerchantParamsPage extends ParamsPage{

	private Config testConfig;

	private String not_found="Not Found";

	@FindBy(name="submit")
	WebElement addKeyValue;

	@FindBy(xpath="//div[@id='merchantParams']/form/select")
	//@FindBy(css="form > select[name='key']")
	protected WebElement paramKey;

	/*
	@FindBy(xpath="(//input[@name='value'])[2]")
	protected WebElement paramValue; 
	 */
	//div[@id='merchantParams']/form/input[4]

	//@FindBy(xpath="//div[@id='merchantParams']/form/input[4]")
	@FindBy(css="#merchantParams > form > input[name='value']")
	protected WebElement paramValue;
	
	
	@FindBy(xpath=".//*[@id='merchantParams']/form[4]/input[6]")
	protected WebElement merchantPublicKey;
	
	@FindBy(xpath=".//*[@id='merchantParams']/form[3]/input[6]")
	protected WebElement merchantPrivatekey;


	public ParamsMerchantParamsPage(Config testConfig){
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,addKeyValue );
	}

	/*add the key and value
	 *@param rownumber in the params excel sheet
	 */
	public TestDataReader addKeyValue(int ParamsRow)
	{
		TestDataReader data = new TestDataReader(testConfig,"Params");
		String Key = data.GetData(ParamsRow, "key");
		Element.selectVisibleText(testConfig, paramKey, Key, "parameter key");
		String value = data.GetData(ParamsRow, "value");
		Element.enterData(testConfig, paramValue, value, "Value");
		Element.click(testConfig, addKeyValue, "Add Parameter's key and value");
		return data;
	}

	/*find the index of the form at which the key is located
	 * @param key to be searched
	 * @return index of the form
	 */
	private int findIndex(String key){
		int index=0;
		key=key+":";
		int xpathCount= testConfig.driver.findElements(By.xpath("//div[@id=\"merchantParams\"]/form")).size();
		String keyString ="";

		for( index=2;index<= xpathCount;index++){
			keyString = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"merchantParams\"]/form["+index+"]").getText();
			if(keyString.equalsIgnoreCase(key)){
				break;
			}
			if(index==xpathCount){
				index=0;
				break;
			}
		}

		return index;
	}

	/*Update the key and value	
	 * @param ParamsRow, Row No. of Params excel file
	 * @param data TestDataReader object
	 */	
	public void updateKeyValue(int ParamsRow, TestDataReader data)
	{
		String key="";
		String value="";

		key = data.GetData(ParamsRow, "key");
		value = data.GetData(ParamsRow, "value");

		updateKeyValue(key,value);				
	}

	/*Update the key and value
	 * @param key String to be updated
	 * @param value int 
	 */
	public void updateKeyValue(String key, String value){

		int i;	

		WebElement updated_value;
		WebElement save_button;

		i= findIndex(key);

		if(i!=0){
			updated_value = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"merchantParams\"]/form["+i+"]/input[5]");
			Element.enterData(testConfig, updated_value, value, "Value");
			save_button= Element.getPageElement(testConfig, How.xPath, "//div[@id=\"merchantParams\"]/form["+i+"]/input[6]");
			Element.click(testConfig, save_button, "updating parameter"+ key);
		}
	}

	/*remove the key from the merchant parameters
	 * @param ParamsRow, row number in Params sheet
	 * @param data, testDatareader
	 */
	public void removeKey(int ParamsRow, TestDataReader data)
	{
		String key="";

		key = data.GetData(ParamsRow, "key");
		removeKey(key);				
	}


	/*remove the key from the merchant parameters
	 * @param key String to be removed
	 */
	public void removeKey(String key){

		int i=0;	

		WebElement remove_button;

		i= findIndex(key);
		if(i!=0){
			remove_button= Element.getPageElement(testConfig, How.xPath, "//div[@id=\"merchantParams\"]/form["+i+"]/input[7]");
			if(remove_button == null) 
				testConfig.logFail("Could not find the parameter - "+ key);
			else
				Element.click(testConfig, remove_button, "Removing parameter - "+ key);
		}	
	} 

	/*read the value of the key from the merchant parameters
	 * @param key String to be removed
	 */
	public String readValue(String key){	
		String key_value ="";
		WebElement inputInbox =Element.getPageElement(
				testConfig,
				How.xPath,
				"//div[@id='merchantParams']/form[contains(.,'" + key
						+ ":')]/input[@name='value']"); 
		key_value= inputInbox.getAttribute("value");
		return key_value;
	}

	/*Verify the key and value in the merchant param tab
	 * @param testMerchantDataRow, row number in the Param sheet  
	 * @param data, testDatareader
	 * 
	 */
	public void verifyKeyValue(int testMerchantDataRow, TestDataReader data){
		String key = data.GetData(testMerchantDataRow, "key").trim();
		String value =  data.GetData(testMerchantDataRow, "value").trim();

		String key_value = readValue(key);

		Helper.compareEquals(testConfig, "key's value", value, key_value);		
	}


	/*Verify the key and value in the merchant param tab whether that key is removed or not
	 * @param testMerchantDataRow, row number in the Param sheet  
	 * @param data, testDatareader
	 * 
	 */
	public void verifyRemove(int testMerchantDataRow, TestDataReader data){
		String key = data.GetData(testMerchantDataRow, "key").trim();

		String key_value = readValue(key);
		Helper.compareEquals(testConfig, "Removed Param", not_found, key_value);
	}
	
	
	public String getMerchantPrivateKey(){
		return merchantPrivatekey.getAttribute("value").trim();
	}
	
	public String getMerchantPublicKey(){
		return merchantPublicKey.getAttribute("value").trim();
	}
	
	public void addMultipleKeys(ArrayList<Integer> paramRowNumbers){
		TestDataReader data;
		for (Integer rowNumber : paramRowNumbers) {
			data = addKeyValue(rowNumber);
			//wait for new page
			Browser.waitForPageLoad(this.testConfig,addKeyValue );
			verifyKeyValue(rowNumber, data);
		}
	}
}
