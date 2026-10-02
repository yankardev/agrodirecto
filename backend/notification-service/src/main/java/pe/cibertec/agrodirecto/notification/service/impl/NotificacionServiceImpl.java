package pe.cibertec.agrodirecto.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.cibertec.agrodirecto.notification.entity.Notificacion;
import pe.cibertec.agrodirecto.notification.repository.NotificacionRepository;
import pe.cibertec.agrodirecto.notification.service.NotificacionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    @Override
    public List<Notificacion> listar() {
        return notificacionRepository.findAll();
    }

    @Override
    public Notificacion marcarComoLeida(Long id) {

        Notificacion notificacion = notificacionRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Notificación no encontrada"));

        notificacion.setLeida(true);

        return notificacionRepository.save(notificacion);
    }
}
