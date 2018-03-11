package com.base;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.Excel_Reader;
import Utility.ExtentManager;
import Utility.Utility;
import io.appium.java_client.android.AndroidDriver;

public class AppBase {

	public static boolean isinitialized = false;
	public static Excel_Reader xls;
	public ExtentReports rep;
	public ExtentTest test;
	public Properties prop;
	@SuppressWarnings("rawtypes")
	public AndroidDriver driver;
	public static Utility Util;
	public boolean testend = true;

	public AppBase() {
		try {
			prop = new Properties();
			FileInputStream fs = new FileInputStream(Constants.Properties_file_path);
			prop.load(fs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initialize(Excel_Reader xls, String testName) {
		if(!isinitialized) {
			xls = new Excel_Reader(Constants.ChannelPatner);
			rep = ExtentManager.getInstance();
			System.out.println("Initialized the ExcelReader and Logger");
			isinitialized = true;
		}
	}	

	/******************************Opening a Mobile App******************************/

	@SuppressWarnings("rawtypes")
	public void LaunchApp() {
		try {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("deviceName", "TOQOPBH6AYBQCIP7");
			capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");
			capabilities.setCapability(CapabilityType.VERSION, "6.0");
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("appPackage", "com.mindsarray.pay1distributor");
			capabilities.setCapability("appActivity", "com.mindsarray.pay1distributor.SplashScreenActivity");
			driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			test.log(LogStatus.PASS, "Successfully Launched the Application");
		}catch (Exception e){
			System.out.println("Unable to open the Application");
			test.log(LogStatus.FAIL, "Unable to open the Application");
			e.printStackTrace();
		}
	}

	public void NavigatetoLogin() {
		try {
			if(new Utility(test, driver).isElementPresent("allow_xpath")){ test.log(LogStatus.INFO, "Navigated to application");
			driver.findElement(By.xpath("//android.widget.Button[@text='Allow']")).click(); test.log(LogStatus.INFO, "Clicking on Allow button");
			if(new Utility(test, driver).isElementPresent("menu_id")) {
				driver.findElement(By.id("com.mindsarray.pay1distributor:id/action_side_panel")).click();
				driver.findElement(By.xpath("//android.widget.TextView[@text='Login']")).click();
				NavigatetoLogin();
			}
			driver.findElement(By.xpath("//android.widget.Button[@text='Deny']")).click();  test.log(LogStatus.INFO, "Clicking on Deny button");
			Thread.sleep(2000);
			driver.findElement(By.xpath("//android.widget.Button[@text='Deny']")).click();  test.log(LogStatus.INFO, "Clicking on Deny button");
			Thread.sleep(2000);
			driver.findElement(By.xpath("//android.widget.Button[@text='Deny']")).click();  test.log(LogStatus.INFO, "Clicking on Deny button");
			Thread.sleep(2000);
			driver.findElement(By.xpath("//android.widget.Button[@text='Deny']")).click();  test.log(LogStatus.INFO, "Clicking on Deny button");
			}
			else if(new Utility(test, driver).isElementPresent("menu_id")) {
				driver.findElement(By.id("com.mindsarray.pay1distributor:id/action_side_panel")).click();
				driver.findElement(By.xpath("//android.widget.TextView[@text='Login']")).click();
				NavigatetoLogin();
			}
		}catch (Exception e) {
			System.out.println("Unable to check the options");
			test.log(LogStatus.ERROR, "Unable to navigate to Login Page");
			new Utility(test, driver).takeScreenShot("Unable to navigate to login page");
			e.printStackTrace();
		} 
	}	

	public void checkbal() {
		driver.findElement(By.id("com.mindsarray.pay1distributor:id/action_side_panel")).click();
		System.out.println(driver.findElement(By.id("com.mindsarray.pay1distributor:id/balanceView")));
	}
}
