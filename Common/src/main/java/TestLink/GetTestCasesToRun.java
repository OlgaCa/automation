package TestLink;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Program that reads test case class and method names and test link test case ids(from test link project) and writes them to an excel sheet named after hudson build tag(used as timestamp).
 * This excel sheet is read by Utils.GenerateTestNGXmlAndRun class to generate the testng.xml file
 * @author vidya.priyadarshini
 *
 */
public class GetTestCasesToRun {

	public static void main(String[] args) throws IOException {

		//hudson Job Name - args[0]
		//hudson Build Tag - args[1]
		//Java Test Class - args[2]
		//Java Test Method - args[3]
		//TestLink TC ID - args[4]

		String filePath = System.getProperty("user.home") + "\\.hudson\\jobs\\" + args[0] + "\\workspace\\" + args[1] + ".xls";
		File f = new File(filePath);

		if(!f.exists()) 
		{
			System.out.println("File - " + filePath + " does not exists.");
			System.out.println("Creating file - " +  filePath);
			FileOutputStream fileout = new FileOutputStream(filePath);

			System.out.println("Writing header row - TCId, JavaClassName, JavaMethodName");
			HSSFWorkbook workbook=new HSSFWorkbook();
			HSSFSheet sheet =  workbook.createSheet("FirstSheet");  

			HSSFRow rowhead =   sheet.createRow((short)0);
			rowhead.createCell(0).setCellValue("TCId");
			rowhead.createCell(1).setCellValue("JavaClassName");
			rowhead.createCell(2).setCellValue("JavaMethodName");

			workbook.write(fileout);

			fileout.flush();
			fileout.close();
		}

		System.out.println("Reading file - " +  filePath);
		FileInputStream fileInputStream = new FileInputStream(filePath);
		HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
		HSSFSheet sheet = workbook.getSheet("FirstSheet");

		//get the current number of rows
		int numRows = sheet.getLastRowNum();

		System.out.println("Writing row num: " + numRows+1 + " - " + args[4] + ", " + args[2] + ", " + args[3]);
		HSSFRow rowhead=   sheet.createRow((short)numRows+1);
		rowhead.createCell(0).setCellValue(args[4]);
		rowhead.createCell(1).setCellValue(args[2]);
		rowhead.createCell(2).setCellValue(args[3]);

		FileOutputStream fileout = new FileOutputStream(filePath);
		workbook.write(fileout);

		fileInputStream.close();
		fileout.flush();
		fileout.close();
	}
}