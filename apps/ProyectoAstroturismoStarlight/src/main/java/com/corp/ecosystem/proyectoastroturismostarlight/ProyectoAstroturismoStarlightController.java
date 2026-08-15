package com.corp.ecosystem.proyectoastroturismostarlight;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoastroturismostarlight")
public class ProyectoAstroturismoStarlightController {
    private final ProyectoAstroturismoStarlightService service;
    public ProyectoAstroturismoStarlightController(ProyectoAstroturismoStarlightService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoAstroturismoStarlightEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoAstroturismoStarlightEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
