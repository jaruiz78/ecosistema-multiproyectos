package com.corp.ecosystem.proyectoindustrialmicrogridmpc;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoindustrialmicrogridmpc")
public class ProyectoIndustrialMicrogridMPCController {
    private final ProyectoIndustrialMicrogridMPCService service;
    public ProyectoIndustrialMicrogridMPCController(ProyectoIndustrialMicrogridMPCService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoIndustrialMicrogridMPCEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoIndustrialMicrogridMPCEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
