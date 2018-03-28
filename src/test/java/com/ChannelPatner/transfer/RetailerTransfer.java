package com.ChannelPatner.transfer;

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

public class RetailerTransfer extends AppBase{

	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String msg = "";
	public String testName ;

	private RetailerTransfer(){
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
			driver.findElement(By.xpath(prop.getProperty("baltransfer_xpath"))).click(); test.log(LogStatus.INFO, "Clicking on Transfer button");
			driver.findElement(By.xpath(prop.getProperty("retailer"))).click();  test.log(LogStatus.INFO, "Selecting retailer option");
			driver.findElement(By.xpath(prop.getProperty("retailerName"))).sendKeys(data.get("Number"));  test.log(LogStatus.INFO, "Entering the retailer number");
			Thread.sleep(2000);
			new TouchAction(driver).tap(400, 517).perform().release();  test.log(LogStatus.INFO, "Selecting the first retailer");
			driver.findElement(By.xpath(prop.getProperty("balAmount"))).sendKeys(data.get("transferamount"));  test.log(LogStatus.INFO, "Entering ampunt to transfer");
			driver.findElement(By.xpath(prop.getProperty("addNote"))).sendKeys(data.get("Note"));  test.log(LogStatus.INFO, "Entering the Note");
 			driver.hideKeyboard();
			driver.findElement(By.xpath(prop.getProperty("confirmButton"))).click();  test.log(LogStatus.INFO, "Clicking on Confirm Button");
 			Thread.sleep(2000);
 			msg = "Successfully transfered the Amount";
 			Util.takeScreenShot(msg);test.log(LogStatus.PASS, msg);
 			msg = driver.findElement(By.id("com.mindsarray.pay1distributor:id/message")).getText();
 			test.log(LogStatus.PASS, msg);fail = false;
		} catch (InterruptedException e) {
			msg = "Unable to transfer amount";
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
		driver.quit();
	}

	@DataProvider
	public Object[][] getData(){
		return DataUtils.getData(xls, testName,testName);
	}
}