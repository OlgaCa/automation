package PageObject.NewMerchantPanel.DashBoard;

import java.math.BigDecimal;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import PageObject.NewMerchantPanel.Transactions.TransactionsPage;
import Utils.Browser;
import Utils.Config;
import Utils.DataBase;
import Utils.Element;
import Utils.Element.How;
import Utils.Helper;

public class DashboardPage {


	@FindBy(css = "div.total_amount.ng-binding")
	private WebElement totalAmount;

	@FindBy(css = "div.transactions.ng-binding")
	private WebElement sucessfulTransaction;

	@FindBy(css = "div.avg_amount.ng-binding")
	private WebElement averageTransaction;

	@FindBy(css = "div.success_rate.ng-binding")
	private WebElement sucessRate;

	@FindBy(css = "div#OverallTransaction>div>div:nth-child(2)>table>tbody>tr:nth-child(2)>td:nth-child(4)")
	private WebElement bounced;

	@FindBy(css = "div#OverallTransaction>div>div:nth-child(2)>table>tbody>tr:nth-child(3)>td:nth-child(4)")
	private WebElement dropped;

	@FindBy(css = "div#OverallTransaction>div>div:nth-child(2)>table>tbody>tr:nth-child(4)>td:nth-child(4)")
	private WebElement failed;

	@FindBy(css = "div#OverallTransaction>div:nth-child(2)>div:nth-child(2)>table>tbody>tr:nth-child(2)>td:nth-child(4)")
	private WebElement retry;

	@FindBy(css = "div#OverallTransaction>div:nth-child(2)>div:nth-child(2)>table>tbody>tr:nth-child(3)>td:nth-child(4)")
	private WebElement uniqueBounced;

	@FindBy(css = "div#OverallTransaction>div:nth-child(2)>div:nth-child(2)>table>tbody>tr:nth-child(4)>td:nth-child(4)")
	private WebElement uniqueDropped;

	@FindBy(css = "div#OverallTransaction>div:nth-child(2)>div:nth-child(2)>table>tbody>tr:nth-child(5)>td:nth-child(4)")
	private WebElement uniqueFailed;

	@FindBy(css = "div#OverallTransaction>div:nth-child(2)>div:nth-child(2)>table>tbody>tr:nth-child(3)>td:nth-child(2)>strong")
	private WebElement uniqueAttempts;

	@FindBy(css = "div#OverallTransaction>div:nth-child(2)>div:nth-child(2)>table>tbody>tr:nth-child(4)>td:nth-child(2)>strong")
	private WebElement uniqueRevert;

	@FindBy(css = "div#OverallTransaction>div:nth-child(2)>div:nth-child(2)>table>tbody>tr:nth-child(5)>td:nth-child(2)>strong")
	private WebElement uniqueSucess;

	@FindBy(css = "[class=unique_transaction_funnel]>div:nth-child(2)>table>tbody>tr:nth-child(3)>td:nth-child(3)>span>span:nth-child(1)")
	private WebElement uniqueFunnelAttempts;

	@FindBy(css = "[class=unique_transaction_funnel]>div:nth-child(2)>table>tbody>tr:nth-child(4)>td:nth-child(3)>span>span:nth-child(1)")
	private WebElement uniqueFunnelRevert;

	@FindBy(css = "[class=unique_transaction_funnel]>div:nth-child(2)>table>tbody>tr:nth-child(5)>td:nth-child(3)>span>span:nth-child(1)")
	private WebElement uniqueFunnelSucess;

	@FindBy(css = "[class=unique_transaction_funnel]>div:nth-child(2)>table>tbody>tr:nth-child(2)>td:nth-child(3)>span>span:nth-child(1)")
	private WebElement uniqueFunnelTrans;

	@FindBy(css = "div#OverallTransaction>div:nth-child(2)>div:nth-child(2)>table>tbody>tr:nth-child(2)>td:nth-child(2)>strong")
	private WebElement unique;

	@FindBy(css = "div#OverallTransaction>div>div:nth-child(2)>table>tbody>tr:nth-child(2)>td:nth-child(2)>strong")
	private WebElement noOfAttempts;

	@FindBy(css = "[class=overall_transaction]>div:nth-child(2)>table>tbody>tr:nth-child(2)>td:nth-child(3)>span>span:nth-child(1)")
	private WebElement attemptsSucess;

	@FindBy(css = "[class=overall_transaction]>div:nth-child(2)>table>tbody>tr:nth-child(3)>td:nth-child(3)>span>span:nth-child(1)")
	private WebElement revertSucess;

	@FindBy(css = "div#OverallTransaction>div>div:nth-child(2)>table>tbody>tr:nth-child(3)>td:nth-child(2)>strong")
	private WebElement statusRevert;

	@FindBy(css = "[class=overall_transaction]>div:nth-child(2)>table>tbody>tr:nth-child(4)>td:nth-child(3)>span>span:nth-child(1)")
	private WebElement sucessTransaction;

	@FindBy(css = "div#OverallTransaction>div>div:nth-child(2)>table>tbody>tr:nth-child(4)>td:nth-child(2)>strong")
	private WebElement sucess;

	private Config testConfig;
	
	@FindBy(linkText = "Overview")
	private WebElement overview;

	public DashboardPage(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);		
		Browser.waitForPageLoad(testConfig,totalAmount );

	}
	
	/**
	 * Method used to verify DashBoard Transaction details
	 * 
	 * @param rowNo
	 */
	public void verifyDashBoardData(int rowNo) {
		String total = totalAmount.getText();
		String[] temp = total.split("\n");
		total = temp[0];
		String avg = averageTransaction.getText();
		temp = avg.split("\n");
		avg = temp[0];
		testConfig.putRunTimeProperty("filterBy", "captured");
		String sucess = sucessfulTransaction.getText();
		temp = sucess.split("\n");
		sucess = temp[0];
		String sucessrate = sucessRate.getText();
		temp = sucessrate.split("\n");
		sucessrate = temp[0];
		sucessrate = sucessrate.replace('%', ' ');

		Map<String, String> map = DataBase.executeSelectQuery(testConfig,
				rowNo, 1);

		String sucessfulTrans = map.get("COUNT(*)");
		// validating the sucessful transactions count
		Helper.compareEquals(testConfig,
				"Comparing total no of sucessful transactions", sucess,
				sucessfulTrans);

		float averagetrans = Float.parseFloat(total) / Float.parseFloat(sucess);
		averagetrans = round(averagetrans, 2);

		// validating average transactions count
		Helper.compareEquals(testConfig,
				"Comparing average amount transactions ",
				Float.parseFloat(avg), averagetrans);

		map = DataBase.executeSelectQuery(testConfig, 128, 1);
		float sucessRate = Float.parseFloat(sucessfulTrans)
				/ Float.parseFloat(map.get("COUNT(*)"));
		sucessRate = sucessRate * 100;

		sucessRate = round(sucessRate, 2);

		// validating sucess rate
		Helper.compareEquals(testConfig,
				"Comparing percentage of sucessful transactions",
				Float.parseFloat(sucessrate), sucessRate);

		map = DataBase.executeSelectQuery(testConfig, 132, 1);
		String totalamount = map.get("SUM(amount)");
		// validating total amount with DB
		Helper.compareEquals(testConfig,
				"Comparing total amount of sucessful transactions", total,
				totalamount);

	}

	/**
	 * Round to certain number of decimals
	 * 
	 * @param d
	 * @param decimalPlace
	 * @return
	 */
	public float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}


	
	/**
	 * Method used to verify overall transactiosn funnel date
	 */
	public void verifyOverallTransactionFunnel() {
		WebElement element;
		
		//used to compare no of attempts
		String noofAttempts = noOfAttempts.getText();
		compareResults(attemptsSucess, noofAttempts);

		//used to compare revert status
		String revertStatus = statusRevert.getText();
		compareResults(revertSucess, revertStatus);

		//used to compare sucess count
		String sucessCount = sucess.getText();
		compareResults(sucessTransaction, sucessCount);

		String bouncedCount = bounced.getText();
		String failedCount = failed.getText();
		String dropCount = dropped.getText();
		String count[] = bouncedCount.split(" ");
		bouncedCount = count[0];
		count = failedCount.split(" ");
		failedCount = count[0];
		count = dropCount.split(" ");
		dropCount = count[0];

		//compares bounced count if it is greater than zero
		if (!bouncedCount.equals("0")) {
			element = Element
					.getPageElement(
							testConfig,
							How.css,
							"[class=overall_transaction]>div:nth-child(2)>table>tbody>tr:nth-child(2)>td:nth-child(3)>span>span:nth-child(2)");
			compareResults(element, bouncedCount);
		}
		//compares failed count if it is greater than zero
		if (!failedCount.equals("0")) {
			element = Element
					.getPageElement(
							testConfig,
							How.css,
							"[class=overall_transaction]>div:nth-child(2)>table>tbody>tr:nth-child(4)>td:nth-child(3)>span>span:nth-child(2)");
			compareResults(element, failedCount);
		}
		//compares drop count if it is greater than zero
		if (!dropCount.equals("0")) {
			element = Element
					.getPageElement(
							testConfig,
							How.css,
							"[class=overall_transaction]>div:nth-child(2)>table>tbody>tr:nth-child(3)>td:nth-child(3)>span>span:nth-child(2)");
			compareResults(element, dropCount);
		}

	}
	
	/**
	 * Method used to verify unique transactions funnel
	 */
	public void verifyUniqueTransactionFunnel() {
		WebElement element;
		
		//compares unique transactions in transactions funnel
		String Unique = unique.getText();
		compareResults(uniqueFunnelTrans, Unique);

		//compares unique attempt in transactions funnel
		String uniqueattempt = uniqueAttempts.getText();
		compareResults(uniqueFunnelAttempts, uniqueattempt);

		//compares revert transactions in transactions funnel
		String revert = uniqueRevert.getText();
		compareResults(uniqueFunnelRevert, revert);

		//compares funnel transactions in transactions funnel
		String funnelRevert = uniqueSucess.getText();
		compareResults(uniqueFunnelSucess, funnelRevert);

		String retryCount = retry.getText();
		String bouncedCount = uniqueBounced.getText();
		String droppedCount = uniqueDropped.getText();
		String failedCount = uniqueFailed.getText();

		String count[] = bouncedCount.split(" ");
		bouncedCount = count[0];

		count = retryCount.split(" ");
		retryCount = count[0];

		count = droppedCount.split(" ");
		droppedCount = count[0];

		count = failedCount.split(" ");
		failedCount = count[0];

		//compares retry count in transactions funnel
		if (!retryCount.equals("0")) {
			element = Element
					.getPageElement(
							testConfig,
							How.css,
							"[class=unique_transaction_funnel]>div:nth-child(2)>table>tbody>tr:nth-child(2)>td:nth-child(3)>span>span:nth-child(2)");
			compareResults(element, retryCount);
		}
		//compares drop count in transactions funnel
		if (!droppedCount.equals("0")) {
			element = Element
					.getPageElement(
							testConfig,
							How.css,
							"[class=unique_transaction_funnel]>div:nth-child(2)>table>tbody>tr:nth-child(4)>td:nth-child(3)>span>span:nth-child(2)");
			compareResults(element, droppedCount);
		}
		//compares failed count in transactions funnel
		if (!failedCount.equals("0")) {
			element = Element
					.getPageElement(
							testConfig,
							How.css,
							"[class=unique_transaction_funnel]>div:nth-child(2)>table>tbody>tr:nth-child(5)>td:nth-child(3)>span>span:nth-child(2)");
			compareResults(element, failedCount);
		}

	}
	
	/**
	 * Method used to compare sucessful transactions percentage based on modes
	 * @param queryRowNo
	 */
	public void validateSucessfulTransactionPercentage(int queryRowNo) {
		// validating CC data
		testConfig.putRunTimeProperty("mode", "CC");
		WebElement element = Element
				.getPageElement(
						testConfig,
						How.css,
						"[id=PaymentMethod]>div>div:nth-child(3)>table>tbody>tr:nth-child(2)>td:nth-child(2)");
		System.out.println(element.getText());
		validateTransactionPercentagesWithDB(element, queryRowNo);

		// validating DC data
		testConfig.putRunTimeProperty("mode", "DC");
		element = Element
				.getPageElement(
						testConfig,
						How.css,
						"[id=PaymentMethod]>div>div:nth-child(3)>table>tbody>tr:nth-child(3)>td:nth-child(2)");
		validateTransactionPercentagesWithDB(element, queryRowNo);

	}
	
	/**
	 * Method used to validate transactions percentage with DB
	 * @param element
	 * @param queryRowNo
	 */
	public void validateTransactionPercentagesWithDB(WebElement element,
			int queryRowNo) {
		Map<String, String> totalSucessfultrans = DataBase.executeSelectQuery(
				testConfig, 129, 1);
		float totalCount = Float
				.parseFloat(totalSucessfultrans.get("COUNT(*)"));
		float ccCount = 0;
		float percentCount = 0.0f;
		float displayedPercent = 0.0f;
		String value = null;
		if (element != null) {
			value = element.getText();
			value = value.replace("%", "");
			displayedPercent = Float.parseFloat(value);
			displayedPercent = round(displayedPercent, 1);
			Map<String, String> ccSucessTrans = DataBase.executeSelectQuery(
					testConfig, queryRowNo, 1);
			ccCount = Float.parseFloat(ccSucessTrans.get("COUNT(*)"));
			percentCount = ccCount / totalCount;
			percentCount = percentCount * 100;
			percentCount = round(percentCount, 1);
			Helper.compareEquals(testConfig, "Comparing values",
					displayedPercent, percentCount);

		}
	}

	/**
	 * Method used to compare the details from transactions/requests page with DB
	 * @param element
	 * @param expectedCount
	 */
	public void compareResults(WebElement element, String expectedCount) {
		Element.click(testConfig, element,
				"click the bar link to get transaction page");
		Browser.wait(testConfig, 2);
		TransactionsPage transactionPage = new TransactionsPage(testConfig);
		String totalCount = transactionPage.getNoOfRecords();
		Helper.compareEquals(testConfig, "No of attempts", expectedCount,
				totalCount);
		Element.click(testConfig, overview, "Click Overview link");

	}
	
	/**
	 * Method used to verify Header Amount Displayed
	 */
	public void verifyAmountHeaders()
	{
		
		Element.verifyElementPresent(testConfig,
				totalAmount,
				"Total Amount");
		Element.verifyElementPresent(testConfig,
				sucessfulTransaction,
				"Successful Transactions");
		Element.verifyElementPresent(testConfig,
				averageTransaction,
				"Average Transactions");
		Element.verifyElementPresent(testConfig,
				sucessRate,
				"Success Rate");
	}

}
