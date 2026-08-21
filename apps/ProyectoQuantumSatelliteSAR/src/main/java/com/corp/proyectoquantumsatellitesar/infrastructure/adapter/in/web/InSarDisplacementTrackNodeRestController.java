package com.corp.proyectoquantumsatellitesar.infrastructure.adapter.in.web;

import com.corp.proyectoquantumsatellitesar.domain.model.InSarDisplacementTrackNode;
import com.corp.proyectoquantumsatellitesar.domain.port.in.ManageInSarDisplacementTrackNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoquantumsatellitesar")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InSarDisplacementTrackNodeRestController {

    private final ManageInSarDisplacementTrackNodeUseCase useCase;

    public InSarDisplacementTrackNodeRestController(ManageInSarDisplacementTrackNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<InSarDisplacementTrackNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        InSarDisplacementTrackNode created = useCase.createInSarDisplacementTrackNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoquantumsatellitesar/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InSarDisplacementTrackNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findInSarDisplacementTrackNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
