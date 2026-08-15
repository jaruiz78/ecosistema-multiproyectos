package com.corp.ecosystem.proyectorutassenderismogr;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectorutassenderismogr")
public class ProyectoRutasSenderismoGRController {
    private final ProyectoRutasSenderismoGRService service;
    public ProyectoRutasSenderismoGRController(ProyectoRutasSenderismoGRService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoRutasSenderismoGREntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoRutasSenderismoGREntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
