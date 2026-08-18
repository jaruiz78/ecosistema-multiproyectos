package com.corp.proyectobluecarbonoceans.infrastructure.adapter.in.web;

import com.corp.proyectobluecarbonoceans.domain.model.MarinePosidoniaCarbonSink;
import com.corp.proyectobluecarbonoceans.domain.port.in.ManageMarinePosidoniaCarbonSinkUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectobluecarbonoceans")
public class MarinePosidoniaCarbonSinkRestController {

    private final ManageMarinePosidoniaCarbonSinkUseCase useCase;

    public MarinePosidoniaCarbonSinkRestController(ManageMarinePosidoniaCarbonSinkUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MarinePosidoniaCarbonSink> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MarinePosidoniaCarbonSink created = useCase.createMarinePosidoniaCarbonSink(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectobluecarbonoceans/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarinePosidoniaCarbonSink> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMarinePosidoniaCarbonSinkById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
