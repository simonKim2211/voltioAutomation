package com.digitalbanking.api.stepdefs;

import com.digitalbanking.api.clients.AuthClient;
import com.digitalbanking.api.clients.CustomerClient;
import com.digitalbanking.api.context.TestContext;
import com.digitalbanking.api.models.CustomerRequest;
import com.digitalbanking.api.models.CustomerUpdateRequest;
import com.digitalbanking.api.models.LoginRequest;
import com.digitalbanking.api.models.RegisterRequest;
import com.digitalbanking.api.utils.DataUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class CustomerSteps {

    private final TestContext testContext;
    private final AuthClient authClient;
    private final CustomerClient customerClient;

    private CustomerRequest customerRequest;
    private CustomerUpdateRequest customerUpdateRequest;

    public CustomerSteps(TestContext testContext) {
        this.testContext = testContext;
        this.authClient = new AuthClient();
        this.customerClient = new CustomerClient();
    }

    @Given("I am an authenticated customer user")
    public void iAmAnAuthenticatedCustomerUser() {
        String username = DataUtils.generateUniqueEmail();
        String password = DataUtils.getValidPassword();

        Response registerResponse = authClient.register(new RegisterRequest(username, password));

        if (registerResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: registration did not return 201. Actual status: "
                            + registerResponse.getStatusCode()
                            + "\nResponse body: "
                            + registerResponse.asPrettyString()
            );
        }

        Response loginResponse = authClient.login(new LoginRequest(username, password));

        if (loginResponse.getStatusCode() != 200) {
            throw new AssertionError(
                    "Precondition failed: login did not return 200. Actual status: "
                            + loginResponse.getStatusCode()
                            + "\nResponse body: "
                            + loginResponse.asPrettyString()
            );
        }

        String accessToken = loginResponse.jsonPath().getString("accessToken");

        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new AssertionError("Precondition failed: accessToken was missing from login response.");
        }

        testContext.setData("username", username);
        testContext.setData("password", password);
        testContext.setAccessToken(accessToken);
    }

    @Given("I have valid PERSON customer details")
    public void iHaveValidPersonCustomerDetails() {
        customerRequest = new CustomerRequest(
                DataUtils.getPersonCustomerName(),
                DataUtils.getCustomerAddress(),
                DataUtils.getPersonCustomerType()
        );

        testContext.setData("expectedCustomerName", DataUtils.getPersonCustomerName());
        testContext.setData("expectedCustomerAddress", DataUtils.getCustomerAddress());
        testContext.setData("expectedCustomerType", DataUtils.getPersonCustomerType());
    }

    @Given("I have valid COMPANY customer details")
    public void iHaveValidCompanyCustomerDetails() {
        customerRequest = new CustomerRequest(
                DataUtils.getCompanyCustomerName(),
                DataUtils.getCustomerAddress(),
                DataUtils.getCompanyCustomerType()
        );

        testContext.setData("expectedCustomerName", DataUtils.getCompanyCustomerName());
        testContext.setData("expectedCustomerAddress", DataUtils.getCustomerAddress());
        testContext.setData("expectedCustomerType", DataUtils.getCompanyCustomerType());
    }

    @Given("I have customer details with missing required fields")
    public void iHaveCustomerDetailsWithMissingRequiredFields() {
        customerRequest = new CustomerRequest(
                null,
                null,
                null
        );
    }

    @Given("I have customer details with invalid customer type")
    public void iHaveCustomerDetailsWithInvalidCustomerType() {
        customerRequest = new CustomerRequest(
                DataUtils.getPersonCustomerName(),
                DataUtils.getCustomerAddress(),
                DataUtils.getInvalidCustomerType()
        );
    }

    @Given("I have already created a valid customer")
    public void iHaveAlreadyCreatedAValidCustomer() {
        customerRequest = new CustomerRequest(
                DataUtils.getPersonCustomerName(),
                DataUtils.getCustomerAddress(),
                DataUtils.getPersonCustomerType()
        );

        testContext.setData("expectedCustomerName", DataUtils.getPersonCustomerName());
        testContext.setData("expectedCustomerAddress", DataUtils.getCustomerAddress());
        testContext.setData("expectedCustomerType", DataUtils.getPersonCustomerType());

        Response createCustomerResponse = customerClient.createCustomer(
                testContext.getAccessToken(),
                customerRequest
        );

        if (createCustomerResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: customer creation did not return 201. Actual status: "
                            + createCustomerResponse.getStatusCode()
                            + "\nResponse body: "
                            + createCustomerResponse.asPrettyString()
            );
        }

        Long customerId = createCustomerResponse.jsonPath().getLong("customerId");

        if (customerId == null) {
            throw new AssertionError("Precondition failed: customerId was missing from create customer response.");
        }

        testContext.setData("customerId", customerId);
        testContext.setResponse(createCustomerResponse);
    }

    @Given("I have valid customer update details")
    public void iHaveValidCustomerUpdateDetails() {
        customerUpdateRequest = new CustomerUpdateRequest(
                DataUtils.getUpdatedCustomerName(),
                DataUtils.getUpdatedCustomerAddress()
        );

        testContext.setData("expectedUpdatedCustomerName", DataUtils.getUpdatedCustomerName());
        testContext.setData("expectedUpdatedCustomerAddress", DataUtils.getUpdatedCustomerAddress());
    }

    @Given("I have customer update details with immutable email field")
    public void iHaveCustomerUpdateDetailsWithImmutableEmailField() {
        customerUpdateRequest = new CustomerUpdateRequest(
                DataUtils.getUpdatedCustomerName(),
                DataUtils.getUpdatedCustomerAddress()
        );

        customerUpdateRequest.setEmail(DataUtils.getImmutableEmail());
    }

    @Given("I have customer update details with immutable accountNumber field")
    public void iHaveCustomerUpdateDetailsWithImmutableAccountNumberField() {
        customerUpdateRequest = new CustomerUpdateRequest(
                DataUtils.getUpdatedCustomerName(),
                DataUtils.getUpdatedCustomerAddress()
        );

        customerUpdateRequest.setAccountNumber(DataUtils.getImmutableAccountNumber());
    }

    @When("I send the create customer request")
    public void iSendTheCreateCustomerRequest() {
        testContext.setResponse(
                customerClient.createCustomer(
                        testContext.getAccessToken(),
                        customerRequest
                )
        );
    }

    @When("I send the create customer request without token")
    public void iSendTheCreateCustomerRequestWithoutToken() {
        testContext.setResponse(
                customerClient.createCustomerWithoutToken(customerRequest)
        );
    }

    @When("I send the get customer request")
    public void iSendTheGetCustomerRequest() {
        Object customerId = testContext.getData("customerId");

        testContext.setResponse(
                customerClient.getCustomer(
                        testContext.getAccessToken(),
                        customerId
                )
        );
    }

    @When("I send the get customer request with invalid customer ID")
    public void iSendTheGetCustomerRequestWithInvalidCustomerId() {
        testContext.setResponse(
                customerClient.getCustomer(
                        testContext.getAccessToken(),
                        "abc"
                )
        );
    }

    @When("I send the update customer request")
    public void iSendTheUpdateCustomerRequest() {
        Object customerId = testContext.getData("customerId");

        testContext.setResponse(
                customerClient.updateCustomer(
                        testContext.getAccessToken(),
                        customerId,
                        customerUpdateRequest
                )
        );
    }
}