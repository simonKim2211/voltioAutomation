package com.digitalbanking.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;


public class AccountRequest {

    private String accountType;
    private BigDecimal balance;
    private BigDecimal interestRate;

    public AccountRequest() {
    }

    public AccountRequest(String accountType, BigDecimal balance, BigDecimal interestRate) {
        this.accountType = accountType;
        this.balance = balance;
        this.interestRate = interestRate;
    }

    public String getAccountType() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}