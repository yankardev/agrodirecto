package pe.cibertec.agrodirecto.catalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.cibertec.agrodirecto.catalog.dto.ProductoRequest;
import pe.cibertec.agrodirecto.catalog.dto.ProductoResponse;
import pe.cibertec.agrodirecto.catalog.entity.Categoria;
import pe.cibertec.agrodirecto.catalog.entity.Producto;
import pe.cibertec.agrodirecto.catalog.repository.CategoriaRepository;
import pe.cibertec.agrodirecto.catalog.repository.ProductoRepository;
import pe.cibertec.agrodirecto.catalog.service.ProductoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public ProductoResponse crear(ProductoRequest request) {

        Categoria categoria = obtenerCategoria(request.getCategoriaId());

        Producto producto = Producto.builder()
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .precio(request.getPrecio())
            .stock(request.getStock())
            .unidadMedida(request.getUnidadMedida())
            .imagenUrl(request.getImagenUrl())
            .agricultorId(request.getAgricultorId())
            .categoria(categoria)
            .activo(true)
            .build();

        producto = productoRepository.save(producto);

        return convertir(producto);
    }

    @Override
    public ProductoResponse obtenerPorId(Long id) {
        return convertir(obtenerProducto(id));
    }

    @Override
    public List<ProductoResponse> listarActivos() {

        return productoRepository.findByActivoTrue()
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Override
    public List<ProductoResponse> listarPorAgricultor(Long agricultorId) {

        return productoRepository.findByAgricultorId(agricultorId)
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Override
    public List<ProductoResponse> listarPorCategoria(Long categoriaId) {

        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId)
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Override
    public ProductoResponse actualizar(Long id, ProductoRequest request) {

        Producto producto = obtenerProducto(id);
        Categoria categoria = obtenerCategoria(request.getCategoriaId());

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setAgricultorId(request.getAgricultorId());
        producto.setCategoria(categoria);

        producto = productoRepository.save(producto);

        return convertir(producto);
    }

    @Override
    public void eliminar(Long id) {

        Producto producto = obtenerProducto(id);

        producto.setActivo(false);

        productoRepository.save(producto);
    }

    private Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Producto no encontrado"));
    }

    private Categoria obtenerCategoria(Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Categoría no encontrada"));
    }

    private ProductoResponse convertir(Producto producto) {

        return new ProductoResponse(
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getPrecio(),
            producto.getStock(),
            producto.getUnidadMedida(),
            producto.getImagenUrl(),
            producto.getActivo(),
            producto.getAgricultorId(),
            producto.getCategoria().getId(),
            producto.getCategoria().getNombre()
        );
    }
}
