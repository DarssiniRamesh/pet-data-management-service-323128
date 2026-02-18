# api-gateway (strangler gateway)

Spring Cloud Gateway service used to implement the **strangler pattern** for the monolith → microservices migration.

## Goals

- Keep **client-facing URLs stable**
- Centralize **Basic Auth** at the gateway (first)
- Route selectively to microservices:
  - `/application/person/**` → `person-service`
  - `/**` → `pets_backend` (monolith) fallback

## Run (local dev)

In separate terminals:

### 1) Monolith
```bash
cd pet-data-management-service-323128/pets_backend
./gradlew bootRun --args='--server.port=3001'
```

### 2) Person Service
```bash
cd pet-data-management-service-323128/services/person-service
./gradlew bootRun --args='--server.port=3002'
```

### 3) Gateway (client-facing)
```bash
cd pet-data-management-service-323128/services/api-gateway
./gradlew bootRun --args='--server.port=3000'
```

## Auth

The gateway enforces HTTP Basic Auth for all routes except:
- `/health`
- `/swagger-ui.html`, `/swagger-ui/**`
- `/api-docs/**`

Default credentials (dev only):
- username: `admin`
- password: `admin`

Override via properties/env:
- `gateway.security.basic.username`
- `gateway.security.basic.password`
