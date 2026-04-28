package com.todo.app;

// REMOVED: @SpringBootApplication main class
// Reason: Quarkus auto-generates a main class — no explicit entry point is needed.
// The original class only contained SpringApplication.run() with no @Bean methods,
// CommandLineRunner, or other logic to migrate.

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class AppApplication {

    public static void main(String[] args) {
        Quarkus.run(args);
    }

}
