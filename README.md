# Franchise API

API reactiva construida con Spring Boot WebFlux y MongoDB para administrar franquicias, sucursales y productos.

## Estado de cumplimiento

### Criterios obligatorios

| Requisito | Estado | Evidencia |
| --- | --- | --- |
| Proyecto en Spring Boot | Cumple | Spring Boot 3.5.13 en `pom.xml` |
| Agregar nueva franquicia | Cumple | `POST /api/franchises` |
| Agregar nueva sucursal | Cumple | `POST /api/franchises/{franchiseId}/branches` |
| Agregar nuevo producto | Cumple | `POST /api/franchises/{franchiseId}/branches/{branchId}/products` |
| Eliminar producto | Cumple | `DELETE /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}` |
| Modificar stock | Cumple | `PATCH /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock` |
| Producto con mayor stock por sucursal | Cumple | `GET /api/franchises/{franchiseId}/top-stock-products` |
| Persistencia con base de datos NoSQL | Cumple | MongoDB reactivo |
| Programación reactiva | Cumple | WebFlux + Reactor + Reactive Mongo |
| Unit tests | Cumple | `FranchiseServiceTest` y `FranchiseControllerTest` |
| Docker | Cumple | `Dockerfile` y `docker-compose.yml` |
| IaC | Cumple | Terraform en `infra/terraform` |
| Clean Architecture | Cumple | capas `domain`, `application`, `infrastructure` |
| Documentación de ejecución local | Cumple | este `README.md` |

### Puntos extra

| Requisito | Estado | Evidencia |
| --- | --- | --- |
| Renombrar franquicia | Cumple | `PATCH /api/franchises/{franchiseId}/name` |
| Renombrar sucursal | Cumple | `PATCH /api/franchises/{franchiseId}/branches/{branchId}/name` |
| Renombrar producto | Cumple | `PATCH /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/name` |
| Contenerización completa | Cumple | Docker + Docker Compose |
| Pipeline de validación | Cumple | GitHub Actions en `.github/workflows/ci.yml` |
| Health check | Cumple | `GET /actuator/health` |

### Observación importante

- La aplicación queda preparada para usar MongoDB local o un proveedor administrado en nube mediante la variable `MONGODB_URI`.
- El aprovisionamiento IaC incluido en el repositorio cubre el entorno local con Docker y Terraform.

## Checklist de la prueba

- Spring Boot 3 con Java 17
- Programacion reactiva con Spring WebFlux y Reactor
- Persistencia con MongoDB reactivo
- Clean Architecture por capas (`domain`, `application`, `infrastructure`)
- Unit tests sobre caso de uso y capa HTTP
- Docker y Docker Compose para ejecucion local
- Terraform para aprovisionar MongoDB local como IaC
- Endpoints extra para renombrar franquicia, sucursal y producto

## Qué cubre esta solución

- Programación reactiva con Spring WebFlux y repositorio reactivo de MongoDB
- Estructura basada en Clean Architecture
- Pruebas unitarias sobre caso de uso y controlador
- Docker para ejecutar la solución localmente
- Terraform para aprovisionar MongoDB en Docker como IaC local
- Documentación para levantar y probar la API

## Arquitectura

La solución está organizada en estas capas:

- `domain`: entidades, reglas de negocio y excepciones del dominio
- `application`: puertos de entrada/salida y casos de uso
- `infrastructure.persistence`: adaptador MongoDB reactivo
- `infrastructure.web`: controladores HTTP, DTOs, mapeadores y manejo de errores

La franquicia es el agregado principal. Cada documento persiste una franquicia con sus sucursales y productos embebidos, lo que simplifica las operaciones pedidas por la prueba.

## Requisitos

- JDK 17
- Docker Desktop
- Opcional: Terraform `>= 1.6`

## Configuración

Variables soportadas:

- `MONGODB_URI` default: `mongodb://localhost:27017/franchise_db`
- `MONGODB_URI` en nube: por ejemplo una cadena de MongoDB Atlas
- `SERVER_PORT` default: `8080`
- Actuator habilitado en `http://localhost:8080/actuator/health`

## Ejecutar local con Maven

1. Levantar MongoDB. Puedes hacerlo con Docker:

```powershell
docker run -d --name franchise-mongodb -p 27017:27017 mongo:7.0
```

2. Configurar Java si aún no está en tu sesión:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

3. Ejecutar pruebas:

```powershell
.\mvnw.cmd test
```

4. Levantar la aplicación:

```powershell
.\mvnw.cmd spring-boot:run
```

5. Validar rapidamente la API:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/franchises -ContentType 'application/json' -Body '{"name":"Franquicia Centro"}'
```

6. Verificar salud del servicio:

```powershell
Invoke-RestMethod -Method Get -Uri http://localhost:8080/actuator/health
```

## Ejecutar con Docker Compose

```powershell
docker compose up --build
```

Esto levanta:

- API en `http://localhost:8080`
- MongoDB en `localhost:27017`

## Aprovisionar MongoDB con Terraform

Desde la carpeta `infra/terraform`:

```powershell
terraform init
terraform apply -auto-approve
```

Terraform crea:

- Una red Docker
- Un volumen persistente
- Un contenedor MongoDB local

Para destruirlo:

```powershell
terraform destroy -auto-approve
```

## Endpoints

### 1. Listar franquicias

`GET /api/franchises`

Respuesta ejemplo:

```json
[
  {
    "id": "fr-1",
    "name": "Franquicia Centro",
    "branches": []
  }
]
```

### 2. Crear franquicia

`POST /api/franchises`

```json
{
  "name": "Franquicia Centro"
}
```

### 3. Agregar sucursal a una franquicia

`POST /api/franchises/{franchiseId}/branches`

```json
{
  "name": "Sucursal Norte"
}
```

### 4. Agregar producto a una sucursal

`POST /api/franchises/{franchiseId}/branches/{branchId}/products`

```json
{
  "name": "Coca-Cola",
  "stock": 12
}
```

### 5. Eliminar producto de una sucursal

`DELETE /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}`

### 6. Actualizar stock de producto

`PATCH /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock`

```json
{
  "stock": 20
}
```

### 7. Obtener el producto con más stock por sucursal de una franquicia

`GET /api/franchises/{franchiseId}/top-stock-products`

Respuesta esperada:

```json
[
  {
    "branchId": "branch-1",
    "branchName": "Sucursal Norte",
    "productId": "product-1",
    "productName": "Coca-Cola",
    "stock": 20
  }
]
```

### Endpoints extra

- `PATCH /api/franchises/{franchiseId}/name`
- `PATCH /api/franchises/{franchiseId}/branches/{branchId}/name`
- `PATCH /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/name`

Body para cualquiera de los renombres:

```json
{
  "name": "Nuevo Nombre"
}
```

## Ejemplo rápido con curl

Listar franquicias:

```bash
curl http://localhost:8080/api/franchises
```

Crear franquicia:

```bash
curl -X POST http://localhost:8080/api/franchises \
  -H "Content-Type: application/json" \
  -d '{"name":"Franquicia Centro"}'
```

Actualizar stock:

```bash
curl -X PATCH http://localhost:8080/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock \
  -H "Content-Type: application/json" \
  -d '{"stock":100}'
```

Health check:

```bash
curl http://localhost:8080/actuator/health
```

## Pruebas incluidas

- `FranchiseServiceTest`: valida la lógica central del caso de uso
- `FranchiseControllerTest`: valida comportamiento HTTP y validaciones del controlador

Comando de validacion:

```powershell
.\mvnw.cmd test
```

## Observaciones para la entrega

- La solución fue pensada para repositorio público y ejecución local rápida
- La persistencia se resolvió con un documento por franquicia y estructuras embebidas de sucursales y productos, suficiente para los casos de uso pedidos
- La aplicación puede conectarse a MongoDB Atlas u otro proveedor Mongo compatible usando `MONGODB_URI` sin cambios de código
- El repositorio incluye pipeline de CI para ejecutar pruebas automáticamente en GitHub Actions
- Si quieres llevarlo a nube como plus adicional, esta base permite cambiar la infraestructura sin tocar la lógica de dominio