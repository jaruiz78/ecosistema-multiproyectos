package com.corp.ecosystem.proyectoplayasinteligentescostas;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoplayasinteligentescostas")
public class ProyectoPlayasInteligentesCostasController {
    private final ProyectoPlayasInteligentesCostasService service;
    public ProyectoPlayasInteligentesCostasController(ProyectoPlayasInteligentesCostasService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoPlayasInteligentesCostasEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoPlayasInteligentesCostasEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
