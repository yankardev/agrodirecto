package pe.cibertec.agrodirecto.auth.service;

import pe.cibertec.agrodirecto.auth.dto.AuthResponse;
import pe.cibertec.agrodirecto.auth.dto.LoginRequest;
import pe.cibertec.agrodirecto.auth.dto.RegisterRequest;
import pe.cibertec.agrodirecto.auth.dto.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getCurrentUser(String email);
}
