package Utils;

import java.lang.reflect.Method;

import org.testng.annotations.Parameters;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.asserts.SoftAssert;

public class TestBase 
{	
	protected static ThreadLocal<Config> threadLocalConfig = new ThreadLocal<Config>();

	@BeforeClass(alwaysRun=true)
	@Parameters({"browser", "environment", "testngOutputDir"})
	public void InitializeParameters(@Optional String browser, @Optional String environment,
			@Optional String testngOutputDir)
	{
		Config.BrowserName = browser;
		Config.Environment = environment;
		Config.ResultsDir = testngOutputDir;
	}
	
	@BeforeMethod
	public void init(){
		Utils.SoftAssert.getSoftAssert();
	}

	@DataProvider(name="GetTestConfig")
	public Object[][] GetTestConfig(Method method) 
	{
		Config testConf = new Config(method);
		testConf.testName = method.getDeclaringClass().getName() + "." +  method.getName();
		testConf.testStartTime = Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		Log.Pass("<B>Test '" + testConf.testName +"' Started on '"+ testConf.testStartTime +"'</B>", testConf);
		testConf.openBrowser();
		threadLocalConfig.set(testConf);

		return new Object[][]
				{
				{testConf}
				};
	}
	
	/**
	 * used by TransactionEmail test cases
	 * @param method
	 * @return
	 */
	@DataProvider(name="GetUrlTestConfig")
	public Object[][] GetUrlTestConfig(Method method) 
	{
		Config testConf = new Config(method);
		testConf.testName = method.getDeclaringClass().getName() + "." +  method.getName();
		testConf.testStartTime = Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		Log.Pass("<B>Test '" + testConf.testName +"' Started on '"+ testConf.testStartTime +"'</B>", testConf);
		testConf.openBrowser();
		threadLocalConfig.set(testConf);
		

		return new Object[][]
				{
				{testConf,new String[] {"ivr", "invoice"}}
				};
	}
	
	/**
	 * used by TransactionEmail test cases
	 * @param method
	 * @return
	 */
	@DataProvider(name="GetRequestRefundTestConfig")
	public Object[][] GetRequestRefundTestConfig(Method method) 
	{
		Config testConf = new Config(method);
		testConf.testName = method.getDeclaringClass().getName() + "." +  method.getName();
		testConf.testStartTime = Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		Log.Pass("<B>Test '" + testConf.testName +"' Started on '"+ testConf.testStartTime +"'</B>", testConf);
		testConf.openBrowser();
		threadLocalConfig.set(testConf);
		

		return new Object[][]
				{
				{testConf,new String[] {"partial", "full"}}
				};
	}
	
	@DataProvider(name="GetTwoBrowserTestConfig")
	public Object[][] GetTwoBrowserTestConfig(Method method) 
	{
		Config testConf = new Config(method);
		Config secondaryConfig = new Config(method);
		
		testConf.testName = method.getDeclaringClass().getName() + "." +  method.getName();
		testConf.testStartTime = Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		Log.Pass("<B>Test '" + testConf.testName +"' Started on '"+ testConf.testStartTime +"'</B>", testConf);
		
		testConf.openBrowser();
		secondaryConfig.openBrowser();
		threadLocalConfig.set(testConf);

		return new Object[][]
				{
				{testConf, secondaryConfig}
				};
	}	
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result)
	{
		Config testConf = threadLocalConfig.get();
		if(testConf!=null)
		{
			
			testConf.endExecutionOnfailure = false;
			testConf.testEndTime = Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			testConf.closeBrowser(result);
			testConf.endTest();
			testConf = null;
		}
	}


	

}
