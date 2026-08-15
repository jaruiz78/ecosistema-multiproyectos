package com.corp.ecosystem.proyectocirculartextiledpp;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectocirculartextiledpp")
public class ProyectoCircularTextileDPPController {
    private final ProyectoCircularTextileDPPService service;
    public ProyectoCircularTextileDPPController(ProyectoCircularTextileDPPService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoCircularTextileDPPEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoCircularTextileDPPEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
