# banco-cicd

[![CI/CD Pipeline](https://github.com/c4rlosaucu/banco-cicd/actions/workflows/build.yml/badge.svg)](https://github.com/c4rlosaucu/banco-cicd/actions/workflows/build.yml)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=c4rlosaucu_banco-cicd2&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=c4rlosaucu_banco-cicd2)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=c4rlosaucu_banco-cicd2&metric=coverage)](https://sonarcloud.io/summary/new_code?id=c4rlosaucu_banco-cicd2)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=c4rlosaucu_banco-cicd2&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=c4rlosaucu_banco-cicd2)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=c4rlosaucu_banco-cicd2&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=c4rlosaucu_banco-cicd2)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=c4rlosaucu_banco-cicd2&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=c4rlosaucu_banco-cicd2)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=c4rlosaucu_banco-cicd2&metric=bugs)](https://sonarcloud.io/summary/new_code?id=c4rlosaucu_banco-cicd2)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=c4rlosaucu_banco-cicd2&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=c4rlosaucu_banco-cicd2)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=c4rlosaucu_banco-cicd2&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=c4rlosaucu_banco-cicd2)

Backend de una aplicación bancaria (Laboratorio 1, Arquitectura de Software, UdeA) con un pipeline
de integración y despliegue continuo (Laboratorio 2). Permite crear clientes, transferir dinero
entre cuentas y consultar el histórico de transacciones.

**API desplegada en la nube:** https://banco-cicd.onrender.com/api/customers

> El servicio corre en un plan gratuito: si estuvo inactivo, la primera petición puede tardar
> cerca de un minuto en responder.

## Tecnologías

- Java 21, Spring Boot 4, Spring Data JPA, MapStruct, Lombok
- MySQL (Aiven en la nube, MySQL local en desarrollo)
- JUnit 5 y Mockito, JaCoCo para cobertura
- GitHub Actions, SonarCloud, Docker / Docker Hub, Render

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/customers` | Lista los clientes |
| GET | `/api/customers/{id}` | Consulta un cliente por id |
| POST | `/api/customers` | Crea un cliente |
| POST | `/api/transactions/transfer` | Transfiere dinero entre dos cuentas |
| GET | `/api/transactions` | Lista todas las transacciones |
| GET | `/api/transactions/account/{accountNumber}` | Histórico de una cuenta |

Ejemplo de transferencia:

```json
{"senderAccountNumber":"111","receiverAccountNumber":"222","amount":100}
```

## Variables de entorno

| Variable | Descripción | Valor por defecto (local)                  |
|---|---|--------------------------------------------|
| `DB_URL` | URL JDBC de la base de datos | `jdbc:mysql://localhost:3306/udeabank?...` |
| `DB_USER` | Usuario de la base de datos | `(tu usuario local)`                       |
| `DB_PASSWORD` | Contraseña de la base de datos | `(tu contraseña local)`                    |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos por CORS, separados por coma | `http://localhost:5173`                    |
| `PORT` | Puerto HTTP (lo inyecta Render) | `8080`                                     |

## Ejecución local

```shell
./mvnw spring-boot:run
```

Requiere un MySQL local con la base `udeabank`. En Windows/PowerShell usa `.\mvnw`.

## Pruebas y cobertura

```shell
./mvnw clean verify
```

Las pruebas usan H2 en memoria (no necesitan MySQL). El reporte de JaCoCo queda en
`target/site/jacoco/`.

## Imagen Docker

```shell
./mvnw clean package -DskipTests
docker build -t c4rlosaucu/banco-cicd:latest .
docker run -p 8080:8080 -e DB_URL="..." -e DB_USER="..." -e DB_PASSWORD="..." c4rlosaucu/banco-cicd:latest
```

La imagen publicada está en Docker Hub: `c4rlosaucu/banco-cicd`.

## Pipeline CI/CD

El workflow `.github/workflows/build.yml` se ejecuta en cada push a `main` y a ramas `feature/**`,
y en cada pull request hacia `main`:

1. **Unit tests**: ejecuta las pruebas con Maven.
2. **SonarCloud analysis**: compila, mide cobertura con JaCoCo y analiza la calidad del código.
3. **Build and Docker image**: genera el JAR, construye la imagen y, solo en `main`, la publica en Docker Hub.
4. **Deploy to Render**: solo en `main`, dispara el despliegue de la imagen en Render mediante un deploy hook.

Se trabaja con estrategia trunk based: cada cambio entra a `main` mediante un pull request desde una rama `feature/*`.

## Repositorios relacionados

- Backend original del Laboratorio 1: https://github.com/c4rlosaucu/banco-backend
- Frontend del Laboratorio 1: https://github.com/c4rlosaucu/banco-frontend
- Laboratorio 2 (aplicación de práctica): https://github.com/c4rlosaucu/lab2arq