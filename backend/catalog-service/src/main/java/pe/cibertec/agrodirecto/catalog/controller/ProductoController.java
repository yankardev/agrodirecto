package pe.cibertec.agrodirecto.catalog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.agrodirecto.catalog.dto.ProductoRequest;
import pe.cibertec.agrodirecto.catalog.dto.ProductoResponse;
import pe.cibertec.agrodirecto.catalog.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(
        @RequestBody ProductoRequest request) {

        return ResponseEntity.ok(
            productoService.crear(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarActivos() {

        return ResponseEntity.ok(
            productoService.listarActivos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(
        @PathVariable Long id) {

        return ResponseEntity.ok(
            productoService.obtenerPorId(id)
        );
    }

    @GetMapping("/agricultor/{agricultorId}")
    public ResponseEntity<List<ProductoResponse>> listarPorAgricultor(
        @PathVariable Long agricultorId) {

        return ResponseEntity.ok(
            productoService.listarPorAgricultor(agricultorId)
        );
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponse>> listarPorCategoria(
        @PathVariable Long categoriaId) {

        return ResponseEntity.ok(
            productoService.listarPorCategoria(categoriaId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
        @PathVariable Long id,
        @RequestBody ProductoRequest request) {

        return ResponseEntity.ok(
            productoService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @PathVariable Long id) {

        productoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
