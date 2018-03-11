package com.ChannelPatner.login;

import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.base.AppBase;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.DataUtils;
import Utility.Excel_Reader;
import Utility.ExtentManager;
import Utility.Utility;
import io.appium.java_client.android.AndroidDriver;

public class DistributorLogin extends AppBase{
	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String msg = "";
	public String testName ;

	public DistributorLogin(){
		try {
			testName = this.getClass().getSimpleName();
			prop = new Properties();
			FileInputStream fs = new FileInputStream(Constants.Properties_file_path); //Object repository path
			prop.load(fs);
			xls = new Excel_Reader(Constants.ChannelPatner); // Loading the Excel Sheet 
			rep = ExtentManager.getInstance(); // Getting the Extent Report Instance 
			test  = rep.startTest(testName); //Starting the Extent Report test
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to run this class and create any instance");
		}
	}

	@BeforeTest()
	public void Testskip(){
		if (DataUtils.isSkip(xls, testName)){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,testName), "Skip");
			skip = true;
			msg= "Skipping the test as runmode is N";
			rep.endTest(test);
			rep.flush();
			throw new SkipException("Skipping test case" + testName + " as runmode set to NO in excel");
		}
	}

	@Test(dataProvider="getData")
	public void login(Hashtable<String,String> data) {
		try {
			this.driver=getLogin(data);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterMethod
	public void reporter(){
		if(fail){
			DataUtils.reportDataSetResult(xls, testName, count+2, "Fail");
			DataUtils.reportDataSetActualResult(xls, testName,count+2, msg);
		}else{
			isTestPass=true;
			DataUtils.reportDataSetResult(xls, testName, count+2, "Pass");
			DataUtils.reportDataSetActualResult(xls, testName,count+2, msg);
		}
		if(skip)
			DataUtils.reportDataSetResult(xls, testName, count+2, "Skip");
		skip=false;
		fail=true;
	}

	@AfterTest
	public void reportTestResult(){
		if(skip)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,testName), "Skip");
		if(isTestPass)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,testName), "Pass");
		else
			DataUtils.reportDataSetResult(xls, "TestCase", DataUtils.getRowNum(xls,testName), "Fail");
		isTestPass = false;
		rep.endTest(test);
		rep.flush();
	}

	@DataProvider
	public Object[][] getData(){
		return DataUtils.getData(xls, testName,testName);
	}

	@SuppressWarnings("rawtypes")
	public AndroidDriver getLogin(Hashtable<String,String> data){
		count++;
		if (data.get("Runmode").equalsIgnoreCase("N")){
			msg="Skipping the test as this set of data is set to N";
			test.log(LogStatus.SKIP, msg);
			skip = true;
			rep.endTest(test);
			rep.flush();
			throw new SkipException(msg);
		}
		try {
			LaunchApp();
			NavigatetoLogin();
			new Utility(test, driver).takeScreenShot("Landed on login page");
			driver.findElement(By.xpath("//android.widget.EditText[@text='Mobile Number']")).sendKeys(data.get("Username")); test.log(LogStatus.INFO, "Entered the username");
			driver.findElement(By.xpath("//android.widget.EditText[@text='PIN']")).sendKeys(data.get("Password")); test.log(LogStatus.INFO, "Entered the Password");
			driver.findElement(By.xpath("//android.widget.Button[@text='Login']")).click(); test.log(LogStatus.INFO, "Clicked on Login button");
			fail =false;msg ="Successfully fetched the bill"; test.log(LogStatus.PASS, msg);
			new Utility(test, driver).takeScreenShot("Successfully logged in");
		} catch (RuntimeException e){
			// TODO Auto-generated catch block
			test.log(LogStatus.ERROR, "Issue while creating a session");
			msg="Issue while creating a session";
			e.printStackTrace();
		}
		return driver;
	}
}

