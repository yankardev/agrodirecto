package pe.cibertec.agrodirecto.order.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pe.cibertec.agrodirecto.order.config.RabbitMQConfig;

@Component
@RequiredArgsConstructor
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicar(String mensaje) {

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.ROUTING_KEY,
            mensaje
        );
    }
}
