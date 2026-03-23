# Skill: Code Review

Activated when asked to review code, check quality, or validate architecture.

## What to check

### Hexagonal Architecture
- [ ] `domain/` has zero Spring or external annotations
- [ ] Controllers inject input port **interfaces**, never use case classes directly
- [ ] Use cases depend only on `domain/` — never on `adapter/`
- [ ] DTOs live in `adapter/in/http/` — never in `domain/`
- [ ] Supabase/storage logic lives only in `adapter/out/storage/`
- [ ] Domain exceptions never leak as HTTP 500

### Clean Code
- [ ] One class per file
- [ ] Constructor injection only — no `@Autowired` on fields
- [ ] All fields are `final`
- [ ] No business logic in controllers
- [ ] Methods do one thing only
- [ ] No magic strings or numbers — use constants or enums
- [ ] Meaningful names — no abbreviations like `usr`, `img`, `req`

### REST
- [ ] `ResponseEntity<T>` on all controller return types
- [ ] Correct HTTP status codes (`201` for create, `204` for delete, `404` for not found)
- [ ] Error responses are consistent and never expose stack traces

### Security
- [ ] No secrets hardcoded
- [ ] No `application-local.properties` committed
- [ ] Input is validated before reaching the use case

## How to review

1. Read the port (interface) first — understand the contract
2. Check the use case implements the port correctly
3. Check the controller delegates without adding logic
4. Check the adapter out maps correctly to/from domain model
5. Flag any layer violation explicitly

## Output format

For each issue found:
- **Location**: file + line
- **Problem**: what is wrong
- **Rule violated**: which rule above
- **Fix**: concrete suggestion
