package com.corp.proyectocircularbiomassbiorefinery.infrastructure.adapter.in.web;

import com.corp.proyectocircularbiomassbiorefinery.domain.model.BiomassFeedstockBatch;
import com.corp.proyectocircularbiomassbiorefinery.domain.port.in.ManageBiomassFeedstockBatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocircularbiomassbiorefinery")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class BiomassFeedstockBatchRestController {

    private final ManageBiomassFeedstockBatchUseCase useCase;

    public BiomassFeedstockBatchRestController(ManageBiomassFeedstockBatchUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<BiomassFeedstockBatch> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        BiomassFeedstockBatch created = useCase.createBiomassFeedstockBatch(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocircularbiomassbiorefinery/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BiomassFeedstockBatch> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findBiomassFeedstockBatchById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
