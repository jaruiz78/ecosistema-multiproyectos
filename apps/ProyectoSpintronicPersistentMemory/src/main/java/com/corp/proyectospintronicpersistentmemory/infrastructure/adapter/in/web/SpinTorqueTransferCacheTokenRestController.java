package com.corp.proyectospintronicpersistentmemory.infrastructure.adapter.in.web;

import com.corp.proyectospintronicpersistentmemory.domain.model.SpinTorqueTransferCacheToken;
import com.corp.proyectospintronicpersistentmemory.domain.port.in.ManageSpinTorqueTransferCacheTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectospintronicpersistentmemory")
public class SpinTorqueTransferCacheTokenRestController {

    private final ManageSpinTorqueTransferCacheTokenUseCase useCase;

    public SpinTorqueTransferCacheTokenRestController(ManageSpinTorqueTransferCacheTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SpinTorqueTransferCacheToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SpinTorqueTransferCacheToken created = useCase.createSpinTorqueTransferCacheToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectospintronicpersistentmemory/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpinTorqueTransferCacheToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSpinTorqueTransferCacheTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
