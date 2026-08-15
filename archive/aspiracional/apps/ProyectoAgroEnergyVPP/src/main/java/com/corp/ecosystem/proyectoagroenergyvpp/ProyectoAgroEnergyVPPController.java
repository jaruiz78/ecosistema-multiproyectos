package com.corp.ecosystem.proyectoagroenergyvpp;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoagroenergyvpp")
public class ProyectoAgroEnergyVPPController {
    private final ProyectoAgroEnergyVPPService service;
    public ProyectoAgroEnergyVPPController(ProyectoAgroEnergyVPPService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoAgroEnergyVPPEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoAgroEnergyVPPEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
