package pe.cibertec.agrodirecto.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.agrodirecto.order.dto.ActualizarEstadoPedidoRequest;
import pe.cibertec.agrodirecto.order.dto.PedidoResponse;
import pe.cibertec.agrodirecto.order.service.PedidoService;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponse>> listarPorCliente(
        @PathVariable Long clienteId) {

        return ResponseEntity.ok(
            pedidoService.listarPorCliente(clienteId)
        );
    }

    @GetMapping("/agricultor/{agricultorId}")
    public ResponseEntity<List<PedidoResponse>> listarPorAgricultor(
        @PathVariable Long agricultorId) {

        return ResponseEntity.ok(
            pedidoService.listarPorAgricultor(agricultorId)
        );
    }

    @PutMapping("/{pedidoId}/estado")
    public ResponseEntity<PedidoResponse> actualizarEstado(
        @PathVariable Long pedidoId,
        @RequestBody ActualizarEstadoPedidoRequest request) {

        return ResponseEntity.ok(
            pedidoService.actualizarEstado(
                pedidoId,
                request.getEstado()
            )
        );
    }
}
