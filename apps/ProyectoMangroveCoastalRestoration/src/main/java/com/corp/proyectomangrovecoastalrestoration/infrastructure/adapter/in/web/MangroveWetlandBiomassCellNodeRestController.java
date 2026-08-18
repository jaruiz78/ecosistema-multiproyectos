package com.corp.proyectomangrovecoastalrestoration.infrastructure.adapter.in.web;

import com.corp.proyectomangrovecoastalrestoration.domain.model.MangroveWetlandBiomassCellNode;
import com.corp.proyectomangrovecoastalrestoration.domain.port.in.ManageMangroveWetlandBiomassCellNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomangrovecoastalrestoration")
public class MangroveWetlandBiomassCellNodeRestController {

    private final ManageMangroveWetlandBiomassCellNodeUseCase useCase;

    public MangroveWetlandBiomassCellNodeRestController(ManageMangroveWetlandBiomassCellNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MangroveWetlandBiomassCellNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MangroveWetlandBiomassCellNode created = useCase.createMangroveWetlandBiomassCellNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomangrovecoastalrestoration/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MangroveWetlandBiomassCellNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMangroveWetlandBiomassCellNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
