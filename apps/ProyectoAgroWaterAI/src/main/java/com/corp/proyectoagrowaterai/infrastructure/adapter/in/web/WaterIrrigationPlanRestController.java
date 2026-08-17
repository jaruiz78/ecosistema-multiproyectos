package com.corp.proyectoagrowaterai.infrastructure.adapter.in.web;

import com.corp.proyectoagrowaterai.domain.model.WaterIrrigationPlan;
import com.corp.proyectoagrowaterai.domain.port.in.ManageWaterIrrigationPlanUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoagrowaterai")
public class WaterIrrigationPlanRestController {

    private final ManageWaterIrrigationPlanUseCase useCase;

    public WaterIrrigationPlanRestController(ManageWaterIrrigationPlanUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<WaterIrrigationPlan> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        WaterIrrigationPlan created = useCase.createWaterIrrigationPlan(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoagrowaterai/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterIrrigationPlan> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findWaterIrrigationPlanById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
