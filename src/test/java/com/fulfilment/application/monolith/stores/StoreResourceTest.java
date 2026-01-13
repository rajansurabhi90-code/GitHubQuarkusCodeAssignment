package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class StoreResourceTest {

    @Test
    public void testGetAllStores() {
        given()
                .when().get("/stores")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testCreateStore() {
        String storeJson = """
            {
              "name": "Test Store",
              "quantityProductsInStock": 10
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(storeJson)
                .when().post("/stores")
                .then()
                .statusCode(201)
                .body("name", is("Test Store"))
                .body("quantityProductsInStock", is(10));
    }

    @Test
    public void testGetSingleStore() {
        String storeJson = """
            {
              "name": "Single Store",
              "quantityProductsInStock": 5
            }
            """;


        Integer storeIdInt = given()
                .contentType(ContentType.JSON)
                .body(storeJson)
                .when().post("/stores")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
        Long storeId = storeIdInt.longValue();

        given()
                .when().get("/stores/" + storeId)
                .then()
                .statusCode(200)
                .body("name", is("Single Store"))
                .body("quantityProductsInStock", is(5));
    }

    @Test
    public void testUpdateStore() {
        String storeJson = """
            {
              "name": "Update Store",
              "quantityProductsInStock": 15
            }
            """;

        Integer storeIdInt =
                given()
                        .contentType(ContentType.JSON)
                        .body(storeJson)
                        .when().post("/stores")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");
        Long storeId = storeIdInt.longValue();

        String updatedJson = """
            {
              "name": "Updated Store",
              "quantityProductsInStock": 20
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(updatedJson)
                .when().put("/stores/" + storeId)
                .then()
                .statusCode(200)
                .body("name", is("Updated Store"))
                .body("quantityProductsInStock", is(20));
    }

    @Test
    public void testDeleteStore() {
        String storeJson = """
            {
              "name": "Delete Store",
              "quantityProductsInStock": 7
            }
            """;

        Integer storeIdInt =
                given()
                        .contentType(ContentType.JSON)
                        .body(storeJson)
                        .when().post("/stores")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");
        Long storeId = storeIdInt.longValue();
        given()
                .when().delete("/stores/" + storeId)
                .then()
                .statusCode(204);
        given()
                .when().get("/stores/" + storeId)
                .then()
                .statusCode(404);
    }
}