package com.corp.proyectogroundwater3daquifermodel.infrastructure.adapter.in.web;

import com.corp.proyectogroundwater3daquifermodel.domain.model.AquiferHydraulicHeadPiezometerNode;
import com.corp.proyectogroundwater3daquifermodel.domain.port.in.ManageAquiferHydraulicHeadPiezometerNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectogroundwater3daquifermodel")
public class AquiferHydraulicHeadPiezometerNodeRestController {

    private final ManageAquiferHydraulicHeadPiezometerNodeUseCase useCase;

    public AquiferHydraulicHeadPiezometerNodeRestController(ManageAquiferHydraulicHeadPiezometerNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AquiferHydraulicHeadPiezometerNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AquiferHydraulicHeadPiezometerNode created = useCase.createAquiferHydraulicHeadPiezometerNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectogroundwater3daquifermodel/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AquiferHydraulicHeadPiezometerNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAquiferHydraulicHeadPiezometerNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
