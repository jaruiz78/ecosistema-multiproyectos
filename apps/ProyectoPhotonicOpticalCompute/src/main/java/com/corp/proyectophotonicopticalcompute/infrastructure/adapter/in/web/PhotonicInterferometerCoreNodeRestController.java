package com.corp.proyectophotonicopticalcompute.infrastructure.adapter.in.web;

import com.corp.proyectophotonicopticalcompute.domain.model.PhotonicInterferometerCoreNode;
import com.corp.proyectophotonicopticalcompute.domain.port.in.ManagePhotonicInterferometerCoreNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectophotonicopticalcompute")
public class PhotonicInterferometerCoreNodeRestController {

    private final ManagePhotonicInterferometerCoreNodeUseCase useCase;

    public PhotonicInterferometerCoreNodeRestController(ManagePhotonicInterferometerCoreNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PhotonicInterferometerCoreNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PhotonicInterferometerCoreNode created = useCase.createPhotonicInterferometerCoreNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectophotonicopticalcompute/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotonicInterferometerCoreNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPhotonicInterferometerCoreNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
