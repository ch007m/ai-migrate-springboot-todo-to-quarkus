package com.todo.app;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

// REMOVED: @SpringBootTest — replaced with @QuarkusTest
@QuarkusTest
class AppApplicationTests {

    @Test
    void contextLoads() {
        // Quarkus context loads successfully if the test starts without exceptions
    }

}
