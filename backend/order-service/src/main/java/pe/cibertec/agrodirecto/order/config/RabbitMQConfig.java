package pe.cibertec.agrodirecto.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "agrodirecto.exchange";
    public static final String QUEUE = "pedido.queue";
    public static final String ROUTING_KEY = "pedido.evento";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue pedidoQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding binding(
        Queue pedidoQueue,
        TopicExchange exchange) {

        return BindingBuilder
            .bind(pedidoQueue)
            .to(exchange)
            .with(ROUTING_KEY);
    }
}
