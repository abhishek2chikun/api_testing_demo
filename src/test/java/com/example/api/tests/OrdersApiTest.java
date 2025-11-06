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
        String orderId = "order123"; // Replace with a valid order ID for actual testing

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
            .body("{\"tradingsymbol\": \"INFY\", \"quantity\": 0, \"order_type\": \"LIMIT\"}")
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
     * Test placing an order with missing required fields.
     */
    @Test
    public void testPlaceOrderMissingFields() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .contentType(ContentType.JSON)
            .body("{\"tradingsymbol\": \"INFY\", \"order_type\": \"LIMIT\"}")
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("detail", containsString("price is required"));
    }

    /**
     * Test placing an order with invalid order_type.
     */
    @Test
    public void testPlaceOrderInvalidOrderType() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
            .contentType(ContentType.JSON)
            .body("{\"tradingsymbol\": \"INFY\", \"quantity\": 10, \"order_type\": \"INVALID\", \"price\": 1500}")
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("detail", containsString("Invalid order_type"));
    }

    /**
     * Test error response for invalid order_id format.
     */
    @Test
    public void testGetOrderInvalidIdFormat() {
        String orderId = "invalidOrderId!@#";

        given()
            .pathParam("order_id", orderId)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 123)
        .when()
            .get("/orders/{order_id}")
        .then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("message", notNullValue());
    }

    /**
     * Test error response for server error.
     */
    @Test
    public void testServerError() {
        // Simulate a server error by hitting an invalid endpoint
        when()
            .get("/orders/invalid")
        .then()
            .statusCode(500)
            .body("status", equalTo(500))
            .body("message", notNullValue());
    }
}
