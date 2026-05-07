package com.digitalbanking.api.models;

import java.math.BigDecimal;

public class TransferRequest {

    private Object fromAccountId;
    private Object toAccountId;
    private BigDecimal amount;
    private String description;

    public TransferRequest() {
    }

    public TransferRequest(Object fromAccountId, Object toAccountId, BigDecimal amount, String description) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.description = description;
    }

    public Object getFromAccountId() {
        return fromAccountId;
    }

    public Object getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public void setFromAccountId(Object fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public void setToAccountId(Object toAccountId) {
        this.toAccountId = toAccountId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}