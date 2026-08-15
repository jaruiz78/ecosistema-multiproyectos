package com.corp.ecosystem.proyectovpp;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectovpp")
public class ProyectoVPPController {
    private final ProyectoVPPService service;
    public ProyectoVPPController(ProyectoVPPService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoVPPEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoVPPEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
