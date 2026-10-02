package pe.cibertec.agrodirecto.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CarritoResponse {
    private Long carritoId;
    private Long clienteId;
    private List<CarritoItemResponse> items;
    private BigDecimal total;
}
