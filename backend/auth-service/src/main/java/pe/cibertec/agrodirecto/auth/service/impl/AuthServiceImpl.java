package pe.cibertec.agrodirecto.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.cibertec.agrodirecto.auth.dto.AuthResponse;
import pe.cibertec.agrodirecto.auth.dto.LoginRequest;
import pe.cibertec.agrodirecto.auth.dto.RegisterRequest;
import pe.cibertec.agrodirecto.auth.dto.UserResponse;
import pe.cibertec.agrodirecto.auth.entity.Rol;
import pe.cibertec.agrodirecto.auth.entity.Usuario;
import pe.cibertec.agrodirecto.auth.repository.RolRepository;
import pe.cibertec.agrodirecto.auth.repository.UsuarioRepository;
import pe.cibertec.agrodirecto.auth.security.JwtService;
import pe.cibertec.agrodirecto.auth.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Rol rol = rolRepository.findByNombre(request.getRol())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rol);
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(
            usuario.getEmail(),
            usuario.getRol().getNombre()
        );

        return new AuthResponse(
            token,
            "Bearer",
            usuario.getEmail(),
            usuario.getRol().getNombre()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(
            request.getPassword(),
            usuario.getPassword())) {

            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(
            usuario.getEmail(),
            usuario.getRol().getNombre()
        );

        return new AuthResponse(
            token,
            "Bearer",
            usuario.getEmail(),
            usuario.getRol().getNombre()
        );
    }

    @Override
    public UserResponse getCurrentUser(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UserResponse(
            usuario.getId(),
            usuario.getNombres(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getRol().getNombre()
        );
    }
}
