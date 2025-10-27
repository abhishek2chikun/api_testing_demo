// Updated test file with additional tests and corrections

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
        String fallback = "http://localhost:8002";  // replace with first server URL if available
        String baseFromSysProp = System.getProperty("baseUrl");
        String baseFromEnv = System.getenv("BASE_URL");
        String resolved = Optional.ofNullable(baseFromSysProp)
                                  .orElse(Optional.ofNullable(baseFromEnv)
                                                 .orElse(fallback));
        RestAssured.baseURI = resolved;
    }

    @Test
    public void testPlaceOrderSuccess() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\", \"quantity\": 10, \"order_type\": \"LIMIT\", \"price\": 1500 }";

        given()
            .contentType(ContentType.JSON)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .queryParam("use_cache", true)
            .body(orderPayload)
        .when()
            .post("/orders")
        .then()
            .statusCode(200)
            .body("order_id", notNullValue())
            .body("status", equalTo("SUCCESS"));
    }

    @Test
    public void testPlaceOrderMissingFields() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\" }"; // Missing quantity and order_type

        given()
            .contentType(ContentType.JSON)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .queryParam("use_cache", true)
            .body(orderPayload)
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("status", equalTo("error"))
            .body("message", containsString("Validation Error"));
    }

    @Test
    public void testListOrdersSuccess() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .queryParam("use_cache", true)
        .when()
            .get("/orders")
        .then()
            .statusCode(200)
            .body("orders", notNullValue())
            .body("orders", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testListOrdersInvalidUserId() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", -1) // Invalid user_id
            .queryParam("use_cache", true)
        .when()
            .get("/orders")
        .then()
            .statusCode(404)
            .body("status", equalTo("error"))
            .body("message", containsString("User not found"));
    }

    @Test
    public void testGetOrderSuccess() {
        String orderId = "validOrderId"; // Replace with a valid order ID

        given()
            .pathParam("order_id", orderId)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .queryParam("use_cache", true)
        .when()
            .get("/orders/{order_id}")
        .then()
            .statusCode(200)
            .body("order_id", equalTo(orderId))
            .body("status", equalTo("SUCCESS"));
    }

    @Test
    public void testGetOrderNotFound() {
        String orderId = "nonExistentOrderId"; // Non-existent order ID

        given()
            .pathParam("order_id", orderId)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .queryParam("use_cache", true)
        .when()
            .get("/orders/{order_id}")
        .then()
            .statusCode(404)
            .body("status", equalTo("error"))
            .body("message", containsString("Order not found"));
    }

    @Test
    public void testHealthCheck() {
        when()
            .get("/health")
        .then()
            .statusCode(200)
            .body("status", equalTo("healthy"));
    }

    @Test
    public void testRootEndpoint() {
        when()
            .get("/")
        .then()
            .statusCode(200)
            .body("available_brokers", notNullValue());
    }

    // New test for error response structure
    @Test
    public void testErrorResponseStructure() {
        String orderPayload = "{ \"tradingsymbol\": \"INVALID\", \"quantity\": -10, \"order_type\": \"LIMIT\", \"price\": 1500 }";

        given()
            .contentType(ContentType.JSON)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .body(orderPayload)
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("status", equalTo("error"))
            .body("message", containsString("Validation Error"))
            .body("detail", notNullValue());
    }

    // New test for authentication enforcement
    @Test
    public void testAuthenticationRequired() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
        .when()
            .get("/orders")
        .then()
            .statusCode(401)
            .body("status", equalTo("error"))
            .body("message", containsString("Missing credentials"));
    }
}
