Add a new endpoint to an existing feature following hexagonal architecture based on: $ARGUMENTS

## Steps

### 1. Define the input port (`domain/port/in/`)
- Create a new use case interface (e.g. `SearchProductUseCase`) with a single method
- If the operation needs input data beyond a simple ID, add a nested `Command` record

### 2. Implement in the application service (`application/service/`)
- Add the new interface to the `implements` list of the existing `@Service` class
- Inject any additional output ports via constructor if needed
- Keep HTTP and JPA concepts out — work only with domain model types

### 3. Extend the persistence adapter if needed (`adapter/out/persistence/`)
- Add a method to the `JpaRepository` interface (e.g. `findByName(String name)`)
- Add the corresponding method to the output port interface (`domain/port/out/`)
- Implement it in the `PersistenceAdapter`

### 4. Add the controller method (`adapter/in/web/`)
- Inject only the new input port interface (add to constructor)
- Choose the correct HTTP verb and status code:
  - 200 for queries
  - 201 + Location header for resource creation
  - 204 for deletions / no-body responses
  - 404 via `ResponseStatusException(HttpStatus.NOT_FOUND)` for missing resources
- Add request/response records if the shape is new; reuse existing ones if appropriate
- Map via the existing `<Feature>Mapper` or extend it

### 5. Add a test
- In `<Feature>ControllerTest` (`@WebMvcTest`): add a test method, mock the new use case with `@MockBean`
- In `<Feature>ServiceTest` (plain JUnit 5): add a test for the application service method

## Conventions
- Never inject the application service class directly into the controller — only the input port interface
- Domain layer must remain free of Spring/JPA imports
- Constructor injection only
