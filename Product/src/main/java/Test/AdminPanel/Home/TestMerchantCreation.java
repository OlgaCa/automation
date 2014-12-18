package Test.AdminPanel.Home;

import Utils.TestBase;

public class TestMerchantCreation extends TestBase{

	public TestMerchantCreation()
	{
		//super("Product");
	}

	/*@Test(description = "Verify that merchant can be created with the details given")
	public void test_createMerchant() 
	{
		int createMerchantRow = 1;
		Boolean isPass = true;
		TransactionHelper helper = new TransactionHelper(conf.driver, false);
		helper.DoLogin();

		CreateMerchantPage createMerchant = helper.home.ClickCreateNewMerchant();
		TestDataReader data = createMerchant.FillMerchantDetails(createMerchantRow);
		helper.home = createMerchant.Submit();

		String merchantName = data.GetData(createMerchantRow, "MerchantName");
		MerchantListPage list = helper.home.SearchMerchant(merchantName);
		list.ApproveFirstMerchant();

		list.SearchMerchant(merchantName);
		list.ActivateFirstMerchant();

		isPass = list.VerifyMerchantDetails(data, createMerchantRow);
		Assert.assertTrue(isPass);
	}

	@Test(dataProvider = "MerchantGateway_DataRows")
	public void test_MerchantGateways(int merchantRow, int gatewayRow) 
	{
		Boolean isPass = true;
		TransactionHelper helper = new TransactionHelper(conf.driver, false);
		helper.DoLogin();

		TestDataReader data =  new TestDataReader(Config.TestDataSheet,"CreateMerchant"); 
		String merchantName = data .GetData(merchantRow, "MerchantName");
		MerchantListPage list = helper.home.SearchMerchant(merchantName);

		MerchantGatewayPage gateway = list.clickFirstMerchantGatewayLink();
		gateway.AddGateway(gatewayRow);
		//TODO - Add the verification code
	}

	@DataProvider
	public Object[][] MerchantGateway_DataRows() {
		return new Object[][] { { "6", "1" }};
	}

	@Test(dataProvider = "GatewayKey_DataRows")
	public void test_MerchantGatewayKeys(int merchantRow, int gatewayKeyRow) {

		Boolean isPass = true;
		TransactionHelper helper = new TransactionHelper(conf.driver, false);
		helper.DoLogin();

		TestDataReader data =  new TestDataReader(Config.TestDataSheet,"CreateMerchant"); 
		String merchantName = data .GetData(merchantRow, "MerchantName");
		MerchantListPage list = helper.home.SearchMerchant(merchantName);
		MerchantGatewayPage gateway = list.clickFirstMerchantGatewayLink();
		gateway.AddGateway(gatewayKeyRow);
		//TODO - Add the verification code
	}

	@DataProvider
	public Object[][] GatewayKey_DataRows() {
		return new Object[][] { { 6, 1 }};
	}*/

	/*@Test
	public void VerifyCreditCardMerchant()
	{
		AdminPage admin = new AdminPage(conf.driver);
		LoginPage login = admin.ClickAdminLogin();
		HomePage home = login.Login();
		TransactionPage trans = home.ClickTestTransaction();
	    trans.FillTransactionDetails(2);
		PaymentOptionsPage payment = trans.Submit();
		Assert.assertTrue(payment.CreditLinkIsPresent());
	}

	@Test
	public void VerifyDebitCardMerchant()
	{
		AdminPage admin = new AdminPage(conf.driver);
		LoginPage login = admin.ClickAdminLogin();
		HomePage home = login.Login();
		TransactionPage trans = home.ClickTestTransaction();
		trans.FillTransactionDetails("3");
		PaymentOptionsPage payment = trans.Submit();
		Assert.assertTrue(payment.DebitLinkIsPresent());
	}

	@Test
	public void VerifyNetBankingMerchant()
	{	
		AdminPage admin = new AdminPage(conf.driver);
		LoginPage login = admin.ClickAdminLogin();
		HomePage home = login.Login();
		TransactionPage trans = home.ClickTestTransaction();
	    trans.FillTransactionDetails("4");
		PaymentOptionsPage payment = trans.Submit();
		Assert.assertTrue(payment.NetBankingLinkIsPresent());
	}

	@Test
	public void VerifyEmiMerchant(){

		AdminPage admin = new AdminPage(conf.driver);
		LoginPage login = admin.ClickAdminLogin();
		HomePage home = login.Login();
		TransactionPage trans = home.ClickTestTransaction();
	    trans.FillTransactionDetails("6");
		PaymentOptionsPage payment = trans.Submit();
		Assert.assertTrue(payment.EmiLinkIsPresent());
	}

	@Test
	public void VerifyCodMerchant(){

		AdminPage admin = new AdminPage(conf.driver);
		LoginPage login = admin.ClickAdminLogin();
		HomePage home = login.Login();
		TransactionPage trans = home.ClickTestTransaction();
		trans.FillTransactionDetails("5");
		PaymentOptionsPage payment = trans.Submit();
		Assert.assertTrue(payment.CodLinkIsPresent());
	}*/
}
