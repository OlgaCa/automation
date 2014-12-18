package Test.AdminPanel.Home;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import PageObject.AdminPanel.DownloadExcel.DownloadExcelPage;
import Test.AdminPanel.Payments.TransactionHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestDownloadExcel extends TestBase{
	
	@Test(description = "Verify download excel succesful refund", dataProvider="GetTestConfig", timeOut=600000, groups="1")
	public void test_VerifyDownloadExcelSuccessfulRefund(Config testConfig) {
		
			
		
		TransactionHelper helper = new TransactionHelper(testConfig, false);
		helper.DoLogin();
		DownloadExcelPage  downloadExcelPage = helper.GetTestDownloadExcelPage();
		
		//select date one day before
		int dd = Integer.parseInt(Element.getFirstSelectedOption(testConfig, Element.getPageElement(testConfig, How.name, "day"), "date").getText())-1;
		int mm = Integer.parseInt(Element.getFirstSelectedOption(testConfig, Element.getPageElement(testConfig, How.name, "monthnum"), "date").getText());
		int yyyy = Integer.parseInt(Element.getFirstSelectedOption(testConfig, Element.getPageElement(testConfig, How.name, "year"), "date").getText());
		
		String filenamedate = Helper.getDate(dd, mm, yyyy,"yyyyddMM");
		File f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//successfulrefundList_"+filenamedate+"_archive.xls.zip");
		if(f.exists()) {
			f.delete();
		}
		
		f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//successfulrefundList_"+filenamedate+".xls");
		if(f.exists()) {
			f.delete();
		}
		
		downloadExcelPage.selectDate(dd,mm,yyyy);
		Element.click(testConfig, downloadExcelPage.getSuccessRefundListButton(), "Positive successful refund list") ;
		Element.click(testConfig, downloadExcelPage.getSubmitButton(), "submit") ;
		
		
		
		String xlFile = getExcelFile(testConfig,"successfulrefundList_"+filenamedate);
		
		String to = Helper.getDate(dd, mm, yyyy, "yyyy-MM-dd");
		String from = Helper.getDatePreviousTo(dd, mm, yyyy, "yyyy-MM-dd");
		testConfig.putRunTimeProperty("from", from);
		testConfig.putRunTimeProperty("to", to);
		testConfig.putRunTimeProperty("action", "refund");
		Map<String,String> map = DataBase.executeSelectQuery(testConfig, 21, 1);

		Browser.wait(testConfig,20);
		TestDataReader tr = new TestDataReader(testConfig,"Sheet1", xlFile);
		Helper.compareEquals(testConfig, "No of records success refund", (map.get("count")), (tr.getRecordsNum()-1)+"");
		if(tr.getRecordsNum()>1){
		int rowToCheck =1 ;
		String id = tr.GetData(rowToCheck, "RequestID");
		testConfig.putRunTimeProperty("id", id);
		map = DataBase.executeSelectQuery(testConfig, 22, 1);
		Helper.compareExcelEquals(testConfig, "checking txnid", map.get("txnid"),tr.GetData(rowToCheck, "TransactionID"));
		Helper.compareExcelEquals(testConfig, "checking trace_no", map.get("trace_no"),tr.GetData(rowToCheck, "Trace Number"));
		Helper.compareExcelEquals(testConfig, "checking bank_ref_no", map.get("bank_ref_no"),tr.GetData(rowToCheck, "Bank Ref #"));
		Helper.compareExcelEquals(testConfig, "checking merchant_key", map.get("merchant_key"),tr.GetData(rowToCheck, "Merchant Key"));
		Helper.compareExcelEquals(testConfig, "checking merchant_name", map.get("merchant_name"),tr.GetData(rowToCheck, "Merchant Name"));
		Helper.compareExcelEquals(testConfig, "checking payu_merchant_id", map.get("payu_merchant_id"),tr.GetData(rowToCheck, "PayU Merchant ID"));
		Helper.compareExcelEquals(testConfig, "checking merchant_site", map.get("merchant_site"),tr.GetData(rowToCheck, "merchant Site"));
		Helper.compareExcelEquals(testConfig, "checking pg_key", map.get("pg_key"),tr.GetData(rowToCheck, "Payment Gateway"));
		Helper.compareExcelEquals(testConfig, "checking amountpaid", map.get("amountpaid"),tr.GetData(rowToCheck, "Amount Paid"));
		Helper.compareExcelEquals(testConfig, "checking transaction_fee", map.get("transaction_fee"),tr.GetData(rowToCheck, "Transaction Fee"));
		Helper.compareExcelEquals(testConfig, "checking amount", map.get("amount"),tr.GetData(rowToCheck, "Amount refund"));
		Helper.compareExcelEquals(testConfig, "checking discount", map.get("discount"),tr.GetData(rowToCheck, "Discount"));
		Helper.compareExcelEquals(testConfig, "checking created_at", map.get("created_at").split(" ")[0],tr.GetData(rowToCheck, "refund Request Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "checking addedon", map.get("addedon").split(" ")[0],tr.GetData(rowToCheck, "Transaction Creation Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "checking addedon", map.get("success_at").split(" ")[0],tr.GetData(rowToCheck, "refund Success Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "currency_type", map.get("currency_type"),tr.GetData(rowToCheck, "Currency"));
		
		}
	 	for(int i=1;i<tr.getRecordsNum();i++){;
	 	String data = tr.GetData(i, "refund Success Timestamp");
	 		Helper.compareEquals(testConfig, "checking dates", data.toString().split(" ")[0], Helper.getDatePreviousTo(dd, mm, yyyy, "yyyy-MM-dd"));
	 	
	 	}
	 	
	 	
	 	
	 	
	 	 f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//txnList_"+filenamedate+"_archive.xls.zip");
		if(f.exists()) {
			f.delete();
		}
		
		f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//txnList_"+filenamedate+".xls");
		if(f.exists()) {
			f.delete();
		}
	 	Browser.wait(testConfig,10);
	 	downloadExcelPage.selectDate(dd,mm,yyyy);
		Element.click(testConfig, downloadExcelPage.getTransactionListButton(), "Positive transaction list") ;
		Element.click(testConfig, downloadExcelPage.getSubmitButton(), "submit") ;
		xlFile = getExcelFile(testConfig,"txnList_"+filenamedate);
		map = DataBase.executeSelectQuery(testConfig, 23, 1);
		Browser.wait(testConfig,10);
		tr = new TestDataReader(testConfig,"Sheet1", xlFile);
		Helper.compareEquals(testConfig, "No of records transaction", (map.get("count")), (tr.getRecordsNum()-1)+"");
		if(tr.getRecordsNum()>1){
		int rowToCheck =1 ;
		String id = tr.GetData(rowToCheck, "PayU ID");
		testConfig.putRunTimeProperty("id", id);
		map = DataBase.executeSelectQuery(testConfig, 24, 1);
		
		Helper.compareExcelEquals(testConfig, "checking payuid", map.get("id"),tr.GetData(rowToCheck, "PayU ID"));
		Helper.compareExcelEquals(testConfig, "checking trace_no", map.get("trace_no"),tr.GetData(rowToCheck, "Trace Number"));
		Helper.compareExcelEquals(testConfig, "checking bank_ref_no", map.get("txnid"),tr.GetData(rowToCheck, "Merchant TxnID"));
		Helper.compareExcelEquals(testConfig, "checking merchant_key", map.get("merchant_key"),tr.GetData(rowToCheck, "Merchant Key"));
		Helper.compareExcelEquals(testConfig, "checking merchant_name", map.get("merchant_name"),tr.GetData(rowToCheck, "Merchant Name"));
		Helper.compareExcelEquals(testConfig, "checking payu_merchant_id", map.get("payu_merchant_id"),tr.GetData(rowToCheck, "PayU Merchant ID"));
		Helper.compareExcelEquals(testConfig, "checking merchant_site", map.get("merchant_site"),tr.GetData(rowToCheck, "merchant Site"));
		//Helper.compareEquals(testConfig, "checking merchant_site", map.get("pgmid"),tr.GetData(rowToCheck, "pgMID"));
		Helper.compareExcelEquals(testConfig, "checking pg_key", map.get("pg_key"),tr.GetData(rowToCheck, "Payment Gateway"));
		Helper.compareExcelEquals(testConfig, "checking transaction_fee", map.get("transaction_fee"),tr.GetData(rowToCheck, "Transaction Fee"));
		
		Helper.compareExcelEquals(testConfig, "checking discount", map.get("discount"),tr.GetData(rowToCheck, "Discount"));
		Helper.compareExcelEquals(testConfig, "checking amountpaid", map.get("amount"),tr.GetData(rowToCheck, "Amount Paid"));
		Helper.compareExcelEquals(testConfig, "checking created_at", map.get("status"),tr.GetData(rowToCheck, "Status"));
		Helper.compareExcelEquals(testConfig, "checking addedon", map.get("addedon").split(" ")[0],tr.GetData(rowToCheck, "Time").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "checking firstname", map.get("firstname"),tr.GetData(rowToCheck, "First Name"));
		Helper.compareExcelEquals(testConfig, "checking lastname", map.get("lastname"),tr.GetData(rowToCheck, "Last Name"));
		Helper.compareExcelEquals(testConfig, "checking email", map.get("email"),tr.GetData(rowToCheck, "Email"));
		Helper.compareExcelEquals(testConfig, "checking phone", map.get("phone"),tr.GetData(rowToCheck, "Phone"));
		Helper.compareExcelEquals(testConfig, "checking ip", map.get("ip"),tr.GetData(rowToCheck, "IP Address"));
		
		Helper.compareExcelEquals(testConfig, "checking bank name", map.get("bank_name"),tr.GetData(rowToCheck, "Bank Name"));
		Helper.compareExcelEquals(testConfig, "checking bank_ref_no", map.get("bank_ref_no"),tr.GetData(rowToCheck, "Bank Ref #"));
		Helper.compareExcelEquals(testConfig, "checking merchant_name", map.get("card_bin"),tr.GetData(rowToCheck, "Card Bin"));
		Helper.compareExcelEquals(testConfig, "checking card_bin", map.get("name_on_card"),tr.GetData(rowToCheck, "Name on Card"));
		Helper.compareExcelEquals(testConfig, "checking card_no", map.get("card_no"),tr.GetData(rowToCheck, "Card #"));
		Helper.compareExcelEquals(testConfig, "checking product_info", map.get("productinfo"),tr.GetData(rowToCheck, "Product Info"));
		Helper.compareExcelEquals(testConfig, "checking address1", map.get("address1").trim(),tr.GetData(rowToCheck, "Address 1").trim());
		Helper.compareExcelEquals(testConfig, "checking address2", map.get("address2").trim(),tr.GetData(rowToCheck, "Address 2").trim());
		Helper.compareExcelEquals(testConfig, "checking city", map.get("city"),tr.GetData(rowToCheck, "City"));
		Helper.compareExcelEquals(testConfig, "checking state", map.get("state"),tr.GetData(rowToCheck, "State"));
		Helper.compareExcelEquals(testConfig, "checking country", map.get("country"),tr.GetData(rowToCheck, "Country"));
		Helper.compareExcelEquals(testConfig, "checking country", map.get("zipcode"),tr.GetData(rowToCheck, "ZIP Code"));
		Helper.compareExcelEquals(testConfig, "currency_type", map.get("currency_type"),tr.GetData(rowToCheck, "Currency"));
		
		}
	 	for(int i=1;i<tr.getRecordsNum();i++){;
	 	String data = tr.GetData(i, "Time");
	 	Helper.compareEquals(testConfig, "checking dates", data.toString().split(" ")[0], Helper.getDatePreviousTo(dd, mm, yyyy, "yyyy-MM-dd"));
	 	
	 	}
	
	 	f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//intlTxnList_"+filenamedate+"_archive.xls.zip");
		if(f.exists()) {
			f.delete();
		}
		
		f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//intlTxnList_"+filenamedate+".xls");
		if(f.exists()) {
			f.delete();
		}
	 	Browser.wait(testConfig,10);
	 	downloadExcelPage.selectDate(dd,mm,yyyy);
		Element.click(testConfig, downloadExcelPage.getInternationalTransactionListButton(), "Positive international transaction list") ;
		Element.click(testConfig, downloadExcelPage.getSubmitButton(), "submit") ;
		
		map = DataBase.executeSelectQuery(testConfig, 27, 1);
		String domestic_card_string = map.get("value") ;
        
        
		xlFile = getExcelFile(testConfig,"intlTxnList_"+filenamedate);
		Browser.wait(testConfig,10);
		testConfig.putRunTimeProperty("domesticBins", domestic_card_string);
		map = DataBase.executeSelectQuery(testConfig, 25, 1);
		tr = new TestDataReader(testConfig,"Sheet1", xlFile);
		
		Helper.compareEquals(testConfig, "No of records success refund", (map.get("count")), (tr.getRecordsNum()-1)+"");
		if(tr.getRecordsNum()>1){
		int rowToCheck =1 ;
		String id = tr.GetData(rowToCheck, "TransactionID");
		testConfig.putRunTimeProperty("id", id);
		map = DataBase.executeSelectQuery(testConfig, 26, 1);
		Helper.compareExcelEquals(testConfig, "checking txnid", map.get("txnid"),tr.GetData(rowToCheck, "Merchant TxnID"));
		Helper.compareExcelEquals(testConfig, "checking merchant_key", map.get("merchant_key"),tr.GetData(rowToCheck, "Merchant Key"));
		Helper.compareExcelEquals(testConfig, "checking merchant_name", map.get("merchant_name"),tr.GetData(rowToCheck, "Merchant Name"));
		Helper.compareExcelEquals(testConfig, "checking pg_key", map.get("pg_key"),tr.GetData(rowToCheck, "Payment Gateway"));
		Helper.compareExcelEquals(testConfig, "checking transaction_fee", map.get("transaction_fee"),tr.GetData(rowToCheck, "Transaction Fee"));
		Helper.compareExcelEquals(testConfig, "checking discount", map.get("discount"),tr.GetData(rowToCheck, "Discount"));
		Helper.compareExcelEquals(testConfig, "checking amountpaid", map.get("amount"),tr.GetData(rowToCheck, "Amount Paid"));
		Helper.compareExcelEquals(testConfig, "checking status", map.get("status"),tr.GetData(rowToCheck, "Status"));
		Helper.compareExcelEquals(testConfig, "checking addedon", map.get("addedon").split(" ")[0],tr.GetData(rowToCheck, "Time").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "checking firstname", map.get("firstname"),tr.GetData(rowToCheck, "First Name"));
		Helper.compareExcelEquals(testConfig, "checking lastname", map.get("lastname"),tr.GetData(rowToCheck, "Last Name"));
		Helper.compareExcelEquals(testConfig, "checking email", map.get("email"),tr.GetData(rowToCheck, "Email"));
		Helper.compareExcelEquals(testConfig, "checking phone", map.get("phone"),tr.GetData(rowToCheck, "Phone"));
		Helper.compareExcelEquals(testConfig, "checking ip", map.get("ip"),tr.GetData(rowToCheck, "IP Address"));
		
		Helper.compareExcelEquals(testConfig, "checking bank name", map.get("bank_name"),tr.GetData(rowToCheck, "Bank Name"));
		Helper.compareExcelEquals(testConfig, "checking bank_ref_no", map.get("bank_ref_no"),tr.GetData(rowToCheck, "Bank Ref Num"));
		Helper.compareExcelEquals(testConfig, "checking merchant_name", map.get("card_bin"),tr.GetData(rowToCheck, "Card Bin"));
		Helper.compareExcelEquals(testConfig, "checking card_bin", map.get("name_on_card"),tr.GetData(rowToCheck, "Name on Card"));
		Helper.compareExcelEquals(testConfig, "checking card_no", map.get("card_no"),tr.GetData(rowToCheck, "Card #"));
		Helper.compareExcelEquals(testConfig, "checking product_info", map.get("productinfo"),tr.GetData(rowToCheck, "Product Info"));
		Helper.compareExcelEquals(testConfig, "checking address1", map.get("address1").trim(),tr.GetData(rowToCheck, "Address 1").trim());
		Helper.compareExcelEquals(testConfig, "checking address2", map.get("address2").trim(),tr.GetData(rowToCheck, "Address 2").trim());
		Helper.compareExcelEquals(testConfig, "checking city", map.get("city"),tr.GetData(rowToCheck, "City"));
		Helper.compareExcelEquals(testConfig, "checking state", map.get("state"),tr.GetData(rowToCheck, "State"));
		Helper.compareExcelEquals(testConfig, "checking country", map.get("country"),tr.GetData(rowToCheck, "Country"));
		Helper.compareExcelEquals(testConfig, "checking country", map.get("zipcode"),tr.GetData(rowToCheck, "ZIP Code"));
		Helper.compareExcelEquals(testConfig, "currency_type", map.get("currency_type"),tr.GetData(rowToCheck, "Currency"));

		
		}
		
		
		for(int i=1;i<tr.getRecordsNum();i++){
	 	String data = tr.GetData(i, "Time");
	 	Helper.compareEquals(testConfig, "checking dates", data.toString().split(" ")[0], Helper.getDatePreviousTo(dd, mm, yyyy, "yyyy-MM-dd"));
	 	
	 	}

		f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//successfulcapture_"+filenamedate+"_archive.xls.zip");
		if(f.exists()) {
			f.delete();
		}
		
		f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//successfulcapture_"+filenamedate+".xls");
		if(f.exists()) {
			f.delete();
		}
		downloadExcelPage.selectDate(dd,mm,yyyy);
		Element.click(testConfig, downloadExcelPage.getSuccessCaptureListButton(), "Positive successful capture capture list") ;
		Element.click(testConfig, downloadExcelPage.getSubmitButton(), "submit") ;
		xlFile = getExcelFile(testConfig,"successfulcaptureList_"+filenamedate);
		Browser.wait(testConfig,10);
		testConfig.putRunTimeProperty("action", "capture");
		map = DataBase.executeSelectQuery(testConfig, 21, 1);
		Browser.wait(testConfig,20);
		tr = new TestDataReader(testConfig,"Sheet1", xlFile);
		Helper.compareEquals(testConfig, "No of records success capture", (map.get("count")), (tr.getRecordsNum()-1)+"");
		if(tr.getRecordsNum()>1){
		int rowToCheck =1 ;
		String id = tr.GetData(rowToCheck, "RequestID");
		testConfig.putRunTimeProperty("id", id);
		map = DataBase.executeSelectQuery(testConfig, 22, 1);
		Helper.compareExcelEquals(testConfig, "checking txnid", map.get("txnid"),tr.GetData(rowToCheck, "TransactionID"));
		Helper.compareExcelEquals(testConfig, "checking trace_no", map.get("trace_no"),tr.GetData(rowToCheck, "Trace Number"));
		Helper.compareExcelEquals(testConfig, "checking bank_ref_no", map.get("bank_ref_no"),tr.GetData(rowToCheck, "Bank Ref #"));
		Helper.compareExcelEquals(testConfig, "checking merchant_key", map.get("merchant_key"),tr.GetData(rowToCheck, "Merchant Key"));
		Helper.compareExcelEquals(testConfig, "checking merchant_name", map.get("merchant_name"),tr.GetData(rowToCheck, "Merchant Name"));
		Helper.compareExcelEquals(testConfig, "checking payu_merchant_id", map.get("payu_merchant_id"),tr.GetData(rowToCheck, "PayU Merchant ID"));
		Helper.compareExcelEquals(testConfig, "checking merchant_site", map.get("merchant_site"),tr.GetData(rowToCheck, "merchant Site"));
		Helper.compareExcelEquals(testConfig, "checking pg_key", map.get("pg_key"),tr.GetData(rowToCheck, "Payment Gateway"));
		Helper.compareExcelEquals(testConfig, "checking amountpaid", map.get("amountpaid"),tr.GetData(rowToCheck, "Amount Paid"));
		Helper.compareExcelEquals(testConfig, "checking transaction_fee", map.get("transaction_fee"),tr.GetData(rowToCheck, "Transaction Fee"));
		Helper.compareExcelEquals(testConfig, "checking amount", map.get("amount"),tr.GetData(rowToCheck, "Amount capture"));
		Helper.compareExcelEquals(testConfig, "checking discount", map.get("discount"),tr.GetData(rowToCheck, "Discount"));
		Helper.compareExcelEquals(testConfig, "checking created_at", map.get("created_at").split(" ")[0],tr.GetData(rowToCheck, "capture Request Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "checking addedon", map.get("addedon").split(" ")[0],tr.GetData(rowToCheck, "Transaction Creation Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "checking successat", map.get("success_at").split(" ")[0],tr.GetData(rowToCheck, "capture Success Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "currency_type", map.get("currency_type"),tr.GetData(rowToCheck, "Currency"));
		
		}
	 	for(int i=1;i<tr.getRecordsNum();i++){;
	 	String data = tr.GetData(i, "capture Success Timestamp");
	 		Helper.compareEquals(testConfig, "checking dates", data.toString().split(" ")[0], Helper.getDatePreviousTo(dd, mm, yyyy, "yyyy-MM-dd"));
	 	
	 	
	 	}
		
	 	f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//successfulchargebackcorrection_"+filenamedate+"_archive.xls.zip");
		if(f.exists()) {
			f.delete();
		}
		
		f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//successfulchargebackcorrection_"+filenamedate+".xls");
		if(f.exists()) {
			f.delete();
		}
		downloadExcelPage.selectDate(dd,mm,yyyy);
		Element.click(testConfig, downloadExcelPage.getSuccessChargebackCorrectionListButton(), "Positive successful chargebackcorrection list") ;
		Element.click(testConfig, downloadExcelPage.getSubmitButton(), "submit") ;
		xlFile = getExcelFile(testConfig,"successfulchargebackreversalList_"+filenamedate);
		Browser.wait(testConfig,10);
		testConfig.putRunTimeProperty("action", "chargebackcorrection");
		 map = DataBase.executeSelectQuery(testConfig, 21, 1);
		Browser.wait(testConfig,20);
		 tr = new TestDataReader(testConfig,"Sheet1", xlFile);
		Helper.compareEquals(testConfig, "No of records success chargebackcorrection", (map.get("count")), (tr.getRecordsNum()-1)+"");
		if(tr.getRecordsNum()>1){
		int rowToCheck =1 ;
		String id = tr.GetData(rowToCheck, "RequestID");
		testConfig.putRunTimeProperty("id", id);
		map = DataBase.executeSelectQuery(testConfig, 22, 1);
		Helper.compareExcelEquals(testConfig, "checking txnid", map.get("txnid"),tr.GetData(rowToCheck, "TransactionID"));
		Helper.compareExcelEquals(testConfig, "checking trace_no", map.get("trace_no"),tr.GetData(rowToCheck, "Trace Number"));
		Helper.compareExcelEquals(testConfig, "checking bank_ref_no", map.get("bank_ref_no"),tr.GetData(rowToCheck, "Bank Ref #"));
		Helper.compareExcelEquals(testConfig, "checking merchant_key", map.get("merchant_key"),tr.GetData(rowToCheck, "Merchant Key"));
		Helper.compareExcelEquals(testConfig, "checking merchant_name", map.get("merchant_name"),tr.GetData(rowToCheck, "Merchant Name"));
		Helper.compareExcelEquals(testConfig, "checking payu_merchant_id", map.get("payu_merchant_id"),tr.GetData(rowToCheck, "PayU Merchant ID"));
		Helper.compareExcelEquals(testConfig, "checking merchant_site", map.get("merchant_site"),tr.GetData(rowToCheck, "merchant Site"));
		Helper.compareExcelEquals(testConfig, "checking pg_key", map.get("pg_key"),tr.GetData(rowToCheck, "Payment Gateway"));
		Helper.compareExcelEquals(testConfig, "checking amountpaid", map.get("amountpaid"),tr.GetData(rowToCheck, "Amount Paid"));
		Helper.compareExcelEquals(testConfig, "checking transaction_fee", map.get("transaction_fee"),tr.GetData(rowToCheck, "Transaction Fee"));
		Helper.compareExcelEquals(testConfig, "checking amount", map.get("amount"),tr.GetData(rowToCheck, "Amount chargebackcorrection"));
		Helper.compareExcelEquals(testConfig, "checking discount", map.get("discount"),tr.GetData(rowToCheck, "Discount"));
		Helper.compareExcelEquals(testConfig, "checking created_at", map.get("created_at").split(" ")[0],tr.GetData(rowToCheck, "chargebackcorrection Request Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "checking addedon", map.get("addedon").split(" ")[0],tr.GetData(rowToCheck, "Transaction Creation Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "checking succesat", map.get("success_at").split(" ")[0],tr.GetData(rowToCheck, "chargebackcorrection Success Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "currency_type", map.get("currency_type"),tr.GetData(rowToCheck, "Currency"));
		
		}
	 	for(int i=1;i<tr.getRecordsNum();i++){;
	 	String data = tr.GetData(i, "chargebackcorrection Success Timestamp");
	 		Helper.compareEquals(testConfig, "checking dates", data.toString().split(" ")[0], Helper.getDatePreviousTo(dd, mm, yyyy, "yyyy-MM-dd"));
	 	
	 	}
		
	 	f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//successfulchargeback_"+filenamedate+"_archive.xls.zip");
		if(f.exists()) {
			f.delete();
		}
		
		f = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//successfulchargeback_"+filenamedate+".xls");
		if(f.exists()) {
			f.delete();
		}
		downloadExcelPage.selectDate(dd,mm,yyyy);
		Element.click(testConfig, downloadExcelPage.getSuccessChargebackListButton(), "Positive successful chargeback  list") ;
		Element.click(testConfig, downloadExcelPage.getSubmitButton(), "submit") ;
		xlFile = getExcelFile(testConfig,"successfulchargebackList_"+filenamedate);
		testConfig.putRunTimeProperty("action", "chargeback");
		map = DataBase.executeSelectQuery(testConfig, 21, 1);
		Browser.wait(testConfig,20);
		tr = new TestDataReader(testConfig,"Sheet1", xlFile);
		Helper.compareEquals(testConfig, "No of records success chargeback", (map.get("count")), (tr.getRecordsNum()-1)+"");
		if(tr.getRecordsNum()>1){
		int rowToCheck =1 ;
		String id = tr.GetData(rowToCheck, "RequestID");
		testConfig.putRunTimeProperty("id", id);
		map = DataBase.executeSelectQuery(testConfig, 22, 1);
		Helper.compareExcelEquals(testConfig, "checking txnid", map.get("txnid"),tr.GetData(rowToCheck, "TransactionID"));
		Helper.compareExcelEquals(testConfig, "checking trace_no", map.get("trace_no"),tr.GetData(rowToCheck, "Trace Number"));
		Helper.compareExcelEquals(testConfig, "checking bank_ref_no", map.get("bank_ref_no"),tr.GetData(rowToCheck, "Bank Ref #"));
		Helper.compareExcelEquals(testConfig, "checking merchant_key", map.get("merchant_key"),tr.GetData(rowToCheck, "Merchant Key"));
		Helper.compareExcelEquals(testConfig, "checking merchant_name", map.get("merchant_name"),tr.GetData(rowToCheck, "Merchant Name"));
		Helper.compareExcelEquals(testConfig, "checking payu_merchant_id", map.get("payu_merchant_id"),tr.GetData(rowToCheck, "PayU Merchant ID"));
		Helper.compareExcelEquals(testConfig, "checking merchant_site", map.get("merchant_site"),tr.GetData(rowToCheck, "merchant Site"));
		Helper.compareExcelEquals(testConfig, "checking pg_key", map.get("pg_key"),tr.GetData(rowToCheck, "Payment Gateway"));
		Helper.compareExcelEquals(testConfig, "checking amountpaid", map.get("amountpaid"),tr.GetData(rowToCheck, "Amount Paid"));
		Helper.compareExcelEquals(testConfig, "checking transaction_fee", map.get("transaction_fee"),tr.GetData(rowToCheck, "Transaction Fee"));
		Helper.compareExcelEquals(testConfig, "checking amount", map.get("amount"),tr.GetData(rowToCheck, "Amount chargeback"));
		Helper.compareExcelEquals(testConfig, "checking discount", map.get("discount"),tr.GetData(rowToCheck, "Discount"));
		Helper.compareExcelEquals(testConfig, "checking created_at", map.get("created_at").split(" ")[0],tr.GetData(rowToCheck, "chargeback Request Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "checking addedon", map.get("addedon").split(" ")[0],tr.GetData(rowToCheck, "Transaction Creation Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "checking addedon", map.get("success_at").split(" ")[0],tr.GetData(rowToCheck, "chargeback Success Timestamp").split(" ")[0]);
		Helper.compareExcelEquals(testConfig, "currency_type", map.get("currency_type"),tr.GetData(rowToCheck, "Currency"));
		
		}
	 	for(int i=1;i<tr.getRecordsNum();i++){;
	 	String data = tr.GetData(i, "chargeback Success Timestamp");
	 		Helper.compareEquals(testConfig, "checking dates", data.toString().split(" ")[0], Helper.getDatePreviousTo(dd, mm, yyyy, "yyyy-MM-dd"));
	 	
	 	}		
	 	
		
		dd=20;
		mm=3;
		yyyy=2020;
		filenamedate = Helper.getDate(dd, mm, yyyy,"yyyyddMM");
		downloadExcelPage.selectDate(dd,mm,yyyy);
		Element.click(testConfig, downloadExcelPage.getSuccessRefundListButton(), "Positive successful refund list") ;
		Element.click(testConfig, downloadExcelPage.getSubmitButton(), "submit") ;
		Browser.wait(testConfig,10);
		Helper.compareEquals(testConfig, "checking successful refund error message","The file successfulrefundList_"+filenamedate+".xls does not exist" , downloadExcelPage.getErrorMsg().getText());
		
		
		downloadExcelPage.selectDate(dd,mm,yyyy);
		Element.click(testConfig, downloadExcelPage.getInternationalTransactionListButton(), "Positive international transaction list") ;
		Element.click(testConfig, downloadExcelPage.getSubmitButton(), "submit") ;
		Browser.wait(testConfig,10);
		Helper.compareEquals(testConfig, "checking successful refund error message","The file intlTxnList_"+filenamedate+".xls does not exist" , downloadExcelPage.getErrorMsg().getText());
		
		downloadExcelPage.selectDate(dd,mm,yyyy);
		Element.click(testConfig, downloadExcelPage.getTransactionListButton(), "Positive successful refund list") ;
		Element.click(testConfig, downloadExcelPage.getSubmitButton(), "submit") ;
		Browser.wait(testConfig,10);
		Helper.compareEquals(testConfig, "checking successful refund error message","The file txnList_"+filenamedate+".xls does not exist" , downloadExcelPage.getErrorMsg().getText());
		
		Assert.assertTrue(testConfig.getTestResult());
	}

	
	private String getExcelFile(Config testConfig,String zipfilenamepre) {
		try {
		String reconfilepath = "C://Users//"+System.getProperty("user.name")+"//Downloads//";

		Browser.wait(testConfig,10);
		
		String zipFileName = reconfilepath+zipfilenamepre+"_archive.xls.zip";
		byte[] buffer = new byte[1024];
		 
	     
	 
	    	//create output directory is not exists
	    	File outputFolder = new File("C://Users//"+System.getProperty("user.name")+"//Downloads//");
	    	
	 
	    	//get the zip file content
	    	ZipInputStream zis = 
	    		new ZipInputStream(new FileInputStream(zipFileName));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();
	    	File newFile = null;
	    	if(ze!=null){
	 
	    	   String fileName = ze.getName();
	          newFile = new File(outputFolder + File.separator + fileName);
	            new File(newFile.getParent()).mkdirs();
	 
	            FileOutputStream fos = new FileOutputStream(newFile);             
	 
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	       		fos.write(buffer, 0, len);
	            }
	 
	            fos.close();   
	          
	    	}
	 
	        zis.closeEntry();
	    	zis.close();
		return reconfilepath+zipfilenamepre+".xls";
		} catch(IOException ex){
			testConfig.logException(ex);
		       return "";
		    }
	}
	
	

}
