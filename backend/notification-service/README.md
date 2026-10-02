# notification-service

Puerto local: **8084**.

Servicio responsable de consumir eventos asíncronos mediante RabbitMQ y procesar notificaciones.

Estructura inicial por capas:

- `controller`: endpoints REST auxiliares.
- `service`: lógica de notificaciones.
- `config`: configuración de RabbitMQ.
- `dto`: mensajes y contratos de eventos.
- `exception`: manejo de errores.

El código funcional se implementará de manera incremental mediante ramas `feature/*`.
