package com.corp.ecosystem.proyectosmartagrisupplychain;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectosmartagrisupplychain")
public class ProyectoSmartAgriSupplyChainController {
    private final ProyectoSmartAgriSupplyChainService service;
    public ProyectoSmartAgriSupplyChainController(ProyectoSmartAgriSupplyChainService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoSmartAgriSupplyChainEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoSmartAgriSupplyChainEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
