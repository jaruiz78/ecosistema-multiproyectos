package com.corp.ecosystem.proyectotaxcomplianceledger;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proyectotaxcomplianceledger")
public class ProyectoTaxComplianceLedgerController {
    private final ProyectoTaxComplianceLedgerService service;
    public ProyectoTaxComplianceLedgerController(ProyectoTaxComplianceLedgerService service) {
        this.service = service;
    }
    
    @PostMapping("/process")
    public ResponseEntity<ProyectoTaxComplianceLedgerEntity> process(@RequestParam String tenantId, @RequestParam double metrics) {
        return ResponseEntity.ok(service.processRealBusinessLogic(tenantId, metrics));
    }
    
    @GetMapping("/metrics")
    public ResponseEntity<List<ProyectoTaxComplianceLedgerEntity>> getMetrics(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getMetrics(tenantId));
    }
}
