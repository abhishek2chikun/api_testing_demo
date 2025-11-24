// Updated test file with additional tests
package com.example.api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class OrdersApiTest {

    @BeforeAll
    public static void setup() {
        String fallback = "http://localhost:8002";
        String baseFromSysProp = System.getProperty("baseUrl");
        String baseFromEnv = System.getenv("BASE_URL");
        String resolved = Optional.ofNullable(baseFromSysProp)
                                  .orElse(Optional.ofNullable(baseFromEnv)
                                                 .orElse(fallback));
        RestAssured.baseURI = resolved;
    }

    @Test
    public void testPlaceOrderSuccess() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .queryParam("use_cache", true)
            .contentType(ContentType.JSON)
            .body("{\"tradingsymbol\": \"INFY\", \"quantity\": 10, \"order_type\": \"LIMIT\", \"price\": 1500}")
        .when()
            .post("/orders")
        .then()
            .statusCode(200)
            .body("order_id", notNullValue())
            .body("status", equalTo("success"));
    }

    @Test
    public void testListOrdersSuccess() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .queryParam("use_cache", true)
        .when()
            .get("/orders")
        .then()
            .statusCode(200)
            .body("orders", notNullValue())
            .body("orders", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testGetOrderSuccess() {
        String orderId = "order123"; // Replace with a valid order ID for actual testing

        given()
            .pathParam("order_id", orderId)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .queryParam("use_cache", true)
        .when()
            .get("/orders/{order_id}")
        .then()
            .statusCode(200)
            .body("order_id", equalTo(orderId))
            .body("status", notNullValue());
    }

    @Test
    public void testPlaceOrderValidationError() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .contentType(ContentType.JSON)
            .body("{\"tradingsymbol\": \"\", \"quantity\": 0, \"order_type\": \"LIMIT\"}")
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("detail", notNullValue());
    }

    @Test
    public void testGetOrderNotFound() {
        String orderId = "nonExistentOrder";

        given()
            .pathParam("order_id", orderId)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .queryParam("use_cache", true)
        .when()
            .get("/orders/{order_id}")
        .then()
            .statusCode(404)
            .body("status", equalTo(404))
            .body("message", notNullValue());
    }

    @Test
    public void testHealthCheck() {
        when()
            .get("/health")
        .then()
            .statusCode(200)
            .body("status", equalTo("healthy"));
    }

    // New test for authentication enforcement
    @Test
    public void testAuthenticationRequired() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .queryParam("use_cache", true)
        .when()
            .get("/orders")
        .then()
            .statusCode(401)
            .body("message", equalTo("Authentication required"));
    }

    // New test for error response standards
    @Test
    public void testErrorResponseStandards() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .contentType(ContentType.JSON)
            .body("{\"tradingsymbol\": \"INVALID\", \"quantity\": -1, \"order_type\": \"LIMIT\"}")
        .when()
            .post("/orders")
        .then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("message", notNullValue())
            .body("detail", notNullValue());
    }

    // New tests for additional validation errors
    @Test
    public void testPlaceOrderWithInvalidOrderType() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .contentType(ContentType.JSON)
            .body("{\"tradingsymbol\": \"INFY\", \"quantity\": 10, \"order_type\": \"INVALID_TYPE\"}")
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("detail", containsString("Invalid order type"));
    }

    @Test
    public void testPlaceOrderWithMissingPriceForLimitOrder() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .contentType(ContentType.JSON)
            .body("{\"tradingsymbol\": \"INFY\", \"quantity\": 10, \"order_type\": \"LIMIT\"}")
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("detail", containsString("Price must be provided for LIMIT orders"));
    }

    @Test
    public void testPlaceOrderWithMissingTriggerPriceForSLOrder() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .contentType(ContentType.JSON)
            .body("{\"tradingsymbol\": \"INFY\", \"quantity\": 10, \"order_type\": \"SL\"}")
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("detail", containsString("Trigger price must be provided for SL orders"));
    }
}
