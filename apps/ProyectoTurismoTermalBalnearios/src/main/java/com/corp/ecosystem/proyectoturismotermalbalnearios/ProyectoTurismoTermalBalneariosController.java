package com.corp.ecosystem.proyectoturismotermalbalnearios;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoturismotermalbalnearios")
public class ProyectoTurismoTermalBalneariosController {
    private final ProyectoTurismoTermalBalneariosService service;
    public ProyectoTurismoTermalBalneariosController(ProyectoTurismoTermalBalneariosService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoTurismoTermalBalneariosEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoTurismoTermalBalneariosEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
