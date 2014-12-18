
package Utils;

import io.selendroid.SelendroidCapabilities;
import io.selendroid.SelendroidConfiguration;
import io.selendroid.SelendroidLauncher;
import io.selendroid.exceptions.SelendroidException;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;

public class Config {

	//parameters that can be overridden through command line and are same for all executing tests
	public static String BrowserName;
	public static String Environment;
	public static String ResultsDir;

	//package fields
	String testStartTime;
	String testEndTime;
	String testName;

	//parameters different for every test 
	public WebDriver driver;
	public Method testMethod;
	public HashMap<String,TestDataReader> dataReader = new HashMap<String,TestDataReader>();
	public TestDataReader testDataReaderObj;
	private boolean testResult;
	public boolean getTestResult() 
	{
		return testResult;
	}

	public Connection connection;

	public boolean enableScreenshot = true;
	public boolean debugMode = false;
	public boolean logToStandardOut = true;
	public boolean endExecutionOnfailure = false;
	public boolean isMobile = false;

	//stores the run time properties (different for every test)
	private Properties runtimeProperties;
	
	public SelendroidLauncher selLaunch;
	public SelendroidCapabilities selCap;
	public boolean isSelendroidServerOn = false;

	public Config(Method method)
	{ 
		try {

			endExecutionOnfailure = true;

			this.testMethod = method;
			this.testResult = true;
			this.connection = null;

			//Read the Config file
			Properties property = new Properties();

			String path = System.getProperty("user.dir") + "//Parameters//Config.properties";
			logComment("Read the configuration file:-" + path.replace("//", "\\"));
			FileInputStream fn = new FileInputStream(path);
			property.load(fn);
			fn.close();

			//override the environment value if passed through ant command line
			if(!(Environment==null || Environment.isEmpty())) 
				property.put("Environment", Environment);

			path = System.getProperty("user.dir") + "//Parameters//"+ property.get("Environment") +".properties";
			logComment("Read the environment file:-" + path.replace("//", "\\"));
			fn = new FileInputStream(path);
			property.load(fn);
			fn.close();

			this.runtimeProperties = new Properties();
			Enumeration<Object> em = property.keys();
			while(em.hasMoreElements())
			{
				String str = (String)em.nextElement();
				putRunTimeProperty(str, (String)property.get(str));
			}

			this.debugMode = (getRunTimeProperty("DebugMode").toLowerCase().equals("true")) ? true:false;
			this.logToStandardOut = (getRunTimeProperty("LogToStandardOut").toLowerCase().equals("true")) ? true:false;

			//override run time properties if passed through ant command line
			if(!(BrowserName==null || BrowserName.isEmpty())) 
				putRunTimeProperty("Browser", BrowserName);

			if(!(ResultsDir==null || ResultsDir.isEmpty()))
				putRunTimeProperty("ResultsDir", ResultsDir);
             
			//Override remote execution in case of mobile
			if(getRunTimeProperty("Browser").equals("android_web") || getRunTimeProperty("Browser").equals("android_native") )
			{putRunTimeProperty("RemoteExecution", "true");}
			
			//Set the full path of test data sheet
			String testDataSheet = System.getProperty("user.dir") + getRunTimeProperty("TestDataSheet");
			logComment("Test data sheet is:-" + testDataSheet.replace("//", "\\"));
			putRunTimeProperty("TestDataSheet", testDataSheet);

			//Set the full path of checkout page
			String checkoutPage = System.getProperty("user.dir") + getRunTimeProperty("checkoutPage");
			logComment("Checkout page is:-" + checkoutPage.replace("//", "\\"));
			putRunTimeProperty("checkoutPage", checkoutPage);

			//Set the full path of results dir
			String resultsDir = System.getProperty("user.dir") + getRunTimeProperty("ResultsDir");
			logComment("Results Directory is:-" + resultsDir.replace("//", "\\"));
			putRunTimeProperty("ResultsDir", resultsDir);
			endExecutionOnfailure = false;
			
			isMobile = (
					(getRunTimeProperty("Browser").equals("android_web") || getRunTimeProperty("Browser").equals("android_native") )
					&& getRunTimeProperty("RemoteExecution").equals("true")
					);
			
			startSelendroidServer(getRunTimeProperty("Browser"));
		} 
		catch (IOException e) {
			logException(e);
		}
	}
	
	/**
	 * Get TestDataReader Objects
	 */
	public TestDataReader getTestDataReaderObjects(String sheetName)
	{
		return dataReader.get(sheetName);
	}

	/**
	 * Set TestDataReader objects
	 */
	public void setTestDataReaderObjects(String[] sheetNames)
	{
		for(int i = 0; i<sheetNames.length;i++)
		{
		this.setTestDataReaderObjects(sheetNames[i]);	
		}
		
	}
	/**
	 * Set TestDataReader object
	 */
	public void setTestDataReaderObjects(String sheetName)
	{
		if(this.getTestDataReaderObjects(sheetName) == null)
		{
	        testDataReaderObj = new TestDataReader(this,sheetName);
	        dataReader.put(sheetName, testDataReaderObj);
		}
	}
	/**
	 * Get the Run Time Property value
	 * @param key key name whose value is needed
	 * @return value of the specified key
	 */
	public String getRunTimeProperty(String key)
	{
		String keyName = key.toLowerCase();
		String value = "";
		try
		{
			value = runtimeProperties.get(keyName).toString();
			if(debugMode)logComment("Reading Run-Time key-" + keyName + " value:-'" + value + "'");
		}
		catch (Exception e)
		{
			if(debugMode)logComment("'" + key + "' not found in Run Time Properties");
			return null;
		}
		return value;
	}
	
	/**
	 * Get the Run Time Property value
	 * @param key key name whose value is needed
	 * @return value of the specified key
	 */
	public Object getObjectRunTimeProperty(String key)
	{
		String keyName = key.toLowerCase();
		Object value = "";
		try
		{
			value = runtimeProperties.get(keyName);
			if(debugMode)logComment("Reading Run-Time key-" + keyName + " value:-'" + value + "'");
		}
		catch (Exception e)
		{
			if(debugMode)logComment("'" + key + "' not found in Run Time Properties");
			return null;
		}
		return value;
	}

	/**
	 * Add the given key value pair in the Run Time Properties
	 * @param key
	 * @param value
	 */
	public void putRunTimeProperty(String key, String value)
	{
		String keyName = key.toLowerCase();    
		runtimeProperties.put(keyName, value);
		if(debugMode)logComment("Putting Run-Time key-" + keyName + " value:-'" + value + "'");
	}
	
	/**
	 * Add the given key value pair in the Run Time Properties
	 * @param key
	 * @param value
	 */
	public void putRunTimeProperty(String key, Object value)
	{
		String keyName = key.toLowerCase();    
		runtimeProperties.put(keyName, value);
		if(debugMode)logComment("Putting Run-Time key-" + keyName + " value:-'" + value + "'");
	}
	
	/**
	 * Add the given key ArrayListJSONObject pair in the Run Time Properties
	 * 
	 */
    public void putJSONArrayListInRunTimeProperty(String key, ArrayList<JSONObject> table)
    {
    	String keyName = key.toLowerCase();    
		runtimeProperties.put(keyName, table);
		if(debugMode)logComment("Putting Run-Time key-" + keyName + " value:-'" + table.toString() + "'");
    }
    /**
	 * Get the Run Time Property value
	 * @param key key name whose value is needed
	 * @return value of the specified key
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<JSONObject> getJSONArrayListFromRunTimeProperty(String key)
	{
		String keyName = key.toLowerCase();
		ArrayList<JSONObject> value;
		try
		{
			value = (ArrayList<JSONObject>) runtimeProperties.get(keyName);
			if(debugMode)logComment("Reading Run-Time key-" + keyName + " value:-'" + value + "'");
		}
		catch (Exception e)
		{
			if(debugMode)logComment("'" + key + "' not found in Run Time Properties");
			return null;
		}
		return value;
	}
	/**
	 * Removes the given key from the Run Time Properties
	 * @param key
	 */
	public void removeRunTimeProperty(String key)
	{
		String keyName = key.toLowerCase();    
		runtimeProperties.remove(keyName);
		if(debugMode)logComment("Removing Run-Time key-" + keyName + " value:-'");
	}

	public void logFail(String message)
	{
		testResult = false;
		Log.Fail(message,this);
	}

	public void logFail(String what, String expected, String actual)
	{
		testResult = false;
		String message = "Expected '" + what + "' was :-'" + expected + "'. But actual is '" + actual + "'";
		Log.Fail(message,this);
	}

	public void logFail(String what, int expected, int actual)
	{
		testResult = false;
		String message = "Expected '" + what + "' was :-'" + expected + "'. But actual is '" + actual + "'";
		Log.Fail(message,this);
	}

	public void logException(Throwable e)
	{
		testResult = false;
		Log.Fail(e.toString(),this);
	}

	public void logPass(String message)
	{
		Log.Pass(message,this);
	}

	public void logPass(String what, String actual)
	{
		String message = "Verified '" + what + "' as :-'" + actual + "'";
		Log.Pass(message,this);
	}

	public void logPass(String what, int actual)
	{
		String message = "Verified '" + what + "' as :-'" + actual + "'";
		Log.Pass(message,this);
	}

	public void logComment(String message)
	{
		Log.Comment(message,this);
	}

	public void logWarning(String message)
	{
		Log.Warning(message,this);
	}
	public void openBrowser()
	{
		int retryCnt = 3;
		
		while(this.driver==null && retryCnt > 0) 
		{
			try
			{
				this.driver = Browser.openBrowser(this);
			}
			catch (Exception e)
			{
				Log.Warning("Retrying the browser launch:-" + e.getLocalizedMessage(), this);
				retryCnt--;
				if(retryCnt==1) 
				{
					try 
					{
						//Wait for 5 minutes, to let other parallel runnning tests finish
						Browser.wait(this, 300);
						Log.Warning("Killing the running browser instances", this);
						String browser = getRunTimeProperty("Browser");
						//Kill the browser, this is when we get error that - 
						//Unable to bind to locking port 7054 within 45000 ms
						switch(browser.toLowerCase())
						{
						case "firefox":
							Runtime.getRuntime().exec("taskkill /f /im firefox.exe");
							break;
						case "chrome":
							Runtime.getRuntime().exec("taskkill /f /im chrome.exe");
							break;
						case "ie":
							Runtime.getRuntime().exec("taskkill /f /im iexplore.exe");
							break;
						case "opera":
							Runtime.getRuntime().exec("taskkill /f /im opera.exe");
							break;
						default:
							break;
						}
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
					endExecutionOnfailure = true;
				}
				if(retryCnt==0)
				{
					logFail("Browser could not be opened");
					Assert.assertTrue(false);
				}
				Browser.wait(this, 2);
			}
		}
		endExecutionOnfailure = false;
	}

	public void closeBrowser(ITestResult result)
	{
		logToStandardOut = true;
		if(result.getThrowable() != null) logException(result.getThrowable());

		try
		{
			Browser.closeBrowser(this);
		}
		catch(Exception e)
		{
		}
		
		try
		{
			Browser.quitBrowser(this);
		}
		catch(Exception ex)
		{
		}
		
		try
		{
			driver.switchTo().defaultContent();
			Browser.closeBrowser(this);
		}
		catch(Exception e)
		{
		}
		
		try
		{
			driver.switchTo().defaultContent();
			Browser.quitBrowser(this);
		}
		catch(Exception ex)
		{
		}
		
		driver = null;
	}

	public void closeBrowser()
	{
		logToStandardOut = true;

		Browser.quitBrowser(this);
		driver = null;
	}

	public void endTest()
	{
		runtimeProperties.clear();

		if(connection !=null)
			try {
				connection.close();
			} catch (SQLException e) {
				logException(e);
			}

		if (!testResult) {
			logFail("<B>Failure occured in test '" + testName +"' Ended on '"+ testEndTime +"'</B>");
		}
		else
		{
			logPass("<B>Test Passed '" + testName +"' Ended on '"+ testEndTime +"'</B>");
		}
		
		if(isSelendroidServerOn){
			try{ 
				selLaunch.stopSelendroid();
				logComment("Stopped Selendroid Server");
			}catch(Exception ex){
				logException(ex);
			}
		}
	}

	public void logFail(String what, float expected, float actual) {
		testResult = false;
		String message = "Expected '" + what + "' was :-'" + expected + "'. But actual is '" + actual + "'";
		Log.Fail(message,this);

	}

	public void logPass(String what, float actual) {
		String message = "Verified '" + what + "' as :-'" + actual + "'";
		Log.Pass(message,this);
	}

	public void setTestDataExcelDynamic(String path){
		//Set the full path of test data sheet
		switch (path) {
			case "productproduction" : path=System.getProperty("user.dir").substring(0,System.getProperty("user.dir").lastIndexOf("\\")) +"\\Product\\Parameters\\PRODUCTION.xls";
			break;
			
			case "product" : path=System.getProperty("user.dir").substring(0,System.getProperty("user.dir").lastIndexOf("\\")) +"\\Product\\Parameters\\ProductTestData.xls";
			break;
			
			case "paisa"	: path=System.getProperty("user.dir").substring(0,System.getProperty("user.dir").lastIndexOf("\\"))	+"\\Paisa\\Parameters\\PaisaTestData.xls";
			break;
			
			case "money"	: path=System.getProperty("user.dir").substring(0,System.getProperty("user.dir").lastIndexOf("\\"))	+"\\Money\\Parameters\\MoneyTestData.xls";
			break;
			
			default ://do nothing
				break;
		}
		logComment("Updated path of Test data sheet is :-" + path);
		putRunTimeProperty("TestDataSheet", path);

	}
	public void startSelendroidServer(String browserName){
		boolean flag = false;
		if(!(browserName.contains("android"))){
			return;
		}else{
			try{
				SelendroidConfiguration config = new SelendroidConfiguration();
				config.setTimeoutEmulatorStart(60000);
				config.setSelendroidServerPort(8081);
				if(browserName.contains("native")){
					if(getRunTimeProperty("androidTestAPK_Name")!=null){
						config.addSupportedApp(getRunTimeProperty("androidTestAPK_Name"));
						flag = true;
						logComment("Testing the Androiud Native App : "+getRunTimeProperty("androidTestAPK_Name").replaceAll(".apk", ""));
					}else{
						logFail("Incorrect Name of the Android APK to be tested");
					}
				}
				selLaunch = new SelendroidLauncher(config);
				//selLaunch.lauchSelendroid();
				isSelendroidServerOn = true;
				logComment("Selendroid server started");
				if(flag){
					selCap = new SelendroidCapabilities(getRunTimeProperty("androidTestAPK_AppId"));
				}
				
			}catch(SelendroidException selEx){
				logComment("Problem in starting Selendroid server - "+selEx.getMessage());
			}
		}

	}
}
