// ProductoRequest.java
package pe.cibertec.agrodirecto.catalog.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoRequest {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String unidadMedida;
    private String imagenUrl;
    private Long agricultorId;
    private Long categoriaId;
}
