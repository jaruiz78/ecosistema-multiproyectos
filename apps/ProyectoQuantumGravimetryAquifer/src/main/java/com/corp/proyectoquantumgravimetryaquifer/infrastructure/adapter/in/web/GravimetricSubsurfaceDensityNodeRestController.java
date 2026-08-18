package com.corp.proyectoquantumgravimetryaquifer.infrastructure.adapter.in.web;

import com.corp.proyectoquantumgravimetryaquifer.domain.model.GravimetricSubsurfaceDensityNode;
import com.corp.proyectoquantumgravimetryaquifer.domain.port.in.ManageGravimetricSubsurfaceDensityNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoquantumgravimetryaquifer")
public class GravimetricSubsurfaceDensityNodeRestController {

    private final ManageGravimetricSubsurfaceDensityNodeUseCase useCase;

    public GravimetricSubsurfaceDensityNodeRestController(ManageGravimetricSubsurfaceDensityNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<GravimetricSubsurfaceDensityNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        GravimetricSubsurfaceDensityNode created = useCase.createGravimetricSubsurfaceDensityNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoquantumgravimetryaquifer/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GravimetricSubsurfaceDensityNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findGravimetricSubsurfaceDensityNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
