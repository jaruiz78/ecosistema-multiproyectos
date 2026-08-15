package com.corp.ecosystem.proyectoairlineinterlinebaggage;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoairlineinterlinebaggage")
public class ProyectoAirlineInterlineBaggageController {
    private final ProyectoAirlineInterlineBaggageService service;
    public ProyectoAirlineInterlineBaggageController(ProyectoAirlineInterlineBaggageService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoAirlineInterlineBaggageEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoAirlineInterlineBaggageEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
