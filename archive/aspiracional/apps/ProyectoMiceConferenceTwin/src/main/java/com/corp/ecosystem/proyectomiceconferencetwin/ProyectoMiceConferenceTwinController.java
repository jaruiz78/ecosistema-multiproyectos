package com.corp.ecosystem.proyectomiceconferencetwin;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectomiceconferencetwin")
public class ProyectoMiceConferenceTwinController {
    private final ProyectoMiceConferenceTwinService service;
    public ProyectoMiceConferenceTwinController(ProyectoMiceConferenceTwinService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoMiceConferenceTwinEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoMiceConferenceTwinEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
