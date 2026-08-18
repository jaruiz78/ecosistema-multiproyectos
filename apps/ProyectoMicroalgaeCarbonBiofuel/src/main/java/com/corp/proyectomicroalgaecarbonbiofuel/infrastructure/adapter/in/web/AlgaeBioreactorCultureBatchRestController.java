package com.corp.proyectomicroalgaecarbonbiofuel.infrastructure.adapter.in.web;

import com.corp.proyectomicroalgaecarbonbiofuel.domain.model.AlgaeBioreactorCultureBatch;
import com.corp.proyectomicroalgaecarbonbiofuel.domain.port.in.ManageAlgaeBioreactorCultureBatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomicroalgaecarbonbiofuel")
public class AlgaeBioreactorCultureBatchRestController {

    private final ManageAlgaeBioreactorCultureBatchUseCase useCase;

    public AlgaeBioreactorCultureBatchRestController(ManageAlgaeBioreactorCultureBatchUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AlgaeBioreactorCultureBatch> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AlgaeBioreactorCultureBatch created = useCase.createAlgaeBioreactorCultureBatch(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomicroalgaecarbonbiofuel/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlgaeBioreactorCultureBatch> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAlgaeBioreactorCultureBatchById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
