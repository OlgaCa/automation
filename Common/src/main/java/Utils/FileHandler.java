package Utils;

import java.awt.AWTException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class FileHandler {
	private static final HSSFCell NULL = null;
	static String ReturnVal;	
	static int rc,cc;
	static String[][] ar = new String[100][100];
	private Config testConfig;


	/**
	 * Get no.of rows on worksheet
	 * @param WBRead : file name of the workbook
	 * @param SSRead: sheet name of the workbook
	 * @return
	 */
	static int getRowCount(String WBRead, String SSRead){
		try{
			FileInputStream fileInputStream = new FileInputStream(WBRead);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet(SSRead);

			rc = worksheet.getLastRowNum();
			//fileInputStream.close();
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rc;
	}


	/**
	 * Read data from Excel
	 * @param WBRead : workbook name
	 * @param SSRead: workbook sheet name
	 * @param RowRead : workbook Row no
	 * @param ColumnRead: workbook column no
	 * @return
	 */
	static String reader(String WBRead, String SSRead, int RowRead, int ColumnRead){
		try{

			FileInputStream fileInputStream = new FileInputStream(WBRead);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet(SSRead);

			HSSFRow row1 = worksheet.getRow(RowRead);
			//HSSFCell dataCell= (HSSFCell)row1.GetCell(ColumnRead, NPOI.SS.UserModel.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			HSSFCell c1 = row1.getCell(ColumnRead, Row.RETURN_NULL_AND_BLANK);

			if(c1 != null)
				ReturnVal = c1.getStringCellValue().trim();

			else{
				ReturnVal = " ";
			}
			//fileInputStream.close();
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ReturnVal;

	}


	/**
	 * 	 Creates an array of all the values in the excel

	 * @param WBRead: workbook name to be select
	 * @param SSRead: workbook sheet name to be select
	 * @return
	 */
	static String[][] replica(String WBRead, String SSRead){
		try{

			FileInputStream fileInputStream = new FileInputStream(WBRead);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet(SSRead);
			int rc,cc;
			String temp="";
			rc = worksheet.getLastRowNum();
			System.out.println(rc);

			for(int i=0; i<=rc; i++){
				HSSFRow row1 = worksheet.getRow(i);
				cc = row1.getPhysicalNumberOfCells();
				System.out.println(cc);

				for(int j=0 ; j<=cc; j++){
					HSSFCell c1 = row1.getCell(j, Row.RETURN_NULL_AND_BLANK);

					if(c1!=null){											
						temp = c1.getStringCellValue().trim();
						HSSFCellStyle cells = c1.getCellStyle();
						c1.setCellStyle(cells);
					}
					else{
						temp= " ";
					}

					ar[i][j] = temp;
					System.out.println(ar[i][j]);
				}			
			}

			System.out.println("done");

		}	
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ar;

	}


	/**
	 * 	// TO get no. of columns in a row
	 * @param WBRead : workbook name to be select
	 * @param SSRead:  workbook sheet name to be select
	 * @param RowRead: workbook sheet row no to be select
	 * @return
	 */
	static int colcount(String WBRead, String SSRead, int RowRead){
		try{

			FileInputStream fileInputStream = new FileInputStream(WBRead);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet(SSRead);

			HSSFRow row1 = worksheet.getRow(RowRead);

			cc = row1.getPhysicalNumberOfCells();
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cc;

	}


	/**
	 * 	// Normal write function
	 * @param WBRead : workbook name to be select
	 * @param SSRead : workbook sheet name to be select
	 * @param RowRead: workbook no of row
	 * @param colwr: workbook no of column
	 * @param str: workbook output
	 */
	static void write(String WBRead, String SSRead, int RowRead, int colwr, String str){
		try{

			FileOutputStream fileout = new FileOutputStream(WBRead);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet(SSRead);
			HSSFFont font = workbook.createFont();

			HSSFRow row1 = worksheet.createRow(RowRead);

			HSSFCell c1 = row1.createCell(colwr);
			c1.setCellValue(str);				
			worksheet.setColumnWidth(colwr, 5000);
			font.setFontName("Calibri");
			//font.setColor(HSSFColor.GREEN.index);
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);
			c1.setCellStyle(cellStyle);		



			workbook.write(fileout);
			fileout.flush();
			fileout.close();

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}



	/**
	 *Create result replica
	 * @param WBRead : workbook name to be copied
	 * @param SSRead: workbook sheet name to be copied
	 * @param WBwrite: workbook name as a output
	 * @param SSwrite: workbook sheet name as a output
	 */
	public static void Create_result_templete(String WBRead, String SSRead, String WBwrite, String SSwrite){
		try{
			String[][] repwr = new String[100][100];
			FileOutputStream fileout = new FileOutputStream(WBwrite);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet(SSwrite); 			
			HSSFFont font = workbook.createFont();

			repwr = replica(WBRead, SSRead);

			int rc = getRowCount(WBRead, SSRead);			
			for(int i=0; i<=rc; i++){
				int cc = colcount(WBRead, SSRead, i);
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
	}	


	/**

	 * Write data to an existing workbook.
	 * @param WBRead: workbook name to be edit
	 * @param SSRead:  workbook sheet name to be edit
	 * @param RowRead:  workbook row no to be edit
	 * @param colwr: workbook column no to be edit
	 * @param str: string to be edit
	 * @param cflag: color the edited cell 
	 */
	public static void write_edit(String WBRead, String SSRead, int RowRead, int colwr, String str,int cflag){
		try{

			FileInputStream fileInputStream = new FileInputStream(WBRead);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet(SSRead);
			//HSSFFont font = workbook.createFont();			

			HSSFRow row = null;
			row = worksheet.getRow(RowRead);
			if (row == null){
				row = worksheet.createRow(RowRead);
			}
			HSSFCell c1 = row.createCell(colwr);
			worksheet.setColumnWidth(colwr, 4200);
			c1.setCellValue(str);
			FileOutputStream fileOut = new FileOutputStream(WBRead);

			workbook.write(fileOut);
			fileOut.close();

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void write_edit(String WBRead, String SSRead, int RowRead, int colwr, String str){
		write_edit( WBRead,  SSRead,  RowRead,  colwr,  str,0);
	}

	/**
	 * Write data to an existing xlsx workbook.
	 * @param WBRead: workbook name to be edit
	 * @param SSRead:  workbook sheet name to be edit
	 * @param RowRead:  workbook row no to be edit
	 * @param colwr: workbook column no to be edit
	 * @param str: string to be edit
	 * @param cflag: color the edited cell 
	 * @throws AWTException 
	 */
	public static void write_editXSS(String WBRead, String SSRead, int RowRead, int colwr, String str,int cflag) throws AWTException{
		try{

			FileInputStream fileInputStream = new FileInputStream(WBRead);
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet worksheet = workbook.getSheet(SSRead);
			//HSSFFont font = workbook.createFont();			

			XSSFRow row = null;
			row = worksheet.getRow(RowRead);
			if (row == null){
				row = worksheet.createRow(RowRead);
			}
			XSSFCell c1 = row.createCell(colwr);
			worksheet.setColumnWidth(colwr, 4200);
			c1.setCellValue(str);


			XSSFFont font = workbook.createFont();
			if(cflag==1){
				font.setFontName("Calibri");
				font.setColor(HSSFColor.RED.index);
				XSSFCellStyle cellStyle2 = workbook.createCellStyle();
				cellStyle2.setFont(font);
				c1.setCellStyle(cellStyle2);
				//System.out.println("done1");
			}

			else if(cflag==0){
				font.setFontName("Calibri");
				font.setColor(HSSFColor.GREEN.index);
				XSSFCellStyle cellStyle2 = workbook.createCellStyle();
				cellStyle2.setFont(font);
				c1.setCellStyle(cellStyle2);
			}

			FileOutputStream fileOut = new FileOutputStream(WBRead);

			workbook.write(fileOut);
			fileOut.close();			

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write_toedit(String WBRead, String SSRead, int RowRead, int colwr, String str,String status, int cflag){
		try{

			FileInputStream fileInputStream = new FileInputStream(WBRead);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet(SSRead);
			//HSSFFont font = workbook.createFont();			
			System.out.println("########"+RowRead+"#########");
			HSSFRow row = null;
			row = worksheet.getRow(RowRead);
			if (row == null){
				row = worksheet.createRow(RowRead);
			}
			HSSFCell c1 = row.createCell(colwr);
			worksheet.setColumnWidth(colwr, 4200);
			c1.setCellValue(str);



			HSSFFont font = workbook.createFont();
			if(cflag==1){
				font.setFontName("Calibri");
				font.setColor(HSSFColor.RED.index);
				HSSFCellStyle cellStyle2 = workbook.createCellStyle();
				cellStyle2.setFont(font);
				c1.setCellStyle(cellStyle2);
				//System.out.println("done1");
			}

			else if(cflag==0){
				font.setFontName("Calibri");
				font.setColor(HSSFColor.GREEN.index);
				HSSFCellStyle cellStyle2 = workbook.createCellStyle();
				cellStyle2.setFont(font);
				c1.setCellStyle(cellStyle2);
			}

			FileOutputStream fileOut = new FileOutputStream(WBRead);

			workbook.write(fileOut);
			fileOut.close();

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}



	}

	/* Set cell data blank.
	 * @param WBRead: workbook name to be edit
	 * @param SSRead:  workbook sheet name to be edit
	 * @param RowRead:  workbook row no to be edit
	 * @param colwr: workbook column no to be edit
	 */
	public static void setCellBlank(String WBRead, String SSRead, int RowRead, int colwr){
		try{

			FileInputStream fileInputStream = new FileInputStream(WBRead);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet(SSRead);
			//HSSFFont font = workbook.createFont();			

			HSSFRow row = null;
			row = worksheet.getRow(RowRead);
			if (row == null){
				row = worksheet.createRow(RowRead);
			}
			HSSFCell c1 = row.createCell(colwr);
			worksheet.setColumnWidth(colwr, 4200);
			c1.removeCellComment();
			FileOutputStream fileOut = new FileOutputStream(WBRead);

			workbook.write(fileOut);
			fileOut.close();

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Set row data blank upto a number of columns.
	 * @param WBRead: workbook name to be edit
	 * @param SSRead:  workbook sheet name to be edit
	 * @param RowRead:  workbook row no to make blank
	 * @param colwr: number of columns to make blank in a row
	 */
	public static void setRowBlank(String WBRead, String SSRead, int RowRead, int colwr){
		try{
			FileInputStream fileInputStream = new FileInputStream(WBRead);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet(SSRead);
			//HSSFFont font = workbook.createFont();			

			HSSFRow row = null;
			row = worksheet.getRow(RowRead);
			if (row == null){
				row = worksheet.createRow(RowRead);
			}
			for(int i=0; i<colwr; i++)
			{
				HSSFCell cell = row.createCell(i);
				worksheet.setColumnWidth(i, 4200);
				cell.removeCellComment();
				FileOutputStream fileOut = new FileOutputStream(WBRead);
				workbook.write(fileOut);

				fileOut.close();

			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write_editXLS(Config testConfig, String WorkBookName, String SheetName, int Row, int Column, String InputData, String description){
		try{

			FileInputStream fileInputStream = new FileInputStream(WorkBookName);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet(SheetName);
			//HSSFFont font = workbook.createFont();			

			HSSFRow row = null;
			row = worksheet.getRow(Row);
			if (row == null){
				row = worksheet.createRow(Row);
			}
			HSSFCell c1 = row.createCell(Column);
			worksheet.setColumnWidth(Column, 4200);
			c1.setCellValue(InputData);
			FileOutputStream fileOut = new FileOutputStream(WorkBookName);
			testConfig.logComment("Entered Value of "+description+" as "+InputData);
			workbook.write(fileOut);
			fileOut.close();

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**Converts xml file into excel file
	 * if OutputFileName empty then file is generated in downloads folder and its path is returned
	 * @param testConfig
	 * @param filename - target file
	 * @param OutputFileName- Output file target (if empty file is generated in downloads folder)
	 * @return file path of generated file
	 */
	public static String saveXMLAsExcel(Config testConfig, String filename, String OutputFileName){

		try {
			if(OutputFileName.isEmpty())
				OutputFileName = System.getProperty("user.home") + "\\Downloads\\"+Helper.getCurrentDateTime("HH-mm-ss")+".xls";
			// Creating a Workbook
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet SettlementInputXLS = wb.createSheet("SettlementInputXLS");
			HSSFRow XLSrow;
			HSSFCell XLScell;

			// Parsing XML Document
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = (Document) dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			
			NodeList rows = doc.getElementsByTagName("Row");
			
			//Copying data in new XLS
			for (int rowSize = 0; rowSize < rows.getLength(); rowSize++) 
			{
				Node row = rows.item(rowSize);	
				NodeList cols = row.getChildNodes();
				XLSrow = SettlementInputXLS.createRow(rowSize);
				for(int Column=0; Column<cols.getLength(); Column++)
				{
					Node col = cols.item(Column);
					String Inputdata = col.getTextContent();
					if(Inputdata.equals("\n"))
						continue;
					XLScell= XLSrow.createCell(Column);
					SettlementInputXLS.setColumnWidth(Column, 4200);
					XLScell.setCellValue(Inputdata);
					testConfig.logComment("Entered Value of "+Inputdata);
				}	
			}
			// Outputting to Excel spreadsheet
	         FileOutputStream output = new FileOutputStream(new File(OutputFileName));
	         wb.write(output);
	         output.flush();
	         output.close();
	         Log.Comment("File: "+filename+" has been saved as Excel with name as: "+OutputFileName,testConfig);
	         return OutputFileName;
	      }
		catch (IOException e) {
			testConfig.logComment("IOException " + e.getMessage());
	         return null;
	      } catch (ParserConfigurationException e) {
	    	  testConfig.logComment("ParserConfigurationException " + e.getMessage());
	         return null;
	      } catch (SAXException e) {
	    	  testConfig.logComment("SAXException " + e.getMessage());
	         return OutputFileName;
	      }
	}

}	
