# Laboratorio5-00037221

Proyecto Spring Boot con pruebas unitarias, Docker y GitHub Actions.

## Requisitos

- Java 17
- Docker
- Docker Compose

## Ejecutar pruebas

```bash
./mvnw test
```

## Levantar con Docker

Primero crear o completar el archivo `.env` con los datos de la base:

```env
POSTGRES_DB=laboratorio
POSTGRES_USER=lab_user
POSTGRES_PASSWORD=change-me
DOCKER_IMAGE=laboratorio5-00037221:local
```

Luego ejecutar:

```bash
docker compose up --build
```

La API queda en:

```text
http://localhost:8080
```

Health check:

```text
http://localhost:8080/actuator/health/readiness
```

## CI/CD

El workflow de GitHub Actions hace lo siguiente:

1. Ejecuta las pruebas.
2. Construye los contenedores.
3. Hace una prueba de humo con `curl`.
4. Publica la imagen en Docker Hub cuando se sube a `main`.

Secretos usados en GitHub:

- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`

## Enlaces

- Repositorio: `https://github.com/claudia034/Laboratorio5_PNC`
- Docker Hub: `https://hub.docker.com/r/claudia034/laboratorio5-00037221`
