package com.ChannelPatner.signup;

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

public class AddDistributor extends AppBase{
	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String msg = "";

	private AddDistributor(){
		try {
			prop = new Properties();
			FileInputStream fs = new FileInputStream(Constants.Properties_file_path); //Object repository path
			prop.load(fs);
			xls = new Excel_Reader(Constants.ChannelPatner); // Loading the Excel Sheet 
			rep = ExtentManager.getInstance(); // Getting the Extent Report Instance 
			test  = rep.startTest(this.getClass().getSimpleName()); //Starting the Extent Report test
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to run this class and create any instance");
		}
	}

	@BeforeTest()
	public void Testskip(){
		if (DataUtils.isSkip(xls, this.getClass().getSimpleName())){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Skip");
			skip = true;
			msg= "Skipping the test as runmode is N";
			rep.endTest(test);
			rep.flush();
			throw new SkipException("Skipping test case" + this.getClass().getSimpleName() + " as runmode set to NO in excel");
		}
	}

	@Test(dataProvider="getData")
	public void Adddistributors(Hashtable<String,String> data){
		count++;
		try {
			if (data.get("Runmode").equalsIgnoreCase("N")){
				test.log(LogStatus.SKIP, "Skipping the test as this set of data is set to N");
				skip = true;
				rep.endTest(test);
				rep.flush();
				throw new SkipException("Skipping the test as this set of data is set to N");
			}
			LaunchApp();
			NavigatetoLogin();
			Util = new Utility(test, driver);
			Thread.sleep(3000);
			driver.findElement(By.xpath(prop.getProperty("createAccount"))).click(); test.log(LogStatus.INFO, "Clicking on Create Account Button");
			driver.findElement(By.xpath(prop.getProperty("crtDistributor"))).click(); test.log(LogStatus.INFO, "Clicking on Retailer SignUp Button");
			driver.findElement(By.id(prop.getProperty("distributorName"))).sendKeys(data.get("Name")); test.log(LogStatus.INFO, "Entering the Distributor Name");
			driver.findElement(By.id(prop.getProperty("distMobileNum"))).sendKeys(data.get("Mobile")); test.log(LogStatus.INFO, "Entering the Mobile Number");
			driver.findElement(By.id(prop.getProperty("distEmail"))).sendKeys(data.get("Email")); test.log(LogStatus.INFO, "Entering the Email ID");
			driver.findElement(By.id(prop.getProperty("distFirmName"))).sendKeys(data.get("Firm Name")); test.log(LogStatus.INFO, "Entering the Firm Name");
			driver.findElement(By.xpath("//android.widget.TextView[@text='Select Current Business']")).click();
			driver.findElement(By.xpath("//android.widget.TextView[@text='Mobile/Mobile Accessories']")).click(); test.log(LogStatus.INFO, "Selecting Mobile Accessories as business");
			driver.findElement(By.id(prop.getProperty("distPincode"))).sendKeys(data.get("Pincode")); test.log(LogStatus.INFO, "Entering the Pincode Number");
			driver.findElement(By.xpath(prop.getProperty("confirmBtn"))).click(); test.log(LogStatus.INFO, "Clicking on Confirm button");	
			driver.findElement(By.xpath(prop.getProperty("enterOTp"))).sendKeys(data.get("Otp")); test.log(LogStatus.INFO, "Entering the Otp Number");	
			driver.findElement(By.xpath(prop.getProperty("ContinueBtn"))).click(); test.log(LogStatus.INFO, "Confirming the otp number");
			Util.takeScreenShot("Successfully navigated to Otp screen");
			test.log(LogStatus.PASS, "Successfully navigated to Otp Screen"); msg ="Successfully navigated to Otp Screen"; fail = false;
		} catch (InterruptedException e){
			// TODO Auto-generated catch block
			test.log(LogStatus.ERROR, "Issue while creating a distributor");
			msg="Issue while adding remitter"; 
			Util.takeScreenShot(msg);
			e.printStackTrace();
		}
	}

	@AfterMethod
	public void reporter(){
		if(fail){
			DataUtils.reportDataSetResult(xls, this.getClass().getSimpleName(), count+2, "Fail");
			DataUtils.reportDataSetActualResult(xls, this.getClass().getSimpleName(),count+2, msg);
		}else{
			isTestPass=true;
			DataUtils.reportDataSetResult(xls, this.getClass().getSimpleName(), count+2, "Pass");
			DataUtils.reportDataSetActualResult(xls, this.getClass().getSimpleName(),count+2, msg);
		}
		if(skip)
			DataUtils.reportDataSetResult(xls, this.getClass().getSimpleName(), count+2, "Skip");
		skip=false;
		fail=true;
	}

	@AfterTest
	public void reportTestResult(){
		if(skip)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Skip");
		if(isTestPass)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Pass");
		else
			DataUtils.reportDataSetResult(xls, "TestCase", DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Fail");
		isTestPass = false;
		rep.endTest(test);
		rep.flush();
	}

	@DataProvider
	public Object[][] getData(){
		return DataUtils.getData(xls, this.getClass().getSimpleName(),this.getClass().getSimpleName());
	}
}