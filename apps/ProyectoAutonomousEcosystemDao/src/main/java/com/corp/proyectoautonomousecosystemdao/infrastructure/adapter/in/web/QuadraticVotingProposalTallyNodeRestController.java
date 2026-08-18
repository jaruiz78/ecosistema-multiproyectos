package com.corp.proyectoautonomousecosystemdao.infrastructure.adapter.in.web;

import com.corp.proyectoautonomousecosystemdao.domain.model.QuadraticVotingProposalTallyNode;
import com.corp.proyectoautonomousecosystemdao.domain.port.in.ManageQuadraticVotingProposalTallyNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoautonomousecosystemdao")
public class QuadraticVotingProposalTallyNodeRestController {

    private final ManageQuadraticVotingProposalTallyNodeUseCase useCase;

    public QuadraticVotingProposalTallyNodeRestController(ManageQuadraticVotingProposalTallyNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<QuadraticVotingProposalTallyNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        QuadraticVotingProposalTallyNode created = useCase.createQuadraticVotingProposalTallyNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoautonomousecosystemdao/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuadraticVotingProposalTallyNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findQuadraticVotingProposalTallyNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
