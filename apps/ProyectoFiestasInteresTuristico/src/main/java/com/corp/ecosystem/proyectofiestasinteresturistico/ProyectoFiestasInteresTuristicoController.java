package com.corp.ecosystem.proyectofiestasinteresturistico;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectofiestasinteresturistico")
public class ProyectoFiestasInteresTuristicoController {
    private final ProyectoFiestasInteresTuristicoService service;
    public ProyectoFiestasInteresTuristicoController(ProyectoFiestasInteresTuristicoService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoFiestasInteresTuristicoEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoFiestasInteresTuristicoEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
