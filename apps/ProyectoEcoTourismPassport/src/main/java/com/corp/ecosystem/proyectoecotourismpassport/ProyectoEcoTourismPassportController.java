package com.corp.ecosystem.proyectoecotourismpassport;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoecotourismpassport")
public class ProyectoEcoTourismPassportController {
    private final ProyectoEcoTourismPassportService service;
    public ProyectoEcoTourismPassportController(ProyectoEcoTourismPassportService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoEcoTourismPassportEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoEcoTourismPassportEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
