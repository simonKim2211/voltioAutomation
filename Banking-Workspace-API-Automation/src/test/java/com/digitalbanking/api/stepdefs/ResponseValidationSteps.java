package com.digitalbanking.api.stepdefs;

import com.digitalbanking.api.context.TestContext;
import com.digitalbanking.api.utils.DataUtils;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import com.digitalbanking.api.clients.AccountClient;

import java.util.List;

import static org.junit.Assert.*;

public class ResponseValidationSteps {

    private final TestContext testContext;

    public ResponseValidationSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        Response response = testContext.getResponse();

        assertNotNull("Response is null. API request may not have been executed.", response);

        int actualStatusCode = response.getStatusCode();

        assertEquals(
                "Unexpected status code. Response body: " + response.asPrettyString(),
                expectedStatusCode,
                actualStatusCode
        );
    }

    @Then("the register response should contain user details")
    public void theRegisterResponseShouldContainUserDetails() {
        Response response = testContext.getResponse();

        assertNotNull("userId is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().getString("userId"));

        assertNotNull("username is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().getString("username"));

        assertNotNull("roles is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().getList("roles"));

        assertNotNull("createdAt is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().getString("createdAt"));

        boolean hasIsActive = response.jsonPath().get("isActive") != null;
        boolean hasActive = response.jsonPath().get("active") != null;

        assertTrue(
                "Neither isActive nor active field was found. Response body: " + response.asPrettyString(),
                hasIsActive || hasActive
        );

        assertEquals(
                "Username mismatch. Response body: " + response.asPrettyString(),
                testContext.getString("username"),
                response.jsonPath().getString("username")
        );
    }

    @Then("the register response should not contain passwordHash")
    public void theRegisterResponseShouldNotContainPasswordHash() {
        Response response = testContext.getResponse();

        String responseBody = response.asString();

        assertFalse(
                "passwordHash should not be returned in register response.",
                responseBody.contains("passwordHash")
        );
    }

    @Then("the login response should contain token details")
    public void theLoginResponseShouldContainTokenDetails() {
        Response response = testContext.getResponse();

        String accessToken = response.jsonPath().getString("accessToken");
        String refreshToken = response.jsonPath().getString("refreshToken");
        String tokenType = response.jsonPath().getString("tokenType");

        assertNotNull("accessToken is missing", accessToken);
        assertFalse("accessToken is empty", accessToken.trim().isEmpty());

        assertNotNull("refreshToken is missing", refreshToken);
        assertFalse("refreshToken is empty", refreshToken.trim().isEmpty());

        assertEquals("Bearer", tokenType);

        assertNotNull("expiresIn is missing", response.jsonPath().get("expiresIn"));

        testContext.setAccessToken(accessToken);
    }

    @Then("the customer response should contain created customer details")
    public void theCustomerResponseShouldContainCreatedCustomerDetails() {
        Response response = testContext.getResponse();

        assertNotNull("customerId is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().get("customerId"));

        assertEquals(
                "Customer name mismatch. Response body: " + response.asPrettyString(),
                testContext.getString("expectedCustomerName"),
                response.jsonPath().getString("name")
        );

        assertEquals(
                "Customer address mismatch. Response body: " + response.asPrettyString(),
                testContext.getString("expectedCustomerAddress"),
                response.jsonPath().getString("address")
        );

        assertEquals(
                "Customer type mismatch. Response body: " + response.asPrettyString(),
                testContext.getString("expectedCustomerType"),
                response.jsonPath().getString("type")
        );

        assertNotNull("createdAt is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().getString("createdAt"));

        assertNotNull("updatedAt is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().getString("updatedAt"));

        List<?> accounts = response.jsonPath().getList("accounts");

        assertNotNull("accounts field is missing. Response body: " + response.asPrettyString(), accounts);

        testContext.setData("customerId", response.jsonPath().getLong("customerId"));
    }

    @Then("the customer response should contain updated customer details")
    public void theCustomerResponseShouldContainUpdatedCustomerDetails() {
        Response response = testContext.getResponse();

        assertNotNull("customerId is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().get("customerId"));

        assertEquals(
                "Updated customer name mismatch. Response body: " + response.asPrettyString(),
                testContext.getString("expectedUpdatedCustomerName"),
                response.jsonPath().getString("name")
        );

        assertEquals(
                "Updated customer address mismatch. Response body: " + response.asPrettyString(),
                testContext.getString("expectedUpdatedCustomerAddress"),
                response.jsonPath().getString("address")
        );

        assertNotNull("updatedAt is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().getString("updatedAt"));
    }

    @Then("the account response should contain created account details")
    public void theAccountResponseShouldContainCreatedAccountDetails() {
        Response response = testContext.getResponse();

        assertNotNull("accountId is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().get("accountId"));

        assertEquals(
                "Account type mismatch. Response body: " + response.asPrettyString(),
                testContext.getString("expectedAccountType"),
                response.jsonPath().getString("accountType")
        );

        assertNotNull("status is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().getString("status"));

        assertEquals(
                "Account should be ACTIVE after creation. Response body: " + response.asPrettyString(),
                "ACTIVE",
                response.jsonPath().getString("status")
        );

        String actualBalance = response.jsonPath().getString("balance");
        assertNotNull("balance is missing. Response body: " + response.asPrettyString(), actualBalance);

        java.math.BigDecimal expectedBalance = new java.math.BigDecimal(testContext.getString("expectedBalance"));
        java.math.BigDecimal actualBalanceValue = new java.math.BigDecimal(actualBalance);

        assertEquals(
                "Balance mismatch. Response body: " + response.asPrettyString(),
                0,
                expectedBalance.compareTo(actualBalanceValue)
        );

        if ("SAVINGS".equals(testContext.getString("expectedAccountType"))) {
            String actualInterestRate = response.jsonPath().getString("interestRate");

            assertNotNull("interestRate is missing for SAVINGS account. Response body: " + response.asPrettyString(),
                    actualInterestRate);

            java.math.BigDecimal expectedInterestRate =
                    new java.math.BigDecimal(testContext.getString("expectedInterestRate"));
            java.math.BigDecimal actualInterestRateValue =
                    new java.math.BigDecimal(actualInterestRate);

            assertEquals(
                    "Interest rate mismatch. Response body: " + response.asPrettyString(),
                    0,
                    expectedInterestRate.compareTo(actualInterestRateValue)
            );
        }

        testContext.setData("accountId", response.jsonPath().get("accountId"));
    }

    @Then("the customer accounts response should contain the created account")
    public void theCustomerAccountsResponseShouldContainTheCreatedAccount() {
        Response response = testContext.getResponse();

        Object expectedAccountId = testContext.getData("accountId");

        assertNotNull("accountId was not stored in TestContext.", expectedAccountId);

        String responseBody = response.asString();

        assertTrue(
                "Created accountId was not found in accounts list. Response body: " + response.asPrettyString(),
                responseBody.contains(expectedAccountId.toString())
        );
    }

    @Then("the account response should contain updated interest rate")
    public void theAccountResponseShouldContainUpdatedInterestRate() {
        Response response = testContext.getResponse();

        String actualInterestRate = response.jsonPath().getString("interestRate");

        assertNotNull("interestRate is missing. Response body: " + response.asPrettyString(), actualInterestRate);

        java.math.BigDecimal expectedInterestRate =
                new java.math.BigDecimal(testContext.getString("expectedUpdatedInterestRate"));
        java.math.BigDecimal actualInterestRateValue =
                new java.math.BigDecimal(actualInterestRate);

        assertEquals(
                "Updated interest rate mismatch. Response body: " + response.asPrettyString(),
                0,
                expectedInterestRate.compareTo(actualInterestRateValue)
        );
    }

    @Then("the money movement response should show balance {string}")
    public void theMoneyMovementResponseShouldShowBalance(String expectedBalance) {
        Response response = testContext.getResponse();

        String actualBalanceString = response.jsonPath().getString("account.balance");

        assertNotNull(
                "account.balance is missing from money movement response. Response body: " + response.asPrettyString(),
                actualBalanceString
        );

        java.math.BigDecimal expectedBalanceValue = new java.math.BigDecimal(expectedBalance);
        java.math.BigDecimal actualBalanceValue = new java.math.BigDecimal(actualBalanceString);

        assertEquals(
                "Balance mismatch. Response body: " + response.asPrettyString(),
                0,
                expectedBalanceValue.compareTo(actualBalanceValue)
        );

        assertNotNull(
                "transaction is missing from money movement response. Response body: " + response.asPrettyString(),
                response.jsonPath().get("transaction")
        );

        assertEquals(
                "Transaction status should be SUCCESS. Response body: " + response.asPrettyString(),
                "SUCCESS",
                response.jsonPath().getString("transaction.status")
        );
    }

    @Then("the source account balance should be {string}")
    public void theSourceAccountBalanceShouldBe(String expectedBalance) {
        AccountClient accountClient = new AccountClient();

        Response accountResponse = accountClient.getAccount(
                testContext.getAccessToken(),
                testContext.getData("sourceAccountId")
        );

        assertEquals(
                "Could not retrieve source account. Response body: " + accountResponse.asPrettyString(),
                200,
                accountResponse.getStatusCode()
        );

        java.math.BigDecimal expectedBalanceValue = new java.math.BigDecimal(expectedBalance);
        java.math.BigDecimal actualBalanceValue = new java.math.BigDecimal(
                accountResponse.jsonPath().getString("balance")
        );

        assertEquals(
                "Source account balance mismatch. Response body: " + accountResponse.asPrettyString(),
                0,
                expectedBalanceValue.compareTo(actualBalanceValue)
        );
    }

    @Then("the destination account balance should be {string}")
    public void theDestinationAccountBalanceShouldBe(String expectedBalance) {
        AccountClient accountClient = new AccountClient();

        Response accountResponse = accountClient.getAccount(
                testContext.getAccessToken(),
                testContext.getData("destinationAccountId")
        );

        assertEquals(
                "Could not retrieve destination account. Response body: " + accountResponse.asPrettyString(),
                200,
                accountResponse.getStatusCode()
        );

        java.math.BigDecimal expectedBalanceValue = new java.math.BigDecimal(expectedBalance);
        java.math.BigDecimal actualBalanceValue = new java.math.BigDecimal(
                accountResponse.jsonPath().getString("balance")
        );

        assertEquals(
                "Destination account balance mismatch. Response body: " + accountResponse.asPrettyString(),
                0,
                expectedBalanceValue.compareTo(actualBalanceValue)
        );
    }
    @Then("the transaction history response should contain at least one transaction")
    public void theTransactionHistoryResponseShouldContainAtLeastOneTransaction() {
        Response response = testContext.getResponse();

        Object transactionCountObject = response.jsonPath().get("transactionCount");

        if (transactionCountObject != null) {
            int transactionCount = response.jsonPath().getInt("transactionCount");

            assertTrue(
                    "Expected at least one transaction, but transactionCount was "
                            + transactionCount
                            + ". Response body: "
                            + response.asPrettyString(),
                    transactionCount > 0
            );

            return;
        }

        java.util.List<?> transactions = response.jsonPath().getList("transactions");

        assertNotNull(
                "transactions field is missing. Response body: " + response.asPrettyString(),
                transactions
        );

        assertTrue(
                "Expected at least one transaction. Response body: " + response.asPrettyString(),
                transactions.size() > 0
        );
    }

    @Then("the transaction history response should contain transaction direction {string}")
    public void theTransactionHistoryResponseShouldContainTransactionDirection(String expectedDirection) {
        Response response = testContext.getResponse();

        String responseBody = response.asString();

        assertTrue(
                "Expected transaction direction "
                        + expectedDirection
                        + " was not found. Response body: "
                        + response.asPrettyString(),
                responseBody.contains(expectedDirection)
        );
    }

    @Then("the response content type should contain {string}")
    public void theResponseContentTypeShouldContain(String expectedContentType) {
        Response response = testContext.getResponse();

        String actualContentType = response.getContentType();

        assertNotNull(
                "Content-Type header is missing. Response body: " + response.asString(),
                actualContentType
        );

        assertTrue(
                "Expected Content-Type to contain "
                        + expectedContentType
                        + " but was "
                        + actualContentType,
                actualContentType.contains(expectedContentType)
        );
    }

    @Then("the standing order response should contain created standing order details")
    public void theStandingOrderResponseShouldContainCreatedStandingOrderDetails() {
        Response response = testContext.getResponse();

        assertNotNull(
                "standingOrderId is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().get("standingOrderId")
        );

        assertEquals(
                "Payee account mismatch. Response body: " + response.asPrettyString(),
                testContext.getString("expectedPayeeAccount"),
                response.jsonPath().getString("payeeAccount")
        );

        assertEquals(
                "Payee name mismatch. Response body: " + response.asPrettyString(),
                testContext.getString("expectedPayeeName"),
                response.jsonPath().getString("payeeName")
        );

        assertEquals(
                "Frequency mismatch. Response body: " + response.asPrettyString(),
                testContext.getString("expectedStandingOrderFrequency"),
                response.jsonPath().getString("frequency")
        );

        assertEquals(
                "Standing order should be ACTIVE after creation. Response body: " + response.asPrettyString(),
                "ACTIVE",
                response.jsonPath().getString("status")
        );

        assertNotNull(
                "nextRunDate is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().getString("nextRunDate")
        );

        testContext.setData("standingOrderId", response.jsonPath().get("standingOrderId"));
    }

    @Then("the standing orders list should contain the created standing order")
    public void theStandingOrdersListShouldContainTheCreatedStandingOrder() {
        Response response = testContext.getResponse();

        Object standingOrderId = testContext.getData("standingOrderId");

        assertNotNull("standingOrderId was not stored in TestContext.", standingOrderId);

        assertTrue(
                "Created standing order ID was not found in list response. Response body: "
                        + response.asPrettyString(),
                response.asString().contains(standingOrderId.toString())
        );
    }

    @Then("the spending insights response should contain required fields")
    public void theSpendingInsightsResponseShouldContainRequiredFields() {
        Response response = testContext.getResponse();

        assertNotNull(
                "accountId is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().get("accountId")
        );

        assertNotNull(
                "period is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().get("period")
        );

        assertNotNull(
                "totalDebitSpend is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().get("totalDebitSpend")
        );

        assertNotNull(
                "transactionCount is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().get("transactionCount")
        );

        assertNotNull(
                "categoryBreakdown is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().get("categoryBreakdown")
        );

        assertNotNull(
                "sixMonthTrend is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().get("sixMonthTrend")
        );
    }

    @Then("the spending insights response should contain category breakdown")
    public void theSpendingInsightsResponseShouldContainCategoryBreakdown() {
        Response response = testContext.getResponse();

        java.util.List<?> categoryBreakdown = response.jsonPath().getList("categoryBreakdown");

        assertNotNull(
                "categoryBreakdown is missing. Response body: " + response.asPrettyString(),
                categoryBreakdown
        );

        assertTrue(
                "Expected categoryBreakdown to contain entries. Response body: " + response.asPrettyString(),
                categoryBreakdown.size() > 0
        );
    }

    @Then("the spending insights response should contain six month trend")
    public void theSpendingInsightsResponseShouldContainSixMonthTrend() {
        Response response = testContext.getResponse();

        java.util.List<?> sixMonthTrend = response.jsonPath().getList("sixMonthTrend");

        assertNotNull(
                "sixMonthTrend is missing. Response body: " + response.asPrettyString(),
                sixMonthTrend
        );

        assertTrue(
                "Expected sixMonthTrend to contain entries. Response body: " + response.asPrettyString(),
                sixMonthTrend.size() > 0
        );
    }

    @Then("the RRSP account response should contain created RRSP account details")
    public void theRrspAccountResponseShouldContainCreatedRrspAccountDetails() {
        Response response = testContext.getResponse();

        assertNotNull(
                "accountId is missing. Response body: " + response.asPrettyString(),
                response.jsonPath().get("accountId")
        );

        assertEquals(
                "Account type mismatch. Response body: " + response.asPrettyString(),
                DataUtils.getRrspAccountType(),
                response.jsonPath().getString("accountType")
        );

        assertEquals(
                "RRSP account should be ACTIVE after creation. Response body: " + response.asPrettyString(),
                "ACTIVE",
                response.jsonPath().getString("status")
        );

        String actualBalance = response.jsonPath().getString("balance");

        assertNotNull(
                "balance is missing. Response body: " + response.asPrettyString(),
                actualBalance
        );

        java.math.BigDecimal expectedBalance = DataUtils.getRrspOpeningBalance();
        java.math.BigDecimal actualBalanceValue = new java.math.BigDecimal(actualBalance);

        assertEquals(
                "RRSP opening balance mismatch. Response body: " + response.asPrettyString(),
                0,
                expectedBalance.compareTo(actualBalanceValue)
        );

        testContext.setData("accountId", response.jsonPath().get("accountId"));
    }
}