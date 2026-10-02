package pe.cibertec.agrodirecto.order.service;

import pe.cibertec.agrodirecto.order.dto.AgregarCarritoRequest;
import pe.cibertec.agrodirecto.order.dto.CarritoResponse;

public interface CarritoService {

    CarritoResponse agregarProducto(AgregarCarritoRequest request);

    CarritoResponse obtenerCarrito(Long clienteId);

    void eliminarItem(Long detalleId);

    void vaciarCarrito(Long clienteId);
}
