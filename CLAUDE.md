# Project: To Carol API

Java 21 + Spring Boot 4.0.4 REST API — Hexagonal Architecture (Ports & Adapters).
Handles user CRUD, permissions, and Supabase Storage integration (upload/delete images).

## Commands

```bash
# Run
./mvnw spring-boot:run

# Build
./mvnw clean install

# Build without tests
./mvnw clean install -DskipTests

# Test all
./mvnw test

# Test single class
./mvnw test -Dtest=ClassName

# Test single method
./mvnw test -Dtest=ClassName#methodName
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

## Environment Variables

All secrets must be defined in `application-local.properties` (gitignored).

```properties
supabase.url=${SUPABASE_URL}
supabase.key=${SUPABASE_API_KEY}
supabase.storage.bucket=${SUPABASE_STORAGE_NAME}
```

Never hardcode secrets. Never commit `application-local.properties`.

## Architecture

Hexagonal (Ports & Adapters). Dependency rule is strict:

```
adapter  →  application  →  domain
```

Nothing in `domain/` knows about Spring, HTTP, or storage providers.

```
com.tocarol.api/
├── domain/
│   ├── model/               # Pure Java — User, Permission, Image (no annotations)
│   ├── port/
│   │   ├── in/              # Use case interfaces — CreateUserUseCase, UploadImageUseCase
│   │   └── out/             # Gateway interfaces — UserGateway, ImageStorageGateway
│   └── exception/           # Domain exceptions — UserNotFoundException, UnauthorizedException
│
├── application/
│   └── usecase/             # Implements input ports (@Service)
│                            # user/ — CreateUser, UpdateUser, DeleteUser, GetUser
│                            # image/ — UploadImage, DeleteImage, ListImages
│                            # permission/ — AssignPermission, RevokePermission
│
└── adapter/
    ├── in/
    │   └── http/            # @RestController, DTOs, mappers
    │                        # user/ — UserController
    │                        # image/ — ImageController
    │                        # health/ — HealthCheckController
    └── out/
        ├── persistence/     # UserGateway implementation (future — database)
        └── storage/         # ImageStorageGateway implementation (Supabase)
```

## Features

**User CRUD** (`adapter/in/http/user/`)
- `POST   /users`
- `GET    /users/:id`
- `PUT    /users/:id`
- `DELETE /users/:id`

**Permissions** (`adapter/in/http/permission/`)
- `POST   /users/:id/permissions`
- `DELETE /users/:id/permissions/:permissionId`

**Images — Supabase Storage** (`adapter/in/http/image/`)
- `GET    /images`         — list images from bucket
- `POST   /images`         — upload image to Supabase
- `DELETE /images/:name`   — remove image from Supabase

**Health**
- `GET    /health`

## Layer Rules

**`domain/`**
- Zero Spring annotations — plain Java only.
- No knowledge of HTTP, Supabase, or any external provider.
- Entities: `User`, `Permission`, `Image`.
- Input ports: one interface per use case (e.g. `CreateUserUseCase`).
- Output ports: gateway interfaces (e.g. `UserGateway`, `ImageStorageGateway`).

**`application/`**
- Annotated with `@Service`.
- Implements input ports.
- Depends only on `domain/` — never on `adapter/`.
- No HTTP or I/O logic here.

**`adapter/in/http/`**
- Annotated with `@RestController`.
- Injects input port interfaces — never use case classes directly.
- DTOs live here — never in `domain/`.
- No business logic — delegate everything to use cases.
- Return `ResponseEntity<T>` always.

**`adapter/out/storage/`**
- Implements `ImageStorageGateway`.
- All Supabase HTTP calls live here.
- Maps Supabase responses to domain model.

## Testing Strategy

| Layer | Tool | Spring Context |
|---|---|---|
| Domain / Application | JUnit 5 + Mockito | No |
| HTTP Adapter | `@WebMvcTest` + `@MockBean` | Partial |
| Storage Adapter | JUnit 5 + HTTP mock | No |

- Domain and application tests are plain Java — fast, no Spring context.
- Controllers are tested with `@WebMvcTest`, mocking input ports.
- Never test implementation — test behavior through ports.

## Code Style

- One class per file.
- Constructor injection only — no `@Autowired` on fields.
- Prefer `final` fields.
- No business logic in controllers.
- No framework annotations in `domain/`.
- Domain exceptions are caught at the adapter layer — never leak as HTTP 500.
- DTOs are immutable — use records when possible.

## Gotchas

- `application-local.properties` is gitignored — new devs must create it manually.
- Supabase client lives only in `adapter/out/storage/` — never leak it to domain or application.
- When adding a new feature: define the port first, then the use case, then the adapter.