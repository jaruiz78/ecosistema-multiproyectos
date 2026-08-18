package com.corp.proyectomantlegeodynamicssimulator.infrastructure.adapter.in.web;

import com.corp.proyectomantlegeodynamicssimulator.domain.model.MantlePlumeThermalUpwellingNode;
import com.corp.proyectomantlegeodynamicssimulator.domain.port.in.ManageMantlePlumeThermalUpwellingNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomantlegeodynamicssimulator")
public class MantlePlumeThermalUpwellingNodeRestController {

    private final ManageMantlePlumeThermalUpwellingNodeUseCase useCase;

    public MantlePlumeThermalUpwellingNodeRestController(ManageMantlePlumeThermalUpwellingNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MantlePlumeThermalUpwellingNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MantlePlumeThermalUpwellingNode created = useCase.createMantlePlumeThermalUpwellingNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomantlegeodynamicssimulator/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MantlePlumeThermalUpwellingNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMantlePlumeThermalUpwellingNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
