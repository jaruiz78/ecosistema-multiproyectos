package com.corp.ecosystem.proyectocriticalmineralsmrv;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectocriticalmineralsmrv")
public class ProyectoCriticalMineralsMRVController {
    private final ProyectoCriticalMineralsMRVService service;
    public ProyectoCriticalMineralsMRVController(ProyectoCriticalMineralsMRVService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoCriticalMineralsMRVEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoCriticalMineralsMRVEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
