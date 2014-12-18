
/**
 * @author atul.jain
 *
 */
package Test.zKillFirefox;

import org.testng.annotations.Test;

import Utils.TestBase;


public class KillFirefox extends TestBase{

	@Test(description = "Kill all firefox instances",  dataProvider="GetTestConfig")
	public void testKillFirefox(Config testConfig) throws IOException
	{
		Runtime.getRuntime().exec("cmd /c start C:\\PayU\\Prerequisite\\QuitAllFirefox.bat");
	}
	
	@Test(description = "Kill all firefox instances",  dataProvider="GetTestConfig")
	public void testKillFirefox2(Config testConfig) throws IOException
	{
		Runtime.getRuntime().exec("cmd /c start C:\\PayU\\Prerequisite\\QuitAllFirefox.bat");
	}
}