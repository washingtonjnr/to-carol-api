# Command: New Feature

Creates a complete feature following Hexagonal Architecture (Ports & Adapters).

## Usage

```
/new-feature <verb> <name> [scope] (optional notes)
```

### Examples

```
/new-feature create health-check
/new-feature get pictures [all files]
/new-feature create user (validate email format and reject duplicates)
/new-feature get pictures [all files] (create a generic provider and a SupabaseProvider for DI — so in the future we can swap Supabase for S3 or similar)
```

---

## Argument Reference

| Argument | Required | Description |
|---|---|---|
| `verb` | Yes | HTTP intent: `create`, `get`, `update`, `delete`, `upload`, `list` |
| `name` | Yes | Feature name in kebab-case: `health-check`, `pictures`, `user` |
| `[scope]` | No | Clarifies what the operation targets: `[all files]`, `[by id]`, `[by user]` |
| `(notes)` | No | Free-text instructions — architecture decisions, patterns, constraints |

---

## What gets generated

When no `(notes)` are provided, generate the full hexagonal flow for the given verb + name:

### Full flow (default)

**1. Domain — `domain/model/`**
- Pure Java class representing the entity (no annotations).

**2. Input Port — `domain/port/in/`**
- Interface named `<Verb><Name>UseCase`.
- Single method `execute(input)` returning the domain model or void.

**3. Output Port — `domain/port/out/`** *(if the feature needs external data)*
- Interface named `<Name>Gateway` or `<Name>Repository`.
- Methods: `save`, `findById`, `findAll`, `delete` — only what the feature needs.

**4. Use Case — `application/usecase/<name>/`**
- Class named `<Verb><Name>Service`.
- Annotated with `@Service`.
- Implements the input port.
- Constructor injection of output port interface.

**5. HTTP Adapter — `adapter/in/http/<name>/`**
- `<Name>Controller` annotated with `@RestController`.
- Injects input port interface — never the use case class directly.
- Request/Response DTOs as Java records.
- Returns `ResponseEntity<T>`.
- Correct HTTP status: `200`, `201`, `204`, `404`.

**6. Exception** *(if applicable)*
- `<Name>NotFoundException` in `domain/exception/`.

**7. Test stubs**
- Use case test: plain JUnit 5 + Mockito, no Spring.
- Controller test: `@WebMvcTest` + `@MockBean`.

---

## Behavior with `(notes)`

When `(notes)` are provided, read them carefully and adjust the generated flow accordingly.

### Common patterns from notes

**"create a generic provider + concrete implementation"**
→ Generate an output port interface (e.g. `StorageProvider`) + a concrete adapter (e.g. `SupabaseStorageProvider`) in `adapter/out/storage/`.
→ Name the interface generically so it can be swapped (S3, GCS, etc.) without touching domain or application.

**"no persistence needed"**
→ Skip output port and gateway. Use case operates on domain logic only.

**"reuse existing gateway"**
→ Do not create a new output port. Inject the existing one in the new use case.

**"validate X"**
→ Add validation logic in the use case — never in the controller.
→ Throw a domain exception if validation fails.

**"return only public fields"**
→ Create a response DTO that excludes sensitive fields. Map from domain model in the controller.

---

## Rules (always apply)

- `domain/` has zero Spring or external annotations.
- Output ports are interfaces — concrete implementations live in `adapter/out/`.
- Controllers inject input port interfaces — never use case classes.
- DTOs are Java records — immutable by default.
- One class per file.
- Constructor injection only — no `@Autowired` on fields.
- Domain exceptions are mapped to HTTP responses in the controller — never leak as 500.

---

## Output format

For each generated file, show:
1. Full file path.
2. Complete file content.
3. One-line explanation of its role in the flow.

After all files, show a summary:

```
Feature: <name>
Endpoint: <METHOD> /<route>
Flow: Controller → <InputPort> → <UseCase> → <OutputPort> → <Adapter>
Files created: N
```