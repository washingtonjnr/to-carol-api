Create a complete new feature following hexagonal architecture for this Spring Boot API based on: $ARGUMENTS

## Package structure to create

All files go under `src/main/java/com/tocarol/api/<featureName>/`:

```
<featureName>/
├── domain/
│   ├── model/           <FeatureName>.java          ← pure Java, no annotations
│   ├── port/
│   │   ├── in/          Create<FeatureName>UseCase.java
│   │   │                Update<FeatureName>UseCase.java
│   │   │                Delete<FeatureName>UseCase.java
│   │   │                Find<FeatureName>UseCase.java
│   │   │                
│   │   └── out/         <FeatureName>Repository.java
│   │
│   └── service/         (optional domain service if needed)
│
├── application/
│   └── service/         <FeatureName>Service.java
└── adapter/
    ├── in/web/          <FeatureName>Controller.java
    │                    <FeatureName>Request.java
    │                    <FeatureName>Response.java
    │                    <FeatureName>Mapper.java
    │                    
    └── out/persistence/ <FeatureName>JpaEntity.java
                         <FeatureName>JpaRepository.java
                         <FeatureName>PersistenceAdapter.java
                         <FeatureName>PersistenceMapper.java
```

## Step-by-step instructions

### 1. Domain model (`domain/model/`)
- Plain Java class with fields inferred from the description
- No `@Entity`, no Spring annotations
- Private fields, constructor, getters (immutable where possible)

### 2. Output port (`domain/port/out/`)
- Interface `<FeatureName>Repository` with methods: `save`, `findById`, `findAll`, `deleteById`

### 3. Input ports (`domain/port/in/`)
- One interface per use case, each with a single method
- Use a nested `Command` record inside the interface for input data

### 4. Application service (`application/service/`)
- `@Service` class implementing all input port interfaces
- Constructor-inject the output port (`<FeatureName>Repository`)
- Throw `EntityNotFoundException` (or similar) when not found
- No JPA, no HTTP concepts here

### 5. Persistence adapter (`adapter/out/persistence/`)
- `<FeatureName>JpaEntity`: `@Entity` + `@Table` JPA class
- `<FeatureName>JpaRepository`: `JpaRepository<JpaEntity, Long>`
- `<FeatureName>PersistenceAdapter`: `@Component` implementing the output port, uses JpaRepository + mapper
- `<FeatureName>PersistenceMapper`: maps between domain model ↔ JPA entity

### 6. Web adapter (`adapter/in/web/`)
- `<FeatureName>Controller`: `@RestController`, `@RequestMapping("/api/<feature-plural>")`
  - `GET /` → 200 list
  - `GET /{id}` → 200 or 404
  - `POST /` → 201
  - `PUT /{id}` → 200 or 404
  - `DELETE /{id}` → 204
- Inject only input port interfaces, never the application service class directly
- `<FeatureName>Request` / `<FeatureName>Response`: records for HTTP I/O
- `<FeatureName>Mapper`: maps request → command, domain model → response

### 7. Tests (`src/test/java/com/tocarol/api/<featureName>/`)
- `<FeatureName>ServiceTest`: plain JUnit 5 + Mockito, no Spring context
- `<FeatureName>ControllerTest`: `@WebMvcTest`, mock input ports with `@MockBean`
- `<FeatureName>PersistenceAdapterTest`: `@DataJpaTest`

## Conventions
- Constructor injection only — no `@Autowired` on fields
- Domain layer has zero Spring/JPA imports
- `Long` as primary key type
- snake_case table and column names via `@Table`/`@Column`
- `FetchType.LAZY` on all relationships
