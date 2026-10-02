package pe.cibertec.agrodirecto.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.agrodirecto.auth.dto.AuthResponse;
import pe.cibertec.agrodirecto.auth.dto.LoginRequest;
import pe.cibertec.agrodirecto.auth.dto.RegisterRequest;
import pe.cibertec.agrodirecto.auth.service.AuthService;
import org.springframework.security.core.Authentication;
import pe.cibertec.agrodirecto.auth.dto.UserResponse;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {

        return ResponseEntity.ok(
            authService.getCurrentUser(authentication.getName())
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("Acceso permitido para ADMIN");
    }
}
