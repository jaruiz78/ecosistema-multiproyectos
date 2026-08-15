package com.corp.ecosystem.proyectosoilbiocarbontwin;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectosoilbiocarbontwin")
public class ProyectoSoilBioCarbonTwinController {
    private final ProyectoSoilBioCarbonTwinService service;
    public ProyectoSoilBioCarbonTwinController(ProyectoSoilBioCarbonTwinService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoSoilBioCarbonTwinEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoSoilBioCarbonTwinEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
