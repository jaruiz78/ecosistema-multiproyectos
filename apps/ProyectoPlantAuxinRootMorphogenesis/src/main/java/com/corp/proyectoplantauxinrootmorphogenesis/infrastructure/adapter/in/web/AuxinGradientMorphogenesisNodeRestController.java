package com.corp.proyectoplantauxinrootmorphogenesis.infrastructure.adapter.in.web;

import com.corp.proyectoplantauxinrootmorphogenesis.domain.model.AuxinGradientMorphogenesisNode;
import com.corp.proyectoplantauxinrootmorphogenesis.domain.port.in.ManageAuxinGradientMorphogenesisNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoplantauxinrootmorphogenesis")
public class AuxinGradientMorphogenesisNodeRestController {

    private final ManageAuxinGradientMorphogenesisNodeUseCase useCase;

    public AuxinGradientMorphogenesisNodeRestController(ManageAuxinGradientMorphogenesisNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AuxinGradientMorphogenesisNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AuxinGradientMorphogenesisNode created = useCase.createAuxinGradientMorphogenesisNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoplantauxinrootmorphogenesis/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuxinGradientMorphogenesisNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAuxinGradientMorphogenesisNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
