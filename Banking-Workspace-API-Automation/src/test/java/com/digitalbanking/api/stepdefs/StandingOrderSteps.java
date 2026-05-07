package com.digitalbanking.api.stepdefs;

import com.digitalbanking.api.clients.StandingOrderClient;
import com.digitalbanking.api.context.TestContext;
import com.digitalbanking.api.models.StandingOrderRequest;
import com.digitalbanking.api.utils.DataUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class StandingOrderSteps {

    private final TestContext testContext;
    private final StandingOrderClient standingOrderClient;

    private StandingOrderRequest standingOrderRequest;

    public StandingOrderSteps(TestContext testContext) {
        this.testContext = testContext;
        this.standingOrderClient = new StandingOrderClient();
    }

    @Given("I have valid standing order details")
    public void iHaveValidStandingOrderDetails() {
        standingOrderRequest = buildValidStandingOrderRequest();
        saveExpectedStandingOrderData();
    }

    @Given("I have standing order details with invalid payee account")
    public void iHaveStandingOrderDetailsWithInvalidPayeeAccount() {
        standingOrderRequest = new StandingOrderRequest(
                DataUtils.getInvalidPayeeAccount(),
                DataUtils.getPayeeName(),
                DataUtils.getStandingOrderAmount(),
                DataUtils.getStandingOrderFrequency(),
                DataUtils.getStandingOrderStartDateMoreThan24Hours(),
                DataUtils.getStandingOrderEndDate(),
                DataUtils.getStandingOrderReference()
        );
    }

    @Given("I have standing order details with amount above daily transfer limit")
    public void iHaveStandingOrderDetailsWithAmountAboveDailyTransferLimit() {
        standingOrderRequest = new StandingOrderRequest(
                DataUtils.getValidPayeeAccount(),
                DataUtils.getPayeeName(),
                DataUtils.getStandingOrderAmountAboveDailyLimit(),
                DataUtils.getStandingOrderFrequency(),
                DataUtils.getStandingOrderStartDateMoreThan24Hours(),
                DataUtils.getStandingOrderEndDate(),
                DataUtils.getStandingOrderReference()
        );
    }

    @Given("I have standing order details with start date less than 24 hours")
    public void iHaveStandingOrderDetailsWithStartDateLessThan24Hours() {
        standingOrderRequest = new StandingOrderRequest(
                DataUtils.getValidPayeeAccount(),
                DataUtils.getPayeeName(),
                DataUtils.getStandingOrderAmount(),
                DataUtils.getStandingOrderFrequency(),
                DataUtils.getStandingOrderStartDateLessThan24Hours(),
                DataUtils.getStandingOrderEndDate(),
                DataUtils.getStandingOrderReference()
        );
    }

    @Given("I have already created a valid standing order")
    public void iHaveAlreadyCreatedAValidStandingOrder() {
        standingOrderRequest = buildValidStandingOrderRequest();
        saveExpectedStandingOrderData();

        Response response = standingOrderClient.createStandingOrder(
                testContext.getAccessToken(),
                testContext.getData("accountId"),
                standingOrderRequest
        );

        if (response.getStatusCode() != 201) {
            throw new AssertionError(
                    "Precondition failed: standing order creation did not return 201. Actual status: "
                            + response.getStatusCode()
                            + "\nResponse body: "
                            + response.asPrettyString()
            );
        }

        Object standingOrderId = response.jsonPath().get("standingOrderId");

        if (standingOrderId == null) {
            throw new AssertionError(
                    "Precondition failed: standingOrderId was missing. Response body: "
                            + response.asPrettyString()
            );
        }

        testContext.setData("standingOrderId", standingOrderId);
        testContext.setResponse(response);
    }

    @When("I send the create standing order request")
    public void iSendTheCreateStandingOrderRequest() {
        testContext.setResponse(
                standingOrderClient.createStandingOrder(
                        testContext.getAccessToken(),
                        testContext.getData("accountId"),
                        standingOrderRequest
                )
        );
    }

    @When("I send the list standing orders request")
    public void iSendTheListStandingOrdersRequest() {
        testContext.setResponse(
                standingOrderClient.listStandingOrders(
                        testContext.getAccessToken(),
                        testContext.getData("accountId")
                )
        );
    }

    @When("I send the cancel standing order request")
    public void iSendTheCancelStandingOrderRequest() {
        testContext.setResponse(
                standingOrderClient.cancelStandingOrder(
                        testContext.getAccessToken(),
                        testContext.getData("standingOrderId")
                )
        );
    }

    @When("I send the same standing order request again")
    public void iSendTheSameStandingOrderRequestAgain() {
        testContext.setResponse(
                standingOrderClient.createStandingOrder(
                        testContext.getAccessToken(),
                        testContext.getData("accountId"),
                        standingOrderRequest
                )
        );
    }

    private StandingOrderRequest buildValidStandingOrderRequest() {
        return new StandingOrderRequest(
                DataUtils.getValidPayeeAccount(),
                DataUtils.getPayeeName(),
                DataUtils.getStandingOrderAmount(),
                DataUtils.getStandingOrderFrequency(),
                DataUtils.getStandingOrderStartDateMoreThan24Hours(),
                DataUtils.getStandingOrderEndDate(),
                DataUtils.getStandingOrderReference()
        );
    }

    private void saveExpectedStandingOrderData() {
        testContext.setData("expectedPayeeAccount", DataUtils.getValidPayeeAccount());
        testContext.setData("expectedPayeeName", DataUtils.getPayeeName());
        testContext.setData("expectedStandingOrderAmount", DataUtils.getStandingOrderAmount().toPlainString());
        testContext.setData("expectedStandingOrderFrequency", DataUtils.getStandingOrderFrequency());
        testContext.setData("expectedStandingOrderReference", DataUtils.getStandingOrderReference());
    }
}