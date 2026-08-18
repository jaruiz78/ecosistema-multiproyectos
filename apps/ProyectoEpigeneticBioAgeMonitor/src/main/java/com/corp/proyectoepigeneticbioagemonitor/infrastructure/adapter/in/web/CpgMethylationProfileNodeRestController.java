package com.corp.proyectoepigeneticbioagemonitor.infrastructure.adapter.in.web;

import com.corp.proyectoepigeneticbioagemonitor.domain.model.CpgMethylationProfileNode;
import com.corp.proyectoepigeneticbioagemonitor.domain.port.in.ManageCpgMethylationProfileNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoepigeneticbioagemonitor")
public class CpgMethylationProfileNodeRestController {

    private final ManageCpgMethylationProfileNodeUseCase useCase;

    public CpgMethylationProfileNodeRestController(ManageCpgMethylationProfileNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CpgMethylationProfileNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CpgMethylationProfileNode created = useCase.createCpgMethylationProfileNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoepigeneticbioagemonitor/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpgMethylationProfileNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCpgMethylationProfileNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
