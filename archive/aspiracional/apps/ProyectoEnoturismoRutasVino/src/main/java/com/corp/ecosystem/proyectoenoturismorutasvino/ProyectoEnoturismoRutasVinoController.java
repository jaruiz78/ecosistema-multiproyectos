package com.corp.ecosystem.proyectoenoturismorutasvino;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoenoturismorutasvino")
public class ProyectoEnoturismoRutasVinoController {
    private final ProyectoEnoturismoRutasVinoService service;
    public ProyectoEnoturismoRutasVinoController(ProyectoEnoturismoRutasVinoService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoEnoturismoRutasVinoEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoEnoturismoRutasVinoEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
