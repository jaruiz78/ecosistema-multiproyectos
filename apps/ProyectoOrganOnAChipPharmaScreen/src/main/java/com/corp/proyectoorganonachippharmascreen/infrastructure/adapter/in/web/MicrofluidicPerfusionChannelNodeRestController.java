package com.corp.proyectoorganonachippharmascreen.infrastructure.adapter.in.web;

import com.corp.proyectoorganonachippharmascreen.domain.model.MicrofluidicPerfusionChannelNode;
import com.corp.proyectoorganonachippharmascreen.domain.port.in.ManageMicrofluidicPerfusionChannelNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoorganonachippharmascreen")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MicrofluidicPerfusionChannelNodeRestController {

    private final ManageMicrofluidicPerfusionChannelNodeUseCase useCase;

    public MicrofluidicPerfusionChannelNodeRestController(ManageMicrofluidicPerfusionChannelNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MicrofluidicPerfusionChannelNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MicrofluidicPerfusionChannelNode created = useCase.createMicrofluidicPerfusionChannelNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoorganonachippharmascreen/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MicrofluidicPerfusionChannelNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMicrofluidicPerfusionChannelNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
