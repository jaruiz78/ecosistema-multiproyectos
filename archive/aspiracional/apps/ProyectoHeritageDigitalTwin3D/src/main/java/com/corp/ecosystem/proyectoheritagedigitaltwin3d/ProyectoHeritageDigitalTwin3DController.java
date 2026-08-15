package com.corp.ecosystem.proyectoheritagedigitaltwin3d;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoheritagedigitaltwin3d")
public class ProyectoHeritageDigitalTwin3DController {
    private final ProyectoHeritageDigitalTwin3DService service;
    public ProyectoHeritageDigitalTwin3DController(ProyectoHeritageDigitalTwin3DService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoHeritageDigitalTwin3DEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoHeritageDigitalTwin3DEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
