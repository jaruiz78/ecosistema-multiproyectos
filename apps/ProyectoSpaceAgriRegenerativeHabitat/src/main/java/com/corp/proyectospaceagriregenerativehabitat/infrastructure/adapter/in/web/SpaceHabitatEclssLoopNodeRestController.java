package com.corp.proyectospaceagriregenerativehabitat.infrastructure.adapter.in.web;

import com.corp.proyectospaceagriregenerativehabitat.domain.model.SpaceHabitatEclssLoopNode;
import com.corp.proyectospaceagriregenerativehabitat.domain.port.in.ManageSpaceHabitatEclssLoopNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectospaceagriregenerativehabitat")
public class SpaceHabitatEclssLoopNodeRestController {

    private final ManageSpaceHabitatEclssLoopNodeUseCase useCase;

    public SpaceHabitatEclssLoopNodeRestController(ManageSpaceHabitatEclssLoopNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SpaceHabitatEclssLoopNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SpaceHabitatEclssLoopNode created = useCase.createSpaceHabitatEclssLoopNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectospaceagriregenerativehabitat/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceHabitatEclssLoopNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSpaceHabitatEclssLoopNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
