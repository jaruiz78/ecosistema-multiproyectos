package com.corp.ecosystem.proyectoquantumsatellitesync;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoquantumsatellitesync")
public class ProyectoQuantumSatelliteSyncController {
    private final ProyectoQuantumSatelliteSyncService service;
    public ProyectoQuantumSatelliteSyncController(ProyectoQuantumSatelliteSyncService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoQuantumSatelliteSyncEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoQuantumSatelliteSyncEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
