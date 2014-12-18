package Utils;

public class SoftAssert {

	private static org.testng.asserts.SoftAssert soft;

	private SoftAssert(){
		
	}
	public static org.testng.asserts.SoftAssert getSoftAssert() {

		if (soft == null) {
			soft = new org.testng.asserts.SoftAssert();
		}
		return soft;

	}

}
