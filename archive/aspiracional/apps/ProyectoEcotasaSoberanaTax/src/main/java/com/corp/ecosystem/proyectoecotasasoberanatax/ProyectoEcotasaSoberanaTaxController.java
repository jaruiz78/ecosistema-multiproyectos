package com.corp.ecosystem.proyectoecotasasoberanatax;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoecotasasoberanatax")
public class ProyectoEcotasaSoberanaTaxController {
    private final ProyectoEcotasaSoberanaTaxService service;
    public ProyectoEcotasaSoberanaTaxController(ProyectoEcotasaSoberanaTaxService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoEcotasaSoberanaTaxEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoEcotasaSoberanaTaxEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
