package com.corp.ecosystem.proyectosmartstreetlightingv2g;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectosmartstreetlightingv2g")
public class ProyectoSmartStreetLightingV2GController {
    private final ProyectoSmartStreetLightingV2GService service;
    public ProyectoSmartStreetLightingV2GController(ProyectoSmartStreetLightingV2GService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoSmartStreetLightingV2GEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoSmartStreetLightingV2GEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
