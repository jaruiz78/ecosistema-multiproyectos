package com.corp.ecosystem.proyectoporttwinautonomous;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoporttwinautonomous")
public class ProyectoPortTwinAutonomousController {
    private final ProyectoPortTwinAutonomousService service;
    public ProyectoPortTwinAutonomousController(ProyectoPortTwinAutonomousService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoPortTwinAutonomousEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoPortTwinAutonomousEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
