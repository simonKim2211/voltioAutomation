package com.digitalbanking.api.stepdefs;

import com.digitalbanking.api.clients.AuthClient;
import com.digitalbanking.api.context.TestContext;
import com.digitalbanking.api.models.LoginRequest;
import com.digitalbanking.api.models.RegisterRequest;
import com.digitalbanking.api.utils.DataUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class AuthSteps {

    private final TestContext testContext;
    private final AuthClient authClient;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    public AuthSteps(TestContext testContext) {
        this.testContext = testContext;
        this.authClient = new AuthClient();
    }

    @Given("I have valid registration details")
    public void iHaveValidRegistrationDetails() {
        String username = DataUtils.generateUniqueEmail();
        String password = DataUtils.getValidPassword();

        registerRequest = new RegisterRequest(username, password);

        testContext.setData("username", username);
        testContext.setData("password", password);
    }

    @Given("I have registration details with invalid email")
    public void iHaveRegistrationDetailsWithInvalidEmail() {
        registerRequest = new RegisterRequest(
                DataUtils.getInvalidEmail(),
                DataUtils.getValidPassword()
        );
    }

    @Given("I have registration details with weak password")
    public void iHaveRegistrationDetailsWithWeakPassword() {
        registerRequest = new RegisterRequest(
                DataUtils.generateUniqueEmail(),
                DataUtils.getWeakPassword()
        );
    }

    @Given("I have already registered a valid user")
    public void iHaveAlreadyRegisteredAValidUser() {
        String username = DataUtils.generateUniqueEmail();
        String password = DataUtils.getValidPassword();

        registerRequest = new RegisterRequest(username, password);

        testContext.setResponse(authClient.register(registerRequest));

        if (testContext.getResponse().getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: user registration did not return 201. Actual status: "
                            + testContext.getResponse().getStatusCode()
                            + "\nResponse body: "
                            + testContext.getResponse().asPrettyString()
            );
        }

        testContext.setData("username", username);
        testContext.setData("password", password);
    }

    @When("I send the register request")
    public void iSendTheRegisterRequest() {
        testContext.setResponse(authClient.register(registerRequest));
    }

    @When("I send the register request again with the same username")
    public void iSendTheRegisterRequestAgainWithTheSameUsername() {
        String username = testContext.getString("username");
        String password = testContext.getString("password");

        RegisterRequest duplicateRegisterRequest = new RegisterRequest(username, password);

        testContext.setResponse(authClient.register(duplicateRegisterRequest));
    }

    @When("I send the login request with valid credentials")
    public void iSendTheLoginRequestWithValidCredentials() {
        loginRequest = new LoginRequest(
                testContext.getString("username"),
                testContext.getString("password")
        );

        testContext.setResponse(authClient.login(loginRequest));
    }

    @When("I send the login request with wrong password")
    public void iSendTheLoginRequestWithWrongPassword() {
        loginRequest = new LoginRequest(
                testContext.getString("username"),
                DataUtils.getWrongPassword()
        );

        testContext.setResponse(authClient.login(loginRequest));
    }

    @When("I access a protected endpoint without token")
    public void iAccessAProtectedEndpointWithoutToken() {
        testContext.setResponse(authClient.accessProtectedEndpointWithoutToken());
    }
}