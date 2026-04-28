package com.todo.app;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

// REMOVED: @SpringBootTest — replaced with @QuarkusTest
// The original test only verified Spring context loading.
// Replaced with a Quarkus startup + HTTP endpoint smoke test.
@QuarkusTest
class AppApplicationTests {

    @Test
    void contextLoads() {
        // Verify the app starts and the home redirect works
        given()
            .redirects().follow(false)
            .when().get("/")
            .then().statusCode(303);
    }

}
