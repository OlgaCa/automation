package Utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Popup {

	/**
	 * Accept the Pop-up
	 * @param Config test config instance for driver instance to be used
	 */
	public static void ok(Config testConfig)
	{
		Alert alert= getPopup(testConfig);
		try
		{
			if(alert!=null)
			{
				alert.accept();
				testConfig.driver.switchTo().defaultContent();
				testConfig.logComment("Accepted the Pop-up.");
			}
		}
		catch(Exception e) 
		{
			testConfig.logException(e);
		}
	}

	/**
	 * Cancel the Pop-up
	 * @param Config test config instance for driver instance to be used
	 */
	public static void cancel(Config testConfig)
	{
		Alert alert= getPopup(testConfig);
		try
		{
			if(alert!=null)
			{
				alert.accept();
				testConfig.driver.switchTo().defaultContent();
				testConfig.logComment("Dismissed the Pop-up.");
			}
		}
		catch(Exception e) 
		{
			testConfig.logException(e);
		}
	}

	/**
	 * Get the Pop-up Text
	 * @param Config test config instance for driver instance to be used
	 * @return Pop-up Text
	 */
	public static String text(Config testConfig)
	{
		Alert alert= getPopup(testConfig);
		String text = alert.getText();
		return text;
	}

	private static Alert getPopup(Config testConfig)
	{
		try{
			Long ObjectWaitTime = Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime"));
			WebDriverWait wait= new WebDriverWait(testConfig.driver, ObjectWaitTime);
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			alert = testConfig.driver.switchTo().alert();
			testConfig.logComment("Got the Alert with text '" + alert.getText() + "'");
			return alert;
		}
		catch(Exception e)
		{
			testConfig.logFail("Could not find any Alert");
			testConfig.logException(e);
			return null;
		}
	}
	
	/**
	 * Java method to type characters in windows file upload dialog boxes
	 * @param characters Filename to upload
	 * @param robot
	 */
	public static void type(String characters)  {
		    Robot robot = null;
			try {
				robot = new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        robot.delay(9000);
		    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    robot.delay(5000);
		    StringBuffer newText = new StringBuffer() ;
	        for(int i=0;i<characters.length();i++)
	        {
	        	newText.append(characters.charAt(i));
	        	if(characters.charAt(i)=='\\'){
	        		if(characters.charAt(i+1)=='\\'){
	        			i++;
	        		}
	        	}
	        }
		    StringSelection stringSelection = new StringSelection( newText.toString() );
		    clipboard.setContents(stringSelection,stringSelection);

		    robot.delay(5000);
		    robot.keyPress(KeyEvent.VK_CLEAR);
		    // press CTRL+V
		    robot.keyPress(KeyEvent.VK_CONTROL);
		    robot.keyPress(KeyEvent.VK_V);
		    robot.keyRelease(KeyEvent.VK_V);
		    // release CTRL+V
		    robot.keyRelease(KeyEvent.VK_CONTROL);
		    robot.delay(5000);

		    //Press Enter
		    robot.keyPress(KeyEvent.VK_ENTER);
		   // robot.keyPress(KeyEvent.VK_ENTER);
		    //Release Enter
		    //robot.keyRelease(KeyEvent.VK_ENTER);
		    robot.keyRelease(KeyEvent.VK_ENTER);
		    robot.delay(5000);
		}

/*	*//**
	 * Java method to download a file to downloads folder
	 *//*
	public static void saveFileToDownloadsFolder() 
	{
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CLEAR);

		robot.delay(5000);
		// press ALT +s
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_ALT);

		// press ALT+S
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_O);
		robot.keyRelease(KeyEvent.VK_O);
		robot.keyRelease(KeyEvent.VK_ALT);

		// press ALT+S
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.delay(5000);

		// Press Enter
		robot.keyPress(KeyEvent.VK_ENTER);
		// Release Enter
		robot.keyRelease(KeyEvent.VK_ENTER);
	}*/
	/**
	 * Java method to get system clipboard contents
	 */
	public String getClipboardContents(WebElement el)
	{
		String returnText = null;
		
		Clipboard systemClipboard =Toolkit.getDefaultToolkit().getSystemClipboard();
			// get the contents on the clipboard in a 
			// transferable object
			Transferable clipboardContents =systemClipboard.getContents(el);
			// check if clipboard is empty
			if (clipboardContents== null) {
				returnText = null;
			} else
				try {
					// see if DataFlavor of 
					// DataFlavor.stringFlavor is supported
					if (clipboardContents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
						// return text content
						 returnText =(String) clipboardContents.getTransferData(DataFlavor.stringFlavor);
						return returnText;
					}
				} catch (UnsupportedFlavorException ufe) {
					ufe.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
		return returnText;
	}	
	
	public void clearClipBoard() 
	{

    	Clipboard clipboard =Toolkit.getDefaultToolkit().getSystemClipboard();
		
    	//clear clipboard
		  StringSelection stringSelection = new StringSelection("");
		  clipboard.setContents(stringSelection,stringSelection);
	}
	
	public static void confirmNoPopup(Config testConfig,String txt){
		
		try
		{
			Long ObjectWaitTime = Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime"));
			WebDriverWait wait= new WebDriverWait(testConfig.driver, 10);
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			//Alert alert = testConfig.driver.switchTo().alert();
			if(alert!=null)
			{
				boolean result = true;
				if(alert.getText().equals(txt))
				{
					result = false;
					testConfig.driver.switchTo().alert();
					Helper.compareTrue(testConfig, "Absence of Alert with text as:" + txt, result);
					alert.accept();
					return;
				}
				
				Helper.compareTrue(testConfig, "Absence of Alert with text as: " + txt, result);
				return;
				
			}
			else
			{
				Helper.compareTrue(testConfig, "Absence of Alert with text as: " + txt, true);
				return;
			}
		}
		catch(Exception e) 
		{
			Helper.compareTrue(testConfig, "Absence of Alert with text as: " + txt, true);
			return;
		}
		
	}
	
	
	public static boolean isAlertPresent(Config testConfig){
		try
		{
			Alert alert=testConfig.driver.switchTo().alert();
			testConfig.logComment("Got the Alert with text '" + alert.getText() + "'");
			return true;
		}
		catch(Exception e){
			Log.Comment("Alert not present", testConfig);
			return false;
		}
		}
}
