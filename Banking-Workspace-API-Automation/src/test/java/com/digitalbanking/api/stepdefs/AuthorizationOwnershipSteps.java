package com.digitalbanking.api.stepdefs;

import com.digitalbanking.api.clients.AccountClient;
import com.digitalbanking.api.clients.AuthClient;
import com.digitalbanking.api.clients.CustomerClient;
import com.digitalbanking.api.clients.MoneyMovementClient;
import com.digitalbanking.api.context.TestContext;
import com.digitalbanking.api.models.*;
import com.digitalbanking.api.utils.DataUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class AuthorizationOwnershipSteps {

    private final TestContext testContext;
    private final AuthClient authClient;
    private final CustomerClient customerClient;
    private final AccountClient accountClient;
    private final MoneyMovementClient moneyMovementClient;

    private CustomerUpdateRequest customerUpdateRequest;
    private MoneyMovementRequest moneyMovementRequest;
    private TransferRequest transferRequest;

    public AuthorizationOwnershipSteps(TestContext testContext) {
        this.testContext = testContext;
        this.authClient = new AuthClient();
        this.customerClient = new CustomerClient();
        this.accountClient = new AccountClient();
        this.moneyMovementClient = new MoneyMovementClient();
    }

    @Given("Customer A and Customer B both exist with customer profiles")
    public void customerAAndCustomerBBothExistWithCustomerProfiles() {
        createCustomerUserA();
        createCustomerUserB();
    }

    @Given("Customer B has a CHECKING account")
    public void customerBHasACheckingAccount() {
        AccountRequest accountRequest = new AccountRequest(
                DataUtils.getCheckingAccountType(),
                DataUtils.getOpeningBalance(),
                null
        );

        Response response = accountClient.createAccount(
                testContext.getString("customerBToken"),
                testContext.getData("customerBCustomerId"),
                accountRequest
        );

        if (response.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: Customer B account creation failed. Actual status: "
                            + response.getStatusCode()
                            + "\nResponse body: "
                            + response.asPrettyString()
            );
        }

        testContext.setData("customerBAccountId", response.jsonPath().get("accountId"));
    }

    @Given("Customer B has source and destination CHECKING accounts")
    public void customerBHasSourceAndDestinationCheckingAccounts() {
        AccountRequest sourceAccountRequest = new AccountRequest(
                DataUtils.getCheckingAccountType(),
                DataUtils.getSourceAccountOpeningBalance(),
                null
        );

        Response sourceResponse = accountClient.createAccount(
                testContext.getString("customerBToken"),
                testContext.getData("customerBCustomerId"),
                sourceAccountRequest
        );

        if (sourceResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: Customer B source account creation failed. Actual status: "
                            + sourceResponse.getStatusCode()
                            + "\nResponse body: "
                            + sourceResponse.asPrettyString()
            );
        }

        AccountRequest destinationAccountRequest = new AccountRequest(
                DataUtils.getCheckingAccountType(),
                DataUtils.getDestinationAccountOpeningBalance(),
                null
        );

        Response destinationResponse = accountClient.createAccount(
                testContext.getString("customerBToken"),
                testContext.getData("customerBCustomerId"),
                destinationAccountRequest
        );

        if (destinationResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: Customer B destination account creation failed. Actual status: "
                            + destinationResponse.getStatusCode()
                            + "\nResponse body: "
                            + destinationResponse.asPrettyString()
            );
        }

        testContext.setData("customerBSourceAccountId", sourceResponse.jsonPath().get("accountId"));
        testContext.setData("customerBDestinationAccountId", destinationResponse.jsonPath().get("accountId"));
    }

    @Given("I have transfer details from Customer B source account")
    public void iHaveTransferDetailsFromCustomerBSourceAccount() {
        transferRequest = new TransferRequest(
                testContext.getData("customerBSourceAccountId"),
                testContext.getData("customerBDestinationAccountId"),
                DataUtils.getTransferAmount(),
                DataUtils.getTransferDescription()
        );
    }

    @When("Customer A sends a get customer request for Customer B")
    public void customerASendsAGetCustomerRequestForCustomerB() {
        testContext.setResponse(
                customerClient.getCustomer(
                        testContext.getString("customerAToken"),
                        testContext.getData("customerBCustomerId")
                )
        );
    }

    @When("Customer A sends an update customer request for Customer B")
    public void customerASendsAnUpdateCustomerRequestForCustomerB() {
        customerUpdateRequest = new CustomerUpdateRequest(
                DataUtils.getUpdatedCustomerName(),
                DataUtils.getUpdatedCustomerAddress()
        );

        testContext.setResponse(
                customerClient.updateCustomer(
                        testContext.getString("customerAToken"),
                        testContext.getData("customerBCustomerId"),
                        customerUpdateRequest
                )
        );
    }

    @When("Customer A sends a get account request for Customer B account")
    public void customerASendsAGetAccountRequestForCustomerBAccount() {
        testContext.setResponse(
                accountClient.getAccount(
                        testContext.getString("customerAToken"),
                        testContext.getData("customerBAccountId")
                )
        );
    }

    @When("Customer A sends a list accounts request for Customer B")
    public void customerASendsAListAccountsRequestForCustomerB() {
        testContext.setResponse(
                accountClient.listCustomerAccounts(
                        testContext.getString("customerAToken"),
                        testContext.getData("customerBCustomerId")
                )
        );
    }

    @When("Customer A sends a deposit request for Customer B account")
    public void customerASendsADepositRequestForCustomerBAccount() {
        moneyMovementRequest = new MoneyMovementRequest(
                DataUtils.getDepositAmount(),
                DataUtils.getDepositDescription()
        );

        testContext.setResponse(
                moneyMovementClient.deposit(
                        testContext.getString("customerAToken"),
                        testContext.getData("customerBAccountId"),
                        moneyMovementRequest
                )
        );
    }

    @When("Customer A sends a withdrawal request for Customer B account")
    public void customerASendsAWithdrawalRequestForCustomerBAccount() {
        moneyMovementRequest = new MoneyMovementRequest(
                DataUtils.getWithdrawalAmount(),
                DataUtils.getWithdrawalDescription()
        );

        testContext.setResponse(
                moneyMovementClient.withdraw(
                        testContext.getString("customerAToken"),
                        testContext.getData("customerBAccountId"),
                        moneyMovementRequest
                )
        );
    }

    @When("Customer A sends the transfer request")
    public void customerASendsTheTransferRequest() {
        testContext.setResponse(
                moneyMovementClient.transfer(
                        testContext.getString("customerAToken"),
                        transferRequest
                )
        );
    }

    @When("I send the list all customers request")
    public void iSendTheListAllCustomersRequest() {
        testContext.setResponse(
                customerClient.listAllCustomers(
                        testContext.getAccessToken()
                )
        );
    }

    private void createCustomerUserA() {
        String username = DataUtils.generateUniqueEmail();
        String password = DataUtils.getValidPassword();

        Response registerResponse = authClient.register(new RegisterRequest(username, password));

        if (registerResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: Customer A registration failed. Actual status: "
                            + registerResponse.getStatusCode()
                            + "\nResponse body: "
                            + registerResponse.asPrettyString()
            );
        }

        Response loginResponse = authClient.login(new LoginRequest(username, password));

        if (loginResponse.getStatusCode() != 200) {
            throw new AssertionError(
                    "Precondition failed: Customer A login failed. Actual status: "
                            + loginResponse.getStatusCode()
                            + "\nResponse body: "
                            + loginResponse.asPrettyString()
            );
        }

        String token = loginResponse.jsonPath().getString("accessToken");

        CustomerRequest customerRequest = new CustomerRequest(
                DataUtils.getPersonCustomerName(),
                DataUtils.getCustomerAddress(),
                DataUtils.getPersonCustomerType()
        );

        Response createCustomerResponse = customerClient.createCustomer(token, customerRequest);

        if (createCustomerResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: Customer A profile creation failed. Actual status: "
                            + createCustomerResponse.getStatusCode()
                            + "\nResponse body: "
                            + createCustomerResponse.asPrettyString()
            );
        }

        testContext.setData("customerAToken", token);
        testContext.setData("customerACustomerId", createCustomerResponse.jsonPath().get("customerId"));
    }

    private void createCustomerUserB() {
        String username = DataUtils.generateUniqueEmail();
        String password = DataUtils.getValidPassword();

        Response registerResponse = authClient.register(new RegisterRequest(username, password));

        if (registerResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: Customer B registration failed. Actual status: "
                            + registerResponse.getStatusCode()
                            + "\nResponse body: "
                            + registerResponse.asPrettyString()
            );
        }

        Response loginResponse = authClient.login(new LoginRequest(username, password));

        if (loginResponse.getStatusCode() != 200) {
            throw new AssertionError(
                    "Precondition failed: Customer B login failed. Actual status: "
                            + loginResponse.getStatusCode()
                            + "\nResponse body: "
                            + loginResponse.asPrettyString()
            );
        }

        String token = loginResponse.jsonPath().getString("accessToken");

        CustomerRequest customerRequest = new CustomerRequest(
                DataUtils.getCompanyCustomerName(),
                DataUtils.getCustomerAddress(),
                DataUtils.getCompanyCustomerType()
        );

        Response createCustomerResponse = customerClient.createCustomer(token, customerRequest);

        if (createCustomerResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: Customer B profile creation failed. Actual status: "
                            + createCustomerResponse.getStatusCode()
                            + "\nResponse body: "
                            + createCustomerResponse.asPrettyString()
            );
        }

        testContext.setData("customerBToken", token);
        testContext.setData("customerBCustomerId", createCustomerResponse.jsonPath().get("customerId"));
    }

    @When("Customer A sends a create RRSP account request for Customer B")
    public void customerASendsACreateRrspAccountRequestForCustomerB() {
        AccountRequest rrspAccountRequest = new AccountRequest(
                DataUtils.getRrspAccountType(),
                DataUtils.getRrspOpeningBalance(),
                null
        );

        testContext.setResponse(
                accountClient.createAccount(
                        testContext.getString("customerAToken"),
                        testContext.getData("customerBCustomerId"),
                        rrspAccountRequest
                )
        );
    }
}