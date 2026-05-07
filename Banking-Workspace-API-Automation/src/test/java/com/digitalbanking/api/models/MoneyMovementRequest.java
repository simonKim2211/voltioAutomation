package com.digitalbanking.api.models;

import java.math.BigDecimal;

public class MoneyMovementRequest {

    private BigDecimal amount;
    private String description;

    public MoneyMovementRequest() {
    }

    public MoneyMovementRequest(BigDecimal amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}