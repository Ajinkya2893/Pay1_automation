package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Utility {

	public static WebDriver driver;
	public Properties prop;
	protected ExtentTest test;

	public Utility(ExtentTest test,WebDriver driver){
		this.test=test;
		Utility.driver=driver;

		prop = new Properties();
		try {
			FileInputStream fs = new FileInputStream(Constants.Properties_file_path);
			prop.load(fs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/***************************Validation keywords*********************************/
	public String verifyText(String locatorKey,String expectedText){
		try {
			WebElement e = getElement(locatorKey);
			String actualText = e.getText();
			System.out.println(actualText +"====="+ expectedText);
			if(actualText.equals(expectedText))
				return Constants.PASS;
			else
				return Constants.FAIL;
		}catch (Exception e) {
			e.printStackTrace();
			return Constants.FAIL;
		}
	}

	public String verifyElementPresent(String locatorKey){
		try{
			boolean result= isElementPresent(locatorKey);
			if(result)
				return Constants.PASS;
			else
				return Constants.FAIL+" - Could not find Element "+ locatorKey;
		}
		catch(Exception e) {
			e.printStackTrace();
			return Constants.FAIL;
		}
	}


	public String verifyElementNotPresent(String locatorKey){
		try{
			boolean result= isElementPresent(locatorKey);
			if(!result)
				return Constants.PASS;
			else
				return Constants.FAIL+" - Could find Element "+ locatorKey;
		}
		catch(Exception e) {
			e.printStackTrace();
			return Constants.FAIL;
		}
	}

	public String scrollTo(String xpathKey){
		int y = getElement(xpathKey).getLocation().y;
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("window.scrollTo(0,"+(y-200)+")");
		return Constants.PASS; 
	}
	public String wait(String timeout) {

		try {
			Thread.sleep(Integer.parseInt(timeout));
		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Constants.PASS;
	}

	/************************Utility Functions********************************/
	public WebElement getElement(String locatorKey){
		WebElement e = null;
		try{
			if(locatorKey.endsWith("_id"))
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			else if(locatorKey.endsWith("_xpath"))
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			else if(locatorKey.endsWith("_name"))
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));
		}catch(Exception ex){
			reportFailure("Failure in Element Extraction - "+ locatorKey);
			Assert.fail("Failure in Element Extraction - "+ locatorKey);
		}

		return e;

	}

	public boolean isElementPresent(String locatorKey){
		try {
			List<WebElement> e = null;

			if(locatorKey.endsWith("_id"))
				e = driver.findElements(By.id(prop.getProperty(locatorKey)));
			else if(locatorKey.endsWith("_xpath")){
				e = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
			}
			else if(locatorKey.endsWith("_name"))
				e = driver.findElements(By.name(prop.getProperty(locatorKey)));

			if(e.size()==0){
				//System.out.println("Element not found");
				return false;
			}
			else {
				//System.out.println("Element found");
				return true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/******************************Reporting functions******************************/

	public void reportFailure(String failureMessage){
		try{
			takeScreenShot(failureMessage);
			test.log(LogStatus.FAIL,failureMessage);
		}
		catch(Exception e) {
			e.printStackTrace();

		}
	}
	public String takeScreenShot(String message){
		// decide name - time stamp
		Date d = new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ","_")+".png";
		String path=Constants.SCREENSHOT_PATH+screenshotFile;
		// take screenshot
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Constants.FAIL;
		}
		test.log(LogStatus.INFO, message+" "+test.addScreenCapture(path));
		return Constants.PASS;
	}

	public void Wait(String locatorKey) {
		try{
			WebDriverWait wait = new WebDriverWait(driver, 25);
			
			if(locatorKey.endsWith("_id"))
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorKey)));
			else if(locatorKey.endsWith("_xpath"))
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorKey)));
			else if(locatorKey.endsWith("_name"))
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorKey)));
		}catch(Exception ex){
			ex.printStackTrace();
			//Assert.fail("Failure in Element Extraction - "+ locatorKey);
		}
	}

}
