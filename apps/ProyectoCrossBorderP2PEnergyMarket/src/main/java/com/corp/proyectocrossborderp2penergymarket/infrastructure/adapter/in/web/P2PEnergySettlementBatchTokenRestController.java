package com.corp.proyectocrossborderp2penergymarket.infrastructure.adapter.in.web;

import com.corp.proyectocrossborderp2penergymarket.domain.model.P2PEnergySettlementBatchToken;
import com.corp.proyectocrossborderp2penergymarket.domain.port.in.ManageP2PEnergySettlementBatchTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocrossborderp2penergymarket")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class P2PEnergySettlementBatchTokenRestController {

    private final ManageP2PEnergySettlementBatchTokenUseCase useCase;

    public P2PEnergySettlementBatchTokenRestController(ManageP2PEnergySettlementBatchTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<P2PEnergySettlementBatchToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        P2PEnergySettlementBatchToken created = useCase.createP2PEnergySettlementBatchToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocrossborderp2penergymarket/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<P2PEnergySettlementBatchToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findP2PEnergySettlementBatchTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
