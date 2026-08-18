package com.corp.proyectomicrobialelectrosynthesisbiofuel.infrastructure.adapter.in.web;

import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.model.CathodeBiofilmElectronUptakeNode;
import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.port.in.ManageCathodeBiofilmElectronUptakeNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomicrobialelectrosynthesisbiofuel")
public class CathodeBiofilmElectronUptakeNodeRestController {

    private final ManageCathodeBiofilmElectronUptakeNodeUseCase useCase;

    public CathodeBiofilmElectronUptakeNodeRestController(ManageCathodeBiofilmElectronUptakeNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CathodeBiofilmElectronUptakeNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CathodeBiofilmElectronUptakeNode created = useCase.createCathodeBiofilmElectronUptakeNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomicrobialelectrosynthesisbiofuel/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CathodeBiofilmElectronUptakeNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCathodeBiofilmElectronUptakeNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
