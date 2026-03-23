Create a new domain model and its persistence representation following hexagonal architecture based on: $ARGUMENTS

## What to build

### 1. Domain model (`domain/model/`)
- Plain Java class — no `@Entity`, no Spring annotations
- Fields inferred from the description with appropriate Java types
- Private fields, all-args constructor, getters
- Use value objects (inner records or separate classes) for concepts with their own rules (e.g. `Email`, `Money`)

### 2. Output port (`domain/port/out/`)
- Interface `<ModelName>Repository` with:
  - `<ModelName> save(<ModelName> model)`
  - `Optional<<ModelName>> findById(Long id)`
  - `List<<ModelName>> findAll()`
  - `void deleteById(Long id)`
- Add extra query methods only if clearly needed by the feature

### 3. JPA entity (`adapter/out/persistence/`)
- `<ModelName>JpaEntity` with `@Entity`, `@Table(name = "<table_name>")`
- `@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;`
- Mirror the domain fields with `@Column` annotations:
  - `nullable = false` for required fields
  - `unique = true` for unique fields
  - `name = "snake_case_name"` when the field name differs
- Relationships: `@ManyToOne`, `@OneToMany`, `@ManyToMany` with `FetchType.LAZY`
- No-arg constructor (JPA requirement)

### 4. Persistence mapper (`adapter/out/persistence/`)
- `<ModelName>PersistenceMapper` — maps `JpaEntity` ↔ domain model
- Static methods or a `@Component` class depending on complexity

### 5. Persistence adapter (`adapter/out/persistence/`)
- `<ModelName>JpaRepository extends JpaRepository<<ModelName>JpaEntity, Long>`
- `<ModelName>PersistenceAdapter @Component` implementing `<ModelName>Repository` (the output port)

## Naming conventions
- Domain class: PascalCase (`OrderItem`)
- JPA entity: `OrderItemJpaEntity`
- Table: snake_case (`order_item`)
- Column: snake_case
- Package: `com.tocarol.api.<featureName>`
