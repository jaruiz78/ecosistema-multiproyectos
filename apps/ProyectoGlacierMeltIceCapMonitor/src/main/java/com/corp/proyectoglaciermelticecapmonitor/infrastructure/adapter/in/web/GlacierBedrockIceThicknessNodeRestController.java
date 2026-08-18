package com.corp.proyectoglaciermelticecapmonitor.infrastructure.adapter.in.web;

import com.corp.proyectoglaciermelticecapmonitor.domain.model.GlacierBedrockIceThicknessNode;
import com.corp.proyectoglaciermelticecapmonitor.domain.port.in.ManageGlacierBedrockIceThicknessNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoglaciermelticecapmonitor")
public class GlacierBedrockIceThicknessNodeRestController {

    private final ManageGlacierBedrockIceThicknessNodeUseCase useCase;

    public GlacierBedrockIceThicknessNodeRestController(ManageGlacierBedrockIceThicknessNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<GlacierBedrockIceThicknessNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        GlacierBedrockIceThicknessNode created = useCase.createGlacierBedrockIceThicknessNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoglaciermelticecapmonitor/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlacierBedrockIceThicknessNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findGlacierBedrockIceThicknessNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
