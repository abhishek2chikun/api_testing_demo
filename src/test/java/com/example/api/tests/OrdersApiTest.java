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
    public void testPlaceOrderValidationError() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .contentType(ContentType.JSON)
            .body("{\"tradingsymbol\": \"INFY\", \"quantity\": 0, \"order_type\": \"LIMIT\"}")
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("detail", notNullValue());
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
            .body("orders", notNullValue());
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
            .body("order_id", equalTo(orderId));
    }

    @Test
    public void testGetOrderNotFound() {
        String orderId = "nonExistentOrder";
        given()
            .pathParam("order_id", orderId)
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

    // New test for error response
    @Test
    public void testInternalServerError() {
        // Simulate a server error by calling an invalid endpoint
        when()
            .get("/orders/invalidEndpoint")
        .then()
            .statusCode(500)
            .body("status", equalTo(500))
            .body("message", notNullValue())
            .body("error_id", notNullValue());
    }

    // New test for authentication enforcement
    @Test
    public void testAuthenticationRequired() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .contentType(ContentType.JSON)
        .when()
            .post("/orders")
        .then()
            .statusCode(401)
            .body("status", equalTo(401))
            .body("message", notNullValue());
    }
}
