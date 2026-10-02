package pe.cibertec.agrodirecto.catalog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.agrodirecto.catalog.dto.CategoriaRequest;
import pe.cibertec.agrodirecto.catalog.dto.CategoriaResponse;
import pe.cibertec.agrodirecto.catalog.service.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(
        @RequestBody CategoriaRequest request) {

        return ResponseEntity.ok(
            categoriaService.crear(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {

        return ResponseEntity.ok(
            categoriaService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerPorId(
        @PathVariable Long id) {

        return ResponseEntity.ok(
            categoriaService.obtenerPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(
        @PathVariable Long id,
        @RequestBody CategoriaRequest request) {

        return ResponseEntity.ok(
            categoriaService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @PathVariable Long id) {

        categoriaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
