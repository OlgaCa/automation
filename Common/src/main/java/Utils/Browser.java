package Utils;

import io.selendroid.SelendroidDriver;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.codehaus.jackson.JsonParseException;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.opera.core.systems.OperaDriver;

import edu.umass.cs.benchlab.har.HarEntries;
import edu.umass.cs.benchlab.har.HarEntry;
import edu.umass.cs.benchlab.har.HarLog;
import edu.umass.cs.benchlab.har.tools.HarFileReader;

public class Browser {

	/**
	 * Opens the new browser instance using the given config
	 * 
	 * @return new browser instance
	 * @throws IOException
	 */
	public static WebDriver openBrowser(Config testConfig) throws IOException {
		boolean remoteExecution = (testConfig.getRunTimeProperty(
				"RemoteExecution").toLowerCase().equals("true")) ? true : false;
		String browser = testConfig.getRunTimeProperty("Browser");

		testConfig.logComment("Launching '" + browser + "' browser");
		WebDriver driver = null;

		if (!remoteExecution) {
			// Launch the respective browser's Web drivers
			switch (browser.toLowerCase()) {
			case "firefox":
				FirefoxProfile ffProfile = new FirefoxProfile();

				// firefox preferences
				if (testConfig.getRunTimeProperty("DebugMode").contentEquals(
						"true")) {
					final String firebugPath = System.getProperty("user.dir")
							+ "\\..\\Prerequisite\\firebug-1.12.6.xpi";
					// netexport extension
					final File netExport = new File(
							System.getProperty("user.dir")
									+ "\\..\\Prerequisite\\netExport-0.9b4.xpi");

					try {
						ffProfile.addExtension(new File(firebugPath));
						if (testConfig.getRunTimeProperty("NetExport")
								.contentEquals("true")) {
							ffProfile.addExtension(netExport);
							// Setting netExport preferences
							ffProfile
									.setPreference(
											"extensions.firebug.netexport.alwaysEnableAutoExport",
											true);
							ffProfile
									.setPreference(
											"extensions.firebug.netexport.autoExportToFile",
											true);
							ffProfile.setPreference(
									"extensions.firebug.netexport.Automation",
									true);
							ffProfile.setPreference(
									"extensions.firebug.netexport.showPreview",
									false);
							ffProfile
									.setPreference(
											"extensions.firebug.netexport.defaultLogDir",
											System.getProperty("user.dir")
													+ "\\CaptureNetworkTraffic");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// firebug preferences
					// Setting Firebug preferences
					ffProfile.setPreference(
							"extensions.firebug.currentVersion", "2.0");
					ffProfile.setPreference(
							"extensions.firebug.addonBarOpened", true);
					ffProfile.setPreference(
							"extensions.firebug.console.enableSites", true);
					ffProfile.setPreference(
							"extensions.firebug.script.enableSites", true);
					ffProfile.setPreference(
							"extensions.firebug.net.enableSites", true);
					ffProfile.setPreference(
							"extensions.firebug.previousPlacement", 1);
					ffProfile.setPreference(
							"extensions.firebug.allPagesActivation", "on");
					ffProfile.setPreference("extensions.firebug.onByDefault",
							true);
					ffProfile.setPreference(
							"extensions.firebug.defaultPanelName", "net");
				}

				ffProfile.setPreference("dom.max_chrome_script_run_time", 0);
				ffProfile.setPreference("dom.max_script_run_time", 0);

				// We don't have any untrusted website in PP, hence this is not
				// required
				// ffProfile.setAssumeUntrustedCertificateIssuer(false);
				// ffProfile.setAcceptUntrustedCertificates (true);

				// automatically download excel files
				ffProfile.setPreference("browser.helperApps.alwaysAsk.force",
						false);
				ffProfile
						.setPreference(
								"browser.helperApps.neverAsk.saveToDisk",
								"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/x-msdos-program, application/x-unknown-application-octet-stream, application/vnd.ms-powerpoint, application/excel, application/vnd.ms-publisher, application/x-unknown-message-rfc822, application/vnd.ms-excel, application/msword, application/x-mspublisher, application/x-tar, application/zip, application/x-gzip,application/x-stuffit,application/vnd.ms-works, application/powerpoint, application/rtf, application/postscript, application/x-gtar, video/quicktime, video/x-msvideo, video/mpeg, audio/x-wav, audio/x-midi, audio/x-aiff, text/html");
				ffProfile.setPreference(
						"browser.download.manager.showWhenStarting", false);
				// ffProfile.setPreference("webdriver.load.strategy", "fast");
				DesiredCapabilities ffCapability = DesiredCapabilities
						.firefox();
				/*
				 * ffCapability.setCapability("dom.max_chrome_script_run_time",
				 * 0); ffCapability.setCapability("dom.max_script_run_time", 0);
				 */
				ffProfile.setEnableNativeEvents(false);

				ffCapability.setCapability("firefox_profile", ffProfile);
				// unable to bind to locking port 7054 within 45 seconds
				FirefoxBinary fb = new FirefoxBinary();
				fb.setTimeout(java.util.concurrent.TimeUnit.SECONDS
						.toMillis(90));
				driver = new FirefoxDriver(fb, ffProfile);

				break;
			// for headless browser execution , used for running api test cases
			case "htmlunit":
				driver = new HtmlUnitDriver(true);
				break;
			case "chrome":
				System.setProperty("webdriver.chrome.driver",
						"..\\lib\\chromedriver.exe");
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--start-maximized");
				driver = new ChromeDriver(chromeOptions);
				break;
			case "ie":
				System.setProperty("webdriver.ie.driver",
						"..\\lib\\IEDriverServer.exe");
				DesiredCapabilities ieCapability = DesiredCapabilities
						.internetExplorer();
				ieCapability.setCapability("ignoreProtectedModeSettings", true);
				ieCapability
						.setCapability(
								InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
								true);
				driver = new InternetExplorerDriver(ieCapability);
				break;
			case "opera":
				DesiredCapabilities operaCapability = DesiredCapabilities
						.opera();
				operaCapability.setCapability("opera.port", -1);
				driver = new OperaDriver(operaCapability);
				break;
			default:
				testConfig.logFail(browser + "- is not supported");
				Assert.assertTrue(false);
			}
		} else {
			// Launch the remote web driver
			DesiredCapabilities capability = null;

			testConfig.logComment("Start remote execution");

			switch (browser.toLowerCase()) {
			case "firefox":
				capability = DesiredCapabilities.firefox();
				capability.setBrowserName("firefox");
				capability.setPlatform(org.openqa.selenium.Platform.ANY);
				break;

			case "chrome":
				capability = DesiredCapabilities.chrome();
				System.setProperty("webdriver.chrome.driver",
						"lib\\chromedriver.exe");
				capability.setBrowserName("chrome");
				capability.setPlatform(org.openqa.selenium.Platform.ANY);
				break;

			case "ie":
				capability = DesiredCapabilities.internetExplorer();
				System.setProperty("webdriver.ie.driver",
						"lib\\IEDriverServer.exe");
				capability.setBrowserName("iexplore");
				capability.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				capability.setCapability("ignoreProtectedModeSettings", true);
				break;

			case "opera":
				capability = DesiredCapabilities.opera();
				capability.setCapability("opera.port", -1);
				capability.setBrowserName("opera");
				capability.setPlatform(org.openqa.selenium.Platform.ANY);
				break;

			case "android_web":
				capability = DesiredCapabilities.android();
				break;

			case "android_native":
				break;

			default:
				testConfig.logFail(browser + "- is not supported");
				Assert.assertTrue(false);
			}

			try {
				if (browser.equalsIgnoreCase("android_web")) {

					driver = new RemoteWebDriver(capability);

				} else if (browser.equalsIgnoreCase("android_native")) {

					try {
						driver = new SelendroidDriver(testConfig.selCap);
						driver.switchTo().window("WEBVIEW");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {

					driver = new RemoteWebDriver(new URL(
							"http://localhost:4444/wd/hub"), capability);
				}

			} catch (MalformedURLException e) {
				testConfig.logException(e);
			}

		}
		if (driver != null) {

			Long ObjectWaitTime = Long.parseLong(testConfig
					.getRunTimeProperty("ObjectWaitTime"));
			driver.manage().timeouts()
					.implicitlyWait(ObjectWaitTime, TimeUnit.SECONDS);
			if (!(browser.equalsIgnoreCase("android_web"))
					&& !(browser.equalsIgnoreCase("android_native"))) {
				driver.manage().window().maximize();
			}

		}

		return driver;
	}

	/**
	 * Refresh browser once
	 */
	public static void browserRefresh(Config testConfig) {
		testConfig.driver.navigate().refresh();
		testConfig.logComment("Refreshing the browser...");
	}

	/**
	 * Navigate to driver the URL specified
	 * 
	 * @param Config
	 *            test config instance
	 * @param url
	 *            URL to be navigated
	 */
	public static void navigateToURL(Config testConfig, String url) {
		testConfig.logComment("Navigate to web page- '" + url + "'");
		testConfig.driver.get(url);
		if (url.contains(".payu.in"))
			Browser.setCookieValue(testConfig, "test_name",
					testConfig.testName, ".payu.in");
	}

	/**
	 * Quits this driver, closing every associated window.
	 * 
	 * @param Config
	 *            test config instance for the browser to be quit
	 */
	public static void quitBrowser(Config testConfig) {
		try {
			if (testConfig.driver != null) {
				if (testConfig.getRunTimeProperty("NetExport").contentEquals(
						"true")) {
					String filePath = new String(System.getProperty("user.dir")
							+ "\\CaptureNetworkTraffic");

					File f = new File(filePath);
					File[] fList = f.listFiles();

					for (int i = 0; i < fList.length; i++) {
						if (fList[i] != null)
							fList[i].delete();
					}
				}
				testConfig.logComment("Quit the browser");
				testConfig.driver.quit();
			}
		} catch (UnreachableBrowserException e) {
			testConfig.logWarning(e.toString());
		}
	}

	/**
	 * Close the current window, quitting the browser if it's the last window
	 * currently open.
	 * 
	 * @param Config
	 *            test config instance for the browser to be closed
	 */
	public static void closeBrowser(Config testConfig) {
		try {
			if (testConfig.driver != null) {

				testConfig.logComment("Close the current browser window");
				testConfig.driver.close();
			}
		} catch (UnreachableBrowserException e) {
			testConfig.logWarning(e.toString());
		}
	}

	/**
	 * Switch the driver to the specified window
	 * 
	 * @param Config
	 *            test config instance
	 * @param windowHandle
	 *            Name of the window to be switched to
	 */
	public static void switchToGivenWindow(Config testConfig,
			String windowHandle) {
		if (testConfig.driver != null) {
			testConfig.logComment("Switching to the given window");
			testConfig.driver.switchTo().window(windowHandle);
		}
	}

	/**
	 * Switch the driver to the new window
	 * 
	 * @param Config
	 *            test config instance
	 * @return window handle of the old window, so that it can be switched back
	 *         later
	 */
	public static String switchToNewWindow(Config testConfig)
	{
		if(testConfig.driver != null)
		{
			testConfig.logComment("Switching to the new window");
			String oldWindow = testConfig.driver.getWindowHandle();

			if(testConfig.driver.getWindowHandles().size() < 2)
			{
				testConfig.logFail("No new window appeared, windows count available :-" + testConfig.driver.getWindowHandles().size());
			}

			for(String winHandle : testConfig.driver.getWindowHandles()){
				if(winHandle!=oldWindow)
				{
					testConfig.driver.switchTo().window(winHandle);
				}
			}

			return oldWindow;
		}
		return null;
	}

	/**
	 * Verify Page URL
	 * 
	 * @param Config
	 *            test config instance
	 * @param expectedURL
	 * @return true if actual URL contains the expected URL
	 */
	public static boolean verifyURL(Config testConfig, String expectedURL) {
		try {
			int retries = 30;
			String actualURL = testConfig.driver.getCurrentUrl().toLowerCase();
			expectedURL = expectedURL.toLowerCase();

			while (retries > 0) {
				if (actualURL.contains(expectedURL)) {
					testConfig.logPass("Browser URL", actualURL);

					// Verify that page stays on same page (no internal
					// redirect)
					Browser.wait(testConfig, 5);
					actualURL = testConfig.driver.getCurrentUrl().toLowerCase();
					if (!actualURL.contains(expectedURL)) {
						testConfig.logFail("Browser URL", expectedURL,
								actualURL);
						return false;
					}

					return true;
				}
				Browser.wait(testConfig, 1);
				actualURL = testConfig.driver.getCurrentUrl().toLowerCase();
				retries--;
			}
			testConfig.logFail("Browser URL", expectedURL, actualURL);
			return false;
		} catch (UnreachableBrowserException e) {
			testConfig.endExecutionOnfailure = true;
			testConfig.logException(e);
			return false;
		}
	}

	/**
	 * Verify Intermediate Page URL (which gets auto-redirected) like processing
	 * page
	 * 
	 * @param Config
	 *            test config instance
	 * @param expectedURL
	 * @return true if actual URL contains the expected URL
	 */
	public static void verifyIntermediateURL(Config testConfig,
			String expectedURL) {
		try {
			int retries = 3;
			String actualURL = testConfig.driver.getCurrentUrl().toLowerCase();
			expectedURL = expectedURL.toLowerCase();

			while (retries > 0) {
				if (actualURL.contains(expectedURL)) {
					testConfig.logPass("Browser URL", actualURL);
					return;
				}
				// Browser.wait(testConfig, 1);
				actualURL = testConfig.driver.getCurrentUrl().toLowerCase();
				retries--;
			}
			testConfig.logFail("Browser URL", expectedURL, actualURL);
		} catch (UnreachableBrowserException e) {
			testConfig.endExecutionOnfailure = true;
			testConfig.logException(e);
		}
	}

	/**
	 * Delete the cookies of the given browser instance
	 * 
	 * @param Config
	 *            test config instance
	 */
	public static void deleteCookies(Config testConfig) {
		if (testConfig.driver != null) {
			testConfig.logComment("Delete all cookies!!");
			testConfig.driver.manage().deleteAllCookies();
		}
	}

	public static String getCookieValue(Config testConfig, String cookieName) {
		String value = null;
		if (testConfig.driver != null) {
			Cookie cookie = testConfig.driver.manage().getCookieNamed(
					cookieName);
			if (cookie == null) {
				testConfig.logFail("Cookie " + cookieName + " Not found");
				return null;
			}
			value = cookie.getValue();
			testConfig.logComment("Read the cookie named '" + cookieName
					+ "' value as '" + value + "'");
		}
		return value;
	}

	public static void setCookieValue(Config testConfig, String cookieName,
			String cookieValue, String cookieDomain) {
		if (testConfig.driver != null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, 1);
			Date tomorrow = cal.getTime();

			Cookie cookie = new Cookie(cookieName, cookieValue, cookieDomain,
					"/", tomorrow);
			testConfig.driver.manage().addCookie(cookie);
			testConfig.logComment("Added the cookie - Name: '" + cookieName
					+ "' Value: '" + cookieValue + "' Domain: '" + cookieDomain
					+ "' Expiry: '" + tomorrow.toString() + "'");
		}
	}

	/**
	 * Waits for the given WebElement to appear on the specified browser
	 * instance
	 * 
	 * @param Config
	 *            test config instance
	 * @param element
	 *            element to be searched
	 */
	public static void waitForPageLoad(Config testConfig, WebElement element) {
		testConfig.logComment("Wait for page load by waiting for element "
				+ Element.getIdentifier(element));

		Long ObjectWaitTime = Long.parseLong(testConfig
				.getRunTimeProperty("ObjectWaitTime"));
		WebDriverWait wait = new WebDriverWait(testConfig.driver,
				ObjectWaitTime + 10);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (StaleElementReferenceException e) {
			wait.until(ExpectedConditions.visibilityOf(element));

		}
	}

	/**
	 * Executes JavaScript in the context of the currently selected frame or
	 * window in the Config driver instance.
	 * 
	 * @param javaScriptToExecute
	 *            Java Script To Execute
	 * @return If the script has a return value (i.e. if the script contains a
	 *         return statement), then the following steps will be taken: For an
	 *         HTML element, this method returns a WebElement For a decimal, a
	 *         Double is returned For a non-decimal number, a Long is returned
	 *         For a boolean, a Boolean is returned For all other cases, a
	 *         String is returned. For an array, return a List<Object> with each
	 *         object following the rules above. We support nested lists. Unless
	 *         the value is null or there is no return value, in which null is
	 *         returned
	 */
	public static Object executeJavaScript(Config testConfig,
			String javaScriptToExecute) {
		testConfig.logComment("Execute javascript:-" + javaScriptToExecute);
		JavascriptExecutor javaScript = (JavascriptExecutor) testConfig.driver;
		return javaScript.executeScript(javaScriptToExecute);
	}

	/**
	 * Pause the execution for given seconds
	 * 
	 * @param seconds
	 */
	public static void wait(Config testConfig, int seconds) {
		int milliseconds = seconds * 1000;
		try {
			Thread.sleep(milliseconds);
			testConfig.logComment("Wait for '" + seconds + "' seconds");

		} catch (InterruptedException e) {

		}
	}

	/**
	 * Takes the screenshot of the current active browser window
	 * 
	 * @param Config
	 *            test config instance
	 * @param destination
	 *            file to which screenshot is to be saved
	 */
	public static void takeScreenShoot(Config testConfig, File destination) {

		try {

			if (testConfig.driver != null) {
				File screenshot;
				Boolean remoteExecution = (testConfig.getRunTimeProperty(
						"RemoteExecution").toLowerCase().equals("true")) ? true
						: false;

				if (remoteExecution) {
					WebDriver augumentedDriver = new Augmenter()
							.augment(testConfig.driver);
					screenshot = ((TakesScreenshot) augumentedDriver)
							.getScreenshotAs(OutputType.FILE);
				} else {
					screenshot = ((TakesScreenshot) testConfig.driver)
							.getScreenshotAs(OutputType.FILE);
				}

				try {
					FileUtils.copyFile(screenshot, destination);

					float compressionQuality = (float) 0.5;
					try {
						compressionQuality = Float
								.parseFloat(testConfig
										.getRunTimeProperty("ScreenshotCompressionQuality"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					compressJpegFile(destination, destination,
							compressionQuality);
				} catch (IOException e) {
					e.printStackTrace();
				}

				testConfig.logComment("<B>Page URL</B>:- "
						+ testConfig.driver.getCurrentUrl());

				if (destination.getAbsolutePath().contains("test-output"))
					testConfig.logComment("<B>Screenshot</B>:- <a href=html/"
							+ destination.getName() + " target='_blank' >"
							+ destination.getName() + "</a>");
				else
					testConfig.logComment("<B>Screenshot</B>:- <a href="
							+ destination.getName() + " target='_blank' >"
							+ destination.getName() + "</a>");
			}
		} catch (Exception e) {
			testConfig
					.logWarning("Unable to take screenshot:- " + e.toString());
		}
	}

	/**
	 * Uses the specified method name to generate a destination file name where
	 * screenshot can be saved
	 * 
	 * @param Config
	 *            test config instance
	 * @return file using which we can call takescreenshot
	 */
	public static File getScreenShotFile(Config testConfig) {
		File dest = getScreenShotDirectory(testConfig);
		return new File(dest.getPath() + "\\"
				+ getFileName(testConfig.testMethod));
	}

	private static String getFileName(Method testMethod) {
		String nameScreenshot = testMethod.getDeclaringClass().getName() + "."
				+ testMethod.getName();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date) + "_" + nameScreenshot + ".png";
	}

	private static File getScreenShotDirectory(Config testConfig) {
		File dest = new File(testConfig.getRunTimeProperty("ResultsDir")
				+ "//html//");

		/*
		 * Commenting out since, the current results folder will be passed by
		 * ant script and we do not need to calculate File resultsFolder = new
		 * File(System.getProperty("user.dir")+"//Results//" +
		 * Config.productName); File[] directories = resultsFolder.listFiles(new
		 * FilenameFilter() {
		 * 
		 * @Override public boolean accept(File dir, String name) { return
		 * dir.isDirectory(); } });
		 * 
		 * long lastMod = Long.MIN_VALUE;
		 * 
		 * File dest = null;
		 * 
		 * if(directories!=null) { for(File directory:directories) { if
		 * (directory.lastModified() > lastMod) { dest = directory; lastMod =
		 * directory.lastModified(); } } }
		 */

		return dest;
	}

	// Reads the jpeg image in infile, compresses the image,
	// and writes it back out to outfile.
	// compressionQuality ranges between 0 and 1,
	// 0-lowest, 1-highest.
	private static void compressJpegFile(File infile, File outfile, float compressionQuality) {
		try {
			// Retrieve jpg image to be compressed
			RenderedImage rendImage = ImageIO.read(infile);

			// Find a jpeg writer
			ImageWriter writer = null;
			Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("gif");
			if (iter.hasNext()) {
				writer = (ImageWriter)iter.next();
			}

			// Prepare output file
			ImageOutputStream ios = ImageIO.createImageOutputStream(outfile);
			writer.setOutput(ios);

			// Set the compression quality
			ImageWriteParam iwparam = new MyImageWriteParam();
			iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT) ;
			iwparam.setCompressionQuality(compressionQuality);

			// Write the image
			writer.write(new IIOImage(rendImage, null, null));

			// Cleanup
			ios.flush();
			writer.dispose();
			ios.close();
		} catch (IOException e) {
		}
		catch(OutOfMemoryError outOfMemoryError){	
		}
	}

	// This class overrides the setCompressionQuality() method to workaround
	// a problem in compressing JPEG images using the javax.imageio package.
	public static class MyImageWriteParam extends JPEGImageWriteParam {
		public MyImageWriteParam() {
			super(Locale.getDefault());
		}

		// This method accepts quality levels between 0 (lowest) and 1 (highest)
		// and simply converts
		// it to a range between 0 and 256; this is not a correct conversion
		// algorithm.
		// However, a proper alternative is a lot more complicated.
		// This should do until the bug is fixed.
		public void setCompressionQuality(float quality) {
			if (quality < 0.0F || quality > 1.0F) {
				throw new IllegalArgumentException("Quality out-of-bounds!");
			}
			this.compressionQuality = 256 - (quality * 256);
		}
	}

	public static File lastFileModified(Config testConfig, String dir) {
		File fl = new File(dir);
		File[] files = fl.listFiles();
		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

		return files[0];
	}

	/**
	 * @param testConfig
	 *            - element of Config
	 * @param path
	 *            - path of the folder where file is present
	 * @param name
	 *            - some text that is present in file name
	 * @return - file name of the last modified file with matching text
	 */
	public static File lastFileModifiedWithDesiredName(Config testConfig, String path, String name) {
		File fl = new File(path);
		File choise = null;
		List<File> arrayOfSortedFiles = new ArrayList<File>();
		long lastMod = Long.MIN_VALUE;
		for (int retry=0;retry<=5;retry++)
		{
			//making a list of files in download folder
			System.out.println("Wait for file to download");
			Browser.wait(testConfig, 5);
			File[] files = fl.listFiles(new FileFilter() {public boolean accept(File file) {
				return file.isFile();
			}
			});
			//Matching names of desired file
			for (File file : files) {
				if (file.getName().contains(name))
					arrayOfSortedFiles.add(file);
			}
			if (arrayOfSortedFiles.size()>0)
				break;
			else
				continue;
		}
		//Finding matching file which has been last modified
		for (File matchingfile : arrayOfSortedFiles) {
			if (matchingfile.lastModified() > lastMod) {
				choise = matchingfile;
				lastMod = matchingfile.lastModified();			}	
			}
		if (choise==null)
			Log.Fail("No File found with name"+name, testConfig);
		else
		System.out.println("The file chosen is as: "+choise.getName());
		return choise;
	}

	/**
	 * @param testConfig
	 *            - element of Config
	 * @param path
	 *            - path of the folder where file is present
	 * @param name
	 *            - some text that is present in file name
	 * @return - file name with matching text
	 */
	public static File DesiredFileDownload(Config testConfig, String path,String name) {
		File fl = new File(path);
		File choise = null;
		for (int retry=0;retry<=15;retry++)
		{
			//storing all file names in files array
			File[] files = fl.listFiles(new FileFilter() {public boolean accept(File file) {return file.isFile();}});

			//Finding the desired file
			for (File file : files) {
				//System.out.println(file);
				if (file.getName().contains(name)&&(!file.getName().contains("part"))) 
				{
					choise = file;
					break;
				}
			}
			//checking if file with required name has been found out
			if (choise==null && retry ==15)
			{
				Log.Fail("File with name "+name+" does not exist in "+path, testConfig);
				break;
			}
			else if (choise==null)
				Browser.wait(testConfig, 1);
			else
				break;
		}
		return choise;
	}

	/**
	 * To return back to previous page
	 * 
	 * @param testConfig
	 * @param url
	 */
	public static void goBack(Config testConfig) {
		testConfig.logComment("Clicking on back button on browser");
		testConfig.driver.navigate().back();
	}

	/**
	 * Returns the firebug request response for a given request url
	 * 
	 * @param testConfig
	 * @param request
	 * @return String[] 0 - Request, 1- Response
	 */
	public static String[] getFireBugResponse(String request)
	{
		String[] reqResponse = new String[2];
		try {
			String filePath = new String(System.getProperty("user.dir") + "\\CaptureNetworkTraffic");

			File f = new File(filePath);
			File[] fList = f.listFiles();
			if (fList.length == 0) {
			    return null;
			}

			Arrays.sort(fList, LastModifiedFileComparator.LASTMODIFIED_REVERSE);			
			HarFileReader r = new HarFileReader();

			System.out.println("Reading " + fList[0]);
			HarLog log = r.readHarFile(fList[0]);

			// Access all elements as objects HarBrowser browser =
			log.getBrowser();
			HarEntries entries = log.getEntries();

			// Used for loops List<HarPage> pages =
			log.getPages().getPages();
			List<HarEntry> hentry = entries.getEntries();
			
			//Output "response" code of entries.
		      for (HarEntry entry : hentry)
		      {
		          if(entry.getRequest().getUrl().contains(request))
		          {
		          reqResponse[0]= entry.getRequest().getUrl();
		          reqResponse[1]=entry.getResponse().getContent().toString();
		          }
		      }

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reqResponse;

	}

	/**
	 * Returns the latest firebug request response. Useful for debugging failure
	 * points
	 * 
	 * @param testConfig
	 * @param request
	 * @return String[] 0 - Request, 1- Response
	 */
	public static String[] getLatestFireBugResponse(Config testConfig)
	{
		String[] reqResponse = new String[2];
		try {
			String filePath = new String(System.getProperty("user.dir") + "\\CaptureNetworkTraffic");

			File f = new File(filePath);
			File[] fList = f.listFiles();
			if (fList.length == 0) {
			    return null;
			}

			Arrays.sort(fList, LastModifiedFileComparator.LASTMODIFIED_REVERSE);			
			HarFileReader r = new HarFileReader();

			System.out.println("Reading " + fList[0]);
			HarLog log = r.readHarFile(fList[0]);

			// Access all elements as objects HarBrowser browser =
			log.getBrowser();
			HarEntries entries = log.getEntries();

			// Used for loops List<HarPage> pages =
			log.getPages().getPages();
			List<HarEntry> hentry = entries.getEntries();
			
			ListIterator<HarEntry> iEntry = hentry.listIterator();
			//Output "response" code of entries.
		      while(iEntry.hasNext())
		      {
		    	  
		    	  HarEntry last = iEntry.next();
		    	  if(!last.getRequest().getUrl().contains("https://test.payumoney.com/auth/oauth/authorize?client_id=10181&redirect_uri=https:%2F%2Ftest.payumoney.com%2Fblank.html&response_type=token&scope=trust"))
		    	  {
		          reqResponse[0]= last.getRequest().getUrl();
		          if(last.getResponse()!=null)
		          {
		          reqResponse[1]=last.getResponse().getContent().toString();
		    	  }
		         
		    	  }
		      }

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reqResponse;

	}

	public static void uploadFileWithJS(Config testConfig, String strJSLocater,
			String strFilePath, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) testConfig.driver;
		js.executeScript(strJSLocater + ".style.display = \"block\";");
		js.executeScript(strJSLocater + ".style.visibility = 'visible';");
		js.executeScript(strJSLocater + ".style.opacity = 1;");
		js.executeScript(strJSLocater + ".style.width = '1px';");
		js.executeScript(strJSLocater + ".style.height = '1px';");
		element.sendKeys(strFilePath);
	}

	/**
	 * Waits for popup till 5 times of the time you specify in seconds
	 * 
	 * @param testConfig
	 * @param seconds
	 */
	public static void waitForPopUp(Config testConfig, int seconds) {

		// Time to poll for every 5 seconds whether popup is present or not
		int threshold = 5;

		for (int i = 0; i < seconds; i++) {

			// Time to poll for every 5 seconds whether popup is present or not
			if (Popup.isAlertPresent(testConfig)) {
				Popup.ok(testConfig);
				break;
			}

			Browser.wait(testConfig, threshold);
		}
	}

}
