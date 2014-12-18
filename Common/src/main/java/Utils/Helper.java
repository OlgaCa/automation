package Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Random;

import org.openqa.selenium.JavascriptExecutor;

import Utils.Element.How;

import com.google.common.base.CharMatcher;

public class Helper {

	public enum FileType {
		recon, refund, settlement, massInvoice, adminRecon, bankSettledRecon, incompleteEsscom,
		incompleteCitinodal, uploadTDR, invalidTDR, reconTDR1, reconTDR2, esscomSettlement, GetReconData,
		citinodalSettlement, merchantSummary, TIDUTR, invalidTIDUTR, massInvoiceInvalid, initiatedSettlement, FinalCumulativeSettlement,statusPan,
		statusPennySentToBank, statusUtr, royalPalmFile, kamal3File, compulsiveWebfrontFile,webfrontUploadFile;
	};




	/**
	 * @param phpResponse PHP Response Array returned by some transaction or webservice
	 * @return Key-Value pairs of the response array
	 */
	public static Hashtable<String, String> convertPhpArrayResponseToJavaList(String phpResponse)
	{
		Hashtable<String, String> response = null;

		if(phpResponse != null && !phpResponse.isEmpty())
		{
			response = new Hashtable<String, String>();

			//Remove the starting '('
			phpResponse = phpResponse.substring(phpResponse.indexOf("(") + 1);

			//Remove the trailing ')'
			phpResponse = phpResponse.substring(0, phpResponse.lastIndexOf(")"));

			//Split the key value pairs
			String [] responseArray = phpResponse.split("\\[");
			for (String pair:responseArray)
			{
				String [] keyValue = pair.split("] =>");
				if(keyValue.length == 2)
				{
					if(!keyValue[1].trim().startsWith("Array"))
					{
						response.put(keyValue[0].trim(), keyValue[1].trim());
					}
					else //the value is another Array
					{
						int subArrayStart = phpResponse.indexOf("=> Array");
						String value = phpResponse.substring(subArrayStart + 2);
						response.put(keyValue[0].trim(), value);
						break;
					}
				}
			}
		}

		return response;
	}
	/**
	 * @param phpResponse Ist PHP Response Array returned by get multiple user card  webservice
	 * @return Key-Value pairs of the Ist response array
	 */
	public static Hashtable<String, String> convertIstPhpGetUserCardResponseToJavaList(String phpResponse)
	{
		Hashtable<String, String> response = null;

		if(phpResponse != null && !phpResponse.isEmpty())
		{
			response = new Hashtable<String, String>();

			//Remove the starting '('
			phpResponse = phpResponse.substring(phpResponse.indexOf("(") + 1);

			//Remove the trailing ')'
			phpResponse = phpResponse.substring(0, phpResponse.indexOf(")") - 2);

			//Split the key value pairs
			String [] responseArray = phpResponse.split("\\[");
			for (String pair:responseArray)
			{
				String [] keyValue = pair.split("] =>");
				if(keyValue.length == 2)
				{
					if(!keyValue[1].trim().startsWith("Array"))
					{
						response.put(keyValue[0].trim(), keyValue[1].trim());
					}
					else //the value is another Array
					{
						int subArrayStart = phpResponse.indexOf("=> Array");
						String value = phpResponse.substring(subArrayStart + 2);
						response.put(keyValue[0].trim(), value);
						break;
					}
				}
			}
		}

		return response;
	}

	/**
	 * @param phpResponse 2nd PHP Response Array returned by get multiple user card  webservice
	 * @return Key-Value pairs of the response array
	 */
	public static Hashtable<String, String> convert2ndPhpArrayGetUserCardToJavaList(String phpResponse)
	{
		Hashtable<String, String> response = null;
		Hashtable<String, String> nextresponse = null;

		if(phpResponse != null && !phpResponse.isEmpty())
		{
			response = new Hashtable<String, String>();

			//Remove the starting '('
			phpResponse = phpResponse.substring(phpResponse.indexOf("(") + 1);

			//Remove the trailing ')'
			phpResponse = phpResponse.substring(0, phpResponse.lastIndexOf(")"));

			//Split the key value pairs
			String [] responseArray = phpResponse.split("\\[");
			for (String pair:responseArray)
			{
				String [] keyValue = pair.split("] =>");
				if(keyValue.length == 2)
				{
					if(!keyValue[1].trim().startsWith("Array"))
					{
						response.put(keyValue[0].trim(), keyValue[1].trim());
					}
					else //the value is another Array
					{
						//Remove the starting '('
						nextresponse = new Hashtable<String, String>();
						phpResponse = phpResponse.substring(phpResponse.indexOf("(") + 1);

						responseArray = phpResponse.split("\\[");
						for (String nextpair:responseArray)
						{
							keyValue = nextpair.split("] =>");
							if(keyValue.length == 2)
							{
								if(!keyValue[1].trim().startsWith("Array"))
								{
									nextresponse.put(keyValue[0].trim(), keyValue[1].trim());
								}

							}
							else 
								keyValue = nextpair.split("] =>");
						}
						return nextresponse;
					}
				}
			}
		}

		return nextresponse;

	}

	/**
	 * @param phpResponse PHP Response Array returned by getEmiAmountAccordingToInterest webservice
	 * @return Key-Value pairs of the response array
	 */
	public static Hashtable<String, String> convertgetEmiAmountResponseToJavaList(String phpResponse, String pg_id, String ibibo_code)
	{
		Hashtable<String, String> response = null;

		if(phpResponse != null && !phpResponse.isEmpty())
		{
			response = new Hashtable<String, String>();

			//Remove the starting '('
			phpResponse = phpResponse.substring(phpResponse.indexOf("(") + 1);

			//Remove the trailing ')'
			phpResponse = phpResponse.substring(0, phpResponse.lastIndexOf(")"));

			//Get the value of [pg_id]
			phpResponse = phpResponse.substring(phpResponse.indexOf("[" + pg_id + "]") + 1);

			//Remove the starting '('
			phpResponse = phpResponse.substring(phpResponse.indexOf("(") + 1);

			//Remove the trailing ')'
			phpResponse = phpResponse.substring(0, phpResponse.lastIndexOf(")"));

			//Get the value of [ibibo_code]
			phpResponse = phpResponse.substring(phpResponse.indexOf("[" + ibibo_code + "]") + 1);

			//Remove the starting '('
			phpResponse = phpResponse.substring(phpResponse.indexOf("(") + 1);

			//Remove the trailing ')'
			phpResponse = phpResponse.substring(0, phpResponse.indexOf(")"));

			//Split the key value pairs
			String [] responseArray = phpResponse.split("\\[");
			for (String pair:responseArray)
			{
				String [] keyValue = pair.split("] =>");
				if(keyValue.length == 2)
				{
					if(!keyValue[1].trim().startsWith("Array"))
					{
						response.put(keyValue[0].trim(), keyValue[1].trim());
					}
					else //the value is another Array
					{
						int subArrayStart = phpResponse.indexOf("=> Array");
						String value = phpResponse.substring(subArrayStart + 2);
						response.put(keyValue[0].trim(), value);
						break;
					}
				}
			}
		}

		return response;
	}

	/**
	 * Replaces the arguments like {$someArg} present in input string with its value from RuntimeProperties
	 * @param input string in which some Argument is present
	 * @return replaced string
	 */
	public static String replaceArgumentsWithRunTimeProperties(Config testConfig, String input)
	{
		if(input.contains("{$")){
			int index=input.indexOf("{$");
			input.length();
			input.indexOf("}",index+2);
			String key=input.substring(index+2,input.indexOf("}",index+2));
			String value= testConfig.getRunTimeProperty(key);

			input=input.replace("{$"+key+"}", value);
			return replaceArgumentsWithRunTimeProperties(testConfig,input);
		}
		else{
			return input;
		}

	}

	/**
	 * Generate a random Alpha-Numeric string of given length
	 * @param length Length of string to be generated
	 */
	public static String generateRandomAlphaNumericString(int length)
	{
		Random rd = new Random();
		String aphaNumericString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder(length);

		for( int i = 0; i < length; i++ ) 
		{
			sb.append( aphaNumericString.charAt( rd.nextInt(aphaNumericString.length()) ) );
		}

		return sb.toString();
	}

	/**
	 * Generate a random Alphabets string of given length
	 * @param length Length of string to be generated
	 */
	public static String generateRandomAlphabetsString(int length)
	{
		Random rd = new Random();
		String aphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder(length);

		for( int i = 0; i < length; i++ ) 
		{
			sb.append( aphaNumericString.charAt( rd.nextInt(aphaNumericString.length()) ) );
		}

		return sb.toString();
	}

	/**
	 * Generate a random number of given length
	 * @param length Length of number to be generated
	 * @return
	 */
	public static long generateRandomNumber(int length)
	{
		long randomNumber = 1;
		int retryCount = 1;

		//retryCount added for generating specified length's number
		while(retryCount>0){
			String strNum = Double.toString(Math.random());
			strNum = strNum.replace(".","");

			if(strNum.length() > length)
			{
				strNum = strNum.substring(0,length);
			}
			else
			{
				int remainingLength = length - strNum.length() + 1;
				randomNumber = generateRandomNumber(remainingLength);
				strNum = strNum.concat(Long.toString(randomNumber));
			}

			randomNumber=Long.parseLong(strNum);

			if(String.valueOf(randomNumber).length()<length){
				retryCount++;
			}
			else{
				retryCount =0;
			}
			
		}

		return randomNumber;
	}

	public static void compareEquals(Config testConfig, String what, String expected, String actual)
	{
		if(expected == null & actual == null) 
		{
			testConfig.logPass(what, actual);
			return;
		}

		if(actual != null)
		{
			if(!actual.equals(expected)){
				testConfig.logFail(what,expected, actual);
			}
			else{
				testConfig.logPass(what, actual);
			}
		}
		else
		{
			testConfig.logFail(what,expected, actual);
		}
	}

	public static void compareContains(Config testConfig, String what, String expected, String actual)
	{
		actual=actual.trim();
		if(actual != null)

		{
			if(!actual.contains(expected.trim())){
				testConfig.logFail(what,expected, actual);
			}
			else{
				testConfig.logPass(what, actual);
			}
		}
		else
		{
			testConfig.logFail(what,expected, actual);
		}
	}


	public static void compareTrue(Config testConfig, String what, boolean actual) 
	{
		if(!actual)
		{
			testConfig.logFail("Failed to verify " + what);
		}
		else
		{
			testConfig.logPass("Verified " + what);
		}
	}

	public static void compareEquals(Config testConfig, String what, int expected, int actual)
	{
		if(actual!=expected){
			testConfig.logFail(what,expected, actual);
		}
		else{
			testConfig.logPass(what, actual);
		}
	}

	public static void compareEquals(Config testConfig, String what, float expected, float actual)
	{
		if(actual!=expected){
			testConfig.logFail(what,expected, actual);
		}
		else{
			testConfig.logPass(what, actual);
		}
	}




	public static String getCurrentDateTime(String format)
	{
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter= new SimpleDateFormat(format);
		String dateNow = formatter.format(currentDate.getTime());
		return dateNow;
	}

	public static String getCurrentDate(String format)
	{
		//get current date
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}

	// get current time in given format
	public static String getCurrentTime(String format)
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter= new SimpleDateFormat(format);
		String currentTime = formatter.format(cal.getTime());

		return currentTime;
	}

	/** 
	 * This utility method returns Date in readable format. Converts from Unix Format.
	 * @param unixformat - unix format to be converted
	 * @param format - Format in which the date will be returned 
	 * @return 
	 */  
	// Convert to readable format from Unixdatetime stamp
	public static String convertDatefromUnix(long unixformat,String format)
	{
		Date date = new Date(unixformat);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String convertedDate = formatter.format(date);
		return convertedDate;
	}
	/** 
	 * This utility method returns a future or past date after/before number of days. 
	 * @param days
	 * @param format sample format yyyy-MM-dd 
	 * @return 
	 */  
	public static String getDateBeforeOrAfterDays(int days, String format) {  
		Date tomorrow = new Date(); 
		DateFormat dateFormat = new SimpleDateFormat(format);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		tomorrow = cal.getTime();

		return dateFormat.format(tomorrow);  
	}  


	public static String getExcelFile(Config testConfig, FileType fileType)
	{	
		String path = null;
		switch (fileType) {
		case recon:
			path = testConfig.getRunTimeProperty("ReconDataSheet");
			path = System.getProperty("user.dir") + path;
			break;

		case refund:
			path = testConfig.getRunTimeProperty("RefundDataSheet");
			path = System.getProperty("user.dir") + path;
			break;

		case settlement:
			path = testConfig.getRunTimeProperty("SettlementDataSheet");
			path = System.getProperty("user.dir") + path;
			break;

		case massInvoice:
			path = testConfig.getRunTimeProperty("massInvoiceValidDataSheet");
			path = System.getProperty("user.dir") + path;
			break;

		case adminRecon:
			path = testConfig.getRunTimeProperty("AdminRecon");
			path = System.getProperty("user.dir") + path;
			break;

		case bankSettledRecon:
			path = testConfig.getRunTimeProperty("SettledReconFile");
			path = System.getProperty("user.dir") + path;
			break;

		case incompleteEsscom:
			path = testConfig.getRunTimeProperty("Esscom2");
			path = System.getProperty("user.dir") + path;
			break;

		case incompleteCitinodal:
			path = testConfig.getRunTimeProperty("CitiBankNodal_invalid1");
			path = System.getProperty("user.dir") + path;
			break;

		case uploadTDR:
			path = testConfig.getRunTimeProperty("TDRupload1");
			path = System.getProperty("user.dir") + path;
			break;

		case invalidTDR:
			path = testConfig.getRunTimeProperty("TDRupload2");
			path = System.getProperty("user.dir") + path;
			break;

		case reconTDR1:
			path = testConfig.getRunTimeProperty("TDRrecon1");
			path = System.getProperty("user.dir") + path;
			break;

		case reconTDR2:
			path = testConfig.getRunTimeProperty("TDRrecon2");
			path = System.getProperty("user.dir") + path;
			break;

		case esscomSettlement:
			path = testConfig.getRunTimeProperty("EsscomSettlementDataSheet");
			path = System.getProperty("user.dir") + path;
			break;

		case GetReconData:
			path = testConfig.getRunTimeProperty("GetReconDataFile");
			path = System.getProperty("user.dir") + path;
			break;

		case citinodalSettlement:
			path = testConfig.getRunTimeProperty("CitiBankNodalFile");
			path = System.getProperty("user.dir") + path;
			break;

		case merchantSummary:
			path = testConfig.getRunTimeProperty("MerchantSummaryFile");
			path = System.getProperty("user.dir") + path;
			break;

		case TIDUTR:
			path = testConfig.getRunTimeProperty("TIDUTRFile");
			path = System.getProperty("user.dir") + path;
			break;

		case invalidTIDUTR:
			path = testConfig.getRunTimeProperty("invalidTIDUTRFile");
			path = System.getProperty("user.dir") + path;
			break;

		case massInvoiceInvalid:
			path = testConfig.getRunTimeProperty("massInvoiceInvalidSheet");
			path = System.getProperty("user.dir") + path;
			break;

		case initiatedSettlement:
			path = testConfig.getRunTimeProperty("InitiatedSettlementFile");
			path = System.getProperty("user.dir") + path;
			break;

		case FinalCumulativeSettlement:
			path = testConfig.getRunTimeProperty("FinalCumulativeSettlementFile");
			path = System.getProperty("user.dir") + path;
			break;

		case statusPan:
			path = testConfig.getRunTimeProperty("StatusPan");
			path = System.getProperty("user.dir") + path;
			break;

		case statusPennySentToBank:
			path = testConfig.getRunTimeProperty("StatusPennySentToBank");
			path = System.getProperty("user.dir") + path;
			break;

		case statusUtr:
			path = testConfig.getRunTimeProperty("StatusUtr");
			path = System.getProperty("user.dir") + path;
			break;

		case royalPalmFile:
			path = testConfig.getRunTimeProperty("RoyalPalm");
			path = System.getProperty("user.dir") + path;
			break;	

		case kamal3File:
			path = testConfig.getRunTimeProperty("kamal3File");
			path = System.getProperty("user.dir") + path;
			break;	

		case compulsiveWebfrontFile:
			path = testConfig.getRunTimeProperty("compulsiveWebfrontFile");
			path = System.getProperty("user.dir") + path;
			break;	

		case webfrontUploadFile:
			path = testConfig.getRunTimeProperty("webfrontUploadFile");
			path = System.getProperty("user.dir") + path;
			break;
		default:
			break;

		}

		String datetime = Helper.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		System.out.println("datetime="+datetime);
		String newFileName = datetime + ".xls";
		newFileName = CharMatcher.is(':').removeFrom(newFileName);

		String newFilePath = "C:\\Users\\"+System.getProperty("user.name")+"\\Downloads\\"+newFileName;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		OutputStream os = null;
		try {
			os = new FileOutputStream(newFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			byte[] buffer = new byte[1024];

			int length;
			while ((length = fis.read(buffer)) > 0){
				os.write(buffer, 0, length);
			}

			if (fis != null)fis.close();
			if (os != null)os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		//CopyUtils.copy(path, "C://Users//"+System.getProperty("user.name")+"//Downloads//"+newFileName);

		//	Helper.createFileReplica("c://PayU//Product"+path, "C://Users//"+System.getProperty("user.name")+"//Downloads//"+newFileName);

		return newFilePath;
	}



	public static String getDatePreviousTo(int dd,int mm,int yyyy, String format) {  
		Calendar date = new GregorianCalendar(yyyy,mm-1,dd);
		date.add(Calendar.DAY_OF_YEAR, -1);
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date.getTime());  
	}  

	public static String getDate(int dd,int mm,int yyyy, String format) {  
		Calendar date = new GregorianCalendar(yyyy,mm-1,dd);
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date.getTime());  
	}  

	public static void compareExcelEquals(Config testConfig, String what, String expected, String actual)
	{
		if (actual != null) {
			if (!actual.equals("{skip}")) {
				if (!actual.equals(expected)) {
					if (expected.equals("")){
						testConfig.logPass(what, actual);
					}
					else{
						testConfig.logFail(what, expected, actual);
					}
				} else {
					testConfig.logPass(what, actual);
				}
			} else {
				testConfig.logWarning("Skipping Verification of " + what
						+ " as " + actual);
			}
		} else

		{
			testConfig.logFail(what, expected, actual);
		}
	}
	/**
	 * Executes the specified cron.php file
	 * 
	 * @param cronName
	 */
	public static void executeCron(Config testConfig,String cronFileName) {
		// Save the current URL
		String currentUrl = testConfig.driver.getCurrentUrl();

		// GO to cron URL
		String cronUrl = testConfig.getRunTimeProperty("CronUrl");
		Browser.navigateToURL(testConfig, cronUrl);
		Element.click(testConfig,
				Element.getPageElement(testConfig, How.linkText, cronFileName),
				"Executing the cron " + cronFileName);
		Browser.wait(testConfig, 5);

		// Restore the original URL
		Browser.navigateToURL(testConfig, currentUrl);
	}
	/**
	 * @param testConfig
	 * @param what
	 * @param expected This value must be value having more than 2 digits after decimal
	 * @param actual
	 */
	public static void compareValues(Config testConfig, String what, String expected, String actual)
	{
		if(expected == null & actual == null) 
		{
			testConfig.logPass(what, actual);
			return;
		}

		if(actual != null)
		{
			String[] expectedValue = expected.split(".");
			expected = expectedValue[1];
			expected = expected.substring(0, 2);
			String[] actualValue = actual.split(".");
			actual = actualValue[1];
			expected = String.valueOf(expectedValue);
			if(!actual.equals(expected)){
				testConfig.logFail(what,expected, actual);
			}else{
				testConfig.logPass(what, actual);
			}
		}
		else
		{
			testConfig.logFail(what,expected, actual);
		}
	}


	/**
	 * 	
	 * @param WRpath is the file path from whose replica has to be created 
	 * @param WBwrite output file path
	 * @return
	 */
	//Not in use anymore
	/*	private static String createFileReplica(String WRpath, String WBwrite)
	{
		String SSRead = "Sheet1" ;
		String SSwrite = "Sheet1" ;

		try{
			String[][] repwr = null;
			FileOutputStream fileout = new FileOutputStream(WBwrite);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet(SSwrite);
			HSSFFont font = workbook.createFont();

			repwr = FileHandler.replica(WRpath, SSRead);

			int rc = FileHandler.getRowCount(WRpath, SSRead);
			for(int i=0; i<=rc; i++){
				int cc = FileHandler.colcount(WRpath, SSRead, i);
				HSSFRow row1 = worksheet.createRow(i);
				for(int j=0; j<=cc; j++){
					HSSFCell c1 = row1.createCell(j);
					worksheet.setColumnWidth(j, 4200);
					c1.setCellValue(repwr[i][j]);
					font.setFontName("Calibri");
					HSSFCellStyle cellStyle = workbook.createCellStyle();
					cellStyle.setFont(font);
					c1.setCellStyle(cellStyle);
				}
			}

			workbook.write(fileout);
			fileout.flush();
			fileout.close();

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SSwrite;
	} */

	public static String getFourWeekStartingDate(String format) {  
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat(format);
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
		if(day_of_week==1) 
			day_of_week=6;
		else
			day_of_week-=2;
		cal.add(Calendar.DAY_OF_YEAR, (0-day_of_week));
		cal.add(Calendar.WEEK_OF_YEAR, -3);
		return dateFormat.format(cal.getTime());  
	}  

	public static String getThisWeekStartingDate(String format) {  
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat(format);
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DAY_OF_YEAR, (1-day_of_week));
		return dateFormat.format(cal.getTime());  
	}  

	public static String getThisWeekEndingDate(String format) {  
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat(format);
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DAY_OF_YEAR, (7-day_of_week));
		return dateFormat.format(cal.getTime());  
	}  

	public static String getDateBeforeOrAfterYears(int years, String format) {  
		Date tomorrow = new Date(); 
		DateFormat dateFormat = new SimpleDateFormat(format);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, years);
		tomorrow = cal.getTime();

		return dateFormat.format(tomorrow);  
	}

	public static String getDateBeforeOrAfterDays(int days, String NEW_FORMAT,
			String date) {

		String OLD_FORMAT = "dd/MM/yyyy";
		String newDateString = null;
		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sdf.applyPattern(NEW_FORMAT);
		newDateString = sdf.format(d);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(newDateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.add(Calendar.DATE, days); // number of days to add
		return sdf.format(c.getTime()); // dt is now the new date

	}

	public static String changeDateFormat(String oldDateString)
	{
		String OLD_FORMAT = "dd/MM/yyyy";
		String NEW_FORMAT = "yyyy/MM/dd";
		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		Date d = null;
		try {
			d = sdf.parse(oldDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sdf.applyPattern(NEW_FORMAT);
		return sdf.format(d);
	}

	/**
	 * To change the filePath containing \\ to /
	 * @param existingFilePath
	 * @return new FilePath
	 */
	public static String changeFilePath (String existingFilePath)
	{
		// format filePath
		StringBuffer newText = new StringBuffer() ;
		for(int i=0;i<existingFilePath.length();i++)
		{
			boolean flag = false;
			//newText.append(filePath.charAt(i));
			if(existingFilePath.charAt(i)=='/'){
				if(existingFilePath.charAt(i+1)=='/'){
					flag = true;
					newText.append('\\');
					i++;
				}
				else
					newText.append(existingFilePath.charAt(i));
			}
			if(!flag) newText.append(existingFilePath.charAt(i));

		}
		String newFilePath = newText.toString();

		return newFilePath;
	}
	/**
	 * Get the roundOff value to desired minimum fraction of digits.
	 * @param roundOffValue
	 * @param minimumFractionDigits
	 * @return
	 */
	public static String roundOff(double roundOffValue,int minimumFractionDigits){

		final DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(minimumFractionDigits);
		df.setRoundingMode(RoundingMode.HALF_UP);
		String strRoundOffValue = df.format(roundOffValue);
		return strRoundOffValue;
	}
	/**
	 * Get the ConvenienceFee Calculation for the amount given 
	 * @param amount
	 * @param convPercent
	 * @param convFlatFee
	 * @return
	 */
	public static String calculateConvenienceFee(String amount,String convPercent,String convFlatFee){

		String amountWithConv = "";
		double serviceTax= 0.1236;
		double dAmount = Double.parseDouble(amount);
		double dConVPercent = Double.parseDouble(convPercent);
		double dConVFlatFee = Double.parseDouble(convFlatFee);
		double dTotalConveninenceFee;
		dTotalConveninenceFee = (dAmount * dConVPercent/100) + dConVFlatFee;
		dTotalConveninenceFee = dTotalConveninenceFee + (dTotalConveninenceFee * serviceTax);
		amountWithConv = String.valueOf(dTotalConveninenceFee);
		return amountWithConv;
	}
	

	/**
	 * This method truncates/sacles the given number to specified number of decimals given
	 * @param dNumber
	 * @param numberofDecimals
	 * @return
	 */
	 public static String truncateDecimal(double dNumber,int numberofDecimals) {
	
		 String trucatedValue ="";
		 if ( dNumber > 0) {
		 BigDecimal number = new BigDecimal(String.valueOf(dNumber)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
		 trucatedValue = String.valueOf(number);
		 return trucatedValue;
		 } else {
		 BigDecimal number = new BigDecimal(String.valueOf(dNumber)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
		 trucatedValue = String.valueOf(number);
		 return trucatedValue;
		 }
	}

		/**
		 * This function generate Random Alphabets String and put it into runTimeProperty
		 * @param testConfig
		 * @param length - Size of String
		 * @param variableName - Name to be used in runTimeProperty
		 */
		public static void generateRandomStringAndPutRunTime(Config testConfig, int length, String variableName)
		{
			String var = Helper.generateRandomAlphabetsString(length);
			testConfig.putRunTimeProperty(variableName, var);
		}

		/**
		 * Function to change Date from 20/12/15 to 20-12-15 and to merge Date & Time to make 1 field.
		 * @param date
		 * @param time
		 * @return
		 */
		public static String changeDateTimeFormat(String date, String time) {
			String dateTime = "";
			dateTime = changeDateFormatSeperator(date);
			dateTime = dateTime.concat(" ");
			dateTime = dateTime.concat(time);
			return dateTime;
		}

		/**
		 * Function to change Date from 20/12/15 to 20-12-15
		 * @param date
		 * @return
		 */
		public static String changeDateFormatSeperator(String date)
		{
			String dateOnly = "";
			dateOnly = date.replaceAll("/", "-");
			return dateOnly;
		}
}
