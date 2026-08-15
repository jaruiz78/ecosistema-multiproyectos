package com.corp.ecosystem.proyectobioagritrace;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectobioagritrace")
public class ProyectoBioAgriTraceController {
    private final ProyectoBioAgriTraceService service;
    public ProyectoBioAgriTraceController(ProyectoBioAgriTraceService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoBioAgriTraceEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoBioAgriTraceEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
