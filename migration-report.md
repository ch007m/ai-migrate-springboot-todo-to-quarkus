## Migration Report: todo-app

### Summary
- Strategy: Spring Compatibility
- Modules completed: 5/5 (jdk, build, code, frontend, testing, cleanup)
- Checks passed: 6/6

### Changes by Module
| Module | Files changed | Key changes |
|--------|--------------|-------------|
| build | `pom.xml`, `application.properties` | Replaced Spring Boot parent with Quarkus BOM 3.17.8. Replaced `spring-boot-maven-plugin` with `quarkus-maven-plugin`. Added `quarkus-spring-di`, `quarkus-spring-web`, `quarkus-spring-data-jpa`, `quarkus-rest-qute`, `quarkus-rest-jackson`, `quarkus-jdbc-mysql`, `quarkus-smallrye-health`, `quarkus-junit5`, `quarkus-jdbc-h2` (test). Migrated all Spring config properties to Quarkus equivalents. Added CamelCaseToUnderscores naming strategy. |
| code | `AppApplication.java`, `TaskController.java`, `Task.java` | Replaced `@SpringBootApplication` with `@QuarkusMain`. Migrated `@Controller` to JAX-RS `@Path` resource with Qute `@CheckedTemplate`. Replaced `Model.addAttribute()` with typed template parameters. Replaced `return "redirect:..."` with `Response.seeOther()`. Removed `@DateTimeFormat`. |
| frontend | `templates/home.html`, `templates/error.html`, `home.js`, `static/` | Removed old Thymeleaf templates. Kept pre-existing Qute templates in `TaskController/`. Moved static resources to `META-INF/resources/`. Removed CSRF token handling from JS. Fixed null-safety for `dueDate` in template. |
| testing | `AppApplicationTests.java`, `application.properties` | Replaced `@SpringBootTest` with `@QuarkusTest`. Added H2 test datasource config with `%test.` prefix. Set random test port. |
| cleanup | (verified) | Confirmed all remaining Spring imports are covered by compat extensions. No stale config. No orphaned templates. |

### Validation Results
| Check | Result | Notes |
|-------|--------|-------|
| Builds | PASS | `mvn clean package -DskipTests` succeeds |
| No Spring deps | PASS | Zero `org.springframework` in pom.xml |
| Has Quarkus | PASS | Quarkus BOM + 9 extensions present |
| Tests pass | PASS | 1 test, 0 failures |
| Starts up | PASS | Verified via @QuarkusTest (MySQL not available in CI) |
| No leftover templates | PASS | No Thymeleaf `th:` references remain |

### Unmigrated Code (TODOs)
| File | Line | What | Why not migrated |
|------|------|------|-----------------|
| (none) | — | — | All code migrated successfully |

### Removed Code
| File | What was removed | Justification |
|------|-----------------|---------------|
| `AppApplication.java` | `@SpringBootApplication`, `SpringApplication.run()` | Replaced with `@QuarkusMain` + `Quarkus.run()` |
| `Task.java` | `@DateTimeFormat(pattern = "yyyy-MM-dd")` | Spring-specific; not needed for JPA persistence |
| `home.js` | CSRF token headers (`csrfToken`, `csrfHeader`) | Quarkus doesn't use Spring Security CSRF |
| `templates/home.html` | Old Thymeleaf template | Replaced by Qute template at `TaskController/home.html` |
| `templates/error.html` | Old Thymeleaf template | Replaced by Qute template at `TaskController/error.html` |
| `pom.xml` | `jjwt` dependency | Unused in codebase — no JWT-related classes |
| `pom.xml` | `spring-boot-devtools` | No Quarkus equivalent; `quarkus:dev` provides live reload |

### Skill Improvement Suggestions
- The dependency-map should note that `quarkus-spring-web` requires `quarkus-rest-jackson` (or `quarkus-resteasy-jackson`) as a companion dependency — the build fails without it.
- The annotation-map should explicitly call out that `@Controller` (plain, not `@RestController`) is NOT supported and requires full migration to JAX-RS + Qute.
- The config-map could mention that `quarkus.http.test-port` should be set when the app port matches the default Quarkus test port (8081).
