package Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Element {

	/**
	 * Locator technique 
	 */
	public static enum How {className,css, id, linkText, name, partialLinkText, tagName, xPath};

	/**
	 * Gets the WebElement using the specified locator technique on the passed driver page
	 * @param Config test config instance for the driver
	 * @param how Locator technique to use
	 * @param what element to be found with given technique (any arguments in this string will be replaced with run time properties)
	 * @return found WebElement
	 */
	public static WebElement getPageElement(Config testConfig, How how, String what)
	{
		testConfig.logComment("Get the WebElement with " + how + ":" + what);
		what = Helper.replaceArgumentsWithRunTimeProperties(testConfig, what);

		try
		{
			switch(how)
			{
			case className: return testConfig.driver.findElement(By.className(what));
			case css: return testConfig.driver.findElement(By.cssSelector(what));
			case id: return testConfig.driver.findElement(By.id(what));
			case linkText: return testConfig.driver.findElement(By.linkText(what));
			case name: return testConfig.driver.findElement(By.name(what));
			case partialLinkText: return testConfig.driver.findElement(By.partialLinkText(what));
			case tagName: return testConfig.driver.findElement(By.tagName(what));
			case xPath: return testConfig.driver.findElement(By.xpath(what));
			default: return null;
			}
		}
		catch(StaleElementReferenceException e1){
			//retry
			Browser.wait(testConfig, 3);
			testConfig.logComment("Retrying getting element" + how + ":" + what);
			return  getPageElement(testConfig,how, what);
		}
		catch(Exception e)
		{
			testConfig.logWarning("Could not find the element on page");
			return null;
		}
	}

	/**
	 * Gets the list of WebElements using the specified locator technique on the passed driver page
	 * @param Config test config instance for the driver
	 * @param how Locator technique to use
	 * @param what element to be found with given technique (any arguments in this string will be replaced with run time properties)
	 * @return List of WebElements Found
	 */
	public static List<WebElement> getListOfElements(Config testConfig, How how,String what){
		testConfig.logComment("Get the List of WebElements with " + how + ":" + what);
		try
		{
			switch(how)
			{
			case className: 
				return testConfig.driver.findElements(By.className(what));
			case css: 
				return testConfig.driver.findElements(By.cssSelector(what));
			case id: 
				return testConfig.driver.findElements(By.id(what));
			case linkText: 
				return testConfig.driver.findElements(By.linkText(what));
			case name: 
				return testConfig.driver.findElements(By.name(what));
			case partialLinkText: 
				return testConfig.driver.findElements(By.partialLinkText(what));
			case tagName: 
				return testConfig.driver.findElements(By.tagName(what));
			case xPath: 
				return testConfig.driver.findElements(By.xpath(what));
			default: 
				return null;
			}
		}
		catch(StaleElementReferenceException e1){
			//retry
			return  getListOfElements(testConfig,how, what);
		}
		catch(Exception e)
		{
			testConfig.logWarning("Could not find the list of the elements on page");
			return null;
		}
	}

	
	private static List<WebElement> getiFramesOnPage(WebDriver driver)
	{
		//List<WebElement> frames = driver.findElements(By.tagName("frame"));
		List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
		//frames.addAll(iframes);
		return iframes;
	}

	/**
	 * Gets the WebElement using the specified locator technique in the frames present on the passed page
	 * @param Config test config instance for the driver
	 * @param how Locator technique to use
	 * @param what element to be found with given technique (any arguments in this string will be replaced with run time properties)
	 * @return found WebElement
	 */
	public static WebElement getiFrameElement(Config testConfig, How how, String what)
	{
		getOutOfFrame(testConfig);		
		return findiFrameElement(testConfig, how, what);
	}

	private static WebElement findiFrameElement(Config testConfig, How how, String what)
	{
		List<WebElement> frames = getiFramesOnPage(testConfig.driver);
		if(frames.isEmpty()) return null;
		WebElement element = null;

		for(WebElement fr: frames)
		{
			if(element !=null)
			{
				return element;
			}

			try
			{
				testConfig.driver.switchTo().frame(fr);
			}
			catch (StaleElementReferenceException e)
			{
				testConfig.driver.switchTo().defaultContent();
				try
				{
				testConfig.driver.switchTo().frame(fr);
				}
				catch(StaleElementReferenceException ex)
				{
					testConfig.logWarning(ex.toString());
				}
			}

			element = getPageElement(testConfig,how, what);

			if (element == null) 
			{
				element = findiFrameElement(testConfig,how,what);
			}
		}

		return element;
	}

	/**
	 * Getting out of frame
	 */
	public static void getOutOfFrame(Config testConfig)
	{
		testConfig.driver.switchTo().defaultContent();
	}

	/**
	 * Returns the How locator used to find the specified Webelement
	 * @param element
	 * @return String representation of locator
	 */
	public static String getIdentifier(WebElement element)
	{
		String elementStr = element.toString();
		return "[" + elementStr.substring(elementStr.indexOf("->") + 3);
	}

	/**
	 * Enters the given 'value'in the specified WebElement
	 * @param testConfig Config instance used for logging
	 * @param element WebElement where data needs to be entered
	 * @param value value to the entered
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void enterData(Config testConfig, WebElement element, String value, String description)
	{
		if(!value.equalsIgnoreCase("{skip}"))
		{
			//encode the html characters so that they get printed correctly
			String message = StringUtils.replaceEach(value, new String[]{"&", "\"", "<", ">"}, new String[]{"&amp;", "&quot;", "&lt;", "&gt;"});
			
			testConfig.logComment("Enter the " + description + " as '" +  message + "'");
			element.clear();
			element.sendKeys(value);
		}
	}
	
	/**
	 * Enters the given 'value'in the specified WebElement after clicking on it
	 * @param testConfig Config instance used for logging
	 * @param element WebElement where data needs to be entered
	 * @param value value to the entered
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void enterDataAfterClick(Config testConfig, WebElement element, String value, String description)
	{
		if(!value.equalsIgnoreCase("{skip}"))
		{
			//encode the html characters so that they get printed correctly
			String message = StringUtils.replaceEach(value, new String[]{"&", "\"", "<", ">"}, new String[]{"&amp;", "&quot;", "&lt;", "&gt;"});
			
			testConfig.logComment("Enter the " + description + " as '" +  message + "'");
			element.click();
			element.clear();
			Browser.wait(testConfig, 1);
			element.sendKeys(value);
		}
	}
	
	/**
	 * Enters the given 'value'in the specified WebElement without clear
	 * @param testConfig Config instance used for logging
	 * @param element WebElement where data needs to be entered
	 * @param value value to the entered
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void enterDataWithoutClear(Config testConfig, WebElement element, String value, String description)
	{
		if(!value.equalsIgnoreCase("{skip}"))
		{
			//encode the html characters so that they get printed correctly
			String message = StringUtils.replaceEach(value, new String[]{"&", "\"", "<", ">"}, new String[]{"&amp;", "&quot;", "&lt;", "&gt;"});
			
			testConfig.logComment("Enter the " + description + " as '" +  message + "'");
			element.sendKeys(value);
		}
	}
	
	/**
	 * Enters the given 'value'in the specified File name WebElement
	 * @param testConfig Config instance used for logging
	 * @param element Filename WebElement where data needs to be entered
	 * @param value value to the entered
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void enterFileName(Config testConfig, WebElement element, String value, String description)
	{
		if(!value.equalsIgnoreCase("{skip}"))
		{
			testConfig.logComment("Enter the " + description + " as '" +  value + "'");
			element.sendKeys(value);
		}
		else{
			testConfig.logComment("Skipped file entry for "+description);
		}
	}
	
	/**
	 * Presses the given Key in the specified WebElement
	 * @param testConfig Config instance used for logging
	 * @param element Filename WebElement where data needs to be entered
	 * @param Key key to the entered
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void KeyPress(Config testConfig, WebElement element, Keys key, String description)
	{	
			testConfig.logComment("Press the key '" +  key.toString() + "' on " +  description + "");
			element.sendKeys(key);
	}

	/**
	 * Selects the given 'value' attribute for the specified WebElement
	 * @param testConfig Config instance used for logging
	 * @param element WebElement to select
	 * @param value value to the selected
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void selectValue(Config testConfig, WebElement element, String value, String description)
	{
		if(!value.equalsIgnoreCase("{skip}"))
		{ 
			testConfig.logComment("Select the " + description + " dropdown value '" +  value + "'");
			Select sel = new Select(element);
			sel.selectByValue(value);
		}
	}

	/**
	 * Selects the given visible text 'value' for the specified WebElement
	 * @param testConfig Config instance used for logging
	 * @param element WebElement to select
	 * @param value visible text value to the selected
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void selectVisibleText(Config testConfig, WebElement element, String value, String description)
	{
		if(!value.equalsIgnoreCase("{skip}"))
		{ 
			testConfig.logComment("Select the " + description + " dropdown text '" +  value + "'");
			Select sel = new Select(element);
			sel.selectByVisibleText(value);
		}
	}

	/**
	 * Verifies if given visible text 'value' for the specified WebElement is absent on the page
	 * @param testConfig Config instance used for logging
	 * @param element WebElement to select
	 * @param value visible text value to be verified for absence
	 * @param description description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void verifySelectVisibleTextNotPresent(Config testConfig, WebElement element, String value, String description)
	{
		try
		{
			testConfig.driver.manage().timeouts().implicitlyWait(1 , TimeUnit.SECONDS);
			Select sel = new Select(element);
			sel.selectByVisibleText(value);
			testConfig.logFail("Select value '" + value + "' is present in the drop down '" + description + "'");
		}
		catch(NoSuchElementException e)
		{
			testConfig.logPass("Select value '" + value + "' is absent in the drop down '" + description + "'");
			return;
		}
		finally
		{
			Long ObjectWaitTime = Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime"));
			testConfig.driver.manage().timeouts().implicitlyWait(ObjectWaitTime , TimeUnit.SECONDS);
		}
	}

	/**
	 * Get the first selected option in this select webelement
	 * @param testConfig Config instance used for logging
	 * @param element WebElement whose first selected value is to be read
	 * @return
	 */
	public static WebElement getFirstSelectedOption(Config testConfig, WebElement element, String description)
	{
		testConfig.logComment("Get the first selected value for " + description);
		try 
		{
		
		Select sel = new Select(element);
		return sel.getFirstSelectedOption();
		}catch(StaleElementReferenceException e)
		{
			Select sel = new Select(element);
			return sel.getFirstSelectedOption();
		}
		
		
	}

	/**
	 * Gets all the available string options in the Select Element
	 * @param testConfig Config instance used for logging
	 * @param element Select WebElement
	 * @return String list of options 
	 */
	public static List<String> getAllOptionsInSelect(Config testConfig, WebElement element)
	{
		testConfig.logComment("Retrieve all the Options present for this specified Select WebElement");
		Select sel = new Select(element);
		List<WebElement> elements=sel.getOptions();
		List<String> options=new ArrayList<String>(elements.size());

		for(WebElement e:elements){
			options.add(e.getText());
		}
		return options;	
	}
	
	/**
	 * Gets all the selected options in the Select Element
	 * @param testConfig Config instance used for logging
	 * @param element Select WebElement
	 * @return String list of options 
	 */
	public static List<String> getAllSelectedOptions(Config testConfig, WebElement element)
	{
		testConfig.logComment("Retrieve all the Options selected for this specified Select WebElement");
		Select sel = new Select(element);
		
		List<WebElement> elements=sel.getAllSelectedOptions();
		List<String> options=new ArrayList<String>(elements.size());

		for(WebElement e:elements){
			options.add(e.getText());
		}
		return options;	
	}

	/**
	 * @param testConfig Config instance used for logging
	 * @param element WebElement to be clicked
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void click(Config testConfig, WebElement element, String description)
	{
		testConfig.logComment("Click on '" + description + "'");
		try 
		{
				element.click();	
		} 
		catch(StaleElementReferenceException e)
		{
			element.click();
		}
		catch (UnreachableBrowserException e) 
		{
			testConfig.endExecutionOnfailure = true;
			testConfig.logException(e);
		}
	}

	/**
	 * @param testConfig Config instance used for logging
	 * @param element WebElement to be submitted
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void submit(Config testConfig, WebElement element, String description)
	{
		testConfig.logComment("Submit '" + description + "'");
		element.submit();
	}

	/**
	 * @param Config test config instance for the driver
	 * @param element WebElement to be double clicked
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void doubleClick(Config testConfig, WebElement element, String description)
	{
		testConfig.logComment("Double Click on '" + description + "'");
		Actions action = new Actions(testConfig.driver);
		action.doubleClick(element).perform();
	}

	/**
	 * @param testConfig Config instance used for logging
	 * @param element WebElement to be checked
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void check(Config testConfig, WebElement element, String description)
	{
		testConfig.logComment("Check '" + description + "'");
		if(!element.isSelected())
		{
			element.click();
		}
	}

	/**
	 * @param testConfig Config instance used for logging
	 * @param element WebElement to be unchecked
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void uncheck(Config testConfig, WebElement element, String description)
	{
		testConfig.logComment("Un-Check '" + description + "'");
		if(element.isSelected())
		{
			element.click();
		}
	}

	/**
	 * @param testConfig Config instance used for logging
	 * @param element WebElement to be cleared
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void clear(Config testConfig, WebElement element, String description)
	{
		testConfig.logComment("Clear data of '" + description + "'");
		element.clear();
	}

	/**
	 * Wait for element to be visible on the page
	 * @param Config test config instance for the driver instance on which element is to be searched
	 * @param element element to be searched
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void waitForVisibility(Config testConfig, WebElement element, String description)
	{
		testConfig.logComment("Wait for element '" + description + "' to be visible on the page.");
		Long ObjectWaitTime = Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime"));
		WebDriverWait wait= new WebDriverWait(testConfig.driver, ObjectWaitTime);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Wait for element to be stale on the page
	 * @param Config test config instance for the driver instance on which element is to be searched
	 * @param element element to be searched
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void waitForStaleness(Config testConfig, WebElement element, String description)
	{
		testConfig.logComment("Wait for element '" + description + "' to be stable on the page.");
		Long ObjectWaitTime = Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime"));
		WebDriverWait wait= new WebDriverWait(testConfig.driver, ObjectWaitTime);
		wait.until(ExpectedConditions.stalenessOf(element));
	}

	/**
	 * @param Config test config instance for the driver instance
	 * @param element WebElement on which mouse is to be moved
	 * @param description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void mouseMove(Config testConfig, WebElement element, String description)
	{
		testConfig.logComment("Move Mouse on '" + description + "'");

		Locatable hoverItem = (Locatable) element;
		Mouse mouse = ((HasInputDevices) testConfig.driver).getMouse();
		mouse.mouseMove(hoverItem.getCoordinates());
	}

	/**
	 * Fire Java Script event on the given WebElement
	 * @param Config test config instance for the driver instance
	 * @param element element on which event is to be fired
	 * @param eventName name of the event to be fired
	 */
	public static void fireEvent(Config testConfig, WebElement element, String eventName)
	{
		testConfig.logComment("Fire the event '" + eventName + "' on '" + getIdentifier(element) +"'");
		String javaScript = null;
		String onEventName = null;
		if (eventName.toLowerCase().startsWith("on"))
		{
			onEventName = eventName;
			eventName = eventName.substring(2);
		}
		else
		{
			onEventName = "on" + eventName;
		}

		javaScript = "var canBubble = false;" + System.getProperty("line.separator") +
				"var element = arguments[0];" + System.getProperty("line.separator") +
				"    if (document.createEventObject()) {" + System.getProperty("line.separator") +
				"        var evt = document.createEventObject();" + System.getProperty("line.separator") +
				"        arguments[0].fireEvent('" + onEventName + "', evt);" + System.getProperty("line.separator") +
				"    }" + System.getProperty("line.separator") +
				"    else {" + System.getProperty("line.separator") +
				"        var evt = document.createEvent(\"HTMLEvents\");" + System.getProperty("line.separator") +
				"        evt.initEvent('" + eventName + "', true, true);" + System.getProperty("line.separator") +
				"        arguments[0].dispatchEvent(evt);" + System.getProperty("line.separator") +
				"    }";

		((RemoteWebDriver)testConfig.driver).executeAsyncScript(javaScript, element);	
	}

	
	public static void fireMouseEvent(Config testConfig, WebElement element, String eventName)
    {
           testConfig.logComment("Fire the event '" + eventName + "' on '" + getIdentifier(element) +"'");
           
           String javaScript = "var node = arguments[0];" + System.getProperty("line.separator") +
            " var doc;" + System.getProperty("line.separator") +
              " doc = document; "+ System.getProperty("line.separator") +
              "				 var eventName = \"" + eventName + "\"; "+ System.getProperty("line.separator") +
            " if (node.fireEvent) { "+ System.getProperty("line.separator") +
"                 // IE-style "+ System.getProperty("line.separator") +
"                 var event = doc.createEventObject(); "+ System.getProperty("line.separator") +
"                 event.synthetic = true; // allow detection of synthetic events "+ System.getProperty("line.separator") +
"                 node.fireEvent(\"on\" + eventName, event); "+ System.getProperty("line.separator") +
"               } else if (node.dispatchEvent) { "+ System.getProperty("line.separator") +
"                 // Gecko-style approach is much more difficult. "+ System.getProperty("line.separator") +
"                 var eventClass = \"\"; "+ System.getProperty("line.separator") +
"                 // Different events have different event classes. "+ System.getProperty("line.separator") +
"                 // If this switch statement can't map an eventName to an eventClass, "+ System.getProperty("line.separator") +
"                 // the event firing is going to fail. "+ System.getProperty("line.separator") +
"                 switch (eventName) { "+ System.getProperty("line.separator") +
"                   case \"click\": // Dispatching of 'click' appears to not work correctly in Safari. Use 'mousedown' or 'mouseup' instead. "+ System.getProperty("line.separator") +
"                   case \"mousedown\": "+ System.getProperty("line.separator") +
"                   case \"mouseup\": "+ System.getProperty("line.separator") +
"                   case \"hover\": "+ System.getProperty("line.separator") +	
"                   case \"mousemove\": "+ System.getProperty("line.separator") +	
"                     eventClass = \"MouseEvents\"; "+ System.getProperty("line.separator") +
"                     break; "+ System.getProperty("line.separator") +
"                   case \"focus\": "+ System.getProperty("line.separator") +
"                   case \"change\": "+ System.getProperty("line.separator") +
"                   case \"blur\": "+ System.getProperty("line.separator") +
"                   case \"select\": "+ System.getProperty("line.separator") +
"                     eventClass = \"HTMLEvents\"; "+ System.getProperty("line.separator") +
"                     break; "+ System.getProperty("line.separator") +
"                   default: "+ System.getProperty("line.separator") +
"                     throw \"JSUtil.fireEvent: Couldn't find an event class for event '\" + eventName + \"'.\"; "+ System.getProperty("line.separator") +
"                     break; "+ System.getProperty("line.separator") +
"                 } "+ System.getProperty("line.separator") +
"                 var event = doc.createEvent(eventClass); "+ System.getProperty("line.separator") +
"                 var bubbles = eventName == \"change\" ? false : true;   "+ System.getProperty("line.separator") +
"                 event.initEvent(eventName, bubbles, true); // All events created as bubbling and cancelable. "+ System.getProperty("line.separator") +
"                 event.synthetic = true; // allow detection of synthetic events "+ System.getProperty("line.separator") +
"                 node.dispatchEvent(event); "+ System.getProperty("line.separator") +
"               }; ";
           
           System.out.println(javaScript);
           
           ((RemoteWebDriver)testConfig.driver).executeScript(javaScript, element);     
           
           /*String javaScript = null;
           String onEventName = null;
           if (eventName.toLowerCase().startsWith("on"))
           {
                  onEventName = eventName;
                  eventName = eventName.substring(2);
           }
           else
           {
                  onEventName = "on" + eventName;
           }

             
                        javaScript = "var canBubble = false;" + System.getProperty("line.separator") +
                                      "var element = arguments[0];" + System.getProperty("line.separator") +
                                      "    if (arguments[0].initMouseEvent) {" + System.getProperty("line.separator") +
                                      "       var evt = document.createEvent(\"MouseEvent\");" + System.getProperty("line.separator") +
                                      "        evt.initMouseEvent('" + eventName + "', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" + System.getProperty("line.separator") +
                                      "        arguments[0].dispatchEvent(evt);" + System.getProperty("line.separator") +
                                      "   } else {" + System.getProperty("line.separator") +
                                      
                                      "    if (document.createEventObject) {" + System.getProperty("line.separator") +
                                      "        var evt = document.createEventObject(window.arguments[0]);" + System.getProperty("line.separator") +
                                      "        arguments[0].fireEvent('" + onEventName + "', evt);" + System.getProperty("line.separator") +
                                      "    }" + System.getProperty("line.separator") +
                                      "    }";
                        testConfig.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//"        evt.initMouseEvent('" + eventName + "', true, true, window, 0, arguments[0].screenX,  arguments[0].screenY,  arguments[0].clientX,  arguments[0].clientY, arguments[0].ctrlKey,  arguments[0].altKey,  arguments[0].shiftKey,  arguments[0].metaKey,0, null);" + System.getProperty("line.separator") +
                        
           ((RemoteWebDriver)testConfig.driver).executeAsyncScript(javaScript, element);     */
    }

	/**
	 * Method used to scroll up and down horizontally in browser
	 * @param testConfig
	 * @param from
	 * @param to
	 */
	public static void pageScroll(Config testConfig,String from,String to)
	{
		JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
		js.executeScript("window.scrollBy("+from+","+to+")");
	}
	
	
	/**
	 * Verifies if element is present on the page
	 * @param testConfig Config instance used for logging
	 * @param element element to be verified
	 * @param description description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void verifyElementPresent(Config testConfig, WebElement element, String description)
	{
		if(element.isDisplayed())
		{
			testConfig.logPass("Verified the presence of element '" + description + "' on the page");
		}
		else
		{
			testConfig.logFail("Element '" + description + "' is not present on the page");
		}
	}

	/**
	 * Verifies if element is absent on the page
	 * @param testConfig Config instance used for logging
	 * @param element element to be verified
	 * @param description description logical name of specified WebElement, used for Logging purposes in report
	 */
	public static void verifyElementNotPresent(Config testConfig, WebElement element, String description)
	{
		try {
		if(!IsElementDisplayed(testConfig, element))
		{
			testConfig.logPass("Verified the absence of element '" + description + "' on the page");
		} 
		
		else
		{
			testConfig.logFail("Element '" + description + "' is present on the page");
		}
		} catch(StaleElementReferenceException e)
		{
			if(!IsElementDisplayed(testConfig, element))
			{
				testConfig.logPass("Verified the absence of element '" + description + "' on the page");
			} 
			
			else
			{
				testConfig.logFail("Element '" + description + "' is present on the page");
			}
		}
		
	}
	
	public static Boolean isElementDeleted(Config testConfig, How how, String what)
	{
		Boolean isDeleted = false;
		testConfig.logComment("Get the WebElement with " + how + ":" + what);
		what = Helper.replaceArgumentsWithRunTimeProperties(testConfig, what);
        WebElement element;
		try
		{
			switch(how)
			{
			case className: element = testConfig.driver.findElement(By.className(what));
			case css:  element = testConfig.driver.findElement(By.cssSelector(what));
			case id:  element = testConfig.driver.findElement(By.id(what));
			case linkText: element = testConfig.driver.findElement(By.linkText(what));
			case name:  element = testConfig.driver.findElement(By.name(what));
			case partialLinkText:  element = testConfig.driver.findElement(By.partialLinkText(what));
			case tagName:  element = testConfig.driver.findElement(By.tagName(what));
			case xPath:  element =  testConfig.driver.findElement(By.xpath(what));
			default:  element = null;
			isDeleted = false;
			}
			
		}
		catch(NoSuchElementException e)
		{
			isDeleted = true;
		}
		return isDeleted;
	}
	public static Boolean IsElementDisplayed(Config testConfig, WebElement element)
	{
		Boolean visible = true;
		if(element == null)
			return false;
		try
		{
			testConfig.driver.manage().timeouts().implicitlyWait(2 , TimeUnit.SECONDS);
			visible = element.isDisplayed();
		}
		catch(StaleElementReferenceException e)
		{
			testConfig.driver.manage().timeouts().implicitlyWait(2 , TimeUnit.SECONDS);
			visible = element.isDisplayed();
		}
		catch(NoSuchElementException e)
		{
			visible = false;
		}
		catch(ElementNotVisibleException e)
		{
			visible = false;
		}
		finally
		{
			Long ObjectWaitTime = Long.parseLong(testConfig.getRunTimeProperty("ObjectWaitTime"));
			testConfig.driver.manage().timeouts().implicitlyWait(ObjectWaitTime , TimeUnit.SECONDS);
		}
		return visible;
	}
	

public static WebElement getLastElementInCollection(Config testConfig,How how,String strDefinition){
	List<WebElement> webElements=getListOfElements(testConfig, how, strDefinition);
	return webElements.get(webElements.size()-1);
}



	public static void waitForElementDisplay(Config testConfig,WebElement element){

//		testConfig.logComment("waiting for element : "+element.toString());
		for (int i = 1; i <= 50; i++) {
			if (IsElementDisplayed(testConfig, element)) break;
			else {
					if(i==50){
						testConfig.logComment("element : "+element.toString().split("->")[1]+" not found on the page : "+testConfig.driver.getTitle());
					}
			}
		}
	
	}
	/**
	 * waits for element to disappear
	 */
	public static void waitForElementToDisappear(Config testConfig,
			WebElement elementName) {
		try {

			for (int i = 1; i <= 50; i++) {
				if (!(IsElementDisplayed(testConfig, elementName)))
					break;
				else {
					if (i == 50) {
						testConfig.logComment("element : "
								+ elementName.toString().split("->")[1]
								+ " is visible on the page : "
								+ testConfig.driver.getTitle());
					}
				}
			}

		} catch (NoSuchElementException e) {
			testConfig.logComment("element"
					+ " is not present on page");
		}
	}

	/**
	 * This function reveals file input element and sends file path
	 * 
	 * @param testConfig - for logging purposes
	 * @param csspath - path to uploader input
	 * @param filePath - path to file
	 * @param description - for logging purposes
	 */
	public static void revealInputAndUploadFile(Config testConfig, String csspath, String filePath, String description) {
		JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
		String strJS = "$(\"" + csspath + "\")[0]";
		testConfig.logComment(strJS);
		js.executeScript(strJS + ".style.display = \"block\";");
		js.executeScript(strJS + ".style.visibility = 'visible';");
		js.executeScript(strJS + ".style.opacity = 1;");
		js.executeScript(strJS + ".style.width = '1px';");
		js.executeScript(strJS + ".style.height = '1px';");
		
		WebElement fileInput = getPageElement(testConfig, How.css, csspath);
		Element.enterFileName(testConfig, fileInput, filePath, description);
	}
}
