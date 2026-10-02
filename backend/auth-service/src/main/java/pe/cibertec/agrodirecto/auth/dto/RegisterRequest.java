package pe.cibertec.agrodirecto.auth.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nombres;
    private String apellidos;
    private String email;
    private String password;
    private String rol;
}
