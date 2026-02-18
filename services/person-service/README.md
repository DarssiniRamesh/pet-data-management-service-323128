# person-service (extracted microservice)

First extracted microservice for the Pets Spring Boot monolith migration.

## Goal

Preserve the original contract **initially**, including:
- Path prefix: `/application/person/**`
- Response envelope: `{ "success": boolean, "message": string|null, "data": any }`
- CRUD + pagination
- Validation + consistent error envelope

## Run

```bash
cd pet-data-management-service-323128/services/person-service
../..//pets_backend/gradlew bootRun
```

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
