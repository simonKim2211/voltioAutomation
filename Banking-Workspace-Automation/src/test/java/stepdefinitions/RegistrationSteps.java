package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.RegistrationPage;
import utils.ConfigReader;
import utils.DriverUtilities;

public class RegistrationSteps {

    private RegistrationPage registrationPage;
    private static String sharedRegistrationEmail;

    @Given("user opens the registration page link")
    public void user_opens_the_registration_page_link() {
        registrationPage = new RegistrationPage(DriverUtilities.getInstance().getDriver());
        registrationPage.openRegistrationPage(buildRegistrationUrl(ConfigReader.getBaseUrl()));
    }

    @When("registration account type step is displayed")
    public void registration_account_type_step_is_displayed() {
        registrationPage.waitForRegistrationStepOne();
    }

    @And("user selects account type {string} and continues registration")
    public void user_selects_account_type_and_continues_registration(String accountType) {
        registrationPage.selectAccountTypeAndContinue(resolveAccountType(accountType));
    }

    @And("user enters registration name {string} email {string} password {string} and address {string}")
    public void user_enters_registration_details(String name, String email, String password, String address) {
        registrationPage.enterName(resolveName(name));
        registrationPage.enterEmail(resolveEmail(email));
        registrationPage.enterPassword(resolvePassword(password));
        registrationPage.enterAddress(resolveAddress(address));
    }

    @And("user submits the registration form")
    public void user_submits_the_registration_form() {
        registrationPage.submitRegistration();
    }

    @Then("user should see registration result {string}")
    public void user_should_see_registration_result(String expectedResult) {
        if ("success".equalsIgnoreCase(expectedResult)) {
            Assert.assertTrue("Expected successful registration, but it did not succeed.",
                    registrationPage.isRegistrationSuccessful());
        } else {
            Assert.assertTrue("Expected registration error, but no error state was detected.",
                    registrationPage.isRegistrationErrorDisplayed());
        }
    }

    private String resolveName(String name) {
        if (name == null) {
            return "";
        }

        String token = name.trim().toLowerCase();
        if ("autoname".equals(token)) {
            return "User" + System.currentTimeMillis();
        }
        if ("blank".equals(token) || "empty".equals(token)) {
            return "";
        }

        return name;
    }

    private String resolveAccountType(String accountType) {
        if (accountType == null) {
            return "Personal";
        }

        String token = accountType.trim().toLowerCase();
        switch (token) {
            case "business":
                return "Business";
            case "personal":
            default:
                return "Personal";
        }
    }

    private String buildRegistrationUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            return "https://frontend-524103119199.northamerica-northeast2.run.app/register";
        }

        String normalized = baseUrl.trim();
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        return normalized + "/register";
    }

    private String resolveEmail(String email) {
        if (email == null) {
            return "";
        }

        String token = email.trim().toLowerCase();
        if ("uniqueemail".equals(token)) {
            return "user." + System.currentTimeMillis() + "@example.com";
        }
        if ("sharedregistrationemail".equals(token)) {
            if (sharedRegistrationEmail == null) {
                sharedRegistrationEmail = "shared." + System.currentTimeMillis() + "@example.com";
            }
            return sharedRegistrationEmail;
        }
        if ("invalidemail".equals(token)) {
            return "invalid-email-format";
        }
        if ("blank".equals(token) || "empty".equals(token)) {
            return "";
        }

        return email;
    }

    private String resolvePassword(String password) {
        if (password == null) {
            return "";
        }

        String token = password.trim().toLowerCase();
        if ("strongpassword".equals(token)) {
            return "Strong@12345";
        }
        if ("weakpassword".equals(token)) {
            return "12345";
        }
        if ("blank".equals(token) || "empty".equals(token)) {
            return "";
        }

        return password;
    }

    private String resolveAddress(String address) {
        if (address == null) {
            return "";
        }

        String token = address.trim().toLowerCase();
        if ("autoaddress".equals(token)) {
            return "100 Main Street";
        }
        if ("blank".equals(token) || "empty".equals(token)) {
            return "";
        }

        return address;
    }
}

