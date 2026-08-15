package com.corp.ecosystem.proyectosubsurfacegeotwin;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectosubsurfacegeotwin")
public class ProyectoSubSurfaceGeoTwinController {
    private final ProyectoSubSurfaceGeoTwinService service;
    public ProyectoSubSurfaceGeoTwinController(ProyectoSubSurfaceGeoTwinService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoSubSurfaceGeoTwinEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoSubSurfaceGeoTwinEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
