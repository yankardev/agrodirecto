package pe.cibertec.agrodirecto.notification.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.cibertec.agrodirecto.notification.entity.Notificacion;
import pe.cibertec.agrodirecto.notification.repository.NotificacionRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PedidoEventListener {

    private final NotificacionRepository notificacionRepository;

    @RabbitListener(queues = "pedido.queue")
    public void recibirEvento(String mensaje) {

        Notificacion notificacion = Notificacion.builder()
            .tipo(obtenerTipo(mensaje))
            .mensaje(mensaje)
            .fecha(LocalDateTime.now())
            .leida(false)
            .build();

        notificacionRepository.save(notificacion);

        System.out.println("Notificación guardada: " + mensaje);
    }

    private String obtenerTipo(String mensaje) {

        if (mensaje.startsWith("PEDIDO_CREADO")) {
            return "PEDIDO_CREADO";
        }

        if (mensaje.startsWith("PEDIDO_ACTUALIZADO")) {
            return "PEDIDO_ACTUALIZADO";
        }

        return "OTRO";
    }
}
