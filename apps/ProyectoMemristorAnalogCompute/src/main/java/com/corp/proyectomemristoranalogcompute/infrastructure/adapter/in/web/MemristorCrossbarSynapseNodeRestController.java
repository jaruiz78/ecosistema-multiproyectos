package com.corp.proyectomemristoranalogcompute.infrastructure.adapter.in.web;

import com.corp.proyectomemristoranalogcompute.domain.model.MemristorCrossbarSynapseNode;
import com.corp.proyectomemristoranalogcompute.domain.port.in.ManageMemristorCrossbarSynapseNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomemristoranalogcompute")
public class MemristorCrossbarSynapseNodeRestController {

    private final ManageMemristorCrossbarSynapseNodeUseCase useCase;

    public MemristorCrossbarSynapseNodeRestController(ManageMemristorCrossbarSynapseNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MemristorCrossbarSynapseNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MemristorCrossbarSynapseNode created = useCase.createMemristorCrossbarSynapseNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomemristoranalogcompute/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemristorCrossbarSynapseNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMemristorCrossbarSynapseNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
