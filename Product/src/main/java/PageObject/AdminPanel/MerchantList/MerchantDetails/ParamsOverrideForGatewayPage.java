
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
import Utils.Element.How;

public class ParamsOverrideForGatewayPage extends ParamsPage{

	private Config testConfig;

	private String not_found = "Not Found";

	@FindBy(name="pg_id")
	private WebElement param_gateway;

	@FindBy(xpath="//div[@id='gatewayParams']/form/select[2]")
	private WebElement param_key;

	@FindBy(xpath="//div[@id='gatewayParams']/form/input[4]")
	private WebElement param_value;

	@FindBy(name="submitPg")
	private WebElement submit_pg;

	public ParamsOverrideForGatewayPage(Config testConfig) {
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,override_for_gateway );		
	}

	/*To add Gateway Parameters
	 * @param paramRowNo, row number of the Params sheet
	 * @return data, object of TestDataReader
	 */
	public TestDataReader addGatewayParam(int paramRowNo){
		TestDataReader data = new TestDataReader(testConfig,"Params");

		String value = "";

		value = data.GetData(paramRowNo, "gateway");
		Element.selectVisibleText(testConfig, param_gateway, value, "gateway name");

		value = data.GetData(paramRowNo, "key");
		Element.selectVisibleText(testConfig, param_key, value, "Add Key");

		value = data.GetData(paramRowNo, "value");
		Element.enterData(testConfig, param_value, value, "Value");

		Element.click(testConfig, submit_pg, "Add the parameter");

		return data;	
	}


	/*readValue reads the value of the given key parameters of Override for Gateway
	 * @param payment_gateway, payment gateway to override
	 * @param key_name, key name
	 * @return the value of the key associated
	 */	
	public String readGatewayvalue(String payment_gateway, String key_name){

		String pg_name ="";
		payment_gateway="Gateway: "+payment_gateway;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='gp_pgid']")).size();
		/*formating the parameters to match with the payment parameters written on the page*/
		String other_param=key_name+":";

		String key_value= not_found;

		int j=0;
		int no_of_form=0; 
		int flag=0;
		for(int i=1;i<= no_of_gateway;i++){
			if(flag==1){
				break;
			}
			/*getting the payment Gateway String*/
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"gp_pgid\"]["+i+"]").getText();
			j=j+2;	

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='gatewayParams']/div["+j+"]/form")).size();
			String payment_gateway_name="";

			/* if gatewayname matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(payment_gateway)){
				for(int n=1;n<=no_of_form;n++){
					payment_gateway_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='gatewayParams']/div["+j+"]/form["+n+"]").getText();
					if(payment_gateway_name.equalsIgnoreCase (other_param)){
						flag=1;
						key_value = Element.getPageElement(testConfig, How.xPath,  "//div[@id='gatewayParams']/div["+j+"]/form["+n+"]/input[6]").getAttribute("value");

					}

				}

			}

		}
		return key_value;
	}


	/*To update the Payment Gateway parameters
	 * @param merchantParamRow, row number of the Params sheet
	 * @param data, object of TestDataReader
	 */
	public void updateGatewayKeyValue(int merchantParamsRow, TestDataReader data){
		String payment_gateway="";
		String key="";
		String value="";

		payment_gateway = data.GetData(merchantParamsRow, "gateway");
		key = data.GetCurrentEnvironmentData(merchantParamsRow, "key");
		value = data.GetData(merchantParamsRow, "value");

		updateGatewayKeyValue(payment_gateway, key, value);		
	}


	/** 
	 * update the specified value for the payment gateway parameters
	 * @param gateway_name whose value to be updated
	 * @param key_name whose value to be updated
	 * @param value, value of the key
	 */
	public void updateGatewayKeyValue(String payment_gateway, String key_name, String value){

		String pg_name ="";
		payment_gateway="Gateway: "+payment_gateway;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='gp_pgid']")).size();
		/*formating the parameters to match with the payment parameters written on the page*/
		String other_param=key_name+":";

		WebElement updated_value;
		WebElement save_button;

		int j=0;
		int no_of_form=0; 
		int flag=0;
		for(int i=1;i<= no_of_gateway;i++){
			if(flag==1){
				break;
			}
			/*getting the payment Gateway String*/
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"gp_pgid\"]["+i+"]").getText();
			j=j+2;	

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='gatewayParams']/div["+j+"]/form")).size();
			String payment_gateway_name="";

			/* if gatewayname matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(payment_gateway)){
				for(int n=1;n<=no_of_form;n++){
					payment_gateway_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='gatewayParams']/div["+j+"]/form["+n+"]").getText();
					if(payment_gateway_name.equalsIgnoreCase (other_param)){
						flag=1;
						updated_value = Element.getPageElement(testConfig, How.xPath,  "//div[@id='gatewayParams']/div["+j+"]/form["+n+"]/input[6]");
						Element.enterData(testConfig, updated_value, value, "Value");
						save_button= Element.getPageElement(testConfig, How.xPath,  "//div[@id='gatewayParams']/div["+j+"]/form["+n+"]/input[7]");
						Element.click(testConfig, save_button, "updating parameter "+ payment_gateway);

					}

				}

			}

		}

	}


	/*To remove the Payment Gateway parameters
	 * @param paramRowNo, row number of the Params sheet
	 * @param data, object of TestDataReader
	 */	
	public void removePaymentGatewayKeyValue(int merchantParamsRow, TestDataReader data){
		String payment_gateway="";
		String key="";

		payment_gateway = data.GetData(merchantParamsRow, "gateway");
		key = data.GetCurrentEnvironmentData(merchantParamsRow, "key");

		removePaymentGatewaynKeyValue(payment_gateway, key);		
	}


	/*To remove the Value of the given key parameters of payment gateway
	 * @param gateway_name name of the gateway
	 * @param key_name, key name
	 */
	public void removePaymentGatewaynKeyValue(String payment_gateway, String key_name){

		String pg_name ="";
		payment_gateway="Gateway: "+payment_gateway;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='gp_pgid']")).size();
		/*formating the parameters to match with the payment parameters written on the page*/
		String other_param=key_name+":";

		WebElement remove_button;

		int j=0;
		int no_of_form=0; 
		int flag=0;
		for(int i=1;i<= no_of_gateway;i++){
			if(flag==1){
				break;
			}
			/*getting the payment Gateway String*/
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"gp_pgid\"]["+i+"]").getText();
			j=j+2;

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='gatewayParams']/div["+j+"]/form")).size();
			String payment_gateway_name="";

			/* if gateway name matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(payment_gateway)){
				for(int n=1;n<=no_of_form;n++){
					payment_gateway_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='gatewayParams']/div["+j+"]/form["+n+"]").getText();
					if(payment_gateway_name.equalsIgnoreCase (other_param)){
						flag=1;
						remove_button= Element.getPageElement(testConfig, How.xPath,  "//div[@id='gatewayParams']/div["+j+"]/form["+n+"]/input[8]");
						Element.click(testConfig, remove_button, "removing parameter "+ payment_gateway);

					}

				}

			}

		}


	}

	/*To verify that the parameters are added/updated successfully for the Payment Gateway
	 *@param paramRowNo, row number of the Params sheet
	 *@param data, object of TestDataReader
	 */	
	public void verifyPaymentGatewayKeyValue(int paramRowNo, TestDataReader data){
		String payment_gateway = data.GetData(paramRowNo, "gateway").trim();		
		String key = data.GetData(paramRowNo, "key").trim();
		String value =  data.GetData(paramRowNo, "value").trim();

		String key_value = readGatewayvalue(payment_gateway, key);

		Helper.compareEquals(testConfig, "key's value", value, key_value);
	}


	/*To verify that the parameters are removed successfully for the Payment Gateway
	 *@param paramRowNo, row number of the Params sheet
	 *@param data, object of TestDataReader
	 */	
	public void verifyPaymentGatewayRemove(int paramRowNo, TestDataReader data){
		String payment_gateway = data.GetData(paramRowNo, "gateway").trim();
		String key = data.GetData(paramRowNo,"key");

		String  value = readGatewayvalue(payment_gateway, key);

		Helper.compareEquals(testConfig, "Removed Param", not_found, value);

	}
}
