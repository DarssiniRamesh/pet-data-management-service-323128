# pets-backend (monolith wrapper)

This folder exists to align the **existing monolith** with the new `/services` layout.

Important: the monolith codebase has **not** been moved yet (to avoid breaking existing preview/build paths).  
The authoritative monolith project remains at:

- `pet-data-management-service-323128/pets_backend`

## Run (recommended)

```bash
cd pet-data-management-service-323128/pets_backend
./gradlew bootRun --args="--server.port=3001"
```

## Why this wrapper exists

Other services now live under `pet-data-management-service-323128/services/*` (e.g. `api-gateway`, `person-service`).  
This wrapper makes it clear that the monolith is also a “service” in the architecture, even before physically moving it.

## API

- Swagger UI: `http://localhost:3001/docs` (redirects to `/swagger-ui.html`)
- OpenAPI JSON: `http://localhost:3001/api-docs`

## Notes for future migration

When we eventually move the monolith into `/services/pets-backend` for real, we should:
- relocate Gradle project files and `src/`
- update any references that assume `pets_backend/`
- ensure `api-gateway` fallback route still targets the correct host/port
