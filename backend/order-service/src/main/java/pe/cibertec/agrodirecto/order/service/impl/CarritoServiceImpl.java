package pe.cibertec.agrodirecto.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.cibertec.agrodirecto.order.client.CatalogClient;
import pe.cibertec.agrodirecto.order.dto.AgregarCarritoRequest;
import pe.cibertec.agrodirecto.order.dto.CarritoItemResponse;
import pe.cibertec.agrodirecto.order.dto.CarritoResponse;
import pe.cibertec.agrodirecto.order.entity.Carrito;
import pe.cibertec.agrodirecto.order.entity.DetalleCarrito;
import pe.cibertec.agrodirecto.order.repository.CarritoRepository;
import pe.cibertec.agrodirecto.order.repository.DetalleCarritoRepository;
import pe.cibertec.agrodirecto.order.service.CarritoService;
import pe.cibertec.agrodirecto.order.client.CatalogClient;
import pe.cibertec.agrodirecto.order.dto.ProductoCatalogResponse;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final DetalleCarritoRepository detalleCarritoRepository;
    private final CatalogClient catalogClient;

    @Override
    @Transactional
    public CarritoResponse agregarProducto(AgregarCarritoRequest request) {

        ProductoCatalogResponse producto =
            catalogClient.obtenerProducto(request.getProductoId());

        if (producto == null || Boolean.FALSE.equals(producto.getActivo())) {
            throw new RuntimeException("Producto no disponible");
        }

        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        if (producto.getStock() < request.getCantidad()) {
            throw new RuntimeException("Stock insuficiente");
        }

        Carrito carrito = carritoRepository
            .findByClienteIdAndActivoTrue(request.getClienteId())
            .orElseGet(() -> carritoRepository.save(
                Carrito.builder()
                    .clienteId(request.getClienteId())
                    .activo(true)
                    .build()
            ));

        DetalleCarrito detalle = detalleCarritoRepository
            .findByCarritoIdAndProductoId(
                carrito.getId(),
                request.getProductoId()
            )
            .orElse(null);

        if (detalle != null) {

            int nuevaCantidad =
                detalle.getCantidad() + request.getCantidad();

            if (producto.getStock() < nuevaCantidad) {
                throw new RuntimeException("Stock insuficiente");
            }

            detalle.setCantidad(nuevaCantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setAgricultorId(producto.getAgricultorId());

        } else {

            detalle = DetalleCarrito.builder()
                .carrito(carrito)
                .productoId(producto.getId())
                .agricultorId(producto.getAgricultorId())
                .cantidad(request.getCantidad())
                .precioUnitario(producto.getPrecio())
                .build();
        }

        detalleCarritoRepository.save(detalle);

        return obtenerCarrito(request.getClienteId());
    }

    @Override
    public CarritoResponse obtenerCarrito(Long clienteId) {

        Carrito carrito = carritoRepository
            .findByClienteIdAndActivoTrue(clienteId)
            .orElseThrow(() ->
                new RuntimeException("Carrito no encontrado"));

        List<CarritoItemResponse> items =
            detalleCarritoRepository.findByCarritoId(carrito.getId())
                .stream()
                .map(this::convertirItem)
                .toList();

        BigDecimal total = items.stream()
            .map(CarritoItemResponse::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CarritoResponse(
            carrito.getId(),
            carrito.getClienteId(),
            items,
            total
        );
    }

    @Override
    @Transactional
    public void eliminarItem(Long detalleId) {

        if (!detalleCarritoRepository.existsById(detalleId)) {
            throw new RuntimeException("Item no encontrado");
        }

        detalleCarritoRepository.deleteById(detalleId);
    }

    @Override
    @Transactional
    public void vaciarCarrito(Long clienteId) {

        Carrito carrito = carritoRepository
            .findByClienteIdAndActivoTrue(clienteId)
            .orElseThrow(() ->
                new RuntimeException("Carrito no encontrado"));

        detalleCarritoRepository.deleteByCarritoId(carrito.getId());
    }

    private CarritoItemResponse convertirItem(DetalleCarrito detalle) {

        BigDecimal subtotal =
            detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));

        return new CarritoItemResponse(
            detalle.getId(),
            detalle.getProductoId(),
            detalle.getAgricultorId(),
            detalle.getCantidad(),
            detalle.getPrecioUnitario(),
            subtotal
        );
    }
}
