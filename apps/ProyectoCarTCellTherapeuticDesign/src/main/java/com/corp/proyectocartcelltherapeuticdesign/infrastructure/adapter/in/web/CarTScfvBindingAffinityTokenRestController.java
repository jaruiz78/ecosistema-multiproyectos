package com.corp.proyectocartcelltherapeuticdesign.infrastructure.adapter.in.web;

import com.corp.proyectocartcelltherapeuticdesign.domain.model.CarTScfvBindingAffinityToken;
import com.corp.proyectocartcelltherapeuticdesign.domain.port.in.ManageCarTScfvBindingAffinityTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocartcelltherapeuticdesign")
public class CarTScfvBindingAffinityTokenRestController {

    private final ManageCarTScfvBindingAffinityTokenUseCase useCase;

    public CarTScfvBindingAffinityTokenRestController(ManageCarTScfvBindingAffinityTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CarTScfvBindingAffinityToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CarTScfvBindingAffinityToken created = useCase.createCarTScfvBindingAffinityToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocartcelltherapeuticdesign/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarTScfvBindingAffinityToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCarTScfvBindingAffinityTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
