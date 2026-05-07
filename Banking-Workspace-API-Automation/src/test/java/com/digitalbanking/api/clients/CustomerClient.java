package com.digitalbanking.api.clients;

import com.digitalbanking.api.config.ConfigReader;
import com.digitalbanking.api.models.CustomerRequest;
import com.digitalbanking.api.models.CustomerUpdateRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CustomerClient {

    private final String baseUrl;

    public CustomerClient() {
        this.baseUrl = ConfigReader.getProperty("base.url");
    }

    public Response createCustomer(String token, CustomerRequest requestBody) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/customers");
    }

    public Response createCustomerWithoutToken(CustomerRequest requestBody) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/customers");
    }

    public Response getCustomer(String token, Object customerId) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
                .when()
                .get("/api/customers/" + customerId);
    }

    public Response updateCustomer(String token, Object customerId, CustomerUpdateRequest requestBody) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch("/api/customers/" + customerId);
    }

    public Response listAllCustomers(String token) {
        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
                .when()
                .get("/api/customers");
    }
}