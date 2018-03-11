package com.ChannelPatner.login;

import java.io.FileInputStream;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.base.AppBase;

import Utility.Constants;
import Utility.Excel_Reader;
import Utility.ExtentManager;
import io.appium.java_client.TouchAction;

public class test extends AppBase{
	
	public test() {
		try {
			prop = new Properties();
			FileInputStream fs = new FileInputStream(Constants.Properties_file_path); //Object repository path
			prop.load(fs);
			xls = new Excel_Reader(Constants.ChannelPatner); // Loading the Excel Sheet 
			rep = ExtentManager.getInstance(); // Getting the Extent Report Instance 
			test  = rep.startTest(this.getClass().getSimpleName()); //Starting the Extent Report test
			System.out.println();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to run this class and create any instance");
		}
	}
	
	@Test
	public void check() {
		LaunchApp();
		NavigatetoLogin();
		//login();
		checkbal();
	}

	@Test(priority=2)
	public void transfermoney(){
		driver.findElement(By.xpath(prop.getProperty("baltransfer"))).click();
		driver.findElement(By.xpath(prop.getProperty("salesman"))).click();
		driver.findElement(By.xpath(prop.getProperty("retailerName"))).sendKeys("7101000521");
		new TouchAction(driver).tap(70, 500).perform().release();
		driver.findElement(By.xpath(prop.getProperty("balAmount"))).sendKeys("5");
		driver.findElement(By.xpath(prop.getProperty("addNote"))).sendKeys("test");
		driver.findElement(By.xpath(prop.getProperty("confirmButton"))).click();
	}
	
	@Test(priority=3)
	public void createRetailer() throws InterruptedException {
		driver.findElement(By.xpath(prop.getProperty("createRetailer"))).click();
		driver.findElement(By.xpath(prop.getProperty("retailerNu"))).sendKeys("");
		driver.findElement(By.xpath(prop.getProperty("retailerShop"))).sendKeys("");
		driver.findElement(By.xpath(prop.getProperty("createretail"))).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(prop.getProperty("otp"))).sendKeys("");
		driver.findElement(By.xpath(prop.getProperty("conf"))).click();
	}
	
	@Test(priority=4)
	public void retailerlist() {
		driver.findElement(By.xpath(prop.getProperty(""))).click();
		driver.findElement(By.xpath(prop.getProperty(""))).sendKeys("");
		System.out.println(driver.findElement(By.xpath(prop.getProperty("shopname"))));
		System.out.println(driver.findElement(By.xpath(prop.getProperty("mobileno"))));
		driver.findElement(By.xpath(prop.getProperty("edit"))).click();
		
	}
}
