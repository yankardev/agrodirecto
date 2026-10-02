package pe.cibertec.agrodirecto.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String nombres;
    private String apellidos;
    private String email;
    private String rol;
}
