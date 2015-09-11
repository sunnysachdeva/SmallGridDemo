package com.gspann.grid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import com.google.common.base.Function;

public  class Cls_Generic_methods  {


	static Logger logger = Logger.getLogger(Cls_Generic_methods.class.getName());

	public static WebDriver fn_LaunchBrowser(String BrowserType)
			throws Exception {

		WebDriver Driver_Object = null;
		if(Driver_Object==null)
		{
			if (BrowserType.equalsIgnoreCase("FF")
					|| BrowserType.equalsIgnoreCase("Firefox")) {
				Driver_Object = new FirefoxDriver();
			} else if (BrowserType.equalsIgnoreCase("Safari")) {
				Driver_Object = new SafariDriver();
			} else if (BrowserType.equalsIgnoreCase("chrome")|| BrowserType.equalsIgnoreCase("Chrome")) {
				System.setProperty("webdriver.chrome.driver",".\\Resources\\Drivers\\chromedriver.exe");
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--test-type");
				LoggingPreferences loggingprefs = new LoggingPreferences();
				loggingprefs.enable(LogType.BROWSER, Level.ALL);
				capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
				capabilities.setCapability("chrome.binary", ".\\Resources\\Drivers\\chromedriver.exe");
				capabilities.setCapability("chrome.switches", Arrays.asList("--incognito"));
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				Driver_Object = new ChromeDriver(capabilities);
			} else if (BrowserType.equalsIgnoreCase("IE")|| BrowserType.equalsIgnoreCase("InternetExplorer")) {
				System.setProperty("webdriver.ie.driver",".\\Resources\\Drivers\\IEDriverServer.exe");
				InternetExplorerDriverService.Builder service = new InternetExplorerDriverService.Builder();
				service = service.withLogLevel(InternetExplorerDriverLogLevel.TRACE);
				service = service.withLogFile(new File("d:\\logs\\selenium.log"));
				//	DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
				//	ieCapabilities.setCapability("requireWindowFocus", true);
				//.setCapability("ie.ensureCleanSession", true);
				//ieCapabilities.setCapability("nativeEvents", false);
				//			ieCapabilities.setCapability(CapabilityType.BROWSER_NAME,"Internet Explorer");
				//			ieCapabilities.setCapability(CapabilityType.VERSION, "8");
				//		ieCapabilities.setCapability("ie.forceCreateProcessApi", true);
				//			ieCapabilities.setCapability("ie.browserCommandLineSwitches","-private");

				// ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
				// true);
				Driver_Object = new InternetExplorerDriver(service.build());
				// Driver_Object = new InternetExplorerDriver();
			} else if (BrowserType.equalsIgnoreCase("remote")) {
				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setBrowserName("chrome");
				Driver_Object = new RemoteWebDriver(new URL(
						"http://localhost:4446/wd/hub"), cap);
			}
			// DO NOT DELETE IT
			/*
			 * else if(BrowserType.equalsIgnoreCase("bmp")){ ProjectSnappyProxy
			 * objProjectSnappyProxy = ProjectSnappyProxy.getInstance();
			 * System.setProperty("webdriver.chrome.driver",
			 * ".\\Resources\\chromedriver.exe"); Driver_Object=new
			 * ChromeDriver(objProjectSnappyProxy.getProxyDesiredCapabilties(4567));
			 * 
			 * }
			 */
			else {

				logger.info("Provided Browser Type is invalid, please check");
			}

		}

		Driver_Object.manage().window().maximize();
		Driver_Object.manage().timeouts().implicitlyWait(Integer.parseInt(getConfigValues("ImplicitWait")),TimeUnit.SECONDS);

		return Driver_Object;
	}

	public static void getURL(WebDriver driver, String sURL)
	{
		driver.get(sURL);
	}


	/**
	 * Returns current Date Time
	 * @return
	 */
	public static String getDateTime() {
		String sDateTime="";
		try {
			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
			Date now = new Date();
			String strDate = sdfDate.format(now);
			String strTime = sdfTime.format(now);
			strTime = strTime.replace(":", "-");
			sDateTime = "D" + strDate + "_T" + strTime;
		} catch (Exception e) {
			System.err.println(e);
		}
		return sDateTime;
	}

	public static String extractJSLogs(WebDriver driver,String sTestCaseName) throws IOException {
		String sPath = System.getProperty("user.dir")+ "\\Output\\Reports\\JSLogs\\"+sTestCaseName+"_"+getDateTime()+"_JSErrorExtractLogs.txt";
		File file = new File(sPath);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);

		for (LogEntry entry : logEntries) {
			bw.write(new Date(entry.getTimestamp()) + ">>> " + entry.getLevel() + ">>> " + entry.getMessage());
			bw.write("\r\n");
			bw.write("\r\n");
			logger.info(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
		}
		bw.close();
		return sPath;
	}

	/**
	 * Get logger file configuration
	 */
	public static void getLoggerConfiguration() {
		PropertyConfigurator.configure(System.getProperty("user.dir")+ "\\Config\\log4j.properties");
	}

	/**
	 * Returns the File Path of the Screen Shot Image
	 * 
	 * @param driver
	 * @param sTestScenario
	 * @param sStep
	 * @return
	 * @throws Exception
	 */
	public static String TakeScreenshotTest(WebDriver driver,String sTestScenario, String sStep) throws Exception {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String sFilename = null;
		String sScreenShotPath = "";
		try {
			sFilename = sTestScenario + "_" + "Screenshot-" + sStep + ".png";
			sScreenShotPath = System.getProperty("user.dir")+ "\\Output\\ScreenShots\\" + sFilename;
			FileUtils.copyFile(scrFile, new File(sScreenShotPath));

		} catch (Exception e) {
			logger.error("Error occurred while taking screenshot"
					+ e.getMessage());
		}
		return sScreenShotPath;
	}

	/**
	 * Highlights the Border of an Element
	 * 
	 * @param driver
	 * @param element
	 */
	public static void highlightElementBorder(WebDriver driver,
			WebElement element) {

		try {
			if (Cls_Generic_methods.getConfigValues("Highlight").equals("1")) {
				for (int i = 0; i < 1; i++) {
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript(
							"arguments[0].setAttribute('style', arguments[1]);",
							element, "color: yellow; border: 2px solid green;");
					// + "border: 4px solid red;");
					Thread.sleep(1200);
					js.executeScript(
							"arguments[0].setAttribute('style', arguments[1]);",
							element, "");
				}
			}

		} catch (Exception e) {
			logger.error("Error Occured highlighting Border " + element);
		}
	}

	public static By getLocator(String propPath, String sPropertyValue) {
		By byProperty = null;

		try {

			String sProperty = getPropValues(propPath, sPropertyValue);

			String[] arrProperty = sProperty.split("=", 2);
			logger.info(sProperty);
			String sLocator = arrProperty[0];
			String sLocator_Property = arrProperty[1];

			// Find the Element Property to be used for Identifying the Object
			if (sLocator.equalsIgnoreCase("css")) {
				byProperty = By.cssSelector(sLocator_Property);
			} else if (sLocator.equalsIgnoreCase("id")) {
				byProperty = By.id(sLocator_Property);
			} else if (sLocator.equalsIgnoreCase("linkText")) {
				byProperty = By.linkText(sLocator_Property);
			} else if (sLocator.equalsIgnoreCase("name")) {
				byProperty = By.name(sLocator_Property);
			} else if (sLocator.equalsIgnoreCase("partialLinkText")) {
				byProperty = By.partialLinkText(sLocator_Property);
			} else if (sLocator.equalsIgnoreCase("tagName")) {
				byProperty = By.tagName(sLocator_Property);
			} else if (sLocator.equalsIgnoreCase("xpath")) {
				byProperty = By.xpath(sLocator_Property);
			}
		} catch (Exception e) {
			logger.error("Error Occured getting the Locator " + byProperty
					+ "==>" + e.getMessage());
		}
		return byProperty;
	}

	public static String getPropValues(String propPath, String sProperty) {
		String sResult = "";
		InputStream input = null;
		try {
			Properties prop = new Properties();
			input = new FileInputStream(System.getProperty("user.dir")
					+ "\\OR\\" + propPath + ".properties");
			prop.load(input);
			sResult = prop.getProperty(sProperty);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sResult;
	}

	/**
	 * Returns the Value for the Key mentioned in Config File
	 * 
	 * @param sProperty
	 * @return
	 */
	public static String getConfigValues(String sProperty) {
		String sValue = "";
		InputStream input = null;
		try {
			Properties prop = new Properties();
			input = new FileInputStream(System.getProperty("user.dir")
					+ "\\Config\\Config" + ".properties");
			prop.load(input);
			sValue = prop.getProperty(sProperty);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sValue;
	}

	public static String returnLogFilePath() throws IOException {
		String TimeStamp = fn_GetCurrentTimeStamp();
		String FolderPath = System.getProperty("user.dir")
				+ "\\Output\\Logs\\application_" + TimeStamp + "\\";
		File FolderObj = new File(FolderPath);
		FolderObj.mkdir();
		FolderPath = FolderObj.getAbsolutePath();
		String FullFilePath = FolderPath + "\\" + "application.log";
		System.setProperty("filename", FullFilePath);
		return FullFilePath;

	}

	/* Method for taking the Screen Shot End */
	// This Method Return the Date as String
	public static String fn_GetCurrentTimeStamp() {
		Date dte = new Date();
		DateFormat df = DateFormat.getDateTimeInstance();
		String strdte = df.format(dte);
		strdte = strdte.replaceAll(":", "_");
		return strdte;
	}

	/**
	 * Attempts to try to find element again
	 * 
	 * @param driver
	 * @param by
	 * @return
	 * @throws Exception 
	 */
	public static WebElement retryingFindElement(WebDriver driver, By by)
			throws Exception {
		WebElement element = null;
		int attempts = 0;
		int iTotalAttempts = Integer
				.parseInt(getConfigValues("TotalRetryAttempts"));
		try
		{
			while (attempts < iTotalAttempts) {
				logger.info("ReTrying For Element to find Again ");
				try {
					Thread.sleep(50l);
					driver.findElement(by);
					break;
				} catch (StaleElementReferenceException e) {
					logger.error("Error Occured while retrying Find Element "
							+ element + "==> " + e.getMessage());
				} catch (TimeoutException e) {
					logger.info("TimeOut");
				} catch (NoSuchElementException e) {
					logger.info("Encountered No Such Element exception on Attempt No : "
							+ attempts);
				}

				attempts++;
			}
		} catch (Exception e)
		{
			throw new Exception("Exception "+e.getMessage());
		}
		return element;
	}

	/**
	 * Wait for element to present on the DOM of a page and visible. Visibility
	 * means that the element is not only displayed but also has a height and
	 * width that is greater than 0.
	 * 
	 * @author ssach5
	 * @param driver
	 * @param by
	 * @return
	 * @throws Exception
	 */
	public static WebElement FindAndWaitForElement(WebDriver driver, final By by)
			throws Exception {

		WebElement element = null;
		WebDriverWait wait = null;
		int timeOut = 0;
		try {
			timeOut = Integer.parseInt(Cls_Generic_methods.getConfigValues("timeOutInSeconds"));
			wait = new WebDriverWait(driver, timeOut, 10);
			element = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(by);
				}
			});
		} catch (Exception e) {
			element = retryingFindElement(driver, by);
		}
		return element;

	}

	/**
	 * Wait for the List<WebElement> to be present in the DOM, regardless of
	 * being displayed or not. Returns all elements within the current page DOM.
	 * 
	 * @param WebDriver
	 *            The driver object to be used
	 * @param By
	 *            selector to find the element
	 * @param int The time in seconds to wait until returning a failure
	 *
	 * @return List<WebElement> all elements within the current page DOM, or
	 *         null (if the timeout is reached)
	 */
	public static List<WebElement> waitForListElementsPresent(WebDriver driver,
			final By by)
			{
		List<WebElement> elements;
		int timeOutInSeconds=Integer.parseInt(Cls_Generic_methods.getConfigValues("timeOutInSeconds"));
		try {

			logger.info("Waiting for List of Elements to be present");
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			wait.until((new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driverObject) {
					return areElementsPresent(driverObject, by);
				}
			}));

			elements = driver.findElements(by);

			return elements; // return the element
		} catch (Exception e) {
			logger.error("Exception ocurred " + e.getMessage());
		}
		return null;
			}

	/**
	 * Waits and Switches to the Frame
	 * 
	 * @param driver
	 * @param timeOutInSeconds
	 * @param locator
	 * @throws InterruptedException
	 */
	public static void waitAndSwitchToFrame(WebDriver driver,
			int timeOutInSeconds, By locator) throws InterruptedException {
		waitForPageLoad(driver);
		logger.info("Switching to Frame" + locator.toString());
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}

	/**
	 * Waits and Switched to the Frame found by its Id or Name
	 * 
	 * @param driver
	 * @param timeOutInSeconds
	 * @param sFrameName
	 * @throws InterruptedException
	 */
	public static void waitAndSwitchToFrame(WebDriver driver,
			int timeOutInSeconds, String sFrameName)
					throws InterruptedException {
		WebDriverWait wait = null;
		try {
			waitForPageLoad(driver);
			logger.info("Waiting and Switching to Frame by its Name "
					+ sFrameName);
			wait = new WebDriverWait(driver, timeOutInSeconds);
			wait.until(ExpectedConditions
					.frameToBeAvailableAndSwitchToIt(sFrameName));
			logger.info("Switched to Frame : " + sFrameName);
		} catch (TimeoutException e) {
			driver.navigate().refresh();
			waitForPageLoad(driver);
			wait.until(ExpectedConditions
					.frameToBeAvailableAndSwitchToIt(sFrameName));
		} catch (Exception e) {
			logger.error("Error Occured while switching to frame " + sFrameName
					+ e.getMessage());
			driver.switchTo().frame(sFrameName);
		}

	}

	/**
	 * Checks if the List<WebElement> are in the DOM, regardless of being
	 * displayed or not.
	 * 
	 * @param driver
	 *            - The driver object to use to perform this element search
	 * @param by
	 *            - selector to find the element
	 * @return boolean
	 */
	private static boolean areElementsPresent(WebDriver driver, By by) {
		try {
			driver.findElements(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Checks if the element is in the DOM and displayed.
	 * 
	 * @param driver
	 *            - The driver object to use to perform this element search
	 * @param by
	 *            - selector to find the element
	 * @return boolean
	 */
	private static boolean isElementPresentAndDisplay(WebDriver driver, By by) {
		try {
			return driver.findElement(by).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Checks if the element is clickable.
	 * 
	 * @param driver
	 *            - The driver object to use to perform this element search
	 * @param by
	 *            - selector to find the element
	 * @return boolean
	 */
	private static boolean isElementClickable(WebDriver driver, By by) {
		WebElement element;
		try {
			element = driver.findElement(by);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(getConfigValues("timeOutInSeconds")));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Checks if the element is clickable.
	 * 
	 * @param driver
	 * @param element
	 * @return
	 */
	private static boolean isElementClickable(WebDriver driver,
			WebElement element) {
		try {
			logger.info("Checking if Element is Clickable");
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(getConfigValues("timeOutInSeconds")));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (NoSuchElementException e) {
			logger.info("Some thing wrong with Element " + e.getMessage());
			return false;
		}
	}

	/**
	 * Checks if the elment is in the DOM, regardless of being displayed or not.
	 * 
	 * @param driver
	 *            - The driver object to use to perform this element search
	 * @param by
	 *            - selector to find the element
	 * @return boolean
	 */
	private static boolean isElementPresent(WebDriver driver, By by) {
		WebElement element;
		try {
			element = driver.findElement(by);

			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Checks if the text is present in the element.
	 * 
	 * @param driver
	 *            - The driver object to use to perform this element search
	 * @param by
	 *            - selector to find the element that should contain text
	 * @param text
	 *            - The Text element you are looking for
	 * @return true or false
	 */
	private static boolean isTextPresent(WebDriver driver, By by, String text) {
		try {
			return driver.findElement(by).getText().contains(text);
		} catch (NullPointerException e) {
			return false;
		}
	}

	public static boolean waitForElementToBeDisplay(WebElement element,
			int maxSecondTimeout, boolean... isFailOnExcaption)
					throws Exception {
		try {
			logger.info("INTO METHOD waitForElementToBeDisplay");
			maxSecondTimeout = maxSecondTimeout * 20;
			while ((!element.isDisplayed() && (maxSecondTimeout > 0))) {
				// logger.info("Loading...CountDown="+maxSecondTimeout);
				Thread.sleep(50l);
				maxSecondTimeout--;
			}
			if ((maxSecondTimeout == 0) && (isFailOnExcaption.length != 0)) {
				if (isFailOnExcaption[0] == true) {
					logger.error("Element is not display within "
							+ (maxSecondTimeout / 20) + "Sec.");
					throw new Exception("Element is not display within "
							+ (maxSecondTimeout / 20) + "Sec.");
				}
			}
			logger.info("OUT OF METHOD waitForElementToBeDisplay");
			return true;
		} catch (UnhandledAlertException e) {
			logger.error("Unexpected alert is coming->waitForElementToBeDisplay->"
					+ e.getMessage());
			throw new UnhandledAlertException(
					"Unexpected alert is coming->waitForElementToBeDisplay->"
							+ e.getMessage());
		} catch (NoSuchElementException e) {
			logger.error("The element to be search is not present in the page->waitForElementToBeDisplay->"
					+ e.getMessage());
			throw new NoSuchElementException(
					"The element to be search is not present in the page->waitForElementToBeDisplay->"
							+ e.getMessage());
		} catch (Exception e) {
			logger.error("Some exception in waitForElementToBeDisplay"
					+ e.getMessage());
			throw new Exception("Some exception in waitForElementToBeDisplay"
					+ e.getMessage());
		}
	}

	/**
	 * Return true if Element is Enabled
	 * 
	 * @param element
	 * @return
	 */
	public static boolean isElementEnabled(WebElement element) {
		boolean isEnabled = false;
		try {
			logger.info("Checking if Element is enabled");
			isEnabled = element.isEnabled();
			logger.info("Element is enabled " + element);
		} catch (Exception e) {
			logger.error("Some thing wrong with element " + e.getMessage());
		}
		return isEnabled;
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	public static boolean isElementDisabled(WebElement element) {
		boolean isDisabled = false;
		try {
			logger.info("Checking if Element is disabled");
			isDisabled = element.isEnabled();
			logger.info("Element is disabled " + element);
		} catch (Exception e) {
			logger.error("Some thing wrong with element " + e.getMessage());
		}
		return isDisabled;
	}

	/**
	 * Performs Double Click on the Element using Action Class
	 * 
	 * @param driver
	 * @param element
	 */
	public static void doubleClick(WebDriver driver, WebElement element) {
		try {
			logger.info("Double Click element via Action");
			Actions action = new Actions(driver);
			highlightElementBorder(driver, element);
			action.doubleClick(element).build().perform();
		} catch (Exception e) {
			logger.error("Error Occured while double clicking " + element);
		}
	}


	/**
	 * Performs Right Click on the Element using Action
	 * 
	 * @param driver
	 * @param element
	 */
	public static void rightClick(WebDriver driver, WebElement element) {
		try {
			logger.info("Right Click on Element using Action Class");
			Actions action = new Actions(driver);
			highlightElementBorder(driver, element);
			action.contextClick(element).build().perform();
		} catch (Exception e) {
			logger.error("Error Occured while right clicking " + element);
		}
	}

	/**
	 * Switch back to the Main or Default Window
	 * 
	 * @param driver
	 */
	public static WebDriver switchToDefault(WebDriver driver) {

		try {
			logger.info("Switch to Default Content");
			return driver.switchTo().defaultContent();
		} catch (Exception e) {
			logger.error("Error Occured while switch to default windiw ");
		}
		return driver;
	}

	public static boolean executeJS(String sScript) {
		boolean Isexecuted = false;
		try {

		} catch (Exception e) {
			return false;
		}
		return Isexecuted;
	}

	/**
	 * Waits for Page Load via Java Script Ready State
	 * 
	 * @param driver
	 * @param iTimeOut
	 * @throws InterruptedException
	 */
	public static boolean waitForPageLoad(WebDriver driver)
			throws InterruptedException {
		boolean isLoaded = false;
		int iTimeOut = Integer.parseInt(Cls_Generic_methods
				.getConfigValues("timeOutInSeconds"));
		Thread.sleep(2000);
		try {
			logger.info("Waiting For Page load via JS");
			ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript(
							"return document.readyState").equals("complete");
				}
			};
			WebDriverWait wait = new WebDriverWait(driver, iTimeOut);
			wait.until(pageLoadCondition);
			isLoaded = true;
		} catch (Exception e) {
			logger.error("Error Occured waiting for Page Load "
					+ driver.getCurrentUrl());
		}
		return isLoaded;
	}

	/**
	 * Waits for Page Load via Java Script Ready State
	 * 
	 * @param driver
	 * @param iTimeOut
	 * @throws InterruptedException
	 */
	public static boolean waitForPageLoad(WebDriver driver, int iTimeOut)
			throws InterruptedException {
		boolean isLoaded = false;


		try {
			logger.info("Waiting For Page load via JS");
			ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript(
							"return document.readyState").equals("complete");
				}
			};
			WebDriverWait wait = new WebDriverWait(driver, iTimeOut);
			wait.until(pageLoadCondition);
			isLoaded = true;
		} catch (Exception e) {
			logger.error("Error Occured waiting for Page Load "
					+ driver.getCurrentUrl());
		}
		return isLoaded;
	}

	/**
	 * Selects a particular WebElement from the Select . It can be used when
	 * HTML have Select <Option> DOM
	 *
	 * @param lstElementList
	 * @param sValueToBeSelected
	 */
	public static void selectFromList(WebElement select,
			String sValueToBeSelected) {
		try {
			logger.info("Inside getElementFromList method");
			List<WebElement> options = select
					.findElements(By.tagName("option"));
			logger.info("Total elements having Option TAG :" + options.size());
			for (WebElement option : options) {
				logger.info(option.getText());
				if (option.getText().trim()
						.equalsIgnoreCase(sValueToBeSelected.trim())) {
					logger.info("Tag Name matched and will be clicked");
					option.click();
					break;
				}

			}
		} catch (Exception e) {
			logger.error("Error ocurred while selecting the element fron List"
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	public static WebElement fetchFromList(WebElement select,
			String sValueToBeSelected) {
		WebElement element=null;
		try {
			logger.info("Inside getElementFromList method");
			List<WebElement> options = select
					.findElements(By.tagName("option"));
			logger.info("Total elements having Option TAG :" + options.size());
			for (WebElement option : options) {logger.info(option.getText());
			if (option.getText().trim().equalsIgnoreCase(sValueToBeSelected.trim())) {
				logger.info("Tag Name matched and will be clicked");
				element=option;
				break;
			}

			}
		} catch (Exception e) {
			logger.error("Error ocurred while selecting the element fron List"
					+ e.getMessage());
			e.printStackTrace();
		}
		return element;

	}

	public static void selectFromListByIndex(WebElement element, String index) {
		try {
			logger.info("Inside getElementFromList method");
			int iIndex = Integer.parseInt(index);
			Select select = new Select(element);
			select.selectByIndex(iIndex);
		} catch (Exception e) {
			logger.error("Error ocurred while selecting the element fron List"
					+ e.getMessage());
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 * @param element
	 * @param sValue
	 */
	public static void selectFromListByValue(WebElement element, String sValue) {
		try {
			logger.info("Inside getElementFromList method");
			Select select = new Select(element);
			select.selectByValue(sValue);
		} catch (Exception e) {
			logger.error("Error ocurred while selecting the element by its Value"
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	public static void selectFromList(List<WebElement> lstElementList,
			String sValueToBeSelected) throws Exception {
		logger.info("START OF FUNCTION->selectFromList");
		boolean flag = false;
		try {
			logger.info("Total element found --> " + lstElementList.size());
			logger.info("Value to be selected " + sValueToBeSelected
					+ " from list" + lstElementList);

			for (WebElement e : lstElementList) {
				logger.info(">>>>>>>>>>>>>" + e.getText() + "<<<<<<<<<<<<<<<");
				if (e.getText().equalsIgnoreCase(sValueToBeSelected)) {
					logger.info("Value matched in list as->" + e.getText());
					e.click();
					flag = true;
					break;
				}
			}
			if (flag == false) {
				logger.error("No match found in the list for value->"
						+ sValueToBeSelected);
				// throw new
				// Exception("No match found in the list for value->"+sValueToBeSelected);
			}
			logger.info("END OF FUNCTION->selectFromList");
		} catch (Exception e) {
			logger.error("THERE IS AN EXCEPTION ON SELECTING VALUE FROM LIST->"
					+ e.getMessage());

		}
	}

	public static WebElement fetchFromList(List<WebElement> lstElementList,
			String sValueToBeSelected) throws Exception {
		logger.info("START OF FUNCTION->selectFromList");

		WebElement element=null;
		try {
			logger.info("Total element found --> " + lstElementList.size());
			logger.info("Value to be selected " + sValueToBeSelected
					+ " from list" + lstElementList);

			for (WebElement e : lstElementList) {
				logger.info(">>>>>>>>>>>>>" + e.getText() + "<<<<<<<<<<<<<<<");
				if (e.getText().equalsIgnoreCase(sValueToBeSelected)) {
					logger.info("Value matched in list as->" + e.getText());
					element=e;

					break;
				}
			}

			logger.info("END OF FUNCTION->selectFromList");
		} catch (Exception e) {
			logger.error("THERE IS AN EXCEPTION ON SELECTING VALUE FROM LIST->"
					+ e.getMessage());

		}
		return element;
	}

	/**
	 * mouse overs to an element specified.
	 * 
	 * @param driver
	 * @param Element
	 */
	public static void mouseOverElement(WebDriver driver, WebElement element) {
		try {
			logger.info("Mouse Over Element :" + element);
			Actions builder = new Actions(driver);
			builder.moveToElement(element).build().perform();
			logger.info("Mouse Overed to Element " + element);
		} catch (Exception e) {
			logger.error("Exception ocurred while moving to element "
					+ e.getMessage());
		}
	}

	/**
	 * wait till test present on element
	 * 
	 * @param element
	 * @param maxSecondTimeout
	 * @param isFailOnExcaption
	 *            ( optional parameter true if fail on exception)
	 * @throws Exception
	 */
	public static boolean waitForTextToBePresentOnElement(WebElement element,
			int maxSecondTimeout, String matchText,
			boolean... isFailOnExcaption) throws Exception {
		try {
			logger.info("INTO METHOD waitForTextToBePresentOnElement");
			maxSecondTimeout = maxSecondTimeout * 20;
			while ((!element.isDisplayed() && (maxSecondTimeout > 0) && (element
					.getText().toLowerCase().equalsIgnoreCase(matchText
							.toLowerCase().trim())))) {
				logger.info("Loading...CountDown=" + maxSecondTimeout);
				Thread.sleep(50l);
				maxSecondTimeout--;
			}
			if ((maxSecondTimeout == 0) && (isFailOnExcaption.length != 0)) {
				if (isFailOnExcaption[0] == true) {
					logger.error("Element is not display within "
							+ (maxSecondTimeout / 20) + "Sec.");
					throw new Exception("Element is not display within "
							+ (maxSecondTimeout / 20) + "Sec.");
				}
			}
			logger.info("OUT OF METHOD waitForTextToBePresentOnElement");
			return true;
		} catch (UnhandledAlertException e) {
			throw new UnhandledAlertException(
					"Unexpected alert is coming->waitForTextToBePresentOnElement->"
							+ e.getMessage());
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException(
					"The element to be search is not present in the page->waitForTextToBePresentOnElement->"
							+ e.getMessage());
		} catch (Exception e) {
			throw new Exception(
					"Some exception in waitForTextToBePresentOnElement"
							+ e.getMessage());
		}
	}

	/**
	 * Wait for html element to be hidden
	 * 
	 * @param element
	 * @param maxSecondTimeout
	 * @param isFailOnExcaption
	 *            ( optional parameter true if fail on exception)
	 * @throws Exception
	 * @throws IOException
	 */
	public static boolean waitForElementToBeHidden(WebElement element,
			int maxSecondTimeout, boolean... isFailOnExcaption)
					throws Exception {
		try {
			logger.info("INTO waitForElementToBeHidden METHOD");
			maxSecondTimeout = maxSecondTimeout * 20;
			while (element.isDisplayed() && (maxSecondTimeout > 0)) {
				// logger.info("Loading...CountDown="+maxSecondTimeout);
				Thread.sleep(50l);
				maxSecondTimeout--;
			}
			if ((maxSecondTimeout == 0) && (isFailOnExcaption.length != 0)) {
				if (isFailOnExcaption[0] == true) {
					logger.error("Element is not hidden within "
							+ (maxSecondTimeout / 20) + "Sec.");
					throw new Exception("Element is not hidden within "
							+ (maxSecondTimeout / 20) + "Sec.");
				}
			}
			logger.info("OUT OF METHOD waitForElementToBeHidden");
			return true;
		} catch (NoSuchElementException e) {
			throw new Exception(
					"The element to be serach is not present in the page->waitForElementToBeHidden->"
							+ e.getMessage());
		} catch (Exception e) {
			throw new Exception("Some exception in waitForElementToBeHidden"
					+ e.getMessage());
		}
	}

	/**
	 * Uses ExpectedCondition to wait for Element to Appear Using wait
	 * 
	 * @param driver
	 * @param element
	 * @param timeOutInSeconds
	 * @param pollingInMilliSeconds
	 * @return
	 * @throws Exception
	 * @author ssachdeva
	 */
	public static boolean waitForElementToAppear(WebDriver driver,
			final WebElement element, int timeOutInSeconds,
			int pollingInMilliSeconds) throws Exception {
		try {
			logger.info("Wait for Element to Appear using Wait until element is Displayed");
			return (new WebDriverWait(driver, timeOutInSeconds,
					pollingInMilliSeconds))
					.until(new ExpectedCondition<Boolean>() {

						public Boolean apply(WebDriver driver) {
							logger.info("Waiting for element to be displayed ");
							return element.isDisplayed();
						}
					});

		} catch (NoSuchElementException e) {
			logger.error("The element to be serach is not present in the page->waitForElementToBeHidden->"
					+ e.getMessage());
			throw new Exception(
					"The element to be serach is not present in the page->waitForElementToBeHidden->"
							+ e.getMessage());
		} catch (Exception e) {
			logger.error("Some exception in waitForElementToBeHidden"
					+ e.getMessage());
			throw new Exception("Some exception in waitForElementToBeHidden"
					+ e.getMessage());
		}

	}

	/**
	 * Waits for an Element to DisAppear using Wait
	 * 
	 * @param driver
	 * @param element
	 * @param timeOutInSeconds
	 * @param pollingInMilliSeconds
	 * @return
	 * @throws Exception
	 */
	public static boolean waitForElementToDisAppear(WebDriver driver,
			final WebElement element, int timeOutInSeconds,
			int pollingInMilliSeconds) throws Exception {
		try {
			logger.info("Waiting for element to disappear using wait untile element is not Displayed");
			return (new WebDriverWait(driver, timeOutInSeconds,
					pollingInMilliSeconds))
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver driver) {

							logger.info("Waiting dfor element to be disappear ");
							return !element.isDisplayed();
						}
					});

		} catch (NoSuchElementException e) {
			logger.error("The element to be serach is not present in the page->waitForElementToBeHidden->"
					+ e.getMessage());
			throw new Exception(
					"The element to be serach is not present in the page->waitForElementToBeHidden->"
							+ e.getMessage());
		} catch (Exception e) {
			logger.error("Some exception in waitForElementToBeHidden"
					+ e.getMessage());
			throw new Exception("Some exception in waitForElementToBeHidden"
					+ e.getMessage());
		}

	}

	/**
	 * Returns an Random Integer Number from an Range
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int generateRandomNumber(int min, int max) {
		int mynumber = 0;
		try {
			logger.info("Generating Randon Number");
			Random r = new Random();
			mynumber = r.nextInt(max - min) + min;

		} catch (Exception e) {
			logger.error("Error ocurred while generating Randon Num"
					+ e.getMessage());
		}
		return mynumber;
	}

	/**
	 * Waits for an Alert or Pop Up
	 * 
	 * @param driver
	 * @param maxTimeInSeconds
	 * @throws InterruptedException
	 */
	public static void waitForAlert(WebDriver driver, int maxTimeInSeconds)
			throws InterruptedException {
		while ((maxTimeInSeconds) != 0) {

			driver.switchTo().alert().accept();
			logger.info("Alert Accepted");
			break;
		}
	}

	/**
	 * Waits Until the Attribute of Element got Changed.
	 * 
	 * @param webElement
	 * @param attribute
	 * @param value
	 * @param maxSecondTimeout
	 */
	public static void waitTillElementAttributeChange(WebElement webElement,
			String attribute, String value, int maxSecondTimeout) {
		// boolean flag=false;
		try {
			logger.info("INTO METHOD waitTillElementAttributeChange");
			maxSecondTimeout = maxSecondTimeout * 20;
			while (webElement.getAttribute(attribute) != null) {
				if ((!webElement.getAttribute(attribute.trim()).toLowerCase()
						.contains(value.trim().toLowerCase()))
						&& (maxSecondTimeout > 0)) {
					logger.info("Loading...CountDown=" + maxSecondTimeout);
					try {
						Thread.sleep(50l);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					maxSecondTimeout--;

				}

			}
			logger.info("OUT OF METHOD waitTillElementAttributeChange");
		} catch (Exception e) {
			logger.error("SOME ERROR CAME IN METHOD->waitTillElementAttributeChange->"
					+ e.getMessage());

		}
	}

	public static void dragAndDrop(WebDriver driver, WebElement source,
			WebElement destination) throws Exception {
		try {
			logger.info("START OF METHOD dragAndDrop ::::::::::::::::::::::::::::");
			Actions builder = new Actions(driver);
			builder.dragAndDrop(source, destination).build().perform();
			// Action dragAndDrop =
			// builder.clickAndHold(source).moveToElement(destination).release(destination).build();
			// dragAndDrop.perform();
			logger.info("END OF METHOD dragAndDrop ::::::::::::::::::::::::::");
		} catch (Exception e) {
			logger.error("::::::::::::::::::::::::::::::" + e.getMessage());
			throw new Exception("::::::::::::::::::::::::" + e.getMessage());
		}
	}

	/**
	 * Drags and Drop by Holding the Element
	 * 
	 * @param driver
	 * @param source
	 * @param destination
	 * @throws Exception
	 */
	public static void dragAndDropByClickHold(WebDriver driver,
			WebElement source, WebElement destination) throws Exception {
		try {
			logger.info("START OF METHOD dragAndDrop BY Click Hold ::::::::::::::::::::::::::::");
			Actions builder = new Actions(driver);
			builder.clickAndHold(source).build().perform();
			builder.moveToElement(destination).build().perform();
			builder.release(destination).build().perform();
			logger.info("END OF METHOD dragAndDrop BY Click Hold::::::::::::::::::::::::::");
		} catch (Exception e) {
			logger.error("::::::::::::::::::::::::::::::" + e.getMessage());
			throw new Exception("::::::::::::::::::::::::" + e.getMessage());
		}
	}

	/**
	 * Select hold and release object to destination place.
	 * 
	 * @param driver
	 * @param source
	 * @param destination
	 * @throws Exception
	 */
	public static void dragAndDropByClickHoldRelease(WebDriver driver,
			WebElement source, WebElement destination) throws Exception {
		try {
			logger.info("START OF METHOD dragAndDropByClickHoldRelease ::::::::::::::::::::::::::::");
			Actions builder = new Actions(driver);
			builder.clickAndHold(source).moveToElement(destination)
			.release(destination).build().perform();

			logger.info("END OF METHOD dragAndDropByClickHoldRelease ::::::::::::::::::::::::::");
		} catch (Exception e) {
			logger.error("::::::::::::::::::::::::::::::" + e.getMessage());
			throw new Exception("::::::::::::::::::::::::" + e.getMessage());
		}
	}

	/**
	 * Returns selected option in select Drop Down
	 * 
	 * @param select
	 * @param sValueToBeSelected
	 */
	public static String getSelectedOption(WebElement options) {
		String sSelectedOption = null;
		try {
			logger.info("INTO THE METHOD getSelectedOption");
			Select selectedOption = new Select(options);
			sSelectedOption = selectedOption.getFirstSelectedOption().getText();
			logger.info("OUT OF METHOD getSelectedOption");
		} catch (Exception e) {
			logger.error("Some error came on function->select->"
					+ e.getMessage());
		}
		return sSelectedOption;
	}

	/**
	 * Wait For Element to Enable
	 * 
	 * @param element
	 * @param maxSecondTimeout
	 * @return
	 * @throws Exception
	 */
	public static boolean waitForElementToEnable(WebElement element,
			int maxSecondTimeout) throws Exception {
		try {
			logger.info("INTO waitForElementToEnable METHOD");
			maxSecondTimeout = maxSecondTimeout * 20;
			while (!element.isEnabled() && (maxSecondTimeout > 0)) {
				// logger.info("Loading...CountDown="+maxSecondTimeout);
				Thread.sleep(50l);
				maxSecondTimeout--;
			}
			if ((maxSecondTimeout == 0)) {

				logger.error("Element is not enabled within "
						+ (maxSecondTimeout / 20) + "Sec.");
				throw new Exception("Element is not enabled within "
						+ (maxSecondTimeout / 20) + "Sec.");
			}
			logger.info("OUT OF METHOD waitForElementToEnable");
			return true;
		} catch (NoSuchElementException e) {
			throw new Exception(
					"The element to be enabled is not present in the page->waitForElementToEnable->"
							+ e.getMessage());
		} catch (Exception e) {
			throw new Exception("Some exception in waitForElementToEnable"
					+ e.getMessage());
		}
	}

	/**
	 * Wait For Element to Disable
	 * 
	 * @param element
	 * @param maxSecondTimeout
	 * @return
	 * @throws Exception
	 */
	public static boolean waitForElementToDisable(WebElement element,
			int maxSecondTimeout) throws Exception {
		try {
			logger.info("INTO waitForElementToDisable METHOD");
			maxSecondTimeout = maxSecondTimeout * 20;
			while (element.isEnabled() && (maxSecondTimeout > 0)) {
				// logger.info("Loading...CountDown="+maxSecondTimeout);
				Thread.sleep(50l);
				maxSecondTimeout--;
			}
			if ((maxSecondTimeout == 0)) {

				logger.error("Element is not disabled within "
						+ (maxSecondTimeout / 20) + "Sec.");
				throw new Exception("Element is not disabled within "
						+ (maxSecondTimeout / 20) + "Sec.");
			}
			logger.info("OUT OF METHOD waitForElementToDisable");
			return true;
		} catch (NoSuchElementException e) {
			throw new Exception(
					"The element to be disabled is not present in the page->waitForElementToDisable->"
							+ e.getMessage());
		} catch (Exception e) {
			throw new Exception("Some exception in waitForElementToDisable"
					+ e.getMessage());
		}
	}

	/**
	 * Waits for an Element to get Stale or deleted from DOM
	 * 
	 * @param driver
	 * @param iTimeOutInSeconds
	 * @param by
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static boolean waitForElementToStale(WebDriver driver,
			int iTimeOutInSeconds, By by) throws NumberFormatException,
			IOException {
		boolean isStale = true;
		int iAttempt = 0;
		try {
			iTimeOutInSeconds = iTimeOutInSeconds * 20;
			while (isStale && iTimeOutInSeconds > 0) {
				iAttempt++;
				logger.info("Waiting for Element to Statle Attempt Number :"
						+ iAttempt);
				driver.manage().timeouts()
				.implicitlyWait(100, TimeUnit.MILLISECONDS);
				logger.info("Element :" + driver.findElement(by).isDisplayed());
				if (driver.findElements(by).size() == 0) {
					isStale = false;
					break;
				}
				Thread.sleep(30l);
				iTimeOutInSeconds--;
			}
		} catch (NoSuchElementException e) {
			logger.error("No Element Found.This Means Loader is no more in HTML. Moving out of waitForElementToStale!!!");
			isStale = false;
		} catch (StaleElementReferenceException s) {
			logger.error("Given Element is stale from DOM Moving out of waitForElementToStale!!!");
			isStale = false;
		} catch (Exception e) {
			logger.error("Some Exception ocurred Please check code!!!");
		} finally {
			driver.manage()
			.timeouts()
			.implicitlyWait(
					Integer.parseInt(getConfigValues("ImplicitWait")),
					TimeUnit.SECONDS);
		}
		return isStale;
	}

	/**
	 * Returns true if checkbox is checked.
	 * 
	 * @author ssachdeva
	 * @param element
	 * @return
	 */
	public static boolean isCheckboxChecked(WebElement element) {
		boolean isChecked = false;
		try {
			logger.info("getting class of received element "
					+ element.getAttribute("class"));
			if (element.getAttribute("class").contains("selected")) {
				logger.info("check box is checked");
				isChecked = true;
			} else {
				isChecked = false;
			}

		} catch (Exception e) {
			logger.error("Some exception ocurred while finding if Region Checkbox is checked or not "
					+ e.getMessage());
		}
		return isChecked;
	}

	/**
	 * waits and click on element
	 * 
	 * @param driver
	 * @param element
	 * @throws Exception 
	 */
	public static void clickElement(WebDriver driver, WebElement element) throws Exception {

		logger.info("Clicking element " + element);
		if(driver instanceof InternetExplorerDriver)
		{
			moveToElementAndClick(driver, element);
		}
		else{
			if (isElementClickable(driver, element)) {
				highlightElementBorder(driver, element);
				element.click();
			} else {
				throw new Exception("Element not in state of clickable");
			}
		}

		logger.info("Waited and Clicked on element ");

	}


	public static void clickElementByAction(WebDriver driver, WebElement element) {

		Actions action = new Actions(driver);
		action.click(element).build().perform();
	}

	/**
	 * Waits and then sendkeys to element
	 * 
	 * @param driver
	 * @param element
	 * @param sValue
	 */
	public static void sendKeys(WebDriver driver, WebElement element,
			String sValue) {
		WebElement ele;
		try {
			logger.info("Waiting for an element to be clickable " + element);
			highlightElementBorder(driver, element);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(getConfigValues("timeOutInSeconds")));
			ele = wait.until(ExpectedConditions.elementToBeClickable(element));
			ele.sendKeys(sValue);
			logger.info("Waited and send value to on element " + element);
		} catch (Exception e) {
			logger.info("Exception while supplying text to element"
					+ e.getMessage());
		}
	}

	/**
	 * waits for specified duration and checks that an element is present on
	 * DOM. Visibility means that the element is not only displayed but also has
	 * a height and width that is greater than 0.
	 * 
	 * @param driver
	 * @param element
	 * @param sValue
	 */
	public static void waitForElementToBeVisible(WebDriver driver,
			WebElement element) {
		logger.info("Waiting for an element to be visible " + element);
		WebDriverWait wait = new WebDriverWait(driver,
				Integer.parseInt(getConfigValues("timeOutInSeconds")));
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static void waitForElementToBeVisible(WebDriver driver, By locator) {
		try {
			logger.info("Waiting for an element to be visible using By locator "
					+ locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(getConfigValues("timeOutInSeconds")));
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (Exception e) {
			logger.info("Exception while waiting for visibility"
					+ e.getMessage());
		}
	}

	public static boolean IsElementVisible(WebElement element) throws Exception
	{
		boolean isVisible=false;

		try
		{
			if( element.isDisplayed() && element.isEnabled())
			{
				isVisible=true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return isVisible;
	}

	/**
	 * An expectation for checking if the given text is present in the specified
	 * element
	 * 
	 * @param driver
	 * @param element
	 * @param sText
	 */
	public static void waitFortextToBePresentInElement(WebDriver driver,
			final WebElement element, final String sText) {
		WebDriverWait wait = new WebDriverWait(driver,
				Integer.parseInt(getConfigValues("timeOutInSeconds")));
		wait.until(ExpectedConditions.textToBePresentInElement(element, sText));
	}

	/**
	 * An expectation for checking if the given text is present in the element
	 * that matches the given locator.
	 * 
	 * @param driver
	 * @param locator
	 * @param sText
	 */
	public static void waitFortextToBePresentInElementLocated(WebDriver driver,
			By locator, final String sText) {
		WebDriverWait wait = new WebDriverWait(driver,
				Integer.parseInt(getConfigValues("timeOutInSeconds")));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(locator,
				sText));
	}

	/**
	 * switch to window handle based on handle name
	 * 
	 * @param driver
	 * @param wndHandle
	 */
	public static void switchToWindowHandle(WebDriver driver, String wndHandle) {

		logger.info("inside switchtowindowhandle Method");
		Set<String> handler = driver.getWindowHandles();
		for (String handlesname : handler) {
			driver.switchTo().window(handlesname);
			String var = driver.getTitle();
			logger.info("window Handle --> " + var);
			if (var.equalsIgnoreCase(wndHandle)) {
				logger.info("Title matched hence switching to handle "
						+ wndHandle);
				driver.switchTo().window(handlesname);
			} else {
				driver.switchTo().defaultContent();
			}
		}

	}

	/**
	 * Performs click operation using JS
	 * 
	 * @param driver
	 * @param element
	 */
	public static void clickByJS(WebDriver driver, WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public static WebDriver getDriverBackUp(WebDriver driver, String mainHandle) {
		return driver.switchTo().window(mainHandle);
	}

	/**
	 * Switch to window handle
	 * 
	 * @param driver
	 * @param wndHandle
	 */
	public static void switchToWindowHandle1(WebDriver driver, String wndHandle) {
		driver.switchTo().window(wndHandle);
	}

	public static String takeSnapShotAndRetPath(WebDriver driver)
			throws Exception {
		logger.info("INTO METHOD->Fn_TakeSnapShotAndRetPath");
		String FullSnapShotFilePath = "";

		try {
			if (Cls_Generic_methods.getConfigValues("TakeSnapshot").trim().equals("1")) {
				logger.info("Take Screen shot started");
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				String sFilename = null;
				sFilename = "Screenshot-" +getDateTime() + ".png";
				FullSnapShotFilePath = System.getProperty("user.dir")+ "\\Output\\ScreenShots\\" + sFilename;
				FileUtils.copyFile(scrFile, new File(FullSnapShotFilePath));
			}
		}
		catch(Exception e)
		{

		}

		return FullSnapShotFilePath;
	}

	/**
	 * Scroll down the page
	 * 
	 * @param driver
	 */
	public static void scrollDown(WebDriver driver) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, 250)");
	}

	/**
	 * Scroll Up the Page
	 * 
	 * @param driver
	 */
	public static void scrollUp(WebDriver driver) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(250, 0)");
	}

	/**
	 * Scroll to the Element using JavaScript
	 * 
	 * @param driver
	 * @param element
	 */
	public static void scrollToElement(WebDriver driver, WebElement element) {
		logger.info("Scrolling to Element");
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}
	/**
	 * Move to the Element using coordinates
	 * 
	 * @param driver
	 * @param element
	 */
	public static void moveToElement(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element, element.getLocation().getX(), element
				.getLocation().getY());
		action.build().perform();
	}

	/**
	 * mouse overs to an element and click specified.
	 * 
	 * @param driver
	 * @param Element
	 */
	public static void moveToElementAndClick(WebDriver driver,
			WebElement element) {
		Actions builder = new Actions(driver);
		builder.moveToElement(element).click().build().perform();
	}

	/**
	 * Input the text using JavaScript
	 * 
	 * @param driver
	 * @param element
	 * @param sData
	 * @throws InterruptedException 
	 */
	public static void sendKeysByJS(WebDriver driver, WebElement element,
			String sData) throws InterruptedException {
		Thread.sleep(750);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].value=arguments[1];", element,
				sData);
	}

	/**
	 * Returns numeric digits from String
	 * 
	 * @param src
	 * @return
	 */
	public static String extractDigits(String src) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < src.length(); i++) {
			char c = src.charAt(i);
			if (Character.isDigit(c)) {
				builder.append(c);
			}
		}
		return builder.toString();
	}

	/**
	 * Waits for specific duration and then clicks
	 * 
	 * @param driver
	 * @param element
	 * @param iTimeOut
	 * @throws Exception 
	 */
	public static void clickAndWait(WebDriver driver, WebElement element,
			int iTimeOut) throws Exception {
		logger.info("into click and Wait method");
		clickElement(driver, element);
		Thread.sleep(iTimeOut);
		logger.info("Clicked element " + element + "and waited for " + iTimeOut
				+ "seconds");
	}

	/**
	 * Gets the Title of the Current Page
	 * 
	 * @param driver
	 * @return
	 */
	public static String getTitle(WebDriver driver) {
		return driver.getTitle();
	}

	public static void acceptAlert(WebDriver driver, int iTimeOut)
			throws InterruptedException {
		waitForAlert(driver, iTimeOut);
		Alert alert = driver.switchTo().alert();
		alert.accept();

	}

	public static void cancelAlert(WebDriver driver, int iTimeOut)
			throws InterruptedException {
		waitForAlert(driver, iTimeOut);
		Alert alert = driver.switchTo().alert();
		alert.dismiss();

	}

	/**
	 * Return text from the element
	 * 
	 * @param element
	 * @return
	 */
	public static String getText(WebElement element) {
		return element.getText();
	}

	public static void closeAllBrowsers(WebDriver driver)
	{
		driver.quit();
	}

	public static int getItemCount(List<WebElement> list)
	{
		return  (list == null) ? 0 : list.size();
	}

	public static String getLoggedInUser()
	{
		return System.getProperty("user.name");
	}

	public static String getHostName() throws UnknownHostException
	{
		return InetAddress.getLocalHost().getHostName();
	}

//	public static void clickByImage(String sImagePath) throws FindFailed
//	{
//		
//		Match match = findImageElement(sImagePath);
//		
//		if(match!=null)
//		{
//			System.out.println("match found");
//			match.click();
//		}
//	}
//
//	public static Match findImageElement(String sImagePath) throws FindFailed
//	{
//		Pattern pattern = new Pattern(sImagePath);
//		Screen screen =new Screen();
//		Match match = screen.find(pattern);
//		return match;
//	}
//
//	public static boolean isElementExistsByImage(String sImagePath)
//	{
//		boolean isExists=false;
//		try
//		{
//			Match match = findImageElement(sImagePath);
//			if(match!=null)
//			{
//				isExists=true;
//			}
//		}catch(FindFailed f)
//		{
//			logger.error("Can not find the Image "+ f.getMessage());
//		}
//		return isExists;
//	}
}

