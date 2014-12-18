package PageObject.MerchantPanel.Transactions;


import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.MerchantPanel.Home.DashboardPage;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

public class TransactionFilterPage {
	private Config testConfig;

	@FindBy(name="qterm")
	private WebElement search;

	@FindBy(name="qtype")
	private WebElement searchDropDown;

	@FindBy(css="input[type=\"submit\"]")
	private WebElement clickGo;

	public enum SearchOn {TransactionId, PayUId, CustomerName, CustomerEmail, RequestId, PayuSettlementId, UTRno};

	public TransactionFilterPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, search);

	}

	public void SearchTransaction(String searchCriteria, SearchOn searchOn) 
	{

		switch(searchOn)
		{
		case TransactionId:

			Element.enterData(testConfig, search,searchCriteria, "Search using transaction id");
			break;

		case PayUId:
			Element.selectVisibleText(testConfig, searchDropDown, "PayU ID", "payu id option");
			Element.enterData(testConfig, search,searchCriteria, "Search using PayUId");
			break;

		case RequestId:
			Element.selectVisibleText(testConfig, searchDropDown, "Request ID", "request id option");
			Element.enterData(testConfig, search,searchCriteria, "Search using Request Id");
			break;

		case CustomerName:
			Element.selectVisibleText(testConfig, searchDropDown, "Customer Name", "payu id option");

			Element.enterData(testConfig, search,searchCriteria, "Search using CustomerName");
			break;

		case CustomerEmail:
			Element.selectVisibleText(testConfig, searchDropDown, "Customer Email", "payu id option");
			Element.enterData(testConfig, search,searchCriteria, "Search using Customer email id");
			break;

		case PayuSettlementId:
			Element.selectVisibleText(testConfig, searchDropDown, "PayU Settlement ID", "payu settlement id option");
			Element.enterData(testConfig, search,searchCriteria, "Search using PayuSettlementId");
			break;

		case UTRno:
			Element.selectVisibleText(testConfig, searchDropDown, "UTR No.", "UTR no option");
			Element.enterData(testConfig, search,searchCriteria, "Search using UTR no.");
			break;

		}		
		Element.click(testConfig, clickGo, "Click on Go, to search the transaction");

	}
	public void verifyFilter(Config testConfig, int FilterRow,
			DashboardPage dashBoard, TestDataReader dataSheet)

	{


		if (dataSheet.GetData(FilterRow, "date_from").equals("0")) {
			testConfig.putRunTimeProperty("to",
					Helper.getDateBeforeOrAfterDays(1, "yyyy-MM-dd"));
			testConfig.putRunTimeProperty("from",
					Helper.getDateBeforeOrAfterDays(-7, "yyyy-MM-dd"));

		} else {
			dashBoard.Filldateinputfrom(dataSheet.GetData(FilterRow,"date_from"));
			dashBoard.Filldateinputto(dataSheet.GetData(FilterRow, "date_to"));
			
			dashBoard.FillCaptureddatefrom(dataSheet.GetData(FilterRow,"date_from"));
			dashBoard.FillCaptureddateto(dataSheet.GetData(FilterRow, "date_to"));

			testConfig.putRunTimeProperty("from",Helper.changeDateFormat(dataSheet.GetData(FilterRow,"date_from")));	
			testConfig.putRunTimeProperty("to", Helper.getDateBeforeOrAfterDays(1, "yyyy-MM-dd",dataSheet.GetData(FilterRow,"date_to")));

		}

		Browser.wait(testConfig, 2);
		dashBoard.AmountTab();
		Browser.wait(testConfig, 2);

		if (!dataSheet.GetData(FilterRow, "min_amount").equals(null))
			dashBoard.SubmitMinAmount(dataSheet.GetData(FilterRow, "min_amount"));

		if (!dataSheet.GetData(FilterRow, "max_amount").equals(null))
			dashBoard.SubmitMaxAmount(dataSheet.GetData(FilterRow, "max_amount"));
		dashBoard.apply();
		Browser.wait(testConfig, 5);
		Map<String, String> transaction_count = DataBase.executeSelectQuery(testConfig,Integer.parseInt(dataSheet.GetData(FilterRow, "sql_row")), 1);

		Helper.compareEquals(testConfig, "result count",transaction_count.get("total"), dashBoard.transactionno());

	}

	public void verifyPaymentTypeFilter(Config testConfig, int FilterRow,
			DashboardPage dashBoard, TestDataReader dataSheet) 

	{

		if (dataSheet.GetData(FilterRow, "date_from").equals("0")) {
			testConfig.putRunTimeProperty("to",
					Helper.getDateBeforeOrAfterDays(1, "yyyy-MM-dd"));
			testConfig.putRunTimeProperty("from",
					Helper.getDateBeforeOrAfterDays(-7, "yyyy-MM-dd"));

		} else {
			dashBoard.Filldateinputfrom(dataSheet.GetData(FilterRow,"date_from"));
			dashBoard.Filldateinputto(dataSheet.GetData(FilterRow, "date_to"));

			testConfig.putRunTimeProperty("from",Helper.changeDateFormat(dataSheet.GetData(FilterRow,"date_from")));	
			testConfig.putRunTimeProperty("to", Helper.getDateBeforeOrAfterDays(1, "yyyy-MM-dd",dataSheet.GetData(FilterRow,"date_to")));

		}

		dashBoard.paymentType();
		Browser.wait(testConfig, 2);
		dashBoard.ALLpaymentType();
		Browser.wait(testConfig, 2);

		if (dataSheet.GetData(FilterRow, "all_payment_type").equals("1"))
			dashBoard.ALLpaymentType();

		if (dataSheet.GetData(FilterRow, "cc_payment_type").equals("1"))
			dashBoard.CCpaymentType();

		if (dataSheet.GetData(FilterRow, "dc_payment_type").equals("1"))
			dashBoard.DCpaymentType();

		if (dataSheet.GetData(FilterRow, "nb_payment_type").equals("1"))
			dashBoard.NBpaymentType();

		if (dataSheet.GetData(FilterRow, "COD_payment_type").equals("1"))
			dashBoard.CODpaymentType();

		if (dataSheet.GetData(FilterRow, "emi_payment_type").equals("1"))
			dashBoard.EMIpaymentType();



		Browser.wait(testConfig, 5);

		dashBoard.apply();
		Browser.wait(testConfig, 5);
		Map<String, String> transaction_count = DataBase.executeSelectQuery(
				testConfig,
				Integer.parseInt(dataSheet.GetData(FilterRow, "sql_row")), 1);

		Helper.compareEquals(testConfig, "result count",transaction_count.get("total"), dashBoard.transactionno());

	}

}	

