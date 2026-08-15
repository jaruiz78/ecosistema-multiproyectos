package com.corp.ecosystem.proyectoquantumresistantrwa;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoquantumresistantrwa")
public class ProyectoQuantumResistantRWAController {
    private final ProyectoQuantumResistantRWAService service;
    public ProyectoQuantumResistantRWAController(ProyectoQuantumResistantRWAService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoQuantumResistantRWAEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoQuantumResistantRWAEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
