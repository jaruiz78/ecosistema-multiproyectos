package com.corp.ecosystem.proyectosmartwaterdesal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectosmartwaterdesal")
public class ProyectoSmartWaterDesalController {
    private final ProyectoSmartWaterDesalService service;
    public ProyectoSmartWaterDesalController(ProyectoSmartWaterDesalService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoSmartWaterDesalEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoSmartWaterDesalEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
