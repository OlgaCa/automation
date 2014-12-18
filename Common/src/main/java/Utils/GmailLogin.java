package Utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Element.How;

public class GmailLogin {
	private Config testConfig;

	public GmailLogin(Config testConfig) {
		this.testConfig = testConfig;
		Browser.navigateToURL(testConfig, "https://mail.google.com/mail/&hl=en");
		PageFactory.initElements(this.testConfig.driver, this);
		Browser.waitForPageLoad(testConfig, Signin);
	}

	@FindBy(id="Email")
	private WebElement email;
	
	@FindBy(id="Passwd")
	private WebElement password;
	
	@FindBy(id="signIn")
	private WebElement Signin;
	
	@FindBy(id="gmail-sign-in")
	private WebElement SigninButton;
	
	 public GmailVerification Login(String userName, String passWord) {

			//	String url = "https://mail.google.com/mail/h/";
				//Browser.navigateToURL(testConfig,url);
		      //  Browser.wait(testConfig, 4);
		     //   Element.getPageElement(testConfig, How.id, "signIn").click();
		        Browser.wait(testConfig, 4);
		        //Enter email credentials
		        Element.enterData(testConfig, email, userName, "User Name");
		        Element.enterData(testConfig, password, passWord, "Password");
				Element.click(testConfig, Signin, "Signing in");
				Browser.wait(testConfig, 10);
				return new GmailVerification(testConfig);	
			}
			

}
