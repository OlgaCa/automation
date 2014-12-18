package Test.Security;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class testHDFC {
	@Test
	public void testCiti() throws Exception{
	WebDriver driver = new FirefoxDriver();
	String baseUrl="https://admin.payu.in/";
	int retry=1000000;

	driver.get(baseUrl + "/home");
	driver.findElement(By.linkText("Admin Login")).click();
	driver.findElement(By.id("username")).clear();
	driver.findElement(By.id("username")).sendKeys("qc");
	driver.findElement(By.id("password")).clear();
	driver.findElement(By.id("password")).sendKeys("password1");
	driver.findElement(By.id("Submit")).click();
	driver.findElement(By.linkText("Test Transaction")).click();
	new Select(driver.findElement(By.name("key"))).selectByVisibleText("Tradus - tradus");
	driver.findElement(By.name("pg")).clear();
	driver.findElement(By.name("pg")).sendKeys("CC");
	driver.findElement(By.name("bankcode")).clear();
	driver.findElement(By.name("bankcode")).sendKeys("CC");
	driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
	for (int i=0;i<100;i++){
		System.out.println("i is "+i+" - "+driver.getCurrentUrl());
	}

}
}