package com.corp.proyectohydrodynamicdamfloodrouting.infrastructure.adapter.in.web;

import com.corp.proyectohydrodynamicdamfloodrouting.domain.model.FloodInundationGridCellNode;
import com.corp.proyectohydrodynamicdamfloodrouting.domain.port.in.ManageFloodInundationGridCellNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectohydrodynamicdamfloodrouting")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class FloodInundationGridCellNodeRestController {

    private final ManageFloodInundationGridCellNodeUseCase useCase;

    public FloodInundationGridCellNodeRestController(ManageFloodInundationGridCellNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<FloodInundationGridCellNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        FloodInundationGridCellNode created = useCase.createFloodInundationGridCellNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectohydrodynamicdamfloodrouting/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FloodInundationGridCellNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findFloodInundationGridCellNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
