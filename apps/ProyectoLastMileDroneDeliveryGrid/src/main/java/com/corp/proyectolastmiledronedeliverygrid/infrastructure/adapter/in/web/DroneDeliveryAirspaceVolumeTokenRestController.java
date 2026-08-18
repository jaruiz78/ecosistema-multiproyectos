package com.corp.proyectolastmiledronedeliverygrid.infrastructure.adapter.in.web;

import com.corp.proyectolastmiledronedeliverygrid.domain.model.DroneDeliveryAirspaceVolumeToken;
import com.corp.proyectolastmiledronedeliverygrid.domain.port.in.ManageDroneDeliveryAirspaceVolumeTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectolastmiledronedeliverygrid")
public class DroneDeliveryAirspaceVolumeTokenRestController {

    private final ManageDroneDeliveryAirspaceVolumeTokenUseCase useCase;

    public DroneDeliveryAirspaceVolumeTokenRestController(ManageDroneDeliveryAirspaceVolumeTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<DroneDeliveryAirspaceVolumeToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        DroneDeliveryAirspaceVolumeToken created = useCase.createDroneDeliveryAirspaceVolumeToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectolastmiledronedeliverygrid/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DroneDeliveryAirspaceVolumeToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDroneDeliveryAirspaceVolumeTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
