package com.corp.proyectotaxcomplianceledger.infrastructure.adapter.in.web;

import com.corp.proyectotaxcomplianceledger.domain.model.TaxComplianceLedger;
import com.corp.proyectotaxcomplianceledger.domain.port.in.ManageTaxComplianceLedgerUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectotaxcomplianceledger")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_sistemas_distribuidos">FACULTAD_II: Sistemas Distribuidos, Consenso & TLA+</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class TaxComplianceLedgerRestController {

    private final ManageTaxComplianceLedgerUseCase useCase;

    public TaxComplianceLedgerRestController(ManageTaxComplianceLedgerUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<TaxComplianceLedger> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        TaxComplianceLedger created = useCase.createTaxComplianceLedger(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectotaxcomplianceledger/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaxComplianceLedger> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findTaxComplianceLedgerById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
