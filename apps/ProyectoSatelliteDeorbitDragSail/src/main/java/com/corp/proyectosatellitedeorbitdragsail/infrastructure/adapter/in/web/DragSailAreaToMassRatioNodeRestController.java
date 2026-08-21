package com.corp.proyectosatellitedeorbitdragsail.infrastructure.adapter.in.web;

import com.corp.proyectosatellitedeorbitdragsail.domain.model.DragSailAreaToMassRatioNode;
import com.corp.proyectosatellitedeorbitdragsail.domain.port.in.ManageDragSailAreaToMassRatioNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosatellitedeorbitdragsail")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DragSailAreaToMassRatioNodeRestController {

    private final ManageDragSailAreaToMassRatioNodeUseCase useCase;

    public DragSailAreaToMassRatioNodeRestController(ManageDragSailAreaToMassRatioNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<DragSailAreaToMassRatioNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        DragSailAreaToMassRatioNode created = useCase.createDragSailAreaToMassRatioNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosatellitedeorbitdragsail/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DragSailAreaToMassRatioNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDragSailAreaToMassRatioNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
