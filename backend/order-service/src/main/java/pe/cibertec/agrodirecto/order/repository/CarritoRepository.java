package pe.cibertec.agrodirecto.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.agrodirecto.order.entity.Carrito;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByClienteIdAndActivoTrue(Long clienteId);
}
