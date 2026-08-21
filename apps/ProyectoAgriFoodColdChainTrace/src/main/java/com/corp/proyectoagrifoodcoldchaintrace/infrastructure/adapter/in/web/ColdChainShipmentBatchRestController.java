package com.corp.proyectoagrifoodcoldchaintrace.infrastructure.adapter.in.web;

import com.corp.proyectoagrifoodcoldchaintrace.domain.model.ColdChainShipmentBatch;
import com.corp.proyectoagrifoodcoldchaintrace.domain.port.in.ManageColdChainShipmentBatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoagrifoodcoldchaintrace")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ColdChainShipmentBatchRestController {

    private final ManageColdChainShipmentBatchUseCase useCase;

    public ColdChainShipmentBatchRestController(ManageColdChainShipmentBatchUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ColdChainShipmentBatch> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ColdChainShipmentBatch created = useCase.createColdChainShipmentBatch(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoagrifoodcoldchaintrace/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColdChainShipmentBatch> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findColdChainShipmentBatchById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
