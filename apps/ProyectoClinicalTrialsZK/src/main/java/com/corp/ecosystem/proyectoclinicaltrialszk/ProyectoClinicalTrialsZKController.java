package com.corp.ecosystem.proyectoclinicaltrialszk;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoclinicaltrialszk")
public class ProyectoClinicalTrialsZKController {
    private final ProyectoClinicalTrialsZKService service;
    public ProyectoClinicalTrialsZKController(ProyectoClinicalTrialsZKService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoClinicalTrialsZKEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoClinicalTrialsZKEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
