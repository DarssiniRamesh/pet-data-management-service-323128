# person-service (extracted microservice)

First extracted microservice for the Pets Spring Boot monolith migration.

## Goal

Preserve the original contract **initially**, including:
- Path prefix: `/application/person/**`
- Response envelope: `{ "success": boolean, "message": string|null, "data": any }`
- CRUD + pagination
- Validation + consistent error envelope

## Run

Preferred (uses this service's local Gradle wrapper):

```bash
cd pet-data-management-service-323128/services/person-service
./gradlew bootRun
```

Run on a dedicated port (recommended when running alongside the monolith/gateway):

```bash
cd pet-data-management-service-323128/services/person-service
./gradlew bootRun --args='--server.port=3002'
```

Fallback options:

- Use the monolith wrapper (works even if you don't have Gradle installed):
  ```bash
  cd pet-data-management-service-323128/services/person-service
  ../../pets_backend/gradlew bootRun
  ```
- Or use a locally installed Gradle:
  ```bash
  cd pet-data-management-service-323128/services/person-service
  gradle bootRun
  ```

## Database migrations (Flyway)

This service uses **Flyway** for deterministic schema management.

### Where migrations live
- `src/main/resources/db/migration/`
- Naming: `V<version>__<description>.sql` (e.g. `V1__create_persons_table.sql`)

### How migrations are applied
- On startup, Spring Boot auto-runs Flyway and records applied versions in `flyway_schema_history`.
- Hibernate DDL generation is disabled (`spring.jpa.hibernate.ddl-auto=validate`) so the schema source of truth is the migrations.

### Local development notes
- Default DB is H2 in-memory. Each fresh process starts with an empty DB and Flyway re-creates the schema deterministically.
- For a persistent DB (future cutover), keep Flyway enabled and point `spring.datasource.url` to the real database; Flyway will migrate it to the required version.

Notes:
- This service is a standalone Gradle project (it includes its own `build.gradle` and `settings.gradle`).
- If you prefer, you can also run with a local Gradle installation:
  ```bash
  gradle bootRun
  ```

## API

- Swagger UI: `/docs` (redirects to `/swagger-ui.html`)
- OpenAPI JSON: `/api-docs`

### Endpoints

- `POST   /application/person`
- `GET    /application/person/{id}`
- `GET    /application/person?page=0&size=20&sort=id&dir=asc`
- `PUT    /application/person/{id}`
- `DELETE /application/person/{id}`
