package com.corp.proyectobioagritrace.infrastructure.adapter.in.web;

import com.corp.proyectobioagritrace.domain.model.BioAgriGenomicSample;
import com.corp.proyectobioagritrace.domain.port.in.ManageBioAgriGenomicSampleUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectobioagritrace")
public class BioAgriGenomicSampleRestController {

    private final ManageBioAgriGenomicSampleUseCase useCase;

    public BioAgriGenomicSampleRestController(ManageBioAgriGenomicSampleUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<BioAgriGenomicSample> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        BioAgriGenomicSample created = useCase.createBioAgriGenomicSample(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectobioagritrace/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BioAgriGenomicSample> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findBioAgriGenomicSampleById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
