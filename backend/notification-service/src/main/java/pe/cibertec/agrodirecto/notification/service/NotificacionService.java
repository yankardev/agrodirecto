package pe.cibertec.agrodirecto.notification.service;

import pe.cibertec.agrodirecto.notification.entity.Notificacion;

import java.util.List;

public interface NotificacionService {

    List<Notificacion> listar();

    Notificacion marcarComoLeida(Long id);
}
