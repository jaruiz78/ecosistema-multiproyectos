package com.corp.proyectoagrobiorobotics.infrastructure.adapter.in.web;

import com.corp.proyectoagrobiorobotics.domain.model.AgriRobotSwarmAgent;
import com.corp.proyectoagrobiorobotics.domain.port.in.ManageAgriRobotSwarmAgentUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoagrobiorobotics")
public class AgriRobotSwarmAgentRestController {

    private final ManageAgriRobotSwarmAgentUseCase useCase;

    public AgriRobotSwarmAgentRestController(ManageAgriRobotSwarmAgentUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AgriRobotSwarmAgent> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AgriRobotSwarmAgent created = useCase.createAgriRobotSwarmAgent(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoagrobiorobotics/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgriRobotSwarmAgent> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAgriRobotSwarmAgentById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
