package pe.cibertec.agrodirecto.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.agrodirecto.order.dto.AgregarCarritoRequest;
import pe.cibertec.agrodirecto.order.dto.CarritoResponse;
import pe.cibertec.agrodirecto.order.service.CarritoService;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @PostMapping
    public ResponseEntity<CarritoResponse> agregarProducto(
        @RequestBody AgregarCarritoRequest request) {

        return ResponseEntity.ok(
            carritoService.agregarProducto(request)
        );
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<CarritoResponse> obtenerCarrito(
        @PathVariable Long clienteId) {

        return ResponseEntity.ok(
            carritoService.obtenerCarrito(clienteId)
        );
    }

    @DeleteMapping("/item/{detalleId}")
    public ResponseEntity<Void> eliminarItem(
        @PathVariable Long detalleId) {

        carritoService.eliminarItem(detalleId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> vaciarCarrito(
        @PathVariable Long clienteId) {

        carritoService.vaciarCarrito(clienteId);

        return ResponseEntity.noContent().build();
    }
}
