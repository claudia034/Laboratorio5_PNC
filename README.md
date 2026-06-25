# Laboratorio5-00037221

Aplicacion Spring Boot modernizada con pruebas unitarias Mockito, Docker Compose con PostgreSQL y pipeline CI/CD para publicar en Docker Hub.

## Requisitos

- Java 17
- Docker y Docker Compose
- Cuenta de Docker Hub para publicar la imagen desde GitHub Actions

## Tests

```bash
./mvnw test
```

Las pruebas unitarias del servicio usan `@Mock` e `@InjectMocks`; no levantan el contexto completo de Spring ni usan H2.

## Docker local

Define las variables de entorno antes de levantar los contenedores:

```bash
export POSTGRES_DB=laboratorio
export POSTGRES_USER=lab_user
export POSTGRES_PASSWORD=change-me
export DOCKER_IMAGE=laboratorio5-00037221:local
docker compose up --build
```

La API queda disponible en `http://localhost:8080` y la prueba de humo usa `http://localhost:8080/actuator/health/readiness`.

## GitHub Actions

El workflow `.github/workflows/ci-cd.yml` ejecuta:

1. Compilacion y pruebas unitarias con Maven.
2. Construccion temporal con Docker Compose.
3. Prueba de humo con `curl` contra Actuator.
4. Publicacion en Docker Hub solo cuando el cambio llega a `main`.

Configura estos secretos en GitHub:

- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`

Configura una regla de proteccion para `main` exigiendo que el check `Tests and Docker smoke test` pase antes de permitir merge.

## Entregables

- Repositorio: `https://github.com/<usuario>/<repositorio>`
- Docker Hub: `https://hub.docker.com/r/<usuario>/laboratorio5-00037221`
