package com.corp.proyectoquantumdotinfraredcamera.infrastructure.adapter.in.web;

import com.corp.proyectoquantumdotinfraredcamera.domain.model.QdipInfraredPixelMatrixBatch;
import com.corp.proyectoquantumdotinfraredcamera.domain.port.in.ManageQdipInfraredPixelMatrixBatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoquantumdotinfraredcamera")
public class QdipInfraredPixelMatrixBatchRestController {

    private final ManageQdipInfraredPixelMatrixBatchUseCase useCase;

    public QdipInfraredPixelMatrixBatchRestController(ManageQdipInfraredPixelMatrixBatchUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<QdipInfraredPixelMatrixBatch> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        QdipInfraredPixelMatrixBatch created = useCase.createQdipInfraredPixelMatrixBatch(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoquantumdotinfraredcamera/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QdipInfraredPixelMatrixBatch> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findQdipInfraredPixelMatrixBatchById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
