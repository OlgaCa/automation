package PageObject.NewMerchantPanel.Transactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.NewMerchantPanel.Overview.MerchantPanelPage.filterTypes;
import Test.NewMerchantPanel.Transactions.TransactionsHelper;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;
import Utils.TestDataReader;

public class RequestsPage {

	private Config testConfig;

	@FindBy(linkText = "Requests")
	private WebElement requests;

	@FindBy(css = "li.disabled > a.ng-binding")
	private WebElement recordsCount;
	
	@FindBy(partialLinkText = "Customize Columns")
	private WebElement customizeColumns;

	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr:nth-child(1)>td:nth-child(3)>span")
	private WebElement txnID;

	@FindBy(css = "#TransactionTable>table>tbody>tr:nth-child(1)>td:nth-child(3)")
	private WebElement txnId;

	@FindBy(css = "#TransactionTable>table>tbody>tr:nth-child(1)>td:nth-child(4)")
	private WebElement date;

	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr:nth-child(1)>td:nth-child(2)")
	private WebElement requestId;

	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr:nth-child(1)>td:nth-child(9)>span")
	private WebElement amount;

	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr:nth-child(1)>td:nth-child(7)>span")
	private WebElement status;

	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr:nth-child(1)>td:nth-child(5)>span")
	private WebElement firstName;

	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr:nth-child(1)>td:nth-child(8)>span")
	private WebElement payuID;

	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr:nth-child(1)>td:nth-child(6)>span")
	private WebElement reqAction;

	@FindBy(css = "#TransactionTable>table>thead:nth-child(1)>tr>th:nth-child(4)>a>label>b")
	private WebElement sortByDate;

	@FindBy(css = "#TransactionTable>table>thead:nth-child(1)>tr>th:nth-child(3)>a>label>b")
	private WebElement sortByTxnId;

	@FindBy(css = "#TransactionTable>table>thead:nth-child(1)>tr>th:nth-child(2)>a>label>b")
	private WebElement sortByRequestId;

	@FindBy(css = "div[id=TransactionTable]>table>tbody>tr>td:nth-child(6)>span")
	private WebElement reqStatus;

	@FindBy(css = "span.glyphicon.glyphicon-chevron-right")
	private WebElement moveRight;

	@FindBy(css = "#LeftTrigger")
	private WebElement moveLeft;

	@FindBy(linkText = "Filter")
	private WebElement filter;;

	@FindBy(css = "div[id=TxnFilter]>ul>li:nth-child(3)>ul>li:nth-child(1)>label>span")
	private WebElement requestAll;

	@FindBy(css = "div[id=TxnFilter]>ul>li:nth-child(3)>ul>li:nth-child(2)>label>span")
	private WebElement requestCapture;

	@FindBy(css = "div[id=TxnFilter]>ul>li:nth-child(3)>ul>li:nth-child(3)>label>span")
	private WebElement requestRefund;

	@FindBy(css = "div[id=TxnFilter]>ul>li:nth-child(3)>ul>li:nth-child(3)>label>span")
	private WebElement requestCancel;

	@FindBy(css = "li.block.last > label > a.green")
	private WebElement applyFilter;

	@FindBy(css = "label.hover.ng-binding")
	private WebElement colRequestId;

	@FindBy(css = "th.ng-scope.initial > a.ng-scope > label.hover.ng-binding")
	private WebElement colTransId;

	@FindBy(css = "[id=TransactionTable]>table>thead>tr>th:nth-child(4)>a>label")
	private WebElement colDate;

	@FindBy(css = "[id=TransactionTable]>table>thead>tr>th:nth-child(5)>a>label")
	private WebElement colCustName;

	@FindBy(css = "th.ng-scope.initial > label.ng-scope.ng-binding")
	private WebElement colAction;

	@FindBy(css = "[id=TransactionTable]>table>thead>tr>th:nth-child(7)>label")
	private WebElement colStatus;

	@FindBy(css = "[id=TransactionTable]>table>thead>tr>th:nth-child(8)>a>label")
	private WebElement colPayuId;

	@FindBy(css = "[id=TransactionTable]>table>thead>tr>th:nth-child(9)>a>label")
	private WebElement colAmount;

	@FindBy(css = "[id=TransactionTable]>table>thead>tr>th:nth-child(10)>label")
	private WebElement colPgMid;

	public RequestsPage(Config testConfig) {

		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, reqStatus);
	}
	
	@FindBy(css="#Page>div>div>div>div:nth-child(2)>a>Strong")
	private WebElement exportExcel;
	public void exportToExcel()
	{
		Element.click(testConfig, exportExcel, "Click Export to excel");
	}

	@FindBy(css = "td.ng-scope > span.ng-scope.ng-binding")
	private WebElement transId;

	/**
	 * Method used to validate status in requests page
	 */
	public void validateTransactionInRefundPage() {

		Element.click(testConfig, requests, "Click on requests tab");
		Browser.wait(testConfig, 2);
		String status = reqStatus.getText();
		Element.IsElementDisplayed(testConfig, transId);
		Helper.compareEquals(testConfig, "Comparing the status in Refund page",
				status, "refund");

	}

	/**
	 * Verify Request Status in All Requests Tab and Transaction tab
	 * 
	 * @param transactionID
	 * @param refundAmount
	 * @param reqStatus
	 * @return
	 */
	public void verifyRequestedRefunds(String refId,int count) {
		
		TransactionsHelper transactionHelper=new TransactionsHelper(testConfig);
		transactionHelper.searchDataByFilterType(filterTypes.ReferenceID, refId);
		
		verifyRequestStatus("Success",count,139);
	}
	/**
	 * Getting the compared status in Request tab for given count and compare it
	 * with DB
	 */
	public void verifyRequestStatus(String expectedStatus, int count, int rowNo) {
		Browser.wait(testConfig, 2);
		String actualStatus;
		if (Element.IsElementDisplayed(testConfig, moveRight))
			Element.click(testConfig, moveRight, "Slide to right");

		for (int i = 1; i <= count; i++) {
			actualStatus = Element.getPageElement(
					testConfig,
					How.css,
					"[id=TransactionTable]>table>tbody>tr:nth-child(" + i
							+ ")>td:nth-child(7)>span").getText();
			Helper.compareEquals(testConfig, "COD status in Request Tab",
					expectedStatus, actualStatus);

			Map<String, String> map = DataBase.executeSelectQuery(testConfig,
					rowNo, i);
			Helper.compareEquals(testConfig, "Comparing Count values ",
					expectedStatus.toLowerCase(), map.get("status")
							.toLowerCase());
		}
	}

	/**
	 * Method used to verify weather default columns are selected or not
	 */
	public void verifyDefaultCheckedColumns() {
		Element.click(testConfig, customizeColumns,
				"Clicking the customize Columns");

		List<WebElement> list = Element.getListOfElements(testConfig, How.css,
				".ng-scope.on.def");
		for (WebElement element : list) {
			if (!(element.getText().equals("Request ID")
					|| element.getText().equals("Transaction ID")
					|| element.getText().equals("Date")
					|| element.getText().equals("PayU ID")
					|| element.getText().equals("Amount")
					|| element.getText().equals("Status")
					|| element.getText().equals("Requested Action") || element
					.getText().equals("Customer Name")) && (list.size() == 8)) {
				Helper.compareTrue(testConfig,
						"Comparing default checked items", false);
			}
		}
		Element.click(testConfig, customizeColumns,
				"Clicking the customize Columns");

	}

	/**
	 * Method used to verify the transaction details with DB
	 * 
	 * @param rowNo
	 */
	public void verifyRequestedTransactionDetails(int rowNo) {

		String elementText = "";
		List<WebElement> list = Element
				.getListOfElements(
						testConfig,
						How.xPath,
						".//*[@id='TransactionTable']/table/tbody/tr[1]//td[contains(@class,'ng-scope')]");
		List<String> valuesList = new ArrayList<>();
		int j = 0;
		for (WebElement element : list) {
			list.get(0).getText();
			elementText = element.getText();
			if (elementText.equals("")) {
				Element.click(testConfig, moveRight, "description");
				elementText = element.getText();
			}
			if (elementText.equals("-")) {
				elementText = "";
			}
			valuesList.add(elementText);

			System.out.println(elementText + "Element positionis " + j);
			j++;
		}

		Map<String, String> map = DataBase.executeSelectQuery(testConfig,
				rowNo, 1);

		Map<String, String> updatemap = DataBase.executeSelectQuery(testConfig,
				4, 1);

		Map<String, String> issuingBank = DataBase.executeSelectQuery(
				testConfig, 133, 1);

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(0),
				map.get("txnid"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", requestId.getText(),
				updatemap.get("id"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(3),
				updatemap.get("action"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(2),
				map.get("firstname"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(6),
				map.get("amount"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(4)
						.toLowerCase(), updatemap.get("status").toString()
						.toLowerCase());
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(5),
				map.get("id"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(7),
				map.get("productinfo"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(8),
				map.get("lastname"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(9),
				map.get("email"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(10),
				map.get("phone"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(11),
				map.get("ip"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(12),

				map.get("city"));

		// merchant name missing

		// Helper.compareEquals(testConfig,
		// "Comapring transaction values with DB",valuesList.get(13), merchant
		// name
		// map.get("udf3"));
		//
		// Helper.compareEquals(testConfig,
		// "Comapring transaction values with DB",valuesList.get(14), bank name
		// map.get("udf3"));

		String gateway = map.get("paymentgatewayid");
		if (gateway.equals("7")) {
			gateway = "AXIS";
		} else {
			gateway = "HDFC2";
		}
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(15),
				gateway);

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(16),
				updatemap.get("bank_ref_no"));

		elementText = issuingBank.get("is_domestic");
		if (elementText.equals("1")) {
			elementText = "domestic";
		} else {
			elementText = "international";
		}
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(17),
				elementText);

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(18),
				updatemap.get("bank_arn"));
		// Helper.compareEquals(testConfig,
		// "Comapring transaction values with DB",valuesList.get(19), //payement
		// type creditcard
		// map.get("udf4"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(20),
				map.get("error_code"));
		// Helper.compareEquals(testConfig,
		// "Comapring transaction values with DB", valuesList.get(21),
		// map.get("udf2"));
		if (valuesList.get(22).contains(map.get("field9"))) {
			Helper.compareTrue(testConfig, "Comparing payement msg", true);
		}

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(23),
				issuingBank.get("issuing_bank"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(24),
				map.get("payment_source"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(25),
				map.get("name_on_card"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(26),
				map.get("card_no"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(27),
				map.get("address1"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(28),
				map.get("address2"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(29),
				map.get("state"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(30),
				map.get("country"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(31),
				map.get("zipcode"));

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(41),
				map.get("amount"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(42)
						.trim(), map.get("discount"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(43),
				map.get("additional_charges"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(44),
				updatemap.get("inr_amount"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(45),
				map.get("amount"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(46),
				updatemap.get("bank_service_fee"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(47),
				updatemap.get("bank_service_tax"));

		elementText = updatemap.get("mer_settled");
		if (elementText.equals("2")) {
			elementText = "settled";
		} else {
			elementText = "unsettled";
		}
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(50),
				elementText);
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(56),
				map.get("udf1"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(57),
				map.get("udf2"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(58),
				map.get("udf3"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(59),
				map.get("udf4"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(60),
				map.get("udf5"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(61),
				map.get("field1"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(62),
				map.get("field2"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(63),
				map.get("field3"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(64),
				map.get("field4"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", valuesList.get(65),
				map.get("field5"));

	}

	/**
	 * Method used to test weather default columns are present or not
	 */
	public void verifyDefaultColumns() {
		Element.IsElementDisplayed(testConfig, colRequestId);
		Element.IsElementDisplayed(testConfig, colTransId);
		Element.IsElementDisplayed(testConfig, colDate);
		Element.IsElementDisplayed(testConfig, colCustName);
		Element.IsElementDisplayed(testConfig, colAction);

		for (int i = 0; i < 5; i++)
			Element.click(testConfig, moveRight, "move right");

		Element.IsElementDisplayed(testConfig, colStatus);
		Element.IsElementDisplayed(testConfig, colPayuId);
		Element.IsElementDisplayed(testConfig, colAmount);
		Element.IsElementDisplayed(testConfig, colPgMid);
	}

	/**
	 * Uncheck all requests types
	 */
	public void uncheckRequestTypes() {
		Element.check(testConfig, requestAll, "Check Request All Check box");
		Element.click(testConfig, requestAll, "Uncheck Request All Check box");
	}

	/**
	 * Method used to get total no of records
	 * @return
	 */
	public int getNoOfRecords()
	{
		int count=0;
		if (Element.IsElementDisplayed(testConfig, recordsCount)) {
			String countText = recordsCount.getText();
			String[] splitValues = countText.split(" ");
			countText = splitValues[splitValues.length - 1].trim();

			return Integer.parseInt(countText);
		}
		return count;
	}
	
	public enum requestType{All,Capture,Refund,Cancel};
	/**
	 * Method used to filter order by status
	 * 
	 * @param status
	 */
	public void filterByRequestType(requestType type) {

		Element.click(testConfig, filter, "Clicking the Amount Range");
		uncheckRequestTypes();

		switch (type) {

		case All:

			Element.check(testConfig, requestAll,
					"Checking the Request Type All check box");
			testConfig.logComment("Request Type All is checked");
			break;

		case Capture:

			Element.check(testConfig, requestCapture,
					"Checking the Capture check box");
			testConfig.logComment("Check Capture check box is selected");
			break;

		case Refund:

			Element.check(testConfig, requestRefund,
					"Ckecking the Refunded Check Box");
			testConfig.logComment("Net banking Check Box is Selected");
			break;

		case Cancel:

			Element.check(testConfig, requestCancel,
					"Ckecking the requestCancel check Box");
			testConfig.logComment("ALL Check Box is Selected");
			break;

		default:
			testConfig.logComment("Given Filter Option is not available");
			break;
		}

		Element.click(testConfig, applyFilter,
				"Filter the list with Selected Item");

	}

	/**
	 * Method used to verify the Request details with Downloaded File
	 * 
	 * @param rowNo
	 */
	public void compareTransactionDetailsWithDownloadedFile(int rowNo,
			TestDataReader reader) {

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", txnID.getText(),
				reader.GetData(rowNo, "Transaction ID"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", firstName.getText(),
				reader.GetData(rowNo, "Customer Name"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", requestId.getText(),
				reader.GetData(rowNo, "Request ID"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", reqAction.getText(),
				reader.GetData(rowNo, "Requested Action"));

		for (int i = 0; i < 3; i++)
			Element.click(testConfig, moveRight, "Click the right slide");

		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", payuID.getText(),
				reader.GetData(rowNo, "PayU ID"));
		Helper.compareEquals(testConfig,
				"Comparing trnasaction values with test data", status.getText()
						.toUpperCase(), reader.GetData(rowNo, "Status"));
		Helper.compareEquals(testConfig,
				"Comapring transaction values with DB", amount.getText(),
				reader.GetData(rowNo, "Amount"));

	}

	/**
	 * Method used to click sorting elements and store txnid and payuid in
	 * properties
	 * 
	 * @param sortBy
	 */
	public void clickSortTransactionTable(String sortBy) {

		switch (sortBy) {
		case "TransactionID":
			Element.click(testConfig, sortByTxnId, "Click Sort by TxnID");
			break;
		case "RequestId":
			Element.click(testConfig, sortByRequestId, "Click Sort by TxnID");
			break;
		case "Date":
			Element.click(testConfig, sortByDate, "Click Sort by Date");
			break;

		default:
			break;
		}
		Element.IsElementDisplayed(testConfig, txnId);
		testConfig.putRunTimeProperty("txnid", txnId.getText());
		Element.click(testConfig, moveRight, "click right to get payui id");
		Element.click(testConfig, moveRight, "click right to get payui id");
		testConfig.putRunTimeProperty("mihpayid", payuID.getText());
		Element.click(testConfig, moveLeft, "click right to get payui id");
		Element.click(testConfig, moveLeft, "click right to get payui id");
	}

	/**
	 * Method used to compare dates in display table with DB based
	 * @param rowNo
	 */
	public void verifySortedValuesWithDB(int rowNo) {

		Map<String, String> map = DataBase.executeSelectQuery(testConfig,
				rowNo, 1);

		Helper.compareEquals(testConfig, "Comparing the result with DB",
				requestId.getText(), map.get("id"));

		String Date = date.getText();
		String[] tempDate = Date.split(" ");
		String time = tempDate[1];
		Date = tempDate[0];
		Date = Helper.changeDateFormat(Date);
		Date = Date.replace("/", "-");
		String expectedDate = map.get("created_at");
		tempDate = expectedDate.split(" ");
		String expectedDay = tempDate[0];
		String expectedTime = tempDate[1];
		expectedTime = expectedTime.substring(0, expectedTime.length() - 2);
		Helper.compareEquals(testConfig, "Comparing the result with DB", Date,
				expectedDay);
		Helper.compareEquals(testConfig, "Comparing the result with DB", time,
				expectedTime);
	}
	/**
	 * Method used to select one row from the table
	 * 
	 * @param searchData
	 * @param i
	 */
	public void selectRequestedRowToCompare(String[] searchData, int i) {
		
		Browser.wait(testConfig, 2);
		testConfig.putRunTimeProperty("txnId",transId.getText());
		for(int k=0;k<2;k++)
			Element.click(testConfig, moveRight, "move right");
		String payuID = Element
				.getPageElement(
						testConfig,
						How.css,
						"#TransactionTable>table>tbody>tr:nth-child("+i+")>td:nth-child(8)").getText();
		testConfig.putRunTimeProperty("mihpayid",payuID);
		Element.getPageElement(testConfig, How.css,
				"#TransactionTable>table>tbody>tr:nth-child("+i+")>td:nth-child(2)")
				.click();
		

	}

}
