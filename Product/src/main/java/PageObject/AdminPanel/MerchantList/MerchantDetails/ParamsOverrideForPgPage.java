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

public class ParamsOverrideForPgPage extends ParamsPage {

	private Config testConfig;

	@FindBy(linkText = "Override For Payment Type")
	private WebElement override_for_payment_type;

	@FindBy(linkText = "Override For Pg-Category")
	private WebElement override_for_pg_category;

	@FindBy(css = "div[id='paymentTypeParams'] select[name='pg_id']")
	protected WebElement gateway;

	@FindBy(css = "div[id='paymentTypeParams'] select[name='ibibo_code']")
	protected WebElement paymentOption;

	@FindBy(css = "div[id='paymentTypeParams'] select[name='key']")
	protected WebElement paramKey;

	@FindBy(css = "div[id='paymentTypeParams'] input[name='value']")
	protected WebElement paramValue;

	@FindBy(css = "div[id='pgCategoryParams'] select[name='pg_id']")
	protected WebElement pg_gateway;

	@FindBy(css = "div[id='pgCategoryParams'] select[name='category']")
	protected WebElement pg_category;

	@FindBy(css = "div[id='pgCategoryParams'] select[name='key']")
	protected WebElement pg_paramKey;

	@FindBy(css = "div[id='pgCategoryParams'] input[name='value']")
	protected WebElement pg_paramValue;

	@FindBy(name = "submitPgCat")
	private WebElement addCategoryParam;

	@FindBy(xpath = "//input[@name='submitPt']")
	private WebElement addPaymentTypeParam;

	private String not_found= "Not Found";

	public ParamsOverrideForPgPage(Config testConfig) {
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, override_for_payment_type);
	}

	/*clicks the Override for Payment Type Tab*/
	public void clickOverrideForPaymentType() {
		Element.click(testConfig, override_for_payment_type,
				"override_for_payment_type tab");
	}

	/*clicks the Override for Pg Category Tab*/
	public void clickOverrideForPgCategory() {
		Element.click(testConfig, override_for_pg_category,
				"override_for_pg_category tab");
	}

	/* to add parameter on PaymentType tab 
	 * @param rowno of the parameter in the Params sheet
	 * @return data, object of TestDataReader 
	 */
	public TestDataReader addPaymentTypeParameters(int paramRowNo) {
		TestDataReader data = new TestDataReader(testConfig, "Params");

		String value = "";

		value = data.GetData(paramRowNo, "gateway");
		Element.selectVisibleText(testConfig, gateway, value, "gateway");

		value = data.GetData(paramRowNo, "paymentoption");
		Element.selectVisibleText(testConfig, paymentOption, value,
				"paymentOption");

		value = data.GetData(paramRowNo, "key");
		Element.selectVisibleText(testConfig, paramKey, value, "paramKey");

		value = data.GetData(paramRowNo, "value");
		Element.enterData(testConfig, paramValue, value, "Value");

		Element.click(testConfig, addPaymentTypeParam,
				"Add Parameter's gateway, paymentOption, key and value");

		return data;
	}


	/*readValue reads the value of the given key parameters of Payment Type
	 * @param gateway_name name of the gateway
	 * @param payment_option, payment option for that gateway
	 * @param key_name, key name
	 * @return the value of the key associated
	 */	
	public String readPaymentTypeValue(String gateway_name,String payment_option, String key_name){

		String pg_name ="";
		gateway_name="Gateway: "+gateway_name;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='gateway_pgId']")).size();

		/*formating the parameters to match with the payment parameters written on the page*/
		String other_param=payment_option+"\n"+key_name+":";

		String key_value=not_found;

		int j=0;
		int no_of_form=0; 
		int flag=0;
		for(int i=1;i<= no_of_gateway;i++){
			if(flag==1){
				break;
			}
			/*getting the payment Gateway String*/
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"gateway_pgId\"]["+i+"]").getText();

			j=j+2;

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='paymentTypeParams']/div["+j+"]/form")).size();

			String pyament_option_name="";

			/* if gateway name matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(gateway_name)){
				for(int n=1;n<=no_of_form;n++){			
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='paymentTypeParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						key_value = Element.getPageElement(testConfig, How.xPath,  "//div[@id='paymentTypeParams']/div["+j+"]/form["+n+"]/input[7]").getAttribute("value");
					}
				}
			}

		}
		return key_value;
	}


	/*To update the Payment Type parameters
	 * @param paramRowNo, row number of the Params sheet
	 * @param data, object of TestDataReader
	 */
	public void updatePaymentTypeKeyValue(int paramRowNo, TestDataReader data){
		String gateway ="";
		String payment_option="";
		String key="";
		String value="";

		gateway = data.GetData(paramRowNo, "gateway");	
		payment_option = data.GetData(paramRowNo, "paymentoption");
		key = data.GetData(paramRowNo, "key");
		value = data.GetData(paramRowNo, "value");

		updatePaymentTypeKeyValue(gateway, payment_option, key, value);		
	}


	/** 
	 * update the specified value for the payment type parameters
	 * @param gateway_name whose value to be updated
	 * @param payment_option whose value to be updated
	 * @param key_name whose value to be updated
	 * @param value, value of the key
	 */
	public void updatePaymentTypeKeyValue(String gateway_name,String payment_option, String key_name, String value){

		String pg_name ="";
		gateway_name="Gateway: "+gateway_name;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='gateway_pgId']")).size();

		/*formating the parameters to match with the payment parameters written on the page*/
		String other_param=payment_option+"\n"+key_name+":";

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
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"gateway_pgId\"]["+i+"]").getText();

			j=j+2;

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='paymentTypeParams']/div["+j+"]/form")).size();

			String pyament_option_name="";

			/* if gateway name matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(gateway_name)){
				for(int n=1;n<=no_of_form;n++){
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='paymentTypeParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						updated_value = Element.getPageElement(testConfig, How.xPath,  "//div[@id='paymentTypeParams']/div["+j+"]/form["+n+"]/input[7]");
						Element.enterData(testConfig, updated_value, value, "Value");
						save_button= Element.getPageElement(testConfig, How.xPath,  "//div[@id='paymentTypeParams']/div["+j+"]/form["+n+"]/input[8]");
						Element.click(testConfig, save_button, "updating parameter "+ gateway_name);

					}

				}

			}

		}

	}
	

	/*To remove the Payment Type parameters
	 * @param paramRowNo, row number of the Params sheet
	 * @param data, object of TestDataReader
	 */	
	public void removePaymentTypeKeyValue(int paramRowNo, TestDataReader data){
		String gateway="";
		String payment_option="";
		String key="";

		gateway = data.GetData(paramRowNo, "gateway");	
		payment_option = data.GetData(paramRowNo, "paymentoption");
		key = data.GetData(paramRowNo, "key");

		removePaymentTypeKeyValue(gateway,payment_option, key);		
	}


	/*To remove the Value of the given key parameters of payment type
	 * @param gateway_name name of the gateway
	 * @param payment_option, payment option for that gateway
	 * @param key_name, key name
	 */
	public void removePaymentTypeKeyValue(String gateway_name,String payment_option, String key_name){

		String pg_name ="";
		gateway_name="Gateway: "+gateway_name;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='gateway_pgId']")).size();

		/*formating the parameters to match with the payment parameters written on the page*/
		String other_param=payment_option+"\n"+key_name+":";

		WebElement remove_button;

		int j=0;
		int no_of_form=0; 
		int flag=0;
		for(int i=1;i<= no_of_gateway;i++){
			if(flag==1){
				break;
			}
			/*getting the payment Gateway String*/
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"gateway_pgId\"]["+i+"]").getText();

			j=j+2;

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='paymentTypeParams']/div["+j+"]/form")).size();

			String pyament_option_name="";

			/* if gateway name matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we remove the  key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(gateway_name)){
				for(int n=1;n<=no_of_form;n++){
					if(flag==1){
						break;
					}
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='paymentTypeParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						remove_button= Element.getPageElement(testConfig, How.xPath,  "//div[@id='paymentTypeParams']/div["+j+"]/form["+n+"]/input[9]");
						Element.click(testConfig, remove_button, "removing parameter "+ gateway_name);
					}

				}

			}

		}

	}


	/*To verify that the parameters are added/updated successfully for the Payment Type
	 *@param paramRowNo, row number of the Params sheet
	 *@param data, object of TestDataReader
	 */	
	public void verifyPaymentTypeKeyValue(int testMerchantDataRow, TestDataReader data){
		String gateway = data.GetData(testMerchantDataRow, "gateway");
		String payment_option = data.GetData(testMerchantDataRow, "paymentoption").trim();		
		String key = data.GetData(testMerchantDataRow, "key").trim();
		String value =  data.GetData(testMerchantDataRow, "value").trim();

		String key_value = readPaymentTypeValue(gateway,payment_option, key);

		Helper.compareEquals(testConfig, "key's value", value, key_value);

	}


	/*To verify that the parameters are removed successfully for the Payment Type
	 *@param paramRowNo, row number of the Params sheet
	 *@param data, object of TestDataReader
	 */	
	public void verifyPaymentTypeRemove(int testMerchantDataRow, TestDataReader data){
		String gateway = data.GetData(testMerchantDataRow, "gateway");		
		String payment_option = data.GetData(testMerchantDataRow, "paymentoption").trim();
		String key = data.GetData(testMerchantDataRow,"key");

		String  value = readPaymentTypeValue(gateway, payment_option, key);

		Helper.compareEquals(testConfig, "Removed Param", not_found, value);

	}

	/***************************Methods for Override for pg Category***********************/


	/* to add parameter on PgCategory tab 
	 * @param rowno of the parameter 
	 * @return data, object of TestDataReader
	 */
	public TestDataReader addPgCategoryParameters(int paramRowNo) {
		TestDataReader data = new TestDataReader(testConfig, "Params");

		String value = "";

		value = data.GetData(paramRowNo, "gateway");
		Element.selectVisibleText(testConfig, pg_gateway, value, "gateway");

		value = data.GetData(paramRowNo, "category");
		Element.selectVisibleText(testConfig, pg_category, value, "pg_category");

		value = data.GetData(paramRowNo, "key");
		Element.selectVisibleText(testConfig, pg_paramKey, value, "pg_paramKey");

		value = data.GetData(paramRowNo, "value");
		Element.enterData(testConfig, pg_paramValue, value, "pg_Value");

		Element.click(testConfig, addCategoryParam,
				"Add pg_categories gateway, category, key and value");

		return data;
	}


	/*reads the value of the given parameters of pg category
	 * @param gateway_name name of the gateway
	 * @param payment_category, payment category 
	 * @param key_name, key name
	 * @return the value of the key associated
	 */
	public String readPgCategoryKeyValue(String gateway_name,String payment_category, String key_name){

		String pg_name ="";
		gateway_name="Gateway: "+gateway_name;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='pg_cat_pgId']")).size();

		/*formating the parameters to match with the payment parameters written on the page*/
		String other_param=payment_category+"\n"+key_name+":";

		String key_value=not_found;

		int j=0;
		int no_of_form=0; 
		int flag=0;
		for(int i=1;i<= no_of_gateway;i++){
			if(flag==1){
				break;
			}
			/*getting the payment Gateway String*/
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"pg_cat_pgId\"]["+i+"]").getText();
			j=j+2;

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='pgCategoryParams']/div["+j+"]/form")).size();
			String pyament_option_name="";

			/* if option matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(gateway_name)){
				for(int n=1;n<=no_of_form;n++){
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='pgCategoryParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						key_value = Element.getPageElement(testConfig, How.xPath,  "//div[@id='pgCategoryParams']/div["+j+"]/form["+n+"]/input[7]").getAttribute("value");
					}

				}

			}

		}

		return key_value;
	}


	/*To update the Pg Category parameters
	 * @param paramRowNo, row number of the Params sheet
	 * @param data, object of TestDataReader
	 */
	public void updatePgCategoryKeyValue(int paramRowNo, TestDataReader data){
		String gateway ="";
		String payment_category="";
		String key="";
		String value="";

		gateway = data.GetData(paramRowNo, "gateway");	
		payment_category = data.GetData(paramRowNo, "category");
		key = data.GetData(paramRowNo, "key");
		value = data.GetData(paramRowNo, "value");

		updatePgCategoryKeyValue(gateway, payment_category, key, value);		
	}


	/**
	 * update the specified value for the pg category parameters
	 * @param gateway_name whose value to be updated
	 * @param payment_category whose value to be updated
	 * @param key_name whose value to be updated
	 * @param value, value to the key
	 */
	public void updatePgCategoryKeyValue(String gateway_name,String payment_category, String key_name, String value){

		String pg_name ="";
		gateway_name="Gateway: "+gateway_name;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='pg_cat_pgId']")).size();

		/*formating the parameters to match with the payment parameters written on the page*/
		String other_param=payment_category+"\n"+key_name+":";

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
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"pg_cat_pgId\"]["+i+"]").getText();
			j=j+2;

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='pgCategoryParams']/div["+j+"]/form")).size();
			String pyament_option_name="";

			/* if option name matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(gateway_name)){
				for(int n=1;n<=no_of_form;n++){
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='pgCategoryParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						updated_value = Element.getPageElement(testConfig, How.xPath,  "//div[@id='pgCategoryParams']/div["+j+"]/form["+n+"]/input[7]");
						Element.enterData(testConfig, updated_value, value, "Value");
						save_button= Element.getPageElement(testConfig, How.xPath,  "//div[@id='pgCategoryParams']/div["+j+"]/form["+n+"]/input[8]");
						Element.click(testConfig, save_button, "updating parameter "+ gateway_name);

					}

				}

			}

		}

	}


	/*To remove the pg category parameters
	 * @param paramRowNo, row number of the Params sheet
	 * @param data, object of TestDataReader
	 */		
	public void removePgCategoryKeyValue(int paramRowNo, TestDataReader data){
		String gateway="";
		String payment_category="";
		String key="";

		gateway = data.GetData(paramRowNo, "gateway");	
		payment_category = data.GetData(paramRowNo, "category");
		key = data.GetData(paramRowNo, "key");

		removePgCategoryKeyValue(gateway,payment_category, key);		
	}


	/*remove the Value of the given key parameters for pg category
	 * @param gateway_name name of the gateway
	 * @param payment_category, payment category for that gateway
	 * @param key_name, key name
	 */
	public void removePgCategoryKeyValue(String gateway_name,String payment_category, String key_name){

		String pg_name ="";
		gateway_name="Gateway: "+gateway_name;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='pg_cat_pgId']")).size();

		/*formating the parameters to match with the payment parameters written on the page*/
		String other_param=payment_category+"\n"+key_name+":";

		WebElement remove_button;

		int j=0;
		int no_of_form=0; 
		int flag=0;
		for(int i=1;i<= no_of_gateway;i++){
			if(flag==1){
				break;
			}
			/*getting the payment Gateway String*/
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"pg_cat_pgId\"]["+i+"]").getText();
			j=j+2;

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='pgCategoryParams']/div["+j+"]/form")).size();
			String pyament_option_name="";
			/* if option name matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(gateway_name)){
				for(int n=1;n<=no_of_form;n++){
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='pgCategoryParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						remove_button= Element.getPageElement(testConfig, How.xPath,  "//div[@id='pgCategoryParams']/div["+j+"]/form["+n+"]/input[9]");
						Element.click(testConfig, remove_button, "removing parameter "+ gateway_name);

					}

				}

			}

		}

	}


	/*To verify that the parameters are added/updated successfully for the pg category
	 *@param paramRowNo, row number of the Params sheet
	 *@param data, object of TestDataReader
	 */	
	public void verifyPgCategoryKeyValue(int testMerchantDataRow, TestDataReader data){
		String gateway = data.GetData(testMerchantDataRow, "gateway");
		String payment_category = data.GetData(testMerchantDataRow, "category").trim();		
		String key = data.GetData(testMerchantDataRow, "key").trim();
		String value =  data.GetData(testMerchantDataRow, "value").trim();

		String key_value = readPgCategoryKeyValue(gateway,payment_category, key);

		Helper.compareEquals(testConfig, "key's value", value, key_value);
	}


	/*To verify that the parameters are removed successfully for the pg category
	 *@param paramRowNo, row number of the Params sheet
	 *@param data, object of TestDataReader
	 */	
	public void verifyPgCategoryRemove(int testMerchantDataRow, TestDataReader data){
		String gateway = data.GetData(testMerchantDataRow, "gateway");		
		String payment_category = data.GetData(testMerchantDataRow, "category").trim();
		String key = data.GetData(testMerchantDataRow,"key");

		String  value = readPgCategoryKeyValue(gateway, payment_category, key);

		Helper.compareEquals(testConfig, "Removed Param", not_found, value);
	}

}
