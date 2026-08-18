package com.corp.proyectowildfiresmokehealthalert.infrastructure.adapter.in.web;

import com.corp.proyectowildfiresmokehealthalert.domain.model.SmokePlumePm25ConcentrationGridNode;
import com.corp.proyectowildfiresmokehealthalert.domain.port.in.ManageSmokePlumePm25ConcentrationGridNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectowildfiresmokehealthalert")
public class SmokePlumePm25ConcentrationGridNodeRestController {

    private final ManageSmokePlumePm25ConcentrationGridNodeUseCase useCase;

    public SmokePlumePm25ConcentrationGridNodeRestController(ManageSmokePlumePm25ConcentrationGridNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SmokePlumePm25ConcentrationGridNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SmokePlumePm25ConcentrationGridNode created = useCase.createSmokePlumePm25ConcentrationGridNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectowildfiresmokehealthalert/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SmokePlumePm25ConcentrationGridNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSmokePlumePm25ConcentrationGridNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
