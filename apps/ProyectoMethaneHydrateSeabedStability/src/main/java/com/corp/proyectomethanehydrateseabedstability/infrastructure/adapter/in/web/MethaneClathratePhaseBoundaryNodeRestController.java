package com.corp.proyectomethanehydrateseabedstability.infrastructure.adapter.in.web;

import com.corp.proyectomethanehydrateseabedstability.domain.model.MethaneClathratePhaseBoundaryNode;
import com.corp.proyectomethanehydrateseabedstability.domain.port.in.ManageMethaneClathratePhaseBoundaryNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomethanehydrateseabedstability")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MethaneClathratePhaseBoundaryNodeRestController {

    private final ManageMethaneClathratePhaseBoundaryNodeUseCase useCase;

    public MethaneClathratePhaseBoundaryNodeRestController(ManageMethaneClathratePhaseBoundaryNodeUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MethaneClathratePhaseBoundaryNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MethaneClathratePhaseBoundaryNode created = useCase.createMethaneClathratePhaseBoundaryNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomethanehydrateseabedstability/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MethaneClathratePhaseBoundaryNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMethaneClathratePhaseBoundaryNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
