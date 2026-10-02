package pe.cibertec.agrodirecto.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.agrodirecto.catalog.entity.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();

    List<Producto> findByAgricultorId(Long agricultorId);

    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
}
