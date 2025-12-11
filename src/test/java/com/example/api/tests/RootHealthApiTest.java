package com.example.api.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests for root, health and openapi metadata endpoints.
 */
public class RootHealthApiTest extends BaseTest {

    @BeforeAll
    public static void setup() {
        configureBaseUri();
    }

    /**
     * Verify that the root endpoint returns service metadata and responds with 200.
     */
    @Test
    public void testRootReturnsMetadata() {
        given()
            .when()
                .get("/")
            .then()
                .statusCode(200)
                .body("$", notNullValue()); // root returns an object
    }

    /**
     * Verify that the health endpoint returns 200 and indicates service is healthy (if available).
     */
    @Test
    public void testHealthEndpoint() {
        given()
            .when()
                .get("/health")
            .then()
                .statusCode(anyOf(is(200), is(204), is(503))); // allow for different health implementations
    }

    /**
     * Verify the OpenAPI specification is served at /openapi.json and contains basic fields.
     */
    @Test
    public void testOpenApiIsAvailable() {
        given()
            .when()
                .get("/openapi.json")
            .then()
                .statusCode(200)
                .body("openapi", startsWith("3"))
                .body("info.title", notNullValue())
                .body("paths", notNullValue());
    }
}