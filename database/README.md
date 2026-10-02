# Base de datos

El entorno local usa un servidor PostgreSQL con bases separadas por responsabilidad:

- `auth_db`
- `catalog_db`
- `order_db`
- `report_db`

La separación permite evolucionar hacia independencia por microservicio sin exigir múltiples servidores durante el desarrollo académico.
