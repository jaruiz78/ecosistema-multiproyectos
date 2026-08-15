package com.corp.ecosystem.proyectopresatwinscada;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectopresatwinscada")
public class ProyectoPresaTwinSCADAController {
    private final ProyectoPresaTwinSCADAService service;
    public ProyectoPresaTwinSCADAController(ProyectoPresaTwinSCADAService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoPresaTwinSCADAEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoPresaTwinSCADAEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
