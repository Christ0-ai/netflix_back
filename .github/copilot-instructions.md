# Netflix API

## Overview
A Spring Boot REST API exposing movies (`Movie`), reviews (`Review`) and users (`User`) in a "Netflix"-style catalog. Strict layered architecture (Controller interface/impl → Service → Mapper → Repository → Entity), with centralized exception handling and i18n via `messages.properties`.

## Technology Stack
- Language: Java 25
- Framework: Spring Boot 4.1 (spring-boot-starter-webmvc, data-jpa, restclient, validation)
- Database: PostgreSQL (in-memory H2 for tests)
- Migrations: Liquibase (`src/main/resources/db/changelog/`)
- DTO↔Entity mapping: MapStruct 1.6.3
- Boilerplate: Lombok
- API documentation: springdoc-openapi (Swagger UI)
- Build Tool: Maven (wrapper `./mvnw`)
- Testing Framework: JUnit 5 + Mockito (pure unit tests, no `@SpringBootTest` for services)
- Formatting: Spotless (Google Java Format)

## General Instructions
- Only propose high-confidence changes; never assume an API that doesn't exist in the codebase.
- Follow the existing layered pipeline (see "Architecture") for any new resource.
- Comment non-trivial design decisions (e.g. why a field is `@Mapping(ignore = true)`), not obvious code.

## Spring Boot Instructions
### Dependency Injection
- Constructor injection only, via `@AllArgsConstructor` (Lombok) on `@Service`/`@RestController` classes.
- Dependency fields declared `private final`.

### Configuration
- Configuration lives in `src/main/resources/application.properties` (`.properties` format, not YAML in this project).
- Only one active profile today: `test` via `src/test/resources/application-test.properties` (H2, Liquibase disabled, `ddl-auto=create-drop`). In local/prod: PostgreSQL + Liquibase (`ddl-auto=none`), schema generated exclusively by the changelogs.
- Secrets (DB credentials, PgAdmin) are externalized via `.env` + Docker Compose, never hardcoded (see README).

### Request Validation
- Bean Validation (`jakarta.validation`) on request DTOs (records), always with the `message` attribute set to a **key** from `Messages.java`, never a literal string — e.g. in `MovieRequestDto`: `@NotBlank(message = Messages.MOVIE_TITLE_NULL)`, `@Pattern(regexp = "^(https?://).+\\.(jpg|jpeg|png|webp)$", message = Messages.MOVIE_POSTER_PATH_INVALID)`.
- Each DTO field also carries a Swagger `@Schema(description = ..., example = ...)` for API documentation — keep both in sync when adding a field.

## Project Structure
Describe your package/folder organization:
- `src/main/` - Main application code
- `src/test/` - Unit tests

## Coding Conventions

### Naming Conventions
- Layer suffixes, illustrated with `Movie`: `Movie` = entity, `MovieApi` = controller interface, `MovieController` = implementation, `MovieService`/`MovieServiceImpl`, `MovieMapper`, `MovieRepository`. `Review` and `User` follow the exact same suffixes with fewer endpoints.
- DTOs: `record`s named `MovieRequestDto` / `MovieResponseDto` (same pattern for `Review`/`User`) in `service/dto`.
- Methods: `findAll`, `findById`, `findByTitle`, `addMovie` — explicit verbs aligned with Spring Data derived methods (`findByTitle`, `existsByTitleAndReleaseDate`).
- i18n message constants in `Messages.java`: `SCREAMING_SNAKE_CASE` (e.g. `MOVIE_ID_NOT_FOUND`), value = dot-case key in `messages.properties` (e.g. `movie.id.not-found`).
- Packages: lowercase, singular (`controller`, `service`, `model`, not `controllers`/`models`).

### Code style
- Formatting enforced by Spotless/Google Java Format — run `./mvnw spotless:apply` before finalizing a change.
- Entities: `@Data @NoArgsConstructor @AllArgsConstructor`, `id` as `int` (not `Long`) generated via `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
- Enums persisted as `@Enumerated(EnumType.STRING)`.
- Every controller method logs endpoint access via `@Slf4j`: `log.info("Access to endpoint GET/movies")` (format `"Access to endpoint <METHOD>/<path>"`).

## Architecture

### Layer Structure (pipeline to reproduce for any new resource)
```
MovieApi (interface, OpenAPI doc @Tag/@Operation/@ApiResponse)
  → MovieController (impl, @RestController @AllArgsConstructor @Slf4j, delegates to the service)
  → MovieService (interface)
  → MovieServiceImpl (@Service @Transactional, business logic + validation)
  → MovieMapper (MapStruct, Entity ↔ DTO)
  → MovieRepository (JpaRepository<Movie, Integer>)
  → Movie (JPA entity)
```
File locations for the example above: `controller/MovieApi.java` / `controller/impl/MovieController.java` / `service/MovieService.java` / `service/impl/MovieServiceImpl.java` / `mapper/MovieMapper.java` / `repository/MovieRepository.java` / `model/Movie.java`. `Review` and `User` implement the same pipeline with fewer endpoints (no create/update yet, just `findAll`/`findById`).

### Design Patterns
- Repository pattern via Spring Data JPA (derived methods: `findByTitle`, `existsByTitleAndReleaseDate`).
- Transactional service layer: `@Transactional` on the class, `@Transactional(readOnly = true)` on reads.
- Immutable DTOs (records) for all API responses — never expose a JPA entity directly outside the service.
- Endpoints always return `ResponseEntity<T>` (never a bare `T`).
- Resource creation (POST): returns `201 Created` with a `Location` header built via `ServletUriComponentsBuilder.fromCurrentRequest()` (see `MovieController.addMovie`).

## Common Patterns
### Creating a New Service (e.g. adding an `Actor` resource)
Entity `model/Actor.java` (`@Entity`, Lombok `@Data/@NoArgsConstructor/@AllArgsConstructor`, `id` as `int`) → DTOs `service/dto/ActorRequestDto.java`/`ActorResponseDto.java` (records) → `repository/ActorRepository.java extends JpaRepository<Actor, Integer>` → `mapper/ActorMapper.java` (`@Mapper(componentModel = "spring")`, `@Mapping(ignore = true)` on `id`) → `service/ActorService.java` + `service/impl/ActorServiceImpl.java` (`@Service @Transactional`, throw `EntityNotFoundException(Messages.ACTOR_ID_NOT_FOUND)` when missing) → add keys in `messages.properties` + constants in `Messages.java` → `controller/ActorApi.java` (interface, `@Tag`, full OpenAPI doc) + `controller/impl/ActorController.java` (implements the interface, logs each endpoint) → Liquibase changelog if new table → Mockito tests in `service/impl/ActorServiceImplTest.java`.

### Error Handling
- Specific exception types: `MovieException` (generic business error → 400), `MovieDuplicateException` (conflict → 409, message already resolved via `MessageSourceAccessor` at throw time), `EntityNotFoundException` (jakarta, → 404), `MethodArgumentNotValidException` (`@Valid` → 400 with a list of invalid fields).
- i18n translation happens **only** in `controller/advice/ApplicationControllerAdvice` via `messageSource.getMessage(...)` — services/DTOs only pass keys from `Messages.java`, never a hardcoded string.

## Testing Guidelines

### Test Structure
- Location: `src/test/java/com/netflix/api/service/impl/`
- Naming: `[ClassUnderTest]Test` (e.g. `MovieServiceImplTest`)
- JUnit 5 + Mockito only (no Spring context loaded for service tests): `@ExtendWith(MockitoExtension.class)`, `@Mock` for repository/mapper/`MessageSourceAccessor`.
- Group tests by tested method with `@Nested @DisplayName("xxxTests")` (e.g. `getMoviesTests`, `addMovieTests`).
- Explicit, descriptive `@DisplayName` on every `@Test`, style: `"findById(), should thrown EntityNotFoundException when id not found"`.
- Grouped assertions via `Assertions.assertAll(...)` with an explicit message on each assertion.
- Verify mock interactions with `verify(...)` and `verifyNoInteractions(...)` (e.g. check the mapper isn't called when the repository finds nothing).

### Test Coverage
- Write a unit test per service method: success case + failure/exception case.
- In-memory H2 + `application-test.properties` for potential integration tests (Liquibase disabled, `ddl-auto=create-drop`).

## Build and Verification
- After any change, verify the project still builds: `./mvnw clean package` (or `./mvnw test` for tests only).
- Always run `./mvnw spotless:apply` to guarantee consistent formatting (Google Java Format) before considering a change done.
- For schema changes: add a Liquibase changelog in `db/changelog/` and reference it in `db.changelog-master.yaml`, then `./mvnw liquibase:update` (check status with `./mvnw liquibase:status`).
- CI (`.github/workflows/github-actions-workflow.yml`) runs on every push to `main`/`develop` and on every PR, in 3 sequential jobs: **Format check** (`./mvnw clean validate` + `./mvnw spotless:check` — fails if you forgot `spotless:apply`), **Unit Tests** (`./mvnw clean test`), **Build** (`./mvnw -B clean package -DskipTests`). Replicate these commands locally before pushing to avoid CI failures.

## Anti-Patterns
- ❌ Never use field injection (`@Autowired` on fields) — constructor injection only (`@AllArgsConstructor`, `private final`).
- ❌ Avoid returning `null`; use `Optional` instead where applicable.
- ❌ Don't catch generic exceptions without handling — use the specific exception types described in "Error Handling".
- ❌ Never expose entities directly in REST responses — always go through a DTO/mapper.
- ❌ Avoid hardcoded configuration values — externalize via `application.properties`/`.env`.
- ❌ Don't commit generated or unformatted (non-Spotless) code.
- ❌ Don't use magic numbers or hardcoded strings for HTTP status codes — use `HttpStatus`.
