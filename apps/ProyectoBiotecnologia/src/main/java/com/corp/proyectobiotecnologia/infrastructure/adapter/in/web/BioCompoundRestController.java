package com.corp.proyectobiotecnologia.infrastructure.adapter.in.web;

import com.corp.proyectobiotecnologia.domain.model.BioCompound;
import com.corp.proyectobiotecnologia.domain.port.in.ManageBioCompoundUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectobiotecnologia")
public class BioCompoundRestController {

    private final ManageBioCompoundUseCase useCase;

    public BioCompoundRestController(ManageBioCompoundUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<BioCompound> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        BioCompound created = useCase.createBioCompound(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectobiotecnologia/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BioCompound> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findBioCompoundById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
