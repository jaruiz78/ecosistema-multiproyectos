package com.corp.proyectocoastalclifferosiondefense.infrastructure.adapter.in.web;

import com.corp.proyectocoastalclifferosiondefense.domain.model.CliffRetreatErosionRateNode;
import com.corp.proyectocoastalclifferosiondefense.domain.port.in.ManageCliffRetreatErosionRateNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocoastalclifferosiondefense")
public class CliffRetreatErosionRateNodeRestController {

    private final ManageCliffRetreatErosionRateNodeUseCase useCase;

    public CliffRetreatErosionRateNodeRestController(ManageCliffRetreatErosionRateNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CliffRetreatErosionRateNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CliffRetreatErosionRateNode created = useCase.createCliffRetreatErosionRateNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocoastalclifferosiondefense/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CliffRetreatErosionRateNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCliffRetreatErosionRateNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
