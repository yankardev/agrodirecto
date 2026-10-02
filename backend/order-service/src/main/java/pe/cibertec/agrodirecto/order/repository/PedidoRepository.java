package pe.cibertec.agrodirecto.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.agrodirecto.order.entity.Pedido;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByAgricultorId(Long agricultorId);
}
