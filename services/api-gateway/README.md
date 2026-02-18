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

## Auth (HTTP Basic)

The gateway enforces **HTTP Basic Auth** for all routes except:
- `/health`
- `/docs`
- `/swagger-ui.html`, `/swagger-ui/**`
- `/api-docs/**` (downstream OpenAPI JSON proxied via the gateway)

What this means in your browser:
- Visiting `http://<host>:3000/` (or any protected API path like `/application/person/**`) will show a **username/password prompt**.
- Visiting `http://<host>:3000/swagger-ui.html` should **not** prompt (it is allowlisted).

### Default credentials (dev only)
- username: `admin`
- password: `admin`

### Configure credentials (recommended via env vars)
Set the following environment variables:
- `GATEWAY_SECURITY_BASIC_USERNAME`
- `GATEWAY_SECURITY_BASIC_PASSWORD`

These env vars are mapped to Spring properties `gateway.security.basic.username` / `gateway.security.basic.password` in
`src/main/resources/application.properties`.

See `.env.example` in this folder for a ready-to-copy template.
