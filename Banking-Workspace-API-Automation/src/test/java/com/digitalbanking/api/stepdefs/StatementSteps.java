package com.digitalbanking.api.stepdefs;

import com.digitalbanking.api.clients.StatementClient;
import com.digitalbanking.api.context.TestContext;
import io.cucumber.java.en.When;

import java.time.YearMonth;

public class StatementSteps {

    private final TestContext testContext;
    private final StatementClient statementClient;

    public StatementSteps(TestContext testContext) {
        this.testContext = testContext;
        this.statementClient = new StatementClient();
    }

    @When("I send the monthly statement request with period {string}")
    public void iSendTheMonthlyStatementRequestWithPeriod(String period) {
        testContext.setResponse(
                statementClient.getMonthlyStatement(
                        testContext.getAccessToken(),
                        testContext.getData("accountId"),
                        period
                )
        );
    }

    @When("I send the monthly statement request for current period")
    public void iSendTheMonthlyStatementRequestForCurrentPeriod() {
        String currentPeriod = YearMonth.now().toString();

        testContext.setResponse(
                statementClient.getMonthlyStatement(
                        testContext.getAccessToken(),
                        testContext.getData("accountId"),
                        currentPeriod
                )
        );
    }

    @When("Customer A sends a monthly statement request for Customer B account")
    public void customerASendsAMonthlyStatementRequestForCustomerBAccount() {
        String previousPeriod = YearMonth.now().minusMonths(1).toString();

        testContext.setResponse(
                statementClient.getMonthlyStatement(
                        testContext.getString("customerAToken"),
                        testContext.getData("customerBAccountId"),
                        previousPeriod
                )
        );
    }

    @When("I send the monthly statement request for non-existing account")
    public void iSendTheMonthlyStatementRequestForNonExistingAccount() {
        String previousPeriod = YearMonth.now().minusMonths(1).toString();

        testContext.setResponse(
                statementClient.getMonthlyStatement(
                        testContext.getAccessToken(),
                        999999999,
                        previousPeriod
                )
        );
    }
}