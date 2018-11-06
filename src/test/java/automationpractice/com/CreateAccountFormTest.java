package automationpractice.com;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import automationpractice.com.pageObject.Account;
import automationpractice.com.pageObject.CreateAccount;
import automationpractice.com.pageObject.CreateAccountForm;
import automationpractice.com.pageObject.Homepage;
import automationpractice.com.pageObject.SignInForm;
import utils.EmailsGenerator;

public class CreateAccountFormTest {

	private WebDriver driver;

	private Homepage homepage;
	private CreateAccount createAccount;
	private CreateAccountForm createAccountForm;
	private SignInForm signin;
	private Account account;

	@BeforeClass
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "c:\\Users\\jacek.lucarz\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();

		homepage = new Homepage(driver);
		createAccount = new CreateAccount(driver);
		createAccountForm = new CreateAccountForm(driver);
		signin = new SignInForm(driver);
		account = new Account(driver);

		String baseUrl = "http://automationpractice.com/index.php";
		driver.manage().window().maximize();
		driver.get(baseUrl);
	}

	@AfterClass
	public void closeAll() {
		account.getAccountLogout().click();
		driver.quit();
	}

	@Test(priority = 1)
	public void authenticationPage() {
		homepage.getSignInBtn().click();

		Assert.assertTrue(createAccount.getCreateAccountForm().isDisplayed());
		Assert.assertTrue(createAccount.getCreatAccountEmailField().isDisplayed());
		Assert.assertTrue(createAccount.getCreateAccountBtn().isDisplayed());
		Assert.assertTrue(signin.getSignInForm().isDisplayed());
	}

	@Test(priority = 2)
	public void authenticationPageEmailField() {
		// Without email
		createAccount.getCreateAccountBtn().click();

		Assert.assertTrue(createAccount.getEmailErrorMessage().isDisplayed());

		createAccount.setCreateAccountEmailField("jack");
		createAccount.getCreateAccountBtn().click();

		Assert.assertTrue(createAccount.getEmailErrorMessage().isDisplayed());
		Assert.assertTrue(createAccount.getEmailFieldHighlightedRed().isDisplayed());

		// Registered email
		createAccount.setCreateAccountEmailField("jacek.lucarz@gmail.com");
		createAccount.getCreateAccountBtn().click();

		Assert.assertTrue(createAccount.getEmailBeenRegistered().isDisplayed());

		// Correct email
		createAccount.setCreateAccountEmailField(EmailsGenerator.getNextEmail());
		createAccount.getCreateAccountBtn().click();

		Assert.assertTrue(createAccountForm.getAccountCreationForm().isDisplayed());
	}

	@Test(priority = 3)
	public void personalInfoFields() {
		// With values
		createAccountForm.setCustomerFirstNameField("Jacek");
		createAccountForm.setCustomerLastNameField("Lucarz");
		createAccountForm.setCustomerEmailField("jacek.lucarz@gmail.com");
		createAccountForm.setCustomerPasswordField("password");

		createAccountForm.getAccountCreationForm().click();

		Assert.assertTrue(createAccountForm.getFirstNameHighlightedGreen().isDisplayed());
		Assert.assertTrue(createAccountForm.getLastNameHighlightedGreen().isDisplayed());
		Assert.assertTrue(createAccountForm.getEmailHighlightedGreen().isDisplayed());
		Assert.assertTrue(createAccountForm.getPasswordHighlightedGreen().isDisplayed());

		// Without values
		createAccountForm.setCustomerFirstNameField("");
		createAccountForm.setCustomerLastNameField("");
		createAccountForm.setCustomerEmailField("");
		createAccountForm.setCustomerPasswordField("");

		createAccountForm.getAccountCreationForm().click();

		Assert.assertTrue(createAccountForm.getFirstNameHighlightedRed().isDisplayed());
		Assert.assertTrue(createAccountForm.getLastNameHighlightedRed().isDisplayed());
		Assert.assertTrue(createAccountForm.getEmailHighlightedRed().isDisplayed());
		Assert.assertTrue(createAccountForm.getPasswordHighlightedRed().isDisplayed());
	}

	@Test(priority = 4)
	public void requiredFieldsEmpty() {
		createAccountForm.setCustomerLastNameField("");
		createAccountForm.setCustomerPasswordField("");
		createAccountForm.selectCustomerDateOfBirthDay("");
		createAccountForm.selectCustomerDateOfBirthMonth("");
		createAccountForm.selectCustomerDateOfBirthYear("");
		createAccountForm.setAddressField("");
		createAccountForm.setCityField("");
		createAccountForm.selectState("");
		createAccountForm.setPostalCodeField("");
		createAccountForm.setHomePhoneField("");
		createAccountForm.setMobilePhoneField("");
		createAccountForm.setAddressAliasField("");
		createAccountForm.getRegisterBtn().click();
		createAccountForm.selectCountry("-");
		Assert.assertTrue(createAccountForm.getPhoneNumberError().isDisplayed());
		Assert.assertTrue(createAccountForm.getLastNameError().isDisplayed());
		Assert.assertTrue(createAccountForm.getFirstNameError().isDisplayed());
		Assert.assertTrue(createAccountForm.getEmailRequiredError().isDisplayed());
		Assert.assertTrue(createAccountForm.getPasswordRequiredError().isDisplayed());
		//Assert.assertTrue(createAccountForm.getCountryRequiredError().isDisplayed());
		Assert.assertTrue(createAccountForm.getAddressRequiredError().isDisplayed());
		Assert.assertTrue(createAccountForm.getAddressAliasRequiredError().isDisplayed());
		Assert.assertTrue(createAccountForm.getCityRequiredError().isDisplayed());
//		Assert.assertTrue(createAccountForm.getCountryUnselectedError().isDisplayed());
		createAccountForm.getRegisterBtn().click();

//		Assert.assertTrue(createAccountForm.getStateRequredError().isDisplayed());
//		Assert.assertTrue(createAccountForm.getPostalCodeError().isDisplayed());
	}

	@Test(priority = 5)
	public void requiredFieldsInputFormat() throws Exception {
		// Wrong format
		createAccountForm.setCustomerEmailField("jacek.lucarz@gmail");
		createAccountForm.setCustomerPasswordField("x");
		//createAccountForm.setPostalCodeField("123x");
		createAccountForm.setHomePhoneField("1234x");
		createAccountForm.setMobilePhoneField("1234x");

		createAccountForm.getRegisterBtn().click();

		Assert.assertTrue(createAccountForm.getEmailInvalidError().isDisplayed());
		Assert.assertTrue(createAccountForm.getPasswordInvalidError().isDisplayed());
		//Assert.assertTrue(createAccountForm.getPostalCodeError().isDisplayed());
		Assert.assertTrue(createAccountForm.getHomePhoneInvalidError().isDisplayed());
		Assert.assertTrue(createAccountForm.getMobilePhoneInvalidError().isDisplayed());

		// Correct format
		createAccountForm.setCustomerEmailField("jacek.lucarz@gmail.com");
		createAccountForm.setCustomerPasswordField("password");
		//createAccountForm.setPostalCodeField("12345");
		createAccountForm.setHomePhoneField("123234234");
		createAccountForm.setMobilePhoneField("123234234");

		Assert.assertTrue(createAccountForm.getEmailInvalidError().isDisplayed());
		Assert.assertTrue(createAccountForm.getPasswordInvalidError().isDisplayed());
		//Assert.assertTrue(createAccountForm.getPostalCodeError().isDisplayed());
		Assert.assertTrue(createAccountForm.getHomePhoneInvalidError().isDisplayed());
		Assert.assertTrue(createAccountForm.getMobilePhoneInvalidError().isDisplayed());

	}

	@Test(priority = 6)
	public void createAccountSuccessfully() {
		// Required fields filled
		createAccountForm.selectCountry("United States");
		createAccountForm.setCustomerFirstNameField("Maniek");
		createAccountForm.setCustomerLastNameField("Maniak");
		createAccountForm.setCustomerPasswordField("password");
		createAccountForm.selectCustomerDateOfBirthDay("11");
		createAccountForm.selectCustomerDateOfBirthMonth("11");
		createAccountForm.selectCustomerDateOfBirthYear("1911");
		createAccountForm.setAddressField("Warszawa");
		createAccountForm.setCityField("Warszawa");
		createAccountForm.selectState("1");
		createAccountForm.setPostalCodeField("12345");
		createAccountForm.setHomePhoneField("123123");
		createAccountForm.setMobilePhoneField("123123");
		createAccountForm.setAddressAliasField("pogorzelska");
		createAccountForm.getRegisterBtn().click();

//		Assert.assertTrue(createAccountForm.getEmailBeenRegistered().isDisplayed());

		createAccountForm.setCustomerEmailField(EmailsGenerator.getNextEmail());
		createAccountForm.setCustomerPasswordField("password");
		createAccountForm.getRegisterBtn().click();

		Assert.assertTrue(createAccountForm.successfullyCreatedAccount().isDisplayed());
	}
}
