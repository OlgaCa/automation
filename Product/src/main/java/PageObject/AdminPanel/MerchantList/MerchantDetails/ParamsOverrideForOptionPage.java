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

public class ParamsOverrideForOptionPage extends ParamsPage{

	private Config testConfig;

	private String not_found ="Not Found";

	
	@FindBy(linkText = "Override For Category")
	private WebElement override_for_category;

	@FindBy(linkText = "Override For Option")
	private WebElement override_for_option;

	@FindBy(xpath="//select[@name='ibibo_code']")
	private WebElement param_option;

	@FindBy(xpath="//div[@id='optionParams']/form/select[2]")
	private WebElement param_key;

	@FindBy(xpath="//div[@id='optionParams']/form/input[4]")
	private WebElement param_value;

	@FindBy(name="submitOption")
	private WebElement submit_button;

	@FindBy(xpath="//div[@id='categoryParams']/form/select")
	private WebElement param_category;

	@FindBy(xpath="//div[@id='categoryParams']/form/select[2]")
	private WebElement category_key;

	@FindBy(xpath="//div[@id='categoryParams']/form/input[4]")
	private WebElement category_value;

	@FindBy(name="submitCat")
	private WebElement submit_category_button;
	

	public ParamsOverrideForOptionPage(Config testConfig) {
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig,param_option );	

	}
	
	/*clicks the Override for Category Tab*/
	public void clickOverrideForCategory() {
		Element.click(testConfig, override_for_category,"override_for_category tab");
	}

	/*clicks the Override for Option Tab*/
	public void clickOverrideForOption() {
		Element.click(testConfig, override_for_option,"override_for_option tab");
	}

	
	/* To add parameter on Option tab 
	 * @param rowno of the parameter in the Params sheet
	 * @return data, object of TestDataReader 
	 */	
	public TestDataReader addOptionNewParams(int paramRowNo){
		TestDataReader data = new TestDataReader(testConfig,"Params");

		String value = "";

		value = data.GetData(paramRowNo, "paymentoption");
		Element.selectVisibleText(testConfig, param_option, value, "gateway option");

		value = data.GetData(paramRowNo, "key");
		Element.selectVisibleText(testConfig, param_key, value, "Add Key");

		value = data.GetData(paramRowNo, "value");
		Element.enterData(testConfig, param_value, value, "Value");

		Element.click(testConfig, submit_button, "Adding the parameter for payment option type");

		return data;
	}

	/*readValue reads the value of the given key parameters of Option
	 * @param payment_option, payment option for that gateway
	 * @param key_name, key name
	 * @return the value of the key associated
	 */	
	public String readPaymentOptionvalue(String payment_option, String key_name){

		String pg_name ="";
		payment_option="Gateway: "+payment_option;
	
		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='option_ibibo_code']")).size();
		/*formating the parameters to match with the payment parameters written on the page*/
		String other_param=key_name+":";

		String key_value=not_found;

		int j=0;
		int no_of_form=0; 
		int flag=0;
	
		for(int i=1;i<= no_of_gateway;i++){
			if(flag==1){
				break;
			}
			/*getting the payment Gateway String*/
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"option_ibibo_code\"]["+i+"]").getText();
			j=j+2;	

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='optionParams']/div["+j+"]/form")).size();
	
			String pyament_option_name="";

			/* if option name matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(payment_option)){
				for(int n=1;n<=no_of_form;n++){				
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='optionParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						key_value = Element.getPageElement(testConfig, How.xPath,  "//div[@id='optionParams']/div["+j+"]/form["+n+"]/input[6]").getAttribute("value");

					}

				}

			}

		}
		return key_value;
	}


	/*To update the Option parameters
	 * @param paramRowNo, row number of the Params sheet
	 * @param data, object of TestDataReader
	 */
	public void updatePaymentOptionKeyValue(int paramRowNo, TestDataReader data){
		String payment_option="";
		String key="";
		String value="";

		payment_option = data.GetData(paramRowNo, "paymentoption");
		key = data.GetData(paramRowNo, "key");
		value = data.GetData(paramRowNo, "value");

		updatePaymentOptionKeyValue(payment_option, key, value);		
	}


	/** 
	 * update the specified value for the Option parameters 
	 * @param gateway_name whose value to be updated
	 * @param payment_option whose value to be updated
	 * @param key_name whose value to be updated
	 */	
	public void updatePaymentOptionKeyValue(String payment_option, String key_name, String value){

		String pg_name ="";
		payment_option="Gateway: "+payment_option;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='option_ibibo_code']")).size();
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
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"option_ibibo_code\"]["+i+"]").getText();
			j=j+2;	

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='optionParams']/div["+j+"]/form")).size();
			String pyament_option_name="";

			/* if option name matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(payment_option)){
				for(int n=1;n<=no_of_form;n++){
					
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='optionParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						updated_value = Element.getPageElement(testConfig, How.xPath,  "//div[@id='optionParams']/div["+j+"]/form["+n+"]/input[6]");
						Element.enterData(testConfig, updated_value, value, "Value");
						save_button= Element.getPageElement(testConfig, How.xPath,  "//div[@id='optionParams']/div["+j+"]/form["+n+"]/input[7]");
						Element.click(testConfig, save_button, "updating parameter "+ payment_option);

					}

				}

			}

		}

	}


	/*To remove the Option parameters
	 * @param paramRowNo, row number of the Params sheet
	 * @param data, object of TestDataReader
	 */	
	public void removePaymentOptionKeyValue(int paramRowNo, TestDataReader data){
		String payment_option="";
		String key="";

		payment_option = data.GetData(paramRowNo, "paymentoption");
		key = data.GetData(paramRowNo, "key");

		removePaymentOptionKeyValue(payment_option, key);		
	}

	
	/*To remove the Value of the given key parameters of Option
	 * @param payment_option, payment option 
	 * @param key_name, key name
	 */
	public void removePaymentOptionKeyValue(String payment_option, String key_name){

		String pg_name ="";
		payment_option="Gateway: "+payment_option;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='option_ibibo_code']")).size();
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
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"option_ibibo_code\"]["+i+"]").getText();
			j=j+2;

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='optionParams']/div["+j+"]/form")).size();
			String pyament_option_name="";

			/* if gateway name matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(payment_option)){
				for(int n=1;n<=no_of_form;n++){
					
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='optionParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						remove_button= Element.getPageElement(testConfig, How.xPath,  "//div[@id='optionParams']/div["+j+"]/form["+n+"]/input[8]");
						Element.click(testConfig, remove_button, "removing parameter "+ payment_option);

					}

				}

			}

		}

	}
	

	/*To verify that the parameters are added/updated successfully for the Option
	 *@param paramRowNo, row number of the Params sheet
	 *@param data, object of TestDataReader
	 */	
	public void verifyPaymentOptionKeyValue(int testMerchantDataRow, TestDataReader data){
		String payment_option = data.GetData(testMerchantDataRow, "paymentoption").trim();		
		String key = data.GetData(testMerchantDataRow, "key").trim();
		String value =  data.GetData(testMerchantDataRow, "value").trim();

		String key_value = readPaymentOptionvalue(payment_option, key);

		Helper.compareEquals(testConfig, "key's value", value, key_value);
	}

	
	/*To verify that the parameters are removed successfully for the Option
	 *@param paramRowNo, row number of the Params sheet
	 *@param data, object of TestDataReader
	 */	
	public void verifyPaymentOptionRemove(int testMerchantDataRow, TestDataReader data){
		String payment_option = data.GetData(testMerchantDataRow, "paymentoption").trim();
		String key = data.GetData(testMerchantDataRow,"key");

		String  value = readPaymentOptionvalue(payment_option, key);

		Helper.compareEquals(testConfig, "Removed Param", not_found, value);

	}

/**********Methods for Payment Category*************/

	/* to add parameter on Category tab 
	 * @param rowno of the parameter 
	 * @return data, object of TestDataReader
	 */
	public TestDataReader addCategoryNewParams(int paramRowNo){
		TestDataReader data = new TestDataReader(testConfig,"Params");

		String value = "";

		value = data.GetData(paramRowNo, "category");
		Element.selectVisibleText(testConfig, param_category, value, "category");

		value = data.GetData(paramRowNo, "key");
		Element.selectVisibleText(testConfig, category_key, value, "Add Key");

		value = data.GetData(paramRowNo, "value");
		Element.enterData(testConfig, category_value, value, "Value");

		Element.click(testConfig, submit_category_button, "Adding the parameter for payment category type");
		
		return data;
	}


	/*reads the value of the given key parameters for category parameters
	 * @param payment_category, payment category 
	 * @param key_name, key name
	 * @return the value of the key associated
	 */
	public String readPaymentCategoryvalue(String payment_category, String key_name){

		String pg_name ="";
		payment_category="Category: "+payment_category;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='category_param']")).size();
		/*formating the parameters to match with the payment parameters written on the page*/
		String other_param=key_name+":";

		String key_value=not_found;

		int j=0;
		int no_of_form=0; 
		int flag=0;
		for(int i=1;i<= no_of_gateway;i++){
			if(flag==1){
				break;
			}
			/*getting the payment Gateway String*/
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"category_param\"]["+i+"]").getText();
			j=j+2;	

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='categoryParams']/div["+j+"]/form")).size();
			String pyament_option_name="";

			/* if gateway name matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(payment_category)){
				for(int n=1;n<=no_of_form;n++){
					
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='categoryParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						key_value = Element.getPageElement(testConfig, How.xPath,  "//div[@id='categoryParams']/div["+j+"]/form["+n+"]/input[6]").getAttribute("value");					
					}

				}

			}

		}
		return key_value;
	}

	
	/*To update the Category parameters
	 * @param paramRowNo, row number of the Params sheet
	 * @param data, object of TestDataReader
	 */
	public void updatePaymentCategoryKeyValue(int paramRowNo, TestDataReader data){
		String payment_category="";
		String key="";
		String value="";

		payment_category = data.GetData(paramRowNo, "category");
		key = data.GetData(paramRowNo, "key");
		value = data.GetData(paramRowNo, "value");

		updatePaymentCategoryKeyValue(payment_category, key, value);		
	}
	
	
	/**
	 * update the specified value for the category parameters
	 * @param payment_category whose value to be updated
	 * @param key_name whose value to be updated
	 * @param value, value of the key
	 */	
	public void updatePaymentCategoryKeyValue(String payment_category, String key_name, String value){

		String pg_name ="";
		payment_category="Category: "+payment_category;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='category_param']")).size();
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
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"category_param\"]["+i+"]").getText();
			j=j+2;	

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='categoryParams']/div["+j+"]/form")).size();
			String pyament_option_name="";

			/* if gatewayname matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(payment_category)){
				for(int n=1;n<=no_of_form;n++){
					
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='categoryParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						updated_value = Element.getPageElement(testConfig, How.xPath,  "//div[@id='categoryParams']/div["+j+"]/form["+n+"]/input[6]");
						Element.enterData(testConfig, updated_value, value, "Value");
						save_button= Element.getPageElement(testConfig, How.xPath,  "//div[@id='categoryParams']/div["+j+"]/form["+n+"]/input[7]");
						Element.click(testConfig, save_button, "updating parameter "+ payment_category);

					}

				}

			}

		}

	}

	
	/*To remove the category parameters
	 * @param paramRowNo, row number of the Params sheet
	 * @param data, object of TestDataReader
	 */	
	public void removePaymentCategoryKeyValue(int paramRowNo, TestDataReader data){
		String payment_category="";
		String key="";

		payment_category = data.GetData(paramRowNo, "category");
		key = data.GetData(paramRowNo, "key");

		removePaymentCategoryKeyValue(payment_category, key);		
	}
	
	
	/*remove the Value of the given key parameters for pg category
	 * @param payment_category, payment category for that gateway
	 * @param key_name, key name
	 */
	public void removePaymentCategoryKeyValue(String payment_category, String key_name){

		String pg_name ="";
		payment_category="Category: "+payment_category;

		/*Calculating the number of payment Gateway in the update section*/
		int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='category_param']")).size();
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
			pg_name = Element.getPageElement(testConfig, How.xPath, "//div[@id=\"category_param\"]["+i+"]").getText();
			j=j+2;

			/*calculating the number of forms under gateway name*/
			no_of_form = testConfig.driver.findElements(By.xpath("//div[@id='categoryParams']/div["+j+"]/form")).size();
			String pyament_option_name="";

			/* if option matches we loop through each form to find whether other parameters also matches or not
			 * if all parameters matches we update the value for that key and then stop processing the further gateways
			 * */
			if(pg_name.equalsIgnoreCase(payment_category)){
				for(int n=1;n<=no_of_form;n++){
					
					pyament_option_name=	Element.getPageElement(testConfig, How.xPath, "//div[@id='categoryParams']/div["+j+"]/form["+n+"]").getText();
					if(pyament_option_name.equalsIgnoreCase (other_param)){
						flag=1;
						remove_button= Element.getPageElement(testConfig, How.xPath,  "//div[@id='categoryParams']/div["+j+"]/form["+n+"]/input[8]");
						Element.click(testConfig, remove_button, "removing parameter "+ payment_category);

					}

				}

			}

		}

	}

	
	/*To verify that the parameters are added/updated successfully for the category
	 *@param paramRowNo, row number of the Params sheet
	 *@param data, object of TestDataReader
	 */	
	public void verifyPaymentCategoryKeyValue(int testMerchantDataRow, TestDataReader data){
		String payment_category = data.GetData(testMerchantDataRow, "category").trim();		
		String key = data.GetData(testMerchantDataRow, "key").trim();
		String value =  data.GetData(testMerchantDataRow, "value").trim();

		String key_value = readPaymentCategoryvalue(payment_category, key);

		Helper.compareEquals(testConfig, "key's value", value, key_value);

	}

	
	/*To verify that the parameters are removed successfully for the category
	 *@param paramRowNo, row number of the Params sheet
	 *@param data, object of TestDataReader
	 */	
	public void verifyPaymentCategoryRemove(int testMerchantDataRow, TestDataReader data){
		String payment_option = data.GetData(testMerchantDataRow, "category").trim();
		String key = data.GetData(testMerchantDataRow,"key");

		String  value = readPaymentCategoryvalue(payment_option, key);

		Helper.compareEquals(testConfig, "Removed Param", not_found, value);

	}

}
