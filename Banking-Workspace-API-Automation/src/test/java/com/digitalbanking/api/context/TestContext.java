package com.digitalbanking.api.context;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class TestContext {

    private Response response;
    private String accessToken;
    private final Map<String, Object> scenarioData = new HashMap<>();

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setData(String key, Object value) {
        scenarioData.put(key, value);
    }

    public Object getData(String key) {
        return scenarioData.get(key);
    }

    public String getString(String key) {
        Object value = scenarioData.get(key);
        return value == null ? null : value.toString();
    }

    public void clear() {
        response = null;
        accessToken = null;
        scenarioData.clear();
    }
}