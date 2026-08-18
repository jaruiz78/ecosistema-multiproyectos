package com.corp.proyectointerplanetarydelaytolerantrelay.infrastructure.adapter.in.web;

import com.corp.proyectointerplanetarydelaytolerantrelay.domain.model.DtnBundleCustodyTransferToken;
import com.corp.proyectointerplanetarydelaytolerantrelay.domain.port.in.ManageDtnBundleCustodyTransferTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectointerplanetarydelaytolerantrelay")
public class DtnBundleCustodyTransferTokenRestController {

    private final ManageDtnBundleCustodyTransferTokenUseCase useCase;

    public DtnBundleCustodyTransferTokenRestController(ManageDtnBundleCustodyTransferTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<DtnBundleCustodyTransferToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        DtnBundleCustodyTransferToken created = useCase.createDtnBundleCustodyTransferToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectointerplanetarydelaytolerantrelay/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DtnBundleCustodyTransferToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDtnBundleCustodyTransferTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
