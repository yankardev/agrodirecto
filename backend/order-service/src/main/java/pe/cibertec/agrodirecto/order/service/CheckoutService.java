package pe.cibertec.agrodirecto.order.service;

import pe.cibertec.agrodirecto.order.dto.CheckoutRequest;
import pe.cibertec.agrodirecto.order.dto.PedidoResponse;

import java.util.List;

public interface CheckoutService {

    List<PedidoResponse> procesarCheckout(CheckoutRequest request);
}
