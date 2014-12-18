package Test.MerchantPanel.Transactions;

import java.util.Map;

import org.testng.Assert;

import PageObject.AdminPanel.MerchantList.MerchantListPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.MerchantDetailsPage;
import PageObject.AdminPanel.MerchantList.MerchantDetails.PaymentTypesPage;
import PageObject.AdminPanel.Payments.Response.BankRedirectPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Test.AdminPanel.Payments.TransactionHelper.ExpectedResponsePage;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class testDynamicSwitching extends TestBase {
	
	MerchantDetailsPage merchantDetailsPage;
	PaymentTypesPage paymentTypesPage;
	
	//@Test(description = "Verify that when both payment gateways are up the one with greater health(higher priority) is picked",dataProvider="GetTestConfig", timeOut=600000, groups="1")
	//TODO: need to bring health of HDFC CBK to >0 and <0.5
	public void testVerifyTransactionGatewayWithHigherHealthAndPriorityIsPicked(Config testConfig) {
		
        int transactionRowNum = 43;
        int cardDetailsRowNum = 1;
		TestDataReader paymentTypeData = new TestDataReader(testConfig, "PaymentType");
        TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
        tranHelper.DoLogin();
        //gateway1 is HDFC CBK
        // make both gateways up if in case any of them is down
        int paymentTypeRowNum =109;
        String pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
        tranHelper.MakeGatewayAsStatusUp(pgType);
    	//get corresponding pg_gateway_id for this PG_TYPE
		TestDataReader pgInfoData = new TestDataReader(testConfig, "PGInfo");
	
        testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID"));

        // get health of gateway1
        String gatewayHealth1 = tranHelper.getGatewayHealth(pgType,paymentTypeRowNum);
        testConfig.logComment("Health of gateway1 " + pgType + gatewayHealth1 );
        //TODO: gateway2 change bank to HDFC NB 
        paymentTypeRowNum =212;
        pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
        tranHelper.MakeGatewayAsStatusUp(pgType);
        //get corresponding pg_gateway_id for this PG_TYPE
      	pgInfoData = new TestDataReader(testConfig, "PGInfo");
      	
        testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID"));

        // get health of HDFC gateway
        String gatewayHealth2 = tranHelper.getGatewayHealth(pgType,paymentTypeRowNum);
        testConfig.logComment("Health of gateway2 " + pgType + gatewayHealth2 );
        
        if(Double.parseDouble(gatewayHealth1)>Double.parseDouble(gatewayHealth2)) 
    	{
        	paymentTypeRowNum =109;
        	pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
        	testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID"));

    	}
    	else 
    	{
    		paymentTypeRowNum =212;
       	 pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
         testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID"));
   
    	}
        
         // increase priority of healthier gateway to highest priority
        tranHelper.transactionData = new TestDataReader(tranHelper.testConfig,"TransactionDetails");
		String merchantName = tranHelper.transactionData.GetData(transactionRowNum, "Comments");
		MerchantListPage merchantListPage = tranHelper.home.clickMerchantList();
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		paymentTypesPage = merchantDetailsPage.clickPaymentOptions();
		paymentTypesPage.clickNetbankingTab();
		String bName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
		
		 int gatewayRowNum = paymentTypesPage.getNBGatewayRowPosition(pgType,bName);
		 int gatewayPriorityRowNum = paymentTypesPage.getNBPriorityRowNum(gatewayRowNum,pgType);
		  testConfig.logComment("Gateway " + pgType +  "before priority change " + gatewayPriorityRowNum);
		  if(gatewayPriorityRowNum>1)
		  { 
			  do{
		         paymentTypesPage.increasePriorityByOne(gatewayRowNum,gatewayPriorityRowNum);
		         gatewayPriorityRowNum = paymentTypesPage.getNBPriorityRowNum(gatewayPriorityRowNum,pgType); 
		         paymentTypesPage.clickNetbankingTab();
			    } while(gatewayPriorityRowNum!=1);
		  }
		  testConfig.logComment("Gateway " + pgType + "after priority change " + gatewayPriorityRowNum);

		  int gatewayHighCount = 0;
		  int gatewayLowCount = 0;
		  int failureCount = 0;
		  
			for(int i =0;i<10;i++)
			{
			
		  //do test transaction 

				tranHelper.GetTestTransactionPage();

				tranHelper.bankRedirectPage = (BankRedirectPage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		 //click submit query on simulator page
	         if(Element.getPageElement(testConfig, How.css, "input[type=\"submit\"]") != null)
	        	 Element.click(testConfig, Element.getPageElement(testConfig, How.css, "input[type=\"submit\"]"), "Submit");
			
			// get gateway id for above transaction
			Map<String, String> transData = DataBase.executeSelectQuery(testConfig, 1, 1);
			
			if(transData!=null)
			{
			String gatewayId = transData.get("pgId");
			if(gatewayId.contentEquals(testConfig.getRunTimeProperty("pg_id")))
				gatewayHighCount++;
			else
				gatewayLowCount++;
		       
			Helper.compareEquals(testConfig, "gateway id", testConfig.getRunTimeProperty("pg_id"), gatewayId);
			}
			else failureCount++;
			}

			testConfig.logComment("No. of time gateway with greater health is used is" + gatewayHighCount);
			testConfig.logComment("No. of time gateway with lower health is used is" + gatewayLowCount);
			testConfig.logComment("No. of failed transaction is " + failureCount);
			Helper.compareTrue(testConfig, "No of times gateway with greater health is used is less than 80%", gatewayHighCount>=8);
			Helper.compareTrue(testConfig, "No of times gateway with lesser health is used is more than 20%", gatewayLowCount<=2);
			
			Assert.assertTrue(testConfig.getTestResult());
	}
	
	//@Test(description = "Verify that health of a gateway less than .5  is never used. ", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	
	public void testVerifyGatewayWithGreaterHealthUsedAlways(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, true);
		
		
				helper.DoLogin();
				int transactionRowNum =43;
				int gatewayPriorityRowNum;
				int cardDetailsRowNum = 1;
					
				helper.transactionData = new TestDataReader(helper.testConfig,"TransactionDetails");
				
				 // get health of gateway 1
		        int paymentTypeRowNum =109;
		        TestDataReader paymentTypeData = new TestDataReader(testConfig, "PaymentType");
		        String pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
		        
		        String gatewayHealth1 = helper.getGatewayHealth(pgType,paymentTypeRowNum);
		        testConfig.logComment("Health of gateway1 "+ pgType + gatewayHealth1 );
		        
		        //get health of gateway 2
		        paymentTypeRowNum =212;
		        pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");

		        String gatewayHealth2 = helper.getGatewayHealth(pgType,paymentTypeRowNum);
		        testConfig.logComment("Health of gateway2 " + pgType + gatewayHealth2 );
		        //get corresponding pg_gateway_id for this PG_TYPE
				TestDataReader pgInfoData = new TestDataReader(testConfig, "PGInfo");
			
		        if(Double.parseDouble(gatewayHealth1)>Double.parseDouble(gatewayHealth2)) 
		    	{
		        	paymentTypeRowNum =109;
		        	pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
		        	testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID"));

		    	}
		    	else 
		    	{
		    		paymentTypeRowNum =212;
		       	    pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
		            testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID"));
		   
		    	}
		        String merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");
				MerchantListPage merchantListPage = helper.home.clickMerchantList();
				merchantListPage.SearchMerchant(merchantName);
				merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
				paymentTypesPage = merchantDetailsPage.clickPaymentOptions();
				paymentTypesPage.clickNetbankingTab();
		          // lower priority of healthier gateway to lowest priority
		          String bName = paymentTypeData.GetData(paymentTypeRowNum, "Bank Name");
				
		          int gatewayRowNum = paymentTypesPage.getNBGatewayRowPosition(pgType,bName);
		 		      gatewayPriorityRowNum = paymentTypesPage.getNBPriorityRowNum(gatewayRowNum,pgType);
		 		      int no_of_banks = paymentTypesPage.getNumberOfNBBanks(gatewayRowNum);
		 		  testConfig.logComment("Gateway " + pgType +  "before priority change " + gatewayPriorityRowNum);
				  if(gatewayPriorityRowNum < no_of_banks-1)
				  { 
					  do{
						  paymentTypesPage.decreasePriorityByOne(gatewayRowNum,gatewayPriorityRowNum);
						  paymentTypesPage.clickNetbankingTab();
						   gatewayPriorityRowNum = paymentTypesPage.getNBPriorityRowNum(gatewayPriorityRowNum,pgType); 
					         paymentTypesPage.clickNetbankingTab();
						} while(gatewayPriorityRowNum!=paymentTypesPage.getNBGatewayRowPosition(pgType,bName));
				  }
				  testConfig.logComment("Gateway " + pgType + "after priority change " + gatewayPriorityRowNum);
				  int gatewayHighCount = 0;
				  int gatewayLowCount = 0;
				  int failureCount = 0;
					for(int i =0;i<10;i++)
					{
					
				  //do test transaction 

				  helper.GetTestTransactionPage();

				  helper.bankRedirectPage = (BankRedirectPage)helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
				  //click submit query on simulator page
			         if(Element.getPageElement(testConfig, How.css, "input[type=\"submit\"]").isDisplayed())
			        	 Element.click(testConfig, Element.getPageElement(testConfig, How.css, "input[type=\"submit\"]"), "Submit");
					// get gateway id for above transaction
					Map<String, String> transData = DataBase.executeSelectQuery(testConfig, 1, 1);
					if(transData!=null)
					{
					String gatewayId = transData.get("pgId");
					if(gatewayId.contentEquals(testConfig.getRunTimeProperty("pg_id")))
						gatewayHighCount++;
					else
						gatewayLowCount++;
				       
					Helper.compareEquals(testConfig, "gateway id", testConfig.getRunTimeProperty("pg_id"), gatewayId);
					}
					else 
					{
						failureCount++;
					}
					}
					testConfig.logComment("No. of time gateway with greater health is used is" + gatewayHighCount);
					testConfig.logComment("No. of time gateway with lower health is used is" + gatewayLowCount);
					testConfig.logComment("No. of failed transactions is" + failureCount++);
					Helper.compareTrue(testConfig, "No of times gateway with greater health is used is equal to 100%", gatewayHighCount==10);
					
					Assert.assertTrue(testConfig.getTestResult());
 
				  }
	
//	@Test(description = "Verify Dynamic Switching for one gateway",	dataProvider="GetTestConfig", timeOut=600000, groups="1")
   public void testDSOneGateway(Config testConfig) {
		//TODO: configure paymentTypeRowNum and transactionRowNum accordingly
		    int transactionRowNum = 43;
	        int cardDetailsRowNum = 1;
	        int  paymentTypeRowNum =212;
	      
			TestDataReader paymentTypeData = new TestDataReader(testConfig, "PaymentType");
	        TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
	        tranHelper.DoLogin();
	       
	        String pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
	        tranHelper.getGatewayId(pgType);
			if(tranHelper.getGatewayStatus(pgType).equals("1"))
			{
				testConfig.logComment("verifying gateway Id for gateway with up status");
				// do 5 NB transactions and verify 100% rule
				doNBTransactionsAndVerify100PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,5,testConfig.getRunTimeProperty("pg_id"));
				// make gateway down
				doInvalidTransactionAndMakeGatewayDown(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,pgType);
				//verify gateway is down
				if(tranHelper.getGatewayStatus(pgType).equals("0") || tranHelper.getGatewayStatus(pgType).equals("2"))
					testConfig.logFail("Gateway " + pgType + "is not down");
				testConfig.logComment("verifying gateway Id for gateway with down status");
				
				// do five transactions to verify gatewayId for 100% rule
				doNBTransactionsAndVerify100PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,5,testConfig.getRunTimeProperty("pg_id"));
			}
			else if(tranHelper.getGatewayStatus(pgType).equals("0") || tranHelper.getGatewayStatus(pgType).equals("2"))
			{
				testConfig.logComment("verifying gateway Id for gateway with down status");
				
				// do five transactions to verify gatewayId for 100% rule
				doNBTransactionsAndVerify100PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,5,testConfig.getRunTimeProperty("pg_id"));
		
				// make gateway up
				tranHelper.MakeGatewayAsStatusUp(pgType);
				testConfig.logComment("verifying gateway Id for gateway with up status");
				
				// do five transactions to verify gatewayId for 100% rule
				doNBTransactionsAndVerify100PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,5,testConfig.getRunTimeProperty("pg_id"));
		
			}
			}
			
	
//	@Test(description = "Verify Dynamic Switching for two gateways", dataProvider="GetTestConfig", timeOut=600000, groups="1")
		   public void testDSTwoGateway(Config testConfig) {	
		    int transactionRowNum = 43;
	        int cardDetailsRowNum = 1;
	        String higherHealth;
	        String pgTypeHigher;
	        String pgTypeLower;
	        String higherHealthGatewayId;
	        String lowerHealthGatewayId;
			TestDataReader paymentTypeData = new TestDataReader(testConfig, "PaymentType");
	        TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
	        // TODO: configure gateway1
	        int paymentTypeRowNum =109;
	        String pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
		
	        // get health of gateway1
	        String gatewayHealth1 = tranHelper.getGatewayHealth(pgType,paymentTypeRowNum);
	        testConfig.logComment("Health of gateway1 " + pgType + gatewayHealth1 );
	      
	        // TODO: configure gateway2
	        paymentTypeRowNum =109;
	        pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
		
	        // get health of gateway2
	        String gatewayHealth2 = tranHelper.getGatewayHealth(pgType,paymentTypeRowNum);
	        testConfig.logComment("Health of gateway1 " + pgType + gatewayHealth2 );
	        //for getting gateway info
			TestDataReader pgInfoData = new TestDataReader(testConfig, "PGInfo");
		
	        //compare gateway health 
	        if(Double.parseDouble(gatewayHealth1)>Double.parseDouble(gatewayHealth2)) 
	    	{
	        	paymentTypeRowNum =109;
	        	pgTypeHigher = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
	        	higherHealth = gatewayHealth1;
	        	testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID"));
	        	higherHealthGatewayId = testConfig.getRunTimeProperty("pg_id");
	        	paymentTypeRowNum =212;
	       	    pgTypeLower = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
	            lowerHealthGatewayId = pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID");
                
	    	}
	    	else 
	    	{
	    		paymentTypeRowNum =212;
	       	    pgTypeHigher = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
	         	higherHealth = gatewayHealth2;
	            testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID"));
	            higherHealthGatewayId = testConfig.getRunTimeProperty("pg_id");
	            //TODO: configure paymentTypeRowNum
	            paymentTypeRowNum =109;
	       	    pgTypeLower = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
	         	lowerHealthGatewayId = pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID");
               
	    	}
	        testConfig.logComment("Health of higher health gateway is" + higherHealth);
	        testConfig.logComment("PG id of higher health gateway is" + higherHealthGatewayId);
	        
	        if(Double.parseDouble(gatewayHealth1) > 0.5 && Double.parseDouble(gatewayHealth2) > 0.5)
	        {
	        	//ensure both gateways are up
	       	 tranHelper.MakeGatewayAsStatusUp(pgTypeHigher);
	    	 tranHelper.MakeGatewayAsStatusUp(pgTypeLower);
	         
	         
	          // do 10 transactions and verify 80/20 rule
	        	doNBTransactionsAndVerify_80_20_PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,10,higherHealthGatewayId,lowerHealthGatewayId);

	        }
	        
	        else //(Double.parseDouble(gatewayHealth1) > 0.5 && Double.parseDouble(gatewayHealth2) < 0.5 ))
	        {
	        	//do 10 transactions and verify 100 % rule for higher health gateway
	        	doNBTransactionsAndVerify100PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,10,higherHealthGatewayId);
				
	        }
	        
	        //make both gateways down
	        //TODO: configure to set ds = 1 if it is not 1
	    	doInvalidTransactionAndMakeGatewayDown(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,pgTypeHigher);
	    	doInvalidTransactionAndMakeGatewayDown(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,pgTypeLower);
	    	//do 10 transactions and verify 100 % rule for higher health gateway
        	doNBTransactionsAndVerify100PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,10,higherHealthGatewayId);
			
        	//make one gateway up and one down
        	 tranHelper.MakeGatewayAsStatusUp(pgTypeHigher);
        	 //verify if one gateway is down
        	 if(tranHelper.getGatewayStatus(pgTypeLower).equals("0") || tranHelper.getGatewayStatus(pgTypeLower).equals("2"))
        	 {
        			//do 10 transactions and verify 100 % rule for up status gateway
             	doNBTransactionsAndVerify100PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,10,higherHealthGatewayId);
     		
        	 }
	}
	       
	
	
	//@Test(description = "Verify Dynamic Switching for three gateways",dataProvider="GetTestConfig", timeOut=600000, groups="1")
		   public void testDSThreeGateway(Config testConfig) {
		//TODO: ensure ds is 1
		  int transactionRowNum = 43;
	        int cardDetailsRowNum = 1;
	        String highestHealth;
	        String secondHigherHealth;
	        String pgTypeHigher;
	        String pgTypeSecondHigher;
	        String pgTypeLower;
	        String higherHealthGatewayId;
	        String lowerHealthGatewayId;
	        String secondHigherHealthGatewayId;
			TestDataReader paymentTypeData = new TestDataReader(testConfig, "PaymentType");
	        TransactionHelper tranHelper = new TransactionHelper(testConfig, true);
	        // TODO: configure gateway1
	        int paymentTypeRowNum =109;
	        String pgType1 = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
		
	        // get health of gateway1
	        String gatewayHealth1 = tranHelper.getGatewayHealth(pgType1,paymentTypeRowNum);
	        testConfig.logComment("Health of gateway1 " + pgType1 + gatewayHealth1 );
	      
	        // TODO: configure gateway2
	        paymentTypeRowNum =109;
	        String pgType2 = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
		
	        // get health of gateway2
	        String gatewayHealth2 = tranHelper.getGatewayHealth(pgType2,paymentTypeRowNum);
	        testConfig.logComment("Health of gateway1 " + pgType2 + gatewayHealth2 );
	       
	        //TODO: configure gateway3
	        paymentTypeRowNum =109;
	        String pgType3 = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
		
	        // get health of gateway3
	        String gatewayHealth3 = tranHelper.getGatewayHealth(pgType3,paymentTypeRowNum);
	        testConfig.logComment("Health of gateway1 " + pgType3 + gatewayHealth3 );
	        
	        //for getting gateway info
			TestDataReader pgInfoData = new TestDataReader(testConfig, "PGInfo");
		
			  //compare gateway health 
	        if(Double.parseDouble(gatewayHealth1)>Double.parseDouble(gatewayHealth2) && Double.parseDouble(gatewayHealth1)>Double.parseDouble(gatewayHealth3) ) 
	        {
	        	highestHealth = gatewayHealth1;
	        	 pgTypeHigher =pgType1;
	        	 //set pg_id as id of gateway with greatest health
	        	testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgTypeHigher, "PG_TYPE_ID"));
	        	higherHealthGatewayId = testConfig.getRunTimeProperty("pg_id");
	        	if(Double.parseDouble(gatewayHealth2)>Double.parseDouble(gatewayHealth3))
	        	{
	      	       
	        	secondHigherHealth = gatewayHealth2;
	        	pgTypeSecondHigher = pgType2;
	       	    pgTypeLower =pgType3;
	         	lowerHealthGatewayId = pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgTypeLower, "PG_TYPE_ID");
               
	        	}
	        	else{
	        		secondHigherHealth = gatewayHealth3;
	        		pgTypeSecondHigher = pgType3;
		       	    pgTypeLower = pgType2;
		         	lowerHealthGatewayId = pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgTypeLower, "PG_TYPE_ID");
	              
	        	}
	        }
	        else if(Double.parseDouble(gatewayHealth2)>Double.parseDouble(gatewayHealth1) && Double.parseDouble(gatewayHealth2)>Double.parseDouble(gatewayHealth3))
	        {
	        	highestHealth = gatewayHealth2;
	        	 pgTypeHigher = pgType2 ;
		        	testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgTypeHigher, "PG_TYPE_ID"));
		        	higherHealthGatewayId = testConfig.getRunTimeProperty("pg_id");
		        	
	        	if(Double.parseDouble(gatewayHealth1)>Double.parseDouble(gatewayHealth3))
	        	{
		        	secondHigherHealth = gatewayHealth1;
		        	pgTypeSecondHigher = pgType1;
		        	
		       	    pgTypeLower = pgType3;
		         	lowerHealthGatewayId = pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgTypeLower, "PG_TYPE_ID");
	              
	        	}
		        	else
		        	{		
		        	secondHigherHealth = gatewayHealth3;
		        	pgTypeSecondHigher = pgType3;
		       	    pgTypeLower = pgType1;
		         	lowerHealthGatewayId = pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgTypeLower, "PG_TYPE_ID");
		        	}
	        }
	        else {
	        	highestHealth = gatewayHealth3;
	        	 pgTypeHigher = pgType3;
		        	testConfig.putRunTimeProperty("pg_id", pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgTypeHigher, "PG_TYPE_ID"));
		        	higherHealthGatewayId = testConfig.getRunTimeProperty("pg_id");
		        
	        	if(Double.parseDouble(gatewayHealth1)>Double.parseDouble(gatewayHealth2))
	        	{
		        	secondHigherHealth = gatewayHealth1;
		        	pgTypeSecondHigher = pgType1;
		       	    pgTypeLower = pgType2;
		         	lowerHealthGatewayId = pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgTypeLower, "PG_TYPE_ID");
	              
	        	}
		        	else{
		        		secondHigherHealth = gatewayHealth2;
		        		pgTypeSecondHigher = pgType2;
			       	    pgTypeLower = pgType1;
			         	lowerHealthGatewayId = pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgTypeLower, "PG_TYPE_ID");
		              
		        	}
	        }
	        
	        //ensure all 3 gateways are up
	        tranHelper.MakeGatewayAsStatusUp(pgType1);
	        tranHelper.MakeGatewayAsStatusUp(pgType2);
	        tranHelper.MakeGatewayAsStatusUp(pgType3);
	        //do ten transactions are verify 80,20 rule for highest and second higher health gateways 
        	doNBTransactionsAndVerify_80_20_PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,10,higherHealthGatewayId,lowerHealthGatewayId);

	        //if health of any 2 is less than .5 or all health is <0.5
        	if((Double.parseDouble(gatewayHealth1)<0.5 && Double.parseDouble(gatewayHealth2)<0.5) || (Double.parseDouble(gatewayHealth1)<0.5 && Double.parseDouble(gatewayHealth3)<0.5)|| (Double.parseDouble(gatewayHealth2)<0.5 && Double.parseDouble(gatewayHealth3)<0.5) || (Double.parseDouble(gatewayHealth1)<0.5 && Double.parseDouble(gatewayHealth2)<0.5 && Double.parseDouble(gatewayHealth3)<0.5))
        	{
        		 //do ten transactions and verify 100% rule for highest health gateway
        		doNBTransactionsAndVerify100PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,10,testConfig.getRunTimeProperty("pg_id"));
        	}
	        
	        //make status of only one gateway(lowest health gateway) as down
        	// make gateway down
			doInvalidTransactionAndMakeGatewayDown(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,pgTypeLower);
			
			secondHigherHealthGatewayId = pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgTypeSecondHigher, "PG_TYPE_ID");
            
			//compare to check if health of gateways is > or < than 0.5
			if(Double.parseDouble(highestHealth)>0.5 &&  Double.parseDouble(secondHigherHealth) > 0.5)
			{
				
	        //do ten transactions are verify 80,20 rule for highest and second higher health gateways 
        	doNBTransactionsAndVerify_80_20_PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,10,higherHealthGatewayId,secondHigherHealthGatewayId);
			}
			// if health of any one or both is less than 0.5 , 100% rule for highest transaction
			else if(Double.parseDouble(highestHealth) <0.5 || Double.parseDouble(secondHigherHealth) < 0.5) 
			{
				//do ten transacitons and verify 100% rule for highest gateway
				doNBTransactionsAndVerify100PercentRule(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,10,testConfig.getRunTimeProperty("pg_id"));
	        	
			}
	        
	        
	       
		
	}
	
	/**
	 * Do a netbanking transaction and verify gateway Id
	 * @param testConfig
	 * @param tranHelper
	 * @param transactionRowNum
	 * @param paymentTypeRowNum
	 * @param cardDetailsRowNum
	 * @return
	 */

public String doNBTransactionAndVerifyGatewayId(Config testConfig, TransactionHelper tranHelper, int transactionRowNum,int paymentTypeRowNum,int cardDetailsRowNum,String expectedGatewayId)
{
	
	  //do test transaction 
	tranHelper.GetTestTransactionPage();

	tranHelper.bankRedirectPage = (BankRedirectPage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
     //click submit query on simulator page
    if(Element.getPageElement(testConfig, How.css, "input[type=\"submit\"]") != null)
    	Element.click(testConfig, Element.getPageElement(testConfig, How.css, "input[type=\"submit\"]"), "Submit");
    
   // get gateway id for above transaction
	Map<String, String> transData = DataBase.executeSelectQuery(testConfig, 1, 1);
	
	if(transData!=null)
	{
	String gatewayId = transData.get("pgId");
	if(gatewayId.contentEquals(expectedGatewayId))
       return "success";
	else
		return "invalid gateway";
	}
	//return "transaction failure"; TODO: track failed transactions as well
testConfig.logFail("transaction not found");
return "fail";
	
}
public String doNBTransactionAndVerifyGatewayId(Config testConfig, TransactionHelper tranHelper, int transactionRowNum,int paymentTypeRowNum,int cardDetailsRowNum,String expectedGatewayId1,String expectedGatewayId2)

{
	  //do test transaction 
		tranHelper.GetTestTransactionPage();

		tranHelper.bankRedirectPage = (BankRedirectPage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
	     //click submit query on simulator page
	    if(Element.getPageElement(testConfig, How.css, "input[type=\"submit\"]") != null)
	    	Element.click(testConfig, Element.getPageElement(testConfig, How.css, "input[type=\"submit\"]"), "Submit");
	    
	   // get gateway id for above transaction
		Map<String, String> transData = DataBase.executeSelectQuery(testConfig, 1, 1);
		
		if(transData!=null)
		{
		String gatewayId = transData.get("pgId");
		if(gatewayId.contentEquals(expectedGatewayId1)) 
	       return "successHigh";
		else if(gatewayId.contentEquals(expectedGatewayId2))
		   return "successLow";
		else
			return "invalid gateway";
		}
		//return "transaction failure"; TODO: track failed transactions as well
	testConfig.logFail("transaction not found");
	return "fail";
}
/**
 * Do n number of NB transactions and verify gateway Id
 * @param testConfig
 * @param tranHelper
 * @param transactionRowNum
 * @param paymentTypeRowNum
 * @param cardDetailsRowNum
 * @param count - Number of NB transactions to be made
 */
public void doNBTransactionsAndVerify100PercentRule(Config testConfig, TransactionHelper tranHelper, int transactionRowNum,int paymentTypeRowNum,int cardDetailsRowNum,int count,String expectedGatewayId)
{
	String transStatus;
	int successCount=0;
	int failureCount = 0;
	// do five transactions to verify gatewayId for 100% rule
	for(int i =0;i<count;i++)
	{
	
		transStatus = doNBTransactionAndVerifyGatewayId(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,expectedGatewayId);
		switch(transStatus)
		{
		case "success":
			successCount++;
			break;
			
		case "invalid gateway":
			testConfig.logFail("Transaction routed via gateway with gateway id as " + testConfig.getRunTimeProperty("pg_id"));
			break;
		/*case "transaction failure":
			failureCount++;
			break;*/
		}
	}
		
	testConfig.logComment("No. of successful transactions for " + "is" + successCount);
	//testConfig.logComment("No. of failed transactions is" + failureCount);
	failureCount = 0;
	successCount = 0;
}

public void doNBTransactionsAndVerify_80_20_PercentRule(Config testConfig, TransactionHelper tranHelper, int transactionRowNum,int paymentTypeRowNum,int cardDetailsRowNum,int count,String higherHealthGatewayId, String lowerHealthGatewayId)
{
	 int gatewayHighCount = 0;
	  int gatewayLowCount = 0;
	  int failureCount = 0;
	  String transStatus;
	  
	  for(int i = 0 ; i<count; i++)
	  {
		  transStatus = doNBTransactionAndVerifyGatewayId(testConfig,tranHelper,transactionRowNum,paymentTypeRowNum,cardDetailsRowNum,higherHealthGatewayId,lowerHealthGatewayId);
		  switch(transStatus)
			{
			case "successHigh":
				gatewayHighCount++;
				break;
				
			case "successLow":
				gatewayLowCount++;
			case "invalid gateway":
				testConfig.logFail("Transaction routed via gateway with gateway id as " + testConfig.getRunTimeProperty("pg_id"));
				break;
				
			}
				
			/*case "transaction failure":
				failureCount++;
				break;*/
			}	
	  
	  testConfig.logComment("No. of time gateway with greater health is used is" + gatewayHighCount);
		testConfig.logComment("No. of time gateway with lower health is used is" + gatewayLowCount);
		//testConfig.logComment("No. of failed transaction is " + failureCount);
		Helper.compareTrue(testConfig, "No of times gateway with greater health is used is less than 80%", gatewayHighCount>=8);
		Helper.compareTrue(testConfig, "No of times gateway with lesser health is used is more than 20%", gatewayLowCount<=2);
		
}
/**
 * make 3 in progress NB transactions and make gateway down
 * @param testConfig
 * @param tranHelper
 * @param transactionRowNum
 * @param paymentTypeRowNum
 * @param cardDetailsRowNum
 */
//TODO: configure the i condition based on simulator settings
public void doInvalidTransactionAndMakeGatewayDown(Config testConfig, TransactionHelper tranHelper, int transactionRowNum,int paymentTypeRowNum,int cardDetailsRowNum,String pgType)
{
 for(int i = 0;i<10; i++)
 {
	  //do test transaction 
		tranHelper.GetTestTransactionPage();
		tranHelper.bankRedirectPage = (BankRedirectPage)tranHelper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
		if(tranHelper.getGatewayStatus(pgType).equals("0") || tranHelper.getGatewayStatus(pgType).equals("2"))
			break;
			
 }

}

		    
	/*@Test(description = "Verify that transaction success rate is equal to pay success rate parameter", 
			dataProvider="GetTestConfig", timeOut=600000, groups="1")
	
	public void testVerifyPaySuccessRate(Config testConfig) {
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		//Union Bank of India
				int transactionRowNum = 36;
				int paymentTypeRowNum = 212;
				int cardDetailsRowNum = -1;
				
		 // get health of gateway 1
        TestDataReader paymentTypeData = new TestDataReader(testConfig, "PaymentType");
        String pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
        
        String gatewayHealth1 = helper.getGatewayHealth(pgType,paymentTypeRowNum);
        
      // get health of gateway 2 (HDFC NB)
      	paymentTypeRowNum = 109;
        pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
     
        String gatewayHealth2 = helper.getGatewayHealth(pgType,paymentTypeRowNum);
        String keyName = "paySuccessRate";
        
      if(Double.parseDouble(gatewayHealth1)>Double.parseDouble(gatewayHealth2)) 
    	{
    	  paymentTypeRowNum = 212;
    	  pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
          
    	}
    	else 
    	{
    		paymentTypeRowNum = 109;
    		 pgType = paymentTypeData.GetData(paymentTypeRowNum, "PG_TYPE");
    		
    	}
       TestDataReader pgInfoData = new TestDataReader(testConfig, "PGInfo");
       String pg_id = pgInfoData.GetCorrespondingColumnValue("PG_TYPE", pgType, "PG_TYPE_ID");
    	
		
        helper.transactionData = new TestDataReader(helper.testConfig,"TransactionDetails");
		String merchantName = helper.transactionData.GetData(transactionRowNum, "Comments");
		MerchantListPage merchantListPage = helper.home.clickMerchantList();
		merchantListPage.SearchMerchant(merchantName);
		merchantDetailsPage = merchantListPage.clickFirstMerchantKey();
		PGKeysPage pgKeys = merchantDetailsPage.clickPGKeys();
		//set success rate as 7 for healthier gateway
		pgKeys.addPGKey(pg_id, keyName,"7");
		testConfig.logComment("gateway1 health is " + gatewayHealth1);
		testConfig.logComment("gateway2 health is" + gatewayHealth2);
		int failCount = 0;
		for(int i =0;i<10;i++)
		{
			 helper.GetTestTransactionPage();
			
			 helper.bankRedirectPage = (BankRedirectPage) helper.DoTestTransaction(transactionRowNum, paymentTypeRowNum, cardDetailsRowNum, ExpectedResponsePage.BankRedirectPage);
			 
				 //click submit query on simulator page
			 Element.click(testconfig, Element.getPageElement(testConfig, How.css, "input[type=\"submit\"]"), "submit" );
					// get status for above transaction
				Map<String, String> transData = DataBase.executeSelectQuery(testConfig, 1, 1);
				if(transData.get("status").contentEquals("failed"))
				{
					failCount ++;
				}
				  
		}
		testConfig.logComment("The no. of transaction failures are" + failCount);
		Helper.compareTrue(testConfig, "No of transaction failures", failCount<=4);
	
		Assert.assertTrue(testConfig.getTestResult());
	}

*/
}
