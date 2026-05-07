package com.digitalbanking.api.clients;

import com.digitalbanking.api.config.ConfigReader;
import com.digitalbanking.api.models.LoginRequest;
import com.digitalbanking.api.models.RegisterRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {

    private final String baseUrl;

    public AuthClient() {
        this.baseUrl = ConfigReader.getProperty("base.url");
    }

    public Response register(RegisterRequest requestBody) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/auth/register");
    }

    public Response login(LoginRequest requestBody) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/auth/login");
    }

    public Response accessProtectedEndpointWithoutToken() {
        return given()
                .baseUri(baseUrl)
                .accept(ContentType.JSON)
                .when()
                .get("/api/customers/1");
    }
}