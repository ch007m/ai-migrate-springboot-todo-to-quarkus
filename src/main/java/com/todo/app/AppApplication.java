package com.todo.app;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

// REMOVED: @SpringBootApplication — Quarkus auto-discovers beans; no equivalent needed.
// Quarkus generates a main class automatically, but we keep an explicit one for clarity.
@QuarkusMain
public class AppApplication {

    public static void main(String[] args) {
        Quarkus.run(args);
    }

}
