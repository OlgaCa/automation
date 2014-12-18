package Test.AdminPanel.WebServices;

import PageObject.AdminPanel.Home.HomePage;
import PageObject.AdminPanel.Home.LoginPage;
import PageObject.AdminPanel.Payments.Response.TestResponsePage;
import PageObject.AdminPanel.WebServices.TestwsPage;
import PageObject.AdminPanel.WebServices.TestwsResponsePage;
import Utils.Config;
import Utils.TestDataReader;

public class TestWebServicesCall {

	private Config testConfig;

	public LoginPage login;
	public HomePage home;
	public TestwsPage testws;
	public TestwsResponsePage testWsresponse;
	public TestDataReader webServiceMerchantData;
	public TestResponsePage testResponse;

	public TestWebServicesCall(Config testConfig)
	{
		this.testConfig = testConfig;
	}

	/**
	 * Navigates the current browser window to the web services page
	 */
	public void GetTestWebServicesPage()
	{
		try
		{
			testResponse = new TestResponsePage(testConfig);
			//home = testResponse.ClickHome();
			testws = home.ClickTestWebService();
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}
	}

	/*public void DoTestWebServices(TestDataReader transactionData, int transactionRow,TestResponsePage testResponse, int webserviceRow, int webserviceresponseRow)
	{
		try
		{
			//Fill Transaction Details
			testws = new TestwsPage(testConfig);
			webServiceMerchantData = testws.FillWebServiceDetail(transactionData, transactionRow, testResponse, webserviceRow);
			testWsresponse = testws.clickSubmit();
			testWsresponse.VerifyWebServiceResponse(transactionData, transactionRow, webserviceRow, webserviceresponseRow);
		}
		catch(Exception e)
		{
			testConfig.logException(e);
			throw e;
		}
	}*/
	
	
}
