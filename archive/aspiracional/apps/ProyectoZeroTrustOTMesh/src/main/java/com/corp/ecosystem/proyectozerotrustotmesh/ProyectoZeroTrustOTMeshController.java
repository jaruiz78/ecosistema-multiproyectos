package com.corp.ecosystem.proyectozerotrustotmesh;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectozerotrustotmesh")
public class ProyectoZeroTrustOTMeshController {
    private final ProyectoZeroTrustOTMeshService service;
    public ProyectoZeroTrustOTMeshController(ProyectoZeroTrustOTMeshService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoZeroTrustOTMeshEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoZeroTrustOTMeshEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
