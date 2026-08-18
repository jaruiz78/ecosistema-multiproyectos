package com.corp.proyectoneurospatialllm.infrastructure.adapter.in.web;

import com.corp.proyectoneurospatialllm.domain.model.SpatialGeoPromptToken;
import com.corp.proyectoneurospatialllm.domain.port.in.ManageSpatialGeoPromptTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoneurospatialllm")
public class SpatialGeoPromptTokenRestController {

    private final ManageSpatialGeoPromptTokenUseCase useCase;

    public SpatialGeoPromptTokenRestController(ManageSpatialGeoPromptTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SpatialGeoPromptToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SpatialGeoPromptToken created = useCase.createSpatialGeoPromptToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoneurospatialllm/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpatialGeoPromptToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSpatialGeoPromptTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
