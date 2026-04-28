package com.todo.app;

// REMOVED: @SpringBootTest — replaced with @QuarkusTest
// Reason: Quarkus uses its own test framework. @SpringBootTest is not compatible.

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class AppApplicationTests {

	@Test
	void contextLoads() {
		// Verify the application starts and the home page is accessible
		given()
			.when().get("/home")
			.then().statusCode(200);
	}
}
