package com.corp.proyectocarbonledger.infrastructure.adapter.in.web;

import com.corp.proyectocarbonledger.domain.model.CarbonLedger;
import com.corp.proyectocarbonledger.domain.port.in.ManageCarbonLedgerUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocarbonledger")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_sistemas_distribuidos">FACULTAD_II: Sistemas Distribuidos, Consenso & TLA+</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class CarbonLedgerRestController {

    private final ManageCarbonLedgerUseCase useCase;

    public CarbonLedgerRestController(ManageCarbonLedgerUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CarbonLedger> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CarbonLedger created = useCase.createCarbonLedger(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocarbonledger/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarbonLedger> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCarbonLedgerById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
