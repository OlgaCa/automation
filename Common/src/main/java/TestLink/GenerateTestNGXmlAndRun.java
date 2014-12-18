package TestLink;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.TestNG;
import org.testng.TestNGException;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;


/**
 * Class that reads the excel sheet generated by GetTestCasesToRun class as
 * input and generates the testNG.xml programatically. Reads the excel sheet Common/TestLink/<Project>GroupNames.xls file and creates groupsDetails JSONObject to 
 * construct TestNG xml with groups information.  Upon successful testNGXml
 * creation, runs the test cases specified in the testNGXml.
 * 
 * @author vidya.priyadarshini
 * 
 */
public class GenerateTestNGXmlAndRun {

	//Flag to on/off logging
	public boolean debugMode = true;

	// hudson Job Name - args[0]
	// hudson Build Tag - args[1]
	// ProjectName - args[2]
	// Environment - args[3]
	// Browser - args[4]
	// IsParallel - args[5]
	// RunAllMethodsOfIncludedClasses - args[6]
	// ParallelType - args[7]

	public static void main(String args[]) throws IOException, JSONException {
		GenerateTestNGXmlAndRun genAndRunTestNG = new GenerateTestNGXmlAndRun();

		//Get groupDetails and Interfering details of testcases based on input given in <Project>/<Project>GroupNames.xls file
		//Returns two objects with group name as respective keys and the values as 1. interfering value of that group 2. arraylist of classes in that group
		ReadGroupExcelSheetRet2Values<HashMap<String, String>, HashMap<String, ArrayList<String>>> groupDetailsObject = genAndRunTestNG
				.readGroupNamesExcelSheet(args[0], args[2]);

		//extract GroupDetails hashmap object
		//<GroupName, ArrayListOfClasses of that group> read from  groupname excel sheet
		HashMap<String,ArrayList<String>> groupDetails = groupDetailsObject.second();

		//convert the groupdetails hashmap to JSONObject for ease of parsing
		JSONObject groupDetailsObj = new JSONObject(groupDetails);

		//extract Interfering details of testcases hashmap object
		//<GroupName, Interfering Value> read from the groupname excel sheet
		HashMap<String,String> groupInterferingDetails = groupDetailsObject.first();

		//Get the testcasedetails to be run by reading the .xls file generated by GetTestCasesToRun.java class into a hashmap object
		//<ClassName,ArrayList Of methods> -> got after reading test cases of testplan after they were written to excel 
		HashMap<String, ArrayList<String>> testDetails = genAndRunTestNG.readExcelSheet(args[0], args[1]);

		JSONObject testDetailsObj = new JSONObject(testDetails);

		JSONArray testSuiteXmlObj = genAndRunTestNG.genTestSuiteXmlObject(groupDetailsObj, testDetailsObj);

		genAndRunTestNG.genAndRunTestNGXml(testSuiteXmlObj,groupInterferingDetails, args[0], args[1],
				args[2], args[3], args[4], args[5],args[7], args[6]);

		// this is old method without groups info
		// genAndRunTestNG.runTestNGTest(testDetails, args[0], args[1], args[2],
		// args[3], args[4], args[5]);

	}

	@SuppressWarnings("rawtypes")
	/**
	 * 
	 * @param testSuiteXmlObj - contains all the test cases from test link which need to be run from test plan
	 * @param groupInterferingDetails
	 * @param jobName
	 * @param buildTag
	 * @param projectName
	 * @param environment
	 * @param browser
	 * @param isParallel
	 * @param parallelType
	 * @throws IOException
	 * @throws JSONException
	 */
	public void genAndRunTestNGXml(JSONArray testSuiteXmlObj,HashMap<String,String> groupInterferingDetails, String jobName,
			String buildTag, String projectName, String environment,
			String browser, String isParallel, String parallelType, String RunCompleteClass) throws IOException,
			JSONException {

		HashMap<String,String> parameters = new HashMap<String,String>();
		parameters.put("parallelType", parallelType);
		parameters.put("environment", environment);
		parameters.put("browser", browser);
		

		File file = new File("TestNG.xml");
		FileWriter writer = new FileWriter(file);
		if(debugMode)System.out.println("Create TestNG Xml file for defining TC to be run- "
				+ file);

		// Create an instance on TestNG
		TestNG myTestNG1 = new TestNG();

		// Create an instance of XML Suite and assign a name to it
		XmlSuite mySuite = new XmlSuite();
		mySuite.setName(projectName + " Automation");

		// Check is suite is to be run in parallel
		if (isParallel.contentEquals("true")) {
			System.out.println("ParallelType is---->>>"+parallelType);
			if(parallelType.contentEquals("classes")){
				mySuite.setParallel("classes");
			}
			else {
				mySuite.setParallel("methods");
			}
			mySuite.setThreadCount(8);
		}
		//set environment and browser as parameters in testngxml
		mySuite.setParameters(parameters);

		// Set listeners
		List<Class> listnerClasses = new ArrayList<Class>();
		listnerClasses.add(org.uncommons.reportng.HTMLReporter.class);
		listnerClasses.add(org.uncommons.reportng.JUnitXMLReporter.class);
		myTestNG1.setListenerClasses(listnerClasses);

		// disable default listeners
		myTestNG1.setUseDefaultListeners(false);

		// Set the results directory path
		String outputDir = System.getProperty("user.home")
				+ "\\.hudson\\jobs\\" + jobName + "\\workspace\\" + projectName
				+ "\\Results\\" + buildTag;
		if(debugMode)System.out.println("Create Result Output Directory - "+ new File(outputDir).mkdirs());
		myTestNG1.setOutputDirectory(outputDir);

		// set reportng properties
		System.setProperty("org.uncommons.reportng.title", "PayU "
				+ projectName + " Test Report");
		System.setProperty("org.uncommons.reportng.escape-output", "false");

		// set system property for output directory (used for saving
		// screenshots)
		System.setProperty("testngOutputDir", "\\Results\\" + buildTag);

		// override environment value of Config.properties, if this is passed
		if (!(environment == null || environment.isEmpty()))
			System.setProperty("environment", environment);

		// override browser name of Config.properties, if this is passed
		if (!(browser == null || browser.isEmpty()))
			System.setProperty("browser", browser);

		// Add the suite to the list of suites.
		List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
		// Get all the class names to be run
		for (int i = 0; i < testSuiteXmlObj.length(); i++) {
			Iterator<?> groupItr = testSuiteXmlObj.getJSONObject(i).keys();
			JSONObject groupObj = testSuiteXmlObj.getJSONObject(i);

			List<XmlClass> myClasses = null;
			XmlClass testClass = null;
			XmlTest myTest = null;
			while (groupItr.hasNext()) {
				String groupKey = groupItr.next().toString();
				//System.out.println("Trouble Maker which is not present in groups excel sheet ------->" + groupObj.get(groupKey).toString());

				JSONArray classesObj = new JSONArray(groupObj.get(groupKey)
						.toString());

				//Iterator<?> classItr = groupObj.keys();
				//JSONObject classObj = classesObj.getJSONObject(k);

				String isInterfering = groupInterferingDetails.get(groupKey);
				// Create an instance of XmlTest and assign a name for it.
				myTest = new XmlTest();
				myTest.setName(groupKey);
				myTest.setVerbose(0);
				if(isInterfering==null)
					isInterfering = "TRUE";
				if(isInterfering.contentEquals("TRUE"))
					myTest.setThreadCount(1);

				// Create a list which can contain the classes that you want to
				// run.
				myClasses = new ArrayList<XmlClass>();

				for(int k=0;k<classesObj.length();k++)
				{
					//get a class json object
					JSONObject classObj = classesObj.getJSONObject(k);
					ArrayList<String> keys = new ArrayList<String>();
					Iterator<?> classItr = classObj.keys();
					//get the classnames
					while (classItr.hasNext()) {
						keys.add(classItr.next().toString());

					}
					/*if(keys.size()>1)
						System.out.println("I have more classes" + classObj.toString());
					 */for(int l=0;l<keys.size();l++)
					 {
						 String classKey = keys.get(l);

						 //	String groupKey1 = classItr.next().toString();
						 //String classKey = groupObj.getString(groupKey1);
						 if(debugMode)System.out.println("Creating Test node for class - "
								 + classKey);

						 try{
							 // Create an instance of XmlClass and assign a name for it.
							 testClass = new XmlClass(classKey);
						 } catch(TestNGException e)
						 {
							 if(debugMode)System.out.println("Exception is: " + e);
						 }
						 if(RunCompleteClass.contentEquals("false"))
						 {
							 // Add the name of methods to be run for this class
							 ArrayList<XmlInclude> methodsToRun = new ArrayList<XmlInclude>();
							 String[] methods = null;

							 String m = classObj.getString(classKey);
							 if (m.substring(1, m.length() - 1).contains(","))

							 {
								 methods = m.substring(1, m.length() - 1).split(", ");

								 if(debugMode)System.out.println("Including method - ");
								 for (int j = 0; j < methods.length; j++) {
									 if(debugMode)System.out.print("\t" + methods[j]);
									 methodsToRun.add(new XmlInclude(methods[j]));
								 }
								 if(testClass!=null)
									 testClass.setIncludedMethods(methodsToRun);

								 if(debugMode)System.out.println();
							 } else {
								 if(debugMode)System.out.println("Including method - "
										 + m.substring(1, m.length() - 1));
								 methodsToRun.add(new XmlInclude(m.substring(1,
										 m.length() - 1)));
								 if(testClass!=null)
									 testClass.setIncludedMethods(methodsToRun);

							 }
						 }
						 if(testClass!=null)
							 myClasses.add(testClass);
					 }

				}
			}

			// Assign that to the XmlTest Object created earlier.
			myTest.setXmlClasses(myClasses);
			myTest.setSuite(mySuite);
			mySuite.addTest(myTest);

		}
		mySuites.add(mySuite);
		// Set the list of Suites to the testNG object you created earlier.
		myTestNG1.setXmlSuites(mySuites);
		// Save the created xml
		if(debugMode)System.out.println("Created XML - \n" + mySuite.toXml());
		writer.write(mySuite.toXml());
		writer.close();
		// invoke run() - this will run the classes and methods specified in
		// testNG1.xml file
		myTestNG1.run();
	}

	/**
	 * Util method to construct TestSuiteXml Object array
	 * 
	 * @throws JSONException
	 */
	public JSONArray genTestSuiteXmlObject(JSONObject groupDetailsObj, JSONObject testDetailsObj) throws JSONException {
		JSONArray testSuiteXmlObj = new JSONArray();

		Iterator<?> groupDetailsKeys = groupDetailsObj.keys();
		Iterator<?> testDetailsKeys = testDetailsObj.keys();

		String groupName = null;
		JSONObject testDetEntry = null;
		JSONObject testSuiteEntry = new JSONObject();
		int i = 0;
		while (testDetailsKeys.hasNext()) 
		{
			// HashMap<String, String> testDetailEntry = new HashMap<String,
			// String>();
			testDetEntry = new JSONObject();
			//testSuiteEntry = new JSONObject();

			String className = (String) testDetailsKeys.next();
			groupDetailsKeys = groupDetailsObj.keys();

			while (groupDetailsKeys.hasNext()) 
			{
				groupName = (String) groupDetailsKeys.next();
				String classNamesInThatGroup = groupDetailsObj.get(groupName).toString();

				if(!classNamesInThatGroup.isEmpty() && !className.isEmpty())
					//classNamesInThatGroup is an arraylist of classes that belong to a group and className is a single class picked from testlink
					if (classNamesInThatGroup.contains(className)) {

						if(className!=null)
						{

							String methodNames = testDetailsObj.get(className).toString();
							//this testDetEntry is a single class that belongs to groupName along with their method names
							testDetEntry.put(className, methodNames);
							className = null;
						}

						break;
					}

			}
			if(groupName!=null)
			{
				//consolidate all classes (along with their methods) that belong to groupName. ie. structure of testSuiteEntry is 
				//{groupName1:{className1:methods1Array,className2:methods2Array},groupName2:{className3:methods3Array,className4:methods4Array}}
				testSuiteEntry.append(groupName, testDetEntry);
				groupName = null;
			}
		}

		Iterator<?> groupNames = testSuiteEntry.keys();

		//Construct a JSONArray , by splitting the testSuiteEntry 
		while(groupNames.hasNext())
		{   //eg. groupname = groupName1
			String groupname = groupNames.next().toString();
			//eg. testClassesEntryArray = {className1:methods1Array,className2:methods2Array}
			JSONArray testClassesEntryArray = testSuiteEntry.getJSONArray(groupname);
			JSONObject testSuiteEntryObj = new JSONObject();
			if(!testClassesEntryArray.toString().contentEquals("{}")) //excluding empty class array, i.e. groupname is there in excel but the corresponding class is not included in test plan
			{
				//eg. {groupName1:{className1:methods1Array},className2:methods2Array}
				testSuiteEntryObj.put(groupname, testClassesEntryArray);
				//eg. 0 - groupName1:{className1:methods1Array,className2:methods2Array} , 1 - groupName2:{className3:methods3Array,className4:methods4Array}
				testSuiteXmlObj.put(i, testSuiteEntryObj);
				i++;
			}
		}


		testDetailsKeys = testDetailsObj.keys();

		//if there are no group names in excel sheet, even then add testcase to xml with group name as classname
		testDetEntry = new JSONObject();
		while(testDetailsKeys.hasNext())
		{
			String className = (String) testDetailsKeys.next();
			String methodNames = testDetailsObj.get(className)
					.toString();
			boolean isPresent = false;
			int index =testSuiteXmlObj.length();
			for(i=0; i<testSuiteXmlObj.length();i++)
			{
				if(testSuiteXmlObj.get(i).toString().contains(className))
					isPresent = true;
			}
			if(isPresent == false)
			{
				String[] fullName = className.split("\\.");
				groupName = fullName[fullName.length-1];
				if(!className.isEmpty() && !methodNames.isEmpty())
				{

					testDetEntry.put(className, methodNames);
					testSuiteEntry.append(groupName, testDetEntry);
					testDetEntry = new JSONObject();

					//Construct a JSONArray , by splitting the testSuiteEntry 
					//eg. testClassesEntryArray = {className1:methods1Array,className2:methods2Array}
					JSONArray testClassesEntryArray = testSuiteEntry.getJSONArray(groupName);
					JSONObject testSuiteEntryObj = new JSONObject();
					if(!testClassesEntryArray.toString().contentEquals("{}")) //excluding empty class array, i.e. groupname is there in excel but the corresponding class is not included in test plan
					{
						//eg. {groupName1:{className1:methods1Array},className2:methods2Array}
						testSuiteEntryObj.put(groupName, testClassesEntryArray);
						//eg. 0 - groupName1:{className1:methods1Array,className2:methods2Array} , 1 - groupName2:{className3:methods3Array,className4:methods4Array}
						testSuiteXmlObj.put(index, testSuiteEntryObj);
						index++;
					}


					/*if(!testSuiteEntry.get(groupName).toString().contentEquals("{}"))
					{	
						testSuiteXmlObj.put(index, testSuiteEntry);
						index++;
					}*/
				}
			}

		}

		return testSuiteXmlObj;
	}

	/**
	 * Util method that reads group names and interfering values for classes and
	 * creates a hashmap which maps group name with corresponding java classes
	 * and interfering value
	 */

	public ReadGroupExcelSheetRet2Values<HashMap<String, String>, HashMap<String, ArrayList<String>>> readGroupNamesExcelSheet(
			String jobName, String projectName) throws IOException {
		String filePath = System.getProperty("user.home") + "\\.hudson\\jobs\\"
				+ jobName + "\\workspace\\" + projectName + "\\" + projectName
				+ "GroupNames.xls";

		// Stores the group name and the corresponding classes list
		HashMap<String, ArrayList<String>> groupNameDetails = new HashMap<String, ArrayList<String>>();
		// Stores the group name and corresponding interfering value
		HashMap<String, String> groupInterferingDetails = new HashMap<String, String>();
		try {
			if(debugMode)System.out.println("Reading file - " + filePath);
			FileInputStream file = new FileInputStream(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheetAt(0);

			int lastRow = sheet.getLastRowNum();
			int currentRow = 1;


			for (int i = 1; i <= lastRow; i++) {
				currentRow = i;
				// This will hold all classes belonging to current group name

				ArrayList<String> classes = new ArrayList<String>();

				// Read the group name in current row
				String currentGroupName = sheet.getRow(i).getCell(0).toString();
				String isInterfering = sheet.getRow(i).getCell(2).toString();
				// Check if current group is already added
				if (!groupNameDetails.containsKey(currentGroupName)) {
					if(debugMode)System.out.println("Identifying classes to be grouped under group name - "+ currentGroupName);

					while (i <= lastRow) {
						if (sheet.getRow(i).getCell(0).toString()
								.contentEquals(currentGroupName)) {
							if(debugMode)System.out.println("Identifying class"+ sheet.getRow(i).getCell(1).toString());
							classes.add(sheet.getRow(i).getCell(1).toString());
						}
						i++;

					}
					groupNameDetails.put(currentGroupName, classes);
					groupInterferingDetails
					.put(currentGroupName, isInterfering);

				}
				i = currentRow;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ReadGroupExcelSheetRet2Values<HashMap<String, String>, HashMap<String, ArrayList<String>>>(
				groupInterferingDetails, groupNameDetails);

	}

	/**
	 * 
	 * Util method that reads the excel sheet containing test details and
	 * creates a hash map which maps the Java class name with corresponding java
	 * method names
	 * 
	 * @param buildTag
	 * @param jobName
	 * @return HashMap containing testcase details with key as classname and value as methods array list
	 */
	public HashMap<String, ArrayList<String>> readExcelSheet(String jobName,
			String buildTag) throws IOException {
		String filePath = System.getProperty("user.home") + "\\.hudson\\jobs\\"
				+ jobName + "\\workspace\\" + buildTag + ".xls";

		// Stores the class name and the corresponding method list
		HashMap<String, ArrayList<String>> testDetails = new HashMap<String, ArrayList<String>>();

		try {
			if(debugMode)System.out.println("Reading file - " + filePath);
			FileInputStream file = new FileInputStream(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheetAt(0);

			int lastRow = sheet.getLastRowNum();
			for (int i = 1; i < sheet.getLastRowNum(); i++) {
				// This will hold all methods belonging to current class name
				ArrayList<String> methods = new ArrayList<String>();

				// Read the class name in current row
				String currentClass = sheet.getRow(i).getCell(1).toString();

				// Check if current class is already added
				if (!testDetails.containsKey(currentClass)) {
					if(debugMode)System.out.println("Identifying methods to be run for class - "+ currentClass);

					// Add current test method for this new class
					methods.add(sheet.getRow(i).getCell(2).toString());

					// Compare it with class names in subsequent rows
					for (int j = i + 1; j <= sheet.getLastRowNum(); j++) {
						// If class name matches, add the corresponding method
						// to method list
						if (currentClass.equals(sheet.getRow(j).getCell(1)
								.toString())) {
							methods.add(sheet.getRow(j).getCell(2).toString());
						}
					}

					// add all these methods against the current class name
					testDetails.put(currentClass, methods);
				}
			}

			// In case last row contains a unique class
			if (!testDetails.containsKey(sheet.getRow(lastRow).getCell(1)
					.toString())) {
				if(debugMode)System.out.println("Identifying methods to be run for class - "+ sheet.getRow(lastRow).getCell(1).toString());

				ArrayList<String> methods = new ArrayList<String>();
				methods.add(sheet.getRow(lastRow).getCell(2).toString());
				testDetails.put(sheet.getRow(lastRow).getCell(1).toString(),
						methods);
			}

			file.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return testDetails;
	}

	/**
	 * Creates the testNG Xml programatically based on the test details hash
	 * map. Upon creation, runs the generated testNG.xml suite.
	 * 
	 **/
	@SuppressWarnings("rawtypes")
	public void runTestNGTest(Map<String, ArrayList<String>> testDetails,
			String jobName, String buildTag, String projectName,
			String environment, String browser, String isParallel)
					throws IOException {
		File file = new File("TestNG.xml");
		FileWriter writer = new FileWriter(file);
		if(debugMode)System.out.println("Create TestNG Xml file for defining TC to be run- "+ file);

		// Create an instance on TestNG
		TestNG myTestNG = new TestNG();

		// Create an instance of XML Suite and assign a name to it
		XmlSuite mySuite = new XmlSuite();
		mySuite.setName(projectName + " Automation");

		// Check is suite is to be run in parallel
		if (isParallel.contentEquals("true")) {
			mySuite.setParallel("methods");
			mySuite.setThreadCount(5);
		}

		// Set listeners
		List<Class> listnerClasses = new ArrayList<Class>();
		listnerClasses.add(org.uncommons.reportng.HTMLReporter.class);
		listnerClasses.add(org.uncommons.reportng.JUnitXMLReporter.class);
		myTestNG.setListenerClasses(listnerClasses);

		// disable default listeners
		myTestNG.setUseDefaultListeners(false);

		// Set the results directory path
		String outputDir = System.getProperty("user.home")
				+ "\\.hudson\\jobs\\" + jobName + "\\workspace\\" + projectName
				+ "\\Results\\" + buildTag;
		if(debugMode)System.out.println("Create Result Output Directory - "+ new File(outputDir).mkdirs());
		myTestNG.setOutputDirectory(outputDir);

		// set reportng properties
		System.setProperty("org.uncommons.reportng.title", "PayU "
				+ projectName + " Test Report");
		System.setProperty("org.uncommons.reportng.escape-output", "false");

		// set system property for output directory (used for saving
		// screenshots)
		System.setProperty("testngOutputDir", "\\Results\\" + buildTag);

		// override environment value of Config.properties, if this is passed
		if (!(environment == null || environment.isEmpty()))
			System.setProperty("environment", environment);

		// override browser name of Config.properties, if this is passed
		if (!(browser == null || browser.isEmpty()))
			System.setProperty("browser", browser);

		// Get all the class names to be run
		Set<String> keys = testDetails.keySet();
		Iterator<String> itr = keys.iterator();

		String key;
		ArrayList<String> value;

		// Loop through all the class names
		while (itr.hasNext()) {
			key = itr.next();
			value = testDetails.get(key);
			if(debugMode)System.out.println("Creating Test node for class - " + key);

			// Create an instance of XmlTest and assign a name for it.
			XmlTest myTest = new XmlTest();
			myTest.setName(key);
			myTest.setVerbose(0);

			// Create an instance of XmlClass and assign a name for it.
			XmlClass testClass = new XmlClass(key);

			// Add the name of methods to be run for this class
			ArrayList<XmlInclude> methodsToRun = new ArrayList<XmlInclude>();
			for (int k = 0; k < value.size(); k++) {
				if(debugMode)System.out.println("Including method - " + value.get(k));
				methodsToRun.add(new XmlInclude(value.get(k)));
			}
			testClass.setIncludedMethods(methodsToRun);

			// Assign this XmlClass to the XmlTest
			// testClass.setXmlTest(myTest);

			// TODO if the group name has been passed as well, use that
			/*
			 * String myGroupName = "functest"; List<String> groups = new
			 * ArrayList<String>(); groups.add(myGroupName);
			 * myTest.setIncludedGroups(groups);
			 */

			// Create a list which can contain the classes that you want to run.
			List<XmlClass> myClasses = new ArrayList<XmlClass>();
			myClasses.add(testClass);

			// Assign that to the XmlTest Object created earlier.
			myTest.setXmlClasses(myClasses);
			myTest.setSuite(mySuite);
			//mySuite.addTest(myTest);
		}

		// Add the suite to the list of suites.
		List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
		mySuites.add(mySuite);

		// Set the list of Suites to the testNG object you created earlier.
		myTestNG.setXmlSuites(mySuites);

		// Save the created xml
		if(debugMode)System.out.println("Created XML - \n" + mySuite.toXml());
		writer.write(mySuite.toXml());
		writer.close();

		// invoke run() - this will run the classes and methods specified in
		// testNG.xml file
		myTestNG.run();
	}
}