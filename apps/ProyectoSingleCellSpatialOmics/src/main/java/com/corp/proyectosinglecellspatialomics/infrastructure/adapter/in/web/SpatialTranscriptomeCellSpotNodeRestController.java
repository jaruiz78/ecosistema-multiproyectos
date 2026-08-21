package com.corp.proyectosinglecellspatialomics.infrastructure.adapter.in.web;

import com.corp.proyectosinglecellspatialomics.domain.model.SpatialTranscriptomeCellSpotNode;
import com.corp.proyectosinglecellspatialomics.domain.port.in.ManageSpatialTranscriptomeCellSpotNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosinglecellspatialomics")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SpatialTranscriptomeCellSpotNodeRestController {

    private final ManageSpatialTranscriptomeCellSpotNodeUseCase useCase;

    public SpatialTranscriptomeCellSpotNodeRestController(ManageSpatialTranscriptomeCellSpotNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SpatialTranscriptomeCellSpotNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SpatialTranscriptomeCellSpotNode created = useCase.createSpatialTranscriptomeCellSpotNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosinglecellspatialomics/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpatialTranscriptomeCellSpotNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSpatialTranscriptomeCellSpotNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
