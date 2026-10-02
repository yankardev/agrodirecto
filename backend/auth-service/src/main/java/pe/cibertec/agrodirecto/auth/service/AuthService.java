package pe.cibertec.agrodirecto.auth.service;

import pe.cibertec.agrodirecto.auth.dto.AuthResponse;
import pe.cibertec.agrodirecto.auth.dto.LoginRequest;
import pe.cibertec.agrodirecto.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
