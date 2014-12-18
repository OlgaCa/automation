package Utils;

import java.io.File;

import org.apache.maven.surefire.report.ReportEntry;
import org.testng.Assert;
import org.testng.Reporter;

class Log {

	private static Boolean escapeOutput = false;

	public static void Pass(String message, Config testConfig)
	{
		if(testConfig.logToStandardOut) logToStandard(message);
		if(!escapeOutput) 
		{
			message = "<font color='Green'>" + message + "</font></br>";
		}
		Reporter.log(message);
	}

	public static void Warning(String message, Config testConfig)
	{
		if(testConfig.logToStandardOut) logToStandard(message);
		if(!escapeOutput) 
		{
			message = "<font color='Orange'>" + message + "</font></br>";
		}
		Reporter.log(message);
	
		
	}

	public static void Fail(String message, Config testConfig)
	{
		if(testConfig.enableScreenshot)
		{
			if(testConfig.driver!=null && testConfig.testMethod!=null)
			{
				File dest = Browser.getScreenShotFile(testConfig);
				Browser.takeScreenShoot(testConfig, dest);
				if(testConfig.getRunTimeProperty("NetExport").contentEquals("true"))
				{
				String[] reqRes = new String[2];
				reqRes = Browser.getLatestFireBugResponse(testConfig);
				if(reqRes!=null)
				{
				testConfig.logComment("Last request is" + reqRes[0]);
				testConfig.logComment("Last response is" + reqRes[1]);
				}
				}
			}
		}
		if(testConfig.logToStandardOut) logToStandard(message);
		if(!escapeOutput) 
		{
			message = "<font color='Red'>" + message + "</font></br>";
		}
		//Reporter.log(message);
		org.testng.asserts.SoftAssert soft=SoftAssert.getSoftAssert();
		soft.fail(message+"This is generated by soft asset");
	

		//Stop the execution if end execution flag is ON
		if(testConfig.endExecutionOnfailure) Assert.fail("Ending execution in the middle!");
	}

	public static void Comment(String message, Config testConfig)
	{
		if(testConfig.logToStandardOut) logToStandard(message);
		if(!escapeOutput) 
		{
			message = "<font color='Black'>" + message + "</font></br>";
		}
		Reporter.log(message);
	}

	private static void logToStandard(String message)
	{
		System.out.println(message);
	}
}
