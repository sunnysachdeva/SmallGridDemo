package com.gspann.grid;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestClass {
	
	WebDriver driver=null;
	
	@Parameters("browser")
	@BeforeClass
	public void setup(@Optional String browserType) throws MalformedURLException{
		DesiredCapabilities desiredCapabilities=null;
		if(browserType.equalsIgnoreCase("chrome")){
			desiredCapabilities= DesiredCapabilities.chrome();
			desiredCapabilities.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
			desiredCapabilities.setPlatform(Platform.WINDOWS);
		}else if(browserType.equalsIgnoreCase("firefox")){
			desiredCapabilities=DesiredCapabilities.firefox();
			desiredCapabilities.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
			desiredCapabilities.setPlatform(Platform.WINDOWS);
		}
		driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), desiredCapabilities);
	}
	
	@BeforeMethod
	public void bm(){
		driver.get("http://www.guru99.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}
	
	@Test
	public void testA(){
		WebElement element =driver.findElement(By.xpath("//a[text()=' Blog ']"));
		element.click();
	}
	
	@AfterClass
	public void cleanUp(){
		driver.close();
	}
}
