package pe.cibertec.agrodirecto.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.cibertec.agrodirecto.order.dto.ProductoCatalogResponse;

@FeignClient(
    name = "catalog-service",
    url = "${catalog.service.url:http://localhost:8082}"
)
public interface CatalogClient {

    @GetMapping("/api/productos/{id}")
    ProductoCatalogResponse obtenerProducto(@PathVariable Long id);
}
