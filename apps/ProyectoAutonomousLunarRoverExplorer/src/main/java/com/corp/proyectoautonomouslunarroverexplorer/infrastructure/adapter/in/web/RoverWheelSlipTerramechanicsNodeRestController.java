package com.corp.proyectoautonomouslunarroverexplorer.infrastructure.adapter.in.web;

import com.corp.proyectoautonomouslunarroverexplorer.domain.model.RoverWheelSlipTerramechanicsNode;
import com.corp.proyectoautonomouslunarroverexplorer.domain.port.in.ManageRoverWheelSlipTerramechanicsNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoautonomouslunarroverexplorer")
public class RoverWheelSlipTerramechanicsNodeRestController {

    private final ManageRoverWheelSlipTerramechanicsNodeUseCase useCase;

    public RoverWheelSlipTerramechanicsNodeRestController(ManageRoverWheelSlipTerramechanicsNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<RoverWheelSlipTerramechanicsNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        RoverWheelSlipTerramechanicsNode created = useCase.createRoverWheelSlipTerramechanicsNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoautonomouslunarroverexplorer/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoverWheelSlipTerramechanicsNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findRoverWheelSlipTerramechanicsNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
