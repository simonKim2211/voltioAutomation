package com.digitalbanking.api.clients;

import com.digitalbanking.api.config.ConfigReader;
import com.digitalbanking.api.models.AccountRequest;
import com.digitalbanking.api.models.AccountUpdateRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AccountClient {

    private final String baseUrl;

    public AccountClient() {
        this.baseUrl = ConfigReader.getProperty("base.url");
    }

    public Response createAccount(String token, Object customerId, AccountRequest requestBody) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/customers/" + customerId + "/accounts");
    }

    public Response getAccount(String token, Object accountId) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
                .when()
                .get("/accounts/" + accountId);
    }

    public Response listCustomerAccounts(String token, Object customerId) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
                .when()
                .get("/customers/" + customerId + "/accounts");
    }

    public Response updateAccount(String token, Object accountId, AccountUpdateRequest requestBody) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/accounts/" + accountId);
    }
}