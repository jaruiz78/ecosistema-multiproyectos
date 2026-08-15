package com.corp.ecosystem.proyectoagua;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoagua")
public class ProyectoAguaController {
    private final ProyectoAguaService service;
    public ProyectoAguaController(ProyectoAguaService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoAguaEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoAguaEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
