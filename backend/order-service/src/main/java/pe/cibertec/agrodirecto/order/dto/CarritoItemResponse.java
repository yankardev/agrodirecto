package pe.cibertec.agrodirecto.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CarritoItemResponse {
    private Long detalleId;
    private Long productoId;
    private Long agricultorId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
