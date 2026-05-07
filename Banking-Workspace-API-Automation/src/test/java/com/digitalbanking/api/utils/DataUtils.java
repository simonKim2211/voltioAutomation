package com.digitalbanking.api.utils;

import java.time.Instant;

public class DataUtils {

    private DataUtils() {
    }

    public static String generateUniqueEmail() {
        return "apiuser_" + Instant.now().toEpochMilli() + "@testmail.com";
    }

    public static String getValidPassword() {
        return "Secure@123";
    }

    public static String getWeakPassword() {
        return "weak";
    }

    public static String getInvalidEmail() {
        return "invalid-email-format";
    }

    public static String getWrongPassword() {
        return "WrongPassword@123";
    }

    public static String getPersonCustomerName() {
        return "Jane Doe";
    }

    public static String getCompanyCustomerName() {
        return "Maple Tech Inc";
    }

    public static String getCustomerAddress() {
        return "123 Main Street, Toronto, ON";
    }

    public static String getUpdatedCustomerName() {
        return "Jane Smith";
    }

    public static String getUpdatedCustomerAddress() {
        return "456 Queen Street, Toronto, ON";
    }

    public static String getInvalidCustomerType() {
        return "INVALID_TYPE";
    }

    public static String getPersonCustomerType() {
        return "PERSON";
    }

    public static String getCompanyCustomerType() {
        return "COMPANY";
    }

    public static String getImmutableEmail() {
        return "immutable@testmail.com";
    }

    public static String getImmutableAccountNumber() {
        return "1234567890";
    }

    public static String getCheckingAccountType() {
        return "CHECKING";
    }

    public static String getSavingsAccountType() {
        return "SAVINGS";
    }

    public static java.math.BigDecimal getOpeningBalance() {
        return new java.math.BigDecimal("100.00");
    }

    public static java.math.BigDecimal getZeroBalance() {
        return new java.math.BigDecimal("0.00");
    }

    public static java.math.BigDecimal getNegativeBalance() {
        return new java.math.BigDecimal("-10.00");
    }

    public static java.math.BigDecimal getSavingsInterestRate() {
        return new java.math.BigDecimal("1.2500");
    }

    public static java.math.BigDecimal getUpdatedSavingsInterestRate() {
        return new java.math.BigDecimal("2.5000");
    }

    public static java.math.BigDecimal getDepositAmount() {
        return new java.math.BigDecimal("25.00");
    }

    public static java.math.BigDecimal getWithdrawalAmount() {
        return new java.math.BigDecimal("20.00");
    }

    public static java.math.BigDecimal getTransferAmount() {
        return new java.math.BigDecimal("75.00");
    }

    public static java.math.BigDecimal getInsufficientFundsAmount() {
        return new java.math.BigDecimal("150.00");
    }

    public static java.math.BigDecimal getZeroAmount() {
        return new java.math.BigDecimal("0.00");
    }

    public static java.math.BigDecimal getNegativeAmount() {
        return new java.math.BigDecimal("-5.00");
    }

    public static java.math.BigDecimal getSourceAccountOpeningBalance() {
        return new java.math.BigDecimal("200.00");
    }

    public static java.math.BigDecimal getDestinationAccountOpeningBalance() {
        return new java.math.BigDecimal("50.00");
    }

    public static String getDepositDescription() {
        return "Test deposit";
    }

    public static String getWithdrawalDescription() {
        return "Test withdrawal";
    }

    public static String getTransferDescription() {
        return "Test transfer";
    }

    public static String generateIdempotencyKey() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String getValidPayeeAccount() {
        return "GB82WEST12345698765432";
    }

    public static String getInvalidPayeeAccount() {
        return "INVALID123";
    }

    public static String getPayeeName() {
        return "Toronto Hydro";
    }

    public static java.math.BigDecimal getStandingOrderAmount() {
        return new java.math.BigDecimal("50.00");
    }

    public static java.math.BigDecimal getStandingOrderAmountAboveDailyLimit() {
        return new java.math.BigDecimal("3001.00");
    }

    public static String getStandingOrderFrequency() {
        return "MONTHLY";
    }

    public static String getStandingOrderReference() {
        return "RENT2026";
    }

    public static String getStandingOrderStartDateMoreThan24Hours() {
        return java.time.OffsetDateTime
                .now(java.time.ZoneOffset.UTC)
                .plusDays(2)
                .withNano(0)
                .toString();
    }

    public static String getStandingOrderStartDateLessThan24Hours() {
        return java.time.OffsetDateTime
                .now(java.time.ZoneOffset.UTC)
                .plusHours(2)
                .withNano(0)
                .toString();
    }

    public static String getStandingOrderEndDate() {
        return java.time.OffsetDateTime
                .now(java.time.ZoneOffset.UTC)
                .plusMonths(3)
                .withNano(0)
                .toString();
    }

    public static String generateEventId() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String getMandatoryNotificationEventType() {
        return "StandingOrderFailure";
    }

    public static String getOptionalNotificationEventType() {
        return "StandingOrderCreation";
    }

    public static String getUnknownNotificationEventType() {
        return "UnknownEventType";
    }

    public static String getBusinessTimestampNow() {
        return java.time.OffsetDateTime
                .now(java.time.ZoneOffset.UTC)
                .withNano(0)
                .toString();
    }

    public static String getRrspAccountType() {
        return "RRSP";
    }

    public static java.math.BigDecimal getRrspOpeningBalance() {
        return new java.math.BigDecimal("10000.00");
    }

    public static java.math.BigDecimal getRrspInvalidInterestRate() {
        return new java.math.BigDecimal("4.0000");
    }
}