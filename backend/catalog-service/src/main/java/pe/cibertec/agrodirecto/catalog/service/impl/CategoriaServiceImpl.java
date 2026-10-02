package pe.cibertec.agrodirecto.catalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.cibertec.agrodirecto.catalog.dto.CategoriaRequest;
import pe.cibertec.agrodirecto.catalog.dto.CategoriaResponse;
import pe.cibertec.agrodirecto.catalog.entity.Categoria;
import pe.cibertec.agrodirecto.catalog.repository.CategoriaRepository;
import pe.cibertec.agrodirecto.catalog.service.CategoriaService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public CategoriaResponse crear(CategoriaRequest request) {

        if (categoriaRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RuntimeException("La categoría ya existe");
        }

        Categoria categoria = Categoria.builder()
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .activo(true)
            .build();

        categoria = categoriaRepository.save(categoria);

        return convertir(categoria);
    }

    @Override
    public CategoriaResponse obtenerPorId(Long id) {
        Categoria categoria = buscarCategoria(id);
        return convertir(categoria);
    }

    @Override
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll()
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Override
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {

        Categoria categoria = buscarCategoria(id);

        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        categoria = categoriaRepository.save(categoria);

        return convertir(categoria);
    }

    @Override
    public void eliminar(Long id) {

        Categoria categoria = buscarCategoria(id);

        categoria.setActivo(false);

        categoriaRepository.save(categoria);
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Categoría no encontrada"));
    }

    private CategoriaResponse convertir(Categoria categoria) {

        return new CategoriaResponse(
            categoria.getId(),
            categoria.getNombre(),
            categoria.getDescripcion(),
            categoria.getActivo()
        );
    }
}
