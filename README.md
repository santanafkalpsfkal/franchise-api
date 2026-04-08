# Franchise API

API reactiva para administrar franquicias, sucursales y productos.

## Despliegue en nube

- API publicada en Render: `https://franchise-api-mqip.onrender.com`
- Endpoint publico base: `https://franchise-api-mqip.onrender.com/`
- Endpoint publico de estado: `https://franchise-api-mqip.onrender.com/health`
- Health check: `https://franchise-api-mqip.onrender.com/actuator/health`

Variables esperadas en el despliegue:

- `MONGODB_URI`
- `SERVER_PORT=8080`

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

Para usar una cadena externa, puedes definir `MONGODB_URI` en un archivo `.env` en la raiz del proyecto.

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

Si usas MongoDB Atlas u otra instancia externa, crea `.env` con:

```dotenv
MONGODB_URI=tu_cadena_de_conexion
SERVER_PORT=8080
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