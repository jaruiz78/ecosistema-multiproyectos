package com.corp.proyectospaceisltelecommunications.infrastructure.adapter.in.web;

import com.corp.proyectospaceisltelecommunications.domain.model.SpaceSatelliteIslNode;
import com.corp.proyectospaceisltelecommunications.domain.port.in.ManageSpaceSatelliteIslNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectospaceisltelecommunications")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SpaceSatelliteIslNodeRestController {

    private final ManageSpaceSatelliteIslNodeUseCase useCase;

    public SpaceSatelliteIslNodeRestController(ManageSpaceSatelliteIslNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SpaceSatelliteIslNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SpaceSatelliteIslNode created = useCase.createSpaceSatelliteIslNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectospaceisltelecommunications/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceSatelliteIslNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSpaceSatelliteIslNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
