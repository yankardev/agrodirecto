package pe.cibertec.agrodirecto.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.agrodirecto.notification.entity.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
}
