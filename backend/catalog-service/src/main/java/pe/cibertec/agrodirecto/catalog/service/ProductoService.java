package pe.cibertec.agrodirecto.catalog.service;

import pe.cibertec.agrodirecto.catalog.dto.ProductoRequest;
import pe.cibertec.agrodirecto.catalog.dto.ProductoResponse;

import java.util.List;

public interface ProductoService {

    ProductoResponse crear(ProductoRequest request);

    ProductoResponse obtenerPorId(Long id);

    List<ProductoResponse> listarActivos();

    List<ProductoResponse> listarPorAgricultor(Long agricultorId);

    List<ProductoResponse> listarPorCategoria(Long categoriaId);

    ProductoResponse actualizar(Long id, ProductoRequest request);

    void eliminar(Long id);
}
