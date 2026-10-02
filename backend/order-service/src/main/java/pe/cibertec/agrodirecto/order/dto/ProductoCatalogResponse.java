package pe.cibertec.agrodirecto.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoCatalogResponse {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;
    private Long agricultorId;
    private Long categoriaId;
    private String categoriaNombre;
}
