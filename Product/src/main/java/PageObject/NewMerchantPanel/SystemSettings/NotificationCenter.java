package PageObject.NewMerchantPanel.SystemSettings;

import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Helper;
import Utils.TestDataReader;

/**This class incorporates elements and action functions for notifications
 * On system settings page on MerchantPanel
 * @author sharadjain
 *
 */
public class NotificationCenter extends SystemSettingsPage {

	private Config testConfig;

		
	public NotificationCenter(Config testConfig) {
		super(testConfig);
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		initializeMerchantAlertRowsValues();
		initializeCustomerAlertRowsValues();
	}

	public enum MerchantAlertType {
		SuccessfulTransactionAlert, FailedTransactionAlert, BouncedTransactionAlert, DroppedTransactionAlert, UserCancelledTransactionAlert, TransactionStatusChangeReport, RefundSuccessfulAlert, DailySettlementReport, ProductUpdates;

		@Override
		public String toString() {
			return splitCamelCase(this.name());

		}

		/**Separates Camel Cased string into words
		 * @param camelCasedString
		 * @return
		 */
		private String splitCamelCase(String camelCasedString) {
			return camelCasedString.replaceAll(String.format("%s|%s|%s",
					"(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
					"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
		}
	};
	
	public enum CustomerAlertType {
		SuccessfulTransactionAlert, FailedTransactionAlert, BouncedTransactionAlert, DroppedTransactionAlert,RefundSuccessfulAlert;

		@Override
		public String toString() {
			return splitCamelCase(this.name());

		}

		/**Separates Camel Cased string into words
		 * @param camelCasedString
		 * @return
		 */
		private String splitCamelCase(String camelCasedString) {
			return camelCasedString.replaceAll(String.format("%s|%s|%s",
					"(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
					"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
		}
	};
	
	private HashMap<MerchantAlertType, NotificationWithValues> MerchantAlertsMap;
	
	private HashMap<CustomerAlertType, Notification> CustomerAlertsMap;
	
	/**Initializes values for merchant Notifications present on page 
	 * 
	 */
	private void initializeMerchantAlertRowsValues() {
		MerchantAlertsMap= new HashMap<>(9, 1.0f);
	
		MerchantAlertsMap.put(MerchantAlertType.SuccessfulTransactionAlert,
				new NotificationWithValues(SuccessfulAlertNotification));
	
		MerchantAlertsMap.put(MerchantAlertType.FailedTransactionAlert,
				new NotificationWithValues(FailedAlertNotification));
		
		MerchantAlertsMap.put(MerchantAlertType.BouncedTransactionAlert,
				new NotificationWithValues(BouncedAlertNotification));
		
		MerchantAlertsMap.put(MerchantAlertType.DroppedTransactionAlert,
				new NotificationWithValues(DroppedAlertNotification));
		
		MerchantAlertsMap.put(MerchantAlertType.UserCancelledTransactionAlert,
				new NotificationWithValues(UserCancelledAlertNotification));
		
		MerchantAlertsMap.put(MerchantAlertType.TransactionStatusChangeReport,
				new NotificationWithValues(TransactionStatusChangeAlert));
		
		MerchantAlertsMap.put(MerchantAlertType.RefundSuccessfulAlert,
				new NotificationWithValues(RefundSuccessfulNotification));
		
		MerchantAlertsMap.put(MerchantAlertType.DailySettlementReport,
				new NotificationWithValues(DailySettlementReport));
		
		MerchantAlertsMap.put(MerchantAlertType.ProductUpdates,
				new NotificationWithValues(ProductUpdates));
	}

	/**Initializes values for Customer Notifications present on page 
	 * 
	 */
	private void initializeCustomerAlertRowsValues() {
		CustomerAlertsMap= new HashMap<>();
	
		CustomerAlertsMap.put(CustomerAlertType.SuccessfulTransactionAlert,
				new Notification(SuccessfulTransactionCustomerAlert));
		CustomerAlertsMap.put(CustomerAlertType.FailedTransactionAlert,
				new Notification(FailedTransactionCustomerAlert));
		CustomerAlertsMap.put(CustomerAlertType.BouncedTransactionAlert,
				new Notification(BouncedTransactionCustomerAlert));
		CustomerAlertsMap.put(CustomerAlertType.DroppedTransactionAlert,
				new Notification(DroppedTransactionCustomerAlert));
		CustomerAlertsMap.put(CustomerAlertType.RefundSuccessfulAlert,
				new Notification(RefundTransactionCustomerAlert));
	}
	
	@FindBy(id="NotificationCenter")
	private WebElement NotificationCenterDiv;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(3)>ul")
	private WebElement MerchantNotificationsGroup;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(3)>ul>li:nth-of-type(1) li:nth-of-type(1)")
	private WebElement SuccessfulAlertNotification;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(3)>ul>li:nth-of-type(1) li:nth-of-type(2)")
	private WebElement FailedAlertNotification;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(3)>ul>li:nth-of-type(1) li:nth-of-type(3)")
	private WebElement BouncedAlertNotification;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(3)>ul>li:nth-of-type(1) li:nth-of-type(4)")
	private WebElement DroppedAlertNotification;

	@FindBy(css="#NotificationCenter>div:nth-of-type(3)>ul>li:nth-of-type(1) li:nth-of-type(5)")
	private WebElement UserCancelledAlertNotification;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(3)>ul>li:nth-of-type(1) li:nth-of-type(6)")
	private WebElement TransactionStatusChangeAlert;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(3)>ul>li:nth-of-type(1) li:nth-of-type(7)")
	private WebElement RefundSuccessfulNotification;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(3)>ul>li:nth-of-type(2) li:nth-of-type(1)")
	private WebElement DailySettlementReport;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(3)>ul>li:nth-of-type(3) li:nth-of-type(1)")
	private WebElement ProductUpdates;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(5) >ul>li li:nth-of-type(1)")
	private WebElement SuccessfulTransactionCustomerAlert;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(5) >ul>li li:nth-of-type(2)")
	private WebElement FailedTransactionCustomerAlert;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(5) >ul>li li:nth-of-type(3)")
	private WebElement BouncedTransactionCustomerAlert;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(5) >ul>li li:nth-of-type(4)")
	private WebElement DroppedTransactionCustomerAlert;
	
	@FindBy(css="#NotificationCenter>div:nth-of-type(5) >ul>li li:nth-of-type(5)")
	private WebElement RefundTransactionCustomerAlert;
	
	
	/**Class encompasses properties and behavior for Toggle button
	 * @author sharadjain
	 *
	 */
	private class ToggleButton{
		private WebElement toggleButtonElement;
		private String alertName;
		public ToggleButton(WebElement toggleButtonElement, String AlertName) {
			this.toggleButtonElement = toggleButtonElement;
			this.alertName = AlertName;
		}

		/**
		 * Clicks on Toggle button
		 */
		public void clickToggleButton() {
			toggleButtonElement.click();
		}
		
		/**Turns on if not
		 * 
		 */
		public void SetAlertOn() {
			if (!getStatus()) {
				clickToggleButton();
				verifyNotificationText(alertName + " has been " + "enabled.");
				testConfig.logComment("Toggle button for " + alertName
						+ " set to on");
			} else {
				testConfig.logComment("Toggle button for " + alertName
						+ "is already set to on");
			}
		}
		
		/**Turns Off if not
		 * 
		 */
		public void SetAlertOff() {
			if (getStatus()) {
				clickToggleButton();
				verifyNotificationText(alertName + " has been " + "disabled.");
				testConfig.logComment("Toggle button for " + alertName
						+ " set to off");
			} else {
				testConfig.logComment("Toggle button for " + alertName
						+ "is already set to off");
			}
		}
		
		/**Gets Current status of button
		 * using current 'class' attribute
		 * @return boolean as indicator for status
		 */
		public boolean getStatus(){
			String classOfToggleButton =toggleButtonElement.getAttribute("class");
			if(classOfToggleButton.equals("on"))	{
				return true;
			}
			else if(classOfToggleButton.equals("off"))	{
				return false;
			}
			testConfig.logFail("Toggle button neither off nor on");
			return false;
		}
	}
	
	/**Class encompasses properties and behavior for editable fields present
	 * for merchant notifications
	 * @author sharadjain
	 *
	 */
	private class EditableTextValue{
		private WebElement ParentDiv;
		private WebElement TextField;
		private WebElement SaveButton;
		private WebElement CancelButton;
		private String AlertName;
		public EditableTextValue(WebElement TextInput, String AlertName){
			this.ParentDiv =TextInput;
			this.AlertName = AlertName;
			this.TextField = ParentDiv.findElement(By.tagName("input"));
		}
		
		/**Gets current value of email
		 * @return
		 */
		public String getEmail(){
			return TextField.getAttribute("value");
		}
		
		/**Enters string in input email field
		 * @param NewEmail - new email string
		 */
		public void enterNewEmailValue(String NewEmail){
			Element.enterData(testConfig, TextField, NewEmail,"Email for "+ AlertName );
		}
		
		/**Checks if email field indicates error
		 * By checking class attribute of email field
		 * @return boolean value indicating showing presence of error
		 */
		public boolean checkForError() {
			testConfig.logComment(TextField.getAttribute("class"));
			if (TextField.getAttribute("class").contains(" er"))//Error class is ' er'
				return true;
			else
				return false;
		}
		
		/**Finds Save button for merchant notification
		 * and clicks on it
		 */
		public void clickSave(){
			this.SaveButton = ParentDiv.findElement(By.cssSelector(".butn.right>.btn-success"));
			Element.click(testConfig, SaveButton, "Save button for "+ AlertName);
			Browser.wait(testConfig, 7);
		}
		
		/**
		 * Finds Cancel button for merchant notification and clicks on it
		 */
		public void clickCancel() {
			this.CancelButton = ParentDiv.findElement(By
					.cssSelector(".butn.right>.btn-default"));
			Element.click(testConfig, SaveButton, "Cancel button for "
					+ AlertName);
		}
	}
	
	/**Class encompasses properties and behavior for notifications
	 * @author sharadjain
	 *
	 */
	private class Notification{
		boolean currentStatus;
		String Name;
		ToggleButton togglebutton;	
		
		//Constructor
		public Notification(WebElement NotificationRow){
			Name = NotificationRow.findElement(By.tagName("label")).getText();
			togglebutton = new ToggleButton(NotificationRow.findElement(By.tagName("a")),Name);
			currentStatus = togglebutton.getStatus();
		}
		
	}
	
	/**Class encompasses properties and behavior for notifications which consists of 
	 * value input fields
	 * e.g. Merchant Notifications
	 * @author sharadjain
	 *
	 */
	private class NotificationWithValues extends Notification {
		EditableTextValue emailField;

		public NotificationWithValues(WebElement NotificationRow) {
			super(NotificationRow);
			emailField = new EditableTextValue(NotificationRow.findElement(By
					.cssSelector(".input")), Name);
		}

		/**Changes email value for notifications
		 * and saves it by either toggling On
		 * or clicking on Save
		 * Verifies confirmation message
		 * @param newEmail
		 */
		public void changeEmail(String newEmail) {
			if (emailField.getEmail().equalsIgnoreCase(newEmail))
				testConfig.logComment(Name + "is already set to " + newEmail);
			else {
				emailField.enterNewEmailValue(newEmail);
				if (togglebutton.getStatus())
					emailField.clickSave();
				else
					togglebutton.SetAlertOn();
			}
		}
		
		/**Changes email value for notification
		 * @param EmailIDRows - row numbers from 'EmailConfig' sheet
		 */
		public void changeEmail(ArrayList<Integer> EmailIDRows) {
			TestDataReader dataReader = new TestDataReader(testConfig,
					"EmailConfig");
			String newEmailValue = "";
			String currentSeparator = "";
			for (Integer EmailIdRow : EmailIDRows) {
				newEmailValue = newEmailValue + currentSeparator
						+ dataReader.GetData(EmailIdRow, "EmailID");
				currentSeparator = ",";
			}
			changeEmail(newEmailValue);
		}

		public void changeEmailAndVerify(String newEmail) {
			changeEmail(newEmail);
			if (!emailField.checkForError())
				testConfig.logPass("Email Input accepts valid value for "
						+ Name);
			else
				testConfig.logFail("Email Input accepts valid value for "
						+ Name);
		}

		/**Performs email validations by entering valid/invalid values
		 * 
		 */
		public void doEmailValidations() {
			// Scenario 1 invalid email
			changeEmail(Helper.generateRandomAlphaNumericString(14));
			if (emailField.checkForError())
				testConfig.logPass("Email Input shows error for " + Name);
			else
				testConfig.logFail("Email Input error is not shown  for "
						+ Name);

			// Scenario 2 for valid email
			String validEmail = Helper.generateRandomAlphabetsString(5) + "@"
					+ Helper.generateRandomAlphabetsString(5) + "."
					+ Helper.generateRandomAlphabetsString(3);
			changeEmailAndVerify(validEmail);
		}
		
		/**Performs email validations by entering valid/invalid values
		 * 
		 */
		public void doEmailValidationsWhenOffState() {
			// Scenario 1 invalid email
			emailField.enterNewEmailValue(Helper.generateRandomAlphaNumericString(14));
			togglebutton.clickToggleButton();
			
			if (emailField.checkForError())
				testConfig.logPass("Email Input shows error for " + Name);
			else
				testConfig.logFail("Email Input error is not shown  for "
						+ Name);

			// Scenario 2 for valid email
			String validEmail = Helper.generateRandomAlphabetsString(5) + "@"
					+ Helper.generateRandomAlphabetsString(5) + "."
					+ Helper.generateRandomAlphabetsString(3);
			changeEmailAndVerify(validEmail);
		}

	}
	
	public enum ToggleState{On,Off};
	
	/**Verifies alert toggles are set to specified state for specifies merchantAlerts
	 * @param state
	 * @param merchantAlerts
	 */
	public void verifyAlertToggleAre(ToggleState state, ArrayList<MerchantAlertType> merchantAlerts ){
		for (MerchantAlertType merchantAlertRow : merchantAlerts) {
			switch (state) {
			case On:
					Helper.compareTrue(testConfig, "Status for "+merchantAlertRow.toString(), MerchantAlertsMap.get(merchantAlertRow).currentStatus);
				break;

			case Off:
				
				 if(!MerchantAlertsMap.get(merchantAlertRow).currentStatus){
					 testConfig.logPass("Status for "+merchantAlertRow.toString()+" is set to off");
				 }
				 else
					 testConfig.logFail("Status for "+merchantAlertRow.toString()+" is not set to off");
				break;
			}
		}
	}
	
	/**Performs email validations for both state on and off
	 * 
	 */
	public void DoAlertEmailValidations() {
		//For Scenario when State is ON
		for (MerchantAlertType MerchantAlert : MerchantAlertsMap.keySet()) {
			MerchantAlertsMap.get(MerchantAlert).togglebutton.SetAlertOn();
			MerchantAlertsMap.get(MerchantAlert).doEmailValidations();
		}
		//For Scenario when State is Off
		for (MerchantAlertType MerchantAlert : MerchantAlertsMap.keySet()) {
			MerchantAlertsMap.get(MerchantAlert).togglebutton.SetAlertOff();
			MerchantAlertsMap.get(MerchantAlert).doEmailValidationsWhenOffState();
		}
	}
	
	/**Verifies multiple emails are allowed for each merchant notification
	 * 
	 */
	public void verifyMultipleEmailIdsAllowedForAlerts() {
		String multipleEmails = new TestDataReader(testConfig, "StringValues").GetData(12, "Value");
		for (MerchantAlertType merchantAlertRow : MerchantAlertsMap.keySet()) {
			MerchantAlertsMap.get(merchantAlertRow).changeEmailAndVerify(multipleEmails);
		}
		
	}
	
	/**Sets Merchant alert to specified value
	 * @param merchantAlertType- type of merchant alert 
	 * @param EmailIDRows - email id rows from 'EmailConfig' sheet
	 */
	public void setAlertFor(MerchantAlertType merchantAlertType, ArrayList<Integer> EmailIDRows){
		MerchantAlertsMap.get(merchantAlertType).changeEmail(EmailIDRows);
	}
	
	/**Turns Off Merchant Alert
	 * @param merchantAlertType- type of merchant alert 
	 */
	public void turnOffMerchantAlertFor(MerchantAlertType merchantAlertType){
		MerchantAlertsMap.get(merchantAlertType).togglebutton.SetAlertOff();
	}
	
	/**Sets Customer alert to On/Off
	 * @param CustomerAlertType- type of merchant alert 
	 * @param Status - On/Off
	 */
	public void setAlertFor(CustomerAlertType customerAlertType, ToggleState toggleState){
		if(toggleState== ToggleState.On)
		CustomerAlertsMap.get(customerAlertType).togglebutton.SetAlertOn();
		
		if(toggleState== ToggleState.Off)
		CustomerAlertsMap.get(customerAlertType).togglebutton.SetAlertOff();
	}
	
}