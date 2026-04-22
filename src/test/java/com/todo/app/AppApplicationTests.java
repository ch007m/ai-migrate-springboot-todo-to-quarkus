package com.todo.app;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class AppApplicationTests {

	@Test
	void homePageLoads() {
		given()
			.when().get("/home")
			.then().statusCode(200);
	}

}
