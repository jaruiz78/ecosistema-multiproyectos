package com.corp.ecosystem.proyectopharmacoldchain;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectopharmacoldchain")
public class ProyectoPharmaColdChainController {
    private final ProyectoPharmaColdChainService service;
    public ProyectoPharmaColdChainController(ProyectoPharmaColdChainService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoPharmaColdChainEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoPharmaColdChainEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
