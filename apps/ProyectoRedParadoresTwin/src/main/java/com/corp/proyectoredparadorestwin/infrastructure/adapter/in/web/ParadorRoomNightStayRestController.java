package com.corp.proyectoredparadorestwin.infrastructure.adapter.in.web;

import com.corp.proyectoredparadorestwin.domain.model.ParadorRoomNightStay;
import com.corp.proyectoredparadorestwin.domain.port.in.ManageParadorRoomNightStayUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoredparadorestwin")
public class ParadorRoomNightStayRestController {

    private final ManageParadorRoomNightStayUseCase useCase;

    public ParadorRoomNightStayRestController(ManageParadorRoomNightStayUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ParadorRoomNightStay> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ParadorRoomNightStay created = useCase.createParadorRoomNightStay(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoredparadorestwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParadorRoomNightStay> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findParadorRoomNightStayById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
