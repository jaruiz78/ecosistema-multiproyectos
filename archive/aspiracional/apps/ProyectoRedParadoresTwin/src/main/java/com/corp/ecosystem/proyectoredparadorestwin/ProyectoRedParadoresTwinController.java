package com.corp.ecosystem.proyectoredparadorestwin;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoredparadorestwin")
public class ProyectoRedParadoresTwinController {
    private final ProyectoRedParadoresTwinService service;
    public ProyectoRedParadoresTwinController(ProyectoRedParadoresTwinService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoRedParadoresTwinEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoRedParadoresTwinEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
