package pe.cibertec.agrodirecto.order.dto;

import lombok.Data;

@Data
public class AgregarCarritoRequest {
    private Long clienteId;
    private Long productoId;
    private Long agricultorId;
    private Integer cantidad;
}
