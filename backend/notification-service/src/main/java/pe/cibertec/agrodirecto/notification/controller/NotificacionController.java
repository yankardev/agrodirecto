package pe.cibertec.agrodirecto.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.agrodirecto.notification.entity.Notificacion;
import pe.cibertec.agrodirecto.notification.service.NotificacionService;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<Notificacion>> listar() {
        return ResponseEntity.ok(
            notificacionService.listar()
        );
    }

    @PutMapping("/{id}/leida")
    public ResponseEntity<Notificacion> marcarComoLeida(
        @PathVariable Long id) {

        return ResponseEntity.ok(
            notificacionService.marcarComoLeida(id)
        );
    }
}
