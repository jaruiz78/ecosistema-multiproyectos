package com.corp.ecosystem.proyectodroneairspaceuspace;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectodroneairspaceuspace")
public class ProyectoDroneAirspaceUSpaceController {
    private final ProyectoDroneAirspaceUSpaceService service;
    public ProyectoDroneAirspaceUSpaceController(ProyectoDroneAirspaceUSpaceService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoDroneAirspaceUSpaceEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoDroneAirspaceUSpaceEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
