package TestLink;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.testng.annotations.Test;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ActionOnDuplicate;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;


public class ImportTestCases extends JFrame implements ActionListener{


	public static void main(String args[]) throws ClassNotFoundException, IOException, InvocationTargetException, InterruptedException
	{


		//Substitute your Test Link Dev Key Here
		//final String DEV_KEY = "8cc70e02f58a425b06f88364c586c5be";

		final String DEV_KEY =  "d5539eafe6572eaeddf07db1fc674cac";
		//Substitute your Test Link Server URL Here
		//final String SERVER_URL = "http://localhost:81/testlink-1.9.9/lib/api/xmlrpc/v1/xmlrpc.php";

		final String SERVER_URL = "http://payu-testlink.com/lib/api/xmlrpc/v1/xmlrpc.php";

		String pathName = "C:\\PayU\\Product\\bin\\Test\\";
		String packageName = "Test";   
		final String jobName = "RunTestLink";
		String projectName = "Product";
		//Use the event dispatch thread for Swing components
		EventQueue.invokeAndWait(new Runnable()
		{
			public void run()
			{
				//create GUI frame
				try {
					new ImportTestCases(SERVER_URL,DEV_KEY,jobName).setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}          
			}
		});
		//  ImportTestCases.constructTestCaseInfo(SERVER_URL,DEV_KEY,projectName,pathName,packageName,jobName);
		//List<Class> classes = ImportTestCases.findClasses(new File(pathName), packageName);
		/*for(int j=0;j<classes.size();j++)
		{
		    String className = classes.get(j).getName();
			ImportTestCases.addOnlyNewSuites(SERVER_URL, DEV_KEY,className,jobName,projectName);
		}*/
		//Add all testcases to testplan
		//	ImportTestCases.addAllTestCasesToTestPlan(pathName,packageName, SERVER_URL,DEV_KEY,projectName,testPlanName);

	}	 

	public ImportTestCases(String SERVER_URL,String DEV_KEY,String jobName) throws IOException
	{
		//Using a standard Java icon
		Icon optionIcon = UIManager.getIcon("FileView.computerIcon");
		String[] authorNames = { "sonia.gupta", "atul.jain", "nitin.mukhija", "manju.bala", "jyoti.patial","tulasi.ram","vishal.saur","bhaskar.pahuja","kamal.pant","mukesh.rajput","naveen.kumar","rahul.kumar","urvashi.ahuja","rohit.jindal" };
		
		String[] projectType = { "Product", "Paisa" };
		//make sure the program exits when the frame closes
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Class Name Input");
		setSize(500,300);
		//This will center the JFrame in the middle of the screen
		setLocationRelativeTo(null);
		//Input dialog with a text field
		String className =  JOptionPane.showInputDialog(this ,"Enter class Name with package name eg:Test.Merchant.AdminPanel");
		System.out.println("Class Name entered " + className);

		//Input dialog with a text field
		String groupName =  JOptionPane.showInputDialog(this ,"Enter group Name");
		System.out.println("Group Name entered " + groupName);
		
		//Combo box of project types
		JComboBox projectList = new JComboBox(projectType);
		projectList.addActionListener(this);
		String projectName = (String)JOptionPane.showInputDialog(this, "Pick a Name:"
				, "ComboBox Dialog", JOptionPane.QUESTION_MESSAGE
				, optionIcon, projectType, projectType[0]);

		/*//Input dialog with a text field
		String projectName =  JOptionPane.showInputDialog(this ,"Enter project Name");
		 */
		
		System.out.println("Project Name entered " + projectName);
		
		//Combo box of author names
		JComboBox authorList = new JComboBox(authorNames);
		authorList.addActionListener(this);
		String authorName = (String)JOptionPane.showInputDialog(this, "Pick a Name:"
				, "ComboBox Dialog", JOptionPane.QUESTION_MESSAGE
				, optionIcon, authorNames, authorNames[0]);

		System.out.println("author Name chosen " + authorName);
		String isSerial = JOptionPane.showInputDialog(this ,"Enter TRUE if testcase is serial, FALSE if not");
		System.out.println("isSerial entered " + isSerial);


		addOnlyNewSuites(SERVER_URL, DEV_KEY,groupName,className,isSerial,jobName,projectName,authorName);



	}

	public static void addOnlyNewSuites(String SERVER_URL, String DEV_KEY,String newGroupName,String className,String isSerial,String jobName,String projectName,String authorName) throws IOException
	{
		/*String filePath = System.getProperty("user.home") + "\\.hudson\\jobs\\"
				+ jobName + "\\workspace\\" + projectName + "\\" + projectName
				+ "GroupNames.xls";*/
		String filePath = "C:\\PayU\\" + projectName + "\\" + projectName
				+ "GroupNames.xls";
		//String filePath = "../" + projectName + "/" + projectName + "GroupNames.xls";

		System.out.println("Reading file - " + filePath);
		InputStream input = new BufferedInputStream(
				new FileInputStream(filePath));
		POIFSFileSystem fs = new POIFSFileSystem( input );

		HSSFWorkbook workbook = new HSSFWorkbook(fs);
		HSSFSheet sheet = workbook.getSheetAt(0);

		int lastRow = sheet.getLastRowNum();
		HashMap<String,Integer> groupsMap = new HashMap<String,Integer>();
		Integer projectId = getProjectId(SERVER_URL, DEV_KEY, projectName);
		System.out.println("last row is " + lastRow);
		for(int i=1;i<=lastRow;i++)
		{
			if(sheet.getRow(i).getCell(4)!=null)
				groupsMap.put(sheet.getRow(i).getCell(1).toString(), Integer.parseInt(sheet.getRow(i).getCell(4).toString()));
			else
				groupsMap.put(sheet.getRow(i).getCell(1).toString(), 0);

		}
		String className1 = className;
		String[] classSuites = className.split("\\.");
		StringBuffer createdPart = new StringBuffer();
		String notCreatedPart = null;
		//Boolean isEnd = false;
		String groupClassName = null;
		Integer matchCnt = 0;
		Integer drpcnt = 0;
		List<Integer> classSuiteId = new ArrayList<Integer>();
		Boolean isPresent = false;
		String groupClassName1 = null;
		for(int i = 0 ;i<classSuites.length;i++)
		{

			//iterate through groups to compare with classSuites[i]

			Set<String> itr = groupsMap.keySet();
			Iterator<String> iterator = itr.iterator();

			while(iterator.hasNext())
			{
				String key = iterator.next().toString();
				groupClassName = key;
				String[] groupSuites = groupClassName.split("\\.");
				if(groupSuites.length>i)
				{

					if(classSuites[i].contentEquals(groupSuites[i]))
					{
						isPresent = true;
						//remove old entry
						if(!classSuiteId.isEmpty())
							classSuiteId.remove(0);
						classSuiteId.add(groupsMap.get(groupClassName));
						groupClassName1 = groupClassName;
						createdPart = createdPart.append(groupSuites[i]);
						matchCnt = i+1;
						drpcnt = classSuites.length - matchCnt;
						if(i!=classSuites.length-1)
							createdPart.append(".");
						break;
					}	    		
				}
			}
			if(!isPresent)
				break;
			isPresent = false;
		}

		System.out.println("To Create class name is" + className1);

		System.out.println("Created Part is " + createdPart);
		System.out.println("Class Suite Id of " +  groupClassName1 + " is "+classSuiteId.get(0));
		System.out.println("Match cnt is " + matchCnt);
		System.out.println("Drop cnt is" + drpcnt);

		URL url = null;
		TestSuite[] suiteObj = null;
		TestSuite suiteObj1 = null;
		try {
			url = new URL(SERVER_URL);

			TestLinkAPI api = new TestLinkAPI(url,DEV_KEY);
			Class classObj = Class.forName(className);


			if(createdPart.length()==className1.length())
			{
				// suite has already been created for class.So need to only add new testcases for newly created test methods
				Integer suiteId = classSuiteId.get(0);
				TestCase[] presentTestCasesInTL = api.getTestCasesForTestSuite(suiteId, null, null);

				for(int i =0;i< presentTestCasesInTL.length;i++)

				{
					System.out.println("Testcases in TL are:" + presentTestCasesInTL[i].getName());

				}
				for(Method method : classObj.getMethods())
				{
					Annotation annotation = method.getAnnotation(Test.class);
					Boolean isTestCasePresent = false;
					Test test = (Test) annotation;
					if (method.isAnnotationPresent(Test.class))
					{

						String methodName = method.getName();
						for(int i =0;i< presentTestCasesInTL.length;i++)

						{
							//System.out.println("Testcases in TL are:" + presentTestCasesInTL[i].getName());
							if(presentTestCasesInTL[i].getName().contentEquals(methodName))
							{
								isTestCasePresent = true;
							}

						}
						if(!isTestCasePresent)
						{
							System.out.println("Creating test case for " + methodName);

							TestCase testCaseObj = createTestCase(SERVER_URL, DEV_KEY, methodName, suiteId,test.description(), ExecutionType.AUTOMATED,projectId,authorName);

							Integer testCaseId = testCaseObj.getId();

							System.out.println("Adding javatestclass custom field");
							updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "JavaTestClass", className);
							System.out.println("Adding javatestmethod custom field");
							updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "JavaTestMethod", methodName);
							System.out.println("Adding AutomatedBy custom field");
							updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "AutomatedBy", authorName);
							System.out.println("Adding Automation Status custom field");
							updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "Automation_Status", "Ready To Run");
							System.out.println("Adding Browser custom field");
							updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "Browser", "Any");
							System.out.println("Adding Groups custom field");
							ImportTestCases.updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "Groups", "FVT");
							System.out.println("Adding How found custom field");
							ImportTestCases.updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "How_Found", "Test Design");
							System.out.println("Adding Interfering custom field");
							ImportTestCases.updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "Interfering", isSerial);

							//add testcase to testplan
							addTestCasesToTestPlan(SERVER_URL, DEV_KEY, "Regression", projectName, projectId, testCaseId);
						}
					}
				}

			}

			else 
			{
				ArrayList<String> createdSuites = new ArrayList<String>();
				HashMap<String,TestSuite> suiteObjects = new HashMap<String,TestSuite>();

				notCreatedPart = (String) className1.subSequence(createdPart.length(), className1.length());
				System.out.println("not created " + notCreatedPart);
				//Get number of times new packages have to be created
				String[] notCreated = notCreatedPart.split("\\.");
				ArrayList<String> finalNotCreated = new ArrayList<String>();
				for(int i=0;i<notCreated.length;i++)
				{
					if(!notCreated[i].isEmpty())
						finalNotCreated.add(notCreated[i]);
				}
				System.out.println("Packages to create");
				for(int i=0;i< finalNotCreated.size();i++)
				{
					System.out.println(finalNotCreated.get(i));
				}
				//create the not created suites
				//find suiteId of appropriate parent node
				int maxLength = longestSubstr(className1, createdPart.toString());
				String subString = groupClassName1.substring(maxLength-1);
				int count = StringUtils.countMatches(subString, ".");

				System.out.println("Number of times to go up the directory level is" + count);

				for(int i = 0;i<=count; i++)
				{
					//get parent suite obj
					suiteObj = api.getTestSuiteByID(classSuiteId);
					classSuiteId.remove(0);
					//add parent suite id to drill one level up
					if(i==count)
						classSuiteId.add(suiteObj[0].getId());
					else 
						classSuiteId.add(suiteObj[0].getParentId());

				}
				if(suiteObj!=null)
				{
					System.out.println("Parent suite name is " + suiteObj[0].getName());
					System.out.println("Parent suite id is " + classSuiteId.get(0));
				}
				System.out.println("Creating top level suite" + finalNotCreated.get(0));
				if(!(createdSuites.contains(finalNotCreated.get(0))))
				{
					Integer parentId = null;
					if(!classSuiteId.isEmpty())
						parentId = classSuiteId.get(0);
					suiteObj1 = createTestSuite(SERVER_URL, DEV_KEY, finalNotCreated.get(0), parentId, "description",projectId);
					if(suiteObj1!=null)
					{
						createdSuites.add(finalNotCreated.get(0));
						suiteObjects.put(finalNotCreated.get(0), suiteObj1);
					}
				}
				/*if(suiteObj!=null)
   				createdSuites.add(finalNotCreated.get(0));
				 *///create suites for every not created package name
				for(int i=1;i<finalNotCreated.size();i++)
				{
					if(!(createdSuites.contains(finalNotCreated.get(i))))
					{
						System.out.println("Creating Suite " + finalNotCreated.get(i) + " as child of" + suiteObj1.getName());
						TestSuite parentSuiteObj = suiteObjects.get(finalNotCreated.get(i-1));
						Integer parentId = parentSuiteObj.getId();
						suiteObj1 = createTestSuite(SERVER_URL, DEV_KEY,finalNotCreated.get(i), parentId, "child" + i,projectId);
						createdSuites.add(finalNotCreated.get(i));
						suiteObjects.put(finalNotCreated.get(i), suiteObj1);
					}
				}

				//create new testcases for all methods in newly created class
				classObj = Class.forName(className);

				for (Method method : classObj.getMethods())
				{
					Annotation annotation = method.getAnnotation(Test.class);

					Test test = (Test) annotation;
					if (method.isAnnotationPresent(Test.class))
					{

						String methodName = method.getName();
						System.out.println("Creating test case for " + methodName);
						TestCase testCaseObj = createTestCase(SERVER_URL, DEV_KEY, methodName, suiteObj1.getId(),test.description(), ExecutionType.AUTOMATED,projectId,authorName);
						Integer testCaseId = testCaseObj.getId();

						System.out.println("Adding javatestclass custom field");
						updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "JavaTestClass", className);
						System.out.println("Adding javatestmethod custom field");
						updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "JavaTestMethod", methodName);
						System.out.println("Adding AutomatedBy custom field");
						updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "AutomatedBy", authorName);
						System.out.println("Adding Automation Status custom field");
						updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "Automation_Status", "Ready To Run");
						System.out.println("Adding Browser custom field");
						updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "Browser", "Any");
						System.out.println("Adding Groups custom field");
						ImportTestCases.updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "Groups", "FVT");
						System.out.println("Adding How found custom field");
						ImportTestCases.updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "How_Found", "Test Design");

						//add testcase to testplan
						addTestCasesToTestPlan(SERVER_URL, DEV_KEY, "Regression", projectName, projectId, testCaseId);


					}

				}

				System.out.println("Reading file - " + filePath);
				input = new BufferedInputStream(
						new FileInputStream(filePath));
				fs = new POIFSFileSystem( input );

				workbook = new HSSFWorkbook(fs);
				sheet = workbook.getSheetAt(0);

				lastRow = sheet.getLastRowNum();
				Boolean isAlreadyPresent = false;

				for(int i =0;i<=lastRow;i++)
				{
					if(sheet.getRow(i).getCell(1).toString().contentEquals(className))
						isAlreadyPresent = true;
				}
				if(!isAlreadyPresent)
				{
					//write groupName
					System.out.println("Writing group name" + newGroupName + "to row number" +lastRow+1 );
					HSSFRow row = sheet.createRow(lastRow+1);
					HSSFCell cell = row.createCell(0);
					cell.setCellValue(newGroupName);

					//write className
					System.out.println("Writing class name" + className + "to row number" +lastRow+1 );
					cell = row.createCell(1);
					cell.setCellValue(className);

					//write is Serial value
					System.out.println("Writing is serial value " + isSerial + "to row number" +lastRow+1 );
					cell = row.createCell(2);
					cell.setCellValue(isSerial);

					//write authorName
					System.out.println("Writing author name" + authorName + "to row number" +lastRow+1 );
					cell = row.createCell(3);
					cell.setCellValue(authorName);
					//write suite id of suite for 
					System.out.println("Writing suite id" + suiteObj1.getId() + " for className " + className);
					cell = row.createCell(4);
					cell.setCellValue(suiteObj1.getId().toString());

					FileOutputStream fileout = new FileOutputStream(filePath);

					workbook.write(fileout);

					fileout.flush();
					fileout.close();

				}

			}
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public static int longestSubstr(String first, String second) {
		if (first == null || second == null || first.length() == 0 || second.length() == 0) {
			return 0;
		}

		int maxLen = 0;
		int fl = first.length();
		int sl = second.length();
		int[][] table = new int[fl+1][sl+1];

		for(int s=0; s <= sl; s++)
			table[0][s] = 0;
		for(int f=0; f <= fl; f++)
			table[f][0] = 0;




		for (int i = 1; i <= fl; i++) {
			for (int j = 1; j <= sl; j++) {
				if (first.charAt(i-1) == second.charAt(j-1)) {
					if (i == 1 || j == 1) {
						table[i][j] = 1;
					}
					else {
						table[i][j] = table[i - 1][j - 1] + 1;
					}
					if (table[i][j] > maxLen) {
						maxLen = table[i][j];
					}
				}
			}
		}
		return maxLen;
	}

	public static void addAllTestCasesToTestPlan(String pathName,String packageName,String SERVER_URL,String DEV_KEY,String projectName,String testPlanName) throws ClassNotFoundException
	{
		List<Class> classes = findClasses(new File(pathName), packageName);

		Integer projectId = ImportTestCases.getProjectId(SERVER_URL, DEV_KEY, projectName);
		Boolean isPresent = false;

		for(int j=1;j<classes.size();j++)
		{
			String className = classes.get(j).getName();
			String[] fullName = className.split("\\.");
			String testSuiteName = fullName[fullName.length-1];
			String methodName = null;
			Integer testCaseId = null;
			System.out.println("Looking test methods inside " + className);
			TestCase[] testCases = ImportTestCases.getTestCasesForTestPlan(SERVER_URL, DEV_KEY, testPlanName, projectName);

			for (Method method : classes.get(j).getMethods())
			{
				isPresent = false;
				//TODO: add description
				if (method.isAnnotationPresent(Test.class))
				{
					methodName = method.getName();
					testCaseId = ImportTestCases.getTestCaseIdByName(SERVER_URL, DEV_KEY, methodName, testSuiteName, projectName);

					for(TestCase testCase: testCases)
					{
						if(testCase.getId().equals(testCaseId))
						{
							isPresent = true;
						}
					}
					if(!isPresent)
					{
						System.out.println("Adding to tesplan testcase " + methodName);
						ImportTestCases.addTestCasesToTestPlan(SERVER_URL,DEV_KEY,testPlanName,projectName,projectId,testCaseId);
					}
				}
			}
		}
	}


	public static void constructTestCaseInfo(String SERVER_URL,String DEV_KEY,String projectName,String pathName,String packageName,String jobName)
	{
		TestSuite suiteObj = null;
		TestCase testCaseObj = null;
		Integer projectId = getProjectId(SERVER_URL, DEV_KEY, projectName);
		Annotation annotation = null;
		/*String filePath = System.getProperty("user.home") + "\\.hudson\\jobs\\"
				+ jobName + "\\workspace\\" + projectName + "\\" + projectName
				+ "GroupNames.xls";*/
		//String filePath = "../" + projectName + "/" + projectName + "GroupNames.xls";
		String filePath = "C:\\PayU\\" + projectName + "\\" + projectName
				+ "GroupNames.xls";

		try {
			ArrayList<String> createdSuites = new ArrayList<String>();
			HashMap<String,TestSuite> suiteObjects = new HashMap<String,TestSuite>();
			List<Class> classes = ImportTestCases.findClasses(new File(pathName), packageName);
			for(int j=0;j<classes.size();j++)
			{
				String className = classes.get(j).getName();
				System.out.println("Current Class Name is " + className);
				String methodName = null;
				String[] suites = className.split("\\.");
				System.out.println("Creating top level suite" + suites[0]);
				if(!(createdSuites.contains(suites[0])))
				{
					suiteObj = createTestSuite(SERVER_URL, DEV_KEY, suites[0], null, "description",projectId);
					createdSuites.add(suites[0]);
					suiteObjects.put(suites[0], suiteObj);
				}
				if(suiteObj!=null)
					createdSuites.add(suites[0]);
				//create suites for every package name
				for(int i=1;i<suites.length;i++)
				{
					if(!(createdSuites.contains(suites[i])))
					{
						System.out.println("Creating Suite " + suites[i] + " as child of" + suiteObj.getName());
						TestSuite parentSuiteObj = suiteObjects.get(suites[i-1]);
						Integer parentId = parentSuiteObj.getId();
						suiteObj = createTestSuite(SERVER_URL, DEV_KEY, suites[i], parentId, "child" + i,projectId);
						createdSuites.add(suites[i]);
						suiteObjects.put(suites[i], suiteObj);
					}
					//if suite is equal to classname write suite id to <Project> GroupNames.xls sheet
					if(i==suites.length-1)
					{
						System.out.println("Reading file - " + filePath);
						InputStream input = new BufferedInputStream(
								new FileInputStream(filePath));
						POIFSFileSystem fs = new POIFSFileSystem( input );

						HSSFWorkbook workbook = new HSSFWorkbook(fs);
						HSSFSheet sheet = workbook.getSheetAt(0);

						int lastRow = sheet.getLastRowNum();

						for (int k = 1; k <= lastRow; k++) {
							if(sheet.getRow(k).getCell(1).toString().contentEquals(className))
							{
								//write suite id of suite for corresponding 
								System.out.println("Writing suite id" + suiteObj.getId() + " for className " + suites[i]);
								HSSFCell cell = sheet.getRow(k).createCell(4);
								cell.setCellValue(suiteObj.getId().toString());
								System.out.println("cell value" + sheet.getRow(k).getCell(4));
							}
						}
						FileOutputStream fileout = new FileOutputStream(filePath);

						workbook.write(fileout);

						fileout.flush();
						fileout.close();
					}
				}
				for (Method method : classes.get(j).getMethods())
				{
					annotation = method.getAnnotation(Test.class);


					Test test = (Test) annotation;
					//TODO: add description
					if (method.isAnnotationPresent(Test.class))
					{

						methodName = method.getName();
						System.out.println(method.getName());
						String[] fullClassName = className.split("\\.");
						TestSuite classSuite = suiteObjects.get(fullClassName[fullClassName.length-1]);
						Integer classSuiteId = classSuite.getId();
						System.out.println("Creating test case for " + methodName);
						testCaseObj = createTestCase(SERVER_URL, DEV_KEY, methodName, classSuiteId,test.description(), ExecutionType.AUTOMATED,projectId,"admin");
						Integer testCaseId = testCaseObj.getId();

						System.out.println("Adding javatestclass custom field");
						updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "JavaTestClass", className);
						System.out.println("Adding javatestmethod custom field");
						updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "JavaTestMethod", methodName);
						System.out.println("Adding AutomatedBy custom field");
						updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "AutomatedBy", "PayuQC");
						System.out.println("Adding Automation Status custom field");
						updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "Automation_Status", "Ready To Run");
						System.out.println("Adding Browser custom field");
						updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "Browser", "Any");
						System.out.println("Adding Groups custom field");
						ImportTestCases.updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "Groups", "FVT");
						System.out.println("Adding How found custom field");
						ImportTestCases.updateTestCaseCustomFieldValues(SERVER_URL, DEV_KEY, testCaseId, projectId, "How_Found", "Test Design");

					}
				}
			}


		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Integer getTestCaseIdByName(String SERVER_URL,String DEV_KEY,String testCaseName,String testSuiteName,String testProjectName)
	{
		URL url = null;
		Integer testCaseId = null;
		try {
			url = new URL(SERVER_URL);

			TestLinkAPI api = new TestLinkAPI(url,DEV_KEY);
			testCaseId = api.getTestCaseIDByName(testCaseName, testSuiteName, testProjectName, null);
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testCaseId;	
	}

	public static void addTestCasesToTestPlan(String SERVER_URL,String DEV_KEY,String testPlanName, String projectName, Integer projectId,Integer testCaseId)
	{
		URL url = null;
		try {
			url = new URL(SERVER_URL);

			TestLinkAPI api = new TestLinkAPI(url,DEV_KEY);
			TestPlan testPlan = api.getTestPlanByName(testPlanName, projectName);

			api.addTestCaseToTestPlan(projectId, testPlan.getId(), testCaseId, 1, null, null, null);
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Integer getProjectId(String SERVER_URL,String DEV_KEY,String projectName)
	{
		URL url = null;
		Integer projectId = null;
		try {
			url = new URL(SERVER_URL);

			TestLinkAPI api = new TestLinkAPI(url,DEV_KEY);

			TestProject[] projects = api.getProjects();

			for(TestProject project : projects)
			{
				if(project.getName().contentEquals(projectName))
					projectId = project.getId();

			}
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return projectId;
	}

	public static TestCase[] getTestCasesForTestPlan(String SERVER_URL,String DEV_KEY,String testPlanName, String testProjectName)
	{
		URL url = null;
		Integer projectId = null;
		TestSuite suiteObj = null;
		TestCase[] testCases = null;
		try {
			url = new URL(SERVER_URL);
			TestLinkAPI api = new TestLinkAPI(url,DEV_KEY);
			TestPlan testPlan = api.getTestPlanByName(testPlanName, testProjectName);
			testCases = api.getTestCasesForTestPlan(testPlan.getId(), null, null, null, null, null, null, null, null, null, null);

		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testCases;
	}
	public static TestSuite createTestSuite(String SERVER_URL,String DEV_KEY,String suiteName,Integer parentId,String summary,Integer projectId)
	{
		URL url = null;
		TestSuite suiteObj = null;
		try {
			url = new URL(SERVER_URL);
			TestLinkAPI api = new TestLinkAPI(url,DEV_KEY);



			ActionOnDuplicate action = ActionOnDuplicate.BLOCK;

			suiteObj = api.createTestSuite(projectId, suiteName, summary, parentId, null, true, action);


			System.out.println(suiteObj.getId());


		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return suiteObj;

	}
	public static TestCase createTestCase(String SERVER_URL,String DEV_KEY,String methodName,Integer testSuiteId,String description,ExecutionType eType,Integer projectId,String authorName)
	{
		URL url = null;
		TestCase testCaseObj = null;
		try {
			url = new URL(SERVER_URL);
			TestLinkAPI api = new TestLinkAPI(url,DEV_KEY);
			TestProject[] projects = api.getProjects();

			ActionOnDuplicate action = ActionOnDuplicate.GENERATE_NEW;

			testCaseObj = api.createTestCase(methodName, testSuiteId, projectId, authorName, description, null, null, null, eType, null, null, true, action);

		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testCaseObj;

	}

	public static void updateTestCaseCustomFieldValues(String SERVER_URL, String DEV_KEY,Integer testCaseId,Integer projectId,String customFieldName,String customFieldValue)
	{
		URL url = null;
		try {
			url = new URL(SERVER_URL);
			TestLinkAPI api = new TestLinkAPI(url,DEV_KEY);

			api.updateTestCaseCustomFieldDesignValue(testCaseId, 1, projectId, customFieldName, customFieldValue);

		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
	 *
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static Class[] getClasses(String packageName)
			throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
