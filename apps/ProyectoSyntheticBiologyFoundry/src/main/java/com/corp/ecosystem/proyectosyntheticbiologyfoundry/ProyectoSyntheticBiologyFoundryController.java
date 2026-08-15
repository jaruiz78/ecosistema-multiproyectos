package com.corp.ecosystem.proyectosyntheticbiologyfoundry;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectosyntheticbiologyfoundry")
public class ProyectoSyntheticBiologyFoundryController {
    private final ProyectoSyntheticBiologyFoundryService service;
    public ProyectoSyntheticBiologyFoundryController(ProyectoSyntheticBiologyFoundryService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoSyntheticBiologyFoundryEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoSyntheticBiologyFoundryEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
