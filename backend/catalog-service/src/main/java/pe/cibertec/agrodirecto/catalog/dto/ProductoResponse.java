// ProductoResponse.java
package pe.cibertec.agrodirecto.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductoResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String unidadMedida;
    private String imagenUrl;
    private Boolean activo;
    private Long agricultorId;
    private Long categoriaId;
    private String categoriaNombre;
}
