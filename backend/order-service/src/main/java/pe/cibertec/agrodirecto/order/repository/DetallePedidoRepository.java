package pe.cibertec.agrodirecto.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.agrodirecto.order.entity.DetallePedido;

import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoId(Long pedidoId);
}
