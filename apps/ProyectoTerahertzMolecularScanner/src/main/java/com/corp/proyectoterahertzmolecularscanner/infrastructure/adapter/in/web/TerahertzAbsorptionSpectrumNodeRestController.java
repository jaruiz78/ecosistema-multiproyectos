package com.corp.proyectoterahertzmolecularscanner.infrastructure.adapter.in.web;

import com.corp.proyectoterahertzmolecularscanner.domain.model.TerahertzAbsorptionSpectrumNode;
import com.corp.proyectoterahertzmolecularscanner.domain.port.in.ManageTerahertzAbsorptionSpectrumNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoterahertzmolecularscanner")
public class TerahertzAbsorptionSpectrumNodeRestController {

    private final ManageTerahertzAbsorptionSpectrumNodeUseCase useCase;

    public TerahertzAbsorptionSpectrumNodeRestController(ManageTerahertzAbsorptionSpectrumNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<TerahertzAbsorptionSpectrumNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        TerahertzAbsorptionSpectrumNode created = useCase.createTerahertzAbsorptionSpectrumNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoterahertzmolecularscanner/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerahertzAbsorptionSpectrumNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findTerahertzAbsorptionSpectrumNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
