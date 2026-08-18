package com.corp.proyectotsunamiearlywarningsystem.infrastructure.adapter.in.web;

import com.corp.proyectotsunamiearlywarningsystem.domain.model.TsunamiWaveformPressureSensorNode;
import com.corp.proyectotsunamiearlywarningsystem.domain.port.in.ManageTsunamiWaveformPressureSensorNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectotsunamiearlywarningsystem")
public class TsunamiWaveformPressureSensorNodeRestController {

    private final ManageTsunamiWaveformPressureSensorNodeUseCase useCase;

    public TsunamiWaveformPressureSensorNodeRestController(ManageTsunamiWaveformPressureSensorNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<TsunamiWaveformPressureSensorNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        TsunamiWaveformPressureSensorNode created = useCase.createTsunamiWaveformPressureSensorNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectotsunamiearlywarningsystem/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TsunamiWaveformPressureSensorNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findTsunamiWaveformPressureSensorNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
