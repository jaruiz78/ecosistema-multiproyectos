package com.corp.ecosystem.proyectosmartdestinationdti;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectosmartdestinationdti")
public class ProyectoSmartDestinationDTIController {
    private final ProyectoSmartDestinationDTIService service;
    public ProyectoSmartDestinationDTIController(ProyectoSmartDestinationDTIService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoSmartDestinationDTIEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoSmartDestinationDTIEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
