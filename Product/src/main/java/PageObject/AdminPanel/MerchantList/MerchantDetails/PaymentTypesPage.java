
package PageObject.AdminPanel.MerchantList.MerchantDetails;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;

public class PaymentTypesPage {

         	private Config testConfig;
			@FindBy(xpath="(//input[@name='priorityDown'])[1]")
			private WebElement priorityDownButton;
			
			@FindBy(xpath="(//input[@name='priorityUp'])[1])")
			private WebElement priorityUpButton;
			
			@FindBy(linkText="Payment Types")
			private WebElement payment_types;
			
			@FindBy(linkText="Netbanking")
			private WebElement netBanking;
			
			private int position;
			
			
			
			public PaymentTypesPage(Config testConfig)
			{
				this.testConfig = testConfig;
				PageFactory.initElements(this.testConfig.driver, this);
				Browser.waitForPageLoad(this.testConfig, payment_types);
				
			}
			
			/**
			 * decrease priority of credit card bank by one
			 * @param index
			 */
			public void decreasePriorityByOne(int index)
			{
				priorityDownButton = Element.getPageElement(testConfig, How.xPath, "(//input[@name='priorityDown'])[" + index + "]");
				Element.click(testConfig, priorityDownButton, "decrement priority");
				
			}
			/**
			 * decrease priority of netbanking by one
			 * @param gatewayRow
			 * @param bankRow
			 */
			public void decreasePriorityByOne(int gatewayRow,int bankRow)
			{
				priorityDownButton = Element.getPageElement(testConfig, How.xPath, "//*[@id='netbanking']/div[" + gatewayRow + "]/div[" + bankRow + "]/div[2]/form/input[8]");
				Element.click(testConfig, priorityDownButton, "decrement priority");
				
			}
			public void clickNetbankingTab()
			{
				Element.click(testConfig, netBanking, "NetBanking Tab");
				
			}
			/**
			 * increase priority of credit card bank by one
			 * @param index
			 */
			public void increasePriorityByOne(int index)
			{
				priorityUpButton = Element.getPageElement(testConfig, How.xPath, "(//input[@name='priorityUp'])[" + index + "]");
				Element.click(testConfig, priorityUpButton, "increase priority");
				
			}
			
			/**
			 * increase priority of netbanking bank by one
			 * @param gatewayRow
			 * @param bankRow
			 */
			public void increasePriorityByOne(int gatewayRow, int bankRow)
			{
				priorityUpButton = Element.getPageElement(testConfig, How.xPath, "//*[@id='netbanking']/div[" + gatewayRow + "]/div[" + bankRow + "]/div[2]/form/input[9]");
				Element.click(testConfig, priorityUpButton, "increase priority");
				
			}
			/**
			 * Get the position/index of given Netbanking gateway
			 * @param payment_gateway
			 * @param bName
			 * @return
			 */
			public int getNBGatewayRowPosition(String payment_gateway, String bName)
			{
				/*Calculating the number of payment Gateway*/
				int no_of_gateway = getNumberOfNBGateways();
				String bankName;
				position=1;
				for(int i =1;i<=no_of_gateway;i++)
				{
					 bankName = Element.getPageElement(testConfig, How.xPath, "//div[@id='" + "netbanking" + "']/b[" + i + "]").getText();
					    if(bankName.equalsIgnoreCase(bName))
					    {
					    	return position;
					    	
					    	
					    } else position=position+1;
				}
				
				
				 testConfig.logComment("Given gateway is not added for merchant");
					
					return 0;
				
			}
			/**
			 * get index/position of a given bank within a given netbanking gateway
			 * @param posRowNum
			 * @param payment_gateway
			 * @return
			 */
			public int getNBPriorityRowNum(int posRowNum, String payment_gateway)
			{
				int numBanks = getNumberOfNBBanks(posRowNum);
				String gatewayName;
				int r=1;
				int i=0;
				int index;
				position=-1;
				do{
		            index = r+i;
		            position+=2;
				    gatewayName = Element.getPageElement(testConfig, How.xPath, "//div[@id='netbanking']/div[" + posRowNum + "]/div[" + index + "]/div").getText();
				    if(gatewayName.equalsIgnoreCase(payment_gateway))
				    {
				    	return position;
				    }
				    numBanks--;
				    i++;
				    r++;
				//}
		      }while(numBanks!=-1);
		    	 
				
			
				testConfig.logComment("Given NB bank name not added for merchant");
				return 0;
			}
			
			/**
			 * get index/position of a credit card bank
			 * @param payment_gateway
			 * @param paymentType
			 * @return
			 */
			public int getGatewayRowPosition(String payment_gateway,String paymentType)
			{
				/*Calculating the number of payment Gateway*/
				int no_of_gateway = getNumberOfCCGateways();
				//int no_of_gateway = testConfig.driver.findElements(By.xpath("//div[@id='creditcard']//form[@action='https://admin.payu.in/release/merchantGateway']")).size();
				String gatewayName;
				int r=1;
				int i=0;
				int index;
				position=0;
				do{
				//for(int r = 1i=0;i<=7;i+=2)
		            index = r+i;
		            position++;
				    gatewayName = Element.getPageElement(testConfig, How.xPath, "//div[@id='" + paymentType + "']/div/div[" + index + "]/div").getText();
				    if(gatewayName.equalsIgnoreCase(payment_gateway))
				    {
				    	return position;
				    }
				    no_of_gateway--;
				    i++;
				    r++;
				//}
		      }while(no_of_gateway!=-1);
		    	  testConfig.logComment("Given gateway is not added for merchant");
				
				return 0;
			
			}
			/** Get number of credit card gateways
			 * 
			 * @return
			 */
			public int getNumberOfCCGateways()
			{
				return testConfig.driver.findElements(By.xpath("//div[@id='creditcard']//form[@action='https://admin.payu.in/release/merchantGateway']")).size();
					
			}
			/**
			 * Get number of netbanking gateways
			 * @return
			 */
			public int getNumberOfNBGateways()
			{
				return testConfig.driver.findElements(By.xpath("//div[@id='netbanking']/b")).size();
			}
			
			/**
			 * Get number of banks within a netbanking gateway.( note: subtract one from the result)  
			 * @param index
			 * @return
			 */
			public int getNumberOfNBBanks(int index)
			{
				return testConfig.driver.findElements(By.xpath("//div[@id='netbanking']/div[" + index + "]/div")).size();
				
			}

}
