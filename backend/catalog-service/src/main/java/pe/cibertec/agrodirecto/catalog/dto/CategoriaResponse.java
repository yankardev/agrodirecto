// CategoriaResponse.java
package pe.cibertec.agrodirecto.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
