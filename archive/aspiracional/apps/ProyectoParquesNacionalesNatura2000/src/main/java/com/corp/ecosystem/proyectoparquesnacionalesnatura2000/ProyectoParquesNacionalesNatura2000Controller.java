package com.corp.ecosystem.proyectoparquesnacionalesnatura2000;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoparquesnacionalesnatura2000")
public class ProyectoParquesNacionalesNatura2000Controller {
    private final ProyectoParquesNacionalesNatura2000Service service;
    public ProyectoParquesNacionalesNatura2000Controller(ProyectoParquesNacionalesNatura2000Service service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoParquesNacionalesNatura2000Entity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoParquesNacionalesNatura2000Entity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
