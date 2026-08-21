package com.corp.proyectopermafrostthawmonitor.infrastructure.adapter.in.web;

import com.corp.proyectopermafrostthawmonitor.domain.model.PermafrostThawDepthSubsidenceNode;
import com.corp.proyectopermafrostthawmonitor.domain.port.in.ManagePermafrostThawDepthSubsidenceNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectopermafrostthawmonitor")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PermafrostThawDepthSubsidenceNodeRestController {

    private final ManagePermafrostThawDepthSubsidenceNodeUseCase useCase;

    public PermafrostThawDepthSubsidenceNodeRestController(ManagePermafrostThawDepthSubsidenceNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PermafrostThawDepthSubsidenceNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PermafrostThawDepthSubsidenceNode created = useCase.createPermafrostThawDepthSubsidenceNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectopermafrostthawmonitor/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermafrostThawDepthSubsidenceNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPermafrostThawDepthSubsidenceNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
