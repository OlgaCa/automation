package Utils;

import java.util.Properties;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import javax.mail.*;
import Utils.Browser;
import Utils.Config;
import Utils.GmailVerification;
import Utils.Element.How;

public class GmailVerification {

	private Config testConfig;

	public GmailVerification(Config testConfig) {
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, searchButton);
	}

	@FindBy(linkText="Create a filter")
	private WebElement createFilter;

	@FindBy(name="cf1_subj")
	private WebElement subJect;

	@FindBy(name="cf1_has")
	private WebElement hasthewords;

	@FindBy(name="nvp_bu_tssb")
	private WebElement clickTestSearch;
	
	@FindBy(id="gbqfbw")
	private WebElement searchButton;

	@FindBy(css="div.UJ div.Cp div table tbody tr")
	private WebElement selectEmail;
	public void verifyEmailText(String subject) {
		String emailText = selectEmail.getText();
		Helper.compareContains(testConfig, "Subject", subject, emailText);
	}

	@FindBy(css="div.msg")
	private WebElement checkEmail;	
	public void verifyEmailContent(String has_the_word) {
		String emailContent = checkEmail.getText();
		Helper.compareContains(testConfig, "Has_the_word", has_the_word, emailContent);
	}

	@FindBy(xpath="//tr[4]/td[2]/a")
	private WebElement invoiceLink;
	
	public void FilterEmail(String subject, String has_the_words) {

		//Creating a filter
		Element.click(testConfig, createFilter, "Create Filter");
		Element.enterData(testConfig, subJect, subject, "Subject");
		Element.enterData(testConfig, hasthewords, has_the_words, "Has the words");
		Element.click(testConfig,clickTestSearch, "Click on Test Search");
	}

	public void SelectEmail() {
		Element.click(testConfig,selectEmail, "Select the Email");
	}
	
	public void searchMail(String searchkey) {
		Element.enterData(testConfig, Element.getPageElement(testConfig, How.id, "gbqfq"), searchkey + Keys.ENTER, "search for txnid");
	}
	
}

