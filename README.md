# Pet Data Management Service - Workspace

This workspace contains the existing `pets_backend` Spring Boot service and will evolve into a multi-service layout.

## Existing service (still runnable)
Path:
- `pet-data-management-service-323128/pets_backend`

Run:
```bash
cd pet-data-management-service-323128/pets_backend
./gradlew bootRun
```

## Migration layout (introduced in step 03.00)
At the repository root:
- `services/` - deployable services (new microservices + gateway will be added here)
- `libs/` - optional shared libraries

No code has been moved yet to avoid breaking existing build/run paths.
