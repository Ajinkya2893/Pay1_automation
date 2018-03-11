package Utility;

public class Constants {

	//Properties file path
	public static final String Properties_file_path = System.getProperty("user.dir")+"/src/test/resources/ChannelPatner.properties";

	//ExcelSheets paths 
	public static final String ChannelPatner = System.getProperty("user.dir")+"//ExcelSheet//RetailerChannel.xlsx";
	
	
	//All the Excel Related Values
	public static final String KEYWORDS_SHEET = "Keywords";
	
	public static final String TCID_COL = "TCID";
	public static final String KEYWORD_COL = "Keywords";
	public static final String OBJECT_COL = "Object";
	public static final String DATA_COL = "Data";
	public static final String RESULTS_COL = "Result";
	
	public static final String TESTCASES_SHEET = "TestCase";
	public static final String RUNMODE_COL = "Runmode";
	public static final String DESCRIPTION_COL = "Description";
	public static final String SCREENSHOT_PATH = System.getProperty("user.dir")+"//Screenshots//";
	public static final String REPORT_PATH = System.getProperty("user.dir")+"//Reports//";
	public static final String PASS = "PASS";
	public static final String FAIL = "FAIL";
	
}
