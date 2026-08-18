package com.corp.proyectocrisprbaseeditingtherapy.infrastructure.adapter.in.web;

import com.corp.proyectocrisprbaseeditingtherapy.domain.model.BaseEditorTransitionEfficiencyToken;
import com.corp.proyectocrisprbaseeditingtherapy.domain.port.in.ManageBaseEditorTransitionEfficiencyTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocrisprbaseeditingtherapy")
public class BaseEditorTransitionEfficiencyTokenRestController {

    private final ManageBaseEditorTransitionEfficiencyTokenUseCase useCase;

    public BaseEditorTransitionEfficiencyTokenRestController(ManageBaseEditorTransitionEfficiencyTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<BaseEditorTransitionEfficiencyToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        BaseEditorTransitionEfficiencyToken created = useCase.createBaseEditorTransitionEfficiencyToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocrisprbaseeditingtherapy/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseEditorTransitionEfficiencyToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findBaseEditorTransitionEfficiencyTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
