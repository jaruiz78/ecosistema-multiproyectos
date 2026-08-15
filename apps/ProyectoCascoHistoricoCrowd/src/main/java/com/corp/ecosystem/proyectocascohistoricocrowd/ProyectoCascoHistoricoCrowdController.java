package com.corp.ecosystem.proyectocascohistoricocrowd;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectocascohistoricocrowd")
public class ProyectoCascoHistoricoCrowdController {
    private final ProyectoCascoHistoricoCrowdService service;
    public ProyectoCascoHistoricoCrowdController(ProyectoCascoHistoricoCrowdService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoCascoHistoricoCrowdEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoCascoHistoricoCrowdEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
