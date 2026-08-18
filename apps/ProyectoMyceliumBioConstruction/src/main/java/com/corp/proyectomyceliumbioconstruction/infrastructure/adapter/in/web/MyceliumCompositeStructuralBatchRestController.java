package com.corp.proyectomyceliumbioconstruction.infrastructure.adapter.in.web;

import com.corp.proyectomyceliumbioconstruction.domain.model.MyceliumCompositeStructuralBatch;
import com.corp.proyectomyceliumbioconstruction.domain.port.in.ManageMyceliumCompositeStructuralBatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomyceliumbioconstruction")
public class MyceliumCompositeStructuralBatchRestController {

    private final ManageMyceliumCompositeStructuralBatchUseCase useCase;

    public MyceliumCompositeStructuralBatchRestController(ManageMyceliumCompositeStructuralBatchUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MyceliumCompositeStructuralBatch> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MyceliumCompositeStructuralBatch created = useCase.createMyceliumCompositeStructuralBatch(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomyceliumbioconstruction/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyceliumCompositeStructuralBatch> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMyceliumCompositeStructuralBatchById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
