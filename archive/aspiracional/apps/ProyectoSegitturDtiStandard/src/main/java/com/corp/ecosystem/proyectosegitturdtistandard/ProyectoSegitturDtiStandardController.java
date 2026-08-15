package com.corp.ecosystem.proyectosegitturdtistandard;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectosegitturdtistandard")
public class ProyectoSegitturDtiStandardController {
    private final ProyectoSegitturDtiStandardService service;
    public ProyectoSegitturDtiStandardController(ProyectoSegitturDtiStandardService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoSegitturDtiStandardEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoSegitturDtiStandardEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
