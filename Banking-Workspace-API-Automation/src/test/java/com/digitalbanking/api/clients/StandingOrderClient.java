package com.digitalbanking.api.clients;

import com.digitalbanking.api.config.ConfigReader;
import com.digitalbanking.api.models.StandingOrderRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class StandingOrderClient {

    private final String baseUrl;

    public StandingOrderClient() {
        this.baseUrl = ConfigReader.getProperty("base.url");
    }

    public Response createStandingOrder(String token, Object accountId, StandingOrderRequest requestBody) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/accounts/" + accountId + "/standing-orders");
    }

    public Response listStandingOrders(String token, Object accountId) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
                .when()
                .get("/accounts/" + accountId + "/standing-orders");
    }

    public Response cancelStandingOrder(String token, Object standingOrderId) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
                .when()
                .delete("/standing-orders/" + standingOrderId);
    }
}