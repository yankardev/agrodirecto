# Interfaces AgroDirecto — prototipo local

Este prototipo Angular presenta **dos interfaces diferenciadas**, inspiradas en el formato de capturas del informe EFSRT, pero adaptadas al comercio agrícola de AgroDirecto.

## Requisitos
- Node.js 22 y npm
- Git
- No requiere iniciar los microservicios ni PostgreSQL para probar las pantallas.

## Cómo ejecutar en tu computadora

Desde la raíz del repositorio:

```bash
git fetch origin
git switch feature/interfaces-tienda-admin
cd frontend/agrodirecto-web
npm install
npm start
```

Abre **http://localhost:4200**.

Si aún no tienes el repositorio descargado:

```bash
git clone https://github.com/yankardev/agrodirecto.git
cd agrodirecto
git switch feature/interfaces-tienda-admin
cd frontend/agrodirecto-web
npm install
npm start
```

## Pantallas
- **/**: selección de interfaz.
- **/tienda**: catálogo, filtros, búsqueda, carrito, pedidos y formulario de reclamaciones.
- **/admin**: resumen, inventario, estados de pedidos, aprobación de agricultores, reportes visuales y reclamaciones.

## Cómo probar
1. Ingresa al área de compras, agrega productos y registra un pedido con nombre y dirección.
2. Cambia al panel administrativo: el pedido aparecerá en **Gestión de pedidos**.
3. Cambia el estado del pedido o ajusta el stock. Regresa a la tienda para observar los cambios.
4. Los datos se guardan en el **localStorage** de tu navegador. Puedes usar «Restablecer datos de prueba» desde administración.

## Limitaciones importantes
- Es un **prototipo de interfaz**, no una implementación completa del sistema.
- La navegación entre cliente y administrador **no tiene autenticación ni autorización real**. No usar en producción.
- Las compras no procesan pagos, envíos ni transacciones del backend. El descuento de stock es solo una simulación local.
- Los pedidos, agricultores y reclamaciones se almacenan en el navegador, **no en PostgreSQL**.
- Los indicadores son calculados sobre datos de prueba; las exportaciones PDF, Excel y CSV todavía no están implementadas.
- El frontend no consume aún los endpoints de Spring Boot. La integración con JWT, catálogo, checkout y reportes es trabajo posterior.

## Verificación de compilación

```bash
npm run build
```

Si el puerto 4200 está ocupado, usa `npm start -- --port 4201`.
