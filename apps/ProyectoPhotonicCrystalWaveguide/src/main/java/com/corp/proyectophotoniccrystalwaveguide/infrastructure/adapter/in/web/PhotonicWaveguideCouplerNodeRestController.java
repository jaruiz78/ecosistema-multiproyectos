package com.corp.proyectophotoniccrystalwaveguide.infrastructure.adapter.in.web;

import com.corp.proyectophotoniccrystalwaveguide.domain.model.PhotonicWaveguideCouplerNode;
import com.corp.proyectophotoniccrystalwaveguide.domain.port.in.ManagePhotonicWaveguideCouplerNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectophotoniccrystalwaveguide")
public class PhotonicWaveguideCouplerNodeRestController {

    private final ManagePhotonicWaveguideCouplerNodeUseCase useCase;

    public PhotonicWaveguideCouplerNodeRestController(ManagePhotonicWaveguideCouplerNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PhotonicWaveguideCouplerNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PhotonicWaveguideCouplerNode created = useCase.createPhotonicWaveguideCouplerNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectophotoniccrystalwaveguide/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotonicWaveguideCouplerNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPhotonicWaveguideCouplerNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
