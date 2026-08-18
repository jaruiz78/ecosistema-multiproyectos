package com.corp.proyectorecursivesnarkverifier.infrastructure.adapter.in.web;

import com.corp.proyectorecursivesnarkverifier.domain.model.Halo2ProofAggregationBatchToken;
import com.corp.proyectorecursivesnarkverifier.domain.port.in.ManageHalo2ProofAggregationBatchTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectorecursivesnarkverifier")
public class Halo2ProofAggregationBatchTokenRestController {

    private final ManageHalo2ProofAggregationBatchTokenUseCase useCase;

    public Halo2ProofAggregationBatchTokenRestController(ManageHalo2ProofAggregationBatchTokenUseCase useCase) {
        this.useCase = useCase;
    }

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
