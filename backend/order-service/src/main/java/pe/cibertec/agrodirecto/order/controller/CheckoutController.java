package pe.cibertec.agrodirecto.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.agrodirecto.order.dto.CheckoutRequest;
import pe.cibertec.agrodirecto.order.dto.PedidoResponse;
import pe.cibertec.agrodirecto.order.service.CheckoutService;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<List<PedidoResponse>> procesarCheckout(
        @RequestBody CheckoutRequest request) {

        return ResponseEntity.ok(
            checkoutService.procesarCheckout(request)
        );
    }
}
