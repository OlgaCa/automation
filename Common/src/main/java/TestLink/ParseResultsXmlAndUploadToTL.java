package TestLink;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.model.Attachment;
import br.eti.kinoshita.testlinkjavaapi.model.ReportTCResultResponse;

/**
 * This class parses the xml files in Results\TimeStamp\xml  and updates the status of test cases in Test Link 
 * @author vidya.priyadarshini
 *
 */
public class ParseResultsXmlAndUploadToTL 
{
	//Substitute your Test Link Dev Key Here
	//public static final String DEV_KEY = "8cc70e02f58a425b06f88364c586c5be";
	//Substitute your Test Link Server URL Here
	//public static final String SERVER_URL = "http://localhost:81/testlink-1.9.9/lib/api/xmlrpc/v1/xmlrpc.php";

	public static final String DEV_KEY =  "d5539eafe6572eaeddf07db1fc674cac";
	//Substitute your Test Link Server URL Here
			
	public static final String SERVER_URL = "http://payu-testlink.com/lib/api/xmlrpc/v1/xmlrpc.php";
			

	//hudson Job Name - args[0]
	//hudson Build Tag - args[1]
	//ProjectName - args[2]
	//TestLink Plan Name - args[3]
	//TestLink Build Name - args[4]
	//TestLinkProjectName - args[5]

	public static void main(String[] args) throws IOException 
	{
		//Get test plan Id for the given test plan
		Integer testPlanId = ParseResultsXmlAndUploadToTL.getTestPlanIdByName(args[3],args[5]);

		//ParseResultsXmlAndUploadToTL.getTestCasesForTestPlan(testPlanId);
		//Upload test link test case results
		
		ParseResultsXmlAndUploadToTL.uploadTCResults(testPlanId, args[0], args[1], args[2], args[3], args[4],args[5]);
		
	}
	
	public static void getTestCasesForTestPlan(String testPlanId)
	{
		String xml = getTestCasesForTestPlanRequestXml(testPlanId);
		try
		{
			String postResponse = postRequest(xml);
			//System.out.println(postResponse);
			 JSONObject jsonObj = XML.toJSONObject(postResponse); 
			 JSONObject response = new JSONObject(jsonObj.getJSONObject("methodResponse").toString());
	        System.out.println(response.toString());
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static String getTestCasesForTestPlanRequestXml(String testPlanId)
	{
		
		String requestXml="<?xml version='1.0' ?><methodCall><methodName>tl.getTestCasesForTestPlan</methodName>"
                          +"<params>\n<param>\n<value>\n<struct>\n<member>\n<name>devKey</name>"
                          + "<value><string>4d1a44022bc4382c6f732b5c6da29080</string></value></member>"
                          + "<member>\n<name>testplanid</name>\n<value><string>29</string></value></member>"
                          +"</struct>\n</value>\n</param>\n</params>\n</methodCall>";
		
		return requestXml;
	}

	/**
	 * Util method to obtain test plan Id for a given test plan
	 * @param testPlanName
	 */
	public static Integer getTestPlanIdByName(String testPlanName,String projectName)
	{
		String xml = getTestPlanByNameRequestXml(testPlanName,projectName);
		//String id = "";
		Integer id = null;
		try
		{
			String postResponse = postRequest(xml);
			//System.out.println("getTestPlanIdByName response is" + postResponse);
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docFactory.setNamespaceAware(true);
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new InputSource(new StringReader(postResponse)));
			doc.getDocumentElement().normalize();

			// get member tags
			NodeList listOfNameTags = doc.getElementsByTagName("member");
			for (int s = 0; s < listOfNameTags.getLength(); s++) 
			{
				Node firstMemberNode = listOfNameTags.item(s);
				if (firstMemberNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					org.w3c.dom.Element firstMemberElement = (org.w3c.dom.Element) firstMemberNode;
					String nameTag = firstMemberElement.getElementsByTagName("name").item(0).getTextContent();
					if(nameTag.contentEquals("id"))
					{
						id = Integer.parseInt(firstMemberElement.getElementsByTagName("string").item(0).getTextContent());
					}
				}
				if(id!=null)
					break;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * Xml request for getTestPlanByName method execution
	 */
	public static String getTestPlanByNameRequestXml(String testPlanName,String projectName) 
	{
		String xml = "<?xml version='1.0' ?><methodCall>"
				+ "<methodName>tl.getTestPlanByName</methodName>"
				+"<params>\n<param>\n<value>\n<struct>\n<member>"
				+"<name>devKey</name>" 
				+ "<value><string>" + DEV_KEY + "</string></value>"
				+"</member>\n<member>\n<name>testplanname</name>"
				+"<value><string>" + testPlanName + "</string></value>"
				+"</member>\n<member>\n<name>testprojectname</name>"
				+"<value><string>" +projectName + "</string></value>"
				+"</member>\n</struct>\n</value>\n</param>\n</params>\n</methodCall>";

		return xml;
	}

	/**
	 * Util method that executes the given request xml on test link server 
	 */
	public static String postRequest(String xml) throws IOException 
	{
		String postResponse = "";
		PostMethod post = new PostMethod(SERVER_URL);
		RequestEntity entity = new StringRequestEntity(xml, "text/xml; charset=iso-8859-1", null);
		try 
		{
			post.setRequestEntity(entity);
			post.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-1");
			HttpClient httpclient = new HttpClient();
			int result = httpclient.executeMethod(post);
			post.toString();
			System.out.println("Response status code: " + result);
			System.out.println("Response body: ");
			//System.out.println(post.getResponseBodyAsString());
			postResponse = post.getResponseBodyAsString();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			post.releaseConnection();
		}
		return postResponse;
	}
	
	
	

	public static ReportTCResultResponse uploadTCResultsAPI(Integer testCaseId,Integer testPlanId,ExecutionStatus status, String buildName,String notes,Integer platformId)
	{
		URL url = null;
		ReportTCResultResponse resultResponse = null;
		try {
			url = new URL(SERVER_URL);
		TestLinkAPI api = new TestLinkAPI(url,DEV_KEY);
		resultResponse = api.reportTCResult(testCaseId, null, testPlanId, status, null, buildName, notes, null, null, platformId, null, null, null);
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultResponse;
	}
	/**
	 * Util method that uploads status and notes for executed test cases in test link
	 * @param excelFilePath - path of excel sheet generated by GetTestCasesToRun java class
	 * @param testPlanName - name of test link test plan 
	 * @param testBuildName - name of test link build
	 * @param testPlanId - id of test link test plan
	 * @param jobName - name of hudson job
	 * @param projectName - name of test link project name ( Either paisa or product )
	 */
	public static void uploadTCResults(Integer testPlanId, String jobName, String buildTag, String projectName, String testPlanName,String testBuildName,String testLinkProjName) throws IOException 
	{
		String xmlOutputFilePath = System.getProperty("user.home") + "\\.hudson\\jobs\\" + jobName + "\\workspace\\" + projectName + "\\Results\\" + buildTag + "\\xml\\";
		//file generated by GetTestCasesToRun.java
		String filePath = System.getProperty("user.home") + "\\.hudson\\jobs\\" + jobName + "\\workspace\\" + buildTag + ".xls";
       // String mynotesPath = "file:///" + System.getProperty("user.home") + "\\.hudson\\jobs\\" + jobName + "\\workspace\\" + projectName + "\\Results\\" + buildTag + "\\html\\index.html";
		//String status;
		ExecutionStatus status = null;
		String notes;
		String resultClassReport;

		//String tcId ;
		Integer tcId;
		String tcClass;
		String tcMethod;

		try 
		{
			FileInputStream file = new FileInputStream(new File(filePath));

			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) 
			{
				//tcId = sheet.getRow(i).getCell(0).toString();
				tcId = Integer.parseInt(sheet.getRow(i).getCell(0).toString());
				tcClass = sheet.getRow(i).getCell(1).toString();
				tcMethod = sheet.getRow(i).getCell(2).toString();

				//status = "";
				notes = "";

				System.out.println("TC Id is - " + tcId);
				System.out.println("TC Method is -" + tcMethod);

				InetAddress ownIP = InetAddress.getLocalHost();
				String ip = ownIP.getHostAddress();
                //NOTE: Hardcoding windows 8 ip address
				ip = "10.100.17.210";
				notes =   ip + "/" +projectName +"/Results/" + buildTag +  "/html/index.html";
				resultClassReport = xmlOutputFilePath + tcClass + "_results.xml";
				
				System.out.println("Open File -" + resultClassReport);
				File f = new File(resultClassReport);
				
				if (f.exists()) 
				{
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
					Document doc = docBuilder.parse(new File(resultClassReport));
					doc.getDocumentElement().normalize();

					// get testcase tags
					NodeList listOfTestCases = doc.getElementsByTagName("testcase");

					//TODO - Flaw with this is that for every method of same class we will open the _results.xml and parse. 
					//Rather can we open the _results.xml once and push all the results of it.
					//TODO - Vidya: to address this flaw we can convert the xml document into a javaobject using JAX and then access results to update to testlink

					for (int s = 0; s < listOfTestCases.getLength(); s++) 
					{
						Node firstTestCaseNode = listOfTestCases.item(s);
						if (firstTestCaseNode.getNodeType() == Node.ELEMENT_NODE) 
						{
							org.w3c.dom.Element firstTestCaseElement = (org.w3c.dom.Element) firstTestCaseNode;
							String testCaseName = firstTestCaseElement.getAttribute("name");
							if(testCaseName.contentEquals(tcMethod))
							{
								System.out.println("Found testcase " + testCaseName + "in" + resultClassReport);
								//check if test passed/failed
								NodeList failureNodes = firstTestCaseElement.getChildNodes();
								if(!(failureNodes.item(1)==null) && failureNodes.item(1).getNodeName().contentEquals("skipped"))
								{
								status = ExecutionStatus.BLOCKED;
								//upload only result as blocked
								uploadTCResultsAPI(tcId, testPlanId, status, testBuildName, notes, 0);
								
								}
								else if(!(failureNodes.item(1)==null) && failureNodes.item(1).getNodeName().contentEquals("failure"))
								{
									status = ExecutionStatus.FAILED;
									//TODO: change to notes for running in desktop system
									//mynotesPath = mynotesPath + failureNodes.item(1).getTextContent();
									notes = notes + failureNodes.item(1).getTextContent();
									if(notes.contains("<"))
									{  
										notes = notes.replaceAll("<", "(");
										//mynotesPath =mynotesPath.replaceAll("<", "(");;
										
									}
									//upload results and attachement
									ReportTCResultResponse resultResponse = uploadTCResultsAPI(tcId, testPlanId, status, testBuildName, notes, 0);
									Integer executionId = resultResponse.getExecutionId();
									String resultPath = System.getProperty("user.home") + "\\.hudson\\jobs\\" + jobName + "\\workspace\\" + projectName + "\\Results\\" + buildTag + "\\html\\";
									//upload screenshot attachment
									uploadAttachment(executionId,resultPath,tcMethod);
									
								}
								else
								{
									//status = "p";
									status = ExecutionStatus.PASSED;
									//upload only result as passed
									uploadTCResultsAPI(tcId, testPlanId, status, testBuildName, notes, 0);
									
								}
							}
						}
						/*if(status!=null)
							break;*/
					}


					//System.out.println("status is - " + status);
					//System.out.println("notes is - " + notes);
					//System.out.println("my system notes is - " + mynotesPath);

					//TODO: Uncomment first and comment second for running in desktop system
					//String xml = updateTCResultsRequestXml(tcId,testPlanId,notes,status,testBuildName,testPlanName,"0", testLinkProjName);
					//String xml = updateTCResultsRequestXml(tcId,testPlanId,mynotesPath,status,testBuildName,testPlanName,"0", testLinkProjName);
					/*ReportTCResultResponse resultResponse = uploadTCResultsAPI(tcId, testPlanId, status, testBuildName, notes, 0);
					Integer executionId = resultResponse.getExecutionId();
					String resultPath = System.getProperty("user.home") + "\\.hudson\\jobs\\" + jobName + "\\workspace\\" + projectName + "\\Results\\" + buildTag + "\\html\\";
					//upload screenshot attachment
					uploadAttachment(executionId,resultPath,tcMethod);*/
					//String response = postRequest(xml);
					// JSONObject jsonObj = XML.toJSONObject(response); 
						
					//System.out.println(response);
				}
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Exception caught ="+e.getMessage());
		}
	}

	public static void uploadAttachment(Integer executionId,String resultPath,String testCaseName)
	{
		URL url = null;
		Attachment resultResponse = null;
		try {
			url = new URL(SERVER_URL);
		TestLinkAPI api = new TestLinkAPI(url,DEV_KEY);
		File attachmentFile = findScreenshotFile(resultPath,testCaseName);
		
      //  File attachmentFile = new File("C:\\Users\\vidya.priyadarshini\\.hudson\\jobs\\RunTestLink\\workspace\\Product\\Results\\hudson-RunTestLink-37\\html\\2014-02-18_10-14-04_Test.AdminPanel.RiskRules.CategorizedDenyRule.MaxamountDenyOnCategories.Maxamount_deny_rule_travel.png");
        if(attachmentFile!=null && attachmentFile.length()<1048576)
        {
        String fileContent = null;
        
        try {
                byte[] byteArray = FileUtils.readFileToByteArray(attachmentFile);
                fileContent = new String(Base64.encodeBase64(byteArray));
        } catch (IOException e) {
                e.printStackTrace( System.err );
                System.exit(-1);
        }
		resultResponse = api.uploadExecutionAttachment(executionId, "Screenshot", "Failure Screenshot", attachmentFile.getName(), "image/png", fileContent);
		}
        else if(attachmentFile.length()>=1048576)
        {
        	System.out.println("Screen shot for test case "+ testCaseName + " is not attached due to size greater than 1MB");
        }
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
	//	return resultResponse;
		
	}
	
	public static File findScreenshotFile(String resultsPath,String testCaseName)
	{
		// File representing the folder that you select using a FileChooser
	    final File dir = new File(resultsPath);
        File screenshotFile = null;
	    // array of supported extensions (use a List if you prefer)
	    final String[] EXTENSIONS = new String[]{
	        "jpg", "png", "bmp" // and other formats you need
	    };
	    // filter to identify images based on their extensions
	    final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

	       
	        public boolean accept(final File dir, final String name) {
	            for (final String ext : EXTENSIONS) {
	                if (name.endsWith("." + ext)) {
	                    return (true);
	                }
	            }
	            return (false);
	        }
	    };

	
	        if (dir.isDirectory()) { // make sure it's a directory
	            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                     if(f.getName().endsWith(testCaseName + ".png"))
                     {
                    	screenshotFile =f; 
                     System.out.println("Found screenshot file for" + testCaseName);
                     break;
                     }
	             
	            }
	        }
	        return screenshotFile;
	    }
	
	/**
	 * Xml request for updateTCResults method execution
	 */
	public static String updateTCResultsRequestXml(String tcId, String testPlanId, String notes, String status, String buildName, String testPlanName,String testPlatformId, String projectName) 
	{
		String xml = "<?xml version='1.0' ?>"
				+"<methodCall>\n<methodName>tl.reportTCResult</methodName>"
				+"<params>\n<param>\n<value>\n<struct>\n<member>"
				+"<name>devKey</name>"
				+"<value><string>" + DEV_KEY + "</string></value>"
				+"</member>\n<member>\n<name>testprojectname</name>"
				+"<value><string>" + projectName + "</string></value>"
				+"</member>\n<member>\n<name>testplanname</name>"
				+"<value><string>" + testPlanName + "</string></value>"
				+"</member>\n" + "<member>\n<name>testplanid</name>"
				+"<value><string>" + testPlanId + "</string></value>"
				+"</member>\n"+
				"<member>\n<name>testcaseid</name>"
				+"<value><string>" + tcId + "</string></value>"
				+"</member>\n<member>\n<name>status</name>"
				+"<value><string>" + status + "</string></value>"
				+"</member>\n<member>\n<name>buildname</name>" 
				+"<value><string>" + buildName + "</string></value>"
				+"</member>\n<member>\n<name>platformid</name>" 
				+"<value><string>" +testPlatformId + "</string></value>"
				+"</member>\n<member>\n<name>notes</name>"
				+"<value><string>" + notes + "</string></value>"
				+"</member>\n</struct>\n</value>\n</param>\n</params>\n</methodCall>";

		System.out.println("xml request is" + xml);
		return xml;
	}
}