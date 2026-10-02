package pe.cibertec.agrodirecto.catalog.service;

import pe.cibertec.agrodirecto.catalog.dto.CategoriaRequest;
import pe.cibertec.agrodirecto.catalog.dto.CategoriaResponse;

import java.util.List;

public interface CategoriaService {

    CategoriaResponse crear(CategoriaRequest request);

    CategoriaResponse obtenerPorId(Long id);

    List<CategoriaResponse> listar();

    CategoriaResponse actualizar(Long id, CategoriaRequest request);

    void eliminar(Long id);
}
