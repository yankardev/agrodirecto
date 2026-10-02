# Arquitectura inicial

```text
Angular :4200
    |
    v
API Gateway :8080
    |
    +--> Auth Service :8081 ------> auth_db
    +--> Catalog Service :8082 ---> catalog_db
    +--> Order Service :8083 -----> order_db
    |          |
    |          +----> RabbitMQ ----> Notification Service :8084
    |
    +--> Report Service :8085 ----> report_db
```

Cada microservicio Spring Boot mantiene separación interna por capas:

`Controller -> Service -> Repository -> Entity`
