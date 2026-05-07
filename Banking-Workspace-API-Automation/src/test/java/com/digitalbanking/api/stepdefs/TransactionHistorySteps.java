package com.digitalbanking.api.stepdefs;

import com.digitalbanking.api.clients.MoneyMovementClient;
import com.digitalbanking.api.clients.TransactionClient;
import com.digitalbanking.api.context.TestContext;
import com.digitalbanking.api.models.MoneyMovementRequest;
import com.digitalbanking.api.utils.DataUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class TransactionHistorySteps {

    private final TestContext testContext;
    private final MoneyMovementClient moneyMovementClient;
    private final TransactionClient transactionClient;

    public TransactionHistorySteps(TestContext testContext) {
        this.testContext = testContext;
        this.moneyMovementClient = new MoneyMovementClient();
        this.transactionClient = new TransactionClient();
    }

    @Given("I have completed a deposit transaction")
    public void iHaveCompletedADepositTransaction() {
        MoneyMovementRequest request = new MoneyMovementRequest(
                DataUtils.getDepositAmount(),
                DataUtils.getDepositDescription()
        );

        Response response = moneyMovementClient.deposit(
                testContext.getAccessToken(),
                testContext.getData("accountId"),
                request
        );

        if (response.getStatusCode() != 200) {
            throw new AssertionError(
                    "Precondition failed: deposit did not return 200. Actual status: "
                            + response.getStatusCode()
                            + "\nResponse body: "
                            + response.asPrettyString()
            );
        }

        testContext.setData("expectedTransactionDirection", "CREDIT");
        testContext.setResponse(response);
    }

    @Given("I have completed a withdrawal transaction")
    public void iHaveCompletedAWithdrawalTransaction() {
        MoneyMovementRequest request = new MoneyMovementRequest(
                DataUtils.getWithdrawalAmount(),
                DataUtils.getWithdrawalDescription()
        );

        Response response = moneyMovementClient.withdraw(
                testContext.getAccessToken(),
                testContext.getData("accountId"),
                request
        );

        if (response.getStatusCode() != 200) {
            throw new AssertionError(
                    "Precondition failed: withdrawal did not return 200. Actual status: "
                            + response.getStatusCode()
                            + "\nResponse body: "
                            + response.asPrettyString()
            );
        }

        testContext.setData("expectedTransactionDirection", "DEBIT");
        testContext.setResponse(response);
    }

    @When("I send the transaction history request")
    public void iSendTheTransactionHistoryRequest() {
        testContext.setResponse(
                transactionClient.getTransactionHistory(
                        testContext.getAccessToken(),
                        testContext.getData("accountId")
                )
        );
    }

    @When("I send the transaction history request without date filters")
    public void iSendTheTransactionHistoryRequestWithoutDateFilters() {
        testContext.setResponse(
                transactionClient.getTransactionHistory(
                        testContext.getAccessToken(),
                        testContext.getData("accountId")
                )
        );
    }

    @When("I send the transaction history PDF export request")
    public void iSendTheTransactionHistoryPdfExportRequest() {
        testContext.setResponse(
                transactionClient.exportTransactionHistoryPdf(
                        testContext.getAccessToken(),
                        testContext.getData("accountId")
                )
        );
    }

    @When("Customer A sends a transaction history request for Customer B account")
    public void customerASendsATransactionHistoryRequestForCustomerBAccount() {
        testContext.setResponse(
                transactionClient.getTransactionHistory(
                        testContext.getString("customerAToken"),
                        testContext.getData("customerBAccountId")
                )
        );
    }
}