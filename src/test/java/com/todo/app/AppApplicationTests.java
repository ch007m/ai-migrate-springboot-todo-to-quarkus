package com.todo.app;

// REMOVED: @SpringBootTest — replaced with @QuarkusTest
// Reason: Quarkus uses its own test lifecycle. @SpringBootTest is not supported
// without quarkus-spring-boot-test, and @QuarkusTest is the standard equivalent.

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
class AppApplicationTests {

    @Test
    void contextLoads() {
        // Verifies that the Quarkus application context starts successfully
        // (equivalent to the Spring Boot contextLoads test)
    }

    @Test
    void homePageReturnsHtml() {
        given()
            .when().get("/home")
            .then()
            .statusCode(200)
            .body(containsString("Awesome To-Do App"));
    }

}
