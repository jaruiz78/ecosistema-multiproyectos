package com.corp.proyectopiezoelectrickineticharvester.infrastructure.adapter.in.web;

import com.corp.proyectopiezoelectrickineticharvester.domain.model.PiezoelectricCantileverBeamNode;
import com.corp.proyectopiezoelectrickineticharvester.domain.port.in.ManagePiezoelectricCantileverBeamNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectopiezoelectrickineticharvester")
public class PiezoelectricCantileverBeamNodeRestController {

    private final ManagePiezoelectricCantileverBeamNodeUseCase useCase;

    public PiezoelectricCantileverBeamNodeRestController(ManagePiezoelectricCantileverBeamNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PiezoelectricCantileverBeamNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PiezoelectricCantileverBeamNode created = useCase.createPiezoelectricCantileverBeamNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectopiezoelectrickineticharvester/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PiezoelectricCantileverBeamNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPiezoelectricCantileverBeamNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
