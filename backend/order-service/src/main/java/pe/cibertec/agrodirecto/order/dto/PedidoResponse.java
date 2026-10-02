package pe.cibertec.agrodirecto.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PedidoResponse {
    private Long pedidoId;
    private Long clienteId;
    private Long agricultorId;
    private String estado;
    private BigDecimal total;
    private LocalDateTime fecha;
}
