package pe.cibertec.agrodirecto.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.agrodirecto.order.entity.DetalleCarrito;

import java.util.List;
import java.util.Optional;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {

    List<DetalleCarrito> findByCarritoId(Long carritoId);

    Optional<DetalleCarrito> findByCarritoIdAndProductoId(
        Long carritoId,
        Long productoId
    );

    void deleteByCarritoId(Long carritoId);
}
