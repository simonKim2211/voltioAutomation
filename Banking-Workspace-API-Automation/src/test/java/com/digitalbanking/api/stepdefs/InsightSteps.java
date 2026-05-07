package com.digitalbanking.api.stepdefs;

import com.digitalbanking.api.clients.InsightClient;
import com.digitalbanking.api.context.TestContext;
import io.cucumber.java.en.When;

import java.time.LocalDate;

public class InsightSteps {

    private final TestContext testContext;
    private final InsightClient insightClient;

    public InsightSteps(TestContext testContext) {
        this.testContext = testContext;
        this.insightClient = new InsightClient();
    }

    @When("I send the spending insights request for current month")
    public void iSendTheSpendingInsightsRequestForCurrentMonth() {
        LocalDate today = LocalDate.now();

        testContext.setResponse(
                insightClient.getSpendingInsights(
                        testContext.getAccessToken(),
                        testContext.getData("accountId"),
                        today.getYear(),
                        today.getMonthValue()
                )
        );
    }

    @When("I send the spending insights request for future month")
    public void iSendTheSpendingInsightsRequestForFutureMonth() {
        LocalDate futureMonth = LocalDate.now().plusMonths(1);

        testContext.setResponse(
                insightClient.getSpendingInsights(
                        testContext.getAccessToken(),
                        testContext.getData("accountId"),
                        futureMonth.getYear(),
                        futureMonth.getMonthValue()
                )
        );
    }

    @When("Customer A sends a spending insights request for Customer B account")
    public void customerASendsASpendingInsightsRequestForCustomerBAccount() {
        LocalDate today = LocalDate.now();

        testContext.setResponse(
                insightClient.getSpendingInsights(
                        testContext.getString("customerAToken"),
                        testContext.getData("customerBAccountId"),
                        today.getYear(),
                        today.getMonthValue()
                )
        );
    }
}