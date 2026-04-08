# Franchise API

API reactiva para administrar franquicias, sucursales y productos.

## Stack

- Spring Boot 3.5.13
- Java 17
- Spring WebFlux
- MongoDB reactivo
- JUnit 5, Mockito y Reactor Test
- Docker y Docker Compose
- Terraform

## Estructura

- `domain`: entidades y reglas de negocio
- `application`: casos de uso y puertos
- `infrastructure.persistence`: persistencia con MongoDB
- `infrastructure.web`: API HTTP, DTOs y manejo de errores

## Requisitos

- JDK 17
- Docker Desktop
- Terraform 1.6 o superior para la parte de IaC

## Variables de entorno

- `MONGODB_URI`: `mongodb://localhost:27017/franchise_db`
- `SERVER_PORT`: `8080`

## Nota sobre persistencia

La solución utiliza MongoDB en entorno local mediante Docker para facilitar la ejecución y evaluación. No obstante, la aplicación es completamente compatible con proveedores en la nube como MongoDB Atlas mediante la configuración de la variable de entorno `MONGODB_URI`.

## Ejecución local

1. Levantar MongoDB:

```powershell
docker run -d --name franchise-mongodb -p 27017:27017 mongo:7.0
```

2. Configurar Java 17 en la sesión si hace falta:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-17'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

3. Ejecutar pruebas:

```powershell
.\mvnw.cmd test
```

4. Ejecutar la aplicación:

```powershell
.\mvnw.cmd spring-boot:run
```

5. Verificar salud:

```powershell
Invoke-RestMethod -Method Get -Uri http://localhost:8080/actuator/health
```

## Docker Compose

```powershell
docker compose up --build
```

Servicios expuestos:

- API: `http://localhost:8080`
- MongoDB: `localhost:27017`

## Terraform

Desde `infra/terraform`:

```powershell
terraform init
terraform apply -auto-approve
```

Esto aprovisiona recursos Docker locales para MongoDB.

Para destruirlos:

```powershell
terraform destroy -auto-approve
```

## Endpoints

- `GET /api/franchises`
- `POST /api/franchises`
- `POST /api/franchises/{franchiseId}/branches`
- `POST /api/franchises/{franchiseId}/branches/{branchId}/products`
- `DELETE /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}`
- `PATCH /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock`
- `GET /api/franchises/{franchiseId}/top-stock-products`
- `PATCH /api/franchises/{franchiseId}/name`
- `PATCH /api/franchises/{franchiseId}/branches/{branchId}/name`
- `PATCH /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/name`

## Ejemplo rápido

```bash
curl -X POST http://localhost:8080/api/franchises \
  -H "Content-Type: application/json" \
  -d '{"name":"Franquicia Centro"}'
```

## Pruebas

```powershell
.\mvnw.cmd test
```