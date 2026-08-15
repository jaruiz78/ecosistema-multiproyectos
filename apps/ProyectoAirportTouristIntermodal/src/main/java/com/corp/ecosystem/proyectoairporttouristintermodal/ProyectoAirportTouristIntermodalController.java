package com.corp.ecosystem.proyectoairporttouristintermodal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoairporttouristintermodal")
public class ProyectoAirportTouristIntermodalController {
    private final ProyectoAirportTouristIntermodalService service;
    public ProyectoAirportTouristIntermodalController(ProyectoAirportTouristIntermodalService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoAirportTouristIntermodalEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoAirportTouristIntermodalEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
