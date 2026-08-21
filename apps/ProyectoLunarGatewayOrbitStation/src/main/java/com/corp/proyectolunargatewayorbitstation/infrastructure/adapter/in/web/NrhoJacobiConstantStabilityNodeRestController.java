package com.corp.proyectolunargatewayorbitstation.infrastructure.adapter.in.web;

import com.corp.proyectolunargatewayorbitstation.domain.model.NrhoJacobiConstantStabilityNode;
import com.corp.proyectolunargatewayorbitstation.domain.port.in.ManageNrhoJacobiConstantStabilityNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectolunargatewayorbitstation")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class NrhoJacobiConstantStabilityNodeRestController {

    private final ManageNrhoJacobiConstantStabilityNodeUseCase useCase;

    public NrhoJacobiConstantStabilityNodeRestController(ManageNrhoJacobiConstantStabilityNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<NrhoJacobiConstantStabilityNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        NrhoJacobiConstantStabilityNode created = useCase.createNrhoJacobiConstantStabilityNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectolunargatewayorbitstation/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NrhoJacobiConstantStabilityNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findNrhoJacobiConstantStabilityNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
