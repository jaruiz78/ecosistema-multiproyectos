package com.corp.ecosystem.proyectohoteltwinrevpar;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectohoteltwinrevpar")
public class ProyectoHotelTwinRevPARController {
    private final ProyectoHotelTwinRevPARService service;
    public ProyectoHotelTwinRevPARController(ProyectoHotelTwinRevPARService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoHotelTwinRevPAREntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoHotelTwinRevPAREntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
