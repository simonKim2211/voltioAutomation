package com.digitalbanking.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandingOrderRequest {

    private String payeeAccount;
    private String payeeName;
    private BigDecimal amount;
    private String frequency;
    private String startDate;
    private String endDate;
    private String reference;

    public StandingOrderRequest() {
    }

    public StandingOrderRequest(
            String payeeAccount,
            String payeeName,
            BigDecimal amount,
            String frequency,
            String startDate,
            String endDate,
            String reference
    ) {
        this.payeeAccount = payeeAccount;
        this.payeeName = payeeName;
        this.amount = amount;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reference = reference;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getReference() {
        return reference;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}