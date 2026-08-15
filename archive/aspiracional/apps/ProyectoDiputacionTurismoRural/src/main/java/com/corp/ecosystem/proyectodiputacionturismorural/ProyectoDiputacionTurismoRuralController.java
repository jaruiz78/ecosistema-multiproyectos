package com.corp.ecosystem.proyectodiputacionturismorural;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectodiputacionturismorural")
public class ProyectoDiputacionTurismoRuralController {
    private final ProyectoDiputacionTurismoRuralService service;
    public ProyectoDiputacionTurismoRuralController(ProyectoDiputacionTurismoRuralService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoDiputacionTurismoRuralEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoDiputacionTurismoRuralEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
