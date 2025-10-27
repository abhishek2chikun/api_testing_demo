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

    /**
     * Test placing an order successfully.
     */
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

    /**
     * Test listing orders successfully.
     */
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

    /**
     * Test retrieving a specific order successfully.
     */
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

    /**
     * Test placing an order with validation errors.
     */
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

    /**
     * Test retrieving a non-existent order.
     */
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

    /**
     * Test the health check endpoint.
     */
    @Test
    public void testHealthCheck() {
        when()
            .get("/health")
        .then()
            .statusCode(200)
            .body("status", equalTo("healthy"));
    }

    /**
     * Test authentication enforcement.
     */
    @Test
    public void testAuthenticationEnforcement() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
        .when()
            .get("/orders")
        .then()
            .statusCode(401)
            .body("status", equalTo(401))
            .body("message", notNullValue());
    }

    /**
     * Test error response structure.
     */
    @Test
    public void testErrorResponseStructure() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .contentType(ContentType.JSON)
            .body("{\"tradingsymbol\": \"\", \"quantity\": 0, \"order_type\": \"LIMIT\"}")
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("status", equalTo(422))
            .body("message", notNullValue())
            .body("detail", notNullValue());
    }
}
