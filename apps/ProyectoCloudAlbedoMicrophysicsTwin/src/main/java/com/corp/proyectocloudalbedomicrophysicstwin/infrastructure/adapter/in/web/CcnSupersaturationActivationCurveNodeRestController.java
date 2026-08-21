package com.corp.proyectocloudalbedomicrophysicstwin.infrastructure.adapter.in.web;

import com.corp.proyectocloudalbedomicrophysicstwin.domain.model.CcnSupersaturationActivationCurveNode;
import com.corp.proyectocloudalbedomicrophysicstwin.domain.port.in.ManageCcnSupersaturationActivationCurveNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocloudalbedomicrophysicstwin")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CcnSupersaturationActivationCurveNodeRestController {

    private final ManageCcnSupersaturationActivationCurveNodeUseCase useCase;

    public CcnSupersaturationActivationCurveNodeRestController(ManageCcnSupersaturationActivationCurveNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CcnSupersaturationActivationCurveNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CcnSupersaturationActivationCurveNode created = useCase.createCcnSupersaturationActivationCurveNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocloudalbedomicrophysicstwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CcnSupersaturationActivationCurveNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCcnSupersaturationActivationCurveNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
