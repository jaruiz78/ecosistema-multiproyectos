package com.corp.proyectoxenotransplantationimmunetwin.infrastructure.adapter.in.web;

import com.corp.proyectoxenotransplantationimmunetwin.domain.model.XenoOrganCompatibilityScoreToken;
import com.corp.proyectoxenotransplantationimmunetwin.domain.port.in.ManageXenoOrganCompatibilityScoreTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoxenotransplantationimmunetwin")
public class XenoOrganCompatibilityScoreTokenRestController {

    private final ManageXenoOrganCompatibilityScoreTokenUseCase useCase;

    public XenoOrganCompatibilityScoreTokenRestController(ManageXenoOrganCompatibilityScoreTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<XenoOrganCompatibilityScoreToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        XenoOrganCompatibilityScoreToken created = useCase.createXenoOrganCompatibilityScoreToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoxenotransplantationimmunetwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<XenoOrganCompatibilityScoreToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findXenoOrganCompatibilityScoreTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
