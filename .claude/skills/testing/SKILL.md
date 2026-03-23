# Skill: Testing

Activated when asked to write, review, or improve tests.

## Testing Strategy

| Layer | Tool | Spring Context | Speed |
|---|---|---|---|
| Domain / Application | JUnit 5 + Mockito | No | Fast |
| HTTP Adapter | `@WebMvcTest` + `@MockBean` | Partial | Medium |
| Storage Adapter | JUnit 5 + HTTP mock | No | Fast |

## Rules

- Test behavior through ports — never test implementation details
- Domain and application tests must have **zero** Spring context
- One test class per production class
- Test method names: `should[ExpectedBehavior]When[Condition]`
- No logic in tests — one assertion focus per test
- Always test the unhappy path (not found, invalid input, unauthorized)

## Patterns by Layer

### Domain / Application (plain JUnit 5)

```java
class CreateUserUseCaseTest {

    private final UserGateway userGateway = Mockito.mock(UserGateway.class);
    private final CreateUserUseCase useCase = new CreateUserService(userGateway);

    @Test
    void shouldCreateUserWhenInputIsValid() {
        // arrange
        var input = new CreateUserInput("carol@tocarol.com", "Carol");
        given(userGateway.save(any())).willReturn(new User("1", "Carol", "carol@tocarol.com"));

        // act
        var result = useCase.execute(input);

        // assert
        assertThat(result.name()).isEqualTo("Carol");
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        given(userGateway.existsByEmail("carol@tocarol.com")).willReturn(true);
        assertThatThrownBy(() -> useCase.execute(input))
            .isInstanceOf(EmailAlreadyExistsException.class);
    }
}
```

### HTTP Adapter (@WebMvcTest)

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean CreateUserUseCase createUserUseCase;

    @Test
    void shouldReturn201WhenUserIsCreated() throws Exception {
        given(createUserUseCase.execute(any())).willReturn(userResponse);

        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content("""{ "name": "Carol", "email": "carol@tocarol.com" }"""))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Carol"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        given(createUserUseCase.execute(any())).willThrow(new UserNotFoundException("1"));

        mockMvc.perform(get("/users/1"))
            .andExpect(status().isNotFound());
    }
}
```

## What to always test

**Use cases**
- Happy path
- Domain exception when entity not found
- Domain exception when business rule is violated

**Controllers**
- Correct HTTP status for success
- Correct HTTP status for each domain exception
- Response body shape

**Storage adapter**
- Correct URL and headers sent to Supabase
- Correct mapping of Supabase response to domain model

## What NOT to test

- Getters and setters
- Framework behavior (Spring routing, Jackson serialization)
- `main()` method