package com.digitalbanking.api.clients;

import com.digitalbanking.api.config.ConfigReader;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class InsightClient {

    private final String baseUrl;

    public InsightClient() {
        this.baseUrl = ConfigReader.getProperty("base.url");
    }

    public Response getSpendingInsights(String token, Object accountId, int year, int month) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .accept("application/json")
                .queryParam("year", year)
                .queryParam("month", month)
                .when()
                .get("/accounts/" + accountId + "/insights");
    }
}