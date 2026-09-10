# Tarefas API

A small **REST API for managing tasks**, built with Spring Boot. Structured the way a real
service is: request/response DTOs, bean validation, a service layer, proper HTTP semantics,
a global error handler, tests at each layer, OpenAPI docs and a one-command Docker setup.

## Endpoints

Base path: `/tarefas`

| Method | Path | Success | Notes |
|---|---|---|---|
| `POST` | `/tarefas` | `201 Created` + `Location` | body validated; `400` with per-field errors if invalid |
| `GET` | `/tarefas` | `200` | list |
| `GET` | `/tarefas/{id}` | `200` | `404` (RFC 7807 `ProblemDetail`) if missing |
| `PUT` | `/tarefas/{id}` | `200` | validated; `404` if missing |
| `DELETE` | `/tarefas/{id}` | `204 No Content` | `404` if missing |

**Request** — `{ nome, dataEntrega (yyyy-MM-dd, today or later), responsavel }`, all required.
**Response** — `{ id, nome, dataEntrega, responsavel }` (the JPA entity is never exposed).

Interactive docs: **`/swagger-ui.html`** · OpenAPI JSON: `/v3/api-docs`.

## Architecture

```
Controller  → HTTP only: status codes, Location header, @Valid
  Service   → business rules, not-found handling
  Repository → Spring Data JPA
DTOs        → TarefaRequest (validated) / TarefaResponse
Advice      → GlobalExceptionHandler → ProblemDetail for 404 and 400
```

## Stack

`Java 17` · `Spring Boot 3.2` · `Spring Web` · `Spring Data JPA` · `Bean Validation` ·
`springdoc-openapi` · `MySQL` (runtime) / `H2` (tests) · `Maven` · `JUnit 5` + `Mockito`

## Run

### Docker (app + MySQL)

```bash
docker compose up --build      # API on http://localhost:8080, Swagger at /swagger-ui.html
```

### Locally

Needs a MySQL database `tarefas_db` (Hibernate creates the table via `ddl-auto=update`).
Credentials come from env vars, with a local default:

```bash
export SPRING_DATASOURCE_PASSWORD=yourpassword
./mvnw spring-boot:run
./mvnw test                    # 8 tests: @WebMvcTest, @DataJpaTest, context
```

## Example

```bash
curl -i -X POST http://localhost:8080/tarefas \
  -H "Content-Type: application/json" \
  -d '{"nome":"Entregar relatório","dataEntrega":"2026-12-30","responsavel":"Andrei"}'
# → 201 Created, Location: /tarefas/1

curl -s http://localhost:8080/tarefas/999
# → 404 { "type":"about:blank","title":"Not Found","status":404,"detail":"Tarefa 999 não encontrada" }

curl -s -X POST http://localhost:8080/tarefas -H "Content-Type: application/json" -d '{}'
# → 400 { ..., "detail":"Um ou mais campos são inválidos", "errors":{ "nome":"nome é obrigatório", ... } }
```

## Tests

| Test | Scope |
|---|---|
| `TarefaControllerTest` | `@WebMvcTest` + mocked service — status codes, `Location`, validation `400`, `404` |
| `TarefaRepositoryTest` | `@DataJpaTest` on H2 — persistence round-trip, `existsById` after delete |
| `TarefasApiApplicationTests` | context loads (H2) |
