package com.example.api.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;

import java.util.Optional;

/**
 * Base test class that configures RestAssured base URI and provides shared utilities.
 */
public class BaseTest {
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Configure RestAssured base URI using the following precedence:
     * 1. System property "baseUrl"
     * 2. Environment variable "BASE_URL"
     * 3. Fallback "http://localhost:8002"
     */
    public static void configureBaseUri() {
        String fallback = "http://localhost:8002";
        String baseFromSysProp = System.getProperty("baseUrl");
        String baseFromEnv = System.getenv("BASE_URL");
        String resolved = Optional.ofNullable(baseFromSysProp)
                .orElse(Optional.ofNullable(baseFromEnv).orElse(fallback));
        RestAssured.baseURI = resolved;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}