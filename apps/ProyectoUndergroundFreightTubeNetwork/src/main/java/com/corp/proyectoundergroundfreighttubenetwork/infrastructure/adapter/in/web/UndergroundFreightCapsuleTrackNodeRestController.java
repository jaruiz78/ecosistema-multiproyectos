package com.corp.proyectoundergroundfreighttubenetwork.infrastructure.adapter.in.web;

import com.corp.proyectoundergroundfreighttubenetwork.domain.model.UndergroundFreightCapsuleTrackNode;
import com.corp.proyectoundergroundfreighttubenetwork.domain.port.in.ManageUndergroundFreightCapsuleTrackNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoundergroundfreighttubenetwork")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class UndergroundFreightCapsuleTrackNodeRestController {

    private final ManageUndergroundFreightCapsuleTrackNodeUseCase useCase;

    public UndergroundFreightCapsuleTrackNodeRestController(ManageUndergroundFreightCapsuleTrackNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<UndergroundFreightCapsuleTrackNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        UndergroundFreightCapsuleTrackNode created = useCase.createUndergroundFreightCapsuleTrackNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoundergroundfreighttubenetwork/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UndergroundFreightCapsuleTrackNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findUndergroundFreightCapsuleTrackNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
