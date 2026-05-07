package com.digitalbanking.api.stepdefs;

import com.digitalbanking.api.clients.NotificationClient;
import com.digitalbanking.api.context.TestContext;
import com.digitalbanking.api.models.NotificationEventRequest;
import com.digitalbanking.api.utils.DataUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.Map;

public class NotificationSteps {

    private final TestContext testContext;
    private final NotificationClient notificationClient;

    private NotificationEventRequest notificationEventRequest;

    public NotificationSteps(TestContext testContext) {
        this.testContext = testContext;
        this.notificationClient = new NotificationClient();
    }

    @Given("I have a valid mandatory notification event payload")
    public void iHaveAValidMandatoryNotificationEventPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("reason", "Standing order failed due to insufficient funds");

        notificationEventRequest = new NotificationEventRequest(
                DataUtils.generateEventId(),
                DataUtils.getMandatoryNotificationEventType(),
                testContext.getData("accountId"),
                testContext.getData("customerId"),
                DataUtils.getBusinessTimestampNow(),
                payload
        );
    }

    @Given("I have a notification event payload with unknown event type")
    public void iHaveANotificationEventPayloadWithUnknownEventType() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("reason", "Unknown event type validation");

        notificationEventRequest = new NotificationEventRequest(
                DataUtils.generateEventId(),
                DataUtils.getUnknownNotificationEventType(),
                testContext.getData("accountId"),
                testContext.getData("customerId"),
                DataUtils.getBusinessTimestampNow(),
                payload
        );
    }

    @Given("I have a notification event payload with missing required fields")
    public void iHaveANotificationEventPayloadWithMissingRequiredFields() {
        notificationEventRequest = new NotificationEventRequest(
                null,
                null,
                testContext.getData("accountId"),
                testContext.getData("customerId"),
                null,
                null
        );
    }

    @When("I send the notification evaluate request without API key")
    public void iSendTheNotificationEvaluateRequestWithoutApiKey() {
        testContext.setResponse(
                notificationClient.evaluateWithoutApiKey(notificationEventRequest)
        );
    }

    @When("I send the notification evaluate request with invalid API key")
    public void iSendTheNotificationEvaluateRequestWithInvalidApiKey() {
        testContext.setResponse(
                notificationClient.evaluateWithInvalidApiKey(notificationEventRequest)
        );
    }

    @When("I send the notification evaluate request with customer JWT")
    public void iSendTheNotificationEvaluateRequestWithCustomerJwt() {
        testContext.setResponse(
                notificationClient.evaluateWithCustomerJwt(
                        testContext.getAccessToken(),
                        notificationEventRequest
                )
        );
    }
}