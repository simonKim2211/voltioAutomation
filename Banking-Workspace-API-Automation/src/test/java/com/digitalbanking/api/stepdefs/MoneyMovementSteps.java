package com.digitalbanking.api.stepdefs;

import com.digitalbanking.api.clients.AccountClient;
import com.digitalbanking.api.clients.MoneyMovementClient;
import com.digitalbanking.api.context.TestContext;
import com.digitalbanking.api.models.MoneyMovementRequest;
import com.digitalbanking.api.models.TransferRequest;
import com.digitalbanking.api.utils.DataUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class MoneyMovementSteps {

    private final TestContext testContext;
    private final MoneyMovementClient moneyMovementClient;
    private final AccountClient accountClient;

    private MoneyMovementRequest moneyMovementRequest;
    private TransferRequest transferRequest;

    public MoneyMovementSteps(TestContext testContext) {
        this.testContext = testContext;
        this.moneyMovementClient = new MoneyMovementClient();
        this.accountClient = new AccountClient();
    }

    @Given("I have valid deposit details")
    public void iHaveValidDepositDetails() {
        moneyMovementRequest = new MoneyMovementRequest(
                DataUtils.getDepositAmount(),
                DataUtils.getDepositDescription()
        );
    }

    @Given("I have deposit details with zero amount")
    public void iHaveDepositDetailsWithZeroAmount() {
        moneyMovementRequest = new MoneyMovementRequest(
                DataUtils.getZeroAmount(),
                DataUtils.getDepositDescription()
        );
    }

    @Given("I have deposit details with negative amount")
    public void iHaveDepositDetailsWithNegativeAmount() {
        moneyMovementRequest = new MoneyMovementRequest(
                DataUtils.getNegativeAmount(),
                DataUtils.getDepositDescription()
        );
    }

    @Given("I have valid withdrawal details")
    public void iHaveValidWithdrawalDetails() {
        moneyMovementRequest = new MoneyMovementRequest(
                DataUtils.getWithdrawalAmount(),
                DataUtils.getWithdrawalDescription()
        );
    }

    @Given("I have withdrawal details greater than current balance")
    public void iHaveWithdrawalDetailsGreaterThanCurrentBalance() {
        moneyMovementRequest = new MoneyMovementRequest(
                DataUtils.getInsufficientFundsAmount(),
                DataUtils.getWithdrawalDescription()
        );
    }

    @Given("I have valid transfer details")
    public void iHaveValidTransferDetails() {
        transferRequest = new TransferRequest(
                testContext.getData("sourceAccountId"),
                testContext.getData("destinationAccountId"),
                DataUtils.getTransferAmount(),
                DataUtils.getTransferDescription()
        );
    }

    @Given("I have transfer details greater than source balance")
    public void iHaveTransferDetailsGreaterThanSourceBalance() {
        transferRequest = new TransferRequest(
                testContext.getData("sourceAccountId"),
                testContext.getData("destinationAccountId"),
                new BigDecimal("300.00"),
                DataUtils.getTransferDescription()
        );
    }

    @Given("I have transfer details using same source and destination account")
    public void iHaveTransferDetailsUsingSameSourceAndDestinationAccount() {
        Object accountId = testContext.getData("accountId");

        transferRequest = new TransferRequest(
                accountId,
                accountId,
                DataUtils.getTransferAmount(),
                DataUtils.getTransferDescription()
        );
    }

    @When("I send the deposit request")
    public void iSendTheDepositRequest() {
        testContext.setResponse(
                moneyMovementClient.deposit(
                        testContext.getAccessToken(),
                        testContext.getData("accountId"),
                        moneyMovementRequest
                )
        );
    }

    @When("I send the deposit request with the same idempotency key twice")
    public void iSendTheDepositRequestWithTheSameIdempotencyKeyTwice() {
        String idempotencyKey = DataUtils.generateIdempotencyKey();

        Response firstResponse = moneyMovementClient.depositWithIdempotencyKey(
                testContext.getAccessToken(),
                testContext.getData("accountId"),
                moneyMovementRequest,
                idempotencyKey
        );

        if (firstResponse.getStatusCode() != 200) {
            throw new AssertionError(
                    "First deposit request failed. Actual status: "
                            + firstResponse.getStatusCode()
                            + "\nResponse body: "
                            + firstResponse.asPrettyString()
            );
        }

        Response secondResponse = moneyMovementClient.depositWithIdempotencyKey(
                testContext.getAccessToken(),
                testContext.getData("accountId"),
                moneyMovementRequest,
                idempotencyKey
        );

        testContext.setResponse(secondResponse);
    }

    @When("I send the withdrawal request")
    public void iSendTheWithdrawalRequest() {
        testContext.setResponse(
                moneyMovementClient.withdraw(
                        testContext.getAccessToken(),
                        testContext.getData("accountId"),
                        moneyMovementRequest
                )
        );
    }

    @When("I send the withdrawal request with the same idempotency key twice")
    public void iSendTheWithdrawalRequestWithTheSameIdempotencyKeyTwice() {
        String idempotencyKey = DataUtils.generateIdempotencyKey();

        Response firstResponse = moneyMovementClient.withdrawWithIdempotencyKey(
                testContext.getAccessToken(),
                testContext.getData("accountId"),
                moneyMovementRequest,
                idempotencyKey
        );

        if (firstResponse.getStatusCode() != 200) {
            throw new AssertionError(
                    "First withdrawal request failed. Actual status: "
                            + firstResponse.getStatusCode()
                            + "\nResponse body: "
                            + firstResponse.asPrettyString()
            );
        }

        Response secondResponse = moneyMovementClient.withdrawWithIdempotencyKey(
                testContext.getAccessToken(),
                testContext.getData("accountId"),
                moneyMovementRequest,
                idempotencyKey
        );

        testContext.setResponse(secondResponse);
    }

    @When("I send the transfer request")
    public void iSendTheTransferRequest() {
        testContext.setResponse(
                moneyMovementClient.transfer(
                        testContext.getAccessToken(),
                        transferRequest
                )
        );
    }

    @Then("the final account balance should be {string}")
    public void theFinalAccountBalanceShouldBe(String expectedBalance) {
        Response accountResponse = accountClient.getAccount(
                testContext.getAccessToken(),
                testContext.getData("accountId")
        );

        if (accountResponse.getStatusCode() != 200) {
            throw new AssertionError(
                    "Could not retrieve account for final balance validation. Actual status: "
                            + accountResponse.getStatusCode()
                            + "\nResponse body: "
                            + accountResponse.asPrettyString()
            );
        }

        BigDecimal actualBalance = new BigDecimal(
                accountResponse.jsonPath().getString("balance")
        );

        BigDecimal expectedBalanceValue = new BigDecimal(expectedBalance);

        assertEquals(
                "Final balance mismatch. Account response: " + accountResponse.asPrettyString(),
                0,
                expectedBalanceValue.compareTo(actualBalance)
        );
    }
}