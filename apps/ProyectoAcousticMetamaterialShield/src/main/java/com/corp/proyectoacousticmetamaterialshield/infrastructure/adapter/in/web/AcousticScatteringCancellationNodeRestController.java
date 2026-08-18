package com.corp.proyectoacousticmetamaterialshield.infrastructure.adapter.in.web;

import com.corp.proyectoacousticmetamaterialshield.domain.model.AcousticScatteringCancellationNode;
import com.corp.proyectoacousticmetamaterialshield.domain.port.in.ManageAcousticScatteringCancellationNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoacousticmetamaterialshield")
public class AcousticScatteringCancellationNodeRestController {

    private final ManageAcousticScatteringCancellationNodeUseCase useCase;

    public AcousticScatteringCancellationNodeRestController(ManageAcousticScatteringCancellationNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AcousticScatteringCancellationNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AcousticScatteringCancellationNode created = useCase.createAcousticScatteringCancellationNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoacousticmetamaterialshield/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcousticScatteringCancellationNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAcousticScatteringCancellationNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
