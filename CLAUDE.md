# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Run the application
./mvnw spring-boot:run

# Build (compile + test)
./mvnw clean install

# Build without tests
./mvnw clean install -DskipTests

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=SomeControllerTest

# Run a single test method
./mvnw test -Dtest=SomeControllerTest#methodName
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

## Stack

- **Java 21**, Spring Boot 4.0.4
- **Spring Web MVC** — REST API layer
- **Spring Data JPA** + Hibernate — data access
- **PostgreSQL** — database (runtime driver; connection must be configured in `application.properties`)

## Architecture — Hexagonal (Ports & Adapters)

Each feature lives in its own package under `com.tocarol.api.<feature>` and is split into three layers:

```
com.tocarol.api/
└── <feature>/
    ├── domain/
    │   ├── model/          # Pure Java entities and value objects (no framework annotations)
    │   ├── port/
    │   │   ├── in/         # Input ports: use case interfaces (e.g. CreateProductUseCase)
    │   │   └── out/        # Output ports: repository/gateway interfaces (e.g. ProductRepository)
    │   └── service/        # Domain services (optional, stateless business logic)
    ├── application/
    │   └── service/        # Use case implementations (@Service), orchestrate domain + output ports
    └── adapter/
        ├── in/
        │   └── web/        # REST controllers (@RestController), DTOs, mappers
        └── out/
            └── persistence/ # JPA entities, Spring Data repositories, mappers to/from domain model
```

**Key rules:**
- `domain/` has zero Spring or JPA annotations — plain Java only.
- `application/` depends only on `domain/` (input + output ports).
- `adapter/` depends on `application/` and `domain/`; never the reverse.
- Persistence JPA entities (`adapter/out/persistence/`) are separate from domain model classes (`domain/model/`). A mapper converts between them.
- Input ports are interfaces; application services implement them.
- Output ports are interfaces; persistence adapters implement them.

Configuration lives in `src/main/resources/application.properties`. Database connection (`spring.datasource.*`) must be configured before the app can start.

## Testing

- **Domain / application layer** — plain JUnit 5, no Spring context needed.
- **Web adapter** — `@WebMvcTest` + mock the input port with `@MockBean`.
- **Persistence adapter** — `@DataJpaTest` against an in-memory or test database.

Test source root: `src/test/java/com/tocarol/api/`.
