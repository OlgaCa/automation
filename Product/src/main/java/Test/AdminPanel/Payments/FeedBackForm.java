package Test.AdminPanel.Payments;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import Utils.Config;
import Utils.DataBase;
import Utils.Helper;
import Utils.TestBase;
import Utils.TestDataReader;

public class FeedBackForm extends TestBase {

	/**
	 * Verify that feedback is submitted successfully  
	 * @param testConfig
	 */

	@Test(description = "Verify that feedback is submitted", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void testFeedBackMessageSubmittedOnDB(Config testConfig) {

		//excute the testcase only if cust_feedback_flag value=1
		Map verifyDBValue = DataBase.executeSelectQuery(testConfig, 106, 1);
		if (verifyDBValue.get("value").equals("1")) {

			//Navigate till Payment Option page
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			helper.DoLogin();
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(138);
			helper.payment = helper.trans.Submit();
			
			//click on feedback button submit the feedback and close the feedback form
			helper.payment.clickFeedBackButton();
			String message = Helper.generateRandomAlphabetsString(10);
			helper.payment.writeFeedBackMessage(message);
			helper.payment.submitFeedBackForm();
			helper.payment.verifyFeedbackForm();
			helper.payment.closeFeedBackForm();
			
			//Perform the transaction
			helper.ccTab = helper.payment.clickCCTab();
			helper.ccTab.FillCardDetails(1);
			helper.payment.clickPayNow();
			
			//get the feedback from the database and verify it with the entered message
			Map customerconfig = DataBase
					.executeSelectQuery(testConfig, 108, 1);
			
			Helper.compareEquals(testConfig, "Feedback message stored in DB", message, customerconfig.get("feedback_msg").toString());


		} else {
			testConfig
					.logComment("FeedBack Flag is not equal to 1 hence feedback form is not visible	");
		}
		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verfiy the phone number in feedback message is same as phone number present in DB when flagback_callback_number is set at 1
	 * @param testConfig
	 */
	@Test(description = "Verify that phone number on feedback is same as given in flag feedback_callback_number", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void testPhoneNumberOnFeedBackMessageAndDB(
			Config testConfig) {

		//execute the testcase only if cust_feedback_flag value=1
		Map verifyDBValue = DataBase.executeSelectQuery(testConfig, 106, 1);
		if (verifyDBValue.get("value").equals("1")) {
			
			//Navigate till Payment Option page
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			helper.DoLogin();
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(138);
			helper.payment = helper.trans.Submit();
			
			//verify if the phone number visible in feedback message is same as in DB 
			helper.payment.clickFeedBackButton();
			String number = helper.payment.getPhoneNumber();
			helper.payment.verifyPhoneNumber(number);

		} else {
			testConfig
					.logComment("FeedBack Flag is not equal to 1 hence feedback form is not visible	");
		}
		Assert.assertTrue(testConfig.getTestResult());

	}

//	/**
//	 * Verify the UI of FeedBack weather all 
//	 *  1. Heading "Feedback"
//		2. Message given is
//		"Facing a problem or have some feedback?
//		Leave us a message or give us a call on987655432110"
//		3. Feedback message container which can have 512 characters only
//		4. Submit feedback button at left lower corner
//		5. Close button on right top corner
//	 * @param testConfig
//	 */
//	@Test(description = "Verify UI of feedback", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
//	public void testUIOfFeedback(Config testConfig) {
//		
//		//execute the testcase only if cust_feedback_flag value=1
//		Map verifyDBValue = DataBase.executeSelectQuery(testConfig, 106, 1);
//		if (verifyDBValue.get("value").equals("1")) {
//			
//			//Navigate till Payment Option page
//			TransactionHelper helper = new TransactionHelper(testConfig, false);
//			helper.DoLogin();
//			helper.GetTestTransactionPage();
//			helper.transactionData = helper.trans.FillTransactionDetails(138);
//			helper.payment = helper.trans.Submit();
//			
//			//Verify UI of feedback
//			helper.payment.clickFeedBackButton();
//			helper.payment.verifyFeedbackMessage();
//			helper.payment.verifyFeedbackSubmit();
//			helper.payment.verifyHeadingFeedback();
//			helper.payment.verifyFeedbackClose();
//
//		} else {
//			testConfig
//					.logComment("FeedBack Flag is not equal to 1 hence feedback form is not visible	");
//		}
//		Assert.assertTrue(testConfig.getTestResult());
//
//	}

	/**
	 * Verify weather transaction is successful when feedback message gets submitted 
	 * @param testConfig
	 */
	//@Test(description = "Verify that transaction flow is working fine when feedback is On", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	//public void testThatTransactionFlowIsWorkingFineWhenFeedBackIsOn(
//			Config testConfig) {
//
//		//execute the testcase only if cust_feedback_flag value=1
//		Map verifyDBValue = DataBase.executeSelectQuery(testConfig, 106, 1);
//		if (verifyDBValue.get("value").equals("1")) {
//			
//
//			//Navigate till Payment Option page
//			TransactionHelper helper = new TransactionHelper(testConfig, false);
//			int transactionrownum = 138;
//			int carddetails = 1;
//			helper.DoLogin();
//			helper.GetTestTransactionPage();
//			helper.transactionData = helper.trans
//					.FillTransactionDetails(transactionrownum);
//			helper.payment = helper.trans.Submit();
//			String transacionID = helper.payment.getTransactionId();
//			transacionID = transacionID.substring(4);
//			testConfig.putRunTimeProperty("txnId", transacionID);
//			helper.payment.clickFeedBackButton();
//			//enter the feedback message
//			String message = Helper.generateRandomAlphabetsString(10);
//			helper.payment.writeFeedBackMessage(message);
//			helper.payment.submitFeedBackForm();
//			helper.payment.verifyFeedbackForm();
//			helper.payment.closeFeedBackForm();
//			//complete the transaction and verify if it was successful transaction or not
//			helper.ccTab = helper.payment.clickCCTab();
//			Map map = DataBase.executeSelectQuery(testConfig, 109, 1);
//			testConfig.putRunTimeProperty("id", map.get("id"));
//			String feedbackmsg = DataBase
//					.executeSelectQuery(testConfig, 110, 1).get("feedback_msg");
//			Helper.compareEquals(testConfig, "feedback_msg", message,
//					feedbackmsg);
//			helper.ccTab.FillCardDetails(carddetails);
//			helper.testResponse = helper.payment.clickPayNow();
//			helper.testResponse.VerifyTransactionResponse(new TestDataReader(
//					testConfig, "TransactionDetails"), transactionrownum,
//					new TestDataReader(testConfig, "PaymentType"), 5);
//
//		} else {
//			testConfig
//					.logComment("FeedBack Flag is not equal to 1 hence feedback form is not visible	");
//		}
//		Assert.assertTrue(testConfig.getTestResult());
//
//	}

	/**
	 * Verify weather all the special characters are not submitted in the DB only  email regular expressions are allowed
	 * @param testConfig
	 */
	@Test(description = "Verify that all special characters are not saved in database", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void testAllSpecialCharactersAreNotStoredInDB(
			Config testConfig) {

		//execute the testcase only if cust_feedback_flag value=1
		Map verifyDBValue = DataBase.executeSelectQuery(testConfig, 106, 1);
		if (verifyDBValue.get("value").equals("1")) {
			

			//Navigate till Payment Option page
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			int transactionrownum = 138;
			helper.DoLogin();
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans
					.FillTransactionDetails(transactionrownum);
			helper.payment = helper.trans.Submit();
			String texid = helper.payment.getTransactionId();
			texid = texid.substring(4);
			System.out.println(texid);
			testConfig.putRunTimeProperty("txnId", texid);
			
			//enter all the special characters in the feedback message
			helper.payment.clickFeedBackButton();
			String message = "~!@#$%^&*()_+`-=][\';/.,|:?><\"";
			helper.payment.writeFeedBackMessage(message);
			helper.payment.submitFeedBackForm();
			helper.payment.verifyFeedbackForm();
			helper.payment.closeFeedBackForm();
			
			//check weather all the special characters are entered or only  email regular expressions are allowed
			Map map = DataBase.executeSelectQuery(testConfig, 109, 1);
			testConfig.putRunTimeProperty("id", map.get("id"));
			String feedbackmsg = DataBase
					.executeSelectQuery(testConfig, 110, 1).get("feedback_msg");
			String expectedchar = "!@&()_-'.,:?";
			if (expectedchar.equals(feedbackmsg)) {
				testConfig
						.logComment("Only email regexp are allowed in the DB");
			} else {
				testConfig.logComment("All Special characters are entered");
			}

		} else {
			testConfig
					.logComment("FeedBack Flag is not equal to 1 hence feedback form is not visible	");
		}
		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verify if FeedBack Message allows only 512 characters in the message box and also check the same in DB
	 * @param testConfig
	 */
	@Test(description = "Verify that feedback allows only 512 characters", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void testMaximumLengthOfFeedBackMessage(Config testConfig) {

		//execute the testcase only if cust_feedback_flag value=1
		Map verifyDBValue = DataBase.executeSelectQuery(testConfig, 106, 1);
		if (verifyDBValue.get("value").equals("1")) {

			//Navigate till Payment Option page
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			helper.DoLogin();
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(138);
			helper.payment = helper.trans.Submit();
			helper.payment.clickFeedBackButton();
			
			//enter a large message which is larger than 512 characters and check if it entered more than 512
			String message = Helper.generateRandomAlphabetsString(600);
			helper.payment.writeFeedBackMessage(message);
			helper.payment.feedbackMessageLength();
			helper.payment.submitFeedBackForm();
			helper.payment.verifyFeedbackForm();
			helper.payment.closeFeedBackForm();
			
			//go to DB and get the message entered and check if it is more than 512 characters
			Map map = DataBase.executeSelectQuery(testConfig, 109, 1);
			testConfig.putRunTimeProperty("id", map.get("id"));
			String feedbackmsg = DataBase
					.executeSelectQuery(testConfig, 110, 1).get("feedback_msg");
			Helper.compareEquals(testConfig,
					"Feedback Message max 512 characters", 512,
					feedbackmsg.length());

		} else {
			testConfig
					.logComment("FeedBack Flag is not equal to 1 hence feedback form is not visible	");
		}
		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verify if the FeedBack Message is available in the retry page and also check if the message is getting save in DB or not
	 * @param testConfig
	 */
	@Test(description = "Verify feedback on retry page", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void testFeedBackMessageOnRetryPage(Config testConfig) {

		//execute the testcase only if cust_feedback_flag value=1
		Map verifyDBValue = DataBase.executeSelectQuery(testConfig, 106, 1);
		if (verifyDBValue.get("value").equals("1")) {

			//Navigate to retry payement options page
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			int transactionrownum = 138;
			int carddetails = 55;
			helper.DoLogin();
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans
					.FillTransactionDetails(transactionrownum);
			helper.payment = helper.trans.Submit();
			helper.ccTab = helper.payment.clickCCTab();
			helper.ccTab.FillCardDetails(carddetails);
			String texid = helper.payment.getTransactionId();
			helper.payment.clickPayNowToGetTryAgainPage();
			texid = texid.substring(4);
			testConfig.putRunTimeProperty("txnId", texid);
			//write the feedback message and check in DB weather message entered correctly or not.
			helper.payment.clickFeedBackButton();
			String message = Helper.generateRandomAlphabetsString(10);
			helper.payment.writeFeedBackMessage(message);
			helper.payment.submitFeedBackForm();
			helper.payment.verifyFeedbackForm();
			helper.payment.closeFeedBackForm();
			Map map = DataBase.executeSelectQuery(testConfig, 109, 1);
			testConfig.putRunTimeProperty("id", map.get("id"));
			String feedbackmsg = DataBase
					.executeSelectQuery(testConfig, 110, 1).get("feedback_msg");
			Helper.compareEquals(testConfig, "feedback_msg", message,
					feedbackmsg);

		} else {
			testConfig
					.logComment("FeedBack Flag is not equal to 1 hence feedback form is not visible	");
		}
		Assert.assertTrue(testConfig.getTestResult());
	}
	
	
	/**
	 * Verify weather controls like Cntrl + X, Cntrl + C, Cntrl +V are working in FeedBack Message
	 * @param testConfig
	 */
	@Test(description = "Verify that other control works on feedback container", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void testOtherControlOnFeedBackMessage(Config testConfig) {

		//execute the testcase only if cust_feedback_flag value=1
		Map verifyDBValue = DataBase.executeSelectQuery(testConfig, 106, 1);
		if (verifyDBValue.get("value").equals("1")) {
			//Navigate till Payment Option page
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			int transactionrownum = 138;
			helper.DoLogin();
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans
					.FillTransactionDetails(transactionrownum);
			helper.payment = helper.trans.Submit();
			String texid = helper.payment.getTransactionId();
			texid = texid.substring(4);
			testConfig.putRunTimeProperty("txnId", texid);
			//enter message and perform some operations like Ctrl + X, Cntrl + V, Cntrl + C
			helper.payment.clickFeedBackButton();
			String message = Helper.generateRandomAlphabetsString(10);
			helper.payment.writeFeedBackMessage(message);
			message = helper.payment.cutMessage();
			helper.payment.pasteMessage(message);
			helper.payment.submitFeedBackForm();
			helper.payment.verifyFeedbackForm();
			helper.payment.closeFeedBackForm();
			Map map = DataBase.executeSelectQuery(testConfig, 109, 1);
			testConfig.putRunTimeProperty("id", map.get("id"));
			String feedbackmsg = DataBase
					.executeSelectQuery(testConfig, 110, 1).get("feedback_msg");
			Helper.compareEquals(testConfig, "feedback_msg", message,
					feedbackmsg);
		} else {
			testConfig
					.logComment("FeedBack Flag is not equal to 1 hence feedback form is not visible	");
		}
		Assert.assertTrue(testConfig.getTestResult());
	}

	/**
	 * Verify weather the Masking is performed successfully in DB when a valid card is entered
	 * @param testConfig
	 */
	@Test(description = "Verify that card number entered correctly", dataProvider = "GetTestConfig", timeOut = 600000, groups = "1")
	public void testCardNumberMasking(Config testConfig) {

		//execute the testcase only if cust_feedback_flag value=1
		Map verifyDBValue = DataBase.executeSelectQuery(testConfig, 106, 1);
		if (verifyDBValue.get("value").equals("1")) {
			//Navigate till Payment Option page
			TransactionHelper helper = new TransactionHelper(testConfig, false);
			helper.DoLogin();
			helper.GetTestTransactionPage();
			helper.transactionData = helper.trans.FillTransactionDetails(138);
			helper.payment = helper.trans.Submit();
			//enter a valid Card Number in the feedback message
			helper.payment.clickFeedBackButton();
			TestDataReader treader = new TestDataReader(testConfig,
					"CardDetails");
			String message = treader.GetData(1, "CC");
			String[] messageSubstr = {
					message.subSequence(0, 6).toString(),
					message.subSequence(6, 12).toString(),
					message.subSequence((message.length() - 4),
							message.length()).toString() };
			helper.payment.writeFeedBackMessage(message);
			helper.payment.feedbackMessageLength();
			helper.payment.submitFeedBackForm();
			helper.payment.verifyFeedbackForm();
			helper.payment.closeFeedBackForm();
			//get the card number from DB and check if it is same as entered
			Map map = DataBase.executeSelectQuery(testConfig, 109, 1);
			testConfig.putRunTimeProperty("id", map.get("id"));
			String feedbackmsg = DataBase
					.executeSelectQuery(testConfig, 110, 1).get("feedback_msg");
			String[] feedbackSubstr = {
					feedbackmsg.subSequence(0, 6).toString(),
					feedbackmsg.subSequence(6, 12).toString(),
					feedbackmsg.subSequence((feedbackmsg.length() - 4),
							feedbackmsg.length()).toString() };
			Helper.compareEquals(testConfig, "Comparing first 6 numbers",
					messageSubstr[0], feedbackSubstr[0]);
			Helper.compareEquals(testConfig,
					"Comparing XXXXXX from card number", "XXXXXX",
					feedbackSubstr[1]);
			Helper.compareEquals(testConfig, "Comparing first 6 numbers",
					messageSubstr[2], feedbackSubstr[2]);
		} else {
			testConfig
					.logComment("FeedBack Flag is not equal to 1 hence feedback form is not visible	");
		}
		Assert.assertTrue(testConfig.getTestResult());
	}
}
