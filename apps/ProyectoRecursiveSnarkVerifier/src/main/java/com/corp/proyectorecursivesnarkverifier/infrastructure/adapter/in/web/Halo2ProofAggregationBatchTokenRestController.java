package com.corp.proyectorecursivesnarkverifier.infrastructure.adapter.in.web;

import com.corp.proyectorecursivesnarkverifier.domain.model.Halo2ProofAggregationBatchToken;
import com.corp.proyectorecursivesnarkverifier.domain.port.in.ManageHalo2ProofAggregationBatchTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectorecursivesnarkverifier")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class Halo2ProofAggregationBatchTokenRestController {

    private final ManageHalo2ProofAggregationBatchTokenUseCase useCase;

    public Halo2ProofAggregationBatchTokenRestController(ManageHalo2ProofAggregationBatchTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<Halo2ProofAggregationBatchToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        Halo2ProofAggregationBatchToken created = useCase.createHalo2ProofAggregationBatchToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectorecursivesnarkverifier/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Halo2ProofAggregationBatchToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHalo2ProofAggregationBatchTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
