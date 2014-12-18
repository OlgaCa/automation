package PageObject.NewMerchantPanel.Transactions;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utils.Config;
import Utils.Element;
import Utils.Helper;

/** Consist of filter options page that is displayed on clicking filter
 * @author sharadjain
 *
 */
public class CustomizeColumnsPage {

	private HashMap<String, WebElement> Customizationcheckboxes ;
	
	private Config testConfig;
	
	@FindBy(css ="#CustomizeColumns>ul>li>ul>li>label")
	private List<WebElement> listOfCheckboxes;
	
	public CustomizeColumnsPage(Config testConfig){
		this.testConfig = testConfig;
		PageFactory.initElements(this.testConfig.driver, this);
		getAllCheckboxes();
		displayNameOfCheckBoxes();
	}

	/**Gets all checkboxes and stores it in Customizationcheckboxes collection
	 * 
	 */
	private void getAllCheckboxes() {
		Customizationcheckboxes = new HashMap<String, WebElement>();
		for (WebElement Checkbox : listOfCheckboxes) {
			Customizationcheckboxes.put(Checkbox.getText(), Checkbox.findElement(By.tagName("span")));
		}
		
	}
	
	/**Displays names of all checkboxes
	 * 
	 */
	public void displayNameOfCheckBoxes(){
		for (String key : Customizationcheckboxes.keySet()) {
			testConfig.logComment(key);
			
		}
	}
	
	/**Marks specified column checkbox as checked
	 * @param checkboxName - Name of the customize column checkbox
	 */
	public void selectCheckbox(String checkboxName){
		WebElement CheckboxToBeSelected = Customizationcheckboxes.get(checkboxName);
		Element.click(testConfig, CheckboxToBeSelected, checkboxName+" Checkbox");
	}
	
	/** Verifies list of names of checkboxes specified is not displayed 
	 * @param CheckBoxesNotToBeDisplayed - list of checkboxes that should not be displayed
	 */
	public void verifyOptionsNotPresentInTheList(
			List<String> CheckBoxesNotToBeDisplayed) {
		for (String checkboxName : CheckBoxesNotToBeDisplayed) {

			Helper.compareTrue(testConfig, checkboxName + " checkbox not present",
					Customizationcheckboxes.get(checkboxName) == null);
		}

	}
	
	
}

