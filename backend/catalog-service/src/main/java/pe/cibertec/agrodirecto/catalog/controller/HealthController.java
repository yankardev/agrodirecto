package pe.cibertec.agrodirecto.catalog.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalog")
public class HealthController {
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "catalog-service", "status", "UP");
    }
}
