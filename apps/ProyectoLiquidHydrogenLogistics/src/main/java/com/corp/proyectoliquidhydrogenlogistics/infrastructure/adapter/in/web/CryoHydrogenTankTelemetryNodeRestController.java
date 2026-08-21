package com.corp.proyectoliquidhydrogenlogistics.infrastructure.adapter.in.web;

import com.corp.proyectoliquidhydrogenlogistics.domain.model.CryoHydrogenTankTelemetryNode;
import com.corp.proyectoliquidhydrogenlogistics.domain.port.in.ManageCryoHydrogenTankTelemetryNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoliquidhydrogenlogistics")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CryoHydrogenTankTelemetryNodeRestController {

    private final ManageCryoHydrogenTankTelemetryNodeUseCase useCase;

    public CryoHydrogenTankTelemetryNodeRestController(ManageCryoHydrogenTankTelemetryNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CryoHydrogenTankTelemetryNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CryoHydrogenTankTelemetryNode created = useCase.createCryoHydrogenTankTelemetryNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoliquidhydrogenlogistics/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CryoHydrogenTankTelemetryNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCryoHydrogenTankTelemetryNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
