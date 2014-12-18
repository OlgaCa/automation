package PageObject.MerchantPanel.Settings;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Browser;
import Utils.Config;
import Utils.Element;
import Utils.Element.How;
import Utils.TestDataReader;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

public class MerchantCheckoutPage {
	private Config testConfig;
	
	@FindBy(css="span.mceIcon.mce_code")
	private WebElement ClickHtml;
	
	@FindBy(id="htmlSource")
	private WebElement HtmlSource;
	
	@FindBy(id="insert")
	private WebElement UpdateHtml;
	
	@FindBy(id="submit")
	private WebElement SubmitHtml;
	

	public MerchantCheckoutPage(Config testConfig)
	{
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(this.testConfig, ClickHtml);
	}
	
	public String getHtmlText(){
		String htmlText = null;
		if(Element.IsElementDisplayed(testConfig, Element.getPageElement(testConfig, How.id, "contents"))){
		 htmlText =  Element.getPageElement(testConfig, How.id, "contents").getText().replaceAll("<[^>]+>", "");
		}
		return htmlText;
	}
	
	public void EditHtmlSource() {
		Element.click(testConfig, ClickHtml, "Click on HtmlSource");
		Browser.wait(testConfig, 3);
		
	}
	
	public void Submit(){
		Element.click(testConfig, SubmitHtml, "Submit on HtmlSource");
		
	}
	
	

}