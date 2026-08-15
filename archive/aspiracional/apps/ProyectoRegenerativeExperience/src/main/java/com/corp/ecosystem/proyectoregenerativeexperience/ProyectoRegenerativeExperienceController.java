package com.corp.ecosystem.proyectoregenerativeexperience;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoregenerativeexperience")
public class ProyectoRegenerativeExperienceController {
    private final ProyectoRegenerativeExperienceService service;
    public ProyectoRegenerativeExperienceController(ProyectoRegenerativeExperienceService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoRegenerativeExperienceEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoRegenerativeExperienceEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
