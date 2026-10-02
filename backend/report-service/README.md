# report-service

Puerto local: **8085**.

Estructura interna por capas:

- `controller`: endpoints REST.
- `service`: contratos de negocio.
- `service/impl`: implementación de la lógica.
- `repository`: persistencia.
- `entity`: entidades del dominio.
- `dto`: objetos de entrada/salida.
- `config`: configuración.
- `exception`: manejo de errores.

El código funcional se implementará de manera incremental mediante ramas `feature/*`.
