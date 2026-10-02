# AgroDirecto

Plataforma web para conectar directamente pequeños agricultores con consumidores, centralizando catálogo, inventario, carrito, pedidos y reportes.

## Stack tecnológico

- Frontend: Angular + TypeScript
- Backend: Java 21 + Spring Boot
- Arquitectura: microservicios; arquitectura por capas dentro de cada servicio
- APIs: REST + OpenAPI/Swagger
- Seguridad: Spring Security + JWT (implementación progresiva)
- Persistencia: PostgreSQL + Spring Data JPA
- Mensajería: RabbitMQ
- Infraestructura: Docker / Docker Compose
- Pruebas: JUnit, Mockito y Postman
- Versionamiento: Git + GitHub

## Microservicios iniciales

| Servicio | Puerto | Responsabilidad |
|---|---:|---|
| api-gateway | 8080 | Entrada única a los servicios |
| auth-service | 8081 | Usuarios, roles, autenticación y JWT |
| catalog-service | 8082 | Agricultores, categorías, productos, precios y stock |
| order-service | 8083 | Carrito, compra, pedidos y detalle de pedidos |
| notification-service | 8084 | Eventos y notificaciones mediante RabbitMQ |
| report-service | 8085 | Consultas y reportes de negocio |

## Estructura

```text
agrodirecto/
├── frontend/agrodirecto-web/
├── backend/
│   ├── api-gateway/
│   ├── auth-service/
│   ├── catalog-service/
│   ├── order-service/
│   ├── notification-service/
│   └── report-service/
├── database/
├── infrastructure/
├── docs/
├── postman/
└── .github/
```

## Inicio rápido

### 1. Infraestructura

```bash
cp .env.example .env
docker compose -f infrastructure/docker-compose.yml --env-file .env up -d
```

PostgreSQL quedará en `localhost:5432` y RabbitMQ en `localhost:5672`.
La consola de RabbitMQ estará en `http://localhost:15672`.

### 2. Backend

Desde la raíz:

```bash
cd backend
mvn clean test
mvn -pl auth-service spring-boot:run
```

Cada microservicio puede ejecutarse por separado desde IntelliJ.

### 3. Frontend

```bash
cd frontend/agrodirecto-web
npm install
npm start
```

Frontend: `http://localhost:4200`

## Flujo Git del equipo

- `main`: código estable.
- `develop`: integración del equipo.
- `feature/<nombre>`: desarrollo de cada funcionalidad.
- `fix/<nombre>`: correcciones.

Nunca desarrollar directamente sobre `main`.

Ejemplo:

```bash
git checkout develop
git pull origin develop
git checkout -b feature/catalogo-productos
# trabajar
git add .
git commit -m "feat: implement product catalog"
git push -u origin feature/catalogo-productos
```

Después crear Pull Request hacia `develop`.

## Convención de commits

- `feat:` nueva funcionalidad
- `fix:` corrección
- `docs:` documentación
- `refactor:` refactorización
- `test:` pruebas
- `chore:` configuración o mantenimiento

## Equipo

- Yan Carlos
- Deyner
- Jennyfer
- Yuly
- Alexander

## Estado inicial

Este repositorio contiene la estructura base para iniciar el trabajo colaborativo. Las reglas de negocio, entidades y endpoints se implementarán de forma incremental por módulos.
