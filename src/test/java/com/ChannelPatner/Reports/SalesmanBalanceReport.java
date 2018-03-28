package com.ChannelPatner.Reports;

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

import com.ChannelPatner.login.DistributorLogin;
import com.base.AppBase;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.DataUtils;
import Utility.Excel_Reader;
import Utility.ExtentManager;
import Utility.Utility;
import io.appium.java_client.TouchAction;

public class SalesmanBalanceReport extends AppBase{

	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String msg = "";
	public String testName ;

	private SalesmanBalanceReport(){
		try {
			testName = this.getClass().getSimpleName();
			prop = new Properties();
			FileInputStream fs = new FileInputStream(Constants.Properties_file_path); //Object repository path
			prop.load(fs);
			xls = new Excel_Reader(Constants.ChannelPatner); // Loading the Excel Sheet 
			rep = ExtentManager.getInstance(); // Getting the Extent Report Instance 
			test  = rep.startTest(testName); //Starting the Extent Report test
		} catch (Exception e) {
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
	public void retialertransfer(Hashtable<String,String> data) {
		count++;
		try {
			if (data.get("Runmode").equalsIgnoreCase("N")){
				test.log(LogStatus.SKIP, "Skipping the test as this set of data is set to N");
				skip = true;
				rep.endTest(test);
				rep.flush();
				throw new SkipException("Skipping the test as this set of data is set to N");
			}
			this.driver = new DistributorLogin().getLogin(data);
			Util = new Utility(test, driver);
			Thread.sleep(3000);
			if(Util.isElementPresent("backbtn_xpath"))
				driver.findElement(By.xpath(prop.getProperty("backbtn_xpath"))).click();	
			driver.findElement(By.xpath(prop.getProperty("reportretailer"))).click();
			/*			driver.findElement(By.xpath(prop.getProperty("retailernum"))).sendKeys(data.get("Number"));
			Thread.sleep(2000);
			new TouchAction(driver).tap(400, 490).perform().release();*/
			System.out.println(driver.findElement(By.id(prop.getProperty("fetchretailnum"))).getText());
			System.out.println(data.get("Number"));
			if((driver.findElement(By.xpath(prop.getProperty("fetchsalenum"))).getText()).contains(data.get("Number"))) {
				msg = "Retailer Number matched";
				if((driver.findElement(By.id(prop.getProperty("fetchretailamo"))).getText()).contains(data.get("transferamount"))){
					msg = "Retailer Amount matched";
					driver.findElement(By.id(prop.getProperty("pullbackbtn"))).click();new Utility(test, driver).takeScreenShot(msg);
					new TouchAction(driver).tap(400, 1100).perform().release();
					new Utility(test, driver).takeScreenShot(msg);test.log(LogStatus.PASS, msg); fail = false;
				}else {
					msg = "Retailer Amount mismatched";
					new Utility(test, driver).takeScreenShot(msg);test.log(LogStatus.ERROR, msg); fail = true;
				}
			}else {
				msg = "Retailer Nummber Mismatched";
				new Utility(test, driver).takeScreenShot(msg);test.log(LogStatus.ERROR, msg); fail = true;
			}
		} catch (InterruptedException e) {
			msg = "Unable to fetch retailer Balance";
			DataUtils.reportError(xls, testName, count+2, msg);
			fail = true;
			test.log(LogStatus.ERROR, msg);
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
			DataUtils.reportDataSetActualResult(xls, testName, count+2, msg);
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
}