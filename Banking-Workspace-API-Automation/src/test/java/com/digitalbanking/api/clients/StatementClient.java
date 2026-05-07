package com.digitalbanking.api.clients;

import com.digitalbanking.api.config.ConfigReader;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class StatementClient {

    private final String baseUrl;

    public StatementClient() {
        this.baseUrl = ConfigReader.getProperty("base.url");
    }

    public Response getMonthlyStatement(String token, Object accountId, String period) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .accept("application/pdf")
                .when()
                .get("/accounts/" + accountId + "/statements/" + period);
    }
}