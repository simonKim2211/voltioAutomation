package stepdefinitions;

import org.junit.Assert;

import io.cucumber.java.en.*;
import pages.LoginPage;
import utils.ConfigReader;
import utils.DriverUtilities;

public class LoginSteps {

    private LoginPage loginPage;

    @Given("user is on the home page")
    public void user_is_on_the_home_page() {
        loginPage = new LoginPage(DriverUtilities.getInstance().getDriver());
        loginPage.openHomePage(ConfigReader.getBaseUrl());
    }

    @When("user clicks the Login navigation button")
    public void user_clicks_the_login_navigation_button() {
        loginPage.clickLoginNavButton();
        Assert.assertTrue("User was not navigated to login page.", loginPage.isOnLoginPage());
    }

    @When("user enters email {string} and password {string}")
    public void user_enters_email_and_password(String email, String password) {
        loginPage.enterEmail(resolveTestData(email));
        loginPage.enterPassword(resolveTestData(password));
    }

    @When("user clicks the Sign In button")
    public void user_clicks_the_sign_in_button() {
        loginPage.clickSignInButton();
    }

    @Then("user should see login result {string}")
    public void user_should_see_login_result(String expectedResult) {
        if (expectedResult.equalsIgnoreCase("success")) {
            Assert.assertTrue("Expected login success, but login failed.", loginPage.isLoginSuccessful());
        } else {
            Assert.assertTrue("Expected login error, but no error was found.", loginPage.isLoginErrorDisplayed());
        }
    }

    private String resolveTestData(String value) {
        if (value == null) {
            return "";
        }

        switch (value.trim().toLowerCase()) {
            case "validusername":
                return ConfigReader.getValidUsername();
            case "validpassword":
                return ConfigReader.getValidPassword();
            case "invalidpassword":
                return ConfigReader.getInvalidPassword();
            case "unregisteredemail":
                return "unregistered." + System.currentTimeMillis() + "@example.com";
            case "blank":
            case "empty":
                return "";
            default:
                return value;
        }
    }
}