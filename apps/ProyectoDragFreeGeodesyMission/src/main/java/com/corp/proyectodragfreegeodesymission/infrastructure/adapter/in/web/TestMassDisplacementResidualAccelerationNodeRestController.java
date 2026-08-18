package com.corp.proyectodragfreegeodesymission.infrastructure.adapter.in.web;

import com.corp.proyectodragfreegeodesymission.domain.model.TestMassDisplacementResidualAccelerationNode;
import com.corp.proyectodragfreegeodesymission.domain.port.in.ManageTestMassDisplacementResidualAccelerationNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectodragfreegeodesymission")
public class TestMassDisplacementResidualAccelerationNodeRestController {

    private final ManageTestMassDisplacementResidualAccelerationNodeUseCase useCase;

    public TestMassDisplacementResidualAccelerationNodeRestController(ManageTestMassDisplacementResidualAccelerationNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<TestMassDisplacementResidualAccelerationNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        TestMassDisplacementResidualAccelerationNode created = useCase.createTestMassDisplacementResidualAccelerationNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectodragfreegeodesymission/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestMassDisplacementResidualAccelerationNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findTestMassDisplacementResidualAccelerationNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
