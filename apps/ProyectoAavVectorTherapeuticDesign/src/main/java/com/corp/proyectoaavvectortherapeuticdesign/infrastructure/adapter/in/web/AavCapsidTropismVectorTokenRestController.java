package com.corp.proyectoaavvectortherapeuticdesign.infrastructure.adapter.in.web;

import com.corp.proyectoaavvectortherapeuticdesign.domain.model.AavCapsidTropismVectorToken;
import com.corp.proyectoaavvectortherapeuticdesign.domain.port.in.ManageAavCapsidTropismVectorTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoaavvectortherapeuticdesign")
public class AavCapsidTropismVectorTokenRestController {

    private final ManageAavCapsidTropismVectorTokenUseCase useCase;

    public AavCapsidTropismVectorTokenRestController(ManageAavCapsidTropismVectorTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AavCapsidTropismVectorToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AavCapsidTropismVectorToken created = useCase.createAavCapsidTropismVectorToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoaavvectortherapeuticdesign/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AavCapsidTropismVectorToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAavCapsidTropismVectorTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
