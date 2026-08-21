package com.corp.proyectolandslidedebrisflowhazard.infrastructure.adapter.in.web;

import com.corp.proyectolandslidedebrisflowhazard.domain.model.DebrisFlowRunoutVelocityImpactNode;
import com.corp.proyectolandslidedebrisflowhazard.domain.port.in.ManageDebrisFlowRunoutVelocityImpactNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectolandslidedebrisflowhazard")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DebrisFlowRunoutVelocityImpactNodeRestController {

    private final ManageDebrisFlowRunoutVelocityImpactNodeUseCase useCase;

    public DebrisFlowRunoutVelocityImpactNodeRestController(ManageDebrisFlowRunoutVelocityImpactNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<DebrisFlowRunoutVelocityImpactNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        DebrisFlowRunoutVelocityImpactNode created = useCase.createDebrisFlowRunoutVelocityImpactNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectolandslidedebrisflowhazard/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DebrisFlowRunoutVelocityImpactNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDebrisFlowRunoutVelocityImpactNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
