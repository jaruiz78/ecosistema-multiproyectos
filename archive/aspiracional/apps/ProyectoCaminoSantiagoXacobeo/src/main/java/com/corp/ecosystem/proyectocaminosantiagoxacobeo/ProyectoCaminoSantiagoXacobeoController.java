package com.corp.ecosystem.proyectocaminosantiagoxacobeo;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectocaminosantiagoxacobeo")
public class ProyectoCaminoSantiagoXacobeoController {
    private final ProyectoCaminoSantiagoXacobeoService service;
    public ProyectoCaminoSantiagoXacobeoController(ProyectoCaminoSantiagoXacobeoService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoCaminoSantiagoXacobeoEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoCaminoSantiagoXacobeoEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
