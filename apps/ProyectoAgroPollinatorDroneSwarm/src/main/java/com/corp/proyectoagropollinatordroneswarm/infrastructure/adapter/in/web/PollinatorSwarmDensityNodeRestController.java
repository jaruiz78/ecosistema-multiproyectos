package com.corp.proyectoagropollinatordroneswarm.infrastructure.adapter.in.web;

import com.corp.proyectoagropollinatordroneswarm.domain.model.PollinatorSwarmDensityNode;
import com.corp.proyectoagropollinatordroneswarm.domain.port.in.ManagePollinatorSwarmDensityNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoagropollinatordroneswarm")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PollinatorSwarmDensityNodeRestController {

    private final ManagePollinatorSwarmDensityNodeUseCase useCase;

    public PollinatorSwarmDensityNodeRestController(ManagePollinatorSwarmDensityNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PollinatorSwarmDensityNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PollinatorSwarmDensityNode created = useCase.createPollinatorSwarmDensityNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoagropollinatordroneswarm/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollinatorSwarmDensityNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPollinatorSwarmDensityNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
