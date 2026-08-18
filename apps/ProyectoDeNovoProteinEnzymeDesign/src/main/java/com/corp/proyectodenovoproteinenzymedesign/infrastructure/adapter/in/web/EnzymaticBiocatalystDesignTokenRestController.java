package com.corp.proyectodenovoproteinenzymedesign.infrastructure.adapter.in.web;

import com.corp.proyectodenovoproteinenzymedesign.domain.model.EnzymaticBiocatalystDesignToken;
import com.corp.proyectodenovoproteinenzymedesign.domain.port.in.ManageEnzymaticBiocatalystDesignTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectodenovoproteinenzymedesign")
public class EnzymaticBiocatalystDesignTokenRestController {

    private final ManageEnzymaticBiocatalystDesignTokenUseCase useCase;

    public EnzymaticBiocatalystDesignTokenRestController(ManageEnzymaticBiocatalystDesignTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<EnzymaticBiocatalystDesignToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        EnzymaticBiocatalystDesignToken created = useCase.createEnzymaticBiocatalystDesignToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectodenovoproteinenzymedesign/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnzymaticBiocatalystDesignToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findEnzymaticBiocatalystDesignTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
