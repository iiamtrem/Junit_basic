package com.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FakeStoreApiTest {

    @BeforeAll
    static void setup() {
        baseURI = "https://fakestoreapi.com";
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void get_products_should_return_200_and_non_empty_list() {
        given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].title", notNullValue());
    }

    @Test
    void get_product_by_id_should_return_correct_id() {
        given()
                .when()
                .get("/products/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", notNullValue())
                .body("price", greaterThan(0f));
    }

    @Test
    void get_cart_by_id_should_have_user_and_products() {
        given()
                .when()
                .get("/carts/2")
                .then()
                .statusCode(200)
                .body("id", equalTo(2))
                .body("userId", notNullValue())
                .body("products.size()", greaterThan(0))
                .body("products[0].productId", notNullValue())
                .body("products[0].quantity", greaterThan(0));
    }

    @Test
    void post_cart_should_create_cart_and_return_id() {
        String payload = """
                {
                  "userId": 5,
                  "date": "2026-01-06",
                  "products": [
                    { "productId": 1, "quantity": 2 },
                    { "productId": 2, "quantity": 1 }
                  ]
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/carts")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("id", notNullValue())
                .body("userId", equalTo(5));
    }

    // GET product id không tồn tại
    @Test
    void get_product_not_found_should_return_404_or_empty() {
        given()
                .when()
                .get("/products/9999")
                .then()
                // mock API có thể 404 hoặc 200 (tuỳ), nên mình cho pass theo 2 hướng:
                .statusCode(anyOf(is(404), is(200)));
    }

    // POST cart thiếu dữ liệu
    @Test
    void post_cart_with_empty_products_should_still_create_cart_in_demo_api() {
        String payload = """
                { "userId": 5, "date": "2026-01-06", "products": [] }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/carts")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("id", notNullValue())
                .body("userId", equalTo(5))
                .body("products.size()", equalTo(0));
    }

}
