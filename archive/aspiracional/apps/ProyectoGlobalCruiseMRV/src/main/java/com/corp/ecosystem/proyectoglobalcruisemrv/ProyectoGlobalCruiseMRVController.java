package com.corp.ecosystem.proyectoglobalcruisemrv;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoglobalcruisemrv")
public class ProyectoGlobalCruiseMRVController {
    private final ProyectoGlobalCruiseMRVService service;
    public ProyectoGlobalCruiseMRVController(ProyectoGlobalCruiseMRVService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoGlobalCruiseMRVEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoGlobalCruiseMRVEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
