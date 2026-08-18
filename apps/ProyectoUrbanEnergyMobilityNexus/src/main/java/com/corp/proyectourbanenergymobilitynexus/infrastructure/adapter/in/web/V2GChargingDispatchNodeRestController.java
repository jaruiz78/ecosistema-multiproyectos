package com.corp.proyectourbanenergymobilitynexus.infrastructure.adapter.in.web;

import com.corp.proyectourbanenergymobilitynexus.domain.model.V2GChargingDispatchNode;
import com.corp.proyectourbanenergymobilitynexus.domain.port.in.ManageV2GChargingDispatchNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectourbanenergymobilitynexus")
public class V2GChargingDispatchNodeRestController {

    private final ManageV2GChargingDispatchNodeUseCase useCase;

    public V2GChargingDispatchNodeRestController(ManageV2GChargingDispatchNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<V2GChargingDispatchNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        V2GChargingDispatchNode created = useCase.createV2GChargingDispatchNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectourbanenergymobilitynexus/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<V2GChargingDispatchNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findV2GChargingDispatchNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
