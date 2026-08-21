package com.corp.proyectosuperconductinggridprotection.infrastructure.adapter.in.web;

import com.corp.proyectosuperconductinggridprotection.domain.model.HtsFaultCurrentLimiterNode;
import com.corp.proyectosuperconductinggridprotection.domain.port.in.ManageHtsFaultCurrentLimiterNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosuperconductinggridprotection")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HtsFaultCurrentLimiterNodeRestController {

    private final ManageHtsFaultCurrentLimiterNodeUseCase useCase;

    public HtsFaultCurrentLimiterNodeRestController(ManageHtsFaultCurrentLimiterNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HtsFaultCurrentLimiterNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HtsFaultCurrentLimiterNode created = useCase.createHtsFaultCurrentLimiterNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosuperconductinggridprotection/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HtsFaultCurrentLimiterNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHtsFaultCurrentLimiterNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
