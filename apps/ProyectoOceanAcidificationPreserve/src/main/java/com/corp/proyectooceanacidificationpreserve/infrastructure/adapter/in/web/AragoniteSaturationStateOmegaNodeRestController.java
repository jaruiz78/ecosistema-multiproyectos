package com.corp.proyectooceanacidificationpreserve.infrastructure.adapter.in.web;

import com.corp.proyectooceanacidificationpreserve.domain.model.AragoniteSaturationStateOmegaNode;
import com.corp.proyectooceanacidificationpreserve.domain.port.in.ManageAragoniteSaturationStateOmegaNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectooceanacidificationpreserve")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AragoniteSaturationStateOmegaNodeRestController {

    private final ManageAragoniteSaturationStateOmegaNodeUseCase useCase;

    public AragoniteSaturationStateOmegaNodeRestController(ManageAragoniteSaturationStateOmegaNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AragoniteSaturationStateOmegaNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AragoniteSaturationStateOmegaNode created = useCase.createAragoniteSaturationStateOmegaNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectooceanacidificationpreserve/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AragoniteSaturationStateOmegaNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAragoniteSaturationStateOmegaNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
