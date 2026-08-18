package com.corp.proyectothresholdblsoraclenetwork.infrastructure.adapter.in.web;

import com.corp.proyectothresholdblsoraclenetwork.domain.model.BlsAggregatedSignatureDataFeedToken;
import com.corp.proyectothresholdblsoraclenetwork.domain.port.in.ManageBlsAggregatedSignatureDataFeedTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectothresholdblsoraclenetwork")
public class BlsAggregatedSignatureDataFeedTokenRestController {

    private final ManageBlsAggregatedSignatureDataFeedTokenUseCase useCase;

    public BlsAggregatedSignatureDataFeedTokenRestController(ManageBlsAggregatedSignatureDataFeedTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<BlsAggregatedSignatureDataFeedToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        BlsAggregatedSignatureDataFeedToken created = useCase.createBlsAggregatedSignatureDataFeedToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectothresholdblsoraclenetwork/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlsAggregatedSignatureDataFeedToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findBlsAggregatedSignatureDataFeedTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
