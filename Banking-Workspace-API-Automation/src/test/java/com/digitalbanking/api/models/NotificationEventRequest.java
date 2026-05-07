package com.digitalbanking.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationEventRequest {

    private String eventId;
    private String eventType;
    private Object accountId;
    private Object customerId;
    private String businessTimestamp;
    private Map<String, Object> payload;

    public NotificationEventRequest() {
    }

    public NotificationEventRequest(
            String eventId,
            String eventType,
            Object accountId,
            Object customerId,
            String businessTimestamp,
            Map<String, Object> payload
    ) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.accountId = accountId;
        this.customerId = customerId;
        this.businessTimestamp = businessTimestamp;
        this.payload = payload;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public Object getAccountId() {
        return accountId;
    }

    public Object getCustomerId() {
        return customerId;
    }

    public String getBusinessTimestamp() {
        return businessTimestamp;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setAccountId(Object accountId) {
        this.accountId = accountId;
    }

    public void setCustomerId(Object customerId) {
        this.customerId = customerId;
    }

    public void setBusinessTimestamp(String businessTimestamp) {
        this.businessTimestamp = businessTimestamp;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}