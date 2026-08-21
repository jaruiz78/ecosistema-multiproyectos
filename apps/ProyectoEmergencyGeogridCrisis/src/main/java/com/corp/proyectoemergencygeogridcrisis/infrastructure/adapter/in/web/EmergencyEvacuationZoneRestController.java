package com.corp.proyectoemergencygeogridcrisis.infrastructure.adapter.in.web;

import com.corp.proyectoemergencygeogridcrisis.domain.model.EmergencyEvacuationZone;
import com.corp.proyectoemergencygeogridcrisis.domain.port.in.ManageEmergencyEvacuationZoneUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoemergencygeogridcrisis")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class EmergencyEvacuationZoneRestController {

    private final ManageEmergencyEvacuationZoneUseCase useCase;

    public EmergencyEvacuationZoneRestController(ManageEmergencyEvacuationZoneUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<EmergencyEvacuationZone> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        EmergencyEvacuationZone created = useCase.createEmergencyEvacuationZone(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoemergencygeogridcrisis/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyEvacuationZone> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findEmergencyEvacuationZoneById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
