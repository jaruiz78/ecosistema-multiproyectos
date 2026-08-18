package com.corp.proyectoquantumkeyescrowsharding.infrastructure.adapter.in.web;

import com.corp.proyectoquantumkeyescrowsharding.domain.model.ShamirPqcKeyShardBundleToken;
import com.corp.proyectoquantumkeyescrowsharding.domain.port.in.ManageShamirPqcKeyShardBundleTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoquantumkeyescrowsharding")
public class ShamirPqcKeyShardBundleTokenRestController {

    private final ManageShamirPqcKeyShardBundleTokenUseCase useCase;

    public ShamirPqcKeyShardBundleTokenRestController(ManageShamirPqcKeyShardBundleTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ShamirPqcKeyShardBundleToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ShamirPqcKeyShardBundleToken created = useCase.createShamirPqcKeyShardBundleToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoquantumkeyescrowsharding/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShamirPqcKeyShardBundleToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findShamirPqcKeyShardBundleTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
