# Tarefas API

A small **REST API for managing tasks**, built with Spring Boot and JPA over MySQL.
Practical exercise in API design, persistence and testing.

## Endpoints

Base path: `/tarefas`

| Method | Path | Description |
|---|---|---|
| `POST` | `/tarefas` | Create a task |
| `GET` | `/tarefas` | List all tasks |
| `GET` | `/tarefas/{id}` | Get a task by id |
| `PUT` | `/tarefas/{id}` | Update a task (creates it with that id if missing) |
| `DELETE` | `/tarefas/{id}` | Delete a task |

**Task** = `{ id, nome, dataEntrega (yyyy-MM-dd), responsavel }`.

## Stack

`Java 17` · `Spring Boot 3.2` · `Spring Web` · `Spring Data JPA` · `MySQL` · `Maven` · `JUnit 5`

## Running

1. Create a MySQL database named `tarefas_db` (Hibernate creates the `tarefas` table via `ddl-auto=update`).
2. Set your DB credentials in `src/main/resources/application.properties`.
3. Start it:

```bash
./mvnw spring-boot:run        # http://localhost:8080/tarefas
./mvnw test                   # run the tests
```

Example:

```bash
curl -X POST http://localhost:8080/tarefas \
  -H "Content-Type: application/json" \
  -d '{"nome":"Entregar relatório","dataEntrega":"2025-06-30","responsavel":"Andrei"}'
```

## Note

`application.properties` currently has a hard-coded local DB user/password. For anything beyond
local practice, move those to environment variables (`SPRING_DATASOURCE_USERNAME` /
`SPRING_DATASOURCE_PASSWORD`) and keep only placeholders in the committed file.
