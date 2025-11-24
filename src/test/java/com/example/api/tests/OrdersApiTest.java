// Updated OrdersApiTest.java with additional tests

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
        .when()
            .get("/orders")
        .then()
            .statusCode(200)
            .body("orders", notNullValue())
            .body("orders", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testGetOrderSuccess() {
        String orderId = "order123"; // Assume this order ID exists for testing

        given()
            .pathParam("order_id", orderId)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
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
            .body("{\"tradingsymbol\": \"\", \"quantity\": -1, \"order_type\": \"LIMIT\"}")
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("detail", notNullValue());
    }

    @Test
    public void testGetOrderNotFound() {
        String nonExistentOrderId = "nonExistentOrder";

        given()
            .pathParam("order_id", nonExistentOrderId)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
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

    // New test for service metadata
    @Test
    public void testServiceMetadata() {
        when()
            .get("/")
        .then()
            .statusCode(200)
            .body("available_brokers", notNullValue());
    }

    // New test for error responses
    @Test
    public void testErrorResponse() {
        given()
            .queryParam("broker", "invalidBroker")
            .queryParam("user_id", 123)
        .when()
            .get("/orders")
        .then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("message", notNullValue());
    }

    // New test for authentication/authorization enforcement
    @Test
    public void testAuthenticationRequired() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 999) // Assuming this user does not have access
        .when()
            .get("/orders")
        .then()
            .statusCode(403)
            .body("status", equalTo(403))
            .body("message", notNullValue());
    }
}
