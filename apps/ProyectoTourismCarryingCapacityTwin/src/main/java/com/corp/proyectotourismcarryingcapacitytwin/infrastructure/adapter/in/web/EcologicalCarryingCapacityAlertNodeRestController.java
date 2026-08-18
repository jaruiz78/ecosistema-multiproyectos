package com.corp.proyectotourismcarryingcapacitytwin.infrastructure.adapter.in.web;

import com.corp.proyectotourismcarryingcapacitytwin.domain.model.EcologicalCarryingCapacityAlertNode;
import com.corp.proyectotourismcarryingcapacitytwin.domain.port.in.ManageEcologicalCarryingCapacityAlertNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectotourismcarryingcapacitytwin")
public class EcologicalCarryingCapacityAlertNodeRestController {

    private final ManageEcologicalCarryingCapacityAlertNodeUseCase useCase;

    public EcologicalCarryingCapacityAlertNodeRestController(ManageEcologicalCarryingCapacityAlertNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<EcologicalCarryingCapacityAlertNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        EcologicalCarryingCapacityAlertNode created = useCase.createEcologicalCarryingCapacityAlertNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectotourismcarryingcapacitytwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EcologicalCarryingCapacityAlertNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findEcologicalCarryingCapacityAlertNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
