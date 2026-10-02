package pe.cibertec.agrodirecto.order.dto;

import lombok.Data;

@Data
public class CheckoutRequest {
    private Long clienteId;
    private String direccionEntrega;
    private String referencia;
}
