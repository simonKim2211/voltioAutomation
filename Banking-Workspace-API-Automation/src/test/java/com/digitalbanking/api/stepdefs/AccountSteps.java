package com.digitalbanking.api.stepdefs;

import com.digitalbanking.api.clients.AccountClient;
import com.digitalbanking.api.context.TestContext;
import com.digitalbanking.api.models.AccountRequest;
import com.digitalbanking.api.models.AccountUpdateRequest;
import com.digitalbanking.api.utils.DataUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class AccountSteps {

    private final TestContext testContext;
    private final AccountClient accountClient;

    private AccountRequest accountRequest;
    private AccountUpdateRequest accountUpdateRequest;

    public AccountSteps(TestContext testContext) {
        this.testContext = testContext;
        this.accountClient = new AccountClient();
    }

    @Given("I have valid CHECKING account details")
    public void iHaveValidCheckingAccountDetails() {
        accountRequest = new AccountRequest(
                DataUtils.getCheckingAccountType(),
                DataUtils.getOpeningBalance(),
                null
        );

        testContext.setData("expectedAccountType", DataUtils.getCheckingAccountType());
        testContext.setData("expectedBalance", DataUtils.getOpeningBalance().toPlainString());
    }

    @Given("I have valid SAVINGS account details")
    public void iHaveValidSavingsAccountDetails() {
        accountRequest = new AccountRequest(
                DataUtils.getSavingsAccountType(),
                DataUtils.getOpeningBalance(),
                DataUtils.getSavingsInterestRate()
        );

        testContext.setData("expectedAccountType", DataUtils.getSavingsAccountType());
        testContext.setData("expectedBalance", DataUtils.getOpeningBalance().toPlainString());
        testContext.setData("expectedInterestRate", DataUtils.getSavingsInterestRate().toPlainString());
    }

    @Given("I have CHECKING account details with interest rate")
    public void iHaveCheckingAccountDetailsWithInterestRate() {
        accountRequest = new AccountRequest(
                DataUtils.getCheckingAccountType(),
                DataUtils.getOpeningBalance(),
                DataUtils.getSavingsInterestRate()
        );
    }

    @Given("I have SAVINGS account details without interest rate")
    public void iHaveSavingsAccountDetailsWithoutInterestRate() {
        accountRequest = new AccountRequest(
                DataUtils.getSavingsAccountType(),
                DataUtils.getOpeningBalance(),
                null
        );
    }

    @Given("I have account details with negative balance")
    public void iHaveAccountDetailsWithNegativeBalance() {
        accountRequest = new AccountRequest(
                DataUtils.getCheckingAccountType(),
                DataUtils.getNegativeBalance(),
                null
        );
    }

    @Given("I have already created a valid CHECKING account")
    public void iHaveAlreadyCreatedAValidCheckingAccount() {
        accountRequest = new AccountRequest(
                DataUtils.getCheckingAccountType(),
                DataUtils.getOpeningBalance(),
                null
        );

        testContext.setData("expectedAccountType", DataUtils.getCheckingAccountType());
        testContext.setData("expectedBalance", DataUtils.getOpeningBalance().toPlainString());

        Response createAccountResponse = accountClient.createAccount(
                testContext.getAccessToken(),
                testContext.getData("customerId"),
                accountRequest
        );

        if (createAccountResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: CHECKING account creation did not return 201. Actual status: "
                            + createAccountResponse.getStatusCode()
                            + "\nResponse body: "
                            + createAccountResponse.asPrettyString()
            );
        }

        Object accountId = createAccountResponse.jsonPath().get("accountId");

        if (accountId == null) {
            throw new AssertionError("Precondition failed: accountId was missing from create account response.");
        }

        testContext.setData("accountId", accountId);
        testContext.setResponse(createAccountResponse);
    }

    @Given("I have already created a valid SAVINGS account")
    public void iHaveAlreadyCreatedAValidSavingsAccount() {
        accountRequest = new AccountRequest(
                DataUtils.getSavingsAccountType(),
                DataUtils.getOpeningBalance(),
                DataUtils.getSavingsInterestRate()
        );

        testContext.setData("expectedAccountType", DataUtils.getSavingsAccountType());
        testContext.setData("expectedBalance", DataUtils.getOpeningBalance().toPlainString());
        testContext.setData("expectedInterestRate", DataUtils.getSavingsInterestRate().toPlainString());

        Response createAccountResponse = accountClient.createAccount(
                testContext.getAccessToken(),
                testContext.getData("customerId"),
                accountRequest
        );

        if (createAccountResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: SAVINGS account creation did not return 201. Actual status: "
                            + createAccountResponse.getStatusCode()
                            + "\nResponse body: "
                            + createAccountResponse.asPrettyString()
            );
        }

        Object accountId = createAccountResponse.jsonPath().get("accountId");

        if (accountId == null) {
            throw new AssertionError("Precondition failed: accountId was missing from create account response.");
        }

        testContext.setData("accountId", accountId);
        testContext.setResponse(createAccountResponse);
    }

    @Given("I have valid savings account update details")
    public void iHaveValidSavingsAccountUpdateDetails() {
        accountUpdateRequest = new AccountUpdateRequest(DataUtils.getUpdatedSavingsInterestRate());
        testContext.setData("expectedUpdatedInterestRate", DataUtils.getUpdatedSavingsInterestRate().toPlainString());
    }

    @When("I send the create account request")
    public void iSendTheCreateAccountRequest() {
        testContext.setResponse(
                accountClient.createAccount(
                        testContext.getAccessToken(),
                        testContext.getData("customerId"),
                        accountRequest
                )
        );
    }

    @When("I send the get account request")
    public void iSendTheGetAccountRequest() {
        testContext.setResponse(
                accountClient.getAccount(
                        testContext.getAccessToken(),
                        testContext.getData("accountId")
                )
        );
    }

    @When("I send the list customer accounts request")
    public void iSendTheListCustomerAccountsRequest() {
        testContext.setResponse(
                accountClient.listCustomerAccounts(
                        testContext.getAccessToken(),
                        testContext.getData("customerId")
                )
        );
    }

    @When("I send the update account request")
    public void iSendTheUpdateAccountRequest() {
        testContext.setResponse(
                accountClient.updateAccount(
                        testContext.getAccessToken(),
                        testContext.getData("accountId"),
                        accountUpdateRequest
                )
        );
    }

    @When("I send the get account request with invalid account ID")
    public void iSendTheGetAccountRequestWithInvalidAccountId() {
        testContext.setResponse(
                accountClient.getAccount(
                        testContext.getAccessToken(),
                        "abc"
                )
        );
    }

    @Given("I have already created source and destination CHECKING accounts")
    public void iHaveAlreadyCreatedSourceAndDestinationCheckingAccounts() {
        AccountRequest sourceAccountRequest = new AccountRequest(
                DataUtils.getCheckingAccountType(),
                DataUtils.getSourceAccountOpeningBalance(),
                null
        );

        Response sourceAccountResponse = accountClient.createAccount(
                testContext.getAccessToken(),
                testContext.getData("customerId"),
                sourceAccountRequest
        );

        if (sourceAccountResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: source account creation did not return 201. Actual status: "
                            + sourceAccountResponse.getStatusCode()
                            + "\nResponse body: "
                            + sourceAccountResponse.asPrettyString()
            );
        }

        AccountRequest destinationAccountRequest = new AccountRequest(
                DataUtils.getCheckingAccountType(),
                DataUtils.getDestinationAccountOpeningBalance(),
                null
        );

        Response destinationAccountResponse = accountClient.createAccount(
                testContext.getAccessToken(),
                testContext.getData("customerId"),
                destinationAccountRequest
        );

        if (destinationAccountResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: destination account creation did not return 201. Actual status: "
                            + destinationAccountResponse.getStatusCode()
                            + "\nResponse body: "
                            + destinationAccountResponse.asPrettyString()
            );
        }

        Object sourceAccountId = sourceAccountResponse.jsonPath().get("accountId");
        Object destinationAccountId = destinationAccountResponse.jsonPath().get("accountId");

        testContext.setData("sourceAccountId", sourceAccountId);
        testContext.setData("destinationAccountId", destinationAccountId);
    }

    @Given("I have valid RRSP account details")
    public void iHaveValidRrspAccountDetails() {
        accountRequest = new AccountRequest(
                DataUtils.getRrspAccountType(),
                DataUtils.getRrspOpeningBalance(),
                null
        );

        testContext.setData("expectedAccountType", DataUtils.getRrspAccountType());
        testContext.setData("expectedBalance", DataUtils.getRrspOpeningBalance().toPlainString());
    }

    @Given("I have RRSP account details with interest rate")
    public void iHaveRrspAccountDetailsWithInterestRate() {
        accountRequest = new AccountRequest(
                DataUtils.getRrspAccountType(),
                DataUtils.getRrspOpeningBalance(),
                DataUtils.getRrspInvalidInterestRate()
        );
    }

    @Given("I have already created a valid RRSP account")
    public void iHaveAlreadyCreatedAValidRrspAccount() {
        accountRequest = new AccountRequest(
                DataUtils.getRrspAccountType(),
                DataUtils.getRrspOpeningBalance(),
                null
        );

        testContext.setData("expectedAccountType", DataUtils.getRrspAccountType());
        testContext.setData("expectedBalance", DataUtils.getRrspOpeningBalance().toPlainString());

        Response createAccountResponse = accountClient.createAccount(
                testContext.getAccessToken(),
                testContext.getData("customerId"),
                accountRequest
        );

        if (createAccountResponse.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: RRSP account creation did not return 201. Actual status: "
                            + createAccountResponse.getStatusCode()
                            + "\nResponse body: "
                            + createAccountResponse.asPrettyString()
            );
        }

        Object accountId = createAccountResponse.jsonPath().get("accountId");

        if (accountId == null) {
            throw new AssertionError("Precondition failed: accountId was missing from RRSP create account response.");
        }

        testContext.setData("accountId", accountId);
        testContext.setResponse(createAccountResponse);
    }
}