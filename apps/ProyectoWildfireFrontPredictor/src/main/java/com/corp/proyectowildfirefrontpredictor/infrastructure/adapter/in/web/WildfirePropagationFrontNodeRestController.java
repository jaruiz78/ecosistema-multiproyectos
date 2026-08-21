package com.corp.proyectowildfirefrontpredictor.infrastructure.adapter.in.web;

import com.corp.proyectowildfirefrontpredictor.domain.model.WildfirePropagationFrontNode;
import com.corp.proyectowildfirefrontpredictor.domain.port.in.ManageWildfirePropagationFrontNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectowildfirefrontpredictor")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class WildfirePropagationFrontNodeRestController {

    private final ManageWildfirePropagationFrontNodeUseCase useCase;

    public WildfirePropagationFrontNodeRestController(ManageWildfirePropagationFrontNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<WildfirePropagationFrontNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        WildfirePropagationFrontNode created = useCase.createWildfirePropagationFrontNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectowildfirefrontpredictor/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WildfirePropagationFrontNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findWildfirePropagationFrontNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
