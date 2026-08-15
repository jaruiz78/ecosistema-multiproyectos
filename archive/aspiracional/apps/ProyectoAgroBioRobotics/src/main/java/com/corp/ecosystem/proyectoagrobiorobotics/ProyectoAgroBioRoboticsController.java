package com.corp.ecosystem.proyectoagrobiorobotics;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectoagrobiorobotics")
public class ProyectoAgroBioRoboticsController {
    private final ProyectoAgroBioRoboticsService service;
    public ProyectoAgroBioRoboticsController(ProyectoAgroBioRoboticsService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoAgroBioRoboticsEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoAgroBioRoboticsEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
