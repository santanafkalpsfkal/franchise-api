# Franchise API

API reactiva construida con Spring Boot WebFlux y MongoDB para administrar franquicias, sucursales y productos.

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
- `SERVER_PORT` default: `8080`

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

### 1. Crear franquicia

`POST /api/franchises`

```json
{
  "name": "Franquicia Centro"
}
```

### 2. Agregar sucursal a una franquicia

`POST /api/franchises/{franchiseId}/branches`

```json
{
  "name": "Sucursal Norte"
}
```

### 3. Agregar producto a una sucursal

`POST /api/franchises/{franchiseId}/branches/{branchId}/products`

```json
{
  "name": "Coca-Cola",
  "stock": 12
}
```

### 4. Eliminar producto de una sucursal

`DELETE /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}`

### 5. Actualizar stock de producto

`PATCH /api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock`

```json
{
  "stock": 20
}
```

### 6. Obtener el producto con más stock por sucursal de una franquicia

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

Crear franquicia:

```bash
curl -X POST http://localhost:8080/api/franchises \
  -H "Content-Type: application/json" \
  -d '{"name":"Franquicia Centro"}'
```

## Pruebas incluidas

- `FranchiseServiceTest`: valida la lógica central del caso de uso
- `FranchiseControllerTest`: valida comportamiento HTTP y validaciones del controlador

## Observaciones para la entrega

- La solución fue pensada para repositorio público y ejecución local rápida
- Si vas a subirla a GitHub, conviene inicializar el repositorio y hacer commits pequeños por capa
- Si quieres despliegue cloud como plus adicional, esta base permite extender a AWS, Azure o GCP sin cambiar la lógica de dominio