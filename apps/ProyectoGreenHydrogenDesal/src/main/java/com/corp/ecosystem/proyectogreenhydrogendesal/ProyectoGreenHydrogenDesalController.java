package com.corp.ecosystem.proyectogreenhydrogendesal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectogreenhydrogendesal")
public class ProyectoGreenHydrogenDesalController {
    private final ProyectoGreenHydrogenDesalService service;
    public ProyectoGreenHydrogenDesalController(ProyectoGreenHydrogenDesalService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoGreenHydrogenDesalEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoGreenHydrogenDesalEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
