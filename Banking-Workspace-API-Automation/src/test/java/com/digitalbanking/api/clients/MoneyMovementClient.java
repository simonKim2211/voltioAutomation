package com.digitalbanking.api.clients;

import com.digitalbanking.api.config.ConfigReader;
import com.digitalbanking.api.models.MoneyMovementRequest;
import com.digitalbanking.api.models.TransferRequest;
import com.digitalbanking.api.utils.DataUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class MoneyMovementClient {

    private final String baseUrl;

    public MoneyMovementClient() {
        this.baseUrl = ConfigReader.getProperty("base.url");
    }

    public Response deposit(String token, Object accountId, MoneyMovementRequest requestBody) {
        return depositWithIdempotencyKey(
                token,
                accountId,
                requestBody,
                DataUtils.generateIdempotencyKey()
        );
    }

    public Response depositWithIdempotencyKey(
            String token,
            Object accountId,
            MoneyMovementRequest requestBody,
            String idempotencyKey
    ) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .header("Idempotency-Key", idempotencyKey)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/accounts/" + accountId + "/deposit");
    }

    public Response withdraw(String token, Object accountId, MoneyMovementRequest requestBody) {
        return withdrawWithIdempotencyKey(
                token,
                accountId,
                requestBody,
                DataUtils.generateIdempotencyKey()
        );
    }

    public Response withdrawWithIdempotencyKey(
            String token,
            Object accountId,
            MoneyMovementRequest requestBody,
            String idempotencyKey
    ) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .header("Idempotency-Key", idempotencyKey)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/accounts/" + accountId + "/withdraw");
    }

    public Response transfer(String token, TransferRequest requestBody) {
        return transferWithIdempotencyKey(
                token,
                requestBody,
                DataUtils.generateIdempotencyKey()
        );
    }

    public Response transferWithIdempotencyKey(
            String token,
            TransferRequest requestBody,
            String idempotencyKey
    ) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .header("Idempotency-Key", idempotencyKey)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/accounts/transfer");
    }
}