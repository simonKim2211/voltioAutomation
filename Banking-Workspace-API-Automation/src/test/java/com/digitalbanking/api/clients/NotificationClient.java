package com.digitalbanking.api.clients;

import com.digitalbanking.api.config.ConfigReader;
import com.digitalbanking.api.models.NotificationEventRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class NotificationClient {

    private final String baseUrl;

    public NotificationClient() {
        this.baseUrl = ConfigReader.getProperty("base.url");
    }

    public Response evaluateWithoutApiKey(NotificationEventRequest requestBody) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/notifications/evaluate");
    }

    public Response evaluateWithInvalidApiKey(NotificationEventRequest requestBody) {
        return given()
                .baseUri(baseUrl)
                .header("X-API-Key", ConfigReader.getProperty("notification.api.key"))
                .header("X-Service-ID", ConfigReader.getProperty("notification.service.id"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/notifications/evaluate");
    }

    public Response evaluateWithCustomerJwt(String token, NotificationEventRequest requestBody) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/notifications/evaluate");
    }
}