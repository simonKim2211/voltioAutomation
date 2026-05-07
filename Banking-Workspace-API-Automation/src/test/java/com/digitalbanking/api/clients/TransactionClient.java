package com.digitalbanking.api.clients;

import com.digitalbanking.api.config.ConfigReader;
import io.restassured.response.Response;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;

public class TransactionClient {

    private final String baseUrl;

    public TransactionClient() {
        this.baseUrl = ConfigReader.getProperty("base.url");
    }

    public Response getTransactionHistory(String token, Object accountId) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .accept("application/json")
                .when()
                .get("/accounts/" + accountId + "/transactions");
    }

    public Response getTransactionHistoryWithDateRange(String token, Object accountId, String startDate, String endDate) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .accept("application/json")
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .when()
                .get("/accounts/" + accountId + "/transactions");
    }

    public Response exportTransactionHistoryPdf(String token, Object accountId) {
        String startDate = LocalDate.now(ZoneOffset.UTC)
                .minusDays(7)
                .toString();

        String endDate = LocalDate.now(ZoneOffset.UTC)
                .plusDays(1)
                .toString();

        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .accept("application/pdf")
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .when()
                .get("/accounts/" + accountId + "/transactions/export");
    }
}