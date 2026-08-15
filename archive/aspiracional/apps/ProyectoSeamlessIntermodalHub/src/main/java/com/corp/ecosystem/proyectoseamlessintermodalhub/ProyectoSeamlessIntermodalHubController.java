package com.corp.ecosystem.proyectoseamlessintermodalhub;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoseamlessintermodalhub")
public class ProyectoSeamlessIntermodalHubController {
    private final ProyectoSeamlessIntermodalHubService service;
    public ProyectoSeamlessIntermodalHubController(ProyectoSeamlessIntermodalHubService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoSeamlessIntermodalHubEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoSeamlessIntermodalHubEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
